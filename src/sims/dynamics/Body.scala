/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics

import sims.geometry._
import sims.dynamics.joints._

/**Ein 2-Dimensionaler Koerper besteht aus mehreren Formen. Im gegensatz zu letzteren, enthaelt ein Koerper dynamische Informationen (v, F, etc...).
 * @param shps zu dem Koerper gehoerende Formen.*/
class Body(shps: Shape*){
  
  /**Einzigartige Identifikationsnummer dieses Koerpers.*/
  val uid = Body.nextUid
  
  /**Formen aus denen dieser Koerper besteht.*/
  val shapes: List[Shape] = shps.toList
  
  //Formen werden bei Initialisierung eingefuegt
  for (s <- shapes) {
    s.body = this
    s.refLocalPos = s.pos - pos
    s.rotation0 = s.rotation
  }
  
  private var isFixed: Boolean = false
  
  /**Gibt an ob dieser Koerper fixiert ist.*/
  def fixed = isFixed
  
  /**Fixiert oder unfixiert diesen Koerper.*/
  def fixed_=(value: Boolean) = {
    if (value) {linearVelocity = Vector2D.Null; angularVelocity = 0.0}
    isFixed = value
  } 
  
  /**Gibt an ob die Eigenschaften dieses Koerpers ueberwacht werden sollen.
   * @see World#monitors*/
  var monitor: Boolean = false
  
  /**Ermittelt die Position dieses Koerpers. Die Position entspricht dem Schwerpunkt.
   * @return Position dieses Koerpers*/
  def pos: Vector2D = // Shwerpunkt = sum(pos*mass)/M
    (Vector2D.Null /: shapes)((v: Vector2D, s: Shape) => v + s.pos * s.mass) /
    (0.0 /: shapes)((i: Double, s: Shape) => i + s.mass)
  
  /**Setzt die Position dieses Koerpers und verschiebt dadurch die Positionen seiner Formen.
   * @param newPos neue Position*/
  def pos_=(newPos: Vector2D) = {
    val stepPos = pos
    shapes.foreach((s: Shape) => s.pos = s.pos - stepPos + newPos)
  }
  
  /**Enthaelt die aktuelle Rotation dieses Koerpers.*/
  private var _rotation: Double = 0.0 //shapes(0).rotation
  
  /**Ergibt die aktuelle Rotation dieses Koerpers.
   * @return aktuelle Rotation dieses Koerpers*/
  def rotation: Double = _rotation
  
  /**Setzt die Rotation dieses Koerpers. Dazu werden auch die Positionen und Rotationen seiner Formen entsprechend veraendert.
   * @param r neue Rotation*/
  def rotation_=(newRotation: Double) = {
    _rotation = newRotation
    val stepPos = pos
    for (s <- shapes) {
      s.rotation = newRotation + s.rotation0
      s.pos = stepPos + (s.refLocalPos rotate (newRotation))
    }  
  }
  
  /**Lineargeschwindigkeit dieses Koerpers.*/
  var linearVelocity: Vector2D = Vector2D.Null
  
  /**Winkelgeschwindigkeit dieses Koerpers.*/
  var angularVelocity: Double = 0
  
  /**Lineargeschwindigkeit des gegebenen Punktes auf diesem Koerper. In Weltkoordinaten.*/
  def velocityOfPoint(point: Vector2D) = linearVelocity + ((point - pos).leftNormal * angularVelocity)
  
  /**Resultierende Kraft auf den Schwerpunkt dieses Koerpers.*/
  var force: Vector2D = Vector2D.Null
  
  /**Resultierender Drehmoment zu dem Schwerpunkt dieses Koerpers.*/
  var torque: Double = 0
  
  /**Ergibt die Masse dieses Koerpers. Die Masse ist gleich die Summe aller Massen seiner Formen.
   * @return Masse des Koerpers*/
  def mass: Double = if (fixed) Double.PositiveInfinity else (0.0 /: shapes)((i: Double, s: Shape) => i + s.mass)
  
  /**Ergibt den Traegheitsmoment zu dem Schwerpunkt dieses Koerpers. Der Traegheitsmoment wird mit Hilfe des Steinerschen Satzes errechnet.
   * @return Traegheitsmoment relativ zu dem Schwerpunkt dieses Koerpers*/
  def I: Double = if (fixed) Double.PositiveInfinity else
    (0.0 /: (for (s <- shapes) yield (s.I + s.mass * ((s.pos - pos) dot (s.pos - pos)))))(_+_)
  
  /**Wendet eine Kraft auf den Schwerpunkt dieses Koerpers an.
   * @param force anzuwendender Kraftvektor*/
  def applyForce(force: Vector2D) = if (!fixed) this.force += force
  
  /**Wendet eine Kraft auf einen Punkt dieses Koerpers an. Achtung: der gegebene Punkt wird nicht auf angehoerigkeit dieses
   * Koerpers ueberprueft.
   * @param force anzuwendender Kraftvektor
   * @param point Ortsvektor des Punktes auf den die Kraft wirken soll (gegeben in Weltkoordinaten).*/
  def applyForce(force: Vector2D, point: Vector2D) = if (!fixed) {this.force += force; torque += (point - pos) cross force}
  
  /**Wendet einen Impuls auf den Schwerpunkt dieses Koerpers an.
   * @param impulse anzuwendender Impulsvektor*/  
  def applyImpulse(impulse: Vector2D) = if (!fixed) linearVelocity += impulse / mass
  
  /**Wendet einen Impuls auf einen Punkt dieses Koerpers an. Achtung: der gegebene Punkt wird nicht auf angehoerigkeit dieses
   * Koerpers ueberprueft.
   * @param impulse anzuwendender Impulsvektor
   * @param point Ortsvektor des Punktes auf den der Impuls wirken soll (gegeben in Weltkoordinaten).*/
  def applyImpulse(impulse: Vector2D, point: Vector2D) = if (!fixed) {linearVelocity += impulse / mass; angularVelocity += ((point - pos) cross impulse) / I}
  
  /**Ueberprueft ob der gegebene Punkt <code>point</code> sich in diesem Koerper befindet.*/
  def contains(point: Vector2D) = shapes.exists(_.contains(point))
  
  override def toString: String = {
    "Body" + uid + "	" + shapes + "	fixed=" + fixed + "	m=" + mass + "	I=" + I + 	"	pos=" + pos + "	rot=" + rotation + "	v=" + linearVelocity + "	w=" + angularVelocity + "	F=" + force + "	tau=" + torque
  }
  
  /**Erstellt einen neuen Koerper der zusaetzlich die Form <code>s</code> enthaelt.
   * @param s zusaetzliche Form
   * @return neuer Koerper*/
  def ^(s: Shape) = new Body((s :: shapes): _*)
  
  /**Erstellt einen neuen Koerper der zusaetzlich die Formen von dem Koerper <code>b</code> enthaelt.
   * @param b Koerper mit zusaetzlichen Formen
   * @return neuer Koerper*/
  def ^(b: Body) = {
    val shapes = this.shapes ::: b.shapes
    new Body(shapes: _*)
  }
}

object Body {
  private var uidCounter = -1
  private def nextUid = {uidCounter += 1; uidCounter}
}