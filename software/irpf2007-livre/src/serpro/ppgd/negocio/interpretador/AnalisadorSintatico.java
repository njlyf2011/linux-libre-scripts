/* AnalisadorSintatico - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.interpretador;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;

import serpro.ppgd.negocio.ObjetoNegocio;

public abstract class AnalisadorSintatico implements PropertyChangeListener
{
  protected Map operacoes;
  protected ObjetoNegocio instanciaPrincipal;
  protected StringTokenizer tokensOperacao;
  protected LinkedList tokens;
  protected int posAtual;
  
  private AnalisadorSintatico ()
  {
    operacoes = new Hashtable ();
    instanciaPrincipal = null;
    tokensOperacao = null;
    tokens = new LinkedList ();
    posAtual = 0;
  }
  
  public AnalisadorSintatico (String strOp, ObjetoNegocio pInstanciaPrincipal)
  {
    operacoes = new Hashtable ();
    instanciaPrincipal = null;
    tokensOperacao = null;
    tokens = new LinkedList ();
    posAtual = 0;
    tokensOperacao = new StringTokenizer (strOp, " ");
    while (tokensOperacao.hasMoreTokens ())
      tokens.add (tokensOperacao.nextToken ().trim ());
    instanciaPrincipal = pInstanciaPrincipal;
  }
  
  protected String getTokenAtual ()
  {
    if (posAtual < tokens.size ())
      {
	String token = ((String) tokens.get (posAtual)).trim ();
	return token;
      }
    return null;
  }
  
  protected String getProximoToken ()
  {
    posAtual++;
    return ((String) tokens.get (posAtual)).trim ();
  }
  
  protected String getToken (int pos)
  {
    if (pos < tokens.size ())
      return (String) tokens.get (pos);
    return null;
  }
  
  protected void avancaToken ()
  {
    posAtual++;
  }
  
  protected boolean temMaisTokens ()
  {
    return posAtual + 1 < tokens.size ();
  }
  
  protected boolean ehOperador (String proxToken)
  {
    if (proxToken.equals ("+") || proxToken.equals ("-") || proxToken.equals ("*") || proxToken.equals ("/"))
      return true;
    return false;
  }
  
  protected boolean ehCondicional (String token)
  {
    return token.toUpperCase ().equals ("SE");
  }
}
