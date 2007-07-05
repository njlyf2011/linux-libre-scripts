/* AtividadeRural - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural;
import serpro.ppgd.irpf.atividaderural.brasil.ARBrasil;
import serpro.ppgd.irpf.atividaderural.exterior.ARExterior;
import serpro.ppgd.negocio.ObjetoNegocio;

public class AtividadeRural extends ObjetoNegocio
{
  private ARBrasil brasil = new ARBrasil ();
  private ARExterior exterior = new ARExterior ();
  
  public ARBrasil getBrasil ()
  {
    return brasil;
  }
  
  public ARExterior getExterior ()
  {
    return exterior;
  }
}
