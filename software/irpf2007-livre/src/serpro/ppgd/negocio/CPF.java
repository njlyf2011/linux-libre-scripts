/* CPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import serpro.ppgd.negocio.util.UtilitariosString;

public class CPF extends Informacao
{
  public CPF ()
  {
    /* empty */
  }
  
  public CPF (ObjetoNegocio owner, String nomeCampo)
  {
    super (owner, nomeCampo);
  }
  
  public String getConteudoFormatado ()
  {
    return UtilitariosString.formataCPF (asString ());
  }
  
  public void setConteudo (String conteudo)
  {
    super.setConteudo (UtilitariosString.retiraMascara (conteudo));
  }
}
