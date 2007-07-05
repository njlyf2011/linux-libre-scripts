/* Dividas - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.dividas;
import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.Valor;

public class Dividas extends Colecao
{
  private Valor totalExercicioAnterior;
  private Valor totalExercicioAtual;
  
  public Dividas ()
  {
    super (serpro.ppgd.irpf.dividas.Divida.class.getName ());
    totalExercicioAnterior = new Valor (this, "Total exercicio Anterior");
    totalExercicioAtual = new Valor (this, "Total exercicio Atual");
    setFicha ("D\u00edvidas e \u00d4nus Reais");
    totalExercicioAnterior.setReadOnly (true);
    totalExercicioAtual.setReadOnly (true);
  }
  
  public void objetoInserido (Object o)
  {
    setFicha (getFicha ());
  }
  
  public Valor getTotalExercicioAnterior ()
  {
    return totalExercicioAnterior;
  }
  
  public Valor getTotalExercicioAtual ()
  {
    return totalExercicioAtual;
  }
}
