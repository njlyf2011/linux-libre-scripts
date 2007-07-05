/* ExcecaoErroDeLeitura - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.receitanet.carregador;

public class ExcecaoErroDeLeitura extends ExcecaoCarregador
{
  public ExcecaoErroDeLeitura ()
  {
    /* empty */
  }
  
  public ExcecaoErroDeLeitura (String mensagem)
  {
    super (mensagem);
  }
  
  public ExcecaoErroDeLeitura (Throwable excecao)
  {
    super (excecao);
  }
  
  public ExcecaoErroDeLeitura (String mensagem, Throwable excecao)
  {
    super (mensagem, excecao);
  }
}
