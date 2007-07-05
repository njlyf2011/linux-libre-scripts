/* ApuracaoResultadoBrasil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural.brasil;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;

public class ApuracaoResultadoBrasil extends ObjetoNegocio
{
  public static final String NOME_CAMPO_RESULTADO_TRIBUTAVEL_BR = "Resultado Tribut\u00e1vel - Brasil";
  private Valor receitaBrutaTotal = new Valor (this, "");
  private Valor despesaCusteio = new Valor (this, "");
  private Valor resultadoI = new Valor (this, "");
  private Valor prejuizoExercicioAnterior = new Valor (this, "");
  private Valor resultadoAposCompensacaoPrejuizo = new Valor (this, "");
  private Valor opcaoArbitramento = new Valor (this, "");
  private Valor resultadoTributavel = new Valor (this, "Resultado Tribut\u00e1vel - Brasil");
  private Valor prejuizoCompensar = new Valor (this, "");
  private Valor receitaRecebidaContaVenda = new Valor (this, "");
  private Valor valorAdiantamento = new Valor (this, "");
  private Valor resultadoNaoTributavel = new Valor (this, "");
  
  public ApuracaoResultadoBrasil ()
  {
    setFicha ("Apura\u00e7\u00e3o do Resultado Tribut\u00e1vel - BRASIL");
    receitaBrutaTotal.setReadOnly (true);
    despesaCusteio.setReadOnly (true);
    resultadoI.setReadOnly (true);
    resultadoAposCompensacaoPrejuizo.setReadOnly (true);
    opcaoArbitramento.setReadOnly (true);
    resultadoTributavel.setReadOnly (true);
    prejuizoCompensar.setReadOnly (true);
    resultadoNaoTributavel.setReadOnly (true);
  }
  
  public Valor getDespesaCusteio ()
  {
    return despesaCusteio;
  }
  
  public Valor getOpcaoArbitramento ()
  {
    return opcaoArbitramento;
  }
  
  public Valor getPrejuizoCompensar ()
  {
    return prejuizoCompensar;
  }
  
  public Valor getPrejuizoExercicioAnterior ()
  {
    return prejuizoExercicioAnterior;
  }
  
  public Valor getReceitaBrutaTotal ()
  {
    return receitaBrutaTotal;
  }
  
  public Valor getReceitaRecebidaContaVenda ()
  {
    return receitaRecebidaContaVenda;
  }
  
  public Valor getResultadoAposCompensacaoPrejuizo ()
  {
    return resultadoAposCompensacaoPrejuizo;
  }
  
  public Valor getResultadoI ()
  {
    return resultadoI;
  }
  
  public Valor getResultadoNaoTributavel ()
  {
    return resultadoNaoTributavel;
  }
  
  public Valor getResultadoTributavel ()
  {
    return resultadoTributavel;
  }
  
  public Valor getValorAdiantamento ()
  {
    return valorAdiantamento;
  }
}
