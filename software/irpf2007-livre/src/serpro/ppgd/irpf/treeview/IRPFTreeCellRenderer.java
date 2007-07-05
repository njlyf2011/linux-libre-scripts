/* IRPFTreeCellRenderer - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.treeview;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import serpro.ppgd.gui.ConstantesGlobaisGUI;
import serpro.ppgd.infraestrutura.treeview.NoGenerico;
import serpro.ppgd.negocio.util.UtilitariosString;

public class IRPFTreeCellRenderer extends DefaultTreeCellRenderer
{
  protected final ImageIcon ICO_INDICADOR = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("imagens/ico_indicador.gif"));
  protected JTree tree;
  protected Object ultimoNodo;
  
  public IRPFTreeCellRenderer (JTree ptree)
  {
    tree = ptree;
    setLeafIcon (null);
    setFocusable (false);
    setBackgroundSelectionColor (getBackgroundNonSelectionColor ());
    tree.addMouseMotionListener (new MouseMotionAdapter ()
    {
      public void mouseMoved (MouseEvent me)
      {
	TreePath treePath = tree.getPathForLocation (me.getX (), me.getY ());
	Object obj;
	if (treePath != null)
	  obj = treePath.getLastPathComponent ();
	else
	  obj = null;
	if (obj != ultimoNodo)
	  {
	    ultimoNodo = obj;
	    tree.repaint ();
	  }
      }
    });
  }
  
  public Component getTreeCellRendererComponent (JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
  {
    tree.setRowHeight (0);
    setTextSelectionColor (getTextNonSelectionColor ());
    NoGenerico nodo = (NoGenerico) value;
    if (nodo.isRoot ())
      {
	NoGenerico no = (NoGenerico) value;
	setOpenIcon (no.getIconeExpandido ());
	setClosedIcon (no.getIconeContraido ());
	setVerticalTextPosition (1);
      }
    else if (nodo != null)
      {
	setOpenIcon (nodo.getIconeExpandido ());
	setClosedIcon (nodo.getIconeContraido ());
	setLeafIcon (nodo.getIconeContraido ());
	setToolTipText (nodo.getTooltip ());
      }
    super.getTreeCellRendererComponent (tree, value, sel, expanded, leaf, row, hasFocus);
    if (! nodo.isRoot ())
      {
	if (nodo.getChildCount () == 0)
	  setBorder (BorderFactory.createEmptyBorder (3, 0, 3, 0));
	else
	  setBorder (null);
	if (! ((NoGenerico) value).isActionHabilitado ())
	  {
	    setForeground (ConstantesGlobaisGUI.COR_CINZA_CLARO);
	    setEnabled (false);
	  }
	else
	  {
	    setForeground (ConstantesGlobaisGUI.COR_PRETO);
	    setEnabled (true);
	  }
	if (value == ultimoNodo && nodo.isActionHabilitado ())
	  setText ("<HTML><U>" + getText () + "</U></HTML>");
      }
    setOpenIcon (null);
    setClosedIcon (null);
    if (sel && nodo.isActionHabilitado () && nodo.getAction () != null)
      {
	setText (UtilitariosString.expandeStringHTML (getText () + '^', "<FONT COLOR=#336699><u>", 0));
	setText ("<HTML><U>" + getText () + "</U></HTML>");
      }
    return this;
  }
}
