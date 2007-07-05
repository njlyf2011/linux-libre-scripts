/* PPGDComboBox - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.editors;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import serpro.ppgd.negocio.Informacao;

public class PPGDComboBox extends JComboBox
{
  protected Informacao informacao = null;
  
  public PPGDComboBox ()
  {
    /* empty */
  }
  
  public PPGDComboBox (Object[] items)
  {
    super (items);
  }
  
  public PPGDComboBox (Vector items)
  {
    super (items);
  }
  
  public PPGDComboBox (ComboBoxModel aModel)
  {
    super (aModel);
  }
  
  public Informacao getInformacao ()
  {
    return informacao;
  }
  
  public void setInformacao (Informacao informacao)
  {
    this.informacao = informacao;
  }
}
