/* RendimentosTributaveisDeducoes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.resumo;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;

public class RendimentosTributaveisDeducoes extends ObjetoNegocio
{
  private Valor rendRecebidoPJTitular = new Valor (this, "");
  private Valor rendRecebidoPJDependentes = new Valor (this, "");
  private Valor rendRecebidoPFTitular = new Valor (this, "");
  private Valor rendRecebidoPFDependentes = new Valor (this, "");
  private Valor rendRecebidoExterior = new Valor (this, "");
  private Valor rendTributavelAR = new Valor (this, "");
  private Valor totalRendimentos = new Valor (this, "");
  private Valor previdenciaOficial = new Valor (this, "");
  private Valor previdenciaFAPI = new Valor (this, "");
  private Valor dependentes = new Valor (this, "");
  private Valor despesasInstrucao = new Valor (this, "");
  private Valor despesasMedicas = new Valor (this, "");
  private Valor pensaoAlimenticia = new Valor (this, "");
  private Valor livroCaixa = new Valor (this, "");
  private Valor totalDeducoes = new Valor (this, "");
  
  public RendimentosTributaveisDeducoes ()
  {
    rendRecebidoPJTitular.setReadOnly (true);
    rendRecebidoPJDependentes.setReadOnly (true);
    rendRecebidoPFTitular.setReadOnly (true);
    rendRecebidoPFDependentes.setReadOnly (true);
    rendRecebidoExterior.setReadOnly (true);
    rendTributavelAR.setReadOnly (true);
    totalRendimentos.setReadOnly (true);
    previdenciaOficial.setReadOnly (true);
    previdenciaFAPI.setReadOnly (true);
    dependentes.setReadOnly (true);
    despesasInstrucao.setReadOnly (true);
    despesasMedicas.setReadOnly (true);
    pensaoAlimenticia.setReadOnly (true);
    livroCaixa.setReadOnly (true);
    totalDeducoes.setReadOnly (true);
  }
  
  public Valor getDependentes ()
  {
    return dependentes;
  }
  
  public Valor getDespesasInstrucao ()
  {
    return despesasInstrucao;
  }
  
  public Valor getDespesasMedicas ()
  {
    return despesasMedicas;
  }
  
  public Valor getLivroCaixa ()
  {
    return livroCaixa;
  }
  
  public Valor getPensaoAlimenticia ()
  {
    return pensaoAlimenticia;
  }
  
  public Valor getPrevidenciaFAPI ()
  {
    return previdenciaFAPI;
  }
  
  public Valor getPrevidenciaOficial ()
  {
    return previdenciaOficial;
  }
  
  public Valor getRendRecebidoExterior ()
  {
    return rendRecebidoExterior;
  }
  
  public Valor getRendRecebidoPFDependentes ()
  {
    return rendRecebidoPFDependentes;
  }
  
  public Valor getRendRecebidoPFTitular ()
  {
    return rendRecebidoPFTitular;
  }
  
  public Valor getRendRecebidoPJDependentes ()
  {
    return rendRecebidoPJDependentes;
  }
  
  public Valor getRendRecebidoPJTitular ()
  {
    return rendRecebidoPJTitular;
  }
  
  public Valor getRendTributavelAR ()
  {
    return rendTributavelAR;
  }
  
  public Valor getTotalDeducoes ()
  {
    return totalDeducoes;
  }
  
  public Valor getTotalRendimentos ()
  {
    return totalRendimentos;
  }
}
