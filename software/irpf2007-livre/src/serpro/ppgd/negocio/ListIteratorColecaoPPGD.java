/* ListIteratorColecaoPPGD - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import java.util.List;
import java.util.ListIterator;

class ListIteratorColecaoPPGD implements ListIterator
{
  private ListIterator implementacao;
  private Colecao owner;
  private List lista;
  private Object objetoCorrente;
  
  private void verificaTipoInserido (Object o)
  {
    if (owner.getTipoItens () != null && ! owner.getTipoItens ().isInstance (o))
      throw new IllegalArgumentException ("Tentativa inv\u00e1lida de inser\u00e7\u00e3o do elemento " + o + " na cole\u00e7\u00e3o " + owner);
  }
  
  private void verificaTamanho (int index)
  {
    if (owner.getTamanho () != 0 && index == owner.getTamanho ())
      throw new IllegalArgumentException ("A cole\u00e7\u00e3o ultrapassou o tamanho limite.");
  }
  
  public ListIteratorColecaoPPGD (ListIterator implementacao, Colecao owner, List lista)
  {
    this.implementacao = implementacao;
    this.owner = owner;
    this.lista = lista;
  }
  
  public void add (Object o)
  {
    verificaTipoInserido (o);
    verificaTamanho (lista.size ());
    implementacao.add (o);
    owner.objetoInserido (o);
  }
  
  public boolean hasNext ()
  {
    return implementacao.hasNext ();
  }
  
  public boolean hasPrevious ()
  {
    return implementacao.hasPrevious ();
  }
  
  public Object next ()
  {
    objetoCorrente = implementacao.next ();
    return objetoCorrente;
  }
  
  public int nextIndex ()
  {
    return implementacao.nextIndex ();
  }
  
  public Object previous ()
  {
    objetoCorrente = implementacao.previous ();
    return objetoCorrente;
  }
  
  public int previousIndex ()
  {
    return implementacao.previousIndex ();
  }
  
  public void remove ()
  {
    owner.objetoARemover (objetoCorrente);
    implementacao.remove ();
    owner.objetoRemovido (objetoCorrente);
  }
  
  public void set (Object o)
  {
    verificaTipoInserido (o);
    owner.objetoARemover (objetoCorrente);
    implementacao.set (o);
    owner.objetoRemovido (objetoCorrente);
    owner.objetoInserido (o);
  }
}
