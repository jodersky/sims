/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.gui

import graphyx.actors._
import graphyx.gui._
import scala.swing._
import scala.swing.event._

class ControlPanel(container: Container) extends BoxPanel(Orientation.Horizontal) {
  val btnStart = new Button {text = "Start"} //; icon = new javax.swing.ImageIcon("""play.png"""); tooltip = "Start"}
  val btnStep = new Button {text = "Step"}
  val btnStop = new Button {text = "Stop"}
  val btnExit = new Button {text = "Exit"}
  val btnFire = new Button {text = "Fire!"}
  val btnReset = new Button {text = "Reset"}
  val cboTest = new ComboBox(Graphyx.tests)
  
  contents ++= List(btnStart, btnStep, btnStop, btnExit, new Separator, btnFire, btnReset, cboTest)
  
  listenTo(btnStart, btnStep, btnStop, btnExit, btnFire, btnReset, cboTest.selection)
  reactions += {
    case ButtonClicked(`btnStart`) => Graphyx.physicsActor ! Start
    case ButtonClicked(`btnStop`) => Graphyx.physicsActor ! Stop
    case ButtonClicked(`btnStep`) => Graphyx.physicsActor.world.step()
    case ButtonClicked(`btnExit`) => Graphyx.exit
    case ButtonClicked(`btnFire`) => Graphyx.physicsActor ! FireEvent
    case ButtonClicked(`btnReset`) => Graphyx.test = Graphyx.tests(cboTest.selection.index)
    case SelectionChanged(`cboTest`) => Graphyx.test = Graphyx.tests(cboTest.selection.index)
  }
  
}
