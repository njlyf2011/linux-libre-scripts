/* MesFundosInvestimentos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendavariavel;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;

public class MesFundosInvestimentos extends ObjetoNegocio
{
  public static final String IMPOSTO_PAGO = "Imposto pago";
  public static final String IMPOSTO_DEVIDO = "Imposto devido";
  public static final String ALÍQUOTA_DO_IMPOSTO = "Al\u00edquota do imposto";
  public static final String PREJUÍZO_A_COMPENSAR = "Preju\u00edzo a compensar";
  public static final String BASE_DE_CÁLCULO_DO_IMPOSTO = "Base de c\u00e1lculo do imposto";
  public static final String RESULTADO_NEGATIVO_ATÉ_O_MÊS_ANTERIOR = "Resultado negativo at\u00e9 o m\u00eas anterior";
  public static final String RESULTADO_LÍQUIDO_DO_MÊS = "Resultado l\u00edquido do m\u00eas";
  private Valor resultLiquidoMes = new Valor (this, "Resultado l\u00edquido do m\u00eas");
  private Valor resultNegativoAnterior = new Valor (this, "Resultado negativo at\u00e9 o m\u00eas anterior");
  private Valor baseCalcImposto = new Valor (this, "Base de c\u00e1lculo do imposto");
  private Valor prejuizoCompensar = new Valor (this, "Preju\u00edzo a compensar");
  private Valor aliquotaImposto = new Valor (this, "Al\u00edquota do imposto");
  private Valor impostoDevido = new Valor (this, "Imposto devido");
  private Valor impostoPago = new Valor (this, "Imposto pago");
  private int mes;
  
  public MesFundosInvestimentos (int aMes)
  {
    mes = aMes;
    aliquotaImposto.setConteudo ("20,00");
  }
  
  public Valor getAliquotaImposto ()
  {
    return aliquotaImposto;
  }
  
  public Valor getBaseCalcImposto ()
  {
    return baseCalcImposto;
  }
  
  public Valor getImpostoDevido ()
  {
    return impostoDevido;
  }
  
  public Valor getImpostoPago ()
  {
    return impostoPago;
  }
  
  public Valor getPrejuizoCompensar ()
  {
    return prejuizoCompensar;
  }
  
  public Valor getResultLiquidoMes ()
  {
    return resultLiquidoMes;
  }
  
  public Valor getResultNegativoAnterior ()
  {
    return resultNegativoAnterior;
  }
  
  public boolean isVazio ()
  {
    boolean ret = baseCalcImposto.isVazio () && impostoDevido.isVazio () && impostoPago.isVazio () && prejuizoCompensar.isVazio () && resultLiquidoMes.isVazio () && resultNegativoAnterior.isVazio ();
    return ret;
  }
}
