/* CadastroTabelasIRPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.tabelas;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import serpro.ppgd.negocio.ElementoTabela;
import serpro.ppgd.negocio.util.FabricaTratamentoErro;
import serpro.ppgd.negocio.util.TrataErroSistemicoIf;
import serpro.ppgd.negocio.util.UtilitariosString;
import serpro.ppgd.repositorio.RepositorioException;
import serpro.ppgd.repositorio.RepositorioTabelasBasicasIf;
import serpro.ppgd.repositorio.repositorioXML.RepositorioTabelasBasicasXML;

public abstract class CadastroTabelasIRPF
{
  private static RepositorioTabelasBasicasIf repositorioTabelasBasicas = new RepositorioTabelasBasicasXML ();
  private static TrataErroSistemicoIf trataErro = FabricaTratamentoErro.getTrataErroSistemico ();
  private static boolean testarCRC = true;
  private static Hashtable tabelaMunicipios = new Hashtable ();
  private static List colecaoUFs;
  private static List colecaoPaises;
  private static List colecaoPaisesExterior;
  private static List colecaoTipoBens;
  private static List colecaoTipoBensAR;
  private static List colecaoTipoDividas;
  private static List colecaoTipoPagamentos;
  private static List colecaoDependencias;
  private static List colecaoTipoAtividadesRural;
  private static List colecaoCondicoesExploracao;
  private static List colecaoOcupacoesPrincipal;
  private static List colecaoBancos;
  private static List colecaoBancosDebito;
  private static List colecaoNaturezasOcupacao;
  private static List colecaoRepresentacoes;
  private static List colecaoMunicipios;
  private static List colecaoTipoLogradouro;
  
  public static List recuperarUFs (int pColunaDeOrdenacao)
  {
    try
      {
	if (colecaoUFs == null)
	  colecaoUFs = repositorioTabelasBasicas.recuperarObjetosTabela ("ufssigla.xml", testarCRC);
	ordenarElementosPorColuna (pColunaDeOrdenacao, colecaoUFs);
      }
    catch (RepositorioException e)
      {
	trataErro.trataErroSistemico (e);
      }
    return colecaoUFs;
  }
  
  public static void salvarUFs ()
  {
    colecaoUFs = null;
    try
      {
	repositorioTabelasBasicas.salvar ("ufssigla.xml", recuperarUFs (1));
      }
    catch (RepositorioException e)
      {
	e.printStackTrace ();
      }
  }
  
  public static void salvarPaises ()
  {
    colecaoUFs = null;
    try
      {
	repositorioTabelasBasicas.salvar ("paises.xml", recuperarPaises ());
      }
    catch (RepositorioException e)
      {
	e.printStackTrace ();
      }
  }
  
  public static List recuperarPaises ()
  {
    try
      {
	if (colecaoPaises == null)
	  colecaoPaises = repositorioTabelasBasicas.recuperarObjetosTabela ("paises.xml", testarCRC);
      }
    catch (RepositorioException e)
      {
	trataErro.trataErroSistemico (e);
      }
    return colecaoPaises;
  }
  
  public static List recuperarPaisesExterior ()
  {
    if (colecaoPaisesExterior == null)
      {
	colecaoPaisesExterior = new Vector ();
	colecaoPaisesExterior.addAll (recuperarPaises ());
	colecaoPaisesExterior.remove (0);
      }
    return colecaoPaisesExterior;
  }
  
  public static List recuperarTipoBens ()
  {
    try
      {
	if (colecaoTipoBens == null)
	  colecaoTipoBens = repositorioTabelasBasicas.recuperarObjetosTabela ("tipoBens.xml", testarCRC);
      }
    catch (RepositorioException e)
      {
	trataErro.trataErroSistemico (e);
      }
    return colecaoTipoBens;
  }
  
  public static List recuperarTipoBensAR ()
  {
    try
      {
	if (colecaoTipoBensAR == null)
	  colecaoTipoBensAR = repositorioTabelasBasicas.recuperarObjetosTabela ("tipoBensAR.xml", testarCRC);
      }
    catch (RepositorioException e)
      {
	trataErro.trataErroSistemico (e);
      }
    return colecaoTipoBensAR;
  }
  
  public static List recuperarTipoDividas ()
  {
    try
      {
	if (colecaoTipoDividas == null)
	  colecaoTipoDividas = repositorioTabelasBasicas.recuperarObjetosTabela ("tipoDividas.xml", testarCRC);
      }
    catch (RepositorioException e)
      {
	trataErro.trataErroSistemico (e);
      }
    return colecaoTipoDividas;
  }
  
  public static List recuperarTipoPagamentos ()
  {
    try
      {
	if (colecaoTipoPagamentos == null)
	  colecaoTipoPagamentos = repositorioTabelasBasicas.recuperarObjetosTabela ("tipoPagamentos.xml", testarCRC);
      }
    catch (RepositorioException e)
      {
	trataErro.trataErroSistemico (e);
      }
    return colecaoTipoPagamentos;
  }
  
  public static List recuperarDependencias ()
  {
    try
      {
	if (colecaoDependencias == null)
	  colecaoDependencias = repositorioTabelasBasicas.recuperarObjetosTabela ("dependencias.xml", testarCRC);
      }
    catch (RepositorioException e)
      {
	trataErro.trataErroSistemico (e);
      }
    return colecaoDependencias;
  }
  
  public static void salvarDependencias ()
  {
    colecaoDependencias = null;
    try
      {
	repositorioTabelasBasicas.salvar ("dependencias.xml", recuperarDependencias ());
      }
    catch (RepositorioException e)
      {
	e.printStackTrace ();
      }
  }
  
  public static List recuperarTipoAtividadesRural ()
  {
    try
      {
	if (colecaoTipoAtividadesRural == null)
	  colecaoTipoAtividadesRural = repositorioTabelasBasicas.recuperarObjetosTabela ("tipoAtividadesRural.xml", testarCRC);
      }
    catch (RepositorioException e)
      {
	trataErro.trataErroSistemico (e);
      }
    return colecaoTipoAtividadesRural;
  }
  
  public static List recuperarCondicoesExploracao ()
  {
    try
      {
	if (colecaoCondicoesExploracao == null)
	  colecaoCondicoesExploracao = repositorioTabelasBasicas.recuperarObjetosTabela ("condicoesExploracao.xml", testarCRC);
      }
    catch (RepositorioException e)
      {
	trataErro.trataErroSistemico (e);
      }
    return colecaoCondicoesExploracao;
  }
  
  public static void salvarCondicoesExploracao ()
  {
    colecaoCondicoesExploracao = null;
    try
      {
	repositorioTabelasBasicas.salvar ("condicoesExploracao.xml", recuperarCondicoesExploracao ());
      }
    catch (RepositorioException e)
      {
	e.printStackTrace ();
      }
  }
  
  public static List recuperarOcupacoesPrincipal ()
  {
    try
      {
	if (colecaoOcupacoesPrincipal == null)
	  colecaoOcupacoesPrincipal = repositorioTabelasBasicas.recuperarObjetosTabela ("ocupacoesPrincipal.xml", testarCRC);
      }
    catch (RepositorioException e)
      {
	trataErro.trataErroSistemico (e);
      }
    return colecaoOcupacoesPrincipal;
  }
  
  public static List recuperarBancos ()
  {
    try
      {
	if (colecaoBancos == null)
	  colecaoBancos = repositorioTabelasBasicas.recuperarObjetosTabela ("bancos.xml", testarCRC);
      }
    catch (RepositorioException e)
      {
	trataErro.trataErroSistemico (e);
      }
    return colecaoBancos;
  }
  
  public static List recuperarBancosDebito ()
  {
    try
      {
	if (colecaoBancosDebito == null)
	  colecaoBancosDebito = repositorioTabelasBasicas.recuperarObjetosTabela ("bancos_debito.xml", testarCRC);
      }
    catch (RepositorioException e)
      {
	trataErro.trataErroSistemico (e);
      }
    return colecaoBancosDebito;
  }
  
  public static List recuperarNaturezasOcupacao ()
  {
    try
      {
	if (colecaoNaturezasOcupacao == null)
	  colecaoNaturezasOcupacao = repositorioTabelasBasicas.recuperarObjetosTabela ("naturezasOcupacao.xml", testarCRC);
      }
    catch (RepositorioException e)
      {
	trataErro.trataErroSistemico (e);
      }
    return colecaoNaturezasOcupacao;
  }
  
  public static void salvarNaturezasOcupacao ()
  {
    colecaoNaturezasOcupacao = null;
    try
      {
	repositorioTabelasBasicas.salvar ("naturezasOcupacao.xml", recuperarNaturezasOcupacao ());
      }
    catch (RepositorioException e)
      {
	e.printStackTrace ();
      }
  }
  
  public static void salvarBancos ()
  {
    colecaoBancos = null;
    try
      {
	repositorioTabelasBasicas.salvar ("bancos.xml", recuperarBancos ());
      }
    catch (RepositorioException e)
      {
	e.printStackTrace ();
      }
  }
  
  public static void salvarBancosDebito ()
  {
    colecaoBancosDebito = null;
    try
      {
	repositorioTabelasBasicas.salvar ("bancos_debito.xml", recuperarBancosDebito ());
      }
    catch (RepositorioException e)
      {
	e.printStackTrace ();
      }
  }
  
  public static void salvarTipoPagamento ()
  {
    colecaoTipoPagamentos = null;
    try
      {
	repositorioTabelasBasicas.salvar ("tipoPagamentos.xml", recuperarTipoPagamentos ());
      }
    catch (RepositorioException e)
      {
	e.printStackTrace ();
      }
  }
  
  public static List recuperarRepresentacoes ()
  {
    try
      {
	if (colecaoRepresentacoes == null)
	  colecaoRepresentacoes = repositorioTabelasBasicas.recuperarObjetosTabela ("representacoes.xml", testarCRC);
      }
    catch (RepositorioException e)
      {
	trataErro.trataErroSistemico (e);
      }
    return colecaoRepresentacoes;
  }
  
  public static List recuperarMunicipios (String uf, int pColunaDeOrdenacao)
  {
    try
      {
	if (tabelaMunicipios.containsKey (uf))
	  return (List) tabelaMunicipios.get (uf);
	List novaColecaoMunicipios = repositorioTabelasBasicas.recuperarObjetosTabela (uf + ".xml", testarCRC);
	ordenarElementosPorColuna (pColunaDeOrdenacao, novaColecaoMunicipios);
	tabelaMunicipios.put (uf, novaColecaoMunicipios);
	return novaColecaoMunicipios;
      }
    catch (RepositorioException e)
      {
	trataErro.trataErroSistemico (e);
	return colecaoRepresentacoes;
      }
  }
  
  public static boolean isTestarCRC ()
  {
    return testarCRC;
  }
  
  public static void setTestarCRC (boolean b)
  {
    testarCRC = b;
  }
  
  private static void gravaAtributo (String pXml, String pCodMunicipio, String pCepInicial, String pCepFinal)
  {
    System.out.println ("XML: " + pXml);
    Document docXml = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
    try
      {
	DocumentBuilder docBuilder = factory.newDocumentBuilder ();
	docXml = docBuilder.parse (pXml);
      }
    catch (ParserConfigurationException e)
      {
	e.printStackTrace ();
      }
    catch (SAXException e)
      {
	e.printStackTrace ();
      }
    catch (IOException e)
      {
	e.printStackTrace ();
      }
    NodeList listaElem = docXml.getElementsByTagName ("ITEM");
    for (int i = 0; i < listaElem.getLength (); i++)
      {
	Element elem = (Element) listaElem.item (i);
	if (elem.getAttribute ("COL1").equals (pCodMunicipio))
	  {
	    String atributoFaixas = elem.getAttribute ("COL3");
	    if (atributoFaixas == null || atributoFaixas.trim ().length () == 0)
	      atributoFaixas = pCepInicial + "-" + pCepFinal;
	    else
	      {
		String novaFaixa = pCepInicial + "-" + pCepFinal;
		if (atributoFaixas.indexOf (novaFaixa) < 0)
		  atributoFaixas += "," + (String) novaFaixa;
	      }
	    elem.setAttribute ("COL3", atributoFaixas);
	    break;
	  }
      }
    javax.xml.transform.Source source = new DOMSource (docXml);
    File file = new File (pXml);
    javax.xml.transform.Result result = new StreamResult (file);
    try
      {
	Transformer xformer = TransformerFactory.newInstance ().newTransformer ();
	xformer.setOutputProperty ("method", "xml");
	xformer.setOutputProperty ("indent", "yes");
	xformer.transform (source, result);
      }
    catch (TransformerConfigurationException e1)
      {
	e1.printStackTrace ();
      }
    catch (TransformerFactoryConfigurationError e1)
      {
	e1.printStackTrace ();
      }
    catch (TransformerException e)
      {
	e.printStackTrace ();
      }
  }
  
  public static void main (String[] Args)
  {
    setTestarCRC (false);
    salvarBancosDebito ();
  }
  
  private static void ordenarElementosPorColuna (final int pColuna, List pColecao)
  {
    Collections.sort (pColecao, new Comparator ()
    {
      public int compare (Object arg0, Object arg1)
      {
	ElementoTabela elemento1 = (ElementoTabela) arg0;
	ElementoTabela elemento2 = (ElementoTabela) arg1;
	if (pColuna >= elemento1.size ())
	  return UtilitariosString.removeAcentos (elemento1.getConteudo (0)).compareTo (UtilitariosString.removeAcentos (elemento2.getConteudo (0)));
	return UtilitariosString.removeAcentos (elemento1.getConteudo (pColuna)).compareTo (UtilitariosString.removeAcentos (elemento2.getConteudo (pColuna)));
      }
    });
  }
  
  public static List recuperarMeses ()
  {
    List retorno = new Vector ();
    ElementoTabela elemento = new ElementoTabela ();
    elemento.setConteudo (0, "Janeiro");
    retorno.add (0, elemento);
    elemento = new ElementoTabela ();
    elemento.setConteudo (0, "Fevereiro");
    retorno.add (1, elemento);
    elemento = new ElementoTabela ();
    elemento.setConteudo (0, "Mar\u00e7o");
    retorno.add (2, elemento);
    elemento = new ElementoTabela ();
    elemento.setConteudo (0, "Abril");
    retorno.add (3, elemento);
    elemento = new ElementoTabela ();
    elemento.setConteudo (0, "Maio");
    retorno.add (4, elemento);
    elemento = new ElementoTabela ();
    elemento.setConteudo (0, "Junho");
    retorno.add (5, elemento);
    elemento = new ElementoTabela ();
    elemento.setConteudo (0, "Julho");
    retorno.add (6, elemento);
    elemento = new ElementoTabela ();
    elemento.setConteudo (0, "Agosto");
    retorno.add (7, elemento);
    elemento = new ElementoTabela ();
    elemento.setConteudo (0, "Setembro");
    retorno.add (8, elemento);
    elemento = new ElementoTabela ();
    elemento.setConteudo (0, "Outubro");
    retorno.add (9, elemento);
    elemento = new ElementoTabela ();
    elemento.setConteudo (0, "Novembro");
    retorno.add (10, elemento);
    elemento = new ElementoTabela ();
    elemento.setConteudo (0, "Dezembro");
    retorno.add (11, elemento);
    return retorno;
  }
  
  public static void salvarTiposLogradouro ()
  {
    colecaoTipoLogradouro = null;
    try
      {
	repositorioTabelasBasicas.salvar ("tipoLogradouro.xml", recuperarTiposLogradouro ());
      }
    catch (RepositorioException e)
      {
	e.printStackTrace ();
      }
  }
  
  public static List recuperarTiposLogradouro ()
  {
    try
      {
	if (colecaoTipoLogradouro == null)
	  colecaoTipoLogradouro = repositorioTabelasBasicas.recuperarObjetosTabela ("tipoLogradouro.xml", testarCRC);
      }
    catch (RepositorioException e)
      {
	trataErro.trataErroSistemico (e);
      }
    return colecaoTipoLogradouro;
  }
}
