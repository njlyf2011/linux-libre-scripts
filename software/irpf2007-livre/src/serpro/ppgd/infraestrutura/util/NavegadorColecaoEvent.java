/* NavegadorColecaoEvent - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import java.util.EventObject;

import serpro.ppgd.negocio.ObjetoNegocio;

public class NavegadorColecaoEvent extends EventObject
{
  private ObjetoNegocio objetoNegocio;
  
  public NavegadorColecaoEvent (Object source)
  {
    super (source);
  }
  
  public void setObjetoNegocio (ObjetoNegocio objetoNegocio)
  {
    this.objetoNegocio = objetoNegocio;
  }
  
  public ObjetoNegocio getObjetoNegocio ()
  {
    return objetoNegocio;
  }
}
