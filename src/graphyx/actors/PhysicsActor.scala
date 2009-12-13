/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.actors

import graphyx._
import graphyx.graphics._
import sims.dynamics._
import scala.actors._
class PhysicsActor extends Actor{
  var world = new World
  
  var continue = true
  var simulate = false
  
  private var _fps = 0
  def fps = _fps
  
  def act{
    println("Physics actor started.")
    while (continue) {
      
      var t0 = System.nanoTime
      
      if (simulate) {
        world.step()
      }
      
      Graphyx.guiActor ! new Scene(world) {override val fps = _fps}
      
        receiveWithin(0) {
          case TIMEOUT => () 
            
          case Stop => {
            simulate = false
            println("Simulation stopped.")
          }
          case Start => {
            simulate = true
            println("Simulation started.")
          }
          case Exit => {
            continue = false
          }
          case sw @ SetWorld(w: World) => world = w
          
          case FireEvent => Graphyx.test.fireEvent()
          
          case other => println("Engine received unknown command: '" + other + "'")
     	}
      
      val h = (System.nanoTime - t0) / 1000000
      val f = 60
      val T = (1.0/f) * 1000
      if (T-h > 0)
        Thread.sleep((T-h).toLong)
      _fps = (1.0/((System.nanoTime - t0) / 1000000000.0)).toInt
     //println((1.0/((System.nanoTime - t0) / 1000000000.0)).toInt)
    }
    println("Physics actor exited.")
  }
}

case object Start
case object Stop
case object Exit
case class SetWorld(world: World)
case object FireEvent


