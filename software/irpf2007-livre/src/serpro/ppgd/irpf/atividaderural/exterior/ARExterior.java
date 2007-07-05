/* ARExterior - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural.exterior;
import java.util.List;

import serpro.ppgd.irpf.atividaderural.ImovelAR;
import serpro.ppgd.irpf.atividaderural.MovimentacaoRebanho;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Pendencia;

public class ARExterior extends ObjetoNegocio
{
  private IdentificacaoImovelARExterior identificacaoImovel = new IdentificacaoImovelARExterior ();
  private ColecaoReceitasDespesas receitasDespesas = new ColecaoReceitasDespesas ();
  private ApuracaoResultadoExterior apuracaoResultado = new ApuracaoResultadoExterior ();
  private MovimentacaoRebanho movimentacaoRebanho = new MovimentacaoRebanho ();
  private ColecaoBensARExterior bens = new ColecaoBensARExterior ();
  private ColecaoDividasARExterior dividas = new ColecaoDividasARExterior ();
  
  public ARExterior ()
  {
    getMovimentacaoRebanho ().setFicha ("Movimenta\u00e7\u00e3o do Rebanho - EXTERIOR");
  }
  
  public ApuracaoResultadoExterior getApuracaoResultado ()
  {
    return apuracaoResultado;
  }
  
  public ColecaoBensARExterior getBens ()
  {
    return bens;
  }
  
  public ColecaoDividasARExterior getDividas ()
  {
    return dividas;
  }
  
  public IdentificacaoImovelARExterior getIdentificacaoImovel ()
  {
    return identificacaoImovel;
  }
  
  public MovimentacaoRebanho getMovimentacaoRebanho ()
  {
    return movimentacaoRebanho;
  }
  
  public ColecaoReceitasDespesas getReceitasDespesas ()
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
	ImovelAR imovel = (ImovelAR) getIdentificacaoImovel ().recuperarLista ().get (0);
	Pendencia p = new Pendencia ((byte) 3, imovel.getCodigo (), imovel.getCodigo ().getNomeCampo (), tab.msg ("ficha_imovel_ar_nao_preenchida"), numeroItem);
	retorno.add (p);
      }
    return retorno;
  }
}
