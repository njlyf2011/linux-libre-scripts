/* DefaultIRPFCellRenderer - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class DefaultIRPFCellRenderer implements TableCellRenderer
{
  public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    JLabel retorno = new JLabel ();
    retorno.setOpaque (true);
    retorno.setText ((String) value);
    if (! table.getModel ().isCellEditable (row, column))
      {
	retorno.setBackground (new JPanel ().getBackground ());
	retorno.setBorder (BorderFactory.createEtchedBorder ());
      }
    else
      {
	retorno.setBackground (Color.WHITE);
	retorno.setFont (retorno.getFont ().deriveFont (0));
      }
    retorno.setHorizontalAlignment (4);
    if (hasFocus)
      {
	retorno.setForeground (Color.WHITE);
	retorno.setBackground (table.getSelectionBackground ());
      }
    return retorno;
  }
}
