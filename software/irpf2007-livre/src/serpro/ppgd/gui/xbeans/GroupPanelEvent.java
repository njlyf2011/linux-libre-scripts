/* GroupPanelEvent - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.util.EventObject;

import serpro.ppgd.negocio.Informacao;

public class GroupPanelEvent extends EventObject
{
  private Informacao informacao;
  
  public GroupPanelEvent (Object source)
  {
    super (source);
  }
  
  public Informacao getInformacao ()
  {
    return informacao;
  }
  
  public void setInformacao (Informacao informacao)
  {
    this.informacao = informacao;
  }
}
