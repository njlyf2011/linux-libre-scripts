/* RepositorioXMLDefault - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.persistenciagenerica;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.repositorio.repositorioXML.RepositorioXMLException;

public class RepositorioXMLDefault implements RepositorioGenericoIf
{
  private Map cacheObjetosAbertos = new Hashtable ();
  
  public ObjetoNegocio getObjeto (String pathArquivoXML) throws RepositorioXMLException
  {
    FabricaUtilitarios.cacheTempToReflection.clear ();
    if (cacheObjetosAbertos.containsKey (pathArquivoXML))
      return (ObjetoNegocio) cacheObjetosAbertos.get (pathArquivoXML);
    Document arquivoDados = leArquivo (pathArquivoXML);
    if (arquivoDados == null)
      return null;
    String classe = arquivoDados.getDocumentElement ().getAttribute ("classeJava");
    ObjetoNegocio retorno = instanciaObjetoNegocio (classe);
    preencheObjetoNegocio (arquivoDados.getDocumentElement (), null, retorno);
    cacheObjetosAbertos.put (pathArquivoXML, retorno);
    return retorno;
  }
  
  public boolean temObjetoNegocioEmCache (String pathArquivoXML)
  {
    return cacheObjetosAbertos.containsKey (pathArquivoXML);
  }
  
  public boolean temObjetoPersistido (String pathArquivoXML)
  {
    File f = new File (pathArquivoXML);
    return f.exists ();
  }
  
  public ObjetoNegocio preencheObjeto (ObjetoNegocio obj, String pathArquivoXML, boolean armazenaEmCache) throws RepositorioXMLException
  {
    FabricaUtilitarios.cacheTempToReflection.clear ();
    Document arquivoDados = leArquivo (pathArquivoXML);
    if (arquivoDados == null)
      return null;
    String classe = arquivoDados.getDocumentElement ().getAttribute ("classeJava");
    preencheObjetoNegocio (arquivoDados.getDocumentElement (), null, obj);
    if (armazenaEmCache)
      cacheObjetosAbertos.put (pathArquivoXML, obj);
    return obj;
  }
  
  public void salvar (ObjetoNegocio obj, String pathArquivoXML) throws RepositorioXMLException
  {
    FabricaUtilitarios.cacheTempToReflection.clear ();
    if (! cacheObjetosAbertos.containsKey (pathArquivoXML))
      cacheObjetosAbertos.put (pathArquivoXML, obj);
    try
      {
	Document cacheDados = obterDOM (obj);
	File arquivoXML = new File (pathArquivoXML);
	FileOutputStream os = new FileOutputStream (arquivoXML);
	StreamResult result = new StreamResult (os);
	DOMSource source = new DOMSource (cacheDados);
	TransformerFactory transFactory = TransformerFactory.newInstance ();
	Transformer transformer = transFactory.newTransformer ();
	transformer.transform (source, result);
	os.close ();
      }
    catch (Exception e)
      {
	throw new RepositorioXMLException (e);
      }
  }
  
  public void excluir (String pathArquivoXML) throws RepositorioXMLException
  {
    String arq = pathArquivoXML;
    File f = new File (arq);
    f.delete ();
    if (cacheObjetosAbertos.containsKey (pathArquivoXML))
      cacheObjetosAbertos.remove (pathArquivoXML);
  }
  
  private void preencheObjetoNegocio (Element nodoAtual, Element nodoPai, ObjetoNegocio objetoNegocio) throws RepositorioXMLException
  {
    NamedNodeMap namedNodeMap = nodoAtual.getAttributes ();
    Iterator itFields = FabricaUtilitarios.getAllFields (objetoNegocio.getClass ()).iterator ();
    while (itFields.hasNext ())
      {
	Field f = (Field) itFields.next ();
	if (! Modifier.isTransient (f.getModifiers ()))
	  {
	    try
	      {
		Informacao info = (Informacao) FabricaUtilitarios.getValorFieldGenerico (f.getName (), objetoNegocio);
		Node item = namedNodeMap.getNamedItem (f.getName ());
		info.setConteudo (item.getNodeValue ());
	      }
	    catch (Exception exception)
	      {
		/* empty */
	      }
	  }
      }
    if (objetoNegocio instanceof Colecao)
      preencheColecao (nodoAtual, (Colecao) objetoNegocio);
    else
      {
	NodeList nodeList = nodoAtual.getChildNodes ();
	for (int i = 0; i < nodeList.getLength (); i++)
	  {
	    Node nodo = nodeList.item (i);
	    if (nodo instanceof Element)
	      {
		ObjetoNegocio obj = (ObjetoNegocio) FabricaUtilitarios.getValorFieldGenerico (nodo.getNodeName (), objetoNegocio);
		preencheObjetoNegocio ((Element) nodo, nodoAtual, obj);
	      }
	  }
      }
  }
  
  private void preencheColecao (Element nodoAtual, Colecao colecao) throws RepositorioXMLException
  {
    NamedNodeMap namedNodeMap = nodoAtual.getAttributes ();
    Iterator itFields = FabricaUtilitarios.getAllFields (colecao.getClass ()).iterator ();
    while (itFields.hasNext ())
      {
	Field f = (Field) itFields.next ();
	if (! Modifier.isTransient (f.getModifiers ()))
	  {
	    try
	      {
		Informacao info = (Informacao) FabricaUtilitarios.getValorFieldGenerico (f.getName (), colecao);
		Node item = namedNodeMap.getNamedItem (f.getName ());
		info.setConteudo (item.getNodeValue ());
	      }
	    catch (Exception exception)
	      {
		/* empty */
	      }
	  }
      }
    NodeList nodeList = nodoAtual.getChildNodes ();
    for (int i = 0; i < nodeList.getLength (); i++)
      {
	Node nodo = nodeList.item (i);
	if (nodo instanceof Element && nodo.getNodeName ().equals ("item"))
	  {
	    ObjetoNegocio obj = colecao.instanciaNovoObjeto ();
	    colecao.recuperarLista ().add (obj);
	    preencheObjetoNegocio ((Element) nodo, nodoAtual, obj);
	  }
      }
  }
  
  private void preencheElementoXML (Document documentoXML, Element nodoAtual, Element nodoPai, ObjetoNegocio objetoNegocio)
  {
    if (nodoPai != null)
      nodoPai.appendChild (nodoAtual);
    Iterator itAtributos = FabricaUtilitarios.getAllFields (objetoNegocio.getClass ()).iterator ();
    while (itAtributos.hasNext ())
      {
	Field field = (Field) itAtributos.next ();
	if (! Modifier.isTransient (field.getModifiers ()))
	  {
	    Object valorAtributo = FabricaUtilitarios.getValorFieldGenerico (field.getName (), objetoNegocio);
	    if (valorAtributo instanceof Informacao)
	      {
		if (((Informacao) valorAtributo).isAtributoPersistente ())
		  {
		    if (valorAtributo != null)
		      nodoAtual.setAttribute (field.getName (), ((Informacao) valorAtributo).getConteudoFormatado ());
		  }
	      }
	    else if (valorAtributo instanceof ObjetoNegocio)
	      {
		if (((ObjetoNegocio) valorAtributo).isPersistente ())
		  {
		    Element novoNodo = documentoXML.createElement (field.getName ());
		    preencheElementoXML (documentoXML, novoNodo, nodoAtual, (ObjetoNegocio) valorAtributo);
		    if (valorAtributo instanceof Colecao)
		      {
			novoNodo.setAttribute ("tipoItens", ((Colecao) valorAtributo).getTipoItens ().getName ());
			preencheElementoXMLColecao (documentoXML, novoNodo, nodoAtual, (Colecao) valorAtributo);
		      }
		  }
	      }
	  }
      }
  }
  
  private void preencheElementoXMLColecao (Document documentoXML, Element nodoAtual, Element nodoPai, Colecao colecao)
  {
    Iterator itItems = colecao.recuperarLista ().iterator ();
    while (itItems.hasNext ())
      {
	Element novoNodo = documentoXML.createElement ("item");
	ObjetoNegocio obj = (ObjetoNegocio) itItems.next ();
	preencheElementoXML (documentoXML, novoNodo, nodoAtual, obj);
      }
  }
  
  private Document obterDOM (ObjetoNegocio objetoNegocio) throws RepositorioXMLException
  {
    Document declaracaoDOM = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
    factory.setValidating (false);
    Document document;
    try
      {
	DocumentBuilder builder = factory.newDocumentBuilder ();
	declaracaoDOM = builder.newDocument ();
	Element root = declaracaoDOM.createElement ("classe");
	declaracaoDOM.appendChild (root);
	root.setAttribute ("classeJava", objetoNegocio.getClass ().getName ());
	root.setAttribute ("xmlns", ConstantesGlobais.XMLNS);
	if (objetoNegocio instanceof Colecao)
	  {
	    root.setAttribute ("tipoItens", ((Colecao) objetoNegocio).getTipoItens ().getName ());
	    preencheElementoXMLColecao (declaracaoDOM, root, null, (Colecao) objetoNegocio);
	  }
	else
	  preencheElementoXML (declaracaoDOM, root, null, objetoNegocio);
	declaracaoDOM.normalize ();
	document = declaracaoDOM;
      }
    catch (ParserConfigurationException e)
      {
	LogPPGD.erro ("Erro de configura\u00e7\u00e3o da f\u00e1brica DOM: " + e.getMessage ());
	throw new RepositorioXMLException (e);
      }
    return document;
  }
  
  private ObjetoNegocio instanciaObjetoNegocio (String pClasseAInstanciar) throws RepositorioXMLException
  {
    ObjetoNegocio resultado = null;
    ObjetoNegocio objetonegocio;
    try
      {
	Class classeAInstanciar = Class.forName (pClasseAInstanciar);
	if (! Class.forName ((serpro.ppgd.negocio.ObjetoNegocio.class).getName ()).isAssignableFrom (classeAInstanciar))
	  throw new RepositorioXMLException ("A classe n\u00e3o \u00e9 um objeto de neg\u00f3cio");
	Constructor construtor = classeAInstanciar.getConstructor (new Class[0]);
	resultado = (ObjetoNegocio) construtor.newInstance (new Object[0]);
	objetonegocio = resultado;
      }
    catch (InvocationTargetException e)
      {
	throw new RepositorioXMLException (e);
      }
    catch (Exception e)
      {
	throw new RepositorioXMLException (e);
      }
    return objetonegocio;
  }
  
  private Document leArquivo (String pathArquivoDados)
  {
    Document declaracaoDOM = null;
    if (pathArquivoDados == null)
      return null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
    factory.setValidating (false);
    try
      {
	DocumentBuilder builder = factory.newDocumentBuilder ();
	declaracaoDOM = builder.parse (pathArquivoDados);
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
    return declaracaoDOM;
  }
  
  
  static
  {
    FabricaUtilitarios.usaCacheParaReflexao = true;
  }
}
