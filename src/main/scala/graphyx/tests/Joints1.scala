/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.tests

import sims.dynamics._
import sims.dynamics.joints._
import sims.geometry._

object Joints1 extends Test{
  override val title = "Joints1"
  val world = new World
  def init = {
    val anchor = new Body(new Circle(0.03, 1) {pos = Vector2D(1,5)}) {fixed = true}
    val weight = (new Rectangle(0.1, 0.5, 1) {pos = Vector2D(1,0)}).asBody
    val joint = new DistanceJoint(anchor, anchor.pos, weight, weight.pos + Vector2D(0.1,0.2))
    world += anchor
    world += weight
    world += joint
  }
}
