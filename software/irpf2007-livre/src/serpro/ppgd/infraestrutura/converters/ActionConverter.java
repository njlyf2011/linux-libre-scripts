/* ActionConverter - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.converters;
import javax.swing.Action;

import org.jdom.Attribute;
import org.swixml.Converter;
import org.swixml.Localizer;

public class ActionConverter implements Converter
{
  
  public Object convert (Class type, Attribute attr, Localizer localizer)
  {
    Action action;
    try
      {
	action = (Action) Class.forName (attr.getValue ()).newInstance ();
      }
    catch (Exception e)
      {
	return null;
      }
    return action;
  }
  
  public Class convertsTo ()
  {
    return javax.swing.Action.class;
  }
  
}
