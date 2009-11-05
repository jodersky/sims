/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics.joints.test

import sims.dynamics._
import sims.geometry._

class UnitCircleJoint(body: Body, anchor: Vector2D) extends Joint{
  
  val node1 = body
  val node2 = body
  
  private val a = anchor - body.pos
  private val initRotation = body.rotation
  def connection = (a rotate (body.rotation - initRotation)) + body.pos
  def x = connection
  def v = body.velocityOfPoint(connection)

  /*
   * C = ||x|| - 1
   * Cdot = x/||x|| dot v = u dot v
   * J = [u	(r cross u)]
   */
  def correctVelocity(h: Double) = {
    val r = connection - body.pos
    val u = x.unit
    val cr = r cross u
    val mc = 1.0/(1/body.mass + 1/body.I * cr * cr)
    val lambda = -mc * (u dot v)
    val Pc = u * lambda
    
    val vupdate = u * lambda / body.mass
    val wupdate = (r cross u) * lambda / body.I
    
    println("dv = " + vupdate + "	dw = " + wupdate)
    body.linearVelocity = body.linearVelocity +  u * lambda / body.mass
    body.angularVelocity = body.angularVelocity + (r cross u) * lambda / body.I 
  }
  
  def correctPosition(h: Double) = {}
}
