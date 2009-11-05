/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.prefabs

import sims.geometry._
import sims.dynamics._
import sims.dynamics.joints._

class Net(width: Int, height: Int, initPos: Vector2D) extends Prefab {
  val nodeDistance: Double = 0.2
  val nodeRadius: Double = 0.05
  val nodeDensity: Double = 4
  
  val springConstant: Double = 50
  val springDamping: Double = 0
  
  private val connectors: Array[Array[Body]] = 
    makeConnectors(width, height)
  
  override val bodies: List[Body] = for (row <- connectors.toList; elem <- row) yield elem
  override val joints = connect(connectors)
  
  private def makeConnectors(w: Int, h: Int): Array[Array[Body]] = {
    for(i <- (0 until w).toArray[Int]) yield
      for(j <- (0 until h).toArray[Int]) yield
        new Body(new Circle(nodeRadius, nodeDensity) {pos = Vector2D(nodeDistance * i, nodeDistance * j) + initPos})
  }
  
  private def connect(connectors: Array[Array[Body]]): List[DistanceJoint] = {
    var r: List[DistanceJoint] = Nil
    for(i <- 0 to connectors.length - 1; j <- 0 to connectors(i).length - 1) {
      if (i > 0)
        r = connect(connectors(i-1)(j),connectors(i)(j)) :: r
      if (j > 0)
        r = connect(connectors(i)(j-1),connectors(i)(j)) :: r
    }
    r
  }
  
  private def connect(s1: Body, s2: Body): DistanceJoint = 
    new DistanceJoint(s1, s1.pos, s2, s2.pos)
  
}