/* ListaPPGD - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import serpro.ppgd.negocio.util.LogPPGD;

public class ListaPPGD implements List
{
  private Vector implementacao = new Vector ();
  private Colecao owner;
  
  public ListaPPGD (Colecao owner)
  {
    this.owner = owner;
  }
  
  private void objetoInserido (Object o)
  {
    owner.objetoInserido (o);
    if (owner.isObservadoresListaAtivos ())
      owner.getObservadoresLista ().firePropertyChange ("ObjetoInserido", "alterou", o);
  }
  
  private void objetoARemover (Object o)
  {
    owner.objetoARemover (o);
    if (owner.isObservadoresListaAtivos ())
      owner.getObservadoresLista ().firePropertyChange ("ObjetoAremover", "alterou", o);
  }
  
  private void objetoRemovido (Object o)
  {
    owner.objetoRemovido (o);
    if (owner.isObservadoresListaAtivos ())
      owner.getObservadoresLista ().firePropertyChange ("ObjetoRemovido", "alterou", o);
  }
  
  private void verificaTipoInserido (Object o)
  {
    if (owner.getTipoItens () != null && ! owner.getTipoItens ().isInstance (o))
      {
	String msg = "Tentativa inv\u00e1lida de inser\u00e7\u00e3o do elemento " + o + " na cole\u00e7\u00e3o " + owner;
	LogPPGD.erro (msg);
	throw new IllegalArgumentException (msg);
      }
  }
  
  private void verificaTamanho (int index)
  {
    if (owner.getTamanho () != 0 && index >= owner.getTamanho ())
      {
	String msg = "A cole\u00e7\u00e3o " + owner.getClass ().getName () + "ultrapassou o tamanho limite de " + owner.getTamanho () + ", foi requerido o index " + index;
	LogPPGD.erro (msg);
	throw new IllegalArgumentException (msg);
      }
  }
  
  public void add (int index, Object element)
  {
    verificaTipoInserido (element);
    verificaTamanho (size ());
    implementacao.add (index, element);
    objetoInserido (element);
  }
  
  public boolean add (Object o)
  {
    verificaTipoInserido (o);
    verificaTamanho (size ());
    if (implementacao.add (o))
      {
	objetoInserido (o);
	return true;
      }
    return false;
  }
  
  public boolean addAll (Collection c)
  {
    Iterator i = c.iterator ();
    while (i.hasNext ())
      verificaTipoInserido (i.next ());
    if (implementacao.addAll (c))
      {
	i = c.iterator ();
	while (i.hasNext ())
	  objetoInserido (i.next ());
	return true;
      }
    return false;
  }
  
  public boolean addAll (int index, Collection c)
  {
    Iterator i = c.iterator ();
    if (c.size () + size () > owner.getTamanho ())
      throw new IllegalArgumentException ("A cole\u00e7\u00e3o ultrapassou o tamanho limite.");
    while (i.hasNext ())
      verificaTipoInserido (i.next ());
    if (implementacao.addAll (index, c))
      {
	i = c.iterator ();
	while (i.hasNext ())
	  objetoInserido (i.next ());
	return true;
      }
    return false;
  }
  
  public boolean addAll (Collection c, boolean rodaEventoInsercao)
  {
    Iterator i = c.iterator ();
    if (owner.getTamanho () > 0 && c.size () + size () > owner.getTamanho ())
      throw new IllegalArgumentException ("A cole\u00e7\u00e3o ultrapassou o tamanho limite.");
    while (i.hasNext ())
      verificaTipoInserido (i.next ());
    if (implementacao.addAll (c))
      {
	if (rodaEventoInsercao)
	  {
	    i = c.iterator ();
	    while (i.hasNext ())
	      objetoInserido (i.next ());
	  }
	return true;
      }
    return false;
  }
  
  public void clear ()
  {
    Iterator i = implementacao.iterator ();
    while (i.hasNext ())
      objetoARemover (i.next ());
    implementacao.clear ();
  }
  
  public boolean contains (Object o)
  {
    return implementacao.contains (o);
  }
  
  public boolean containsAll (Collection c)
  {
    return implementacao.containsAll (c);
  }
  
  public Object get (int index)
  {
    return implementacao.get (index);
  }
  
  public int indexOf (Object o)
  {
    return implementacao.indexOf (o);
  }
  
  public boolean isEmpty ()
  {
    return implementacao.isEmpty ();
  }
  
  public Iterator iterator ()
  {
    return new IteratorColecaoPPGD (implementacao.iterator (), owner);
  }
  
  public int lastIndexOf (Object o)
  {
    return implementacao.lastIndexOf (o);
  }
  
  public ListIterator listIterator ()
  {
    return new ListIteratorColecaoPPGD (implementacao.listIterator (), owner, this);
  }
  
  public ListIterator listIterator (int index)
  {
    return new ListIteratorColecaoPPGD (implementacao.listIterator (index), owner, this);
  }
  
  public Object remove (int index)
  {
    Object o = null;
    if (index >= 0 && index < implementacao.size ())
      {
	o = implementacao.get (index);
	objetoARemover (o);
      }
    Object result = implementacao.remove (index);
    objetoRemovido (o);
    return result;
  }
  
  public boolean remove (Object o)
  {
    boolean result = false;
    if (implementacao.contains (o))
      {
	objetoARemover (o);
	result = implementacao.remove (o);
	objetoRemovido (o);
      }
    return result;
  }
  
  public boolean removeAll (Collection c)
  {
    Iterator i = c.iterator ();
    boolean result = false;
    while (i.hasNext ())
      {
	Object o = i.next ();
	if (implementacao.contains (o))
	  objetoARemover (o);
      }
    result = implementacao.removeAll (c);
    if (result)
      {
	i = c.iterator ();
	while (i.hasNext ())
	  {
	    Object o = i.next ();
	    if (implementacao.contains (o))
	      objetoRemovido (o);
	  }
      }
    return result;
  }
  
  public boolean retainAll (Collection c)
  {
    Iterator i = implementacao.iterator ();
    while (i.hasNext ())
      {
	Object o = i.next ();
	if (! c.contains (o))
	  objetoARemover (o);
      }
    boolean result = implementacao.retainAll (c);
    i = implementacao.iterator ();
    while (i.hasNext ())
      {
	Object o = i.next ();
	if (! c.contains (o))
	  objetoRemovido (o);
      }
    return result;
  }
  
  public Object set (int index, Object element)
  {
    verificaTipoInserido (element);
    verificaTamanho (index);
    Object o = null;
    if (index >= 0 && index < implementacao.size ())
      {
	o = implementacao.get (index);
	objetoARemover (o);
      }
    Object res = implementacao.set (index, element);
    if (index >= 0 && index < implementacao.size ())
      objetoRemovido (o);
    owner.objetoInserido (element);
    return res;
  }
  
  public int size ()
  {
    return implementacao.size ();
  }
  
  public List subList (int fromIndex, int toIndex)
  {
    return implementacao.subList (fromIndex, toIndex);
  }
  
  public Object[] toArray ()
  {
    return implementacao.toArray ();
  }
  
  public Object[] toArray (Object[] a)
  {
    return implementacao.toArray (a);
  }
}
