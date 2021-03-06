package org.oxbow.squirm

import org.oxbow.squirm._
import scala.xml.NodeSeq
import scala.xml.XML
import java.util.UUID
import java.text.SimpleDateFormat
import java.util.Date

trait Component {
    def render: NodeSeq
}

abstract class Container(val components: Seq[Component]) extends Component {
    def render = renderComponents(components)
    protected def renderComponents(components: Iterable[Component]) = components.foldLeft(NodeSeq.Empty)(_ ++ _.render)
}

object Nada extends Component {
    override val render = NodeSeq.Empty
}

case class Markup( content: NodeSeq ) extends Component {
    override val render: NodeSeq = content
}


//////// PAGE //////////////////////////////////////////////////////////////////////////////////////////////////


case class Page( title: String = "", 
                 header: String = "", 
                 footer: Option[PageFooter] = None,
                 backButton: Boolean = true)(override val components: Component*)
    extends Container(components) {
  
    import Page._

    private def template(content: => NodeSeq) = {

        <html>
            <head>
                <meta charset="utf-8"/>
                <meta name="viewport" content="width=device-width, initial-scale=1"/>
                <title>{ title }</title>
                <script src={ jqJsPath }></script>
                <script src={ jqValidatePath }></script>
                <script type="text/javascript"> {formValidationScript}</script>  
                <link rel="stylesheet" href={ jqmCssPath }/>
                <style type="text/css">{formValidationCss}</style>
                <script src={ jqmJsPath }></script>
            </head>
            <body>
                <div data-role="page" data-add-back-btn={ backButton.attr }>
                    { PageHeader(header).render }
                    <div data-role="content">{ content }</div>
                    { footer.getOrElse(Nada).render }
                </div>
            </body>
        </html>
                    
    }

    override def render: NodeSeq = template( /*renderComponents(components)*/ super.render)
    
    private lazy val formValidationScript = scala.xml.Unparsed(
        """$(document).bind('pageinit', function(event){ 
               $('form').validate({
                });                   
           })"""
    )
    
    private lazy val formValidationCss = 
        """ input.error {
               border-width: 2px !important;
		       border-color: #cd0a0a !important;
		       background: #fef1ec;
		    }
            textarea.error {
               width: 100%;
               border-width: 2px !important;
		       border-color: #cd0a0a !important;
		       background: #fef1ec;
		    }
            label.error {
               float: left;
               width: 100%;
               padding: 3px;
		       color: #cd0a0a !important;
		    }
        
        """

}

object Page {

    var localPath: Option[String] = None // local resource path for off-line testing

    private val jqVersion  = "1.7.1"
    private val jqvVersion = "1.9"
    private val jqmVersion = "1.1.1"
    
    private lazy val remotePath = "http://code.jquery.com/"
    private lazy val remoteMsPath = "http://ajax.aspnetcdn.com/ajax/"
        
    private lazy val jqPath  = localPath.getOrElse(remotePath)
    private lazy val jqmPath = localPath.getOrElse(remotePath + "mobile/%s/".format(jqmVersion))
    private lazy val jqvPath = localPath.getOrElse(remoteMsPath + "jquery.validate/%s/".format(jqvVersion))

    protected lazy val jqmCssPath = jqmPath + "jquery.mobile-%s.min.css".format(jqmVersion)
    protected lazy val jqJsPath   = jqPath  + "jquery-%s.min.js".format(jqVersion)
    protected lazy val jqmJsPath  = jqmPath + "jquery.mobile-%s.min.js".format(jqmVersion)
    protected lazy val jqValidatePath = jqvPath + "jquery.validate.min.js"
    
}

case class PageHeader(
        title: String = "Header", 
        dataTheme: String = "a", 
        fixedPosition: Boolean = true) extends Component {

    /**
     * This method can be overridden to provide different content
     */
    protected lazy val content: NodeSeq = <h1>{ title }</h1>
    override val render: NodeSeq = <div data-role="header" data-theme={ dataTheme } data-position= { fixedPosition.attr("fixed") }>{ content }</div>

}


case class PageFooter(
        title: String = "Footer", 
        dataTheme: String = "a", 
        fixedPosition: Boolean = true )(override val components:Component*) 
    extends Container(components) {

    /**
     * This method can be overridden to provide different content
     */
    protected lazy val content: NodeSeq = <h1>{ title }</h1>
    override val render: NodeSeq = 
         <div data-role="footer" data-theme={ dataTheme } data-position= { fixedPosition.attr("fixed") } >{ 
            if ( components.isEmpty ) content else super.render
         }</div>

}


case class NavBar(override val components: Component*) extends Container(components) {
    override val render: NodeSeq = <div data-role="navbar"><ul>{ super.render }</ul></div>
}

//////// LIST VIEW //////////////////////////////////////////////////////////////////////////////////////////////////

case class ListView(insets: Boolean = true)(override val components: Seq[Component]) extends Container(components) {
    override val render: NodeSeq =
        <ul data-role="listview" data-inset={ insets.toString } data-theme="d">{ super.render }</ul>
}

object ListView {
    def apply(components: Component*): ListView = new ListView(true)(components)
}

case class LinkListItem(
        title: String, 
        url: String, 
        icon: String = null, 
        transition: String = null,
        dialog: Boolean = false,
        noajax: Boolean = false ) extends Component {
    override val render: NodeSeq = 
    <li><a href={ url } 
           data-icon={icon} 
           data-transition={transition} 
           data-rel={dialog.attr("dialog")}
           data-ajax={noajax.attr("false")}>{ title }</a></li>
}

case class PropertyListItem(
        title: String, 
        value: String) extends Component {
    override val render: NodeSeq =
        <li>{ title } <p class="ui-li-aside"><strong>{ value }</strong></p></li>
}

//////// COLLAPSIBLES ////////////////////////////////////////////////////////////////////////////////////////////////

case class Collapsible(
        title: String, 
        collapsed: Boolean, 
        override val components: List[Component])
    extends Container(components) {
    override val render: NodeSeq =
        <div data-role="collapsible" data-collapsed={ collapsed.toString }>
            <h6>{ title }</h6>
            <p>{ ListView(true)(components).render }</p>
        </div>
}

object Collapsible {

    def apply(title: String, collapsed: Boolean, components: Component*) =
        new Collapsible(title, collapsed, components.toList)

    def apply(title: String, components: Component*) =
        new Collapsible(title, true, components.toList)

}

case class CollapsibleSet(elements: Collapsible*) extends Container(elements) {
    override val render: NodeSeq =
        <div data-role="collapsible-set" data-theme="b" data-content-theme="d">{ super.render }</div>
}

//////// FIELDS ////////////////////////////////////////////////////////////////////////////////////////////////

case class Form(
        action: String, 
        method: String = "post", 
        message: Option[String] = None )(components: Component*)
    extends Container(components) {

    //TODO parametrize 'data-ajax', 'data-transition' and 'theme' 

    override def render: NodeSeq =
        <form class="validate" action={ action } method={ method } data-ajax="false" data-transition="pop">
            <div class="ui-body ui-body-d">
                {
                    renderComponents(
                        if (message.isDefined)
                            Seq(PageHeader(message.get, "e")) ++ components else components)
                }
            </div>
        </form>

}

case class Field(
        title: String, 
        id: String, 
        fieldType: String = "text", 
        required: Boolean = true, 
        defaultValue: String = "") extends Component {
    
    override val render: NodeSeq =
        <fieldset data-role="fieldcontain">
            <label for={ id }>{ title }</label>
            <input type={ fieldType } name={ id } id={ id } value={ defaultValue } class={ required.attr("required") }/>
        </fieldset>

}


case class TextArea(
        title: String, 
        id: String, 
        required: Boolean = true, 
        defaultValue: String = "") extends Component {
    
    override val render: NodeSeq =
        <fieldset data-role="fieldcontain">
            <label for={ id }>{ title }</label>
            <textarea name={ id } id={ id } value={ defaultValue } class={ required.attr("required") }/>
        </fieldset>

}

case class PasswordField(
        override val title: String, 
        override val id: String, 
        override val required: Boolean = true, 
        override val defaultValue: String = "")
    extends Field(title, id, "password", required, defaultValue)

case class SubmitButton(title: String = "Submit", icon: String="") extends Component {
    override val render: NodeSeq =
        <input type="submit" data-theme="b" data-icon={icon} name="submit" value={ title } />
}

//////// BUTTONS ////////////////////////////////////////////////////////////////////////////////////////////////////////

case class ControlGroup(horizontal: Boolean)(override val components: Component*) extends Container(components) {
    override val render: NodeSeq = {
        <div data-role="controlgroup" data-type={ horizontal.attr("horizontal") }>{ super.render }</div>
    }
}

case class Button(
        title: String, 
        link: String, 
        theme: String = "d", 
        icon: String=null,
        back: Boolean = false ) extends Component {
    override val render: NodeSeq = 
    
    <a href={ link } data-role="button" data-theme={ theme } data-icon={icon} data-rel={back.attr("back")}>{ title }</a>
}


//////// LAYOUTS ////////////////////////////////////////////////////////////////////////////////////////////////////////

object Grid {
   val columnIds = "abcd"
   def widthId( width: Int ) = columnIds(width-2)  
}

case class Grid(width: Int = 2)( override val components: Component*) extends Container(components) {
    
   import Grid._
  
    override val render: NodeSeq = {

        val w = if (width > 5) 5 else width

        <div class={ "ui-grid-" + widthId(w) }>
        {
            components.zipWithIndex.map { case (component, index) => 
              <div class={ "ui-block-" + columnIds(index % w) }>{ component.render }</div>
            }
        }
        </div>

    }

}


