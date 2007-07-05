/* PainelTransmitirDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import serpro.ppgd.irpf.ColecaoIdDeclaracao;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.gui.TableModelSelecionaDeclaracao;
import serpro.ppgd.irpf.gui.TableSelecionaDeclaracao;
import serpro.ppgd.irpf.txt.gravacaorestauracao.RepositorioDeclaracaoCentralTxt;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.UtilitariosArquivo;
import serpro.ppgd.negocio.util.UtilitariosString;
import serpro.ppgd.negocio.util.Validador;
import serpro.receitanet.Receitanet;
import serpro.receitanet.carregador.Carregador;

public class PainelTransmitirDeclaracao extends JPanel
{
  private static final String LABEL_TITULO = "Transmitir declara\u00e7\u00e3o";
  private static Receitanet recnet = null;
  private static final String TITULO_DEC_JA_TRANSMITIDA = "Erro - Declara\u00e7\u00e3o j\u00e1 transmitida";
  private static final String MSG_DEC_JA_TRANSMITIDA = "Esta declara\u00e7\u00e3o j\u00e1 foi transmitida com sucesso";
  private boolean receitanetOk = true;
  private File dirSelecionado = new File (FabricaUtilitarios.getPathCompletoDirGravadas ());
  private Vector repositorios;
  private JButton btnAjuda;
  private JButton btnSair;
  private JButton btnSelecionarPasta;
  private JButton btnTransmitir;
  private JCheckBox chkCertificacaoDigital;
  private TableSelecionaDeclaracao edtTableDecs;
  private JLabel jLabel1;
  private JLabel jLabel6;
  private JPanel jPanel2;
  private JPanel jPanel3;
  private JPanel jPanel4;
  private JScrollPane jScrollPane1;
  private JLabel lblDirSelecionado;
  
  public static void exibeMsgReceitanetNaoInstalado ()
  {
    String msg = "<HTML>Para entregar a declara\u00e7\u00e3o preenchida por meio do programa IRPF" + ConstantesGlobais.EXERCICIO + " Java, \u00e9 necess\u00e1rio o Receitanet Java. <BR>" + "Adote os seguintes procedimentos: <BR>" + "1) Na p\u00e1gina da Receita Federal na internet, fa\u00e7a download e instale o programa Receitanet Java, vers\u00e3o mais atualizada; <BR>" + "2) Transmita a declara\u00e7\u00e3o IRPF" + ConstantesGlobais.EXERCICIO + ".</HTML>";
    JLabel lblMsg = new JLabel (msg);
    JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), lblMsg, "Transmitir declara\u00e7\u00e3o", 0);
  }
  
  public static boolean verificaReceitanetOk ()
  {
    try
      {
	if (recnet == null)
	  recnet = Carregador.carregar ();
	return true;
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	recnet = null;
	return false;
      }
  }
  
  public PainelTransmitirDeclaracao ()
  {
    initComponents ();
    PlataformaPPGD.getPlataforma ().setHelpID (this, "CInternet");
    PlataformaPPGD.getPlataforma ().setHelpID (btnAjuda, "CInternet");
    jScrollPane1.getViewport ().setBackground (Color.WHITE);
    dirSelecionado.mkdirs ();
    edtTableDecs.addDeclaracaoSelectionListener (new ListSelectionListener ()
    {
      public void valueChanged (ListSelectionEvent e)
      {
	if (edtTableDecs.getSelectedRowCount () > 0)
	  btnTransmitir.setEnabled (true);
	else
	  btnTransmitir.setEnabled (false);
      }
    });
    edtTableDecs.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent e)
      {
	if (e.getClickCount () == 2 && edtTableDecs.getSelectedRowCount () > 0)
	  PainelTransmitirDeclaracao.this.btnTransmitirActionPerformed (null);
      }
    });
    atualizaTextoDirSelecionado ();
  }
  
  private void atualizaTextoDirSelecionado ()
  {
    String caminhoGravacao = dirSelecionado.getPath ();
    if (System.getProperty ("os.name").startsWith ("Windows"))
      caminhoGravacao = caminhoGravacao.replaceFirst ("/", "");
    lblDirSelecionado.setText ("<HTML><B>" + caminhoGravacao + "</B></HTML>");
    ((TableModelSelecionaDeclaracao) edtTableDecs.getModel ()).setColecaoIdDeclaracao (obtemListaDeclaracoesGravadas (dirSelecionado.getPath ()));
  }
  
  private ColecaoIdDeclaracao obtemListaDeclaracoesGravadas (String path)
  {
    File dir = new File (path);
    FilenameFilter filter = new FilenameFilter ()
    {
      public boolean accept (File dir_6_, String name)
      {
	return name.endsWith (".DEC");
      }
    };
    File[] files = dir.listFiles (filter);
    repositorios = new Vector ();
    ColecaoIdDeclaracao ids = new ColecaoIdDeclaracao ();
    if (files != null)
      {
	for (int i = 0; i < files.length; i++)
	  {
	    RepositorioDeclaracaoCentralTxt repDeclaracao = null;
	    try
	      {
		repDeclaracao = new RepositorioDeclaracaoCentralTxt ("ARQ_IRPF", files[i]);
		repDeclaracao.validarDeclaracaoNaoPersistido ();
		ids.recuperarLista ().add (repDeclaracao.recuperarIdDeclaracaoNaoPersistido ());
		repositorios.add (repDeclaracao);
	      }
	    catch (GeracaoTxtException e)
	      {
		e.printStackTrace ();
		JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "<HTML>O Arquivo<BR>" + repDeclaracao.getPath () + "<BR>est\u00e1 corrompido:<BR>" + e.getMessage () + "</HTML>", "Erro", 0);
	      }
	    catch (IOException e)
	      {
		e.printStackTrace ();
		JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), e.getMessage (), "Erro", 0);
	      }
	  }
      }
    return ids;
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jLabel6 = new JLabel ();
    lblDirSelecionado = new JLabel ();
    btnSelecionarPasta = new JButton ();
    jScrollPane1 = new JScrollPane ();
    edtTableDecs = new TableSelecionaDeclaracao ();
    jPanel4 = new JPanel ();
    jPanel2 = new JPanel ();
    btnSair = new JButton ();
    btnAjuda = new JButton ();
    btnTransmitir = new JButton ();
    jPanel3 = new JPanel ();
    chkCertificacaoDigital = new JCheckBox ();
    jLabel1.setForeground (new Color (0, 0, 128));
    jLabel1.setText ("<HTML><B>Selecione o CPF da Declara\u00e7\u00e3o a ser transmitida:</B></HTML>");
    jLabel6.setForeground (new Color (0, 0, 128));
    jLabel6.setText ("<HTML><CENTER><B>Selecione a pasta onde est\u00e1 declara\u00e7\u00e3o a ser transmitida:</B></CENTER></HTML>");
    lblDirSelecionado.setText ("<HTML><B>Gravadas</B></HTML>");
    lblDirSelecionado.setBorder (BorderFactory.createBevelBorder (1));
    btnSelecionarPasta.setText ("<HTML><B>...</B></HTML>");
    btnSelecionarPasta.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelTransmitirDeclaracao.this.btnSelecionarPastaActionPerformed (evt);
      }
    });
    edtTableDecs.setAutoResizeMode (0);
    jScrollPane1.setViewportView (edtTableDecs);
    btnSair.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/CANCELAR.PNG")));
    btnSair.setMnemonic ('S');
    btnSair.setText ("<HTML><B><U>S</U>air</B></HTML>");
    btnSair.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelTransmitirDeclaracao.this.btnSairActionPerformed (evt);
      }
    });
    btnAjuda.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/HELP.PNG")));
    btnAjuda.setMnemonic ('A');
    btnAjuda.setText ("<HTML><B><U>A</U>juda</B></HTML>");
    btnTransmitir.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/OK.PNG")));
    btnTransmitir.setMnemonic ('T');
    btnTransmitir.setText ("<HTML><B><U>T</U>ransmitir</B></HTML>");
    btnTransmitir.setEnabled (false);
    btnTransmitir.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelTransmitirDeclaracao.this.btnTransmitirActionPerformed (evt);
      }
    });
    GroupLayout jPanel2Layout = new GroupLayout (jPanel2);
    jPanel2.setLayout (jPanel2Layout);
    jPanel2Layout.setHorizontalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().addContainerGap ().add (btnTransmitir).addPreferredGap (0).add (btnSair).addPreferredGap (0).add (btnAjuda).addContainerGap (-1, 32767)));
    jPanel2Layout.setVerticalGroup (jPanel2Layout.createParallelGroup (1).add (2, jPanel2Layout.createSequentialGroup ().addContainerGap (-1, 32767).add (jPanel2Layout.createParallelGroup (3).add (btnTransmitir).add (btnSair).add (btnAjuda))));
    jPanel4.add (jPanel2);
    chkCertificacaoDigital.setText ("<HTML><B>Transmitir com certifica\u00e7\u00e3o Digital</B></HTML>");
    chkCertificacaoDigital.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkCertificacaoDigital.setMargin (new Insets (0, 0, 0, 0));
    jPanel3.add (chkCertificacaoDigital);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jLabel6, -2, 612, -2).add (layout.createSequentialGroup ().add (lblDirSelecionado, -2, 567, -2).addPreferredGap (0).add (btnSelecionarPasta, -1, 63, 32767)).add (jLabel1).add (jScrollPane1, -1, 636, 32767).add (jPanel3, -1, 636, 32767).add (2, jPanel4, -1, 636, 32767)).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel6).addPreferredGap (0).add (layout.createParallelGroup (3).add (lblDirSelecionado, -2, 25, -2).add (btnSelecionarPasta)).addPreferredGap (0).add (jLabel1).addPreferredGap (0).add (jScrollPane1, -2, 213, -2).addPreferredGap (0).add (jPanel3, -2, -1, -2).addPreferredGap (0).add (jPanel4, -2, 48, -2).addContainerGap (27, 32767)));
  }
  
  private void btnTransmitirActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    ProcessoSwing.executa (new Tarefa ()
    {
      public Object run ()
      {
	int[] rows = edtTableDecs.getSelectedRows ();
	for (int i = 0; i < rows.length; i++)
	  {
	    int linha = rows[i];
	    if (linha != -1)
	      transmitirDeclaracao (linha);
	  }
	return null;
      }
    });
  }
  
  private void btnSairActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
  
  private void btnSelecionarPastaActionPerformed (ActionEvent evt)
  {
    ProcessoSwing.executa (new Tarefa ()
    {
      public Object run ()
      {
	JFileChooser fc = UtilitariosGUI.setFileChooserProperties ("Selecionar a unidade de transmiss\u00e3o", "Selecionar", "Selecionar", "Seleciona unidade que cont\u00e9m declara\u00e7\u00f5es a transmitir");
	fc.setMultiSelectionEnabled (false);
	fc.setAcceptAllFileFilterUsed (false);
	fc.setFileSelectionMode (1);
	int retorno = fc.showOpenDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal ());
	if (retorno == 0)
	  {
	    dirSelecionado = fc.getSelectedFile ();
	    PainelTransmitirDeclaracao.this.atualizaTextoDirSelecionado ();
	  }
	return null;
      }
    });
  }
  
  public void transmitirDeclaracao (int linhaSelecionada)
  {
    StringBuffer msg = new StringBuffer ();
    RepositorioDeclaracaoCentralTxt repDecl = (RepositorioDeclaracaoCentralTxt) repositorios.get (linhaSelecionada);
    try
      {
	if (repDecl != null)
	  {
	    String arquivoREC = repDecl.getPath ().substring (0, repDecl.getPath ().length () - 4) + ".REC";
	    String arquivoRECSimples = arquivoREC.substring (0, arquivoREC.lastIndexOf (File.separator) + 9) + ".REC";
	    File fileArquivoREC = new File (arquivoREC);
	    File fileArquivoRECSimples = new File (arquivoRECSimples);
	    boolean comCertif = chkCertificacaoDigital.isSelected ();
	    if (recnet == null)
	      exibeMsgReceitanetNaoInstalado ();
	    else
	      {
		int retornoReceitaNet = recnet.enviarDeclaracao (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), new File (repDecl.getPath ()), comCertif, msg);
		if (retornoReceitaNet == 0 || retornoReceitaNet == 2)
		  {
		    String nrRecibo = repDecl.recuperarNroRecibo ();
		    repDecl.atualizarNroReciboTransmitida (nrRecibo);
		    IdentificadorDeclaracao idTransm = edtTableDecs.getIdentificadorDeclaracao (linhaSelecionada);
		    idTransm.getNumReciboDecRetif ().setConteudo (nrRecibo);
		    if (IRPFFacade.existeDeclaracao (idTransm.getCpf ().asString ()))
		      {
			IdentificadorDeclaracao idPersistido = IRPFFacade.getInstancia ().recuperarIdDeclaracao (idTransm.getCpf ().asString ());
			idPersistido.getNumReciboDecRetif ().setConteudo (nrRecibo);
		      }
		    String dvNrRecibo = "" + Validador.calcularModulo11 (nrRecibo, null, 2);
		    dvNrRecibo += Validador.calcularModulo11 (nrRecibo + dvNrRecibo, null, 2);
		    nrRecibo = "\n Nr. Recibo: " + nrRecibo + "-" + dvNrRecibo;
		    String path = repDecl.getPath ();
		    String nomeFileDEC = path;
		    String nomeFileREC = path.substring (0, path.length () - 4) + ".REC";
		    path = FabricaUtilitarios.getPathCompletoDirTransmitidas ();
		    UtilitariosArquivo.copiaArquivo (nomeFileDEC, path);
		    UtilitariosArquivo.copiaArquivo (nomeFileREC, path);
		    String mensagemDialog = null;
		    if (retornoReceitaNet == 0)
		      mensagemDialog = UtilitariosString.insereQuebraDeLinha (msg.toString (), 100, "<BR>") + "<BR>\u00c9 fundamental a impress\u00e3o do recibo como prova da entrega da declara\u00e7\u00e3o." + nrRecibo;
		    else
		      mensagemDialog = UtilitariosString.insereQuebraDeLinha (msg.toString (), 100, "<BR>");
		    JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), new JLabel ("<HTML>" + mensagemDialog + "</HTML>"), "Informa\u00e7\u00e3o", 1);
		    String arqDEC = repDecl.getPath ();
		    String arqREC = arqDEC.substring (0, arqDEC.length () - 4) + ".REC";
		  }
		else if (retornoReceitaNet == 1)
		  JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), new JLabel ("<HTML>" + UtilitariosString.insereQuebraDeLinha (msg.toString (), 100, "<BR>") + "</HTML>"), "Transmitir declara\u00e7\u00e3o", 0);
	      }
	  }
      }
    catch (Exception e)
      {
	JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), new JLabel ("<HTML>" + UtilitariosString.insereQuebraDeLinha (msg.toString (), 100, "<BR>") + "</HTML>"), "Transmitir declara\u00e7\u00e3o", 0);
      }
  }
  
  public void setReceitanetOk (boolean receitanetOk)
  {
    this.receitanetOk = receitanetOk;
  }
  
  public boolean isReceitanetOk ()
  {
    return receitanetOk;
  }
}
