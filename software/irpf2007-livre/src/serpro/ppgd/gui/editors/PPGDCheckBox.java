/* PPGDCheckBox - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.editors;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;

import serpro.ppgd.negocio.Informacao;

public class PPGDCheckBox extends JCheckBox
{
  protected Informacao informacao = null;
  
  public PPGDCheckBox ()
  {
    /* empty */
  }
  
  public PPGDCheckBox (String text)
  {
    super (text);
  }
  
  public PPGDCheckBox (String text, boolean selected)
  {
    super (text, selected);
  }
  
  public PPGDCheckBox (String codigo, String descricao, boolean selecionado)
  {
    setName (codigo);
    setText (descricao);
    setSelected (selecionado);
  }
  
  public PPGDCheckBox (Action a)
  {
    super (a);
  }
  
  public PPGDCheckBox (Icon icon)
  {
    super (icon);
  }
  
  public PPGDCheckBox (Icon icon, boolean selected)
  {
    super (icon, selected);
  }
  
  public PPGDCheckBox (String text, Icon icon)
  {
    super (text, icon);
  }
  
  public PPGDCheckBox (String text, Icon icon, boolean selected)
  {
    super (text, icon, selected);
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
