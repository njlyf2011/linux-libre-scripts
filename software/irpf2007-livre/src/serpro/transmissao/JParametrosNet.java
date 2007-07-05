/* JParametrosNet - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.transmissao;

public class JParametrosNet
{
  public String m_strNomeArqDeclaracao;
  public String m_strMensagem;
  
  public JParametrosNet (String string)
  {
    m_strNomeArqDeclaracao = string;
    m_strMensagem = "";
  }
  
  public String getValueMensagem ()
  {
    return m_strMensagem;
  }
}
