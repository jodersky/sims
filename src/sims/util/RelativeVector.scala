/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.util

import sims.geometry._
import sims.dynamics._

class RelativeVector(val x: Double, val y: Double) {
  def above(point: Vector2D): Vector2D = point + Vector2D(0, x)
  def below(point: Vector2D): Vector2D = point - Vector2D(0, x)
  def left(point: Vector2D): Vector2D = point - Vector2D(x, 0)
  def right(point: Vector2D): Vector2D = point + Vector2D(x, 0)
  def from(point: Vector2D): Vector2D = point + Vector2D(x, y)
  
  def above(s: Shape): Vector2D = this.above(s.pos)
  def below(s: Shape): Vector2D = this.below(s.pos)
  def left(s: Shape): Vector2D = this.left(s.pos)
  def right(s: Shape): Vector2D = this.right(s.pos)
  def from(s: Shape): Vector2D = this.from(s.pos)  
}
