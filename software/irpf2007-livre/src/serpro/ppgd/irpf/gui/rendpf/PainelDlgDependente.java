/* PainelDlgDependente - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.rendpf;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditCPF;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.ProcessoSwing;
import serpro.ppgd.infraestrutura.util.Tarefa;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.negocio.util.TabelaMensagens;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorCPF;

public class PainelDlgDependente extends JPanel
{
  private TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
  public JButton btnAdicionar;
  public JButton btnAjuda;
  public JButton btnCancelar;
  private JButton btnExcluir;
  public JButton btnOk;
  private JEditCPF jEditCPF1;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JPanel jPanel1;
  private JScrollPane jScrollPane1;
  private JList lstListaCPF;
  
  public PainelDlgDependente ()
  {
    initComponents ();
    PlataformaPPGD.getPlataforma ().setHelpID (this, "CPF_Dependentes");
    PlataformaPPGD.getPlataforma ().setHelpID (btnAjuda, "CPF_Dependentes");
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (f.getSize2D () + 1.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
    lstListaCPF.addListSelectionListener (new ListSelectionListener ()
    {
      public void valueChanged (ListSelectionEvent e)
      {
	btnExcluir.setEnabled (lstListaCPF.getSelectedIndices ().length > 0);
      }
    });
    btnExcluir.setEnabled (false);
    jEditCPF1.getInformacao ().addValidador (new ValidadorCPF ((byte) 3));
    ((JTextField) jEditCPF1.getComponenteFoco ()).addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	btnAdicionar.doClick ();
      }
    });
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jEditCPF1 = new JEditCPF ();
    jScrollPane1 = new JScrollPane ();
    lstListaCPF = new JList ();
    jPanel1 = new JPanel ();
    jLabel2 = new JLabel ();
    btnAjuda = new JButton ();
    btnCancelar = new JButton ();
    btnOk = new JButton ();
    btnAdicionar = new JButton ();
    btnExcluir = new JButton ();
    jLabel1.setText ("<HTML><B>Informe o CPF dos dependentes que tiveram<BR>rendimentos tribut\u00e1veis recebidos de Pessoas<BR>F\u00edsicas ou do Exterior.</B></HTML>");
    lstListaCPF.setModel (new CPFDependenteListModel ());
    lstListaCPF.setCellRenderer (new CPFDependenteListRenderer ());
    jScrollPane1.setViewportView (lstListaCPF);
    jPanel1.setBackground (new Color (0, 0, 255));
    jPanel1.setBorder (BorderFactory.createBevelBorder (0));
    jLabel2.setForeground (new Color (255, 255, 255));
    jLabel2.setText ("<HTML><B>CPF do dependente</B></HTML>");
    jPanel1.add (jLabel2);
    btnAjuda.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/HELP.PNG")));
    btnAjuda.setMnemonic ('A');
    btnAjuda.setText ("<HTML><B><U>A</U>juda</B></HTML>");
    btnCancelar.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/CANCELAR.PNG")));
    btnCancelar.setMnemonic ('C');
    btnCancelar.setText ("<HTML><B><U>C</U>ancelar</B></HTML>");
    btnCancelar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelDlgDependente.this.btnCancelarActionPerformed (evt);
      }
    });
    btnOk.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/OK.PNG")));
    btnOk.setMnemonic ('O');
    btnOk.setText ("<HTML><B><U>O</U>k</B></HTML>");
    btnOk.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelDlgDependente.this.btnOkActionPerformed (evt);
      }
    });
    btnAdicionar.setMnemonic ('d');
    btnAdicionar.setText ("<HTML><B>A<U>d</U>icionar</B></HTML>");
    btnAdicionar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelDlgDependente.this.btnAdicionarActionPerformed (evt);
      }
    });
    btnExcluir.setText ("<HTML><B>E<U>x</U>cluir</B></HTML>");
    btnExcluir.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelDlgDependente.this.btnExcluirActionPerformed (evt);
      }
    });
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (btnOk).addPreferredGap (0, 22, 32767).add (btnCancelar).add (18, 18, 18)).add (layout.createSequentialGroup ().add (31, 31, 31).add (layout.createParallelGroup (1).add (jEditCPF1, -2, 147, -2).add (jScrollPane1, -2, 126, -2)).addPreferredGap (0, 62, 32767))).addPreferredGap (0).add (layout.createParallelGroup (2, false).add (btnAdicionar, -1, -1, 32767).add (btnAjuda, -1, -1, 32767).add (btnExcluir, -1, -1, 32767)).addContainerGap ()).add (layout.createSequentialGroup ().add (10, 10, 10).add (jLabel1, -2, 334, -2)).add (layout.createSequentialGroup ().add (31, 31, 31).add (jPanel1, -2, 125, -2).addContainerGap (217, 32767)));
    layout.linkSize (new Component[] { btnAdicionar, btnAjuda, btnCancelar, btnOk }, 1);
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel1, -2, 59, -2).addPreferredGap (0).add (layout.createParallelGroup (1).add (jPanel1, -2, 30, -2).add (layout.createSequentialGroup ().add (34, 34, 34).add (layout.createParallelGroup (2, false).add (jEditCPF1, -1, -1, 32767).add (btnAdicionar, -1, -1, 32767)).addPreferredGap (0).add (layout.createParallelGroup (1).add (btnExcluir).add (jScrollPane1, -2, -1, -2)))).addPreferredGap (0, -1, 32767).add (layout.createParallelGroup (1).add (2, btnAjuda).add (2, layout.createParallelGroup (3).add (btnOk).add (btnCancelar))).addContainerGap ()));
    layout.linkSize (new Component[] { btnAdicionar, btnAjuda, btnCancelar, btnOk }, 2);
  }
  
  private void btnExcluirActionPerformed (ActionEvent evt)
  {
    ((CPFDependenteListModel) lstListaCPF.getModel ()).deletaItens (lstListaCPF.getSelectedValues ());
    lstListaCPF.clearSelection ();
  }
  
  private void btnAdicionarActionPerformed (ActionEvent evt)
  {
    jEditCPF1.setarCampo ();
    jEditCPF1.chamaValidacao ();
    if (! jEditCPF1.getInformacao ().isVazio ())
      {
	if (! jEditCPF1.getInformacao ().isValido ())
	  {
	    JOptionPane.showMessageDialog (SwingUtilities.getRoot (this), tab.msg ("add_cpf_dep"), "Erro", 0);
	    jEditCPF1.getButtonMensagem ().setVisible (false);
	    jEditCPF1.setaFoco (false);
	  }
	else
	  {
	    ((CPFDependenteListModel) lstListaCPF.getModel ()).addCPF (jEditCPF1.getInformacao ().getConteudoFormatado ());
	    jEditCPF1.getInformacao ().clear ();
	    jEditCPF1.setaFoco (false);
	  }
      }
  }
  
  private void btnOkActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    ProcessoSwing.executa (new Tarefa ()
    {
      public Object run ()
      {
	IRPFFacade.getInstancia ().getRendPFDependente ().getColecaoCPFDependentes ().clear ();
	((CPFDependenteListModel) lstListaCPF.getModel ()).getColecaoCPFDependentes ().excluirRegistrosEmBranco ();
	List itens = ((CPFDependenteListModel) lstListaCPF.getModel ()).getColecaoCPFDependentes ().recuperarLista ();
	Iterator it = itens.iterator ();
	while (it.hasNext ())
	  IRPFFacade.getInstancia ().getRendPFDependente ().getColecaoCPFDependentes ().recuperarLista ().add (it.next ());
	return null;
      }
    });
  }
  
  private void btnCancelarActionPerformed (ActionEvent evt)
  {
    jEditCPF1.getInformacao ().clear ();
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
}
