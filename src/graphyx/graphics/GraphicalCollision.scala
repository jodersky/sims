/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.graphics

import sims.collision._

case class GraphicalCollision(real: Collision) extends GraphicalObject{
  val points = real.points
  val normal = real.normal 
  def draw() = {
    g.setColor(java.awt.Color.GREEN)
    for (p <- points) {drawPoint(p); drawVector(normal, p)}
  }
}