/* RetornosValidacoes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import java.util.Enumeration;
import java.util.Vector;

public class RetornosValidacoes
{
  private Vector lista = new Vector ();
  private boolean todosValidos = true;
  private RetornoValidacao primeiroRetornoValidacaoMaisSevero = null;
  
  public void add (RetornoValidacao ret)
  {
    lista.add (ret);
    if (primeiroRetornoValidacaoMaisSevero == null)
      primeiroRetornoValidacaoMaisSevero = ret;
    if (ret.getSeveridade () > primeiroRetornoValidacaoMaisSevero.getSeveridade ())
      primeiroRetornoValidacaoMaisSevero = ret;
    if (! ret.isValido ())
      todosValidos = false;
  }
  
  public void add (byte nivelSeveridade, String msg)
  {
    RetornoValidacao ret = new RetornoValidacao (msg, nivelSeveridade);
    add (ret);
  }
  
  public void add (byte nivelSeveridade)
  {
    add (nivelSeveridade, "");
  }
  
  public void add (String mensagem)
  {
    add ((byte) 0, mensagem);
  }
  
  public RetornoValidacao getPrimeiroRetornoValidacaoMaisSevero ()
  {
    if (primeiroRetornoValidacaoMaisSevero != null)
      return primeiroRetornoValidacaoMaisSevero;
    return new RetornoValidacao ("ok", (byte) 0);
  }
  
  public boolean isTodosValidos ()
  {
    return todosValidos;
  }
  
  public Enumeration elements ()
  {
    return lista.elements ();
  }
  
  public boolean isEmpty ()
  {
    return lista.isEmpty ();
  }
  
  public void clear ()
  {
    lista.clear ();
    primeiroRetornoValidacaoMaisSevero = null;
    todosValidos = true;
  }
}
