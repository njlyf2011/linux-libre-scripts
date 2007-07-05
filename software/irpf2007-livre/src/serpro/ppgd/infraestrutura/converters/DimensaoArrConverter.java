/* DimensaoArrConverter - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.converters;
import java.awt.Dimension;
import java.util.StringTokenizer;

import org.jdom.Attribute;
import org.swixml.Converter;
import org.swixml.Localizer;
import org.swixml.converters.DimensionConverter;

public final class DimensaoArrConverter implements Converter
{
  public static final Class TEMPLATE;
  
  public Object convert (Class type, Attribute attr, Localizer localizer)
  {
    if (attr != null)
      {
	StringTokenizer st = new StringTokenizer (attr.getValue (), "|");
	Dimension[] arDim = new Dimension[st.countTokens ()];
	DimensionConverter dim = new DimensionConverter ();
	Attribute att = new Attribute ("att", "att");
	int i = 0;
	while (st.hasMoreTokens ())
	  {
	    att.setValue (st.nextToken ());
	    arDim[i++] = (Dimension) dim.convert (type, att, localizer);
	  }
	return arDim;
      }
    return null;
  }
  
  public Class convertsTo ()
  {
    return TEMPLATE;
  }
  
  
  static
  {
    TEMPLATE = java.awt.Dimension[].class;
  }
}
