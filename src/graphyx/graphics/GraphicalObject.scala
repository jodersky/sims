/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.graphics

/**Only copies functional info! (e.g. Graphical world does not include shapes, bodies).*/
trait GraphicalObject extends Drawable{
  
  /**Pointer to real object.*/
  val real: AnyRef
  def draw(): Unit
}
