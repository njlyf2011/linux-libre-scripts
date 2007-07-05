/* RendPJ - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendpj;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;

public class RendPJ extends ObjetoNegocio
{
  private ColecaoRendPJTitular colecaoRendPJTitular = null;
  private ColecaoRendPJDependente colecaoRendPJDependente = null;
  private Valor totalRendRecebPessoaJuridica = new Valor (this, "Total Rend. Receb. PJ Tit + Dep");
  
  public RendPJ (IdentificadorDeclaracao id)
  {
    colecaoRendPJTitular = new ColecaoRendPJTitular (id);
    colecaoRendPJDependente = new ColecaoRendPJDependente (id);
  }
  
  public ColecaoRendPJDependente getColecaoRendPJDependente ()
  {
    return colecaoRendPJDependente;
  }
  
  public ColecaoRendPJTitular getColecaoRendPJTitular ()
  {
    return colecaoRendPJTitular;
  }
  
  public Valor getTotalRendRecebPessoaJuridica ()
  {
    return totalRendRecebPessoaJuridica;
  }
}
