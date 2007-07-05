/* CalculosDividas - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import java.util.Iterator;

import serpro.ppgd.irpf.dividas.Divida;
import serpro.ppgd.irpf.dividas.Dividas;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class CalculosDividas extends Observador
{
  private Dividas dividas = null;
  
  public CalculosDividas (Dividas _dividas)
  {
    dividas = _dividas;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (nomePropriedade != null)
      {
	if (nomePropriedade.equals ("ObjetoInserido"))
	  {
	    ((Divida) valorNovo).getValorExercicioAtual ().addObservador (this);
	    ((Divida) valorNovo).getValorExercicioAnterior ().addObservador (this);
	  }
	else if (nomePropriedade.equals ("ObjetoRemovido"))
	  {
	    ((Divida) valorNovo).getValorExercicioAtual ().removeObservador (this);
	    ((Divida) valorNovo).getValorExercicioAnterior ().removeObservador (this);
	  }
      }
    calculaTotalExercicioAtual ();
    calculaTotalExercicioAnterior ();
  }
  
  private void calculaTotalExercicioAtual ()
  {
    Valor total = new Valor ();
    Iterator it = dividas.recuperarLista ().iterator ();
    while (it.hasNext ())
      {
	Divida Divida = (Divida) it.next ();
	total.append ('+', Divida.getValorExercicioAtual ());
      }
    dividas.getTotalExercicioAtual ().setConteudo (total);
  }
  
  private void calculaTotalExercicioAnterior ()
  {
    Valor total = new Valor ();
    Iterator it = dividas.recuperarLista ().iterator ();
    while (it.hasNext ())
      {
	Divida Divida = (Divida) it.next ();
	total.append ('+', Divida.getValorExercicioAnterior ());
      }
    dividas.getTotalExercicioAnterior ().setConteudo (total);
  }
}
