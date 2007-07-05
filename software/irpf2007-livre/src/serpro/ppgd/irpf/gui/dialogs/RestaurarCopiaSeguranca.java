/* RestaurarCopiaSeguranca - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import serpro.ppgd.formatosexternos.txt.RegistroTxt;
import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.gui.UtilitariosGUI;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.ProcessoSwing;
import serpro.ppgd.infraestrutura.util.Tarefa;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.txt.gravacaorestauracao.ImportadorTxt;
import serpro.ppgd.irpf.txt.gravacaorestauracao.RepositorioDeclaracaoCentralTxt;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.TabelaMensagens;
import serpro.ppgd.negocio.util.UtilitariosArquivo;
import serpro.ppgd.negocio.util.Validador;
import serpro.receitanet.Util;

public class RestaurarCopiaSeguranca extends AbstractAction
{
  class DBKFilter extends FileFilter
  {
    private final String padraoNomeCopiaSeguranca = "\\d{11}-IRPF-" + ConstantesGlobais.EXERCICIO + "-" + ConstantesGlobais.ANO_BASE + "-(ORIGI|RETIF|origi|retif).(DEC|DBK|dec|dbk)";
    
    public boolean accept (File f)
    {
      if (f.isDirectory ())
	return true;
      return Validador.validarString (f.getName (), padraoNomeCopiaSeguranca);
    }
    
    public String getDescription ()
    {
      return "Arquivos IRPF-" + ConstantesGlobais.EXERCICIO + "-" + ConstantesGlobais.ANO_BASE;
    }
  }
  
  public void actionPerformed (ActionEvent e)
  {
    ProcessoSwing.executa (new Tarefa ()
    {
      public Object run ()
      {
	RestaurarCopiaSeguranca.this.restauraCopiaSeguranca ();
	IRPFUtil.habilitaComponentesTemDeclaracao ();
	return null;
      }
    });
  }
  
  private void restauraCopiaSeguranca ()
  {
    try
      {
	TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
	JFileChooser fc = UtilitariosGUI.setFileChooserProperties ("Restaura\u00e7\u00e3o de c\u00f3pia de seguran\u00e7a de declara\u00e7\u00e3o", "Procurar em:", "Restaurar", "Restaura c\u00f3pia de seguran\u00e7a");
	fc.setAcceptAllFileFilterUsed (false);
	fc.setFileFilter (new DBKFilter ());
	fc.setMultiSelectionEnabled (true);
	if (fc.showDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), null) == 0)
	  {
	    File[] selectedFiles = fc.getSelectedFiles ();
	    for (int i = 0; i < selectedFiles.length; i++)
	      {
		ImportadorTxt importador = new ImportadorTxt ();
		IdentificadorDeclaracao idDeclaracao = importador.restaurarIdDeclaracao (selectedFiles[i]);
		boolean confirmacao = true;
		if (importador.existeDeclaracaoExercicioAtual (selectedFiles[i]))
		  {
		    if (JOptionPane.showConfirmDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), tab.msg ("copia_seg_restauracao_confirmacao", new String[] { idDeclaracao.getCpf ().getConteudoFormatado () }), "Confirma\u00e7\u00e3o", 0, 3) != 0)
		      confirmacao = false;
		    else
		      {
			IRPFFacade.excluirDeclaracao (idDeclaracao.getCpf ().asString ());
			IRPFFacade.criarDeclaracao (idDeclaracao);
		      }
		  }
		if (confirmacao)
		  {
		    importador.restaurarDeclaracao (selectedFiles[i]);
		    File fileRec = UtilitariosArquivo.getRECCorrespondente (selectedFiles[i]);
		    if (fileRec != null && controleSRFBate (selectedFiles[i], fileRec))
		      copiaDecRec (selectedFiles[i], fileRec);
		    JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), tab.msg ("copia_seg_restauracao_ok", new String[] { idDeclaracao.getNome ().asString (), idDeclaracao.getCpf ().getConteudoFormatado () }), "Informa\u00e7\u00e3o", 1);
		  }
	      }
	  }
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), e.getMessage (), "Erro", 0);
      }
  }
  
  private void copiaDecRec (File dec, File fileRec)
  {
    UtilitariosArquivo.copiaArquivo (dec.toString (), FabricaUtilitarios.getPathCompletoDirGravadas ());
    UtilitariosArquivo.copiaArquivo (fileRec.toString (), FabricaUtilitarios.getPathCompletoDirGravadas ());
    UtilitariosArquivo.copiaArquivo (dec.toString (), FabricaUtilitarios.getPathCompletoDirTransmitidas ());
    UtilitariosArquivo.copiaArquivo (fileRec.toString (), FabricaUtilitarios.getPathCompletoDirTransmitidas ());
  }
  
  private boolean controleSRFBate (File dec, File rec) throws GeracaoTxtException, IOException
  {
    RegistroTxt registroRecibo = null;
    RegistroTxt headerDec = null;
    RepositorioDeclaracaoCentralTxt repositorioRecibo = new RepositorioDeclaracaoCentralTxt ("ARQ_COMPLRECIBO", rec);
    RepositorioDeclaracaoCentralTxt repDeclaracao = new RepositorioDeclaracaoCentralTxt ("ARQ_IRPF", dec);
    registroRecibo = repositorioRecibo.recuperarRegistroComplementoRecibo ();
    headerDec = repDeclaracao.recuperarRegistroHeader ();
    String controleSRFcodificado = registroRecibo.fieldByName ("CONTROLE_SRF").asString ();
    String hash = headerDec.fieldByName ("NR_HASH").asString ();
    String controleDecodificado = Util.decodificaControleSRF (controleSRFcodificado.getBytes ());
    return hash.equals (controleDecodificado);
  }
}
