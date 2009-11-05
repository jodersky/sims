/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.actors

import graphyx.graphics._
import graphyx.gui._
import scala.actors._

class GUIActor extends Actor{
  val container = new Container
  
  var continue = true

  def act() = {
    container.show()
    println("GUI actor started.")
    while (continue) {
        receive {
          case Exit => {
            continue = false
          }
          case s @ Scene(_) => container.update(s)
          case other => println("Engine received unknown command: " + other)
     	}
      }
    println("GUI actor exited.")
  }
}
