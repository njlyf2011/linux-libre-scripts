/* PainelImpostoPagar - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.gui.ConstantesGlobaisGUI;
import serpro.ppgd.gui.UtilitariosGUI;
import serpro.ppgd.gui.xbeans.JEditInteiro;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.negocio.Inteiro;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelImpostoPagar extends JPanel
{
  private DeclaracaoIRPF declaracaoIRPF = null;
  private Inteiro numQuotas = new Inteiro ();
  private JButton btnOk;
  private JEditInteiro edtNumQuotas;
  private JEditValor edtSaldoIAP;
  private JEditValor edtValorQuota;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JPanel jPanel1;
  private JPanel jPanel2;
  private JPanel jPanel3;
  private JPanel jPanel4;
  private JPanel jPanel5;
  
  public PainelImpostoPagar (DeclaracaoIRPF dec)
  {
    initComponents ();
    declaracaoIRPF = dec;
    edtSaldoIAP.setInformacao (declaracaoIRPF.getResumo ().getCalculoImposto ().getSaldoImpostoPagar ());
    edtNumQuotas.setInformacao (numQuotas);
    edtValorQuota.setInformacao (declaracaoIRPF.getResumo ().getCalculoImposto ().getValorQuota ());
    numQuotas.setLimiteMinimo (declaracaoIRPF.getResumo ().getCalculoImposto ().getNumQuotas ().getLimiteMinimo ());
    numQuotas.setLimiteMaximo (declaracaoIRPF.getResumo ().getCalculoImposto ().getNumQuotas ().getLimiteMaximo ());
    int auxNumQuotas = declaracaoIRPF.getResumo ().getCalculoImposto ().getNumQuotas ().asInteger ();
    numQuotas.setConteudo (auxNumQuotas);
    if (auxNumQuotas > numQuotas.getLimiteMaximo ())
      declaracaoIRPF.getResumo ().getCalculoImposto ().getNumQuotas ().setConteudo (numQuotas.asString ());
    numQuotas.addObservador (new Observador ()
    {
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	SwingUtilities.invokeLater (new Runnable ()
	{
	  public void run ()
	  {
	    if (numQuotas.getConteudoAntigo ().equals ("1") && numQuotas.asInteger () > 1)
	      JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), TabelaMensagens.getTabelaMensagens ().msg ("info_quotas"), "Informa\u00e7\u00e3o", 1);
	    declaracaoIRPF.getResumo ().getCalculoImposto ().getNumQuotas ().setConteudo (numQuotas.asString ());
	  }
	});
      }
    });
    UtilitariosGUI.aumentaFonte (jLabel1, 2);
    UtilitariosGUI.aumentaFonte (jLabel2, 2);
    UtilitariosGUI.aumentaFonte (jLabel3, 2);
    UtilitariosGUI.aumentaFonte (jLabel4, 2);
    UtilitariosGUI.aumentaFonte (jLabel5, 2);
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jLabel2 = new JLabel ();
    jPanel2 = new JPanel ();
    jPanel1 = new JPanel ();
    edtSaldoIAP = new JEditValor ();
    jLabel3 = new JLabel ();
    jPanel4 = new JPanel ();
    jPanel3 = new JPanel ();
    jLabel4 = new JLabel ();
    edtNumQuotas = new JEditInteiro ();
    jLabel5 = new JLabel ();
    edtValorQuota = new JEditValor ();
    jPanel5 = new JPanel ();
    btnOk = new JButton ();
    jLabel1.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel1.setHorizontalAlignment (0);
    jLabel1.setText ("<HTML><B>Confirme o n\u00famero de quotas para pagamento do imposto:</B></HTML>");
    jLabel1.setVerticalAlignment (3);
    jLabel2.setHorizontalAlignment (0);
    jLabel2.setText ("<HTML><B>SALDO DE IMPOSTO A PAGAR</B></HTML>");
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap (-1, 32767).add (edtSaldoIAP, -2, 137, -2)));
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (edtSaldoIAP, -2, -1, -2).addContainerGap (-1, 32767)));
    jPanel2.add (jPanel1);
    jLabel3.setHorizontalAlignment (0);
    jLabel3.setText ("<HTML><B>PARCELAMENTO</B></HTML>");
    jLabel4.setHorizontalAlignment (0);
    jLabel4.setText ("<HTML><B>N\u00famero de quotas (at\u00e9 8):</B></HTML>");
    jLabel5.setHorizontalAlignment (0);
    jLabel5.setText ("<HTML><B>Valor da quota:</B></HTML>");
    GroupLayout jPanel3Layout = new GroupLayout (jPanel3);
    jPanel3.setLayout (jPanel3Layout);
    jPanel3Layout.setHorizontalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().add (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().add (jLabel4, -1, 205, 32767).addPreferredGap (0).add (edtNumQuotas, -2, 78, -2)).add (jPanel3Layout.createSequentialGroup ().add (jLabel5, -1, 139, 32767).addPreferredGap (0).add (edtValorQuota, -2, 144, -2).addPreferredGap (0))).addContainerGap ()));
    jPanel3Layout.setVerticalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().addContainerGap ().add (jPanel3Layout.createParallelGroup (2).add (edtNumQuotas, -2, -1, -2).add (jLabel4, -2, 14, -2)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (2).add (jLabel5, -2, 14, -2).add (edtValorQuota, -2, -1, -2)).addContainerGap (-1, 32767)));
    jPanel3Layout.linkSize (new Component[] { edtValorQuota, jLabel5 }, 2);
    jPanel3Layout.linkSize (new Component[] { edtNumQuotas, jLabel4 }, 2);
    jPanel4.add (jPanel3);
    btnOk.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/OK.PNG")));
    btnOk.setMnemonic ('O');
    btnOk.setText ("<HTML><B><U>O</U>K</B></HTML>");
    btnOk.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelImpostoPagar.this.btnOkActionPerformed (evt);
      }
    });
    jPanel5.add (btnOk);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (jLabel1, -2, 517, -2).add (jLabel2, -1, 517, 32767).add (jPanel2, -1, 517, 32767).add (2, jLabel3, -1, 517, 32767).add (2, jPanel4, -1, 517, 32767).add (2, jPanel5, -1, 517, 32767));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jLabel1).addPreferredGap (0).add (jLabel2).addPreferredGap (0).add (jPanel2, -2, -1, -2).addPreferredGap (0).add (jLabel3).addPreferredGap (0).add (jPanel4, -2, -1, -2).addPreferredGap (0).add (jPanel5, -2, -1, -2).addContainerGap (36, 32767)));
  }
  
  private void btnOkActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getNumQuotas ().setConteudo (numQuotas.asInteger ());
  }
}
