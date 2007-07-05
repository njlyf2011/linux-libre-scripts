/* IdentificacaoImovelARExterior - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural.exterior;
import serpro.ppgd.negocio.Colecao;

public class IdentificacaoImovelARExterior extends Colecao
{
  
  public IdentificacaoImovelARExterior ()
  {
    super (serpro.ppgd.irpf.atividaderural.ImovelAR.class.getName ());
    setFicha ("Dados e Identifica\u00e7\u00e3o do Im\u00f3vel Rural - EXTERIOR");
  }
  
  public void objetoInserido (Object o)
  {
    setFicha (getFicha ());
  }
}
