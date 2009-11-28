package sims.dynamics.joints.test

import sims.geometry._

case class PrismaticJoint(node1: Body, anchor1: Vector2D, node2: Body, anchor2: Vector2D) extends Joint{
  def this(node1: Body, node2: Body) = this(node1, node1.pos, node2, node2.pos)
  
  val angle = node2.rotation - node1.rotation
  
  private val a1 = anchor1 - node1.pos
  private val a2 = anchor2 - node2.pos
  private val initRotation1 = node1.rotation
  private val initRotation2 = node2.rotation
  
  def connection1 = (a1 rotate (node1.rotation - initRotation1)) + node1.pos
  def connection2 = (a2 rotate (node2.rotation - initRotation2)) + node2.pos
  
  /**Relative Position der Bindungspunkte.*/
  def x = connection2 - connection1
  
  /**Relative Geschwindigkeit der Bindungspunkte.*/
  def v = node2.velocityOfPoint(connection2) - node1.velocityOfPoint(connection1)
  
  
  def correctVelocity(h: Double) = {
    correctLinear(h)
    //correctAngular(h)
  }
  
  def correctLinear(h: Double) = {
    val x = this.x.unit	//relativer Abstand
    val n0 = x.leftNormal
    val v = this.v	//relative Geschwindigkeit
    val r1 = (connection1 - node1.pos)	//Abstand Punkt-Schwerpunkt, Koerper 1
    val r2 = (connection2 - node2.pos)	//Abstand Punkt-Schwerpunkt, Koerper 2
    val cr1 = r1 cross n0	//Kreuzprodukt
    val cr2 = r2 cross n0	//Kreuzprodukt
    val Cdot = n0 dot v
    val invMass = 1/node1.mass + 1/node1.I * cr1 * cr1 + 1/node2.mass + 1/node2.I * cr2 * cr2
    val m = if (invMass == 0.0) 0.0 else 1/invMass
    val impulse = -n0 * m * Cdot
    node1.applyImpulse(-impulse, connection1)
    node2.applyImpulse(impulse, connection2)
  }
  
  //J=[-1,1]
  
  def correctAngular(h: Double) = {
    val w = node2.angularVelocity - node1.angularVelocity
    val Cdot = w
    val invMass = node1.I + node2.I
    val m = 1 / invMass
    val lambda = m * Cdot
    node1.angularVelocity += lambda / node1.I
    node2.angularVelocity += -lambda / node2.I
  }
  
  def correctPosition(h: Double) = {
    /*
    val x = this.x.unit	//relativer Abstand
    val n0 = x.leftNormal
    val v = this.v	//relative Geschwindigkeit
    val r1 = (connection1 - node1.pos)	//Abstand Punkt-Schwerpunkt, Koerper 1
    val r2 = (connection2 - node2.pos)	//Abstand Punkt-Schwerpunkt, Koerper 2
    val cr1 = r1 cross n0	//Kreuzprodukt
    val cr2 = r2 cross n0	//Kreuzprodukt
    val C = n0 dot x
    val invMass = 1/node1.mass + 1/node1.I * cr1 * cr1 + 1/node2.mass + 1/node2.I * cr2 * cr2
    val m = if (invMass == 0.0) 0.0 else 1/invMass
    val impulse = -n0 * m * C
    node1.pos += -impulse
    node1.rotation += -impulse cross r1
    node2.pos += impulse
    node2.rotation += impulse cross r2
    
    val relOmega = node2.angularVelocity - node2.angularVelocity
    val invMassOmega = node1.I + node2.I
    val mOmega = if (invMassOmega == 0.0) 0.0 else 1/invMassOmega
    val Crot = node2.rotation - node2.rotation + angle 
    node1.rotation += mOmega * Crot
    node2.rotation += -mOmega * Crot
      */
  }
}
