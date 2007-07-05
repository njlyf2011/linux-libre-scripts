/* ConverterTabelasTXTtoXML - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.tabelas;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.util.UtilitariosString;

public class ConverterTabelasTXTtoXML
{
  private File url;
  private List lst1 = new Vector ();
  private List lst2 = new Vector ();
  private List lst3 = new Vector ();
  private List lst4 = new Vector ();
  private List lst5 = new Vector ();
  private String CRC;
  static String caminhoTabelas;
  
  private void leArquivoTXT (String stFileName, int inicio1, int inicio2, int inicio3, int inicio4)
  {
    try
      {
	lst1.clear ();
	lst2.clear ();
	lst3.clear ();
	lst4.clear ();
	url = new File (caminhoTabelas + stFileName);
	FileReader file = new FileReader (url);
	LineNumberReader inputString = new LineNumberReader (file);
	CRC = "0";
	int inic1 = inicio1;
	int inic2 = inicio2;
	int inic3 = inicio3;
	int inic4 = inicio4;
	String s1 = "";
	String s2 = "";
	String s3 = "";
	String s4 = "";
	for (String linha = inputString.readLine (); linha != null; linha = inputString.readLine ())
	  {
	    if (inicio3 == 0)
	      inic3 = linha.length ();
	    if (inicio4 == 0)
	      inic4 = linha.length ();
	    if (linha.substring (0, 2).compareTo ("\020\014") != 0)
	      {
		s1 = linha.substring (inicio1, inicio2).trim ();
		lst1.add (s1);
		CRC = UtilitariosString.GerarCRC (CRC, s1);
		s2 = linha.substring (inicio2, inic3).trim ();
		lst2.add (s2);
		CRC = UtilitariosString.GerarCRC (CRC, s2);
		if (inicio3 != 0)
		  {
		    s3 = linha.substring (inicio3, inic4).trim ();
		    lst3.add (s3);
		    CRC = UtilitariosString.GerarCRC (CRC, s3);
		  }
		if (inicio4 != 0)
		  {
		    s4 = linha.substring (inicio4, linha.length ()).trim ();
		    lst4.add (s4);
		    CRC = UtilitariosString.GerarCRC (CRC, s4);
		  }
	      }
	  }
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  private String getCepCidade (String codCidade)
  {
    String faixaCep = "";
    try
      {
	url = new File (caminhoTabelas + "ceps.txt");
	FileReader file = new FileReader (url);
	LineNumberReader inputString = new LineNumberReader (file);
	for (String linha = inputString.readLine (); linha != null; linha = inputString.readLine ())
	  {
	    String codAtual = linha.substring (0, 4).trim ();
	    if (codCidade.equals (codAtual))
	      {
		if (faixaCep.equals (""))
		  faixaCep = linha.substring (4, 12).trim () + "-" + linha.substring (12, 20).trim ();
		else
		  faixaCep += "," + linha.substring (4, 12).trim () + "-" + linha.substring (12, 20).trim ();
	      }
	  }
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
    return faixaCep;
  }
  
  private void leArquivoTXTMunicipio (String stFileName, int inicio1, int inicio2, int inicio3, int inicio4)
  {
    int NumCidades = 0;
    try
      {
	lst1.clear ();
	lst2.clear ();
	lst3.clear ();
	url = new File (caminhoTabelas + stFileName);
	FileReader file = new FileReader (url.getPath ());
	LineNumberReader inputString = new LineNumberReader (file);
	int inic1 = inicio1;
	int inic2 = inicio2;
	int inic3 = inicio3;
	int inic4 = inicio4;
	String s1 = "";
	String s2 = "";
	String sigla = "";
	String linha = inputString.readLine ();
	do
	  {
	    inic4 = linha.length ();
	    if (linha.substring (0, 2).compareTo ("\020\014") != 0)
	      {
		NumCidades = Integer.parseInt (linha.substring (inic1, inic2));
		sigla = linha.substring (inic2, inic3);
		CRC = "0";
		for (int cont = 0; cont != NumCidades; cont++)
		  {
		    if (linha.substring (0, 2).compareTo ("\020\014") != 0)
		      {
			linha = inputString.readLine ();
			s1 = linha.substring (inic1, inic2).trim ();
			lst1.add (s1);
			CRC = UtilitariosString.GerarCRC (CRC, s1);
			s2 = linha.substring (inic2, inic4).trim ();
			lst2.add (s2);
			CRC = UtilitariosString.GerarCRC (CRC, s2);
			String cep = getCepCidade (s1);
			lst3.add (cep);
			CRC = UtilitariosString.GerarCRC (CRC, cep);
			System.out.println (CRC);
		      }
		  }
		salvar (sigla + ".xml");
		lst1.clear ();
		lst2.clear ();
		lst3.clear ();
	      }
	    linha = inputString.readLine ();
	  }
	while (linha != null);
	System.out.println ("FIM DA LEITURA DO ARQUIVO!");
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  private void leArquivoOcupacaoPrincipal1TXT (String stFileName, int inicio1, int inicio2)
  {
    try
      {
	lst1.clear ();
	lst2.clear ();
	lst3.clear ();
	lst4.clear ();
	url = new File (caminhoTabelas + stFileName);
	FileReader file = new FileReader (url.getPath ());
	LineNumberReader inputString = new LineNumberReader (file);
	CRC = "0";
	int inic1 = inicio1;
	int inic2 = inicio2;
	String s1 = "";
	String s2 = "";
	for (String linha = inputString.readLine (); linha != null; linha = inputString.readLine ())
	  {
	    if (linha.substring (0, 2).compareTo ("\020\014") != 0)
	      {
		s1 = linha.substring (inicio1, inicio2).trim ();
		s2 = linha.substring (inicio2, linha.length ()).trim ();
		leArquivoOcupacaoPrincipal2TXT ("ocupPrincipal2.txt", s1, s2, 0, 2, 5);
	      }
	  }
      }
    catch (Exception exception)
      {
	/* empty */
      }
  }
  
  private void leArquivoOcupacaoPrincipal2TXT (String stFileName, String chave1, String chave2, int inicio1, int inicio2, int inicio3)
  {
    try
      {
	url = new File (caminhoTabelas + stFileName);
	FileReader file = new FileReader (url.getPath ());
	LineNumberReader inputString = new LineNumberReader (file);
	int inic1 = inicio1;
	int inic2 = inicio2;
	String s1 = "";
	String s3 = "";
	String s4 = "";
	String linha = inputString.readLine ();
	s1 = linha.substring (inicio1, inicio2).trim ();
	while (linha != null)
	  {
	    if (linha.substring (0, 2).compareTo ("\020\014") != 0 && s1.compareTo (chave1) == 0)
	      {
		s3 = linha.substring (inicio2, inicio3).trim ();
		lst1.add (s3);
		CRC = UtilitariosString.GerarCRC (CRC, s3);
		lst2.add (chave1);
		CRC = UtilitariosString.GerarCRC (CRC, chave1);
		lst3.add (chave2);
		CRC = UtilitariosString.GerarCRC (CRC, chave2);
		s4 = linha.substring (inicio3, linha.length ()).trim ();
		lst4.add (s4);
		CRC = UtilitariosString.GerarCRC (CRC, s4);
	      }
	    linha = inputString.readLine ();
	    s1 = linha.substring (inicio1, inicio2).trim ();
	  }
      }
    catch (Exception exception)
      {
	/* empty */
      }
  }
  
  private void leArquivoAjudaCampo (String stFileName)
  {
    try
      {
	lst1.clear ();
	lst2.clear ();
	url = new File (caminhoTabelas + stFileName);
	FileReader file = new FileReader (url.getPath ());
	LineNumberReader inputString = new LineNumberReader (file);
	for (String linha = inputString.readLine (); linha != null; linha = inputString.readLine ())
	  {
	    lst1.add (linha);
	    linha = inputString.readLine ();
	    lst2.add (linha);
	  }
      }
    catch (Exception exception)
      {
	/* empty */
      }
  }
  
  public void salvarAjudaCampo (String stFileName)
  {
    Document idsDOM = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
    factory.setValidating (false);
    try
      {
	DocumentBuilder builder = factory.newDocumentBuilder ();
	idsDOM = builder.newDocument ();
	Element root = idsDOM.createElement ("AjudaContextual");
	idsDOM.appendChild (root);
	root.setAttribute ("xmlns", ConstantesGlobais.XMLNS);
	for (int i = 0; i < lst1.size (); i++)
	  {
	    String col1 = (String) lst1.get (i);
	    String col2 = (String) lst2.get (i);
	    Element node = idsDOM.createElement ("Msg");
	    root.appendChild (node);
	    node.setAttribute ("Id", col1);
	    node.setAttribute ("Valor", col2);
	  }
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
    idsDOM.normalize ();
    try
      {
	String path = url.getPath ();
	int i;
	for (i = path.length () - 1; i != 0; i--)
	  {
	    if (path.charAt (i) == '/')
	      break;
	  }
	path = path.substring (0, i + 1) + stFileName;
	File file = new File (path);
	try
	  {
	    file.createNewFile ();
	  }
	catch (IOException ioexception)
	  {
	    /* empty */
	  }
	File arquivoXML = new File (path);
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
  
  public void salvar (String stFileName)
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
	for (int i = 0; i < lst1.size (); i++)
	  {
	    String col1 = (String) lst1.get (i);
	    String col2 = (String) lst2.get (i);
	    String col3 = "";
	    String col4 = "";
	    if (lst3.size () > 0)
	      col3 = (String) lst3.get (i);
	    if (lst4.size () > 0)
	      col4 = (String) lst4.get (i);
	    Element node = idsDOM.createElement ("ITEM");
	    root.appendChild (node);
	    node.setAttribute ("COL1", col1);
	    node.setAttribute ("COL2", col2);
	    if (lst3.size () > 0)
	      node.setAttribute ("COL3", col3);
	    if (lst4.size () > 0)
	      node.setAttribute ("COL4", col4);
	  }
	Element node = idsDOM.createElement ("ITEM");
	root.appendChild (node);
	node.setAttribute ("CRC", CRC);
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
    idsDOM.normalize ();
    try
      {
	String path = url.getParent ();
	path += File.separator + (String) stFileName;
	File file = new File (path);
	try
	  {
	    file.createNewFile ();
	  }
	catch (IOException e)
	  {
	    e.printStackTrace ();
	  }
	File arquivoXML = new File (path);
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
  
  public static void main (String[] args)
  {
    try
      {
	ConverterTabelasTXTtoXML tb = new ConverterTabelasTXTtoXML ();
	caminhoTabelas = "c:/temp//";
	tb.leArquivoTXT ("Paises.txt", 0, 3, 0, 0);
	tb.salvar ("paises.xml");
      }
    catch (Exception exception)
      {
	/* empty */
      }
  }
}
