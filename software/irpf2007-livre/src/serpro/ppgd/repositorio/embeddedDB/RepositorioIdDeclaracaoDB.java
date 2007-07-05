/* RepositorioIdDeclaracaoDB - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.embeddedDB;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.IdDeclaracao;
import serpro.ppgd.negocio.IdUsuario;
import serpro.ppgd.negocio.util.FabricaTratamentoErro;
import serpro.ppgd.repositorio.RepositorioException;
import serpro.ppgd.repositorio.RepositorioIdDeclaracaoIf;

public class RepositorioIdDeclaracaoDB implements RepositorioIdDeclaracaoIf
{
  private RepositorioIdDeclaracaoDAO repositorioIdDeclaracaoDAO = null;
  private List listObjetosPersistentes;
  private List listIdDeclaracoes;
  
  private RepositorioIdDeclaracaoDB ()
  {
    /* empty */
  }
  
  public RepositorioIdDeclaracaoDB (String pNomeCompletoClasseDoIdDec)
  {
    repositorioIdDeclaracaoDAO = new RepositorioIdDeclaracaoDAO (pNomeCompletoClasseDoIdDec);
  }
  
  public List recuperarIdDeclaracoes (List listIds)
  {
    List list;
    try
      {
	if (listObjetosPersistentes == null)
	  {
	    listObjetosPersistentes = new Vector ();
	    listIdDeclaracoes = new Vector ();
	    Iterator itIdDecs = repositorioIdDeclaracaoDAO.recuperarIdDeclaracoes (listIds).iterator ();
	    while (itIdDecs.hasNext ())
	      {
		IdDeclaracao idDecAtual = (IdDeclaracao) itIdDecs.next ();
		listIdDeclaracoes.add (idDecAtual);
		listObjetosPersistentes.add (new ObjetoPersistente (idDecAtual, true));
	      }
	  }
	list = listIdDeclaracoes;
      }
    catch (RepositorioException e)
      {
	FabricaTratamentoErro.getTrataErroSistemico ().trataErroSistemico (e);
	return null;
      }
    return list;
  }
  
  public boolean existeIdDeclaracao (IdDeclaracao idDeclaracao)
  {
    for (int i = 0; i < listObjetosPersistentes.size (); i++)
      {
	ObjetoPersistente objPersistenteAtual = (ObjetoPersistente) listObjetosPersistentes.get (i);
	if (((IdDeclaracao) objPersistenteAtual.instancia).equals (idDeclaracao))
	  return true;
      }
    return false;
  }
  
  public IdDeclaracao retornaIdDeclaracaoPersistido (IdDeclaracao idDeclaracao)
  {
    for (int i = 0; i < listObjetosPersistentes.size (); i++)
      {
	ObjetoPersistente objPersistenteAtual = (ObjetoPersistente) listObjetosPersistentes.get (i);
	if (((IdDeclaracao) objPersistenteAtual.instancia).equals (idDeclaracao))
	  return (IdDeclaracao) objPersistenteAtual.instancia;
      }
    return null;
  }
  
  public IdDeclaracao criarIdDeclaracao (IdUsuario id)
  {
    IdDeclaracao idDeclaracao = criarIdDeclaracaoNaoPersistido (id);
    listIdDeclaracoes.add (idDeclaracao);
    listObjetosPersistentes.add (new ObjetoPersistente (idDeclaracao, false));
    return idDeclaracao;
  }
  
  public IdDeclaracao criarIdDeclaracao (IdDeclaracao idDeclaracao)
  {
    listIdDeclaracoes.add (idDeclaracao);
    listObjetosPersistentes.add (new ObjetoPersistente (idDeclaracao, false));
    return idDeclaracao;
  }
  
  public IdDeclaracao criarIdDeclaracaoNaoPersistido (IdUsuario id)
  {
    IdDeclaracao idDeclaracao = repositorioIdDeclaracaoDAO.criaInstanciaIdDeclaracaoConcreto (id);
    idDeclaracao.getExercicio ().setConteudo (ConstantesGlobais.EXERCICIO);
    idDeclaracao.getTipo ().setConteudo ("DECLARA\u00c7\u00c3O DE AJUSTE ANUAL");
    return idDeclaracao;
  }
  
  public void removerIdDeclaracao (IdDeclaracao idDeclaracao) throws RepositorioException
  {
    for (int i = 0; i < listObjetosPersistentes.size (); i++)
      {
	ObjetoPersistente objPersAtual = (ObjetoPersistente) listObjetosPersistentes.get (i);
	IdDeclaracao idDecl = (IdDeclaracao) objPersAtual.instancia;
	if (idDeclaracao.equals (idDecl))
	  {
	    repositorioIdDeclaracaoDAO.deletar (idDecl);
	    listObjetosPersistentes.remove (i);
	    listIdDeclaracoes.remove (i);
	    return;
	  }
      }
    throw new RepositorioException ("IdDeclara\u00e7\u00e3o n\u00e3o localizado");
  }
  
  public void salvar (IdDeclaracao idDeclaracao) throws RepositorioException
  {
    for (int i = 0; i < listObjetosPersistentes.size (); i++)
      {
	ObjetoPersistente objPersAtual = (ObjetoPersistente) listObjetosPersistentes.get (i);
	IdDeclaracao idDecl = (IdDeclaracao) objPersAtual.instancia;
	if (idDeclaracao.equals (idDecl))
	  {
	    if (objPersAtual.persistido)
	      repositorioIdDeclaracaoDAO.alterar (idDecl);
	    else
	      {
		repositorioIdDeclaracaoDAO.inserir (idDecl);
		objPersAtual.persistido = true;
	      }
	    return;
	  }
      }
    throw new RepositorioException ("IdDeclara\u00e7\u00e3o n\u00e3o localizado");
  }
}
