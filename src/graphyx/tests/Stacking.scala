/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.tests

import sims.dynamics._
import sims.geometry._

object Stacking extends Test{
  val world = new World
  val title = "Stacking"
  def init(): Unit = {
    val sideWidth = 0.3
    val sideHeight = 0.2
    val boxes = 5
    val distance = 0.1
    val stack = for (i <- (0 until boxes).toList) yield (new Rectangle(sideWidth / (2 + 0.3 * i), sideHeight / 2, 1) {pos = Vector2D(1, i * (sideHeight + distance))}).asBody
    stack(0).fixed = true
    for (box <- stack) world += box
  }
}
