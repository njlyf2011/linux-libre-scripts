/* PainelImprimirDARFMaed - Decompiled by JODE
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
import serpro.ppgd.irpf.gui.TableModelSelecionaDeclaracao;
import serpro.ppgd.irpf.gui.TableSelecionaDeclaracao;
import serpro.ppgd.irpf.impressao.ImpressaoDarfMulta;
import serpro.ppgd.irpf.txt.gravacaorestauracao.RepositorioDeclaracaoCentralTxt;
import serpro.ppgd.irpf.util.ConstantesGlobaisIRPF;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.Validador;
import serpro.receitanet.Util;

public class PainelImprimirDARFMaed extends JPanel
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
  
  public PainelImprimirDARFMaed ()
  {
    initComponents ();
    PlataformaPPGD.getPlataforma ().setHelpID (this, "CDARFMULTA");
    PlataformaPPGD.getPlataforma ().setHelpID (btnAjuda, "CDARFMULTA");
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
	  PainelImprimirDARFMaed.this.btnImprimirActionPerformed (null);
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
	String cpfDec = null;
	int i = 0;
	for (/**/; i < filesREC.length; i++)
	  {
	    String nomeFileREC = filesREC[i].getName ();
	    try
	      {
		repositorioRecibo = new RepositorioDeclaracaoCentralTxt ("ARQ_COMPLRECIBO", new File (path + File.separator + nomeFileREC));
		registroRecibo = repositorioRecibo.recuperarRegistroComplementoRecibo ();
		if (repositorioRecibo.recuperarRegistroComplementoReciboMulta () == null)
		  continue;
		cpfDec = registroRecibo.fieldByName ("NR_CPF").asString ();
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
		    repDeclaracao.validarDeclaracaoNaoPersistido ();
		    ids.recuperarLista ().add (repDeclaracao.recuperarIdDeclaracaoNaoPersistido ());
		    repositorios.add (repDeclaracao);
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
	PainelImprimirDARFMaed.this.btnSelecionarPastaActionPerformed (evt);
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
	PainelImprimirDARFMaed.this.btnSairActionPerformed (evt);
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
	PainelImprimirDARFMaed.this.btnImprimirActionPerformed (evt);
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
	    PainelImprimirDARFMaed.this.atualizaTextoDirSelecionado ();
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
	    new ImpressaoDarfMulta ("DARF de Multa", "relDarfMulta.jasper", fileDec, fileRec, true);
	  }
      }
    catch (Exception e)
      {
	JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "<HTML>Ocorreu um erro inesperado:<BR>" + e.getMessage () + "</HTML>", "Imprimir Recibo", 0);
      }
  }
}
