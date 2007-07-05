/* ValidadorMovimentacaoRebanho - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.Valor;

public class ValidadorMovimentacaoRebanho extends ValidadorDefault
{
  private ItemMovimentacaoRebanho itemMovimentacaoRebanho = null;
  
  public ValidadorMovimentacaoRebanho (ItemMovimentacaoRebanho pItem)
  {
    super ((byte) 2);
    setVerificaVazio (true);
    setMensagemValidacao (tab.msg ("ficha_mov_rebanho_ar"));
    itemMovimentacaoRebanho = pItem;
  }
  
  public RetornoValidacao validarImplementado ()
  {
    Valor param1 = new Valor ();
    Valor param2 = new Valor ();
    param1.append ('+', itemMovimentacaoRebanho.getEstoqueInicial ());
    param1.append ('+', itemMovimentacaoRebanho.getAquisicoesAno ());
    param1.append ('+', itemMovimentacaoRebanho.getNascidosAno ());
    param2.append ('+', itemMovimentacaoRebanho.getConsumo ());
    param2.append ('+', itemMovimentacaoRebanho.getVendas ());
    if (param1.comparacao ("<", param2))
      return new RetornoValidacao (getMensagemValidacao (), getSeveridade ());
    return null;
  }
}
