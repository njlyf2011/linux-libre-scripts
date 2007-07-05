/* CalculosMovimentacaoRebanho - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import serpro.ppgd.irpf.atividaderural.ItemMovimentacaoRebanho;
import serpro.ppgd.negocio.Observador;

public class CalculosMovimentacaoRebanho extends Observador
{
  private ItemMovimentacaoRebanho itemMovimentacaoRebanho = null;
  
  public CalculosMovimentacaoRebanho (ItemMovimentacaoRebanho pItem)
  {
    itemMovimentacaoRebanho = pItem;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    itemMovimentacaoRebanho.getEstoqueFinal ().clear ();
    itemMovimentacaoRebanho.getEstoqueFinal ().append ('+', itemMovimentacaoRebanho.getEstoqueInicial ());
    itemMovimentacaoRebanho.getEstoqueFinal ().append ('+', itemMovimentacaoRebanho.getAquisicoesAno ());
    itemMovimentacaoRebanho.getEstoqueFinal ().append ('+', itemMovimentacaoRebanho.getNascidosAno ());
    itemMovimentacaoRebanho.getEstoqueFinal ().append ('-', itemMovimentacaoRebanho.getConsumo ());
    itemMovimentacaoRebanho.getEstoqueFinal ().append ('-', itemMovimentacaoRebanho.getVendas ());
  }
}
