/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.geometry

import sims.math._

/**Projektion auf eine Achse.
 * <p>
 * Ueblicherweise werden Projektionen in SiMS fuer Kollisionserkennung benutzt.
 * @param axis Achse der Projektion
 * @param lower unterer Wert der Projektion
 * @param upper oberer Wert der Projektion*/
case class Projection(axis: Vector2D,
                      lower: Double,
                      upper: Double) {
  require(axis != Vector2D.Null)
  
  /**Ueberprueft ob sich diese Projektion mit der Projektion <code>other</code> ueberschneidet.*/
  def overlaps(other: Projection): Boolean = {
    require(axis == other.axis, "Cannot compare two projections on different axes!")
      !((other.lower - this.upper) > 0 || (this.lower - other.upper) > 0)
  }
  
  
  /**Ergibt die Ueberlappung dieser Projektion und der Projektion <code>other</code>.*/
  def overlap(other: Projection): Double = {
    require(axis == other.axis, "Cannot compare two projections on different axes!")
        (Math.max(lower, other.lower) - Math.min(upper, other.upper)).abs
  }
}
