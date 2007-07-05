/* ValidadorImpeditivoDefault - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;

public abstract class ValidadorImpeditivoDefault extends ValidadorDefault
{
  public static final int OK_IMPEDITIVO = 0;
  public static final int OK_CANCELAR = 1;
  public static final int EXIBE_POPUP = 0;
  public static final int EXIBE_PAINELDICAS = 1;
  protected Object proximoConteudo = "";
  protected int tipoExibicao = 0;
  private String stringOkCustomizada = null;
  private String stringCancelarCustomizada = null;
  private boolean desfazModificacaoAoCancelar = true;
  protected int tipoMensagem = -1;
  
  public ValidadorImpeditivoDefault (String msg)
  {
    super ((byte) 3);
    setSeveridade ((byte) 4);
    setMensagemValidacao (msg);
  }
  
  public abstract void acaoOk ();
  
  public abstract void acaoCancelar ();
  
  public abstract String getTituloPopup ();
  
  public Object getProximoConteudo ()
  {
    return proximoConteudo;
  }
  
  public void setProximoConteudo (Object proximoConteudo)
  {
    this.proximoConteudo = proximoConteudo;
  }
  
  public int getTipoExibicao ()
  {
    return tipoExibicao;
  }
  
  public void setTipoExibicao (int tipoExibicao)
  {
    this.tipoExibicao = tipoExibicao;
  }
  
  public void setStringOkCustomizada (String stringOkCustomizada)
  {
    this.stringOkCustomizada = stringOkCustomizada;
  }
  
  public String getStringOkCustomizada ()
  {
    return stringOkCustomizada;
  }
  
  public void setStringCancelarCustomizada (String stringCancelarCustomizada)
  {
    this.stringCancelarCustomizada = stringCancelarCustomizada;
  }
  
  public String getStringCancelarCustomizada ()
  {
    return stringCancelarCustomizada;
  }
  
  public boolean isDesfazModificacaoAoCancelar ()
  {
    return desfazModificacaoAoCancelar;
  }
  
  public void setDesfazModificacaoAoCancelar (boolean desfazModificacaoAoCancelar)
  {
    this.desfazModificacaoAoCancelar = desfazModificacaoAoCancelar;
  }
  
  public int getTipoMensagem ()
  {
    return tipoMensagem;
  }
  
  public void setTipoMensagem (int tipoMensagem)
  {
    this.tipoMensagem = tipoMensagem;
  }
}
