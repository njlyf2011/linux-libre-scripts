/* PainelGravarEntrega - Decompiled by JODE
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

import javax.swing.BorderFactory;
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
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.gui.TableModelSelecionaDeclaracao;
import serpro.ppgd.irpf.gui.TableSelecionaDeclaracao;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.txt.gravacaorestauracao.ConstantesRepositorio;
import serpro.ppgd.irpf.txt.gravacaorestauracao.GravadorTXT;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.Logico;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelGravarEntrega extends JPanel
{
  private File dirSelecionado = new File (FabricaUtilitarios.getPathCompletoDirGravadas ());
  private JButton btnAjuda;
  private JButton btnCancelar;
  private JButton btnGravar;
  private JButton btnSelecionarPasta;
  private TableSelecionaDeclaracao edtTableDecs;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JPanel jPanel2;
  private JPanel jPanel3;
  private JPanel jPanel4;
  private JScrollPane jScrollPane1;
  private JLabel lblDirSelecionado;
  
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
		      DeclaracaoIRPF decAtual = IRPFFacade.getInstancia ().recuperarDeclaracaoIRPF (idAtual.getCpf ().asString ());
		      boolean simplesEhMelhor = decAtual.simplesEhMelhor ();
		      if (idAtual.isCompleta () && simplesEhMelhor || ! idAtual.isCompleta () && ! simplesEhMelhor)
			{
			  PainelGravacaoEntregaModeloMelhor painelGravacaoEntregaModeloMelhor = new PainelGravacaoEntregaModeloMelhor (decAtual, simplesEhMelhor);
			  IRPFGuiUtil.exibeDialog ((JDialog) SwingUtilities.getRoot (PainelGravarEntrega.this), painelGravacaoEntregaModeloMelhor, true, "Confirma\u00e7\u00e3o", false);
			}
		      if (decAtual.getModelo ().getSaldoImpostoPagar ().comparacao (">", "0,00"))
			{
			  PainelImpostoPagar painelImpostoPagar = new PainelImpostoPagar (decAtual);
			  IRPFGuiUtil.exibeDialog (painelImpostoPagar, true, "Quotas do Imposto a Pagar", false);
			}
		      else if (decAtual.getModelo ().getImpostoRestituir ().comparacao (">", "0,00"))
			{
			  if (decAtual.getContribuinte ().getExterior ().getConteudoFormatado ().equals (Logico.NAO))
			    {
			      PainelDadosRestituicao painelDadosRestituicao = new PainelDadosRestituicao (decAtual);
			      IRPFGuiUtil.exibeDialog (painelDadosRestituicao, true, "Dados da Restitui\u00e7\u00e3o", false);
			      if (painelDadosRestituicao.opcao == -1)
				continue;
			    }
			  else
			    JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), tab.msg ("dados_restituicao_exterior"), "Informa\u00e7\u00e3o", 1);
			}
		      PainelVerificarPendencias painelVerificarPendencias = new PainelVerificarPendencias (decAtual);
		      if (painelVerificarPendencias.getTotalErros () > 0)
			{
			  if (JOptionPane.showConfirmDialog (PainelGravarEntrega.this.getParent (), tab.msg ("gravacao_entrega_erros_impeditivos", new String[] { idAtual.getCpf ().getConteudoFormatado (), path }), "Confirma\u00e7\u00e3o", 0, 3) == 0)
			    {
			      IRPFUtil.abrirDeclaracao (idAtual, true);
			      IRPFGuiUtil.exibeDialog (painelVerificarPendencias, true, "Verifica\u00e7\u00e3o de Pend\u00eancias", true);
			      return;
			    }
			}
		      else
			{
			  if (painelVerificarPendencias.getTotalAvisos () > 0)
			    {
			      PainelGravacaoEntregaPendenciasNaoImpeditivas painelGravacaoEntregaPendenciasNaoImpeditivas = new PainelGravacaoEntregaPendenciasNaoImpeditivas ();
			      IRPFGuiUtil.exibeDialog ((JDialog) SwingUtilities.getRoot (PainelGravarEntrega.this), painelGravacaoEntregaPendenciasNaoImpeditivas, true, "Grava\u00e7\u00e3o da Declara\u00e7\u00e3o", true);
			      int opcao = PainelGravacaoEntregaPendenciasNaoImpeditivas.OPCAO_SELECIONADA;
			      if (opcao == PainelGravacaoEntregaPendenciasNaoImpeditivas.OPT_CORRIGIR)
				{
				  IRPFUtil.abrirDeclaracao (idAtual, true);
				  IRPFGuiUtil.exibeDialog (painelVerificarPendencias, true, "Verifica\u00e7\u00e3o de Pend\u00eancias", false);
				  return;
				}
			      if (opcao == PainelGravacaoEntregaPendenciasNaoImpeditivas.OPT_CANCELAR)
				continue;
			      if (PainelGravacaoEntregaPendenciasNaoImpeditivas.OPT_GRAVAR != 0)
				{
				  /* empty */
				}
			    }
			  if (new File (GravadorTXT.montaNome (ConstantesRepositorio.GRAV_GERACAO, path, idAtual)).exists ())
			    {
			      int ret = JOptionPane.showConfirmDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), tab.msg ("gravar_entrega_ja_existe"), "Declara\u00e7\u00e3o j\u00e1 foi gravada", 0);
			      if (ret == 1)
				continue;
			    }
			  GravadorTXT.gravarDeclaracao (idAtual, path);
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
  
  public PainelGravarEntrega ()
  {
    initComponents ();
    PlataformaPPGD.getPlataforma ().setHelpID (this, "CENTREGAR");
    PlataformaPPGD.getPlataforma ().setHelpID (btnAjuda, "CENTREGAR");
    jScrollPane1.getViewport ().setBackground (Color.WHITE);
    edtTableDecs.addDeclaracaoSelectionListener (new ListSelectionListener ()
    {
      public void valueChanged (ListSelectionEvent e)
      {
	if (edtTableDecs.getSelectedRowCount () > 0)
	  btnGravar.setEnabled (true);
	else
	  btnGravar.setEnabled (false);
      }
    });
    edtTableDecs.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent e)
      {
	if (e.getClickCount () == 2 && edtTableDecs.getSelectedRowCount () > 0)
	  PainelGravarEntrega.this.btnGravarActionPerformed (null);
      }
    });
    dirSelecionado.mkdirs ();
    atualizaTextoDirSelecionado ();
  }
  
  private void atualizaTextoDirSelecionado ()
  {
    String caminhoGravacao = dirSelecionado.getPath ();
    if (System.getProperty ("os.name").startsWith ("Windows"))
      caminhoGravacao = caminhoGravacao.replaceFirst ("/", "");
    lblDirSelecionado.setText ("<HTML><B>" + caminhoGravacao + "</B></HTML>");
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jPanel3 = new JPanel ();
    jLabel2 = new JLabel ();
    jLabel3 = new JLabel ();
    jLabel4 = new JLabel ();
    jLabel5 = new JLabel ();
    jScrollPane1 = new JScrollPane ();
    edtTableDecs = new TableSelecionaDeclaracao ();
    jPanel4 = new JPanel ();
    jPanel2 = new JPanel ();
    btnCancelar = new JButton ();
    btnAjuda = new JButton ();
    btnGravar = new JButton ();
    jLabel6 = new JLabel ();
    lblDirSelecionado = new JLabel ();
    btnSelecionarPasta = new JButton ();
    jLabel1.setForeground (new Color (0, 0, 128));
    jLabel1.setText ("<HTML><B>Selecione o CPF da Declara\u00e7\u00e3o a ser gravada:</B></HTML>");
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
    jPanel3Layout.setHorizontalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().addContainerGap ().add (jLabel2, -2, 89, -2).addPreferredGap (0).add (jLabel3, -2, 105, -2).addPreferredGap (0).add (jLabel4, -2, 91, -2).addPreferredGap (0).add (jLabel5).addContainerGap (223, 32767)));
    jPanel3Layout.setVerticalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().addContainerGap ().add (jPanel3Layout.createParallelGroup (3).add (jLabel2).add (jLabel3).add (jLabel5).add (jLabel4)).addContainerGap (16, 32767)));
    edtTableDecs.setAutoResizeMode (0);
    jScrollPane1.setViewportView (edtTableDecs);
    btnCancelar.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/CANCELAR.PNG")));
    btnCancelar.setMnemonic ('C');
    btnCancelar.setText ("<HTML><B><U>C</U>ancelar</B></HTML>");
    btnCancelar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelGravarEntrega.this.btnCancelarActionPerformed (evt);
      }
    });
    btnAjuda.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/HELP.PNG")));
    btnAjuda.setMnemonic ('A');
    btnAjuda.setText ("<HTML><B><U>A</U>juda</B></HTML>");
    btnGravar.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/OK.PNG")));
    btnGravar.setMnemonic ('v');
    btnGravar.setText ("<HTML><B><U>G</U>ravar</B></HTML>");
    btnGravar.setEnabled (false);
    btnGravar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelGravarEntrega.this.btnGravarActionPerformed (evt);
      }
    });
    GroupLayout jPanel2Layout = new GroupLayout (jPanel2);
    jPanel2.setLayout (jPanel2Layout);
    jPanel2Layout.setHorizontalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().addContainerGap ().add (btnGravar).addPreferredGap (0).add (btnCancelar).addPreferredGap (0).add (btnAjuda).addContainerGap (-1, 32767)));
    jPanel2Layout.linkSize (new Component[] { btnAjuda, btnCancelar, btnGravar }, 1);
    jPanel2Layout.setVerticalGroup (jPanel2Layout.createParallelGroup (1).add (2, jPanel2Layout.createSequentialGroup ().addContainerGap (-1, 32767).add (jPanel2Layout.createParallelGroup (3).add (btnGravar).add (btnCancelar).add (btnAjuda))));
    jPanel2Layout.linkSize (new Component[] { btnAjuda, btnCancelar, btnGravar }, 2);
    jPanel4.add (jPanel2);
    jLabel6.setForeground (new Color (0, 0, 128));
    jLabel6.setText ("<HTML><CENTER><B>Selecione a pasta onde ser\u00e1 feita a grava\u00e7\u00e3o:</B></CENTER></HTML>");
    lblDirSelecionado.setText ("<HTML><B>Gravadas</B></HTML>");
    lblDirSelecionado.setBorder (BorderFactory.createBevelBorder (1));
    btnSelecionarPasta.setText ("<HTML><B>...</B></HTML>");
    btnSelecionarPasta.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelGravarEntrega.this.btnSelecionarPastaActionPerformed (evt);
      }
    });
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jPanel4, -1, 622, 32767).add (2, jLabel6, -1, 622, 32767).add (layout.createSequentialGroup ().add (lblDirSelecionado, -1, 573, 32767).addPreferredGap (0).add (btnSelecionarPasta)).add (jLabel1).add (2, jScrollPane1, -1, 622, 32767).add (2, jPanel3, -1, -1, 32767)).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel6).addPreferredGap (0).add (layout.createParallelGroup (3).add (lblDirSelecionado, -2, 25, -2).add (btnSelecionarPasta)).addPreferredGap (0).add (jLabel1).addPreferredGap (0).add (jScrollPane1, -2, 213, -2).addPreferredGap (0).add (jPanel3, -2, -1, -2).addPreferredGap (0).add (jPanel4, -1, -1, 32767).addContainerGap ()));
  }
  
  private void btnSelecionarPastaActionPerformed (ActionEvent evt)
  {
    ProcessoSwing.executa (new Tarefa ()
    {
      public Object run ()
      {
	JFileChooser fc = UtilitariosGUI.setFileChooserProperties ("Grava\u00e7\u00e3o de Declara\u00e7\u00e3o para Entrega \u00e0 SRF", "Selecionar", "Gravar", "Gravar C\u00f3pia de Seguran\u00e7a");
	fc.setApproveButtonText ("Gravar");
	fc.setMultiSelectionEnabled (false);
	fc.setAcceptAllFileFilterUsed (false);
	fc.setFileSelectionMode (1);
	int retorno = fc.showOpenDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal ());
	if (retorno == 0)
	  {
	    dirSelecionado = fc.getSelectedFile ();
	    PainelGravarEntrega.this.atualizaTextoDirSelecionado ();
	  }
	return null;
      }
    });
  }
  
  private void btnGravarActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    ProcessoSwing.executa (new Tarefa ()
    {
      public Object run ()
      {
	ExecutarGravarIdDeclaracao executarGravarIdDeclaracao = new ExecutarGravarIdDeclaracao (dirSelecionado);
	executarGravarIdDeclaracao.gravar ();
	return null;
      }
    });
  }
  
  private void btnCancelarActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
}
