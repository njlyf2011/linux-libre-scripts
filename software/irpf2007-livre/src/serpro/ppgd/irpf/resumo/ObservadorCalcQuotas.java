/* ObservadorCalcQuotas - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.resumo;
import serpro.ppgd.negocio.Observador;

public class ObservadorCalcQuotas extends Observador
{
  private CalculoImposto calcImposto;
  
  public ObservadorCalcQuotas (CalculoImposto imposto)
  {
    calcImposto = imposto;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (! calcImposto.getSaldoImpostoPagar ().isVazio ())
      calcImposto.getValorQuota ().setConteudo (calcImposto.getSaldoImpostoPagar ().operacao ('/', calcImposto.getNumQuotas ().getConteudoFormatado ()));
  }
}
