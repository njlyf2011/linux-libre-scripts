/* PainelImprimirRecibo - Decompiled by JODE
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
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Vector;

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

import serpro.ppgd.formatosexternos.txt.RegistroTxt;
import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.gui.UtilitariosGUI;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.ProcessoSwing;
import serpro.ppgd.infraestrutura.util.Tarefa;
import serpro.ppgd.irpf.ColecaoIdDeclaracao;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.gui.TableModelSelecionaDeclaracao;
import serpro.ppgd.irpf.gui.TableSelecionaDeclaracao;
import serpro.ppgd.irpf.impressao.ImpressaoDeclaracao;
import serpro.ppgd.irpf.txt.gravacaorestauracao.RepositorioDeclaracaoCentralTxt;
import serpro.ppgd.irpf.util.ConstantesGlobaisIRPF;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.UtilitariosString;
import serpro.ppgd.negocio.util.Validador;
import serpro.ppgd.repositorio.RepositorioException;
import serpro.receitanet.Util;

public class PainelImprimirRecibo extends JPanel
{
  private File dirSelecionado = new File (FabricaUtilitarios.getPathCompletoDirGravadas ());
  private Vector repositorios;
  private JButton btnAjuda;
  private JButton btnImprimir;
  private JButton btnSair;
  private JButton btnSelecionarPasta;
  private TableSelecionaDeclaracao edtTableDecs;
  private JLabel jLabel1;
  private JLabel jLabel6;
  private JPanel jPanel2;
  private JPanel jPanel4;
  private JScrollPane jScrollPane1;
  private JLabel lblDirSelecionado;
  
  public PainelImprimirRecibo ()
  {
    initComponents ();
    PlataformaPPGD.getPlataforma ().setHelpID (this, "CRECIBO");
    PlataformaPPGD.getPlataforma ().setHelpID (btnAjuda, "CRECIBO");
    jScrollPane1.getViewport ().setBackground (Color.WHITE);
    dirSelecionado.mkdirs ();
    edtTableDecs.addDeclaracaoSelectionListener (new ListSelectionListener ()
    {
      public void valueChanged (ListSelectionEvent e)
      {
	if (edtTableDecs.getSelectedRowCount () > 0)
	  btnImprimir.setEnabled (true);
	else
	  btnImprimir.setEnabled (false);
      }
    });
    edtTableDecs.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent e)
      {
	if (e.getClickCount () == 2 && edtTableDecs.getSelectedRowCount () > 0)
	  PainelImprimirRecibo.this.btnImprimirActionPerformed (null);
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
    FilenameFilter filterREC = new FilenameFilter ()
    {
      public boolean accept (File dir_6_, String name)
      {
	boolean retorno = Validador.validarString (name, ConstantesGlobaisIRPF.PADRAO_NOME_ARQ_RECIBO) || Validador.validarString (name, "\\d{8}.(REC|rec)");
	return retorno;
      }
    };
    File[] filesREC = dir.listFiles (filterREC);
    repositorios = new Vector ();
    ColecaoIdDeclaracao ids = new ColecaoIdDeclaracao ();
    if (filesREC != null)
      {
	RepositorioDeclaracaoCentralTxt repositorioRecibo = null;
	RegistroTxt registroRecibo = null;
	RegistroTxt headerDec = null;
	String cpfDec = null;
	String controleSRFcodificado = null;
	for (int i = 0; i < filesREC.length; i++)
	  {
	    String nomeFileREC = filesREC[i].getName ();
	    try
	      {
		repositorioRecibo = new RepositorioDeclaracaoCentralTxt ("ARQ_COMPLRECIBO", new File (path + File.separator + nomeFileREC));
		registroRecibo = repositorioRecibo.recuperarRegistroComplementoRecibo ();
		cpfDec = registroRecibo.fieldByName ("NR_CPF").asString ();
		controleSRFcodificado = registroRecibo.fieldByName ("CONTROLE_SRF").asString ();
		if (cpfDec == null || cpfDec.trim ().length () == 0)
		  JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "Imprimir recibo - Erro ao ler arquivo do recibo.", "Imprimir Recibo", 0);
	      }
	    catch (GeracaoTxtException e)
	      {
		e.printStackTrace ();
		JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "<HTML>O Arquivo<BR>" + repositorioRecibo.getPath () + "<BR>est\u00e1 corrompido:<BR>" + e.getMessage () + "</HTML>", "Imprimir Recibo", 0);
	      }
	    catch (IOException e)
	      {
		e.printStackTrace ();
		JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "<HTML>Ocorreu um erro inesperado:<BR>" + e.getMessage () + "</HTML>", "Imprimir Recibo", 0);
	      }
	    File fileDEC = getArquivoDec (path, dir, nomeFileREC);
	    if (fileDEC.exists ())
	      {
		try
		  {
		    RepositorioDeclaracaoCentralTxt repDeclaracao = new RepositorioDeclaracaoCentralTxt ("ARQ_IRPF", fileDEC);
		    headerDec = repDeclaracao.recuperarRegistroHeader ();
		    if (controleSRFBate (headerDec, controleSRFcodificado))
		      {
			repDeclaracao.validarDeclaracaoNaoPersistido ();
			ids.recuperarLista ().add (repDeclaracao.recuperarIdDeclaracaoNaoPersistido ());
			repositorios.add (repDeclaracao);
		      }
		  }
		catch (IllegalArgumentException e)
		  {
		    JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "<HTML>Ocorreu um erro inesperado:<BR>" + e.getMessage () + "</HTML>", "Imprimir Recibo", 0);
		  }
		catch (GeracaoTxtException e)
		  {
		    JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "<HTML>O Arquivo<BR>" + fileDEC.getPath () + "<BR>est\u00e1 corrompido:<BR>" + e.getMessage () + "</HTML>", "Erro", 0);
		  }
		catch (IOException e)
		  {
		    e.printStackTrace ();
		    JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "<HTML>Ocorreu um erro inesperado:<BR>" + e.getMessage () + "</HTML>", "Imprimir Recibo", 0);
		  }
	      }
	  }
      }
    return ids;
  }
  
  private boolean controleSRFBate (RegistroTxt headerDec, String controleSRFcodificado) throws GeracaoTxtException
  {
    String hash = headerDec.fieldByName ("NR_HASH").asString ();
    String controleDecodificado = Util.decodificaControleSRF (controleSRFcodificado.getBytes ());
    return hash.equals (controleDecodificado);
  }
  
  private File getArquivoDec (String path, File dir, String nomeFileRec)
  {
    String nomeFileDec = null;
    File retorno = null;
    if (nomeFileRec.length () > 12)
      {
	nomeFileDec = nomeFileRec.substring (0, nomeFileRec.length () - 4) + ".DEC";
	retorno = new File (path + File.separator + nomeFileDec);
      }
    else
      {
	FilenameFilter filterDEC = new FilenameFilter ()
	{
	  public boolean accept (File dir_9_, String name)
	  {
	    boolean retorno_10_ = Validador.validarString (name, ConstantesGlobaisIRPF.PADRAO_NOME_ARQ_DECLARACAO);
	    return retorno_10_;
	  }
	};
	File[] files = dir.listFiles (filterDEC);
	File fileDecMaisAtual = null;
	for (int i = 0; i < files.length; i++)
	  {
	    String nomeArqDecAtual = files[i].getName ();
	    if ((fileDecMaisAtual == null || fileDecMaisAtual.lastModified () <= files[i].lastModified ()) && nomeArqDecAtual.substring (0, 8).equals (nomeFileRec.subSequence (0, 8)))
	      fileDecMaisAtual = files[i];
	  }
	retorno = fileDecMaisAtual;
	if (retorno == null)
	  {
	    nomeFileDec = nomeFileRec.substring (0, nomeFileRec.length () - 4) + ".DEC";
	    retorno = new File (path + File.separator + nomeFileDec);
	  }
      }
    return retorno;
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
    btnImprimir = new JButton ();
    jLabel1.setForeground (new Color (0, 0, 128));
    jLabel1.setText ("<HTML><B>Selecione os declara\u00e7\u00f5es para imprimir o recibo:</B></HTML>");
    jLabel6.setForeground (new Color (0, 0, 128));
    jLabel6.setText ("<HTML><CENTER><B>Selecione a pasta onde est\u00e1 o recibo da declara\u00e7\u00e3o transmitida:</B></CENTER></HTML>");
    lblDirSelecionado.setText ("<HTML><B>Gravadas</B></HTML>");
    lblDirSelecionado.setBorder (BorderFactory.createBevelBorder (1));
    btnSelecionarPasta.setText ("<HTML><B>...</B></HTML>");
    btnSelecionarPasta.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelImprimirRecibo.this.btnSelecionarPastaActionPerformed (evt);
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
	PainelImprimirRecibo.this.btnSairActionPerformed (evt);
      }
    });
    btnAjuda.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/HELP.PNG")));
    btnAjuda.setMnemonic ('A');
    btnAjuda.setText ("<HTML><B><U>A</U>juda</B></HTML>");
    btnImprimir.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/impressora.png")));
    btnImprimir.setMnemonic ('I');
    btnImprimir.setText ("<HTML><B><U>I</U>mprimir</B></HTML>");
    btnImprimir.setEnabled (false);
    btnImprimir.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelImprimirRecibo.this.btnImprimirActionPerformed (evt);
      }
    });
    GroupLayout jPanel2Layout = new GroupLayout (jPanel2);
    jPanel2.setLayout (jPanel2Layout);
    jPanel2Layout.setHorizontalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().addContainerGap ().add (btnImprimir).addPreferredGap (0).add (btnSair).addPreferredGap (0).add (btnAjuda).addContainerGap (-1, 32767)));
    jPanel2Layout.linkSize (new Component[] { btnAjuda, btnImprimir, btnSair }, 1);
    jPanel2Layout.setVerticalGroup (jPanel2Layout.createParallelGroup (1).add (2, jPanel2Layout.createSequentialGroup ().addContainerGap (-1, 32767).add (jPanel2Layout.createParallelGroup (3).add (btnImprimir).add (btnSair).add (btnAjuda))));
    jPanel2Layout.linkSize (new Component[] { btnAjuda, btnImprimir, btnSair }, 2);
    jPanel4.add (jPanel2);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jPanel4, -2, 632, -2).add (jLabel6, -2, 612, -2).add (layout.createSequentialGroup ().add (lblDirSelecionado, -2, 567, -2).addPreferredGap (0).add (btnSelecionarPasta)).add (jLabel1).add (jScrollPane1, -2, 612, -2)).addContainerGap (31, 32767)));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel6).addPreferredGap (0).add (layout.createParallelGroup (3).add (lblDirSelecionado, -2, 25, -2).add (btnSelecionarPasta)).addPreferredGap (0).add (jLabel1).addPreferredGap (0).add (jScrollPane1, -2, 213, -2).addPreferredGap (0).add (jPanel4, -1, 75, 32767).addContainerGap ()));
  }
  
  private void btnImprimirActionPerformed (ActionEvent evt)
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
	      imprimirRecibo (linha);
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
	JFileChooser fc = UtilitariosGUI.setFileChooserProperties ("Selecionar a unidade para impress\u00e3o de recibo", "Selecionar", "Selecionar", "Seleciona unidade que cont\u00e9m a declara\u00e7\u00e3o transmitida");
	fc.setMultiSelectionEnabled (false);
	fc.setAcceptAllFileFilterUsed (false);
	fc.setFileSelectionMode (1);
	int retorno = fc.showOpenDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal ());
	if (retorno == 0)
	  {
	    dirSelecionado = fc.getSelectedFile ();
	    PainelImprimirRecibo.this.atualizaTextoDirSelecionado ();
	  }
	return null;
      }
    });
  }
  
  public void imprimirRecibo (int linhaSelecionada)
  {
    String msg = "";
    try
      {
	RepositorioDeclaracaoCentralTxt repDecl = (RepositorioDeclaracaoCentralTxt) repositorios.get (linhaSelecionada);
	if (repDecl != null)
	  {
	    String arqDEC = repDecl.getPath ();
	    int indiceUltimoSeparador = arqDEC.lastIndexOf (File.separator);
	    String arqREC = arqDEC.substring (0, arqDEC.length () - 4) + ".REC";
	    String cpf = null;
	    File fileDec = new File (arqDEC);
	    File fileRec = new File (arqREC);
	    if (! fileRec.exists ())
	      {
		arqREC = arqDEC.substring (0, indiceUltimoSeparador + 1);
		arqREC += cpf.substring (0, 8);
		arqREC += ".REC";
		fileRec = new File (arqREC);
	      }
	    System.out.println ("Vai imprimir RECIBO *");
	    ImpressaoDeclaracao impressao = gerarRecibo (fileDec, fileRec);
	    impressao.visualizar ();
	  }
      }
    catch (Exception e)
      {
	JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "<HTML>Ocorreu um erro inesperado:<BR>" + e.getMessage () + "</HTML>", "Imprimir Recibo", 0);
      }
  }
  
  private ImpressaoDeclaracao gerarRecibo (File arquivoDeclaracao, File arquivoRecibo) throws IOException
  {
    RepositorioDeclaracaoCentralTxt repositorioDeclaracao = null;
    RegistroTxt registroDeclaracao = null;
    RegistroTxt registroHeader = null;
    RepositorioDeclaracaoCentralTxt repositorioRecibo = null;
    RegistroTxt registroRecibo = null;
    RegistroTxt registroMulta = null;
    try
      {
	repositorioDeclaracao = new RepositorioDeclaracaoCentralTxt ("ARQ_IRPF", arquivoDeclaracao);
	registroDeclaracao = repositorioDeclaracao.recuperarRegistroRecibo ();
	IdentificadorDeclaracao idDeclaracao = repositorioDeclaracao.recuperarIdDeclaracaoNaoPersistido ();
	registroHeader = repositorioDeclaracao.recuperarRegistroHeader ();
	repositorioRecibo = new RepositorioDeclaracaoCentralTxt ("ARQ_COMPLRECIBO", arquivoRecibo);
	registroRecibo = repositorioRecibo.recuperarRegistroComplementoRecibo ();
	registroMulta = repositorioRecibo.recuperarRegistroComplementoReciboMulta ();
	System.out.println ("registro multa: " + registroMulta);
	ImpressaoDeclaracao impressao = new ImpressaoDeclaracao ();
	impressao.addImpressaoDeclaracao ("Recibo", "relRecibo.jasper", null, null);
	repositorioRecibo.validarComplementoRecibo (idDeclaracao);
	String RW001 = " ";
	System.out.println ("detalhe in aplic->" + registroRecibo.fieldByName ("IN_APLIC_TRANSMISSAO").asString ());
	System.out.println ("cod agente transmissor->" + registroRecibo.fieldByName ("COD_AG_TRANSMISSOR").asString ());
	System.out.println ("nome transmissor->" + registroRecibo.fieldByName ("APLIC_TRANSMISSAO").asString ());
	if (registroRecibo.fieldByName ("IN_APLIC_TRANSMISSAO").asString ().equals ("1") || registroRecibo.fieldByName ("APLIC_TRANSMISSAO").asString ().trim ().length () == 0)
	  RW001 = " ";
	else if (registroRecibo.fieldByName ("IN_APLIC_TRANSMISSAO").asString ().equals ("2") && registroRecibo.fieldByName ("COD_AG_TRANSMISSOR").asString ().equals ("999"))
	  RW001 = registroRecibo.fieldByName ("APLIC_TRANSMISSAO").asString ();
	else if (registroRecibo.fieldByName ("IN_APLIC_TRANSMISSAO").asString ().equals ("2") && ! registroRecibo.fieldByName ("COD_AG_TRANSMISSOR").asString ().equals ("999"))
	  RW001 = registroRecibo.fieldByName ("APLIC_TRANSMISSAO").asString () + registroRecibo.fieldByName ("COD_AG_TRANSMISSOR").asString ();
	impressao.addParametro ("exercicio", ConstantesGlobais.EXERCICIO);
	impressao.addParametro ("anobase", ConstantesGlobais.ANO_BASE);
	if (registroDeclaracao.fieldByName ("IN_COMPLETA").asBoolean ())
	  impressao.addParametro ("TITULO", "COMPLETA");
	else
	  impressao.addParametro ("TITULO", "SIMPLIFICADA");
	impressao.addParametro ("nomeContribuinte", registroDeclaracao.fieldByName ("NM_NOME").asString ());
	impressao.addParametro ("cpfContribuinte", UtilitariosString.formataCPF (registroDeclaracao.fieldByName ("NR_CPF").asString ()));
	if (registroDeclaracao.fieldByName ("SG_UF").asString ().equals ("EX"))
	  impressao.addParametro ("endereco", registroDeclaracao.fieldByName ("NM_LOGRA").asString ());
	else
	  {
	    String end = registroDeclaracao.fieldByName ("TIP_LOGRA").asString () + " " + registroDeclaracao.fieldByName ("NM_LOGRA").asString ();
	    impressao.addParametro ("endereco", end);
	  }
	impressao.addParametro ("numero", registroDeclaracao.fieldByName ("NR_NUMERO").asString ());
	impressao.addParametro ("complemento", registroDeclaracao.fieldByName ("NM_COMPLEM").asString ());
	impressao.addParametro ("bairro", registroDeclaracao.fieldByName ("NM_BAIRRO").asString ());
	impressao.addParametro ("cep", UtilitariosString.formataCEP (registroDeclaracao.fieldByName ("NR_CEP").asString ()));
	impressao.addParametro ("municipio", registroDeclaracao.fieldByName ("NM_MUNICIP").asString ());
	impressao.addParametro ("uf", registroDeclaracao.fieldByName ("SG_UF").asString ());
	String telefone = registroDeclaracao.fieldByName ("NR_TELEFONE").asString ();
	if (telefone.length () > 0)
	  telefone = "(" + registroDeclaracao.fieldByName ("NR_DDD_TELEFONE").asString () + ") " + telefone;
	impressao.addParametro ("telefone", telefone);
	if (registroDeclaracao.fieldByName ("IN_RETIFICADORA").asBoolean ())
	  impressao.addParametro ("retificadora", "SIM");
	else
	  impressao.addParametro ("retificadora", "N\u00c3O");
	impressao.addParametro ("Tributaveis", registroDeclaracao.fieldByName ("VR_TOTTRIB").asValor ().getConteudoFormatado ());
	impressao.addParametro ("devido", registroDeclaracao.fieldByName ("VR_IMPDEV").asValor ().getConteudoFormatado ());
	impressao.addParametro ("Restituir", registroDeclaracao.fieldByName ("VR_IMPREST").asValor ().getConteudoFormatado ());
	impressao.addParametro ("Especie", registroDeclaracao.fieldByName ("VR_GCIMPOSTOPAGO").asValor ().getConteudoFormatado ());
	impressao.addParametro ("Pagar", registroDeclaracao.fieldByName ("VR_IMPPAGAR").asValor ().getConteudoFormatado ());
	impressao.addParametro ("DATA", registroRecibo.fieldByName ("DIAREC").asString () + "/" + registroRecibo.fieldByName ("MESREC").asString () + "/" + registroRecibo.fieldByName ("ANOREC").asString ());
	impressao.addParametro ("HORA", registroRecibo.fieldByName ("HORAREC").asString () + ":" + registroRecibo.fieldByName ("MINREC").asString () + ":" + registroRecibo.fieldByName ("SEGREC").asString ());
	impressao.addParametro ("RW001", RW001);
	String nrRecibo = registroHeader.fieldByName ("NR_HASH").asString ();
	String dvNrRecibo = "" + Validador.calcularModulo11 (nrRecibo, null, 2);
	dvNrRecibo += Validador.calcularModulo11 (nrRecibo + dvNrRecibo, null, 2);
	String nrReciboForma = nrRecibo.substring (0, 2) + "." + nrRecibo.substring (2, 4) + "." + nrRecibo.substring (4, 6) + "." + nrRecibo.substring (6, 8) + "." + nrRecibo.substring (8, 10);
	impressao.addParametro ("HashSemDV", nrReciboForma);
	String numReciboComDV = nrReciboForma + " - " + dvNrRecibo;
	impressao.addParametro ("NUM_RECIBO_FORM_DV", numReciboComDV);
	impressao.addParametro ("ASS_RECIBO", registroRecibo.fieldByName ("ASSINATURA").asString ());
	if (registroDeclaracao.fieldByName ("VR_IMPPAGAR").asValor ().isVazio ())
	  {
	    if (! registroDeclaracao.fieldByName ("NR_BANCO").asString ().trim ().equals (""))
	      {
		impressao.addParametro ("lblRestitParcel", "RESTITUI\u00c7\u00c3O");
		impressao.addParametro ("lblBancoQuotas", "C\u00d3DIGO DO BANCO");
		impressao.addParametro ("BancoNumQuotas", registroDeclaracao.fieldByName ("NR_BANCO").asString ());
		impressao.addParametro ("AgenciaValor", "AG\u00caNCIA BANC\u00c1RIA");
		String dvAgencia = registroDeclaracao.fieldByName ("NR_DV_AGENCIA").asString ();
		String agencia = registroDeclaracao.fieldByName ("NR_AGENCIA").asString ();
		if (dvAgencia.trim ().length () > 0)
		  agencia += "-" + (String) dvAgencia;
		impressao.addParametro ("AgenciaValQuota", agencia);
		impressao.addParametro ("lblCodBancoOuCC", "CONTA PARA CR\u00c9DITO");
		String conta = registroDeclaracao.fieldByName ("NR_CONTA").asString ();
		conta += "-" + registroDeclaracao.fieldByName ("NR_DV_CONTA").asString ();
		impressao.addParametro ("codBancoOuCC", conta);
	      }
	    else
	      {
		impressao.addParametro ("lblCodBancoOuCC", "");
		impressao.addParametro ("codBancoOuCC", "");
	      }
	  }
	else
	  {
	    impressao.addParametro ("lblRestitParcel", "PARCELAMENTO (Vencimento da 1a quota em 30/04/" + ConstantesGlobais.EXERCICIO + ")");
	    impressao.addParametro ("lblBancoQuotas", "N\u00daMERO DE QUOTAS");
	    impressao.addParametro ("BancoNumQuotas", registroDeclaracao.fieldByName ("NR_QUOTAS").asString ());
	    impressao.addParametro ("AgenciaValor", "VALOR DA QUOTA");
	    impressao.addParametro ("AgenciaValQuota", registroDeclaracao.fieldByName ("VR_QUOTA").asValor ().getConteudoFormatado ());
	    if (! registroDeclaracao.fieldByName ("NR_BANCO").asString ().trim ().equals (""))
	      {
		impressao.addParametro ("comDebitoAutomatico", "true");
		impressao.addParametro ("lblCodBancoOuCC", "C\u00d3DIGO DO BANCO");
		impressao.addParametro ("codBancoOuCC", registroDeclaracao.fieldByName ("NR_BANCO").asString ());
		String dvAgencia = registroDeclaracao.fieldByName ("NR_DV_AGENCIA").asString ();
		String agencia = registroDeclaracao.fieldByName ("NR_AGENCIA").asString ();
		agencia += "-" + dvAgencia;
		impressao.addParametro ("agencia", agencia);
		String conta = registroDeclaracao.fieldByName ("NR_CONTA").asString ();
		conta += "-" + registroDeclaracao.fieldByName ("NR_DV_CONTA").asString ();
		impressao.addParametro ("conta", conta);
	      }
	    else
	      {
		impressao.addParametro ("lblCodBancoOuCC", "");
		impressao.addParametro ("codBancoOuCC", "");
	      }
	  }
	String assCertDigital = registroRecibo.fieldByName ("NI_ASSINATURA_DECL").asString ();
	if (assCertDigital != null && (assCertDigital.trim ().length () == 11 || assCertDigital.trim ().length () == 14))
	  {
	    if (assCertDigital.trim ().length () == 11)
	      assCertDigital = UtilitariosString.formataCPF (assCertDigital);
	    else
	      assCertDigital = UtilitariosString.formataCNPJ (assCertDigital);
	    impressao.addParametro ("LBL_ASS_CERT_DIGITAL", "Esta declara\u00e7\u00e3o foi assinada com o certificado digital do NI " + assCertDigital);
	  }
	else
	  impressao.addParametro ("LBL_ASS_CERT_DIGITAL", "");
	if (registroMulta != null && registroMulta.fieldByName ("IN_ACAO_FISCAL").asString ().equals ("1"))
	  impressao.addParametro ("contribSobFiscal", "Esta declara\u00e7\u00e3o est\u00e1 sendo apresentada ap\u00f3s o in\u00edcio de procedimento fiscal.\nSomente alimentar\u00e3o a base de dados da SRF as informa\u00e7\u00f5es das seguintes fichas:\n1 - IDENTIFICA\u00c7\u00c3O DO CONTRIBUINTE\n2 - BENS E DIREITOS\n3 - D\u00cdVIDAS E \u00d4NUS REAIS\nAs demais informa\u00e7\u00f5es que est\u00e3o sendo alteradas devem ser entregues \u00e0 fiscaliza\u00e7\u00e3o.");
	if (registroMulta != null && registroMulta.fieldByName ("NR_DISTRIBUICAO").asString ().trim ().length () > 0)
	  {
	    String codNotificacao = registroMulta.fieldByName ("NR_DISTRIBUICAO").asString ();
	    impressao.addParametro ("Notificacao", codNotificacao.substring (0, codNotificacao.length () - 2) + "-" + codNotificacao.substring (codNotificacao.length () - 2, codNotificacao.length ()));
	    String lblCodNotificacao = "C\u00d3DIGO DA NOTIFICA\u00c7\u00c3O DE MULTA POR ATRASO NA ENTREGA DA DECLARA\u00c7\u00c3O";
	    impressao.addParametro ("lblNotificacao", lblCodNotificacao);
	    gerarImpressaoNotificacaoMulta (arquivoDeclaracao, arquivoRecibo, impressao, registroMulta, registroDeclaracao, registroRecibo, numReciboComDV);
	  }
	else
	  {
	    impressao.addParametro ("lblNotificacao", "");
	    impressao.addParametro ("Notificacao", "");
	  }
	return impressao;
      }
    catch (RepositorioException e)
      {
	e.printStackTrace ();
	JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), e.getMessage (), "Impress&atilde;o do Recibo", 1);
      }
    catch (FileNotFoundException e)
      {
	e.printStackTrace ();
	JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), e.getMessage (), "Impress&atilde;o do Recibo", 1);
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), e.getMessage (), "Impress&atilde;o do Recibo", 1);
      }
    return null;
  }
  
  private void gerarImpressaoNotificacaoMulta (File arquivoDeclaracao, File arquivoRecibo, ImpressaoDeclaracao impressao, RegistroTxt pRegMulta, RegistroTxt pRegDec, RegistroTxt registroRecibo, String numReciboComDV) throws IOException, RepositorioException, GeracaoTxtException
  {
    impressao.addImpressaoDeclaracao ("Notifica\u00e7\u00e3o de Lan\u00e7amento", "relNotificacao.jasper", null, null);
    Valor tempoAtraso = pRegMulta.fieldByName ("QT_MESES").asValor ();
    impressao.addParametroUltimo ("DtEntregaNot", registroRecibo.fieldByName ("DIAREC").asString () + "/" + registroRecibo.fieldByName ("MESREC").asString () + "/" + registroRecibo.fieldByName ("ANOREC").asString ());
    impressao.addParametroUltimo ("HrEntregaNot", registroRecibo.fieldByName ("HORAREC").asString () + ":" + registroRecibo.fieldByName ("MINREC").asString () + ":" + registroRecibo.fieldByName ("SEGREC").asString ());
    impressao.addParametroUltimo ("MesesNot", pRegMulta.fieldByName ("QT_MESES").asString ());
    impressao.addParametroUltimo ("impDev", pRegDec.fieldByName ("VR_IMPDEV").asValor ().getConteudoFormatado ());
    if (tempoAtraso.comparacao (">", "20"))
      tempoAtraso.setConteudo ("20");
    impressao.addParametroUltimo ("tempoAtrasoMax", tempoAtraso.getConteudoFormatado ());
    Valor impostoDevido = pRegDec.fieldByName ("VR_IMPDEV").asValor ();
    Valor valorCalculado = impostoDevido.operacao ('*', tempoAtraso);
    valorCalculado.append ('/', "100,00");
    impressao.addParametroUltimo ("MultaCalc", valorCalculado.getConteudoFormatado ());
    impressao.addParametro ("EXERCICIO", ConstantesGlobais.EXERCICIO);
    impressao.addParametro ("ANO_CALENDARIO", ConstantesGlobais.ANO_BASE);
    impressao.addParametro ("ReciboNot", numReciboComDV);
    String strCodNot = pRegMulta.fieldByName ("NR_DISTRIBUICAO").asString ();
    if (strCodNot != null && strCodNot.length () > 2)
      strCodNot = strCodNot.substring (0, strCodNot.length () - 2) + "-" + strCodNot.substring (strCodNot.length () - 2);
    impressao.addParametro ("CodNot", strCodNot);
    impressao.addParametro ("MunicNot", pRegDec.fieldByName ("NM_MUNICIP").asString ());
    Valor valMultaFixa = new Valor ();
    valMultaFixa.setConteudo (pRegMulta.fieldByName ("VR_MULTA").asValor ());
    impressao.addParametroUltimo ("MultaFixa", valMultaFixa.getConteudoFormatado ());
    impressao.addParametroUltimo ("Multa", valMultaFixa.getConteudoFormatado ());
    impressao.addParametroUltimo ("CONSTANTE_VAL_MULTA", ConstantesGlobais.MULTA_POR_ATRASO_ENTREGA.getConteudoFormatado ());
    impressao.addParametroUltimo ("NOME_AUDITOR_TECNICO", pRegMulta.fieldByName ("NM_DELEGADO").asString ());
    int cargo = pRegMulta.fieldByName ("NR_CARGO").asInteger ();
    if (cargo == 1)
      impressao.addParametroUltimo ("CARGO", "AUDITOR FISCAL DA RECEITA FEDERAL");
    else if (cargo == 2)
      impressao.addParametroUltimo ("CARGO", "T\u00c9CNICO DA RECEITA FEDERAL");
    impressao.addParametroUltimo ("MATRIC_AUDITOR_TECNICO", pRegMulta.fieldByName ("NR_MATRIC_DELEGADO").asString ());
    String nomeDelegacia = "";
    String codDelegacia = pRegMulta.fieldByName ("NR_UA").asString ();
    nomeDelegacia = pRegMulta.fieldByName ("TP_DELEGACIA").asString ();
    nomeDelegacia += " " + pRegMulta.fieldByName ("NM_UA").asString ();
    impressao.addParametroUltimo ("NOME_DELEGACIA", nomeDelegacia);
  }
}
