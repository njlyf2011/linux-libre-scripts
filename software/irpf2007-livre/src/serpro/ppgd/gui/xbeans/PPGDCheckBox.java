/* PPGDCheckBox - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import javax.swing.JCheckBox;

public class PPGDCheckBox extends JCheckBox
{
  private String valor;
  private PPGDButtonGroup ppgdButtonGroup;
  
  public void setValor (String valor)
  {
    this.valor = valor;
    if (ppgdButtonGroup != null)
      ppgdButtonGroup.atualizaInterface (this);
  }
  
  public String getValor ()
  {
    return valor;
  }
  
  protected void setPPGDButtonGroup (PPGDButtonGroup buttonGroup)
  {
    ppgdButtonGroup = buttonGroup;
  }
  
  protected PPGDButtonGroup getPPGDButtonGroup ()
  {
    return ppgdButtonGroup;
  }
}
