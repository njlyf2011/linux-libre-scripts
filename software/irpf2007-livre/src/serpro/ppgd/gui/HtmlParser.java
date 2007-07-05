/* HtmlParser - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

public class HtmlParser
{
  private HTMLEditorKit.ParserCallback callback;
  private String texto;
  
  class ParserCallbackLocal extends HTMLEditorKit.ParserCallback
  {
    public void handleComment (char[] data, int pos)
    {
      /* empty */
    }
    
    public void handleError (String errorMsg, int pos)
    {
      /* empty */
    }
    
    public void handleSimpleTag (HTML.Tag t, MutableAttributeSet a, int pos)
    {
      /* empty */
    }
    
    public void handleStartTag (HTML.Tag t, MutableAttributeSet a, int pos)
    {
      /* empty */
    }
    
    public void handleEndTag (HTML.Tag t, int pos)
    {
      /* empty */
    }
    
    public void handleText (char[] data, int pos)
    {
      if (texto == null)
	texto = String.copyValueOf (data);
      else
	{
	  HtmlParser htmlparser = HtmlParser.this;
	  String string = htmlparser.texto;
	  StringBuffer stringbuffer = new StringBuffer (string);
	  htmlparser.texto = stringbuffer.append (" ").append (String.copyValueOf (data)).toString ();
	}
    }
  }
  
  class HTMLParse extends HTMLEditorKit
  {
    public HTMLEditorKit.Parser getParser ()
    {
      return super.getParser ();
    }
  }
  
  public HtmlParser (String html, HTMLEditorKit.ParserCallback callback)
  {
    StringReader r = new StringReader (html);
    HTMLEditorKit.Parser parse = new HTMLParse ().getParser ();
    if (callback == null)
      this.callback = new ParserCallbackLocal ();
    else
      this.callback = callback;
    try
      {
	parse.parse (r, this.callback, true);
      }
    catch (IOException e)
      {
	e.printStackTrace ();
      }
  }
  
  public String getTexto ()
  {
    String saida = texto;
    texto = "";
    return saida;
  }
}
