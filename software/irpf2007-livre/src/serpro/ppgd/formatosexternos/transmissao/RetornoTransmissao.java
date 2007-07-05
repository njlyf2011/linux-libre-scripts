/* RetornoTransmissao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.transmissao;

public class RetornoTransmissao
{
  private int codRetorno = 0;
  private String msgRetorno = null;
  
  public RetornoTransmissao (int pCodRetorno, String pMsgRetorno)
  {
    codRetorno = pCodRetorno;
    msgRetorno = pMsgRetorno;
  }
  
  private RetornoTransmissao ()
  {
    /* empty */
  }
  
  public int getCodRetorno ()
  {
    return codRetorno;
  }
  
  public void setCodRetorno (int codRetorno)
  {
    this.codRetorno = codRetorno;
  }
  
  public String getMsgRetorno ()
  {
    return msgRetorno;
  }
  
  public void setMsgRetorno (String msgRetorno)
  {
    this.msgRetorno = msgRetorno;
  }
}
