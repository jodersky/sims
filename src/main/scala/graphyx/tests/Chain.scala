/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.tests

import sims.dynamics._
import sims.dynamics.joints._
import sims.prefabs._
import sims.geometry._

object Chain extends Test{
  override val title = "Chain"
  val world = new World
  def init = {
    val nodes = for (i <- (0 until 20).toList) yield (new Circle(0.02, 1) {pos = Vector2D(i * 0.2, 1)}).asBody
    nodes(0).fixed = true
    nodes(19).fixed = true
    val connectors = for (i <- (0 until nodes.length - 1).toList) yield new DistanceJoint(nodes(i), nodes(i + 1))
    for (n <- nodes) world += n
    for (c <- connectors) world += c
    world += (new Circle(1,0.001) {pos = Vector2D(2,2)}).asBody
  }
}
