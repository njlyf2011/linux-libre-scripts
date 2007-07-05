/* MapeamentoTXT - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.txt;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;

public class MapeamentoTXT
{
  private static MapeamentoTXT instancia = null;
  private Document mapeamentoDOM;
  
  protected MapeamentoTXT ()
  {
    String pathArquivoMapeamento = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.formatosexternos.mapeamento", "mapeamentoTxt");
    mapeamentoDOM = carregarDOM (Thread.currentThread ().getContextClassLoader ().getResourceAsStream (pathArquivoMapeamento));
  }
  
  /**
   * @deprecated
   */
  public static MapeamentoTXT getInstance ()
  {
    if (instancia == null)
      instancia = new MapeamentoTXT ();
    return instancia;
  }
  
  public static MapeamentoTXT getInstancia ()
  {
    if (instancia == null)
      instancia = new MapeamentoTXT ();
    return instancia;
  }
  
  private Document carregarDOM (InputStream arquivoMapeamento)
  {
    Document mapeamento = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
    factory.setValidating (false);
    try
      {
	DocumentBuilder builder = factory.newDocumentBuilder ();
	mapeamento = builder.parse (arquivoMapeamento);
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
    return mapeamento;
  }
  
  public String getColecaoRegistroMultipo (String tipoArquivo, String pTipoReg)
  {
    Element documentElement = mapeamentoDOM.getDocumentElement ();
    LogPPGD.debug (documentElement.getNodeName ());
    NodeList filhos = documentElement.getChildNodes ();
    for (int i = 0; i < filhos.getLength (); i++)
      {
	if (filhos.item (i) instanceof Element && filhos.item (i).getNodeName ().equals ("DeclaracaoTXT"))
	  {
	    Element nodeFilho = (Element) filhos.item (i);
	    String temp = nodeFilho.getAttribute ("TipoArquivo");
	    if (nodeFilho.getAttribute ("TipoArquivo").equals (tipoArquivo))
	      {
		NodeList netos = nodeFilho.getChildNodes ();
		for (int j = 0; j < netos.getLength (); j++)
		  {
		    if (netos.item (j) instanceof Element && netos.item (j).getNodeName ().equals ("Registro"))
		      {
			Element nodeNeto = (Element) netos.item (j);
			if (nodeNeto.getAttribute ("Identificador").equals (pTipoReg))
			  return nodeNeto.getAttribute ("Colecao").trim ();
		      }
		  }
	      }
	  }
      }
    return "";
  }
  
  public boolean participaImportacao (String tipoArquivo, String pTipoReg)
  {
    Element documentElement = mapeamentoDOM.getDocumentElement ();
    LogPPGD.debug (documentElement.getNodeName ());
    NodeList filhos = documentElement.getChildNodes ();
    for (int i = 0; i < filhos.getLength (); i++)
      {
	if (filhos.item (i) instanceof Element && filhos.item (i).getNodeName ().equals ("DeclaracaoTXT"))
	  {
	    Element nodeFilho = (Element) filhos.item (i);
	    String temp = nodeFilho.getAttribute ("TipoArquivo");
	    if (nodeFilho.getAttribute ("TipoArquivo").equals (tipoArquivo))
	      {
		NodeList netos = nodeFilho.getChildNodes ();
		for (int j = 0; j < netos.getLength (); j++)
		  {
		    if (netos.item (j) instanceof Element && netos.item (j).getNodeName ().equals ("Registro"))
		      {
			Element nodeNeto = (Element) netos.item (j);
			if (nodeNeto.getAttribute ("Identificador").equals (pTipoReg))
			  {
			    if (nodeNeto.getAttribute ("ParticipaImportacao").trim ().equals ("false"))
			      return false;
			    return true;
			  }
		      }
		  }
	      }
	  }
      }
    return true;
  }
  
  public boolean participaGravacao (String tipoArquivo, String pTipoReg)
  {
    Element documentElement = mapeamentoDOM.getDocumentElement ();
    LogPPGD.debug (documentElement.getNodeName ());
    NodeList filhos = documentElement.getChildNodes ();
    for (int i = 0; i < filhos.getLength (); i++)
      {
	if (filhos.item (i) instanceof Element && filhos.item (i).getNodeName ().equals ("DeclaracaoTXT"))
	  {
	    Element nodeFilho = (Element) filhos.item (i);
	    String temp = nodeFilho.getAttribute ("TipoArquivo");
	    if (nodeFilho.getAttribute ("TipoArquivo").equals (tipoArquivo))
	      {
		NodeList netos = nodeFilho.getChildNodes ();
		for (int j = 0; j < netos.getLength (); j++)
		  {
		    if (netos.item (j) instanceof Element && netos.item (j).getNodeName ().equals ("Registro"))
		      {
			Element nodeNeto = (Element) netos.item (j);
			if (nodeNeto.getAttribute ("Identificador").equals (pTipoReg))
			  {
			    if (nodeNeto.getAttribute ("ParticipaGravacao").trim ().equals ("false"))
			      return false;
			    return true;
			  }
		      }
		  }
	      }
	  }
      }
    return true;
  }
  
  public NodeList getRelacaoCamposRegistro (String tipoArquivo, String identificador)
  {
    Element documentElement = mapeamentoDOM.getDocumentElement ();
    LogPPGD.debug (documentElement.getNodeName ());
    NodeList filhos = documentElement.getChildNodes ();
    for (int i = 0; i < filhos.getLength (); i++)
      {
	if (filhos.item (i) instanceof Element && filhos.item (i).getNodeName ().equals ("DeclaracaoTXT"))
	  {
	    Element nodeFilho = (Element) filhos.item (i);
	    String temp = nodeFilho.getAttribute ("TipoArquivo");
	    if (nodeFilho.getAttribute ("TipoArquivo").equals (tipoArquivo))
	      {
		NodeList netos = nodeFilho.getChildNodes ();
		for (int j = 0; j < netos.getLength (); j++)
		  {
		    if (netos.item (j) instanceof Element && netos.item (j).getNodeName ().equals ("Registro"))
		      {
			Element nodeNeto = (Element) netos.item (j);
			if (nodeNeto.getAttribute ("Identificador").equals (identificador))
			  {
			    NodeList bisnetos = nodeNeto.getChildNodes ();
			    LogPPGD.debug (nodeFilho.getNodeName ());
			    return bisnetos;
			  }
		      }
		  }
	      }
	  }
      }
    return null;
  }
  
  public List getTiposDeRegistrosArquivo (String tipoArquivo)
  {
    List retorno = new Vector ();
    Element documentElement = mapeamentoDOM.getDocumentElement ();
    LogPPGD.debug (documentElement.getNodeName ());
    NodeList filhos = documentElement.getChildNodes ();
    for (int i = 0; i < filhos.getLength (); i++)
      {
	if (filhos.item (i) instanceof Element && filhos.item (i).getNodeName ().equals ("DeclaracaoTXT"))
	  {
	    Element nodeFilho = (Element) filhos.item (i);
	    String temp = nodeFilho.getAttribute ("TipoArquivo");
	    if (nodeFilho.getAttribute ("TipoArquivo").equals (tipoArquivo))
	      {
		NodeList netos = nodeFilho.getChildNodes ();
		for (int j = 0; j < netos.getLength (); j++)
		  {
		    if (netos.item (j) instanceof Element && netos.item (j).getNodeName ().equals ("Registro"))
		      {
			Element nodeNeto = (Element) netos.item (j);
			retorno.add (nodeNeto.getAttribute ("Identificador"));
		      }
		  }
	      }
	  }
      }
    return retorno;
  }
  
  public static void main (String[] args)
  {
    LogPPGD.debug ("Property javax.xml.parsers.DocumentBuilderFactory: " + System.getProperty ("javax.xml.parsers.DocumentBuilderFactory"));
    LogPPGD.debug ("Implementa\u00e7\u00e3o do JAXP: " + DocumentBuilderFactory.newInstance ());
    try
      {
	MapeamentoTXT fabrica = new MapeamentoTXT ();
	LogPPGD.debug ("cheguei no final");
      }
    catch (Exception e)
      {
	LogPPGD.erro (e.getMessage ());
	e.printStackTrace ();
      }
  }
}
