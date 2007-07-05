/* Operacoes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendavariavel;
import java.util.List;

import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class Operacoes extends ObjetoNegocio
{
  public static final String PROP_BASE_CALC = "BASE CALCULO";
  private Valor mercadoVistaAcoes = new Valor (this, "");
  private Valor mercadoVistaOuro = new Valor (this, "");
  private Valor mercadoVistaForaBolsa = new Valor (this, "");
  private Valor mercadoOpcoesAcoes = new Valor (this, "");
  private Valor mercadoOpcoesOuro = new Valor (this, "");
  private Valor mercadoOpcoesForaDeBolsa = new Valor (this, "");
  private Valor mercadoOpcoesOutros = new Valor (this, "");
  private Valor mercadoFuturoDolar = new Valor (this, "");
  private Valor mercadoFuturoIndices = new Valor (this, "");
  private Valor mercadoFuturoJuros = new Valor (this, "");
  private Valor mercadoFuturoOutros = new Valor (this, "");
  private Valor mercadoTermoAcoes = new Valor (this, "");
  private Valor mercadoTermoOutros = new Valor (this, "");
  private Valor resultadoLiquidoMes = new Valor (this, "");
  private Valor resultadoNegativoMesAnterior = new Valor (this, "");
  private Valor baseCalculoImposto = new Valor (this, "BASE CALCULO");
  private Valor prejuizoCompensar = new Valor (this, "");
  private Alfa aliquotaDoImposto = new Alfa (this, "");
  private Valor impostoDevido = new Valor (this, "");
  private String VALOR_ALIQUOTA;
  
  public Operacoes (String valorAliquota)
  {
    VALOR_ALIQUOTA = valorAliquota;
    adicionarObservadorValoresMercado (this);
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    atualizaOperacoes (this);
  }
  
  private void adicionarObservadorValoresMercado (Observador pObservador)
  {
    adicionaObservador (mercadoVistaAcoes, pObservador);
    adicionaObservador (mercadoVistaOuro, pObservador);
    adicionaObservador (mercadoVistaForaBolsa, pObservador);
    adicionaObservador (mercadoOpcoesAcoes, pObservador);
    adicionaObservador (mercadoTermoOutros, pObservador);
    adicionaObservador (mercadoTermoAcoes, pObservador);
    adicionaObservador (mercadoFuturoOutros, pObservador);
    adicionaObservador (mercadoFuturoJuros, pObservador);
    adicionaObservador (mercadoFuturoIndices, pObservador);
    adicionaObservador (mercadoFuturoDolar, pObservador);
    adicionaObservador (mercadoOpcoesOutros, pObservador);
    adicionaObservador (mercadoOpcoesOuro, pObservador);
    adicionaObservador (mercadoOpcoesForaDeBolsa, pObservador);
    adicionaObservador (resultadoNegativoMesAnterior, pObservador);
  }
  
  private void atualizaOperacoes (Operacoes operacoes)
  {
    Valor resultLiqMes = new Valor ();
    resultLiqMes.append ('+', operacoes.getMercadoVistaAcoes ());
    resultLiqMes.append ('+', operacoes.getMercadoVistaOuro ());
    resultLiqMes.append ('+', operacoes.getMercadoVistaForaBolsa ());
    resultLiqMes.append ('+', operacoes.getMercadoOpcoesAcoes ());
    resultLiqMes.append ('+', operacoes.getMercadoOpcoesOuro ());
    resultLiqMes.append ('+', operacoes.getMercadoOpcoesForaDeBolsa ());
    resultLiqMes.append ('+', operacoes.getMercadoOpcoesOutros ());
    resultLiqMes.append ('+', operacoes.getMercadoFuturoDolar ());
    resultLiqMes.append ('+', operacoes.getMercadoFuturoIndices ());
    resultLiqMes.append ('+', operacoes.getMercadoFuturoJuros ());
    resultLiqMes.append ('+', operacoes.getMercadoFuturoOutros ());
    resultLiqMes.append ('+', operacoes.getMercadoTermoAcoes ());
    resultLiqMes.append ('+', operacoes.getMercadoTermoOutros ());
    operacoes.getResultadoLiquidoMes ().setConteudo (resultLiqMes);
    if (operacoes.getResultadoLiquidoMes ().comparacao (">", operacoes.getResultadoNegativoMesAnterior ()))
      {
	Valor baseCalc = new Valor ();
	baseCalc.append ('+', operacoes.getResultadoLiquidoMes ());
	baseCalc.append ('-', operacoes.getResultadoNegativoMesAnterior ());
	operacoes.getBaseCalculoImposto ().setConteudo (baseCalc);
	operacoes.getPrejuizoCompensar ().clear ();
      }
    else
      {
	Valor prejuCompensar = new Valor ();
	prejuCompensar.append ('+', operacoes.getResultadoNegativoMesAnterior ());
	prejuCompensar.append ('-', operacoes.getResultadoLiquidoMes ());
	operacoes.getPrejuizoCompensar ().setConteudo (prejuCompensar);
	operacoes.getBaseCalculoImposto ().clear ();
      }
    if (operacoes.getBaseCalculoImposto ().comparacao (">", "0,00"))
      {
	String aliquotaImpostoDevido = "0," + VALOR_ALIQUOTA;
	operacoes.getImpostoDevido ().setConteudo (operacoes.getBaseCalculoImposto ().operacao ('*', aliquotaImpostoDevido));
      }
    else
      operacoes.getImpostoDevido ().clear ();
  }
  
  private void adicionaObservador (Informacao pObservado, Observador pObservador)
  {
    pObservado.addObservador (pObservador);
  }
  
  public Alfa getAliquotaDoImposto ()
  {
    return aliquotaDoImposto;
  }
  
  public void setAliquotaDoImposto (Alfa aliquotaDoImposto)
  {
    this.aliquotaDoImposto = aliquotaDoImposto;
  }
  
  public Valor getImpostoDevido ()
  {
    return impostoDevido;
  }
  
  public Valor getMercadoTermoAcoes ()
  {
    return mercadoTermoAcoes;
  }
  
  public Valor getMercadoTermoOutros ()
  {
    return mercadoTermoOutros;
  }
  
  public Valor getPrejuizoCompensar ()
  {
    return prejuizoCompensar;
  }
  
  public Valor getBaseCalculoImposto ()
  {
    return baseCalculoImposto;
  }
  
  public Valor getMercadoFuturoDolar ()
  {
    return mercadoFuturoDolar;
  }
  
  public Valor getMercadoFuturoIndices ()
  {
    return mercadoFuturoIndices;
  }
  
  public Valor getMercadoFuturoJuros ()
  {
    return mercadoFuturoJuros;
  }
  
  public Valor getMercadoFuturoOutros ()
  {
    return mercadoFuturoOutros;
  }
  
  public Valor getMercadoOpcoesAcoes ()
  {
    return mercadoOpcoesAcoes;
  }
  
  public Valor getMercadoOpcoesForaDeBolsa ()
  {
    return mercadoOpcoesForaDeBolsa;
  }
  
  public Valor getMercadoOpcoesOuro ()
  {
    return mercadoOpcoesOuro;
  }
  
  public Valor getMercadoOpcoesOutros ()
  {
    return mercadoOpcoesOutros;
  }
  
  public Valor getMercadoVistaAcoes ()
  {
    return mercadoVistaAcoes;
  }
  
  public Valor getMercadoVistaForaBolsa ()
  {
    return mercadoVistaForaBolsa;
  }
  
  public Valor getMercadoVistaOuro ()
  {
    return mercadoVistaOuro;
  }
  
  public Valor getResultadoLiquidoMes ()
  {
    return resultadoLiquidoMes;
  }
  
  public Valor getResultadoNegativoMesAnterior ()
  {
    return resultadoNegativoMesAnterior;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List listaCamposPendencia = recuperarCamposInformacao ();
    return listaCamposPendencia;
  }
  
  public boolean isVazio ()
  {
    boolean retorno = true;
    retorno = retorno && mercadoVistaAcoes.isVazio ();
    retorno = retorno && mercadoVistaOuro.isVazio ();
    retorno = retorno && mercadoVistaForaBolsa.isVazio ();
    retorno = retorno && mercadoOpcoesAcoes.isVazio ();
    retorno = retorno && mercadoOpcoesOuro.isVazio ();
    retorno = retorno && mercadoOpcoesForaDeBolsa.isVazio ();
    retorno = retorno && mercadoOpcoesOutros.isVazio ();
    retorno = retorno && mercadoFuturoDolar.isVazio ();
    retorno = retorno && mercadoFuturoIndices.isVazio ();
    retorno = retorno && mercadoFuturoJuros.isVazio ();
    retorno = retorno && mercadoFuturoOutros.isVazio ();
    retorno = retorno && mercadoTermoAcoes.isVazio ();
    retorno = retorno && mercadoTermoOutros.isVazio ();
    return retorno;
  }
}
