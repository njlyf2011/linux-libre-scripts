/* ValidadorNI - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.util.Validador;

public class ValidadorNI extends ValidadorDefault
{
  public ValidadorNI (byte severidade)
  {
    super (severidade);
  }
  
  public RetornoValidacao validarImplementado ()
  {
    RetornoValidacao retornoValidacao = Validador.validarNI (getInformacao ().asString ());
    if (retornoValidacao == null)
      return null;
    retornoValidacao.setMensagemValidacao ("\"" + getInformacao ().getNomeCampo () + "\" " + "inv\u00e1lido");
    return retornoValidacao;
  }
}
