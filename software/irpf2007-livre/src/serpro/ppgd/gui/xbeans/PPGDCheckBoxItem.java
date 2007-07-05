/* PPGDCheckBoxItem - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.Font;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;

public class PPGDCheckBoxItem extends JCheckBox implements BotaoItemIf
{
  private String valorSelecionadoTrue = "1";
  protected int incrementoTamanhoFonte = 0;
  protected float tamanhoOriginal = -1.0F;
  
  public PPGDCheckBoxItem ()
  {
    /* empty */
  }
  
  public PPGDCheckBoxItem (String text)
  {
    super (text);
  }
  
  public PPGDCheckBoxItem (String text, boolean selected)
  {
    super (text, selected);
  }
  
  public PPGDCheckBoxItem (Action a)
  {
    super (a);
  }
  
  public PPGDCheckBoxItem (Icon icon)
  {
    super (icon);
  }
  
  public PPGDCheckBoxItem (Icon icon, boolean selected)
  {
    super (icon, selected);
  }
  
  public PPGDCheckBoxItem (String text, Icon icon)
  {
    super (text, icon);
  }
  
  public PPGDCheckBoxItem (String text, Icon icon, boolean selected)
  {
    super (text, icon, selected);
  }
  
  public String getValorSelecionadoTrue ()
  {
    return valorSelecionadoTrue;
  }
  
  public void setValorSelecionadoTrue (String valorSelecionadoTrue)
  {
    this.valorSelecionadoTrue = valorSelecionadoTrue;
  }
  
  public void setEstiloFonte (int estilo)
  {
    Font f = getFont ();
    f = f.deriveFont (estilo);
    setFont (f);
  }
  
  public int getEstiloFonte ()
  {
    Font f = getFont ();
    return f.getStyle ();
  }
  
  public void setIncrementoTamanhoFonte (int incremento)
  {
    incrementoTamanhoFonte = incremento;
    Font f = getFont ();
    if (tamanhoOriginal == -1.0F)
      tamanhoOriginal = f.getSize2D ();
    f = f.deriveFont (tamanhoOriginal + (float) incremento);
    setFont (f);
  }
  
  public int getIncrementoTamanhoFonte ()
  {
    return incrementoTamanhoFonte;
  }
}
