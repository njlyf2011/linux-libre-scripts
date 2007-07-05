/* IRPFUtil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.util;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import serpro.ppgd.gui.pendencia.MapeamentoInformacaoEditCampo;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.treeview.ArvoreGenerica;
import serpro.ppgd.infraestrutura.treeview.NoGenerico;
import serpro.ppgd.infraestrutura.util.PainelCacher;
import serpro.ppgd.infraestrutura.util.ProcessoSwing;
import serpro.ppgd.infraestrutura.util.Tarefa;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.gui.comparativo.PainelComparativo;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class IRPFUtil
{
  public static final int EM_PREENCHIMENTO = 0;
  public static final int EM_PENDENCIA = 1;
  private static int estadoSistema = 0;
  public static String[] listaComponentesAbrirFechar = { "btnFechar", "btnFichaAnterior", "btnFichaProxima", "btnExibeTree", "btnVerPendencias", "mnuFechar", "mnuVerPendencias", "mnuConverter" };
  private static String[] listaComponentesTemDeclaracao = { "btnAbrir", "mnuAbrir", "btnGravarEntrega", "mnuGravarEntrega", "mnuGravarCopia", "mnuImprimirDeclaracao", "mnuImprimirDarf", "popUpImprimirDeclaracao", "popUpImprimirDARF" };
  public static boolean temDeclaracaoAberta = false;
  public static String DIR_DADOS = FabricaUtilitarios.getPathCompletoDirDadosAplicacao ();
  
  static
  {
    String executandoSobJWS = System.getProperty ("ppgd.jws");
    if (executandoSobJWS != null && executandoSobJWS.trim ().equals ("true"))
      DIR_DADOS = System.getProperty ("user.home") + "/IRPF" + ConstantesGlobais.EXERCICIO + "/aplicacao/dados";
  }
  
  public static void abrirDeclaracao (final IdentificadorDeclaracao id, boolean executaProcessoSwing)
  {
    if (executaProcessoSwing)
      ProcessoSwing.executa (new Tarefa ()
      {
	public Object run ()
	{
	  PlataformaPPGD.getPlataforma ().getJanelaPrincipal ().setEnabled (false);
	  IRPFFacade.abreDeclaracao (id);
	  PlataformaPPGD.getPlataforma ().getJanelaPrincipal ().setEnabled (true);
	  return null;
	}
      });
    else
      {
	PlataformaPPGD.getPlataforma ().getJanelaPrincipal ().setEnabled (false);
	IRPFFacade.abreDeclaracao (id);
	PlataformaPPGD.getPlataforma ().getJanelaPrincipal ().setEnabled (true);
      }
    temDeclaracaoAberta = true;
    PainelCacher.getInstance ().inicia ();
    JSplitPane split = (JSplitPane) PlataformaPPGD.getPlataforma ().getComponent ("splitPPGD");
    split.setVisible (true);
    PlataformaPPGD.getPlataforma ().getJanelaPrincipal ().validate ();
    PlataformaPPGD.getPlataforma ().habilitaComponentes (listaComponentesAbrirFechar);
    PlataformaPPGD.getPlataforma ().getComponent ("mnuFichas").setVisible (true);
    PlataformaPPGD.getPlataforma ().getComponent ("mnuPreenchimento").setVisible (true);
    atualizaTituloDeclaracaoAberta ();
    atualizaTreeMenu ();
    ArvoreGenerica a = (ArvoreGenerica) PlataformaPPGD.getPlataforma ().getComponent ("ppgdarvore");
    a.selecionaNodoInicial ();
  }
  
  public static void atualizaTituloDeclaracaoAberta ()
  {
    String tit = FabricaUtilitarios.getProperties ().getProperty ("titulo");
    String tipoDec = null;
    if (IRPFFacade.getInstancia ().getIdDeclaracaoAberto ().getTipoDeclaracao ().asString ().equals ("1"))
      tipoDec = TabelaMensagens.getTabelaMensagens ().msg ("dec_simplificada");
    else
      tipoDec = TabelaMensagens.getTabelaMensagens ().msg ("dec_completa");
    PlataformaPPGD.getPlataforma ().getJanelaPrincipal ().setTitle (tit + " - " + IRPFFacade.getInstancia ().getIdDeclaracaoAberto ().getCpf ().getConteudoFormatado () + " - " + IRPFFacade.getInstancia ().getIdDeclaracaoAberto ().getNome ().getConteudoFormatado () + " - " + tipoDec);
  }
  
  public static void atualizaTreeMenu ()
  {
    TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
    boolean ehSimplificada = IRPFFacade.getInstancia ().getIdDeclaracaoAberto ().getTipoDeclaracao ().asString ().equals ("1");
    ArvoreGenerica a = (ArvoreGenerica) PlataformaPPGD.getPlataforma ().getComponent ("ppgdarvore");
    NoGenerico noSelecionado = null;
    if (a.getSelectionPath () != null && a.getSelectionPath ().getLastPathComponent () != null)
      noSelecionado = (NoGenerico) a.getSelectionPath ().getLastPathComponent ();
    if (ehSimplificada)
      a.setNodoVisivel (tab.msg ("tree_rend_tributaveis_deducoes"), false);
    else
      {
	a.setNodoVisivel (tab.msg ("tree_rend_tributaveis_deducoes"), true);
	a.setNodoVisivel (tab.msg ("tree_calculo_imposto"), false);
	a.setNodoVisivel (tab.msg ("tree_calculo_imposto"), true);
	a.setNodoVisivel (tab.msg ("tree_outras_informacoes"), false);
	a.setNodoVisivel (tab.msg ("tree_outras_informacoes"), true);
	a.setNodoVisivel (tab.msg ("tree_comparativo"), false);
	a.setNodoVisivel (tab.msg ("tree_comparativo"), true);
      }
    if (noSelecionado != null)
      {
	a.selecionarNodo (noSelecionado.getNome ());
	a.expandPath (a.encontrarObjetoNaTree (a.getVctNos ().get (noSelecionado.getNome ())));
      }
    JMenuItem menuConverter = (JMenuItem) PlataformaPPGD.getPlataforma ().getComponent ("mnuConverter");
    menuConverter.setText (ehSimplificada ? tab.msg ("opcao_converter_dec_completa") : tab.msg ("opcao_converter_dec_simplificada"));
  }
  
  public static void excluirDeclaracao (List ids)
  {
    IRPFFacade.excluirDeclaracao (ids);
  }
  
  public static void fecharDeclaracao ()
  {
    if (temDeclaracaoAberta)
      {
	DeclaracaoIRPF declaracaoirpf = IRPFFacade.getInstancia ().getDeclaracao ();
	Class[] var_classes = new Class[1];
	int i = 0;
	Class var_class = javax.swing.JPanel.class;
	var_classes[i] = var_class;
	declaracaoirpf.removeObservadores (var_classes);
	IRPFFacade.salvaDeclaracaoAberta ();
      }
    temDeclaracaoAberta = false;
    PainelCacher.getInstance ().encerra ();
    JSplitPane split = (JSplitPane) PlataformaPPGD.getPlataforma ().getComponent ("splitPPGD");
    split.setVisible (false);
    PlataformaPPGD.getPlataforma ().desabilitaComponentes (listaComponentesAbrirFechar);
    PlataformaPPGD.getPlataforma ().getComponent ("mnuFichas").setVisible (false);
    PlataformaPPGD.getPlataforma ().getComponent ("mnuPreenchimento").setVisible (false);
    String tit = FabricaUtilitarios.getProperties ().getProperty ("titulo");
    PlataformaPPGD.getPlataforma ().getJanelaPrincipal ().setTitle (tit);
    MapeamentoInformacaoEditCampo.limpaAssociacoes ();
    IRPFFacade.fechaDeclaracao ();
  }
  
  public static void habilitaComponentesAbrirFechar ()
  {
    PlataformaPPGD.getPlataforma ().habilitaComponentes (listaComponentesAbrirFechar);
  }
  
  public static void habilitaComponentesTemDeclaracao ()
  {
    if (IRPFFacade.getInstancia ().existeDeclaracoes ())
      PlataformaPPGD.getPlataforma ().habilitaComponentes (listaComponentesTemDeclaracao);
    else
      PlataformaPPGD.getPlataforma ().desabilitaComponentes (listaComponentesTemDeclaracao);
  }
  
  public static void converterDeclaracao (DeclaracaoIRPF declaracaoIRPF)
  {
    if (! declaracaoIRPF.getIdentificadorDeclaracao ().isCompleta ())
      declaracaoIRPF.getIdentificadorDeclaracao ().getTipoDeclaracao ().setConteudo ("0");
    else
      declaracaoIRPF.getIdentificadorDeclaracao ().getTipoDeclaracao ().setConteudo ("1");
    if (temDeclaracaoAberta)
      {
	PainelCacher painelcacher = PainelCacher.getInstance ();
	Class var_class = serpro.ppgd.irpf.gui.comparativo.PainelComparativo.class;
	PainelComparativo painelComparativo = (PainelComparativo) painelcacher.obtemUrgentemente (var_class.getName ());
	painelComparativo.atualizaBotaoOpcao ();
	atualizaTituloDeclaracaoAberta ();
	atualizaTreeMenu ();
      }
  }
  
  public static void setEstadoSistema (int estadoSistema)
  {
    IRPFUtil.estadoSistema = estadoSistema;
  }
  
  public static int getEstadoSistema ()
  {
    return estadoSistema;
  }
}
