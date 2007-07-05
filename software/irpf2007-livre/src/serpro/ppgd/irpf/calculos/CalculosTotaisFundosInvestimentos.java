/* CalculosTotaisFundosInvestimentos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import serpro.ppgd.irpf.rendavariavel.FundosInvestimentos;
import serpro.ppgd.irpf.rendavariavel.MesFundosInvestimentos;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class CalculosTotaisFundosInvestimentos extends Observador
{
  private FundosInvestimentos fundInvest;
  
  public CalculosTotaisFundosInvestimentos (FundosInvestimentos fundInvest)
  {
    this.fundInvest = fundInvest;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    MesFundosInvestimentos[] meses = fundInvest.getMeses ();
    if ("Base de c\u00e1lculo do imposto".equals (nomePropriedade))
      {
	Valor totalBaseCalc = new Valor ();
	for (int i = 0; i < 12; i++)
	  totalBaseCalc.append ('+', meses[i].getBaseCalcImposto ());
	fundInvest.getTotalBaseCalcImposto ().setConteudo (totalBaseCalc);
      }
    else if ("Imposto pago".equals (nomePropriedade))
      {
	Valor totalImpostoPago = new Valor ();
	for (int i = 0; i < 12; i++)
	  totalImpostoPago.append ('+', meses[i].getImpostoPago ());
	fundInvest.getTotalImpostoPago ().setConteudo (totalImpostoPago);
      }
  }
}
