/* JComponentCellRenderer - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.table;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class JComponentCellRenderer implements TableCellRenderer
{
  public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    if (isSelected)
      {
	((JComponent) value).setBackground (table.getSelectionBackground ());
	((JComponent) value).setForeground (table.getSelectionForeground ());
	if (value instanceof JPanel)
	  {
	    Component[] c = ((JPanel) value).getComponents ();
	    for (int i = 0; i < c.length; i++)
	      {
		if (c[i] instanceof JLabel)
		  ((JComponent) c[i]).setForeground (table.getSelectionForeground ());
	      }
	  }
      }
    return (JComponent) value;
  }
}
