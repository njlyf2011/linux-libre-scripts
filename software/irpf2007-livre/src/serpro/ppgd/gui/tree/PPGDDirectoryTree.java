/* PPGDDirectoryTree - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.tree;
import java.awt.Component;
import java.io.File;
import java.io.FileFilter;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class PPGDDirectoryTree extends JTree implements TreeSelectionListener
{
  protected String status = "";
  protected String rootNode = null;
  protected ImageIcon openedFolderIcon = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("imagens/pastaAberta.png"));
  protected ImageIcon closedFolderIcon = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("imagens/pastaFechada.png"));
  protected boolean showFiles = true;
  protected boolean showHiddenFiles = false;
  protected FileFilter filter = new FileFilter ()
  {
    public boolean accept (File file)
    {
      return false;
    }
  };
  
  public PPGDDirectoryTree (File pFile, boolean pShowFiles, boolean pShowHiddenFiles)
  {
    super (createPPGDTreeModel (pFile, pShowFiles, pShowHiddenFiles));
    showFiles = pShowFiles;
    setRootVisible (false);
    setToggleClickCount (1);
    addTreeSelectionListener (this);
    ((DefaultTreeCellRenderer) getCellRenderer ()).setLeafIcon (null);
    ((DefaultTreeCellRenderer) getCellRenderer ()).setIcon (closedFolderIcon);
    ((DefaultTreeCellRenderer) getCellRenderer ()).setOpenIcon (openedFolderIcon);
    ((DefaultTreeCellRenderer) getCellRenderer ()).setClosedIcon (closedFolderIcon);
    putClientProperty ("JTree.lineStyle", "None");
    setCellRenderer (new DefaultTreeCellRenderer ()
    {
      public Component getTreeCellRendererComponent (JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
      {
	PPGDFileTreeNode nodo = (PPGDFileTreeNode) value;
	if (nodo.getUserObject () == null)
	  return new JLabel ("");
	File file = (File) nodo.getUserObject ();
	do
	  {
	    if (leaf && showFiles)
	      {
		try
		  {
		    setLeafIcon (FileSystemView.getFileSystemView ().getSystemIcon (file));
		    break;
		  }
		catch (Exception exception)
		  {
		    break;
		  }
	      }
	    if (FileSystemView.getFileSystemView ().isDrive (file))
	      {
		setOpenIcon (FileSystemView.getFileSystemView ().getSystemIcon (file));
		setClosedIcon (FileSystemView.getFileSystemView ().getSystemIcon (file));
	      }
	    else
	      {
		setOpenIcon (openedFolderIcon);
		setClosedIcon (closedFolderIcon);
	      }
	  }
	while (false);
	super.getTreeCellRendererComponent (tree, value, sel, expanded, leaf, row, hasFocus);
	return this;
      }
    });
    addTreeExpansionListener (new TreeExpansionListener ()
    {
      class UpdateStatus extends Thread
      {
	public void run ()
	{
	  try
	    {
	      Thread.currentThread ();
	      Thread.sleep (450L);
	    }
	  catch (InterruptedException interruptedexception)
	    {
	      /* empty */
	    }
	  SwingUtilities.invokeLater (new Runnable ()
	  {
	    public void run ()
	    {
	      status = "";
	    }
	  });
	}
      }
      
      public void treeCollapsed (TreeExpansionEvent e)
      {
	/* empty */
      }
      
      public void treeExpanded (TreeExpansionEvent e)
      {
	try
	  {
	    TreePath path = e.getPath ();
	    PPGDFileTreeNode node = (PPGDFileTreeNode) path.getLastPathComponent ();
	    if (! node.isExplored ())
	      {
		DefaultTreeModel model = (DefaultTreeModel) PPGDDirectoryTree.this.getModel ();
		status = "exploring ...";
		node.explore (showFiles, showHiddenFiles);
		model.nodeStructureChanged (node);
	      }
	  }
	catch (Exception exception)
	  {
	    /* empty */
	  }
      }
    });
  }
  
  public static DefaultTreeModel createPPGDTreeModel (File file, boolean pShowFiles, boolean pShowHiddenFiles)
  {
    PPGDFileTreeNode rootNode = new PPGDFileTreeNode (file);
    rootNode.explore (pShowFiles, pShowHiddenFiles);
    return new DefaultTreeModel (rootNode);
  }
  
  public void valueChanged (TreeSelectionEvent e)
  {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent ();
    if (node != null)
      selected (node);
  }
  
  public void updateRoot (File pFile)
  {
    PPGDFileTreeNode rootNode = new PPGDFileTreeNode (pFile);
    rootNode.explore (showFiles, showHiddenFiles);
    ((DefaultTreeModel) getModel ()).setRoot (rootNode);
  }
  
  public void clear ()
  {
    ((DefaultMutableTreeNode) getModel ().getRoot ()).removeAllChildren ();
    ((DefaultTreeModel) getModel ()).nodeStructureChanged ((DefaultMutableTreeNode) getModel ().getRoot ());
  }
  
  protected void selected (DefaultMutableTreeNode node)
  {
    /* empty */
  }
  
  public static void main (String[] args)
  {
    PPGDDirectoryTree tree = new PPGDDirectoryTree (File.listRoots ()[1], false, false);
    JDialog dlg = new JDialog ((java.awt.Frame) null);
    dlg.getContentPane ().add (tree);
    dlg.pack ();
    dlg.setDefaultCloseOperation (3);
    dlg.setVisible (true);
  }
}
