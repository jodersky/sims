/*
 * Simple Mechanics Simulator (SiMS)
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package sims.collision

import sims.dynamics._
import sims.geometry._
import scala.collection._
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap

/**A conrete implementation of <code>Detector</code>. <code>GridDetector</code> divides the world into a grid
 * for faster collision detection.*/
class GridDetector(override val world: World) extends Detector {
  
  /**Array of collision detection methods. These methods return <code>true</code> if two shapes are colliding.*/
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
  
   /**Array of methods returning collisions. It is assumed that both shapes are colliding.*/
  val collisionMethods = new ArrayBuffer[PartialFunction[(Shape, Shape), Collision]]
  collisionMethods += {
    case (c1: Circle, c2: Circle) => CircleCollision(c1, c2)
    case (p1: ConvexPolygon, p2: ConvexPolygon) => PolyCollision(p1, p2)
    case (p: ConvexPolygon, c: Circle) => PolyCircleCollision(p, c)
    case (c: Circle, p: ConvexPolygon) => PolyCircleCollision(p, c)
  }
  
  /**Checks the pair of shapes <code>p</code> for collision.
   * @param p Pair of shapes.*/
  def colliding(p: Pair) = {
   if (detectionMethods.exists(_.isDefinedAt(p))) 
     detectionMethods.find(_.isDefinedAt(p)).get.apply(p)
    else throw new IllegalArgumentException("No collision method for colliding pair!")
  }
  
  /**Returns the collision between both shapes of the pair <code>p</code>.
   * @param p Pair of shapes.*/
  def collision(p: Pair): Collision = {
    if (collisionMethods.exists(_.isDefinedAt(p)))
      collisionMethods.find(_.isDefinedAt(p)).get.apply(p)
    else throw new IllegalArgumentException("No collision found in colliding pair!")
  }
  
  /**Width and height of a grid cell.*/
  var gridSide: Double = 2
  
  /**Returns potential colliding pairs of shapes of the world <code>world</code>.
   * <p>
   * A potential colliding pair is a pair of two shapes that comply with the following criteria:
   * <ul>
   * <li>The shapes are situated in the same grid cell.</li>
   * <li>Their AABBs overlap.</li>
   * <li>The shapes do not belong to the same body.</li>
   * <li>At least one shape is not fixed.</li>
   * <li>Both shapes are {@link dynamics.Shape#collidable}.</li>
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
  
  /**All potential colliding pairs of the world.
   * @see getPairs*/
  def pairs = {if (world.time != cache._1) cache = (world.time, getPairs); cache._2}
  
  /**Returns all colliding pairs.*/
  def collidingPairs: Seq[Pair] = for(p <- pairs; if (colliding(p))) yield p
  
  /**Returns all collisions.*/
  def collisions: Seq[Collision] = for(p <- pairs; if (colliding(p))) yield collision(p)
}
