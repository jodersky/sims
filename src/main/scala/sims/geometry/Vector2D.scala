/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.geometry

import scala.math._

/**A 2D vector.
 * @param x 1st component
 * @param y 2nd component*/
case class Vector2D(x: Double, y: Double) {
    
  /**Vector addition.*/
  def +(v: Vector2D): Vector2D = Vector2D(x + v.x, y + v.y)
  
  /**Vector substraction.*/
  def -(v: Vector2D): Vector2D = this + (v * -1)
  
  /**Scalar multiplication.*/
  def *(n: Double): Vector2D = Vector2D(x * n, y * n)
  
  /**Scalar division.*/
  def /(n: Double): Vector2D = this * (1/n)
  
  /**Unary minus.*/
  def unary_- : Vector2D = Vector2D(-x, -y)
  
  /**Dot product.*/
  def dot(v: Vector2D): Double = x * v.x + y * v.y
  
  /**Cross product. Length only because in 2D. The direction would be given by the x3-axis.*/
  def cross(v: Vector2D): Double = x * v.y - y * v.x
  
  /**Norm or length of this vector.*/
  val length: Double = math.sqrt(x * x + y * y)
  
  /**Unit vector.*/
  def unit: Vector2D = if (!(x == 0.0 && y == 0.0)) Vector2D(x / length, y / length) 
    else throw new IllegalArgumentException("Null vector does not have a unit vector.")
  
  /**Returns the projection of this vector onto the vector <code>v</code>.*/
  def project(v: Vector2D): Vector2D = {
    if (v != Vector2D.Null)
      v * ((this dot v) / (v dot v))
    else
      Vector2D.Null
    }
  
  /**Returns a rotation of this vector by <code>angle</code> radian.*/
  def rotate(angle: Double): Vector2D = {
    Vector2D(cos(angle) * x - sin(angle) * y,
             cos(angle) * y + sin(angle) * x)
  }
  
  /**Left normal vector. (-y, x)*/
  def leftNormal: Vector2D = Vector2D(-y, x)
  
  /**Right normal vector. (y, -x)*/
  def rightNormal: Vector2D = Vector2D(y, -x)
  
  /**Checks if this vector is the null vector.*/
  def isNull: Boolean = this == Vector2D.Null
  
  /**Returns a list of this vector's components.*/
  def components = List(x, y)
}

/**Contains special vectors.*/
object Vector2D {
  
  /**Null vector.*/
  val Null = Vector2D(0,0)
  
  /**Horizontal unit vector. (1,0)*/
  val i = Vector2D(1,0)
  
  /**Vertical unit vector. (0,1)*/
  val j = Vector2D(0,1)
}

