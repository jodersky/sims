/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics

import sims.geometry._
import sims.collision._

/**
 * Circle ist die Definition eines Kreises.
 * @param radius Radius dieses Kreises
 * @param density Dichte dieses Kreises
 */
case class Circle(radius: Double,					// Radius
                  density: Double) extends Shape{	// Dichte
  
  val volume = Math.Pi * radius * radius
  
  val I = mass * radius * radius / 2
  
  // AABB(Zentrum - Radius, Zentrum + Radius)
  def AABB = new AABB(pos - Vector2D(radius,radius),
                      pos + Vector2D(radius,radius))
  
  def project(axis: Vector2D) = if (axis.x != 0) Projection(axis,
                                                            (pos.project(axis).x / axis.x) - radius,
                                                            (pos.project(axis).x / axis.x) + radius)
                                else Projection(axis,
                                                (pos.project(axis).y / axis.y) - radius,
                                                (pos.project(axis).y / axis.y) + radius)
  
  //Ist der gegebene punkt im Radius dieses kreises?
  def contains(point: Vector2D) = (point - pos).length <= radius
}
