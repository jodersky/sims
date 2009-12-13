/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.tests

import sims.dynamics._
import sims.geometry._
object EmptyTest extends Test{
  override val title = "Empty Test"
  val world = new World
  def init = {}
}
