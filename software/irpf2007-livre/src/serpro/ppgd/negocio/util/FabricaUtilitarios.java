/* FabricaUtilitarios - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.util;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.ElementoTabela;
import serpro.ppgd.negocio.IdDeclaracao;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;

public class FabricaUtilitarios
{
  private static Properties properties;
  public static String nomeClasseId = null;
  public static String nomeClasseIdDeclaracao = null;
  public static String nomeClasseDeclaracao = null;
  public static boolean usaCacheParaReflexao;
  public static Map cacheTempToReflection;
  
  /**
   * @deprecated
   */
  public static String getPathCompletoDirAplicacao ()
  {
    URL url = (serpro.ppgd.negocio.util.FabricaUtilitarios.class).getResource ("/aplicacao.properties");
    String path;
    if (url == null)
      path = System.getProperty ("user.dir");
    else
      {
	URI uri = null;
	try
	  {
	    uri = new URI (url.getFile ());
	    String s = uri.getPath ();
	    if (s == null)
		s = url.getPath ();
	    path = UtilitariosArquivo.extraiPath (s);
	  }
	catch (URISyntaxException e)
	  {
	    path = System.getProperty ("user.dir");
	  }
      }
    if (path == null || path.equals (""))
	path = ".";
    return path;
  }
  
  public static Properties getProperties ()
  {
    if (properties == null)
      {
	properties = UtilitariosArquivo.loadProperties ("/aplicacao.properties", serpro.ppgd.negocio.util.FabricaUtilitarios.class);
	String dirDados = properties.getProperty ("aplicacao.diretorio.dados");
	if (dirDados == null || dirDados.equals (""))
	  {
	    dirDados = getPathCompletoDirAplicacao ();
	    properties.setProperty ("aplicacao.diretorio.dados", dirDados);
	  }
      }
    return properties;
  }
  
  /**
   * @deprecated
   */
  public static String getPathCompletoDirDadosAplicacao ()
  {
    String dirDados = getProperties ().getProperty ("aplicacao.diretorio.dados");
    if (dirDados.charAt (dirDados.length () - 1) != '/')
      dirDados += "/";
    dirDados += "aplicacao/dados";
    return dirDados;
  }
  
  /**
   * @deprecated
   */
  public static String getPathCompletoDirImpressao ()
  {
    return getPathCompletoDirAplicacao () + "impressao";
  }
  
  /**
   * @deprecated
   */
  public static String getPathCompletoDirGravadas ()
  {
    return getPathCompletoDirAplicacao () + "gravadas";
  }
  
  /**
   * @deprecated
   */
  public static String getPathCompletoDirTransmitidas ()
  {
    return getPathCompletoDirAplicacao () + "transmitidas";
  }
  
  /**
   * @deprecated
   */
  public static String getPathCompletoDirLib ()
  {
    return getPathCompletoDirAplicacao () + "lib";
  }
  
  public static void criaDiretorioBasicos ()
  {
    File flDados = new File (getPathCompletoDirImpressao ());
    flDados.mkdirs ();
    flDados = new File (getPathCompletoDirGravadas ());
    flDados.mkdirs ();
    flDados = new File (getPathCompletoDirTransmitidas ());
    flDados.mkdirs ();
    flDados = new File (getPathCompletoDirLib ());
    flDados.mkdirs ();
  }
  
  public static List getFieldsCamposInformacao (Class pClass)
  {
    List retorno = new Vector ();
    Field[] fields = pClass.getDeclaredFields ();
    for (int i = 0; i < fields.length; i++)
      {
	if ((serpro.ppgd.negocio.Informacao.class).isAssignableFrom (fields[i].getType ()))
	  retorno.add (fields[i]);
      }
    if (pClass.getSuperclass () != null && ! pClass.getSuperclass ().equals (serpro.ppgd.negocio.ObjetoNegocio.class))
      retorno.addAll (getFieldsCamposInformacao (pClass.getSuperclass ()));
    return retorno;
  }
  
  public static List getAllFields (Class pClass)
  {
    if (usaCacheParaReflexao && cacheTempToReflection.containsKey (pClass))
      return (List) cacheTempToReflection.get (pClass);
    List retorno = new Vector ();
    Field[] fields = pClass.getDeclaredFields ();
    for (int i = 0; i < fields.length; i++)
      retorno.add (fields[i]);
    if (pClass.getSuperclass () != null)
      retorno.addAll (getAllFields (pClass.getSuperclass ()));
    if (usaCacheParaReflexao)
      cacheTempToReflection.put (pClass, retorno);
    return retorno;
  }
  
  public static List getAllFieldsOfSpecificType (Class pClass, Class pTipoDesejado)
  {
    List retorno = new Vector ();
    Field[] fields = pClass.getDeclaredFields ();
    for (int i = 0; i < fields.length; i++)
      {
	if (pTipoDesejado.isAssignableFrom (fields[i].getType ()))
	  retorno.add (fields[i]);
      }
    if (pClass.getSuperclass () != null)
      retorno.addAll (getAllFieldsOfSpecificType (pClass.getSuperclass (), pTipoDesejado));
    return retorno;
  }
  
  public static List getFieldsCamposObjetoNegocio (Class pClass)
  {
    List retorno = new Vector ();
    Field[] fields = pClass.getDeclaredFields ();
    for (int i = 0; i < fields.length; i++)
      {
	if ((serpro.ppgd.negocio.ObjetoNegocio.class).isAssignableFrom (fields[i].getType ()))
	  retorno.add (fields[i]);
      }
    if (pClass.getSuperclass () != null && ! pClass.getSuperclass ().equals (serpro.ppgd.negocio.ObjetoNegocio.class))
      retorno.addAll (getFieldsCamposInformacao (pClass.getSuperclass ()));
    return retorno;
  }
  
  public static Object getValorField (Field pField, Object pInstancia)
  {
    boolean isAcessible = pField.isAccessible ();
    Object object;
    try
      {
	pField.setAccessible (true);
	Object retorno = pField.get (pInstancia);
	pField.setAccessible (isAcessible);
	object = retorno;
      }
    catch (Exception e)
      {
	pField.setAccessible (isAcessible);
	throw new IllegalArgumentException ("Erro na obten\u00e7\u00e3o do valor do atributo :" + e.getMessage ());
      }
    return object;
  }
  
  public static Object getValorField (String pField, Object pInstancia)
  {
    Object object;
    try
      {
	Field f = null;
	Iterator itFields = getFieldsCamposInformacao (pInstancia.getClass ()).iterator ();
	while (itFields.hasNext ())
	  {
	    Field fieldAtual = (Field) itFields.next ();
	    if (fieldAtual.getName ().equals (pField))
	      f = fieldAtual;
	  }
	object = getValorField (f, pInstancia);
      }
    catch (Exception e)
      {
	throw new IllegalArgumentException ("Erro na obten\u00e7\u00e3o do valor do atributo :" + e.getMessage ());
      }
    return object;
  }
  
  public static Object getValorFieldGenerico (String pField, Object pInstancia)
  {
    Exception exception;
  while_3_:
    do
      {
	do
	  {
	    Iterator itFields;
	    try
	      {
		if (pInstancia == null)
		  break;
		itFields = getAllFields (pInstancia.getClass ()).iterator ();
		Field f = null;
	      }
	    catch (Exception exception_0_)
	      {
		exception = exception_0_;
		break while_3_;
	      }
	    while (itFields.hasNext ())
	      {
		Field fieldAtual = (Field) itFields.next ();
		if (fieldAtual.getName ().equals (pField))
		  {
		    Field f = fieldAtual;
		    return getValorField (f, pInstancia);
		  }
		try
		  {
		    /* empty */
		  }
		catch (Exception exception_1_)
		  {
		    exception = exception_1_;
		    break while_3_;
		  }
	      }
	  }
	while (false);
	return null;
      }
    while (false);
    Exception e = exception;
    throw new IllegalArgumentException ("Erro na obten\u00e7\u00e3o do valor do atributo :" + pField + " \nErro:\n" + e.getMessage ());
  }
  
  public static Object obtemAtributo (ObjetoNegocio pObj, String pSequenciaMetodos)
  {
    StringTokenizer atributos = new StringTokenizer (pSequenciaMetodos, ".");
    Object ultimoAtributoNegocioAcessado = pObj;
    while (atributos.hasMoreTokens ())
      {
	String atributoAtual = atributos.nextToken ();
	ultimoAtributoNegocioAcessado = getValorFieldGenerico (atributoAtual, ultimoAtributoNegocioAcessado);
      }
    return ultimoAtributoNegocioAcessado;
  }
  
  public static Object obtemAtributoComExcecao (ObjetoNegocio pObj, String pSequenciaMetodos)
  {
    StringTokenizer atributos = new StringTokenizer (pSequenciaMetodos, ".");
    Object ultimoAtributoNegocioAcessado = pObj;
    while (atributos.hasMoreTokens ())
      {
	String atributoAtual = atributos.nextToken ();
	ultimoAtributoNegocioAcessado = getValorFieldGenericoComExcecao (atributoAtual, ultimoAtributoNegocioAcessado);
      }
    return ultimoAtributoNegocioAcessado;
  }
  
  public static Object getValorFieldGenericoComExcecao (String pField, Object pInstancia)
  {
    Exception exception;
  while_4_:
    do
      {
	Iterator itFields;
	try
	  {
	    Field f = null;
	    itFields = getAllFields (pInstancia.getClass ()).iterator ();
	  }
	catch (Exception exception_2_)
	  {
	    exception = exception_2_;
	    break;
	  }
	while (itFields.hasNext ())
	  {
	    Field fieldAtual = (Field) itFields.next ();
	    if (fieldAtual.getName ().equals (pField))
	      {
		Field f = fieldAtual;
		return getValorField (f, pInstancia);
	      }
	    try
	      {
		/* empty */
	      }
	    catch (Exception exception_3_)
	      {
		exception = exception_3_;
		break while_4_;
	      }
	  }
	throw new IllegalArgumentException ("Erro na obten\u00e7\u00e3o do valor do atributo :" + pField);
      }
    while (false);
    Exception e = exception;
    throw new IllegalArgumentException ("Erro na obten\u00e7\u00e3o do valor do atributo :" + pField + " \nErro:\n" + e.getMessage ());
  }
  
  public static Object invocaMetodo (String pNomeMetodo, Object pInstancia, Class[] tiposParametros, Object[] parametros)
  {
    Object retorno = null;
    try
      {
	Method m = pInstancia.getClass ().getMethod (pNomeMetodo, tiposParametros);
	retorno = m.invoke (pInstancia, parametros);
      }
    catch (Exception e)
      {
	FabricaTratamentoErro.getTrataErroSistemico ().trataErroSistemico (e);
      }
    return retorno;
  }
  
  public static Field getField (Object pInstancia, Object pAtributo)
  {
    Exception exception;
  while_5_:
    do
      {
	Iterator it;
	try
	  {
	    it = getFieldsCamposInformacao (pInstancia.getClass ()).iterator ();
	  }
	catch (Exception exception_4_)
	  {
	    exception = exception_4_;
	    break;
	  }
	while (it.hasNext ())
	  {
	    Field fieldAtual = (Field) it.next ();
	    Object valorObtido = getValorField (fieldAtual, pInstancia);
	    if (valorObtido != null && valorObtido.getClass ().isAssignableFrom (pAtributo.getClass ()) && valorObtido.equals (pAtributo))
	      return fieldAtual;
	    try
	      {
		/* empty */
	      }
	    catch (Exception exception_5_)
	      {
		exception = exception_5_;
		break while_5_;
	      }
	  }
	return null;
      }
    while (false);
    Exception e = exception;
    throw new IllegalArgumentException ("Erro na obten\u00e7\u00e3o do Field referente ao atributo :" + e.getMessage ());
  }
  
  public static Field getFieldPorNome (Class aClasseDoAtributo, String aAtributo)
  {
    Field retorno = null;
    Iterator it = getAllFields (aClasseDoAtributo.getClass ()).iterator ();
    while (it.hasNext ())
      {
	Field f = (Field) it.next ();
	if (f.getName ().equals (aAtributo))
	  {
	    retorno = f;
	    break;
	  }
      }
    return retorno;
  }
  
  public static void preencherObjetoComAtributosXml (Object pObj, Element pElemento)
  {
    Iterator itCampos = getAllFieldsOfSpecificType (pObj.getClass (), serpro.ppgd.negocio.Informacao.class).iterator ();
    while (itCampos.hasNext ())
      {
	Field field = (Field) itCampos.next ();
	Informacao valorAtributo = (Informacao) getValorField (field, pObj);
	try
	  {
	    String valAtributo = pElemento.getAttribute (field.getName ());
	    if (valAtributo != null)
	      valorAtributo.setConteudo (valAtributo);
	    else
	      valorAtributo.setConteudo ("");
	  }
	catch (Exception exception)
	  {
	    /* empty */
	  }
      }
  }
  
  public static void preencherXMLComAtributosDoObjeto (Object pObj, Element pElemento)
  {
    Iterator itCampos = getFieldsCamposInformacao (pObj.getClass ()).iterator ();
    while (itCampos.hasNext ())
      {
	Field field = (Field) itCampos.next ();
	Informacao valorAtributo = (Informacao) getValorField (field, pObj);
	try
	  {
	    String valAtributo = valorAtributo.asString ();
	    if (valAtributo != null)
	      pElemento.setAttribute (field.getName (), valAtributo);
	    else
	      pElemento.setAttribute (field.getName (), "");
	  }
	catch (Exception exception)
	  {
	    /* empty */
	  }
      }
  }
  
  public static List verificarPendencias (Object pObj)
  {
    List retorno = new Vector ();
    if (pObj == null)
      return retorno;
    if (pObj instanceof ObjetoNegocio)
      {
	if (pObj instanceof Colecao)
	  ((Colecao) pObj).excluirRegistrosEmBranco ();
	retorno.addAll (((ObjetoNegocio) pObj).verificarPendencias (-1));
      }
    else
      return retorno;
    Field[] fields = pObj.getClass ().getDeclaredFields ();
    for (int i = 0; i < fields.length; i++)
      {
	Object valAtributo = null;
	try
	  {
	    valAtributo = getValorField (fields[i], pObj);
	  }
	catch (Exception e)
	  {
	    LogPPGD.erro ("N\u00e3o foi poss\u00edvel obter valor do atributo->" + fields[i].getName ());
	  }
	if (valAtributo != null && ! Modifier.isTransient (fields[i].getModifiers ()) && valAtributo instanceof ObjetoNegocio && ! (valAtributo instanceof IdDeclaracao))
	  {
	    if (pObj instanceof Colecao)
	      ((Colecao) pObj).excluirRegistrosEmBranco ();
	    retorno.addAll (verificarPendencias ((ObjetoNegocio) valAtributo));
	  }
      }
    return retorno;
  }
  
  public static Document carregarDOM (String arquivoMapeamento)
  {
    Document mapeamentoDOM = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
    factory.setValidating (false);
    try
      {
	DocumentBuilder builder = factory.newDocumentBuilder ();
	mapeamentoDOM = builder.parse (Thread.currentThread ().getContextClassLoader ().getResourceAsStream (arquivoMapeamento));
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
  
  public static void ordenarElementosPorColuna (final int pColuna, List pColecao)
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
  
  public static ObjetoNegocio instanciaObjetoNegocio (Class classeAInstanciar, IdDeclaracao id)
  {
    ObjetoNegocio retorno = null;
    try
      {
	Class[] argumentosFormais = { serpro.ppgd.negocio.IdDeclaracao.class };
	Object[] argumentosReais = { id };
	Constructor construtor = null;
	try
	  {
	    construtor = classeAInstanciar.getConstructor (argumentosFormais);
	    retorno = (ObjetoNegocio) construtor.newInstance (argumentosReais);
	  }
	catch (Exception e)
	  {
	    retorno = (ObjetoNegocio) classeAInstanciar.newInstance ();
	  }
      }
    catch (IllegalAccessException e)
      {
	FabricaTratamentoErro.getTrataErroSistemico ().trataErroSistemico (e);
      }
    catch (InstantiationException e)
      {
	FabricaTratamentoErro.getTrataErroSistemico ().trataErroSistemico (e);
      }
    return retorno;
  }
  
  public static ObjetoNegocio instanciaObjetoNegocio (Class classeAInstanciar) throws Exception
  {
    return (ObjetoNegocio) classeAInstanciar.newInstance ();
  }
  
  public static void preencheObjetoNegocioComResultSet (ResultSet resultSet, Object pObj)
  {
    Iterator itCamposId = getAllFieldsOfSpecificType (pObj.getClass (), serpro.ppgd.negocio.Informacao.class).iterator ();
    while (itCamposId.hasNext ())
      {
	Field field = (Field) itCamposId.next ();
	Informacao valorAtributo = (Informacao) getValorField (field, pObj);
	if (! (valorAtributo instanceof Valor) || ! ((Valor) valorAtributo).isCampoCalculado ())
	  {
	    try
	      {
		valorAtributo.setConteudo (resultSet.getString (field.getName ()));
	      }
	    catch (Exception exception)
	      {
		/* empty */
	      }
	  }
      }
  }
  
  public static PreenchedorCodigo criaPreenchedorCodigo ()
  {
    return new PreenchedorCodigo ();
  }
  
  public static PreenchedorCodigo criaPreenchedorCodigo (Codigo aCod)
  {
    return new PreenchedorCodigo (aCod);
  }
  
  private void preencheColecao (Colecao pColecao, IdDeclaracao pIdDec, String pNomeAtributoColecao, ResultSet rs)
  {
    String sql = "";
    try
      {
	while (rs.next ())
	  {
	    ObjetoNegocio instancia = instanciaObjetoNegocio (pColecao.getTipoItens (), pIdDec);
	    preencheObjetoNegocioComResultSet (rs, instancia);
	    pColecao.recuperarLista ().add (instancia);
	  }
      }
    catch (SQLException e)
      {
	e.printStackTrace ();
      }
  }
  
  
  static
  {
    nomeClasseId = getProperties ().getProperty ("aplicacao.classes.id", (serpro.ppgd.negocio.impl.IdUsuarioImpl.class).getName ());
    nomeClasseIdDeclaracao = getProperties ().getProperty ("aplicacao.classes.iddeclaracao", (serpro.ppgd.negocio.impl.IdDeclaracaoImpl.class).getName ());
    nomeClasseDeclaracao = getProperties ().getProperty ("aplicacao.classes.declaracao");
    usaCacheParaReflexao = false;
    cacheTempToReflection = new Hashtable ();
  }
}
