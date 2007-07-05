/* CNPJ - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import serpro.ppgd.negocio.util.UtilitariosString;

public class CNPJ extends Informacao
{
  public CNPJ ()
  {
    /* empty */
  }
  
  public CNPJ (ObjetoNegocio owner, String nomeCampo)
  {
    super (owner, nomeCampo);
  }
  
  public String getConteudoFormatado ()
  {
    return UtilitariosString.formataCNPJ (asString ());
  }
  
  public void setConteudo (String conteudo)
  {
    super.setConteudo (UtilitariosString.retiraMascara (conteudo));
  }
}
