/* PainelNovaDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditAlfa;
import serpro.ppgd.gui.xbeans.JEditCPF;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelNovaDeclaracao extends JPanel
{
  private TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
  private JButton btnAjuda;
  private JButton btnCancelar;
  private JButton btnOk;
  private JEditCPF edtCPF;
  private JEditAlfa edtNome;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JPanel jPanel1;
  private JPanel jPanel2;
  private JPanel jPanel3;
  
  public PainelNovaDeclaracao ()
  {
    initComponents ();
    PlataformaPPGD.getPlataforma ().setHelpID (this, "CCRIAR");
    PlataformaPPGD.getPlataforma ().setHelpID (btnAjuda, "CCRIAR");
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (1);
    f = f.deriveFont (f.getSize2D () + 1.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
    ((JTextField) edtNome.getComponenteFoco ()).addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	PainelNovaDeclaracao.this.btnOkActionPerformed (e);
      }
    });
    ((JTextField) edtCPF.getComponenteFoco ()).addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	edtNome.setaFoco (false);
      }
    });
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jPanel1 = new JPanel ();
    jLabel2 = new JLabel ();
    edtCPF = new JEditCPF ();
    jLabel3 = new JLabel ();
    edtNome = new JEditAlfa ();
    jPanel3 = new JPanel ();
    jPanel2 = new JPanel ();
    btnOk = new JButton ();
    btnCancelar = new JButton ();
    btnAjuda = new JButton ();
    jLabel1.setForeground (new Color (0, 0, 128));
    jLabel1.setHorizontalAlignment (0);
    jLabel1.setText ("<HTML><B>Contribuinte</B.</HTML>");
    jLabel2.setForeground (new Color (0, 0, 128));
    jLabel2.setText ("<HTML><B>CPF</B></HTML>");
    jLabel2.setVerticalAlignment (3);
    jLabel3.setForeground (new Color (0, 0, 128));
    jLabel3.setText ("<HTML><B>Nome</B></HTML>");
    jLabel3.setVerticalAlignment (3);
    edtNome.setMaxChars (60);
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (edtNome, -1, 483, 32767).add (jLabel3, -2, 44, -2).add (edtCPF, -2, 160, -2).add (jLabel2, -2, 33, -2)).addContainerGap ()));
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jLabel2).addPreferredGap (0).add (edtCPF, -2, -1, -2).addPreferredGap (0).add (jLabel3, -2, 22, -2).addPreferredGap (0).add (edtNome, -2, -1, -2).addContainerGap (-1, 32767)));
    btnOk.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/OK.PNG")));
    btnOk.setMnemonic ('O');
    btnOk.setText ("<HTML><B><U>O</U>k</B></HTML>");
    btnOk.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelNovaDeclaracao.this.btnOkActionPerformed (evt);
      }
    });
    btnCancelar.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/CANCELAR.PNG")));
    btnCancelar.setMnemonic ('C');
    btnCancelar.setText ("<HTML><B><U>C</U>ancelar</B></HTML>");
    btnCancelar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelNovaDeclaracao.this.btnCancelarActionPerformed (evt);
      }
    });
    btnAjuda.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/HELP.PNG")));
    btnAjuda.setMnemonic ('A');
    btnAjuda.setText ("<HTML><B><U>A</U>juda</B></HTML>");
    GroupLayout jPanel2Layout = new GroupLayout (jPanel2);
    jPanel2.setLayout (jPanel2Layout);
    jPanel2Layout.setHorizontalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().addContainerGap ().add (btnOk).addPreferredGap (0).add (btnCancelar).addPreferredGap (0).add (btnAjuda).addContainerGap (-1, 32767)));
    jPanel2Layout.linkSize (new Component[] { btnAjuda, btnCancelar, btnOk }, 1);
    jPanel2Layout.setVerticalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().addContainerGap ().add (jPanel2Layout.createParallelGroup (3).add (btnOk).add (btnCancelar).add (btnAjuda)).addContainerGap (-1, 32767)));
    jPanel2Layout.linkSize (new Component[] { btnAjuda, btnCancelar, btnOk }, 2);
    jPanel3.add (jPanel2);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (jLabel1, -1, 523, 32767).add (2, jPanel3, -1, 523, 32767).add (2, layout.createSequentialGroup ().addContainerGap ().add (jPanel1, -1, -1, 32767).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel1).addPreferredGap (0).add (jPanel1, -2, -1, -2).addPreferredGap (0).add (jPanel3, -2, -1, -2).addContainerGap (-1, 32767)));
  }
  
  private void btnCancelarActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
  
  private void btnOkActionPerformed (ActionEvent evt)
  {
    IdentificadorDeclaracao idnovo = new IdentificadorDeclaracao ();
    edtCPF.setarCampo ();
    edtNome.setarCampo ();
    idnovo.getCpf ().setConteudo (edtCPF.getInformacao ().asString ());
    idnovo.getNome ().setConteudo (edtNome.getInformacao ().asString ());
    idnovo.getCpf ().validar ();
    idnovo.getNome ().validar ();
    if (idnovo.getCpf ().isVazio ())
      {
	JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), new JLabel (tab.msg ("cpf_branco")), "Erro", 0);
	edtCPF.setaFoco (false);
      }
    else if (! idnovo.getCpf ().isValido ())
      {
	JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), new JLabel (tab.msg ("cpf_invalido")), "Erro", 0);
	edtCPF.setaFoco (false);
      }
    else if (idnovo.getNome ().isVazio ())
      {
	JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), new JLabel (tab.msg ("nome_branco")), "Erro", 0);
	edtNome.setaFoco (false);
      }
    else if (! idnovo.getNome ().isValido ())
      {
	JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), new JLabel (idnovo.getNome ().getRetornoTodasValidacoes ().getPrimeiroRetornoValidacaoMaisSevero ().getMensagemValidacao ()), "Erro", 0);
	edtNome.setaFoco (false);
      }
    else if (IRPFFacade.existeDeclaracao (idnovo.getCpf ().asString ()))
      {
	JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), new JLabel (tab.msg ("existe_cpf")), "Aten\u00e7\u00e3o", 2);
	edtCPF.setaFoco (false);
      }
    else
      {
	((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
	edtCPF.setarCampo ();
	edtNome.setarCampo ();
	IRPFFacade.criarDeclaracao (idnovo);
	IRPFUtil.abrirDeclaracao (idnovo, true);
	IRPFUtil.habilitaComponentesTemDeclaracao ();
      }
  }
}
