/* JComponentCellEditor - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.table;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;

public class JComponentCellEditor implements TableCellEditor, TreeCellEditor, Serializable
{
  protected EventListenerList listenerList = new EventListenerList ();
  protected transient ChangeEvent changeEvent = null;
  protected JComponent editorComponent = null;
  protected JComponent container = null;
  
  public Component getComponent ()
  {
    return editorComponent;
  }
  
  public Object getCellEditorValue ()
  {
    return editorComponent;
  }
  
  public boolean isCellEditable (EventObject anEvent)
  {
    return true;
  }
  
  public boolean shouldSelectCell (EventObject anEvent)
  {
    if (editorComponent != null && anEvent instanceof MouseEvent && ((MouseEvent) anEvent).getID () == 501)
      {
	Component dispatchComponent = SwingUtilities.getDeepestComponentAt (editorComponent, 3, 3);
	MouseEvent e = (MouseEvent) anEvent;
	MouseEvent e2 = new MouseEvent (dispatchComponent, 502, e.getWhen () + 100000L, e.getModifiers (), 3, 3, e.getClickCount (), e.isPopupTrigger ());
	dispatchComponent.dispatchEvent (e2);
	e2 = new MouseEvent (dispatchComponent, 500, e.getWhen () + 100001L, e.getModifiers (), 3, 3, 1, e.isPopupTrigger ());
	dispatchComponent.dispatchEvent (e2);
      }
    return false;
  }
  
  public boolean stopCellEditing ()
  {
    fireEditingStopped ();
    return true;
  }
  
  public void cancelCellEditing ()
  {
    fireEditingCanceled ();
  }
  
  public void addCellEditorListener (CellEditorListener l)
  {
    EventListenerList eventlistenerlist = listenerList;
    Class var_class = javax.swing.event.CellEditorListener.class;
    eventlistenerlist.add (var_class, l);
  }
  
  public void removeCellEditorListener (CellEditorListener l)
  {
    EventListenerList eventlistenerlist = listenerList;
    Class var_class = javax.swing.event.CellEditorListener.class;
    eventlistenerlist.remove (var_class, l);
  }
  
  protected void fireEditingStopped ()
  {
    Object[] listeners = listenerList.getListenerList ();
    for (int i = listeners.length - 2; i >= 0; i -= 2)
      {
	Object object = listeners[i];
	Class var_class = javax.swing.event.CellEditorListener.class;
	if (object == var_class)
	  {
	    if (changeEvent == null)
	      changeEvent = new ChangeEvent (this);
	    ((CellEditorListener) listeners[i + 1]).editingStopped (changeEvent);
	  }
      }
  }
  
  protected void fireEditingCanceled ()
  {
    Object[] listeners = listenerList.getListenerList ();
    for (int i = listeners.length - 2; i >= 0; i -= 2)
      {
	Object object = listeners[i];
	Class var_class = javax.swing.event.CellEditorListener.class;
	if (object == var_class)
	  {
	    if (changeEvent == null)
	      changeEvent = new ChangeEvent (this);
	    ((CellEditorListener) listeners[i + 1]).editingCanceled (changeEvent);
	  }
      }
  }
  
  public Component getTreeCellEditorComponent (JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row)
  {
    String stringValue = tree.convertValueToText (value, isSelected, expanded, leaf, row, false);
    editorComponent = (JComponent) value;
    container = tree;
    return editorComponent;
  }
  
  public Component getTableCellEditorComponent (JTable table, Object value, boolean isSelected, int row, int column)
  {
    editorComponent = (JComponent) value;
    container = table;
    return editorComponent;
  }
}
