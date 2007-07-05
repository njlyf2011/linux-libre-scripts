/* TableGCap - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.ganhosdecapital;
import java.awt.Dimension;

import serpro.ppgd.gui.table.GroupableTableColumnModel;
import serpro.ppgd.irpf.gui.IRPFTable;

public abstract class TableGCap extends IRPFTable
{
  protected void executaValidacaoAposEdicao (int col, int row)
  {
    /* empty */
  }
  
  public void proximaCelula (int row, int col)
  {
    /* empty */
  }
  
  protected void configuraLayout ()
  {
    setRowHeight (20);
    Dimension dim = new Dimension (getTableHeader ().getWidth (), 30);
    getTableHeader ().setPreferredSize (dim);
    GroupableTableColumnModel cm = (GroupableTableColumnModel) getColumnModel ();
    cm.getColumn (0).setPreferredWidth (35);
    cm.getColumn (1).setPreferredWidth (350);
    cm.getColumn (2).setPreferredWidth (120);
  }
}
