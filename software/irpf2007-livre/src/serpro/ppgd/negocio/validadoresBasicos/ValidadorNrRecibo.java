/* ValidadorNrRecibo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import serpro.ppgd.negocio.Logico;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.util.Validador;

public class ValidadorNrRecibo extends ValidadorDefault
{
  private Logico validar;
  private String MSG = "O n\u00famero do recibo da declara\u00e7\u00e3o anterior n\u00e3o foi preenchido";
  
  public ValidadorNrRecibo (byte severidade, Logico validar, String pMsg)
  {
    super (severidade);
    this.validar = validar;
    if (pMsg != null)
      setMensagemValidacao (pMsg);
  }
  
  public RetornoValidacao validarImplementado ()
  {
    if (! validar.getConteudoFormatado ().equals (Logico.SIM))
      return null;
    if (Validador.validarNrRecibo (getInformacao ().asString ()))
      return null;
    return new RetornoValidacao (getMensagemValidacao (), getSeveridade ());
  }
  
  public Logico getValidar ()
  {
    return validar;
  }
  
  public void setValidar (Logico validar)
  {
    this.validar = validar;
  }
}
