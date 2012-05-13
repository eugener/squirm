package org.oxbow.squirm

import org.scalatra.ScalatraKernel
import scala.xml.NodeSeq

/**
 * Support for component rendering in Scalatra 
 * Usage: add the trait to your Scalatra servlet
 */
trait SquirmSupport extends ScalatraKernel {

   override def renderResponse(actionResult: Any) = actionResult match {
     case c: Component => super.renderResponse( c.render )
     case cc: Iterable[Component] => cc.foldLeft(NodeSeq.Empty)( _ ++ _.render )
     case _ => super.renderResponse(actionResult)
   }
    
}