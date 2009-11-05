/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.collision


import sims.geometry._
import sims.dynamics._
import scala.collection._
import scala.collection.mutable._

/**Eine Welt ermittelt ihre Kollisionen durch konkrete Implementierungen dieser Klasse.*/
abstract class Detector {
  
  /**Die Welt dessen Formen auf Kollisionen ueberprueft werden sollen.*/
  val world: World
  
  /**Ergibt alle Kollisionen zwischen Formen der Welt <code>world</code>.*/
  def collisions: Seq[Collision]
}