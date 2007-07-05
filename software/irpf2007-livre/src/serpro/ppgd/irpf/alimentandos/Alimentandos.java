/* Alimentandos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.alimentandos;
import java.util.Iterator;

import serpro.ppgd.negocio.Colecao;

public class Alimentandos extends Colecao
{
  
  public Alimentandos ()
  {
    super (serpro.ppgd.irpf.alimentandos.Alimentando.class.getName ());
  }
  
  public String getNomeAlimentandoByChave (String chave)
  {
    Iterator it = recuperarLista ().iterator ();
    while (it.hasNext ())
      {
	Alimentando a = (Alimentando) it.next ();
	if (a.getChave ().equals (chave))
	  return a.getNome ().getConteudoFormatado ();
      }
    return null;
  }
}
