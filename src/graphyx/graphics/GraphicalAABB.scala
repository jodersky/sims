/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.graphics

import sims.collision._
class GraphicalAABB(val real: AABB) extends AABB(real.minVertex, real.maxVertex) with GraphicalObject {
   override def draw() = {
    g.setColor(java.awt.Color.BLACK)
    g.drawRect((minVertex.x * scale * ppm).toInt,
               correctY(maxVertex.y * scale * ppm).toInt,
               ((maxVertex - minVertex).x * scale * ppm).toInt,
               ((maxVertex - minVertex).y * scale * ppm).toInt)
  }
}
