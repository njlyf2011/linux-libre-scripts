/* CalendarioListener - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.calendario;
import java.util.EventListener;

public interface CalendarioListener extends EventListener
{
  public void marcouDia (CalendarioEvent calendarioevent);
  
  public void desmarcouDia (CalendarioEvent calendarioevent);
  
  public void mudouMesAno (CalendarioEvent calendarioevent);
}
