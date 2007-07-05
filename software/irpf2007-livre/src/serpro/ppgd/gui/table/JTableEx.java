/* JTableEx - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.table;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class JTableEx extends JTable
{
  protected HashMap rowHeights;
  
  public JTableEx ()
  {
    this (null, null, null);
  }
  
  public JTableEx (TableModel dm)
  {
    this (dm, null, null);
  }
  
  public JTableEx (TableModel dm, TableColumnModel cm)
  {
    this (dm, cm, null);
  }
  
  public JTableEx (TableModel dm, TableColumnModel cm, ListSelectionModel sm)
  {
    super (dm, cm, sm);
    rowHeights = new HashMap ();
    setUI (new TableUIEx ());
  }
  
  public JTableEx (int numRows, int numColumns)
  {
    this (new DefaultTableModel (numRows, numColumns));
  }
  
  public JTableEx (Vector rowData, Vector columnNames)
  {
    super (rowData, columnNames);
    rowHeights = new HashMap ();
    setUI (new TableUIEx ());
  }
  
  public JTableEx (Object[][] rowData, Object[] columnNames)
  {
    super (rowData, columnNames);
    rowHeights = new HashMap ();
    setUI (new TableUIEx ());
  }
  
  public int rowAtPoint (Point point)
  {
    int y = point.y;
    if (y < 0)
      return -1;
    int rowSpacing = getIntercellSpacing ().height;
    int rowCount = getRowCount ();
    int rowHeight = 0;
    for (int i = 0; i < rowCount; i++)
      {
	rowHeight += getRowHeight (i) + rowSpacing;
	if (y < rowHeight)
	  return i;
      }
    return -1;
  }
  
  public Rectangle getCellRect (int row, int column, boolean includeSpacing)
  {
    int index = 0;
    int columnMargin = getColumnModel ().getColumnMargin ();
    Enumeration enumeration = getColumnModel ().getColumns ();
    Rectangle cellFrame = new Rectangle ();
    cellFrame.height = getRowHeight (row) + rowMargin;
    int rowSpacing = getIntercellSpacing ().height;
    int y = 0;
    for (int i = 0; i < row; i++)
      y += getRowHeight (i) + rowSpacing;
    cellFrame.y = y;
    while (enumeration.hasMoreElements ())
      {
	TableColumn aColumn = (TableColumn) enumeration.nextElement ();
	cellFrame.width = aColumn.getWidth () + columnMargin;
	if (index == column)
	  break;
	cellFrame.x += cellFrame.width;
	index++;
      }
    if (! includeSpacing)
      {
	Dimension spacing = getIntercellSpacing ();
	cellFrame.setBounds (cellFrame.x + spacing.width / 2, cellFrame.y + spacing.height / 2, cellFrame.width - spacing.width, cellFrame.height - spacing.height);
      }
    return cellFrame;
  }
  
  public void tableChanged (TableModelEvent e)
  {
    if (e == null || e.getFirstRow () == -1)
      {
	clearSelection ();
	if (getAutoCreateColumnsFromModel ())
	  createDefaultColumnsFromModel ();
	resizeAndRepaint ();
	if (tableHeader != null)
	  tableHeader.resizeAndRepaint ();
      }
    else if (e.getType () == 1)
      tableRowsInserted (e);
    else if (e.getType () == -1)
      tableRowsDeleted (e);
    else
      {
	int modelColumn = e.getColumn ();
	int start = e.getFirstRow ();
	int end = e.getLastRow ();
	if (start == -1)
	  {
	    start = 0;
	    end = 2147483647;
	  }
	Rectangle dirtyRegion;
	if (modelColumn == -1)
	  dirtyRegion = new Rectangle (0, getCellRect (start, 0, false).y, getColumnModel ().getTotalColumnWidth (), 0);
	else
	  {
	    int column = convertColumnIndexToView (modelColumn);
	    dirtyRegion = getCellRect (start, column, false);
	  }
	if (end != 2147483647)
	  {
	    dirtyRegion.height = getCellRect (end + 1, 0, false).y - dirtyRegion.y;
	    repaint (dirtyRegion.x, dirtyRegion.y, dirtyRegion.width, dirtyRegion.height);
	  }
	else
	  resizeAndRepaint ();
      }
  }
  
  private void tableRowsInserted (TableModelEvent e)
  {
    int start = e.getFirstRow ();
    int end = e.getLastRow ();
    if (start < 0)
      start = 0;
    int rowCount = getRowCount ();
    int rowsInserted = end - start + 1;
    for (int r = start; r < rowCount; r++)
      {
	Integer height = (Integer) rowHeights.get (new Integer (r));
	if (height != null)
	  rowHeights.put (new Integer (r + rowsInserted), height);
      }
    Rectangle drawRect = new Rectangle (0, getCellRect (start, 0, false).y, getColumnModel ().getTotalColumnWidth (), 0);
    drawRect.height = getCellRect (rowCount, 0, false).y - drawRect.y;
    if (selectionModel != null)
      {
	if (end < 0)
	  end = getRowCount () - 1;
	int length = end - start + 1;
	selectionModel.insertIndexInterval (start, length, true);
      }
    revalidate ();
  }
  
  private void tableRowsDeleted (TableModelEvent e)
  {
    int start = e.getFirstRow ();
    int end = e.getLastRow ();
    if (start < 0)
      start = 0;
    int deletedCount = end - start + 1;
    int previousRowCount = getRowCount () + deletedCount;
    for (int i = start; i <= end; i++)
      resetRowHeight (i);
    for (int r = end + 1; r < previousRowCount; r++)
      {
	Integer height = (Integer) rowHeights.get (new Integer (r));
	if (height != null)
	  rowHeights.put (new Integer (r - deletedCount), height);
      }
    Rectangle drawRect = new Rectangle (0, getCellRect (start, 0, false).y, getColumnModel ().getTotalColumnWidth (), 0);
    drawRect.height = getCellRect (previousRowCount, 0, false).y - drawRect.y;
    if (selectionModel != null)
      {
	if (end < 0)
	  end = getRowCount () - 1;
	selectionModel.removeIndexInterval (start, end);
      }
    revalidate ();
  }
  
  public int getRowHeight (int row)
  {
    Object o = rowHeights.get (new Integer (row));
    if (o == null)
      return this.getRowHeight ();
    return ((Integer) o).intValue ();
  }
  
  public void setRowHeight (int row, int height)
  {
    rowHeights.put (new Integer (row), new Integer (height));
    revalidate ();
  }
  
  void resetRowHeight (int row)
  {
    rowHeights.remove (new Integer (row));
    revalidate ();
  }
  
  void resetRowHeight ()
  {
    rowHeights.clear ();
    revalidate ();
  }
}
