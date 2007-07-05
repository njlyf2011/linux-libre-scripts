/* TableFundosInvestimentos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.rendavariavel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;

import serpro.ppgd.gui.EditValor;
import serpro.ppgd.gui.table.GroupableTableColumnModel;
import serpro.ppgd.irpf.gui.IRPFTable;
import serpro.ppgd.irpf.rendavariavel.FundosInvestimentos;

public class TableFundosInvestimentos extends IRPFTable
{
  private static final FundosInvestimentos vazio = new FundosInvestimentos ();
  
  public TableFundosInvestimentos ()
  {
    ((EditValor) getEditorDefault ().getEditCampo ()).setAceitaNumerosNegativos (true);
  }
  
  protected void iniciaModelVazio ()
  {
    setModel (new TableModelFundosInvestimentos (vazio));
  }
  
  protected void executaValidacaoAposEdicao (int col, int row)
  {
    /* empty */
  }
  
  public void proximaCelula (int row, int col)
  {
    /* empty */
  }
  
  protected TableCellEditor instanciaCellEditor (int col)
  {
    if (col == 0)
      return super.instanciaCellEditor (col);
    return getEditorDefault ();
  }
  
  protected void configuraLayout ()
  {
    setRowHeight (20);
    GroupableTableColumnModel cm = (GroupableTableColumnModel) getColumnModel ();
    cm.getColumn (0).setPreferredWidth (200);
  }
  
  public static void main (String[] args)
  {
    JFrame frame = new JFrame ();
    TableFundosInvestimentos tableFundosInvestimentos = new TableFundosInvestimentos ();
    tableFundosInvestimentos.setAutoscrolls (true);
    tableFundosInvestimentos.setAutoResizeMode (0);
    final JScrollPane scrollPane = new JScrollPane (tableFundosInvestimentos);
    scrollPane.getViewport ().addChangeListener (new ChangeListener ()
    {
      public void stateChanged (ChangeEvent e)
      {
	System.out.println ("repaint");
	scrollPane.repaint ();
      }
    });
    frame.getContentPane ().add (scrollPane);
    frame.pack ();
    frame.setVisible (true);
  }
}
