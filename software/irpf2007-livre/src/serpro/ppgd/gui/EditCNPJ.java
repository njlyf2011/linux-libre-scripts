/* EditCNPJ - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import serpro.ppgd.negocio.CNPJ;
import serpro.ppgd.negocio.Informacao;

public class EditCNPJ extends EditMascara
{
  private static String maskara = "**.***.***/****-**";
  private static String caracteresValidos = "0123456789 ";
  
  public EditCNPJ ()
  {
    this (new CNPJ (null, "CNPJ:"));
  }
  
  public EditCNPJ (Informacao campo)
  {
    super (campo, maskara.length ());
    setMascara (maskara);
    setCaracteresValidos (caracteresValidos);
  }
}
