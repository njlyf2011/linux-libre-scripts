/* JEditCNPJ - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import serpro.ppgd.negocio.CNPJ;
import serpro.ppgd.negocio.Informacao;

public class JEditCNPJ extends JEditMascara
{
  private static String maskara = "**.***.***/****-**";
  private static String caracteresValidos = "0123456789 ";
  
  public JEditCNPJ ()
  {
    super (new CNPJ (null, "CNPJ"), maskara);
    setCaracteresValidos (caracteresValidos);
  }
  
  public JEditCNPJ (Informacao campo)
  {
    super (campo, maskara);
    setCaracteresValidos (caracteresValidos);
  }
}
