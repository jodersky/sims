/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.graphics

import sims.dynamics._
case class GraphicalWorld(real: World){
  val time = real.time
  val timeStep = real.timeStep
  val iterations = real.iterations
  val overCWarning = real.overCWarning
  val gravity = real.gravity
  val monitorResults = for (m <- real.monitors) yield
    new MonitorResult(m,
                      for (b <- real.bodies.toList; if (b.monitor)) yield (b.uid, m._1, m._2(b))
    )
  val monitorFlatResults = for (b <- real.bodies; m <- real.monitors; if (b.monitor)) yield (b.uid, m._1, m._2(b))
  val enableCollisionDetection = real.enableCollisionDetection
  val enablePositionCorrection = real.enablePositionCorrection
}

class MonitorResult (
  val monitor: (String, Body => Any),
  val results: List[(Int, String, Any)]
)
