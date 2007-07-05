/* RepositorioGenericoIf - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.persistenciagenerica;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.repositorio.repositorioXML.RepositorioXMLException;

public interface RepositorioGenericoIf
{
  public ObjetoNegocio getObjeto (String string) throws RepositorioXMLException;
  
  public void salvar (ObjetoNegocio objetonegocio, String string) throws RepositorioXMLException;
  
  public void excluir (String string) throws RepositorioXMLException;
}
