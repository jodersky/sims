/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics.joints

import sims.geometry._

/**Eine Hooksche Feder.
 * @param node1 erster Koerper der Verbindung
 * @param anchor1 Bindungspunkt auf Koerper eins
 * @param node2 zweiter Koerper der Verbindung
 * @param anchor2 Bindungspunkt auf Koerper zwei
 * @param springConstant Federkonstante
 * @param initialLength Initiallaenge
 */
case class SpringJoint(node1: Body, anchor1: Vector2D, node2: Body, anchor2: Vector2D, springConstant: Double, initialLength: Double) extends Joint with ForceJoint{
  
  def this(node1: Body, anchor1: Vector2D, node2: Body, anchor2: Vector2D, springConstant: Double) = {
    this(node1: Body, anchor1, node2: Body, anchor2, springConstant: Double, (anchor2 - anchor1).length)
  }
  
  def this(node1: Body, node2: Body, springConstant: Double, initialLength: Double) = {
    this(node1: Body, node1.pos, node2: Body, node2.pos, springConstant: Double, initialLength: Double)
  }
  def this(node1: Body, node2: Body, springConstant: Double) = {
    this(node1: Body, node1.pos, node2: Body, node2.pos, springConstant: Double, (node2.pos - node1.pos).length)
  }
  
  private val a1 = anchor1 - node1.pos
  private val a2 = anchor2 - node2.pos
  private val initRotation1 = node1.rotation
  private val initRotation2 = node2.rotation
  
  /**Ergibt den Bindungspunkt auf Koerper eins.*/
  def connection1 = (a1 rotate (node1.rotation - initRotation1)) + node1.pos
  
  /**Ergibt den Bindungspunkt auf Koerper zwei.*/
  def connection2 = (a2 rotate (node2.rotation - initRotation2)) + node2.pos
  
  /**Daempfung.*/
  var damping = 0.0
  
  /**Relative Position der Bindungspunkte.*/
  def x = connection2 - connection1
  
  /**Relative Geschwindigkeit der Bindungspunkte.*/
  def v = node2.velocityOfPoint(connection2) - node1.velocityOfPoint(connection1)
  
  /**Ergibt die Federkraft nach dem Hookschen Gesetz.*/
  def force = (x.length - initialLength) * springConstant
  
  /**Uebt die Federkraft auf die Bindungspunkte aus.*/
  def applyForce() = {
    node1.applyForce(x.unit * force - (v * damping) project x, connection1)
    node2.applyForce(-x.unit * force - (v * damping) project x, connection2)
    //println("this should not happen")
  }
  
  def correctVelocity(h: Double) = {
    /*
    val x = this.x	//relativer Abstand
    val v = this.v	//relative Geschwindigkeit
    val r1 = (connection1 - node1.pos)	//Abstand Punkt-Schwerpunkt, Koerper 1
    val r2 = (connection2 - node2.pos)	//Abstand Punkt-Schwerpunkt, Koerper 2
    val cr1 = r1 cross x.unit	//Kreuzprodukt
    val cr2 = r2 cross x.unit	//Kreuzprodukt
    val Cdot = x.unit dot v	//Velocity-Constraint
    val invMass = 1/node1.mass + 1/node1.I * cr1 * cr1 + 1/node2.mass + 1/node2.I * cr2 * cr2	//=J M^-1 JT
    val m = if (invMass == 0.0) 0.0 else 1/invMass	//Test um Nulldivision zu vermeiden
    val lambda = Math.min(Math.max(-this.force * h, (-m * Cdot)), this.force * h)
    println (force * h, -m * Cdot)
    val impulse = x.unit * lambda
    node1.applyImpulse(-impulse, connection1)
    node2.applyImpulse(impulse, connection2)
    */
  }

  def correctPosition(h: Double) = {
    
  }
}