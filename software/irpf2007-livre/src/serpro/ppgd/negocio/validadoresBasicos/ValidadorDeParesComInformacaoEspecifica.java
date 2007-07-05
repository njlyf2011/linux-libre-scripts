/* ValidadorDeParesComInformacaoEspecifica - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.util.LogPPGD;

public class ValidadorDeParesComInformacaoEspecifica extends ValidadorDefault
{
  private byte severidadeCaso1 = 1;
  private byte severidadeCaso2 = 1;
  private byte severidadeCaso3 = 1;
  private byte severidadeCaso4 = 0;
  private String informacaoValida;
  
  public ValidadorDeParesComInformacaoEspecifica (byte severidadeCaso1, byte severidadeCaso2, byte severidadeCaso3, String informacaoValida)
  {
    super ((byte) 1);
    this.severidadeCaso1 = severidadeCaso1;
    this.severidadeCaso2 = severidadeCaso2;
    this.severidadeCaso3 = severidadeCaso3;
    this.informacaoValida = informacaoValida;
  }
  
  public ValidadorDeParesComInformacaoEspecifica (byte severidadeCaso1, byte severidadeCaso2, byte severidadeCaso3, Informacao parValidacao, String informacaoValida)
  {
    super ((byte) 1);
    this.severidadeCaso1 = severidadeCaso1;
    this.severidadeCaso2 = severidadeCaso2;
    this.severidadeCaso3 = severidadeCaso3;
    setParValidacao (parValidacao);
    this.informacaoValida = informacaoValida;
  }
  
  public RetornoValidacao validarImplementado ()
  {
    Informacao parValidacao = null;
    setValidadorAtivo (false);
    if (getParValidacao () == null || ! (getParValidacao () instanceof Informacao))
      {
	String msg = "Campo " + getInformacao ().getNomeCampo () + " de " + getInformacao ().getOwner ().getClass () + " est\u00e1 com seu Par de valida\u00e7\u00e3o nulo ou n\u00e3o \u00e9 Informacao";
	LogPPGD.erro (msg);
	throw new IllegalArgumentException (msg);
      }
    parValidacao = (Informacao) getParValidacao ();
    if (! getInformacao ().isVazio ())
      getInformacao ().validar ();
    if (! parValidacao.isVazio ())
      parValidacao.validar ();
    setValidadorAtivo (true);
    boolean omitidoOuInvalido1 = getInformacao ().isVazio () || ! getInformacao ().isValido () || ! getInformacao ().asString ().equals (informacaoValida);
    boolean omitidoOuInvalido2 = parValidacao.isVazio () || ! parValidacao.isValido ();
    if (omitidoOuInvalido1 && omitidoOuInvalido2)
      {
	RetornoValidacao retornoValidacao = new RetornoValidacao ("\"" + getInformacao ().getNomeCampo () + "\" e \"" + parValidacao.getNomeCampo () + "\" est\u00e3o ambos em branco ou inv\u00e1lidos");
	retornoValidacao.setSeveridade (severidadeCaso1);
	setSeveridade (severidadeCaso1);
	LogPPGD.debug ("Valida\u00e7\u00e3o em " + this.getClass () + " do campo " + getInformacao ().getNomeCampo () + "com o campo " + parValidacao.getNomeCampo () + " teve resultado: " + severidadeCaso1);
	return retornoValidacao;
      }
    if (omitidoOuInvalido1 && ! omitidoOuInvalido2)
      {
	RetornoValidacao retornoValidacao = new RetornoValidacao ("\"" + getInformacao ().getNomeCampo () + "\" est\u00e1 omitido e \"" + parValidacao.getNomeCampo () + "\" est\u00e1 preenchido");
	setSeveridade (severidadeCaso2);
	retornoValidacao.setSeveridade (severidadeCaso2);
	return retornoValidacao;
      }
    if (! omitidoOuInvalido1 && omitidoOuInvalido2)
      {
	RetornoValidacao retornoValidacao = new RetornoValidacao ("\"" + getInformacao ().getNomeCampo () + "\" est\u00e1 preenchido com o valor " + informacaoValida + " e \"" + parValidacao.getNomeCampo () + "\" est\u00e1 omitido");
	setSeveridade (severidadeCaso3);
	retornoValidacao.setSeveridade (severidadeCaso3);
	return retornoValidacao;
      }
    RetornoValidacao retornoValidacao = new RetornoValidacao ("");
    retornoValidacao.setSeveridade (severidadeCaso4);
    return retornoValidacao;
  }
  
  public byte getSeveridadeCaso1 ()
  {
    return severidadeCaso1;
  }
  
  public byte getSeveridadeCaso2 ()
  {
    return severidadeCaso2;
  }
  
  public byte getSeveridadeCaso3 ()
  {
    return severidadeCaso3;
  }
  
  public byte getSeveridadeCaso4 ()
  {
    return severidadeCaso4;
  }
  
  public void setSeveridadeCaso1 (byte b)
  {
    severidadeCaso1 = b;
  }
  
  public void setSeveridadeCaso2 (byte b)
  {
    severidadeCaso2 = b;
  }
  
  public void setSeveridadeCaso3 (byte b)
  {
    severidadeCaso3 = b;
  }
  
  public void setSeveridadeCaso4 (byte b)
  {
    severidadeCaso4 = b;
  }
}
