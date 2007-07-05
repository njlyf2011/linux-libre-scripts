/* UtilXML - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.repositorioXML.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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

import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.IdDeclaracao;
import serpro.ppgd.negocio.IdUsuario;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;

public class UtilXML
{
  private static String pXml = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.persistencia.mapeamentoXML", "/mapeamentoObjetoXML.xml");
  
  public static void geraMapeamentoObjetoXML ()
  {
    LogPPGD.debug ("XML: " + pXml);
    Document idsDOM = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
    factory.setValidating (false);
    try
      {
	DocumentBuilder builder = factory.newDocumentBuilder ();
	idsDOM = builder.newDocument ();
	Element root = idsDOM.createElement ("Mapeamento");
	idsDOM.appendChild (root);
	root.setAttribute ("xmlns", ConstantesGlobais.XMLNS);
	root.setAttribute ("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	root.setAttribute ("xsi:schemaLocation", "http://www.receita.gov.br/declaracao");
	root.setAttribute ("ElementoXML", "Declaracao");
	root.setAttribute ("ClasseJava", FabricaUtilitarios.nomeClasseDeclaracao);
	ObjetoNegocio instanciaDeclaracao = instanciaDeclaracao ();
	preencheAtributos (instanciaDeclaracao, null, "DadosDeclaracao", null, idsDOM, instanciaDeclaracao.getClass ().getName (), root, "DadosDeclaracao", false);
      }
    catch (ParserConfigurationException e)
      {
	String msg = "Erro de configura\u00e7\u00e3o da f\u00e1brica DOM: " + e.getMessage ();
	LogPPGD.erro (msg);
      }
    catch (ClassNotFoundException e)
      {
	String msg = "Classe n\u00e3o encontrada: " + e.getMessage ();
	LogPPGD.erro (msg);
      }
    catch (Exception e)
      {
	String msg = e.getMessage ();
	LogPPGD.erro (msg);
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
	transformer.setOutputProperty ("method", "xml");
	transformer.setOutputProperty ("indent", "yes");
	transformer.transform (source, result);
      }
    catch (FileNotFoundException e)
      {
	String msg = "Arquivo nao localizado: " + arquivoXML.getPath () + " - " + e.getMessage ();
	LogPPGD.erro (msg);
      }
    catch (TransformerConfigurationException e)
      {
	String msg = "Erro de configura\u00e7\u00e3o da f\u00e1brica de transforma\u00e7\u00e3o DOM: " + e.getMessage ();
	LogPPGD.erro (msg);
      }
    catch (TransformerException e)
      {
	String msg = "Erro de transforma\u00e7\u00e3o DOM-XML: " + e.getMessage ();
	LogPPGD.erro (msg);
      }
  }
  
  private static void preencheAtributos (ObjetoNegocio pObj, String pElementoXMLAnterior, String pElementoXmlAtual, String pMetodoAcesso, Document document, String pNomeClasse, Element nodoPai, String pPertenceA, boolean itemDeColecao) throws Exception
  {
    LogPPGD.erro ("->" + pPertenceA);
    Element node = document.createElement ("Classe");
    nodoPai.appendChild (node);
    node.setAttribute ("ElementoXML", pElementoXmlAtual);
    Element nodePertenceA = document.createElement ("PertenceA");
    node.appendChild (nodePertenceA);
    if (itemDeColecao)
      node.setAttribute ("ClasseJava", pNomeClasse);
    if (pMetodoAcesso != null)
      {
	nodePertenceA.setAttribute ("MetodoAcesso", pMetodoAcesso);
	nodePertenceA.setAttribute ("ElementoXML", pPertenceA.substring (0, pPertenceA.lastIndexOf (".")));
      }
    else
      nodePertenceA.setAttribute ("MetodoAcesso", "this");
    if ((serpro.ppgd.negocio.Colecao.class).isAssignableFrom (pObj.getClass ()) && ((Colecao) pObj).getTipoItens () != null)
      {
	Class[] argumentosFormais = { serpro.ppgd.negocio.IdDeclaracao.class };
	Object[] argumentosReais = { pObj.getIdDeclaracao () };
	Constructor construtor = ((Colecao) pObj).getTipoItens ().getConstructor (argumentosFormais);
	ObjetoNegocio itemColecao = (ObjetoNegocio) construtor.newInstance (argumentosReais);
	String nomeAtributo = ((Colecao) pObj).getTipoItens ().getName ();
	nomeAtributo = nomeAtributo.substring (nomeAtributo.lastIndexOf ('.') + 1);
	preencheAtributos (itemColecao, pElementoXmlAtual, nomeAtributo, "recuperarLista", document, ((Colecao) pObj).getTipoItens ().getName (), node, pPertenceA + "." + nomeAtributo, true);
      }
    Iterator itCamposInformacao = FabricaUtilitarios.getFieldsCamposInformacao (Class.forName (pNomeClasse)).iterator ();
    while (itCamposInformacao.hasNext ())
      {
	Field field = (Field) itCamposInformacao.next ();
	Element nodeAtributo = document.createElement ("Atributo");
	String nomeAtributo = field.getName ();
	nodeAtributo.setAttribute ("ElementoXML", nomeAtributo.substring (0, 1).toUpperCase () + nomeAtributo.substring (1, nomeAtributo.length ()));
	node.appendChild (nodeAtributo);
      }
    Iterator itCamposObjetoNegocio = FabricaUtilitarios.getFieldsCamposObjetoNegocio (Class.forName (pNomeClasse)).iterator ();
    while (itCamposObjetoNegocio.hasNext ())
      {
	Field field = (Field) itCamposObjetoNegocio.next ();
	ObjetoNegocio valor = (ObjetoNegocio) FabricaUtilitarios.getValorField (field, pObj);
	String elementoXML = field.getName ().substring (0, 1).toUpperCase () + field.getName ().substring (1, field.getName ().length ());
	String nomeMetodoAcesso = "get" + elementoXML;
	preencheAtributos (valor, pElementoXmlAtual, elementoXML, nomeMetodoAcesso, document, field.getType ().getName (), node, pPertenceA + "." + elementoXML, false);
      }
  }
  
  private static String getPathArquivo ()
  {
    String path = FabricaUtilitarios.getPathCompletoDirAplicacao ();
    File file = new File (path);
    file.mkdirs ();
    file = new File (path + pXml);
    try
      {
	if (file.exists ())
	  file.delete ();
	file.createNewFile ();
      }
    catch (IOException e)
      {
	e.printStackTrace ();
	return null;
      }
    return file.getPath ();
  }
  
  private static ObjetoNegocio instanciaDeclaracao () throws Exception
  {
    if (FabricaUtilitarios.nomeClasseDeclaracao != null)
      {
	IdUsuario idTemporario = (IdUsuario) Class.forName (FabricaUtilitarios.nomeClasseId).newInstance ();
	Class[] argumentosFormais = { serpro.ppgd.negocio.IdUsuario.class };
	Object[] argumentosReais = { idTemporario };
	Constructor construtor = Class.forName (FabricaUtilitarios.nomeClasseIdDeclaracao).getConstructor (argumentosFormais);
	IdDeclaracao idDeclTemp = (IdDeclaracao) construtor.newInstance (argumentosReais);
	argumentosFormais = new Class[] { serpro.ppgd.negocio.IdDeclaracao.class };
	argumentosReais = new Object[] { idDeclTemp };
	construtor = Class.forName (FabricaUtilitarios.nomeClasseDeclaracao).getConstructor (argumentosFormais);
	ObjetoNegocio decl = (ObjetoNegocio) construtor.newInstance (argumentosReais);
	return decl;
      }
    return null;
  }
  
}
