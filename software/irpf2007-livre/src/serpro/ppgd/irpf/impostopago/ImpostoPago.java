/* ImpostoPago - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.impostopago;
import java.util.List;

import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.Valor;

public class ImpostoPago extends ObjetoNegocio
{
  private Valor impostoComplementar = new Valor (this, "");
  private Valor impostoPagoExterior = new Valor (this, "Imposto Pago no Exterior");
  private Valor impostoDevidoComRendExterior = new Valor (this, "");
  private Valor impostoDevidoSemRendExterior = new Valor (this, "");
  private Valor limiteLegalImpPagoExterior = new Valor (this, "");
  private Valor impostoRetidoFonte = new Valor (this, "Imposto de renda na fonte");
  
  public ImpostoPago ()
  {
    impostoPagoExterior.addValidador (new ValidadorDefault ((byte) 1)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (! impostoPagoExterior.isVazio () && limiteLegalImpPagoExterior.isVazio ())
	  return new RetornoValidacao (tab.msg ("imposto_pago_exterior_limite"), getSeveridade ());
	return null;
      }
    });
    impostoDevidoComRendExterior.setReadOnly (true);
    impostoDevidoSemRendExterior.setReadOnly (true);
    limiteLegalImpPagoExterior.setReadOnly (true);
    setFicha ("Imposto Pago");
  }
  
  public Valor getImpostoComplementar ()
  {
    return impostoComplementar;
  }
  
  public Valor getImpostoPagoExterior ()
  {
    return impostoPagoExterior;
  }
  
  public Valor getImpostoRetidoFonte ()
  {
    return impostoRetidoFonte;
  }
  
  public Valor getImpostoDevidoComRendExterior ()
  {
    return impostoDevidoComRendExterior;
  }
  
  public Valor getImpostoDevidoSemRendExterior ()
  {
    return impostoDevidoSemRendExterior;
  }
  
  public Valor getLimiteLegalImpPagoExterior ()
  {
    return limiteLegalImpPagoExterior;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List retorno = super.recuperarListaCamposPendencia ();
    retorno.add (getImpostoRetidoFonte ());
    return retorno;
  }
}
