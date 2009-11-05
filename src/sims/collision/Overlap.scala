/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.collision

import sims.geometry._

case class Overlap(poly: ConvexPolygon, sideNum: Int, overlap: Double)
