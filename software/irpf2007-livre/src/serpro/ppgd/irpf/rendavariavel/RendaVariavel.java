/* RendaVariavel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendavariavel;
import java.util.Hashtable;

import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class RendaVariavel extends ObjetoNegocio
{
  private GanhosLiquidosOuPerdas janeiro = new GanhosLiquidosOuPerdas ();
  private GanhosLiquidosOuPerdas fevereiro = new GanhosLiquidosOuPerdas ();
  private GanhosLiquidosOuPerdas marco = new GanhosLiquidosOuPerdas ();
  private GanhosLiquidosOuPerdas abril = new GanhosLiquidosOuPerdas ();
  private GanhosLiquidosOuPerdas maio = new GanhosLiquidosOuPerdas ();
  private GanhosLiquidosOuPerdas junho = new GanhosLiquidosOuPerdas ();
  private GanhosLiquidosOuPerdas julho = new GanhosLiquidosOuPerdas ();
  private GanhosLiquidosOuPerdas agosto = new GanhosLiquidosOuPerdas ();
  private GanhosLiquidosOuPerdas setembro = new GanhosLiquidosOuPerdas ();
  private GanhosLiquidosOuPerdas outubro = new GanhosLiquidosOuPerdas ();
  private GanhosLiquidosOuPerdas novembro = new GanhosLiquidosOuPerdas ();
  private GanhosLiquidosOuPerdas dezembro = new GanhosLiquidosOuPerdas ();
  private GanhosLiquidosOuPerdas[] meses = { janeiro, fevereiro, marco, abril, maio, junho, julho, agosto, setembro, outubro, novembro, dezembro };
  private FundosInvestimentos fundInvest = new FundosInvestimentos ();
  private Valor totalBaseCalculo = new Valor (this, "");
  private Valor totalIRFonteDayTrade = new Valor (this, "");
  private Valor totalImpostoRetidoFonteLei11033 = new Valor (this, "");
  private Valor totalImpostoPago = new Valor (this, "");
  private Valor totalImpostoPagoComFundInvest = new Valor (this, "");
  
  public RendaVariavel ()
  {
    getJaneiro ().getOperacoesComuns ().getResultadoNegativoMesAnterior ().setHabilitado (true);
    getJaneiro ().getOperacoesDayTrade ().getResultadoNegativoMesAnterior ().setHabilitado (true);
    addObservadorAtualizaIrFonteDayTradeProxMes (janeiro, fevereiro);
    addObservadorAtualizaIrFonteDayTradeProxMes (fevereiro, marco);
    addObservadorAtualizaIrFonteDayTradeProxMes (marco, abril);
    addObservadorAtualizaIrFonteDayTradeProxMes (abril, maio);
    addObservadorAtualizaIrFonteDayTradeProxMes (maio, junho);
    addObservadorAtualizaIrFonteDayTradeProxMes (junho, julho);
    addObservadorAtualizaIrFonteDayTradeProxMes (julho, agosto);
    addObservadorAtualizaIrFonteDayTradeProxMes (agosto, setembro);
    addObservadorAtualizaIrFonteDayTradeProxMes (setembro, outubro);
    addObservadorAtualizaIrFonteDayTradeProxMes (outubro, novembro);
    addObservadorAtualizaIrFonteDayTradeProxMes (novembro, dezembro);
    addObservadorAtualizaResultadoNegativoProxMes (janeiro, fevereiro);
    addObservadorAtualizaResultadoNegativoProxMes (fevereiro, marco);
    addObservadorAtualizaResultadoNegativoProxMes (marco, abril);
    addObservadorAtualizaResultadoNegativoProxMes (abril, maio);
    addObservadorAtualizaResultadoNegativoProxMes (maio, junho);
    addObservadorAtualizaResultadoNegativoProxMes (junho, julho);
    addObservadorAtualizaResultadoNegativoProxMes (julho, agosto);
    addObservadorAtualizaResultadoNegativoProxMes (agosto, setembro);
    addObservadorAtualizaResultadoNegativoProxMes (setembro, outubro);
    addObservadorAtualizaResultadoNegativoProxMes (outubro, novembro);
    addObservadorAtualizaResultadoNegativoProxMes (novembro, dezembro);
  }
  
  private void addObservadorAtualizaIrFonteDayTradeProxMes (final GanhosLiquidosOuPerdas ganhosMesAnt, final GanhosLiquidosOuPerdas ganhosMesPost)
  {
    class ObservadorAtualizaIRDayTradeProxMes extends Observador
    {
      GanhosLiquidosOuPerdas ganhoMesAnterior = null;
      GanhosLiquidosOuPerdas ganhoMesPosterior = null;
      
      public ObservadorAtualizaIRDayTradeProxMes ()
      {
	ganhoMesAnterior = ganhosMesAnt;
	ganhoMesPosterior = ganhosMesPost;
      }
      
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	ganhoMesPosterior.getIrFonteDayTradeMesesAnteriores ().setConteudo (ganhoMesAnterior.getIrFonteDayTradeAcompensar ());
      }
    };
    ganhosMesAnt.getIrFonteDayTradeAcompensar ().addObservador (new ObservadorAtualizaIRDayTradeProxMes ());
  }
  
  public String obterMesFormatoNumerico (GanhosLiquidosOuPerdas ganhos)
  {
    if (ganhos.equals (janeiro))
      return "01";
    if (ganhos.equals (fevereiro))
      return "02";
    if (ganhos.equals (marco))
      return "03";
    if (ganhos.equals (abril))
      return "04";
    if (ganhos.equals (maio))
      return "05";
    if (ganhos.equals (junho))
      return "06";
    if (ganhos.equals (julho))
      return "07";
    if (ganhos.equals (agosto))
      return "08";
    if (ganhos.equals (setembro))
      return "09";
    if (ganhos.equals (outubro))
      return "10";
    if (ganhos.equals (novembro))
      return "11";
    if (ganhos.equals (dezembro))
      return "12";
    return null;
  }
  
  private void addObservadorAtualizaResultadoNegativoProxMes (final GanhosLiquidosOuPerdas ganhosMesAnt, final GanhosLiquidosOuPerdas ganhosMesPost)
  {
    class ObservadorAtualizaResultadoNegProxMes extends Observador
    {
      GanhosLiquidosOuPerdas ganhoMesAnterior = null;
      GanhosLiquidosOuPerdas ganhoMesPosterior = null;
      
      public ObservadorAtualizaResultadoNegProxMes ()
      {
	ganhoMesAnterior = ganhosMesAnt;
	ganhoMesPosterior = ganhosMesPost;
      }
      
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	if (((Informacao) observado).getOwner ().equals (ganhoMesAnterior.getOperacoesComuns ()))
	  ganhoMesPosterior.getOperacoesComuns ().getResultadoNegativoMesAnterior ().setConteudo (ganhoMesAnterior.getOperacoesComuns ().getPrejuizoCompensar ());
	else
	  ganhoMesPosterior.getOperacoesDayTrade ().getResultadoNegativoMesAnterior ().setConteudo (ganhoMesAnterior.getOperacoesDayTrade ().getPrejuizoCompensar ());
      }
    };
    ganhosMesAnt.getOperacoesComuns ().getPrejuizoCompensar ().addObservador (new ObservadorAtualizaResultadoNegProxMes ());
    ganhosMesAnt.getOperacoesDayTrade ().getPrejuizoCompensar ().addObservador (new ObservadorAtualizaResultadoNegProxMes ());
  }
  
  public void adicionarObservadorCalculosTotaisRendaVariavel (Observador pObservador)
  {
    janeiro.adicionarObservadorCalculosRendaVariavel (pObservador);
    fevereiro.adicionarObservadorCalculosRendaVariavel (pObservador);
    marco.adicionarObservadorCalculosRendaVariavel (pObservador);
    abril.adicionarObservadorCalculosRendaVariavel (pObservador);
    maio.adicionarObservadorCalculosRendaVariavel (pObservador);
    junho.adicionarObservadorCalculosRendaVariavel (pObservador);
    julho.adicionarObservadorCalculosRendaVariavel (pObservador);
    agosto.adicionarObservadorCalculosRendaVariavel (pObservador);
    setembro.adicionarObservadorCalculosRendaVariavel (pObservador);
    outubro.adicionarObservadorCalculosRendaVariavel (pObservador);
    novembro.adicionarObservadorCalculosRendaVariavel (pObservador);
    dezembro.adicionarObservadorCalculosRendaVariavel (pObservador);
  }
  
  public void adicionarObservGanhosRendaVar (Observador pObservador)
  {
    getTotalBaseCalculo ().addObservador (pObservador);
    getTotalIRFonteDayTrade ().addObservador (pObservador);
    getTotalImpostoPago ().addObservador (pObservador);
    getTotalImpostoRetidoFonteLei11033 ().addObservador (pObservador);
  }
  
  public Valor getTotalBaseCalculo ()
  {
    return totalBaseCalculo;
  }
  
  public Valor getTotalIRFonteDayTrade ()
  {
    return totalIRFonteDayTrade;
  }
  
  public GanhosLiquidosOuPerdas getAbril ()
  {
    return abril;
  }
  
  public GanhosLiquidosOuPerdas getAgosto ()
  {
    return agosto;
  }
  
  public GanhosLiquidosOuPerdas getDezembro ()
  {
    return dezembro;
  }
  
  public GanhosLiquidosOuPerdas getFevereiro ()
  {
    return fevereiro;
  }
  
  public GanhosLiquidosOuPerdas getJaneiro ()
  {
    return janeiro;
  }
  
  public GanhosLiquidosOuPerdas getJulho ()
  {
    return julho;
  }
  
  public GanhosLiquidosOuPerdas getJunho ()
  {
    return junho;
  }
  
  public GanhosLiquidosOuPerdas getMaio ()
  {
    return maio;
  }
  
  public GanhosLiquidosOuPerdas getMarco ()
  {
    return marco;
  }
  
  public GanhosLiquidosOuPerdas getNovembro ()
  {
    return novembro;
  }
  
  public GanhosLiquidosOuPerdas getOutubro ()
  {
    return outubro;
  }
  
  public GanhosLiquidosOuPerdas getSetembro ()
  {
    return setembro;
  }
  
  public GanhosLiquidosOuPerdas getGanhosPorIndice (int mes)
  {
    if (mes == 0)
      return janeiro;
    if (mes == 1)
      return fevereiro;
    if (mes == 2)
      return marco;
    if (mes == 3)
      return abril;
    if (mes == 4)
      return maio;
    if (mes == 5)
      return junho;
    if (mes == 6)
      return julho;
    if (mes == 7)
      return agosto;
    if (mes == 8)
      return setembro;
    if (mes == 9)
      return outubro;
    if (mes == 10)
      return novembro;
    if (mes == 11)
      return dezembro;
    return null;
  }
  
  public Valor getTotalImpostoRetidoFonteLei11033 ()
  {
    return totalImpostoRetidoFonteLei11033;
  }
  
  public Valor getTotalImpostoPago ()
  {
    return totalImpostoPago;
  }
  
  public boolean temResultadoLiquido ()
  {
    for (int i = 0; i < 12; i++)
      {
	if (! meses[i].getOperacoesComuns ().getResultadoLiquidoMes ().isVazio () || ! meses[i].getOperacoesDayTrade ().getResultadoLiquidoMes ().isVazio ())
	  return true;
      }
    return false;
  }
  
  public Hashtable obterTotalAnual ()
  {
    Hashtable retorno = new Hashtable ();
    Valor valTotalResLiquidos = new Valor ();
    Valor valTotalResNegativo = new Valor ();
    Valor valTotalBaseCalcImposto = new Valor ();
    Valor valTotalPrejuizoCompensar = new Valor ();
    Valor valTotalImpostoDevidoOp = new Valor ();
    Valor valTotalImpostoDevidoConsolidacao = new Valor ();
    Valor valTotalIRDayTradeMesesAnt = new Valor ();
    Valor valTotalIRDayTradeCompensar = new Valor ();
    Valor valTotalImpostoPagar = new Valor ();
    GanhosLiquidosOuPerdas mesAtual = null;
    for (int i = 0; i <= 11; i++)
      {
	mesAtual = getGanhosPorIndice (i);
	valTotalResLiquidos.append ('+', mesAtual.getOperacoesComuns ().getResultadoLiquidoMes ());
	valTotalResLiquidos.append ('+', mesAtual.getOperacoesDayTrade ().getResultadoLiquidoMes ());
	valTotalBaseCalcImposto.append ('+', mesAtual.getOperacoesComuns ().getBaseCalculoImposto ());
	valTotalBaseCalcImposto.append ('+', mesAtual.getOperacoesDayTrade ().getBaseCalculoImposto ());
	valTotalImpostoDevidoOp.append ('+', mesAtual.getOperacoesComuns ().getImpostoDevido ());
	valTotalImpostoDevidoOp.append ('+', mesAtual.getOperacoesDayTrade ().getImpostoDevido ());
	valTotalImpostoDevidoConsolidacao.append ('+', mesAtual.getTotalImpostoDevido ());
	valTotalImpostoPagar.append ('+', mesAtual.getImpostoApagar ());
	if (mesAtual.equals (dezembro))
	  {
	    valTotalResNegativo.setConteudo (mesAtual.getOperacoesComuns ().getResultadoNegativoMesAnterior ());
	    valTotalResNegativo.append ('+', mesAtual.getOperacoesDayTrade ().getResultadoNegativoMesAnterior ());
	    valTotalPrejuizoCompensar.setConteudo (mesAtual.getOperacoesComuns ().getPrejuizoCompensar ());
	    valTotalPrejuizoCompensar.append ('+', mesAtual.getOperacoesDayTrade ().getPrejuizoCompensar ());
	    valTotalIRDayTradeMesesAnt.setConteudo (mesAtual.getIrFonteDayTradeMesesAnteriores ());
	    valTotalIRDayTradeCompensar.setConteudo (mesAtual.getIrFonteDayTradeAcompensar ());
	  }
      }
    retorno.put ("TotalResultadosLiquidos", valTotalResLiquidos);
    retorno.put ("TotalResultadosNegativos", valTotalResNegativo);
    retorno.put ("BaseCalculoImposto", valTotalBaseCalcImposto);
    retorno.put ("PrejuizoCompensar", valTotalPrejuizoCompensar);
    retorno.put ("ImpostoDevido", valTotalImpostoDevidoOp);
    retorno.put ("ImpostoDevidoConsolidacao", valTotalImpostoDevidoConsolidacao);
    retorno.put ("IRDayTradeMesesAnteriores", valTotalIRDayTradeMesesAnt);
    retorno.put ("IRDayTradeCompensar", valTotalIRDayTradeCompensar);
    retorno.put ("TotalImpostoAPagar", valTotalImpostoPagar);
    return retorno;
  }
  
  public void clear ()
  {
    janeiro.clear ();
    fevereiro.clear ();
    marco.clear ();
    abril.clear ();
    maio.clear ();
    junho.clear ();
    julho.clear ();
    agosto.clear ();
    setembro.clear ();
    outubro.clear ();
    novembro.clear ();
    dezembro.clear ();
  }
  
  public boolean isVazio ()
  {
    boolean ret = totalBaseCalculo.isVazio ();
    ret = ret && totalImpostoPago.isVazio ();
    ret = ret && totalImpostoRetidoFonteLei11033.isVazio ();
    ret = ret && totalIRFonteDayTrade.isVazio ();
    for (int i = 0; i < 12 && ret; i++)
      ret = getGanhosPorIndice (i).isVazio ();
    return ret;
  }
  
  public FundosInvestimentos getFundInvest ()
  {
    return fundInvest;
  }
  
  public void setTotalImpostoPagoComFundInvest (Valor totalImpostoPagoComFundInvest)
  {
    this.totalImpostoPagoComFundInvest = totalImpostoPagoComFundInvest;
  }
  
  public Valor getTotalImpostoPagoComFundInvest ()
  {
    return totalImpostoPagoComFundInvest;
  }
}
