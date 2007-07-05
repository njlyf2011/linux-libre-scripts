/* NavegadorRemocaoListener - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import java.util.EventListener;

public interface NavegadorRemocaoListener extends EventListener
{
  public boolean confirmaExclusao (NavegadorRemocaoEvent navegadorremocaoevent);
  
  public void objetoExcluido (NavegadorRemocaoEvent navegadorremocaoevent);
}
