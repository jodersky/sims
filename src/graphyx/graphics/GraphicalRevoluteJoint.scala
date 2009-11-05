/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.graphics

import sims.geometry._
import sims.dynamics.joints._

case class GraphicalRevoluteJoint(real: RevoluteJoint) extends GraphicalJoint {
  val connection1 = real.connection1
  
   def draw(): Unit = {
    g.setColor(java.awt.Color.darkGray)
    drawPoint(connection1)
  }

}
