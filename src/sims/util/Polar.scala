/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.util

import sims.geometry._
import scala.Math._

/**Polare Koordinaten.*/
case class Polar(distance: Double, angle: Double) {
  
  /**Ergibt die Vektorrepraesantation dieser polaren Koordinaten.*/
  def toCarthesian = Vector2D(distance * sin(angle), distance * cos(angle))
}
