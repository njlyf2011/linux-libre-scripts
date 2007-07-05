/* NavegadorColecaoListener - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import java.util.EventListener;

public interface NavegadorColecaoListener extends EventListener
{
  public void exibeOutro (NavegadorColecaoEvent navegadorcolecaoevent);
  
  public void exibeColecaoVazia (NavegadorColecaoEvent navegadorcolecaoevent);
}
