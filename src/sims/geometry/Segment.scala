/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.geometry

/**Ein Segment wird durch seine beiden Extrempunkte gegeben.
 * @param vertex1 Ortsvektor des 1. Extrempunkts
 * @param vertex2 Ortsvektor des 2. Extrempunkts*/
case class Segment(vertex1: Vector2D, vertex2: Vector2D){
  require(vertex1 != vertex2, "A segment must have 2 distinct vertices!")
  
  /**Laenge dieses Segments.*/
  val length = (vertex2 - vertex1).length
  
  /**Vektor von EP1 zu EP2.*/
  val d = vertex2 - vertex1
  
  /**Einheitsrichtungsvektor.*/
  val d0 = d.unit
  
  /**Normalenvektor. Richtung: 90 Grad rechts zu d.*/
  val n = d.rightNormal
  
  /**Normaleneinheitsvektor. Richtung: 90 Grad rechts zu d.*/
  val n0 = n.unit
  
  /**Kleinster Abstand zwischen diesem Segment und dem Punkt <code>p</code>.*/
  def distance(point: Vector2D): Double = {
    val v = point - vertex1	//Vektor von EP1 zu point
    val projection = v project d
    val alpha = if (d.x != 0) d.x / projection.x else d.y / projection.y
    if (alpha >= 0 && projection.length <= length)	//Punkt ist naeher zu der Geraden zwischen EP1 und EP2
      (v project n0).length
    else if (alpha < 0)	//Punkt ist naeher zu EP1
      (point - vertex1).length
    else if (alpha > 0)	//Punkt ist naeher zu EP2
       (point - vertex2).length
       else
         throw new IllegalArgumentException("Error occured trying to compute distance between segment and point.")
  }
  
  def clipToSegment(s: Segment): Option[Vector2D] = {
    
    val distance1 = (vertex1 - s.vertex1) dot s.n0
    val distance2 = (vertex2 - s.vertex1)  dot s.n0
    
    if (distance1 * distance2 < 0) { //auf anderen Seiten
      /* Geradengleichungen
       * ==================
       * Segment1:	s1: x = a + alpha * r | alpha in [0,1]
       * Segment2:	s2: x = b + beta * s | beta in [0,1]
       * 
       * alpha = [s2(a1-b1)-s1(a2-b2)] / [r2s1-r1s2]
       * beta = [r2(b1-a1)-r1(b2-a2)] / [r1s2-r2s1]
       *      = [r1(b2-a2)]-r2(b1-a1) / [r2s1-r1s2]
       * s1: vertex1 + alpha * d
       * s2: s.vertex1 + beta * s.d
       */
      val denom: Double = d.y * s.d.x - d.x * s.d.y 
      val alpha: Double = (s.d.y * (vertex1.x - s.vertex1.x) - s.d.x * (vertex1.y - s.vertex1.y)) / denom
      val beta: Double = (d.x * (s.vertex1.y - vertex1.y) - d.y * (s.vertex1.x - vertex1.x)) / denom
      if (0.0 <= alpha && alpha <= 1.0 && 0.0 <= beta && beta <= 1.0) Some(vertex1 + d * alpha)
      else None
    }
    else None
  }
}
