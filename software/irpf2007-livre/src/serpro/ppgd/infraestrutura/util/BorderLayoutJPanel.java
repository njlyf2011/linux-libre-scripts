/* BorderLayoutJPanel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

public class BorderLayoutJPanel extends JPanel
{
  public BorderLayoutJPanel ()
  {
    super (new BorderLayout ());
  }
  
  public Component add (Component c)
  {
    super.add (c, "Center");
    return c;
  }
}
