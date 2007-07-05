/* RendTributacaoExclusiva - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendTributacaoExclusiva;
import serpro.ppgd.irpf.ColecaoItemQuadroAuxiliar;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class RendTributacaoExclusiva extends ObjetoNegocio
{
  private Valor decimoTerceiro = new Valor ();
  private Valor ganhosRendaVariavel = new Valor ();
  private Valor rendAplicacoes = new Valor ();
  private ColecaoItemQuadroAuxiliar rendAplicacoesQuadroAuxiliar = new ColecaoItemQuadroAuxiliar ();
  private Valor outros = new Valor ();
  private ColecaoItemQuadroAuxiliar outrosQuadroAuxiliar = new ColecaoItemQuadroAuxiliar ();
  private Valor decimoTerceiroDependentes = new Valor ();
  private Valor rendExcetoDecimoTerceiro = new Valor ();
  private ColecaoItemQuadroAuxiliar rendExcetoDecimoTerceiroQuadroAuxiliar = new ColecaoItemQuadroAuxiliar ();
  private Valor total = new Valor ();
  private Alfa descTotal = new Alfa ();
  
  public RendTributacaoExclusiva ()
  {
    getDecimoTerceiro ().setReadOnly (true);
    getGanhosRendaVariavel ().setReadOnly (true);
    getDecimoTerceiroDependentes ().setReadOnly (true);
    getTotal ().setReadOnly (true);
  }
  
  public void addObservador (Observador obs)
  {
    ganhosRendaVariavel.addObservador (obs);
    rendAplicacoes.addObservador (obs);
    outros.addObservador (obs);
    decimoTerceiroDependentes.addObservador (obs);
    rendExcetoDecimoTerceiro.addObservador (obs);
  }
  
  public ColecaoItemQuadroAuxiliar getOutrosQuadroAuxiliar ()
  {
    return outrosQuadroAuxiliar;
  }
  
  public ColecaoItemQuadroAuxiliar getRendAplicacoesQuadroAuxiliar ()
  {
    return rendAplicacoesQuadroAuxiliar;
  }
  
  public ColecaoItemQuadroAuxiliar getRendExcetoDecimoTerceiroQuadroAuxiliar ()
  {
    return rendExcetoDecimoTerceiroQuadroAuxiliar;
  }
  
  public Valor getTotal ()
  {
    return total;
  }
  
  public void setTotal (Valor total)
  {
    this.total = total;
  }
  
  public Valor getDecimoTerceiro ()
  {
    return decimoTerceiro;
  }
  
  public void setDecimoTerceiro (Valor decimoTerceiro)
  {
    this.decimoTerceiro = decimoTerceiro;
  }
  
  public Valor getDecimoTerceiroDependentes ()
  {
    return decimoTerceiroDependentes;
  }
  
  public void setDecimoTerceiroDependentes (Valor decimoTerceiroDependentes)
  {
    this.decimoTerceiroDependentes = decimoTerceiroDependentes;
  }
  
  public Valor getGanhosRendaVariavel ()
  {
    return ganhosRendaVariavel;
  }
  
  public void setGanhosRendaVariavel (Valor ganhosRendaVariavel)
  {
    this.ganhosRendaVariavel = ganhosRendaVariavel;
  }
  
  public Valor getOutros ()
  {
    return outros;
  }
  
  public void setOutros (Valor outros)
  {
    this.outros = outros;
  }
  
  public Valor getRendAplicacoes ()
  {
    return rendAplicacoes;
  }
  
  public void setRendAplicacoes (Valor rendAplicacoes)
  {
    this.rendAplicacoes = rendAplicacoes;
  }
  
  public Valor getRendExcetoDecimoTerceiro ()
  {
    return rendExcetoDecimoTerceiro;
  }
  
  public void setRendExcetoDecimoTerceiro (Valor rendExcetoDecimoTerceiro)
  {
    this.rendExcetoDecimoTerceiro = rendExcetoDecimoTerceiro;
  }
  
  public Alfa getDescTotal ()
  {
    return descTotal;
  }
  
  public void setDescTotal (Alfa descTotal)
  {
    this.descTotal = descTotal;
  }
  
  public Valor recuperarExclusivosTitular ()
  {
    Valor result = new Valor ();
    result.append ('+', decimoTerceiro);
    result.append ('+', rendAplicacoes);
    result.append ('+', outros);
    result.append ('+', ganhosRendaVariavel);
    return result;
  }
  
  public Valor recuperarExclusivosDependentes ()
  {
    return decimoTerceiroDependentes.operacao ('+', rendExcetoDecimoTerceiro);
  }
  
  public Valor recuperarTotalTitularExceto13_RV_e_GC ()
  {
    return getRendAplicacoes ().operacao ('+', getOutros ());
  }
}
