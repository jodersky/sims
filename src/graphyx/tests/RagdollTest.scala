/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.tests

import sims.dynamics._
import sims.dynamics.joints._
import sims.geometry._
import sims.prefabs._

object RagdollTest extends Test {
  val title = "Ragdoll"
  val world = new World
  
  def init = {
    val ground = new Body((for (i <- 0 to 1000) yield new Circle(0.5,1) {pos = Vector2D(0.5 * (i - 500), 0)}): _*)
    ground.fixed = true
    world += ground
    
    world += new Ragdoll(Vector2D(0, 5))
  }

}
