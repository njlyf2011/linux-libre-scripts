/* ElementoTabela - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import java.util.List;
import java.util.Vector;

public class ElementoTabela
{
  private List lstElementoTabela = new Vector ();
  
  public void setConteudo (int nrColuna, String conteudo)
  {
    if (nrColuna > lstElementoTabela.size () - 1)
      {
	for (int i = lstElementoTabela.size () - 1; i < nrColuna; i++)
	  lstElementoTabela.add (null);
      }
    lstElementoTabela.set (nrColuna, conteudo);
  }
  
  public String getConteudo (int nrColuna)
  {
    if (nrColuna < lstElementoTabela.size ())
      return (String) lstElementoTabela.get (nrColuna);
    return null;
  }
  
  public int size ()
  {
    return lstElementoTabela.size ();
  }
  
  public String toString ()
  {
    return getConteudo (0);
  }
}
