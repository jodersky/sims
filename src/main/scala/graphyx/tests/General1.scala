/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.tests

import sims._
import sims.geometry._
import sims.collision._
import sims.dynamics._
import sims.dynamics.joints._
object General1 extends Test{
  override val title = "General1"
  val world = new World
  
  def init() = {
    val b0 = new Circle(0.1,1).asBody
    b0.fixed = true
    val b1 = (new Circle(0.1,1) {pos = Vector2D(0,1)}).asBody
    //b0.linearVelocity = Vector2D(0,0.4)
    b0.monitor = true
    b1.monitor = true
    val circles = for (i <- (0 until 10).toList) yield (new Circle(0.1,1) {pos = Vector2D(0, 1.2 + 0.2 * i)}).asBody
    //for (c <- circles) world += c
    //world.gravity = Vector2D.Null
   // world += b0 
    //world += b1
    
    world.monitors += ("Veclocity = ", _.linearVelocity.length.toString)
    
    val b2 = (new Rectangle(0.1,0.15,1) {pos = Vector2D(1,0)}).asBody
    b2.fixed = true
    b2.rotation = 2
    val b3 = (new Circle(0.1,1) {pos = Vector2D(1,1.1)}).asBody
    //world += b2
    //world += b3
    
    val b4 = (new Rectangle(0.1,0.15,1) {pos = Vector2D(2,0)}).asBody
    b4.fixed = true
    b4.rotation = 2
    val b5 = (new Rectangle(0.1,0.15,1) {pos = Vector2D(2,1.2)}).asBody
    //world += b4
    //world += b5
    
    val bn = (new RegularPolygon(5,0.1,1) {pos = Vector2D(3,0)}).asBody
    //world += bn
    
    //Make cannon and balls
    val hull = new Body(
      new Rectangle(0.1,0.5,1) {pos = Vector2D(0.1, 0.5)},
      new Rectangle(0.1,0.5,1) {pos = Vector2D(0.5, 0.5)},
      new Rectangle(0.1,0.1,1) {pos = Vector2D(0.3, 0.1)}
    )
    hull.fixed = true
    hull.rotation -= scala.math.Pi / 4
    
    val ball = new Body(
      new Circle(0.1,20) {pos = Vector2D(0.3, 0.3)}
    )
    val ball2 = new Body(
      new Circle(0.1,20) {pos = Vector2D(0.3, 0.5)}
    )
    world += hull
    world += ball
    world += ball2
    
    //Swing
    val anchor = (new Circle(0.05,1) {pos = Vector2D(25,10)}).asBody
    anchor.fixed = true
    val block = (new RegularPolygon(9,0.5,2) {pos = Vector2D(25,1)}).asBody
    val joint = new DistanceJoint(anchor, block)
    world += anchor
    world += block
    world += joint    

    //Make stack
    val stack = for (i <- (0 until 10).toList) yield (new Circle(0.1,1) {pos = Vector2D(30, 0.2 + 0.2 * i)}).asBody
    for (e <- stack) world += e
    
    
    /*
    val springBoardHull = new Body(
      new Rectangle(0.1,0.5,1) {pos = Vector2D(-1.1, 0.5)},
      new Rectangle(0.1,0.5,1) {pos = Vector2D(-1.5, 0.5)},
      new Rectangle(0.1,0.1,1) {pos = Vector2D(-1.3, 0.1)}
    )
    springBoardHull.fixed = true
    world += springBoardHull
    
    val springBoard = new Body(
      new Circle(0.1,20) {pos = Vector2D(-1.3, 0.5)}
    )
    world += springBoard
    
    val spring = new SpringJoint(springBoardHull, Vector2D(-1.3, 0.1), springBoard, springBoard.pos, 2000)
    spring.damping = 10
    world += spring
    */
   
    val ground = (new Rectangle(1000,0.5,1) {pos = Vector2D(0, -0.4)}).asBody
    ground.fixed = true
    world += ground
    
    world += (new Circle(0.1,1) {pos = Vector2D(2,2)}) ~ (new Circle(0.1,1) {pos = Vector2D(2,2.2)}) 
  }
  
  enableEvent = true
  override def fireEvent() = blastBomb
  
  def blastBomb() = {
    val bombPos = Vector2D(0, 0)
    val bombRadius = 1
    val bombImpulse = 10
    val region = new Circle(bombRadius, 0) {pos = bombPos}
    val detector = world.detector.asInstanceOf[GridDetector]
    val collisions = for (s <- world.shapes; if detector.colliding(collision.Pair(region, s))) yield detector.collision(collision.Pair(region, s))
    for (c <- collisions) {
      if (c.shape1 != region)
        for(p <- c.points) c.shape1.body.applyImpulse((p - bombPos).unit * bombImpulse, p)
      if (c.shape2 != region)
        for(p <- c.points) c.shape2.body.applyImpulse((p - bombPos).unit * bombImpulse, p)
    }
  }
}
