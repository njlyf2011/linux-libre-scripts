/* PainelSugereImportacao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.gui.UtilitariosGUI;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelSugereImportacao extends JPanel
{
  private static TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
  private JButton btnAjuda;
  private JButton btnNao;
  private JButton btnSim;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JPanel jPanel1;
  private JPanel jPanel3;
  
  public PainelSugereImportacao ()
  {
    initComponents ();
    PlataformaPPGD.getPlataforma ().setHelpID (this, "CRECUPERAR");
    PlataformaPPGD.getPlataforma ().setHelpID (btnAjuda, "CRECUPERAR");
    UtilitariosGUI.aumentaFonte (jLabel2, 1);
    UtilitariosGUI.aumentaFonte (jLabel3, 1);
    UtilitariosGUI.aumentaFonte (jLabel4, 1);
    UtilitariosGUI.aumentaFonte (jLabel5, 1);
    UtilitariosGUI.aumentaFonte (jLabel6, 1);
  }
  
  private void initComponents ()
  {
    jPanel3 = new JPanel ();
    jPanel1 = new JPanel ();
    btnSim = new JButton ();
    btnNao = new JButton ();
    btnAjuda = new JButton ();
    jLabel2 = new JLabel ();
    jLabel3 = new JLabel ();
    jLabel4 = new JLabel ();
    jLabel5 = new JLabel ();
    jLabel6 = new JLabel ();
    btnSim.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/OK.PNG")));
    btnSim.setMnemonic ('S');
    btnSim.setText ("<HTML><B><U>S</U>im</B></HTML>");
    btnSim.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelSugereImportacao.this.btnSimActionPerformed (evt);
      }
    });
    btnNao.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/BTNO.PNG")));
    btnNao.setMnemonic ('N');
    btnNao.setText ("<HTML><B><U>N</U>\u00e3o</B></HTML>");
    btnNao.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelSugereImportacao.this.btnNaoActionPerformed (evt);
      }
    });
    btnAjuda.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/HELP.PNG")));
    btnAjuda.setMnemonic ('A');
    btnAjuda.setText ("<HTML><B><U>A</U>juda</B></HTML>");
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (btnSim).addPreferredGap (0).add (btnNao).addPreferredGap (0).add (btnAjuda).addContainerGap (-1, 32767)));
    jPanel1Layout.linkSize (new Component[] { btnAjuda, btnNao, btnSim }, 1);
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (3).add (btnSim).add (btnNao).add (btnAjuda)).addContainerGap (-1, 32767)));
    jPanel1Layout.linkSize (new Component[] { btnAjuda, btnNao, btnSim }, 2);
    jPanel3.add (jPanel1);
    jLabel2.setForeground (new Color (0, 0, 128));
    jLabel2.setText ("<html><b>O programa permite a importa\u00e7\u00e3o de dados da Declara\u00e7\u00e3o de Ajuste Anual do exerc\u00edcio de 2006, <br>inclusive a declara\u00e7\u00e3o de bens.</b></html>");
    jLabel3.setForeground (new Color (0, 0, 128));
    jLabel3.setText ("<html><p><b>Para saber que dados podem ser importados, clique no bot\u00e3o AJUDA ou pressione a tecla F1.</b></p></html>");
    jLabel4.setForeground (new Color (0, 0, 128));
    jLabel4.setText ("<html><b>A importa\u00e7\u00e3o \u00e9 feita para o mesmo modelo de declara\u00e7\u00e3o utilizado em 2006, completo ou <br>simplificado.</b></html>");
    jLabel5.setForeground (new Color (0, 0, 128));
    jLabel5.setText ("<html><b>Ap\u00f3s a importa\u00e7\u00e3o dos dados, se desejar mudar de modelo, escolha, no menu Ferramentas, a <br>op\u00e7\u00e3o Converter para...</b></html>");
    jLabel6.setForeground (new Color (0, 0, 128));
    jLabel6.setText ("<html><p><b>Deseja importar dados da declara\u00e7\u00e3o de 2006?</b></p></html>");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jPanel3, -1, 573, 32767).addContainerGap ()).add (layout.createSequentialGroup ().add (jLabel3, -1, 549, 32767).add (34, 34, 34)).add (layout.createSequentialGroup ().add (jLabel4, -1, 549, 32767).add (34, 34, 34)).add (layout.createSequentialGroup ().add (jLabel5, -1, 549, 32767).add (34, 34, 34)).add (layout.createSequentialGroup ().add (jLabel6).addContainerGap (318, 32767)).add (layout.createSequentialGroup ().add (jLabel2, -1, 573, 32767).addContainerGap ()))));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel2).addPreferredGap (0).add (jLabel3).addPreferredGap (0).add (jLabel4).addPreferredGap (0).add (jLabel5).addPreferredGap (0).add (jLabel6).addPreferredGap (0, -1, 32767).add (jPanel3, -2, -1, -2).addContainerGap ()));
  }
  
  private void btnNaoActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    IRPFGuiUtil.exibeDialog (new PainelNovaDeclaracao (), true, tab.msg ("nova_declaracao"), false);
  }
  
  private void btnSimActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    new RecuperarExercicioAnterior ().actionPerformed (evt);
  }
}
