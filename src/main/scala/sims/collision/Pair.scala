/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.collision

import scala.language.implicitConversions

import sims.dynamics._

/**Pair of shapes.*/
case class Pair(s1: Shape, s2: Shape){
  
  override def equals(other: Any) = { //overriden to prevent removal during "GridDetector.getPairs" 
    other match {
      case Pair(a, b) => ((a eq this.s1) && (b eq this.s2)) || ((b eq this.s1) && (a eq this.s2))  
      case _ => false
    }
  }
}

object Pair {
	implicit def pair2Tuple(x: Pair) = (x.s1, x.s2)
}
