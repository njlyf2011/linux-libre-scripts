/* PainelEspolio - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.espolio;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditAlfa;
import serpro.ppgd.gui.xbeans.JEditCPF;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;

public class PainelEspolio extends JPanel implements PainelIRPFIf
{
  private JEditAlfa jEditAlfa1;
  private JEditAlfa jEditAlfa2;
  private JEditCPF jEditCPF1;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JPanel jPanel1;
  
  public PainelEspolio ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "Esp\u00f3lio");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (1);
    f = f.deriveFont (f.getSize2D () + 2.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
  }
  
  private void initComponents ()
  {
    jLabel2 = new JLabel ();
    jEditCPF1 = new JEditCPF ();
    jLabel3 = new JLabel ();
    jLabel4 = new JLabel ();
    jEditAlfa1 = new JEditAlfa ();
    jEditAlfa2 = new JEditAlfa ();
    jPanel1 = new JPanel ();
    jLabel1 = new JLabel ();
    jLabel2.setText ("01. N\u00b0 do CPF do inventariante");
    jEditCPF1.setInformacaoAssociada ("espolio.cpfInventariante");
    jLabel3.setText ("02. Nome do inventariante");
    jLabel4.setText ("03. Endere\u00e7o do inventariante");
    jEditAlfa1.setInformacaoAssociada ("espolio.nomeInventariante");
    jEditAlfa2.setInformacaoAssociada ("espolio.endInventariante");
    jLabel1.setText ("Esta ficha dever\u00e1 ser preenchida se a declara\u00e7\u00e3o for de esp\u00f3lio");
    jPanel1.add (jLabel1);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jLabel2).add (jLabel3).add (jLabel4)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jEditCPF1, -2, 123, -2).add (jEditAlfa2, -1, 273, 32767).add (jEditAlfa1, -1, 273, 32767)).addContainerGap ()).add (2, jPanel1, -1, 466, 32767));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (21, 21, 21).add (jPanel1, -2, 26, -2).add (17, 17, 17).add (layout.createParallelGroup (2).add (jLabel2).add (jEditCPF1, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel3).add (jEditAlfa1, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel4).add (jEditAlfa2, -2, -1, -2)).addContainerGap (164, 32767)));
  }
  
  public String getTituloPainel ()
  {
    return "Esp\u00f3lio";
  }
  
  public void vaiExibir ()
  {
    /* empty */
  }
  
  public void painelExibido ()
  {
    /* empty */
  }
}
