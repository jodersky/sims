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

class ShapeInfoPanel(container: Container) extends GridPanel(2,2) {
  
  this.border = Swing.EmptyBorder(3,3,3,3)
  this.hGap = 3
  this.vGap = 3
  
  val lblBody = new Label("Body")
  val lblValBody = new Label("0")
  
  val lblShape = new Label("Shape")
  val lblValShape = new Label("0")
  
  val components = List(
    lblBody, lblValBody,
    lblShape, lblValShape
  )
  
  contents ++= components
  
}
