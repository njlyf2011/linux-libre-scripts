/* ValidadorImpeditivoLogico - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;

public abstract class ValidadorImpeditivoLogico extends ValidadorImpeditivoDefault
{
  protected String valorOpcaoDoLogico = "";
  
  public ValidadorImpeditivoLogico (String msg)
  {
    super (msg);
  }
  
  public String getValorOpcaoDoLogico ()
  {
    return valorOpcaoDoLogico;
  }
  
  public void setValorOpcaoDoLogico (String pOpcaoDoLogico)
  {
    valorOpcaoDoLogico = pOpcaoDoLogico;
  }
}
