/* RepositorioIdDB - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.embeddedDB;
import java.util.List;
import java.util.Vector;

import serpro.ppgd.negocio.IdUsuario;
import serpro.ppgd.negocio.util.FabricaTratamentoErro;
import serpro.ppgd.repositorio.RepositorioException;
import serpro.ppgd.repositorio.RepositorioIdIf;

public class RepositorioIdDB implements RepositorioIdIf
{
  private RepositorioIdDAO repositorioIdDAO = null;
  
  public RepositorioIdDB (String pNomeCompletoClasseDoId)
  {
    repositorioIdDAO = new RepositorioIdDAO (pNomeCompletoClasseDoId);
  }
  
  public IdUsuario criaInstanciaIdConcreto ()
  {
    IdUsuario idusuario;
    try
      {
	Object retorno = repositorioIdDAO.criaInstanciaIdConcreto ();
	idusuario = (IdUsuario) retorno;
      }
    catch (Exception e)
      {
	throw new IllegalArgumentException ("Erro na instancia\u00e7\u00e3o do IdUsuario Concreto :" + e.getMessage ());
      }
    return idusuario;
  }
  
  public List recuperarIds ()
  {
    List list;
    try
      {
	list = repositorioIdDAO.recuperarIds ();
      }
    catch (RepositorioException e)
      {
	FabricaTratamentoErro.getTrataErroSistemico ().trataErroSistemico (e);
	return new Vector ();
      }
    return list;
  }
  
  public IdUsuario recuperarId (IdUsuario pId)
  {
    IdUsuario idusuario;
    try
      {
	idusuario = repositorioIdDAO.recuperarId (pId);
      }
    catch (RepositorioException e)
      {
	FabricaTratamentoErro.getTrataErroSistemico ().trataErroSistemico (e);
	return null;
      }
    return idusuario;
  }
  
  public IdUsuario criarId (IdUsuario pId) throws RepositorioException
  {
    if (recuperarId (pId) == null)
      repositorioIdDAO.inserir (pId);
    else
      throw new RepositorioException ("O IdUsuario j\u00e1 existe!!!");
    return pId;
  }
  
  public IdUsuario criarIdNaoPersistido (String pNi)
  {
    IdUsuario idNovo = repositorioIdDAO.criaInstanciaIdConcreto ();
    idNovo.getNiContribuinte ().setConteudo (pNi);
    return idNovo;
  }
  
  public void removerId (IdUsuario id) throws RepositorioException
  {
    try
      {
	repositorioIdDAO.deletar (id);
      }
    catch (RepositorioException e)
      {
	FabricaTratamentoErro.getTrataErroSistemico ().trataErroSistemico (e);
      }
  }
  
  public void salvar (IdUsuario id) throws RepositorioException
  {
    if (recuperarId (id) == null)
      {
	try
	  {
	    repositorioIdDAO.inserir (id);
	  }
	catch (RepositorioException e)
	  {
	    FabricaTratamentoErro.getTrataErroSistemico ().trataErroSistemico (e);
	  }
      }
    else
      {
	try
	  {
	    repositorioIdDAO.alterar (id);
	  }
	catch (RepositorioException e)
	  {
	    FabricaTratamentoErro.getTrataErroSistemico ().trataErroSistemico (e);
	  }
      }
  }
  
  public void salvar () throws RepositorioException
  {
    /* empty */
  }
}
