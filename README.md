squirm
======
Scala API for JQuery Mobile

Examples
===============================

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
    	    
    	    Form( "login" )(     
    	        
    	        Field( title = "User Name:", id = "user" ),
    	        Field( title = "Password:", id = "pswd", fieldType = "password" ),
    	        SubmitButton("Login")
    	    
    	    )
    	        
    	)