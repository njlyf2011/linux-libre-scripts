/* GanhosLiquidosOuPerdas - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendavariavel;
import java.util.List;

import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class GanhosLiquidosOuPerdas extends ObjetoNegocio
{
  public static final String PROP_IR_FONTE_DAYTRADE = "IRFONTEDAYTRADE";
  public static final String PROP_IMP_RETIDO_LEI_11033 = "IR LEI 11033";
  public static final String PROP_IMP_PAGO = "IMP PAGO";
  public static final String MES = "M\u00eas";
  private Operacoes operacoesComuns = new Operacoes ("15");
  private Operacoes operacoesDayTrade = new Operacoes ("20");
  private Valor totalImpostoDevido = new Valor (this, "");
  private Valor irFonteDayTradeMesAtual = new Valor (this, "IRFONTEDAYTRADE");
  private Valor irFonteDayTradeMesesAnteriores = new Valor (this, "");
  private Valor irFonteDayTradeAcompensar = new Valor (this, "");
  private Valor impostoApagar = new Valor (this, "");
  private Valor impostoPago = new Valor (this, "IMP PAGO");
  private Valor impostoRetidoFonteLei11033 = new Valor (this, "IR LEI 11033");
  public static final String ALIQUOTA_DAYTRADE = "20";
  public static final String ALIQUOTA_COMUM = "15";
  
  public GanhosLiquidosOuPerdas ()
  {
    getOperacoesComuns ().getAliquotaDoImposto ().setConteudo ("15%");
    getOperacoesDayTrade ().getAliquotaDoImposto ().setConteudo ("20%");
    getOperacoesComuns ().getResultadoLiquidoMes ().setReadOnly (true);
    getOperacoesDayTrade ().getResultadoLiquidoMes ().setReadOnly (true);
    getOperacoesComuns ().getBaseCalculoImposto ().setReadOnly (true);
    getOperacoesDayTrade ().getBaseCalculoImposto ().setReadOnly (true);
    getOperacoesComuns ().getPrejuizoCompensar ().setReadOnly (true);
    getOperacoesDayTrade ().getPrejuizoCompensar ().setReadOnly (true);
    getOperacoesComuns ().getAliquotaDoImposto ().setHabilitado (false);
    getOperacoesDayTrade ().getAliquotaDoImposto ().setHabilitado (false);
    getOperacoesComuns ().getImpostoDevido ().setReadOnly (true);
    getOperacoesDayTrade ().getImpostoDevido ().setReadOnly (true);
    getOperacoesComuns ().getResultadoNegativoMesAnterior ().setHabilitado (false);
    getOperacoesDayTrade ().getResultadoNegativoMesAnterior ().setHabilitado (false);
    getTotalImpostoDevido ().setReadOnly (true);
    getIrFonteDayTradeMesesAnteriores ().setReadOnly (true);
    getIrFonteDayTradeAcompensar ().setReadOnly (true);
    getImpostoApagar ().setReadOnly (true);
    getImpostoRetidoFonteLei11033 ().addValidador (new ValidadorImpostoRetidoLei11033 (this));
    ObservadorHabDesabIrRetidoFonteLei11033 observadorHabDesabIrRetidoFonteLei11033 = new ObservadorHabDesabIrRetidoFonteLei11033 (this);
    getTotalImpostoDevido ().addObservador (observadorHabDesabIrRetidoFonteLei11033);
    getIrFonteDayTradeMesAtual ().addObservador (observadorHabDesabIrRetidoFonteLei11033);
    getIrFonteDayTradeMesesAnteriores ().addObservador (observadorHabDesabIrRetidoFonteLei11033);
    getOperacoesComuns ().getImpostoDevido ().addObservador (this);
    getOperacoesDayTrade ().getImpostoDevido ().addObservador (this);
    getIrFonteDayTradeMesAtual ().addObservador (this);
    getIrFonteDayTradeMesesAnteriores ().addObservador (this);
    getImpostoRetidoFonteLei11033 ().addObservador (this);
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    totalImpostoDevido.setConteudo (getOperacoesComuns ().getImpostoDevido ().operacao ('+', getOperacoesDayTrade ().getImpostoDevido ()));
    Valor irDayTradeMesMaisIRDayTradeMesAnteriores = new Valor ();
    irDayTradeMesMaisIRDayTradeMesAnteriores.append ('+', getIrFonteDayTradeMesAtual ());
    irDayTradeMesMaisIRDayTradeMesAnteriores.append ('+', getIrFonteDayTradeMesesAnteriores ());
    if (irDayTradeMesMaisIRDayTradeMesAnteriores.comparacao (">", getTotalImpostoDevido ()))
      {
	Valor irDayTradeComp = new Valor ();
	irDayTradeComp.append ('+', irDayTradeMesMaisIRDayTradeMesAnteriores);
	irDayTradeComp.append ('-', getTotalImpostoDevido ());
	getIrFonteDayTradeAcompensar ().setConteudo (irDayTradeComp);
	getImpostoApagar ().clear ();
      }
    else
      {
	getIrFonteDayTradeAcompensar ().clear ();
	Valor impAPag = new Valor ();
	impAPag.append ('+', getTotalImpostoDevido ());
	impAPag.append ('-', irDayTradeMesMaisIRDayTradeMesAnteriores);
	impAPag.append ('-', getImpostoRetidoFonteLei11033 ());
	getImpostoApagar ().setConteudo (impAPag);
      }
  }
  
  public void adicionarObservadorCalculosRendaVariavel (Observador pObservador)
  {
    operacoesComuns.getBaseCalculoImposto ().addObservador (pObservador);
    operacoesDayTrade.getBaseCalculoImposto ().addObservador (pObservador);
    irFonteDayTradeMesAtual.addObservador (pObservador);
    impostoPago.addObservador (pObservador);
    impostoRetidoFonteLei11033.addObservador (pObservador);
  }
  
  public Operacoes getOperacoesComuns ()
  {
    return operacoesComuns;
  }
  
  public Operacoes getOperacoesDayTrade ()
  {
    return operacoesDayTrade;
  }
  
  public Valor getImpostoApagar ()
  {
    return impostoApagar;
  }
  
  public Valor getImpostoPago ()
  {
    return impostoPago;
  }
  
  public Valor getIrFonteDayTradeAcompensar ()
  {
    return irFonteDayTradeAcompensar;
  }
  
  public Valor getIrFonteDayTradeMesAtual ()
  {
    return irFonteDayTradeMesAtual;
  }
  
  public Valor getIrFonteDayTradeMesesAnteriores ()
  {
    return irFonteDayTradeMesesAnteriores;
  }
  
  public Valor getTotalImpostoDevido ()
  {
    return totalImpostoDevido;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List listaCamposPendencia = super.recuperarCamposInformacao ();
    return listaCamposPendencia;
  }
  
  public boolean estaVazio ()
  {
    boolean retorno = operacoesComuns.isVazio ();
    retorno = retorno && operacoesDayTrade.isVazio ();
    retorno = retorno && irFonteDayTradeMesAtual.isVazio ();
    retorno = retorno && impostoPago.isVazio ();
    retorno = retorno && impostoRetidoFonteLei11033.isVazio ();
    retorno = retorno && operacoesComuns.getResultadoLiquidoMes ().isVazio ();
    retorno = retorno && operacoesComuns.getResultadoNegativoMesAnterior ().isVazio ();
    retorno = retorno && operacoesDayTrade.getResultadoLiquidoMes ().isVazio ();
    retorno = retorno && operacoesDayTrade.getResultadoNegativoMesAnterior ().isVazio ();
    return retorno;
  }
  
  public Valor getImpostoRetidoFonteLei11033 ()
  {
    return impostoRetidoFonteLei11033;
  }
  
  public void clear ()
  {
    super.clear ();
    operacoesComuns.clear ();
    operacoesDayTrade.clear ();
  }
}
