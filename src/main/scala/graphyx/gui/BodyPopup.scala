/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.gui

import scala.swing._
import scala.swing.event._
import sims.dynamics._
import graphyx.graphics._

class BodyPopup extends PopupMenu {
  private var b: Body = _
  def body = b
  def body_=(newBody: Body) = {
    b = newBody
    chckFixed.selected = b.fixed
    chckMonitor.selected = b.monitor
  }
  
  val chckMonitor = new CheckMenuItem("Monitor")
  val chckFixed = new CheckMenuItem("Fixed")
  val btnClose = new MenuItem("Close")
  
  add(chckMonitor)
  add(chckFixed)
  add(btnClose)
  listenTo(chckMonitor, chckFixed, btnClose)
  reactions += {
    case ButtonClicked(b) => {setVisible(false)
      b match {
        case `chckMonitor` => body.monitor = chckMonitor.selected
        case `chckFixed` => body.fixed = chckFixed.selected
        case `btnClose` => ()
        case _ => ()
      }
    } 
  }
}