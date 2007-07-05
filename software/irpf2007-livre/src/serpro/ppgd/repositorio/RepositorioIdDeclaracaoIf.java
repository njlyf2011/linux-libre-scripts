/* RepositorioIdDeclaracaoIf - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio;
import java.util.List;

import serpro.ppgd.negocio.IdDeclaracao;
import serpro.ppgd.negocio.IdUsuario;

public interface RepositorioIdDeclaracaoIf
{
  public List recuperarIdDeclaracoes (List list);
  
  public boolean existeIdDeclaracao (IdDeclaracao iddeclaracao);
  
  public IdDeclaracao retornaIdDeclaracaoPersistido (IdDeclaracao iddeclaracao);
  
  public IdDeclaracao criarIdDeclaracao (IdUsuario idusuario);
  
  public IdDeclaracao criarIdDeclaracao (IdDeclaracao iddeclaracao);
  
  public IdDeclaracao criarIdDeclaracaoNaoPersistido (IdUsuario idusuario);
  
  public void removerIdDeclaracao (IdDeclaracao iddeclaracao) throws RepositorioException;
  
  public void salvar (IdDeclaracao iddeclaracao) throws RepositorioException;
}
