/* TableQuadroAuxiliarTransporteValores - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import java.awt.Color;
import java.awt.Dimension;

import serpro.ppgd.gui.table.GroupableTableColumnModel;
import serpro.ppgd.irpf.ColecaoItemQuadroAuxiliar;

public class TableQuadroAuxiliarTransporteValores extends IRPFTable
{
  public TableQuadroAuxiliarTransporteValores ()
  {
    Dimension dim = new Dimension (getTableHeader ().getWidth (), 25);
    getTableHeader ().setPreferredSize (dim);
    setColumnSelectionAllowed (false);
    setBackground (Color.WHITE);
  }
  
  protected void setaDefaultRenderers ()
  {
    /* empty */
  }
  
  public boolean getScrollableTracksViewportHeight ()
  {
    return true;
  }
  
  public boolean getScrollableTracksViewportWidth ()
  {
    return true;
  }
  
  protected void iniciaModelVazio ()
  {
    setModel (new TableModelQuadroAuxiliarTransporteValores (new ColecaoItemQuadroAuxiliar ()));
  }
  
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
    Dimension dim = new Dimension (getTableHeader ().getWidth (), 50);
    getTableHeader ().setPreferredSize (dim);
    GroupableTableColumnModel cm = (GroupableTableColumnModel) getColumnModel ();
    cm.getColumn (0).setPreferredWidth (85);
    cm.getColumn (1).setPreferredWidth (45);
  }
}
