/* PPGDListModel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.list;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractListModel;

import serpro.ppgd.negocio.Colecao;

public class PPGDListModel extends AbstractListModel implements PropertyChangeListener
{
  protected Colecao colecao = null;
  
  public PPGDListModel (Colecao pColecao)
  {
    colecao = pColecao;
    colecao.getObservadoresLista ().addPropertyChangeListener (this);
  }
  
  public int getSize ()
  {
    return colecao.recuperarLista ().size ();
  }
  
  public Object getElementAt (int index)
  {
    return colecao.recuperarLista ().get (index);
  }
  
  public void atualiza ()
  {
    fireContentsChanged (this, 0, colecao.recuperarLista ().size ());
  }
  
  public void propertyChange (PropertyChangeEvent evt)
  {
    atualiza ();
  }
}
