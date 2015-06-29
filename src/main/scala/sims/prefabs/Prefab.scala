/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.prefabs

import sims.dynamics._
import sims.dynamics.joints._

trait Prefab {
  val bodies: Iterable[Body] = Nil
  val joints: Iterable[Joint] = Nil
}
