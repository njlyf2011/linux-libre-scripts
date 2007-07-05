/* PPGDTextArea - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.editors;
import javax.swing.JTextArea;
import javax.swing.text.Document;

import serpro.ppgd.negocio.Informacao;

public class PPGDTextArea extends JTextArea
{
  protected Informacao informacao = null;
  
  public PPGDTextArea ()
  {
    /* empty */
  }
  
  public PPGDTextArea (int rows, int columns)
  {
    super (rows, columns);
  }
  
  public PPGDTextArea (String text)
  {
    super (text);
  }
  
  public PPGDTextArea (String text, int rows, int columns)
  {
    super (text, rows, columns);
  }
  
  public PPGDTextArea (Document doc)
  {
    super (doc);
  }
  
  public PPGDTextArea (Document doc, String text, int rows, int columns)
  {
    super (doc, text, rows, columns);
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
