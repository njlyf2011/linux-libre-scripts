/* MyCustomRenderer - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import serpro.ppgd.negocio.ElementoTabela;

class MyCustomRenderer extends JPanel implements ListCellRenderer
{
  private JEditCodigo edit;
  
  public MyCustomRenderer (JEditCodigo aEdit)
  {
    FlowLayout flow = new FlowLayout (3, 0, 0);
    super.setLayout (flow);
    edit = aEdit;
  }
  
  public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
  {
    super.removeAll ();
    list.setBackground (edit.combo.getBackground ());
    list.setForeground (edit.combo.getForeground ());
    ElementoTabela elem = (ElementoTabela) value;
    if (edit.tamCols != null && elem != null)
      {
	for (int i = 0; i < edit.tamCols.length; i++)
	  {
	    if (edit.colsVisiveis[i] && ! edit.combo.isPopupVisible () || edit.colsVisiveisExpandidas[i] && edit.combo.isPopupVisible ())
	      {
		JLabel label = new JLabel (elem.getConteudo (i));
		label.setPreferredSize (edit.tamCols[i]);
		if (edit.combo.isPopupVisible ())
		  label.setBorder (edit.getBordaColunas ());
		if (isSelected)
		  {
		    label.setBackground (list.getSelectionBackground ());
		    label.setForeground (list.getSelectionForeground ());
		  }
		else
		  {
		    label.setBackground (list.getBackground ());
		    label.setForeground (list.getForeground ());
		  }
		add (label);
	      }
	  }
      }
    if (isSelected)
      {
	setBackground (list.getSelectionBackground ());
	setForeground (list.getSelectionForeground ());
      }
    else
      {
	setBackground (list.getBackground ());
	setForeground (list.getForeground ());
      }
    return this;
  }
}
