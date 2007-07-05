/* IdDeclaracaoDefault - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.usuariodefault;
import java.io.File;
import java.util.List;
import java.util.Vector;

import serpro.ppgd.negocio.IdDeclaracao;

public class IdDeclaracaoDefault extends IdDeclaracao
{
  public IdDeclaracaoDefault (UsuarioIdDefault pId)
  {
    super ((serpro.ppgd.negocio.IdUsuario) pId);
  }
  
  public String getPathArquivo (String dirDados)
  {
    String path = dirDados + "/UsuarioDefault";
    File flDados = new File (path);
    flDados.mkdirs ();
    StringBuffer nomeArquivoDec = new StringBuffer ();
    nomeArquivoDec.append (path);
    nomeArquivoDec.append ("/UsuarioDefault");
    nomeArquivoDec.append (".xml");
    return nomeArquivoDec.toString ();
  }
  
  protected List getListaAtributosPKIdDec ()
  {
    List retorno = new Vector ();
    return retorno;
  }
}
