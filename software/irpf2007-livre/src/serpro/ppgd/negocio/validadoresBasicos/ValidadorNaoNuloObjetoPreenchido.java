/* ValidadorNaoNuloObjetoPreenchido - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;

public class ValidadorNaoNuloObjetoPreenchido extends ValidadorNaoNulo
{
  public ValidadorNaoNuloObjetoPreenchido (byte severidade)
  {
    super (severidade);
  }
  
  public ValidadorNaoNuloObjetoPreenchido (byte severidade, String pMsg)
  {
    super (severidade, pMsg);
  }
  
  public void validar ()
  {
    if (! getInformacao ().getOwner ().isVazio ())
      super.validar ();
  }
}
