/* CPFDependenteListRenderer - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.rendpf;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import serpro.ppgd.irpf.rendpf.CPFDependente;

public class CPFDependenteListRenderer extends DefaultListCellRenderer
{
  public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
  {
    CPFDependente cpf = (CPFDependente) value;
    return super.getListCellRendererComponent (list, cpf.getCpf ().getConteudoFormatado (), index, isSelected, cellHasFocus);
  }
}
