/* EditNI - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.NI;
import serpro.ppgd.negocio.util.UtilitariosString;

public class EditNI extends EditMascara implements FocusListener
{
  private static String maskara = "**************";
  private static String maskaraCPF = "***.***.***-**";
  private static String maskaraCNPJ = "**.***.***/****-**";
  private static String caracteresValidos = "0123456789 ";
  
  public EditNI ()
  {
    this (new NI (null, "NI"));
  }
  
  public EditNI (Informacao campo)
  {
    aplicaMascaraCorrespondente (campo);
    setAssociaInformacao (campo);
    setCaracteresValidos (caracteresValidos);
    getComponenteEditor ().addFocusListener (this);
  }
  
  public void setarMascaraCPF ()
  {
    getInformacao ().setConteudo (getInformacao ().asString ().trim ());
    setMascara (maskaraCPF);
    setCaracteresValidos (caracteresValidos);
  }
  
  public void setarMascaraCNPJ ()
  {
    setMascara (maskaraCNPJ);
    setCaracteresValidos (caracteresValidos);
  }
  
  public void focusLost (FocusEvent e)
  {
    aplicaMascaraCorrespondente (getInformacao ());
  }
  
  private void aplicaMascaraCorrespondente (Informacao campo)
  {
    if (campo.asString ().length () == 11)
      setarMascaraCPF ();
    else if (campo.asString ().length () == 14)
      setarMascaraCNPJ ();
    else
      setMascara (maskara);
  }
  
  public void focusGained (FocusEvent e)
  {
    String txt = UtilitariosString.retiraMascara (componente.getText ());
    setMascara (maskara);
    setCaracteresValidos (caracteresValidos);
    componente.setText (txt);
  }
}
