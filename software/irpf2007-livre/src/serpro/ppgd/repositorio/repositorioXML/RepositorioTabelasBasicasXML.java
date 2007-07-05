/* RepositorioTabelasBasicasXML - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.repositorioXML;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.ElementoTabela;
import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.negocio.util.UtilitariosArquivo;
import serpro.ppgd.negocio.util.UtilitariosString;
import serpro.ppgd.repositorio.RepositorioTabelasBasicasIf;

public class RepositorioTabelasBasicasXML implements RepositorioTabelasBasicasIf
{
  
  protected String getPathArquivo (String fileName)
  {
    String path = "/" + fileName;
    return path;
  }
  
  private Document leArquivo (String path)
  {
    Document tabelasDOM = null;
    InputStream in = UtilitariosArquivo.getResource (path, serpro.ppgd.repositorio.repositorioXML.RepositorioTabelasBasicasXML.class);
    if (path != null)
      {
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
	factory.setValidating (false);
	try
	  {
	    DocumentBuilder builder = factory.newDocumentBuilder ();
	    tabelasDOM = builder.parse (in);
	    LogPPGD.debug (tabelasDOM.getImplementation ().toString ());
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
    return tabelasDOM;
  }
  
  public List recuperarObjetosTabela (String fileName, boolean testarCRC) throws RepositorioXMLException
  {
    List lstElementoTabela = new Vector ();
    Document tabelasDOM = leArquivo (getPathArquivo (fileName));
    if (tabelasDOM != null)
      {
	Element element = tabelasDOM.getDocumentElement ();
	if (element.hasChildNodes ())
	  {
	    NodeList filhos = element.getChildNodes ();
	    String CRCCalculado = "0";
	    for (int i = 0; i < filhos.getLength (); i++)
	      {
		if (filhos.item (i).getNodeType () == 1)
		  {
		    Element elementFilho = (Element) filhos.item (i);
		    String CRCLido = elementFilho.getAttribute ("CRC");
		    if (CRCLido == "")
		      {
			int j = 0;
			String s;
			do
			  {
			    j++;
			    s = elementFilho.getAttribute ("COL" + j);
			    if (s != "")
			      CRCCalculado = UtilitariosString.GerarCRC (CRCCalculado, s);
			  }
			while (s != "");
			ElementoTabela elementoTabela = new ElementoTabela ();
			for (int k = 1; k < j; k++)
			  elementoTabela.setConteudo (k - 1, elementFilho.getAttribute ("COL" + k));
			lstElementoTabela.add (elementoTabela);
		      }
		    else if (CRCLido.compareTo (CRCCalculado) != 0 && testarCRC)
		      throw new RepositorioXMLException ("Checksum de tabela b\u00e1sica inv\u00e1lido");
		  }
	      }
	  }
      }
    if (lstElementoTabela.size () == 0)
      throw new RepositorioXMLException ("Tabela b\u00e1sica inv\u00e1lida");
    return lstElementoTabela;
  }
  
  public void salvar (String stFileName, List lst)
  {
    Document idsDOM = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
    factory.setValidating (false);
    try
      {
	DocumentBuilder builder = factory.newDocumentBuilder ();
	idsDOM = builder.newDocument ();
	Element root = idsDOM.createElement ("TABELA");
	idsDOM.appendChild (root);
	root.setAttribute ("xmlns", ConstantesGlobais.XMLNS);
	String CRCCalculado = "0";
	for (int i = 0; i < lst.size (); i++)
	  {
	    Element node = idsDOM.createElement ("ITEM");
	    root.appendChild (node);
	    ElementoTabela elementoTabela = (ElementoTabela) lst.get (i);
	    for (int j = 1; j < elementoTabela.size () + 1; j++)
	      {
		node.setAttribute ("COL" + j, elementoTabela.getConteudo (j - 1));
		CRCCalculado = UtilitariosString.GerarCRC (CRCCalculado, elementoTabela.getConteudo (j - 1));
	      }
	  }
	Element node = idsDOM.createElement ("ITEM");
	root.appendChild (node);
	node.setAttribute ("CRC", CRCCalculado);
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
    idsDOM.normalize ();
    try
      {
	URL url = (serpro.ppgd.repositorio.repositorioXML.RepositorioTabelasBasicasXML.class).getResource (getPathArquivo (stFileName));
	File arquivoXML = new File (url.getPath ());
	FileOutputStream os = new FileOutputStream (arquivoXML);
	StreamResult result = new StreamResult (os);
	DOMSource source = new DOMSource (idsDOM);
	TransformerFactory transFactory = TransformerFactory.newInstance ();
	Transformer transformer = transFactory.newTransformer ();
	transformer.transform (source, result);
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
}
