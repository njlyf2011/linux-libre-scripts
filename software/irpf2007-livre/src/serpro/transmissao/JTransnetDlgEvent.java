/* JTransnetDlgEvent - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.transmissao;
import java.awt.AWTEvent;
import java.awt.EventQueue;

public class JTransnetDlgEvent extends EventQueue
{
  private JTransnetDlg dlg;
  
  public JTransnetDlgEvent (JTransnetDlg jtransnetdlg)
  {
    dlg = jtransnetdlg;
  }
  
  protected void dispatchEvent (AWTEvent awtevent)
  {
    dlg.dispatchEvent (awtevent);
  }
}
