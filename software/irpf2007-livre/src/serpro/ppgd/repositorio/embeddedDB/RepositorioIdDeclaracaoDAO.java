/* RepositorioIdDeclaracaoDAO - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.embeddedDB;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.IdDeclaracao;
import serpro.ppgd.negocio.IdUsuario;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.impl.IdUsuarioImpl;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.repositorio.RepositorioException;
import serpro.ppgd.repositorio.embeddedDB.util.UtilDB;

public class RepositorioIdDeclaracaoDAO
{
  public static final String NOME_TABELA_ID_DECLARACAO = "IdDeclaracao";
  private Class classeIdDec;
  
  public RepositorioIdDeclaracaoDAO (String pNomeCompletoClasseDoIdDeclaracao)
  {
    classeIdDec = serpro.ppgd.negocio.impl.IdDeclaracaoImpl.class;
    if (pNomeCompletoClasseDoIdDeclaracao != null)
      {
	try
	  {
	    classeIdDec = Class.forName (pNomeCompletoClasseDoIdDeclaracao);
	  }
	catch (ClassNotFoundException e)
	  {
	    throw new IllegalArgumentException ("O nome da classe de IdDeclaracao passada como par\u00e2metro \u00e9 inv\u00e1lido!!");
	  }
      }
  }
  
  private RepositorioIdDeclaracaoDAO ()
  {
    classeIdDec = serpro.ppgd.negocio.impl.IdDeclaracaoImpl.class;
  }
  
  public static String preparaSQLCriacaoTabelaIdDeclaracao (IdUsuario pId, Class pClasseIdDeclaracao)
  {
    StringBuffer strBuff = new StringBuffer ();
    if (pId == null)
      pId = new IdUsuarioImpl ();
    strBuff.append ("CREATE TABLE ");
    strBuff.append ("IdDeclaracao");
    strBuff.append (" ( ");
    Iterator itCamposId = FabricaUtilitarios.getFieldsCamposInformacao (pClasseIdDeclaracao).iterator ();
    while (itCamposId.hasNext ())
      {
	Field field = (Field) itCamposId.next ();
	strBuff.append (field.getName () + " Varchar ");
	if (itCamposId.hasNext ())
	  strBuff.append (" , ");
      }
    itCamposId = pId.getListaAtributosPK ().iterator ();
    if (itCamposId.hasNext ())
      strBuff.append (" , ");
    while (itCamposId.hasNext ())
      {
	Field field = FabricaUtilitarios.getField (pId, itCamposId.next ());
	strBuff.append (field.getName () + " Varchar ");
	if (itCamposId.hasNext ())
	  strBuff.append (" , ");
      }
    strBuff.append (" ); ");
    return strBuff.toString ();
  }
  
  public IdDeclaracao criaInstanciaIdDeclaracaoConcreto (IdUsuario pId)
  {
    IdDeclaracao iddeclaracao;
    try
      {
	Class[] argumentosFormais = { serpro.ppgd.negocio.IdUsuario.class };
	Object[] argumentosReais = { pId };
	Constructor construtor = classeIdDec.getConstructor (argumentosFormais);
	Object retorno = construtor.newInstance (argumentosReais);
	iddeclaracao = (IdDeclaracao) retorno;
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	throw new IllegalArgumentException ("Erro na instancia\u00e7\u00e3o do IdDeclaracao Concreto :" + e.getMessage ());
      }
    return iddeclaracao;
  }
  
  public IdDeclaracao criarIdDeclaracaoNaoPersistido (IdUsuario id)
  {
    IdDeclaracao idDeclaracao = criaInstanciaIdDeclaracaoConcreto (id);
    idDeclaracao.getExercicio ().setConteudo (ConstantesGlobais.EXERCICIO);
    idDeclaracao.getTipo ().setConteudo ("DECLARA\u00c7\u00c3O DE AJUSTE ANUAL");
    return idDeclaracao;
  }
  
  public void inserir (IdDeclaracao pObj) throws RepositorioException
  {
    try
      {
	Statement statement = UtilDB.getConexao ().createStatement ();
	StringBuffer strBuff = new StringBuffer ();
	strBuff.append ("INSERT INTO ");
	strBuff.append ("IdDeclaracao");
	strBuff.append (" VALUES ( ");
	Iterator itCamposId = FabricaUtilitarios.getFieldsCamposInformacao (pObj.getClass ()).iterator ();
	while (itCamposId.hasNext ())
	  {
	    Field field = (Field) itCamposId.next ();
	    strBuff.append ("'");
	    Object valorAtributo = FabricaUtilitarios.getValorField (field, pObj);
	    if (valorAtributo != null)
	      strBuff.append (((Informacao) valorAtributo).asString ());
	    strBuff.append ("'");
	    if (itCamposId.hasNext ())
	      strBuff.append (" , ");
	  }
	itCamposId = pObj.getId ().getListaAtributosPK ().iterator ();
	if (itCamposId.hasNext ())
	  strBuff.append (" , ");
	while (itCamposId.hasNext ())
	  {
	    Field field = FabricaUtilitarios.getField (pObj.getId (), itCamposId.next ());
	    strBuff.append ("'");
	    Object valorAtributo = FabricaUtilitarios.getValorField (field, pObj.getId ());
	    if (valorAtributo != null)
	      strBuff.append (((Informacao) valorAtributo).asString ());
	    strBuff.append ("'");
	    if (itCamposId.hasNext ())
	      strBuff.append (" , ");
	  }
	strBuff.append (" ); ");
	statement.execute (strBuff.toString ());
	statement.close ();
      }
    catch (SQLException e)
      {
	throw new RepositorioException ("Erro na inser\u00e7\u00e3o do IdDeclaracao:" + e.getMessage ());
      }
  }
  
  public void alterar (IdDeclaracao pObj) throws RepositorioException
  {
    try
      {
	Statement statement = UtilDB.getConexao ().createStatement ();
	StringBuffer strBuff = new StringBuffer ();
	strBuff.append ("UPDATE ");
	strBuff.append ("IdDeclaracao");
	strBuff.append (" SET ");
	Iterator itCamposId = FabricaUtilitarios.getFieldsCamposInformacao (pObj.getClass ()).iterator ();
	while (itCamposId.hasNext ())
	  {
	    Field field = (Field) itCamposId.next ();
	    strBuff.append (field.getName ());
	    strBuff.append (" = ");
	    strBuff.append ("'");
	    Object valorAtributo = FabricaUtilitarios.getValorField (field, pObj);
	    if (valorAtributo != null)
	      strBuff.append (((Informacao) valorAtributo).asString ());
	    strBuff.append ("'");
	    if (itCamposId.hasNext ())
	      strBuff.append (" , ");
	  }
	adicionarClausulaSQL_WHERE (pObj, strBuff);
	strBuff.append (" ; ");
	statement.executeUpdate (strBuff.toString ());
	statement.close ();
      }
    catch (SQLException e)
      {
	throw new RepositorioException ("Erro na atualiza\u00e7\u00e3o do IdDeclaracao:" + e.getMessage ());
      }
  }
  
  public void deletar (IdDeclaracao pObj) throws RepositorioException
  {
    try
      {
	Statement statement = UtilDB.getConexao ().createStatement ();
	StringBuffer strBuff = new StringBuffer ();
	strBuff.append ("DELETE FROM ");
	strBuff.append ("IdDeclaracao");
	adicionarClausulaSQL_WHERE (pObj, strBuff);
	strBuff.append (" ; ");
	int i = statement.executeUpdate (strBuff.toString ());
	LogPPGD.debug ("i->" + i);
	statement.close ();
      }
    catch (SQLException e)
      {
	throw new RepositorioException ("Erro na exclus\u00e3o do IdDeclaracao:" + e.getMessage ());
      }
  }
  
  public List recuperarIdDeclaracoes (List listIds) throws RepositorioException
  {
    List retorno = new Vector ();
    Iterator itIds = listIds.iterator ();
    while (itIds.hasNext ())
      {
	IdUsuario idAtual = (IdUsuario) itIds.next ();
	try
	  {
	    IdDeclaracao idDecl = null;
	    Statement statement = UtilDB.getConexao ().createStatement ();
	    StringBuffer strBuff = new StringBuffer ();
	    strBuff.append ("SELECT * FROM ");
	    strBuff.append ("IdDeclaracao");
	    strBuff.append (",");
	    strBuff.append ("ID");
	    strBuff.append (" WHERE  ");
	    Iterator itPk = idAtual.getListaAtributosPK ().iterator ();
	    while (itPk.hasNext ())
	      {
		Field field = FabricaUtilitarios.getField (idAtual, itPk.next ());
		Informacao info = (Informacao) FabricaUtilitarios.getValorField (field, idAtual);
		strBuff.append ("IdDeclaracao." + field.getName ());
		strBuff.append (" = ");
		strBuff.append ("ID." + field.getName ());
		strBuff.append (" AND ");
		strBuff.append (field.getName ());
		strBuff.append (" = ");
		strBuff.append (info.asString ());
		if (itPk.hasNext ())
		  strBuff.append (" AND ");
	      }
	    strBuff.append (" ; ");
	    ResultSet rs = statement.executeQuery (strBuff.toString ());
	    while (rs.next ())
	      {
		idDecl = criaInstanciaIdDeclaracaoConcreto (idAtual);
		UtilDB.preencheObjeto (rs, idDecl);
		retorno.add (idDecl);
	      }
	    rs.close ();
	    statement.close ();
	  }
	catch (SQLException e)
	  {
	    throw new RepositorioException ("Erro recupera\u00e7\u00e3o de IdDeclaracao:" + e.getMessage ());
	  }
      }
    return retorno;
  }
  
  public IdDeclaracao recuperarIdDeclaracao (IdDeclaracao pObj) throws RepositorioException
  {
    IdDeclaracao iddeclaracao;
    try
      {
	IdDeclaracao retorno = null;
	Statement statement = UtilDB.getConexao ().createStatement ();
	StringBuffer strBuff = new StringBuffer ();
	strBuff.append ("SELECT * FROM ");
	strBuff.append ("IdDeclaracao");
	adicionarClausulaSQL_WHERE (pObj, strBuff);
	strBuff.append (" ; ");
	ResultSet rs = statement.executeQuery (strBuff.toString ());
	while (rs.next ())
	  {
	    retorno = criaInstanciaIdDeclaracaoConcreto (pObj.getId ());
	    UtilDB.preencheObjeto (rs, retorno);
	    UtilDB.preencheObjeto (rs, retorno.getId ());
	  }
	rs.close ();
	statement.close ();
	iddeclaracao = retorno;
      }
    catch (SQLException e)
      {
	throw new RepositorioException ("Erro recupera\u00e7\u00e3o de IdUsuario:" + e.getMessage ());
      }
    return iddeclaracao;
  }
  
  private void adicionarClausulaSQL_WHERE (IdDeclaracao pObj, StringBuffer strBuff)
  {
    Iterator itPk = pObj.getListaAtributosPK ().iterator ();
    if (itPk.hasNext ())
      {
	strBuff.append (" WHERE ");
	while (itPk.hasNext ())
	  {
	    Informacao atributoAtual = (Informacao) itPk.next ();
	    boolean ehCampoDoId = false;
	    Field field = FabricaUtilitarios.getField (pObj, atributoAtual);
	    if (field == null)
	      {
		field = FabricaUtilitarios.getField (pObj.getId (), atributoAtual);
		ehCampoDoId = true;
	      }
	    strBuff.append (field.getName ());
	    strBuff.append (" = ");
	    if (atributoAtual != null && atributoAtual.asString ().trim ().length () > 0)
	      {
		strBuff.append ("'");
		strBuff.append (atributoAtual.asString ());
		strBuff.append ("'");
	      }
	    else
	      strBuff.append (field.getName ());
	    if (itPk.hasNext ())
	      strBuff.append (" AND ");
	  }
      }
  }
  
}
