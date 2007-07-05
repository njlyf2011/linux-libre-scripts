/* DefaultIRPFHeaderCellRenderer - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import serpro.ppgd.infraestrutura.util.PPGDFormPanel;

public class DefaultIRPFHeaderCellRenderer implements TableCellRenderer
{
  public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    PPGDFormPanel pnlHeader = new PPGDFormPanel ();
    pnlHeader.setLayout (new FormLayout ("p:grow(.5),C:p,p:grow(.5)", "p:grow(.5),C:p,p:grow(.5)"));
    pnlHeader.setBackground (Color.BLUE);
    JLabel lblTitulo = new JLabel ();
    lblTitulo.setFont (lblTitulo.getFont ().deriveFont (1));
    lblTitulo.setText ((String) value);
    lblTitulo.setForeground (Color.WHITE);
    pnlHeader.getBuilder ().setRow (2);
    CellConstraints cc = new CellConstraints ();
    pnlHeader.getBuilder ().add (lblTitulo, cc.xy (2, 2));
    pnlHeader.setBorder (BorderFactory.createLineBorder (Color.LIGHT_GRAY));
    return pnlHeader;
  }
}
