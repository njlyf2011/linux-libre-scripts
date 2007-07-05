/* ExecutaHiperlinkDicas - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class ExecutaHiperlinkDicas implements HyperlinkListener
{
  public void hyperlinkUpdate (HyperlinkEvent ev)
  {
    if (ev.getEventType () == HyperlinkEvent.EventType.ACTIVATED)
      {
	String string = ev.getDescription ();
      }
  }
}
