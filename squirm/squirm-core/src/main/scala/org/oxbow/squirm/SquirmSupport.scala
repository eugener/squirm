package org.oxbow.squirm

import org.scalatra.ScalatraBase
import scala.xml.NodeSeq
import org.oxbow.squirm._

/**
 * Support for component rendering in Scalatra 
 * Usage: add the trait to your Scalatra servlet
 */
trait SquirmSupport extends ScalatraBase {

   override def renderResponse(actionResult: Any) = actionResult match {
     case c: Component => super.renderResponse( c.render )
     case cc: Iterable[Component] => cc.foldLeft(NodeSeq.Empty)( _ ++ _.render )
     case _ => super.renderResponse(actionResult)
   }
    
   def confirm( ok: String = "OK", cancelUrl: String )( components:Component* ): Component = {
        
        val comps = components.toSeq :+ 
                    SubmitButton( ok, icon = "check") :+
                    Button( "Cancel", link = cancelUrl, icon = "delete", back=true )
        
        
        new Page( title = "Confirmation", header = "Confirmation" )(
             Form( action = url(request.pathInfo) )(
                 comps: _*    
	             //SubmitButton( ok, icon = "check"),   
	             //Button( "Cancel", link = url( cancelUrl ), icon = "delete", back=true )
             )
        )
    }    
   
   
}