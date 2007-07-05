/* CalculosDeducoesIncentivos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class CalculosDeducoesIncentivos extends Observador
{
  private DeclaracaoIRPF declaracaoIRPF;
  
  public CalculosDeducoesIncentivos (DeclaracaoIRPF dec)
  {
    declaracaoIRPF = dec;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    calculaDeducaoIncentivo (declaracaoIRPF);
  }
  
  public static void calculaDeducaoIncentivo (DeclaracaoIRPF declaracaoIRPF)
  {
    Valor tot = new Valor ();
    tot.append ('+', CalculosPagamentos.totalizarPagamentos (declaracaoIRPF.getPagamentos (), new String[] { "15", "17" }, false));
    tot.append ('+', CalculosPagamentos.totalizarPagamentos (declaracaoIRPF.getPagamentos (), new String[] { "16" }, true));
    Valor limiteImpostoDevido = declaracaoIRPF.getModeloCompleta ().getImposto ().operacao ('*', "0,06");
    if (tot.comparacao (">", limiteImpostoDevido))
      tot.setConteudo (limiteImpostoDevido);
    declaracaoIRPF.getPagamentos ().getTotalDeducaoIncentivo ().setConteudo (tot);
  }
}
