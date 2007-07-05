/* ValidadorCNPJ - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.util.UtilitariosString;
import serpro.ppgd.negocio.util.Validador;

public class ValidadorCNPJ extends ValidadorDefault
{
  private String msgValidacao = "\"CNPJ\" inv\u00e1lido";
  
  public ValidadorCNPJ (byte severidade)
  {
    super (severidade);
  }
  
  public RetornoValidacao validarImplementado ()
  {
    RetornoValidacao retornoValidacao = Validador.validarCNPJ (UtilitariosString.retiraMascara (getInformacao ().getConteudoFormatado ()));
    if (retornoValidacao == null)
      return null;
    retornoValidacao.setMensagemValidacao (getInformacao ().getNomeCampo () + " " + "inv\u00e1lido");
    return retornoValidacao;
  }
}
