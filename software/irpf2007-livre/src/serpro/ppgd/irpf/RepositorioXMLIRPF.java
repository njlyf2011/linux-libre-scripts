/* RepositorioXMLIRPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import serpro.ppgd.gui.DialogoOcupado;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.persistenciagenerica.RepositorioXMLDefault;
import serpro.ppgd.repositorio.RepositorioException;
import serpro.ppgd.repositorio.repositorioXML.RepositorioXMLException;

public class RepositorioXMLIRPF
{
  public static final String PATH_XML_ID_DECS = IRPFUtil.DIR_DADOS + "/" + "iddeclaracoes.xml";
  private IdentificadorDeclaracao identificadorDeclaracao = null;
  private DeclaracaoIRPF declaracaoIRPF = null;
  private ColecaoIdDeclaracao listaIdDeclaracoes = null;
  private RepositorioXMLDefault repositorioIds = new RepositorioXMLDefault ();
  private RepositorioXMLDefault repositorioDeclaracoes = new RepositorioXMLDefault ();
  
  public RepositorioXMLIRPF ()
  {
    carregaIdDeclaracoes ();
  }
  
  private void carregaIdDeclaracoes ()
  {
    if (PlataformaPPGD.isEmDesign ())
      listaIdDeclaracoes = new ColecaoIdDeclaracao ();
    else
      {
	try
	  {
	    testaDiretorioDados ();
	    listaIdDeclaracoes = new ColecaoIdDeclaracao ();
	    if (new File (PATH_XML_ID_DECS).exists())
		repositorioIds.preencheObjeto (listaIdDeclaracoes, PATH_XML_ID_DECS, true);
	  }
	catch (Exception e)
	  {
	    e.printStackTrace ();
	  }
      }
  }
  
  private static void testaDiretorioDados ()
  {
    String diretorioDadosApp = IRPFUtil.DIR_DADOS;
    String path = diretorioDadosApp;
    File flDados = new File (path);
    if (! flDados.exists ())
      flDados.mkdirs ();
  }
  
  public DeclaracaoIRPF recuperarDeclaracaoIRPF (String cpf) throws RepositorioXMLException
  {
    IdentificadorDeclaracao idAtual = getIdDeclaracao (cpf);
    DeclaracaoIRPF dec = null;
    if (repositorioDeclaracoes.temObjetoNegocioEmCache (idAtual.getPathArquivo ()))
      dec = (DeclaracaoIRPF) repositorioDeclaracoes.getObjeto (idAtual.getPathArquivo ());
    else
      {
	dec = new DeclaracaoIRPF (idAtual);
	if (repositorioDeclaracoes.preencheObjeto (dec, idAtual.getPathArquivo (), true) == null)
	  throw new RepositorioXMLException ("A declara\u00e7\u00e3o solicitada nao existe");
	dec.adicionaObservadoresCalculosLate ();
      }
    return dec;
  }
  
  public void salvarDeclaracao (DeclaracaoIRPF dec) throws RepositorioXMLException
  {
    IdentificadorDeclaracao idAtual = dec.getIdentificadorDeclaracao ();
    repositorioDeclaracoes.salvar (dec, idAtual.getPathArquivo ());
  }
  
  public void salvarDeclaracao (String cpf)
  {
    try
      {
	repositorioIds.salvar (getListaIdDeclaracoes (), PATH_XML_ID_DECS);
	IdentificadorDeclaracao idAtual = getIdDeclaracao (cpf);
	DeclaracaoIRPF dec = (DeclaracaoIRPF) repositorioDeclaracoes.getObjeto (idAtual.getPathArquivo ());
	repositorioDeclaracoes.salvar (dec, idAtual.getPathArquivo ());
      }
    catch (RepositorioException e)
      {
	e.printStackTrace ();
      }
  }
  
  public void abreDeclaracao (IdentificadorDeclaracao id) throws RepositorioXMLException
  {
    identificadorDeclaracao = id;
    if (repositorioDeclaracoes.temObjetoNegocioEmCache (id.getPathArquivo ()))
      declaracaoIRPF = (DeclaracaoIRPF) repositorioDeclaracoes.getObjeto (id.getPathArquivo ());
    else
      {
	DialogoOcupado diaOcupado = DialogoOcupado.exibeDialogo (0, 4, "Aguarde...");
	diaOcupado.atualiza ("Abrindo declara\u00e7\u00e3o...");
	declaracaoIRPF = new DeclaracaoIRPF (identificadorDeclaracao);
	diaOcupado.atualiza ();
	repositorioDeclaracoes.preencheObjeto (declaracaoIRPF, id.getPathArquivo (), true);
	diaOcupado.atualiza ();
	declaracaoIRPF.adicionaObservadoresCalculosLate ();
	diaOcupado.atualiza ();
	diaOcupado.finaliza ();
      }
  }
  
  public IdentificadorDeclaracao getIdentificadorDeclaracao ()
  {
    return identificadorDeclaracao;
  }
  
  public boolean existeDeclaracao (String cpf)
  {
    return getListaIdDeclaracoes ().existeCPFCadastrado (cpf);
  }
  
  public ColecaoIdDeclaracao getListaIdDeclaracoes ()
  {
    return listaIdDeclaracoes;
  }
  
  public void criarDeclaracao (IdentificadorDeclaracao id)
  {
    try
      {
	testaDiretorioDados ();
	listaIdDeclaracoes.recuperarLista ().add (id);
	repositorioIds.salvar (listaIdDeclaracoes, PATH_XML_ID_DECS);
	identificadorDeclaracao = id;
	DeclaracaoIRPF dec = new DeclaracaoIRPF (identificadorDeclaracao);
	dec.adicionaObservadoresCalculosLate ();
	repositorioDeclaracoes.salvar (dec, id.getPathArquivo ());
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public void excluirDeclaracao (IdentificadorDeclaracao id)
  {
    try
      {
	testaDiretorioDados ();
	listaIdDeclaracoes.removeCPF (id);
	repositorioIds.salvar (listaIdDeclaracoes, PATH_XML_ID_DECS);
	repositorioDeclaracoes.excluir (id.getPathArquivo ());
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public void excluirDeclaracao (List ids)
  {
    try
      {
	testaDiretorioDados ();
	listaIdDeclaracoes.removeCPF (ids);
	repositorioIds.salvar (listaIdDeclaracoes, PATH_XML_ID_DECS);
	Iterator it = ids.iterator ();
	while (it.hasNext ())
	  {
	    IdentificadorDeclaracao idDec = (IdentificadorDeclaracao) it.next ();
	    repositorioDeclaracoes.excluir (idDec.getPathArquivo ());
	  }
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public void salvaDeclaracaoAberta ()
  {
    try
      {
	repositorioIds.salvar (getListaIdDeclaracoes (), PATH_XML_ID_DECS);
	repositorioDeclaracoes.salvar (declaracaoIRPF, identificadorDeclaracao.getPathArquivo ());
      }
    catch (RepositorioException e)
      {
	e.printStackTrace ();
      }
  }
  
  public void fechaDeclaracao ()
  {
    declaracaoIRPF = null;
    identificadorDeclaracao = null;
  }
  
  public IdentificadorDeclaracao getIdDeclaracaoAberto ()
  {
    return identificadorDeclaracao;
  }
  
  public DeclaracaoIRPF getDeclaracaoAberta ()
  {
    return declaracaoIRPF;
  }
  
  public boolean existeDeclaracoes ()
  {
    if (getListaIdDeclaracoes ().recuperarLista ().size () > 0)
      return true;
    return false;
  }
  
  public IdentificadorDeclaracao getIdDeclaracao (String cpf)
  {
    Iterator it = getListaIdDeclaracoes ().recuperarLista ().iterator ();
    while (it.hasNext ())
      {
	IdentificadorDeclaracao id = (IdentificadorDeclaracao) it.next ();
	if (cpf.equals (id.getCpf ().asString ()))
	  return id;
      }
    return null;
  }
}
