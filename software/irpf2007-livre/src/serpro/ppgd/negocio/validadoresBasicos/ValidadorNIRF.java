/* ValidadorNIRF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.util.UtilitariosString;
import serpro.ppgd.negocio.util.Validador;

public class ValidadorNIRF extends ValidadorDefault
{
  private String msgValidacao = "N\u00famero do im\u00f3vel na SRF inv\u00e1lido";
  
  public ValidadorNIRF (byte severidade)
  {
    super (severidade);
  }
  
  public RetornoValidacao validarImplementado ()
  {
    RetornoValidacao retornoValidacao = Validador.validarNIRF (UtilitariosString.retiraMascara (getInformacao ().getConteudoFormatado ()));
    if (retornoValidacao == null)
      return null;
    retornoValidacao.setSeveridade (getSeveridade ());
    retornoValidacao.setMensagemValidacao (msgValidacao);
    return retornoValidacao;
  }
}
