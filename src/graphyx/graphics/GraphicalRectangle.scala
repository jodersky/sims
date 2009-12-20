/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.graphics

import sims._
import sims.dynamics._
class GraphicalRectangle(val real: Rectangle) extends Rectangle(real.halfWidth, real.halfHeight, real.density) with GraphicalShape {
  override def draw() = {
    g.setColor(java.awt.Color.red)
    fillPolygon(vertices)
    g.setColor(java.awt.Color.BLACK)
    drawPolygon(vertices)
  }
}