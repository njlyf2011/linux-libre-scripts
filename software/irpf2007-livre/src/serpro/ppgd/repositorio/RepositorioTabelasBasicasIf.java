/* RepositorioTabelasBasicasIf - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio;
import java.util.List;

public interface RepositorioTabelasBasicasIf
{
  public List recuperarObjetosTabela (String string, boolean bool) throws RepositorioException;
  
  public void salvar (String string, List list) throws RepositorioException;
}
