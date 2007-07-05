/* ValidadorImpeditivoOpcao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;

public abstract class ValidadorImpeditivoOpcao extends ValidadorImpeditivoDefault
{
  protected String valorCodigoOpcao = "";
  
  public ValidadorImpeditivoOpcao (String msg)
  {
    super (msg);
  }
  
  public String getValorCodigoOpcao ()
  {
    return valorCodigoOpcao;
  }
  
  public void setValorCodigoOpcao (String valorCodigoOpcao)
  {
    this.valorCodigoOpcao = valorCodigoOpcao;
  }
}
