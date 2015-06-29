/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.actors

import akka.actor._
import graphyx._
import graphyx.graphics._
import scala.concurrent.duration._
import sims.dynamics._
class PhysicsActor extends Actor{
  import context._

  var world = new World
  
  var simulate = false
  
  private var _fps = 0
  def fps = _fps
  var t0 = System.nanoTime

  override def preStart() = {
    println("Physics actor started.")
    t0 = System.nanoTime
  }

  def receive = {
    case Tick =>
      t0 = System.nanoTime
      if (simulate) {
        world.step()
      }
      Graphyx.guiActor ! new Scene(world) {override val fps = _fps}
      val h = (System.nanoTime - t0) / 1000000
      val f = 60
      val T = (1.0/f) * 1000
      _fps = (1.0/((System.nanoTime - t0) / 1000000000.0)).toInt
      //println((1.0/((System.nanoTime - t0) / 1000000000.0)).toInt)

      val delay = if (T-h>0) (T-h).toLong else 0l

      if (simulate) {
        system.scheduler.scheduleOnce(delay.milliseconds, self, Tick)
      }

    case Step =>
      world.step()
      Graphyx.guiActor ! new Scene(world)

    case Stop => {
      simulate = false
      println("Simulation stopped.")
    }
    case Start => {
      simulate = true
      self ! Tick
      println("Simulation started.")
    }
  
    case sw @ SetWorld(w: World) => world = w

    case FireEvent => Graphyx.test.fireEvent()

    case other => println("Engine received unknown command: '" + other + "'")
  }

  override def postStop() = {
    println("Physics actor exited.")
  }
}

object PhysicsActor {

  def apply() = Props(classOf[PhysicsActor])

}

case object Tick
case object Start
case object Stop
case object Step
case class SetWorld(world: World)
case object FireEvent


