/*
 * Graphyx
 * copyright (c) 2009 Jakob Odersky
 * made available under the MIT License
*/

package graphyx.gui

import graphyx.graphics._
import sims.geometry._
import sims.dynamics._
import scala.swing._
import scala.swing.event._
import scala.collection.mutable.Map
import scala.collection.mutable.Queue

class WorldPanel(container: Container) extends BoxPanel(Orientation.Vertical){
  cursor = new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR)
  val lblBody = new Label {text = "None @ (0, 0)"}
  contents += lblBody
  val popup = new BodyPopup
  contents += popup
  
  implicit def point2Vector(p: java.awt.Point) = {
    val x = p.x
    val y = size.height - p.y
    new Vector2D((x - offset.x) / scale / ppm, (y - offset.y) / scale / ppm)
  }
  
  private val ppi = java.awt.Toolkit.getDefaultToolkit.getScreenResolution
  val ppm = 39.37007874 * ppi
  var scale = 0.02
  var offset = Vector2D(100, 100) //vector for point coordinates [px]
  
  def scene: Scene = container.scene
  
  def update() = {
    repaint()
  }
  
  var drawBodies = false
  var drawShapes = true
  var drawJoints = true
  var drawAABBs = false
  var drawPairs = false
  var drawCollisions = false
  var trace = false
  
  override def paintComponent(g: java.awt.Graphics2D) = {
    var parts: Seq[Drawable] = Seq()
    if (drawShapes) parts ++= scene.shapes
    if (drawJoints) parts ++= scene.joints
    if (drawAABBs) parts ++= scene.aabbs
    if (drawPairs) parts ++= scene.pairs
    if (drawCollisions) parts ++= scene.collisions
    if (drawBodies) parts ++= scene.bodies
    g.clearRect(0,0,size.width,size.height)
    drawAxes(g)
    g.translate(offset.x.toInt, -offset.y.toInt)
    drawParts(parts, g)
    if (trace) trace(scene.shapes, g)
    g.translate(-offset.x.toInt, offset.y.toInt)
  }
  
  def drawAxes(g: java.awt.Graphics2D): Unit = {
    g.setColor(java.awt.Color.GRAY)
    g.drawLine(0, size.height - offset.y.toInt, size.width, size.height - offset.y.toInt)
    g.drawLine(offset.x.toInt, 0, offset.x.toInt, size.height)
    /*
    val md = scale * ppm
    val hb = size.width / md
    for (i <- 1 to hb.toInt) g.drawLine(offset.x.toInt + i * md.toInt, size.height - offset.y.toInt,
                                        offset.x.toInt + i * md.toInt, size.height - offset.y.toInt + 10)
    */
  }
    
  def drawParts(parts: Iterable[Drawable], g: java.awt.Graphics2D) = {
     for (p <- parts){
      p.g = g
      p.windowHeight = super.size.height
      p.ppm = ppm
      p.scale = this.scale
      p.draw()
    }
  }
  
  val prevPos: Map[Int, Queue[Vector2D]] = Map()
  def trace(shapes: Iterable[GraphicalShape], g: java.awt.Graphics2D) = {
    for (s <- shapes) {
      s.g = g
      s.windowHeight = super.size.height
      s.ppm = ppm
      s.scale = this.scale
      
      
      if (!prevPos.contains(s.uid)) prevPos += (s.uid -> new Queue[Vector2D])
      else {
        prevPos(s.uid).enqueue(s.pos)
        for(i <- 0 until prevPos(s.uid).length - 1) {
          val sp = prevPos(s.uid)(i)
          val ep = prevPos(s.uid)(i + 1)
          s.g.setColor(java.awt.Color.cyan)
          s.drawLine(sp, ep)
        }
        if (prevPos(s.uid).length == 50) prevPos(s.uid).dequeue
      }
    }
  }
  
  def getBody(p: Vector2D): Option[Body] = {
      val shape = scene.shapes.find(_.contains(p))
      if (shape != None) Some(shape.get.real.body)
      else None
  }
  
  var mousePressed: Boolean = false
  var startPoint = new java.awt.Point(0,0)
  var endPoint = new java.awt.Point(0,0)
  var grabbedBody: Option[GrabbedBody] = None
  def grab(b: Body, p: Vector2D) = {
    grabbedBody = Some(new GrabbedBody(b, p))
    b.fixed = true
  }
  
  def release() = {
    if (grabbedBody != None && grabbedBody.get.wasFixed == false)
      grabbedBody.get.body.fixed = false
    grabbedBody = None
  }
  
  listenTo(Mouse.clicks, Mouse.moves, Mouse.wheel)
  reactions += {
    case MousePressed(c,p,x,y,b) => {
      mousePressed = true; startPoint = p; endPoint = p
      x match {
        case 1024 if (getBody(p) != None) => grab(getBody(p).get, p)
        case 1152 if (getBody(p) != None) => {grabbedBody = Some(new GrabbedBody(getBody(p).get, p)); popup.body = grabbedBody.get.body; popup.peer.setLocation(p); popup.visible = true}
        case 4096 if (getBody(p) != None) => {grabbedBody = Some(new GrabbedBody(getBody(p).get, p)); popup.body = grabbedBody.get.body; popup.peer.setLocation(p); popup.visible = true}
        case _ => ()
      }
    }
                                                                         
    case MouseMoved(c,p,i) => {
      lblBody.text = (if (getBody(p) != None) getBody(p).get.uid.toString else "None") + " @ (" + point2Vector(p).x.formatted("%f") + ", " + point2Vector(p).y.formatted("%f") + ")"
    }
    
    case MouseDragged(c,p,1088) => {//drag with shift
      offset += Vector2D((p.x - endPoint.x), -(p.y - endPoint.y))
      mousePressed = true
      endPoint = p
      cursor = new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR)
    }
    
    case MouseDragged(c,p,x) => 
      if (grabbedBody != None) grabbedBody.get.body.pos = p - grabbedBody.get.r 
    
    case MouseReleased(c,p,x,y,b) => {
        mousePressed = false
        endPoint = p
        cursor = new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR)
        release()
    }
    
    case MouseWheelMoved(c,p,1024,y) => { //with left mouse button pressed
      if (grabbedBody != None) grabbedBody.get.body.rotation += 0.05 * y 
    }
    
    case MouseWheelMoved(c,p,x,y) => {
      scale -= scale * 0.02 * y
    }
    
  }
}

class GrabbedBody(b: Body, point: Vector2D){
  def body = b
  val r: Vector2D = point - body.pos
  val wasFixed = b.fixed
}
