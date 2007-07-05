/* IRPFTableModel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;

import com.jgoodies.forms.layout.FormLayout;

import serpro.ppgd.infraestrutura.util.PPGDFormPanel;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;

public abstract class IRPFTableModel extends AbstractTableModel
{
  private ObjetoNegocio objetoNegocio = null;
  
  public IRPFTableModel (ObjetoNegocio pObj)
  {
    objetoNegocio = pObj;
  }
  
  public abstract Informacao getInformacaoAt (int i, int i_0_);
  
  public abstract boolean isCellEditable (int i, int i_1_);
  
  public abstract void setValueAt (Object object, int i, int i_2_);
  
  public abstract String getColumnName (int i);
  
  protected JPanel getCelulaMes (int row)
  {
    PPGDFormPanel pnl = new PPGDFormPanel ();
    JLabel lbl = new JLabel ();
    lbl.setHorizontalAlignment (0);
    lbl.setVerticalAlignment (0);
    switch (row)
      {
      case 0:
	lbl.setText ("Jan.");
	break;
      case 1:
	lbl.setText ("Fev.");
	break;
      case 2:
	lbl.setText ("Mar.");
	break;
      case 3:
	lbl.setText ("Abr.");
	break;
      case 4:
	lbl.setText ("Mai.");
	break;
      case 5:
	lbl.setText ("Jun.");
	break;
      case 6:
	lbl.setText ("Jul.");
	break;
      case 7:
	lbl.setText ("Ago.");
	break;
      case 8:
	lbl.setText ("Set.");
	break;
      case 9:
	lbl.setText ("Out.");
	break;
      case 10:
	lbl.setText ("Nov.");
	break;
      case 11:
	lbl.setText ("Dez.");
	break;
      case 12:
	lbl.setText ("Total");
	lbl.setFont (lbl.getFont ().deriveFont (1));
	break;
      }
    pnl.setLayout (new FormLayout ("2dlu:grow(.5),P,2dlu:grow(.5)", "P"));
    pnl.getBuilder ().setColumn (2);
    pnl.getBuilder ().append (lbl);
    pnl.setBorder (BorderFactory.createEtchedBorder ());
    return pnl;
  }
  
  public void setObjetoNegocio (ObjetoNegocio pObj)
  {
    ObjetoNegocio objetonegocio = objetoNegocio;
    Class[] var_classes = new Class[1];
    int i = 0;
    Class var_class = serpro.ppgd.gui.EditCampo.class;
    var_classes[i] = var_class;
    objetonegocio.removeObservadores (var_classes);
    objetoNegocio = pObj;
    fireTableDataChanged ();
  }
  
  public ObjetoNegocio getObjetoNegocio ()
  {
    return objetoNegocio;
  }
}
