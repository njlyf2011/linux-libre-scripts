/* PainelRendPFTitular - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.rendpf;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.app.acoes.Acoes;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;

public class PainelRendPFTitular extends JPanel implements PainelIRPFIf
{
  private static final String TITULO = "Rendimentos Tribut\u00e1veis Recebidos de Pessoa F\u00edsica e do Exterior";
  private JButton jButton1;
  private JLabel jLabel1;
  private JPanel jPanel1;
  private JScrollPane jScrollPane1;
  public TableRendPF tableRendPF1;
  
  public String getTituloPainel ()
  {
    return "Rendimentos Tribut\u00e1veis Recebidos de Pessoa F\u00edsica e do Exterior";
  }
  
  public PainelRendPFTitular ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "RendPFTitular");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (f.getSize2D () + 1.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
    tableRendPF1.setObjetoNegocio (IRPFFacade.getInstancia ().getRendPFTitular ());
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jScrollPane1 = new JScrollPane ();
    tableRendPF1 = new TableRendPF ();
    jPanel1 = new JPanel ();
    jButton1 = new JButton ();
    jLabel1.setText ("<HTML><P>Devem ser relacionados nesta ficha todos os rendimentos recebidos de pessoa f\u00edsica e do exterior, mesmo aqueles com valores menores do que o limite mensal de isen\u00e7\u00e3o.</P><P>Para que o dependente seja considerado como dedu\u00e7\u00e3o dos rendimentos na declara\u00e7\u00e3o, deve ser relacionado na ficha \"Dependentes\". </P><P>Para que o valor da pens\u00e3o aliment\u00edcia seja considerado como dedu\u00e7\u00e3o dos rendimentos na declara\u00e7\u00e3o, deve ter o respectivo valor e seus benefici\u00e1rios relacionados na ficha \"Pagamentos e Doa\u00e7\u00f5es Efetuados\".</P></HTML>");
    jScrollPane1.setViewportView (tableRendPF1);
    jButton1.setAction (new Acoes ());
    jButton1.setText ("<html><b>Importar</b></html>");
    jButton1.setActionCommand ("importar_cl");
    jPanel1.add (jButton1);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jLabel1, -1, 571, 32767).add (jPanel1, -1, 571, 32767)).addContainerGap ()).add (2, jScrollPane1, -1, 595, 32767));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap (-1, 32767).add (jLabel1).addPreferredGap (0).add (jPanel1, -2, -1, -2).addPreferredGap (0).add (jScrollPane1, -2, 314, -2)));
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
