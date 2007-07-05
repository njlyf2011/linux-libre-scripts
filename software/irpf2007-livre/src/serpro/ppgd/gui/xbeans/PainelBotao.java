/* PainelBotao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JComponent;

public class PainelBotao extends JComponent
{
  public PainelBotao (JButtonMensagem btnMsg)
  {
    setLayout (new FlowLayout (0, 0, 0));
    add (btnMsg);
  }
  
  public Dimension getMinimumSize ()
  {
    return new Dimension (20, 20);
  }
  
  public Dimension getMaximumSize ()
  {
    return new Dimension (20, 20);
  }
  
  public Dimension getPreferredSize ()
  {
    return new Dimension (20, 20);
  }
}
