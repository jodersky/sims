/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.graphics

import sims.collision._
case class GraphicalPair(real: Pair) extends GraphicalObject{
  val pos1 = real.s1.pos
  val pos2 = real.s2.pos
  
  def draw() = {
    g.setColor(java.awt.Color.ORANGE)
    drawLine(pos1, pos2)
  }
}
