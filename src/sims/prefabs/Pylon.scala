/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.prefabs

import sims.geometry._
import sims.dynamics._
import sims.dynamics.joints._

class Pylon extends Prefab{
  val position: Vector2D = Vector2D(2,1)
  val nodeDensity: Double = 100
  val beamHeight: Double = 1
  val beamWidth: Double = 0.5
  val beamNumber: Int = 10
  
  private val nodeRow1 = (for (i <- 0 to beamNumber) yield (new Circle(0.01, nodeDensity) {
    pos = position + Vector2D(0, i * beamHeight)}).asBody).toList
  private val nodeRow2 = (for (i <- 0 to beamNumber) yield (new Circle(0.01, nodeDensity) {
    pos = position + Vector2D(beamWidth, i * beamHeight)}).asBody).toList
  
  private val beamRow1 = (for (i <- 0 until nodeRow1.length - 1) yield
  	new DistanceJoint(nodeRow1(i), nodeRow1(i).pos, nodeRow1(i+1), nodeRow1(i + 1).pos)).toList
  private val beamRow2 = (for (i <- 0 until nodeRow1.length - 1) yield
  	new DistanceJoint(nodeRow2(i), nodeRow2(i).pos, nodeRow2(i+1), nodeRow2(i + 1).pos)).toList
  
  private val latBeams = (for (i <- 0 to beamNumber) yield 
  	new DistanceJoint(nodeRow1(i), nodeRow2(i))).toList
  private val diagBeams1 = (for (i <- 0 until beamNumber) yield
    new DistanceJoint(nodeRow1(i), nodeRow2(i + 1))).toList
  private val diagBeams2 = (for (i <- 0 until beamNumber) yield
    new DistanceJoint(nodeRow2(i), nodeRow1(i + 1))).toList
  
  
  
  
  lazy val nodes = nodeRow1 ++ nodeRow2
  lazy val beams = beamRow1 ++ beamRow2 ++ latBeams ++ diagBeams1 ++ diagBeams2
  
  override val bodies = nodes
  override val joints = beams
  
  
}
