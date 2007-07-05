/* Doacoes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.eleicoes;
import java.util.Iterator;

import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.Valor;

public class Doacoes extends Colecao
{
  private Valor totalDoacoes;
  
  public Doacoes ()
  {
    super (serpro.ppgd.irpf.eleicoes.Doacao.class.getName ());
    totalDoacoes = new Valor (this, "");
    setFicha ("Doa\u00e7\u00f5es a Partidos Pol\u00edticos, Comit\u00eas Financeiros e Candidatos a Cargos Eletivos");
  }
  
  public String getNomeDoadorByChave (String chave)
  {
    Iterator it = recuperarLista ().iterator ();
    while (it.hasNext ())
      {
	Doacao a = (Doacao) it.next ();
	if (a.getChave ().equals (chave))
	  return a.getNome ().getConteudoFormatado ();
      }
    return null;
  }
  
  public Valor getTotalDoacoes ()
  {
    return totalDoacoes;
  }
  
  public void setTotalDoacoes (Valor totalDoacoes)
  {
    this.totalDoacoes = totalDoacoes;
  }
  
  public void objetoInserido (Object o)
  {
    setFicha (getFicha ());
  }
}
