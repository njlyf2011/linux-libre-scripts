/* RepositorioIdXML - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.repositorioXML;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import serpro.ppgd.negocio.IdUsuario;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.repositorio.RepositorioIdIf;

public class RepositorioIdXML implements RepositorioIdIf
{
  private static final String NOME_RAIZ = "Ids";
  private static final String NOME_NODE = "IdUsuario";
  private List listIds;
  private String nomeClasseId;
  
  private RepositorioIdXML ()
  {
    nomeClasseId = (serpro.ppgd.negocio.impl.IdUsuarioImpl.class).getName ();
  }
  
  public RepositorioIdXML (String pNomeClasseId)
  {
    nomeClasseId = (serpro.ppgd.negocio.impl.IdUsuarioImpl.class).getName ();
    if (pNomeClasseId != null)
      nomeClasseId = pNomeClasseId;
  }
  
  public IdUsuario criaInstanciaIdConcreto ()
  {
    IdUsuario idusuario;
    try
      {
	Object retorno = Class.forName (nomeClasseId).newInstance ();
	idusuario = (IdUsuario) retorno;
      }
    catch (Exception e)
      {
	throw new IllegalArgumentException ("Erro na instancia\u00e7\u00e3o do IdUsuario Concreto :" + e.getMessage ());
      }
    return idusuario;
  }
  
  public IdUsuario criarId (IdUsuario pId) throws RepositorioXMLException
  {
    IdUsuario id = recuperarId (pId);
    if (id == null)
      {
	id = pId;
	listIds.add (id);
      }
    else
      throw new RepositorioXMLException ("J\u00e1 existe um IdUsuario para este NI - " + pId);
    return id;
  }
  
  public IdUsuario criarIdNaoPersistido (String niContribuinte)
  {
    IdUsuario id = criaInstanciaIdConcreto ();
    id.getNiContribuinte ().setConteudo (niContribuinte);
    return id;
  }
  
  public IdUsuario recuperarId (IdUsuario pId)
  {
    recuperarIds ();
    for (int i = 0; i < listIds.size (); i++)
      {
	IdUsuario id = (IdUsuario) listIds.get (i);
	if (id.equals (pId))
	  return id;
      }
    return null;
  }
  
  public void preencherAtributosEspecificos (IdUsuario pId, Element pElemento)
  {
    FabricaUtilitarios.preencherObjetoComAtributosXml (pId, pElemento);
  }
  
  public List recuperarIds ()
  {
    if (listIds == null)
      {
	listIds = new Vector ();
	Document idsDOM = leArquivo (getPathArquivo ());
	if (idsDOM != null)
	  {
	    Element element = idsDOM.getDocumentElement ();
	    if (element.hasChildNodes ())
	      {
		NodeList filhos = element.getChildNodes ();
		for (int i = 0; i < filhos.getLength (); i++)
		  {
		    if (filhos.item (i).getNodeType () == 1)
		      {
			Element elementFilho = (Element) filhos.item (i);
			IdUsuario id = criaInstanciaIdConcreto ();
			preencherAtributosEspecificos (id, elementFilho);
			listIds.add (id);
		      }
		  }
	      }
	  }
      }
    return listIds;
  }
  
  public void removerId (IdUsuario id) throws RepositorioXMLException
  {
    for (int i = 0; i < listIds.size (); i++)
      {
	if (id.equals ((IdUsuario) listIds.get (i)))
	  {
	    listIds.remove (i);
	    return;
	  }
      }
    throw new RepositorioXMLException ("IdUsuario n\u00e3o existe");
  }
  
  public void persisteAtributosEspecificos (IdUsuario pId, Element pElemento)
  {
    FabricaUtilitarios.preencherXMLComAtributosDoObjeto (pId, pElemento);
  }
  
  public void salvar () throws RepositorioXMLException
  {
    Document idsDOM = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
    factory.setValidating (false);
    try
      {
	DocumentBuilder builder = factory.newDocumentBuilder ();
	idsDOM = builder.newDocument ();
	Element root = idsDOM.createElement ("Ids");
	idsDOM.appendChild (root);
	root.setAttribute ("xmlns", ConstantesGlobais.XMLNS);
	for (int i = 0; i < listIds.size (); i++)
	  {
	    Element node = idsDOM.createElement ("IdUsuario");
	    root.appendChild (node);
	    persisteAtributosEspecificos ((IdUsuario) listIds.get (i), node);
	  }
      }
    catch (ParserConfigurationException e)
      {
	String msg = "Erro de configura\u00e7\u00e3o da f\u00e1brica DOM: " + e.getMessage ();
	LogPPGD.erro (msg);
	throw new RepositorioXMLException (msg);
      }
    idsDOM.normalize ();
    File arquivoXML = new File (getPathArquivo ());
    try
      {
	FileOutputStream os = new FileOutputStream (arquivoXML);
	StreamResult result = new StreamResult (os);
	DOMSource source = new DOMSource (idsDOM);
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
  }
  
  public void salvar (IdUsuario id) throws RepositorioXMLException
  {
    Document idsDOM = null;
    if (recuperarId (id) == null)
      {
	String msg = "id nao localizado";
	LogPPGD.erro (msg);
	throw new RepositorioXMLException (msg);
      }
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
    factory.setValidating (false);
    try
      {
	DocumentBuilder builder = factory.newDocumentBuilder ();
	idsDOM = builder.newDocument ();
	Element root = idsDOM.createElement ("Ids");
	idsDOM.appendChild (root);
	root.setAttribute ("xmlns", ConstantesGlobais.XMLNS);
	for (int i = 0; i < listIds.size (); i++)
	  {
	    Element node = idsDOM.createElement ("IdUsuario");
	    root.appendChild (node);
	    persisteAtributosEspecificos ((IdUsuario) listIds.get (i), node);
	  }
      }
    catch (ParserConfigurationException e)
      {
	String msg = "Erro de configura\u00e7\u00e3o da f\u00e1brica DOM: " + e.getMessage ();
	LogPPGD.erro (msg);
	throw new RepositorioXMLException (msg);
      }
    idsDOM.normalize ();
    File arquivoXML = new File (getPathArquivo ());
    try
      {
	FileOutputStream os = new FileOutputStream (arquivoXML);
	StreamResult result = new StreamResult (os);
	DOMSource source = new DOMSource (idsDOM);
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
  }
  
  private String getPathArquivo ()
  {
    String path = FabricaUtilitarios.getPathCompletoDirDadosAplicacao ();
    File file = new File (path);
    file.mkdirs ();
    file = new File (path + "/ids.xml");
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
    Document idsDOM = null;
    if (pathArquivoDados != null)
      {
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
	factory.setValidating (false);
	try
	  {
	    DocumentBuilder builder = factory.newDocumentBuilder ();
	    idsDOM = builder.parse (pathArquivoDados);
	    LogPPGD.debug (idsDOM.getImplementation ().toString ());
	  }
	catch (SAXParseException e)
	  {
	    LogPPGD.debug ("Erro de parsing de " + e.getSystemId () + ". linha " + e.getLineNumber () + ": " + e.getMessage ());
	  }
	catch (SAXException e)
	  {
	    Exception x = e;
	    if (e.getException () != null)
	      x = e.getException ();
	    LogPPGD.debug ("Erro de parsing: " + x.getMessage ());
	  }
	catch (ParserConfigurationException e)
	  {
	    LogPPGD.debug ("Erro de configura\u00e7\u00e3o da f\u00e1brica DOM: " + e.getMessage ());
	  }
	catch (IOException e)
	  {
	    LogPPGD.debug ("Erro de I/O: " + e.getMessage ());
	  }
      }
    return idsDOM;
  }
  
}
