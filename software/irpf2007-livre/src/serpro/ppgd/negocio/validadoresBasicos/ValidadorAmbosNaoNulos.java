/* ValidadorAmbosNaoNulos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.util.LogPPGD;

public class ValidadorAmbosNaoNulos extends ValidadorDefault
{
  public ValidadorAmbosNaoNulos (Informacao parValidacao, byte severidade)
  {
    super (severidade);
    setParValidacao (parValidacao);
  }
  
  public RetornoValidacao validarImplementado ()
  {
    if (! getInformacao ().isVazio ())
      {
	setValidadorAtivo (false);
	getInformacao ().validar ();
      }
    Informacao parValidacao;
    if (getParValidacao () != null && getParValidacao () instanceof Informacao)
      {
	parValidacao = (Informacao) getParValidacao ();
	parValidacao.validar ();
      }
    else
      {
	String msg = "Campo " + getInformacao ().getNomeCampo () + " de " + getInformacao ().getOwner ().getClass () + " est\u00e1 com seu Par de valida\u00e7\u00e3o nulo ou n\u00e3o \u00e9 Informacao";
	LogPPGD.erro (msg);
	throw new IllegalArgumentException (msg);
      }
    setValidadorAtivo (true);
    boolean omitidoOuInvalido1 = getInformacao ().isVazio () || ! getInformacao ().isValido ();
    boolean omitidoOuInvalido2 = parValidacao.isVazio () || ! parValidacao.isValido ();
    if (omitidoOuInvalido1 && omitidoOuInvalido2)
      {
	RetornoValidacao retornoValidacao = new RetornoValidacao ("\"" + getInformacao ().getNomeCampo () + "\" e \"" + parValidacao.getNomeCampo () + "\" est\u00e3o ambos em branco ou inv\u00e1lidos");
	return retornoValidacao;
      }
    return null;
  }
}
