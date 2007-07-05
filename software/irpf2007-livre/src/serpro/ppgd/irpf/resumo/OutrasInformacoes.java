/* OutrasInformacoes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.resumo;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;

public class OutrasInformacoes extends ObjetoNegocio
{
  private Valor bensDireitosExercicioAnterior = new Valor (this, "");
  private Valor bensDireitosExercicioAtual = new Valor (this, "");
  private Valor dividasOnusReaisExercicioAnterior = new Valor (this, "");
  private Valor dividasOnusReaisExercicioAtual = new Valor (this, "");
  private Valor informacoesConjuge = new Valor (this, "");
  private Valor rendIsentosNaoTributaveis = new Valor (this, "");
  private Valor rendIsentosTributacaoExclusiva = new Valor (this, "");
  private Valor impostoPagoGCAP = new Valor (this, "");
  private Valor impostoPagoME = new Valor (this, "");
  private Valor totalImpostoRetidoNaFonte = new Valor (this, "");
  private Valor impostoPagoSobreRendaVariavel = new Valor (this, "");
  private Valor totalDoacoesCampanhasEleitorais = new Valor (this, "");
  
  public OutrasInformacoes ()
  {
    bensDireitosExercicioAnterior.setReadOnly (true);
    bensDireitosExercicioAtual.setReadOnly (true);
    dividasOnusReaisExercicioAnterior.setReadOnly (true);
    dividasOnusReaisExercicioAtual.setReadOnly (true);
    informacoesConjuge.setReadOnly (true);
    rendIsentosNaoTributaveis.setReadOnly (true);
    rendIsentosTributacaoExclusiva.setReadOnly (true);
    impostoPagoGCAP.setReadOnly (true);
    impostoPagoME.setReadOnly (true);
    totalImpostoRetidoNaFonte.setReadOnly (true);
    impostoPagoSobreRendaVariavel.setReadOnly (true);
    totalDoacoesCampanhasEleitorais.setReadOnly (true);
  }
  
  public Valor getBensDireitosExercicioAnterior ()
  {
    return bensDireitosExercicioAnterior;
  }
  
  public Valor getBensDireitosExercicioAtual ()
  {
    return bensDireitosExercicioAtual;
  }
  
  public Valor getDividasOnusReaisExercicioAnterior ()
  {
    return dividasOnusReaisExercicioAnterior;
  }
  
  public Valor getDividasOnusReaisExercicioAtual ()
  {
    return dividasOnusReaisExercicioAtual;
  }
  
  public Valor getImpostoPagoGCAP ()
  {
    return impostoPagoGCAP;
  }
  
  public Valor getImpostoPagoME ()
  {
    return impostoPagoME;
  }
  
  public Valor getTotalDoacoesCampanhasEleitorais ()
  {
    return totalDoacoesCampanhasEleitorais;
  }
  
  public Valor getImpostoPagoSobreRendaVariavel ()
  {
    return impostoPagoSobreRendaVariavel;
  }
  
  public Valor getInformacoesConjuge ()
  {
    return informacoesConjuge;
  }
  
  public Valor getRendIsentosNaoTributaveis ()
  {
    return rendIsentosNaoTributaveis;
  }
  
  public Valor getRendIsentosTributacaoExclusiva ()
  {
    return rendIsentosTributacaoExclusiva;
  }
  
  public Valor getTotalImpostoRetidoNaFonte ()
  {
    return totalImpostoRetidoNaFonte;
  }
}
