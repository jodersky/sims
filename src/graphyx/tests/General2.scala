/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.tests

import sims.geometry._
import sims.dynamics._

object General2 extends Test{
  val title = "General2"
  val world = new World
  
  def init = {
    world += new Body(new Rectangle(100,0.5,1) {pos = Vector2D(0, -0.5)}) {fixed = true}
    	//new Body(new Circle(0.05,1) {pos = Vector2D(0, 0.05)}),
    	//new Body(new Rectangle(0.5,0.1,1) {pos = Vector2D(0, 0.2)}),
    	//new Body(new Circle(0.05,1) {pos = Vector2D(1, 0.05)}))
    
    world += new Body(new Circle(0.2,1) {pos = Vector2D(5, 0.2)})
    world += new Body(new Rectangle(1.5,0.1,1) {pos = Vector2D(4.5, 0.5)}, new Rectangle(0.05,0.1,1) {pos = Vector2D(3.05,0.7)})
    world += new Body(new Circle(0.1,1) {pos = Vector2D(3.2, 0.7)})
    world += new Body(new Circle(0.5,10) {pos = Vector2D(8, 0.5)})
    
  }
}
