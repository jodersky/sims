/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.dynamics

/**Randbedingungen erben von dem Trait <code>Constraint</code>.
 * Fuer jeden Constraint koennen Position und Geschwindigkeit korrigiert werden.
 * Ihre Implementierung wurde von Erin Catto's box2d inspiriert.*/
trait Constraint {
  
  /**Korrigiert die Geschwindigkeit der Koerper damit diese den Randbedingungen entsprechen.*/
  def correctVelocity(h: Double): Unit
  
  /**Korrigiert die Position der Koerper damit diese den Randbedingungen entsprechen.*/
  def correctPosition(h: Double): Unit
}
