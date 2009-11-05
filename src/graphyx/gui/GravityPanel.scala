/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.gui

import scala.swing._
import scala.swing.event._
import scala.swing.GridBagPanel._
import sims.geometry._

class GravityPanel(container: Container) extends GridBagPanel{
  
  val c = new Constraints
  c.fill = Fill.Both
  this.border = Swing.EmptyBorder(3,3,3,3)
  
  val sldX = new Slider {max = 500; min = -500; preferredSize = minimumSize}
  val lblX = new Label("0.0")
  val sldY = new Slider {max = 500; min = -500; preferredSize = minimumSize}
  val lblY = new Label("-9.81")
  
  c.gridx = 0
  c.gridy = 0
  c.weightx = 1.0
  c.weighty = 0.0
  super.add(new Label("Gravity"), c)
  
  c.gridx = 0
  c.gridy = 1
  c.weightx = 1.0
  c.weighty = 0.0
  super.add(new Label("X: "), c)
  
  c.gridx = 1
  c.gridy = 1
  c.weightx = 1.0
  c.weighty = 0.0
  super.add(sldX, c)
  
  c.gridx = 2
  c.gridy = 1
  c.weightx = 0.0
  c.weighty = 0.0
  super.add(lblX, c)
  
  c.gridx = 0
  c.gridy = 2
  c.weightx = 1.0
  c.weighty = 0.0
  super.add(new Label("Y: "), c)
  
  c.gridx = 1
  c.gridy = 2
  c.weightx = 1.0
  c.weighty = 0.0
  super.add(sldY, c)
  
  c.gridx = 2
  c.gridy = 2
  c.weightx = 0.0
  c.weighty = 0.0
  super.add(lblY, c)
  
  
  listenTo(sldX, sldY)
  
  reactions += {
    case ValueChanged(s) if (s == sldX || s == sldY) =>
      container.scene.real.gravity = Vector2D(sldX.value / 10.0, sldY.value / 10.0)
  }
  
  def update() = {
    val g = container.scene.world.gravity
    sldX.value = (g.x * 10).toInt
    lblX.text = ((g.x * 10).toInt / 10.0).toString
    sldY.value = (g.y * 10).toInt
    lblY.text = ((g.y * 10).toInt / 10.0).toString
  }
  
}
