/* FichaColecaoIf - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import serpro.ppgd.negocio.ObjetoNegocio;

public interface FichaColecaoIf
{
  public void mostraOutroObjetoNegocio (ObjetoNegocio objetonegocio);
  
  public ObjetoNegocio criaObjetoNegocio ();
  
  public boolean obtemConfirmacaoExclusao (ObjetoNegocio objetonegocio);
}
