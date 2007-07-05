/* PPGDDirectoryTreeModel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.tree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class PPGDDirectoryTreeModel implements TreeModel
{
  public Object getRoot ()
  {
    return null;
  }
  
  public int getChildCount (Object parent)
  {
    return 0;
  }
  
  public boolean isLeaf (Object node)
  {
    return false;
  }
  
  public void addTreeModelListener (TreeModelListener l)
  {
    /* empty */
  }
  
  public void removeTreeModelListener (TreeModelListener l)
  {
    /* empty */
  }
  
  public Object getChild (Object parent, int index)
  {
    return null;
  }
  
  public int getIndexOfChild (Object parent, Object child)
  {
    return 0;
  }
  
  public void valueForPathChanged (TreePath path, Object newValue)
  {
    /* empty */
  }
}
