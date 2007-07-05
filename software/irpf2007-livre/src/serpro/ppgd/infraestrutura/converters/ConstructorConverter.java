/* ConstructorConverter - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.converters;
import org.jdom.Attribute;
import org.swixml.Converter;
import org.swixml.Localizer;

public class ConstructorConverter implements Converter
{
  public static final Class TEMPLATE;
  
  public Object convert (Class type, Attribute attr, Localizer localizer)
  {
    return conv (type, attr);
  }
  
  public static Object conv (Class type, Attribute attr)
  {
    Object c = null;
    try
      {
	Class[] arg = { javax.swing.JTree.class };
	Class classe = Class.forName (attr.getValue ());
	c = classe.getConstructor (arg);
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	throw new IllegalArgumentException ("Erro na instancia\u00e7\u00e3o do Construtor " + e.getMessage ());
      }
    return c;
  }
  
  public Class convertsTo ()
  {
    return TEMPLATE;
  }
  
  
  static
  {
    TEMPLATE = java.lang.reflect.Constructor.class;
  }
}
