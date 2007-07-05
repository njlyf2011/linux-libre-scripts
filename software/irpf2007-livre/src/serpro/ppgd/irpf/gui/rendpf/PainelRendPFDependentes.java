/* PainelRendPFDependentes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.rendpf;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;

public class PainelRendPFDependentes extends JPanel implements PainelIRPFIf
{
  private static final String TITULO = "Rendimentos Tribut\u00e1veis Recebidos de Pessoa F\u00edsica e do Exterior";
  public JButton btnCPFDependentes;
  private JLabel jLabel1;
  private JScrollPane jScrollPane1;
  private TableRendPF tableRendPF1;
  
  public String getTituloPainel ()
  {
    return "Rendimentos Tribut\u00e1veis Recebidos de Pessoa F\u00edsica e do Exterior";
  }
  
  public PainelRendPFDependentes ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "RendPFDependentes");
    initComponents ();
    tableRendPF1.setObjetoNegocio (IRPFFacade.getInstancia ().getRendPFDependente ());
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (f.getSize2D () + 1.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
    btnCPFDependentes.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	PainelRendPFDependentes.this.exibePainelAdicionaDependentes ();
      }
    });
  }
  
  private void exibePainelAdicionaDependentes ()
  {
    JDialog dlg = new JDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), true);
    dlg.getContentPane ().add (new PainelDlgDependente ());
    dlg.pack ();
    dlg.setTitle ("CPF dos Dependentes");
    dlg.setLocationRelativeTo (null);
    dlg.setResizable (false);
    dlg.setVisible (true);
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jScrollPane1 = new JScrollPane ();
    tableRendPF1 = new TableRendPF ();
    btnCPFDependentes = new JButton ();
    jLabel1.setText ("<HTML><P>Devem ser relacionados nesta ficha todos os rendimentos recebidos de pessoa f\u00edsica e do exterior de todos os dependentes, mesmo aqueles com valores menores do que o limite mensal de isen\u00e7\u00e3o.</P><P>Para os Dependentes, n\u00e3o existe importa\u00e7\u00e3o de dados do programa Carn\u00ea-Le\u00e3o.</P></HTML>");
    jScrollPane1.setViewportView (tableRendPF1);
    btnCPFDependentes.setText ("<HTML><B>Informe CPF dos dependentes</B></HTML>");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (2, jScrollPane1, -1, 589, 32767).add (layout.createSequentialGroup ().add (10, 10, 10).add (jLabel1, -1, 569, 32767).add (10, 10, 10)).add (2, layout.createSequentialGroup ().addContainerGap (369, 32767).add (btnCPFDependentes, -2, 210, -2).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (2, layout.createSequentialGroup ().add (jLabel1, -2, 84, -2).addPreferredGap (0, 18, 32767).add (btnCPFDependentes).addPreferredGap (0).add (jScrollPane1, -2, 313, -2)));
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
