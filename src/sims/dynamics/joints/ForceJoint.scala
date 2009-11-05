/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics.joints

/**Eine Verbindung die Kraft auf ihre Bindungskoerper ausueben kann.*/
trait ForceJoint {
  
  /**Uebt eine Kraft auf die Bindungskoerper aus.*/
  def applyForce(): Unit
}
