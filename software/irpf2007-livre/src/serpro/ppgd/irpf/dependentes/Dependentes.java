/* Dependentes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.dependentes;
import java.util.Iterator;

import serpro.ppgd.irpf.contribuinte.Contribuinte;
import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.Valor;

public class Dependentes extends Colecao
{
  public static final String DEDUCAO_DEPENDENTE = "1516,32";
  private transient Valor totalDeducaoDependentes;
  private transient Contribuinte contribuinte;
  private ObservadorTotalizaDependentes obsTotalizaDep;
  
  public Dependentes (Contribuinte aContribuinte)
  {
    super (serpro.ppgd.irpf.dependentes.Dependente.class.getName ());
    totalDeducaoDependentes = new Valor (this, "Total de Redu\u00e7\u00e3o com Dependentes");
    setFicha ("Dependentes");
    totalDeducaoDependentes.setReadOnly (true);
    totalDeducaoDependentes.setFicha (getFicha ());
    totalDeducaoDependentes.setAtributoPersistente (false);
    contribuinte = aContribuinte;
    obsTotalizaDep = new ObservadorTotalizaDependentes (this);
    addObservador (obsTotalizaDep);
  }
  
  public Valor getTotalDeducaoDependentes ()
  {
    return totalDeducaoDependentes;
  }
  
  public void objetoInserido (Object o)
  {
    Dependente dependente = (Dependente) o;
    dependente.setContribuinte (contribuinte);
    dependente.addObservador (obsTotalizaDep);
  }
  
  public void objetoRemovido (Object o)
  {
    Dependente dependente = (Dependente) o;
    dependente.removeObservador (obsTotalizaDep);
  }
  
  public String getNomeDependenteByChave (String chave)
  {
    Iterator it = recuperarLista ().iterator ();
    while (it.hasNext ())
      {
	Dependente d = (Dependente) it.next ();
	if (d.getChave ().equals (chave))
	  return d.getNome ().getConteudoFormatado ();
      }
    return null;
  }
}
