/* DadoAtividadeRuralExterior - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.txt.importacao.atividaderural;
import serpro.ppgd.negocio.Valor;

class DadoAtividadeRuralExterior
{
  public String codPais = null;
  public Valor totalReceitas = new Valor ();
  public Valor totalDespesas = new Valor ();
  public Valor totalProdutoEntregueAdiantamento = new Valor ();
  public Valor totalAdiantamentoRecebido = new Valor ();
  
  public DadoAtividadeRuralExterior (String pais)
  {
    codPais = pais;
  }
}
