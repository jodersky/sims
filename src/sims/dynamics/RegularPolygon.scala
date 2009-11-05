/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics

import Math._
import sims.geometry._

/**Ein regelmaessiges Polygon mit <code>n</code> Seiten, dass der Kreis mit radius <code>radius</code> umschreibt.
 * @param n Anzahl der Seiten.
 * @param radius Radius des umschreibenden Kreises.
 * @param density Dichte.
 */
case class RegularPolygon(n: Int, radius: Double, density: Double) extends Shape with ConvexPolygon{
  require(n >= 3, "Polygon must have at least 3 sides.")
  
  /**Hoehe eines der konstituierneden Dreiecke des Polygons.*/
  private val h: Double = radius * cos(Pi / n)
  /**Halbe Breite eines der konstituierneden Dreiecke des Polygons.*/
  private val b: Double = radius * sin(Pi / n)
  
  def halfDiags = (for (i: Int <- (0 until n).toArray) yield (Vector2D(0, radius) rotate (2 * Pi * i / n))) map (_ rotate rotation)
  
  def vertices = for (h <- halfDiags) yield pos + h
  
  val volume = n * h * b
  
  /**Traegheitsmoment eines der konstituierneden Dreiecke im Zentrum des Polygons.*/
  private val Ic: Double = density * b * (3 * b + 16) * h * h * h * h / 54
  
  val I = n * Ic
}
