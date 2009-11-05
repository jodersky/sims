/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.collision

import geometry._

/**
 * Axis Aligned Bounding Boxes, kurz AABBs, sind Rechtecke die eine bestimmte Form umhuellen.
 * Da AABBs nach den X- und Y-Achsen orientiert sind, ermoeglichen sie eine schnelle
 * und einfache Feststellung ob zwei AABBs sich ueberschneiden.
 * @param minVertex Ortsvektor der minimalen Ecke des AABBs
 * @param maxVertex Ortsvektor der maximalen Ecke des AABBs
 */
case class AABB(val minVertex: Vector2D,
                val maxVertex: Vector2D)
{ 
  /**
   * Ueberprueft ob dieses AABB sich mit dem AABB <code>box</code> ueberschneidet.
   * @param box das mit diesem auf Ueberschneidung zu ueberpruefende AABB*/
  def overlaps(box: AABB): Boolean = {
    val d1 = box.minVertex - maxVertex
    val d2 = minVertex - box.maxVertex
    !(d1.x > 0 || d1.y > 0 || d2.x > 0 || d2.y > 0)
  }
}
