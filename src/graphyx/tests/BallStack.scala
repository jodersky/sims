/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.tests

import sims.dynamics._
import sims.geometry._

object BallStack extends Test{
  val world = new World
  val title = "BallStack"
  def init(): Unit = {
    val ground = new Rectangle(1,0.1,1) {pos = Vector2D(1,0)}
    val wallLeft = new Rectangle(0.1,1,1) {pos = Vector2D(0,1)}
    val wallRight = new Rectangle(0.1,1,1) {pos = Vector2D(2,1)}
    val box = new Body(ground, wallLeft, wallRight) {fixed = true}
    world += box
    world += (new Circle(0.1,1) {pos = Vector2D(1.1, 2.8)}).asBody
    world ++= (for (i <- 0 to 50) yield (new Circle(0.1,1) {pos = Vector2D(1, 3 + 0.2 * i)}).asBody)
    
  }
}
