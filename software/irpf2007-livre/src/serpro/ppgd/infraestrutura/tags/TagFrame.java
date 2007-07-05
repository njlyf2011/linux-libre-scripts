/* TagFrame - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.tags;
import javax.swing.JFrame;

import serpro.ppgd.infraestrutura.PlataformaPPGD;

public class TagFrame extends JFrame
{
  public void setArvore (String arvore)
  {
    PlataformaPPGD.getPlataforma ().aplicaArvore (arvore);
  }
  
  public void setMenubar (String menubar)
  {
    PlataformaPPGD.getPlataforma ().aplicaMenuBar (menubar);
  }
}
