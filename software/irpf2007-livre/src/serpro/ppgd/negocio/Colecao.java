/* Colecao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import java.beans.PropertyChangeSupport;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import serpro.ppgd.negocio.util.FabricaTratamentoErro;
import serpro.ppgd.negocio.util.LogPPGD;

public abstract class Colecao extends ObjetoNegocio
{
  private List lista;
  protected Class tipoItens = null;
  private int tamanho = 0;
  private PropertyChangeSupport observadoresLista;
  private boolean observadoresListaAtivos;
  public static final String OBJETO_INSERIDO = "ObjetoInserido";
  public static final String OBJETO_A_REMOVER = "ObjetoAremover";
  public static final String OBJETO_REMOVIDO = "ObjetoRemovido";
  
  public Colecao (IdDeclaracao idDeclaracao)
  {
    super (idDeclaracao);
    observadoresLista = new PropertyChangeSupport (this);
    observadoresListaAtivos = true;
    setTipoItens (null);
  }
  
  public Colecao ()
  {
    observadoresLista = new PropertyChangeSupport (this);
    observadoresListaAtivos = true;
    setTipoItens (null);
  }
  
  public Colecao (IdDeclaracao idDeclaracao, String nomeClasse)
  {
    this (idDeclaracao);
    setTipoItens (nomeClasse);
  }
  
  public Colecao (String nomeClasse)
  {
    observadoresLista = new PropertyChangeSupport (this);
    observadoresListaAtivos = true;
    setTipoItens (nomeClasse);
  }
  
  public Colecao (IdDeclaracao idDeclaracao, String nomeClasse, int tamanho)
  {
    this (idDeclaracao, nomeClasse);
    this.tamanho = tamanho;
  }
  
  public Colecao (String nomeClasse, int tamanho)
  {
    observadoresLista = new PropertyChangeSupport (this);
    observadoresListaAtivos = true;
    this.tamanho = tamanho;
  }
  
  public void clear ()
  {
    super.clear ();
    recuperarLista ().clear ();
  }
  
  public void setValidadoresAtivos (boolean opcao)
  {
    super.setValidadoresAtivos (opcao);
    Iterator itItems = recuperarLista ().iterator ();
    while (itItems.hasNext ())
      {
	ObjetoNegocio obj = (ObjetoNegocio) itItems.next ();
	obj.setValidadoresAtivos (opcao);
      }
  }
  
  public void setTipoItens (String tipoItens)
  {
    try
      {
	if (tipoItens != null)
	  this.tipoItens = Class.forName (tipoItens);
	else
	  this.tipoItens = null;
      }
    catch (ClassNotFoundException e)
      {
	FabricaTratamentoErro.getTrataErroSistemico ().trataErroSistemico ("A classe " + tipoItens + " especificada com item da cole\u00e7\u00e3o " + this.getClass ().getName () + " n\u00e3o foi encontrada.");
      }
  }
  
  public List recuperarLista ()
  {
    if (lista == null)
      lista = new ListaPPGD (this);
    return lista;
  }
  
  public void objetoInserido (Object o)
  {
    /* empty */
  }
  
  public void objetoARemover (Object o)
  {
    /* empty */
  }
  
  public void objetoRemovido (Object o)
  {
    /* empty */
  }
  
  public int novoObjeto ()
  {
    recuperarLista ().add (instanciaNovoObjeto ());
    return recuperarLista ().size () - 1;
  }
  
  public ObjetoNegocio instanciaNovoObjeto ()
  {
    Class classe = getTipoItens ();
    ObjetoNegocio objetonegocio;
    try
      {
	Object o = classe.newInstance ();
	objetonegocio = (ObjetoNegocio) o;
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	return null;
      }
    return objetonegocio;
  }
  
  public void removeObjeto (int i)
  {
    recuperarLista ().remove (i);
  }
  
  public Class getTipoItens ()
  {
    return tipoItens;
  }
  
  public List verificarPendencias (int numeroItem)
  {
    List listaPendenciasColecao = new Vector ();
    listaPendenciasColecao.addAll (super.verificarPendencias (-1));
    int i = 0;
    Iterator iterator = recuperarLista ().iterator ();
    while (iterator.hasNext ())
      {
	Object object = iterator.next ();
	i++;
	ObjetoNegocio objetoIrpf;
	if (object instanceof ObjetoNegocio)
	  objetoIrpf = (ObjetoNegocio) object;
	else
	  {
	    String msg = "Par\u00e2metro " + object.getClass () + "da classe " + this.getClass () + " n\u00e3o \u00e9 ObjetoNegocio.";
	    LogPPGD.erro (msg);
	    throw new IllegalArgumentException (msg);
	  }
	List listaPendenciasObjeto = objetoIrpf.verificarPendencias (i);
	listaPendenciasColecao.addAll (listaPendenciasObjeto);
      }
    return listaPendenciasColecao;
  }
  
  public void setFicha (String entidade)
  {
    ficha = entidade;
    Iterator iterator = recuperarLista ().iterator ();
    while (iterator.hasNext ())
      {
	Object object = iterator.next ();
	ObjetoNegocio objetoNegocio;
	if (object instanceof ObjetoNegocio)
	  objetoNegocio = (ObjetoNegocio) object;
	else
	  throw new IllegalArgumentException ("Par\u00e2metro " + object.getClass () + " n\u00e3o \u00e9 Informacao.");
	objetoNegocio.setFicha (entidade);
      }
    Iterator itAtributosColecao = recuperarListaCamposPendencia ().iterator ();
    while (itAtributosColecao.hasNext ())
      {
	Informacao info = (Informacao) itAtributosColecao.next ();
	info.setFicha (entidade);
      }
  }
  
  public int getTamanho ()
  {
    return tamanho;
  }
  
  public int recuperarNumeroObjetosNaoNulos ()
  {
    Iterator iterator = recuperarLista ().iterator ();
    int i = 0;
    while (iterator.hasNext ())
      {
	ObjetoNegocio obj = (ObjetoNegocio) iterator.next ();
	if (! obj.isVazio ())
	  i++;
      }
    return i;
  }
  
  public void excluirRegistrosEmBranco ()
  {
    List list = recuperarLista ();
    String a = "";
    for (int i = list.size () - 1; i >= 0; i--)
      {
	ObjetoNegocio objetoIrpf = (ObjetoNegocio) list.get (i);
	if (objetoIrpf.isVazio ())
	  list.remove (i);
      }
  }
  
  public boolean isVazio ()
  {
    Iterator iterator = recuperarLista ().iterator ();
  while_0_:
    do
      {
	ObjetoNegocio obj;
	do
	  {
	    if (! iterator.hasNext ())
	      break while_0_;
	    obj = (ObjetoNegocio) iterator.next ();
	  }
	while (obj.isVazio ());
	return false;
      }
    while (false);
    boolean atributosDaColecaoVazios = super.isVazio ();
    return atributosDaColecaoVazios;
  }
  
  public void removeObservadoresAnonimosDesnecessarios (Class pClasse)
  {
    super.removeObservadoresAnonimosDesnecessarios (pClasse);
    Iterator itItems = recuperarLista ().iterator ();
    while (itItems.hasNext ())
      {
	ObjetoNegocio proximo = (ObjetoNegocio) itItems.next ();
	proximo.removeObservadoresAnonimosDesnecessarios (pClasse);
      }
  }
  
  public void removeObservadores (Class[] pClasse)
  {
    super.removeObservadores (pClasse);
    Iterator itItems = recuperarLista ().iterator ();
    while (itItems.hasNext ())
      {
	ObjetoNegocio proximo = (ObjetoNegocio) itItems.next ();
	proximo.removeObservadores (pClasse);
      }
  }
  
  public PropertyChangeSupport getObservadoresLista ()
  {
    return observadoresLista;
  }
  
  public void addObservador (Observador observador)
  {
    observadoresLista.addPropertyChangeListener (observador);
  }
  
  public void addObservador (String nomePropriedade, Observador observador)
  {
    observadoresLista.addPropertyChangeListener (nomePropriedade, observador);
  }
  
  public void removeObservador (Observador observador)
  {
    observadoresLista.removePropertyChangeListener (observador);
  }
  
  public void removeObservador (String nomePropriedade, Observador observador)
  {
    observadoresLista.removePropertyChangeListener (nomePropriedade, observador);
  }
  
  public boolean isObservadoresListaAtivos ()
  {
    return observadoresListaAtivos;
  }
  
  public void setObservadoresAtivos (boolean observadoresAtivos)
  {
    observadoresListaAtivos = observadoresAtivos;
  }
}
