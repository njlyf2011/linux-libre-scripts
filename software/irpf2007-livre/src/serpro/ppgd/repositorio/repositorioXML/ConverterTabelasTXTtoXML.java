/* ConverterTabelasTXTtoXML - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.repositorioXML;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URL;
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
  private URL url;
  private List lst1 = new Vector ();
  private List lst2 = new Vector ();
  private List lst3 = new Vector ();
  private List lst4 = new Vector ();
  private List lst5 = new Vector ();
  private String CRC;
  
  private void leArquivoTXT (String stFileName, int inicio1, int inicio2, int inicio3, int inicio4)
  {
    try
      {
	lst1.clear ();
	lst2.clear ();
	lst3.clear ();
	lst4.clear ();
	url = this.getClass ().getResource ("/tabelas/ajusteAnual/tabelasBaseAjusteAnual/" + stFileName);
	FileReader file = new FileReader (url.getPath ());
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
	String linha;
	do
	  {
	    linha = inputString.readLine ();
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
	while (linha != null);
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  private void leArquivoTXTMunicipio (String stFileName, int inicio1, int inicio2, int inicio3, int inicio4, int inicio5)
  {
    try
      {
	lst1.clear ();
	lst2.clear ();
	lst3.clear ();
	lst4.clear ();
	url = this.getClass ().getResource ("/tabelas/ajusteAnual/tabelasBaseAjusteAnual/" + stFileName);
	FileReader file = new FileReader (url.getPath ());
	LineNumberReader inputString = new LineNumberReader (file);
	CRC = "0";
	int inic1 = inicio1;
	int inic2 = inicio2;
	int inic3 = inicio3;
	int inic4 = inicio4;
	int inic5 = inicio5;
	String s1 = "";
	String s2 = "";
	String s3 = "";
	String s4 = "";
	String linha;
	do
	  {
	    linha = inputString.readLine ();
	    if (inicio3 == 0)
	      inic3 = linha.length ();
	    if (inicio4 == 0)
	      inic4 = linha.length ();
	    if (linha.substring (0, 1).compareTo ("0") != 0 && linha.substring (0, 2).compareTo ("\020\014") != 0)
	      {
		s1 = linha.substring (inicio2, inicio3).trim ();
		lst1.add (s1);
		CRC = UtilitariosString.GerarCRC (CRC, s1);
		s2 = linha.substring (inicio4, inic5).trim ();
		lst2.add (s2);
		CRC = UtilitariosString.GerarCRC (CRC, s2);
	      }
	  }
	while (linha != null);
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
	url = this.getClass ().getResource ("/srf/irpf/ajusteAnual/tabelasBaseAjusteAnual/" + stFileName);
	FileReader file = new FileReader (url.getPath ());
	LineNumberReader inputString = new LineNumberReader (file);
	CRC = "0";
	int inic1 = inicio1;
	int inic2 = inicio2;
	String s1 = "";
	String s2 = "";
	String linha;
	do
	  {
	    linha = inputString.readLine ();
	    if (linha.substring (0, 2).compareTo ("\020\014") != 0)
	      {
		s1 = linha.substring (inicio1, inicio2).trim ();
		s2 = linha.substring (inicio2, linha.length ()).trim ();
		leArquivoOcupacaoPrincipal2TXT ("ocupPrincipal2.txt", s1, s2, 0, 2, 5);
	      }
	  }
	while (linha != null);
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
	url = this.getClass ().getResource ("/srf/irpf/ajusteAnual/tabelasBaseAjusteAnual/" + stFileName);
	FileReader file = new FileReader (url.getPath ());
	LineNumberReader inputString = new LineNumberReader (file);
	int inic1 = inicio1;
	int inic2 = inicio2;
	String s1 = "";
	String s3 = "";
	String s4 = "";
	String linha = inputString.readLine ();
	s1 = linha.substring (inicio1, inicio2).trim ();
	do
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
	while (linha != null);
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
	url = this.getClass ().getResource ("/tabelas/ajusteAnual/" + stFileName);
	FileReader file = new FileReader (url.getPath ());
	LineNumberReader inputString = new LineNumberReader (file);
	String linha = inputString.readLine ();
	do
	  {
	    lst1.add (linha);
	    linha = inputString.readLine ();
	    lst2.add (linha);
	    linha = inputString.readLine ();
	  }
	while (linha != null);
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
	for (i = path.length () - 1; i != 0 && path.charAt (i) != '/'; i--)
	  {
	    /* empty */
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
	String path = url.getPath ();
	int i;
	for (i = path.length () - 1; i != 0 && path.charAt (i) != '/'; i--)
	  {
	    /* empty */
	  }
	path = path.substring (0, i + 1) + stFileName;
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
	tb.leArquivoTXTMunicipio ("AC.txt", 0, 1, 5, 8, 48);
	tb.salvar ("AC.xml");
	tb.leArquivoTXTMunicipio ("AL.txt", 0, 1, 5, 8, 48);
	tb.salvar ("AL.xml");
	tb.leArquivoTXTMunicipio ("AM.txt", 0, 1, 5, 8, 48);
	tb.salvar ("AM.xml");
	tb.leArquivoTXTMunicipio ("AP.txt", 0, 1, 5, 8, 48);
	tb.salvar ("AP.xml");
	tb.leArquivoTXTMunicipio ("BA.txt", 0, 1, 5, 8, 48);
	tb.salvar ("BA.xml");
	tb.leArquivoTXTMunicipio ("CE.txt", 0, 1, 5, 8, 48);
	tb.salvar ("CE.xml");
	tb.leArquivoTXTMunicipio ("DF.txt", 0, 1, 5, 8, 48);
	tb.salvar ("DF.xml");
	tb.leArquivoTXTMunicipio ("ES.txt", 0, 1, 5, 8, 48);
	tb.salvar ("ES.xml");
	tb.leArquivoTXTMunicipio ("GO.txt", 0, 1, 5, 8, 48);
	tb.salvar ("GO.xml");
	tb.leArquivoTXTMunicipio ("MA.txt", 0, 1, 5, 8, 48);
	tb.salvar ("MA.xml");
	tb.leArquivoTXTMunicipio ("MG.txt", 0, 1, 5, 8, 48);
	tb.salvar ("MG.xml");
	tb.leArquivoTXTMunicipio ("MS.txt", 0, 1, 5, 8, 48);
	tb.salvar ("MS.xml");
	tb.leArquivoTXTMunicipio ("MT.txt", 0, 1, 5, 8, 48);
	tb.salvar ("MT.xml");
	tb.leArquivoTXTMunicipio ("PA.txt", 0, 1, 5, 8, 48);
	tb.salvar ("PA.xml");
	tb.leArquivoTXTMunicipio ("PB.txt", 0, 1, 5, 8, 48);
	tb.salvar ("PB.xml");
	tb.leArquivoTXTMunicipio ("PE.txt", 0, 1, 5, 8, 48);
	tb.salvar ("PE.xml");
	tb.leArquivoTXTMunicipio ("PI.txt", 0, 1, 5, 8, 48);
	tb.salvar ("PI.xml");
	tb.leArquivoTXTMunicipio ("PR.txt", 0, 1, 5, 8, 48);
	tb.salvar ("PR.xml");
	tb.leArquivoTXTMunicipio ("RJ.txt", 0, 1, 5, 8, 48);
	tb.salvar ("RJ.xml");
	tb.leArquivoTXTMunicipio ("RN.txt", 0, 1, 5, 8, 48);
	tb.salvar ("RN.xml");
	tb.leArquivoTXTMunicipio ("RO.txt", 0, 1, 5, 8, 48);
	tb.salvar ("RO.xml");
	tb.leArquivoTXTMunicipio ("RR.txt", 0, 1, 5, 8, 48);
	tb.salvar ("RR.xml");
	tb.leArquivoTXTMunicipio ("RS.txt", 0, 1, 5, 8, 48);
	tb.salvar ("RS.xml");
	tb.leArquivoTXTMunicipio ("SC.txt", 0, 1, 5, 8, 48);
	tb.salvar ("SC.xml");
	tb.leArquivoTXTMunicipio ("SE.txt", 0, 1, 5, 8, 48);
	tb.salvar ("SE.xml");
	tb.leArquivoTXTMunicipio ("SP.txt", 0, 1, 5, 8, 48);
	tb.salvar ("SP.xml");
	tb.leArquivoTXTMunicipio ("TO.txt", 0, 1, 5, 8, 48);
	tb.salvar ("TO.xml");
      }
    catch (Exception exception)
      {
	/* empty */
      }
  }
}
