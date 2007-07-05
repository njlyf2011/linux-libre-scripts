/* ValidadorNome - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.util.Validador;

public class ValidadorNome extends ValidadorDefault
{
  private String msgValidacao = "\"Nome\" inv\u00e1lido";
  
  public ValidadorNome (byte severidade)
  {
    super (severidade);
  }
  
  public RetornoValidacao validarImplementado ()
  {
    RetornoValidacao retorno = Validador.validarNomeCompleto (getInformacao ().asString ());
    if (retorno != null)
      {
	retorno.setSeveridade (getSeveridade ());
	if (getMensagemValidacao ().trim ().length () > 0)
	  retorno.setMensagemValidacao (getMensagemValidacao ());
      }
    return retorno;
  }
}
