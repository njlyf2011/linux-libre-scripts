/* ColecaoBensARExterior - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural.exterior;
import serpro.ppgd.negocio.Colecao;

public class ColecaoBensARExterior extends Colecao
{
  
  public ColecaoBensARExterior ()
  {
    super (serpro.ppgd.irpf.atividaderural.exterior.BemARExterior.class.getName ());
    setFicha ("Bens da Atividade Rural - EXTERIOR");
  }
  
  public void objetoInserido (Object o)
  {
    setFicha (getFicha ());
  }
}
