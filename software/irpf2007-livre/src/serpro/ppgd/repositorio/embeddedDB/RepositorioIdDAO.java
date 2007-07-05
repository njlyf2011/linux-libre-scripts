/* RepositorioIdDAO - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.embeddedDB;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import serpro.ppgd.negocio.IdUsuario;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.repositorio.RepositorioException;
import serpro.ppgd.repositorio.embeddedDB.util.UtilDB;

public class RepositorioIdDAO
{
  public static final String NOME_TABELA_IDS = "ID";
  private Class classeId;
  
  public RepositorioIdDAO (String pNomeCompletoClasseDoId)
  {
    classeId = serpro.ppgd.negocio.impl.IdUsuarioImpl.class;
    if (pNomeCompletoClasseDoId != null)
      {
	try
	  {
	    classeId = Class.forName (pNomeCompletoClasseDoId);
	  }
	catch (ClassNotFoundException e)
	  {
	    throw new IllegalArgumentException ("O nome da classe de IdUsuario passada como par\u00e2metro \u00e9 inv\u00e1lido!!");
	  }
      }
  }
  
  public static String preparaSQLCriacaoTabelaId (Class pClasseId)
  {
    StringBuffer strBuff = new StringBuffer ();
    strBuff.append ("CREATE TABLE ");
    strBuff.append ("ID");
    strBuff.append (" ( ");
    Iterator itCamposId = FabricaUtilitarios.getFieldsCamposInformacao (pClasseId).iterator ();
    while (itCamposId.hasNext ())
      {
	Field field = (Field) itCamposId.next ();
	strBuff.append (field.getName () + " Varchar ");
	if (itCamposId.hasNext ())
	  strBuff.append (" , ");
      }
    strBuff.append (" ); ");
    return strBuff.toString ();
  }
  
  public IdUsuario criaInstanciaIdConcreto ()
  {
    IdUsuario idusuario;
    try
      {
	Object retorno = classeId.newInstance ();
	idusuario = (IdUsuario) retorno;
      }
    catch (Exception e)
      {
	throw new IllegalArgumentException ("Erro na instancia\u00e7\u00e3o do IdUsuario Concreto :" + e.getMessage ());
      }
    return idusuario;
  }
  
  public void inserir (IdUsuario pObj) throws RepositorioException
  {
    try
      {
	Statement statement = UtilDB.getConexao ().createStatement ();
	StringBuffer strBuff = new StringBuffer ();
	strBuff.append ("INSERT INTO ");
	strBuff.append ("ID");
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
	strBuff.append (" ); ");
	statement.executeUpdate (strBuff.toString ());
	statement.close ();
      }
    catch (SQLException e)
      {
	throw new RepositorioException ("Erro na inser\u00e7\u00e3o do IdUsuario:" + e.getMessage ());
      }
  }
  
  public void alterar (IdUsuario pObj) throws RepositorioException
  {
    try
      {
	Statement statement = UtilDB.getConexao ().createStatement ();
	StringBuffer strBuff = new StringBuffer ();
	strBuff.append ("UPDATE ");
	strBuff.append ("ID");
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
	Iterator itPk = pObj.getListaAtributosPK ().iterator ();
	if (itPk.hasNext ())
	  {
	    strBuff.append (" WHERE ");
	    while (itPk.hasNext ())
	      {
		Field field = FabricaUtilitarios.getField (pObj, itPk.next ());
		strBuff.append (field.getName ());
		strBuff.append (" = ");
		strBuff.append ("'");
		Object valorAtributo = FabricaUtilitarios.getValorField (field, pObj);
		if (valorAtributo != null)
		  strBuff.append (((Informacao) valorAtributo).asString ());
		strBuff.append ("'");
		if (itPk.hasNext ())
		  strBuff.append (" AND ");
	      }
	  }
	statement.executeUpdate (strBuff.toString ());
	statement.close ();
      }
    catch (SQLException e)
      {
	throw new RepositorioException ("Erro na atualiza\u00e7\u00e3o do IdUsuario:" + e.getMessage ());
      }
  }
  
  public void deletar (IdUsuario pObj) throws RepositorioException
  {
    try
      {
	Statement statement = UtilDB.getConexao ().createStatement ();
	StringBuffer strBuff = new StringBuffer ();
	strBuff.append ("DELETE FROM ");
	strBuff.append ("ID");
	Iterator itPk = pObj.getListaAtributosPK ().iterator ();
	if (itPk.hasNext ())
	  {
	    strBuff.append (" WHERE ");
	    while (itPk.hasNext ())
	      {
		Field field = FabricaUtilitarios.getField (pObj, itPk.next ());
		strBuff.append (field.getName ());
		strBuff.append (" = ");
		strBuff.append ("'");
		Object valorAtributo = FabricaUtilitarios.getValorField (field, pObj);
		if (valorAtributo != null)
		  strBuff.append (((Informacao) valorAtributo).asString ());
		strBuff.append ("'");
		if (itPk.hasNext ())
		  strBuff.append (" AND ");
	      }
	  }
	strBuff.append (" ; ");
	statement.executeUpdate (strBuff.toString ());
	statement.close ();
      }
    catch (SQLException e)
      {
	throw new RepositorioException ("Erro na exclus\u00e3o do IdUsuario:" + e.getMessage ());
      }
  }
  
  public List recuperarIds () throws RepositorioException
  {
    List retorno = new Vector ();
    try
      {
	Statement statement = UtilDB.getConexao ().createStatement ();
	StringBuffer strBuff = new StringBuffer ();
	strBuff.append ("SELECT * FROM ");
	strBuff.append ("ID");
	strBuff.append (" ; ");
	ResultSet rs = statement.executeQuery (strBuff.toString ());
	while (rs.next ())
	  {
	    IdUsuario novoId = (IdUsuario) classeId.newInstance ();
	    UtilDB.preencheObjeto (rs, novoId);
	    retorno.add (novoId);
	  }
	rs.close ();
	statement.close ();
      }
    catch (SQLException e)
      {
	throw new RepositorioException ("Erro recupera\u00e7\u00e3o de Ids:" + e.getMessage ());
      }
    catch (InstantiationException e)
      {
	throw new RepositorioException ("Erro recupera\u00e7\u00e3o (Instancia\u00e7\u00e3o) de Ids:" + e.getMessage ());
      }
    catch (IllegalAccessException e)
      {
	throw new RepositorioException ("Erro recupera\u00e7\u00e3o (Acesso inv\u00e1lido) de Ids:" + e.getMessage ());
      }
    return retorno;
  }
  
  public IdUsuario recuperarId (IdUsuario pId) throws RepositorioException
  {
    IdUsuario idusuario;
    try
      {
	IdUsuario retorno = null;
	Statement statement = UtilDB.getConexao ().createStatement ();
	StringBuffer strBuff = new StringBuffer ();
	strBuff.append ("SELECT * FROM ");
	strBuff.append ("ID");
	Iterator itPk = pId.getListaAtributosPK ().iterator ();
	if (itPk.hasNext ())
	  {
	    strBuff.append (" WHERE ");
	    while (itPk.hasNext ())
	      {
		Field field = FabricaUtilitarios.getField (pId, itPk.next ());
		strBuff.append (field.getName ());
		strBuff.append (" = ");
		strBuff.append ("'");
		Object valorAtributo = FabricaUtilitarios.getValorField (field, pId);
		if (valorAtributo != null)
		  strBuff.append (((Informacao) valorAtributo).asString ());
		strBuff.append ("'");
		if (itPk.hasNext ())
		  strBuff.append (" AND ");
	      }
	  }
	strBuff.append (" ; ");
	ResultSet rs = statement.executeQuery (strBuff.toString ());
	while (rs.next ())
	  {
	    retorno = (IdUsuario) classeId.newInstance ();
	    UtilDB.preencheObjeto (rs, retorno);
	  }
	rs.close ();
	statement.close ();
	idusuario = retorno;
      }
    catch (SQLException e)
      {
	throw new RepositorioException ("Erro recupera\u00e7\u00e3o de IdUsuario:" + e.getMessage ());
      }
    catch (InstantiationException e)
      {
	throw new RepositorioException ("Erro recupera\u00e7\u00e3o (Instancia\u00e7\u00e3o) de IdUsuario:" + e.getMessage ());
      }
    catch (IllegalAccessException e)
      {
	throw new RepositorioException ("Erro recupera\u00e7\u00e3o (Acesso inv\u00e1lido) de IdUsuario:" + e.getMessage ());
      }
    return idusuario;
  }
  
}
