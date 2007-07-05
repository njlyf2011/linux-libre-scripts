/* UsuarioIdDefault - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.usuariodefault;
import java.util.List;
import java.util.Vector;

import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.IdUsuario;

public class UsuarioIdDefault extends IdUsuario
{
  private Alfa nomeUsuario = new Alfa (this, "Usu\u00e1rio");
  
  public UsuarioIdDefault ()
  {
    nomeUsuario.setConteudo ("Usuario Default");
  }
  
  public List getListaAtributosPK ()
  {
    List retorno = new Vector ();
    retorno.add (nomeUsuario);
    return retorno;
  }
  
  public Alfa getNomeUsuario ()
  {
    return nomeUsuario;
  }
  
  public void setNomeUsuario (Alfa nomeUsuario)
  {
    this.nomeUsuario = nomeUsuario;
  }
}
