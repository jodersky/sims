package graphyx.gui

import graphyx.actors._
import graphyx.gui._
import scala.swing._
import scala.swing.event._

class MenuPanel(container: Container) extends BoxPanel(Orientation.Horizontal) {
  val mnu = new MenuBar
  mnu.contents += new MenuHelp
  contents  += mnu
}
