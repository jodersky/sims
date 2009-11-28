/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.graphics

import sims.dynamics._
trait GraphicalShape extends Shape with GraphicalObject{
  val real:  Shape
  override val uid: Int = real.uid
  pos = real.pos
  rotation = real.rotation
}