squirm
======
Scala API for JQuery Mobile


        Page("Squirm Test", "ListView")(
            ListView( 
		       LinkListItem( "Collapsible Set", url("/collapsible") ),
		       LinkListItem( "Form", url("/form") )
		    )   
        )
        
        
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
        
        Page("Squirm Test", "Form")(
    	    
    	    Form( url("/login"), message = session.getOrElse("login.message", "").toString )(     
    	        
    	        Field( title = "User Name:", id = "user" ),
    	        PasswordField( title = "Password:", id = "pswd" ),
    	        SubmitButton("Login")
    	    
    	    )
    	        
    	)