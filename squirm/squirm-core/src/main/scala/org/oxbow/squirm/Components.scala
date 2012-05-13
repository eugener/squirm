package org.oxbow.squirm

import scala.xml.NodeSeq

trait Component {
    def render: NodeSeq 
}

abstract class Container( val components: Seq[Component] ) extends Component {
    def render = renderComponents(components)
    protected def renderComponents(components: Iterable[Component]) = components.foldLeft(NodeSeq.Empty)(_ ++ _.render)
}


//////// PAGE //////////////////////////////////////////////////////////////////////////////////////////////////

case class Page(title: String = "", headerTitle: String = "")( override val components: Component* ) 
     extends Container( components ) {

   private def template( content: => NodeSeq ) =

    <html>
      <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <title>{ title }</title>
        <link rel="stylesheet" href="http://code.jquery.com/mobile/1.1.0/jquery.mobile-1.1.0.min.css" />
		<script src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
		<script src="http://code.jquery.com/mobile/1.1.0/jquery.mobile-1.1.0.min.js"></script>      </head>
       <body>
          <div data-role="page" data-add-back-btn="true" >
             { PageHeader( headerTitle ).render }
             <div data-role="content">{content}</div>
          </div>
       </body>
    </html>
  
   override def render: NodeSeq = template( /*renderComponents(components)*/ super.render ) 
  

}

case class PageHeader(title: String = "Header", dataTheme: String = "a" ) extends Component {

  /**
   * This method can be overriden to provide different content
   */
  protected val content: NodeSeq = <h1>{ title }</h1>
  override val render: NodeSeq = <div data-role="header" data-theme={dataTheme}>{ content }</div>

}

//////// LIST VIEW //////////////////////////////////////////////////////////////////////////////////////////////////

case class ListView( insets: Boolean = true)( override val components: Seq[Component]  ) extends Container(components) {
   override val render: NodeSeq = 
       <ul data-role="listview" data-inset={insets.toString} data-theme="d" >{super.render}</ul>
}

object ListView {
   def apply( components: Component* ): ListView = new ListView(true)(components)
}

case class LinkListItem( title: String, url: String ) extends Component {
   override val render: NodeSeq = <li><a href={url}>{title}</a></li>
}

case class PropertyListItem( title: String, value: String ) extends Component {
   override val render: NodeSeq =
       <li>{title} <p class="ui-li-aside"><strong>{value}</strong></p></li> 
}

//////// COLLAPSIBLES ////////////////////////////////////////////////////////////////////////////////////////////////

case class Collapsible( title: String, collapsed: Boolean, override val components: List[Component] ) 
  extends Container( components ) {
  override val render: NodeSeq = 
     <div data-role="collapsible" data-collapsed={collapsed.toString} data-theme="b" data-content-theme="d" >
         <h6>{title}</h6>
         <p>{ ListView(true)(components).render}</p>
     </div>
}

object Collapsible {
  
  def apply( title: String, collapsed: Boolean, components: Component*) = 
      new Collapsible( title, collapsed, components.toList )
  
  def apply( title: String, components: Component*) = 
      new Collapsible( title, true, components.toList )
  
}


case class CollapsibleSet( elements: Collapsible* ) extends Container( elements ) {
   override val render: NodeSeq = 
      <div data-role="collapsible-set" >{ super.render }</div>
}

//////// FIELDS ////////////////////////////////////////////////////////////////////////////////////////////////

case class Form( action: String, method: String ="post", message: String = "" )( components: Component* ) 
     extends Container(components) {

   //TODO parametrize 'data-ajax', 'data-transition' and 'theme' 
    
   override def render: NodeSeq = 
       <form action={action} method={method} data-ajax="false" data-transition="pop">
	      <div class="ui-body ui-body-d">
          {
              renderComponents( 
                  if ( !message.trim.isEmpty )  
                     Seq(PageHeader( message, "e" )) ++ components else components  )
          }
          </div>
       </form>

}


case class Field(title: String, id: String, fieldType: String = "text", required: Boolean = true, defaultValue: String = "" ) extends Component {
    override val render: NodeSeq =
        <div data-role="fieldcontain">
            <label for={ id }>{ title }</label>
            <input type={ fieldType } name={ id } id={ id } value={ defaultValue } class={ if (required) "required" else "" }/>
        </div>

}

case class PasswordField( override val title: String, override val id: String, override val required: Boolean = true, override val defaultValue: String = "" )
     extends Field( title, id, "password", required, defaultValue )

case class SubmitButton( title: String = "Submit" ) extends Component {
    override val render: NodeSeq = 
        <button type="submit" data-theme="b" name="submit" value="submit-value">{title}</button> 
}
