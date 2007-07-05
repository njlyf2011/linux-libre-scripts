/* ExcecaoCarregador - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.receitanet.carregador;

public class ExcecaoCarregador extends RuntimeException
{
  public ExcecaoCarregador ()
  {
    /* empty */
  }
  
  public ExcecaoCarregador (String mensagem)
  {
    super (mensagem);
  }
  
  public ExcecaoCarregador (Throwable excecao)
  {
    super (excecao);
  }
  
  public ExcecaoCarregador (String mensagem, Throwable excecao)
  {
    super (mensagem, excecao);
  }
}
