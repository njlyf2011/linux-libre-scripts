/* TableModelIdDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import serpro.ppgd.negocio.IdDeclaracao;

public abstract class TableModelIdDeclaracao extends AbstractTableModel
{
  private String[] nomeColunas;
  private int[] tamanhoColunas;
  List lstIdDeclaracoes;
  
  public TableModelIdDeclaracao (String[] nomeColunas, List lstIdDeclaracoes, int[] pTamColunas)
  {
    this.nomeColunas = nomeColunas;
    this.lstIdDeclaracoes = lstIdDeclaracoes;
    tamanhoColunas = pTamColunas;
  }
  
  public int getColumnCount ()
  {
    return nomeColunas.length;
  }
  
  public int getRowCount ()
  {
    return lstIdDeclaracoes.size ();
  }
  
  public String getColumnName (int col)
  {
    return nomeColunas[col];
  }
  
  public Object getValueAt (int row, int col)
  {
    IdDeclaracao idDeclaracao = getIdDeclaracaoAt (row);
    if (idDeclaracao != null)
      return preparaExibicaoDaLinha (row, col, idDeclaracao);
    return "";
  }
  
  protected abstract Object preparaExibicaoDaLinha (int i, int i_0_, IdDeclaracao iddeclaracao);
  
  public Class getColumnClass (int c)
  {
    return getValueAt (0, c).getClass ();
  }
  
  public IdDeclaracao getIdDeclaracaoAt (int index)
  {
    if (index < lstIdDeclaracoes.size ())
      return (IdDeclaracao) lstIdDeclaracoes.get (index);
    return null;
  }
  
  public void setLstIdDeclaracao (List lstIdDeclaracoes)
  {
    this.lstIdDeclaracoes = lstIdDeclaracoes;
    fireTableDataChanged ();
  }
  
  public int[] getTamanhoColunas ()
  {
    return tamanhoColunas;
  }
  
  public void setTamanhoColunas (int[] tamanhoColunas)
  {
    this.tamanhoColunas = tamanhoColunas;
  }
}
