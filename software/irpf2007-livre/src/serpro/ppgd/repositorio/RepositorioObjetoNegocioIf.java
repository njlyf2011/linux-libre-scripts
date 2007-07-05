/* RepositorioObjetoNegocioIf - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio;
import serpro.ppgd.negocio.IdDeclaracao;
import serpro.ppgd.negocio.ObjetoNegocio;

public interface RepositorioObjetoNegocioIf
{
  public ObjetoNegocio recuperar (IdDeclaracao iddeclaracao) throws RepositorioException;
  
  public ObjetoNegocio criar (IdDeclaracao iddeclaracao) throws RepositorioException;
  
  public void salvar (IdDeclaracao iddeclaracao) throws RepositorioException;
  
  public void deletar (IdDeclaracao iddeclaracao) throws RepositorioException;
  
  public void descarregar (IdDeclaracao iddeclaracao) throws RepositorioException;
}
