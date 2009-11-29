/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics.joints

import sims.geometry._
import sims.dynamics._

/**Joints constrain the movement of two bodies.
 * Their implementation was inspired by Erin Catto's box2d.*/
abstract class Joint extends Constraint{
  
  /**First body of the joint.*/
  val node1: Body
  
  /**Second body of the joint.*/
  val node2: Body
  
  /**Corrects the velocities of this joint's associated bodies.*/
  def correctVelocity(h: Double): Unit
  
  /**Corrects the positions of this joint's associated bodies.*/
  def correctPosition(h: Double): Unit
}