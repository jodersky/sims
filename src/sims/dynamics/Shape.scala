/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics

import sims.geometry._
import sims.collision._

/**
* Eine abstrakte Form.
*/
abstract class Shape{
  
  /**Einzigartige Identifikationsnummer.*/
  val uid: Int = Shape.nextUid
  
  /**Kollisionsfaehigkeit.*/
  var collidable: Boolean = true
  
  /**Teil der Stosszahl bei einer Kollision zwischen dieser Form und einer anderen.
   * Die Stosszahl wird aus dem Produkt der beiden Teile der Formen errechnet.*/
  var restitution: Double = 0.7
  
  /**Teil des Reibungskoeffizienten bei einer Kollision zwischen dieser Form und einer anderen.
   * Der Reibungskoeffizient wird aus dem Produkt der beiden Teile der Formen errechnet.*/
  var friction: Double = 0.707
  
  /**Position des Schwerpunktes in Welt.*/
  var pos: Vector2D = Vector2D.Null
  
  /**Rotation. Entspricht Laenge des Rotationsvektors.*/
  var rotation: Double = 0
  
  /**Initiale Rotation. (Rotation ohne Koerper)*/
  var rotation0 = 0.0
  
  /**Referenzposition in Koerper. Wird zur Rotation von Formen in Koerpern verwendet.*/
  var refLocalPos: Vector2D = Vector2D.Null
  
  /**Dichte. (Masse pro Flaeche)*/
  val density: Double
  
  /**Volumen. Entspricht eigentlich der Flaeche dieser Form (in 2D) wird aber zum Errechnen der Masse verwendet.*/
  val volume: Double
  
  /**Errechnet die Masse dieser Form. Masse ist gleich Volumen mal Dichte.
   @return Masse der Form*/
  def mass = volume * density
  
  /**Errechnet Traegheitsmoment zum Schwerpunkt dieser Form.
   @return Traegheitsmoment zum Schwerpunkt*/
  val I: Double
  
  /**Beinhaltender Koerper.	Sollte nicht selbst bei Initialisierung definiert werden.*/
  var body: Body = _
  
  /**Gibt das umfassende AABB dieser Form zurueck.
   @return umfassendes AABB*/
  def AABB: AABB
  
  /**Ergibt die Projektion dieser Form auf eine Gerade gegeben durch den
   * Richtungsvektor <code>axis</code>.
   * @param axis Richtungsvektor der Geraden
   * @return Projektion dieser Form*/
  def project(axis: Vector2D): Projection
  
  /**Ermittelt ob der gebene Punkt <code>point</code> in dieser Form enthalten ist.*/
  def contains(point: Vector2D): Boolean
  
  /**Baut einen Koerper aus dieser Form.
   @return ein Koerper bestehend aus dieser Form. */
  def asBody = new Body(this)
  
  /**Formen mit denen diese Form nicht Kollidiert.*/
  val transientShapes: collection.mutable.Set[Shape] = collection.mutable.Set()
  
  /**Erstellt einen Koerper aus dieser Form und der Form <code>s</code>.*/
  def ^(s: Shape) = new Body(this, s)
  
  /**Erstellt einen Koerper aus dieser Form und den Formen des Koerpers <code>b</code>.*/
  def ^(b: Body) = {
    val shapes = this :: b.shapes
    new Body(shapes: _*)
  }
}

object Shape {
  private var uidCounter = -1
  private def nextUid = {uidCounter += 1; uidCounter}
}
