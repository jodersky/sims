package graphyx.tests

import sims.dynamics._
import sims.dynamics.joints._
import sims.geometry._
import java.io._

object Spring extends Test{
  val title = "Spring"
  val world = new World
  
  def init = {
    val anchor = Circle(0.05, 10).asBody
    anchor.fixed = true
    val particle = (new Circle(0.1, 20) {pos = Vector2D(0, -3)}).asBody
    val spring = new SpringJoint(anchor, particle, 500, 2)
    spring.damping = 0.5
    
    world += anchor
    world += particle
    world += spring
  }
}
