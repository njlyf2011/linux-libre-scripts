/* ModeloDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;

public abstract class ModeloDeclaracao extends ObjetoNegocio
{
  public static final String LIMITE_ANUAL_27_MEIO_PORCENT = "29.958,88";
  public static final String LIMITE_ANUAL_15_PORCENT = "14.992,32";
  public static final String LIMITE_MENSAL_27_MEIO_PORCENT = "5.993,730";
  public static final String LIMITE_MENSAL_15_PORCENT = "2.248,87";
  public static final String NOME_TOTAL_REND_RECEB_MAIS_EXTERIOR = "Total de Rendimentos Recebidos";
  public static final String NOME_TOTAL_LIVRO_CAIXA_TIT_DEP = "Total Livro Caixa - TIT + DEP";
  protected DeclaracaoIRPF declaracaoIRPF = null;
  protected Valor impostoDevido = new Valor (this, "");
  protected Valor impostoDevidoII = new Valor (this, "");
  protected Valor baseCalculo = new Valor (this, "");
  protected Valor saldoImpostoPagar = new Valor (this, "");
  protected Valor impostoRestituir = new Valor (this, "");
  protected Valor rendRecebidoExterior = new Valor (this, "");
  protected Valor totalRendRecebidosMaisExterior = new Valor (this, "Total de Rendimentos Recebidos");
  protected Valor totalLivroCaixa = new Valor (this, "Total Livro Caixa - TIT + DEP");
  protected Valor totalDoacoesCampanhasEleitorais = new Valor (this, "");
  
  public ModeloDeclaracao (DeclaracaoIRPF dec)
  {
    declaracaoIRPF = dec;
    totalRendRecebidosMaisExterior.setReadOnly (true);
  }
  
  public Valor getTotalRendRecebidosMaisExterior ()
  {
    return totalRendRecebidosMaisExterior;
  }
  
  public Valor getTotalLivroCaixa ()
  {
    return totalLivroCaixa;
  }
  
  public Valor calculaImposto (Valor _baseCalculo)
  {
    Valor retorno = new Valor ();
    if (_baseCalculo.comparacao ("<", "14.992,32"))
      retorno.clear ();
    else if (_baseCalculo.comparacao ("<", "29.958,88"))
      {
	Valor imposto = new Valor ();
	imposto.setConteudo (_baseCalculo.operacao ('*', "0,15"));
	imposto.append ('-', "2.248,87");
	retorno.setConteudo (imposto);
      }
    else
      {
	Valor imposto = new Valor ();
	imposto.setConteudo (_baseCalculo);
	imposto.converteQtdCasasDecimais (3);
	imposto.setConteudo (imposto.operacao ('*', "0,275"));
	imposto.append ('-', "5.993,730");
	imposto.converteQtdCasasDecimais (2);
	retorno.setConteudo (imposto);
      }
    return retorno;
  }
  
  public Valor getImpostoDevido ()
  {
    return impostoDevido;
  }
  
  public Valor getBaseCalculo ()
  {
    return baseCalculo;
  }
  
  public Valor getSaldoImpostoPagar ()
  {
    return saldoImpostoPagar;
  }
  
  public Valor getImpostoRestituir ()
  {
    return impostoRestituir;
  }
  
  public Valor getRendRecebidoExterior ()
  {
    return rendRecebidoExterior;
  }
  
  public abstract void resumoRendimentosTributaveis ();
  
  public abstract void resumoCalculoImposto ();
  
  public abstract void resumoOutrasInformacoes ();
  
  public abstract void aplicaValoresNaDeclaracao ();
  
  public abstract Valor recuperarTotalRendimentosTributaveis ();
  
  public abstract Valor recuperarTotalImpostoPago ();
  
  public abstract String recuperarCodInImpostoPago ();
  
  public Valor getImpostoDevidoII ()
  {
    return impostoDevidoII;
  }
}
