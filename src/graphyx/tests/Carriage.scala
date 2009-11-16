/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.tests

import sims.dynamics._
import sims.dynamics.joints._
import sims.geometry._

object Carriage extends Test{
  val title = "Carriage"
  val world = new World
  
  def init = {
    val car = new Car
    
    val groundShapes = for (i <- (0 to 200).toList) yield (new Rectangle(0.2, 0.1, 1) {
      pos = Vector2D(0.4 * i, 0)
      transientShapes += car.chassis.shapes(0)
    })
    val ground = new Body(groundShapes: _*) //Rectangle(100, 0.1, 1).asBody
    ground.pos = Vector2D(0,0)
    ground.fixed = true
    ground.rotation = -0.2
    
    
    world += ground
    world += car
    
  }
  
  class Car extends sims.prefabs.Prefab {
    
    val chassis = (new Rectangle(0.5, 0.05, 10) {pos = Vector2D(0, 1)}).asBody
    val rightWheel = (new Circle(0.1, 10) {pos = Vector2D(0.4,0.8)}).asBody
    val leftWheel = (new Circle(0.1, 10) {pos = Vector2D(-0.4,0.8)}).asBody
    
    override val bodies = List(chassis, rightWheel, leftWheel)
    
    val rightStabelizer = new DistanceJoint(chassis, rightWheel)
    val rightDamper = new SpringJoint(chassis, chassis.pos + Vector2D(rightWheel.pos.x, 0),
                                        rightWheel, rightWheel.pos, 500) {damping = 1}
    
    val leftStabelizer = new DistanceJoint(chassis, leftWheel)
    val leftDamper = new SpringJoint(chassis, chassis.pos + Vector2D(leftWheel.pos.x, 0),
                                       leftWheel, leftWheel.pos, 500) {damping = 1}
    
    override val joints = List(rightStabelizer, rightDamper, leftStabelizer, leftDamper)
    
  }

}
