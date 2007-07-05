/* ValidadorNIT - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.util.UtilitariosString;
import serpro.ppgd.negocio.util.Validador;

public class ValidadorNIT extends ValidadorDefault
{
  private String msgValidacao;
  
  public ValidadorNIT (byte severidade)
  {
    super (severidade);
    msgValidacao = "\"NIT\" inv\u00e1lido";
  }
  
  public ValidadorNIT (byte severidade, String pMsg)
  {
    super (severidade);
    setVerificaVazio (true);
    setMensagemValidacao (pMsg);
  }
  
  public RetornoValidacao validarImplementado ()
  {
    RetornoValidacao retornoValidacao = Validador.validarNIT (UtilitariosString.retiraMascara (getInformacao ().getConteudoFormatado ()));
    if (retornoValidacao == null)
      return null;
    if (getMensagemValidacao ().trim ().length () == 0)
      retornoValidacao.setMensagemValidacao (getInformacao ().getNomeCampo () + " " + "inv\u00e1lido");
    else
      retornoValidacao.setMensagemValidacao (getMensagemValidacao ());
    return retornoValidacao;
  }
}
