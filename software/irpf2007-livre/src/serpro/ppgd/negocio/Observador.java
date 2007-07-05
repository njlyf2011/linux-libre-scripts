/* Observador - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class Observador implements PropertyChangeListener
{
  private boolean ativo = true;
  
  public void propertyChange (PropertyChangeEvent evt)
  {
    if (isAtivo ())
      notifica (evt.getSource (), evt.getPropertyName (), evt.getOldValue (), evt.getNewValue ());
  }
  
  public abstract void notifica (Object object, String string, Object object_0_, Object object_1_);
  
  public synchronized boolean isAtivo ()
  {
    return ativo;
  }
  
  public synchronized void setAtivo (boolean ativo)
  {
    this.ativo = ativo;
  }
}
