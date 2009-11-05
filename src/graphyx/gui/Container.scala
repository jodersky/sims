/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.gui

import graphyx.graphics._
import sims.dynamics._

class Container {
  val mainFrame = new MainFrame(this)
  
  var scene: Scene = Scene(new World)
  
  def show() = {
    mainFrame.visible = true
  }
  
  def update(s: Scene) = {
    scene = s
    mainFrame.mainPanel.worldPanel.update()
    mainFrame.mainPanel.infoPanel.update()
    mainFrame.mainPanel.optionsPanel.update()
    mainFrame.mainPanel.gravityPanel.update()
  }
  
  def exitGUI() = {
    mainFrame.dispose
    AboutHelpFrame.frame.dispose
  }
}
