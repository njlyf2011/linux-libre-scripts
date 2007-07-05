/* EditData - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import serpro.ppgd.negocio.Data;
import serpro.ppgd.negocio.Informacao;

public class EditData extends EditMascara
{
  private static String maskara = "**/**/****";
  private static String caracteresValidos = "0123456789 ";
  
  public EditData ()
  {
    this (new Data (null, ""));
  }
  
  public EditData (Informacao campo)
  {
    super (campo, maskara.length ());
    setMascara (maskara);
    setSobrescreve (true);
    setCaracteresValidos (caracteresValidos);
  }
  
  public void setarCampo ()
  {
    String s = componente.getText ();
    if (s.length () > d[0].width)
      s = s.substring (0, d[0].width);
    getInformacao ().setConteudo (s);
  }
}
