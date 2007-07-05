/* ColecaoRendPJTitular - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendpj;
import java.util.List;

import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.NI;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;

public class ColecaoRendPJTitular extends Colecao
{
  public static final String NOME_TOTAIS_RENDPJ = "Totais Rend. Recebid";
  protected transient IdentificadorDeclaracao identificadorDeclaracao;
  protected NI niMaiorFontePagadora;
  protected Valor totaisRendRecebidoPJ;
  protected Valor totaisContribuicaoPrevOficial;
  protected Valor totaisImpostoRetidoFonte;
  protected Valor totaisDecimoTerceiro;
  
  public ColecaoRendPJTitular (String classeItem, IdentificadorDeclaracao id)
  {
    super (classeItem);
    identificadorDeclaracao = null;
    niMaiorFontePagadora = new NI (this, "NI");
    totaisRendRecebidoPJ = new Valor (this, "Totais Rend. Recebid");
    totaisContribuicaoPrevOficial = new Valor (this, "Totais Contr. Prev. Oficial");
    totaisImpostoRetidoFonte = new Valor (this, "Totais IR Retido na Fonte");
    totaisDecimoTerceiro = new Valor (this, "Totais 13\u00ba Sal\u00e1rio");
    identificadorDeclaracao = id;
    totaisRendRecebidoPJ.setReadOnly (true);
    totaisContribuicaoPrevOficial.setReadOnly (true);
    totaisImpostoRetidoFonte.setReadOnly (true);
    totaisDecimoTerceiro.setReadOnly (true);
    niMaiorFontePagadora.setReadOnly (true);
  }
  
  public ColecaoRendPJTitular (IdentificadorDeclaracao id)
  {
    this (serpro.ppgd.irpf.rendpj.RendPJTitular.class.getName (), id);
    setFicha ("Rendimentos Tribut\u00e1veis Recebidos de PJ pelo Titular");
  }
  
  public void objetoInserido (Object o)
  {
    ObjetoNegocio obj = (ObjetoNegocio) o;
    obj.setFicha (getFicha ());
  }
  
  public ObjetoNegocio instanciaNovoObjeto ()
  {
    return new RendPJTitular (identificadorDeclaracao);
  }
  
  public Valor getTotaisContribuicaoPrevOficial ()
  {
    return totaisContribuicaoPrevOficial;
  }
  
  public Valor getTotaisDecimoTerceiro ()
  {
    return totaisDecimoTerceiro;
  }
  
  public Valor getTotaisImpostoRetidoFonte ()
  {
    return totaisImpostoRetidoFonte;
  }
  
  public Valor getTotaisRendRecebidoPJ ()
  {
    return totaisRendRecebidoPJ;
  }
  
  public NI getNiMaiorFontePagadora ()
  {
    return niMaiorFontePagadora;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List listaCamposPendencia = super.recuperarListaCamposPendencia ();
    listaCamposPendencia.add (totaisRendRecebidoPJ);
    return listaCamposPendencia;
  }
}
