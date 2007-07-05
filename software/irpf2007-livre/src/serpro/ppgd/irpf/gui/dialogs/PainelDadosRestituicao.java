/* PainelDadosRestituicao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.gui.ConstantesGlobaisGUI;
import serpro.ppgd.gui.UtilitariosGUI;
import serpro.ppgd.gui.xbeans.JEditAlfa;
import serpro.ppgd.gui.xbeans.JEditCodigo;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.DeclaracaoIRPF;

public class PainelDadosRestituicao extends JPanel
{
  public int opcao = -1;
  private DeclaracaoIRPF declaracaoIRPF = null;
  private JButton btnAjuda;
  private JButton btnCancelar;
  private JButton btnOk;
  private JEditAlfa edtAgencia;
  private JEditCodigo edtBanco;
  private JEditAlfa edtDVAgencia;
  private JEditAlfa edtDVConta;
  private JEditAlfa edtNumConta;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JPanel jPanel2;
  
  public PainelDadosRestituicao (DeclaracaoIRPF dec)
  {
    initComponents ();
    PlataformaPPGD.getPlataforma ().setHelpID (this, "infbancarias");
    PlataformaPPGD.getPlataforma ().setHelpID (btnAjuda, "infbancarias");
    declaracaoIRPF = dec;
    edtBanco.setInformacao (declaracaoIRPF.getResumo ().getCalculoImposto ().getBanco ());
    edtAgencia.setInformacao (declaracaoIRPF.getResumo ().getCalculoImposto ().getAgencia ());
    edtDVAgencia.setInformacao (declaracaoIRPF.getResumo ().getCalculoImposto ().getDvAgencia ());
    edtNumConta.setInformacao (declaracaoIRPF.getResumo ().getCalculoImposto ().getContaCredito ());
    edtDVConta.setInformacao (declaracaoIRPF.getResumo ().getCalculoImposto ().getDvContaCredito ());
    UtilitariosGUI.aumentaFonte (jLabel1, 2);
    UtilitariosGUI.aumentaFonte (jLabel2, 2);
    UtilitariosGUI.aumentaFonte (jLabel4, 2);
    UtilitariosGUI.aumentaFonte (jLabel5, 2);
    UtilitariosGUI.aumentaFonte (jLabel6, 2);
    UtilitariosGUI.aumentaFonte (jLabel8, 2);
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jLabel2 = new JLabel ();
    jLabel3 = new JLabel ();
    jLabel4 = new JLabel ();
    edtAgencia = new JEditAlfa ();
    jLabel5 = new JLabel ();
    edtDVAgencia = new JEditAlfa ();
    jLabel6 = new JLabel ();
    jLabel7 = new JLabel ();
    edtNumConta = new JEditAlfa ();
    jLabel8 = new JLabel ();
    edtDVConta = new JEditAlfa ();
    jPanel2 = new JPanel ();
    btnOk = new JButton ();
    btnCancelar = new JButton ();
    btnAjuda = new JButton ();
    edtBanco = new JEditCodigo ();
    jLabel1.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel1.setText ("<HTML><B>Informe o c\u00f3digo do banco (3 algarismos)</B></HTML>");
    jLabel1.setVerticalAlignment (3);
    jLabel2.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel2.setHorizontalAlignment (2);
    jLabel2.setText ("<HTML><B>Banco</B></HTML>");
    jLabel3.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel3.setHorizontalAlignment (2);
    jLabel3.setText ("<HTML><B><P>Informe o c\u00f3digo da ag\u00eancia onde deseja receber sua restitui\u00e7\u00e3o. Indique tamb\u00e9m o d\u00edgito verificador(DV), se houver</P><P>Exemplo: 123-4, sendo 123 o c\u00f3digo da ag\u00eancia e 4 o d\u00edgito verificador.</P><P>Neste caso, informe, no campo da ag\u00eancia 123 e, no campo DV, 4.</P></B></HTML>");
    jLabel4.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel4.setHorizontalAlignment (2);
    jLabel4.setText ("<HTML><B>Ag\u00eancia</B></HTML>");
    edtAgencia.setMaxChars (5);
    jLabel5.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel5.setHorizontalAlignment (2);
    jLabel5.setText ("<HTML><B>DV</B></HTML>");
    edtDVAgencia.setMaxChars (1);
    jLabel6.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel6.setHorizontalAlignment (2);
    jLabel6.setText ("<HTML><B>N\u00ba da conta</B></HTML>");
    jLabel7.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel7.setHorizontalAlignment (0);
    jLabel7.setText ("<HTML><B>Para autorizar o cr\u00e9dito autom\u00e1tico na conta banc\u00e1ria, informe o n\u00ba da conta para cr\u00e9dito, inclusive o DV, se houver</B></HTML>");
    edtNumConta.setMaxChars (13);
    jLabel8.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel8.setHorizontalAlignment (2);
    jLabel8.setText ("<HTML><B>DV</B></HTML>");
    edtDVConta.setMaxChars (2);
    btnOk.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/OK.PNG")));
    btnOk.setText ("<HTML><B><U>O</U>k</B></HTML>");
    btnOk.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelDadosRestituicao.this.btnOkActionPerformed (evt);
      }
    });
    btnCancelar.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/CANCELAR.PNG")));
    btnCancelar.setText ("<HTML><B><U>C</U>ancelar</B></HTML>");
    btnCancelar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelDadosRestituicao.this.btnCancelarActionPerformed (evt);
      }
    });
    btnAjuda.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/HELP.PNG")));
    btnAjuda.setText ("<HTML><B><U>A</U>juda</B></HTML>");
    GroupLayout jPanel2Layout = new GroupLayout (jPanel2);
    jPanel2.setLayout (jPanel2Layout);
    jPanel2Layout.setHorizontalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().addContainerGap ().add (btnOk).addPreferredGap (0).add (btnCancelar).addPreferredGap (0).add (btnAjuda).addContainerGap (188, 32767)));
    jPanel2Layout.setVerticalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().addContainerGap ().add (jPanel2Layout.createParallelGroup (3).add (btnOk).add (btnCancelar).add (btnAjuda)).addContainerGap (-1, 32767)));
    edtBanco.exibeColunaExpandida (0);
    edtBanco.exibeColunaNaoExpandida (0);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (2, layout.createSequentialGroup ().add (layout.createParallelGroup (2).add (1, layout.createSequentialGroup ().add (jLabel2, -2, 75, -2).addPreferredGap (0).add (edtBanco, -2, 369, -2)).add (1, jLabel1, -1, 568, 32767)).add (20, 20, 20)).add (layout.createSequentialGroup ().add (jLabel3, -1, 578, 32767).addContainerGap ()).add (layout.createSequentialGroup ().add (jLabel4, -2, 75, -2).addPreferredGap (0).add (edtAgencia, -2, -1, -2).addPreferredGap (0).add (jLabel5, -2, 28, -2).addPreferredGap (0).add (edtDVAgencia, -2, 49, -2).addContainerGap ()).add (2, layout.createSequentialGroup ().add (jLabel7, -1, 578, 32767).addContainerGap ()).add (layout.createSequentialGroup ().add (jLabel6, -2, 77, -2).addPreferredGap (0).add (edtNumConta, -2, -1, -2).addPreferredGap (0).add (jLabel8, -2, 26, -2).addPreferredGap (0).add (edtDVConta, -2, 50, -2).addContainerGap ()).add (layout.createSequentialGroup ().add (jPanel2, -2, -1, -2).addContainerGap (113, 32767)))));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jLabel1).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel2).add (edtBanco, -1, 20, 32767)).addPreferredGap (0).add (jLabel3).addPreferredGap (0).add (layout.createParallelGroup (1, false).add (jLabel5, -1, 20, 32767).add (edtAgencia, -1, -1, 32767).add (jLabel4, -1, -1, 32767).add (edtDVAgencia, -1, -1, 32767)).addPreferredGap (0).add (jLabel7, -2, 39, -2).addPreferredGap (0).add (layout.createParallelGroup (1, false).add (2, edtDVConta, -1, -1, 32767).add (2, jLabel8, -1, 20, 32767).add (2, edtNumConta, -1, -1, 32767).add (2, jLabel6, -1, -1, 32767)).addPreferredGap (0).add (jPanel2, -2, -1, -2).addContainerGap (25, 32767)));
  }
  
  private void btnCancelarActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
  
  private void btnOkActionPerformed (ActionEvent evt)
  {
    opcao = 1;
    edtBanco.setaCampo ();
    edtAgencia.setarCampo ();
    edtDVAgencia.setarCampo ();
    edtNumConta.setarCampo ();
    edtDVConta.setarCampo ();
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
}
