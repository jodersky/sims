/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.collision

import sims.geometry._
import sims.dynamics._
import scala.collection.mutable.Map
import scala.collection.mutable._

/**Kollision zwischen zwei konvexen Polygonen.*/
case class PolyCollision(p1: ConvexPolygon, p2: ConvexPolygon) extends Collision {
  require(p1.isInstanceOf[Shape])
  require(p2.isInstanceOf[Shape])
  
  def overlap(axis: Vector2D) = {
   // println((p1.project(axis) overlap p2.project(axis)).toString + "	to	" +  (p2.project(axis) overlap p1.project(axis)))
    p1.project(axis) overlap p2.project(axis)
  }
  
  lazy val overlaps = (for (i <- 0 until p2.sides.length) yield Overlap(p2, i, overlap(p2.sides(i).n0))) ++ 
    			 (for (i <- 0 until p1.sides.length) yield Overlap(p1, i, overlap(p1.sides(i).n0)))
  
  private var potMinOverlap = overlaps.find(_.overlap > 0.0)
  require(potMinOverlap != None)
  private var _minOverlap: Overlap = potMinOverlap.get
  var minOverlap: Overlap = { 
    for (o <- overlaps) if ((o.overlap < _minOverlap.overlap) && (o.overlap > 0.0)) _minOverlap = o
    _minOverlap
  }
  
  
  private lazy val refPoly = minOverlap.poly
  private lazy val incPoly = if (minOverlap.poly eq p1) p2 else p1
  
  lazy val shape1 = refPoly.asInstanceOf[Shape]
  lazy val shape2 = incPoly.asInstanceOf[Shape]
  
  lazy val normal = refPoly.sides(minOverlap.sideNum).n0
  lazy val points = (for (v <- incPoly.vertices; if refPoly.contains(v)) yield v) ++
    (for (s <- incPoly.sides;
    val clip = s.clipToSegment(refPoly.sides((refPoly.sides.length - (minOverlap.sideNum + 1)) % refPoly.sides.length));
    if (clip != None)) yield clip.get) ++ 
    (for (s <- incPoly.sides;
    val clip = s.clipToSegment(refPoly.sides((refPoly.sides.length - (minOverlap.sideNum - 1)) % refPoly.sides.length));
    if (clip != None)) yield clip.get)
}
