/* ARBrasil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural.brasil;
import java.util.List;

import serpro.ppgd.irpf.atividaderural.MovimentacaoRebanho;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Pendencia;

public class ARBrasil extends ObjetoNegocio
{
  private IdentificacaoImovelARBrasil identificacaoImovel = new IdentificacaoImovelARBrasil ();
  private ReceitasDespesas receitasDespesas = new ReceitasDespesas ();
  private ApuracaoResultadoBrasil apuracaoResultado = new ApuracaoResultadoBrasil ();
  private MovimentacaoRebanho movimentacaoRebanho = new MovimentacaoRebanho ();
  private ColecaoBensARBrasil bens = new ColecaoBensARBrasil ();
  private ColecaoDividasARBrasil dividas = new ColecaoDividasARBrasil ();
  
  public ARBrasil ()
  {
    getMovimentacaoRebanho ().setFicha ("Movimenta\u00e7\u00e3o do Rebanho - BRASIL");
  }
  
  public ApuracaoResultadoBrasil getApuracaoResultado ()
  {
    return apuracaoResultado;
  }
  
  public ColecaoBensARBrasil getBens ()
  {
    return bens;
  }
  
  public ColecaoDividasARBrasil getDividas ()
  {
    return dividas;
  }
  
  public IdentificacaoImovelARBrasil getIdentificacaoImovel ()
  {
    return identificacaoImovel;
  }
  
  public MovimentacaoRebanho getMovimentacaoRebanho ()
  {
    return movimentacaoRebanho;
  }
  
  public ReceitasDespesas getReceitasDespesas ()
  {
    return receitasDespesas;
  }
  
  public List verificarPendencias (int numeroItem)
  {
    List retorno = super.verificarPendencias (numeroItem);
    boolean temAlgumaOutraFichaPreenchida = ! getReceitasDespesas ().isVazio () || ! getApuracaoResultado ().isVazio () || ! getMovimentacaoRebanho ().isVazio () || ! getBens ().isVazio () || ! getDividas ().isVazio ();
    if (getIdentificacaoImovel ().isVazio () && temAlgumaOutraFichaPreenchida)
      {
	getIdentificacaoImovel ().novoObjeto ();
	ImovelARBrasil imovel = (ImovelARBrasil) getIdentificacaoImovel ().recuperarLista ().get (0);
	Pendencia p = new Pendencia ((byte) 3, imovel.getCodigo (), imovel.getCodigo ().getNomeCampo (), tab.msg ("ficha_imovel_ar_nao_preenchida"), numeroItem);
	retorno.add (p);
      }
    return retorno;
  }
}
