/* CPFDependente - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendpf;
import serpro.ppgd.negocio.CPF;
import serpro.ppgd.negocio.ObjetoNegocio;

public class CPFDependente extends ObjetoNegocio
{
  private CPF cpf = new CPF (this, "");
  
  public CPF getCpf ()
  {
    return cpf;
  }
}
