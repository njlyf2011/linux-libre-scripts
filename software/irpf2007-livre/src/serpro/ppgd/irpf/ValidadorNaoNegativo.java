/* ValidadorNaoNegativo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.Valor;

public class ValidadorNaoNegativo extends ValidadorDefault
{
  public ValidadorNaoNegativo (byte severidade)
  {
    super (severidade);
  }
  
  public RetornoValidacao validarImplementado ()
  {
    Valor info = (Valor) getInformacao ();
    if (info.comparacao ("<", "0,00"))
      return new RetornoValidacao ("Valor negativo", getSeveridade ());
    return null;
  }
}
