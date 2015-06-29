/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.tests

import sims._
import sims.geometry._
import sims.dynamics._

object Net extends Test{
  val title = "Net"
  val world = new World
  
  def init = {
    val n = new prefabs.Net(10, 10, Vector2D(4,4))
    n.bodies(9).fixed = true
    world += n
  }
}
