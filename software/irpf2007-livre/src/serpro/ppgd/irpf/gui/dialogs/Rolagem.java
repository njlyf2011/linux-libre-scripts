/* Rolagem - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import javax.swing.JScrollPane;

class Rolagem extends Thread
{
  private JScrollPane painel;
  
  public void run ()
  {
    for (int x = 0; x < painel.getVerticalScrollBar ().getMaximum (); x++)
      {
	try
	  {
	    Thread.sleep (30L);
	  }
	catch (InterruptedException interruptedexception)
	  {
	    /* empty */
	  }
	if (x == 0 || painel.getVerticalScrollBar ().getValue () == x - 1)
	  painel.getVerticalScrollBar ().setValue (x);
	else
	  x = painel.getVerticalScrollBar ().getValue ();
      }
  }
  
  public Rolagem (JScrollPane pPainel)
  {
    painel = pPainel;
  }
}
