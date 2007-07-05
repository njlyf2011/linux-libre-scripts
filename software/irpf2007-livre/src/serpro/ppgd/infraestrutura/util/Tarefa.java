/* Tarefa - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import java.awt.Window;

import foxtrot.Job;

import serpro.ppgd.infraestrutura.PlataformaPPGD;

public abstract class Tarefa extends Job
{
  private Window parent;
  
  public Tarefa ()
  {
    setParent (PlataformaPPGD.getPlataforma ().getJanelaPrincipal ());
  }
  
  public Tarefa (Window aParent)
  {
    setParent (aParent);
  }
  
  public void setParent (Window parent)
  {
    this.parent = parent;
  }
  
  public Window getParent ()
  {
    return parent;
  }
}
