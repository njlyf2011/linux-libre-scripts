/* ObservadorTotalizaDependentes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.dependentes;
import java.util.Iterator;

import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class ObservadorTotalizaDependentes extends Observador
{
  private Dependentes dependentes;
  
  public ObservadorTotalizaDependentes (Dependentes aDependentes)
  {
    dependentes = aDependentes;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    Valor total = new Valor ();
    Iterator it = dependentes.recuperarLista ().iterator ();
    while (it.hasNext ())
      {
	Dependente item = (Dependente) it.next ();
	if (! item.isVazio ())
	  total.append ('+', "1516,32");
      }
    dependentes.getTotalDeducaoDependentes ().setConteudo (total);
  }
}
