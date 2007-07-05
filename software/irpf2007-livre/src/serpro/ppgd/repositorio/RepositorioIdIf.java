/* RepositorioIdIf - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio;
import java.util.List;

import serpro.ppgd.negocio.IdUsuario;

public interface RepositorioIdIf
{
  public List recuperarIds ();
  
  public IdUsuario recuperarId (IdUsuario idusuario);
  
  public IdUsuario criarId (IdUsuario idusuario) throws RepositorioException;
  
  public IdUsuario criarIdNaoPersistido (String string);
  
  public void removerId (IdUsuario idusuario) throws RepositorioException;
  
  public void salvar (IdUsuario idusuario) throws RepositorioException;
  
  public void salvar () throws RepositorioException;
  
  public IdUsuario criaInstanciaIdConcreto ();
}
