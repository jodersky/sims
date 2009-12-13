/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.graphics

import sims._
import geometry._
import dynamics._
case class GraphicalRegularPolygon(real: RegularPolygon) extends RegularPolygon(real.n, real.radius, real.density) with GraphicalShape{
  override def draw() = {
    g.setColor(java.awt.Color.orange)
    fillPolygon(vertices)
    g.setColor(java.awt.Color.BLACK)
    drawPolygon(vertices)
  }
}