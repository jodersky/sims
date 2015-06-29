/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.gui

import sims.dynamics._
import graphyx.graphics._
import java.io._
class Container {
  val mainFrame = new MainFrame(this)
  
  //val plotFrames = new ArrayBuffer[plot.PlotFrame[Body]]
  
  var scene: Scene = Scene(new World)
  
  def show() = {
    mainFrame.visible = true
  }
  
  def update(s: Scene) = {
    scene = s
    mainFrame.mainPanel.controlPanel.update()
    mainFrame.mainPanel.worldPanel.update()
    mainFrame.mainPanel.infoPanel.update()
    mainFrame.mainPanel.optionsPanel.update()
    mainFrame.mainPanel.gravityPanel.update()
  }
  
  def exitGUI() = {
    mainFrame.dispose
    AboutHelpFrame.frame.dispose
    //plotFrames.foreach(_.dispose)
  }
}
