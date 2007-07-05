/* RepositorioIdDeclaracaoXML - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.repositorioXML;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.IdDeclaracao;
import serpro.ppgd.negocio.IdUsuario;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.repositorio.RepositorioIdDeclaracaoIf;
import serpro.ppgd.repositorio.RepositorioIdIf;

public class RepositorioIdDeclaracaoXML implements RepositorioIdDeclaracaoIf
{
  private static final String NOME_RAIZ = "IdDeclaracoes";
  private static final String NOME_NODE = "IdDeclaracao";
  private List listIdDeclaracoes;
  private String nomeClasseIdDeclaracao;
  private RepositorioIdIf repositorioIdIf;
  
  private RepositorioIdDeclaracaoXML ()
  {
    nomeClasseIdDeclaracao = (serpro.ppgd.negocio.impl.IdDeclaracaoImpl.class).getName ();
    repositorioIdIf = null;
  }
  
  public RepositorioIdDeclaracaoXML (String pNomeClasseIdDeclaracao, RepositorioIdIf pRepositorioId)
  {
    nomeClasseIdDeclaracao = (serpro.ppgd.negocio.impl.IdDeclaracaoImpl.class).getName ();
    repositorioIdIf = null;
    if (pNomeClasseIdDeclaracao != null)
      nomeClasseIdDeclaracao = pNomeClasseIdDeclaracao;
    if (pRepositorioId == null)
      throw new IllegalArgumentException ("\u00c9 necess\u00e1rio passar uma refer\u00eancia n\u00e3o nula do reposit\u00f3rio de Ids");
    repositorioIdIf = pRepositorioId;
  }
  
  public IdDeclaracao criaInstanciaIdDeclaracaoConcreto (IdUsuario pId)
  {
    IdDeclaracao iddeclaracao;
    try
      {
	Class[] argumentosFormais = { serpro.ppgd.negocio.IdUsuario.class };
	Object[] argumentosReais = { pId };
	Class classe = Class.forName (nomeClasseIdDeclaracao);
	Constructor construtor = classe.getConstructor (argumentosFormais);
	Object retorno = construtor.newInstance (argumentosReais);
	iddeclaracao = (IdDeclaracao) retorno;
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	throw new IllegalArgumentException ("Erro na instancia\u00e7\u00e3o do IdDeclaracao Concreto :" + e.getMessage ());
      }
    return iddeclaracao;
  }
  
  public IdDeclaracao criarIdDeclaracao (IdUsuario id)
  {
    IdDeclaracao idDeclaracao = criarIdDeclaracaoNaoPersistido (id);
    listIdDeclaracoes.add (idDeclaracao);
    return idDeclaracao;
  }
  
  public IdDeclaracao criarIdDeclaracao (IdDeclaracao idDeclaracao)
  {
    listIdDeclaracoes.add (idDeclaracao);
    return idDeclaracao;
  }
  
  public IdDeclaracao criarIdDeclaracaoNaoPersistido (IdUsuario id)
  {
    IdDeclaracao idDeclaracao = criaInstanciaIdDeclaracaoConcreto (id);
    idDeclaracao.getExercicio ().setConteudo (ConstantesGlobais.EXERCICIO);
    idDeclaracao.getTipo ().setConteudo ("DECLARA\u00c7\u00c3O DE AJUSTE ANUAL");
    return idDeclaracao;
  }
  
  public boolean existeIdDeclaracao (IdDeclaracao idDeclaracao)
  {
    for (int i = 0; i < listIdDeclaracoes.size (); i++)
      {
	if (((IdDeclaracao) listIdDeclaracoes.get (i)).equals (idDeclaracao))
	  return true;
      }
    return false;
  }
  
  public IdDeclaracao retornaIdDeclaracaoPersistido (IdDeclaracao idDeclaracao)
  {
    for (int i = 0; i < listIdDeclaracoes.size (); i++)
      {
	if (((IdDeclaracao) listIdDeclaracoes.get (i)).equals (idDeclaracao))
	  return (IdDeclaracao) listIdDeclaracoes.get (i);
      }
    return null;
  }
  
  public void removerIdDeclaracao (IdDeclaracao idDeclaracao) throws RepositorioXMLException
  {
    for (int i = 0; i < listIdDeclaracoes.size (); i++)
      {
	IdDeclaracao idDecl = (IdDeclaracao) listIdDeclaracoes.get (i);
	if (idDeclaracao.equals (idDecl))
	  {
	    try
	      {
		salvar ((IdDeclaracao) listIdDeclaracoes.remove (i));
	      }
	    catch (IllegalArgumentException illegalargumentexception)
	      {
		/* empty */
	      }
	    return;
	  }
      }
    throw new RepositorioXMLException ("IdDeclara\u00e7\u00e3o n\u00e3o localizado");
  }
  
  public List recuperarIdDeclaracoes (List listIds)
  {
    if (listIdDeclaracoes == null)
      {
	listIdDeclaracoes = new Vector ();
	Document idDeclaracaoDOM = leArquivo (getPathArquivo ());
	if (idDeclaracaoDOM != null)
	  {
	    Element element = idDeclaracaoDOM.getDocumentElement ();
	    if (element.hasChildNodes ())
	      {
		NodeList filhos = element.getChildNodes ();
		for (int i = 0; i < filhos.getLength (); i++)
		  {
		    if (filhos.item (i).getNodeType () == 1)
		      {
			Element elementFilho = (Element) filhos.item (i);
			IdUsuario id = repositorioIdIf.criaInstanciaIdConcreto ();
			FabricaUtilitarios.preencherObjetoComAtributosXml (id, elementFilho);
			id = repositorioIdIf.recuperarId (id);
			IdDeclaracao idDeclaracao = criaInstanciaIdDeclaracaoConcreto (id);
			preencherAtributosEspecificos (idDeclaracao, elementFilho);
			if (idDeclaracao.existeArquivoDeclaracao (FabricaUtilitarios.getPathCompletoDirDadosAplicacao ()))
			  listIdDeclaracoes.add (idDeclaracao);
		      }
		  }
	      }
	  }
      }
    return listIdDeclaracoes;
  }
  
  public void preencherAtributosEspecificos (IdDeclaracao idDecl, Element pElemento)
  {
    FabricaUtilitarios.preencherObjetoComAtributosXml (idDecl, pElemento);
  }
  
  public void persisteAtributosEspecificos (IdDeclaracao idDecl, Element pElemento)
  {
    FabricaUtilitarios.preencherXMLComAtributosDoObjeto (idDecl, pElemento);
  }
  
  public void salvar (IdDeclaracao idDeclaracao) throws RepositorioXMLException
  {
    Document idDeclaracaoDOM = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
    factory.setValidating (false);
    try
      {
	DocumentBuilder builder = factory.newDocumentBuilder ();
	idDeclaracaoDOM = builder.newDocument ();
	Element root = idDeclaracaoDOM.createElement ("IdDeclaracoes");
	idDeclaracaoDOM.appendChild (root);
	root.setAttribute ("xmlns", ConstantesGlobais.XMLNS);
	for (int i = 0; i < listIdDeclaracoes.size (); i++)
	  {
	    IdDeclaracao idDecl = (IdDeclaracao) listIdDeclaracoes.get (i);
	    if (idDecl.equals (idDecl))
	      {
		Element node = idDeclaracaoDOM.createElement ("IdDeclaracao");
		root.appendChild (node);
		FabricaUtilitarios.preencherXMLComAtributosDoObjeto (idDecl.getId (), node);
		persisteAtributosEspecificos (idDecl, node);
	      }
	  }
      }
    catch (ParserConfigurationException e)
      {
	String msg = "Erro de configura\u00e7\u00e3o da f\u00e1brica DOM: " + e.getMessage ();
	LogPPGD.erro (msg);
	throw new RepositorioXMLException (msg);
      }
    idDeclaracaoDOM.normalize ();
    File arquivoXML = new File (getPathArquivo ());
    try
      {
	FileOutputStream os = new FileOutputStream (arquivoXML);
	StreamResult result = new StreamResult (os);
	DOMSource source = new DOMSource (idDeclaracaoDOM);
	TransformerFactory transFactory = TransformerFactory.newInstance ();
	Transformer transformer = transFactory.newTransformer ();
	transformer.transform (source, result);
	os.close ();
      }
    catch (FileNotFoundException e)
      {
	String msg = "Arquivo nao localizado: " + arquivoXML.getPath () + " - " + e.getMessage ();
	LogPPGD.erro (msg);
	throw new RepositorioXMLException (msg);
      }
    catch (TransformerConfigurationException e)
      {
	String msg = "Erro de configura\u00e7\u00e3o da f\u00e1brica de transforma\u00e7\u00e3o DOM: " + e.getMessage ();
	LogPPGD.erro (msg);
	throw new RepositorioXMLException (msg);
      }
    catch (TransformerException e)
      {
	String msg = "Erro de transforma\u00e7\u00e3o DOM-XML: " + e.getMessage ();
	LogPPGD.erro (msg);
	throw new RepositorioXMLException (msg);
      }
    catch (IOException e)
      {
	String msg = "Erro de IO: " + e.getMessage ();
	LogPPGD.erro (msg);
	throw new RepositorioXMLException (msg);
      }
    if (! existeIdDeclaracao (idDeclaracao))
      {
	String msg = "idDeclaracao nao localizado";
	LogPPGD.debug (msg);
	throw new IllegalArgumentException (msg);
      }
  }
  
  private IdUsuario retornaId (List listIds, IdUsuario pId)
  {
    for (int i = 0; i < listIds.size (); i++)
      {
	IdUsuario id = (IdUsuario) listIds.get (i);
	if (pId.equals (id))
	  return id;
      }
    return null;
  }
  
  private String getPathArquivo ()
  {
    String path = FabricaUtilitarios.getPathCompletoDirDadosAplicacao ();
    File file = new File (path);
    file.mkdirs ();
    file = new File (path + "/idDeclaracoes.xml");
    try
      {
	file.createNewFile ();
      }
    catch (IOException e)
      {
	return null;
      }
    return file.getPath ();
  }
  
  private Document leArquivo (String pathArquivoDados)
  {
    Document idDeclaracaoDOM = null;
    if (pathArquivoDados != null)
      {
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
	factory.setValidating (false);
	try
	  {
	    DocumentBuilder builder = factory.newDocumentBuilder ();
	    idDeclaracaoDOM = builder.parse (pathArquivoDados);
	    LogPPGD.debug (idDeclaracaoDOM.getImplementation ().toString ());
	  }
	catch (SAXParseException e)
	  {
	    LogPPGD.erro ("Erro de parsing de " + e.getSystemId () + ". linha " + e.getLineNumber () + ": " + e.getMessage ());
	  }
	catch (SAXException e)
	  {
	    Exception x = e;
	    if (e.getException () != null)
	      x = e.getException ();
	    LogPPGD.erro ("Erro de parsing: " + x.getMessage ());
	  }
	catch (ParserConfigurationException e)
	  {
	    LogPPGD.erro ("Erro de configura\u00e7\u00e3o da f\u00e1brica DOM: " + e.getMessage ());
	  }
	catch (IOException e)
	  {
	    LogPPGD.erro ("Erro de I/O: " + e.getMessage ());
	  }
      }
    return idDeclaracaoDOM;
  }
  
}
