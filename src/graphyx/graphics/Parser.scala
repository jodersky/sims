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
    case c: Circle => new GraphicalCircle(c)
    case r: Rectangle => new GraphicalRectangle(r)
    case p: RegularPolygon => new GraphicalRegularPolygon(p)
    case _ => throw new IllegalArgumentException("Cannot cast '" + real.getClass + "' to a graphical object.")
  }
  
  def toGraphical(real: Joint) = real match {
    case j: DistanceJoint => new GraphicalDistanceJoint(j)
    case j: SpringJoint => new GraphicalSpringJoint(j)
    case j: RevoluteJoint => new GraphicalRevoluteJoint(j)
    case j: Joint => if (!throwOnUnknown) new GraphicalJoint{override val real = j; def draw = ()}
                     else throw new IllegalArgumentException("Cannot cast '" + real.getClass + "' to a graphical object.")
  }
  
  def toGraphical(real: Collision) = new GraphicalCollision(real)
  def toGraphical(real: Pair) = new GraphicalPair(real)
  def toGraphical(real: AABB) = new GraphicalAABB(real)
  def toGraphical(real: Body) = new GraphicalBody(real)
}
