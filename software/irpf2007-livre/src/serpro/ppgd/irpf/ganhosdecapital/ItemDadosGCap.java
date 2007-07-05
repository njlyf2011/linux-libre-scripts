/* ItemDadosGCap - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.ganhosdecapital;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.CPF;
import serpro.ppgd.negocio.ObjetoNegocio;

public class ItemDadosGCap extends ObjetoNegocio
{
  private Alfa codItem = new Alfa (this, "", 5);
  private Alfa descricao = new Alfa (this, "", 60);
  private CPF cpf = new CPF (this, "");
  
  public Alfa getCodItem ()
  {
    return codItem;
  }
  
  public CPF getCpf ()
  {
    return cpf;
  }
  
  public Alfa getDescricao ()
  {
    return descricao;
  }
}
