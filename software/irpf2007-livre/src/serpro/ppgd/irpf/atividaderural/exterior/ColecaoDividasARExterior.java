/* ColecaoDividasARExterior - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural.exterior;
import serpro.ppgd.negocio.Colecao;

public class ColecaoDividasARExterior extends Colecao
{
  
  public ColecaoDividasARExterior ()
  {
    super (serpro.ppgd.irpf.atividaderural.DividaAR.class.getName ());
    setFicha ("D\u00edvidas Vinculadas \u00e0 Atividade Rural - EXTERIOR");
  }
  
  public void objetoInserido (Object o)
  {
    setFicha (getFicha ());
  }
}
