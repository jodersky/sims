/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.collision

import sims.dynamics._
import sims.geometry._
import scala.collection._
import scala.collection.mutable._

/**Eine konkrete Implementierung von <code>Detector</code>. <code>GridDetector</code> ermittelt
 * alle Kollisionen mit einem Gittersystem.*/
class GridDetector(override val world: World) extends Detector {
  
  /**Array von Kollisionserkennungsmethoden fuer Formenpaare.*/
  val detectionMethods = new ArrayBuffer[PartialFunction[(Shape, Shape), Boolean]]
  detectionMethods += {
    case (c1: Circle, c2: Circle) => {	//Kollision wenn Distanz <= Summe der Radien
      val d = (c1.pos - c2.pos).length
      val rSum = c1.radius + c2.radius
      d - rSum <= 0
      }
    
    case (p1: ConvexPolygon, p2: ConvexPolygon) => {	//SAT
      val sides = p1.sides ++ p2.sides
      val axes = sides map (_.n0)
      axes.forall((a: Vector2D) => p1.project(a) overlaps p2.project(a))
    }
    
    case (p: ConvexPolygon, c: Circle) => {	//Distanz von Zentrum zu Seiten oder Eckpunkten
      val distances = for (s <- p.sides) yield (s distance c.pos)
      distances.exists(_ - c.radius <= 0) || (p contains c.pos)
      }
    
    case (c: Circle, p: ConvexPolygon) => {	//Distanz von Zentrum zu Seiten oder Eckpunkten
      val distances = for (s <- p.sides) yield (s distance c.pos)
      distances.exists(_ - c.radius <= 0) || (p contains c.pos)
      }
  }
  
   /**Array von Kollisionsmethoden fuer Formenpaare.*/
  val collisionMethods = new ArrayBuffer[PartialFunction[(Shape, Shape), Collision]]
  collisionMethods += {
    case (c1: Circle, c2: Circle) => CircleCollision(c1, c2)
    case (p1: ConvexPolygon, p2: ConvexPolygon) => PolyCollision(p1, p2)
    case (p: ConvexPolygon, c: Circle) => PolyCircleCollision(p, c)
    case (c: Circle, p: ConvexPolygon) => PolyCircleCollision(p, c)
  }
  
  /**Gibt an, ob das Formenpaar <code>p</code> kollidiert.
   * @param p Formenpaar.*/
  def colliding(p: Pair) = {
   if (detectionMethods.exists(_.isDefinedAt(p))) 
     detectionMethods.find(_.isDefinedAt(p)).get.apply(p)
    else throw new IllegalArgumentException("No collision method for colliding pair!")
  }
  
  /**Gibt die Kollision des Formenpaares <code>p</code> zurueck.
   * @param p Formenpaar.*/
  def collision(p: Pair): Collision = {
    if (collisionMethods.exists(_.isDefinedAt(p)))
      collisionMethods.find(_.isDefinedAt(p)).get.apply(p)
    else throw new IllegalArgumentException("No collision found in colliding pair!")
  }
  
  /**Breite und Hoehe einer Gitterzelle.*/
  var gridSide: Double = 2
  
  /**Ergibt potenzielle Kollisionspaare der Welt <code>world</code>.
   * <p>
   * Ein Kollisionspaar ist ein Paar aus zwei verschiedenen Formen, das folgenden Bedingungen unterliegt:
   * <ul>
   * <li>Die Formen muessen sich in der gleichen Gitterzelle befinden.</li>
   * <li>Ihre AABBs muessen sich ueberlappen.</li>
   * <li>Die Formen duerfen nicht von dem gleichen Koerper sein.</li>
   * <li>Mindestens eine Form darf nicht Fixiert sein.</li>
   * <li>Beide muessen {@link dynamics.Shape#collidable collidierbar} sein.</li>
   * </ul>*/
  def getPairs = {
    val grid = new HashMap[(Int, Int), List[Shape]]
    def gridCoordinates(v: Vector2D) = ((v.x / gridSide).toInt, (v.y / gridSide).toInt)
    def addToGrid(s: Shape) = {
      val aabb = s.AABB  
      val minCell = gridCoordinates(aabb.minVertex)
      val maxCell = gridCoordinates(aabb.maxVertex)
      val coords = for(i <- (minCell._1 to maxCell._1); j <- (minCell._2 to maxCell._2)) yield (i, j)
      for (c <- coords) {
        if (grid.contains(c))
          {if (grid(c).forall(_ ne s)) grid(c) = s :: grid(c)}
        else
            grid += (c -> List(s))
      	}
      }
    for(s <- world.shapes) addToGrid(s)
    var ps: List[Pair] = Nil
    for(cell <- grid.values) {
      ps = ps ::: (for (s1: Shape <- cell; s2: Shape <- cell;
                    if (s1 ne s2);                       
           			if (s1.body ne s2.body);
           			if (s1.collidable && s2.collidable);
                    if (s1.AABB overlaps s2.AABB);
                    if (!s1.transientShapes.contains(s2) && !s2.transientShapes.contains(s1))) yield Pair(s1, s2)
       				).removeDuplicates
    }
    ps.toSeq
  }
  
  private var cache = (world.time, getPairs)
  
  /**Alle potentiellen Kollisionspaare der Welt.
   * @see getPairs*/
  def pairs = {if (world.time != cache._1) cache = (world.time, getPairs); cache._2}
  
  /**Ergibt alle kollidierenden Paare.*/
  def collidingPairs: Seq[Pair] = for(p <- pairs; if (colliding(p))) yield p
  
  /**Ergibt alle Kollisionen.*/
  def collisions: Seq[Collision] = for(p <- pairs; if (colliding(p))) yield collision(p)
}
