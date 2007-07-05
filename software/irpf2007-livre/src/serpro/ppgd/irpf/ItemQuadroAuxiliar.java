/* ItemQuadroAuxiliar - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;

public class ItemQuadroAuxiliar extends ObjetoNegocio
{
  private Alfa especificacao = new Alfa (this, "Especifica\u00e7\u00e3o", 60);
  private Valor valor = new Valor (this, "Valor");
  
  public Alfa getEspecificacao ()
  {
    return especificacao;
  }
  
  public Valor getValor ()
  {
    return valor;
  }
}
