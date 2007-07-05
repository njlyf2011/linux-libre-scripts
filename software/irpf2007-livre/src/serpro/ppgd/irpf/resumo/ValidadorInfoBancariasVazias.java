/* ValidadorInfoBancariasVazias - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.resumo;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNaoNulo;

public class ValidadorInfoBancariasVazias extends ValidadorNaoNulo
{
  private CalculoImposto calcImposto;
  private Informacao info;
  
  public ValidadorInfoBancariasVazias (byte severidade, CalculoImposto aCalcImposto)
  {
    super (severidade);
    calcImposto = aCalcImposto;
  }
  
  public RetornoValidacao validarImplementado ()
  {
    if (calcImposto.getDebitoAutomatico ().asString ().equals ("autorizado") && (calcImposto.getBanco ().isVazio () || calcImposto.getContaCredito ().isVazio () || calcImposto.getAgencia ().isVazio ()))
      return new RetornoValidacao (tab.msg ("info_bancarias_vazia"), (byte) 3);
    return null;
  }
}
