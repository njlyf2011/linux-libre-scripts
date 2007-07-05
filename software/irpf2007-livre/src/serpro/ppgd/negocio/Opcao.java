/* Opcao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import serpro.ppgd.negocio.util.LogPPGD;

public class Opcao extends Informacao
{
  private String conteudo = "";
  private String C_DELIMITADOR_OPCOES = ";";
  public static String SIM = "1";
  public static String NAO = "0";
  public static String LABEL_SIM = "Sim";
  public static String LABEL_NAO = "N\u00e3o";
  public boolean selecaoMultipla = true;
  private Map opcoes;
  private LinkedList listaOrdenada;
  private Vector listaValidadoresImpeditivosTemporaria;
  
  public Opcao ()
  {
    super ("");
    opcoes = new Hashtable ();
    listaOrdenada = new LinkedList ();
    listaValidadoresImpeditivosTemporaria = new Vector ();
  }
  
  public void addValidador (ValidadorIf validador)
  {
    if (validador instanceof ValidadorImpeditivoDefault && ! (validador instanceof ValidadorImpeditivoOpcao))
      throw new IllegalArgumentException ("O Validador adicionado n\u00e3o \u00e9 do tipo ValidadorImpeditivoOpcao.\n Fa\u00e7a com que esse herde de ValidadorImpeditivoOpcao.");
    super.addValidador (validador);
  }
  
  public void atualizaListaValidadoresImpeditivos (String proximaOpcao)
  {
    Vector lista = new Vector ();
    Iterator itValidadores = super.getListaValidadoresImpeditivos ().iterator ();
    while (itValidadores.hasNext ())
      {
	ValidadorImpeditivoOpcao val = (ValidadorImpeditivoOpcao) itValidadores.next ();
	if (! val.getValorCodigoOpcao ().trim ().equals ("") && getOpcoes ().containsKey (val.getValorCodigoOpcao ().trim ()) && proximaOpcao.equals (val.getValorCodigoOpcao ()))
	  lista.add (val);
      }
    listaValidadoresImpeditivosTemporaria.clear ();
    listaValidadoresImpeditivosTemporaria.addAll (lista);
  }
  
  public void ordenaListaValidadoreImpeditivos ()
  {
    LinkedList retorno = new LinkedList ();
    Iterator itValidadores = getListaValidadoresImpeditivos ().iterator ();
    while (itValidadores.hasNext ())
      {
	ValidadorImpeditivoDefault validador = (ValidadorImpeditivoDefault) itValidadores.next ();
	if (validador.getSeveridade () == 5)
	  retorno.addFirst (validador);
	else if (validador.getSeveridade () == 4)
	  retorno.addLast (validador);
      }
    listaValidadoresImpeditivosTemporaria.clear ();
    listaValidadoresImpeditivosTemporaria.addAll (retorno);
  }
  
  public Vector getListaValidadoresImpeditivos ()
  {
    return listaValidadoresImpeditivosTemporaria;
  }
  
  /**
   * @deprecated
   */
  public void adicionaOpcao (String codigo, String descricao)
  {
    if (opcoes.containsKey (codigo))
      listaOrdenada.remove (opcoes.get (codigo));
    ItemOpcao o = new ItemOpcao (codigo, descricao);
    opcoes.put (codigo, o);
    listaOrdenada.addLast (o);
  }
  
  /**
   * @deprecated
   */
  public void adicionaOpcao (String codigo, String descricao, boolean selecionado)
  {
    if (opcoes.containsKey (codigo))
      listaOrdenada.remove (opcoes.get (codigo));
    ItemOpcao o = new ItemOpcao (codigo, descricao, selecionado);
    opcoes.put (codigo, o);
    listaOrdenada.addLast (o);
  }
  
  public void addOpcao (String codigo, String descricao, boolean selecionado)
  {
    if (opcoes.containsKey (codigo))
      listaOrdenada.remove (opcoes.get (codigo));
    ItemOpcao o = new ItemOpcao (codigo, descricao, selecionado);
    opcoes.put (codigo, o);
    listaOrdenada.addLast (o);
  }
  
  public String getDescricaoOpcao (String valor)
  {
    if (opcoes.containsKey (valor))
      return ((ItemOpcao) opcoes.get (valor)).getDescricao ();
    return "";
  }
  
  public String getCodigoOpcao (String descricao)
  {
    String retorno = "";
    Iterator it = opcoes.values ().iterator ();
    while (it.hasNext ())
      {
	ItemOpcao o = (ItemOpcao) it.next ();
	if (o.getDescricao ().trim ().equals (descricao.trim ()))
	  return o.getCodigo ();
      }
    return retorno;
  }
  
  public String getDescricaoOpcoes ()
  {
    Iterator it = opcoes.values ().iterator ();
    String result = "";
    while (it.hasNext ())
      {
	ItemOpcao o = (ItemOpcao) it.next ();
	if (o.isSelecionado ())
	  {
	    if (result.equals (""))
	      result += o.getDescricao ();
	    else
	      result += C_DELIMITADOR_OPCOES + o.getDescricao ();
	  }
      }
    return result;
  }
  
  public String getCodigoOpcoes ()
  {
    Iterator it = opcoes.values ().iterator ();
    String result = "";
    while (it.hasNext ())
      {
	ItemOpcao o = (ItemOpcao) it.next ();
	if (o.isSelecionado ())
	  {
	    if (result.equals (""))
	      result += o.getCodigo ();
	    else
	      result += C_DELIMITADOR_OPCOES + o.getCodigo ();
	  }
      }
    return result;
  }
  
  public Opcao (ObjetoNegocio owner, String nomeCampo)
  {
    super (owner, nomeCampo);
    opcoes = new Hashtable ();
    listaOrdenada = new LinkedList ();
    listaValidadoresImpeditivosTemporaria = new Vector ();
  }
  
  public Opcao (ObjetoNegocio owner, String nomeCampo, List listaOpcoes)
  {
    super (owner, nomeCampo);
    opcoes = new Hashtable ();
    listaOrdenada = new LinkedList ();
    listaValidadoresImpeditivosTemporaria = new Vector ();
    for (int i = 0; i < listaOpcoes.size (); i++)
      adicionaOpcao (((ElementoTabela) listaOpcoes.get (i)).getConteudo (0), ((ElementoTabela) listaOpcoes.get (i)).getConteudo (1));
  }
  
  public void converteEmTipoSimNao (String descricao, boolean selecionado, String valorInicial)
  {
    adicionaOpcao (LABEL_SIM, descricao, selecionado);
    setConteudo (valorInicial);
  }
  
  public void clear ()
  {
    setConteudo ("");
  }
  
  public String asString ()
  {
    Iterator it = opcoes.values ().iterator ();
    String result = "";
    while (it.hasNext ())
      {
	ItemOpcao o = (ItemOpcao) it.next ();
	if (o.isSelecionado ())
	  {
	    if (result.equals (""))
	      result += o.getCodigo ();
	    else
	      result += C_DELIMITADOR_OPCOES + o.getCodigo ();
	  }
      }
    return result;
  }
  
  public String getConteudoFormatado ()
  {
    return asString ();
  }
  
  public String getConteudo ()
  {
    return asString ();
  }
  
  public void setConteudo (String codigos)
  {
    LogPPGD.debug ("Atribui\u00e7\u00e3o: " + getNomeCampo () + " = " + conteudo);
    String antigo = conteudo;
    clearRetornosValidacoes ();
    List listCodigos = getListByString (codigos, C_DELIMITADOR_OPCOES);
    conteudo = codigos;
    Iterator it = opcoes.values ().iterator ();
    while (it.hasNext ())
      {
	ItemOpcao o = (ItemOpcao) it.next ();
	o.setSelecionado (listCodigos.contains (o.getCodigo ().trim ()));
      }
    if (isVazio ())
      getObservadores ().firePropertyChange (getNomeCampo (), "_", "");
    else
      disparaObservadores (antigo);
  }
  
  public void addSelectedItem (String codigo)
  {
    LogPPGD.debug ("Atribui\u00e7\u00e3o: " + getNomeCampo () + " = " + conteudo);
    if (! isSelecaoMultipla ())
      setConteudo ("");
    ItemOpcao item = (ItemOpcao) opcoes.get (codigo);
    item.setSelecionado (true);
    setConteudo (getCodigoOpcoes ());
  }
  
  public void delSelectedItem (String codigo)
  {
    LogPPGD.debug ("Atribui\u00e7\u00e3o: " + getNomeCampo () + " = " + conteudo);
    ItemOpcao item = (ItemOpcao) opcoes.get (codigo);
    item.setSelecionado (false);
    setConteudo (getCodigoOpcoes ());
  }
  
  public boolean isVazio ()
  {
    return asString () == null || asString ().trim ().length () == 0;
  }
  
  public Map getOpcoes ()
  {
    return opcoes;
  }
  
  public void setOpcoes (Map opcoes)
  {
    this.opcoes = opcoes;
  }
  
  public LinkedList getListaOrdenada ()
  {
    return listaOrdenada;
  }
  
  public boolean isSelecaoMultipla ()
  {
    return selecaoMultipla;
  }
  
  public void setSelecaoMultipla (boolean selecaoMultipla)
  {
    this.selecaoMultipla = selecaoMultipla;
  }
  
  public static List getListByString (String valor, String delimitador)
  {
    int v_idx = 1;
    String valorFragmentado = null;
    String valorTemporario = null;
    List result = new Vector ();
    valorTemporario = valor;
    while (v_idx > 0)
      {
	v_idx = valorTemporario.indexOf (delimitador);
	if (v_idx >= 0)
	  valorFragmentado = valorTemporario.substring (0, v_idx);
	else
	  valorFragmentado = valorTemporario;
	result.add (valorFragmentado);
	valorTemporario = valorTemporario.substring (v_idx + delimitador.length (), valorTemporario.length ());
      }
    return result;
  }
}
