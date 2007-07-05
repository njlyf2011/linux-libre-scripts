/* CadastroId - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio;
import java.util.List;

import serpro.ppgd.negocio.IdDeclaracao;
import serpro.ppgd.negocio.IdUsuario;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.usuariodefault.IdDeclaracaoDefault;
import serpro.ppgd.negocio.usuariodefault.UsuarioIdDefault;

public class CadastroId
{
  protected RepositorioIdIf repId;
  protected RepositorioIdDeclaracaoIf repIdDeclaracao;
  protected RepositorioObjetoNegocioIf repDeclaracao;
  protected IdDeclaracao ultimoIdDeclaracaoRecuperado;
  
  public CadastroId (RepositorioIdIf repId, RepositorioIdDeclaracaoIf repIdDeclaracao, RepositorioObjetoNegocioIf repDeclaracao)
  {
    this.repId = repId;
    this.repIdDeclaracao = repIdDeclaracao;
    this.repDeclaracao = repDeclaracao;
    recuperarIdDeclaracoes ();
  }
  
  public List recuperarIds ()
  {
    return repId.recuperarIds ();
  }
  
  public IdUsuario recuperarId (IdUsuario pId)
  {
    return repId.recuperarId (pId);
  }
  
  public IdUsuario criarId (IdUsuario pId) throws RepositorioException
  {
    return repId.criarId (pId);
  }
  
  public IdUsuario criarIdNaoPersistido (String cpf)
  {
    return repId.criarIdNaoPersistido (cpf);
  }
  
  public List recuperarIdDeclaracoes ()
  {
    return repIdDeclaracao.recuperarIdDeclaracoes (repId.recuperarIds ());
  }
  
  public boolean existeIdDeclaracao (IdDeclaracao idDeclaracao)
  {
    return repIdDeclaracao.existeIdDeclaracao (idDeclaracao);
  }
  
  public IdDeclaracao retornaIdDeclaracaoPersistido (IdDeclaracao idDeclaracao)
  {
    return repIdDeclaracao.retornaIdDeclaracaoPersistido (idDeclaracao);
  }
  
  public IdDeclaracao criarIdDeclaracao (IdUsuario id) throws RepositorioException
  {
    IdDeclaracao idDeclaracao = repIdDeclaracao.criarIdDeclaracao (id);
    ultimoIdDeclaracaoRecuperado = idDeclaracao;
    repDeclaracao.criar (idDeclaracao);
    repDeclaracao.salvar (idDeclaracao);
    return idDeclaracao;
  }
  
  public IdDeclaracao criarIdDeclaracao (IdDeclaracao idDec) throws RepositorioException
  {
    ultimoIdDeclaracaoRecuperado = repIdDeclaracao.criarIdDeclaracao (idDec);
    repDeclaracao.criar (idDec);
    repDeclaracao.salvar (idDec);
    return idDec;
  }
  
  public IdDeclaracao criarIdDeclaracaoNaoPersistido (IdUsuario id)
  {
    ultimoIdDeclaracaoRecuperado = repIdDeclaracao.criarIdDeclaracaoNaoPersistido (id);
    return ultimoIdDeclaracaoRecuperado;
  }
  
  public void removerIdDeclaracao (IdDeclaracao idDeclaracao) throws RepositorioException
  {
    repDeclaracao.deletar (idDeclaracao);
    repIdDeclaracao.removerIdDeclaracao (idDeclaracao);
    if (ultimoIdDeclaracaoRecuperado != null && idDeclaracao.equals (ultimoIdDeclaracaoRecuperado))
      ultimoIdDeclaracaoRecuperado = null;
  }
  
  public void salvarIdDeclaracao (IdDeclaracao idDeclaracao) throws RepositorioException
  {
    IdUsuario id = repId.recuperarId (idDeclaracao.getId ());
    repId.salvar (id);
    IdDeclaracao idDec = repIdDeclaracao.retornaIdDeclaracaoPersistido (idDeclaracao);
    repIdDeclaracao.salvar (idDec);
    repDeclaracao.salvar (idDec);
  }
  
  public ObjetoNegocio recuperarDeclaracao (IdDeclaracao idDeclaracao) throws RepositorioException
  {
    ultimoIdDeclaracaoRecuperado = repIdDeclaracao.retornaIdDeclaracaoPersistido (idDeclaracao);
    ObjetoNegocio retorno = repDeclaracao.recuperar (ultimoIdDeclaracaoRecuperado);
    return retorno;
  }
  
  public ObjetoNegocio getUltimaDeclaracaoAberta () throws RepositorioException
  {
    if (ultimoIdDeclaracaoRecuperado == null)
      throw new RepositorioException ("Ainda n\u00e3o foi aberta nenhuma declara\u00e7\u00e3o.");
    return repDeclaracao.recuperar (ultimoIdDeclaracaoRecuperado);
  }
  
  public void salvarUltimaDeclaracaoAberta () throws RepositorioException
  {
    if (ultimoIdDeclaracaoRecuperado != null)
      salvarIdDeclaracao (ultimoIdDeclaracaoRecuperado);
  }
  
  public IdDeclaracao recuperarIdDeclaracao (IdDeclaracao idDeclaracao) throws RepositorioException
  {
    return repIdDeclaracao.retornaIdDeclaracaoPersistido (idDeclaracao);
  }
  
  public void fecharUltimaDeclaracaoAberta () throws RepositorioException
  {
    salvarUltimaDeclaracaoAberta ();
    ultimoIdDeclaracaoRecuperado = null;
  }
  
  public RepositorioIdIf getRepId ()
  {
    return repId;
  }
  
  public RepositorioIdDeclaracaoIf getRepIdDeclaracao ()
  {
    return repIdDeclaracao;
  }
  
  public IdDeclaracao getUltimoIdDeclaracaoRecuperado ()
  {
    return ultimoIdDeclaracaoRecuperado;
  }
  
  public ObjetoNegocio getObjetoNegocioDefault () throws RepositorioException
  {
    UsuarioIdDefault usuarioDefault = new UsuarioIdDefault ();
    try
      {
	repId.criarId (usuarioDefault);
      }
    catch (RepositorioException e)
      {
	usuarioDefault = (UsuarioIdDefault) repId.recuperarId (usuarioDefault);
      }
    IdDeclaracaoDefault idDeclaracaoDefault = new IdDeclaracaoDefault (usuarioDefault);
    if (! existeIdDeclaracao (idDeclaracaoDefault))
      {
	criarIdDeclaracao (idDeclaracaoDefault);
	salvarIdDeclaracao (idDeclaracaoDefault);
      }
    return recuperarDeclaracao (idDeclaracaoDefault);
  }
  
  public void resetObjetoNegocioDefault ()
  {
    UsuarioIdDefault usuarioDefault = new UsuarioIdDefault ();
    try
      {
	repId.criarId (usuarioDefault);
      }
    catch (RepositorioException e)
      {
	usuarioDefault = (UsuarioIdDefault) repId.recuperarId (usuarioDefault);
      }
    try
      {
	IdDeclaracaoDefault idDeclaracaoDefault = new IdDeclaracaoDefault (usuarioDefault);
	if (existeIdDeclaracao (idDeclaracaoDefault))
	  removerIdDeclaracao (idDeclaracaoDefault);
      }
    catch (RepositorioException repositorioexception)
      {
	/* empty */
      }
  }
  
  public void descarregarDeclaracao (IdDeclaracao idDeclaracao)
  {
    try
      {
	repDeclaracao.descarregar (idDeclaracao);
      }
    catch (RepositorioException repositorioexception)
      {
	/* empty */
      }
  }
}
