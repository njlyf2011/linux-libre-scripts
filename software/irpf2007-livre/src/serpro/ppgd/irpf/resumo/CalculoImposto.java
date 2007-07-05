/* CalculoImposto - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.resumo;
import java.util.List;

import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.contribuinte.Contribuinte;
import serpro.ppgd.irpf.tabelas.CadastroTabelasIRPF;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.Inteiro;
import serpro.ppgd.negocio.Logico;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNaoNulo;

public class CalculoImposto extends ObjetoNegocio
{
  public static final String CAMPO_DEBITO_AUTOMATICO = "D\u00e9bito autom\u00e1tico";
  private Valor baseCalculo = new Valor (this, "");
  private Valor imposto = new Valor (this, "");
  private Valor deducaoIncentivo = new Valor (this, "");
  private Valor totalContribEmpregadoDomestico = new Valor (this, "");
  private Valor impostoDevido = new Valor (this, "");
  private Valor impostoDevidoII = new Valor (this, "");
  private Valor impostoRetidoFonteTitular = new Valor (this, "");
  private Valor impostoRetidoFonteDependentes = new Valor (this, "");
  private Valor carneLeao = new Valor (this, "");
  private Valor impostoComplementar = new Valor (this, "");
  private Valor impostoPagoExterior = new Valor (this, "");
  private Valor impostoRetidoFonteLei11033 = new Valor (this, "");
  private Valor totalImpostoPago = new Valor (this, "");
  private Valor impostoRestituir = new Valor (this, "");
  private Logico debitoAutomatico = new Logico (this, "D\u00e9bito autom\u00e1tico");
  private Codigo banco = new Codigo (this, "Banco", CadastroTabelasIRPF.recuperarBancos ());
  private Alfa agencia = new Alfa (this, "Ag\u00eancia");
  private Alfa dvAgencia = new Alfa (this, "");
  private Alfa contaCredito = new Alfa (this, "Conta para d\u00e9bito");
  private Alfa dvContaCredito = new Alfa (this, "");
  private Valor saldoImpostoPagar = new Valor (this, "");
  private Inteiro numQuotas = new Inteiro (this, "");
  private Valor valorQuota = new Valor (this, "");
  private Valor gcap = new Valor (this, "");
  private Valor rendPJRecebidoTitular = new Valor (this, "");
  private Valor rendPJRecebidoDependentes = new Valor (this, "");
  private Valor rendRecebidoPF = new Valor (this, "");
  private Valor rendRecebidoExterior = new Valor (this, "");
  private Valor resultadoTributavelAR = new Valor (this, "");
  private Valor totalResultadosTributaveis = new Valor (this, "");
  private Valor descontoSimplificado = new Valor (this, "");
  private Valor carneLeaoMaisImpostoComplementar = new Valor (this, "");
  private ObservadorCalcImpostoHabilitaDesabilita observadorCalcImpostoHabilitaDesabilita = new ObservadorCalcImpostoHabilitaDesabilita (this);
  private ObservadorDebitoAutomatico observadorDebitoAutomatico;
  private IdentificadorDeclaracao identificadorDec;
  
  public CalculoImposto (IdentificadorDeclaracao aIdentificador, Contribuinte contribuinte)
  {
    setFicha ("C\u00e1lculo do Imposto");
    identificadorDec = aIdentificador;
    observadorDebitoAutomatico = new ObservadorDebitoAutomatico (this, identificadorDec, contribuinte);
    getImposto ().setReadOnly (true);
    getDeducaoIncentivo ().setReadOnly (true);
    getTotalContribEmpregadoDomestico ().setReadOnly (true);
    getImpostoDevido ().setReadOnly (true);
    getImpostoDevidoII ().setReadOnly (true);
    getCarneLeao ().setReadOnly (true);
    getImpostoComplementar ().setReadOnly (true);
    getImpostoPagoExterior ().setReadOnly (true);
    getTotalImpostoPago ().setReadOnly (true);
    getRendPJRecebidoTitular ().setReadOnly (true);
    getRendPJRecebidoDependentes ().setReadOnly (true);
    getRendRecebidoPF ().setReadOnly (true);
    getRendRecebidoExterior ().setReadOnly (true);
    getResultadoTributavelAR ().setReadOnly (true);
    getTotalResultadosTributaveis ().setReadOnly (true);
    getDescontoSimplificado ().setReadOnly (true);
    getBaseCalculo ().setReadOnly (true);
    getImpostoRetidoFonteTitular ().setReadOnly (true);
    getImpostoRetidoFonteDependentes ().setReadOnly (true);
    getCarneLeaoMaisImpostoComplementar ().setReadOnly (true);
    getImpostoRetidoFonteLei11033 ().setReadOnly (true);
    getSaldoImpostoPagar ().setReadOnly (true);
    getImpostoRestituir ().setReadOnly (true);
    getValorQuota ().setReadOnly (true);
    getDebitoAutomatico ().addOpcao ("autorizado", "Sim");
    getDebitoAutomatico ().addOpcao ("N", "N\u00e3o");
    getDebitoAutomatico ().setConteudo ("N");
    getDebitoAutomatico ().addObservador (observadorDebitoAutomatico);
    identificadorDec.getDeclaracaoRetificadora ().addObservador (observadorDebitoAutomatico);
    contribuinte.getExterior ().addObservador (observadorDebitoAutomatico);
    observadorDebitoAutomatico.habilitadesabilitaDadosBancarios ();
    getSaldoImpostoPagar ().addObservador (observadorCalcImpostoHabilitaDesabilita);
    getSaldoImpostoPagar ().addObservador (observadorDebitoAutomatico);
    observadorCalcImpostoHabilitaDesabilita.habilitadesabilitaDados ();
    getImpostoRestituir ().addObservador (observadorCalcImpostoHabilitaDesabilita);
    getImpostoRestituir ().addObservador (observadorDebitoAutomatico);
    getDebitoAutomatico ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (! getDebitoAutomatico ().isHabilitado ())
	  return null;
	setMensagemValidacao (tab.msg ("debito_autom"));
	return super.validarImplementado ();
      }
    });
    getNumQuotas ().setLimiteMinimo (1);
    getNumQuotas ().setLimiteMaximo (8);
    getNumQuotas ().addObservador (new ObservadorCalcQuotas (this));
    getSaldoImpostoPagar ().addObservador (new ObservadorCalcQuotas (this));
    getBanco ().addValidador (new ValidadorInfoBancariasVazias ((byte) 3, this));
  }
  
  public Alfa getAgencia ()
  {
    return agencia;
  }
  
  public Codigo getBanco ()
  {
    return banco;
  }
  
  public Valor getBaseCalculo ()
  {
    return baseCalculo;
  }
  
  public Valor getCarneLeao ()
  {
    return carneLeao;
  }
  
  public Alfa getContaCredito ()
  {
    return contaCredito;
  }
  
  public Valor getDeducaoIncentivo ()
  {
    return deducaoIncentivo;
  }
  
  public Alfa getDvAgencia ()
  {
    return dvAgencia;
  }
  
  public Alfa getDvContaCredito ()
  {
    return dvContaCredito;
  }
  
  public Valor getGcap ()
  {
    return gcap;
  }
  
  public Valor getImposto ()
  {
    return imposto;
  }
  
  public Valor getImpostoComplementar ()
  {
    return impostoComplementar;
  }
  
  public Valor getImpostoDevido ()
  {
    return impostoDevido;
  }
  
  public Valor getImpostoPagoExterior ()
  {
    return impostoPagoExterior;
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
  
  public Inteiro getNumQuotas ()
  {
    return numQuotas;
  }
  
  public Valor getSaldoImpostoPagar ()
  {
    return saldoImpostoPagar;
  }
  
  public Valor getTotalImpostoPago ()
  {
    return totalImpostoPago;
  }
  
  public Valor getValorQuota ()
  {
    return valorQuota;
  }
  
  public Valor getCarneLeaoMaisImpostoComplementar ()
  {
    return carneLeaoMaisImpostoComplementar;
  }
  
  public Valor getDescontoSimplificado ()
  {
    return descontoSimplificado;
  }
  
  public Valor getRendPJRecebidoDependentes ()
  {
    return rendPJRecebidoDependentes;
  }
  
  public Valor getRendRecebidoPF ()
  {
    return rendRecebidoPF;
  }
  
  public Valor getRendPJRecebidoTitular ()
  {
    return rendPJRecebidoTitular;
  }
  
  public Valor getRendRecebidoExterior ()
  {
    return rendRecebidoExterior;
  }
  
  public Valor getResultadoTributavelAR ()
  {
    return resultadoTributavelAR;
  }
  
  public Valor getTotalResultadosTributaveis ()
  {
    return totalResultadosTributaveis;
  }
  
  public Logico getDebitoAutomatico ()
  {
    return debitoAutomatico;
  }
  
  public Valor getTotalContribEmpregadoDomestico ()
  {
    return totalContribEmpregadoDomestico;
  }
  
  public Valor getImpostoDevidoII ()
  {
    return impostoDevidoII;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List lista = super.recuperarListaCamposPendencia ();
    lista.add (getBanco ());
    lista.add (getAgencia ());
    lista.add (getContaCredito ());
    return lista;
  }
}
