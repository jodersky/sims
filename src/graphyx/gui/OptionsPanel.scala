/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.gui

import graphyx.graphics._
import sims.geometry._
import sims.dynamics._
import scala.swing._
import scala.swing.event._
import GridBagPanel._

class OptionsPanel(container: Container) extends GridPanel(12,2){
  /*
  val c = new Constraints
  c.anchor = Anchor.West
  */
  
  this.border = Swing.EmptyBorder(3,3,3,3)
  this.hGap = 3
  this.vGap = 3
  
  val lblTimeStep = new Label("h [Hz]") {tooltip = "Time Step"}
  val txtTimeStep = new TextField
  
  val lblIterations = new Label("i [1]") {tooltip = "Iterations"}
  val txtIterations = new TextField
  
  val lblCD = new Label("CD") {tooltip = "Collision Detection"}
  val chckCD = new CheckBox("")
  
  val lblPC = new Label("PC") {tooltip = "Position Correction"}
  val chckPC = new CheckBox("")
  
  val lblDraw = new Label("Draw")
  
  val lblDrawBodies = new Label("Bodies")
  val chckDrawBodies = new CheckBox {selected = false}
  
  val lblDrawShapes = new Label("Shapes")
  val chckDrawShapes = new CheckBox {selected = true}
  
  val lblDrawJoints = new Label("Joints")
  val chckDrawJoints = new CheckBox {selected = true}
  
  val lblDrawAABBs = new Label("AABBs")
  val chckDrawAABBs = new CheckBox {selected = false}
  
  val lblDrawPairs = new Label("Pairs")
  val chckDrawPairs = new CheckBox {selected = false}
  
  val lblDrawCollisions = new Label("Collisions")
  val chckDrawCollisions = new CheckBox {selected = false}
  
  val lblTrace = new Label("Trace")
  val chckTrace = new CheckBox {selected = false}
  
  val components = List(
    lblTimeStep, txtTimeStep,
    lblIterations, txtIterations,
    lblCD, chckCD,
    lblPC, chckPC,
    lblDraw, new Label(""),
    lblDrawBodies, chckDrawBodies,
    lblDrawShapes, chckDrawShapes,
    lblDrawJoints, chckDrawJoints,
    lblDrawAABBs, chckDrawAABBs,
    lblDrawPairs, chckDrawPairs,
    lblDrawCollisions, chckDrawCollisions,
    lblTrace, chckTrace
  )
  contents ++= components
  listenTo(components: _*)
  
  reactions += {
    case EditDone(`txtTimeStep`) => container.scene.world.real.timeStep = 1.0 / txtTimeStep.text.toInt
    case EditDone(`txtIterations`) => container.scene.world.real.iterations = txtIterations.text.toInt
    case ButtonClicked(`chckCD`) => container.scene.world.real.enableCollisionDetection = chckCD.selected
    case ButtonClicked(`chckPC`) => container.scene.world.real.enablePositionCorrection = chckPC.selected
    case ButtonClicked(`chckDrawBodies`) => container.mainFrame.mainPanel.worldPanel.drawBodies = chckDrawBodies.selected
    case ButtonClicked(`chckDrawShapes`) => container.mainFrame.mainPanel.worldPanel.drawShapes = chckDrawShapes.selected
    case ButtonClicked(`chckDrawJoints`) => container.mainFrame.mainPanel.worldPanel.drawJoints = chckDrawJoints.selected
    case ButtonClicked(`chckDrawAABBs`) => container.mainFrame.mainPanel.worldPanel.drawAABBs = chckDrawAABBs.selected
    case ButtonClicked(`chckDrawPairs`) => container.mainFrame.mainPanel.worldPanel.drawPairs = chckDrawPairs.selected
    case ButtonClicked(`chckDrawCollisions`) => container.mainFrame.mainPanel.worldPanel.drawCollisions = chckDrawCollisions.selected
    case ButtonClicked(`chckTrace`) => container.mainFrame.mainPanel.worldPanel.trace = chckTrace.selected
  }
  
  def update() = {
    if (!txtTimeStep.peer.hasFocus)
      txtTimeStep.text = (1.0 / container.scene.world.timeStep).toString
    if (!txtIterations.peer.hasFocus)
      txtIterations.text = container.scene.world.iterations.toString
      chckCD.selected = container.scene.world.enableCollisionDetection
      chckPC.selected = container.scene.world.enablePositionCorrection
      chckDrawBodies.selected = container.mainFrame.mainPanel.worldPanel.drawBodies
      chckDrawShapes.selected = container.mainFrame.mainPanel.worldPanel.drawShapes
      chckDrawJoints.selected = container.mainFrame.mainPanel.worldPanel.drawJoints
      chckDrawAABBs.selected = container.mainFrame.mainPanel.worldPanel.drawAABBs
      chckDrawPairs.selected = container.mainFrame.mainPanel.worldPanel.drawPairs
      chckDrawCollisions.selected = container.mainFrame.mainPanel.worldPanel.drawCollisions
      chckTrace.selected = container.mainFrame.mainPanel.worldPanel.trace
  }
  
  
  /*
  def addCell(cm: Component)(x: Int, y: Int) = {
    c.gridx = x
    c.gridy = y
    c.weightx = 0.5
    c.weighty = 0.0
    c.
    if (cm.isInstanceOf[TextArea])
     c.fill = Fill.Horizontal
    else 
       c.fill = Fill.None
    super.add(cm, c)
  }
  
 addCell(lblTimeStep)(0,0)
 addCell(txtTimeStep)(1,0)
 addCell(lblIterations)(0,1)
 addCell(txtIterations)(1,1)
  */
}
