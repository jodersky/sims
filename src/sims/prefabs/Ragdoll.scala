/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.prefabs

import sims.dynamics._
import sims.dynamics.joints._
import sims.geometry._

class Ragdoll(position: Vector2D) extends Prefab {
  val headTorso = new Body(new Circle(0.1, 1) {pos = position},
                           new Rectangle(0.09, 0.35, 1) {pos = position + Vector2D(0, -0.45)})
  val thigh1 = (new Rectangle(0.075, 0.17, 1) {pos = position + Vector2D(0, -0.97)}).asBody
  val thigh2 = (new Rectangle(0.075, 0.17, 1) {pos = position + Vector2D(0, -0.97)}).asBody
  val tibia1 = (new Rectangle(0.075, 0.25, 1) {pos = position + Vector2D(0, -1.39)}).asBody
  val tibia2 = (new Rectangle(0.075, 0.25, 1) {pos = position + Vector2D(0, -1.39)}).asBody
  val foot1 = (new Circle(0.08, 1) {pos = position + Vector2D(0, -1.72)}).asBody
  val foot2 = (new Circle(0.08, 1) {pos = position + Vector2D(0, -1.72)}).asBody
  val upperArm1 = (new Rectangle(0.17, 0.06, 1) {pos = position + Vector2D(0.17, -0.16)}).asBody
  val upperArm2 = (new Rectangle(0.17, 0.06, 1) {pos = position + Vector2D(-0.17, -0.16)}).asBody
  val forearm1 = (new Rectangle(0.15, 0.06, 1) {pos = position + Vector2D(0.49, -0.16)}).asBody
  val forearm2 = (new Rectangle(0.15, 0.06, 1) {pos = position + Vector2D(-0.49, -0.16)}).asBody
  val hand1 = (new Circle(0.07, 1) {pos = position + Vector2D(0.71, -0.16)}).asBody
  val hand2 = (new Circle(0.07, 1) {pos = position + Vector2D(-0.71, -0.16)}).asBody
  
  override val bodies = List(headTorso,
                             thigh1, thigh2,
                             tibia1, tibia2,
                             foot1, foot2,
                             upperArm1, upperArm2,
                             forearm1, forearm2,
                             hand1, hand2)
  private val shapes = bodies.flatMap(_.shapes)
  for (s <- shapes) s.transientShapes ++= shapes
  
  val shoulder1 = RevoluteJoint(headTorso, upperArm1, position + Vector2D(0, -0.16))
  val shoulder2 = RevoluteJoint(headTorso, upperArm2, position + Vector2D(0, -0.16))
  override val joints = List(shoulder1, shoulder2)

}
