/* Acoes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.app.acoes;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.ConverterDeclaracao;
import serpro.ppgd.irpf.gui.ImportarAtividadeRural;
import serpro.ppgd.irpf.gui.ImportarCarneLeao;
import serpro.ppgd.irpf.gui.dialogs.PainelAbrirDeclaracao;
import serpro.ppgd.irpf.gui.dialogs.PainelExcluirDeclaracao;
import serpro.ppgd.irpf.gui.dialogs.PainelGravarCopiaSeguranca;
import serpro.ppgd.irpf.gui.dialogs.PainelGravarEntrega;
import serpro.ppgd.irpf.gui.dialogs.PainelImprimirDARF;
import serpro.ppgd.irpf.gui.dialogs.PainelImprimirDARFCodigoBarras;
import serpro.ppgd.irpf.gui.dialogs.PainelImprimirDARFMaed;
import serpro.ppgd.irpf.gui.dialogs.PainelImprimirDeclaracao;
import serpro.ppgd.irpf.gui.dialogs.PainelImprimirRecibo;
import serpro.ppgd.irpf.gui.dialogs.PainelLeaozinho;
import serpro.ppgd.irpf.gui.dialogs.PainelMemoria;
import serpro.ppgd.irpf.gui.dialogs.PainelSelecaoTipoImpressao;
import serpro.ppgd.irpf.gui.dialogs.PainelSobre;
import serpro.ppgd.irpf.gui.dialogs.PainelSugereImportacao;
import serpro.ppgd.irpf.gui.dialogs.PainelTransmitirDeclaracao;
import serpro.ppgd.irpf.gui.dialogs.PainelVerificarPendencias;
import serpro.ppgd.irpf.gui.dialogs.RecuperarExercicioAnterior;
import serpro.ppgd.irpf.gui.dialogs.RestaurarCopiaSeguranca;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class Acoes extends AbstractAction
{
  private static JLabel lblAvisoExibeTree = new JLabel (TabelaMensagens.getTabelaMensagens ().msg ("exibe_tree"));
  private static TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
  
  public boolean confirmaAcao (String msg)
  {
    if (! IRPFUtil.temDeclaracaoAberta)
      return true;
    int ret = JOptionPane.showConfirmDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), msg, "Confirma\u00e7\u00e3o", 0);
    if (ret == 0)
      {
	IRPFUtil.fecharDeclaracao ();
	return true;
      }
    return false;
  }
  
  public void actionPerformed (ActionEvent e)
  {
    String acao = e.getActionCommand ();
    if (acao.equals ("sair"))
      System.exit (1);
    else if (acao.equals ("exibe_tree"))
      {
	JToggleButton tgBtn = (JToggleButton) PlataformaPPGD.getPlataforma ().getComponent ("btnExibeTree");
	if (tgBtn.isSelected ())
	  ((JSplitPane) PlataformaPPGD.getPlataforma ().getPainelSplit ()).setDividerLocation (PgdIRPF.SPLIT_DIVIDER_LOCATION);
	else if (JOptionPane.showConfirmDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), lblAvisoExibeTree, "Informa\u00e7\u00e3o", 2) == 0)
	  ((JSplitPane) PlataformaPPGD.getPlataforma ().getPainelSplit ()).setDividerLocation (0);
	else
	  tgBtn.setSelected (true);
      }
    else if (acao.equals ("nova"))
      {
	if (confirmaAcao (tab.msg ("msg_confirma_acao_fechar_dec", new String[] { "criar uma nova declara\u00e7\u00e3o" })))
	  IRPFGuiUtil.exibeDialog (new PainelSugereImportacao (), true, tab.msg ("sugere_importacao"), false);
      }
    else if (acao.equals ("excluir"))
      {
	if (confirmaAcao (tab.msg ("msg_confirma_acao_fechar_dec", new String[] { "excluir uma declara\u00e7\u00e3o" })))
	  IRPFGuiUtil.exibeDialog (new PainelExcluirDeclaracao (), true, "Exclus\u00e3o de declara\u00e7\u00e3o", false);
      }
    else if (acao.equals ("abrir"))
      {
	if (confirmaAcao (tab.msg ("msg_confirma_acao_fechar_dec", new String[] { "abrir uma nova declara\u00e7\u00e3o" })))
	  IRPFGuiUtil.exibeDialog (new PainelAbrirDeclaracao (), true, "Abrir Declara\u00e7\u00e3o", false);
      }
    else if (acao.equals ("fechar"))
      {
	IRPFUtil.fecharDeclaracao ();
	IRPFGuiUtil.painelAtualmenteExibido = null;
      }
    else if (acao.equals ("verificar_pendencias"))
      IRPFGuiUtil.exibeDialog (new PainelVerificarPendencias (IRPFFacade.getInstancia ().getDeclaracao ()), true, "Verifica\u00e7\u00e3o de Pend\u00eancias", true);
    else if (acao.equals ("copia_gravar"))
      {
	if (confirmaAcao (tab.msg ("msg_confirma_acao", new String[] { "gravar uma c\u00f3pia de seguran\u00e7a" })))
	  IRPFGuiUtil.exibeDialog (new PainelGravarCopiaSeguranca (), true, "Informa\u00e7\u00e3o", false);
      }
    else if (acao.equals ("copia_restaurar"))
      {
	if (confirmaAcao (tab.msg ("msg_confirma_acao", new String[] { "restaurar uma c\u00f3pia de seguran\u00e7a" })))
	  new RestaurarCopiaSeguranca ().actionPerformed (e);
      }
    else if (acao.equals ("importar_ar"))
      new ImportarAtividadeRural ().actionPerformed (e);
    else if (acao.equals ("importar_cl"))
      new ImportarCarneLeao ().actionPerformed (e);
    else if (acao.equals ("converter_declaracao"))
      new ConverterDeclaracao (IRPFFacade.getInstancia ().getDeclaracao ()).actionPerformed (e);
    else if (acao.equals ("gravar_entrega"))
      {
	if (confirmaAcao (tab.msg ("msg_confirma_acao", new String[] { "gravar uma declara\u00e7\u00e3o" })))
	  IRPFGuiUtil.exibeDialog (new PainelGravarEntrega (), true, "Grava\u00e7\u00e3o de Declara\u00e7\u00e3o", false);
      }
    else if (acao.equals ("importar_anoanterior"))
      {
	if (confirmaAcao (tab.msg ("msg_confirma_acao", new String[] { "Importar dados de uma declara\u00e7\u00e3o do ano anterior" })))
	  new RecuperarExercicioAnterior ().actionPerformed (e);
      }
    else if (acao.equals ("transmitir"))
      {
	if (confirmaAcao (tab.msg ("msg_confirma_acao", new String[] { "transmitir declara\u00e7\u00e3o via internet" })))
	  {
	    if (PainelTransmitirDeclaracao.verificaReceitanetOk ())
	      IRPFGuiUtil.exibeDialog (new PainelTransmitirDeclaracao (), true, "Transmiss\u00e3o via Internet", false);
	    else
	      PainelTransmitirDeclaracao.exibeMsgReceitanetNaoInstalado ();
	  }
      }
    else if (acao.equals ("imprimir_dec"))
      {
	if (IRPFUtil.temDeclaracaoAberta)
	  {
	    IRPFFacade.salvaDeclaracaoAberta ();
	    IRPFGuiUtil.exibeDialog (new PainelSelecaoTipoImpressao (IRPFFacade.getInstancia ().getIdDeclaracaoAberto ()), true, "IRPF " + ConstantesGlobais.EXERCICIO + " Impress\u00e3o", false);
	  }
	else
	  IRPFGuiUtil.exibeDialog (new PainelImprimirDeclaracao (), true, "IRPF " + ConstantesGlobais.EXERCICIO + " Impress\u00e3o", false);
      }
    else if (acao.equals ("imprimir_recibo"))
      IRPFGuiUtil.exibeDialog (new PainelImprimirRecibo (), true, "Impress\u00e3o do Recibo", false);
    else if (acao.equals ("imprimir_darf"))
      {
	if (IRPFUtil.temDeclaracaoAberta)
	  IRPFFacade.salvaDeclaracaoAberta ();
	IRPFGuiUtil.exibeDialog (new PainelImprimirDARF (), true, "Impress\u00e3o do DARF", false);
      }
    else if (acao.equals ("imprimir_darf_codbarras"))
      IRPFGuiUtil.exibeDialog (new PainelImprimirDARFCodigoBarras (), true, "Impress\u00e3o do DARF com c\u00f3digo de barras", false);
    else if (acao.equals ("imprimir_darf_maed"))
      IRPFGuiUtil.exibeDialog (new PainelImprimirDARFMaed (), true, "Impress\u00e3o de DARF de Multa por Atraso na Entrega", false);
    else if (acao.equals ("memoria"))
      IRPFGuiUtil.exibeDialog (new PainelMemoria (), true, "IRPF " + ConstantesGlobais.EXERCICIO, false);
    else if (acao.equals ("leaozinho"))
      IRPFGuiUtil.exibeDialog (new PainelLeaozinho (), true, "IRPF " + ConstantesGlobais.EXERCICIO, false);
    else if (! acao.equals ("tutorial"))
      {
	if (acao.equals ("sobre"))
	  IRPFGuiUtil.exibeDialog (new PainelSobre (), true, "IRPF " + ConstantesGlobais.EXERCICIO, false);
	else
	  acao.equals ("ajuda");
      }
  }
}
