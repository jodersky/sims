/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.tests

import sims.geometry._
import sims.dynamics._
import sims.dynamics.joints._

object Friction2 extends Test{
  val title = "Friction2"
  val world = new World {override val detector = new sims.collision.GridDetector(this) {gridSide = 0.2}}
  
  def init = {
    val shapes = for (i <- (0 to 20).toList) yield (new Rectangle(0.2, 0.1, 1) {
      pos = Vector2D(0.4 * i, 0)
      friction = (i * 1.0 / 10)
      restitution = 0
    })
    val ground = new Body(shapes: _*)
    ground.pos = Vector2D(0,0)
    ground.fixed = true
    ground.rotation = -math.Pi / 5
    world += ground
    
    val b: Body = (new Circle(0.1,10)) ~ (new Circle(0.1,10) {pos = Vector2D(0.2,0)}) ~ (new Circle(0.1,10) {pos = Vector2D(0.4,0)})
    b.pos = Vector2D(0.1,0.1)
    world += b
  }
}
