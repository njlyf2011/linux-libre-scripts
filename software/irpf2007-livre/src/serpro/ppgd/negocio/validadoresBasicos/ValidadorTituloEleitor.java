/* ValidadorTituloEleitor - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.util.UtilitariosString;
import serpro.ppgd.negocio.util.Validador;

public class ValidadorTituloEleitor extends ValidadorDefault
{
  private final String MENSAGEM = "\"N\u00famero do t\u00edtulo eleitoral\" inv\u00e1lido";
  private String msgValidacao = "\"N\u00famero do t\u00edtulo eleitoral\" inv\u00e1lido";
  
  public ValidadorTituloEleitor (byte severidade)
  {
    super (severidade);
  }
  
  public RetornoValidacao validarImplementado ()
  {
    return Validador.validarTituloEleitor (UtilitariosString.retiraMascara (getInformacao ().asString ()));
  }
}
