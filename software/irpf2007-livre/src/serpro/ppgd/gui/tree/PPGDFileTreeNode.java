/* PPGDFileTreeNode - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.tree;
import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

public class PPGDFileTreeNode extends DefaultMutableTreeNode
{
  private boolean explored = false;
  
  public PPGDFileTreeNode (File file)
  {
    if (file != null)
      setUserObject (file);
  }
  
  public boolean getAllowsChildren ()
  {
    if (getUserObject () == null)
      return true;
    return isDirectory ();
  }
  
  public boolean isLeaf ()
  {
    if (getUserObject () == null)
      return false;
    return ! isDirectory ();
  }
  
  public File getFile ()
  {
    return (File) getUserObject ();
  }
  
  public boolean isExplored ()
  {
    if (getUserObject () == null)
      return true;
    return explored;
  }
  
  public boolean isDirectory ()
  {
    if (getUserObject () == null)
      return true;
    File file = getFile ();
    return file.isDirectory ();
  }
  
  public String toString ()
  {
    if (getUserObject () == null)
      return "Raiz";
    File file = (File) getUserObject ();
    String filename = file.toString ();
    int index = filename.lastIndexOf (File.separator);
    return index != -1 && index != filename.length () - 1 ? filename.substring (index + 1) : filename;
  }
  
  public void explore (boolean showFiles, boolean showHiddenFiles)
  {
    if (getUserObject () == null)
      {
	explored = true;
	for (int i = 0; i < File.listRoots ().length; i++)
	  add (new PPGDFileTreeNode (File.listRoots ()[i]));
      }
    else if (isDirectory ())
      {
	if (! isExplored ())
	  {
	    File file = getFile ();
	    File[] children = file.listFiles ();
	    for (int i = 0; i < children.length; i++)
	      {
		if ((showFiles || ! showFiles && children[i].isDirectory ()) && (showHiddenFiles || ! children[i].isHidden ()))
		  add (new PPGDFileTreeNode (children[i]));
	      }
	    explored = true;
	  }
      }
  }
  
  public void setExplored (boolean explored)
  {
    this.explored = explored;
  }
}
