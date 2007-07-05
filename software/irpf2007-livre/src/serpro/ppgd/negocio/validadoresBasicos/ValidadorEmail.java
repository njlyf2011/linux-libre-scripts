/* ValidadorEmail - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.util.Validador;

public class ValidadorEmail extends ValidadorDefault
{
  public ValidadorEmail (byte severidade)
  {
    super (severidade);
  }
  
  public RetornoValidacao validarImplementado ()
  {
    String email = getInformacao ().getConteudoFormatado ();
    Pattern padrao = Pattern.compile ("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");
    Matcher pesquisa = padrao.matcher (email);
    if (! pesquisa.matches ())
      {
	setSeveridade ((byte) 3);
	return new RetornoValidacao ("E-mail inv\u00e1lido.", (byte) 3);
      }
    return Validador.validarEmail (getInformacao ().asString ());
  }
}
