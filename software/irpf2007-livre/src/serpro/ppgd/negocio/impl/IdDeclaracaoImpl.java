/* IdDeclaracaoImpl - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.impl;
import java.io.File;
import java.util.List;
import java.util.Vector;

import serpro.ppgd.negocio.IdDeclaracao;
import serpro.ppgd.negocio.IdUsuario;
import serpro.ppgd.negocio.Logico;

public class IdDeclaracaoImpl extends IdDeclaracao
{
  public IdDeclaracaoImpl (IdUsuario pId)
  {
    super (pId);
  }
  
  public String getPathArquivo (String dirDados)
  {
    String path = dirDados + "/" + getNiContribuinte ().asString ();
    File flDados = new File (path);
    flDados.mkdirs ();
    StringBuffer nomeArquivoDec = new StringBuffer ();
    nomeArquivoDec.append (path);
    nomeArquivoDec.append ("/DEC_");
    nomeArquivoDec.append (getNiContribuinte ().asString ());
    nomeArquivoDec.append ("_");
    nomeArquivoDec.append (getExercicio ().asString ());
    if (getRetificadora ().getConteudoFormatado ().equals (Logico.SIM))
      nomeArquivoDec.append ("_RETIF");
    nomeArquivoDec.append (".xml");
    return nomeArquivoDec.toString ();
  }
  
  public boolean existeArquivoDeclaracao (String dirDados)
  {
    File fl = new File (getPathArquivo (dirDados));
    return fl.exists ();
  }
  
  public List getListaAtributosPKIdDec ()
  {
    List retorno = new Vector ();
    retorno.add (getExercicio ());
    retorno.add (getRetificadora ());
    return retorno;
  }
}
