/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.gui

import graphyx._
import sims._
import scala.swing._

class MainFrame(container: Container) extends Frame{
  super.background = java.awt.Color.WHITE
  title = "graphyx"
  preferredSize = new java.awt.Dimension(1000,800)
  
  reactions += {
    case event.WindowClosing(w) => Graphyx.exit()
  }
  
  val mainPanel = new MainPanel(container)
  contents = mainPanel
  
}
