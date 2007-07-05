/* FabricaDB - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.embeddedDB;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Iterator;

import serpro.ppgd.negocio.IdDeclaracao;
import serpro.ppgd.negocio.IdUsuario;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.repositorio.CadastroId;
import serpro.ppgd.repositorio.RepositorioException;
import serpro.ppgd.repositorio.embeddedDB.util.UtilDB;

public abstract class FabricaDB
{
  private static CadastroId cadastroId = null;
  
  public static CadastroId getCadastroId ()
  {
    if (cadastroId == null)
      {
	serpro.ppgd.repositorio.RepositorioIdIf repositorioId = new RepositorioIdDB (FabricaUtilitarios.nomeClasseId);
	serpro.ppgd.repositorio.RepositorioIdDeclaracaoIf repositorioIdDeclaracao = new RepositorioIdDeclaracaoDB (FabricaUtilitarios.nomeClasseIdDeclaracao);
	serpro.ppgd.repositorio.RepositorioObjetoNegocioIf repositorioObjetoNegocios = new RepositorioObjetoNegocioDB (FabricaUtilitarios.nomeClasseDeclaracao);
	cadastroId = new CadastroId (repositorioId, repositorioIdDeclaracao, repositorioObjetoNegocios);
      }
    return cadastroId;
  }
  
  public static void criaTabelas () throws RepositorioException
  {
    Iterator itSqlCreate;
    try
      {
	UtilDB.getConexao ().setAutoCommit (false);
	IdUsuario idTemporario = (IdUsuario) Class.forName (FabricaUtilitarios.nomeClasseId).newInstance ();
	UtilDB.executarDDL (RepositorioIdDAO.preparaSQLCriacaoTabelaId (Class.forName (FabricaUtilitarios.nomeClasseId)));
	UtilDB.executarDDL (RepositorioIdDeclaracaoDAO.preparaSQLCriacaoTabelaIdDeclaracao (idTemporario, Class.forName (FabricaUtilitarios.nomeClasseIdDeclaracao)));
	if (FabricaUtilitarios.nomeClasseDeclaracao == null)
	  return;
	Class[] argumentosFormais = { serpro.ppgd.negocio.IdUsuario.class };
	Object[] argumentosReais = { idTemporario };
	Constructor construtor = Class.forName (FabricaUtilitarios.nomeClasseIdDeclaracao).getConstructor (argumentosFormais);
	IdDeclaracao idDeclTemp = (IdDeclaracao) construtor.newInstance (argumentosReais);
	argumentosFormais = new Class[] { serpro.ppgd.negocio.IdDeclaracao.class };
	argumentosReais = new Object[] { idDeclTemp };
	construtor = Class.forName (FabricaUtilitarios.nomeClasseDeclaracao).getConstructor (argumentosFormais);
	ObjetoNegocio decl = (ObjetoNegocio) construtor.newInstance (argumentosReais);
	itSqlCreate = UtilDB.preparaSqlCriacaoTabelasObjetoNegocio (decl, "DECLARACAO").iterator ();
	while (itSqlCreate.hasNext ())
	  {
	    String sql = (String) itSqlCreate.next ();
	    LogPPGD.debug ("sql->" + sql);
	    try
	      {
		UtilDB.executarDDL (sql);
	      }
	    catch (Exception e)
	      {
		e.printStackTrace ();
		UtilDB.getConexao ().rollback ();
		return;
	      }
	  }
	UtilDB.getConexao ().commit ();
      }
    catch (ClassNotFoundException e)
      {
	throw new RepositorioException ("Classe n\u00e3o encontrada", e);
      }
    catch (InstantiationException e)
      {
	throw new RepositorioException ("Erro de instancia\u00e7\u00e3o", e);
      }
    catch (IllegalAccessException e)
      {
	throw new RepositorioException ("Acesso ao m\u00e9todo ou construtor ilegal", e);
      }
    catch (InvocationTargetException e)
      {
	throw new RepositorioException ("Erro de invoca\u00e7\u00e3o ao m\u00e9todo ou construtor", e);
      }
    catch (NoSuchMethodException e)
      {
	throw new RepositorioException ("M\u00e9todo ou construtor n\u00e3o encontrado", e);
      }
    catch (SQLException e)
      {
	e.printStackTrace ();
	throw new RepositorioException ("Erro de SQL", e);
      }
  }
  
  public static void recriaTabelas () throws RepositorioException
  {
    try
      {
	try
	  {
	    removeTabelas ();
	  }
	catch (Exception e)
	  {
	    e.printStackTrace ();
	  }
	IdUsuario idTemporario = (IdUsuario) Class.forName (FabricaUtilitarios.nomeClasseId).newInstance ();
	try
	  {
	    UtilDB.executarDDL (RepositorioIdDAO.preparaSQLCriacaoTabelaId (Class.forName (FabricaUtilitarios.nomeClasseId)));
	  }
	catch (Exception e)
	  {
	    e.printStackTrace ();
	  }
	try
	  {
	    UtilDB.executarDDL (RepositorioIdDeclaracaoDAO.preparaSQLCriacaoTabelaIdDeclaracao (idTemporario, Class.forName (FabricaUtilitarios.nomeClasseIdDeclaracao)));
	  }
	catch (Exception e)
	  {
	    e.printStackTrace ();
	  }
	if (FabricaUtilitarios.nomeClasseDeclaracao != null)
	  {
	    Class[] argumentosFormais = { serpro.ppgd.negocio.IdUsuario.class };
	    Object[] argumentosReais = { idTemporario };
	    Constructor construtor = Class.forName (FabricaUtilitarios.nomeClasseIdDeclaracao).getConstructor (argumentosFormais);
	    IdDeclaracao idDeclTemp = (IdDeclaracao) construtor.newInstance (argumentosReais);
	    argumentosFormais = new Class[] { serpro.ppgd.negocio.IdDeclaracao.class };
	    argumentosReais = new Object[] { idDeclTemp };
	    construtor = Class.forName (FabricaUtilitarios.nomeClasseDeclaracao).getConstructor (argumentosFormais);
	    ObjetoNegocio decl = (ObjetoNegocio) construtor.newInstance (argumentosReais);
	    Iterator itSqlCreate = UtilDB.preparaSqlCriacaoTabelasObjetoNegocio (decl, "DECLARACAO").iterator ();
	    while (itSqlCreate.hasNext ())
	      {
		String sql = (String) itSqlCreate.next ();
		LogPPGD.debug ("sql->" + sql);
		try
		  {
		    UtilDB.executarDDL (sql);
		  }
		catch (Exception e)
		  {
		    e.printStackTrace ();
		  }
	      }
	  }
      }
    catch (ClassNotFoundException e)
      {
	throw new RepositorioException ("Classe n\u00e3o encontrada", e);
      }
    catch (InstantiationException e)
      {
	throw new RepositorioException ("Erro de instancia\u00e7\u00e3o", e);
      }
    catch (IllegalAccessException e)
      {
	throw new RepositorioException ("Acesso ao m\u00e9todo ou construtor ilegal", e);
      }
    catch (InvocationTargetException e)
      {
	throw new RepositorioException ("Erro de invoca\u00e7\u00e3o ao m\u00e9todo ou construtor", e);
      }
    catch (NoSuchMethodException e)
      {
	throw new RepositorioException ("M\u00e9todo ou construtor n\u00e3o encontrado", e);
      }
  }
  
  public static void removeTabelas () throws RepositorioException
  {
    try
      {
	UtilDB.getConexao ().setAutoCommit (false);
	try
	  {
	    UtilDB.executarDDL ("DROP TABLE ID");
	  }
	catch (Exception e)
	  {
	    e.printStackTrace ();
	  }
	try
	  {
	    UtilDB.executarDDL ("DROP TABLE IdDeclaracao");
	  }
	catch (Exception e)
	  {
	    e.printStackTrace ();
	  }
	IdUsuario idTemporario = (IdUsuario) Class.forName (FabricaUtilitarios.nomeClasseId).newInstance ();
	FabricaUtilitarios.nomeClasseDeclaracao = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.classes.declaracao");
	if (FabricaUtilitarios.nomeClasseDeclaracao != null)
	  {
	    Class[] argumentosFormais = { serpro.ppgd.negocio.IdUsuario.class };
	    Object[] argumentosReais = { idTemporario };
	    Constructor construtor = Class.forName (FabricaUtilitarios.nomeClasseIdDeclaracao).getConstructor (argumentosFormais);
	    IdDeclaracao idDeclTemp = (IdDeclaracao) construtor.newInstance (argumentosReais);
	    argumentosFormais = new Class[] { serpro.ppgd.negocio.IdDeclaracao.class };
	    argumentosReais = new Object[] { idDeclTemp };
	    construtor = Class.forName (FabricaUtilitarios.nomeClasseDeclaracao).getConstructor (argumentosFormais);
	    ObjetoNegocio decl = (ObjetoNegocio) construtor.newInstance (argumentosReais);
	    Iterator itSqlCreate = UtilDB.preparaSqlRemocaoTabelasObjetoNegocio (decl, "DECLARACAO").iterator ();
	    while (itSqlCreate.hasNext ())
	      {
		String sql = (String) itSqlCreate.next ();
		LogPPGD.debug ("sql->" + sql);
		try
		  {
		    UtilDB.executarDDL (sql);
		  }
		catch (Exception e)
		  {
		    e.printStackTrace ();
		  }
	      }
	    UtilDB.getConexao ().commit ();
	    UtilDB.getConexao ().setAutoCommit (true);
	  }
      }
    catch (ClassNotFoundException e)
      {
	throw new RepositorioException ("Classe n\u00e3o encontrada", e);
      }
    catch (InstantiationException e)
      {
	throw new RepositorioException ("Erro de instancia\u00e7\u00e3o", e);
      }
    catch (IllegalAccessException e)
      {
	throw new RepositorioException ("Acesso ao m\u00e9todo ou construtor ilegal", e);
      }
    catch (InvocationTargetException e)
      {
	throw new RepositorioException ("Erro de invoca\u00e7\u00e3o ao m\u00e9todo ou construtor", e);
      }
    catch (NoSuchMethodException e)
      {
	throw new RepositorioException ("M\u00e9todo ou construtor n\u00e3o encontrado", e);
      }
    catch (SQLException e)
      {
	e.printStackTrace ();
      }
  }
  
}
