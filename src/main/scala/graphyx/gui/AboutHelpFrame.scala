package graphyx.gui

import graphyx.actors._
import graphyx.gui._
import scala.swing._
import scala.swing.event._

class AboutHelpFrame extends Frame {
  title = "About"
  contents = new TextArea(
     """|Graphyx, testing and visualization tool for SiMS.
    	|
     	|copyright (c) 2009 Jakob Odersky
     	|SiMS and Graphyx are made available under the MIT License
     	|
     	|https://github.com/jodersky/sims
     	|
     	|Version 1.2""".stripMargin
  ) {editable = false}
}

object AboutHelpFrame {
  val frame = new AboutHelpFrame
}
