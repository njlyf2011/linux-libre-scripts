/* GravarCopiaTransmitida - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import serpro.ppgd.formatosexternos.txt.RegistroTxt;
import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.gui.UtilitariosGUI;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.ColecaoIdDeclaracao;
import serpro.ppgd.irpf.gui.TableModelSelecionaDeclaracao;
import serpro.ppgd.irpf.gui.TableSelecionaDeclaracao;
import serpro.ppgd.irpf.txt.gravacaorestauracao.RepositorioDeclaracaoCentralTxt;
import serpro.ppgd.irpf.util.ConstantesGlobaisIRPF;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.UtilitariosArquivo;
import serpro.ppgd.negocio.util.Validador;

public class GravarCopiaTransmitida
{
  private final String DICAS_COPIAR = "AjusteAnual.Dicas.Copiar";
  private final String LABEL_ABRIR_DECLARACAO = "GRAVAR C\u00d3PIA DE SEGURAN\u00c7A";
  private final String LABEL_SELECIONAR_UNIDADE = "Selecionar Unidade de Grava\u00e7\u00e3o";
  private Vector repositorios;
  private TableSelecionaDeclaracao edtTableDecs;
  private TableModelSelecionaDeclaracao tbmodel;
  private String caminhoPadrao;
  
  public GravarCopiaTransmitida (TableSelecionaDeclaracao _tb)
  {
    edtTableDecs = _tb;
    tbmodel = (TableModelSelecionaDeclaracao) edtTableDecs.getModel ();
    caminhoPadrao = FabricaUtilitarios.getPathCompletoDirGravadas ();
    try
      {
	tbmodel.setColecaoIdDeclaracao (obtemListaDeclaracoesGravadas (caminhoPadrao));
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  private ColecaoIdDeclaracao obtemListaDeclaracoesGravadas (String path) throws IOException
  {
    File dir = new File (path);
    FilenameFilter filterREC = new FilenameFilter ()
    {
      public boolean accept (File dir_2_, String name)
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
	  public boolean accept (File dir_5_, String name)
	  {
	    boolean retorno_6_ = Validador.validarString (name, ConstantesGlobaisIRPF.PADRAO_NOME_ARQ_DECLARACAO);
	    return retorno_6_;
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
  
  private String selecionaPath ()
  {
    JFileChooser fc = UtilitariosGUI.setFileChooserProperties ("Grava\u00e7\u00e3o de C\u00f3pia de Seguran\u00e7a", "Gravar em:", "Gravar", "Gravar C\u00f3pia de Seguran\u00e7a");
    fc.setApproveButtonText ("Gravar");
    fc.setMultiSelectionEnabled (false);
    fc.setDialogType (1);
    fc.setAcceptAllFileFilterUsed (false);
    fc.setFileSelectionMode (1);
    int retorno = fc.showOpenDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal ());
    if (retorno == 0)
      return fc.getSelectedFile ().getPath ();
    return "";
  }
  
  public void gravarCopia (int linhaSelecionada)
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
	String pathDest = selecionaPath ();
	UtilitariosArquivo.copiaArquivo (fileDec.toString (), pathDest);
	UtilitariosArquivo.copiaArquivo (fileRec.toString (), pathDest);
      }
  }
  
  public void gravarCopia ()
  {
    /* empty */
  }
}
