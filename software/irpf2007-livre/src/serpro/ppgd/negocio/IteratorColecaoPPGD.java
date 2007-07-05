/* IteratorColecaoPPGD - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import java.util.Iterator;

class IteratorColecaoPPGD implements Iterator
{
  private Iterator implementacao;
  private Colecao owner;
  private Object objetoCorrente;
  
  public IteratorColecaoPPGD (Iterator implementacao, Colecao owner)
  {
    this.implementacao = implementacao;
    this.owner = owner;
  }
  
  public boolean hasNext ()
  {
    return implementacao.hasNext ();
  }
  
  public Object next ()
  {
    objetoCorrente = implementacao.next ();
    return objetoCorrente;
  }
  
  public void remove ()
  {
    owner.objetoARemover (objetoCorrente);
    implementacao.remove ();
    owner.objetoRemovido (objetoCorrente);
  }
}
