/* ValidadorNaoNulo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;

public class ValidadorNaoNulo extends ValidadorDefault
{
  public ValidadorNaoNulo (byte severidade)
  {
    super (severidade);
    setVerificaVazio (true);
  }
  
  public ValidadorNaoNulo (byte severidade, String pMsg)
  {
    super (severidade);
    setVerificaVazio (true);
    setMensagemValidacao (pMsg);
  }
  
  public RetornoValidacao validarImplementado ()
  {
    if (getInformacao ().isVazio ())
      {
	if (getMensagemValidacao ().trim ().length () > 0)
	  return new RetornoValidacao (getMensagemValidacao ());
	return new RetornoValidacao ("\"" + getInformacao ().getNomeCampo () + "\" " + "est\u00e1 em branco");
      }
    return null;
  }
}
