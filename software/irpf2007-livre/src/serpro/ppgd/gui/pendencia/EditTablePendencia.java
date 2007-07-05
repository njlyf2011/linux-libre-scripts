/* EditTablePendencia - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.pendencia;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.table.DefaultTableCellRenderer;

import serpro.ppgd.gui.FabricaGUI;
import serpro.ppgd.gui.table.JTableEx;
import serpro.ppgd.negocio.Pendencia;
import serpro.ppgd.negocio.util.UtilitariosString;

public class EditTablePendencia extends JTableEx
{
  protected TableModelPendencia tableModel = null;
  protected int linhaDoMouseOver = -1;
  
  public EditTablePendencia (TableModelPendencia pTableModel)
  {
    ToolTipManager.sharedInstance ().registerComponent (this);
    tableModel = pTableModel;
    setModel (tableModel);
    DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) getTableHeader ().getDefaultRenderer ();
    renderer.setHorizontalAlignment (2);
    configuraLayout ();
    addMouseListener (new SelecionarPendencia (this)
    {
      protected void selecaoCampoPendente (Pendencia pPendencia)
      {
	FabricaGUI.mudaCursor (EditTablePendencia.this, 0);
	selecionaPendencia (pPendencia);
      }
    });
    for (int i = 0; i < getColumnCount (); i++)
      getColumnModel ().getColumn (i).setPreferredWidth (tableModel.getTamColunas ()[i]);
    getColumnModel ().getColumn (1).setCellRenderer (new DefaultTableCellRenderer ()
    {
      public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
      {
	super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
	if (column == 1 && row == linhaDoMouseOver && getText ().trim ().length () != 0)
	  {
	    formataTextoDaPendenciaOnMouseOver (this);
	    return this;
	  }
	formataTextoDaPendencia (this);
	return this;
      }
    });
    setAutoResizeMode (0);
    addMouseMotionListener (new MouseMotionAdapter ()
    {
      public void mouseMoved (MouseEvent me)
      {
	EditTablePendencia.this.setToolTipText ("");
	int lin = EditTablePendencia.this.rowAtPoint (new Point (me.getX (), me.getY ()));
	int col = EditTablePendencia.this.columnAtPoint (new Point (me.getX (), me.getY ()));
	if (col == 1 && lin != -1)
	  {
	    Pendencia p = tableModel.getPendenciaAt (lin);
	    if (p != null && p.getSeveridade () != 0)
	      {
		onMouseOverDaPendencia ();
		linhaDoMouseOver = lin;
		EditTablePendencia.this.setToolTipText ("<HTML><B><LEFT>" + UtilitariosString.insereQuebraDeLinha (p.getMsg (), 40, "<BR>") + "</LEFT></B></HTML>");
		EditTablePendencia.this.repaint ();
		return;
	      }
	  }
	linhaDoMouseOver = -1;
	EditTablePendencia.this.setCursor (Cursor.getPredefinedCursor (0));
      }
    });
    addMouseListener (new MouseAdapter ()
    {
      public void mouseExited (MouseEvent e)
      {
	linhaDoMouseOver = -1;
	EditTablePendencia.this.setCursor (Cursor.getPredefinedCursor (0));
	EditTablePendencia.this.repaint ();
      }
    });
  }
  
  public Object getValueAt (int row, int column)
  {
    Object retorno = super.getValueAt (row, column);
    if (column == 1)
      {
	LinhaPendencia linhaPendencia = ((TableModelPendencia) getModel ()).getLinhaPendenciaAt (row);
	if (linhaPendencia != null && linhaPendencia.getSeveridade () != 0)
	  {
	    String texto = (String) retorno;
	    JLabel lbl = new JLabel (texto);
	    if (getRowHeight () < lbl.getPreferredSize ().height)
	      setRowHeight (row, lbl.getPreferredSize ().height + lbl.getPreferredSize ().height / 2);
	  }
	else if (linhaPendencia != null && linhaPendencia.getSeveridade () == 0)
	  {
	    String texto = (String) retorno;
	    JLabel lbl = new JLabel (texto);
	    if (getRowHeight () < lbl.getPreferredSize ().height)
	      setRowHeight (row, lbl.getPreferredSize ().height + lbl.getPreferredSize ().height / 4);
	  }
      }
    return retorno;
  }
  
  protected void configuraLayout ()
  {
    /* empty */
  }
  
  protected void onMouseOverDaPendencia ()
  {
    setCursor (Cursor.getPredefinedCursor (12));
  }
  
  protected void formataTextoDaPendenciaOnMouseOver (JLabel pLblTexto)
  {
    /* empty */
  }
  
  protected void formataTextoDaPendencia (JLabel pLblTexto)
  {
    /* empty */
  }
  
  public void selecionaPendencia (Pendencia pPendencia)
  {
    /* empty */
  }
}
