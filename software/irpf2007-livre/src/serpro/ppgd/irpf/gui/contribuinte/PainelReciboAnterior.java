/* PainelReciboAnterior - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.contribuinte;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditMascara;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;

public class PainelReciboAnterior extends JPanel
{
  private JEditMascara edtNumReciboAnterior;
  private JLabel jLabel17;
  
  public PainelReciboAnterior ()
  {
    initComponents ();
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, PgdIRPF.FONTE_DEFAULT.deriveFont (1));
  }
  
  private void initComponents ()
  {
    jLabel17 = new JLabel ();
    edtNumReciboAnterior = new JEditMascara ();
    jLabel17.setText ("N\u00ba do recibo da \u00faltima declara\u00e7\u00e3o entregue do exerc\u00edcio de 2006");
    edtNumReciboAnterior.setInformacaoAssociada ("contribuinte.numeroReciboDecAnterior");
    edtNumReciboAnterior.setCaracteresValidos ("0123456789 ");
    edtNumReciboAnterior.setMascara ("************'");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jLabel17).addPreferredGap (0).add (edtNumReciboAnterior, -2, 170, -2)));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createParallelGroup (2).add (edtNumReciboAnterior, -2, -1, -2).add (jLabel17)));
  }
}
