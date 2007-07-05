/* Bens - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.bens;
import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.Valor;

public class Bens extends Colecao
{
  private Valor totalExercicioAnterior;
  private Valor totalExercicioAtual;
  
  public Bens ()
  {
    super (serpro.ppgd.irpf.bens.Bem.class.getName ());
    totalExercicioAnterior = new Valor (this, "Total exercicio Anterior");
    totalExercicioAtual = new Valor (this, "Total exercicio Atual");
    setFicha ("Bens e Direitos");
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
