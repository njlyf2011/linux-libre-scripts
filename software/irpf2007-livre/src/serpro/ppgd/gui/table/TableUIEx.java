/* TableUIEx - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.table;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

class TableUIEx extends BasicTableUI
{
  private Dimension createTableSize (long width)
  {
    int height = ((JTableEx) table).getCellRect (table.getRowCount (), 0, false).y;
    int totalMarginWidth = table.getColumnModel ().getColumnMargin () * table.getColumnCount ();
    long widthWithMargin = Math.abs (width) + (long) totalMarginWidth;
    if (widthWithMargin > 2147483647L)
      widthWithMargin = 2147483647L;
    return new Dimension ((int) widthWithMargin, height);
  }
  
  public Dimension getMinimumSize (JComponent c)
  {
    long width = 0L;
    Enumeration enumeration = table.getColumnModel ().getColumns ();
    while (enumeration.hasMoreElements ())
      {
	TableColumn aColumn = (TableColumn) enumeration.nextElement ();
	width += (long) aColumn.getMinWidth ();
      }
    return createTableSize (width);
  }
  
  public Dimension getPreferredSize (JComponent c)
  {
    long width = 0L;
    Enumeration enumeration = table.getColumnModel ().getColumns ();
    while (enumeration.hasMoreElements ())
      {
	TableColumn aColumn = (TableColumn) enumeration.nextElement ();
	width += (long) aColumn.getPreferredWidth ();
      }
    return createTableSize (width);
  }
  
  public Dimension getMaximumSize (JComponent c)
  {
    long width = 0L;
    Enumeration enumeration = table.getColumnModel ().getColumns ();
    while (enumeration.hasMoreElements ())
      {
	TableColumn aColumn = (TableColumn) enumeration.nextElement ();
	width += (long) aColumn.getMaxWidth ();
      }
    return createTableSize (width);
  }
  
  public void paint (Graphics g, JComponent c)
  {
    Rectangle oldClipBounds = g.getClipBounds ();
    Rectangle clipBounds = new Rectangle (oldClipBounds);
    int tableWidth = table.getColumnModel ().getTotalColumnWidth ();
    clipBounds.width = Math.min (clipBounds.width, tableWidth);
    g.setClip (clipBounds);
    paintGrid (g);
    int firstIndex = table.rowAtPoint (new Point (0, clipBounds.y));
    int lastIndex = lastVisibleRow (clipBounds);
    Rectangle rowRect = new Rectangle (0, 0, tableWidth, ((JTableEx) table).getRowHeight (firstIndex) + table.getRowMargin ());
    rowRect.y = table.getCellRect (firstIndex, 0, false).y;
    for (int index = firstIndex; index <= lastIndex; index++)
      {
	if (rowRect.intersects (clipBounds))
	  paintRow (g, index);
	rowRect.y += rowRect.height;
	rowRect.height = ((JTableEx) table).getRowHeight (index + 1);
      }
    g.setClip (oldClipBounds);
  }
  
  private void paintGrid (Graphics g)
  {
    g.setColor (table.getGridColor ());
    if (table.getShowHorizontalLines ())
      paintHorizontalLines (g);
    if (table.getShowVerticalLines ())
      paintVerticalLines (g);
  }
  
  private void paintHorizontalLines (Graphics g)
  {
    Rectangle r = g.getClipBounds ();
    Rectangle rect = r;
    int rowMargin = table.getRowMargin ();
    int firstIndex = table.rowAtPoint (new Point (0, r.y));
    int lastIndex = lastVisibleRow (r);
    int y = table.getCellRect (firstIndex + 1, 0, false).y - 1;
    for (int index = firstIndex; index <= lastIndex; index++)
      {
	if (y >= rect.y && y <= rect.y + rect.height)
	  g.drawLine (rect.x, y, rect.x + rect.width - 1, y);
	y += ((JTableEx) table).getRowHeight (index + 1) + rowMargin;
      }
  }
  
  private void paintVerticalLines (Graphics g)
  {
    Rectangle rect = g.getClipBounds ();
    int x = 0;
    int count = table.getColumnCount ();
    int horizontalSpacing = table.getIntercellSpacing ().width;
    for (int index = 0; index <= count; index++)
      {
	if (x > 0 && x - 1 >= rect.x && x - 1 <= rect.x + rect.width)
	  g.drawLine (x - 1, rect.y, x - 1, rect.y + rect.height - 1);
	if (index < count)
	  x += table.getColumnModel ().getColumn (index).getWidth () + horizontalSpacing;
      }
  }
  
  private void paintRow (Graphics g, int row)
  {
    Rectangle rect = g.getClipBounds ();
    int column = 0;
    boolean drawn = false;
    int draggedColumnIndex = -1;
    Rectangle draggedCellRect = null;
    Dimension spacing = table.getIntercellSpacing ();
    JTableHeader header = table.getTableHeader ();
    Rectangle cellRect = new Rectangle ();
    cellRect.height = ((JTableEx) table).getRowHeight (row) + spacing.height;
    cellRect.y = table.getCellRect (row, 0, false).y;
    Enumeration enumeration = table.getColumnModel ().getColumns ();
    while (enumeration.hasMoreElements ())
      {
	TableColumn aColumn = (TableColumn) enumeration.nextElement ();
	cellRect.width = aColumn.getWidth () + spacing.width;
	if (cellRect.intersects (rect))
	  {
	    drawn = true;
	    if (header == null || aColumn != header.getDraggedColumn ())
	      paintCell (g, cellRect, row, column);
	    else
	      {
		g.setColor (table.getParent ().getBackground ());
		g.fillRect (cellRect.x, cellRect.y, cellRect.width, cellRect.height);
		draggedCellRect = new Rectangle (cellRect);
		draggedColumnIndex = column;
	      }
	  }
	else if (drawn)
	  break;
	cellRect.x += cellRect.width;
	column++;
      }
    if (draggedColumnIndex != -1 && draggedCellRect != null)
      {
	draggedCellRect.x += header.getDraggedDistance ();
	g.setColor (table.getBackground ());
	g.fillRect (draggedCellRect.x, draggedCellRect.y, draggedCellRect.width, draggedCellRect.height);
	g.setColor (table.getGridColor ());
	int x1 = draggedCellRect.x;
	int y1 = draggedCellRect.y;
	int x2 = x1 + draggedCellRect.width - 1;
	int y2 = y1 + draggedCellRect.height - 1;
	if (table.getShowVerticalLines ())
	  g.drawLine (x2, y1, x2, y2);
	if (table.getShowHorizontalLines ())
	  g.drawLine (x1, y2, x2, y2);
	paintCell (g, draggedCellRect, row, draggedColumnIndex);
      }
  }
  
  private void paintCell (Graphics g, Rectangle cellRect, int row, int column)
  {
    int spacingHeight = table.getRowMargin ();
    int spacingWidth = table.getColumnModel ().getColumnMargin ();
    cellRect.setBounds (cellRect.x + spacingWidth / 2, cellRect.y + spacingHeight / 2, cellRect.width - spacingWidth, cellRect.height - spacingHeight);
    if (table.isEditing () && table.getEditingRow () == row && table.getEditingColumn () == column)
      {
	Component component = table.getEditorComponent ();
	component.setBounds (cellRect);
	component.validate ();
      }
    else
      {
	TableCellRenderer renderer = table.getCellRenderer (row, column);
	Component component = table.prepareRenderer (renderer, row, column);
	if (component.getParent () == null)
	  rendererPane.add (component);
	rendererPane.paintComponent (g, component, table, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
      }
    cellRect.setBounds (cellRect.x - spacingWidth / 2, cellRect.y - spacingHeight / 2, cellRect.width + spacingWidth, cellRect.height + spacingHeight);
  }
  
  private int lastVisibleRow (Rectangle clip)
  {
    int lastIndex = table.rowAtPoint (new Point (0, clip.y + clip.height - 1));
    if (lastIndex == -1)
      lastIndex = table.getRowCount () - 1;
    return lastIndex;
  }
}
