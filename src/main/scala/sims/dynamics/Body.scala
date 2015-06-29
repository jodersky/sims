
/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics

import sims.geometry._
import sims.dynamics.joints._

/**A two dimensional rigid body is made out of shapes.
 * @param shps shapes that belong to this body.*/
class Body(shps: Shape*){
  
  /**Unique identification number.*/
  val uid = Body.nextUid
  
  /**Shapes that belong to this body.*/
  val shapes: List[Shape] = shps.toList
  
  //Shapes are added during initialisation.
  for (s <- shapes) {
    s.body = this
    s.refLocalPos = s.pos - pos
    s.rotation0 = s.rotation
  }
  
  private var isFixed: Boolean = false
  
  /**Returns whether this body is fixed or not.*/
  def fixed = isFixed
  
  /**Fixes or frees this body. By fixing, linear and angular velocities are set to zero.*/
  def fixed_=(value: Boolean) = {
    if (value) {linearVelocity = Vector2D.Null; angularVelocity = 0.0}
    isFixed = value
  } 
  
  /**Flag for a world to monitor the properties of this body.
   * @see World#monitors*/
  var monitor: Boolean = false
  
  /**Returns the position of this body. The position is equivalent to the center of mass.
   * @return position of this body*/
  def pos: Vector2D = // COM = sum(pos*mass)/M
    (Vector2D.Null /: shapes)((v: Vector2D, s: Shape) => v + s.pos * s.mass) /
    (0.0 /: shapes)((i: Double, s: Shape) => i + s.mass)
  
  /**Sets the position of this body. By doing so all its shapes are translated.
   * @param newPos new position*/
  def pos_=(newPos: Vector2D) = {
    val stepPos = pos
    shapes.foreach((s: Shape) => s.pos = s.pos - stepPos + newPos)
  }
  
  /**Contains the current rotation of this body.*/
  private var _rotation: Double = 0.0
  
  /**Returns the current rotation of this body.*/
  def rotation: Double = _rotation
  
  /**Sets the rotation of this body. Position and rotation of shapes are modified accordingly.
   * @param r new rotation*/
  def rotation_=(newRotation: Double) = {
    _rotation = newRotation
    val stepPos = pos
    for (s <- shapes) {
      s.rotation = newRotation + s.rotation0
      s.pos = stepPos + (s.refLocalPos rotate (newRotation))
    }  
  }
  
  /**Linear velocity of this body.*/
  var linearVelocity: Vector2D = Vector2D.Null
  
  /**Angular velocity of this body.*/
  var angularVelocity: Double = 0
  
  /**Linear velocity of the given point on this body (in world coordinates).*/
  def velocityOfPoint(point: Vector2D) = linearVelocity + ((point - pos).leftNormal * angularVelocity)
  
  /**Resulting force on the COM of this body.*/
  var force: Vector2D = Vector2D.Null
  
  /**Resulting torque on this body.*/
  var torque: Double = 0
  
  /**Returns the mass of this body. If the body is free, its mass is the sum of the masses of its shapes.
   * If the body is fixed, its mass is infinite (<code>Double.PositiveInfinity</code>).
   * @return this body's mass*/
  def mass: Double = if (fixed) Double.PositiveInfinity else (0.0 /: shapes)((i: Double, s: Shape) => i + s.mass)
  
  /**Returns the moment of inertia for rotations about the COM of this body.
   * It is calculated using the moments of inertia of this body's shapes and the parallel axis theorem.
   * If the body is fixed, its moment of inertia is infinite (<code>Double.PositiveInfinity</code>).
   * @return moment of inertia for rotations about the COM of this body*/
  def I: Double = if (fixed) Double.PositiveInfinity else
    (0.0 /: (for (s <- shapes) yield (s.I + s.mass * ((s.pos - pos) dot (s.pos - pos)))))(_+_)
  
  /**Applies a force to the COM of this body.
   * @param force applied force*/
  def applyForce(force: Vector2D) = if (!fixed) this.force += force
  
  /**Applies a force to a point on this body. Warning: the point is considered to be contained within this body.
   * @param force applied force
   * @param point position vector of the point (in world coordinates)*/
  def applyForce(force: Vector2D, point: Vector2D) = if (!fixed) {this.force += force; torque += (point - pos) cross force}
  
  /**Applies an impulse to the COM of this body.
   * @param impulse applied impulse*/  
  def applyImpulse(impulse: Vector2D) = if (!fixed) linearVelocity += impulse / mass
  
  /**Applies an impulse to a point on this body. Warning: the point is considered to be contained within this body.
   * @param impulse applied impulse
   * @param point position vector of the point (in world coordinates)*/
  def applyImpulse(impulse: Vector2D, point: Vector2D) = if (!fixed) {linearVelocity += impulse / mass; angularVelocity += ((point - pos) cross impulse) / I}
  
  /**Checks if the point <code>point</code> is contained in this body.*/
  def contains(point: Vector2D) = shapes.exists(_.contains(point))
  
  override def toString: String = {
    "Body" + uid + "	" + shapes + "	fixed=" + fixed + "	m=" + mass + "	I=" + I + 	"	pos=" + pos + "	rot=" + rotation + "	v=" + linearVelocity + "	w=" + angularVelocity + "	F=" + force + "	tau=" + torque
  }
  
  /**Creates a new body containing this body's shapes and the shape <code>s</code>.
   * @param s new shape
   * @return new body*/
  def ~(s: Shape) = new Body((s :: shapes): _*)
  
  /**Creates a new body containing this body's shapes and the shapes of another body <code>b</code>.
   * @param b body with extra shapes
   * @return new body*/
  def ~(b: Body) = new Body((this.shapes ::: b.shapes): _*)
}

object Body {
  private var uidCounter = -1
  private def nextUid = {uidCounter += 1; uidCounter}
}