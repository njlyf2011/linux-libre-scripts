/* TabelaComponentes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.table;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import serpro.ppgd.gui.EditCampo;
import serpro.ppgd.gui.FabricaGUI;
import serpro.ppgd.negocio.CPF;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.RetornosValidacoes;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNaoNulo;

public class TabelaComponentes extends JTable
{
  private static EditCampo edt;
  
  public TabelaComponentes ()
  {
    setRenderEditor ();
  }
  
  public TabelaComponentes (int numRows, int numColumns)
  {
    super (numRows, numColumns);
    setRenderEditor ();
  }
  
  public TabelaComponentes (TableModel dm)
  {
    super (dm);
    setRenderEditor ();
  }
  
  public TabelaComponentes (Object[][] rowData, Object[] columnNames)
  {
    super (rowData, columnNames);
    setRenderEditor ();
  }
  
  public TabelaComponentes (Vector rowData, Vector columnNames)
  {
    super (rowData, columnNames);
    setRenderEditor ();
  }
  
  public TabelaComponentes (TableModel dm, TableColumnModel cm)
  {
    super (dm, cm);
    setRenderEditor ();
  }
  
  public TabelaComponentes (TableModel dm, TableColumnModel cm, ListSelectionModel sm)
  {
    super (dm, cm, sm);
    setRenderEditor ();
  }
  
  private void setRenderEditor ()
  {
    TabelaComponentes tabelacomponentes = this;
    Class var_class = javax.swing.JComponent.class;
    tabelacomponentes.setDefaultRenderer (var_class, new JComponentCellRenderer ());
    TabelaComponentes tabelacomponentes_1_ = this;
    Class var_class_2_ = javax.swing.JComponent.class;
    tabelacomponentes_1_.setDefaultEditor (var_class_2_, new JComponentCellEditor ());
  }
  
  public TableCellRenderer getCellRenderer (int row, int column)
  {
    TableColumn tableColumn = getColumnModel ().getColumn (column);
    TableCellRenderer renderer = tableColumn.getCellRenderer ();
    if (renderer == null)
      {
	Class c = getColumnClass (column);
	Class var_class = c;
	Class var_class_4_ = java.lang.Object.class;
	if (var_class.equals (var_class_4_))
	  {
	    Object o = getValueAt (row, column);
	    if (o != null)
	      c = getValueAt (row, column).getClass ();
	  }
	renderer = getDefaultRenderer (c);
      }
    return renderer;
  }
  
  public TableCellEditor getCellEditor (int row, int column)
  {
    TableColumn tableColumn = getColumnModel ().getColumn (column);
    TableCellEditor editor = tableColumn.getCellEditor ();
    if (editor == null)
      {
	Class c = getColumnClass (column);
	Class var_class = c;
	Class var_class_6_ = java.lang.Object.class;
	if (var_class.equals (var_class_6_))
	  {
	    Object o = getValueAt (row, column);
	    if (o != null)
	      c = getValueAt (row, column).getClass ();
	  }
	editor = getDefaultEditor (c);
      }
    return editor;
  }
  
  public static void main (String[] args)
  {
    TabelaComponentes t = new TabelaComponentes (2, 2);
    CPF alfa = new CPF (null, "Teste")
    {
      public RetornosValidacoes validar ()
      {
	RetornosValidacoes retorno = super.validar ();
	valida (TabelaComponentes.edt, this);
	return retorno;
      }
    };
    alfa.addValidador (new ValidadorNaoNulo ((byte) 3));
    edt = FabricaGUI.criaCampo (alfa, 30);
    Box b = Box.createHorizontalBox ();
    b.add (edt.getRotulo ());
    b.add (edt.getComponenteEditor ());
    b.add (edt.getButtonMensagem ());
    alfa.validar ();
    t.setValueAt (b, 0, 0);
    JFrame f = new JFrame ();
    f.getContentPane ().add (new JScrollPane (t));
    f.setVisible (true);
    f.pack ();
  }
  
  private static void valida (EditCampo edt, Informacao info)
  {
    if (edt != null)
      edt.getButtonMensagem ().setVisible (! info.isValido ());
  }
}
