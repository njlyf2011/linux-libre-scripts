/* IdUsuario - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public abstract class IdUsuario extends ObjetoNegocio
{
  public static final String LABEL_NI = "Identifica\u00e7\u00e3o do Contribuinte";
  public static final String NOME = "Nome";
  private NI niContribuinte;
  private Nome nome = new Nome (this, "Nome");
  private Vector idDeclaracoes;
  
  public IdUsuario ()
  {
    super (null);
    niContribuinte = new NI (this, "Identifica\u00e7\u00e3o do Contribuinte");
    niContribuinte.setReadOnly (true);
    nome.setReadOnly (true);
    idDeclaracoes = new Vector ();
  }
  
  public NI getNiContribuinte ()
  {
    return niContribuinte;
  }
  
  public Nome getNome ()
  {
    return nome;
  }
  
  public List getIdDeclaracoes ()
  {
    return idDeclaracoes;
  }
  
  public IdDeclaracao recuperarIdDeclaracao (int index)
  {
    List listaIdDeclaracaoes = getIdDeclaracoes ();
    if (listaIdDeclaracaoes != null && listaIdDeclaracaoes.size () >= index)
      return (IdDeclaracao) listaIdDeclaracaoes.get (index);
    return null;
  }
  
  public final boolean equals (IdUsuario id)
  {
    List listaAtributosPkParametro = id.getListaAtributosPK ();
    List listaAtributosPk = getListaAtributosPK ();
    if (listaAtributosPk.size () != listaAtributosPkParametro.size ())
      return false;
    Iterator itAtributos = listaAtributosPk.iterator ();
    Iterator itParametro = listaAtributosPkParametro.iterator ();
    while (itAtributos.hasNext ())
      {
	Informacao atributoAtual = (Informacao) itAtributos.next ();
	Informacao atributoParametroAtual = (Informacao) itParametro.next ();
	if (atributoAtual == null || atributoParametroAtual == null)
	  return false;
	if (! atributoAtual.asString ().equals (atributoParametroAtual.asString ()))
	  return false;
      }
    return true;
  }
  
  public abstract List getListaAtributosPK ();
}
