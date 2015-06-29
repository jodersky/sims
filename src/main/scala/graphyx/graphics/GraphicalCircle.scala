/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.graphics

import sims._
import geometry._
import dynamics._
class GraphicalCircle(val real: Circle) extends Circle(real.radius, real.density) with GraphicalShape{
  override def draw() = {
    //val b = math.min(density / 100 * 255, 255)
    //g.setColor(new java.awt.Color(0,0,255, b.toInt))
    g.setColor(java.awt.Color.blue)
    fillCircle(pos, real.radius)
    g.setColor(java.awt.Color.BLACK)
    drawCircle(pos, real.radius)
    this.drawLine(pos, pos + (Vector2D.i rotate rotation) * real.radius)
  }
}
