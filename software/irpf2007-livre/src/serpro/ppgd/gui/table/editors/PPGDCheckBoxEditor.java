/* PPGDCheckBoxEditor - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.table.editors;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;

import serpro.ppgd.gui.EditCampo;

public class PPGDCheckBoxEditor extends DefaultCellEditor implements PPGDCellEditorIf
{
  private EditCampo edt;
  
  public PPGDCheckBoxEditor (EditCampo pEdit)
  {
    super ((JCheckBox) pEdit.getComponenteEditor ());
    edt = pEdit;
  }
  
  public EditCampo getEditCampo ()
  {
    return edt;
  }
}
