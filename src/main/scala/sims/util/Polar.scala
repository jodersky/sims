/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.util

import sims.geometry._
import scala.math._

/**Polar coordinates.*/
case class Polar(distance: Double, angle: Double) {
  
  /**Returns the vector representation of these polar coordinates.*/
  def toCarthesian = Vector2D(distance * sin(angle), distance * cos(angle))
}
