/* Pagamento - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.pagamentos;
import java.util.Iterator;
import java.util.List;

import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.tabelas.CadastroTabelasIRPF;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.NI;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.util.UtilitariosString;
import serpro.ppgd.negocio.util.Validador;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNIT;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNaoNulo;

public class Pagamento extends ObjetoNegocio
{
  public static final String PAGAMENTO_PROPRIAINSTRUCAO_BR = "01";
  public static final String PAGAMENTO_PROPRIAINSTRUCAO_EX = "02";
  public static final String PAGAMENTO_INSTRUCAODEPEND_BR = "03";
  public static final String PAGAMENTO_INSTRUCAODEPEND_EX = "04";
  public static final String PAGAMENTO_INSTRUCAOALIMENT_BR = "05";
  public static final String PAGAMENTO_INSTRUCAOALIMENT_EX = "06";
  public static final String PAGAMENTO_MEDICO_BR = "07";
  public static final String PAGAMENTO_MEDICO_EX = "08";
  public static final String PAGAMENTO_HOSPITAL_BR = "09";
  public static final String PAGAMENTO_HOSPITAL_EX = "10";
  public static final String PAGAMENTO_PLANOSAUDE_BR = "11";
  public static final String PAGAMENTO_PENSAOALIMENTICIA = "12";
  public static final String PAGAMENTO_PREVIPRIVADA = "13";
  public static final String PAGAMENTO_FAPI = "14";
  public static final String PAGAMENTO_ESTATCRIANCA = "15";
  public static final String PAGAMENTO_CULTURA = "16";
  public static final String PAGAMENTO_AUDIOVISUAIS = "17";
  public static final String PAGAMENTO_CONTRIBUICAO_PATRONAL = "18";
  public static final String PAGAMENTO_ADVOGADOS = "19";
  public static final String PAGAMENTO_ADVOGADOS_TRAB = "20";
  public static final String PAGAMENTO_ADVOGADOS_HONORARIOS = "21";
  public static final String PAGAMENTO_PROFLIBERAL = "22";
  public static final String PAGAMENTO_ALUGUEIS = "23";
  public static final String PAGAMENTO_ARRENDRURAL = "24";
  public static final String PAGAMENTO_OUTROS = "25";
  public static final String PERC_LIMITE_DEDUCAO_CONTRIBUICAO_PREV_PRIV_FAPI = "0,12";
  public static final String PERC_LIMITE_DEDUCAO_INCENTIVO = "0,06";
  public static final String LIMITE_DESPESAS_INSTRUCAO = "2.373,84";
  public static final String NOME_CODIGO = "C\u00f3digo";
  private Codigo codigo = new Codigo (this, "C\u00f3digo", CadastroTabelasIRPF.recuperarTipoPagamentos ());
  private Alfa dependenteOuAlimentando = new Alfa (this, "Dependente/Alimentando", 20);
  private Alfa nomeBeneficiario = new Alfa (this, "Nome do Benefici\u00e1rio", 60);
  private NI niBeneficiario = new NI (this, "CPF ou CNPJ do Benefici\u00e1rio");
  private Valor valorPago = new Valor (this, "Valor Pago");
  private Valor parcelaNaoDedutivel = new Valor (this, "Parcela N\u00e3o Dedut\u00edvel/Valor Reembolsado");
  private Alfa nitEmpregadoDomestico = new Alfa (this, "NIT do empregado dom\u00e9stico");
  protected transient IdentificadorDeclaracao identificadorDeclaracao = null;
  
  public Pagamento (IdentificadorDeclaracao id)
  {
    identificadorDeclaracao = id;
    getCodigo ().setColunaFiltro (1);
    nitEmpregadoDomestico.setHabilitado (false);
    getNitEmpregadoDomestico ().addValidador (new ValidadorNIT ((byte) 3, tab.msg ("pagamento_NIT"))
    {
      public RetornoValidacao validarImplementado ()
      {
	if (getNitEmpregadoDomestico ().isHabilitado ())
	  return super.validarImplementado ();
	return null;
      }
    });
    getCodigo ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("pagamento_codigo"))
    {
      public RetornoValidacao validarImplementado ()
      {
	if (identificadorDeclaracao.getTipoDeclaracao ().asString ().equals ("1"))
	  return null;
	return super.validarImplementado ();
      }
    });
    getNomeBeneficiario ().addValidador (new ValidadorNaoNulo ((byte) 3, "")
    {
      public RetornoValidacao validarImplementado ()
      {
	if (identificadorDeclaracao.getTipoDeclaracao ().asString ().equals ("1"))
	  return null;
	getNiBeneficiario ().validar ();
	if (getNomeBeneficiario ().isVazio () && getNiBeneficiario ().isValido ())
	  {
	    setSeveridade ((byte) 2);
	    return new RetornoValidacao (getCodigo ().asString ().equals ("18") ? tab.msg ("pagamento_empregado") : tab.msg ("pagamento_beneficiario"), (byte) 2);
	  }
	if (getNomeBeneficiario ().isVazio () && ! getNiBeneficiario ().isValido ())
	  {
	    setSeveridade ((byte) 3);
	    return new RetornoValidacao (getCodigo ().asString ().equals ("18") ? tab.msg ("pagamento_empregado_ni_invalido") : tab.msg ("pagamento_beneficiario_ni_invalido"), (byte) 3);
	  }
	return null;
      }
    });
    getNiBeneficiario ().addValidador (new ValidadorNaoNulo ((byte) 2)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (identificadorDeclaracao.getTipoDeclaracao ().asString ().equals ("1"))
	  return null;
	String codigoPagamento = getCodigo ().getConteudoAtual (0);
	String codMensagem = null;
	if (codigoPagamento.equals ("01") || codigoPagamento.equals ("03") || codigoPagamento.equals ("05") || codigoPagamento.equals ("07") || codigoPagamento.equals ("09") || codigoPagamento.equals ("11"))
	  codMensagem = "pagamento_nibeneficiario_1";
	else if (codigoPagamento.equals ("12") || codigoPagamento.equals ("13") || codigoPagamento.equals ("14") || codigoPagamento.equals ("15") || codigoPagamento.equals ("16") || codigoPagamento.equals ("17") || codigoPagamento.equals ("19") || codigoPagamento.equals ("20") || codigoPagamento.equals ("21") || codigoPagamento.equals ("22") || codigoPagamento.equals ("23") || codigoPagamento.equals ("24"))
	  codMensagem = "pagamento_nibeneficiario_2";
	RetornoValidacao retornoValidacao = Validador.validarNI (UtilitariosString.retiraMascara (getInformacao ().getConteudoFormatado ()));
	if ((getNiBeneficiario ().isVazio () || retornoValidacao != null) && codMensagem != null)
	  return new RetornoValidacao (tab.msg (codMensagem), (byte) 2);
	return null;
      }
    });
    getParcelaNaoDedutivel ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (identificadorDeclaracao.getTipoDeclaracao ().asString ().equals ("1"))
	  return null;
	String codigoPagamento = getCodigo ().getConteudoAtual (0);
	if ((codigoPagamento.equals ("01") || codigoPagamento.equals ("02") || codigoPagamento.equals ("03") || codigoPagamento.equals ("04") || codigoPagamento.equals ("05") || codigoPagamento.equals ("06") || codigoPagamento.equals ("07") || codigoPagamento.equals ("08") || codigoPagamento.equals ("09") || codigoPagamento.equals ("10") || codigoPagamento.equals ("11") || codigoPagamento.equals ("12") || codigoPagamento.equals ("16")) && getParcelaNaoDedutivel ().comparacao (">", getValorPago ()))
	  return new RetornoValidacao (tab.msg ("pagamento_valor_reembolsado"), (byte) 3);
	return null;
      }
    });
    getValorPago ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (identificadorDeclaracao.getTipoDeclaracao ().asString ().equals ("1"))
	  return null;
	if (getCodigo ().isVazio () && getNomeBeneficiario ().isVazio () && getNiBeneficiario ().isVazio () && getParcelaNaoDedutivel ().isVazio ())
	  return null;
	setMensagemValidacao (getCodigo ().asString ().equals ("18") ? tab.msg ("pagamento_empregado_valor_pago") : tab.msg ("pagamento_valor_pago"));
	return super.validarImplementado ();
      }
    });
    getDependenteOuAlimentando ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("pagamento_dependente_alimentando"))
    {
      public RetornoValidacao validarImplementado ()
      {
	if (identificadorDeclaracao.getTipoDeclaracao ().asString ().equals ("1"))
	  return null;
	String codigoPagamento = getCodigo ().getConteudoAtual (0);
	if (! codigoPagamento.equals ("03") && ! codigoPagamento.equals ("04") && ! codigoPagamento.equals ("05") && ! codigoPagamento.equals ("06"))
	  return null;
	return super.validarImplementado ();
      }
    });
    getNitEmpregadoDomestico ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	return null;
      }
    });
    getNiBeneficiario ().addValidador (new ValidadorDefault ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (getNiBeneficiario ().asString ().equals (identificadorDeclaracao.getCpf ().asString ()))
	  return new RetornoValidacao (tab.msg ("pagamento_cpf_beneficiario_igual"), (byte) 3);
	return null;
      }
    });
  }
  
  public Codigo getCodigo ()
  {
    return codigo;
  }
  
  public NI getNiBeneficiario ()
  {
    return niBeneficiario;
  }
  
  public Alfa getNomeBeneficiario ()
  {
    return nomeBeneficiario;
  }
  
  public Valor getParcelaNaoDedutivel ()
  {
    return parcelaNaoDedutivel;
  }
  
  public Valor getValorPago ()
  {
    return valorPago;
  }
  
  public Alfa getDependenteOuAlimentando ()
  {
    return dependenteOuAlimentando;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List retorno = super.recuperarListaCamposPendencia ();
    retorno.add (codigo);
    retorno.add (dependenteOuAlimentando);
    retorno.add (nomeBeneficiario);
    retorno.add (niBeneficiario);
    retorno.add (nitEmpregadoDomestico);
    retorno.add (valorPago);
    retorno.add (parcelaNaoDedutivel);
    return retorno;
  }
  
  public void setNitEmpregadoDomestico (Alfa nitEmpregadoDomestico)
  {
    this.nitEmpregadoDomestico = nitEmpregadoDomestico;
  }
  
  public Alfa getNitEmpregadoDomestico ()
  {
    return nitEmpregadoDomestico;
  }
  
  public boolean isVazio ()
  {
    Iterator iterator = recuperarCamposInformacao ().iterator ();
    while (iterator.hasNext ())
      {
	Informacao informacao = (Informacao) iterator.next ();
	if (! informacao.isVazio ())
	  return false;
      }
    return true;
  }
}
