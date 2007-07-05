/* HelpUtil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import java.awt.Component;
import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Vector;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JRootPane;

import serpro.ppgd.negocio.util.LogPPGD;

public class HelpUtil
{
  private HelpSet hs;
  private HelpSet.Presentation pres;
  private MyHelpBroker hb;
  private JRootPane rootpane = null;
  private String defaultHelpID;
  private ActionListener contentListener;
  
  public HelpUtil ()
  {
    this (null, null);
  }
  
  public HelpUtil (String defaultHelpID)
  {
    this (null, defaultHelpID);
  }
  
  public HelpUtil (JRootPane rootpane)
  {
    this (rootpane, null);
  }
  
  public HelpUtil (JRootPane rootpane, String defaultHelpID)
  {
    this.rootpane = rootpane;
    setDefaultID (defaultHelpID);
  }
  
  public void setDefaultID (String defaultHelpID)
  {
    this.defaultHelpID = defaultHelpID;
  }
  
  public String getDefaultID (String defaultHelpID)
  {
    return defaultHelpID;
  }
  
  public void setHelpSet (String helpSetName)
  {
    setHelpSet (helpSetName, null);
  }
  
  public void setHelpSet (String helpSetName, String path)
  {
    ClassLoader cl = null;
    hs = null;
    String name = helpSetName;
    cl = Thread.currentThread ().getContextClassLoader ();
    URL url = HelpSet.findHelpSet (cl, name);
    if (url == null)
      LogPPGD.erro ("HelpSet not found " + url);
    else
      {
	try
	  {
	    hs = new HelpSet (cl, url);
	  }
	catch (HelpSetException ex)
	  {
	    LogPPGD.erro ("Could not create HelpSet for " + url);
	  }
	hb = new MyHelpBroker (hs);
	if (rootpane != null)
	  hb.enableHelpKey (rootpane, defaultHelpID, hs);
      }
  }
  
  public void setHelpID (Component c, String id)
  {
    if (c instanceof JButton || c instanceof JMenuItem)
      hb.enableHelpOnButton (c, id, hs);
    else
      enableHelpKey (c, id);
  }
  
  public void setHelpID (MenuItem j, String id)
  {
    hb.enableHelpOnButton (j, id, hs);
  }
  
  public void enableHelpKey (JRootPane root, String id)
  {
    hb.enableHelpKey (root, id, hs);
  }
  
  public void enableHelpKey (Component c, String id)
  {
    hb.enableHelpKey (c, id, hs);
  }
  
  public void exibeAjuda ()
  {
    hb.setDisplayed (true);
  }
  
  public HelpSet getHelpSet ()
  {
    return hs;
  }
  
  public HelpBroker getHelpBroker ()
  {
    return hb;
  }
  
  public void setNavigatorVisible (boolean flag)
  {
    hb.setViewDisplayed (flag);
  }
  
  public void setIcon (ImageIcon icon)
  {
    hb.setHelpIcon (icon);
  }
  
  private URL[] parseURLs (String spec)
  {
    Vector v = new Vector ();
    try
      {
	URL url = new URL (spec);
	v.addElement (url);
      }
    catch (Exception ex)
      {
	LogPPGD.erro ("cannot create URL for " + spec);
      }
    URL[] back = new URL[v.size ()];
    v.copyInto (back);
    return back;
  }
}
