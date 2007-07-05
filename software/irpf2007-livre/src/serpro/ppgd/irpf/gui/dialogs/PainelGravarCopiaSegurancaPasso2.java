/* PainelGravarCopiaSegurancaPasso2 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.gui.UtilitariosGUI;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.ProcessoSwing;
import serpro.ppgd.infraestrutura.util.Tarefa;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.gui.TableModelSelecionaDeclaracao;
import serpro.ppgd.irpf.gui.TableSelecionaDeclaracao;
import serpro.ppgd.irpf.txt.gravacaorestauracao.ConstantesRepositorio;
import serpro.ppgd.irpf.txt.gravacaorestauracao.GravadorTXT;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelGravarCopiaSegurancaPasso2 extends JPanel
{
  private JButton btnAjuda;
  private JButton btnAvancar;
  private JButton btnCancelar;
  private TableSelecionaDeclaracao edtTableDecs;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JPanel jPanel2;
  private JPanel jPanel3;
  private JScrollPane jScrollPane1;
  
  class ExecutarGravarIdDeclaracao
  {
    private File dir = null;
    
    public ExecutarGravarIdDeclaracao (File _dir)
    {
      dir = _dir;
    }
    
    public void gravar ()
    {
      String path = "";
      int[] rows = edtTableDecs.getSelectedRows ();
      TableModelSelecionaDeclaracao tableModel = (TableModelSelecionaDeclaracao) edtTableDecs.getModel ();
      try
	{
	  if (rows.length > 0)
	    {
	      path = dir.getPath ();
	      boolean emiteMsgSucesso = false;
	      for (int i = 0; i < rows.length; i++)
		{
		  IdentificadorDeclaracao idAtual = tableModel.getIdentificadorDeclaracao (rows[i]);
		  if (path != "")
		    {
		      TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
		      if (! GravadorTXT.fileExists (idAtual, path, ConstantesRepositorio.GRAV_COPIA) || JOptionPane.showConfirmDialog (PainelGravarCopiaSegurancaPasso2.this.getParent (), tab.msg ("copia_seg_jah_existe", new String[] { idAtual.getCpf ().getConteudoFormatado (), path }), "Confirma\u00e7\u00e3o", 0, 3) == 0)
			{
			  GravadorTXT.copiarDeclaracao (idAtual, path);
			  emiteMsgSucesso = true;
			}
		    }
		}
	      if (emiteMsgSucesso)
		JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "Grava\u00e7\u00e3o conclu\u00edda com sucesso.", "Informa\u00e7\u00e3o", 1);
	      else
		JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "Grava\u00e7\u00e3o cancelada pelo usu\u00e1rio.", "Informa\u00e7\u00e3o", 1);
	    }
	}
      catch (GeracaoTxtException ev)
	{
	  ev.printStackTrace ();
	  JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), ev.getMessage (), "Erro", 0);
	}
      catch (IOException ev)
	{
	  JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), ev.getMessage (), "Erro", 0);
	}
    }
  }
  
  public PainelGravarCopiaSegurancaPasso2 ()
  {
    initComponents ();
    PlataformaPPGD.getPlataforma ().setHelpID (this, "CCOPIAR");
    PlataformaPPGD.getPlataforma ().setHelpID (btnAjuda, "CCOPIAR");
    jScrollPane1.getViewport ().setBackground (Color.WHITE);
    edtTableDecs.addDeclaracaoSelectionListener (new ListSelectionListener ()
    {
      public void valueChanged (ListSelectionEvent e)
      {
	if (edtTableDecs.getSelectedRowCount () > 0)
	  btnAvancar.setEnabled (true);
	else
	  btnAvancar.setEnabled (false);
      }
    });
    edtTableDecs.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent e)
      {
	if (e.getClickCount () == 2 && edtTableDecs.getSelectedRowCount () > 0)
	  PainelGravarCopiaSegurancaPasso2.this.btnAvancarActionPerformed (null);
      }
    });
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jPanel2 = new JPanel ();
    btnCancelar = new JButton ();
    btnAjuda = new JButton ();
    btnAvancar = new JButton ();
    jPanel3 = new JPanel ();
    jLabel2 = new JLabel ();
    jLabel3 = new JLabel ();
    jLabel4 = new JLabel ();
    jLabel5 = new JLabel ();
    jScrollPane1 = new JScrollPane ();
    edtTableDecs = new TableSelecionaDeclaracao ();
    edtTableDecs.setSelectionMode (2);
    jLabel1.setForeground (new Color (0, 0, 128));
    jLabel1.setText ("<HTML><B>Selecione as declara\u00e7\u00f5es a serem copiadas e clique no bot\u00e3o Avan\u00e7ar</B></HTML>");
    btnCancelar.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/CANCELAR.PNG")));
    btnCancelar.setMnemonic ('C');
    btnCancelar.setText ("<HTML><B><U>C</U>ancelar</B></HTML>");
    btnCancelar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelGravarCopiaSegurancaPasso2.this.btnCancelarActionPerformed (evt);
      }
    });
    btnAjuda.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/HELP.PNG")));
    btnAjuda.setMnemonic ('A');
    btnAjuda.setText ("<HTML><B><U>A</U>juda</B></HTML>");
    btnAvancar.setMnemonic ('v');
    btnAvancar.setText ("<HTML><B>A<U>v</U>an\u00e7ar >></B></HTML>");
    btnAvancar.setEnabled (false);
    btnAvancar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelGravarCopiaSegurancaPasso2.this.btnAvancarActionPerformed (evt);
      }
    });
    GroupLayout jPanel2Layout = new GroupLayout (jPanel2);
    jPanel2.setLayout (jPanel2Layout);
    jPanel2Layout.setHorizontalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().addContainerGap ().add (jPanel2Layout.createParallelGroup (1).add (btnAjuda).add (btnCancelar).add (btnAvancar)).addContainerGap (-1, 32767)));
    jPanel2Layout.linkSize (new Component[] { btnAjuda, btnAvancar, btnCancelar }, 1);
    jPanel2Layout.setVerticalGroup (jPanel2Layout.createParallelGroup (1).add (2, jPanel2Layout.createSequentialGroup ().addContainerGap (205, 32767).add (btnAvancar).addPreferredGap (0).add (btnCancelar).addPreferredGap (0).add (btnAjuda).addContainerGap ()));
    jPanel2Layout.linkSize (new Component[] { btnAjuda, btnAvancar, btnCancelar }, 2);
    jLabel2.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/irpr_gravada.png")));
    jLabel2.setText ("<HTML><B>Gravada</B.</HTML>");
    jLabel3.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/irpr_transmitida.png")));
    jLabel3.setText ("<HTML><B>Transmitida</B.</HTML>");
    jLabel4.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/irpr_completa.png")));
    jLabel4.setText ("<HTML><B>Completa</B></HTML>");
    jLabel5.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/irpr_simplificada.png")));
    jLabel5.setText ("<HTML><B>Simplificada</B></HTML>");
    GroupLayout jPanel3Layout = new GroupLayout (jPanel3);
    jPanel3.setLayout (jPanel3Layout);
    jPanel3Layout.setHorizontalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().addContainerGap ().add (jLabel2, -2, 89, -2).addPreferredGap (0).add (jLabel3, -2, 105, -2).addPreferredGap (0).add (jLabel4, -2, 91, -2).addPreferredGap (0).add (jLabel5).addContainerGap (279, 32767)));
    jPanel3Layout.setVerticalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().addContainerGap ().add (jPanel3Layout.createParallelGroup (3).add (jLabel2).add (jLabel3).add (jLabel5).add (jLabel4)).addContainerGap (16, 32767)));
    edtTableDecs.setAutoResizeMode (0);
    jScrollPane1.setViewportView (edtTableDecs);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (2, layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (2).add (1, jPanel3, -1, -1, 32767).add (layout.createSequentialGroup ().add (jScrollPane1, -1, 543, 32767).addPreferredGap (0).add (jPanel2, -2, -1, -2)).add (1, jLabel1)).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel1).addPreferredGap (0).add (layout.createParallelGroup (1).add (jScrollPane1, -1, 307, 32767).add (jPanel2, -1, -1, 32767)).addPreferredGap (0).add (jPanel3, -2, -1, -2)));
  }
  
  private void btnAvancarActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    ProcessoSwing.executa (new Tarefa ()
    {
      public Object run ()
      {
	JFileChooser fc = UtilitariosGUI.setFileChooserProperties ("Grava\u00e7\u00e3o de C\u00f3pia de Seguran\u00e7a", "Gravar em:", "Gravar", "Gravar C\u00f3pia de Seguran\u00e7a");
	fc.setMultiSelectionEnabled (false);
	fc.setDialogType (1);
	fc.setAcceptAllFileFilterUsed (false);
	fc.setFileSelectionMode (1);
	int retorno = fc.showDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "Gravar");
	if (retorno == 0)
	  {
	    ExecutarGravarIdDeclaracao executarGravarIdDeclaracao = new ExecutarGravarIdDeclaracao (fc.getSelectedFile ());
	    executarGravarIdDeclaracao.gravar ();
	  }
	return null;
      }
    });
  }
  
  private void btnCancelarActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
}
