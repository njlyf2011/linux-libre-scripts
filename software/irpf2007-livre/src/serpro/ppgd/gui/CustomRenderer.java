/* CustomRenderer - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.filechooser.FileSystemView;

class CustomRenderer extends JPanel implements ListCellRenderer
{
  private JComboBox cmb = null;
  
  public CustomRenderer (JComboBox pCmb)
  {
    FlowLayout flow = new FlowLayout (3, 0, 0);
    super.setLayout (flow);
    cmb = pCmb;
  }
  
  public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
  {
    super.removeAll ();
    list.setBackground (cmb.getBackground ());
    list.setForeground (cmb.getForeground ());
    File file = (File) value;
    JLabel label = new JLabel (" " + file.toString ());
    boolean isWindows = System.getProperty ("os.name").toUpperCase ().indexOf ("WIND") != -1;
    boolean isFloppy = file.toString ().toUpperCase ().indexOf ("A:") != -1;
    if (isWindows && isFloppy)
      {
	label.setIcon (ComboFileRoots.iconeDisquete);
	label.setText (file.toString ());
      }
    else
      {
	try
	  {
	    label.setIcon (FileSystemView.getFileSystemView ().getSystemIcon (file));
	  }
	catch (Exception exception)
	  {
	    /* empty */
	  }
      }
    label.setFont (cmb.getFont ());
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
