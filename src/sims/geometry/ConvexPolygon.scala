/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.geometry

import sims.collision._
import sims.geometry._

/**Gemeinsame Eigenschaften aller konvexen Polygone.*/
trait ConvexPolygon {
  
  /**Ergibt Position aller Ecken dieses Polygons. Die Ecken sind gegen den Uhrzeigersinn folgend.
   * @return Ortsvektoren der Ecken*/
  def vertices: Seq[Vector2D]
  
  /**Ergibt alle Seiten dieses Polygons.
   * @return Seiten dieses Polygons*/
  def sides = (for (i <- 0 until vertices.length) yield (new Segment(vertices(i), vertices((i + 1) % vertices.length)))).toArray
  
  /**Ergibt die Projektion dieses Polygons auf eine Gerade gegeben durch den
   * Richtungsvektor <code>axis</code>
   * @param axis Richtungsvektor der Geraden
   * @return Projektion dieses Polygons*/
  def project(axis: Vector2D) = {
    val points = for (v <- vertices) yield {v project axis}
    val bounds = for (p <- points) yield {if (axis.x != 0) p.x / axis.x else p.y / axis.y}
    Projection(axis,
              (bounds(0) /: bounds)(Math.min(_,_)),
              (bounds(0) /: bounds)(Math.max(_,_)))
  }
  
  /**Errechnet das AABB dieses Polygons
   * @return umfassendes AABB
   * @see collision.AABB*/
  def AABB = {
    val xs = vertices map (_.x)
    val ys = vertices map (_.y)
    new AABB(Vector2D(Iterable.min(xs), Iterable.min(ys)),
             Vector2D(Iterable.max(xs), Iterable.max(ys)))
  }
  
  /**Ueberprueft ob sich der gegebene Punkt <code>point</code> in diesem Polygon befindet.
   * <p>
   * Hierzu wird eine Halbgerade von dem Punkt in Richtung der X-Achse gezogen (koennte aber auch beliebig sein).
   * Dann wird die Anzahl der Ueberschneidungen der Halbgeraden mit den Seiten und Ecken des Polygons ermittelt.
   * Ist die Anzahl der Ueberschneidungen ungerade, so befindet sich der Punkt in dem Polygon.
   * Es gibt jedoch Ausnahmen, und zwar wenn die Halbgerade eine Ecke ueberschneidet, ueberschneidet sie sowohl auch zwei Seiten.
   * Daher wird eine generelle Anzahl von Uerberschneidungen errechnet, gegeben durch die Anzahl der Ueberschneidungen mit den Seiten minus
   * die mit den Ecken.
   * Diese Zahl wird dann wie oben geschildert geprueft.*/
  def contains(point: Vector2D) = {
    val r = new Ray(point, Vector2D.i)
    var intersections = 0
    for (s <- sides; if (r intersects s)) intersections += 1
    for (v <- vertices; if (r contains v)) intersections -= 1
    intersections % 2 != 0
  }
}
