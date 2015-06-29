/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.geometry

import sims.math._
import scala.math._

/**A ray.
 * @param point a point on the ray
 * @param direction this ray's directional vector
 * @throws IllegalArgumentException if the directional vector is the null vector*/
case class Ray(point: Vector2D, direction: Vector2D) {
  
  require(direction != Vector2D.Null, "A ray's direction cannot be given by a null vector!")
  
  /**Checks this ray and the given segment for intersection.
   * @param s the segment to test for intersection*/
  def intersects(s: Segment) = {
    val p1 = point
    val p2 = point + direction
    val p3 = s.vertex1
    val p4 = s.vertex2
    val d = (p4.y - p3.y) * (p2.x - p1.x) - (p4.x - p3.x) * (p2.y - p1.y)
    val na = (p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x)
    val nb = (p2.x - p1.x) * (p1.y - p3.y) - (p2.y - p1.y) * (p1.x - p3.x)
    if (d == 0 && na == 0 && nb == 0)
      true //lines are coincident
    else if (d == 0)
      false //parallel
    else {
      val ua = na / d
      val ub = nb / d
      (ub >= 0) && (ub <= 1) && (ua >= 0)
    }
  }
  
  /**Checks if this ray contains the point <code>p</code>.*/
  def contains(p: Vector2D) = {
    val v = p - point
    p == point ||
    Matrix22(direction, v).det == 0 &&
    signum(direction.x) == signum(v.x) &&
    signum(direction.y) == signum(v.y)
  }
}
