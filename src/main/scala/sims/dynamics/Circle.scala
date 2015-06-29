/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics

import sims.geometry._
import sims.collision._

/**
 * A circle.
 * @param radius radius of this circle
 * @param density density of this circle
 */
case class Circle(radius: Double, density: Double) extends Shape{
  
  val volume = math.Pi * radius * radius
  
  val I = mass * radius * radius / 2
  
  def AABB = new AABB(pos - Vector2D(radius,radius),
                      pos + Vector2D(radius,radius))
  
  def project(axis: Vector2D) = if (axis.x != 0) Projection(axis,
                                                            (pos.project(axis).x / axis.x) - radius,
                                                            (pos.project(axis).x / axis.x) + radius)
                                else Projection(axis,
                                                (pos.project(axis).y / axis.y) - radius,
                                                (pos.project(axis).y / axis.y) + radius)
  
  def contains(point: Vector2D) = (point - pos).length <= radius
}
