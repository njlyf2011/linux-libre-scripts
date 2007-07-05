/* MesReceitaDespesa - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural.brasil;
import java.util.Iterator;
import java.util.List;

import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;

public class MesReceitaDespesa extends ObjetoNegocio
{
  public static final String NOME_RECEITA = "RECEITA";
  public static final String NOME_DESPESA = "DESPESA";
  private Valor receitaBrutaMensal = new Valor (this, "RECEITA");
  private Valor despesaCusteioInvestimento = new Valor (this, "DESPESA");
  
  public Valor getDespesaCusteioInvestimento ()
  {
    return despesaCusteioInvestimento;
  }
  
  public Valor getReceitaBrutaMensal ()
  {
    return receitaBrutaMensal;
  }
  
  public boolean isVazio ()
  {
    Iterator iterator = recuperarListaCamposPendencia ().iterator ();
    while (iterator.hasNext ())
      {
	Informacao informacao = (Informacao) iterator.next ();
	if (! informacao.isVazio ())
	  return false;
      }
    return true;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List retorno = recuperarCamposInformacao ();
    return retorno;
  }
}
