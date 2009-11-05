/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.tests

import sims.geometry._
import sims.dynamics._
import sims.dynamics.joints._

object Friction extends Test{
  val title = "Friction"
  val world = new World
  
  var r1: RevoluteJoint = _
  var r2: RevoluteJoint = _
  var r3: RevoluteJoint = _
  
  def init = {
    val ground = (new Rectangle(1000, 0.1, 1) {friction = 1}).asBody
    ground.fixed = true
    world += ground
    
    val ball1 = (new Circle(0.2, 1) {pos = Vector2D(0, 1); friction = 0.05}).asBody
    ball1.angularVelocity = -80
    ball1.monitor = true
    val ball2 = (new Circle(0.2, 1) {pos = Vector2D(1, 1); friction = 0.2}).asBody
    ball2.angularVelocity = -80
    ball2.monitor = true
    val ball3 = (new Circle(0.2, 1) {pos = Vector2D(2, 1); friction = 1}).asBody
    ball3.angularVelocity = -80
    ball3.monitor = true
    
    world += ball1
    world += ball2
    world += ball3
    
    world.monitors += ("", (b: Body) => "I=" + b.I + "\tw=" + b.angularVelocity + "\tEcin=" + 0.5 * b.I * b.angularVelocity * b.angularVelocity)
    
    r1 = RevoluteJoint(ground, ball1, ball1.pos)
    r2 = RevoluteJoint(ground, ball2, ball2.pos)
    r3 = RevoluteJoint(ground, ball3, ball3.pos)
    world += r1
    world += r2
    world += r3
  }
  
  override def fireEvent = {
    world -= r1
    world -= r2
    world -= r3
  }

}
