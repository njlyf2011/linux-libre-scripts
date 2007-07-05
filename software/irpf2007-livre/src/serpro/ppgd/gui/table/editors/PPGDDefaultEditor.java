/* PPGDDefaultEditor - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.table.editors;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

import serpro.ppgd.gui.EditCampo;

public class PPGDDefaultEditor extends DefaultCellEditor implements PPGDCellEditorIf
{
  private EditCampo edt;
  
  public PPGDDefaultEditor (EditCampo pEdit)
  {
    super ((JTextField) pEdit.getComponenteEditor ());
    edt = pEdit;
    edt.setPerdeFocoComEnter (false);
  }
  
  public Component getTableCellEditorComponent (JTable table, Object value, boolean isSelected, int row, int column)
  {
    return super.getTableCellEditorComponent (table, value, isSelected, row, column);
  }
  
  public EditCampo getEditCampo ()
  {
    return edt;
  }
}
