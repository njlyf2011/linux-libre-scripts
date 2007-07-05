/* PainelRendaVariavelFundosInvest - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.rendavariavel;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;

public class PainelRendaVariavelFundosInvest extends JPanel implements PainelIRPFIf
{
  private JLabel jLabel1;
  private JPanel jPanel1;
  private JScrollPane jScrollPane1;
  private TableFundosInvestimentos tableFundosInvestimentos1;
  
  public PainelRendaVariavelFundosInvest ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "FII");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (1);
    f = f.deriveFont (f.getSize2D () + 1.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
    tableFundosInvestimentos1.setObjetoNegocio (IRPFFacade.getInstancia ().getRendaVariavel ().getFundInvest ());
  }
  
  private void initComponents ()
  {
    jScrollPane1 = new JScrollPane ();
    tableFundosInvestimentos1 = new TableFundosInvestimentos ();
    jPanel1 = new JPanel ();
    jLabel1 = new JLabel ();
    tableFundosInvestimentos1.setAutoResizeMode (0);
    jScrollPane1.setViewportView (tableFundosInvestimentos1);
    jLabel1.setText ("GANHOS L\u00cdQUIDOS OU PERDAS");
    jPanel1.add (jLabel1);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jScrollPane1, -1, 479, 32767).addContainerGap ()).add (2, jPanel1, -1, 503, 32767));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jPanel1, -2, -1, -2).addPreferredGap (0).add (jScrollPane1, -2, 180, -2).addContainerGap (88, 32767)));
  }
  
  public String getTituloPainel ()
  {
    return "Renda Vari\u00e1vel - Opera\u00e7\u00f5es Fundos Investimento Imobili\u00e1rio";
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
