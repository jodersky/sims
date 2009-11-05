/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics

import sims.geometry._
import sims.collision._
import sims.dynamics.joints._
import scala.collection.mutable._

/**Eine Welt enthaelt und Simuliert ein System aus Koerpern und Verbindungen.*/
class World {
  
  /**Zeitschritt in dem diese Welt die Simulation vorranschreiten laesst.*/
  var timeStep: Double = 1.0 / 60
  
  /**Anzahl der Constraint-Korrekturen pro Zeitschritt.*/
  var iterations: Int = 10
  
  /**Schwerkraft die in dieser Welt herrscht.*/
  var gravity = Vector2D(0, -9.81)
  
  /**Alle Koerper die diese Welt simuliert.*/
  val bodies = new ArrayBuffer[Body]
  
  /**Alle Verbindungen die diese Welt simuliert.*/
  val joints = new ArrayBuffer[Joint]
  
  /**Ueberwachungsfunktionen fuer Koerper.
   * <p>
   * Das erste Element des Tuples ist die Ueberschrift und das zweite Element, der Wert.*/
  val monitors = new ArrayBuffer[(String, Body => String)]
  
  /**Kollisionsdetektor dieser Welt.*/
  val detector: Detector = new GridDetector(this)
  
  /**Warnung wenn Koerper schneller als Lichtgeschwindigkeit.*/
  var overCWarning = false
  
  /**Kollisionerkennung.*/
  var enableCollisionDetection = true
  
  /**Positionskorrekturen.*/
  var enablePositionCorrection = true
  
  /**Die minimale, nicht als null geltende Geschwindigkeit.*/
  var minLinearVelocity: Double = 0.0001
  
  /**Die minimale, nicht als null geltende Winkelgeschwindigkeit.*/
  var minAngularVelocity: Double = 0.0001
  
  /**Ergibt alle Formen aus allen Koerpern in dieser Welt.*/
  def shapes = for (b <- bodies; s <- b.shapes) yield s
  
  /**Fuegt dieser Welt einen Koerper hinzu.*/
  def +=(body: Body) = bodies += body
  
  /**Fuegt dieser Welt eine Verbindung hinzu.*/
  def +=(joint: Joint): Unit = joints += joint
  
  /**Fuegt dieser Welt ein vorangefertigtes System vaus Koerpern und Verbindungen hinzu.*/
  def +=(p: prefabs.Prefab): Unit = {
    for (b <- p.bodies) this += b
    for (j <- p.joints) this += j
  }
  
  def ++=(bs: Seq[Body]) = for(b <- bs) this += b
  
  /**Entfernt den gegebenen Koerper aus dieser Welt.*/
  def -=(body: Body): Unit = bodies -= body
  
  /**Entfernt die gegebene Verbindung aus dieser Welt.*/
  def -=(joint: Joint): Unit = joints -= joint
  
  /**Entfernt das gegebene System aus Koerpern und Verbindungen aus dieser Welt.*/
  def -=(p: prefabs.Prefab): Unit = {
    for (b <- p.bodies) this -= b
    for (j <- p.joints) this -= j
  }
  
  def --=(bs: Seq[Body]) = for(b <- bs) this -= b
  
  /**Entfernt alle Koerper, Verbindungen und Ueberwachungsfunktionen dieser Welt.*/
  def clear() = {joints.clear(); bodies.clear(); monitors.clear()}
  
  /**Aktuelle Zeit in Sekunden dieser Welt. Nach jedem Zeitschritt wird die Zeit erhoeht.*/
  var time: Double = 0.0
  
  /**Simuliert einen von <code>timeStep</code> angegebenen Zeitschritt.
   * Ihre Aufgabe ist es die Koerper dieser Welt so zu simulieren wie diese sich in einer Welt mit den gegebenen
   * Bedingungen verhalten wuerden.
   * <p>
   * Der Zeitschritt wird in folgenden Phasen ausgefuehrt:
   * <ol>
   * <li>Kraefte wirken auf die Koerper (z.B Schwerkraft, andere Kraftfaehige Objekte).</li>
   * <li>Beschleunigungen werden integriert.</li>
   * <li>Geschwindigkeiten werden korrigiert.</li>
   * <li>Geschwindigkeiten werden integriert.</li>
   * <li>Positionen werden korrigiert.</li>
   * <li>Die Methode <code>postStep()</code> wird ausgefuehrt.</li>
   * </ol>*/
  def step() = {
    time += timeStep
    
    //Kraftobjekte
    for (j <- joints) j match {case f: ForceJoint => f.applyForce; case _ => ()}
    
    //integriert v
    for (b <- bodies) {
      val m = b.mass
      b.applyForce(gravity * b.mass)
      val a = b.force / b.mass
      val alpha = b.torque / b.I
      b.linearVelocity = b.linearVelocity + a * timeStep
      b.angularVelocity = b.angularVelocity + alpha * timeStep
    }
    
    //korrigiert v
    for (i <- 0 until iterations){
      for(c <- joints) c.correctVelocity(timeStep)
      if (enableCollisionDetection) for (c <- detector.collisions) c.correctVelocity(timeStep)
    }
     
    //integriert pos
    for (b <- bodies) {
      //warning when body gets faster than speed of light
      if (b.linearVelocity.length >= 300000000) overCWarning = true
      if (b.linearVelocity.length < minLinearVelocity) b.linearVelocity = Vector2D.Null
      if (b.angularVelocity.abs < minAngularVelocity) b.angularVelocity = 0.0
      b.pos = b.pos + b.linearVelocity * timeStep
      b.rotation = b.rotation + b.angularVelocity * timeStep
      b.force = Vector2D.Null
      b.torque = 0.0   
    }
    
    //korrigiert pos
    if (enablePositionCorrection) for (i <- 0 until iterations){
      for (c <- joints) c.correctPosition(timeStep)
      if (enableCollisionDetection) for (c <- detector.collisions) c.correctPosition(timeStep)
    }
    
    postStep()
  }
  
  /**Wird nach jedem Zeitschritt ausgefuehrt.*/
  def postStep() = {}
  
  /**Ergibt Informationen ueber diese Welt.*/
  def info = {
    "Bodies = " + bodies.length + "\n" +
    "Shapes = " + shapes.length + "\n" +
    "Joints = " + joints.length + "\n" +
    "Collisions = " + detector.collisions.length + "\n" +
    "Monitors = " +  monitors.length + "\n" +
    "Gravity = " + gravity + "m/s^2\n" +
    "Timestep = " + timeStep + "s\n" +
    "Time = " + time + "s\n" +
    "Iterations = " + iterations
  }
}
