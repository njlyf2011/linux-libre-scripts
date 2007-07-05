/* GroupableTableColumnModel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.table;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

public class GroupableTableColumnModel extends DefaultTableColumnModel
{
  protected ArrayList columnGroups = new ArrayList ();
  
  public void addColumnGroup (ColumnGroup columnGroup)
  {
    columnGroups.add (columnGroup);
  }
  
  public Iterator columnGroupIterator ()
  {
    return columnGroups.iterator ();
  }
  
  public ColumnGroup getColumnGroup (int index)
  {
    if (index >= 0 && index < columnGroups.size ())
      return (ColumnGroup) columnGroups.get (index);
    return null;
  }
  
  public Iterator getColumnGroups (TableColumn col)
  {
    if (columnGroups.isEmpty ())
      return null;
    Iterator iter = columnGroups.iterator ();
    while (iter.hasNext ())
      {
	ColumnGroup cGroup = (ColumnGroup) iter.next ();
	Vector v_ret = cGroup.getColumnGroups (col, new Vector ());
	if (v_ret != null)
	  return v_ret.iterator ();
      }
    return null;
  }
}
