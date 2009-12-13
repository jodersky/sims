/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.tests

import sims.dynamics._
trait Test {
  val world: World
  val title: String
  def init(): Unit
  var enableEvent: Boolean = false
  def fireEvent(): Unit = println("No custom event method defined.")
  override def toString() = title
}
