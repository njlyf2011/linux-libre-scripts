/* FormLayoutConverter - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.converters;
import java.awt.LayoutManager;
import java.util.StringTokenizer;

import com.jgoodies.forms.layout.FormLayout;

import org.jdom.Attribute;
import org.swixml.Localizer;
import org.swixml.converters.LayoutConverter;

public class FormLayoutConverter extends LayoutConverter
{
  public static final String FORM_LAYOUT = "formlayout";
  
  public Object convert (Class type, Attribute attr, Localizer localizer)
  {
    LayoutManager lm = null;
    StringTokenizer st = new StringTokenizer (attr.getValue (), "()");
    String s = st.nextToken ().trim ();
    if (s != null)
      {
	s = s.toLowerCase ();
	if (s.equals ("formlayout"))
	  {
	    st = new StringTokenizer (attr.getValue (), "'");
	    st.nextToken ();
	    String colunas = null;
	    String linhas = null;
	    if (st.hasMoreTokens ())
	      colunas = st.nextToken ();
	    if (st.hasMoreTokens ())
	      {
		linhas = st.nextToken ();
		linhas = st.nextToken ();
	      }
	    if (colunas != null && linhas != null)
	      lm = new FormLayout (colunas, linhas);
	    if (colunas != null && linhas == null)
	      lm = new FormLayout (colunas);
	  }
	else
	  lm = (LayoutManager) super.convert (type, attr, localizer);
      }
    return lm;
  }
}
