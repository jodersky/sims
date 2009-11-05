/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics

import sims.geometry._
import sims.collision._

/**Rechteck ist eine Art Polygon.
 * @param halfWidth halbe Breite dieses Rechtecks
 * @param halfHeight halbe Hoehe dieses Rechtecks
 * @param density dichte dieses Rechtecks
 */
case class Rectangle(halfWidth: Double,
                     halfHeight : Double,
                     density: Double) extends Shape with ConvexPolygon{
  
  val volume = halfWidth * halfHeight * 4
  
  val I = 1.0 / 12.0  * mass * ((2 * halfWidth) * (2 * halfWidth) + (2 * halfHeight) * (2 * halfHeight))
  
  /**Ergibt Vektoren vom Zentrum dieses Rectecks bis zu den Ecken.
   * Erste Ecke entspricht der Ecke oben rechts bei einer Rotation von 0.
   * Folgende Ecken sind gegen den Uhrzeigersinn geordnet.
   * @return Vektoren vom Zentrum dieses Rectecks bis zu den Ecken*/
  def halfDiags: Array[Vector2D] = Array(Vector2D(halfWidth, halfHeight),
                                         Vector2D(-halfWidth, halfHeight),
                                         Vector2D(-halfWidth, -halfHeight),
                                         Vector2D(halfWidth, -halfHeight)) map (_ rotate rotation)
  
  /**Ergibt die Ortsvektoren der Ecken dieses Rechtecks. 
   * Erste Ecke entspricht der Ecke oben rechts bei einer Rotation von 0.
   * Folgende Ecken sind gegen den Uhrzeigersinn geordnet.
   * @return Ortsvektoren der Ecken dieses Rechtecks*/
  def vertices = for (h <- halfDiags) yield pos + h
  
} 