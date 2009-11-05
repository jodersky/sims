/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.geometry

import scala.Math._

/**Ein 2-dimensionaler Vektor.
 * @param x 1. Komponente
 * @param y 2. Komponente*/
case class Vector2D(x: Double, y: Double) {
    
  /**Vektoraddition.
   * @param v zu addierender Vektor
   * @return dieser Vektor addiert mit <code>v</code>*/
  def +(v: Vector2D): Vector2D = Vector2D(x + v.x, y + v.y)
  
  /**Vektorsubstraktion.
   * @param v zu substrahierender Vektor
   * @return dieser Vektor substrahiert mit <code>v</code>*/
  def -(v: Vector2D): Vector2D = this + (v * -1)
  
  /**Multiplikation mit einem Skalar.
   * @param n Faktor
   * @return dieser Vektor multipliziert mit <code>n</code>*/
  def *(n: Double): Vector2D = Vector2D(x * n, y * n)
  
  /**Division durch ein Skalar.
   * @param n Nenner
   * @return dieser Vektor dividiert durch <code>n</code>*/
  def /(n: Double): Vector2D = this * (1/n)
  
  /**Minusvorzeichen.*/
  def unary_- : Vector2D = Vector2D(-x, -y)
  
  /**Skalarprodukt.
   * @param v ein anderer Vektor
   * @return Skalarprodukt von diesem Vektor mit <code>v</code>*/
  def dot(v: Vector2D): Double = x * v.x + y * v.y
  
  /**Kreuzprodukt. (Norm des Kreuzproduktes)
   * @param v ein anderer Vektor
   * @return Norm des Kreuzproduktes dieses Vektors mit <code>v</code>. Die Richtung wuerde der x3-Achse entsprechen.*/
  def cross(v: Vector2D): Double = x * v.y - y * v.x
  
  /**Norm dieses Vektors.*/
  val length: Double = Math.sqrt(x * x + y * y)
  
  /**Einheitsvektor dieses Vektors.*/
  def unit: Vector2D = if (!(x == 0.0 && y == 0.0)) Vector2D(x / length, y / length) 
    else throw new IllegalArgumentException("Null vector does not have a unit vector.")
  
  /**Errechnet die Projektion dieses- auf einen anderen Vektor.
   * @param v oben gennanter Vektor
   * @return Projektion dieses Vektors auf <code>v</code>*/
  def project(v: Vector2D): Vector2D = {
    if (v != Vector2D.Null)
      v * ((this dot v) / (v dot v))
    else
      Vector2D.Null
    }
  
  /**Errechnet eine Rotation dieses Vektors.
   * @param angle Winkel in Radian
   * @return der um <code>angle</code> rad rotierte Vektor*/
  def rotate(angle: Double): Vector2D = {
    Vector2D(cos(angle) * x - sin(angle) * y,
             cos(angle) * y + sin(angle) * x)
  }
  
  /**Linker Normalenvektor. (-y, x)*/
  def leftNormal: Vector2D = Vector2D(-y, x)
  
  /**Rechter Normalenvektor. (y, -x)*/
  def rightNormal: Vector2D = Vector2D(y, -x)
  
  /**Ueberprueft, ob die Komponenten dieses Vektors gleich Null sind.*/
  def isNull: Boolean = this == Vector2D.Null
  
  /**Ergibt eine Liste der Komponenten dieses Vektors.*/
  def components = List(x, y)
}

/**Dieses Objekt enthaelt spezielle Vektoren.*/
object Vector2D {
  
  /**Nullvektor.*/
  val Null = Vector2D(0,0)
  
  /**Ein horizontaler Einheitsvektor mit den Komponenten (1;0).*/
  val i = Vector2D(1,0)
  
  /**Ein vertikaler Einheitsvektor mit den Komponenten (0;1).*/
  val j = Vector2D(0,1)
}

