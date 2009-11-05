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
  
  /**Ergibt die Federkraft nach dem Hookschen Gesetz.*/
  def force = (x.length - initialLength) * springConstant
  
  /**Uebt die Federkraft auf die Bindungspunkte aus.*/
  def applyForce() = {
    node1.applyForce(x.unit * force - ((node1 velocityOfPoint connection1) * damping) project x, connection1)
    node2.applyForce(-x.unit * force - ((node2 velocityOfPoint connection2) * damping) project x, connection2)
  }
  
  def correctPosition(h: Double) = ()
  def correctVelocity(h: Double) = ()
}