/* JButtonMensagem - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import serpro.ppgd.infraestrutura.PlataformaPPGD;

public class JButtonMensagem extends JButton
{
  public JButtonMensagem ()
  {
    setOpaque (false);
    setVisible (false);
    setContentAreaFilled (false);
    setFocusable (false);
    setSize (20, 20);
    setMaximumSize (getSize ());
    setBorder (null);
    setText ("");
    if (PlataformaPPGD.isEmDesign ())
      {
	setIcon (new ImageIcon (this.getClass ().getResource ("/imagens/ico_btn_msg_design.png")));
	setVisible (true);
      }
  }
  
  public String getText ()
  {
    return "";
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
