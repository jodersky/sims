/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics.joints

import sims.geometry._
import sims.math._
import sims.dynamics._
import math._

/**A revolute joint that connects two bodies at a singe point. Inspired from JBox2D.
 * <b>Warning:</b> there are still several bugs with revolute joints, if they are between two free
 * bodies and not connected at their respective COMs.*/
case class RevoluteJoint(node1: Body, node2: Body, anchor: Vector2D) extends Joint{
  private val a1 = anchor - node1.pos
  private val a2 = anchor - node2.pos
  private val initRotation1 = node1.rotation
  private val initRotation2 = node2.rotation
  def connection1 = (a1 rotate (node1.rotation - initRotation1)) + node1.pos
  def connection2 = (a2 rotate (node2.rotation - initRotation2)) + node2.pos
  
  def x = connection2 - connection1
  def v = node2.velocityOfPoint(connection2) - node1.velocityOfPoint(connection1)
  
  /* x = connection2 - connection1
   * C = x
   * Cdot = v = v2 - v1 = v2 + (w2 cross r2) - v1 - (w1 cross r1)
   * J = [-I -r1_skew I r2_skew ] ?????
   */
  def correctVelocity(h: Double) = {
    val m1 = node1.mass
    val m2 = node2.mass
    val I1 = node1.I
    val I2 = node2.I
    val r1 = connection1 - node1.pos
    val r2 = connection2 - node2.pos
   
    val K1 = new Matrix22(1/m1 + 1/m2,	0,
                          0,			1/m1 + 1/m2)
    val K2 = new Matrix22(1/I1 * r1.x * r1.x,	-1/I1 * r1.x * r1.y,
                          -1/I1 * r1.x * r1.y,	1/I1 * r1.x * r1.x)
    val K3 = new Matrix22(1/I2 * r2.x * r2.x,	-1/I2 * r2.x * r2.y,
                          -1/I2 * r2.x * r2.y,	1/I2 * r2.x * r2.x)
    val pivotMass = (K1 + K2 + K3).invert
    val cdot = v
    val p = pivotMass * cdot
    node1.applyImpulse(p, connection1)
    node2.applyImpulse(-p, connection2)
  }
  
  def correctPosition(h: Double) = {
    
  }
}
