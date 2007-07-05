/* PainelConfirmacaoMudancaTipoDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.comparativo;
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

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.ConstantesGlobaisGUI;
import serpro.ppgd.gui.UtilitariosGUI;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelConfirmacaoMudancaTipoDeclaracao extends JPanel
{
  private AcaoConfirmaMudancaIf acaoConfirmaMudanca = null;
  private String paramMsg3 = null;
  private JButton btnNao;
  private JButton btnSim;
  private JLabel jLabel1;
  
  public PainelConfirmacaoMudancaTipoDeclaracao (AcaoConfirmaMudancaIf acao, String _paramMsg2)
  {
    initComponents ();
    jLabel1.setFont (PgdIRPF.FONTE_DEFAULT);
    UtilitariosGUI.aumentaFonte (jLabel1, 1);
    UtilitariosGUI.estiloFonte (jLabel1, 1);
    jLabel1.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    UtilitariosGUI.aumentaFonte (btnSim, 1);
    UtilitariosGUI.aumentaFonte (btnNao, 1);
    acaoConfirmaMudanca = acao;
    paramMsg3 = _paramMsg2;
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    btnSim = new JButton ();
    btnNao = new JButton ();
    jLabel1.setText ("<HTML><P><CENTER>OP\u00c7\u00c3O DE DECLARA\u00c7\u00c3O</CENTER></P><BR><P>N\u00e3o poder\u00e1 optar pela Declara\u00e7\u00e3o com Tributa\u00e7\u00e3o Simplificada o contribuinte que, nesta declara\u00e7\u00e3o, apresentou resultado negativo da atividade rural e deseja compens\u00e1-lo com o resultado positivo dos anos posteriores.</P><BR><P>Voc\u00ea deseja compensar o preju\u00edzo com resultados positivos dos anos posteriores?</P></HTML>");
    btnSim.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/pendencias.png")));
    btnSim.setText ("<HTML><B>SIM</B> (Declara\u00e7\u00e3o Completa)</HTML>");
    btnSim.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelConfirmacaoMudancaTipoDeclaracao.this.btnSimActionPerformed (evt);
      }
    });
    btnNao.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/converter.png")));
    btnNao.setText ("<HTML><B>N\u00c3O</B> (Declara\u00e7\u00e3o Simplificada)</HTML>");
    btnNao.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelConfirmacaoMudancaTipoDeclaracao.this.btnNaoActionPerformed (evt);
      }
    });
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (2).add (1, jLabel1, -1, 408, 32767).add (1, layout.createSequentialGroup ().add (btnSim, -2, 176, -2).addPreferredGap (0).add (btnNao))).addContainerGap ()));
    layout.linkSize (new Component[] { btnNao, btnSim }, 1);
    layout.setVerticalGroup (layout.createParallelGroup (1).add (2, layout.createSequentialGroup ().addContainerGap ().add (jLabel1, -2, 201, -2).addPreferredGap (0).add (layout.createParallelGroup (3).add (btnSim).add (btnNao)).addContainerGap ()));
    layout.linkSize (new Component[] { btnNao, btnSim }, 2);
  }
  
  private void btnNaoActionPerformed (ActionEvent evt)
  {
    TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
    JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), tab.msg ("painel_conf_muda_tipo_declaracao_3", new String[] { paramMsg3 }), "Informa\u00e7\u00e3o", 1);
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    acaoConfirmaMudanca.zeraPrejuizoAcompensar ();
  }
  
  private void btnSimActionPerformed (ActionEvent evt)
  {
    TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
    if (JOptionPane.showConfirmDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), tab.msg ("painel_conf_muda_tipo_declaracao_1"), "Informa\u00e7\u00e3o", 2) == 0)
      {
	((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
	acaoConfirmaMudanca.converteDeclaracao ();
	JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), tab.msg ("painel_conf_muda_tipo_declaracao_2"), "Informa\u00e7\u00e3o", 1);
      }
    else
      btnNaoActionPerformed (evt);
  }
}
