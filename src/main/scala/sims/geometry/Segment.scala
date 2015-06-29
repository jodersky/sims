/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.geometry

/**A segment is given by its vertices.
 * @param vertex1 position vector of the first vertex
 * @param vertex2 position vector of the second vertex
 * @throws IllegalArgumentException if both vertices are equal
 */
case class Segment(vertex1: Vector2D, vertex2: Vector2D){
  require(vertex1 != vertex2, "A segment must have 2 distinct vertices!")
  
  /**Length of this segment.*/
  val length = (vertex2 - vertex1).length
  
  /**Vector from <code>vertex1</code> to <code>vertex2</code>.*/
  val d = vertex2 - vertex1
  
  /**Unit directional vector.*/
  val d0 = d.unit
  
  /**Right normal vector.*/
  val n = d.rightNormal
  
  /**Right normal unit vector.*/
  val n0 = n.unit
  
  /**Smallest distance between this segment and the point <code>point</code>.*/
  def distance(point: Vector2D): Double = {
    val v = point - vertex1	//vector from vertex1 to point
    val projection = v project d
    val alpha = if (d.x != 0) d.x / projection.x else d.y / projection.y
    if (alpha >= 0 && projection.length <= length)	//point is closer to line between vertex1 and vertex2
      (v project n0).length
    else if (alpha < 0)	//point is closer to vertex1
      (point - vertex1).length
    else if (alpha > 0)	//point is closer to vertex2
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
