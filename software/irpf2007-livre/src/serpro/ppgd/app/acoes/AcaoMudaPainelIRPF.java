/* AcaoMudaPainelIRPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.app.acoes;
import javax.swing.JPanel;

import serpro.ppgd.gui.xbeans.JDropDownButton;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.acoes.AcaoMudaPainel;
import serpro.ppgd.infraestrutura.treeview.ArvoreGenerica;
import serpro.ppgd.infraestrutura.treeview.NoGenerico;
import serpro.ppgd.infraestrutura.util.PainelCacher;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.PainelPrincipalIRPF;
import serpro.ppgd.irpf.gui.comparativo.AcaoConfirmaMudancaIf;
import serpro.ppgd.irpf.gui.comparativo.PainelConfirmacaoMudancaTipoDeclaracao;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.util.ProcessoAssincronoIf;
import serpro.ppgd.negocio.util.ProcessosAssincronos;

public class AcaoMudaPainelIRPF extends AcaoMudaPainel
{
  
  static
  {
    PainelCacher painelcacher = PainelCacher.getInstance ();
    Class var_class = serpro.ppgd.irpf.gui.PainelPrincipalIRPF.class;
    painelcacher.fazCacheDe (var_class.getName ());
    ProcessosAssincronos.getInstance ().inicia ();
  }
  
  protected void acionaPainel (String pRotuloNodo)
  {
    try
      {
	PainelCacher painelcacher = PainelCacher.getInstance ();
	Class var_class = serpro.ppgd.irpf.gui.PainelPrincipalIRPF.class;
	PainelPrincipalIRPF painelPrincipal = (PainelPrincipalIRPF) painelcacher.obtemUrgentemente (var_class.getName ());
	painelAcionado = PainelCacher.getInstance ().obtemUrgentemente (painelStr);
	((PainelIRPFIf) painelAcionado).vaiExibir ();
	verificacaoResultadoTributavelNegativo (painelAcionado);
	IRPFGuiUtil.painelAtualmenteExibido = painelAcionado;
	painelPrincipal.mudaCorpoPainelPrincipal ((PainelIRPFIf) painelAcionado);
	PlataformaPPGD.getPlataforma ().mudaPainelExibido (painelPrincipal);
	JDropDownButton btnProx = (JDropDownButton) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("btnFichaProxima");
	JDropDownButton btnAnterior = (JDropDownButton) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("btnFichaAnterior");
	btnProx.setEnabled (false);
	btnAnterior.setEnabled (false);
	IRPFGuiUtil.atualizaDropDownListAnterior ();
	IRPFGuiUtil.atualizaDropDownListProximo ();
	ArvoreGenerica a = (ArvoreGenerica) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("ppgdarvore");
	NoGenerico noAtual = (NoGenerico) a.getSelectionPath ().getLastPathComponent ();
	IRPFGuiUtil.atualizaBotoes (noAtual);
	IRPFGuiUtil.atualizaMenuFichas ();
	ProcessoAssincronoIf p = new ProcessoAssincronoIf ()
	{
	  public void executa ()
	  {
	    IRPFFacade.salvaDeclaracaoAberta ();
	  }
	};
	ProcessosAssincronos.getInstance ().adicionaTarefa (p);
	((PainelIRPFIf) painelAcionado).painelExibido ();
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  private void verificacaoResultadoTributavelNegativo (JPanel pnl)
  {
    if (IRPFGuiUtil.painelAtualmenteExibido != null && ! pnl.equals (IRPFGuiUtil.painelAtualmenteExibido))
      {
	String string = IRPFGuiUtil.painelAtualmenteExibido.getClass ().getName ();
	Class var_class = serpro.ppgd.irpf.gui.atividaderural.PainelApuracaoResultadoBrasil.class;
	if (string.equals (var_class.getName ()))
	  {
	    if (IRPFFacade.getInstancia ().getIdDeclaracaoAberto ().getTipoDeclaracao ().asString ().equals ("1") && IRPFFacade.getInstancia ().getAtividadeRural ().getBrasil ().getApuracaoResultado ().getResultadoTributavel ().comparacao ("<", "0,00"))
	      IRPFGuiUtil.exibeDialog (new PainelConfirmacaoMudancaTipoDeclaracao (new AcaoConfirmaMudancaIf ()
	      {
		public void zeraPrejuizoAcompensar ()
		{
		  IRPFFacade.getInstancia ().getAtividadeRural ().getBrasil ().getApuracaoResultado ().getPrejuizoCompensar ().clear ();
		}
		
		public void converteDeclaracao ()
		{
		  IRPFUtil.converterDeclaracao (IRPFFacade.getInstancia ().getDeclaracao ());
		}
	      }, "8"), true, "Apura\u00e7\u00e3o do Resultado - Brasil", false);
	  }
	else
	  {
	    String string_8_ = IRPFGuiUtil.painelAtualmenteExibido.getClass ().getName ();
	    Class var_class_9_ = serpro.ppgd.irpf.gui.atividaderural.PainelApuracaoResultadoExterior.class;
	    if (string_8_.equals (var_class_9_.getName ()) && IRPFFacade.getInstancia ().getIdDeclaracaoAberto ().getTipoDeclaracao ().asString ().equals ("1") && IRPFFacade.getInstancia ().getAtividadeRural ().getExterior ().getApuracaoResultado ().getResultadoTributavel ().comparacao ("<", "0,00"))
	      IRPFGuiUtil.exibeDialog (new PainelConfirmacaoMudancaTipoDeclaracao (new AcaoConfirmaMudancaIf ()
	      {
		public void zeraPrejuizoAcompensar ()
		{
		  IRPFFacade.getInstancia ().getAtividadeRural ().getExterior ().getApuracaoResultado ().getPrejuizoCompensar ().clear ();
		}
		
		public void converteDeclaracao ()
		{
		  IRPFUtil.converterDeclaracao (IRPFFacade.getInstancia ().getDeclaracao ());
		}
	      }, "7"), true, "Apura\u00e7\u00e3o do Resultado - Exterior", false);
	  }
      }
  }
}
