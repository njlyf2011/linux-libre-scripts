/* GroupableTableCellRenderer - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.table;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

public class GroupableTableCellRenderer extends DefaultTableCellRenderer
{
  public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column)
  {
    javax.swing.table.JTableHeader header = table.getTableHeader ();
    if (header != null)
      {
	setForeground (Color.WHITE);
	setBackground (Color.RED);
      }
    setHorizontalAlignment (0);
    setText (value != null ? value.toString () : " ");
    setBorder (UIManager.getBorder ("TableHeader.cellBorder"));
    return this;
  }
}
