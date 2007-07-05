/* CustomComboBox - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.Dimension;

import serpro.ppgd.gui.editors.PPGDComboBox;

class CustomComboBox extends PPGDComboBox
{
  private EditCodigo edit;
  private int folga = 5;
  
  public CustomComboBox (EditCodigo aEdit)
  {
    edit = aEdit;
  }
  
  public Dimension getPreferredSize ()
  {
    int somaWidthCols = folga;
    for (int i = 0; edit.tamCols != null && i < edit.tamCols.length; i++)
      {
	if (edit.tamCols[i] != null)
	  somaWidthCols += edit.tamCols[i].width;
      }
    Dimension d = new Dimension (somaWidthCols, super.getPreferredSize ().height);
    return d;
  }
}
