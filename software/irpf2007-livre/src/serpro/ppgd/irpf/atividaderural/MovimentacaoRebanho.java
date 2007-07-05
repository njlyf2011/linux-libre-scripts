/* MovimentacaoRebanho - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural;
import serpro.ppgd.negocio.ObjetoNegocio;

public class MovimentacaoRebanho extends ObjetoNegocio
{
  private ItemMovimentacaoRebanho bovinos = new ItemMovimentacaoRebanho ();
  private ItemMovimentacaoRebanho suinos = new ItemMovimentacaoRebanho ();
  private ItemMovimentacaoRebanho caprinos = new ItemMovimentacaoRebanho ();
  private ItemMovimentacaoRebanho asininos = new ItemMovimentacaoRebanho ();
  private ItemMovimentacaoRebanho outros = new ItemMovimentacaoRebanho ();
  
  public ItemMovimentacaoRebanho getAsininos ()
  {
    return asininos;
  }
  
  public ItemMovimentacaoRebanho getBovinos ()
  {
    return bovinos;
  }
  
  public ItemMovimentacaoRebanho getCaprinos ()
  {
    return caprinos;
  }
  
  public ItemMovimentacaoRebanho getOutros ()
  {
    return outros;
  }
  
  public ItemMovimentacaoRebanho getSuinos ()
  {
    return suinos;
  }
  
  public void clear ()
  {
    super.clear ();
    bovinos.clear ();
    suinos.clear ();
    caprinos.clear ();
    asininos.clear ();
    outros.clear ();
  }
}
