package graphyx.tests

import sims.dynamics._
import sims.dynamics.joints._
import sims.dynamics.joints.test._
import sims.geometry._

object Wave extends Test{
  val title = "Wave"
  val world = new World {gravity = Vector2D.Null}
  
  def init = {
    val n = 50
    
    val anchors = for (i <- (0 to n).toList) yield
      new Body(new Circle(0.01,1) {pos = Vector2D(0.4 * i, 5)}) {fixed = true}
    
    val particles = for (i <- (0 to n).toList) yield
      new Body(new Circle(0.1,10) {pos = Vector2D(0.4 * i, 0)})
    
    val rails = for (i <- (0 to n).toList) yield
      new PrismaticJoint(anchors(i), particles(i))
    
    val springs = for (i <- (0 to n).toList) yield
      new SpringJoint(anchors(i), particles(i), 5)// {damping = 0.00})
    
    val lateralSprings = for (i <- (0 to (n - 1)).toList) yield
      new SpringJoint(particles(i), particles(i + 1), 50.0)
    
    world ++= anchors
    world ++= particles
    for (j <- rails ++ springs ++ lateralSprings) world += j   
  }
}
