/* ValidadorCPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.util.UtilitariosString;
import serpro.ppgd.negocio.util.Validador;

public class ValidadorCPF extends ValidadorDefault
{
  private String msgValidacao = "\"CPF\" inv\u00e1lido";
  
  public ValidadorCPF (byte severidade)
  {
    super (severidade);
  }
  
  public RetornoValidacao validarImplementado ()
  {
    RetornoValidacao retornoValidacao = Validador.validarCPF (UtilitariosString.retiraMascara (getInformacao ().getConteudoFormatado ()));
    if (retornoValidacao == null)
      return null;
    if (getMensagemValidacao ().trim ().length () == 0)
      retornoValidacao.setMensagemValidacao (getInformacao ().getNomeCampo () + " " + "inv\u00e1lido");
    else
      retornoValidacao.setMensagemValidacao (getMensagemValidacao ());
    return retornoValidacao;
  }
}
