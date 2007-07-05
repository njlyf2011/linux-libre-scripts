/* CalculosGanhosRendaVar - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import serpro.ppgd.irpf.rendTributacaoExclusiva.RendTributacaoExclusiva;
import serpro.ppgd.irpf.rendavariavel.RendaVariavel;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class CalculosGanhosRendaVar extends Observador
{
  private RendaVariavel rendaVariavel = null;
  private RendTributacaoExclusiva rendTributacao = null;
  
  public CalculosGanhosRendaVar (RendaVariavel rendaVar, RendTributacaoExclusiva rendTrib)
  {
    rendaVariavel = rendaVar;
    rendTributacao = rendTrib;
  }
  
  public void notifica (Object arg0, String arg1, Object arg2, Object arg3)
  {
    Valor totalBaseCalculoRV = rendaVariavel.getTotalBaseCalculo ();
    Valor totalIRFonteDayTradeRV = rendaVariavel.getTotalIRFonteDayTrade ();
    Valor totalImpostoPagoRV = rendaVariavel.getTotalImpostoPago ();
    Valor totalImpRetidoLei11033RV = rendaVariavel.getTotalImpostoRetidoFonteLei11033 ();
    Valor totalBaseCalculoFII = rendaVariavel.getFundInvest ().getTotalBaseCalcImposto ();
    Valor totalImpostoPagoFII = rendaVariavel.getFundInvest ().getTotalImpostoPago ();
    Valor ganhosRendaVariavel = new Valor ();
    ganhosRendaVariavel.append ('+', totalBaseCalculoRV);
    ganhosRendaVariavel.append ('-', totalIRFonteDayTradeRV);
    ganhosRendaVariavel.append ('-', totalImpostoPagoRV);
    ganhosRendaVariavel.append ('-', totalImpRetidoLei11033RV);
    ganhosRendaVariavel.append ('+', totalBaseCalculoFII);
    ganhosRendaVariavel.append ('-', totalImpostoPagoFII);
    if (ganhosRendaVariavel.comparacao ("<", "0,00"))
      ganhosRendaVariavel.clear ();
    rendTributacao.getGanhosRendaVariavel ().setConteudo (ganhosRendaVariavel);
  }
}
