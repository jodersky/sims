/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.util

import sims.geometry._
import sims.dynamics._

/**Utility functions for comfortable positioning of bodies.*/
object Positioning {
  
  implicit def int2RelativeVector(x: Int): RelativeVector = new RelativeVector(x, 0)
  implicit def double2RelativeVector(x: Double): RelativeVector = new RelativeVector(x, 0)
  implicit def vector2RelativeVector(v: Vector2D): RelativeVector = new RelativeVector(v.x, v.y)
  implicit def polar2Carthesian(p: Polar): Vector2D = p.toCarthesian
  implicit def polar2RelativeVector(p: Polar): RelativeVector = vector2RelativeVector(p.toCarthesian)
  
  def position(s: Shape)(a: Vector2D) = {
    s.pos = a
  }
}
