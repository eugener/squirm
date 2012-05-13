package org.oxbow.squirm.test

import org.scalatra.ScalatraServlet
import org.oxbow.squirm.SquirmSupport
import org.scalatra.UrlSupport
import org.oxbow.squirm._

class SquirmTestServlet extends ScalatraServlet with UrlSupport with SquirmSupport {

    def contextPath = getServletContext.getContextPath
    
    get("/") {
        Page("Squirm Test", "ListView")(
            ListView( 
		       LinkListItem( "Collapsible Set", url("/collapsible") ),
		       LinkListItem( "Form", url("/form") )
		    )   
        )
    }
    
    get("/collapsible") {
        
        Page("Squirm Test", "Collapsble")(
	        CollapsibleSet(
	               Collapsible( "Collapsible", 
			            ListView( 
			               PropertyListItem( "name", "value" ),
			               PropertyListItem( "name", "value" ),
			               PropertyListItem( "name", "value" )
			            )
	               ),   
	               Collapsible( "Collapsible", 
			            ListView( 
			               PropertyListItem( "name", "value" ),
			               PropertyListItem( "name", "value" ),
			               PropertyListItem( "name", "value" )
			            )
	               ),    
	               Collapsible( "Collapsible", 
			            ListView( 
			               PropertyListItem( "name", "value" ),
			               PropertyListItem( "name", "value" ),
			               PropertyListItem( "name", "value" )
			            )
	               )    
             )
        )    
        
    }
    
    get("/form") {
        
    	Page("Squirm Test", "Form")(
    	    
    	    Form( "login" )(     
    	        
    	        Field( title = "User Name:", id = "user" ),
    	        Field( title = "Password:", id = "pswd", fieldType = "password" ),
    	        SubmitButton("Login")
    	    
    	    )
    	        
    	)
        
    }
    
    
}