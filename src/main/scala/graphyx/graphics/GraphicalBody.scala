/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.graphics

import sims.dynamics._

case class GraphicalBody(real: Body) extends GraphicalObject {
  val pos = real.pos
  val fixed = real.fixed
  val monitor = real.monitor
  def draw() = {
    val radius = 4
    val posX = (pos.x * scale * ppm).toInt
    val posY = correctY(pos.y * scale * ppm).toInt
    g.setColor(java.awt.Color.yellow)
    g.fillArc(posX - radius,
              posY - radius,
              (radius * 2).toInt,
              (radius * 2).toInt,
              0, 90)
    g.fillArc(posX - radius,
              posY - radius,
              (radius * 2).toInt,
              (radius * 2).toInt,
              180, 90)
    g.setColor(java.awt.Color.black)
    g.fillArc(posX - radius,
              posY - radius,
              (radius * 2).toInt,
              (radius * 2).toInt,
              90, 90)
    g.fillArc(posX - radius,
              posY - radius,
              (radius * 2).toInt,
              (radius * 2).toInt,
              270, 90)
  }
}
