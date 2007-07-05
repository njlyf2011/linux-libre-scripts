/* TableReceitasDespesasBrasil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.atividaderural;
import javax.swing.table.TableCellEditor;

import serpro.ppgd.gui.table.GroupableTableColumnModel;
import serpro.ppgd.irpf.atividaderural.brasil.ReceitasDespesas;
import serpro.ppgd.irpf.gui.IRPFTable;

public class TableReceitasDespesasBrasil extends IRPFTable
{
  private static final ReceitasDespesas vazio = new ReceitasDespesas ();
  
  protected void iniciaModelVazio ()
  {
    setModel (new TableModelReceitasDespesasBrasil (vazio));
  }
  
  protected void configuraLayout ()
  {
    super.configuraLayout ();
    GroupableTableColumnModel cm = (GroupableTableColumnModel) getColumnModel ();
    cm.getColumn (0).setPreferredWidth (15);
    cm.getColumn (1).setPreferredWidth (85);
    cm.getColumn (2).setPreferredWidth (85);
  }
  
  protected TableCellEditor instanciaCellEditor (int col)
  {
    if (col == 0)
      return super.instanciaCellEditor (col);
    return getEditorDefault ();
  }
  
  protected void executaValidacaoAposEdicao (int col, int row)
  {
    /* empty */
  }
  
  public void proximaCelula (int row, int col)
  {
    if (col == 2 && row == 11)
      {
	selecionaCelula (1, 0);
	editarCelula (0, 1);
      }
    else if (col == 2)
      {
	selecionaCelula (1, row + 1);
	editarCelula (row + 1, 1);
      }
    else
      {
	selecionaCelula (col + 1, row);
	editarCelula (row, col + 1);
      }
  }
}
