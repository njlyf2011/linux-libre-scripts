/* ActionTransfereFoco - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

class ActionTransfereFoco extends AbstractAction
{
  public void actionPerformed (ActionEvent e)
  {
    KeyboardFocusManager.getCurrentKeyboardFocusManager ().focusNextComponent ();
  }
}
