/* PPGDTextField - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.editors;
import javax.swing.JTextField;
import javax.swing.text.Document;

import serpro.ppgd.negocio.Informacao;

public class PPGDTextField extends JTextField
{
  protected Informacao informacao = null;
  
  public PPGDTextField ()
  {
    /* empty */
  }
  
  public PPGDTextField (int columns)
  {
    super (columns);
  }
  
  public PPGDTextField (String text)
  {
    super (text);
  }
  
  public PPGDTextField (String text, int columns)
  {
    super (text, columns);
  }
  
  public PPGDTextField (Document doc, String text, int columns)
  {
    super (doc, text, columns);
  }
  
  public Informacao getInformacao ()
  {
    return informacao;
  }
  
  public void setInformacao (Informacao info)
  {
    informacao = info;
  }
}
