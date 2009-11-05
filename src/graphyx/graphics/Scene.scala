/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.graphics

import sims.geometry._
import sims.collision._
import sims.dynamics._
import sims.dynamics.joints._
import collection.mutable._

case class Scene(real: World) {
  val world: GraphicalWorld = GraphicalWorld(real)
  val shapes = for (s: Shape <- real.shapes) yield Parser.toGraphical(s)
  val joints = for (j: Joint <- real.joints) yield Parser.toGraphical(j)
  val bodies = for (b: Body <- real.bodies) yield Parser.toGraphical(b)
  val collisions = for (c: Collision <- real.detector.collisions) yield Parser.toGraphical(c)
  val pairs = for (p: Pair <- real.detector.asInstanceOf[GridDetector].pairs) yield Parser.toGraphical(p)
  val aabbs = for (s: Shape <- real.shapes) yield Parser.toGraphical(s.AABB)
  val fps = 0
}
