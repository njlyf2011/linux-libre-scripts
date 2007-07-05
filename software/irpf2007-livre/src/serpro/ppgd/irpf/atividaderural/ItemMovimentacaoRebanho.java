/* ItemMovimentacaoRebanho - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural;
import java.util.Iterator;
import java.util.List;

import serpro.ppgd.irpf.calculos.CalculosMovimentacaoRebanho;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;

public class ItemMovimentacaoRebanho extends ObjetoNegocio
{
  protected Valor estoqueInicial = new Valor (this, "Estoque Inicial");
  protected Valor aquisicoesAno = new Valor (this, "Aquisi\u00e7\u00f5es no Ano");
  protected Valor nascidosAno = new Valor (this, "Nascidos no Ano");
  protected Valor consumo = new Valor (this, "Consumo e Perdas");
  protected Valor vendas = new Valor (this, "Vendas no Ano");
  protected Valor estoqueFinal = new Valor (this, "Estoque Final");
  
  public ItemMovimentacaoRebanho ()
  {
    getEstoqueInicial ().addValidador (new ValidadorMovimentacaoRebanho (this));
    getAquisicoesAno ().addValidador (new ValidadorMovimentacaoRebanho (this));
    getNascidosAno ().addValidador (new ValidadorMovimentacaoRebanho (this));
    getConsumo ().addValidador (new ValidadorMovimentacaoRebanho (this));
    getVendas ().addValidador (new ValidadorMovimentacaoRebanho (this));
    getEstoqueInicial ().addObservador (new CalculosMovimentacaoRebanho (this));
    getAquisicoesAno ().addObservador (new CalculosMovimentacaoRebanho (this));
    getNascidosAno ().addObservador (new CalculosMovimentacaoRebanho (this));
    getConsumo ().addObservador (new CalculosMovimentacaoRebanho (this));
    getVendas ().addObservador (new CalculosMovimentacaoRebanho (this));
  }
  
  public Valor getAquisicoesAno ()
  {
    return aquisicoesAno;
  }
  
  public Valor getConsumo ()
  {
    return consumo;
  }
  
  public Valor getEstoqueFinal ()
  {
    return estoqueFinal;
  }
  
  public Valor getEstoqueInicial ()
  {
    return estoqueInicial;
  }
  
  public Valor getNascidosAno ()
  {
    return nascidosAno;
  }
  
  public Valor getVendas ()
  {
    return vendas;
  }
  
  public boolean isVazio ()
  {
    Iterator iterator = recuperarListaCamposPendencia ().iterator ();
    while (iterator.hasNext ())
      {
	Informacao informacao = (Informacao) iterator.next ();
	if (! informacao.isVazio ())
	  return false;
      }
    return true;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List retorno = recuperarCamposInformacao ();
    return retorno;
  }
}
