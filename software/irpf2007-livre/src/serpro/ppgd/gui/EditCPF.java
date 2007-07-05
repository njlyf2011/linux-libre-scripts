/* EditCPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import serpro.ppgd.negocio.CPF;
import serpro.ppgd.negocio.Informacao;

public class EditCPF extends EditMascara
{
  private static String maskara = "***.***.***-**";
  private static String caracteresValidos = "0123456789 ";
  
  public EditCPF ()
  {
    this (new CPF (null, "CPF:"));
  }
  
  public EditCPF (Informacao campo)
  {
    super (campo, maskara.length ());
    setMascara (maskara);
    setCaracteresValidos (caracteresValidos);
  }
}
