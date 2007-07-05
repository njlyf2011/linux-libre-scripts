/* JEditNirf - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.NIRF;

public class JEditNirf extends JEditMascara
{
  private static String maskara = "*.***.***-*";
  private static String caracteresValidos = "0123456789 ";
  private static final Class beanClass;
  
  static
  {
    Class var_class = serpro.ppgd.gui.xbeans.JEditNirf.class;
    beanClass = var_class;
  }
  
  public JEditNirf ()
  {
    super (new NIRF (), maskara);
    setCaracteresValidos (caracteresValidos);
  }
  
  public JEditNirf (Informacao campo)
  {
    super (campo, maskara);
    setMascara (maskara);
    setCaracteresValidos (caracteresValidos);
  }
}
