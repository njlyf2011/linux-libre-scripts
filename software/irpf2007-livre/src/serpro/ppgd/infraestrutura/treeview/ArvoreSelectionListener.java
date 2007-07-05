/* ArvoreSelectionListener - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.treeview;
import java.awt.event.ActionEvent;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class ArvoreSelectionListener implements TreeSelectionListener
{
  private JTree tree;
  
  public ArvoreSelectionListener (JTree tree)
  {
    this.tree = tree;
  }
  
  public void valueChanged (TreeSelectionEvent e)
  {
    DefaultMutableTreeNode no = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent ();
    if (no != null)
      {
	Object obj = no.getUserObject ();
	if (obj instanceof NoGenerico)
	  {
	    NoGenerico nodo = (NoGenerico) obj;
	    if (nodo.getAction () != null && nodo.isActionHabilitado ())
	      nodo.getAction ().actionPerformed (new ActionEvent (obj, 1001, ((NoGenerico) obj).getActionCommand ()));
	  }
      }
  }
}
