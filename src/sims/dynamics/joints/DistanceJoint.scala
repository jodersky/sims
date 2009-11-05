/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics.joints

import sims.geometry._

/** DistanceJoints halten die Bindungspunkte auf ihren Bindungskoerpern bei einem konstanten Abstand.
 * @param node1 erster Koerper der Verbindung
 * @param anchor1 Bindungspunkt auf Koerper eins
 * @param node2 zweiter Koerper der Verbindung
 * @param anchor2 Bindungspunkt auf Koerper zwei*/
case class DistanceJoint(node1: Body, anchor1: Vector2D, node2: Body, anchor2: Vector2D) extends  Joint{
  def this(node1: Body, node2: Body) = this(node1, node1.pos, node2, node2.pos)
  
  /**Abstand der beiden Bindungspunkte bei initialisierung (der gewollte Abstand).*/
  val distance = (anchor2 - anchor1).length
  
  private val a1 = anchor1 - node1.pos
  private val a2 = anchor2 - node2.pos
  private val initRotation1 = node1.rotation
  private val initRotation2 = node2.rotation
  
  /**Ergibt den Bindungspunkt auf Koerper eins.*/
  def connection1 = (a1 rotate (node1.rotation - initRotation1)) + node1.pos
  
  /**Ergibt den Bindungspunkt auf Koerper zwei.*/
  def connection2 = (a2 rotate (node2.rotation - initRotation2)) + node2.pos
  
  /**Relative Position der Bindungspunkte.*/
  def x = connection2 - connection1
  
  /**Relative Geschwindigkeit der Bindungspunkte.*/
  def v = node2.velocityOfPoint(connection2) - node1.velocityOfPoint(connection1)
  
  /* x = connection2 - connection1
     * C = ||x|| - L
     * u = x / ||x||
     * v = v2 + w2 cross r2 - v1 - w1 cross r1
     * Cdot = u dot v
     * J = [-u	-(r1 cross u)	u	(r2 cross u)]
     * 1/m = J * M^-1 * JT
     * = 1/m1 * u * u + 1/m2 * u * u + 1/I1 * (r1 cross u)^2 + 1/I2 * (r2 cross u)^2*/
  override def correctVelocity(h: Double) = {
    val x = this.x	//relativer Abstand
    val v = this.v	//relative Geschwindigkeit
    val r1 = (connection1 - node1.pos)	//Abstand Punkt-Schwerpunkt, Koerper 1
    val r2 = (connection2 - node2.pos)	//Abstand Punkt-Schwerpunkt, Koerper 2
    val cr1 = r1 cross x.unit	//Kreuzprodukt
    val cr2 = r2 cross x.unit	//Kreuzprodukt
    val Cdot = x.unit dot v	//Velocity-Constraint
    val invMass = 1/node1.mass + 1/node1.I * cr1 * cr1 + 1/node2.mass + 1/node2.I * cr2 * cr2	//=J M^-1 JT
    val m = if (invMass == 0.0) 0.0 else 1/invMass	//Test um Nulldivision zu vermeiden
    val lambda = -m * Cdot	//=-JV/JM^-1JT
    val impulse = x.unit * lambda	//P=J lambda
    node1.applyImpulse(-impulse, connection1)
    node2.applyImpulse(impulse, connection2) 
  }
  
  override def correctPosition(h: Double) = {
    val C = x.length - distance
    val cr1 = (connection1 - node1.pos) cross x.unit
    val cr2 = (connection2 - node2.pos) cross x.unit
    val invMass = 1/node1.mass + 1/node1.I * cr1 * cr1 + 1/node2.mass + 1/node2.I * cr2 * cr2
    val m = if (invMass == 0.0) 0.0 else 1/invMass
    val impulse = -x.unit * m * C
    node1.pos -= impulse / node1.mass
    node2.pos += impulse / node2.mass
    node1.rotation -= ((connection1 - node1.pos) cross impulse) / node1.I
    node2.rotation += ((connection2 - node2.pos) cross impulse) / node2.I
  }
  
}