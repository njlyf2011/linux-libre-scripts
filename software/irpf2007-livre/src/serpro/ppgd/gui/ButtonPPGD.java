/* ButtonPPGD - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class ButtonPPGD extends JButton implements MouseListener
{
  private String label;
  private Icon iconDefault;
  private Icon iconSelecionado;
  private Icon iconPressionado;
  private Icon iconDesabilitado;
  private Icon iconToggled;
  private boolean sublinhar;
  private boolean toggled = false;
  private boolean estiloLink = false;
  
  public ButtonPPGD (String label)
  {
    this (label, null, null, null, null, null);
  }
  
  public ButtonPPGD (String label, Icon iconDefault)
  {
    this (label, iconDefault, null, null, null, null);
  }
  
  public ButtonPPGD (String label, Icon iconDefault, Icon iconSelecionado, Icon iconPressionado)
  {
    this (label, iconDefault, iconSelecionado, iconPressionado, null, null);
  }
  
  public ButtonPPGD (String label, Icon iconDefault, Icon iconSelecionado, Icon iconPressionado, Icon iconDesabilitado)
  {
    this (label, iconDefault, iconSelecionado, iconPressionado, iconDesabilitado, null);
  }
  
  public ButtonPPGD (String label, Icon iconDefault, Icon iconSelecionado, Icon iconPressionado, Icon iconDesabilitado, Icon iconToggled)
  {
    this.iconDefault = iconDefault;
    this.iconSelecionado = iconSelecionado;
    this.iconPressionado = iconPressionado;
    this.iconToggled = iconToggled;
    setDisabledIcon (iconDesabilitado);
    setDisabledSelectedIcon (iconDesabilitado);
    setSublinhar (false);
    setContentAreaFilled (true);
    setAlignmentX (0.5F);
    setHorizontalAlignment (0);
    setIcon (iconDefault);
    if (iconSelecionado != null && iconToggled == null)
      setRolloverIcon (iconSelecionado);
    if (iconPressionado != null)
      setPressedIcon (iconPressionado);
    setText (label);
    addMouseListener (this);
  }
  
  public void setSublinhar (boolean flag)
  {
    sublinhar = flag;
  }
  
  public void setToggled (boolean flag)
  {
    toggled = flag;
    if (toggled)
      setIcon (iconToggled);
    else
      setIcon (iconDefault);
    repaint ();
  }
  
  public boolean getToggled ()
  {
    return toggled;
  }
  
  public void setText (String text)
  {
    label = text;
    super.setText ("<html>" + label + "</html>");
  }
  
  public void setaDimensoes ()
  {
    FontMetrics fm = getFontMetrics (getFont ());
    UtilitariosGUI.setParametrosGUI (this, 10 + SwingUtilities.computeStringWidth (fm, label) + iconDefault.getIconWidth () + 10, iconDefault.getIconHeight () + 10);
  }
  
  public void mouseEntered (MouseEvent e)
  {
    if (sublinhar && isEnabled ())
      {
	FabricaGUI.mudaCursorNoComponente (this, 12);
	super.setText ("<html><u>" + label + "</u></html>");
      }
    if (sublinhar)
      repaint ();
  }
  
  public void mouseExited (MouseEvent e)
  {
    if (sublinhar)
      {
	FabricaGUI.mudaCursorNoComponente (this, 0);
	super.setText ("<html>" + label + "</html>");
      }
    if (sublinhar)
      repaint ();
  }
  
  public void mouseClicked (MouseEvent e)
  {
    /* empty */
  }
  
  public void mousePressed (MouseEvent e)
  {
    /* empty */
  }
  
  public void mouseReleased (MouseEvent e)
  {
    /* empty */
  }
  
  public void setEnabled (boolean enabled)
  {
    if (enabled)
      setForeground (ConstantesGlobaisGUI.COR_PRETO);
    else
      setForeground (ConstantesGlobaisGUI.COR_CINZA_CLARO);
    super.setEnabled (enabled);
  }
  
  public void setEstiloLink (boolean estiloLink)
  {
    this.estiloLink = estiloLink;
    if (estiloLink)
      {
	setSublinhar (true);
	setContentAreaFilled (false);
	setBorderPainted (false);
      }
    else
      {
	setSublinhar (false);
	setContentAreaFilled (true);
	setBorderPainted (true);
      }
  }
  
  public boolean isEstiloLink ()
  {
    return estiloLink;
  }
}
