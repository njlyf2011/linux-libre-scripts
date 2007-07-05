/* PPGDSpinner - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.editors;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

import serpro.ppgd.negocio.Informacao;

public class PPGDSpinner extends JSpinner
{
  protected Informacao informacao = null;
  
  public PPGDSpinner ()
  {
    /* empty */
  }
  
  public PPGDSpinner (SpinnerModel model)
  {
    super (model);
  }
  
  public Informacao getInformacao ()
  {
    return informacao;
  }
  
  public void setInformacao (Informacao informacao)
  {
    this.informacao = informacao;
  }
}
