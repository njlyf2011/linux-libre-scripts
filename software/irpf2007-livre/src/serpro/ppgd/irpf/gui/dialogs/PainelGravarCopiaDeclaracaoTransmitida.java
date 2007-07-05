/* PainelGravarCopiaDeclaracaoTransmitida - Decompiled by JODE
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
import serpro.ppgd.irpf.txt.gravacaorestauracao.RepositorioDeclaracaoCentralTxt;
import serpro.ppgd.irpf.util.ConstantesGlobaisIRPF;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.UtilitariosArquivo;
import serpro.ppgd.negocio.util.Validador;

public class PainelGravarCopiaDeclaracaoTransmitida extends JPanel
{
  private Vector repositorios;
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
      if (rows.length > 0)
	{
	  path = dir.getPath ();
	  boolean emiteMsgSucesso = false;
	  for (int i = 0; i < rows.length; i++)
	    {
	      serpro.ppgd.irpf.IdentificadorDeclaracao idAtual = tableModel.getIdentificadorDeclaracao (rows[i]);
	      if (path != "")
		{
		  gravarCopia (i, path);
		  emiteMsgSucesso = true;
		}
	    }
	  if (emiteMsgSucesso)
	    JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "Grava\u00e7\u00e3o conclu\u00edda com sucesso.", "Informa\u00e7\u00e3o", 1);
	  else
	    JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "Grava\u00e7\u00e3o cancelada pelo usu\u00e1rio.", "Informa\u00e7\u00e3o", 1);
	}
    }
  }
  
  public PainelGravarCopiaDeclaracaoTransmitida ()
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
	  PainelGravarCopiaDeclaracaoTransmitida.this.btnAvancarActionPerformed (null);
      }
    });
    ((TableModelSelecionaDeclaracao) edtTableDecs.getModel ()).setColecaoIdDeclaracao (obtemListaDeclaracoesGravadas (FabricaUtilitarios.getPathCompletoDirGravadas ()));
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
    jLabel1.setForeground (new Color (0, 0, 128));
    jLabel1.setText ("<HTML><B>Selecione as declara\u00e7\u00f5es a serem copiadas e clique no bot\u00e3o Avan\u00e7ar</B></HTML>");
    btnCancelar.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/CANCELAR.PNG")));
    btnCancelar.setMnemonic ('C');
    btnCancelar.setText ("<HTML><B><U>C</U>ancelar</B></HTML>");
    btnCancelar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelGravarCopiaDeclaracaoTransmitida.this.btnCancelarActionPerformed (evt);
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
	PainelGravarCopiaDeclaracaoTransmitida.this.btnAvancarActionPerformed (evt);
      }
    });
    GroupLayout jPanel2Layout = new GroupLayout (jPanel2);
    jPanel2.setLayout (jPanel2Layout);
    jPanel2Layout.setHorizontalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().addContainerGap ().add (jPanel2Layout.createParallelGroup (1).add (btnAjuda).add (btnCancelar).add (btnAvancar)).addContainerGap (34, 32767)));
    jPanel2Layout.linkSize (new Component[] { btnAjuda, btnAvancar, btnCancelar }, 1);
    jPanel2Layout.setVerticalGroup (jPanel2Layout.createParallelGroup (1).add (2, jPanel2Layout.createSequentialGroup ().addContainerGap (201, 32767).add (btnAvancar).addPreferredGap (0).add (btnCancelar).addPreferredGap (0).add (btnAjuda).addContainerGap ()));
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
    jPanel3Layout.setHorizontalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().addContainerGap ().add (jLabel2, -2, 89, -2).addPreferredGap (0).add (jLabel3, -2, 105, -2).addPreferredGap (0).add (jLabel4, -2, 91, -2).addPreferredGap (0).add (jLabel5).addContainerGap (260, 32767)));
    jPanel3Layout.setVerticalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().addContainerGap ().add (jPanel3Layout.createParallelGroup (3).add (jLabel2).add (jLabel3).add (jLabel5).add (jLabel4)).addContainerGap (16, 32767)));
    edtTableDecs.setAutoResizeMode (0);
    jScrollPane1.setViewportView (edtTableDecs);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (2, layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (2).add (1, jPanel3, -1, -1, 32767).add (layout.createSequentialGroup ().add (jScrollPane1, -1, 502, 32767).addPreferredGap (0).add (jPanel2, -2, -1, -2)).add (1, jLabel1)).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel1).addPreferredGap (0).add (layout.createParallelGroup (1).add (jScrollPane1, -1, 303, 32767).add (jPanel2, -1, -1, 32767)).addPreferredGap (0).add (jPanel3, -2, -1, -2)));
  }
  
  private void btnAvancarActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    ProcessoSwing.executa (new Tarefa ()
    {
      public Object run ()
      {
	JFileChooser fc = UtilitariosGUI.setFileChooserProperties ("Grava\u00e7\u00e3o de C\u00f3pia de Seguran\u00e7a", "Procurar em:", "Gravar", "Gravar C\u00f3pia de Seguran\u00e7a");
	fc.setApproveButtonText ("Gravar");
	fc.setMultiSelectionEnabled (false);
	fc.setDialogType (1);
	fc.setAcceptAllFileFilterUsed (false);
	fc.setFileSelectionMode (1);
	int retorno = fc.showOpenDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal ());
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
  
  private ColecaoIdDeclaracao obtemListaDeclaracoesGravadas (String path)
  {
    try
      {
	File dir = new File (path);
	FilenameFilter filterREC = new FilenameFilter ()
	{
	  public boolean accept (File dir_12_, String name)
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
	    for (int i = 0; i < filesREC.length; i++)
	      {
		String nomeFileREC = filesREC[i].getName ();
		try
		  {
		    repositorioRecibo = new RepositorioDeclaracaoCentralTxt ("ARQ_COMPLRECIBO", new File (path + File.separator + nomeFileREC));
		    registroRecibo = repositorioRecibo.recuperarRegistroComplementoRecibo ();
		    cpfDec = registroRecibo.fieldByName ("NR_CPF").asString ();
		    if (cpfDec == null || cpfDec.trim ().length () == 0)
		      JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "Erro ao ler arquivo .DEC", "Erro", 0);
		  }
		catch (GeracaoTxtException e)
		  {
		    e.printStackTrace ();
		    JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), e.getMessage (), "Erro", 0);
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
			e.printStackTrace ();
			JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), e.getMessage (), "Erro", 0);
		      }
		    catch (GeracaoTxtException e)
		      {
			e.printStackTrace ();
			JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), e.getMessage (), "Erro", 0);
		      }
		  }
	      }
	  }
	return ids;
      }
    catch (IOException ioe)
      {
	ioe.printStackTrace ();
	JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "Erro ao acessar o diret\u00f3rio Gravadas: " + ioe.getMessage (), "Erro", 0);
	return new ColecaoIdDeclaracao ();
      }
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
	  public boolean accept (File dir_15_, String name)
	  {
	    boolean retorno_16_ = Validador.validarString (name, ConstantesGlobaisIRPF.PADRAO_NOME_ARQ_DECLARACAO);
	    return retorno_16_;
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
  
  public void gravarCopia (int linhaSelecionada, String pathDest)
  {
    RepositorioDeclaracaoCentralTxt repDecl = (RepositorioDeclaracaoCentralTxt) repositorios.get (linhaSelecionada);
    if (repDecl != null)
      {
	String arqDEC = repDecl.getPath ();
	int indiceUltimoSeparador = arqDEC.lastIndexOf (File.separator);
	String arqREC = arqDEC.substring (0, arqDEC.length () - 4) + ".REC";
	String cpf = null;
	try
	  {
	    cpf = repDecl.recuperarIdDeclaracaoNaoPersistido ().getCpf ().asString ();
	  }
	catch (Exception e)
	  {
	    e.printStackTrace ();
	    JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), e.getMessage (), "Erro", 0);
	    return;
	  }
	File fileDec = new File (arqDEC);
	File fileRec = new File (arqREC);
	if (! fileRec.exists ())
	  {
	    arqREC = arqDEC.substring (0, indiceUltimoSeparador + 1);
	    arqREC += cpf.substring (0, 8);
	    arqREC += ".REC";
	    fileRec = new File (arqREC);
	  }
	UtilitariosArquivo.copiaArquivo (fileDec.toString (), pathDest);
	UtilitariosArquivo.copiaArquivo (fileRec.toString (), pathDest);
      }
  }
}
