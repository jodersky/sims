/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.collision

import sims.dynamics._

/**Formenpaar.*/
case class Pair(s1: Shape, s2: Shape) extends Tuple2(s1, s2){
  def this(t: Tuple2[Shape, Shape]) = this(t._1, t._2)
  
  override def equals(other: Any) = { //overriden to prevent removal during "GridDetector.getPairs" 
    other match {
      case Pair(a, b) => ((a eq this.s1) && (b eq this.s2)) || ((b eq this.s1) && (a eq this.s2))  
      case _ => false
    }
  }
}
