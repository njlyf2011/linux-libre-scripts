/* JEditData - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import javax.swing.JFormattedTextField;

import serpro.ppgd.negocio.Data;
import serpro.ppgd.negocio.Informacao;

public class JEditData extends JEditMascara
{
  private static String maskara = "**/**/****";
  private static String caracteresValidos = "0123456789 ";
  
  public JEditData ()
  {
    super (new Data (), maskara);
    setSobrescreve (true);
    setCaracteresValidos (caracteresValidos);
  }
  
  public JEditData (Informacao campo)
  {
    super (campo, maskara);
    setSobrescreve (true);
    setCaracteresValidos (caracteresValidos);
  }
  
  public void setarCampo ()
  {
    String valor = ((JFormattedTextField) getComponenteEditor ()).getText ();
    int tamMax = getMascara ().length ();
    if (valor.length () > tamMax)
      valor = valor.substring (0, tamMax);
    getInformacao ().setConteudo (valor);
  }
}
