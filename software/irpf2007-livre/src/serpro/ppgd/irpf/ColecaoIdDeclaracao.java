/* ColecaoIdDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf;
import java.util.Iterator;
import java.util.List;

import serpro.ppgd.negocio.Colecao;

public class ColecaoIdDeclaracao extends Colecao
{
  
  public ColecaoIdDeclaracao ()
  {
    super (serpro.ppgd.irpf.IdentificadorDeclaracao.class.getName ());
  }
  
  public boolean existeCPFCadastrado (String cpf)
  {
    for (int i = recuperarLista ().size () - 1; i >= 0; i--)
      {
	IdentificadorDeclaracao id = (IdentificadorDeclaracao) recuperarLista ().get (i);
	if (id.getCpf ().asString ().equals (cpf))
	  return true;
      }
    return false;
  }
  
  public void removeCPF (IdentificadorDeclaracao id)
  {
    recuperarLista ().remove (id);
  }
  
  public void removeCPF (List lista)
  {
    Iterator it = lista.iterator ();
    while (it.hasNext ())
      recuperarLista ().remove (it.next ());
  }
}
