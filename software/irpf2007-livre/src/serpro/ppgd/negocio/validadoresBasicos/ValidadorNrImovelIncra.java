/* ValidadorNrImovelIncra - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.util.UtilitariosString;
import serpro.ppgd.negocio.util.Validador;

public class ValidadorNrImovelIncra extends ValidadorDefault
{
  public ValidadorNrImovelIncra (byte severidade)
  {
    super (severidade);
  }
  
  public ValidadorNrImovelIncra (byte severidade, String pMsg)
  {
    super (severidade);
    setMensagemValidacao (pMsg);
  }
  
  public RetornoValidacao validarImplementado ()
  {
    if (UtilitariosString.retiraMascara (getInformacao ().getConteudoFormatado ()).trim ().length () < 13)
      return new RetornoValidacao (getMensagemValidacao (), getSeveridade ());
    String pCod = UtilitariosString.retiraMascara (getInformacao ().getConteudoFormatado ()).trim ();
    if (! Validador.validarCodImovelIncra (pCod))
      return new RetornoValidacao (getMensagemValidacao (), getSeveridade ());
    return null;
  }
}
