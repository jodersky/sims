/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.actors

import akka.actor._
import graphyx.graphics._

class GUIActor extends Actor{
  val container = new graphyx.gui.Container
  
  override def preStart() = {
    container.show()
    println("GUI actor started.")
  }

  def receive = {
    case s @ Scene(_) => container.update(s)
    case other => println("Engine received unknown command: " + other)
  }

  override def postStop() = {
    container.exitGUI()
    println("GUI actor exited.")
  }

}

object GUIActor {

  def apply() = Props(classOf[GUIActor])

}
