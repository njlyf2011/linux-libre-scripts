/* ApuracaoResultadoExterior - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural.exterior;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;

public class ApuracaoResultadoExterior extends ObjetoNegocio
{
  public static final String NOME_CAMPO_RESULTADO_TRIBUTAVEL_EXT = "Resultado Tribut\u00e1vel - Exterior";
  public static final String COTACAO_DOLAR = "2,1372";
  private Valor resultadoI_EmDolar = new Valor (this, "");
  private Valor resultadoI_EmReais = new Valor (this, "");
  private Valor prejuizoExercicioAnterior = new Valor (this, "");
  private Valor resultadoAposCompensacaoPrejuizo = new Valor (this, "");
  private Valor opcaoArbitramento = new Valor (this, "");
  private Valor resultadoTributavel = new Valor (this, "Resultado Tribut\u00e1vel - Exterior");
  private Valor prejuizoCompensar = new Valor (this, "");
  private Valor receitaRecebidaContaVenda = new Valor (this, "");
  private Valor valorAdiantamento = new Valor (this, "");
  private Valor resultadoNaoTributavel = new Valor (this, "");
  
  public ApuracaoResultadoExterior ()
  {
    setFicha ("Apura\u00e7\u00e3o do Resultado Tribut\u00e1vel - EXTERIOR");
    resultadoI_EmDolar.setReadOnly (true);
    resultadoI_EmReais.setReadOnly (true);
    resultadoAposCompensacaoPrejuizo.setReadOnly (true);
    resultadoTributavel.setReadOnly (true);
    prejuizoCompensar.setReadOnly (true);
    resultadoNaoTributavel.setReadOnly (true);
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
  
  public Valor getReceitaRecebidaContaVenda ()
  {
    return receitaRecebidaContaVenda;
  }
  
  public Valor getResultadoAposCompensacaoPrejuizo ()
  {
    return resultadoAposCompensacaoPrejuizo;
  }
  
  public Valor getResultadoI_EmReais ()
  {
    return resultadoI_EmReais;
  }
  
  public Valor getResultadoI_EmDolar ()
  {
    return resultadoI_EmDolar;
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
