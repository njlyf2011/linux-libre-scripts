/* Pendencia - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;

public class Pendencia
{
  private boolean erro;
  private String msg;
  private int numItem;
  private String campo;
  private String entidade;
  private byte severidade;
  private Informacao campoInformacao;
  private String descricaoCampo;
  
  public Pendencia (byte severidade, Informacao campoInformacao, String descricaoCampo, String mensagem, int numItem)
  {
    setSeveridade (severidade);
    setCampoInformacao (campoInformacao);
    setDescricaoCampo (campoInformacao.getNomeCampo ());
    setMsg (mensagem);
    setNumItem (numItem);
  }
  
  public Pendencia (Exception ePendencia, String entidade, String campo, int numItem)
  {
    if (ePendencia != null)
      {
	setErro (ePendencia.getClass () == (java.lang.Exception.class));
	setMsg (ePendencia.getMessage ());
      }
    setEntidade (entidade);
    setCampo (campo);
    setNumItem (numItem);
  }
  
  public Pendencia (boolean erro, String entidade, String campo, String mensagem, int numItem)
  {
    setErro (erro);
    setEntidade (entidade);
    setCampo (campo);
    setMsg (mensagem);
    setNumItem (numItem);
  }
  
  public String getCampo ()
  {
    return campo;
  }
  
  public String getEntidade ()
  {
    return entidade;
  }
  
  public boolean isErro ()
  {
    return erro;
  }
  
  public String getMsg ()
  {
    return msg;
  }
  
  public int getNumItem ()
  {
    return numItem;
  }
  
  public void setCampo (String string)
  {
    campo = string;
  }
  
  public void setEntidade (String string)
  {
    entidade = string;
  }
  
  public void setErro (boolean b)
  {
    erro = b;
  }
  
  public void setMsg (String string)
  {
    msg = string;
  }
  
  public void setNumItem (int i)
  {
    numItem = i;
  }
  
  public Informacao getCampoInformacao ()
  {
    return campoInformacao;
  }
  
  public String getDescricaoCampo ()
  {
    return descricaoCampo;
  }
  
  public byte getSeveridade ()
  {
    return severidade;
  }
  
  public void setCampoInformacao (Informacao informacao)
  {
    campoInformacao = informacao;
  }
  
  public void setDescricaoCampo (String string)
  {
    descricaoCampo = string;
  }
  
  public void setSeveridade (byte b)
  {
    severidade = b;
  }
  
}
