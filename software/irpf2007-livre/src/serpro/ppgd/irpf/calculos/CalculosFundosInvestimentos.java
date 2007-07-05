/* CalculosFundosInvestimentos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import serpro.ppgd.irpf.rendavariavel.MesFundosInvestimentos;
import serpro.ppgd.irpf.rendavariavel.RendaVariavel;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class CalculosFundosInvestimentos extends Observador
{
  private MesFundosInvestimentos mesFundInvest;
  private RendaVariavel rendVar;
  
  public CalculosFundosInvestimentos (MesFundosInvestimentos aMesFundInvest, RendaVariavel variavel)
  {
    mesFundInvest = aMesFundInvest;
    rendVar = variavel;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if ("Resultado l\u00edquido do m\u00eas".equals (nomePropriedade) || "Resultado negativo at\u00e9 o m\u00eas anterior".equals (nomePropriedade))
      {
	if (mesFundInvest.getResultNegativoAnterior ().comparacao ("<", "0,00"))
	  mesFundInvest.getResultNegativoAnterior ().append ('*', "-1");
	else
	  {
	    Valor baseCalcImposto = mesFundInvest.getResultLiquidoMes ().operacao ('-', mesFundInvest.getResultNegativoAnterior ());
	    if (baseCalcImposto.comparacao (">", "0,00"))
	      {
		mesFundInvest.getBaseCalcImposto ().setConteudo (baseCalcImposto);
		mesFundInvest.getPrejuizoCompensar ().setConteudo ("0,00");
	      }
	    else
	      {
		mesFundInvest.getBaseCalcImposto ().setConteudo ("0,00");
		mesFundInvest.getPrejuizoCompensar ().setConteudo (baseCalcImposto.operacao ('*', "-1"));
	      }
	  }
      }
    else if ("Base de c\u00e1lculo do imposto".equals (nomePropriedade) || "Al\u00edquota do imposto".equals (nomePropriedade))
      {
	Valor impDevido = mesFundInvest.getBaseCalcImposto ().operacao ('*', mesFundInvest.getAliquotaImposto ().operacao ('/', "100,00"));
	mesFundInvest.getImpostoDevido ().setConteudo (impDevido);
      }
    else if ("Preju\u00edzo a compensar".equals (nomePropriedade))
      {
	Valor prejuizo = (Valor) observado;
	mesFundInvest.getResultNegativoAnterior ().setConteudo (prejuizo);
      }
    else if ("Imposto pago".equals (nomePropriedade))
      {
	Valor total = new Valor ();
	for (int i = 0; i <= 11; i++)
	  total.append ('+', rendVar.getFundInvest ().getMeses ()[i].getImpostoPago ());
	rendVar.getTotalImpostoPagoComFundInvest ().setConteudo (total);
      }
  }
}
