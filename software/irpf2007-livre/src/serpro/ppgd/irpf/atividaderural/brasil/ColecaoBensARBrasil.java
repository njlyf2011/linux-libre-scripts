/* ColecaoBensARBrasil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural.brasil;
import serpro.ppgd.negocio.Colecao;

public class ColecaoBensARBrasil extends Colecao
{
  
  public ColecaoBensARBrasil ()
  {
    super (serpro.ppgd.irpf.atividaderural.BemAR.class.getName ());
    setFicha ("Bens da Atividade Rural - BRASIL");
  }
  
  public void objetoInserido (Object o)
  {
    setFicha (getFicha ());
  }
}
