/* GroupableTableHeader - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.table;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class GroupableTableHeader extends JTableHeader
{
  private static final String uiClassID = "GroupableTableHeaderUI";
  protected Vector columnGroups = null;
  
  public GroupableTableHeader (TableColumnModel model)
  {
    super (model);
    setUI (new GroupableTableHeaderUI ());
    setReorderingAllowed (false);
  }
  
  public void setReorderingAllowed (boolean b)
  {
    reorderingAllowed = false;
  }
  
  public void addColumnGroup (ColumnGroup g)
  {
    if (columnGroups == null)
      columnGroups = new Vector ();
    columnGroups.addElement (g);
  }
  
  public Enumeration getColumnGroups (TableColumn col)
  {
    if (columnGroups == null)
      return null;
    Enumeration en = columnGroups.elements ();
    while (en.hasMoreElements ())
      {
	ColumnGroup cGroup = (ColumnGroup) en.nextElement ();
	Vector v_ret = cGroup.getColumnGroups (col, new Vector ());
	if (v_ret != null)
	  return v_ret.elements ();
      }
    return null;
  }
  
  public void setColumnMargin ()
  {
    if (columnGroups != null)
      {
	int columnMargin = getColumnModel ().getColumnMargin ();
	Enumeration en = columnGroups.elements ();
	while (en.hasMoreElements ())
	  {
	    ColumnGroup cGroup = (ColumnGroup) en.nextElement ();
	    cGroup.setColumnMargin (columnMargin);
	  }
      }
  }
}
