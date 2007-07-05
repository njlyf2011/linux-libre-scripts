/* Resumo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.resumo;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.contribuinte.Contribuinte;
import serpro.ppgd.negocio.ObjetoNegocio;

public class Resumo extends ObjetoNegocio
{
  private OutrasInformacoes outrasInformacoes = new OutrasInformacoes ();
  private CalculoImposto calculoImposto;
  private RendimentosTributaveisDeducoes rendimentosTributaveisDeducoes = new RendimentosTributaveisDeducoes ();
  private IdentificadorDeclaracao identificadorDeclaracao;
  
  public Resumo (IdentificadorDeclaracao aIdentificadorDeclaracao, Contribuinte contribuinte)
  {
    identificadorDeclaracao = aIdentificadorDeclaracao;
    calculoImposto = new CalculoImposto (identificadorDeclaracao, contribuinte);
  }
  
  public OutrasInformacoes getOutrasInformacoes ()
  {
    return outrasInformacoes;
  }
  
  public CalculoImposto getCalculoImposto ()
  {
    return calculoImposto;
  }
  
  public RendimentosTributaveisDeducoes getRendimentosTributaveisDeducoes ()
  {
    return rendimentosTributaveisDeducoes;
  }
}
