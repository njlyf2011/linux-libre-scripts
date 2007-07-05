/* PPGDRadioButton - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.editors;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JRadioButton;

import serpro.ppgd.negocio.Informacao;

public class PPGDRadioButton extends JRadioButton
{
  protected Informacao informacao = null;
  
  public Informacao getInformacao ()
  {
    return informacao;
  }
  
  public void setInformacao (Informacao info)
  {
    informacao = info;
  }
  
  public PPGDRadioButton ()
  {
    /* empty */
  }
  
  public PPGDRadioButton (String text)
  {
    super (text);
  }
  
  public PPGDRadioButton (String text, boolean selected)
  {
    super (text, selected);
  }
  
  public PPGDRadioButton (Action a)
  {
    super (a);
  }
  
  public PPGDRadioButton (Icon icon)
  {
    super (icon);
  }
  
  public PPGDRadioButton (Icon icon, boolean selected)
  {
    super (icon, selected);
  }
  
  public PPGDRadioButton (String text, Icon icon)
  {
    super (text, icon);
  }
  
  public PPGDRadioButton (String text, Icon icon, boolean selected)
  {
    super (text, icon, selected);
  }
}
