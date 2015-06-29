/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics

import sims.geometry._
import sims.collision._

/**
* An abstract shape.
*/
abstract class Shape{
  
  /**Unique identification number.*/
  val uid: Int = Shape.nextUid
  
  /**Flag determining this shapes ability to collide with other shapes.*/
  var collidable: Boolean = true
  
  /**Part of the coefficient of restitution for a collision between this shape and another.
   * The coefficient of restitution is calculated out of the product of this part and the other shape's part.*/
  var restitution: Double = 0.7
  
  /**Part of the coefficient of friction for a collision between this shape and another.
   * The coefficient of friction is calculated out of the product of this part and the other shape's part.*/
  var friction: Double = 0.707
  
  /**Position of this shape's COM (in world coordinates).*/
  var pos: Vector2D = Vector2D.Null
  
  /**Rotation of this shape about its COM.*/
  var rotation: Double = 0
  
  /**Initial rotation. Rotation of this shape before it was added to a body.*/
  var rotation0 = 0.0
  
  /**Local position of this shape's body COM to its COM at a body rotation of zero.*/
  var refLocalPos: Vector2D = Vector2D.Null
  
  /**Density. (Mass per area)*/
  val density: Double
  
  /**Volume. The volume is actually equivalent to this shape's area (SiMS is in 2D)
   * and is used with this shape's density to calculate its mass.*/
  val volume: Double
  
  /**Returns the mass of this shape. The mass is given by volume times density.
   @return mass of this shape*/
  def mass = volume * density
  
  /**Moment of inertia for a rotation about this shape's COM.*/
  val I: Double
  
  /**Containing body.*/
  private var _body: Body = _
  
  /**Returns this shape's containing body.*/
  def body = _body
  
  /**Sets this shape's containing body.*/
  private[dynamics] def body_=(b: Body) = _body = b
  
  /**Returns this shape's axis aligned bounding box.*/
  def AABB: AABB
  
  /**Returns the projection of this shape onto the line given by the directional vector <code>axis</code>.
   * @param axis directional vector of the line
   * @return projection of this shape*/
  def project(axis: Vector2D): Projection
  
  /**Checks if the point <code>point</code> is contained in this shape.*/
  def contains(point: Vector2D): Boolean
  
  /**Creates a new body made out of tis shape.
   @return a body made out of tis shape*/
  def asBody = new Body(this)
  
  /**Shapes with which this shape cannot collide.*/
  val transientShapes: collection.mutable.Set[Shape] = collection.mutable.Set()
  
  /**Creates a new body out of this shape and the shape <code>s</code>.*/
  def ~(s: Shape) = new Body(this, s)
  
  /**Creates a new body out of this shape and the shapes of body <code>b</code>.*/
  def ~(b: Body) = {
    val shapes = this :: b.shapes
    new Body(shapes: _*)
  }
}

object Shape {
  private var uidCounter = -1
  private def nextUid = {uidCounter += 1; uidCounter}
}
