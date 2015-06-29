/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.math

import sims.geometry._

/**A 2x2 matrix.
 * @param c11 component 1,1
 * @param c12 component 1,2
 * @param c21 component 2,1
 * @param c22 component 2,2
 */
case class Matrix22(c11: Double, c12: Double, c21: Double, c22: Double) {
  
  /**A 2x2 matrix can be created with two 2D vectors. In this case, each column is represented by a vector.
   * @param c1 first column
   * @param c2 second column*/
  def this(c1: Vector2D, c2: Vector2D) = this(c1.x, c2.x, c1.y, c2.y)
  
  /**Determinant of this matrix.*/
  def det = c11 * c22 - c21 * c12
  
  /**Addition.*/
  def +(m: Matrix22) =
    new Matrix22(c11 + m.c11, c12 + m.c12,
                 c21 + m.c21, c22 + m.c22)
  
  /**Scalar multiplication.*/
  def *(n: Double) = 
    new Matrix22(c11 * n, c12 * n,
                 c21 * n, c22 * n)
  
  /**Matrix multiplication.*/
  def *(m: Matrix22) = 
    new Matrix22(c11 * m.c11 + c12 * m.c21, c11 * m.c12 + c12 * m.c22,
                 c21 * m.c11 + c22 * m.c21, c21 * m.c12 + c22 * m.c22)
  
  /**Multiplikation with a 2D vector.*/
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
