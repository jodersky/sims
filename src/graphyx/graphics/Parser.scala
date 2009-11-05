/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.graphics

import sims.collision._
import sims.dynamics._
import sims.dynamics.joints._
object Parser {
  
  val throwOnUnknown = false
  
  def toGraphical(real: Shape) = real match {
    case c: Circle => GraphicalCircle(c)
    case r: Rectangle => GraphicalRectangle(r)
    case p: RegularPolygon => GraphicalRegularPolygon(p)
    case _ => throw new IllegalArgumentException("Cannot cast '" + real.getClass + "' to a graphical object.")
  }
  
  def toGraphical(real: Joint) = real match {
    case j: DistanceJoint => GraphicalDistanceJoint(j)
    case j: SpringJoint => GraphicalSpringJoint(j)
    case j: RevoluteJoint => GraphicalRevoluteJoint(j)
    case _ => throw new IllegalArgumentException("Cannot cast '" + real.getClass + "' to a graphical object.")
  }
  
  def toGraphical(real: Collision) = GraphicalCollision(real)
  def toGraphical(real: Pair) = GraphicalPair(real)
  def toGraphical(real: AABB) = GraphicalAABB(real)
  def toGraphical(real: Body) = GraphicalBody(real)
}
