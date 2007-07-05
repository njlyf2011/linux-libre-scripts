/* RepositorioObjetoNegocioDAO - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.embeddedDB;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.IdDeclaracao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.repositorio.RepositorioException;
import serpro.ppgd.repositorio.embeddedDB.util.UtilDB;

public class RepositorioObjetoNegocioDAO
{
  private Class classeDec = null;
  private String nomeTabelaDeclaracao = "DECLARACAO";
  
  public RepositorioObjetoNegocioDAO (String pNomeCompletoClasseDaDeclaracao, String pNomeTabela)
  {
    if (pNomeCompletoClasseDaDeclaracao != null)
      {
	try
	  {
	    classeDec = Class.forName (pNomeCompletoClasseDaDeclaracao);
	    if (pNomeTabela != null)
	      nomeTabelaDeclaracao = pNomeTabela;
	  }
	catch (ClassNotFoundException e)
	  {
	    throw new IllegalArgumentException ("O nome da classe da Declaracao passada como par\u00e2metro \u00e9 inv\u00e1lido!!");
	  }
      }
    else
      throw new IllegalArgumentException ("O nome da classe da Declaracao passada como par\u00e2metro \u00e9 inv\u00e1lido!!");
  }
  
  private RepositorioObjetoNegocioDAO ()
  {
    /* empty */
  }
  
  public ObjetoNegocio criarNaoPersistido (IdDeclaracao pIdDec) throws RepositorioException
  {
    return criaInstanciaObjetoNegocio (pIdDec, classeDec);
  }
  
  public void inserir (ObjetoNegocio pObj) throws RepositorioException
  {
    Iterator itSqlInsert = UtilDB.preparaSqlInsertTabelasObjetoNegocio (pObj, nomeTabelaDeclaracao).iterator ();
    String sqlAtual = "";
    try
      {
	while (itSqlInsert.hasNext ())
	  {
	    sqlAtual = (String) itSqlInsert.next ();
	    Statement statement = UtilDB.getConexao ().createStatement ();
	    statement.executeUpdate (sqlAtual);
	    statement.close ();
	  }
      }
    catch (SQLException e)
      {
	e.printStackTrace ();
	throw new RepositorioException ("Erro na execu\u00e7\u00e3o do insert :" + sqlAtual, e);
      }
  }
  
  public void alterar (ObjetoNegocio pObj) throws RepositorioException
  {
    Iterator itSqlInsert = UtilDB.preparaSqlUpdateTabelasObjetoNegocio (pObj, nomeTabelaDeclaracao).iterator ();
    String sqlAtual = "";
    try
      {
	while (itSqlInsert.hasNext ())
	  {
	    sqlAtual = (String) itSqlInsert.next ();
	    Statement statement = UtilDB.getConexao ().createStatement ();
	    statement.executeUpdate (sqlAtual);
	    statement.close ();
	  }
      }
    catch (SQLException e)
      {
	e.printStackTrace ();
	throw new RepositorioException ("Erro na execu\u00e7\u00e3o do UPDATE :" + sqlAtual, e);
      }
  }
  
  public void excluir (ObjetoNegocio pObj) throws RepositorioException
  {
    Iterator itSqlInsert = UtilDB.preparaSqlDeleteTabelasObjetoNegocio (pObj, nomeTabelaDeclaracao).iterator ();
    String sqlAtual = "";
    try
      {
	while (itSqlInsert.hasNext ())
	  {
	    sqlAtual = (String) itSqlInsert.next ();
	    Statement statement = UtilDB.getConexao ().createStatement ();
	    statement.executeUpdate (sqlAtual);
	    statement.close ();
	  }
      }
    catch (SQLException e)
      {
	e.printStackTrace ();
	throw new RepositorioException ("Erro na execu\u00e7\u00e3o do DELETE :" + sqlAtual, e);
      }
  }
  
  public ObjetoNegocio recuperar (IdDeclaracao pIdDeclaracao) throws RepositorioException
  {
    ObjetoNegocio retorno = criaInstanciaObjetoNegocio (pIdDeclaracao, classeDec);
    recuperarObjetoNegocio (retorno, pIdDeclaracao, nomeTabelaDeclaracao);
    return retorno;
  }
  
  private void recuperarObjetoNegocio (ObjetoNegocio pObj, IdDeclaracao pIdDeclaracao, String pNomeTabela) throws RepositorioException
  {
    try
      {
	preencheObjetoNegocio (pObj, pIdDeclaracao, classeDec, pNomeTabela);
	Field[] fields = pObj.getClass ().getDeclaredFields ();
	for (int i = 0; i < fields.length; i++)
	  {
	    if ((serpro.ppgd.negocio.ObjetoNegocio.class).isAssignableFrom (fields[i].getType ()))
	      {
		ObjetoNegocio valor = (ObjetoNegocio) FabricaUtilitarios.getValorField (fields[i], pObj);
		recuperarObjetoNegocio (valor, pIdDeclaracao, fields[i].getName ());
		if ((serpro.ppgd.negocio.Colecao.class).isAssignableFrom (fields[i].getType ()) && ((Colecao) valor).getTipoItens () != null)
		  preencheColecao ((Colecao) valor, pIdDeclaracao, fields[i].getName ());
	      }
	  }
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	throw new RepositorioException ("Erro na gera\u00e7\u00e3o da lista de Sql de Delete das tabelas:", e);
      }
  }
  
  private ObjetoNegocio criaInstanciaObjetoNegocio (IdDeclaracao pIdDec, Class classeObj) throws RepositorioException
  {
    ObjetoNegocio objetonegocio;
    try
      {
	Class[] argumentosFormais = { serpro.ppgd.negocio.IdDeclaracao.class };
	Object[] argumentosReais = { pIdDec };
	Constructor construtor = classeObj.getConstructor (argumentosFormais);
	Object retorno = construtor.newInstance (argumentosReais);
	objetonegocio = (ObjetoNegocio) retorno;
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	throw new IllegalArgumentException ("Erro na instancia\u00e7\u00e3o do objetode neg\u00f3cio :" + e.getMessage ());
      }
    return objetonegocio;
  }
  
  private void preencheObjetoNegocio (ObjetoNegocio pObj, IdDeclaracao pIdDec, Class pTipoClasse, String pNomeTabela) throws RepositorioException
  {
    String sql = "";
    try
      {
	sql = UtilDB.preparaSQLSelectObjetoNegocio (pObj, pNomeTabela);
	Statement statement = UtilDB.getConexao ().createStatement ();
	ResultSet rs = statement.executeQuery (sql);
	if (rs.next ())
	  UtilDB.preencheObjeto (rs, pObj);
	statement.close ();
	rs.close ();
      }
    catch (SQLException e)
      {
	e.printStackTrace ();
	throw new RepositorioException ("Erro na execu\u00e7\u00e3o do SELECT :" + sql, e);
      }
  }
  
  private void preencheColecao (Colecao pColecao, IdDeclaracao pIdDec, String pNomeAtributoColecao) throws RepositorioException
  {
    String sql = "";
    try
      {
	String nomeTabela = pColecao.getTipoItens ().getName ();
	nomeTabela = nomeTabela.substring (nomeTabela.lastIndexOf ('.') + 1);
	sql = UtilDB.preparaSQLSelectObjetoNegocio (criaInstanciaObjetoNegocio (pIdDec, pColecao.getTipoItens ()), pNomeAtributoColecao + "_" + nomeTabela);
	Statement statement = UtilDB.getConexao ().createStatement ();
	ResultSet rs = statement.executeQuery (sql);
	while (rs.next ())
	  {
	    ObjetoNegocio instancia = criaInstanciaObjetoNegocio (pIdDec, pColecao.getTipoItens ());
	    UtilDB.preencheObjeto (rs, instancia);
	    pColecao.recuperarLista ().add (instancia);
	  }
	statement.close ();
	rs.close ();
      }
    catch (SQLException e)
      {
	e.printStackTrace ();
	throw new RepositorioException ("Erro na execu\u00e7\u00e3o do SELECT de Colecao:" + sql, e);
      }
  }
  
}
