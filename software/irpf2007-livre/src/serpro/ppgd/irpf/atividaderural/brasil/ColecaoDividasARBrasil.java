/* ColecaoDividasARBrasil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural.brasil;
import serpro.ppgd.negocio.Colecao;

public class ColecaoDividasARBrasil extends Colecao
{
  
  public ColecaoDividasARBrasil ()
  {
    super (serpro.ppgd.irpf.atividaderural.DividaAR.class.getName ());
    setFicha ("D\u00edvidas Vinculadas \u00e0 Atividade Rural - BRASIL");
  }
  
  public void objetoInserido (Object o)
  {
    setFicha (getFicha ());
  }
}
