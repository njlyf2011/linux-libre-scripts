/* MyHelpBroker - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import java.awt.Button;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.help.CSH;
import javax.help.DefaultHelpBroker;
import javax.help.HelpSet;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;

class MyHelpBroker extends DefaultHelpBroker
{
  public MyHelpBroker (HelpSet hs)
  {
    super (hs);
  }
  
  public MyHelpBroker (HelpSet hs, ImageIcon icon)
  {
    super (hs);
    setHelpIcon (icon);
  }
  
  public void setHelpIcon (ImageIcon icon)
  {
    /* empty */
  }
  
  public void enableHelpOnButton (Component comp, String id, HelpSet hs)
  {
    if (! (comp instanceof AbstractButton) && ! (comp instanceof Button))
      throw new IllegalArgumentException ("Invalid Component");
    if (id == null)
      throw new NullPointerException ("id");
    CSH.setHelpIDString (comp, id);
    if (hs != null)
      CSH.setHelpSet (comp, hs);
    if (comp instanceof AbstractButton)
      {
	AbstractButton button = (AbstractButton) comp;
	button.addActionListener (new ActionListener ()
	{
	  public void actionPerformed (final ActionEvent e)
	  {
	    ProcessoSwing.executa (new Tarefa ()
	    {
	      public Object run ()
	      {
		access$100 (MyHelpBroker.this).actionPerformed (e);
		return null;
	      }
	    });
	  }
	});
      }
    else if (comp instanceof Button)
      {
	Button button = (Button) comp;
	button.addActionListener (new ActionListener ()
	{
	  public void actionPerformed (final ActionEvent e)
	  {
	    ProcessoSwing.executa (new Tarefa ()
	    {
	      public Object run ()
	      {
		access$300 (MyHelpBroker.this).actionPerformed (e);
		return null;
	      }
	    });
	  }
	});
      }
  }
  
  /*synthetic*/ static ActionListener access$100 (MyHelpBroker x0)
  {
    return x0.getDisplayHelpFromSource ();
  }
  
  /*synthetic*/ static ActionListener access$300 (MyHelpBroker x0)
  {
    return x0.getDisplayHelpFromSource ();
  }
}
