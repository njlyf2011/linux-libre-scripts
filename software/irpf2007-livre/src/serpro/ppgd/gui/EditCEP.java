/* EditCEP - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import serpro.ppgd.negocio.CEP;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.util.UtilitariosString;

public class EditCEP extends EditMascara implements FocusListener
{
  private static String maskaraBR = "*****-***";
  private static String maskaraOutrosPaises = "*********";
  private static String caracteresValidos = "0123456789 ";
  private boolean isBrasil = true;
  
  public EditCEP ()
  {
    this (new CEP (""));
    setaBrasil ();
    setSobrescreve (true);
  }
  
  public EditCEP (Informacao campo)
  {
    super (campo, maskaraBR.length ());
    setaBrasil ();
    getComponenteEditor ().addFocusListener (this);
  }
  
  public void setaBrasil ()
  {
    isBrasil = true;
    setMascara (maskaraBR);
    setCaracteresValidos (caracteresValidos);
  }
  
  public void setaOutrosPaises ()
  {
    isBrasil = false;
    setMascara (maskaraOutrosPaises);
    setCaracteresValidos (null);
  }
  
  public void focusLost (FocusEvent e)
  {
    String txt = UtilitariosString.retiraMascara (getInformacao ().getConteudoFormatado ());
    getInformacao ().setConteudo (txt);
  }
  
  public void focusGained (FocusEvent e)
  {
    /* empty */
  }
}
