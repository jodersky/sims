/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.gui

import scala.swing._
import scala.swing._

class InfoPanel(container: Container) extends BoxPanel(Orientation.Vertical){
	preferredSize = new java.awt.Dimension(200, 50)
	
  val out = new TextArea
  out.editable = false
  contents += out
  border = Swing.EmptyBorder(3,3,3,3)
  
  def update() = {
    out.text = "fps=" + container.scene.fps + "\n" + 
    			"t=" + container.scene.world.time.formatted("%f") + "\n"
    if (container.scene.world.overCWarning) {
      out.foreground = java.awt.Color.red
      out.text += "Warning: some bodies passed the speed of light! Simulation may be highly incorrect.\n"
    }
    else out.foreground = java.awt.Color.black
    for (r <- container.scene.world.monitorFlatResults) out.text += "b" + r._1 + " " + r._2 + ": " + r._3 + "\n"
  }
}
