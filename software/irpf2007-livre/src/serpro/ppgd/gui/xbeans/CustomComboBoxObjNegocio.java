/* CustomComboBoxObjNegocio - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;

import serpro.ppgd.gui.editors.PPGDComboBox;

class CustomComboBoxObjNegocio extends PPGDComboBox
{
  private JEditObjetoNegocio edit;
  private int folga = 5;
  
  public CustomComboBoxObjNegocio (JEditObjetoNegocio aEdit)
  {
    edit = aEdit;
  }
  
  public Dimension getPreferredSize ()
  {
    int somaWidthCols = folga;
    for (int i = 0; edit.tamCols != null && i < edit.tamCols.length; i++)
      somaWidthCols += edit.tamCols[i].width;
    Dimension d = new Dimension (somaWidthCols, super.getPreferredSize ().height);
    return d;
  }
  
  protected void buildComponente ()
  {
    /* empty */
  }
  
  protected void readOnlyPropertyChange (boolean readOnly)
  {
    /* empty */
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    /* empty */
  }
  
  public JComponent getComponenteEditor ()
  {
    return null;
  }
  
  public JComponent getComponenteFoco ()
  {
    return null;
  }
}
