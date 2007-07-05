/* ColecaoItemQuadroLucrosDividendos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf;
import java.util.Iterator;

import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.Valor;

public class ColecaoItemQuadroLucrosDividendos extends Colecao
{
  private Valor totais;
  
  public ColecaoItemQuadroLucrosDividendos ()
  {
    super (serpro.ppgd.irpf.ItemQuadroLucrosDividendos.class.getName ());
    totais = new Valor (this, "Totais");
    getTotais ().setReadOnly (true);
  }
  
  public Valor getTotais ()
  {
    return totais;
  }
  
  public void objetoInserido (Object o)
  {
    ((ItemQuadroLucrosDividendos) o).getValor ().addObservador (this);
    calculaTotal ();
  }
  
  public void objetoRemovido (Object o)
  {
    ((ItemQuadroLucrosDividendos) o).getValor ().removeObservador (this);
    calculaTotal ();
  }
  
  private void calculaTotal ()
  {
    Iterator it = recuperarLista ().iterator ();
    totais.clear ();
    while (it.hasNext ())
      {
	ItemQuadroLucrosDividendos itemQuadroAuxiliar = (ItemQuadroLucrosDividendos) it.next ();
	totais.append ('+', itemQuadroAuxiliar.getValor ());
      }
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    calculaTotal ();
  }
}
