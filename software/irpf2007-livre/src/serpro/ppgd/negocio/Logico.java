/* Logico - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

import serpro.ppgd.negocio.util.LogPPGD;

public class Logico extends Informacao
{
  private String conteudo = "";
  public static String SIM = "1";
  public static String NAO = "0";
  public static String LABEL_SIM = "Sim";
  public static String LABEL_NAO = "N\u00e3o";
  public boolean selecaoMultipla = false;
  private Map opcoes;
  private LinkedList listaOrdenada;
  private Vector listaValidadoresimpeditivosTemporaria;
  
  public Logico ()
  {
    super ("");
    opcoes = new Hashtable ();
    listaOrdenada = new LinkedList ();
    listaValidadoresimpeditivosTemporaria = new Vector ();
  }
  
  public void addValidador (ValidadorIf validador)
  {
    if (validador instanceof ValidadorImpeditivoDefault && ! (validador instanceof ValidadorImpeditivoLogico))
      throw new IllegalArgumentException ("O Validador adicionado n\u00e3o \u00e9 do tipo ValidadorImpeditivoLogico.\n Fa\u00e7a com que esse herde de ValidadorImpeditivoLogico.");
    super.addValidador (validador);
  }
  
  public void atualizaListaValidadoresImpeditivos (String proximaOpcao)
  {
    Vector lista = new Vector ();
    Iterator itValidadores = super.getListaValidadoresImpeditivos ().iterator ();
    while (itValidadores.hasNext ())
      {
	ValidadorImpeditivoLogico val = (ValidadorImpeditivoLogico) itValidadores.next ();
	if (! val.getValorOpcaoDoLogico ().trim ().equals ("") && getOpcoes ().containsKey (val.getValorOpcaoDoLogico ().trim ()) && proximaOpcao.equals (val.getValorOpcaoDoLogico ()))
	  lista.add (val);
      }
    listaValidadoresimpeditivosTemporaria.clear ();
    listaValidadoresimpeditivosTemporaria.addAll (lista);
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
    listaValidadoresimpeditivosTemporaria.clear ();
    listaValidadoresimpeditivosTemporaria.addAll (retorno);
  }
  
  public Vector getListaValidadoresImpeditivos ()
  {
    return listaValidadoresimpeditivosTemporaria;
  }
  
  /**
   * @deprecated
   */
  public void adicionaOpcao (String lbl, String val)
  {
    if (opcoes.containsKey (val))
      listaOrdenada.remove (opcoes.get (val));
    OpcaoLogico o = new OpcaoLogico (lbl, val);
    opcoes.put (val, o);
    listaOrdenada.addLast (o);
  }
  
  public void addOpcao (String codigo, String label)
  {
    if (opcoes.containsKey (codigo))
      listaOrdenada.remove (opcoes.get (codigo));
    OpcaoLogico o = new OpcaoLogico (label, codigo);
    opcoes.put (codigo, o);
    listaOrdenada.addLast (o);
  }
  
  public String getLabelOpcao (String valor)
  {
    if (opcoes.containsKey (valor))
      return ((OpcaoLogico) opcoes.get (valor)).labelOpcao;
    return "";
  }
  
  public String getValorOpcao (String lbl)
  {
    String retorno = "";
    Iterator it = opcoes.values ().iterator ();
    while (it.hasNext ())
      {
	OpcaoLogico o = (OpcaoLogico) it.next ();
	if (o.labelOpcao.trim ().equals (lbl.trim ()))
	  return o.valorOpcao;
      }
    return retorno;
  }
  
  public Logico (ObjetoNegocio owner, String nomeCampo)
  {
    super (owner, nomeCampo);
    opcoes = new Hashtable ();
    listaOrdenada = new LinkedList ();
    listaValidadoresimpeditivosTemporaria = new Vector ();
  }
  
  public void converteEmTipoSimNao (String valorInicial)
  {
    adicionaOpcao (LABEL_SIM, SIM);
    adicionaOpcao (LABEL_NAO, NAO);
    setConteudo (valorInicial);
  }
  
  public void clear ()
  {
    setConteudo ("");
  }
  
  public String asString ()
  {
    Iterator it = opcoes.values ().iterator ();
    while (it.hasNext ())
      {
	OpcaoLogico o = (OpcaoLogico) it.next ();
	if (o.selecionado)
	  return o.valorOpcao;
      }
    return conteudo;
  }
  
  public String getConteudoFormatado ()
  {
    return asString ();
  }
  
  public void setConteudo (String val)
  {
    LogPPGD.debug ("Atribui\u00e7\u00e3o: " + getNomeCampo () + " = " + conteudo);
    String antigo = conteudo;
    clearRetornosValidacoes ();
    if (! opcoes.containsKey (val))
      val = "";
    conteudo = val;
    Iterator it = opcoes.values ().iterator ();
    while (it.hasNext ())
      {
	OpcaoLogico o = (OpcaoLogico) it.next ();
	if (o.valorOpcao.trim ().equals (val.trim ()))
	  o.selecionado = true;
	else
	  o.selecionado = false;
      }
    disparaObservadores (antigo);
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
}
