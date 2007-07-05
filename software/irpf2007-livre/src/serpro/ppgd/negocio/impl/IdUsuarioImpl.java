/* IdUsuarioImpl - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.impl;
import java.util.List;
import java.util.Vector;

import serpro.ppgd.negocio.IdUsuario;

public class IdUsuarioImpl extends IdUsuario
{
  public List getListaAtributosPK ()
  {
    List retorno = new Vector ();
    retorno.add (getNiContribuinte ());
    return retorno;
  }
}
