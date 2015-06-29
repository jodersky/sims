/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics

import math._
import sims.geometry._

/**A regular polygon with <code>n</code> sides whose excircle has a radius <code>radius</code>.
 * @param n nmber of sides.
 * @param radius radius of the excircle
 * @param density density of this regular polygon
 * @throws IllegalArgumentException if <code>n</code> is smaller than 3 */
case class RegularPolygon(n: Int, radius: Double, density: Double) extends Shape with ConvexPolygon{
  require(n >= 3, "A polygon must have at least 3 sides.")
  
  /**Height of one of the constituting triangles.*/
  private val h: Double = radius * cos(Pi / n)
  /**Half width of one of the constituting triangles.*/
  private val b: Double = radius * sin(Pi / n)
  
  def halfDiags = (for (i: Int <- (0 until n).toArray) yield (Vector2D(0, radius) rotate (2 * Pi * i / n))) map (_ rotate rotation)
  
  def vertices = for (h <- halfDiags) yield pos + h
  
  val volume = n * h * b
  
  /**Moment of inertia of one of the constituting triangles about the center of this polygon.*/
  private val Ic: Double = density * b * (3 * b + 16) * h * h * h * h / 54
  
  val I = n * Ic
}
