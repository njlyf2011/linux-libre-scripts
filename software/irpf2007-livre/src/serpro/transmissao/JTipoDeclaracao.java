/* JTipoDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.transmissao;

public class JTipoDeclaracao
{
  public String m_strIDDeclaracao;
  public String m_strLabelNome;
  public String m_strLabelNI;
  public String m_strExercicio;
  public String m_strFormatoNI;
  public int m_iTipoFormulario;
  
  public JTipoDeclaracao (String string, int i, String string_0_, String string_1_, String string_2_, String string_3_)
  {
    m_strIDDeclaracao = string;
    m_iTipoFormulario = i;
    m_strLabelNome = string_0_;
    m_strLabelNI = string_1_;
    m_strExercicio = string_2_;
    m_strFormatoNI = string_3_;
  }
}
