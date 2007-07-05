/* UtilDB - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.embeddedDB.util;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.repositorio.RepositorioException;

public abstract class UtilDB
{
  static Connection conexao = null;
  
  public static Connection getConexao () throws RepositorioException
  {
    String pDriver = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.persistencia.conexaoDB.driver");
    String pUrlConexao = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.persistencia.conexaoDB.url");
    String pUsuario = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.persistencia.conexaoDB.usuario");
    String pSenha = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.persistencia.conexaoDB.senha");
    if (pDriver == null)
      throw new RepositorioException ("Est\u00e1 faltando a chave \"aplicacao.persistencia.conexaoDB.driver\" no arquivo de propriedades da aplica\u00e7\u00e3o");
    if (pUrlConexao == null)
      throw new RepositorioException ("Est\u00e1 faltando a chave \"aplicacao.persistencia.conexaoDB.url\" no arquivo de propriedades da aplica\u00e7\u00e3o");
    if (pUsuario == null)
      throw new RepositorioException ("Est\u00e1 faltando a chave \"aplicacao.persistencia.conexaoDB.usuario\" no arquivo de propriedades da aplica\u00e7\u00e3o");
    if (conexao == null)
      {
	try
	  {
	    Class.forName (pDriver);
	    conexao = DriverManager.getConnection (pUrlConexao, pUsuario, pSenha);
	  }
	catch (Exception e)
	  {
	    throw new RepositorioException ("Erro ao criar conex\u00e3o com BD :" + e.getMessage ());
	  }
      }
    return conexao;
  }
  
  public static void preencheObjeto (ResultSet resultSet, Object pObj) throws RepositorioException
  {
    Iterator itCamposId = FabricaUtilitarios.getFieldsCamposInformacao (pObj.getClass ()).iterator ();
    while (itCamposId.hasNext ())
      {
	Field field = (Field) itCamposId.next ();
	LogPPGD.debug ("field->" + field.getName ());
	Informacao valorAtributo = (Informacao) FabricaUtilitarios.getValorField (field, pObj);
	if (! (valorAtributo instanceof Valor) || ! ((Valor) valorAtributo).isCampoCalculado ())
	  {
	    try
	      {
		valorAtributo.setConteudo (resultSet.getString (field.getName ()));
		LogPPGD.debug ("preencheu Atributo:" + resultSet.getString (field.getName ()));
	      }
	    catch (Exception exception)
	      {
		/* empty */
	      }
	  }
      }
  }
  
  public static void finalizaConexao ()
  {
    if (conexao != null)
      {
	try
	  {
	    conexao.close ();
	  }
	catch (Exception exception)
	  {
	    /* empty */
	  }
      }
  }
  
  public static int executarDDL (String comando) throws RepositorioException
  {
    int i;
    try
      {
	Statement statement = getConexao ().createStatement ();
	int retorno = statement.executeUpdate (comando);
	statement.close ();
	i = retorno;
      }
    catch (SQLException e)
      {
	throw new RepositorioException ("Erro na execu\u00e7\u00e3o do comando DDL :" + e.getMessage ());
      }
    return i;
  }
  
  public static String preparaSqlCreateTableObjetoNegocio (ObjetoNegocio pObj, String pNomeTabela) throws RepositorioException
  {
    String string;
    try
      {
	StringBuffer strBuff = new StringBuffer ();
	strBuff.append ("CREATE TABLE ");
	strBuff.append (pNomeTabela);
	strBuff.append (" ( ");
	Iterator itCamposId = FabricaUtilitarios.getFieldsCamposInformacao (pObj.getClass ()).iterator ();
	boolean semNenhumcampoInformacao = true;
	if (itCamposId.hasNext ())
	  semNenhumcampoInformacao = false;
	while (itCamposId.hasNext ())
	  {
	    Field field = (Field) itCamposId.next ();
	    strBuff.append (field.getName () + " Varchar ");
	    if (itCamposId.hasNext ())
	      strBuff.append (" , ");
	  }
	itCamposId = pObj.getIdDeclaracao ().getListaAtributosPK ().iterator ();
	if (itCamposId.hasNext () && ! semNenhumcampoInformacao)
	  strBuff.append (" , ");
	while (itCamposId.hasNext ())
	  {
	    Object campoAtual = itCamposId.next ();
	    Field field = FabricaUtilitarios.getField (pObj.getIdDeclaracao (), campoAtual);
	    if (field == null)
	      field = FabricaUtilitarios.getField (pObj.getIdDeclaracao ().getId (), campoAtual);
	    strBuff.append (field.getName () + " Varchar ");
	    if (itCamposId.hasNext ())
	      strBuff.append (" , ");
	  }
	strBuff.append (" ); ");
	string = strBuff.toString ();
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	throw new RepositorioException ("Erro na cria\u00e7\u00e3o das tabelas do objeto de neg\u00f3cio:", e);
      }
    return string;
  }
  
  public static List preparaSqlCriacaoTabelasObjetoNegocio (ObjetoNegocio pObj, String pNomeTabela) throws RepositorioException
  {
    Vector vector;
    try
      {
	Vector vector_0_ = new Vector ();
	String sqltabela = preparaSqlCreateTableObjetoNegocio (pObj, pNomeTabela);
	if (sqltabela != null)
	  vector_0_.add (sqltabela);
	Field[] fields = pObj.getClass ().getDeclaredFields ();
	for (int i = 0; i < fields.length; i++)
	  {
	    if ((serpro.ppgd.negocio.ObjetoNegocio.class).isAssignableFrom (fields[i].getType ()))
	      {
		ObjetoNegocio valor = (ObjetoNegocio) FabricaUtilitarios.getValorField (fields[i], pObj);
		if ((serpro.ppgd.negocio.Colecao.class).isAssignableFrom (fields[i].getType ()) && ((Colecao) valor).getTipoItens () != null)
		  {
		    Class[] argumentosFormais = { serpro.ppgd.negocio.IdDeclaracao.class };
		    Object[] argumentosReais = { pObj.getIdDeclaracao () };
		    Constructor construtor = ((Colecao) valor).getTipoItens ().getConstructor (argumentosFormais);
		    ObjetoNegocio itemColecao = (ObjetoNegocio) construtor.newInstance (argumentosReais);
		    String nomeTabela = ((Colecao) valor).getTipoItens ().getName ();
		    nomeTabela = nomeTabela.substring (nomeTabela.lastIndexOf ('.') + 1);
		    vector_0_.addAll (preparaSqlCriacaoTabelasObjetoNegocio (itemColecao, fields[i].getName () + "_" + nomeTabela));
		  }
		vector_0_.addAll (preparaSqlCriacaoTabelasObjetoNegocio (valor, fields[i].getName ()));
	      }
	  }
	vector = vector_0_;
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	throw new RepositorioException ("Erro na gera\u00e7\u00e3o da lista de Sql de cria\u00e7\u00e3o de tabelas:", e);
      }
    return vector;
  }
  
  public static List preparaSqlRemocaoTabelasObjetoNegocio (ObjetoNegocio pObj, String pNomeTabela) throws RepositorioException
  {
    Vector vector;
    try
      {
	Vector vector_1_ = new Vector ();
	vector_1_.add ("DROP TABLE " + pNomeTabela);
	Field[] fields = pObj.getClass ().getDeclaredFields ();
	for (int i = 0; i < fields.length; i++)
	  {
	    if ((serpro.ppgd.negocio.ObjetoNegocio.class).isAssignableFrom (fields[i].getType ()))
	      {
		ObjetoNegocio valor = (ObjetoNegocio) FabricaUtilitarios.getValorField (fields[i], pObj);
		if ((serpro.ppgd.negocio.Colecao.class).isAssignableFrom (fields[i].getType ()) && ((Colecao) valor).getTipoItens () != null)
		  {
		    Class[] argumentosFormais = { serpro.ppgd.negocio.IdDeclaracao.class };
		    Object[] argumentosReais = { pObj.getIdDeclaracao () };
		    Constructor construtor = ((Colecao) valor).getTipoItens ().getConstructor (argumentosFormais);
		    ObjetoNegocio itemColecao = (ObjetoNegocio) construtor.newInstance (argumentosReais);
		    String nomeTabela = ((Colecao) valor).getTipoItens ().getName ();
		    nomeTabela = nomeTabela.substring (nomeTabela.lastIndexOf ('.') + 1);
		    vector_1_.addAll (preparaSqlRemocaoTabelasObjetoNegocio (itemColecao, fields[i].getName () + "_" + nomeTabela));
		  }
		vector_1_.addAll (preparaSqlRemocaoTabelasObjetoNegocio (valor, fields[i].getName ()));
	      }
	  }
	vector = vector_1_;
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	throw new RepositorioException ("Erro na gera\u00e7\u00e3o da lista de Sql de cria\u00e7\u00e3o de tabelas:", e);
      }
    return vector;
  }
  
  private static String preparaSQLInsertObjetoNegocio (ObjetoNegocio pObj, String pNomeTabela) throws RepositorioException
  {
    String string;
    try
      {
	StringBuffer strBuff = new StringBuffer ();
	StringBuffer strBuffCampos = new StringBuffer ();
	StringBuffer strBuffValues = new StringBuffer ();
	strBuff.append ("INSERT INTO ");
	strBuff.append (pNomeTabela);
	strBuffCampos.append (" ( ");
	strBuffValues.append (" VALUES ( ");
	Iterator itCamposId = FabricaUtilitarios.getFieldsCamposInformacao (pObj.getClass ()).iterator ();
	boolean semNenhumcampoInformacao = true;
	if (itCamposId.hasNext ())
	  semNenhumcampoInformacao = false;
	while (itCamposId.hasNext ())
	  {
	    Field field = (Field) itCamposId.next ();
	    strBuffCampos.append (field.getName ());
	    strBuffValues.append ("'");
	    Object valorAtributo = FabricaUtilitarios.getValorField (field, pObj);
	    if (valorAtributo != null)
	      strBuffValues.append (((Informacao) valorAtributo).asString ());
	    strBuffValues.append ("' ");
	    if (itCamposId.hasNext ())
	      {
		strBuffCampos.append (" , ");
		strBuffValues.append (" , ");
	      }
	  }
	itCamposId = pObj.getIdDeclaracao ().getListaAtributosPK ().iterator ();
	if (itCamposId.hasNext () && ! semNenhumcampoInformacao)
	  {
	    strBuffCampos.append (" , ");
	    strBuffValues.append (" , ");
	  }
	while (itCamposId.hasNext ())
	  {
	    Object campoAtual = itCamposId.next ();
	    Field field = FabricaUtilitarios.getField (pObj.getIdDeclaracao (), campoAtual);
	    if (field == null)
	      field = FabricaUtilitarios.getField (pObj.getIdDeclaracao ().getId (), campoAtual);
	    strBuffCampos.append (field.getName ());
	    strBuffValues.append ("'");
	    Object valorAtributo = null;
	    try
	      {
		valorAtributo = FabricaUtilitarios.getValorField (field, pObj.getIdDeclaracao ());
	      }
	    catch (IllegalArgumentException e)
	      {
		valorAtributo = FabricaUtilitarios.getValorField (field, pObj.getIdDeclaracao ().getId ());
	      }
	    if (valorAtributo != null)
	      strBuffValues.append (((Informacao) valorAtributo).asString ());
	    strBuffValues.append ("' ");
	    if (itCamposId.hasNext ())
	      {
		strBuffCampos.append (" , ");
		strBuffValues.append (" , ");
	      }
	  }
	strBuffCampos.append (" ) ");
	strBuffValues.append (" ) ");
	strBuff.append (" " + strBuffCampos.toString ());
	strBuff.append (" " + strBuffValues.toString ());
	string = strBuff.toString ();
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	throw new RepositorioException ("Erro na cria\u00e7\u00e3o do SQL Insert do objeto de neg\u00f3cio:" + pObj.getClass ().getName (), e);
      }
    return string;
  }
  
  public static List preparaSqlInsertTabelasObjetoNegocio (ObjetoNegocio pObj, String pNomeTabela) throws RepositorioException
  {
    Vector vector;
    try
      {
	Vector vector_2_ = new Vector ();
	String sqltabela = preparaSQLInsertObjetoNegocio (pObj, pNomeTabela);
	if (sqltabela != null)
	  vector_2_.add (sqltabela);
	Field[] fields = pObj.getClass ().getDeclaredFields ();
	for (int i = 0; i < fields.length; i++)
	  {
	    if ((serpro.ppgd.negocio.ObjetoNegocio.class).isAssignableFrom (fields[i].getType ()))
	      {
		ObjetoNegocio valor = (ObjetoNegocio) FabricaUtilitarios.getValorField (fields[i], pObj);
		if ((serpro.ppgd.negocio.Colecao.class).isAssignableFrom (fields[i].getType ()) && ((Colecao) valor).getTipoItens () != null)
		  {
		    Iterator itItensColecao = ((Colecao) valor).recuperarLista ().iterator ();
		    String nomeTabela = ((Colecao) valor).getTipoItens ().getName ();
		    nomeTabela = nomeTabela.substring (nomeTabela.lastIndexOf ('.') + 1);
		    while (itItensColecao.hasNext ())
		      {
			ObjetoNegocio itemColecao = (ObjetoNegocio) itItensColecao.next ();
			vector_2_.addAll (preparaSqlInsertTabelasObjetoNegocio (itemColecao, fields[i].getName () + "_" + nomeTabela));
		      }
		  }
		vector_2_.addAll (preparaSqlInsertTabelasObjetoNegocio (valor, fields[i].getName ()));
	      }
	  }
	vector = vector_2_;
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	throw new RepositorioException ("Erro na gera\u00e7\u00e3o do SQL de Insert:", e);
      }
    return vector;
  }
  
  private static String preparaSQLUpdateObjetoNegocio (ObjetoNegocio pObj, String pNomeTabela) throws RepositorioException
  {
    Exception exception;
  while_28_:
    do
      {
	StringBuffer strBuff;
	Iterator itCamposId;
	do
	  {
	    String string;
	    try
	      {
		strBuff = new StringBuffer ();
		strBuff.append ("UPDATE ");
		strBuff.append (pNomeTabela);
		strBuff.append (" SET ");
		itCamposId = FabricaUtilitarios.getFieldsCamposInformacao (pObj.getClass ()).iterator ();
		if (itCamposId.hasNext ())
		  break;
		string = null;
	      }
	    catch (Exception exception_3_)
	      {
		exception = exception_3_;
		break while_28_;
	      }
	    return string;
	  }
	while (false);
	String string;
	try
	  {
	    while (itCamposId.hasNext ())
	      {
		Field field = (Field) itCamposId.next ();
		strBuff.append (field.getName ());
		strBuff.append ("=");
		strBuff.append ("'");
		Object valorAtributo = FabricaUtilitarios.getValorField (field, pObj);
		if (valorAtributo != null)
		  strBuff.append (((Informacao) valorAtributo).asString ());
		strBuff.append ("' ");
		if (itCamposId.hasNext ())
		  strBuff.append (" , ");
	      }
	    montaClausulaSQLWhere (strBuff, pObj);
	    strBuff.append (" ; ");
	    string = strBuff.toString ();
	  }
	catch (Exception exception_4_)
	  {
	    exception = exception_4_;
	    break;
	  }
	return string;
      }
    while (false);
    Exception e = exception;
    e.printStackTrace ();
    throw new RepositorioException ("Erro na cria\u00e7\u00e3o do SQL de Update do objeto de neg\u00f3cio:" + pObj.getClass ().getName (), e);
  }
  
  private static void montaClausulaSQLWhere (StringBuffer strBuff, ObjetoNegocio pObj) throws RepositorioException
  {
    strBuff.append (" WHERE ");
    Iterator itCamposId = pObj.getIdDeclaracao ().getListaAtributosPK ().iterator ();
    while (itCamposId.hasNext ())
      {
	Object campoAtual = itCamposId.next ();
	Field field = FabricaUtilitarios.getField (pObj.getIdDeclaracao (), campoAtual);
	if (field == null)
	  field = FabricaUtilitarios.getField (pObj.getIdDeclaracao ().getId (), campoAtual);
	Object valorAtributo = null;
	try
	  {
	    valorAtributo = FabricaUtilitarios.getValorField (field, pObj.getIdDeclaracao ());
	  }
	catch (IllegalArgumentException e)
	  {
	    valorAtributo = FabricaUtilitarios.getValorField (field, pObj.getIdDeclaracao ().getId ());
	  }
	strBuff.append (field.getName ());
	strBuff.append ("=");
	if (valorAtributo != null && ((Informacao) valorAtributo).asString ().trim ().length () != 0)
	  {
	    strBuff.append ("'");
	    strBuff.append (((Informacao) valorAtributo).asString ());
	    strBuff.append ("' ");
	  }
	else
	  strBuff.append (field.getName ());
	if (itCamposId.hasNext ())
	  strBuff.append (" AND ");
      }
  }
  
  public static List preparaSqlUpdateTabelasObjetoNegocio (ObjetoNegocio pObj, String pNomeTabela) throws RepositorioException
  {
    Vector vector;
    try
      {
	Vector vector_5_ = new Vector ();
	String sqltabela = preparaSQLUpdateObjetoNegocio (pObj, pNomeTabela);
	if (sqltabela != null)
	  vector_5_.add (sqltabela);
	Field[] fields = pObj.getClass ().getDeclaredFields ();
	for (int i = 0; i < fields.length; i++)
	  {
	    if ((serpro.ppgd.negocio.ObjetoNegocio.class).isAssignableFrom (fields[i].getType ()))
	      {
		ObjetoNegocio valor = (ObjetoNegocio) FabricaUtilitarios.getValorField (fields[i], pObj);
		if ((serpro.ppgd.negocio.Colecao.class).isAssignableFrom (fields[i].getType ()) && ((Colecao) valor).getTipoItens () != null)
		  {
		    Iterator itItensColecao = ((Colecao) valor).recuperarLista ().iterator ();
		    String nomeTabela = ((Colecao) valor).getTipoItens ().getName ();
		    nomeTabela = nomeTabela.substring (nomeTabela.lastIndexOf ('.') + 1);
		    vector_5_.add (" DELETE FROM " + fields[i].getName () + "_" + nomeTabela + " ; ");
		    while (itItensColecao.hasNext ())
		      {
			ObjetoNegocio itemColecao = (ObjetoNegocio) itItensColecao.next ();
			vector_5_.addAll (preparaSqlInsertTabelasObjetoNegocio (itemColecao, fields[i].getName () + "_" + nomeTabela));
		      }
		  }
		vector_5_.addAll (preparaSqlUpdateTabelasObjetoNegocio (valor, fields[i].getName ()));
	      }
	  }
	vector = vector_5_;
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	throw new RepositorioException ("Erro na gera\u00e7\u00e3o da lista de Sql de update das tabelas:", e);
      }
    return vector;
  }
  
  private static String preparaSQLDeleteObjetoNegocio (ObjetoNegocio pObj, String pNomeTabela) throws RepositorioException
  {
    String string;
    try
      {
	StringBuffer strBuff = new StringBuffer ();
	strBuff.append ("DELETE FROM ");
	strBuff.append (pNomeTabela);
	montaClausulaSQLWhere (strBuff, pObj);
	strBuff.append (" ; ");
	string = strBuff.toString ();
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	throw new RepositorioException ("Erro no preparo no SQL de remo\u00e7\u00e3o do objeto de neg\u00f3cio:", e);
      }
    return string;
  }
  
  public static List preparaSqlDeleteTabelasObjetoNegocio (ObjetoNegocio pObj, String pNomeTabela) throws RepositorioException
  {
    Vector vector;
    try
      {
	Vector vector_6_ = new Vector ();
	String sqltabela = preparaSQLDeleteObjetoNegocio (pObj, pNomeTabela);
	if (sqltabela != null)
	  vector_6_.add (sqltabela);
	Field[] fields = pObj.getClass ().getDeclaredFields ();
	for (int i = 0; i < fields.length; i++)
	  {
	    if ((serpro.ppgd.negocio.ObjetoNegocio.class).isAssignableFrom (fields[i].getType ()))
	      {
		ObjetoNegocio valor = (ObjetoNegocio) FabricaUtilitarios.getValorField (fields[i], pObj);
		if ((serpro.ppgd.negocio.Colecao.class).isAssignableFrom (fields[i].getType ()) && ((Colecao) valor).getTipoItens () != null)
		  {
		    Class[] argumentosFormais = { serpro.ppgd.negocio.IdDeclaracao.class };
		    Object[] argumentosReais = { pObj.getIdDeclaracao () };
		    Constructor construtor = ((Colecao) valor).getTipoItens ().getConstructor (argumentosFormais);
		    ObjetoNegocio itemColecao = (ObjetoNegocio) construtor.newInstance (argumentosReais);
		    String nomeTabela = ((Colecao) valor).getTipoItens ().getName ();
		    nomeTabela = nomeTabela.substring (nomeTabela.lastIndexOf ('.') + 1);
		    vector_6_.addAll (preparaSqlDeleteTabelasObjetoNegocio (itemColecao, fields[i].getName () + "_" + nomeTabela));
		  }
		vector_6_.addAll (preparaSqlDeleteTabelasObjetoNegocio (valor, fields[i].getName ()));
	      }
	  }
	vector = vector_6_;
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	throw new RepositorioException ("Erro na gera\u00e7\u00e3o da lista de Sql de Delete das tabelas:", e);
      }
    return vector;
  }
  
  public static String preparaSQLSelectObjetoNegocio (ObjetoNegocio pObj, String pNomeTabela) throws RepositorioException
  {
    String string;
    try
      {
	StringBuffer strBuff = new StringBuffer ();
	strBuff.append ("SELECT * FROM ");
	strBuff.append (pNomeTabela);
	montaClausulaSQLWhere (strBuff, pObj);
	strBuff.append (" ; ");
	string = strBuff.toString ();
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	throw new RepositorioException ("Erro no preparo no SQL de select do objeto de neg\u00f3cio:", e);
      }
    return string;
  }
  
}
