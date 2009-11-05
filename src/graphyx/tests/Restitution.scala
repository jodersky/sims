/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.tests

import sims.geometry._
import sims.dynamics._

object Restitution extends Test{
  val title = "Restitution"
  val world = new World
  
  def init = {
    world += new Body(new Rectangle(1000,0.1,10) {restitution = 1}) {fixed = true}
    world ++= (for (i <- 0 until 10) yield (new Circle(0.05, 10) {pos = Vector2D(i * 0.5, 1); restitution = i / 10.0}).asBody)
  }

}
