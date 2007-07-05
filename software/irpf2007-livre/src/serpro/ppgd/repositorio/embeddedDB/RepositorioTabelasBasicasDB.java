/* RepositorioTabelasBasicasDB - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.embeddedDB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import serpro.ppgd.negocio.ElementoTabela;
import serpro.ppgd.negocio.util.UtilitariosString;
import serpro.ppgd.repositorio.RepositorioException;
import serpro.ppgd.repositorio.RepositorioTabelasBasicasIf;
import serpro.ppgd.repositorio.embeddedDB.util.UtilDB;

public class RepositorioTabelasBasicasDB implements RepositorioTabelasBasicasIf
{
  public List recuperarObjetosTabela (String pNomeArquivoOuTabela, boolean testarCRC) throws RepositorioException
  {
    List lstElementoTabela = new Vector ();
    String sql = "SELECT * FROM " + pNomeArquivoOuTabela;
    try
      {
	String crcCalculado = "";
	Statement statement = UtilDB.getConexao ().createStatement ();
	ResultSet rs = statement.executeQuery (sql);
	while (rs.next ())
	  {
	    StringBuffer strBufferLinha = new StringBuffer ();
	    int colCount = rs.getMetaData ().getColumnCount ();
	    ElementoTabela elementoTabelaAtual = new ElementoTabela ();
	    for (int i = 1; i < colCount; i++)
	      {
		elementoTabelaAtual.setConteudo (i - 1, rs.getString (i));
		strBufferLinha.append (elementoTabelaAtual.getConteudo (i - 1));
	      }
	    String crcLido = rs.getString (colCount);
	    crcCalculado = UtilitariosString.GerarCRC ("0", strBufferLinha.toString ());
	    if (crcLido.compareTo (crcCalculado) != 0 && testarCRC)
	      throw new RepositorioException ("Checksum de tabela b\u00e1sica inv\u00e1lido");
	    lstElementoTabela.add (elementoTabelaAtual);
	  }
	statement.close ();
	rs.close ();
      }
    catch (SQLException e)
      {
	e.printStackTrace ();
	throw new RepositorioException ("Erro na execu\u00e7\u00e3o do SELECT :" + sql, e);
      }
    return lstElementoTabela;
  }
  
  public void salvar (String pNomeArquivoOuTabela, List lst) throws RepositorioException
  {
    try
      {
	Iterator itElementos = lst.iterator ();
	if (itElementos.hasNext ())
	  {
	    Statement statementLimpa = UtilDB.getConexao ().createStatement ();
	    statementLimpa.executeUpdate ("DELETE FROM " + pNomeArquivoOuTabela);
	  }
	while (itElementos.hasNext ())
	  {
	    StringBuffer sqlAtual = new StringBuffer ();
	    sqlAtual.append ("INSERT INTO ");
	    sqlAtual.append (pNomeArquivoOuTabela);
	    sqlAtual.append ("  VALUES ( ");
	    ElementoTabela elementoAtual = (ElementoTabela) itElementos.next ();
	    for (int i = 0; i < elementoAtual.size (); i++)
	      {
		String valColAtual = elementoAtual.getConteudo (i);
		sqlAtual.append ("'");
		sqlAtual.append (valColAtual);
		sqlAtual.append ("'");
		sqlAtual.append (",");
	      }
	    sqlAtual.append ("'");
	    sqlAtual.append (UtilitariosString.GerarCRC ("0", sqlAtual.toString ()));
	    sqlAtual.append ("'");
	    sqlAtual.append ("); ");
	    Statement statement = UtilDB.getConexao ().createStatement ();
	    statement.executeUpdate (sqlAtual.toString ());
	    statement.close ();
	  }
      }
    catch (SQLException e)
      {
	e.printStackTrace ();
	throw new RepositorioException ("Erro na execu\u00e7\u00e3o do INSERT :", e);
      }
  }
}
