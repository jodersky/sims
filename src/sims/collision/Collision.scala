/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.collision

import dynamics._
import geometry._

/**Collision between two shapes. Contains methods to compute the collision response.*/
abstract class Collision extends Constraint {
  
  /**First colliding shape (reference shape).*/
  val shape1: Shape
  
  /**Second colliding shape (incident shape).*/
  val shape2: Shape
  
  /**Collision points.*/
  val points: Iterable[Vector2D]
  
  /**Normal vector to the collision face.*/
  val normal: Vector2D
  
  /* C = delta
   * Cdot = (vp2 - vp1) dot n
   * = v2 + (w2 cross r2) - v2 - (w1 cross r1)
   * = v2 + (w2 cross (p - x2)) - v2 - (w1 cross(p - x1))
   * J = [-n	-((p-x1) cross n)	n	((p-x2) cross n)]*/
  def correctVelocity(h: Double) = {
    val coefficientOfRestitution = shape1.restitution * shape2.restitution
    for (p <- points) {
      val b1 = shape1.body
      val b2 = shape2.body
      val relativeNormalVelocity = (b2.velocityOfPoint(p) - b1.velocityOfPoint(p)) dot normal
      val Cdot = relativeNormalVelocity + relativeNormalVelocity * coefficientOfRestitution 
      if (Cdot <= 0) {
        val r1 = p - b1.pos
        val r2 = p - b2.pos
        val cr1 = r1 cross normal
        val cr2 = r2 cross normal
        val invMass = 1/b1.mass * (normal dot normal) + 1/b1.I * cr1 * cr1 + 1/b2.mass * (normal dot normal) + 1/b2.I * cr2 * cr2
        val m = if (invMass == 0.0) 0.0 else 1/invMass
        val lambda = -m * Cdot
        //wenn fixed, dann ist Masse unendlich => kein 'if (fixed != true)'
        b1.linearVelocity += -normal * lambda / b1.mass
        b1.angularVelocity += -(r1 cross normal) * lambda / b1.I  
        b2.linearVelocity += normal * lambda / b2.mass
        b2.angularVelocity += (r2 cross normal) * lambda / b2.I
        correctFriction(p, (-normal * lambda).length / h, h)
      }
    }
  }
  
  /* Cdot = vt = [v2 + (w2 cross r2) - v1 - (w2 cross r2)] dot t
   * J = [-t	-(r2 cross t)	t	(r1 cross t)]
   * 1/m = J * M * JT
   * = 1/m1 * (t dot t) + 1/m2 * (t dot t) + 1/I1 * (r1 cross u)^2 + 1/I2 * (r2 cross u)^2*/
  def correctFriction(point: Vector2D, normalForce: Double, h: Double) = {
    val b1 = shape1.body
    val b2 = shape2.body
    val tangent = normal.leftNormal
    val Cdot = (b2.velocityOfPoint(point) - b1.velocityOfPoint(point)) dot tangent 
    val r1 = point - b1.pos
    val r2 = point - b2.pos
    val cr1 = r1 cross tangent
    val cr2 = r2 cross tangent
    val invMass = 1/b1.mass * (tangent dot tangent) + 1/b1.I * cr1 * cr1 + 1/b2.mass * (tangent dot tangent) + 1/b2.I * cr2 * cr2
    val m = if (invMass == 0.0) 0.0 else 1/invMass
    val lambda = -m * Cdot
    val cf = shape1.friction * shape2.friction
    val cl = Math.min(Math.max(-normalForce * cf * h, lambda), normalForce * cf * h)
    val impulse = tangent * cl
    b1.applyImpulse(-impulse, point)
    b2.applyImpulse(impulse, point)
  }
  
  def correctPosition(h: Double) = {
    val b1 = shape1.body
    val b2 = shape2.body
    
    for (p <- points) {
      val overlap = shape1.project(normal) overlap shape2.project(normal)
      val C = Collision.ToleratedOverlap - overlap
      if (C <= 0.0) {
        val r1 = p - b1.pos
        val r2 = p - b2.pos
        val cr1 = r1 cross normal
        val cr2 = r2 cross normal
        val invMass = 1/b1.mass + 1/b1.I * cr1 * cr1 + 1/b2.mass + 1/b2.I * cr2 * cr2
        val m = if (invMass == 0.0) 0.0 else 1/invMass
        val impulse = -normal.unit * m * C
        b1.pos += -impulse / b1.mass
        b1.rotation += -(r1 cross impulse) / b1.I
        b2.pos += impulse / b2.mass
        b2.rotation += (r2 cross impulse) / b2.I
      }
    }
  }
}

object Collision {
  
  /**Tolerated overlap. Collision response will only be applied if the overlap of two shapes exceeds the tolerated overlap.*/
  val ToleratedOverlap: Double = 0.01
}
