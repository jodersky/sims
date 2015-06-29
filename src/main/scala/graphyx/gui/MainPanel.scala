/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.gui

import graphyx.graphics._
import sims._
import scala.swing._
import swing.event._
import GridBagPanel._
import java.awt.Insets

class MainPanel(container: Container) extends scala.swing.GridBagPanel {
  val c = new Constraints
  
  val menuPanel = new MenuPanel(container)
  val worldPanel = new WorldPanel(container)
  val controlPanel = new ControlPanel(container)
  val infoPanel = new InfoPanel(container)
  val optionsPanel = new OptionsPanel(container)
  val shapeInfoPanel = new ShapeInfoPanel(container)
  val gravityPanel = new GravityPanel(container)
  
  val splitter = new SplitPane {
    orientation = Orientation.Vertical
    continuousLayout = true
    resizeWeight = 1
    dividerSize = 2
    leftComponent = worldPanel
    rightComponent = new SplitPane {
      orientation = Orientation.Horizontal
      continuousLayout = true
      resizeWeight = 1
      dividerSize = 2
      topComponent = new SplitPane{
        orientation = Orientation.Horizontal
        continuousLayout = true
        resizeWeight = 1
        dividerSize = 2
        topComponent = infoPanel
        bottomComponent = gravityPanel
      }
      bottomComponent = optionsPanel
    }
  }
  c.fill = Fill.Both

  c.gridx = 0
  c.gridy = 0
  c.weightx = 1.0
  c.weighty = 0.0
  super.add(menuPanel, c)
  
  c.gridx = 0
  c.gridy = 1
  c.weightx = 1.0
  c.weighty = 0.0
  super.add(controlPanel, c)
  
  
  c.gridx = 0
  c.gridy = 2
  c.weightx = 1.0
  c.weighty = 1.0
  super.add(splitter, c)
  
  /*
  c.gridx = 1
  c.gridy = 1
  c.weightx = 0.0
  c.weighty = 1.0
  super.add(infoPanel, c)
  */
}
