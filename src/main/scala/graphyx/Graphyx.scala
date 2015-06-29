/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx

import akka.actor._
import graphyx.actors._
import graphyx.graphics._
import graphyx.tests._
import sims.geometry._
import sims.dynamics._

object Graphyx{
  val tests: List[graphyx.tests.Test] = List(
    CompositeShape,
    Joints1,
    Joints2,
    Spring,
    Atom,
    Chain,
    Wave,
    Net,
    Stacking,
    BallStack,
    Cup,
    Friction,
    Friction2,
    Restitution,
    RagdollTest,
    Carriage,
    General1,
    General2,
    Cannon,
    EmptyTest
  )
  
  private var _test: graphyx.tests.Test = tests(0)
  def test = _test
  def test_=(t: graphyx.tests.Test) = {
    t.world.time = 0
    t.world.clear()
    t.init()
    physicsActor ! SetWorld(t.world)
    _test = t
  }

  val system = ActorSystem()
  val physicsActor = system.actorOf(PhysicsActor(), "physics")
  val guiActor = system.actorOf(GUIActor(), "gui")

  def main(args: Array[String]): Unit = {
    test = tests(0)
  }
  
  def exit() = {
    system.shutdown()
  }
}
