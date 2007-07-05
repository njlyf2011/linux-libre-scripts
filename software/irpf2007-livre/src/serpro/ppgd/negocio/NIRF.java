/* NIRF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import serpro.ppgd.negocio.util.UtilitariosString;

public class NIRF extends Informacao
{
  public NIRF ()
  {
    super (null, "");
  }
  
  public NIRF (ObjetoNegocio owner, String nomeCampo)
  {
    super (owner, nomeCampo);
  }
  
  public NIRF (String pNomeCampo)
  {
    super (pNomeCampo);
  }
  
  public String getConteudoFormatado ()
  {
    return UtilitariosString.formataNIRF (asString ());
  }
  
  public void setConteudo (String conteudo)
  {
    super.setConteudo (UtilitariosString.retiraMascara (conteudo));
  }
}
