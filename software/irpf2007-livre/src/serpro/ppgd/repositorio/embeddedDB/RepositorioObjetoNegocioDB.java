/* RepositorioObjetoNegocioDB - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.embeddedDB;
import serpro.ppgd.negocio.IdDeclaracao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.repositorio.RepositorioException;
import serpro.ppgd.repositorio.RepositorioObjetoNegocioIf;
import serpro.ppgd.repositorio.repositorioXML.HashtableIdDeclaracao;

public class RepositorioObjetoNegocioDB implements RepositorioObjetoNegocioIf
{
  private RepositorioObjetoNegocioDAO repositorioObjetoNegocioDAO = null;
  private HashtableIdDeclaracao objetosAbertos;
  
  public RepositorioObjetoNegocioDB (String pNomeCompletoClasseDaDeclaracao, String pNomeTabela)
  {
    objetosAbertos = new HashtableIdDeclaracao ();
    repositorioObjetoNegocioDAO = new RepositorioObjetoNegocioDAO (pNomeCompletoClasseDaDeclaracao, pNomeTabela);
  }
  
  public RepositorioObjetoNegocioDB (String pNomeCompletoClasseDaDeclaracao)
  {
    objetosAbertos = new HashtableIdDeclaracao ();
    repositorioObjetoNegocioDAO = new RepositorioObjetoNegocioDAO (pNomeCompletoClasseDaDeclaracao, null);
  }
  
  public void descarregar (IdDeclaracao id) throws RepositorioException
  {
    if (objetosAbertos.containsKey (id))
      objetosAbertos.remove (id);
  }
  
  public ObjetoNegocio recuperar (IdDeclaracao idDec) throws RepositorioException
  {
    if (objetosAbertos.containsKey (idDec))
      return (ObjetoNegocio) ((ObjetoPersistente) objetosAbertos.get (idDec)).instancia;
    ObjetoNegocio resultado = repositorioObjetoNegocioDAO.recuperar (idDec);
    objetosAbertos.put (idDec, new ObjetoPersistente (resultado, true));
    return resultado;
  }
  
  public ObjetoNegocio criar (IdDeclaracao idDec) throws RepositorioException
  {
    ObjetoNegocio resultado = null;
    if (idDec == null)
      throw new IllegalArgumentException ("RepositorioObjetoNegocioDB: argumento \u00e9 nulo.");
    if (objetosAbertos.containsKey (idDec))
      throw new IllegalArgumentException ("RepositorioObjetoNegocioDB: ObjetoNegocio solicitado j\u00e1 existe.");
    resultado = repositorioObjetoNegocioDAO.criarNaoPersistido (idDec);
    objetosAbertos.put (idDec, new ObjetoPersistente (resultado, false));
    return resultado;
  }
  
  public void salvar (IdDeclaracao idDec) throws RepositorioException
  {
    if (objetosAbertos.containsKey (idDec))
      {
	ObjetoPersistente objetoPersistente = (ObjetoPersistente) objetosAbertos.get (idDec);
	if (objetoPersistente.persistido)
	  repositorioObjetoNegocioDAO.alterar ((ObjetoNegocio) objetoPersistente.instancia);
	else
	  {
	    repositorioObjetoNegocioDAO.inserir ((ObjetoNegocio) objetoPersistente.instancia);
	    objetoPersistente.persistido = true;
	  }
      }
    else
      throw new IllegalArgumentException ("N\u00e3o h\u00e1 ObjetoNegocio correspondente a esse IdDeclaracao: " + idDec.getNiContribuinte ());
  }
  
  public void deletar (IdDeclaracao idDec) throws RepositorioException
  {
    ObjetoNegocio obj = repositorioObjetoNegocioDAO.criarNaoPersistido (idDec);
    repositorioObjetoNegocioDAO.excluir (obj);
    objetosAbertos.remove (idDec);
  }
}
