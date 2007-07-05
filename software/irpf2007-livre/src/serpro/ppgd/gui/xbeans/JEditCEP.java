/* JEditCEP - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import serpro.ppgd.negocio.CEP;
import serpro.ppgd.negocio.Informacao;

public class JEditCEP extends JEditMascara implements FocusListener
{
  private static String maskaraBR = "*****-***";
  private static String maskaraOutrosPaises = "*********";
  private static String caracteresValidos = "0123456789 ";
  private boolean isBrasileiro = true;
  
  public JEditCEP ()
  {
    super (new CEP ("CEP"), maskaraBR);
    setBrasileiro (true);
    setSobrescreve (true);
  }
  
  public JEditCEP (Informacao campo)
  {
    super (campo, maskaraBR);
    setBrasileiro (true);
  }
  
  private void setaBrasil ()
  {
    setMascara (maskaraBR);
    setCaracteresValidos (caracteresValidos);
  }
  
  private void setaOutrosPaises ()
  {
    setMascara (maskaraOutrosPaises);
    setCaracteresValidos (null);
  }
  
  public void focusLost (FocusEvent e)
  {
    /* empty */
  }
  
  public void focusGained (FocusEvent e)
  {
    /* empty */
  }
  
  public void setBrasileiro (boolean isBrasileiro)
  {
    this.isBrasileiro = isBrasileiro;
    if (isBrasileiro)
      setaBrasil ();
    else
      setaOutrosPaises ();
  }
  
  public boolean isBrasileiro ()
  {
    return isBrasileiro;
  }
}
