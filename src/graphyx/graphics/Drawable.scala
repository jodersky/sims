/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.graphics

import sims.geometry._

/**Enthaelt Methoden und Felder fuer graphische Darstellungen.
 * Alle Klassen die dieses Trait implementieren koennen graphisch dargestellt werden.*/
trait Drawable {
  
  /**Java Graphics Objekt zur graphischen Darstellung*/
  var g: java.awt.Graphics = _
  
  /**Anzahl von Pixeln pro Meter.*/
  var ppm: Double = 39.37007874015748 * 96 //ppm = i/m * p/i
  
  /**Skala in der die graphischen Objekte gezeichnet werden.*/
  var scale: Double = 1.0/100.0
  
  /**Hoehe des Fensters in Pixeln.*/
  var windowHeight = 0
  
  /**Korrigiert einen Y-Wert in Bildschirmkoordinaten zu seinem kartesischen Aequivalent.
   * @param y zu korrigierender Wert*/
  def correctY(y: Double) = windowHeight - y
  
  /**Malt eine Linie auf <code>g</code>.
   * @param startPoint Startpunkt in Weltkoordinaten
   * @param endPoint Endpunkt in Weltkoordinaten*/
  def drawLine(startPoint: Vector2D, endPoint: Vector2D) = {
    val x1 = startPoint.x * scale * ppm
    val y1 = correctY(startPoint.y * scale * ppm)
    val x2 = endPoint.x * scale * ppm
    val y2 = correctY(endPoint.y * scale * ppm)
    g.drawLine(x1.toInt, y1.toInt, x2.toInt, y2.toInt)
  }
  
  /**Malt ein massives Polygon auf <code>g</code>.
   * @param points Eckpunkte des Polygons in Weltkoordinaten*/
  def fillPolygon(points: Seq[Vector2D]) = {
    val xs = points map ((v: Vector2D) => (v.x * scale * ppm).toInt)
    val ys = points map ((v: Vector2D) => correctY((v.y) * scale * ppm).toInt)
    g.fillPolygon(xs.toArray, ys.toArray, points.length)
  }
  
  /**Malt ein Polygon auf <code>g</code>.
   * @param points Eckpunkte des Polygons in Weltkoordinaten*/
  def drawPolygon(points: Seq[Vector2D]) = {
    val xs = points map ((v: Vector2D) => (v.x * scale * ppm).toInt)
    val ys = points map ((v: Vector2D) => correctY((v.y) * scale * ppm).toInt)
    g.drawPolygon(xs.toArray, ys.toArray, points.length)
  }
  
  /**Malt einen massiven Kreis auf <code>g</code>.
   * @param center Mitte des Kreises in Weltkoordinaten
   * @param radius Radius des Kreises*/
  def fillCircle(center: Vector2D, radius: Double) = {
    g.fillOval(((center.x - radius) * scale * ppm).toInt,
               correctY((center.y + radius) * scale * ppm).toInt,
               (radius * scale * ppm * 2).toInt,
               (radius * scale * ppm * 2).toInt)
  }
  
  /**Malt einen Kreis auf <code>g</code>.
   * @param center Mitte des Kreises in Weltkoordinaten
   * @param radius Radius des Kreises*/
  def drawCircle(center: Vector2D, radius: Double) = {
    g.drawOval(((center.x - radius) * scale * ppm).toInt,
               correctY((center.y + radius) * scale * ppm).toInt,
               (radius * scale * ppm * 2).toInt,
               (radius * scale * ppm * 2).toInt)
  }
  
  /**Malt einen Punkt auf <code>g</code>.
   * <p>
   * Der Punkt wird von einem Kreis umgeben.
   * @param point Punkt in Weltkoordinaten*/
  def drawPoint(point: Vector2D) = {
    val radius = 4 //in pixel
    g.drawLine((point.x * scale * ppm).toInt,
               correctY(point.y * scale * ppm).toInt - radius,
               (point.x * scale * ppm).toInt,
               correctY(point.y * scale * ppm).toInt + radius)
    g.drawLine((point.x * scale * ppm).toInt - radius,
               correctY(point.y * scale * ppm).toInt,
               (point.x * scale * ppm).toInt + radius,
               correctY(point.y * scale * ppm).toInt)
    g.drawOval((point.x * scale * ppm).toInt - radius,
               correctY(point.y * scale * ppm).toInt - radius,
               (radius * 2).toInt,
               (radius * 2).toInt)
    
  }
  
  
  /**Malt einen Vektor auf <code>g</code>.
   * @param v Vektor in Weltkoordinaten
   * @param p Ursprungspunkt in Weltkoordinaten
   */
  def drawVector(v: Vector2D, p: Vector2D) = {
    if (!v.isNull) {
     val ep = p + v
     val a1 = ep - ((v.unit rotate (Math.Pi / 6)) * 0.08)
     val a2 = ep - ((v.unit rotate (-Math.Pi / 6)) * 0.08)
     
     g.drawLine((p.x * scale * ppm).toInt, correctY(p.y * scale * ppm).toInt, (ep.x * scale * ppm).toInt, correctY(ep.y * scale * ppm).toInt)
     g.drawLine((a1.x * scale * ppm).toInt, correctY(a1.y * scale * ppm).toInt, (ep.x * scale * ppm).toInt, correctY(ep.y * scale * ppm).toInt)
     g.drawLine((a2.x * scale * ppm).toInt, correctY(a2.y * scale * ppm).toInt, (ep.x * scale * ppm).toInt, correctY(ep.y * scale * ppm).toInt) 
    }
  }
  
  /**Stellt das graphische Objekt dar.*/
  def draw(): Unit
}