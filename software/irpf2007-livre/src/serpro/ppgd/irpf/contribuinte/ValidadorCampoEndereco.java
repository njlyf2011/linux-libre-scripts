/* ValidadorCampoEndereco - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.contribuinte;
import java.util.StringTokenizer;

import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;

public class ValidadorCampoEndereco extends ValidadorDefault
{
  private static final String MSG_ERRO = " com tr\u00eas ou mais caracteres consecutivos repetidos";
  
  public ValidadorCampoEndereco (byte severidade)
  {
    super (severidade);
  }
  
  public RetornoValidacao validarImplementado ()
  {
    return validaStringCompleta ();
  }
  
  private RetornoValidacao validaStringCompleta ()
  {
    if (getInformacao ().isVazio ())
      return null;
    StringTokenizer testeNomeCompleto = new StringTokenizer (getInformacao ().getConteudoFormatado ());
    RetornoValidacao rValidacao = null;
    while (testeNomeCompleto.hasMoreTokens ())
      {
	String testeNome = testeNomeCompleto.nextToken ();
	if (testeNome.length () >= 3)
	  {
	    rValidacao = validaToken (testeNome);
	    if (rValidacao != null)
	      return rValidacao;
	  }
      }
    return null;
  }
  
  private RetornoValidacao validaToken (String pToken)
  {
    int letrasRepetidas = 1;
    for (int j = 1; j < pToken.length (); j++)
      {
	if (pToken.charAt (j) == pToken.charAt (j - 1) && ! Character.isDigit (pToken.charAt (j)))
	  letrasRepetidas++;
	else
	  letrasRepetidas = 1;
	if (letrasRepetidas >= 3 && ! pToken.equals ("III"))
	  return new RetornoValidacao ("O campo \"" + getInformacao ().getNomeCampo () + "\" est\u00e1" + " com tr\u00eas ou mais caracteres consecutivos repetidos");
      }
    return null;
  }
}
