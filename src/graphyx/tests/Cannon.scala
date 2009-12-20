package graphyx.tests

import sims.dynamics._
import sims.geometry._
import sims.prefabs._

object Cannon extends Test{
  val title = "Cannon"
  val world = new World
  
  def init = { 
    val cannon = new Prefab{
      val hull = new Body(
      new Rectangle(0.1,0.5,1) {pos = Vector2D(0.1, 0.6); friction = 0.0},
      new Rectangle(0.1,0.5,1) {pos = Vector2D(0.5, 0.6); friction = 0.0},
      new Rectangle(0.1,0.1,1) {pos = Vector2D(0.3, 0.2); friction = 0.0}
      )
      hull.fixed = true
      hull.rotation -= Math.Pi / 2
    
      val ball = new Body(new Circle(0.1,100) {pos = Vector2D(0.3, 0.56)})
    
      override val bodies = List(hull, ball)
      override val joints = Nil
    }
    val n = 10
    val r = 0.1
    val initialPos = Vector2D(5,0)
    val stack = for (i <- (0 to n).toList) yield {
      for (j <- (0 to n-i).toList) yield
        new Body(
          new Circle(r,10){
            pos = Vector2D(2 * j * r + i * r, 1.7 * i * r) + initialPos
          }
        ) {fixed = (i == 0)}
    }
    world += cannon
    world ++= stack.flatten
    
  }
  
  enableEvent = true
  override def fireEvent() = blastBomb
  
  def blastBomb() = {
    val bombPos = Vector2D(0, 0.56)
    val bombRadius = 1
    val bombImpulse = 200
    val region = new Circle(bombRadius, 0) {pos = bombPos}
    val detector = world.detector.asInstanceOf[sims.collision.GridDetector]
    val collisions = for (s <- world.shapes; if detector.colliding(sims.collision.Pair(region, s))) yield detector.collision(sims.collision.Pair(region, s))
    for (c <- collisions) {
      if (c.shape1 != region)
        for(p <- c.points) c.shape1.body.applyImpulse((p - bombPos).unit * bombImpulse, p)
      if (c.shape2 != region)
        for(p <- c.points) c.shape2.body.applyImpulse((p - bombPos).unit * bombImpulse, p)
    }
  }
}
