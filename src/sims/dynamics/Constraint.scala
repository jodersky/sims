/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics

/**All constraints in SiMS implement this trait.
 * Position and velocity can be corrected for each constraint.
 * The implementation of constraints was inspired by Erin Catto's box2d.*/
trait Constraint {
  
  /**Corrects the velocities of bodies according to this constraint.
   * @param h a time interval, used for converting forces and impulses*/
  def correctVelocity(h: Double): Unit
  
  /**Corrects the positions of bodies according to this constraint.
   * @param h a time interval, used for converting forces and impulses*/
  def correctPosition(h: Double): Unit
}
