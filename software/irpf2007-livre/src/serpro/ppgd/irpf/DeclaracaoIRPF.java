/* DeclaracaoIRPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import serpro.ppgd.irpf.alimentandos.Alimentando;
import serpro.ppgd.irpf.alimentandos.Alimentandos;
import serpro.ppgd.irpf.atividaderural.AtividadeRural;
import serpro.ppgd.irpf.bens.Bens;
import serpro.ppgd.irpf.calculos.CalculosApuracaoResultadoARBrasil;
import serpro.ppgd.irpf.calculos.CalculosApuracaoResultadoARExterior;
import serpro.ppgd.irpf.calculos.CalculosBens;
import serpro.ppgd.irpf.calculos.CalculosDeducoesIncentivos;
import serpro.ppgd.irpf.calculos.CalculosDividas;
import serpro.ppgd.irpf.calculos.CalculosFundosInvestimentos;
import serpro.ppgd.irpf.calculos.CalculosGanhosRendaVar;
import serpro.ppgd.irpf.calculos.CalculosPagamentos;
import serpro.ppgd.irpf.calculos.CalculosReceitaDespesaARExterior;
import serpro.ppgd.irpf.calculos.CalculosReceitasDespesasARBrasil;
import serpro.ppgd.irpf.calculos.CalculosRendIsentos;
import serpro.ppgd.irpf.calculos.CalculosRendPF;
import serpro.ppgd.irpf.calculos.CalculosRendPJDependentes;
import serpro.ppgd.irpf.calculos.CalculosRendPJTitular;
import serpro.ppgd.irpf.calculos.CalculosRendTributacaoExclusiva;
import serpro.ppgd.irpf.calculos.CalculosResumo;
import serpro.ppgd.irpf.calculos.CalculosTotaisFundosInvestimentos;
import serpro.ppgd.irpf.calculos.CalculosTotaisLivroCaixa;
import serpro.ppgd.irpf.calculos.CalculosTotaisRendaVariavel;
import serpro.ppgd.irpf.calculos.CalculosTotalRendRecebidosMaisExterior;
import serpro.ppgd.irpf.comparativo.Comparativo;
import serpro.ppgd.irpf.conjuge.Conjuge;
import serpro.ppgd.irpf.conjuge.ObservadorConjuge;
import serpro.ppgd.irpf.contribuinte.Contribuinte;
import serpro.ppgd.irpf.dependentes.Dependente;
import serpro.ppgd.irpf.dependentes.Dependentes;
import serpro.ppgd.irpf.dividas.Dividas;
import serpro.ppgd.irpf.eleicoes.Doacoes;
import serpro.ppgd.irpf.espolio.Espolio;
import serpro.ppgd.irpf.ganhosdecapital.GanhosDeCapital;
import serpro.ppgd.irpf.impostopago.ImpostoPago;
import serpro.ppgd.irpf.moedaestrangeira.MoedaEstrangeira;
import serpro.ppgd.irpf.pagamentos.CalculosDoacoes;
import serpro.ppgd.irpf.pagamentos.ObservadorCPFDependente;
import serpro.ppgd.irpf.pagamentos.ObservadorCodigoPagamento;
import serpro.ppgd.irpf.pagamentos.ObservadorNomeAlimentando;
import serpro.ppgd.irpf.pagamentos.ObservadorNomeDependente;
import serpro.ppgd.irpf.pagamentos.Pagamento;
import serpro.ppgd.irpf.pagamentos.Pagamentos;
import serpro.ppgd.irpf.pagamentos.ValidadorDeducoesDoacoes;
import serpro.ppgd.irpf.pagamentos.ValidadorDeducoesPrevPrivadaComFapi;
import serpro.ppgd.irpf.rendIsentos.ObservadorResultadoNaoTributavel;
import serpro.ppgd.irpf.rendIsentos.RendIsentos;
import serpro.ppgd.irpf.rendTributacaoExclusiva.RendTributacaoExclusiva;
import serpro.ppgd.irpf.rendavariavel.GanhosLiquidosOuPerdas;
import serpro.ppgd.irpf.rendavariavel.RendaVariavel;
import serpro.ppgd.irpf.rendpf.RendPF;
import serpro.ppgd.irpf.rendpf.RendPFDependente;
import serpro.ppgd.irpf.rendpj.ColecaoRendPJDependente;
import serpro.ppgd.irpf.rendpj.ColecaoRendPJTitular;
import serpro.ppgd.irpf.rendpj.RendPJ;
import serpro.ppgd.irpf.rendpj.RendPJDependente;
import serpro.ppgd.irpf.rendpj.RendPJTitular;
import serpro.ppgd.irpf.resumo.Resumo;
import serpro.ppgd.irpf.util.ConstantesGlobaisIRPF;
import serpro.ppgd.negocio.CPF;
import serpro.ppgd.negocio.NI;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Pendencia;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.persistenciagenerica.RepositorioXMLDefault;

public class DeclaracaoIRPF extends ObjetoNegocio
{
  protected transient IdentificadorDeclaracao identificadorDeclaracao;
  private Contribuinte contribuinte;
  private Conjuge conjuge;
  private RendPJ rendPJ;
  private RendPF rendPFTitular;
  private RendPFDependente rendPFDependente;
  private RendIsentos rendIsentos;
  private RendTributacaoExclusiva rendTributacaoExclusiva;
  private ImpostoPago impostoPago;
  private Dependentes dependentes;
  private Alimentandos alimentandos;
  private Pagamentos pagamentos;
  private Bens bens;
  private Dividas dividas;
  private Espolio espolio;
  private Resumo resumo;
  private Comparativo comparativo;
  private Doacoes doacoes;
  private RendaVariavel rendaVariavel;
  private AtividadeRural atividadeRural;
  private GanhosDeCapital ganhosDeCapital;
  private MoedaEstrangeira moedaEstrangeira;
  private transient ModeloCompleta modeloCompleta;
  private transient ModeloSimplificada modeloSimplificada;
  private transient ModeloDeclaracao modelo;
  
  public DeclaracaoIRPF (IdentificadorDeclaracao id)
  {
    identificadorDeclaracao = id;
    instanciaAtributos ();
    identificadorDeclaracao.setPersistente (false);
    modelo.setPersistente (false);
    adicionaObservadoresCalculos ();
    adicionaObservadoresNegocio ();
    adicionaValidadoresEspeciais ();
  }
  
  private void instanciaAtributos ()
  {
    contribuinte = new Contribuinte (identificadorDeclaracao);
    pagamentos = new Pagamentos (identificadorDeclaracao);
    rendPJ = new RendPJ (identificadorDeclaracao);
    espolio = new Espolio (identificadorDeclaracao);
    conjuge = new Conjuge (identificadorDeclaracao);
    impostoPago = new ImpostoPago ();
    dependentes = new Dependentes (getContribuinte ());
    rendaVariavel = new RendaVariavel ();
    alimentandos = new Alimentandos ();
    bens = new Bens ();
    dividas = new Dividas ();
    atividadeRural = new AtividadeRural ();
    ganhosDeCapital = new GanhosDeCapital ();
    moedaEstrangeira = new MoedaEstrangeira ();
    rendPFTitular = new RendPF ();
    rendPFDependente = new RendPFDependente ();
    rendIsentos = new RendIsentos ();
    rendTributacaoExclusiva = new RendTributacaoExclusiva ();
    comparativo = new Comparativo ();
    resumo = new Resumo (identificadorDeclaracao, contribuinte);
    doacoes = new Doacoes ();
    modeloSimplificada = new ModeloSimplificada (this);
    modeloCompleta = new ModeloCompleta (this);
    modelo = modeloCompleta;
  }
  
  public IdentificadorDeclaracao getIdentificadorDeclaracao ()
  {
    return identificadorDeclaracao;
  }
  
  public void adicionaValidadoresEspeciais ()
  {
    getPagamentos ().addObservador (new Observador ()
    {
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	if (nomePropriedade.equals ("ObjetoInserido"))
	  {
	    ((Pagamento) valorNovo).getValorPago ().addValidador (new ValidadorDeducoesDoacoes ((byte) 2, DeclaracaoIRPF.this, (Pagamento) valorNovo));
	    ((Pagamento) valorNovo).getValorPago ().addValidador (new ValidadorDeducoesPrevPrivadaComFapi ((byte) 2, DeclaracaoIRPF.this, (Pagamento) valorNovo));
	  }
      }
    });
    getImpostoPago ().getImpostoRetidoFonte ().addValidador (new ValidadorDefault ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	DeclaracaoIRPF.this.setFicha ("Imposto Pago");
	boolean temResultadoLiq = getRendaVariavel ().temResultadoLiquido ();
	if (! getImpostoPago ().isVazio () && ! temResultadoLiq)
	  return new RetornoValidacao (tab.msg ("imposto_retido_rendvar_vazio"), (byte) 3);
	return null;
      }
    });
  }
  
  public List verificarPendencias (int numeroItem)
  {
    List retorno = super.verificarPendencias (numeroItem);
    verificaLivroCaixa (numeroItem, retorno);
    return retorno;
  }
  
  protected void verificaLivroCaixa (int numeroItem, List retorno)
  {
    int numItem = numeroItem;
    serpro.ppgd.negocio.Informacao infoPendencia = null;
    if (! getColecaoRendPJTitular ().recuperarLista ().isEmpty ())
      {
	infoPendencia = ((RendPJTitular) getColecaoRendPJTitular ().recuperarLista ().get (0)).getRendRecebidoPJ ();
	numItem = 1;
      }
    else if (! getColecaoRendPJDependente ().recuperarLista ().isEmpty ())
      {
	infoPendencia = ((RendPJTitular) getColecaoRendPJDependente ().recuperarLista ().get (0)).getRendRecebidoPJ ();
	numItem = 1;
      }
    else
      infoPendencia = getColecaoRendPJTitular ().getTotaisRendRecebidoPJ ();
    if (! identificadorDeclaracao.getTipoDeclaracao ().asString ().equals ("1") && getModelo ().getTotalRendRecebidosMaisExterior ().comparacao ("<", getModelo ().getTotalLivroCaixa ()))
      {
	Pendencia p = new Pendencia ((byte) 3, infoPendencia, "Livro Caixa", tab.msg ("rend_maior_livro_caixa"), -1);
	retorno.add (p);
      }
  }
  
  public void adicionaObservadoresCalculos ()
  {
    CalculosRendPF calculosRendPFTitular = new CalculosRendPF (getRendPFTitular ());
    CalculosRendPF calculosRendPFDependente = new CalculosRendPF (getRendPFDependente ());
    getRendPFTitular ().addObservador (calculosRendPFTitular);
    getRendPFDependente ().addObservador (calculosRendPFDependente);
    CalculosRendPJTitular calculosRendPJTitular = new CalculosRendPJTitular (getColecaoRendPJTitular (), this);
    CalculosRendPJDependentes calculosRendPJDependente = new CalculosRendPJDependentes (getColecaoRendPJDependente (), this);
    getColecaoRendPJTitular ().addObservador (calculosRendPJTitular);
    getColecaoRendPJDependente ().addObservador (calculosRendPJDependente);
    CalculosTotalRendRecebidosMaisExterior calculosTotalRendimentosTributaveis = new CalculosTotalRendRecebidosMaisExterior (this);
    getColecaoRendPJTitular ().getTotaisRendRecebidoPJ ().addObservador (calculosTotalRendimentosTributaveis);
    getColecaoRendPJDependente ().getTotaisRendRecebidoPJ ().addObservador (calculosTotalRendimentosTributaveis);
    getRendPFTitular ().getTotalPessoaFisica ().addObservador (calculosTotalRendimentosTributaveis);
    getRendPFTitular ().getTotalExterior ().addObservador (calculosTotalRendimentosTributaveis);
    getRendPFDependente ().getTotalPessoaFisica ().addObservador (calculosTotalRendimentosTributaveis);
    getRendPFDependente ().getTotalExterior ().addObservador (calculosTotalRendimentosTributaveis);
    CalculosTotaisLivroCaixa calculosTotaisLivroCaixa = new CalculosTotaisLivroCaixa (this);
    getRendPFTitular ().getTotalLivroCaixa ().addObservador (calculosTotaisLivroCaixa);
    getRendPFDependente ().getTotalLivroCaixa ().addObservador (calculosTotaisLivroCaixa);
    CalculosRendIsentos calculosRendIsentos = new CalculosRendIsentos (getRendIsentos ());
    getRendIsentos ().addObservador (calculosRendIsentos);
    CalculosRendTributacaoExclusiva calcRendTributacaoExclusiva = new CalculosRendTributacaoExclusiva (this);
    getRendTributacaoExclusiva ().addObservador (calcRendTributacaoExclusiva);
    getColecaoRendPJTitular ().getTotaisDecimoTerceiro ().addObservador (calcRendTributacaoExclusiva);
    getColecaoRendPJDependente ().getTotaisDecimoTerceiro ().addObservador (calcRendTributacaoExclusiva);
    getBens ().addObservador (new CalculosBens (getBens ()));
    getDividas ().addObservador (new CalculosDividas (getDividas ()));
    getPagamentos ().addObservador (new CalculosPagamentos (this));
    CalculosDeducoesIncentivos calculosDeducoesIncentivos = new CalculosDeducoesIncentivos (this);
    getModeloCompleta ().getImposto ().addObservador (calculosDeducoesIncentivos);
    getDoacoes ().addObservador (new CalculosDoacoes (this));
    getAtividadeRural ().getBrasil ().getReceitasDespesas ().addObservadorCalculosTotais (new CalculosReceitasDespesasARBrasil (this));
    CalculosApuracaoResultadoARBrasil apuracaoResultadoARBrasil = new CalculosApuracaoResultadoARBrasil (this);
    getAtividadeRural ().getBrasil ().getReceitasDespesas ().getTotalReceita ().addObservador (apuracaoResultadoARBrasil);
    getAtividadeRural ().getBrasil ().getReceitasDespesas ().getTotalDespesas ().addObservador (apuracaoResultadoARBrasil);
    getAtividadeRural ().getBrasil ().getApuracaoResultado ().getPrejuizoExercicioAnterior ().addObservador (apuracaoResultadoARBrasil);
    getAtividadeRural ().getBrasil ().getApuracaoResultado ().getReceitaRecebidaContaVenda ().addObservador (apuracaoResultadoARBrasil);
    getAtividadeRural ().getBrasil ().getApuracaoResultado ().getValorAdiantamento ().addObservador (apuracaoResultadoARBrasil);
    getAtividadeRural ().getExterior ().getReceitasDespesas ().addObservador (new CalculosReceitaDespesaARExterior (this));
    CalculosApuracaoResultadoARExterior calculosApuracaoResultadoARExterior = new CalculosApuracaoResultadoARExterior (this);
    getAtividadeRural ().getExterior ().getReceitasDespesas ().getTotais ().addObservador (calculosApuracaoResultadoARExterior);
    getAtividadeRural ().getExterior ().getApuracaoResultado ().getPrejuizoExercicioAnterior ().addObservador (calculosApuracaoResultadoARExterior);
    getAtividadeRural ().getExterior ().getApuracaoResultado ().getOpcaoArbitramento ().addObservador (calculosApuracaoResultadoARExterior);
    getAtividadeRural ().getExterior ().getApuracaoResultado ().getReceitaRecebidaContaVenda ().addObservador (calculosApuracaoResultadoARExterior);
    getAtividadeRural ().getExterior ().getApuracaoResultado ().getValorAdiantamento ().addObservador (calculosApuracaoResultadoARExterior);
    getRendaVariavel ().adicionarObservadorCalculosTotaisRendaVariavel (new CalculosTotaisRendaVariavel (this));
    CalculosFundosInvestimentos calFundInvest = null;
    for (int i = 0; i < 12; i++)
      {
	calFundInvest = new CalculosFundosInvestimentos (getRendaVariavel ().getFundInvest ().getMeses ()[i], getRendaVariavel ());
	getRendaVariavel ().getFundInvest ().getMeses ()[i].getResultLiquidoMes ().addObservador (calFundInvest);
	getRendaVariavel ().getFundInvest ().getMeses ()[i].getResultNegativoAnterior ().addObservador (calFundInvest);
	getRendaVariavel ().getFundInvest ().getMeses ()[i].getBaseCalcImposto ().addObservador (calFundInvest);
	getRendaVariavel ().getFundInvest ().getMeses ()[i].getImpostoPago ().addObservador (calFundInvest);
	if (i > 0)
	  getRendaVariavel ().getFundInvest ().getMeses ()[i - 1].getPrejuizoCompensar ().addObservador (calFundInvest);
      }
    getRendaVariavel ().getFundInvest ().adicionarCalculosTotaisFundInvest (new CalculosTotaisFundosInvestimentos (getRendaVariavel ().getFundInvest ()));
    CalculosGanhosRendaVar calcGanhosRendaVar = new CalculosGanhosRendaVar (getRendaVariavel (), getRendTributacaoExclusiva ());
    getRendaVariavel ().getFundInvest ().adicionarObservGanhosFundInvest (calcGanhosRendaVar);
    getRendaVariavel ().adicionarObservGanhosRendaVar (calcGanhosRendaVar);
  }
  
  public void adicionaObservadoresCalculosLate ()
  {
    CalculosResumo calculosResumo = new CalculosResumo (this);
    identificadorDeclaracao.getTipoDeclaracao ().addObservador (calculosResumo);
    getRendPFTitular ().getTotalPessoaFisica ().addObservador (calculosResumo);
    getRendPFDependente ().getTotalPessoaFisica ().addObservador (calculosResumo);
    getRendPFTitular ().getTotalPensao ().addObservador (calculosResumo);
    getRendPFDependente ().getTotalPensao ().addObservador (calculosResumo);
    getRendPFTitular ().getTotalExterior ().addObservador (calculosResumo);
    getRendPFDependente ().getTotalExterior ().addObservador (calculosResumo);
    getRendPFTitular ().getTotalPrevidencia ().addObservador (calculosResumo);
    getRendPFDependente ().getTotalPrevidencia ().addObservador (calculosResumo);
    getRendPFTitular ().getTotalLivroCaixa ().addObservador (calculosResumo);
    getRendPFDependente ().getTotalLivroCaixa ().addObservador (calculosResumo);
    getRendPFTitular ().getTotalDarfPago ().addObservador (calculosResumo);
    getRendPFDependente ().getTotalDarfPago ().addObservador (calculosResumo);
    getRendPFTitular ().getTotalDependentes ().addObservador (calculosResumo);
    getRendPFDependente ().getTotalDependentes ().addObservador (calculosResumo);
    getAtividadeRural ().getBrasil ().getApuracaoResultado ().getResultadoTributavel ().addObservador (calculosResumo);
    getAtividadeRural ().getExterior ().getApuracaoResultado ().getResultadoTributavel ().addObservador (calculosResumo);
    getRendPJ ().getColecaoRendPJTitular ().addObservador (calculosResumo);
    getRendPJ ().getColecaoRendPJDependente ().addObservador (calculosResumo);
    getRendPJ ().getColecaoRendPJTitular ().getTotaisRendRecebidoPJ ().addObservador (calculosResumo);
    getRendPJ ().getColecaoRendPJDependente ().getTotaisRendRecebidoPJ ().addObservador (calculosResumo);
    getRendPJ ().getColecaoRendPJTitular ().getTotaisContribuicaoPrevOficial ().addObservador (calculosResumo);
    getRendPJ ().getColecaoRendPJDependente ().getTotaisContribuicaoPrevOficial ().addObservador (calculosResumo);
    getRendPJ ().getColecaoRendPJTitular ().getTotaisImpostoRetidoFonte ().addObservador (calculosResumo);
    getRendPJ ().getColecaoRendPJDependente ().getTotaisImpostoRetidoFonte ().addObservador (calculosResumo);
    getRendPJ ().getColecaoRendPJTitular ().getTotaisImpostoRetidoFonte ().addObservador (calculosResumo);
    getRendPJ ().getColecaoRendPJDependente ().getTotaisImpostoRetidoFonte ().addObservador (calculosResumo);
    getImpostoPago ().getImpostoComplementar ().addObservador (calculosResumo);
    getImpostoPago ().getImpostoRetidoFonte ().addObservador (calculosResumo);
    getImpostoPago ().getImpostoPagoExterior ().addObservador (calculosResumo);
    getRendIsentos ().getTotal ().addObservador (calculosResumo);
    getRendTributacaoExclusiva ().getTotal ().addObservador (calculosResumo);
    getBens ().addObservador (calculosResumo);
    getBens ().getTotalExercicioAnterior ().addObservador (calculosResumo);
    getBens ().getTotalExercicioAtual ().addObservador (calculosResumo);
    getDividas ().addObservador (calculosResumo);
    getDividas ().getTotalExercicioAnterior ().addObservador (calculosResumo);
    getDividas ().getTotalExercicioAtual ().addObservador (calculosResumo);
    getPagamentos ().addObservador (calculosResumo);
    getPagamentos ().getTotalDeducoesInstrucao ().addObservador (calculosResumo);
    getPagamentos ().getTotalContribuicaoFAPI ().addObservador (calculosResumo);
    getPagamentos ().getTotalDespesasMedicas ().addObservador (calculosResumo);
    getPagamentos ().getTotalPensao ().addObservador (calculosResumo);
    getPagamentos ().getTotalDeducaoIncentivo ().addObservador (calculosResumo);
    getPagamentos ().getTotalContribEmpregadoDomestico ().addObservador (calculosResumo);
    getConjuge ().getResultado ().addObservador (calculosResumo);
    getRendaVariavel ().getTotalImpostoRetidoFonteLei11033 ().addObservador (calculosResumo);
    getRendaVariavel ().getTotalImpostoPago ().addObservador (calculosResumo);
    getRendaVariavel ().getTotalImpostoPagoComFundInvest ().addObservador (calculosResumo);
    getDependentes ().getTotalDeducaoDependentes ().addObservador (calculosResumo);
    getDoacoes ().getTotalDoacoes ().addObservador (calculosResumo);
    getImpostoPago ().getImpostoComplementar ().disparaObservadores ();
  }
  
  public void adicionaObservadoresNegocio ()
  {
    identificadorDeclaracao.getTipoDeclaracao ().addObservador (new ObservadorTipoDeclaracao (this));
    getPagamentos ().addObservador (new ObservadorCodigoPagamento (this));
    getConjuge ().getCpfConjuge ().addObservador (new ObservadorConjuge (getConjuge ()));
    getConjuge ().getDecSimplificada ().addObservador (new ObservadorConjuge (getConjuge ()));
    getConjuge ().getBaseCalculoImposto ().addObservador (new ObservadorConjuge (getConjuge ()));
    getConjuge ().getImpRetidoFonte ().addObservador (new ObservadorConjuge (getConjuge ()));
    getConjuge ().getCarneComImpComplementar ().addObservador (new ObservadorConjuge (getConjuge ()));
    getConjuge ().getRendIsentoNaoTributaveis ().addObservador (new ObservadorConjuge (getConjuge ()));
    getConjuge ().getRendSujeitosTribExcl ().addObservador (new ObservadorConjuge (getConjuge ()));
    getDependentes ().addObservador (new ObservadorNomeDependente (this));
    getDependentes ().addObservador (new ObservadorCPFDependente (this));
    getAlimentandos ().addObservador (new ObservadorNomeAlimentando (this));
    ObservadorResultadoNaoTributavel observadorResultadoNaoTributavel = new ObservadorResultadoNaoTributavel (this);
    getAtividadeRural ().getBrasil ().getApuracaoResultado ().getResultadoNaoTributavel ().addObservador (observadorResultadoNaoTributavel);
    getAtividadeRural ().getExterior ().getApuracaoResultado ().getResultadoNaoTributavel ().addObservador (observadorResultadoNaoTributavel);
  }
  
  public ModeloCompleta getModeloCompleta ()
  {
    return modeloCompleta;
  }
  
  public ModeloSimplificada getModeloSimplificada ()
  {
    return modeloSimplificada;
  }
  
  public ModeloDeclaracao getModelo ()
  {
    if (identificadorDeclaracao.getTipoDeclaracao ().asString ().equals ("0"))
      modelo = getModeloCompleta ();
    else
      modelo = getModeloSimplificada ();
    return modelo;
  }
  
  public void setModeloCompleta ()
  {
    modelo = getModeloCompleta ();
  }
  
  public void setModeloSimplificada ()
  {
    modelo = getModeloSimplificada ();
  }
  
  public Alimentandos getAlimentandos ()
  {
    return alimentandos;
  }
  
  public Doacoes getDoacoes ()
  {
    return doacoes;
  }
  
  public AtividadeRural getAtividadeRural ()
  {
    return atividadeRural;
  }
  
  public Bens getBens ()
  {
    return bens;
  }
  
  public RendPJ getRendPJ ()
  {
    return rendPJ;
  }
  
  public ColecaoRendPJDependente getColecaoRendPJDependente ()
  {
    return rendPJ.getColecaoRendPJDependente ();
  }
  
  public ColecaoRendPJTitular getColecaoRendPJTitular ()
  {
    return rendPJ.getColecaoRendPJTitular ();
  }
  
  public Comparativo getComparativo ()
  {
    return comparativo;
  }
  
  public Conjuge getConjuge ()
  {
    return conjuge;
  }
  
  public Contribuinte getContribuinte ()
  {
    return contribuinte;
  }
  
  public Dependentes getDependentes ()
  {
    return dependentes;
  }
  
  public Dividas getDividas ()
  {
    return dividas;
  }
  
  public Espolio getEspolio ()
  {
    return espolio;
  }
  
  public GanhosDeCapital getGanhosDeCapital ()
  {
    return ganhosDeCapital;
  }
  
  public ImpostoPago getImpostoPago ()
  {
    return impostoPago;
  }
  
  public MoedaEstrangeira getMoedaEstrangeira ()
  {
    return moedaEstrangeira;
  }
  
  public Pagamentos getPagamentos ()
  {
    return pagamentos;
  }
  
  public RendaVariavel getRendaVariavel ()
  {
    return rendaVariavel;
  }
  
  public RendPFDependente getRendPFDependente ()
  {
    return rendPFDependente;
  }
  
  public RendPF getRendPFTitular ()
  {
    return rendPFTitular;
  }
  
  public RendTributacaoExclusiva getRendTributacaoExclusiva ()
  {
    return rendTributacaoExclusiva;
  }
  
  public Resumo getResumo ()
  {
    return resumo;
  }
  
  public NI recuperarPrincipalFontePagadora ()
  {
    ColecaoRendPJTitular colecaoRendPJTitular = getColecaoRendPJTitular ();
    ColecaoRendPJDependente colecaoRendPJDependentes = getColecaoRendPJDependente ();
    Hashtable fontesPagadoras = new Hashtable ();
    colecaoRendPJTitular.excluirRegistrosEmBranco ();
    Iterator itCol = colecaoRendPJTitular.recuperarLista ().iterator ();
    class FontePagadora extends ObjetoNegocio
    {
      public RendPJTitular rendimento;
      public Valor valorTotal = new Valor (this, "");
      public IdentificadorDeclaracao id = null;
      
      public FontePagadora (IdentificadorDeclaracao pId, RendPJTitular pRendPj)
      {
	id = pId;
	rendimento = pRendPj;
      }
    };
    while (itCol.hasNext ())
      {
	RendPJTitular rendPJTitularAtual = (RendPJTitular) itCol.next ();
	if (! fontesPagadoras.containsKey (rendPJTitularAtual.getNIFontePagadora ().asString ()))
	  {
	    FontePagadora fontePagadora = new FontePagadora (getIdentificadorDeclaracao (), rendPJTitularAtual);
	    fontesPagadoras.put (fontePagadora.rendimento.getNIFontePagadora ().asString (), fontePagadora);
	  }
	FontePagadora fontePagadora = (FontePagadora) fontesPagadoras.get (rendPJTitularAtual.getNIFontePagadora ().asString ());
	fontePagadora.valorTotal.append ('+', rendPJTitularAtual.getRendRecebidoPJ ());
      }
    colecaoRendPJTitular.excluirRegistrosEmBranco ();
    itCol = colecaoRendPJDependentes.recuperarLista ().iterator ();
    while (itCol.hasNext ())
      {
	RendPJDependente rendPJDependenteAtual = (RendPJDependente) itCol.next ();
	if (! fontesPagadoras.containsKey (rendPJDependenteAtual.getNIFontePagadora ().asString ()))
	  {
	    FontePagadora fontePagadora = new FontePagadora (getIdentificadorDeclaracao (), rendPJDependenteAtual);
	    fontesPagadoras.put (fontePagadora.rendimento.getNIFontePagadora ().asString (), fontePagadora);
	  }
	FontePagadora fontePagadora = (FontePagadora) fontesPagadoras.get (rendPJDependenteAtual.getNIFontePagadora ().asString ());
	fontePagadora.valorTotal.append ('+', rendPJDependenteAtual.getRendRecebidoPJ ());
      }
    itCol = fontesPagadoras.values ().iterator ();
    FontePagadora maiorFonte = null;
    while (itCol.hasNext ())
      {
	FontePagadora fontePagadora = (FontePagadora) itCol.next ();
	if (maiorFonte == null || fontePagadora.valorTotal.comparacao (">", maiorFonte.valorTotal))
	  maiorFonte = fontePagadora;
      }
    if (maiorFonte == null)
      return new NI (this, "");
    return maiorFonte.rendimento.getNIFontePagadora ();
  }
  
  public NI recuperarSegundaMaiorFontePagadora ()
  {
    ColecaoRendPJTitular colecaoRendPJTitular = getColecaoRendPJTitular ();
    ColecaoRendPJDependente colecaoRendPJDependentes = getColecaoRendPJDependente ();
    Hashtable fontesPagadoras = new Hashtable ();
    colecaoRendPJTitular.excluirRegistrosEmBranco ();
    Iterator itCol = colecaoRendPJTitular.recuperarLista ().iterator ();
    class FontePagadora extends ObjetoNegocio
    {
      public RendPJTitular rendimento;
      public Valor valorTotal = new Valor (this, "");
      public IdentificadorDeclaracao id = null;
      
      public FontePagadora (IdentificadorDeclaracao pId, RendPJTitular pRendPj)
      {
	id = pId;
	rendimento = pRendPj;
      }
    };
    while (itCol.hasNext ())
      {
	RendPJTitular rendPJTitularAtual = (RendPJTitular) itCol.next ();
	if (! fontesPagadoras.containsKey (rendPJTitularAtual.getNIFontePagadora ().asString ()))
	  {
	    FontePagadora fontePagadora = new FontePagadora (getIdentificadorDeclaracao (), rendPJTitularAtual);
	    fontesPagadoras.put (fontePagadora.rendimento.getNIFontePagadora ().asString (), fontePagadora);
	  }
	FontePagadora fontePagadora = (FontePagadora) fontesPagadoras.get (rendPJTitularAtual.getNIFontePagadora ().asString ());
	fontePagadora.valorTotal.append ('+', rendPJTitularAtual.getRendRecebidoPJ ());
      }
    colecaoRendPJTitular.excluirRegistrosEmBranco ();
    itCol = colecaoRendPJDependentes.recuperarLista ().iterator ();
    while (itCol.hasNext ())
      {
	RendPJDependente rendPJDependenteAtual = (RendPJDependente) itCol.next ();
	if (! fontesPagadoras.containsKey (rendPJDependenteAtual.getNIFontePagadora ().asString ()))
	  {
	    FontePagadora fontePagadora = new FontePagadora (getIdentificadorDeclaracao (), rendPJDependenteAtual);
	    fontesPagadoras.put (fontePagadora.rendimento.getNIFontePagadora ().asString (), fontePagadora);
	  }
	FontePagadora fontePagadora = (FontePagadora) fontesPagadoras.get (rendPJDependenteAtual.getNIFontePagadora ().asString ());
	fontePagadora.valorTotal.append ('+', rendPJDependenteAtual.getRendRecebidoPJ ());
      }
    itCol = fontesPagadoras.values ().iterator ();
    FontePagadora maiorFonte = null;
    FontePagadora segundaMaiorFonte = null;
    while (itCol.hasNext ())
      {
	FontePagadora fontePagadora = (FontePagadora) itCol.next ();
	if (maiorFonte == null || fontePagadora.valorTotal.comparacao (">", maiorFonte.valorTotal))
	  {
	    segundaMaiorFonte = maiorFonte;
	    maiorFonte = fontePagadora;
	  }
	else if (segundaMaiorFonte == null || fontePagadora.valorTotal.comparacao (">", segundaMaiorFonte.valorTotal))
	  segundaMaiorFonte = fontePagadora;
      }
    if (segundaMaiorFonte == null)
      return new NI (this, "");
    return segundaMaiorFonte.rendimento.getNIFontePagadora ();
  }
  
  public NI recuperarTerceiraMaiorFontePagadora ()
  {
    ColecaoRendPJTitular colecaoRendPJTitular = getColecaoRendPJTitular ();
    ColecaoRendPJDependente colecaoRendPJDependentes = getColecaoRendPJDependente ();
    Hashtable fontesPagadoras = new Hashtable ();
    colecaoRendPJTitular.excluirRegistrosEmBranco ();
    Iterator itCol = colecaoRendPJTitular.recuperarLista ().iterator ();
    class FontePagadora extends ObjetoNegocio
    {
      public RendPJTitular rendimento;
      public Valor valorTotal = new Valor (this, "");
      public IdentificadorDeclaracao id = null;
      
      public FontePagadora (IdentificadorDeclaracao pId, RendPJTitular pRendPj)
      {
	id = pId;
	rendimento = pRendPj;
      }
    };
    while (itCol.hasNext ())
      {
	RendPJTitular rendPJTitularAtual = (RendPJTitular) itCol.next ();
	if (! fontesPagadoras.containsKey (rendPJTitularAtual.getNIFontePagadora ().asString ()))
	  {
	    FontePagadora fontePagadora = new FontePagadora (getIdentificadorDeclaracao (), rendPJTitularAtual);
	    fontesPagadoras.put (fontePagadora.rendimento.getNIFontePagadora ().asString (), fontePagadora);
	  }
	FontePagadora fontePagadora = (FontePagadora) fontesPagadoras.get (rendPJTitularAtual.getNIFontePagadora ().asString ());
	fontePagadora.valorTotal.append ('+', rendPJTitularAtual.getRendRecebidoPJ ());
      }
    colecaoRendPJTitular.excluirRegistrosEmBranco ();
    itCol = colecaoRendPJDependentes.recuperarLista ().iterator ();
    while (itCol.hasNext ())
      {
	RendPJDependente rendPJDependenteAtual = (RendPJDependente) itCol.next ();
	if (! fontesPagadoras.containsKey (rendPJDependenteAtual.getNIFontePagadora ().asString ()))
	  {
	    FontePagadora fontePagadora = new FontePagadora (getIdentificadorDeclaracao (), rendPJDependenteAtual);
	    fontesPagadoras.put (fontePagadora.rendimento.getNIFontePagadora ().asString (), fontePagadora);
	  }
	FontePagadora fontePagadora = (FontePagadora) fontesPagadoras.get (rendPJDependenteAtual.getNIFontePagadora ().asString ());
	fontePagadora.valorTotal.append ('+', rendPJDependenteAtual.getRendRecebidoPJ ());
      }
    itCol = fontesPagadoras.values ().iterator ();
    FontePagadora maiorFonte = null;
    FontePagadora segundaMaiorFonte = null;
    FontePagadora terceiraMaiorFonte = null;
    while (itCol.hasNext ())
      {
	FontePagadora fontePagadora = (FontePagadora) itCol.next ();
	if (maiorFonte == null || fontePagadora.valorTotal.comparacao (">", maiorFonte.valorTotal))
	  {
	    terceiraMaiorFonte = segundaMaiorFonte;
	    segundaMaiorFonte = maiorFonte;
	    maiorFonte = fontePagadora;
	  }
	else if (segundaMaiorFonte == null || fontePagadora.valorTotal.comparacao (">", segundaMaiorFonte.valorTotal))
	  {
	    terceiraMaiorFonte = segundaMaiorFonte;
	    segundaMaiorFonte = fontePagadora;
	  }
	else if (terceiraMaiorFonte == null || fontePagadora.valorTotal.comparacao (">", terceiraMaiorFonte.valorTotal))
	  terceiraMaiorFonte = fontePagadora;
      }
    if (terceiraMaiorFonte == null)
      return new NI (this, "");
    return terceiraMaiorFonte.rendimento.getNIFontePagadora ();
  }
  
  private CPF recuperarDependentesPorOrdemValor (int numOrdem)
  {
    ColecaoRendPJDependente colRendPJDependentes = getColecaoRendPJDependente ();
    List cpfDependentes = new ArrayList ();
    Iterator itCol = colRendPJDependentes.recuperarLista ().iterator ();
    class CPFDependente extends ObjetoNegocio
    {
      public CPF cpf;
      public Valor valorTotal = new Valor (this, "");
      
      public CPFDependente (CPF cpf)
      {
	this.cpf = cpf;
      }
      
      public boolean equals (Object obj)
      {
	if (obj instanceof CPFDependente && cpf.asString ().equals (((CPFDependente) obj).cpf.asString ()))
	  return true;
	return false;
      }
    };
    while (itCol.hasNext ())
      {
	RendPJDependente rendPJDependenteAtual = (RendPJDependente) itCol.next ();
	CPFDependente cpfDependente = new CPFDependente (rendPJDependenteAtual.getCpfDependente ());
	int ind = cpfDependentes.indexOf (cpfDependente);
	if (ind < 0)
	  cpfDependentes.add (cpfDependente);
	else
	  cpfDependente = (CPFDependente) cpfDependentes.get (ind);
	cpfDependente.valorTotal.append ('+', rendPJDependenteAtual.getRendRecebidoPJ ());
      }
    if (getIdentificadorDeclaracao ().isCompleta ())
      {
	Dependentes colDependentes = getDependentes ();
	itCol = colDependentes.recuperarLista ().iterator ();
	while (itCol.hasNext ())
	  {
	    Dependente dependenteAtual = (Dependente) itCol.next ();
	    CPFDependente cpfDependente = new CPFDependente (dependenteAtual.getCpfDependente ());
	    if (! cpfDependentes.contains (cpfDependente))
	      cpfDependentes.add (cpfDependente);
	  }
      }
    CPFDependente maiorDependente = null;
    for (int i = 0; i < numOrdem; i++)
      {
	itCol = cpfDependentes.iterator ();
	maiorDependente = null;
	while (itCol.hasNext ())
	  {
	    CPFDependente cpfDependente = (CPFDependente) itCol.next ();
	    if (maiorDependente == null || cpfDependente.valorTotal.comparacao (">", maiorDependente.valorTotal))
	      maiorDependente = cpfDependente;
	  }
	cpfDependentes.remove (maiorDependente);
      }
    if (maiorDependente == null)
      return new CPF ();
    return maiorDependente.cpf;
  }
  
  public CPF recuperarMaiorDependente ()
  {
    return recuperarDependentesPorOrdemValor (1);
  }
  
  public CPF recuperarSegundoMaiorDependente ()
  {
    return recuperarDependentesPorOrdemValor (2);
  }
  
  public CPF recuperarTerceiroMaiorDependente ()
  {
    return recuperarDependentesPorOrdemValor (3);
  }
  
  public CPF recuperarQuartoMaiorDependente ()
  {
    return recuperarDependentesPorOrdemValor (4);
  }
  
  public String verificaObrigatoriedadeEntrega ()
  {
    Valor totalRendTributaveis = getModelo () instanceof ModeloCompleta ? new Valor (getResumo ().getRendimentosTributaveisDeducoes ().getTotalRendimentos ().getConteudoFormatado ()) : new Valor (getResumo ().getCalculoImposto ().getTotalResultadosTributaveis ().getConteudoFormatado ());
    if (totalRendTributaveis.comparacao (">", "14.992,32"))
      return "1";
    Valor valRendIsentos = new Valor ();
    valRendIsentos.setConteudo (getRendIsentos ().getTotal ());
    Valor valRendTribut = new Valor ();
    valRendTribut.setConteudo (getRendTributacaoExclusiva ().getTotal ());
    if (valRendIsentos.operacao ('+', valRendTribut).comparacao (">", ConstantesGlobaisIRPF.LIMITE_REND_ISEN_TRIB_EXCL))
      return "1";
    if (getContribuinte ().getNaturezaOcupacao ().getConteudoFormatado ().equals ("12"))
      return "1";
    for (int i = 0; i <= 11; i++)
      {
	GanhosLiquidosOuPerdas ganhoAtual = getRendaVariavel ().getGanhosPorIndice (i);
	if (! ganhoAtual.getOperacoesComuns ().getResultadoLiquidoMes ().isVazio ())
	  return "1";
	if (! ganhoAtual.getOperacoesDayTrade ().getResultadoLiquidoMes ().isVazio ())
	  return "1";
      }
    if (getBens ().getTotalExercicioAtual ().comparacao (">", ConstantesGlobaisIRPF.LIMITE_BENS_DIREITOS))
      return "1";
    Valor recBrutaTotalBR = new Valor ();
    recBrutaTotalBR.setConteudo (getAtividadeRural ().getBrasil ().getApuracaoResultado ().getReceitaBrutaTotal ());
    Valor resultadoIExt = new Valor ();
    resultadoIExt.setConteudo (getAtividadeRural ().getExterior ().getApuracaoResultado ().getResultadoI_EmReais ());
    if (recBrutaTotalBR.operacao ('+', resultadoIExt).comparacao (">", "74.961,60"))
      return "1";
    if (getIdentificadorDeclaracao ().isCompleta ())
      {
	if (! getAtividadeRural ().getBrasil ().getApuracaoResultado ().getPrejuizoCompensar ().isVazio ())
	  return "1";
	if (! getAtividadeRural ().getExterior ().getApuracaoResultado ().getPrejuizoCompensar ().isVazio ())
	  return "1";
      }
    return "0";
  }
  
  public RendIsentos getRendIsentos ()
  {
    return rendIsentos;
  }
  
  public int getChaveDependenteOuAlimentando (Pagamento pag)
  {
    String codTipoPag = pag.getCodigo ().getConteudoAtual (0);
    int ret = 1;
    if (codTipoPag.trim ().equals ("03") || codTipoPag.trim ().equals ("04"))
      {
	Iterator it = getDependentes ().recuperarLista ().iterator ();
	while (it.hasNext ())
	  {
	    Dependente dep = (Dependente) it.next ();
	    if (dep.getNome ().asString ().toUpperCase ().equals (pag.getDependenteOuAlimentando ().asString ().toUpperCase ()))
	      return ret;
	    ret++;
	  }
      }
    else if (codTipoPag.trim ().equals ("05") || codTipoPag.trim ().equals ("06"))
      {
	Iterator it = getAlimentandos ().recuperarLista ().iterator ();
	while (it.hasNext ())
	  {
	    Alimentando alim = (Alimentando) it.next ();
	    if (alim.getNome ().asString ().toUpperCase ().equals (pag.getDependenteOuAlimentando ().asString ().toUpperCase ()))
	      return ret;
	    ret++;
	  }
      }
    return 0;
  }
  
  public String getNomeDependenteOuAlimentandoPorChave (String codTipoPag, String chave)
  {
    if (codTipoPag.trim ().equals ("03") || codTipoPag.trim ().equals ("04"))
      {
	Iterator it = getDependentes ().recuperarLista ().iterator ();
	while (it.hasNext ())
	  {
	    Dependente dep = (Dependente) it.next ();
	    if (dep.getChave ().equals (chave))
	      return dep.getNome ().getConteudoFormatado ();
	  }
      }
    else if (codTipoPag.trim ().equals ("05") || codTipoPag.trim ().equals ("06"))
      {
	Iterator it = getAlimentandos ().recuperarLista ().iterator ();
	while (it.hasNext ())
	  {
	    Alimentando alim = (Alimentando) it.next ();
	    if (alim.getChave ().equals (chave))
	      return alim.getNome ().getConteudoFormatado ();
	  }
      }
    return "";
  }
  
  public Valor recuperarSubTotalExclusivoTransporteRendTribExclusiva ()
  {
    RendTributacaoExclusiva rendTributacaoExclusiva = getRendTributacaoExclusiva ();
    Valor result = new Valor ();
    result.append ('+', recuperarRendaVariavelTribtExclusiva ());
    return result;
  }
  
  public Valor recuperarRendaVariavelTribtExclusiva ()
  {
    return rendTributacaoExclusiva.getGanhosRendaVariavel ();
  }
  
  public boolean simplesEhMelhor ()
  {
    Valor valImpostoRestituirSimpl = new Valor (getModeloSimplificada ().getImpostoRestituir ().getConteudoFormatado ());
    Valor valImpostoRestituirCompl = new Valor (getModeloCompleta ().getImpostoRestituir ().getConteudoFormatado ());
    Valor valImpostoPagarSimpl = new Valor (getModeloSimplificada ().getSaldoImpostoPagar ().getConteudoFormatado ());
    Valor valImpostoPagarCompl = new Valor (getModeloCompleta ().getSaldoImpostoPagar ().getConteudoFormatado ());
    if (valImpostoRestituirSimpl.comparacao (">", valImpostoRestituirCompl) || valImpostoPagarSimpl.comparacao ("<", valImpostoPagarCompl))
      return true;
    return false;
  }
  
  public static void main (String[] args)
  {
    IdentificadorDeclaracao id = new IdentificadorDeclaracao ();
    id.getCpf ().setConteudo ("00338419500");
    DeclaracaoIRPF dec = new DeclaracaoIRPF (id);
    RepositorioXMLDefault rep = new RepositorioXMLDefault ();
    try
      {
	rep.salvar (dec, "C:\\Dec2.xml");
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
}
