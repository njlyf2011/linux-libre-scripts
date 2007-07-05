/* ItemDadosBemImovel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.ganhosdecapital.bensimoveis;
import serpro.ppgd.irpf.ganhosdecapital.ItemDadosGCap;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.CEP;
import serpro.ppgd.negocio.Data;
import serpro.ppgd.negocio.Logico;
import serpro.ppgd.negocio.NI;
import serpro.ppgd.negocio.Valor;

public class ItemDadosBemImovel extends ItemDadosGCap
{
  private Alfa nomeAdquirente = new Alfa (this, "", 50);
  private NI cpfCnpj = new NI (this, "");
  private Alfa especificacao = new Alfa (this, "", 70);
  private Alfa ruaImovel = new Alfa (this, "", 40);
  private Alfa numero = new Alfa (this, "", 15);
  private Alfa complemento = new Alfa (this, "", 10);
  private Alfa bairro = new Alfa (this, "", 20);
  private CEP cep = new CEP (this, "");
  private Alfa municipio = new Alfa (this, "", 30);
  private Alfa uf = new Alfa (this, "", 3);
  private Alfa descricaoOperacao = new Alfa (this, "", 30);
  private Data dataAquisicao = new Data (this, "");
  private Data dataAlienacao = new Data (this, "");
  private Valor valorAlienacao = new Valor (this, "");
  private Alfa especificacaoImovel = new Alfa (this, "", 70);
  private Logico alienacaoPrazo = new Logico (this, "");
  private Logico houveReforma = new Logico (this, "");
  private Valor custoAquisicao = new Valor (this, "");
  private Valor gcCapResultadoI = new Valor (this, "");
  private Valor valorPassivelReducaoResultadoI = new Valor (this, "");
  private Valor percentualReducao = new Valor (this, "");
  private Valor valorReducao = new Valor (this, "");
  
  public ItemDadosBemImovel ()
  {
    alienacaoPrazo.converteEmTipoSimNao (Logico.NAO);
    houveReforma.converteEmTipoSimNao (Logico.NAO);
  }
  
  public Valor getCustoAquisicao ()
  {
    return custoAquisicao;
  }
  
  public Valor getGcCapResultadoI ()
  {
    return gcCapResultadoI;
  }
  
  public Valor getPercentualReducao ()
  {
    return percentualReducao;
  }
  
  public Valor getValorPassivelReducaoResultadoI ()
  {
    return valorPassivelReducaoResultadoI;
  }
  
  public Valor getValorReducao ()
  {
    return valorReducao;
  }
  
  public Logico getAlienacaoPrazo ()
  {
    return alienacaoPrazo;
  }
  
  public Alfa getBairro ()
  {
    return bairro;
  }
  
  public CEP getCep ()
  {
    return cep;
  }
  
  public Alfa getComplemento ()
  {
    return complemento;
  }
  
  public NI getCpfCnpj ()
  {
    return cpfCnpj;
  }
  
  public Data getDataAlienacao ()
  {
    return dataAlienacao;
  }
  
  public Data getDataAquisicao ()
  {
    return dataAquisicao;
  }
  
  public Alfa getDescricaoOperacao ()
  {
    return descricaoOperacao;
  }
  
  public Alfa getEspecificacao ()
  {
    return especificacao;
  }
  
  public Alfa getEspecificacaoImovel ()
  {
    return especificacaoImovel;
  }
  
  public Logico getHouveReforma ()
  {
    return houveReforma;
  }
  
  public Alfa getMunicipio ()
  {
    return municipio;
  }
  
  public Alfa getNomeAdquirente ()
  {
    return nomeAdquirente;
  }
  
  public Alfa getNumero ()
  {
    return numero;
  }
  
  public Alfa getRuaImovel ()
  {
    return ruaImovel;
  }
  
  public Alfa getUf ()
  {
    return uf;
  }
  
  public Valor getValorAlienacao ()
  {
    return valorAlienacao;
  }
}
