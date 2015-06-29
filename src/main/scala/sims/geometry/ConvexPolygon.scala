/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.geometry

import sims.collision._
import sims.geometry._

/**Common properties of all convex polygons.*/
trait ConvexPolygon {
  
  /**Returns positions of all vertices of this Polygon. Vertices are ordered counter-clockwise.
   * @return position vectors of the vertices*/
  def vertices: Seq[Vector2D]
  
  /**Returns all sides of this polygon. The sides are ordered counter-clockwise, the first vertex of the side
   * giving the side index.*/
  def sides = (for (i <- 0 until vertices.length) yield (new Segment(vertices(i), vertices((i + 1) % vertices.length)))).toArray
  
  /**Returns the projection of this polygon onto the line given by the directional vector <code>axis</code>.
   * @param axis directional vector of the line
   * @return projection of this polygon*/
  def project(axis: Vector2D) = {
    val points = for (v <- vertices) yield {v project axis}
    val bounds = for (p <- points) yield {if (axis.x != 0) p.x / axis.x else p.y / axis.y}
    Projection(axis,
              (bounds(0) /: bounds)(math.min(_,_)),
              (bounds(0) /: bounds)(math.max(_,_)))
  }
  
  /**Returns this polygon's axis aligned bounding box.
   * @see collision.AABB*/
  def AABB = {
    val xs = vertices map (_.x)
    val ys = vertices map (_.y)
    new AABB(Vector2D(xs.min, ys.min),
             Vector2D(xs.max, ys.max))
  }
  
  /**Checks if the point <code>point</code> is contained in this polygon.
   * <p>
   * A ray is created, originating from the point and following an arbitrary direction (X-Axis was chosen).
   * The number of intersections between the ray and this polygon's sides (including vertices) is counted.
   * The amount of intersections with vertices is substracted form the previuos number.
   * If the latter number is odd, the point is contained in the polygon.*/
  def contains(point: Vector2D) = {
    val r = new Ray(point, Vector2D.i)
    var intersections = 0
    for (s <- sides; if (r intersects s)) intersections += 1
    for (v <- vertices; if (r contains v)) intersections -= 1
    intersections % 2 != 0
  }
}
