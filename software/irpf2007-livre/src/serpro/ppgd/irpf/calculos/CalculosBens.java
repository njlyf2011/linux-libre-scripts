/* CalculosBens - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import java.util.Iterator;

import serpro.ppgd.irpf.bens.Bem;
import serpro.ppgd.irpf.bens.Bens;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class CalculosBens extends Observador
{
  private Bens bens = null;
  
  public CalculosBens (Bens _bens)
  {
    bens = _bens;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (nomePropriedade != null)
      {
	if (nomePropriedade.equals ("ObjetoInserido"))
	  {
	    ((Bem) valorNovo).getValorExercicioAtual ().addObservador (this);
	    ((Bem) valorNovo).getValorExercicioAnterior ().addObservador (this);
	  }
	else if (nomePropriedade.equals ("ObjetoRemovido"))
	  {
	    ((Bem) valorNovo).getValorExercicioAtual ().removeObservador (this);
	    ((Bem) valorNovo).getValorExercicioAnterior ().removeObservador (this);
	  }
      }
    calculaTotalExercicioAtual ();
    calculaTotalExercicioAnterior ();
  }
  
  private void calculaTotalExercicioAtual ()
  {
    Valor total = new Valor ();
    bens.getTotalExercicioAtual ().clear ();
    Iterator it = bens.recuperarLista ().iterator ();
    while (it.hasNext ())
      {
	Bem bem = (Bem) it.next ();
	total.append ('+', bem.getValorExercicioAtual ());
      }
    bens.getTotalExercicioAtual ().setConteudo (total);
  }
  
  private void calculaTotalExercicioAnterior ()
  {
    Valor total = new Valor ();
    Iterator it = bens.recuperarLista ().iterator ();
    while (it.hasNext ())
      {
	Bem bem = (Bem) it.next ();
	total.append ('+', bem.getValorExercicioAnterior ());
      }
    bens.getTotalExercicioAnterior ().setConteudo (total);
  }
}
