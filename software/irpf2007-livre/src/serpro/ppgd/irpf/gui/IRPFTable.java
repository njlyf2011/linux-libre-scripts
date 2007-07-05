/* IRPFTable - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;

import serpro.ppgd.gui.EditValor;
import serpro.ppgd.gui.table.GroupableTableColumnModel;
import serpro.ppgd.gui.table.GroupableTableHeader;
import serpro.ppgd.gui.table.JComponentCellEditor;
import serpro.ppgd.gui.table.JComponentCellRenderer;
import serpro.ppgd.gui.table.TabelaComponentes;
import serpro.ppgd.gui.table.editors.PPGDCellEditorIf;
import serpro.ppgd.gui.table.editors.PPGDDefaultEditor;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.util.TabelaMensagens;

public abstract class IRPFTable extends TabelaComponentes
{
  private char veioDoKeyListener = ' ';
  protected TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
  private PPGDDefaultEditor editorDefault = null;
  
  public IRPFTable ()
  {
    ToolTipManager.sharedInstance ().registerComponent (this);
    addMouseMotionListener (new MouseMotionAdapter ()
    {
      public void mouseMoved (MouseEvent me)
      {
	IRPFTable.this.setToolTipText ("");
	int lin = IRPFTable.this.rowAtPoint (new Point (me.getX (), me.getY ()));
	int col = IRPFTable.this.columnAtPoint (new Point (me.getX (), me.getY ()));
	String msg = null;
	if (col != -1 && lin != -1 && col != 0)
	  {
	    Informacao info = ((IRPFTableModel) IRPFTable.this.getModel ()).getInformacaoAt (lin, col);
	    if (info != null && ! info.isVazio ())
	      msg = "<HTML><B><LEFT>" + info.getConteudoFormatado () + "</LEFT></B></HTML>";
	  }
	IRPFTable.this.setToolTipText (msg);
      }
    });
    setColumnModel (new GroupableTableColumnModel ());
    setTableHeader (new GroupableTableHeader ((GroupableTableColumnModel) getColumnModel ()));
    getTableHeader ().setReorderingAllowed (false);
    getTableHeader ().setDefaultRenderer (new DefaultIRPFHeaderCellRenderer ());
    iniciaModelVazio ();
    configuraLayout ();
    setaDefaultRenderers ();
    preparaEditores ();
    setSelectionMode (1);
    addKeyListener (new KeyAdapter ()
    {
      public void keyPressed (KeyEvent e)
      {
	IRPFTable.this.setSurrendersFocusOnKeystroke (true);
	int row = IRPFTable.this.getSelectedRow ();
	int col = IRPFTable.this.getSelectedColumn ();
	if (col != 0)
	  {
	    if (e.getKeyCode () == 10)
	      {
		IRPFTable.this.setSurrendersFocusOnKeystroke (true);
		if (row != -1 && col != -1)
		  {
		    if (IRPFTable.this.isCellEditable (row, col))
		      editarCelula (row, col);
		    else
		      {
			proximaCelula (row, col);
			return;
		      }
		  }
	      }
	    if (e.getKeyCode () != 127 && e.getKeyCode () != 112)
	      {
		int ch = e.getKeyCode ();
		boolean eventosEhInclusao = ch != 9 && ch != 10 && ch != 8 && ch != 127 && ch != 37 && ch != 39 && ch != 38 && ch != 40 && ch != 16 && ch != 18 && ch != 8 && ! e.isAltDown () && ! e.isShiftDown () && ! e.isAltGraphDown () && ! e.isControlDown ();
		if (eventosEhInclusao)
		  {
		    if (row != -1 && col != -1)
		      {
			PPGDCellEditorIf cellEditor = (PPGDCellEditorIf) IRPFTable.this.getCellEditor (row, col);
			if (cellEditor.getEditCampo () instanceof EditValor)
			  {
			    boolean ehCaracterValido = Character.isDigit (e.getKeyChar ()) || e.getKeyChar () == '-' || e.getKeyChar () == ',' || e.getKeyCode () == 10;
			    if (! ehCaracterValido)
			      {
				cancelaKeyTyped (e, cellEditor);
				return;
			      }
			  }
			IRPFTable.this.setValueAt ("", row, col);
			veioDoKeyListener = e.getKeyChar ();
		      }
		    else
		      {
			PPGDCellEditorIf cellEditor = (PPGDCellEditorIf) IRPFTable.this.getCellEditor (0, 0);
			cancelaKeyTyped (e, cellEditor);
		      }
		  }
		else
		  IRPFTable.this.setSurrendersFocusOnKeystroke (false);
	      }
	  }
      }
      
      private void cancelaKeyTyped (KeyEvent e, PPGDCellEditorIf cellEditor)
      {
	IRPFTable.this.setSurrendersFocusOnKeystroke (false);
	e.setKeyChar ('\uffff');
	e.consume ();
	UIManager.getLookAndFeel ().provideErrorFeedback (cellEditor.getEditCampo ().getComponenteFoco ());
	cellEditor.getEditCampo ().getInformacao ().disparaObservadores ();
      }
    });
  }
  
  protected void setaDefaultRenderers ()
  {
    IRPFTable irpftable = this;
    Class var_class = javax.swing.JPanel.class;
    irpftable.setDefaultRenderer (var_class, new JComponentCellRenderer ());
    IRPFTable irpftable_6_ = this;
    Class var_class_7_ = java.lang.String.class;
    irpftable_6_.setDefaultRenderer (var_class_7_, new DefaultIRPFCellRenderer ());
  }
  
  protected void configuraLayout ()
  {
    setRowHeight (20);
    Dimension dim = new Dimension (getTableHeader ().getWidth (), 50);
    getTableHeader ().setPreferredSize (dim);
  }
  
  protected abstract void iniciaModelVazio ();
  
  public boolean getScrollableTracksViewportHeight ()
  {
    return false;
  }
  
  public void preparaEditores ()
  {
    TableColumnModel cm = getColumnModel ();
    setEditorDefault (new PPGDDefaultEditor (new EditValor (new Valor ())));
    getEditorDefault ().setClickCountToStart (1);
    Font fonteDefault = new JLabel ().getFont ();
    getEditorDefault ().getEditCampo ().getComponenteEditor ().setFont (fonteDefault);
    fonteDefault = getEditorDefault ().getEditCampo ().getComponenteEditor ().getFont ();
    ((JTextField) getEditorDefault ().getComponent ()).addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	if (IRPFTable.this.isEditing ())
	  {
	    int row = IRPFTable.this.getEditingRow ();
	    int col = IRPFTable.this.getEditingColumn ();
	    IRPFTable.this.getColumnModel ().getColumn (col).getCellEditor ().stopCellEditing ();
	    proximaCelula (row, col);
	  }
      }
    });
    getEditorDefault ().getComponent ().addFocusListener (new FocusAdapter ()
    {
      boolean keyListener = false;
      private int linhaEditada = -1;
      private int colunaEditada = -1;
      
      public void focusGained (FocusEvent e)
      {
	linhaEditada = IRPFTable.this.getEditingRow ();
	colunaEditada = IRPFTable.this.getEditingColumn ();
	if (veioDoKeyListener != ' ')
	  {
	    ((JTextField) getEditorDefault ().getComponent ()).setText (String.valueOf (veioDoKeyListener));
	    veioDoKeyListener = ' ';
	    keyListener = true;
	  }
	String textoAjustado = ((JTextField) getEditorDefault ().getComponent ()).getText ().trim ();
	final String texto = textoAjustado;
	SwingUtilities.invokeLater (new Runnable ()
	{
	  public void run ()
	  {
	    int pos = texto.length ();
	    if (keyListener)
	      {
		((JTextField) getEditorDefault ().getComponent ()).setSelectionStart (1);
		((JTextField) getEditorDefault ().getComponent ()).setSelectionEnd (1);
		keyListener = false;
	      }
	    else
	      {
		((JTextField) getEditorDefault ().getComponent ()).setSelectionStart (0);
		((JTextField) getEditorDefault ().getComponent ()).setSelectionEnd (pos);
	      }
	  }
	});
      }
      
      public void focusLost (FocusEvent e)
      {
	executaValidacaoAposEdicao (colunaEditada, linhaEditada);
      }
    });
    for (int i = 0; i < cm.getColumnCount (); i++)
      getColumnModel ().getColumn (i).setCellEditor (instanciaCellEditor (i));
  }
  
  protected TableCellEditor instanciaCellEditor (int col)
  {
    return new JComponentCellEditor ();
  }
  
  protected abstract void executaValidacaoAposEdicao (int i, int i_16_);
  
  public void selecionaCelula (int col, int lin)
  {
    setRowSelectionInterval (lin, lin);
    setColumnSelectionInterval (col, col);
  }
  
  public void editarCelula (int pRow, int pCol)
  {
    editCellAt (pRow, pCol);
    ((PPGDCellEditorIf) getColumnModel ().getColumn (pCol).getCellEditor ()).getEditCampo ().setaFoco (true);
  }
  
  public abstract void proximaCelula (int i, int i_17_);
  
  protected boolean processKeyBinding (KeyStroke ks, KeyEvent e, int condition, boolean pressed)
  {
    if (ks.getKeyCode () == 10)
      return true;
    return super.processKeyBinding (ks, e, condition, pressed);
  }
  
  public void setObjetoNegocio (ObjetoNegocio obj)
  {
    ((IRPFTableModel) getModel ()).setObjetoNegocio (obj);
  }
  
  public void editingStopped (ChangeEvent e)
  {
    super.editingStopped (e);
    repaint ();
  }
  
  public Component prepareEditor (TableCellEditor editor, int row, int column)
  {
    ((PPGDCellEditorIf) editor).getEditCampo ().setAssociaInformacao (((IRPFTableModel) getModel ()).getInformacaoAt (row, column));
    return super.prepareEditor (editor, row, column);
  }
  
  public void setEditorDefault (PPGDDefaultEditor editorDefault)
  {
    this.editorDefault = editorDefault;
  }
  
  public PPGDDefaultEditor getEditorDefault ()
  {
    return editorDefault;
  }
}
