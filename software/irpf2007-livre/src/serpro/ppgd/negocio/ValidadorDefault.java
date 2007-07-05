/* ValidadorDefault - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import serpro.ppgd.negocio.util.TabelaMensagens;

public abstract class ValidadorDefault implements ValidadorIf
{
  private Informacao informacao;
  private Object parValidacao;
  private RetornosValidacoes retornosValidacoes;
  private byte severidade;
  private boolean verificaVazio = false;
  private boolean validadorAtivo = true;
  private String mensagemValidacao = "";
  protected static TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
  
  public ValidadorDefault (byte severidade)
  {
    this.severidade = severidade;
  }
  
  public void setInformacao (Informacao informacao)
  {
    if (informacao == null)
      throw new IllegalArgumentException ("Informacao \u00e9 null");
    this.informacao = informacao;
  }
  
  protected Informacao getInformacao ()
  {
    return informacao;
  }
  
  public void setParValidacao (Object parValidacao)
  {
    this.parValidacao = parValidacao;
  }
  
  protected Object getParValidacao ()
  {
    return parValidacao;
  }
  
  public void setRetornosValidacoes (RetornosValidacoes lista)
  {
    if (lista == null)
      throw new IllegalArgumentException ("RetornosValidacoes \u00e9 null");
    retornosValidacoes = lista;
  }
  
  protected RetornosValidacoes getRetornosValidacoes ()
  {
    return retornosValidacoes;
  }
  
  public void setSeveridade (byte severidade)
  {
    this.severidade = severidade;
  }
  
  public byte getSeveridade ()
  {
    return severidade;
  }
  
  public abstract RetornoValidacao validarImplementado ();
  
  public void validar ()
  {
    if (isValidadorAtivo () && (isVerificaVazio () || ! getInformacao ().isVazio ()))
      {
	RetornoValidacao ret = validarImplementado ();
	if (ret != null)
	  {
	    if (ret.getSeveridade () > 0)
	      ret.setSeveridade (getSeveridade ());
	    if (! getMensagemValidacao ().trim ().equals (""))
	      ret.setMensagemValidacao (getMensagemValidacao ());
	    getRetornosValidacoes ().add (ret);
	  }
	else
	  getRetornosValidacoes ().add (new RetornoValidacao ((byte) 0));
      }
  }
  
  public void setVerificaVazio (boolean b)
  {
    verificaVazio = b;
  }
  
  public boolean isVerificaVazio ()
  {
    return verificaVazio;
  }
  
  public boolean isValidadorAtivo ()
  {
    return validadorAtivo;
  }
  
  public void setValidadorAtivo (boolean validadorAtivo)
  {
    this.validadorAtivo = validadorAtivo;
  }
  
  public String getMensagemValidacao ()
  {
    return mensagemValidacao;
  }
  
  public void setMensagemValidacao (String string)
  {
    mensagemValidacao = string;
  }
}
