package graphyx.tests

import sims.dynamics._
import sims.dynamics.joints._
import sims.geometry._
import java.io._

object Spring extends Test{
  val title = "Spring"
  val fout = new java.io.FileOutputStream("out.csv")
  val sout = new java.io.PrintStream(fout)
  val world = new World {
    override def postStep = {
      //for (b <- bodies; if (b.monitor)) sout.println(monitors(0)._2(b))
    }
  }
  
  def init = {
    val anchor = Circle(0.05, 10).asBody
    anchor.fixed = true
    val particle = (new Circle(0.1, 10) {pos = Vector2D(0, -1)}).asBody
    val spring = new SpringJoint(anchor, particle, 500, 0.6)
    //val spring = new PrismaticJoint(anchor, particle)
    spring.damping = 1
    particle.monitor = true
    world.monitors += ("", _.pos.y.toString)
    
    world += anchor
    world += particle
    world += spring
  }
}
