package org.oxbow

import scala.xml.NodeSeq
package object squirm {

	implicit def bool2attr( condition: Boolean ) = new  {
	        def attr(trueValue: String): String = if (condition) trueValue else null
	        def attr: String = attr("true")
	}
	
	implicit def xml2cmpt( xml: NodeSeq ) =  Markup(xml)
	implicit def str2cmpt( text: String ) =  Markup(<h2><center>{text}</center></h2>)

}