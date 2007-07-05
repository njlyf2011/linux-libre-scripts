/* ColecaoItemQuadroAuxiliar - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf;
import java.util.Iterator;

import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.Valor;

public class ColecaoItemQuadroAuxiliar extends Colecao
{
  private Valor totais;
  public static final int MAX_TAMANHO_DESCRICAO = 60;
  
  public ColecaoItemQuadroAuxiliar ()
  {
    super (serpro.ppgd.irpf.ItemQuadroAuxiliar.class.getName ());
    totais = new Valor (this, "Totais");
    getTotais ().setReadOnly (true);
  }
  
  public Valor getTotais ()
  {
    return totais;
  }
  
  public void objetoInserido (Object o)
  {
    ((ItemQuadroAuxiliar) o).getValor ().addObservador (this);
    calculaTotal ();
  }
  
  public void objetoRemovido (Object o)
  {
    ((ItemQuadroAuxiliar) o).getValor ().removeObservador (this);
    calculaTotal ();
  }
  
  private void calculaTotal ()
  {
    Iterator it = recuperarLista ().iterator ();
    totais.clear ();
    while (it.hasNext ())
      {
	ItemQuadroAuxiliar itemQuadroAuxiliar = (ItemQuadroAuxiliar) it.next ();
	totais.append ('+', itemQuadroAuxiliar.getValor ());
      }
  }
  
  public String getDescricoes ()
  {
    String desc = "";
    Iterator it = recuperarLista ().iterator ();
    totais.clear ();
    while (it.hasNext ())
      {
	ItemQuadroAuxiliar itemQuadroAuxiliar = (ItemQuadroAuxiliar) it.next ();
	if (! desc.equals (""))
	  desc += ", ";
	desc += itemQuadroAuxiliar.getEspecificacao ().asString ();
      }
    if (desc.length () > 60)
      desc = desc.substring (0, 60);
    return desc;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    calculaTotal ();
  }
}
