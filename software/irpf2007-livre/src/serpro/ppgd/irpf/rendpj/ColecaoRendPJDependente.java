/* ColecaoRendPJDependente - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendpj;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.negocio.ObjetoNegocio;

public class ColecaoRendPJDependente extends ColecaoRendPJTitular
{
  
  public ColecaoRendPJDependente (IdentificadorDeclaracao id)
  {
    super (serpro.ppgd.irpf.rendpj.RendPJDependente.class.getName (), id);
    setFicha ("Rendimentos Tribut\u00e1veis Recebidos de PJ pelos Dependentes");
  }
  
  public ObjetoNegocio instanciaNovoObjeto ()
  {
    return new RendPJDependente (identificadorDeclaracao);
  }
}
