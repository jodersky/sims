/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.collision

import sims.dynamics._
import sims.geometry._

/**Collision between a convex polygon and a circle.*/
case class PolyCircleCollision(p: ConvexPolygon, c: Circle) extends Collision {
  require(p.isInstanceOf[Shape])
  val shape1 = p.asInstanceOf[Shape]
  val shape2 = c
  
  val normal = {
    //minimum overlap
    var min = (p.sides(0) distance c.pos) - c.radius
    var axis = p.sides(0).n0
    for (s <- p.sides; val delta = (s distance c.pos) - c.radius) if (delta <= 0 && delta < min) {
      min = delta
      axis = s.n0
    }
    for (v <- p.vertices; val delta = (v - c.pos).length - c.radius) if (delta <= 0 && delta <= min){
      min = delta
      axis = (c.pos - v).unit
    }
    axis
  }
  
  val points = List(
    c.pos - normal * c.radius
  )
}
