/* ColumnGroup - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.table;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import serpro.ppgd.gui.ConstantesGlobaisGUI;

public class ColumnGroup
{
  protected TableCellRenderer renderer;
  protected Vector v;
  protected String text;
  protected int margin = 0;
  
  public class MyTableCellRenderer extends DefaultTableCellRenderer
  {
    public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
      JTableHeader header = table.getTableHeader ();
      if (header != null)
	{
	  setForeground (header.getForeground ());
	  setBackground (header.getBackground ());
	  setFont (header.getFont ());
	}
      setHorizontalAlignment (0);
      setText (value == null ? "" : value.toString ());
      setBorder (BorderFactory.createMatteBorder (0, 0, 1, 1, ConstantesGlobaisGUI.COR_CINZA));
      UIManager.put ("TableHeader.cellBorder", BorderFactory.createMatteBorder (0, 0, 1, 1, ConstantesGlobaisGUI.COR_CINZA));
      return this;
    }
  }
  
  public ColumnGroup (String text)
  {
    this (null, text);
  }
  
  public ColumnGroup (TableCellRenderer renderer, String text)
  {
    if (renderer == null)
      this.renderer = new MyTableCellRenderer ();
    else
      this.renderer = renderer;
    this.text = text;
    v = new Vector ();
  }
  
  public void add (Object obj)
  {
    if (obj != null)
      v.addElement (obj);
  }
  
  public Vector getColumnGroups (TableColumn c, Vector g)
  {
    g.addElement (this);
    if (v.contains (c))
      return g;
    Enumeration en = v.elements ();
    while (en.hasMoreElements ())
      {
	Object obj = en.nextElement ();
	if (obj instanceof ColumnGroup)
	  {
	    Vector groups = ((ColumnGroup) obj).getColumnGroups (c, (Vector) g.clone ());
	    if (groups != null)
	      return groups;
	  }
      }
    return null;
  }
  
  public TableCellRenderer getHeaderRenderer ()
  {
    return renderer;
  }
  
  public void setHeaderRenderer (TableCellRenderer renderer)
  {
    if (renderer != null)
      this.renderer = renderer;
  }
  
  public Object getHeaderValue ()
  {
    return text;
  }
  
  public Dimension getSize (JTable table)
  {
    Component comp = renderer.getTableCellRendererComponent (table, getHeaderValue (), false, false, -1, -1);
    int height = comp.getPreferredSize ().height;
    int width = 0;
    Enumeration en = v.elements ();
    while (en.hasMoreElements ())
      {
	Object obj = en.nextElement ();
	if (obj instanceof TableColumn)
	  {
	    TableColumn aColumn = (TableColumn) obj;
	    width += aColumn.getWidth ();
	    width += margin;
	  }
	else
	  width += ((ColumnGroup) obj).getSize (table).width;
      }
    return new Dimension (width, height);
  }
  
  public void setColumnMargin (int margin)
  {
    this.margin = margin;
    Enumeration en = v.elements ();
    while (en.hasMoreElements ())
      {
	Object obj = en.nextElement ();
	if (obj instanceof ColumnGroup)
	  ((ColumnGroup) obj).setColumnMargin (margin);
      }
  }
  
  public int getLength ()
  {
    return v.size ();
  }
}
