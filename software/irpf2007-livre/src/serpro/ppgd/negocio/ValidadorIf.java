/* ValidadorIf - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;

public interface ValidadorIf
{
  public void setInformacao (Informacao informacao);
  
  public void setParValidacao (Object object);
  
  public void setRetornosValidacoes (RetornosValidacoes retornosvalidacoes);
  
  public void validar ();
  
  public void setValidadorAtivo (boolean bool);
  
  public boolean isValidadorAtivo ();
}
