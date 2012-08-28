package org.oxbow

package object squirm {

	implicit def bool2attr( condition: Boolean ) = new  {
	        def attr(trueValue: String): String = if (condition) trueValue else null
	        def attr: String = attr("true")
	}

}