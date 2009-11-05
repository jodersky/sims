/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.math

import sims.geometry._

/**Eine 2x2, quadratische Matrix.
 * @param c11 Komponente 1,1
 * @param c12 Komponente 1,2
 * @param c21 Komponente 2,1
 * @param c22 Komponente 2,2
 */
case class Matrix22(c11: Double, c12: Double, c21: Double, c22: Double) {
  
  /**Eine 2x2-dimensionale, quadratische Matrix kann auch mit zwei 2-dimensionalen
   * Vektoren erstellt werden. In diesem Fall repraesentiert jeder Vektor eine Spalte.
   * @param c1 erste Spalte
   * @param c2 zweite Spalte*/
  def this(c1: Vector2D, c2: Vector2D) = this(c1.x, c2.x, c1.y, c2.y)
  
  /**Ergibt die Determinante dieser Matrix.
   * @return Determinante dieser Matrix*/
  def det = c11 * c22 - c21 * c12
  
  /**Addition.*/
  def +(m: Matrix22) =
    new Matrix22(c11 + m.c11, c12 + m.c12,
                 c21 + m.c21, c22 + m.c22)
  
  /**Multiplikation mit einem Skalar.*/
  def *(n: Double) = 
    new Matrix22(c11 * n, c12 * n,
                 c21 * n, c22 * n)
  
  /**Multiplikation mit einer anderen 2x2-Matrix.*/
  def *(m: Matrix22) = 
    new Matrix22(c11 * m.c11 + c12 * m.c21, c11 * m.c12 + c12 * m.c22,
                 c21 * m.c11 + c22 * m.c21, c21 * m.c12 + c22 * m.c22)
  
  /**Multiplikation mit einer 2x1-Matrix (2-dimensionaler Vektor).*/
  def *(v: Vector2D) =
    new Vector2D(c11 * v.x + c12 * v.y,
                 c21 * v.x + c22 * v.y)
  
  /**Inverse.*/
  def invert = 
    new Matrix22(c22 / det, -c12 / det,
                 -c21 / det, c11 / det)
  }

object Matrix22 {
  def apply(c1: Vector2D, c2: Vector2D) = new Matrix22(c1.x, c2.x, c1.y, c2.y)
}
