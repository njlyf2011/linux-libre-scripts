/* PPGDFormattedTextField - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.editors;
import java.text.Format;

import javax.swing.JFormattedTextField;

import serpro.ppgd.negocio.Informacao;

public class PPGDFormattedTextField extends JFormattedTextField
{
  protected Informacao informacao = null;
  
  public PPGDFormattedTextField ()
  {
    /* empty */
  }
  
  public PPGDFormattedTextField (Object value)
  {
    super (value);
  }
  
  public PPGDFormattedTextField (Format format)
  {
    super (format);
  }
  
  public PPGDFormattedTextField (JFormattedTextField.AbstractFormatter formatter)
  {
    super (formatter);
  }
  
  public PPGDFormattedTextField (JFormattedTextField.AbstractFormatterFactory factory)
  {
    super (factory);
  }
  
  public PPGDFormattedTextField (JFormattedTextField.AbstractFormatterFactory factory, Object currentValue)
  {
    super (factory, currentValue);
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
