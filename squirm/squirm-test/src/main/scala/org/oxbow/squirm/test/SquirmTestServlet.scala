package org.oxbow.squirm.test

import org.scalatra.ScalatraServlet
import org.oxbow.squirm.SquirmSupport
import org.scalatra.UrlSupport
import org.oxbow.squirm._

class SquirmTestServlet extends ScalatraServlet with UrlSupport with SquirmSupport {

    def contextPath = getServletContext.getContextPath
    
    before() { // resource path for off-line testing
       Page.localPath = Option(url("/jqm/"))
    }
    
    get("/") {
        Page("Squirm Test", "ListView")(
            ListView( 
		       LinkListItem( "Collapsible Set", url("/collapsible") ),
		       LinkListItem( "Form", url("/form") )
		    )   
        )
    }
    
    get("/collapsible") {
        
        Page("Squirm Test", "Collapsible")(
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
    	    
    	    Form( url("/login"), message = session.getOrElse("login.message", "").toString )(     
    	        
    	        Field( title = "User Name:", id = "user" ),
    	        PasswordField( title = "Password:", id = "pswd" ),
    	        SubmitButton("Login")
    	    
    	    )
    	        
    	)
        
    }
    
    post("/login") {
        
        session("login.message") = "Login Failed"
        redirect(url("/form"))
    }
    
    
}