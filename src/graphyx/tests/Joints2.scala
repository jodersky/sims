/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.tests

import sims.dynamics._
import sims.dynamics.joints._
import sims.geometry._
import sims.util._
import sims.util.Positioning._

object Joints2 extends Test{
  override val title = "Joints2"
  val world = new World
  def init() = {
    val length = 100
    val distance = 0.2
    val anchors = for (i <- (0 until length).toList) yield new Body(new Circle(0.03,1) {pos = Vector2D(i * distance,5)}) {fixed = true}
    val balls = for (i <- (0 until length).toList) yield new Body(new Circle(0.1,1) {pos = Vector2D(i * distance,0); restitution = 1})
    balls(0).pos = Vector2D(0, 5) + Polar(5, -Math.Pi / 1.5).toCarthesian
    val joints = for (i <- (0 until length).toList) yield new DistanceJoint(anchors(i), balls(i))
    for (a <- anchors) world += a
    for (b <- balls) world += b
    for (j <- joints) world += j
  }
}
