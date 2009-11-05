/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics.joints

import sims.geometry._
import sims.dynamics._

/**Joints sind Verbindungen die die Bewegung zwischen zwei Koerpern einschraenken.
 * Ihre Implementierung wurde von Erin Catto's box2d inspiriert.*/
abstract class Joint extends Constraint{
  
  /**Erster Koerper der Verbindung.*/
  val node1: Body
  
  /**Zweiter Koerper der Verbindung.*/
  val node2: Body
  
  /**Korrigiert die Geschwindigkeit der Koerper damit diese den Randbedingungen der Verbindung entsprechen.*/
  def correctVelocity(h: Double): Unit
  
  /**Korrigiert die Position der Koerper damit diese den Randbedingungen der Verbindung entsprechen.*/
  def correctPosition(h: Double): Unit
}