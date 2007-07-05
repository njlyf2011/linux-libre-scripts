/* ModeloCompleta - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf;
import serpro.ppgd.negocio.Valor;

public class ModeloCompleta extends ModeloDeclaracao
{
  public static final String CONTRIB_EMPR_MAX = "536,00";
  private Valor rendRecebidoPJTitular = new Valor (this, "");
  private Valor rendRecebidoPJDependentes = new Valor (this, "");
  private Valor rendRecebidoPFTitular = new Valor (this, "");
  private Valor rendRecebidoPFDependentes = new Valor (this, "");
  private Valor resultadoTributavelAR = new Valor (this, "");
  private Valor totalRendimentos = new Valor (this, "");
  private Valor previdenciaOficial = new Valor (this, "");
  private Valor previdenciaFAPI = new Valor (this, "");
  private Valor deducaoDependentes = new Valor (this, "");
  private Valor despesasInstrucao = new Valor (this, "");
  private Valor despesasMedicas = new Valor (this, "");
  private Valor pensaoAlimenticia = new Valor (this, "");
  private Valor livroCaixa = new Valor (this, "");
  private Valor totalDeducoes = new Valor (this, "");
  private Valor totalContribEmpregadoDomestico = new Valor (this, "");
  private Valor imposto = new Valor (this, "");
  private Valor deducaoIncentivo = new Valor (this, "");
  private Valor impostoRetidoFonteTitular = new Valor (this, "");
  private Valor impostoRetidoFonteDependentes = new Valor (this, "");
  private Valor carneLeao = new Valor (this, "");
  private Valor impostoComplementar = new Valor (this, "");
  private Valor impostoPagoExterior = new Valor (this, "");
  private Valor impostoRetidoFonteLei11033 = new Valor (this, "");
  private Valor totalImpostoPago = new Valor (this, "");
  private Valor bensDireitosExercicioAnterior = new Valor (this, "");
  private Valor bensDireitosExercicioAtual = new Valor (this, "");
  private Valor dividasExercicioAnterior = new Valor (this, "");
  private Valor dividasExercicioAtual = new Valor (this, "");
  private Valor rendIsentosNaoTributaveis = new Valor (this, "");
  private Valor rendSujeitoTribExclusiva = new Valor (this, "");
  private Valor impostoPagoGCAP = new Valor (this, "");
  private Valor impostoPagoME = new Valor (this, "");
  private Valor totalImpostoRetidoNaFonte = new Valor (this, "");
  private Valor impostoPagoSobreRendaVariavel = new Valor (this, "");
  private Valor informacoesConjuge = new Valor (this, "");
  
  public ModeloCompleta (DeclaracaoIRPF dec)
  {
    super (dec);
  }
  
  public void resumoRendimentosTributaveis ()
  {
    rendRecebidoPJTitular.setConteudo (declaracaoIRPF.getColecaoRendPJTitular ().getTotaisRendRecebidoPJ ());
    rendRecebidoPJDependentes.setConteudo (declaracaoIRPF.getColecaoRendPJDependente ().getTotaisRendRecebidoPJ ());
    rendRecebidoPFTitular.setConteudo (declaracaoIRPF.getRendPFTitular ().getTotalPessoaFisica ());
    rendRecebidoPFDependentes.setConteudo (declaracaoIRPF.getRendPFDependente ().getTotalPessoaFisica ());
    rendRecebidoExterior.clear ();
    rendRecebidoExterior.append ('+', declaracaoIRPF.getRendPFTitular ().getTotalExterior ());
    rendRecebidoExterior.append ('+', declaracaoIRPF.getRendPFDependente ().getTotalExterior ());
    resultadoTributavelAR.clear ();
    if (declaracaoIRPF.getAtividadeRural ().getBrasil ().getApuracaoResultado ().getResultadoTributavel ().comparacao (">", "0,00"))
      resultadoTributavelAR.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getApuracaoResultado ().getResultadoTributavel ());
    if (declaracaoIRPF.getAtividadeRural ().getExterior ().getApuracaoResultado ().getResultadoTributavel ().comparacao (">", "0,00"))
      resultadoTributavelAR.append ('+', declaracaoIRPF.getAtividadeRural ().getExterior ().getApuracaoResultado ().getResultadoTributavel ());
    totalRendimentos.clear ();
    totalRendimentos.append ('+', rendRecebidoPJTitular);
    totalRendimentos.append ('+', rendRecebidoPJDependentes);
    totalRendimentos.append ('+', rendRecebidoPFTitular);
    totalRendimentos.append ('+', rendRecebidoPFDependentes);
    totalRendimentos.append ('+', rendRecebidoExterior);
    totalRendimentos.append ('+', resultadoTributavelAR);
    previdenciaOficial.clear ();
    previdenciaOficial.append ('+', declaracaoIRPF.getRendPFTitular ().getTotalPrevidencia ());
    previdenciaOficial.append ('+', declaracaoIRPF.getRendPFDependente ().getTotalPrevidencia ());
    previdenciaOficial.append ('+', declaracaoIRPF.getRendPJ ().getColecaoRendPJTitular ().getTotaisContribuicaoPrevOficial ());
    previdenciaOficial.append ('+', declaracaoIRPF.getRendPJ ().getColecaoRendPJDependente ().getTotaisContribuicaoPrevOficial ());
    previdenciaFAPI.setConteudo (declaracaoIRPF.getPagamentos ().getTotalContribuicaoFAPI ());
    deducaoDependentes.setConteudo (declaracaoIRPF.getDependentes ().getTotalDeducaoDependentes ());
    despesasInstrucao.setConteudo (declaracaoIRPF.getPagamentos ().getTotalDeducoesInstrucao ());
    despesasMedicas.setConteudo (declaracaoIRPF.getPagamentos ().getTotalDespesasMedicas ());
    pensaoAlimenticia.setConteudo (declaracaoIRPF.getPagamentos ().getTotalPensao ());
    livroCaixa.clear ();
    livroCaixa.append ('+', declaracaoIRPF.getRendPFTitular ().getTotalLivroCaixa ());
    livroCaixa.append ('+', declaracaoIRPF.getRendPFDependente ().getTotalLivroCaixa ());
    totalDeducoes.clear ();
    totalDeducoes.append ('+', previdenciaOficial);
    totalDeducoes.append ('+', previdenciaFAPI);
    totalDeducoes.append ('+', deducaoDependentes);
    totalDeducoes.append ('+', despesasInstrucao);
    totalDeducoes.append ('+', despesasMedicas);
    totalDeducoes.append ('+', pensaoAlimenticia);
    totalDeducoes.append ('+', livroCaixa);
  }
  
  public void resumoCalculoImposto ()
  {
    baseCalculo.clear ();
    baseCalculo.append ('+', totalRendimentos);
    baseCalculo.append ('-', totalDeducoes);
    if (baseCalculo.comparacao ("<", "0,00"))
      baseCalculo.clear ();
    imposto.setConteudo (calculaImposto (baseCalculo));
    deducaoIncentivo.setConteudo (declaracaoIRPF.getPagamentos ().getTotalDeducaoIncentivo ());
    impostoDevido.clear ();
    impostoDevido.append ('+', imposto);
    impostoDevido.append ('-', deducaoIncentivo);
    if (impostoDevido.comparacao ("<", "0,00"))
      impostoDevido.clear ();
    totalContribEmpregadoDomestico.setConteudo (declaracaoIRPF.getPagamentos ().getTotalContribEmpregadoDomestico ());
    if (totalContribEmpregadoDomestico.comparacao (">", "536,00"))
      totalContribEmpregadoDomestico.setConteudo ("536,00");
    impostoDevidoII.clear ();
    impostoDevidoII.append ('+', impostoDevido);
    impostoDevidoII.append ('-', totalContribEmpregadoDomestico);
    if (impostoDevidoII.comparacao ("<", "0,00"))
      impostoDevidoII.clear ();
    impostoRetidoFonteTitular.setConteudo (declaracaoIRPF.getColecaoRendPJTitular ().getTotaisImpostoRetidoFonte ());
    impostoRetidoFonteDependentes.setConteudo (declaracaoIRPF.getColecaoRendPJDependente ().getTotaisImpostoRetidoFonte ());
    carneLeao.clear ();
    carneLeao.append ('+', declaracaoIRPF.getRendPFTitular ().getTotalDarfPago ());
    carneLeao.append ('+', declaracaoIRPF.getRendPFDependente ().getTotalDarfPago ());
    impostoComplementar.setConteudo (declaracaoIRPF.getImpostoPago ().getImpostoComplementar ());
    Valor impPagoExt = new Valor ();
    aplicaLimitesImpostoPagoExterior (impPagoExt);
    impostoPagoExterior.setConteudo (impPagoExt);
    impostoRetidoFonteLei11033.setConteudo (declaracaoIRPF.getImpostoPago ().getImpostoRetidoFonte ());
    totalImpostoPago.clear ();
    totalImpostoPago.append ('+', impostoRetidoFonteTitular);
    totalImpostoPago.append ('+', impostoRetidoFonteDependentes);
    totalImpostoPago.append ('+', carneLeao);
    totalImpostoPago.append ('+', impostoRetidoFonteLei11033);
    totalImpostoPago.append ('+', impostoComplementar);
    totalImpostoPago.append ('+', impostoPagoExterior);
    saldoImpostoPagar.clear ();
    impostoRestituir.clear ();
    if (impostoDevidoII.comparacao ("<", totalImpostoPago))
      {
	impostoRestituir.clear ();
	impostoRestituir.append ('+', totalImpostoPago);
	impostoRestituir.append ('-', impostoDevidoII);
      }
    else
      {
	saldoImpostoPagar.clear ();
	saldoImpostoPagar.append ('+', impostoDevidoII);
	saldoImpostoPagar.append ('-', totalImpostoPago);
      }
  }
  
  public void resumoOutrasInformacoes ()
  {
    bensDireitosExercicioAnterior.setConteudo (declaracaoIRPF.getBens ().getTotalExercicioAnterior ());
    bensDireitosExercicioAtual.setConteudo (declaracaoIRPF.getBens ().getTotalExercicioAtual ());
    dividasExercicioAnterior.setConteudo (declaracaoIRPF.getDividas ().getTotalExercicioAnterior ());
    dividasExercicioAtual.setConteudo (declaracaoIRPF.getDividas ().getTotalExercicioAtual ());
    informacoesConjuge.setConteudo (declaracaoIRPF.getConjuge ().getResultado ());
    rendIsentosNaoTributaveis.setConteudo (declaracaoIRPF.getRendIsentos ().getTotal ());
    rendSujeitoTribExclusiva.setConteudo (declaracaoIRPF.getRendTributacaoExclusiva ().getTotal ());
    impostoPagoGCAP.setConteudo (declaracaoIRPF.getGanhosDeCapital ().getTotalImpostoPagoSobreGanhosCapital ());
    impostoPagoME.setConteudo (declaracaoIRPF.getMoedaEstrangeira ().getTotalImpostoPagoSobreGanhosCapital ());
    totalImpostoRetidoNaFonte.clear ();
    totalImpostoRetidoNaFonte.append ('+', declaracaoIRPF.getImpostoPago ().getImpostoRetidoFonte ());
    totalImpostoRetidoNaFonte.append ('+', declaracaoIRPF.getRendaVariavel ().getTotalImpostoRetidoFonteLei11033 ());
    Valor rendVarImpostoPago = new Valor (declaracaoIRPF.getRendaVariavel ().getTotalImpostoPago ().asString ());
    rendVarImpostoPago.append ('+', declaracaoIRPF.getRendaVariavel ().getTotalImpostoPagoComFundInvest ());
    impostoPagoSobreRendaVariavel.setConteudo (rendVarImpostoPago);
    totalDoacoesCampanhasEleitorais.setConteudo (declaracaoIRPF.getDoacoes ().getTotalDoacoes ());
  }
  
  private void aplicaLimitesImpostoPagoExterior (Valor impPagoExt)
  {
    impPagoExt.setConteudo (declaracaoIRPF.getImpostoPago ().getImpostoPagoExterior ());
    Valor baseCalcImpDevidoCOMRendNoExterior = new Valor ();
    baseCalcImpDevidoCOMRendNoExterior.append ('+', totalRendimentos);
    baseCalcImpDevidoCOMRendNoExterior.append ('-', totalDeducoes);
    Valor impDevidoCOMRendExterior = calculaImposto (baseCalcImpDevidoCOMRendNoExterior);
    declaracaoIRPF.getImpostoPago ().getImpostoDevidoComRendExterior ().setConteudo (impDevidoCOMRendExterior);
    Valor baseCalcImpDevidoSEMRendNoExterior = new Valor ();
    baseCalcImpDevidoSEMRendNoExterior.append ('+', totalRendimentos);
    baseCalcImpDevidoSEMRendNoExterior.append ('-', totalDeducoes);
    baseCalcImpDevidoSEMRendNoExterior.append ('-', rendRecebidoExterior);
    Valor impDevidoSEMRendExterior = calculaImposto (baseCalcImpDevidoSEMRendNoExterior);
    Valor limiteImpostoPagoNoExterior = impDevidoCOMRendExterior.operacao ('-', impDevidoSEMRendExterior);
    if (impPagoExt.comparacao (">", limiteImpostoPagoNoExterior))
      impPagoExt.setConteudo (limiteImpostoPagoNoExterior);
    declaracaoIRPF.getImpostoPago ().getImpostoDevidoSemRendExterior ().setConteudo (impDevidoSEMRendExterior);
    declaracaoIRPF.getImpostoPago ().getLimiteLegalImpPagoExterior ().setConteudo (impPagoExt);
  }
  
  public void aplicaValoresNaDeclaracao ()
  {
    declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getRendRecebidoPJTitular ().setConteudo (rendRecebidoPJTitular);
    declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getRendRecebidoPJDependentes ().setConteudo (rendRecebidoPJDependentes);
    declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getRendRecebidoPFTitular ().setConteudo (rendRecebidoPFTitular);
    declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getRendRecebidoPFDependentes ().setConteudo (rendRecebidoPFDependentes);
    declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getRendRecebidoExterior ().setConteudo (rendRecebidoExterior);
    declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getRendTributavelAR ().setConteudo (resultadoTributavelAR);
    declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getTotalRendimentos ().setConteudo (totalRendimentos);
    declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getPrevidenciaOficial ().setConteudo (previdenciaOficial);
    declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getPrevidenciaFAPI ().setConteudo (previdenciaFAPI);
    declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getDependentes ().setConteudo (deducaoDependentes);
    declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getDespesasInstrucao ().setConteudo (despesasInstrucao);
    declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getDespesasMedicas ().setConteudo (despesasMedicas);
    declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getPensaoAlimenticia ().setConteudo (pensaoAlimenticia);
    declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getLivroCaixa ().setConteudo (livroCaixa);
    declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getTotalDeducoes ().setConteudo (totalDeducoes);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getBaseCalculo ().setConteudo (baseCalculo);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getImposto ().setConteudo (imposto);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getDeducaoIncentivo ().setConteudo (deducaoIncentivo);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getTotalContribEmpregadoDomestico ().setConteudo (totalContribEmpregadoDomestico);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoDevido ().setConteudo (impostoDevido);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoDevidoII ().setConteudo (impostoDevidoII);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRetidoFonteTitular ().setConteudo (impostoRetidoFonteTitular);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRetidoFonteDependentes ().setConteudo (impostoRetidoFonteDependentes);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getCarneLeao ().setConteudo (carneLeao);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoComplementar ().setConteudo (impostoComplementar);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoPagoExterior ().setConteudo (impostoPagoExterior);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRetidoFonteLei11033 ().setConteudo (impostoRetidoFonteLei11033);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getTotalImpostoPago ().setConteudo (totalImpostoPago);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getSaldoImpostoPagar ().setConteudo (saldoImpostoPagar);
    declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRestituir ().setConteudo (impostoRestituir);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getBensDireitosExercicioAnterior ().setConteudo (bensDireitosExercicioAnterior);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getBensDireitosExercicioAtual ().setConteudo (bensDireitosExercicioAtual);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getDividasOnusReaisExercicioAnterior ().setConteudo (dividasExercicioAnterior);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getDividasOnusReaisExercicioAtual ().setConteudo (dividasExercicioAtual);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getInformacoesConjuge ().setConteudo (informacoesConjuge);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getRendIsentosNaoTributaveis ().setConteudo (rendIsentosNaoTributaveis);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getRendIsentosTributacaoExclusiva ().setConteudo (rendSujeitoTribExclusiva);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getImpostoPagoGCAP ().setConteudo (impostoPagoGCAP);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getImpostoPagoME ().setConteudo (impostoPagoME);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getTotalImpostoRetidoNaFonte ().setConteudo (totalImpostoRetidoNaFonte);
    declaracaoIRPF.getResumo ().getOutrasInformacoes ().getImpostoPagoSobreRendaVariavel ().setConteudo (impostoPagoSobreRendaVariavel);
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
  
  public Valor getCarneLeao ()
  {
    return carneLeao;
  }
  
  public Valor getDeducaoDependentes ()
  {
    return deducaoDependentes;
  }
  
  public Valor getDeducaoIncentivo ()
  {
    return deducaoIncentivo;
  }
  
  public Valor getDespesasInstrucao ()
  {
    return despesasInstrucao;
  }
  
  public Valor getDespesasMedicas ()
  {
    return despesasMedicas;
  }
  
  public Valor getDividasExercicioAnterior ()
  {
    return dividasExercicioAnterior;
  }
  
  public Valor getDividasExercicioAtual ()
  {
    return dividasExercicioAtual;
  }
  
  public Valor getImposto ()
  {
    return imposto;
  }
  
  public Valor getImpostoComplementar ()
  {
    return impostoComplementar;
  }
  
  public Valor getImpostoPagoExterior ()
  {
    return impostoPagoExterior;
  }
  
  public Valor getImpostoPagoGCAP ()
  {
    return impostoPagoGCAP;
  }
  
  public Valor getImpostoPagoME ()
  {
    return impostoPagoME;
  }
  
  public Valor getImpostoPagoSobreRendaVariavel ()
  {
    return impostoPagoSobreRendaVariavel;
  }
  
  public Valor getImpostoRestituir ()
  {
    return impostoRestituir;
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
  
  public Valor getInformacoesConjuge ()
  {
    return informacoesConjuge;
  }
  
  public Valor getLivroCaixa ()
  {
    return livroCaixa;
  }
  
  public Valor getPensaoAlimenticia ()
  {
    return pensaoAlimenticia;
  }
  
  public Valor getPrevidenciaFAPI ()
  {
    return previdenciaFAPI;
  }
  
  public Valor getPrevidenciaOficial ()
  {
    return previdenciaOficial;
  }
  
  public Valor getRendIsentosNaoTributaveis ()
  {
    return rendIsentosNaoTributaveis;
  }
  
  public Valor getRendRecebidoPFDependentes ()
  {
    return rendRecebidoPFDependentes;
  }
  
  public Valor getRendRecebidoPFTitular ()
  {
    return rendRecebidoPFTitular;
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
  
  public Valor getSaldoImpostoPagar ()
  {
    return saldoImpostoPagar;
  }
  
  public Valor getTotalDeducoes ()
  {
    return totalDeducoes;
  }
  
  public Valor getTotalImpostoPago ()
  {
    return totalImpostoPago;
  }
  
  public Valor getTotalImpostoRetidoNaFonte ()
  {
    return totalImpostoRetidoNaFonte;
  }
  
  public Valor getTotalRendimentos ()
  {
    return totalRendimentos;
  }
  
  public Valor recuperarTotalImpostoPago ()
  {
    Valor result = new Valor ();
    result.append ('+', declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRetidoFonteTitular ());
    result.append ('+', declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRetidoFonteDependentes ());
    result.append ('+', declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoPagoExterior ());
    result.append ('+', declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoComplementar ());
    result.append ('+', declaracaoIRPF.getResumo ().getCalculoImposto ().getCarneLeao ());
    result.append ('+', declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRetidoFonteLei11033 ());
    return result;
  }
  
  public String recuperarCodInImpostoPago ()
  {
    int cod = 0;
    String codStr = "";
    if (! declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRetidoFonteTitular ().isVazio () || ! declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRetidoFonteDependentes ().isVazio ())
      cod++;
    if (! declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoPagoExterior ().isVazio ())
      cod += 8;
    if (! declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoComplementar ().isVazio ())
      cod += 4;
    if (! declaracaoIRPF.getResumo ().getCalculoImposto ().getCarneLeao ().isVazio ())
      cod += 2;
    if (! declaracaoIRPF.getResumo ().getCalculoImposto ().getImpostoRetidoFonteLei11033 ().isVazio ())
      cod += 16;
    if (cod < 9)
      codStr = "0" + cod;
    else
      codStr = "" + cod;
    return codStr;
  }
  
  public Valor recuperarTotalRendimentosTributaveis ()
  {
    Valor result = new Valor (declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getTotalRendimentos ().getConteudoFormatado ());
    return result;
  }
}
