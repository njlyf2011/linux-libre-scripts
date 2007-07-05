/* ModeloSimplificada - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf;
import serpro.ppgd.negocio.Valor;

public class ModeloSimplificada extends ModeloDeclaracao
{
  public static final String LIMITE_DESCONTO_SIMPLIFICADO = "11.167,20";
  private Valor rendRecebidoPJTitular = new Valor (this, "");
  private Valor rendRecebidoPJDependentes = new Valor (this, "");
  private Valor rendRecebidoPF = new Valor (this, "");
  private Valor resultadoTributavelAR = new Valor (this, "");
  private Valor totalResultadosTributaveis = new Valor (this, "");
  private Valor descontoSimplificado = new Valor (this, "");
  private Valor impostoRetidoFonteTitular = new Valor (this, "");
  private Valor impostoRetidoFonteDependentes = new Valor (this, "");
  private Valor carneLeaoMaisImpostoComplementar = new Valor (this, "");
  private Valor carneLeaoMaisImpostoComplementarTitular = new Valor (this, "");
  private Valor impostoRetidoFonteLei11033 = new Valor (this, "");
  private Valor rendIsentosNaoTributaveis = new Valor (this, "");
  private Valor rendSujeitoTribExclusiva = new Valor (this, "");
  private Valor bensDireitosExercicioAnterior = new Valor (this, "");
  private Valor bensDireitosExercicioAtual = new Valor (this, "");
  private Valor dividasExercicioAnterior = new Valor (this, "");
  private Valor dividasExercicioAtual = new Valor (this, "");
  private Valor totalImpostoRetidoNaFonte = new Valor (this, "");
  
  public ModeloSimplificada (DeclaracaoIRPF dec)
  {
    super (dec);
  }
  
  public void resumoCalculoImposto ()
  {
    rendRecebidoPJTitular.setConteudo (declaracaoIRPF.getColecaoRendPJTitular ().getTotaisRendRecebidoPJ ());
    rendRecebidoPJDependentes.setConteudo (declaracaoIRPF.getColecaoRendPJDependente ().getTotaisRendRecebidoPJ ());
    rendRecebidoPF.clear ();
    rendRecebidoPF.append ('+', declaracaoIRPF.getRendPFTitular ().getTotalPessoaFisica ());
    rendRecebidoPF.append ('+', declaracaoIRPF.getRendPFDependente ().getTotalPessoaFisica ());
    rendRecebidoExterior.clear ();
    rendRecebidoExterior.append ('+', declaracaoIRPF.getRendPFTitular ().getTotalExterior ());
    rendRecebidoExterior.append ('+', declaracaoIRPF.getRendPFDependente ().getTotalExterior ());
    resultadoTributavelAR.clear ();
    if (declaracaoIRPF.getAtividadeRural ().getBrasil ().getApuracaoResultado ().getResultadoTributavel ().comparacao (">", "0,00"))
      resultadoTributavelAR.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getApuracaoResultado ().getResultadoTributavel ());
    if (declaracaoIRPF.getAtividadeRural ().getExterior ().getApuracaoResultado ().getResultadoTributavel ().comparacao (">", "0,00"))
      resultadoTributavelAR.append ('+', declaracaoIRPF.getAtividadeRural ().getExterior ().getApuracaoResultado ().getResultadoTributavel ());
    totalResultadosTributaveis.clear ();
    totalResultadosTributaveis.append ('+', rendRecebidoPJTitular);
    totalResultadosTributaveis.append ('+', rendRecebidoPJDependentes);
    totalResultadosTributaveis.append ('+', rendRecebidoPF);
    totalResultadosTributaveis.append ('+', rendRecebidoExterior);
    totalResultadosTributaveis.append ('+', resultadoTributavelAR);
    Valor valDesconto = new Valor ();
    valDesconto.setConteudo (totalResultadosTributaveis.operacao ('*', "0,20"));
    if (valDesconto.comparacao (">", "11.167,20"))
      valDesconto.setConteudo ("11.167,20");
    descontoSimplificado.setConteudo (valDesconto);
    baseCalculo.clear ();
    baseCalculo.append ('+', totalResultadosTributaveis);
    baseCalculo.append ('-', descontoSimplificado);
    impostoDevido.setConteudo (calculaImposto (baseCalculo));
    impostoRetidoFonteTitular.setConteudo (declaracaoIRPF.getColecaoRendPJTitular ().getTotaisImpostoRetidoFonte ());
    impostoRetidoFonteDependentes.setConteudo (declaracaoIRPF.getColecaoRendPJDependente ().getTotaisImpostoRetidoFonte ());
    carneLeaoMaisImpostoComplementar.clear ();
    carneLeaoMaisImpostoComplementar.append ('+', declaracaoIRPF.getRendPFTitular ().getTotalDarfPago ());
    carneLeaoMaisImpostoComplementar.append ('+', declaracaoIRPF.getRendPFDependente ().getTotalDarfPago ());
    carneLeaoMaisImpostoComplementar.append ('+', declaracaoIRPF.getImpostoPago ().getImpostoComplementar ());
    carneLeaoMaisImpostoComplementarTitular.clear ();
    carneLeaoMaisImpostoComplementarTitular.append ('+', declaracaoIRPF.getRendPFTitular ().getTotalDarfPago ());
    carneLeaoMaisImpostoComplementarTitular.append ('+', declaracaoIRPF.getImpostoPago ().getImpostoComplementar ());
    impostoRetidoFonteLei11033.setConteudo (declaracaoIRPF.getImpostoPago ().getImpostoRetidoFonte ());
    saldoImpostoPagar.clear ();
    impostoRestituir.clear ();
    Valor impostoRetidoNaFonte = new Valor ();
    impostoRetidoNaFonte.append ('+', impostoRetidoFonteTitular);
    impostoRetidoNaFonte.append ('+', impostoRetidoFonteDependentes);
    impostoRetidoNaFonte.append ('+', carneLeaoMaisImpostoComplementar);
    impostoRetidoNaFonte.append ('+', impostoRetidoFonteLei11033);
    if (impostoDevido.comparacao ("<", impostoRetidoNaFonte))
      impostoRestituir.setConteudo (impostoRetidoNaFonte.operacao ('-', impostoDevido));
    else
      saldoImpostoPagar.setConteudo (impostoDevido.operacao ('-', impostoRetidoNaFonte));
  }
  
  public void resumoOutrasInformacoes ()
  {
    rendIsentosNaoTributaveis.setConteudo (declaracaoIRPF.getRendIsentos ().getTotal ());
    rendSujeitoTribExclusiva.setConteudo (declaracaoIRPF.getRendTributacaoExclusiva ().getTotal ());
    bensDireitosExercicioAnterior.setConteudo (declaracaoIRPF.getBens ().getTotalExercicioAnterior ());
    bensDireitosExercicioAtual.setConteudo (declaracaoIRPF.getBens ().getTotalExercicioAtual ());
    dividasExercicioAnterior.setConteudo (declaracaoIRPF.getDividas ().getTotalExercicioAnterior ());
    dividasExercicioAtual.setConteudo (declaracaoIRPF.getDividas ().getTotalExercicioAtual ());
    totalImpostoRetidoNaFonte.clear ();
    totalImpostoRetidoNaFonte.append ('+', declaracaoIRPF.getImpostoPago ().getImpostoRetidoFonte ());
    totalImpostoRetidoNaFonte.append ('+', declaracaoIRPF.getRendaVariavel ().getTotalImpostoRetidoFonteLei11033 ());
    totalDoacoesCampanhasEleitorais.setConteudo (declaracaoIRPF.getDoacoes ().getTotalDoacoes ());
  }
  
  public void resumoRendimentosTributaveis ()
  {
    /* empty */
  }
  
  public void aplicaValoresNaDeclaracao ()
  {
    declaracaoIRPF.getResumo ().getCalculoImposto ().getRendPJRecebidoTitular ().setConteudo (rendRecebidoPJTitular);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getRendPJRecebidoDependentes ().setConteudo (rendRecebidoPJDependentes);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getRendRecebidoPF ().setConteudo (rendRecebidoPF);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getRendRecebidoExterior ().setConteudo (rendRecebidoExterior);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getResultadoTributavelAR ().setConteudo (resultadoTributavelAR);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getTotalResultadosTributaveis ().setConteudo (totalResultadosTributaveis);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getDescontoSimplificado ().setConteudo (descontoSimplificado);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getBaseCalculo ().setConteudo (baseCalculo);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoDevido ().setConteudo (impostoDevido);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRetidoFonteTitular ().setConteudo (impostoRetidoFonteTitular);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRetidoFonteDependentes ().setConteudo (impostoRetidoFonteDependentes);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getCarneLeaoMaisImpostoComplementar ().setConteudo (carneLeaoMaisImpostoComplementar);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRetidoFonteLei11033 ().setConteudo (impostoRetidoFonteLei11033);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getSaldoImpostoPagar ().setConteudo (saldoImpostoPagar);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRestituir ().setConteudo (impostoRestituir);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getRendIsentosNaoTributaveis ().setConteudo (rendIsentosNaoTributaveis);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getRendIsentosTributacaoExclusiva ().setConteudo (rendSujeitoTribExclusiva);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getBensDireitosExercicioAnterior ().setConteudo (bensDireitosExercicioAnterior);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getBensDireitosExercicioAtual ().setConteudo (bensDireitosExercicioAtual);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getDividasOnusReaisExercicioAnterior ().setConteudo (dividasExercicioAnterior);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getDividasOnusReaisExercicioAtual ().setConteudo (dividasExercicioAtual);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getTotalImpostoRetidoNaFonte ().setConteudo (totalImpostoRetidoNaFonte);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getTotalDoacoesCampanhasEleitorais ().setConteudo (totalDoacoesCampanhasEleitorais);
  }
  
  public Valor getBensDireitosExercicioAnterior ()
  {
    return bensDireitosExercicioAnterior;
  }
  
  public Valor getBensDireitosExercicioAtual ()
  {
    return bensDireitosExercicioAtual;
  }
  
  public Valor getCarneLeaoMaisImpostoComplementar ()
  {
    return carneLeaoMaisImpostoComplementar;
  }
  
  public Valor getCarneLeaoMaisImpostoComplementarTitular ()
  {
    return carneLeaoMaisImpostoComplementarTitular;
  }
  
  public Valor getDescontoSimplificado ()
  {
    return descontoSimplificado;
  }
  
  public Valor getDividasExercicioAnterior ()
  {
    return dividasExercicioAnterior;
  }
  
  public Valor getDividasExercicioAtual ()
  {
    return dividasExercicioAtual;
  }
  
  public Valor getImpostoDevido ()
  {
    return impostoDevido;
  }
  
  public Valor getImpostoDevidoII ()
  {
    return impostoDevidoII;
  }
  
  public Valor getImpostoRetidoFonteDependentes ()
  {
    return impostoRetidoFonteDependentes;
  }
  
  public Valor getImpostoRetidoFonteLei11033 ()
  {
    return impostoRetidoFonteLei11033;
  }
  
  public Valor getImpostoRetidoFonteTitular ()
  {
    return impostoRetidoFonteTitular;
  }
  
  public Valor getRendIsentosNaoTributaveis ()
  {
    return rendIsentosNaoTributaveis;
  }
  
  public Valor getRendRecebidoExterior ()
  {
    return rendRecebidoExterior;
  }
  
  public Valor getRendRecebidoPF ()
  {
    return rendRecebidoPF;
  }
  
  public Valor getRendRecebidoPJDependentes ()
  {
    return rendRecebidoPJDependentes;
  }
  
  public Valor getRendRecebidoPJTitular ()
  {
    return rendRecebidoPJTitular;
  }
  
  public Valor getRendSujeitoTribExclusiva ()
  {
    return rendSujeitoTribExclusiva;
  }
  
  public Valor getResultadoTributavelAR ()
  {
    return resultadoTributavelAR;
  }
  
  public Valor getTotalResultadosTributaveis ()
  {
    return totalResultadosTributaveis;
  }
  
  public Valor recuperarTotalImpostoPago ()
  {
    Valor result = new Valor ();
    result.append ('+', declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRetidoFonteTitular ());
    result.append ('+', declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRetidoFonteDependentes ());
    result.append ('+', declaracaoIRPF.getResumo ().getCalculoImposto ().getCarneLeaoMaisImpostoComplementar ());
    result.append ('+', declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRetidoFonteLei11033 ());
    return result;
  }
  
  public String recuperarCodInImpostoPago ()
  {
    int cod = 0;
    String codStr = "";
    if (! declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRetidoFonteTitular ().isVazio () || ! declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRetidoFonteDependentes ().isVazio ())
      cod++;
    if (! declaracaoIRPF.getResumo ().getCalculoImposto ().getCarneLeaoMaisImpostoComplementar ().isVazio ())
      cod += 6;
    if (! declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRetidoFonteLei11033 ().isVazio ())
      cod += 16;
    if (cod < 9)
      codStr = "0" + cod;
    else
      codStr = "" + cod;
    return codStr;
  }
  
  public Valor calculaImpostoLei11033ComRendaVariavel ()
  {
    return declaracaoIRPF.getImpostoPago ().getImpostoRetidoFonte ().operacao ('+', declaracaoIRPF.getRendaVariavel ().getTotalImpostoRetidoFonteLei11033 ());
  }
  
  public Valor recuperarTotalRendimentosTributaveis ()
  {
    Valor result = new Valor (declaracaoIRPF.getResumo ().getCalculoImposto ().getTotalResultadosTributaveis ().getConteudoFormatado ());
    return result;
  }
}
