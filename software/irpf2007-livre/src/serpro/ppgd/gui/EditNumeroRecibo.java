/* EditNumeroRecibo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import serpro.ppgd.negocio.Informacao;

public class EditNumeroRecibo extends EditMascara
{
  private static String maskara = "************";
  private static String caracteresValidos = "0123456789 ";
  
  public EditNumeroRecibo ()
  {
    /* empty */
  }
  
  public EditNumeroRecibo (Informacao campo)
  {
    super (campo, maskara.length ());
    setMascara (maskara);
    setCaracteresValidos (caracteresValidos);
  }
}
