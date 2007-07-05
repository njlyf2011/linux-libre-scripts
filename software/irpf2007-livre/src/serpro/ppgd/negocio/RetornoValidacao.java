/* RetornoValidacao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import javax.swing.Action;

public class RetornoValidacao
{
  private String mensagemValidacao;
  private byte severidade;
  protected String mensagemValidacaoExtendida = null;
  public static final byte VALIDO = 0;
  public static final byte ATENCAO = 1;
  public static final byte AVISO = 2;
  public static final byte ERRO = 3;
  public static final byte ERRO_IMPEDITIVO_OK_CANCELAR = 4;
  public static final byte ERRO_IMPEDITIVO = 5;
  private Action actionSim;
  private Action actionNao;
  
  public RetornoValidacao (byte severidade)
  {
    setSeveridade (severidade);
    mensagemValidacao = "";
  }
  
  public RetornoValidacao (String mensagem)
  {
    this ((byte) 2);
    mensagemValidacao = mensagem;
  }
  
  public RetornoValidacao (String mensagem, byte severidade)
  {
    this (severidade);
    mensagemValidacao = mensagem;
  }
  
  public void setSeveridade (byte severidade)
  {
    this.severidade = severidade;
  }
  
  public byte getSeveridade ()
  {
    return severidade;
  }
  
  public void setMensagemValidacao (String string)
  {
    mensagemValidacao = string;
  }
  
  public String getMensagemValidacao ()
  {
    return mensagemValidacao;
  }
  
  public boolean isValido ()
  {
    if (severidade <= 1 || severidade == 4)
      return true;
    return false;
  }
  
  public String toString ()
  {
    return "[Sucesso = " + isValido () + " Mensagem = " + getMensagemValidacao () + " TipoErro = " + getSeveridade () + "]";
  }
  
  public String getMensagemValidacaoExtendida ()
  {
    if (mensagemValidacaoExtendida != null)
      return mensagemValidacaoExtendida;
    return mensagemValidacao;
  }
  
  public void setMensagemValidacaoExtendida (String pMensagemValidacaoExtendida)
  {
    mensagemValidacaoExtendida = pMensagemValidacaoExtendida;
  }
  
  public void setActionSim (Action actionSim)
  {
    this.actionSim = actionSim;
  }
  
  public Action getActionSim ()
  {
    return actionSim;
  }
  
  public void setActionNao (Action actionNao)
  {
    this.actionNao = actionNao;
  }
  
  public Action getActionNao ()
  {
    return actionNao;
  }
}
