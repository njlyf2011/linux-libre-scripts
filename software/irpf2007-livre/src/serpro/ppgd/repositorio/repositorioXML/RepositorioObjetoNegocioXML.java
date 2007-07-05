/* RepositorioObjetoNegocioXML - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.repositorioXML;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.IdDeclaracao;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.negocio.util.UtilitariosArquivo;
import serpro.ppgd.repositorio.RepositorioException;
import serpro.ppgd.repositorio.RepositorioObjetoNegocioIf;

public class RepositorioObjetoNegocioXML implements RepositorioObjetoNegocioIf
{
  private MapeamentoObjetoNegocioXML mapeamento;
  private HashtableIdDeclaracao objetosIRPFAbertos;
  private String diretorioDados;
  private Class classePrincipal;
  
  private RepositorioObjetoNegocioXML ()
  {
    objetosIRPFAbertos = new HashtableIdDeclaracao ();
    diretorioDados = FabricaUtilitarios.getPathCompletoDirDadosAplicacao ();
    classePrincipal = serpro.ppgd.negocio.impl.IdDeclaracaoImpl.class;
  }
  
  public RepositorioObjetoNegocioXML (String pClassePrincipal)
  {
    objetosIRPFAbertos = new HashtableIdDeclaracao ();
    diretorioDados = FabricaUtilitarios.getPathCompletoDirDadosAplicacao ();
    classePrincipal = serpro.ppgd.negocio.impl.IdDeclaracaoImpl.class;
    try
      {
	classePrincipal = Class.forName (pClassePrincipal);
      }
    catch (ClassNotFoundException classnotfoundexception)
      {
	/* empty */
      }
  }
  
  public void descarregar (IdDeclaracao id) throws RepositorioException
  {
    if (objetosIRPFAbertos.containsKey (id))
      objetosIRPFAbertos.remove (id);
  }
  
  public ObjetoNegocio recuperar (IdDeclaracao id) throws RepositorioException
  {
    if (objetosIRPFAbertos.containsKey (id))
      return (ObjetoNegocio) objetosIRPFAbertos.get (id);
    Document arquivoDados = leArquivo (id.getPathArquivo (diretorioDados));
    if (arquivoDados == null)
      throw new RepositorioException ("Arquivo de dados n\u00e3o encontrado.");
    ObjetoNegocio resultado = null;
    resultado = instanciaObjetoNegocio (classePrincipal, id);
    preencheObjetoNegocio (arquivoDados.getDocumentElement (), null, resultado);
    objetosIRPFAbertos.put (id, resultado);
    return resultado;
  }
  
  private void preencheObjetoNegocio (Element nodoAtual, Element nodoPai, ObjetoNegocio objetoNegocio) throws RepositorioXMLException
  {
    NamedNodeMap namedNodeMap = nodoAtual.getAttributes ();
    for (int i = 0; i < namedNodeMap.getLength (); i++)
      {
	Node item = namedNodeMap.item (i);
	try
	  {
	    Informacao info = (Informacao) FabricaUtilitarios.getValorFieldGenerico (item.getNodeName (), objetoNegocio);
	    info.setConteudo (item.getNodeValue ());
	  }
	catch (Exception exception)
	  {
	    /* empty */
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
		Object valObtido = FabricaUtilitarios.getValorFieldGenerico (nodo.getNodeName (), objetoNegocio);
		if (! (valObtido instanceof IdDeclaracao))
		  {
		    ObjetoNegocio obj = (ObjetoNegocio) valObtido;
		    preencheObjetoNegocio ((Element) nodo, nodoAtual, obj);
		  }
	      }
	  }
      }
  }
  
  private void preencheColecao (Element nodoAtual, Colecao colecao) throws RepositorioXMLException
  {
    NamedNodeMap namedNodeMap = nodoAtual.getAttributes ();
    for (int i = 0; i < namedNodeMap.getLength (); i++)
      {
	Node item = namedNodeMap.item (i);
	try
	  {
	    Informacao info = (Informacao) FabricaUtilitarios.getValorFieldGenerico (item.getNodeName (), colecao);
	    info.setConteudo (item.getNodeValue ());
	  }
	catch (Exception exception)
	  {
	    /* empty */
	  }
      }
    Class tipoItens = colecao.getTipoItens ();
    NodeList nodeList = nodoAtual.getChildNodes ();
    for (int i = 0; i < nodeList.getLength (); i++)
      {
	Node nodo = nodeList.item (i);
	if (nodo instanceof Element && nodo.getNodeName ().equals ("item"))
	  {
	    ObjetoNegocio obj = instanciaObjetoNegocio (tipoItens, colecao.getIdDeclaracao ());
	    colecao.recuperarLista ().add (obj);
	    preencheObjetoNegocio ((Element) nodo, nodoAtual, obj);
	  }
      }
  }
  
  private ObjetoNegocio instanciaObjetoNegocio (Class classeAInstanciar, IdDeclaracao id) throws RepositorioXMLException
  {
    ObjetoNegocio resultado = null;
    Object[] argumentosReais = { id };
    ObjetoNegocio objetonegocio;
    try
      {
	Class[] argumentosFormais = { Class.forName ((serpro.ppgd.negocio.IdDeclaracao.class).getName ()) };
	if (! Class.forName ((serpro.ppgd.negocio.ObjetoNegocio.class).getName ()).isAssignableFrom (classeAInstanciar))
	  throw new RepositorioXMLException ("Mapeamento incorreto: classe n\u00e3o \u00e9 ObjetoNegocio");
	Constructor construtor = classeAInstanciar.getConstructor (argumentosFormais);
	resultado = (ObjetoNegocio) construtor.newInstance (argumentosReais);
	objetonegocio = resultado;
      }
    catch (InvocationTargetException e)
      {
	LogPPGD.erro ("Exce\u00e7\u00e3o levantada por m\u00e9todo invocado  pela instrospec\u00e7\u00e3o: " + e.getMessage ());
	throw new RepositorioXMLException (e);
      }
    catch (Exception e)
      {
	LogPPGD.erro ("Mapeamento incorreto: classe n\u00e3o possui construtor (IdDeclaracao)");
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
  
  public ObjetoNegocio criar (IdDeclaracao id) throws RepositorioException
  {
    ObjetoNegocio resultado = null;
    if (id == null)
      throw new IllegalArgumentException ("RepositorioObjetoNegocioXML: argumento \u00e9 nulo.");
    if (objetosIRPFAbertos.containsKey (id))
      throw new IllegalArgumentException ("RepositorioObjetoNegocioXML: ObjetoNegocio solicitado j\u00e1 existe.");
    resultado = instanciaObjetoNegocio (classePrincipal, id);
    objetosIRPFAbertos.put (id, resultado);
    return resultado;
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
  
  private void preencheElementoXML (Document documentoXML, Element nodoAtual, Element nodoPai, ObjetoNegocio objetoNegocio)
  {
    if (nodoPai != null)
      nodoPai.appendChild (nodoAtual);
    Iterator itAtributos = FabricaUtilitarios.getAllFields (objetoNegocio.getClass ()).iterator ();
    while (itAtributos.hasNext ())
      {
	Field field = (Field) itAtributos.next ();
	Object valorAtributo = FabricaUtilitarios.getValorFieldGenerico (field.getName (), objetoNegocio);
	if (! (valorAtributo instanceof IdDeclaracao))
	  {
	    if (valorAtributo instanceof Informacao)
	      {
		if (valorAtributo != null && ((Informacao) valorAtributo).isAtributoPersistente ())
		  {
		    if (valorAtributo instanceof Codigo)
		      nodoAtual.setAttribute (field.getName (), ((Informacao) valorAtributo).asString ());
		    else
		      nodoAtual.setAttribute (field.getName (), ((Informacao) valorAtributo).getConteudoFormatado ());
		  }
	      }
	    else if (valorAtributo instanceof ObjetoNegocio)
	      {
		if (! ((ObjetoNegocio) valorAtributo).isPersistente ())
		  break;
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
  
  public void salvar (IdDeclaracao id) throws RepositorioException
  {
    if (objetosIRPFAbertos.containsKey (id))
      {
	ObjetoNegocio objetoIRPF = (ObjetoNegocio) objetosIRPFAbertos.get (id);
	try
	  {
	    Document cacheDados = obterDOM (objetoIRPF);
	    File arquivoXML = new File (id.getPathArquivo (diretorioDados));
	    FileOutputStream os = new FileOutputStream (arquivoXML);
	    StreamResult result = new StreamResult (os);
	    DOMSource source = new DOMSource (cacheDados);
	    TransformerFactory transFactory = TransformerFactory.newInstance ();
	    Transformer transformer = transFactory.newTransformer ();
	    transformer.transform (source, result);
	    os.close ();
	  }
	catch (TransformerConfigurationException tce)
	  {
	    LogPPGD.erro ("Erro de configura\u00e7\u00e3o da f\u00e1brica de transforma\u00e7\u00e3o DOM");
	    throw new RepositorioXMLException (tce);
	  }
	catch (TransformerException te)
	  {
	    LogPPGD.erro ("Erro de transforma\u00e7\u00e3o DOM-XML");
	    throw new RepositorioXMLException (te);
	  }
	catch (IOException ioe)
	  {
	    LogPPGD.erro ("Erro de IO");
	    throw new RepositorioXMLException (ioe);
	  }
      }
    else
      throw new IllegalArgumentException ("N\u00e3o h\u00e1 ObjetoNegocio correspondente a esse IdDeclaracao: " + id.getNiContribuinte ());
  }
  
  public void deletar (IdDeclaracao id) throws RepositorioException
  {
    String arq = id.getPathArquivo (diretorioDados);
    String path = UtilitariosArquivo.extraiPath (arq);
    File f = new File (arq);
    f.delete ();
    f = new File (path);
    f.delete ();
    if (objetosIRPFAbertos.containsKey (id))
      objetosIRPFAbertos.remove (id);
  }
  
}
