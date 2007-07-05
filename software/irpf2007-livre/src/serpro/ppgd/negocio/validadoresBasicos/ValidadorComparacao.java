/* ValidadorComparacao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;

public class ValidadorComparacao extends ValidadorDefault
{
  public static final byte DEVE_SER_IGUAL = 0;
  public static final byte DEVE_SER_DIFERENTE = 1;
  public static final byte DEVE_SER_MENOR = -2;
  public static final byte DEVE_SER_MENOR_OU_IGUAL = -3;
  public static final byte DEVE_SER_MAIOR = 2;
  public static final byte DEVE_SER_MAIOR_OU_IGUAL = 3;
  private byte regraValidacao = 1;
  
  public ValidadorComparacao (byte severidade)
  {
    super (severidade);
  }
  
  public ValidadorComparacao (byte severidade, byte regra, Object parValidacao)
  {
    super (severidade);
    regraValidacao = regra;
    setParValidacao (parValidacao);
  }
  
  public void setRegraValidacao (byte regra)
  {
    regraValidacao = regra;
  }
  
  public RetornoValidacao validarImplementado ()
  {
    String msg = null;
    int r = getInformacao ().compareTo (getParValidacao ());
    String nomeParValidacao;
    if (getParValidacao () instanceof Informacao)
      nomeParValidacao = ((Informacao) getParValidacao ()).getNomeCampo ();
    else
      nomeParValidacao = getParValidacao ().toString ();
    switch (regraValidacao)
      {
      case 0:
	if (r != 0)
	  msg = "\"" + getInformacao ().getNomeCampo () + " \u00e9 diferente de " + "\"" + nomeParValidacao;
	break;
      case 1:
	if (r == 0)
	  msg = "\"" + getInformacao ().getNomeCampo () + " \u00e9 igual de " + "\"" + nomeParValidacao;
	break;
      case -2:
	if (r >= 0)
	  msg = "\"" + getInformacao ().getNomeCampo () + " deve ser menor que " + "\"" + nomeParValidacao;
	break;
      case -3:
	if (r > 0)
	  msg = "\"" + getInformacao ().getNomeCampo () + " deve ser menor ou igual que " + "\"" + nomeParValidacao;
	break;
      case 2:
	if (r <= 0)
	  msg = "\"" + getInformacao ().getNomeCampo () + " deve ser maior que " + "\"" + nomeParValidacao;
	break;
      case 3:
	if (r < 0)
	  msg = "\"" + getInformacao ().getNomeCampo () + " deve ser maior ou igual que " + "\"" + nomeParValidacao;
	break;
      default:
	throw new IllegalArgumentException ("regra de valida\u00e7\u00e3o inv\u00e1lida: " + regraValidacao);
      }
    if (msg != null)
      return new RetornoValidacao (msg);
    return null;
  }
}
