/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics.joints

/**A joint which can apply a force to its anchor bodies, thus adding or removing energy to the system.*/
trait ForceJoint {
  
  /**Applies a force on the anchor bodies.*/
  def applyForce(): Unit
}
