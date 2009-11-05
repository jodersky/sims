/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.tests

import sims.dynamics._
import sims.geometry._
object Cup extends Test {
  val title = "Cup"
  val world = new World

  def init = {
    val ground = (new Rectangle(1000, 0.1, 1) {friction = 1}).asBody
    ground.fixed = true
    world += ground
    
    val cupShapes = for (i <- 0 to (10 * Math.Pi).toInt) yield new Circle(0.1,1) {pos = Vector2D(Math.cos(-i / 10.0), Math.sin(-i / 10.0)); restitution = 0.0; friction = 1.0}
    val cup = new Body(cupShapes: _*) {fixed = true; pos = Vector2D(0, 1)}
    world += cup
    
    val ball1 = (new Circle(0.2, 1) {pos = Vector2D(0, 2)}).asBody
    val ball2 = (new Circle(0.2, 1) {pos = Vector2D(-0.4, 2)}).asBody
    val ball3 = (new Circle(0.2, 1) {pos = Vector2D(0.4, 2)}).asBody
    world += ball1
    world += ball2
    world += ball3
  }
}
