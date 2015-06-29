/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.collision

import sims.geometry._

/**
 * Axis Aligned Bounding Boxes (AABBs) are rectangles that frame a shape.
 * Their X-Axis and Y-Axis orientation makes it easy to test two AABBs for overlap.
 * @param minVertex Position vector of the bottom-left vertex
 * @param maxVertex Position vector of the upper-right vertex
 */
case class AABB(val minVertex: Vector2D,
                val maxVertex: Vector2D)
{ 
  /**
   * Checks this AABB with <code>box</code> for overlap.
   * @param box AABB with which to check for overlap*/
  def overlaps(box: AABB): Boolean = {
    val d1 = box.minVertex - maxVertex
    val d2 = minVertex - box.maxVertex
    !(d1.x > 0 || d1.y > 0 || d2.x > 0 || d2.y > 0)
  }
}
