/* GanhosDeCapital - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.ganhosdecapital;
import serpro.ppgd.irpf.ganhosdecapital.bensimoveis.ColecaoBensImoveis;
import serpro.ppgd.irpf.ganhosdecapital.bensmoveis.ColecaoBensMoveis;
import serpro.ppgd.irpf.ganhosdecapital.psocietarias.ColecaoPSocietarias;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;

public class GanhosDeCapital extends ObjetoNegocio
{
  private ColecaoBensImoveis bensImoveis = new ColecaoBensImoveis ();
  private ColecaoBensMoveis bensMoveis = new ColecaoBensMoveis ();
  private ColecaoPSocietarias participacoesSocietarias = new ColecaoPSocietarias ();
  private Valor totalImpostoPagoSobreGanhosCapital = new Valor (this, "");
  
  public ColecaoBensImoveis getBensImoveis ()
  {
    return bensImoveis;
  }
  
  public ColecaoBensMoveis getBensMoveis ()
  {
    return bensMoveis;
  }
  
  public ColecaoPSocietarias getParticipacoesSocietarias ()
  {
    return participacoesSocietarias;
  }
  
  public Valor getTotalImpostoPagoSobreGanhosCapital ()
  {
    return totalImpostoPagoSobreGanhosCapital;
  }
}
