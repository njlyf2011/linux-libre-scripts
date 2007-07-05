/* RepositorioColecaoIf - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio;
import java.util.List;

import serpro.ppgd.negocio.IdDeclaracao;

public interface RepositorioColecaoIf
{
  public List recuperar (IdDeclaracao iddeclaracao);
  
  public void salvar (IdDeclaracao iddeclaracao);
}
