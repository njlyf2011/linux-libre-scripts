/* PainelReciboRetif - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.contribuinte;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditMascara;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;

public class PainelReciboRetif extends JPanel
{
  private JEditMascara edtNumRecibo;
  private JLabel lblNumReciboRetif;
  
  public PainelReciboRetif ()
  {
    initComponents ();
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, PgdIRPF.FONTE_DEFAULT.deriveFont (1));
  }
  
  private void initComponents ()
  {
    lblNumReciboRetif = new JLabel ();
    edtNumRecibo = new JEditMascara ();
    lblNumReciboRetif.setText ("N\u00ba do recibo da declara\u00e7\u00e3o anterior do exerc\u00edcio de 2007");
    edtNumRecibo.setInformacaoAssociada ("contribuinte.numReciboDecRetif");
    edtNumRecibo.setCaracteresValidos ("0123456789 ");
    edtNumRecibo.setMascara ("************'");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (lblNumReciboRetif).addPreferredGap (0).add (edtNumRecibo, -2, 170, -2)));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createParallelGroup (2).add (lblNumReciboRetif).add (edtNumRecibo, -2, -1, -2)));
  }
}
