/* MoedaEstrangeira - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.moedaestrangeira;
import serpro.ppgd.irpf.moedaestrangeira.especie.ColecaoEspecie;
import serpro.ppgd.irpf.moedaestrangeira.rendme.ColecaoRendME;
import serpro.ppgd.irpf.moedaestrangeira.rendreais.ColecaoRendReais;
import serpro.ppgd.irpf.moedaestrangeira.rendreaisme.ColecaoRendReaisMe;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;

public class MoedaEstrangeira extends ObjetoNegocio
{
  private ColecaoRendReais rendAuferidosReais = new ColecaoRendReais ();
  private ColecaoRendME rendAuferidosMoedaEstrangeira = new ColecaoRendME ();
  private ColecaoRendReaisMe rendAuferidosReaisMoedaEstrangeira = new ColecaoRendReaisMe ();
  private ColecaoEspecie especie = new ColecaoEspecie ();
  private Valor totalImpostoPagoSobreGanhosCapital = new Valor (this, "");
  
  public ColecaoRendME getRendAuferidosMoedaEstrangeira ()
  {
    return rendAuferidosMoedaEstrangeira;
  }
  
  public ColecaoRendReais getRendAuferidosReais ()
  {
    return rendAuferidosReais;
  }
  
  public ColecaoRendReaisMe getRendAuferidosReaisMoedaEstrangeira ()
  {
    return rendAuferidosReaisMoedaEstrangeira;
  }
  
  public ColecaoEspecie getEspecie ()
  {
    return especie;
  }
  
  public Valor getTotalImpostoPagoSobreGanhosCapital ()
  {
    return totalImpostoPagoSobreGanhosCapital;
  }
}
