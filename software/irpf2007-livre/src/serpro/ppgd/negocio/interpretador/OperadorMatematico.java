/* OperadorMatematico - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.interpretador;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;

public class OperadorMatematico extends AnalisadorSintatico
{
  private Map atributos = null;
  private Valor receptor = null;
  private boolean jahAdicionouObservadores = false;
  private Map colecoesEnvolvidas = new Hashtable ();
  private String identificador = null;
  private PropertyChangeListener observadorGeral = null;
  private boolean desabilitaAtribuicao = false;
  
  public OperadorMatematico (String strOp, ObjetoNegocio pInstanciaPrincipal, Map pAtributos, String pReceptor, String pIdentificador, PropertyChangeListener pObs)
  {
    this (strOp, pInstanciaPrincipal, pAtributos);
    if (pReceptor != null && pReceptor.trim ().length () > 0)
      {
	if (atributos.containsKey (pReceptor))
	  receptor = (Valor) FabricaUtilitarios.obtemAtributo (instanciaPrincipal, (String) atributos.get (pReceptor));
	else
	  receptor = (Valor) FabricaUtilitarios.obtemAtributo (instanciaPrincipal, pReceptor);
      }
    identificador = pIdentificador;
    observadorGeral = pObs;
  }
  
  private OperadorMatematico (String strOp, ObjetoNegocio pInstanciaPrincipal, Map pAtributos)
  {
    super (strOp, pInstanciaPrincipal);
    if (pAtributos != null)
      atributos = pAtributos;
    else
      atributos = new Hashtable ();
  }
  
  public Valor operacao ()
  {
    Valor operando1 = operando ();
    avancaToken ();
    if (getTokenAtual () == null || ! ehOperador (getTokenAtual ()))
      return operando1;
    char sinal = getTokenAtual ().charAt (0);
    avancaToken ();
    Valor retorno = operador (sinal, operando1);
    return retorno;
  }
  
  private Valor atribuicao ()
  {
    boolean literalComObservador = getTokenAtual ().equals ("==>");
    avancaToken ();
    Informacao informacaoAserAlterada = literal (getTokenAtual (), false);
    avancaToken ();
    avancaToken ();
    Informacao novoValor = null;
    if (getTokenAtual ().equals ("{"))
      {
	avancaToken ();
	novoValor = literal (getTokenAtual (), literalComObservador);
	avancaToken ();
	avancaToken ();
      }
    else
      novoValor = operacao ();
    if (! desabilitaAtribuicao)
      informacaoAserAlterada.setConteudo (novoValor.getConteudoFormatado ());
    if (! getTokenAtual ().equals (";"))
      avancaToken ();
    if (getToken (posAtual + 1) != null && (getToken (posAtual + 1).equals ("==>") || getToken (posAtual + 1).equals ("!==>")))
      {
	avancaToken ();
	atribuicao ();
      }
    return new Valor ();
  }
  
  private Valor operando ()
  {
    if (getTokenAtual ().equals ("==>") || getTokenAtual ().equals ("!==>"))
      {
	Valor ret = atribuicao ();
	return ret;
      }
    if (getTokenAtual ().equals ("("))
      {
	avancaToken ();
	Valor retorno = operacao ();
	avancaToken ();
	return retorno;
      }
    if (ehFuncao (getTokenAtual ()))
      return funcao ();
    Informacao ret = literal (getTokenAtual ());
    if (! (ret instanceof Valor))
      LogPPGD.erro ("Erro no token->" + getTokenAtual ());
    return (Valor) ret;
  }
  
  private Valor funcao ()
  {
    if (getTokenAtual ().toUpperCase ().equals ("ARRED"))
      return arredonda ();
    if (getTokenAtual ().toUpperCase ().equals ("PARA_CADA_ITEM_EM"))
      return percorreColecao ();
    return soma ();
  }
  
  private Valor percorreColecao ()
  {
    avancaToken ();
    String strColecao = getTokenAtual ();
    Colecao colecao = null;
    if (atributos.containsKey (strColecao))
      strColecao = (String) atributos.get (strColecao);
    if (strColecao.toUpperCase ().equals ("THIS"))
      colecao = (Colecao) instanciaPrincipal;
    else
      {
	int posicaoLasanha = strColecao.indexOf ("#");
	if (posicaoLasanha != -1)
	  {
	    ObjetoNegocio obj = null;
	    if (posicaoLasanha == 0)
	      obj = instanciaPrincipal;
	    else
	      obj = (ObjetoNegocio) FabricaUtilitarios.obtemAtributo (instanciaPrincipal, strColecao.substring (0, posicaoLasanha));
	    String nomeMetodo = strColecao.substring (posicaoLasanha + 1, strColecao.length ());
	    colecao = (Colecao) FabricaUtilitarios.invocaMetodo (nomeMetodo, obj, new Class[0], new Object[0]);
	  }
	else
	  colecao = (Colecao) FabricaUtilitarios.obtemAtributo (instanciaPrincipal, strColecao);
      }
    if (! jahAdicionouObservadores)
      colecao.getObservadoresLista ().addPropertyChangeListener (this);
    avancaToken ();
    String tokenAtual = getTokenAtual ();
    char sinalOperacao = '+';
    if (tokenAtual.toUpperCase ().equals ("RESULT"))
      {
	avancaToken ();
	sinalOperacao = getTokenAtual ().charAt (0);
	avancaToken ();
      }
    int posicaoToken = posAtual;
    ObjetoNegocio ultimoObjetoNegocio = instanciaPrincipal;
    Valor retorno = new Valor ();
    if (receptor != null)
      retorno.converte (receptor);
    Iterator itItens = colecao.recuperarLista ().iterator ();
    boolean operou = false;
    while (itItens.hasNext ())
      {
	ObjetoNegocio objAtual = (ObjetoNegocio) itItens.next ();
	instanciaPrincipal = objAtual;
	posAtual = posicaoToken;
	retorno.append (sinalOperacao, operando ());
	operou = true;
      }
    instanciaPrincipal = ultimoObjetoNegocio;
    while (! getTokenAtual ().equals ("FIM_PARA_CADA_ITEM_EM"))
      avancaToken ();
    tokenAtual = getTokenAtual ();
    return retorno;
  }
  
  private Valor soma ()
  {
    Valor retorno = new Valor ();
    avancaToken ();
    avancaToken ();
    String strColecao = getTokenAtual ();
    Colecao colecao = null;
    if (strColecao.toUpperCase ().equals ("THIS"))
      colecao = (Colecao) instanciaPrincipal;
    else
      colecao = (Colecao) FabricaUtilitarios.obtemAtributo (instanciaPrincipal, strColecao);
    avancaToken ();
    avancaToken ();
    String intervaloItens = getTokenAtual ();
    int itemInicial = 0;
    int itemFinal = 0;
    boolean todosOsItens = intervaloItens.equals ("*");
    boolean itensAlternados = ! todosOsItens && intervaloItens.indexOf (";") != -1;
    if (! todosOsItens)
      {
	if (itensAlternados)
	  {
	    int indice = intervaloItens.indexOf (";");
	    itemInicial = Integer.parseInt (intervaloItens.substring (0, indice));
	    itemFinal = Integer.parseInt (intervaloItens.substring (indice + 1, intervaloItens.length ()));
	  }
	else
	  {
	    int indice = intervaloItens.indexOf (":");
	    itemInicial = Integer.parseInt (intervaloItens.substring (0, indice));
	    itemFinal = Integer.parseInt (intervaloItens.substring (indice + 1, intervaloItens.length ()));
	  }
      }
    avancaToken ();
    avancaToken ();
    String nomeAtributoItemColecao = getTokenAtual ();
    if (! jahAdicionouObservadores)
      {
	colecao.getObservadoresLista ().addPropertyChangeListener (this);
	Map listaAtributosColecaoEnvolvidosNoCalculo = null;
	if (colecoesEnvolvidas.containsKey (colecao))
	  listaAtributosColecaoEnvolvidosNoCalculo = (Hashtable) colecoesEnvolvidas.get (colecao);
	else
	  listaAtributosColecaoEnvolvidosNoCalculo = new Hashtable ();
	listaAtributosColecaoEnvolvidosNoCalculo.put (nomeAtributoItemColecao, nomeAtributoItemColecao);
	colecoesEnvolvidas.put (colecao, listaAtributosColecaoEnvolvidosNoCalculo);
      }
    if (todosOsItens)
      {
	Iterator itItens = colecao.recuperarLista ().iterator ();
	while (itItens.hasNext ())
	  {
	    ObjetoNegocio item = (ObjetoNegocio) itItens.next ();
	    Valor atributo = (Valor) FabricaUtilitarios.obtemAtributo (item, nomeAtributoItemColecao);
	    retorno.converte (atributo);
	    retorno.append ('+', atributo);
	  }
      }
    else if (itemFinal < colecao.recuperarLista ().size ())
      {
	if (itensAlternados)
	  {
	    ObjetoNegocio objInicial = (ObjetoNegocio) colecao.recuperarLista ().get (itemInicial - 1);
	    ObjetoNegocio objFinal = (ObjetoNegocio) colecao.recuperarLista ().get (itemFinal - 1);
	    Valor atributo1 = (Valor) FabricaUtilitarios.obtemAtributo (objInicial, nomeAtributoItemColecao);
	    Valor atributo2 = (Valor) FabricaUtilitarios.obtemAtributo (objFinal, nomeAtributoItemColecao);
	    retorno.converte (atributo1);
	    retorno.append ('+', atributo1);
	    retorno.append ('+', atributo2);
	  }
	else
	  {
	    for (int i = itemInicial - 1; i <= itemFinal - 1; i++)
	      {
		ObjetoNegocio item = (ObjetoNegocio) colecao.recuperarLista ().get (i);
		Valor atributo = (Valor) FabricaUtilitarios.obtemAtributo (item, nomeAtributoItemColecao);
		retorno.converte (atributo);
		retorno.append ('+', atributo);
	      }
	  }
      }
    avancaToken ();
    return retorno;
  }
  
  private Valor arredonda ()
  {
    Valor retorno = new Valor ();
    avancaToken ();
    avancaToken ();
    Valor operando = operando ();
    avancaToken ();
    avancaToken ();
    int qtdCasas = Integer.parseInt (getTokenAtual ());
    avancaToken ();
    avancaToken ();
    int piso = Integer.parseInt (getTokenAtual ());
    avancaToken ();
    retorno.converte (operando);
    retorno.setConteudo (operando.getConteudoFormatado ());
    retorno.arredonda (qtdCasas, piso);
    return retorno;
  }
  
  private boolean ehFuncao (String str)
  {
    return str.toUpperCase ().equals ("ARRED") || str.toUpperCase ().equals ("SOMA") || str.toUpperCase ().equals ("PARA_CADA_ITEM_EM");
  }
  
  private Valor operador (char pSinal, Valor operandoAnt)
  {
    Valor retorno = new Valor ();
    retorno.converte (operandoAnt);
    retorno.setConteudo (operandoAnt);
    Valor operando2 = operando ();
    retorno.append (pSinal, operando2);
    if (temMaisTokens () && ehOperador (getToken (posAtual + 1)))
      {
	avancaToken ();
	char sinal = getTokenAtual ().charAt (0);
	avancaToken ();
	return operador (sinal, retorno);
      }
    return retorno;
  }
  
  private Informacao literal (String strLiteral)
  {
    return literal (strLiteral, true);
  }
  
  private Informacao literal (String strLiteral, boolean adicionaObservador)
  {
    Informacao retorno = null;
    String atributo = strLiteral;
    if (atributos.containsKey (strLiteral))
      atributo = (String) atributos.get (strLiteral);
    if (ehCondicional (atributo))
      retorno = condicional ();
    else if (ehNumero (atributo))
      retorno = new Valor (atributo);
    else
      {
	try
	  {
	    int posicaoLasanha = atributo.indexOf ("#");
	    if (posicaoLasanha != -1)
	      {
		ObjetoNegocio obj = null;
		if (posicaoLasanha == 0)
		  obj = instanciaPrincipal;
		else
		  obj = (ObjetoNegocio) FabricaUtilitarios.obtemAtributo (instanciaPrincipal, atributo.substring (0, posicaoLasanha));
		String nomeMetodo = atributo.substring (posicaoLasanha + 1, atributo.length ());
		Informacao retornoMetodo = (Informacao) FabricaUtilitarios.invocaMetodo (nomeMetodo, obj, new Class[0], new Object[0]);
		if (retornoMetodo != null)
		  retorno = retornoMetodo;
		else
		  retorno = new Valor ();
	      }
	    else
	      retorno = (Informacao) FabricaUtilitarios.obtemAtributoComExcecao (instanciaPrincipal, atributo);
	  }
	catch (Exception e)
	  {
	    retorno = new Alfa ();
	    if (atributo.indexOf ("'") != -1)
	      atributo = atributo.replaceAll ("'", "");
	    retorno.setConteudo (atributo);
	  }
      }
    if (! possuiObservadorGeral (retorno) && adicionaObservador)
      retorno.getObservadores ().addPropertyChangeListener (observadorGeral);
    return retorno;
  }
  
  private boolean possuiObservadorGeral (Informacao info)
  {
    PropertyChangeListener[] listeners = info.getObservadores ().getPropertyChangeListeners ();
    for (int i = listeners.length - 1; i >= 0; i--)
      {
	if (listeners[i].equals (observadorGeral))
	  return true;
      }
    return false;
  }
  
  private boolean ehNumero (String token)
  {
    for (int i = 0; i < token.length (); i++)
      {
	boolean ehNum = Character.isDigit (token.charAt (i)) || token.charAt (i) == '.' || token.charAt (i) == ',';
	if (! ehNum)
	  return false;
      }
    return true;
  }
  
  private Valor condicional ()
  {
    avancaToken ();
    boolean condicao = condicao ();
    avancaToken ();
    avancaToken ();
    if (! condicao)
      desabilitaAtribuicao = true;
    else
      desabilitaAtribuicao = false;
    Valor operando1 = operando ();
    avancaToken ();
    avancaToken ();
    if (condicao)
      desabilitaAtribuicao = true;
    else
      desabilitaAtribuicao = false;
    Valor operando2 = operando ();
    desabilitaAtribuicao = false;
    if (condicao)
      return operando1;
    return operando2;
  }
  
  private boolean condicao ()
  {
    boolean retorno = false;
    if (getTokenAtual ().equals ("["))
      {
	avancaToken ();
	retorno = condicao ();
	avancaToken ();
	String logico = getTokenAtual ();
	avancaToken ();
	boolean segundaCondicao = condicao ();
	avancaToken ();
	if (logico.toUpperCase ().equals ("E"))
	  retorno = retorno && segundaCondicao;
	else if (logico.toUpperCase ().equals ("OU"))
	  retorno = retorno || segundaCondicao;
	else
	  throw new IllegalArgumentException ("Operador l\u00f3gico E ou OU esperado");
      }
    else
      {
	if (getTokenAtual ().startsWith ("{"))
	  return comparacaoLiteral ();
	return comparacao ();
      }
    return retorno;
  }
  
  private boolean comparacao ()
  {
    Valor operando1 = operando ();
    avancaToken ();
    String comp = getTokenAtual ();
    avancaToken ();
    Valor operando2 = operando ();
    return operando1.comparacao (comp, operando2);
  }
  
  private boolean comparacaoLiteral ()
  {
    avancaToken ();
    Informacao literal1 = literal (getTokenAtual ());
    avancaToken ();
    String sinalComp = getTokenAtual ();
    avancaToken ();
    Informacao literal2 = literal (getTokenAtual ());
    boolean retorno = true;
    if (literal1 instanceof Valor && literal2 instanceof Valor)
      retorno = ((Valor) literal1).comparacao (sinalComp, literal2.getConteudoFormatado ());
    else if (sinalComp.equals ("!=") || sinalComp.equals ("<>"))
      retorno = ! literal1.getConteudoFormatado ().equals (literal2.getConteudoFormatado ());
    else if (sinalComp.equals ("="))
      retorno = literal1.getConteudoFormatado ().equals (literal2.getConteudoFormatado ());
    avancaToken ();
    return retorno;
  }
  
  public void propertyChange (PropertyChangeEvent evt)
  {
    String propertyName = evt != null ? evt.getPropertyName () : null;
    if (propertyName != null && propertyName.equals ("ObjetoInserido"))
      {
	Colecao source = (Colecao) evt.getSource ();
	if (colecoesEnvolvidas.containsKey (source))
	  {
	    ObjetoNegocio obj = (ObjetoNegocio) evt.getNewValue ();
	    Iterator itAtr = ((Map) colecoesEnvolvidas.get (source)).values ().iterator ();
	    while (itAtr.hasNext ())
	      {
		String prop = (String) itAtr.next ();
		Informacao infoAtual = (Informacao) FabricaUtilitarios.getValorFieldGenerico (prop, obj);
		infoAtual.getObservadores ().addPropertyChangeListener (observadorGeral);
	      }
	  }
	atualiza ();
      }
    else if (propertyName != null && propertyName.equals ("ObjetoAremover"))
      {
	Colecao source = (Colecao) evt.getSource ();
	ObjetoNegocio obj = (ObjetoNegocio) evt.getNewValue ();
	if (colecoesEnvolvidas.containsKey (source))
	  {
	    Iterator itAtr = ((Map) colecoesEnvolvidas.get (source)).values ().iterator ();
	    while (itAtr.hasNext ())
	      {
		String prop = (String) itAtr.next ();
		Informacao infoAtual = (Informacao) FabricaUtilitarios.getValorFieldGenerico (prop, obj);
		infoAtual.getObservadores ().removePropertyChangeListener (observadorGeral);
	      }
	  }
	else
	  obj.removeObservadores (new Class[] { observadorGeral.getClass () });
      }
    else if (propertyName != null && propertyName.equals ("ObjetoRemovido"))
      atualiza ();
  }
  
  public void atualiza ()
  {
    try
      {
	posAtual = 0;
	if (receptor != null)
	  receptor.setConteudo (operacao ().getConteudoFormatado ());
	else
	  operacao ();
	jahAdicionouObservadores = true;
      }
    catch (Exception e)
      {
	LogPPGD.erro ("###############ERRO na operacao##############");
	LogPPGD.erro ("identificador->" + getIdentificador ());
	int posErro = posAtual;
	for (posAtual = 0; posAtual <= posErro; posAtual++)
	  LogPPGD.erro (" " + getTokenAtual ());
	LogPPGD.erro (" ");
	LogPPGD.erro ("####################################");
	e.printStackTrace ();
      }
  }
  
  public static void main (String[] args)
  {
    Dec dec = new Dec (null);
    OperadorMatematico op = new OperadorMatematico (" ( 45 * 50 ) + 20 +  SE [ [ { nome != Joselito } E 1 = 1 ] E [ 50 < 12,09 OU 2 = 2 ] ] ENTAO SE 1 != 1 ENTAO 250 SENAO 100  SENAO 10      ", dec, null);
    System.out.println ("Val->" + op.operacao ().getConteudoFormatado ());
  }
  
  public Valor getReceptor ()
  {
    return receptor;
  }
  
  public void setReceptor (Valor receptor)
  {
    this.receptor = receptor;
  }
  
  public Map getColecoesEnvolvidas ()
  {
    return colecoesEnvolvidas;
  }
  
  public boolean isJahAdicionouObservadores ()
  {
    return jahAdicionouObservadores;
  }
  
  public String getIdentificador ()
  {
    return identificador;
  }
  
  public void setIdentificador (String identificador)
  {
    this.identificador = identificador;
  }
}
