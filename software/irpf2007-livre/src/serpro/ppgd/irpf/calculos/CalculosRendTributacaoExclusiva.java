/* CalculosRendTributacaoExclusiva - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.rendTributacaoExclusiva.RendTributacaoExclusiva;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class CalculosRendTributacaoExclusiva extends Observador
{
  private RendTributacaoExclusiva rendTributacao;
  private DeclaracaoIRPF declaracaoIRPF;
  
  public CalculosRendTributacaoExclusiva (DeclaracaoIRPF dec)
  {
    declaracaoIRPF = dec;
    rendTributacao = declaracaoIRPF.getRendTributacaoExclusiva ();
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    calculaTotal ();
  }
  
  private void calculaTotal ()
  {
    Valor total = new Valor (rendTributacao.getDecimoTerceiro ().asString ());
    total.append ('+', rendTributacao.getDecimoTerceiroDependentes ());
    total.append ('+', rendTributacao.getGanhosRendaVariavel ());
    total.append ('+', rendTributacao.getOutros ());
    total.append ('+', rendTributacao.getRendAplicacoes ());
    total.append ('+', rendTributacao.getRendExcetoDecimoTerceiro ());
    rendTributacao.getTotal ().setConteudo (total);
  }
}
