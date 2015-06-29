/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics

import sims.geometry._
import sims.collision._
import sims.dynamics.joints._
import scala.collection.mutable.ArrayBuffer

/**A world contains and simulates a system of rigid bodies and joints.*/
class World {
  
  /**Time intervals in which this world simulates.*/
  var timeStep: Double = 1.0 / 60
  
  /**Number of constraint corrections per time step.*/
  var iterations: Int = 10
  
  /**Gravity in this world.*/
  var gravity = Vector2D(0, -9.81)
  
  /**Bodies contained in this world.*/
  val bodies = new ArrayBuffer[Body]
  
  /**Joints contained in this world.*/
  val joints = new ArrayBuffer[Joint]
  
  /**Monitoring methods for bodies.
   * <p>
   * The first element of the tuple is the method's title and the second the method.
   * Example usage: monitors += ("Y-Position", _.pos.y.toString)
   * This will calculate all bodies - whose <code>monitor</code> field is set to
   * <code>true</code> - second position components.*/
  val monitors = new ArrayBuffer[(String, Body => Any)]
  
  /**Collsion detector who manages collision detection in this world.*/
  val detector: Detector = new GridDetector(this)
  
  /**Warning if a body's velocity exceeds the speed of light.*/
  var overCWarning = false
  
  /**Flag to enable collision detection.*/
  var enableCollisionDetection = true
  
  /**Flag to enable position correction for constraints.*/
  var enablePositionCorrection = true
  
  /**Minimal, non-zero linear velocity.*/
  var minLinearVelocity: Double = 0.0001
  
  /**Minimal, non-zero angular velocity.*/
  var minAngularVelocity: Double = 0.0001
  
  /**Returns all shapes of all bodies in this world.*/
  def shapes = for (b <- bodies; s <- b.shapes) yield s
  
  /**Adds the given body to this world.*/
  def +=(body: Body) = bodies += body
  
  /**Adds the given joint to this world.*/
  def +=(joint: Joint): Unit = joints += joint
  
  /**Adds the given prefabricated system of bodies and joints to this world.*/
  def +=(p: sims.prefabs.Prefab): Unit = {
    for (b <- p.bodies) this += b
    for (j <- p.joints) this += j
  }
  
  /**Adds the given sequence of bodies to this world.*/
  def ++=(bs: Seq[Body]): Unit = for(b <- bs) this += b
  
  /**Removes the given body from this world.*/
  def -=(body: Body): Unit = bodies -= body
  
  /**Removes the given joint from this world.*/
  def -=(joint: Joint): Unit = joints -= joint
  
  /**Removes the given prefabricated system of bodies and joints from this world.*/
  def -=(p: sims.prefabs.Prefab): Unit = {
    for (b <- p.bodies) this -= b
    for (j <- p.joints) this -= j
  }
  
  /**Removes the given sequence of bodies from this world.*/
  def --=(bs: Seq[Body]) = for(b <- bs) this -= b
  
  /**Removes all bodies, joints and monitoring methods from this world.*/
  def clear() = {joints.clear(); bodies.clear(); monitors.clear()}
  
  /**Current time in this world.*/
  var time: Double = 0.0
  
  /**Simulates a time step of the duration <code>timeStep</code>.
   * <p>
   * The time step is simulated in the following phases:
   * <ol>
   * <li>Forces are applied to bodies.</li>
   * <li>Accelerations are integrated.</li>
   * <li>Velocities are corrected.</li>
   * <li>Velocities are integrated.</li>
   * <li>Postions are corrected.</li>
   * <li>The method <code>postStep()</code> is executed.</li>
   * </ol>*/
  def step() = {
    time += timeStep
    
    //force applying objects
    for (j <- joints) j match {case f: ForceJoint => f.applyForce; case _ => ()}
    
    //integration of acclerations, yields velocities
    for (b <- bodies) {
      b.applyForce(gravity * b.mass)
      b.linearVelocity = b.linearVelocity + (b.force / b.mass) * timeStep
      b.angularVelocity = b.angularVelocity + (b.torque / b.I) * timeStep
    }
    
    //correction of velocities
    for (i <- 0 until iterations){
      for(c <- joints) c.correctVelocity(timeStep)
      if (enableCollisionDetection) for (c <- detector.collisions) c.correctVelocity(timeStep)
    }
     
    //integration of velocities, yields positions
    for (b <- bodies) {
      //warning when body gets faster than speed of light
      if (b.linearVelocity.length >= 300000000) overCWarning = true
      if (b.linearVelocity.length < minLinearVelocity) b.linearVelocity = Vector2D.Null
      if (b.angularVelocity.abs < minAngularVelocity) b.angularVelocity = 0.0
      b.pos = b.pos + b.linearVelocity * timeStep
      b.rotation = b.rotation + b.angularVelocity * timeStep
      b.force = Vector2D.Null
      b.torque = 0.0   
    }
    
    //correction of positions
    if (enablePositionCorrection) for (i <- 0 until iterations){
      for (c <- joints) c.correctPosition(timeStep)
      if (enableCollisionDetection) for (c <- detector.collisions) c.correctPosition(timeStep)
    }
    
    postStep()
  }
  
  /**Initially empty method that is executed after each time step. This method
   * may be overriden to create custom behaviour in a world.*/
  def postStep() = {}
  
  /**Returns information about this world.*/
  def info = {
    "Bodies = " + bodies.length + "\n" +
    "Shapes = " + shapes.length + "\n" +
    "Joints = " + joints.length + "\n" +
    "Collisions = " + detector.collisions.length + "\n" +
    "Monitors = " +  monitors.length + "\n" +
    "Gravity = " + gravity + "m/s^2\n" +
    "Timestep = " + timeStep + "s\n" +
    "Time = " + time + "s\n" +
    "Iterations = " + iterations
  }
}
