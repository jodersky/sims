/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics

import sims.geometry._
import sims.collision._

/**A rectangle is a polygon.
 * @param halfWidth this rectangle's half width
 * @param halfHeight this rectangle's half height
 * @param density density of this rectangle
 */
case class Rectangle(halfWidth: Double,
                     halfHeight : Double,
                     density: Double) extends Shape with ConvexPolygon{
  
  val volume = halfWidth * halfHeight * 4
  
  val I = 1.0 / 12.0  * mass * ((2 * halfWidth) * (2 * halfWidth) + (2 * halfHeight) * (2 * halfHeight))
  
  /**Returns the vectors from the center to the vertices of this rectangle.
   * The first vertex is the upper-right vertex at a rotation of 0.
   * Vertices are ordered counter-clockwise.*/
  def halfDiags: Array[Vector2D] = Array(Vector2D(halfWidth, halfHeight),
                                         Vector2D(-halfWidth, halfHeight),
                                         Vector2D(-halfWidth, -halfHeight),
                                         Vector2D(halfWidth, -halfHeight)) map (_ rotate rotation)
  
  /**Returns the position vectors of this rectangle's vertices.
   * The first vertex is the upper-right vertex at a rotation of 0.
   * Vertices are ordered counter-clockwise.*/
  def vertices = for (h <- halfDiags) yield pos + h
  
} 