/* ValidadorCEP - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.util.Validador;

public class ValidadorCEP extends ValidadorDefault
{
  public ValidadorCEP (byte severidade)
  {
    super (severidade);
  }
  
  public RetornoValidacao validarImplementado ()
  {
    return Validador.validarCEP (getInformacao ().asString ());
  }
}
