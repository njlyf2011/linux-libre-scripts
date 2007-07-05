/* ColecaoReceitasDespesas - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural.exterior;
import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.Valor;

public class ColecaoReceitasDespesas extends Colecao
{
  private Valor totais;
  
  public ColecaoReceitasDespesas ()
  {
    super (serpro.ppgd.irpf.atividaderural.exterior.ReceitaDespesa.class.getName ());
    totais = new Valor (this, "Totais");
    setFicha ("Receitas e Despesas - EXTERIOR");
    totais.setReadOnly (true);
  }
  
  public void objetoInserido (Object o)
  {
    setFicha (getFicha ());
  }
  
  public Valor getTotais ()
  {
    return totais;
  }
}
