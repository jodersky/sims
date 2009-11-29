/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.geometry

import sims.math._

/**Projection on an axis.
 * <p>
 * Projections are commonly used in SiMS for collision detection.
 * @param axis directional vector of the axis of the projection
 * @param lower lower value of the projection
 * @param upper upper value of the projection*/
case class Projection(axis: Vector2D,
                      lower: Double,
                      upper: Double) {
  require(axis != Vector2D.Null, "A projection's axis cannot be given by a null vector!")
  
  /**Checks this projection for overlap with another projection <code>other</code>.
   * @throws IllegalArgumentExcepion if both projections axes aren't the same*/
  def overlaps(other: Projection): Boolean = {
    require(axis == other.axis, "Cannot compare two projections on different axes!")
      !((other.lower - this.upper) > 0 || (this.lower - other.upper) > 0)
  }
  
  /**Returns the overlap between this projection and another projection <code>other</code>.
  * @throws IllegalArgumentExcepion if both projections axes aren't the same*/
  def overlap(other: Projection): Double = {
    require(axis == other.axis, "Cannot compare two projections on different axes!")
        (Math.max(lower, other.lower) - Math.min(upper, other.upper)).abs
  }
}
