package graphyx.tests

import sims.dynamics._
import sims.dynamics.joints._
import sims.geometry._

object Atom extends Test{
  val title = "Atom"
  val world = new World
  
  var nucleus = new Body(Circle(0.05, 1000)) {fixed = true}
  var electrons: List[Body] = Nil
  var connections: List[SpringJoint] = Nil
  
  def init = {
    world -= nucleus
    nucleus = new Body(Circle(0.05, 10)) {fixed = true}
    world += nucleus
    world --= electrons
    electrons = Nil
    for (c <- connections) world -= c
    connections = Nil
  }
  
  enableEvent = true
  override def fireEvent = {
    val e = (new Circle(0.1, 10) {pos = Vector2D(0, -1)}).asBody
    e.linearVelocity = Vector2D(-50,100)
    electrons = e :: electrons
    world += e
    
    val c = new SpringJoint(nucleus, e, 500)
    connections = c :: connections
    world += c
  }
}
