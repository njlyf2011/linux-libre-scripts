/* CEP - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import serpro.ppgd.negocio.util.UtilitariosString;

public class CEP extends Informacao
{
  public CEP ()
  {
    /* empty */
  }
  
  public CEP (ObjetoNegocio owner, String nomeCampo)
  {
    super (owner, nomeCampo);
  }
  
  public CEP (String nomeCampo)
  {
    super (nomeCampo);
  }
  
  public String getConteudoFormatado ()
  {
    return UtilitariosString.formataCEP (asString ());
  }
  
  public void setConteudo (String conteudo)
  {
    super.setConteudo (UtilitariosString.retiraMascara (conteudo));
  }
}
