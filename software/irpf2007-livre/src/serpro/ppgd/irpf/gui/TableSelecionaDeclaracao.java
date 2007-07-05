/* TableSelecionaDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import serpro.ppgd.gui.table.TabelaComponentes;
import serpro.ppgd.irpf.IdentificadorDeclaracao;

public class TableSelecionaDeclaracao extends TabelaComponentes
{
  public TableSelecionaDeclaracao ()
  {
    setShowGrid (false);
    Dimension dim = new Dimension (getTableHeader ().getWidth (), 25);
    getTableHeader ().setPreferredSize (dim);
    setModel (new TableModelSelecionaDeclaracao ());
    setBackground (Color.WHITE);
    setRowHeight (20);
    getTableHeader ().setReorderingAllowed (false);
    setColumnSelectionAllowed (false);
    setSelectionMode (0);
    TableColumnModel cm = getColumnModel ();
    cm.getColumn (0).setPreferredWidth (180);
    cm.getColumn (1).setPreferredWidth (170);
    cm.getColumn (2).setPreferredWidth (350);
    getTableHeader ().setPreferredSize (new Dimension (700, getTableHeader ().getPreferredSize ().height));
    repaint ();
  }
  
  public void addDeclaracaoSelectionListener (ListSelectionListener listener)
  {
    getSelectionModel ().addListSelectionListener (listener);
  }
  
  public void removeDeclaracaoSelectionListener (ListSelectionListener listener)
  {
    getSelectionModel ().removeListSelectionListener (listener);
  }
  
  public IdentificadorDeclaracao getIdentificadorDeclaracao (int row)
  {
    return ((TableModelSelecionaDeclaracao) getModel ()).getIdentificadorDeclaracao (row);
  }
  
  public List getIdentificadorDeclaracao (int[] rows)
  {
    List retorno = new ArrayList ();
    for (int i = 0; i < rows.length; i++)
      retorno.add (((TableModelSelecionaDeclaracao) getModel ()).getIdentificadorDeclaracao (rows[i]));
    return retorno;
  }
}
