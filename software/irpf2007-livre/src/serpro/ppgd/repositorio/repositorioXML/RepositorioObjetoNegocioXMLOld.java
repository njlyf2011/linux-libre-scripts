/* RepositorioObjetoNegocioXMLOld - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.repositorioXML;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.IdDeclaracao;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.negocio.util.UtilitariosArquivo;
import serpro.ppgd.repositorio.RepositorioException;
import serpro.ppgd.repositorio.RepositorioObjetoNegocioIf;

public class RepositorioObjetoNegocioXMLOld implements RepositorioObjetoNegocioIf
{
  private static final String NOME_RAIZ = "Mapeamento";
  private MapeamentoObjetoNegocioXML mapeamento;
  private HashtableIdDeclaracao objetosIRPFAbertos = new HashtableIdDeclaracao ();
  private String diretorioDados;
  
  public RepositorioObjetoNegocioXMLOld (String arquivoMapeamento, String diretorioDados)
  {
    Document mapeamentoDom = carregarDOM (arquivoMapeamento);
    if (mapeamentoDom == null)
      LogPPGD.erro ("Mapeamento inv\u00e1lido");
    mapeamento = MapeamentoObjetoNegocioXML.getMapeamento (mapeamentoDom);
    this.diretorioDados = diretorioDados;
  }
  
  public void descarregar (IdDeclaracao id) throws RepositorioException
  {
    if (objetosIRPFAbertos.containsKey (id))
      objetosIRPFAbertos.remove (id);
  }
  
  public ObjetoNegocio criar (IdDeclaracao id) throws RepositorioException
  {
    ObjetoNegocio resultado = null;
    if (id == null)
      throw new IllegalArgumentException ("RepositorioObjetoNegocioXML: argumento \u00e9 nulo.");
    if (objetosIRPFAbertos.containsKey (id))
      throw new IllegalArgumentException ("RepositorioObjetoNegocioXML: ObjetoNegocio solicitado j\u00e1 existe.");
    ObjetoNegocio objetonegocio;
    try
      {
	Class classeSolicitada = mapeamento.getAdaptadorXMLObjeto ("Mapeamento").getClasseJava ();
	resultado = instanciaObjetoNegocio (classeSolicitada, id);
	objetosIRPFAbertos.put (id, resultado);
	objetonegocio = resultado;
      }
    catch (ClassNotFoundException e)
      {
	LogPPGD.erro ("RepositorioObjetoNegocioXML: classe a ser criada n\u00e3o existe.");
	throw new RepositorioXMLException (e);
      }
    return objetonegocio;
  }
  
  public ObjetoNegocio recuperar (IdDeclaracao id) throws RepositorioException
  {
    if (objetosIRPFAbertos.containsKey (id))
      return (ObjetoNegocio) objetosIRPFAbertos.get (id);
    Document arquivoDados = leArquivo (id.getPathArquivo (diretorioDados));
    if (arquivoDados == null)
      throw new RepositorioException ("Arquivo de dados n\u00e3o encontrado.");
    ObjetoNegocio resultado;
    try
      {
	resultado = instanciaObjetoNegocio (mapeamento.getAdaptadorXMLObjeto ("Mapeamento").getClasseJava (), id);
      }
    catch (ClassNotFoundException e)
      {
	LogPPGD.erro ("classe n\u00e3o encontrada.");
	throw new RepositorioException (e);
      }
    if (arquivoDados != null)
      {
	caminhaArvoreDOM (arquivoDados.getDocumentElement (), resultado);
	objetosIRPFAbertos.put (id, resultado);
	return resultado;
      }
    return resultado;
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
  
  private Document carregarDOM (String arquivoMapeamento)
  {
    Document mapeamentoDOM = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
    factory.setValidating (false);
    try
      {
	DocumentBuilder builder = factory.newDocumentBuilder ();
	mapeamentoDOM = builder.parse (UtilitariosArquivo.getResource (arquivoMapeamento, this.getClass ()));
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
    catch (Throwable e)
      {
	e.printStackTrace ();
	System.exit (1);
      }
    return mapeamentoDOM;
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
  
  private ObjetoNegocio determinaConteiner (ObjetoNegocio objetoRaiz, String nomeXMLConteiner) throws RepositorioXMLException
  {
    if (nomeXMLConteiner.length () == 0)
      return objetoRaiz;
    AdaptadorXMLObjeto adaptadorDoConteiner = mapeamento.getAdaptadorXMLObjeto (nomeXMLConteiner);
    ObjetoNegocio conteiner = determinaConteiner (objetoRaiz, adaptadorDoConteiner.getConteiner ());
    String nomeMetodoAcesso = adaptadorDoConteiner.getMetodoAcesso ();
    if (nomeMetodoAcesso.equals ("this"))
      return conteiner;
    ObjetoNegocio objetonegocio;
    try
      {
	Method metodoAcesso = conteiner.getClass ().getMethod (nomeMetodoAcesso, null);
	objetonegocio = (ObjetoNegocio) metodoAcesso.invoke (conteiner, null);
      }
    catch (NoSuchMethodException e)
      {
	LogPPGD.erro ("Mapeamento incorreto");
	throw new RepositorioXMLException (e);
      }
    catch (IllegalAccessException e)
      {
	LogPPGD.erro ("Acesso ilegal " + e.getStackTrace ());
	throw new RepositorioXMLException (e);
      }
    catch (InvocationTargetException e)
      {
	LogPPGD.erro ("Exce\u00e7\u00e3o levantada por m\u00e9todo invocado  pela instrospec\u00e7\u00e3o: " + e.getMessage ());
	throw new RepositorioXMLException (e);
      }
    return objetonegocio;
  }
  
  private ObjetoNegocio determinaObjeto (ObjetoNegocio conteiner, AdaptadorXMLObjeto adaptador) throws RepositorioXMLException
  {
    try
      {
	if (! (conteiner instanceof Colecao))
	  {
	    Class classeConteiner;
	    String nomeMetodoAcesso;
	    classeConteiner = conteiner.getClass ();
	    nomeMetodoAcesso = adaptador.getMetodoAcesso ();
	    if (! nomeMetodoAcesso.equals ("this"))
	      {
		Method metodoAcesso = classeConteiner.getMethod (nomeMetodoAcesso, null);
		return (ObjetoNegocio) metodoAcesso.invoke (conteiner, null);
	      }
	    return conteiner;
	  }

	ObjetoNegocio objetoIRPF = null;
	Colecao conteinerIRPF = (Colecao) conteiner;
	objetoIRPF = instanciaObjetoNegocio (adaptador.getClasseJava (), conteinerIRPF.getIdDeclaracao ());
	((Colecao) conteiner).recuperarLista ().add (objetoIRPF);
	return objetoIRPF;
      }
    catch (IllegalAccessException e)
      {
	LogPPGD.erro ("Acesso ilegal");
	throw new RepositorioXMLException (e);
      }
    catch (InvocationTargetException e)
      {
	LogPPGD.erro ("Exce\u00e7\u00e3o levantada por m\u00e9todo invocado pela instrospec\u00e7\u00e3o: " + e.getMessage ());
	throw new RepositorioXMLException (e);
      }
    catch (ClassNotFoundException e)
      {
	LogPPGD.erro ("Exce\u00e7\u00e3o levantada em instacia\u00e7\u00e3o de objeto: " + e.getMessage ());
	throw new RepositorioXMLException (e);
      }
    catch (SecurityException e)
      {
	LogPPGD.erro ("Security manager n\u00e3o permitiu acesso a instrospec\u00e7\u00e3o: " + e.getMessage ());
	throw new RepositorioXMLException (e);
      }
    catch (NoSuchMethodException e)
      {
	LogPPGD.erro ("M\u00e9todo n\u00e3o existe: " + e.getMessage ());
	throw new RepositorioXMLException (e);
      }
  }
  
  private void populaObjetoIRPF (ObjetoNegocio objetoIRPF, Element nodeXML) throws RepositorioXMLException
  {
    AdaptadorXMLObjeto adaptador = mapeamento.getAdaptadorXMLObjeto (determinaNomeCompleto (nodeXML));
    if (adaptador != null && adaptador.isMarshalling ())
      {
	Iterator iteratorAtributos = null;
	if (adaptador.getAtributos () != null)
	  iteratorAtributos = adaptador.getAtributos ().iterator ();
	else
	  return;
	while (iteratorAtributos.hasNext ())
	  {
	    AdaptadorXMLAtributo adaptadorAtributo = (AdaptadorXMLAtributo) iteratorAtributos.next ();
	    if (adaptadorAtributo.isMarshalling ())
	      {
		try
		  {
		    Method metodoAcesso = objetoIRPF.getClass ().getMethod (adaptadorAtributo.getMetodoAcesso (), null);
		    Informacao atributo = (Informacao) metodoAcesso.invoke (objetoIRPF, null);
		    String dado = nodeXML.getAttribute (adaptadorAtributo.getElementoXML ());
		    if (dado != null && dado.length () > 0)
		      atributo.setConteudo (dado);
		    else
		      LogPPGD.debug ("Atributo " + adaptador.getElementoXML () + "." + adaptadorAtributo.getElementoXML () + " vazio ou inexistente.");
		  }
		catch (NoSuchMethodException e)
		  {
		    LogPPGD.erro ("Mapeamento incorreto: m\u00e9todo inexistente");
		    throw new RepositorioXMLException (e);
		  }
		catch (IllegalAccessException e)
		  {
		    LogPPGD.erro ("Acesso ilegal");
		    throw new RepositorioXMLException (e);
		  }
		catch (InvocationTargetException e)
		  {
		    LogPPGD.erro ("Exce\u00e7\u00e3o levantada por m\u00e9todo invocado  pela instrospec\u00e7\u00e3o: " + e.getMessage ());
		    throw new RepositorioXMLException (e);
		  }
	      }
	  }
      }
  }
  
  private String determinaNomeCompleto (Node node)
  {
    if (node.getParentNode () != node.getOwnerDocument ().getDocumentElement ())
      return determinaNomeCompleto (node.getParentNode ()) + "." + node.getNodeName ();
    return node.getNodeName ();
  }
  
  private void transformaNodoXML2ObjetoIRPF (ObjetoNegocio objetoRaiz, Element nodeXML) throws RepositorioXMLException
  {
    String nomeCompletoNodo = determinaNomeCompleto (nodeXML);
    LogPPGD.debug ("Processamento do n\u00f3: " + nomeCompletoNodo);
    AdaptadorXMLObjeto adaptador = mapeamento.getAdaptadorXMLObjeto (nomeCompletoNodo);
    if (adaptador != null && adaptador.isMarshalling ())
      {
	LogPPGD.debug ("Encontrado adaptador (c/ marshalling) de : " + nomeCompletoNodo + " cujo conteiner \u00e9: " + adaptador.getConteiner ());
	ObjetoNegocio conteiner = determinaConteiner (objetoRaiz, adaptador.getConteiner ());
	ObjetoNegocio objetoIRPF = determinaObjeto (conteiner, adaptador);
	populaObjetoIRPF (objetoIRPF, nodeXML);
      }
  }
  
  private void caminhaArvoreDOM (Element dados, ObjetoNegocio objetoRaiz) throws RepositorioXMLException
  {
    if (dados.hasChildNodes ())
      {
	NodeList filhos = dados.getChildNodes ();
	for (int i = 0; i < filhos.getLength (); i++)
	  {
	    if (filhos.item (i).getNodeType () == 1)
	      {
		Element nodeFilho = (Element) filhos.item (i);
		transformaNodoXML2ObjetoIRPF (objetoRaiz, nodeFilho);
		caminhaArvoreDOM (nodeFilho, objetoRaiz);
	      }
	  }
      }
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
  
  private Element getElement (Element ancestral, String nome)
  {
    NodeList lista = ancestral.getElementsByTagName (nome);
  while_36_:
    do
      {
	if (lista != null && lista.getLength () > 0)
	  {
	    int i = 0;
	    Element resultado;
	    do
	      {
		if (i >= lista.getLength ())
		  break while_36_;
		resultado = (Element) lista.item (i++);
	      }
	    while (resultado.getParentNode () != ancestral);
	    return resultado;
	  }
      }
    while (false);
    Element resultado = ancestral.getOwnerDocument ().createElement (nome);
    ancestral.appendChild (resultado);
    return resultado;
  }
  
  private Element getParent (Element raiz, String chaveMapeamento)
  {
    StringTokenizer tokenizador = new StringTokenizer (chaveMapeamento, ".");
    int qtdTokens = tokenizador.countTokens ();
    Element resultado = raiz;
    if (qtdTokens <= 1)
      resultado = raiz;
    else
      {
	for (int i = 1; i <= qtdTokens - 1; i++)
	  {
	    String nomeElementoXML = tokenizador.nextToken ();
	    resultado = getElement (resultado, nomeElementoXML);
	  }
      }
    return resultado;
  }
  
  private void populaNodeXML (ObjetoNegocio fonte, Element parent, AdaptadorXMLObjeto adaptador) throws RepositorioXMLException
  {
    List atributos = adaptador.getAtributos ();
    if (atributos == null)
      return;

    try
      {
	Element node = getElement (parent, adaptador.getElementoXML ());
	if (!adaptador.isUnmarshalling ())
	  return;

	for (int i = 0; i < atributos.size (); i++)
	  {
	    AdaptadorXMLAtributo adaptadorAtributo = (AdaptadorXMLAtributo) atributos.get (i);
	    if (adaptadorAtributo.isUnmarshalling ())
	      {
		Method metodoAcesso = fonte.getClass ().getMethod (adaptadorAtributo.getMetodoAcesso (), null);
		Informacao atributo = (Informacao) metodoAcesso.invoke (fonte, null);
		String dado = atributo.getConteudoFormatado ();
		if (dado != null)
		  node.setAttribute (adaptadorAtributo.getElementoXML (), dado);
	      }
	  }
      }
    catch (NoSuchMethodException e)
      {
	LogPPGD.erro ("Mapeamento incorreto: m\u00e9todo inexistente");
	throw new RepositorioXMLException (e);
      }
    catch (IllegalAccessException e)
      {
	LogPPGD.erro ("class loader: Acesso ilegal ");
	throw new RepositorioXMLException (e);
      }
    catch (InvocationTargetException e)
      {
	LogPPGD.erro ("Exce\u00e7\u00e3o levantada por m\u00e9todo invocado  pela instrospec\u00e7\u00e3o: " + e.getMessage ());
	throw new RepositorioXMLException (e);
      }
  }
  
  private void populaNodesXMLColecao (Colecao colecao, Element parent, AdaptadorXMLObjeto adaptadorDeItem) throws RepositorioXMLException
  {
    List atributos = adaptadorDeItem.getAtributos ();
    if (atributos == null)
      return;

    List itens;
    int i;
    try
      {
	itens = colecao.recuperarLista ();
	for (i = 0; i < itens.size (); i++)
	  {
	    Object object = itens.get (i);
	    if (! (object instanceof ObjetoNegocio))
	      LogPPGD.erro ("Erro em: " + this.getClass ().getName () + ", objeto " + object.getClass ().getName () + " n\u00e3o \u00e9 ObjetoNegocio");
	    ObjetoNegocio item = (ObjetoNegocio) object;
	    Element node = parent.getOwnerDocument ().createElement (adaptadorDeItem.getElementoXML ());
	    parent.appendChild (node);
	    if (! adaptadorDeItem.isUnmarshalling ())
	      break;
	    for (int j = 0; j < atributos.size (); j++)
	      {
		AdaptadorXMLAtributo adaptadorAtributo = (AdaptadorXMLAtributo) atributos.get (j);
		if (adaptadorAtributo.isUnmarshalling ())
		  {
		    Method metodoAcesso = item.getClass ().getMethod (adaptadorAtributo.getMetodoAcesso (), null);
		    Informacao atributo = (Informacao) metodoAcesso.invoke (item, null);
		    String dado = atributo.getConteudoFormatado ();
		    if (dado != null && dado.length () > 0)
		      node.setAttribute (adaptadorAtributo.getElementoXML (), dado);
		  }
	      }
	  }
      }
    catch (NoSuchMethodException e)
      {
	LogPPGD.erro ("Mapeamento incorreto: m\u00e9todo inexistente");
	throw new RepositorioXMLException (e);
      }
    catch (IllegalAccessException e)
      {
	LogPPGD.erro ("class loader: Acesso ilegal");
	throw new RepositorioXMLException (e);
      }
    catch (InvocationTargetException e)
      {
	LogPPGD.erro ("Exce\u00e7\u00e3o levantada por m\u00e9todo invocado  pela instrospec\u00e7\u00e3o: " + e.getStackTrace ());
	throw new RepositorioXMLException (e);
      }
  }
  
  private void transformaObjetoIRPF2NodeXML (Element raiz, ObjetoNegocio fonte, String chaveMapeamento) throws RepositorioXMLException
  {
    AdaptadorXMLObjeto adaptador = mapeamento.getAdaptadorXMLObjeto (chaveMapeamento);
    String nomeRaiz = mapeamento.getAdaptadorXMLObjeto ("Mapeamento").getElementoXML ();
    if (! adaptador.getElementoXML ().equals (nomeRaiz))
      {
	Element nodeParente = getParent (raiz, chaveMapeamento);
	if (adaptador != null)
	  {
	    ObjetoNegocio conteiner = determinaConteiner (fonte, adaptador.getConteiner ());
	    ObjetoNegocio objeto = null;
	    if (conteiner instanceof Colecao)
	      populaNodesXMLColecao ((Colecao) conteiner, nodeParente, adaptador);
	    else
	      {
		objeto = determinaObjeto (conteiner, adaptador);
		populaNodeXML (objeto, nodeParente, adaptador);
	      }
	  }
      }
  }
  
  private Document obterDOM (ObjetoNegocio objetoIRPF) throws RepositorioXMLException
  {
    Document declaracaoDOM = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
    factory.setValidating (false);
    Document document;
    try
      {
	DocumentBuilder builder = factory.newDocumentBuilder ();
	declaracaoDOM = builder.newDocument ();
	String nomeRaiz = mapeamento.getAdaptadorXMLObjeto ("Mapeamento").getElementoXML ();
	Element root = declaracaoDOM.createElement (nomeRaiz);
	declaracaoDOM.appendChild (root);
	root.setAttribute ("xmlns", ConstantesGlobais.XMLNS);
	Iterator iteratorAdaptadores = mapeamento.iteratorAdaptadoresXMLObjeto ();
	while (iteratorAdaptadores.hasNext ())
	  {
	    String chaveMapeamento = (String) iteratorAdaptadores.next ();
	    transformaObjetoIRPF2NodeXML (root, objetoIRPF, chaveMapeamento);
	  }
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
  
}
