/* TagIncluirArvore - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.tags;
import serpro.ppgd.infraestrutura.PlataformaPPGD;

public class TagIncluirArvore
{
  private String arvore;
  
  public void setArvore (String arvore)
  {
    this.arvore = arvore;
    PlataformaPPGD.getPlataforma ().aplicaArvore (arvore);
  }
  
  public String getArvore ()
  {
    return arvore;
  }
}
