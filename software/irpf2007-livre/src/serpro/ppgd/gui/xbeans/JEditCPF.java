/* JEditCPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import serpro.ppgd.negocio.CPF;
import serpro.ppgd.negocio.Informacao;

public class JEditCPF extends JEditMascara
{
  private static String maskara = "***.***.***-**";
  private static String caracteresValidos = "0123456789 ";
  private static final Class beanClass;
  
  static
  {
    Class var_class = serpro.ppgd.gui.xbeans.JEditCPF.class;
    beanClass = var_class;
  }
  
  public JEditCPF ()
  {
    super (new CPF (), maskara);
    setCaracteresValidos (caracteresValidos);
  }
  
  public JEditCPF (Informacao campo)
  {
    super (campo, maskara);
    setMascara (maskara);
    setCaracteresValidos (caracteresValidos);
  }
}
