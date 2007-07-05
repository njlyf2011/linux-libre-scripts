/* CellConstraintsConverter - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package /*org.swixml...*/serpro.ppgd.infraestrutura/*...*/.converters;
import java.util.StringTokenizer;

import com.jgoodies.forms.layout.CellConstraints;

import org.jdom.Attribute;
import org.swixml.Converter;
import org.swixml.Localizer;

public class CellConstraintsConverter implements Converter
{
  public static final Class TEMPLATE;
  
  public Object convert (Class type, Attribute attr, Localizer localizer)
  {
    return conv (attr);
  }
  
  public static Object conv (Attribute attr)
  {
    StringTokenizer st = new StringTokenizer (attr.getValue (), "(,)");
    try
      {
	String nome;
	CellConstraints cc;
	nome = st.nextToken ().trim ();
	cc = new CellConstraints ();
	if ("xy".equals (nome))
	  {
	    if (2 == st.countTokens ())
	      return cc.xy (Integer.parseInt (st.nextToken ().trim ()), Integer.parseInt (st.nextToken ().trim ()));
	    if (3 == st.countTokens ())
	      return cc.xy (Integer.parseInt (st.nextToken ().trim ()), Integer.parseInt (st.nextToken ().trim ()), stEncAlign (st));
	  }
	else if ("xywh".equals (nome))
	  {
	    if (4 == st.countTokens ())
	      return cc.xywh (Integer.parseInt (st.nextToken ().trim ()), Integer.parseInt (st.nextToken ().trim ()), Integer.parseInt (st.nextToken ().trim ()), Integer.parseInt (st.nextToken ().trim ()));
	    if (5 == st.countTokens ())
	      return cc.xywh (Integer.parseInt (st.nextToken ().trim ()), Integer.parseInt (st.nextToken ().trim ()), Integer.parseInt (st.nextToken ().trim ()), Integer.parseInt (st.nextToken ().trim ()), stEncAlign (st));
	  }
      }
    catch (Exception _)
      {
      }

    return attr.getValue ();
  }
  
  private static String stEncAlign (StringTokenizer st)
  {
    String tmp = st.nextToken ();
    while (st.countTokens () > 0)
      tmp += "," + st.nextToken ();
    return tmp;
  }
  
  public Class convertsTo ()
  {
    return TEMPLATE;
  }
  
  
  static
  {
    TEMPLATE = com.jgoodies.forms.layout.CellConstraints.class;
  }
}
