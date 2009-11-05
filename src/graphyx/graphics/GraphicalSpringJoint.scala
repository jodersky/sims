/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.graphics

import sims.dynamics.joints._

case class GraphicalSpringJoint(real: SpringJoint) extends GraphicalJoint {
  val connection1 = real.connection1
  val connection2 = real.connection2
  def draw(): Unit = {
    g.setColor(java.awt.Color.GRAY)
    drawLine(connection1, connection2)
  }
}
