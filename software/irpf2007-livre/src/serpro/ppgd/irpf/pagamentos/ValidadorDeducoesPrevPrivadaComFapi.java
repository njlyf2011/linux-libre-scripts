/* ValidadorDeducoesPrevPrivadaComFapi - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.pagamentos;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.calculos.CalculosPagamentos;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.Valor;

public class ValidadorDeducoesPrevPrivadaComFapi extends ValidadorDefault
{
  private static final String MSG_LIMITE_DEDUCAO_PREVI_PRIV_E_FAPI = "<HTML>As dedu\u00e7\u00f5es relativas a Contribui\u00e7\u00f5es a Entidades de Previd\u00eancia Privada e<BR> ao FAPI n\u00e3o podem exceder, em conjunto, a 12% dos rendimentos tribut\u00e1veis.";
  private static final String MSG_FINAL_LIMITE_DEDUCAO_PREVI_PRIV_E_FAPI = "Para  o  c\u00e1lculo do imposto devido, o  programa levar\u00e1 em considera\u00e7\u00e3o o limite legal,<BR> entretanto, nesta ficha devem ser informados os valores efetivamente pagos,<BR> independentemente do limite de dedu\u00e7\u00e3o.</HTML>";
  private Valor previdencia;
  private Valor fAPI;
  private Valor totalRendimentosTributaveis;
  private Valor totalLimitado;
  private Valor fapiComPrevPrivada;
  private Valor limiteDeducPrevPrivada;
  private DeclaracaoIRPF declaracaoIRPF = null;
  private Pagamento pagamento = null;
  private String msg = "";
  
  public ValidadorDeducoesPrevPrivadaComFapi (byte severidade, DeclaracaoIRPF dec, Pagamento pagamento)
  {
    super (severidade);
    declaracaoIRPF = dec;
    this.pagamento = pagamento;
  }
  
  public RetornoValidacao validarImplementado ()
  {
    msg = "";
    boolean codigoValido = pagamento.getCodigo ().asString ().equals ("14") || pagamento.getCodigo ().asString ().equals ("13");
    if (codigoValido)
      {
	previdencia = CalculosPagamentos.totalizarPagamentosGlosado (declaracaoIRPF.getPagamentos (), new String[] { "13" }, true);
	fAPI = CalculosPagamentos.totalizarPagamentosGlosado (declaracaoIRPF.getPagamentos (), new String[] { "14" }, true);
	fapiComPrevPrivada = new Valor ();
	fapiComPrevPrivada.append ('+', previdencia);
	fapiComPrevPrivada.append ('+', fAPI);
	totalLimitado = declaracaoIRPF.getPagamentos ().getTotalContribuicaoFAPI ();
	totalRendimentosTributaveis = new Valor ();
	totalRendimentosTributaveis.setConteudo (declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getTotalRendimentos ());
	limiteDeducPrevPrivada = totalRendimentosTributaveis.operacao ('*', "0,12");
	if (codigoValido && fapiComPrevPrivada.comparacao (">", totalLimitado))
	  msg = "<HTML>As dedu\u00e7\u00f5es relativas a Contribui\u00e7\u00f5es a Entidades de Previd\u00eancia Privada e<BR> ao FAPI n\u00e3o podem exceder, em conjunto, a 12% dos rendimentos tribut\u00e1veis.<br>De acordo com os dados da sua declara\u00e7\u00e3o, o total destas dedu\u00e7\u00f5es \u00e9: " + fapiComPrevPrivada + " e o limite legal \u00e9: " + totalLimitado.toString () + " conforme demosntrativo abaixo.<br><br>" + "\"Contribui\u00e7\u00e3o \u00e0 previd\u00eancia privada\": " + previdencia.toString () + "<br><br>" + "\"FAPI\": " + fAPI.toString () + "<br><br>" + "\"Total de contribui\u00e7\u00e3o \u00e0 previd\u00eancia privada e FAPI\": " + fapiComPrevPrivada.toString () + "<br><br>" + "\"Total dos rendimentos tribut\u00e1veis\": " + totalRendimentosTributaveis.toString () + "<br><br>" + "\"12% do rendimento tribut\u00e1vel\": " + limiteDeducPrevPrivada.toString () + "<br><br>" + "Para  o  c\u00e1lculo do imposto devido, o  programa levar\u00e1 em considera\u00e7\u00e3o o limite legal,<BR> entretanto, nesta ficha devem ser informados os valores efetivamente pagos,<BR> independentemente do limite de dedu\u00e7\u00e3o.</HTML>";
      }
    if (msg != null && ! msg.equals (""))
      {
	RetornoValidacao retorno = new RetornoValidacao ("<HTML>As dedu\u00e7\u00f5es relativas a Contribui\u00e7\u00f5es a Entidades de Previd\u00eancia Privada e<BR> ao FAPI n\u00e3o podem exceder, em conjunto, a 12% dos rendimentos tribut\u00e1veis.</HTML>");
	retorno.setMensagemValidacaoExtendida (msg);
	return retorno;
      }
    return null;
  }
}
