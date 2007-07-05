/* Conjuge - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.conjuge;
import java.util.List;

import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.CPF;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorCPF;

public class Conjuge extends ObjetoNegocio
{
  protected transient IdentificadorDeclaracao identificadorDeclaracao = null;
  public static final String CONJUGE_ = "Informa\u00e7\u00f5es do c\u00f4njuge";
  public static final String CONJUGE_BENS_COMUNS = "Os bens comuns do casal est\u00e3o informados nesta declara\u00e7\u00e3o?";
  public static final String CONJUGE_APRESENTOU_SIMPLES = "O c\u00f4njuge apresentou declara\u00e7\u00e3o de ajuste anual simplificada?";
  public static final String CONJUGE_CPF = "CPF";
  public static final String CONJUGE_BASECALCULO = "Base de c\u00e1lculo";
  public static final String CONJUGE_SIMPLES_IMPOSTORETIDOFONTE = "Imposto retido na fonte";
  public static final String CONJUGE_RENDTRIBUTACAOEXCLUSIVA = "Rendimentos sujeitos \u00e0 tributa\u00e7\u00e3o exclusiva";
  public static final String CONJUGE_COMPLETA_IMPOSTORETIDOFONTE = "Total do imposto pago";
  public static final String CONJUGE_IMPOSTORETIDOFONTE = "Imposto retido na fonte";
  public static final String CONJUGE_CARNELEAO = "Carn\u00ea-Le\u00e3o e imposto complementar";
  public static final String CONJUGE_RENDISENTOSNAOTRIBUTAVEIS = "Rendimentos isentos e n\u00e3o-tribut\u00e1veis";
  public static final String CONJUGE_RESULTADO = "Resultado";
  public static final String CONJUGE_RESULTADO_INFO = "Informa\u00e7\u00f5es do c\u00f4njuge";
  private Valor valAntigoImpostoRetidoFonte = new Valor (this, "");
  private Valor valAntigoCarneLeao = new Valor (this, "");
  private CPF cpfConjuge = new CPF (this, "CPF");
  private Alfa decSimplificada = new Alfa (this, "O c\u00f4njuge apresentou declara\u00e7\u00e3o de ajuste anual simplificada?", 1);
  private Valor baseCalculoImposto = new Valor (this, "Base de c\u00e1lculo");
  private Valor impRetidoFonte = new Valor (this, "Imposto retido na fonte");
  private Valor carneComImpComplementar = new Valor (this, "Carn\u00ea-Le\u00e3o e imposto complementar");
  private Valor rendIsentoNaoTributaveis = new Valor (this, "Rendimentos isentos e n\u00e3o-tribut\u00e1veis");
  private Valor rendSujeitosTribExcl = new Valor (this, "Rendimentos sujeitos \u00e0 tributa\u00e7\u00e3o exclusiva");
  private Valor resultado = new Valor (this, "Resultado");
  
  public Conjuge (IdentificadorDeclaracao id)
  {
    identificadorDeclaracao = id;
    setFicha ("Informa\u00e7\u00f5es do C\u00f4njuge");
    valAntigoCarneLeao.setAtributoPersistente (false);
    valAntigoImpostoRetidoFonte.setAtributoPersistente (false);
    decSimplificada.setHabilitado (false);
    baseCalculoImposto.setHabilitado (false);
    impRetidoFonte.setHabilitado (false);
    carneComImpComplementar.setReadOnly (true);
    rendIsentoNaoTributaveis.setHabilitado (false);
    rendSujeitosTribExcl.setHabilitado (false);
    resultado.setReadOnly (true);
    ValidadorCPF validadorCPF = new ValidadorCPF ((byte) 3);
    validadorCPF.setMensagemValidacao (tab.msg ("conjuge_cpf_invalido"));
    getCpfConjuge ().addValidador (validadorCPF);
    getCpfConjuge ().addValidador (new ValidadorDefault ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (getCpfConjuge ().asString ().equals (identificadorDeclaracao.getCpf ().asString ()))
	  return new RetornoValidacao (tab.msg ("conjuge_cpf"), (byte) 3);
	return null;
      }
    });
    getDecSimplificada ().addObservador (new Observador ()
    {
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	if (getDecSimplificada ().asString ().equals ("0") || getDecSimplificada ().isVazio ())
	  getCarneComImpComplementar ().setReadOnly (true);
	else
	  getCarneComImpComplementar ().setReadOnly (false);
      }
    });
  }
  
  public Valor getValAntigoCarneLeao ()
  {
    return valAntigoCarneLeao;
  }
  
  public Valor getValAntigoImpostoRetidoFonte ()
  {
    return valAntigoImpostoRetidoFonte;
  }
  
  public CPF getCpfConjuge ()
  {
    return cpfConjuge;
  }
  
  public Alfa getDecSimplificada ()
  {
    return decSimplificada;
  }
  
  public Valor getBaseCalculoImposto ()
  {
    return baseCalculoImposto;
  }
  
  public Valor getImpRetidoFonte ()
  {
    return impRetidoFonte;
  }
  
  public Valor getCarneComImpComplementar ()
  {
    return carneComImpComplementar;
  }
  
  public Valor getRendIsentoNaoTributaveis ()
  {
    return rendIsentoNaoTributaveis;
  }
  
  public Valor getRendSujeitosTribExcl ()
  {
    return rendSujeitosTribExcl;
  }
  
  public Valor getResultado ()
  {
    return resultado;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List retorno = super.recuperarListaCamposPendencia ();
    retorno.add (getCpfConjuge ());
    return retorno;
  }
}
