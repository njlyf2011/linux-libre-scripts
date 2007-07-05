/* ValidadorDeducoesDoacoes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.pagamentos;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.calculos.CalculosPagamentos;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.Valor;

public class ValidadorDeducoesDoacoes extends ValidadorDefault
{
  private static final String MSG_LIMITE_DEDUCAO_DOACAO = "<HTML>As dedu\u00e7\u00f5es relativas ao Estatuto da Crian\u00e7a, Incentivo \u00e0 Cultura e Incentivo \u00e0 Atividade Audiovisual, em <BR> conjunto, n\u00e3o podem exceder a 6% do valor do imposto.";
  private static final String MSG_FINAL_LIMITE_DEDUCAO_DOACAO = "Para  o  c\u00e1lculo do imposto devido, o  programa levar\u00e1 em considera\u00e7\u00e3o o limite legal,<BR> entretanto, nesta ficha devem ser informados os valores efetivamente pagos,<BR> independentemente do limite de dedu\u00e7\u00e3o.</HTML>";
  private Valor imposto;
  private Valor totDoacoes;
  private Valor limite;
  private Valor totAudioVisual;
  private Valor totCultura;
  private Valor totCrianca;
  private DeclaracaoIRPF declaracaoIRPF = null;
  private Pagamento pagamento = null;
  private String msg = "";
  
  public ValidadorDeducoesDoacoes (byte severidade, DeclaracaoIRPF dec, Pagamento pag)
  {
    super (severidade);
    declaracaoIRPF = dec;
    pagamento = pag;
  }
  
  public RetornoValidacao validarImplementado ()
  {
    msg = "";
    boolean codigoValido = pagamento.getCodigo ().asString ().equals ("15") || pagamento.getCodigo ().asString ().equals ("16") || pagamento.getCodigo ().asString ().equals ("17");
    if (codigoValido)
      {
	totCrianca = CalculosPagamentos.totalizarPagamentosGlosado (declaracaoIRPF.getPagamentos (), new String[] { "15" }, true);
	totCultura = CalculosPagamentos.totalizarPagamentosGlosado (declaracaoIRPF.getPagamentos (), new String[] { "16" }, true);
	totAudioVisual = CalculosPagamentos.totalizarPagamentosGlosado (declaracaoIRPF.getPagamentos (), new String[] { "17" }, true);
	limite = declaracaoIRPF.getPagamentos ().getTotalDeducaoIncentivo ();
	totDoacoes = new Valor ();
	totDoacoes.append ('+', totCrianca);
	totDoacoes.append ('+', totCultura);
	totDoacoes.append ('+', totAudioVisual);
	imposto = new Valor ();
	imposto.setConteudo (declaracaoIRPF.getModeloCompleta ().getImposto ());
	if (totDoacoes.comparacao (">", limite))
	  msg = "<HTML>As dedu\u00e7\u00f5es relativas ao Estatuto da Crian\u00e7a, Incentivo \u00e0 Cultura e Incentivo \u00e0 Atividade Audiovisual, em <BR> conjunto, n\u00e3o podem exceder a 6% do valor do imposto.<BR>De acordo com os dados da sua declara\u00e7\u00e3o, o total destas dedu\u00e7\u00f5es \u00e9: " + totDoacoes + " e o limite legal \u00e9 : " + limite.toString () + " conforme demosntrativo abaixo.<br><br>" + "\"Estatuto da crian\u00e7a\": " + totCrianca.toString () + "<br>" + "\"Incentivo \u00e0 cultura\": " + totCultura.toString () + "<br>" + "\"Incentivo \u00e0 atividade audiovisual\": " + totAudioVisual.toString () + "<br>" + "\"Total de doa\u00e7\u00f5es\": " + totDoacoes.toString () + "<br>" + "\"Imposto\": " + imposto.toString () + "<br>" + "\"Dedu\u00e7\u00e3o de incentivo\": " + limite.toString () + "<br><br>" + "Para  o  c\u00e1lculo do imposto devido, o  programa levar\u00e1 em considera\u00e7\u00e3o o limite legal,<BR> entretanto, nesta ficha devem ser informados os valores efetivamente pagos,<BR> independentemente do limite de dedu\u00e7\u00e3o.</HTML>";
      }
    if (msg != null && ! msg.equals (""))
      {
	RetornoValidacao retorno = new RetornoValidacao ("<HTML>As dedu\u00e7\u00f5es relativas ao Estatuto da Crian\u00e7a, Incentivo \u00e0 Cultura e Incentivo \u00e0 Atividade Audiovisual, em <BR> conjunto, n\u00e3o podem exceder a 6% do valor do imposto.</HTML>");
	retorno.setMensagemValidacaoExtendida (msg);
	return retorno;
      }
    return null;
  }
}
