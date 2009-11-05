/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.tests

import sims.geometry._
import sims.dynamics._

import sims.util._
import sims.util.Positioning._

object CompositeShape extends Test{
  val title = "Composite Shape"
  val world = new World
  
  def init = {
    
    val ground = new Body(Rectangle(1000,0.1,1)) {fixed = true}
    world += ground
    
    val h2o = {
      val h = new Circle(0.2,1)
      val o1 = new Circle(0.05,1)
      val o2 = new Circle(0.05,1) {rotation = 0.3}
      position(o1) {0.25 above h}
      o2.pos = (new Polar(0.25, Math.Pi / 3)) from h
      new Body(h, o1, o2) {pos = Vector2D(0, 0.5)}
    }
    world += h2o
  }

}
 