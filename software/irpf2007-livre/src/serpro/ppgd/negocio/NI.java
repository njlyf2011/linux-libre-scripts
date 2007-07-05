/* NI - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import serpro.ppgd.negocio.util.UtilitariosString;

public class NI extends Informacao
{
  public NI ()
  {
    /* empty */
  }
  
  public NI (ObjetoNegocio owner, String nomeCampo)
  {
    super (owner, nomeCampo);
  }
  
  public String getConteudoFormatado ()
  {
    return UtilitariosString.formataNI (asString ());
  }
  
  public void setConteudo (String conteudo)
  {
    super.setConteudo (UtilitariosString.retiraMascara (conteudo));
  }
}
