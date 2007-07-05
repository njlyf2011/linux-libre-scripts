/* PPGDComboBoxEditor - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.table.editors;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

import serpro.ppgd.gui.EditCampo;

public class PPGDComboBoxEditor extends DefaultCellEditor implements PPGDCellEditorIf
{
  private EditCampo edt;
  
  public PPGDComboBoxEditor (EditCampo pEdit)
  {
    super ((JComboBox) pEdit.getComponenteEditor ());
    edt = pEdit;
  }
  
  public EditCampo getEditCampo ()
  {
    return edt;
  }
}
