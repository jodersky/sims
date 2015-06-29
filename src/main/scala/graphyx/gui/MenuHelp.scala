package graphyx.gui

import graphyx.actors._
import graphyx.gui._
import scala.swing._
import scala.swing.event._

class MenuHelp extends Menu("Help") {
  val miAbout = new MenuItem("About...")
  
  val components = List(miAbout)
  contents ++= components
  
  listenTo(components: _*)
  reactions += {
    case event.ButtonClicked(`miAbout`) => AboutHelpFrame.frame.visible = true
    
  }
}
