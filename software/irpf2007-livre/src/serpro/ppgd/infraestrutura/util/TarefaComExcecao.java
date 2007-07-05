/* TarefaComExcecao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import java.awt.Window;

import foxtrot.Task;

import serpro.ppgd.infraestrutura.PlataformaPPGD;

public abstract class TarefaComExcecao extends Task
{
  private Window parent;
  
  public TarefaComExcecao ()
  {
    setParent (PlataformaPPGD.getPlataforma ().getJanelaPrincipal ());
  }
  
  public TarefaComExcecao (Window aParent)
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
