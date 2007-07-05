/* PPGDTable - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.table;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;

import serpro.ppgd.gui.EditCampo;
import serpro.ppgd.gui.EditCodigo;
import serpro.ppgd.gui.EditMascara;
import serpro.ppgd.gui.FabricaGUI;
import serpro.ppgd.gui.table.editors.PPGDCellEditorIf;
import serpro.ppgd.gui.table.editors.PPGDComboBoxEditor;
import serpro.ppgd.gui.table.editors.PPGDDefaultEditor;
import serpro.ppgd.gui.table.model.PPGDTableModel;
import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;

public class PPGDTable extends TabelaComponentes
{
  protected PPGDTableModel ppgdTableModel = new PPGDTableModel ();
  private boolean mantemRegistrosEmBranco = true;
  
  class CellRendererPersonalizado extends DefaultTableCellRenderer
  {
    int aHorz;
    
    public CellRendererPersonalizado (int pAHorz)
    {
      aHorz = pAHorz;
    }
    
    protected void setValue (Object value)
    {
      if (value != null)
	{
	  setHorizontalTextPosition (aHorz);
	  setHorizontalAlignment (aHorz);
	  setText (value == null ? "" : value.toString ());
	}
      else
	super.setValue (value);
    }
  }
  
  public void setColecao (Colecao pColecao, int tamMaximoGrid)
  {
    ppgdTableModel = new PPGDTableModel ();
    ppgdTableModel.setTamanhoMaximoGrid (tamMaximoGrid);
    ppgdTableModel.setColecao (pColecao);
    setModel (ppgdTableModel);
    preparaEditores ();
    setAutoResizeMode (2);
  }
  
  public void preparaEditores ()
  {
    java.util.Iterator itCampos = ppgdTableModel.getAtributos ().listIterator ();
    int col = 0;
    while (itCampos.hasNext ())
      {
	itCampos.next ();
	setaEditorColuna (ppgdTableModel.getAtributoInformacao (0, col), col);
	col++;
      }
  }
  
  public Component prepareEditor (TableCellEditor editor, int row, int column)
  {
    Informacao info = ppgdTableModel.getAtributoInformacao (row, column);
    if (editor instanceof PPGDCellEditorIf)
      ((PPGDCellEditorIf) editor).getEditCampo ().setAssociaInformacao (info);
    return super.prepareEditor (editor, row, column);
  }
  
  public void setaEditorColuna (Informacao pInfo, int pCol)
  {
    EditCampo editCampo = null;
    editCampo = FabricaGUI.criaCampo (pInfo);
    if (editCampo instanceof EditMascara)
      ((EditMascara) editCampo).setSelecionaTextoOnFocusGained (false);
    DefaultCellEditor editor;
    if (editCampo instanceof EditCodigo)
      editor = new PPGDComboBoxEditor (editCampo);
    else
      editor = new PPGDDefaultEditor (editCampo);
    customizaEditor (editor, pCol);
    getColumnModel ().getColumn (pCol).setCellEditor (editor);
  }
  
  public void editarCelula (int pRow, int pCol)
  {
    editCellAt (pRow, pCol);
    ((PPGDCellEditorIf) getColumnModel ().getColumn (pCol).getCellEditor ()).getEditCampo ().setaFoco (true);
  }
  
  protected void customizaEditor (DefaultCellEditor pEditor, int pCol)
  {
    /* empty */
  }
  
  public boolean getScrollableTracksViewportHeight ()
  {
    return false;
  }
  
  public void setaLarguraColunas (int[] tamCols)
  {
    TableColumnModel cm = getColumnModel ();
    for (int i = 0; i < cm.getColumnCount (); i++)
      {
	cm.getColumn (i).setPreferredWidth (tamCols[i]);
	cm.getColumn (i).setWidth (tamCols[i]);
      }
    resizeAndRepaint ();
  }
  
  public void setaAlinhamentoColuna (int numCol, int aHorz)
  {
    TableColumnModel cm = getColumnModel ();
    CellRendererPersonalizado renderer = new CellRendererPersonalizado (aHorz);
    cm.getColumn (numCol).setCellRenderer (renderer);
    resizeAndRepaint ();
  }
  
  public void limparLinha (int pLinha)
  {
    if (getCellEditor () != null)
      getCellEditor ().stopCellEditing ();
    ppgdTableModel.limparLinha (pLinha);
    limpaLinhasEmBranco ();
  }
  
  public void limpaLinhasEmBranco ()
  {
    if (! isMantemRegistrosEmBranco ())
      ppgdTableModel.getColecao ().excluirRegistrosEmBranco ();
    repaint ();
  }
  
  public PPGDTableModel getPpgdTableModel ()
  {
    return ppgdTableModel;
  }
  
  public boolean isMantemRegistrosEmBranco ()
  {
    return mantemRegistrosEmBranco;
  }
  
  public void setMantemRegistrosEmBranco (boolean mantemRegistrosEmBranco)
  {
    this.mantemRegistrosEmBranco = mantemRegistrosEmBranco;
  }
  
  public void addItem (ObjetoNegocio obj)
  {
    ((PPGDTableModel) getModel ()).getColecao ().recuperarLista ().add (obj);
    ((PPGDTableModel) getModel ()).fireTableDataChanged ();
  }
}
