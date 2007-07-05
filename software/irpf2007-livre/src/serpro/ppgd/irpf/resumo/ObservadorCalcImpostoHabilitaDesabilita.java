/* ObservadorCalcImpostoHabilitaDesabilita - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.resumo;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class ObservadorCalcImpostoHabilitaDesabilita extends Observador
{
  private CalculoImposto calculoImposto = null;
  
  public ObservadorCalcImpostoHabilitaDesabilita (CalculoImposto calc)
  {
    calculoImposto = calc;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    habilitadesabilitaDados ();
  }
  
  public void habilitadesabilitaDados ()
  {
    boolean impostoAPagar = calculoImposto.getSaldoImpostoPagar ().comparacao (">", "0,00");
    calculoImposto.getNumQuotas ().setHabilitado (impostoAPagar);
    if (! impostoAPagar)
      {
	calculoImposto.getValorQuota ().clear ();
	calculoImposto.getNumQuotas ().clear ();
      }
    if (calculoImposto.getSaldoImpostoPagar ().comparacao (">", "0,00"))
      {
	calculoImposto.getNumQuotas ().setLimiteMinimo (1);
	if (calculoImposto.getSaldoImpostoPagar ().comparacao ("<", "100,00"))
	  {
	    calculoImposto.getNumQuotas ().setConteudo (1);
	    calculoImposto.getNumQuotas ().setHabilitado (false);
	  }
	else
	  {
	    Valor maxQuotas = calculoImposto.getSaldoImpostoPagar ().operacao ('/', "50,00");
	    if (maxQuotas.comparacao (">", "8,00"))
	      maxQuotas.setConteudo ("8,00");
	    if (calculoImposto.getNumQuotas ().asInteger () == 0)
	      calculoImposto.getNumQuotas ().setConteudo (1);
	    calculoImposto.getNumQuotas ().setHabilitado (true);
	    calculoImposto.getNumQuotas ().setLimiteMaximo (maxQuotas.getParteInteiraAsLong ().intValue ());
	  }
      }
    else
      {
	calculoImposto.getNumQuotas ().setLimiteMinimo (0);
	calculoImposto.getNumQuotas ().setConteudo (0);
	calculoImposto.getNumQuotas ().clear ();
	calculoImposto.getValorQuota ().clear ();
      }
  }
}
