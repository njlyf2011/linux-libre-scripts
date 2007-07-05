/* ConversorObjetosIRPF2Registros - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.txt.gravacaorestauracao;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import serpro.ppgd.formatosexternos.txt.RegistroTxt;
import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.irpf.ColecaoItemQuadroLucrosDividendos;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.ItemQuadroLucrosDividendos;
import serpro.ppgd.irpf.alimentandos.Alimentando;
import serpro.ppgd.irpf.alimentandos.Alimentandos;
import serpro.ppgd.irpf.atividaderural.AtividadeRural;
import serpro.ppgd.irpf.atividaderural.BemAR;
import serpro.ppgd.irpf.atividaderural.DividaAR;
import serpro.ppgd.irpf.atividaderural.ImovelAR;
import serpro.ppgd.irpf.atividaderural.ItemMovimentacaoRebanho;
import serpro.ppgd.irpf.atividaderural.MovimentacaoRebanho;
import serpro.ppgd.irpf.atividaderural.brasil.ARBrasil;
import serpro.ppgd.irpf.atividaderural.brasil.ApuracaoResultadoBrasil;
import serpro.ppgd.irpf.atividaderural.brasil.ImovelARBrasil;
import serpro.ppgd.irpf.atividaderural.brasil.MesReceitaDespesa;
import serpro.ppgd.irpf.atividaderural.brasil.ReceitasDespesas;
import serpro.ppgd.irpf.atividaderural.exterior.ARExterior;
import serpro.ppgd.irpf.atividaderural.exterior.ApuracaoResultadoExterior;
import serpro.ppgd.irpf.atividaderural.exterior.BemARExterior;
import serpro.ppgd.irpf.atividaderural.exterior.ReceitaDespesa;
import serpro.ppgd.irpf.bens.Bem;
import serpro.ppgd.irpf.bens.Bens;
import serpro.ppgd.irpf.calculos.CalculosPagamentos;
import serpro.ppgd.irpf.conjuge.Conjuge;
import serpro.ppgd.irpf.contribuinte.Contribuinte;
import serpro.ppgd.irpf.dependentes.Dependente;
import serpro.ppgd.irpf.dependentes.Dependentes;
import serpro.ppgd.irpf.dividas.Divida;
import serpro.ppgd.irpf.dividas.Dividas;
import serpro.ppgd.irpf.eleicoes.Doacao;
import serpro.ppgd.irpf.eleicoes.Doacoes;
import serpro.ppgd.irpf.espolio.Espolio;
import serpro.ppgd.irpf.pagamentos.Pagamento;
import serpro.ppgd.irpf.pagamentos.Pagamentos;
import serpro.ppgd.irpf.rendIsentos.RendIsentos;
import serpro.ppgd.irpf.rendTributacaoExclusiva.RendTributacaoExclusiva;
import serpro.ppgd.irpf.rendavariavel.FundosInvestimentos;
import serpro.ppgd.irpf.rendavariavel.GanhosLiquidosOuPerdas;
import serpro.ppgd.irpf.rendavariavel.MesFundosInvestimentos;
import serpro.ppgd.irpf.rendavariavel.Operacoes;
import serpro.ppgd.irpf.rendavariavel.RendaVariavel;
import serpro.ppgd.irpf.rendpf.CPFDependente;
import serpro.ppgd.irpf.rendpf.ColecaoCPFDependentes;
import serpro.ppgd.irpf.rendpf.MesRendPF;
import serpro.ppgd.irpf.rendpf.RendPF;
import serpro.ppgd.irpf.rendpj.ColecaoRendPJDependente;
import serpro.ppgd.irpf.rendpj.ColecaoRendPJTitular;
import serpro.ppgd.irpf.rendpj.RendPJDependente;
import serpro.ppgd.irpf.rendpj.RendPJTitular;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.Logico;
import serpro.ppgd.negocio.Valor;

public class ConversorObjetosIRPF2Registros
{
  private static final Logger logger;
  
  static
  {
    Class var_class = serpro.ppgd.irpf.txt.gravacaorestauracao.ConversorObjetosIRPF2Registros.class;
    logger = Logger.getLogger (var_class.getName ());
  }
  
  public Vector montarRegistroHeader (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "IR");
    objRegTXT.fieldByName ("SISTEMA").set ("IRPF");
    objRegTXT.fieldByName ("EXERCICIO").set (ConstantesGlobais.EXERCICIO);
    objRegTXT.fieldByName ("ANO_BASE").set (ConstantesGlobais.ANO_BASE);
    objRegTXT.fieldByName ("CODIGO_RECNET").set (1700);
    if (objDecl.getIdentificadorDeclaracao ().isRetificadora ())
      {
	objRegTXT.fieldByName ("IN_RETIFICADORA").set (1);
	String numeroRecibo = objDecl.getIdentificadorDeclaracao ().getNumReciboDecRetif ().asString ();
	if (numeroRecibo.length () >= 10)
	  objRegTXT.fieldByName ("NR_RECIBO_ULTIMA_DEC_EX_2006").set (numeroRecibo.substring (0, 10));
	else if (numeroRecibo.length () > 0)
	  objRegTXT.fieldByName ("NR_RECIBO_ULTIMA_DEC_EX_2006").set (numeroRecibo.substring (0, 9));
      }
    else
      objRegTXT.fieldByName ("IN_RETIFICADORA").set (0);
    String numeroDeclaracaoAnoAnterior = objDecl.getContribuinte ().getNumeroReciboDecAnterior ().asString ();
    if (numeroDeclaracaoAnoAnterior.length () >= 10)
      objRegTXT.fieldByName ("NR_RECIBO_ULTIMA_DEC_EX_ANTERIOR").set (numeroDeclaracaoAnoAnterior.substring (0, 10));
    else if (numeroDeclaracaoAnoAnterior.length () > 0)
      objRegTXT.fieldByName ("NR_RECIBO_ULTIMA_DEC_EX_ANTERIOR").set (numeroDeclaracaoAnoAnterior.substring (0, 9));
    objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
    objRegTXT.fieldByName ("TIPO_NI").set (1);
    objRegTXT.fieldByName ("NR_VERSAO").set ("100");
    objRegTXT.fieldByName ("VERSAOTESTEPGD").set ("   ");
    objRegTXT.fieldByName ("NM_NOME").setLimitado (objDecl.getIdentificadorDeclaracao ().getNome ().asString ());
    if (objDecl.getContribuinte ().getExterior ().asString ().equals (Logico.NAO))
      objRegTXT.fieldByName ("SG_UF").set (objDecl.getContribuinte ().getUf ().asString ());
    else
      objRegTXT.fieldByName ("SG_UF").set ("EX");
    objRegTXT.fieldByName ("NR_HASH").set (0);
    objRegTXT.fieldByName ("IN_CERTIFICAVEL").set (1);
    objRegTXT.fieldByName ("DT_NASCIM").set (objDecl.getContribuinte ().getDataNascimento ().asString ());
    objRegTXT.fieldByName ("IN_COMPLETA").set (objDecl.getIdentificadorDeclaracao ().getTipoDeclaracao ().asString ().equals ("0") ? "S" : "N");
    objRegTXT.fieldByName ("IN_GERADA").set (objDecl.getIdentificadorDeclaracao ().isDeclaracaoGerada () ? "S" : "N");
    objRegTXT.fieldByName ("NOME_SO").setLimitado (System.getProperty ("os.name"));
    objRegTXT.fieldByName ("VERSAO_SO").setLimitado (System.getProperty ("os.version"));
    if (objDecl.getContribuinte ().getPais ().getConteudoAtual (0).equals ("105"))
      objRegTXT.fieldByName ("CD_MUNICIP").set (objDecl.getContribuinte ().getMunicipio ().getConteudoAtual (0));
    else
      objRegTXT.fieldByName ("CD_MUNICIP").set ("9701");
    objRegTXT.fieldByName ("NR_CONJ").set (objDecl.getConjuge ().getCpfConjuge ().asString ());
    String NIBaseMaiorPagadora = objDecl.recuperarPrincipalFontePagadora ().asString ();
    if (NIBaseMaiorPagadora != null)
      objRegTXT.fieldByName ("NR_BASE_FONTE_MAIOR").set (NIBaseMaiorPagadora);
    String NIBaseSegundaPagadora = objDecl.recuperarSegundaMaiorFontePagadora ().asString ();
    if (NIBaseSegundaPagadora != null)
      objRegTXT.fieldByName ("NR_BASE_FONTE_DOIS").set (NIBaseSegundaPagadora);
    String NIBaseTerceiraPagadora = objDecl.recuperarTerceiraMaiorFontePagadora ().asString ();
    if (NIBaseTerceiraPagadora != null)
      objRegTXT.fieldByName ("NR_BASE_FONTE_TRES").set (NIBaseTerceiraPagadora);
    String cpfMaiorDep = objDecl.recuperarMaiorDependente ().asString ();
    if (cpfMaiorDep != null)
      objRegTXT.fieldByName ("NR_CPF_DEPE_REND_MAIOR").set (cpfMaiorDep);
    String cpfSegundoMaiorDepPagador = objDecl.recuperarSegundoMaiorDependente ().asString ();
    if (cpfSegundoMaiorDepPagador != null)
      objRegTXT.fieldByName ("NR_CPF_DEPE_REND_DOIS").set (cpfSegundoMaiorDepPagador);
    String cpfTerceiroMaiorDepPagador = objDecl.recuperarTerceiroMaiorDependente ().asString ();
    if (cpfTerceiroMaiorDepPagador != null)
      objRegTXT.fieldByName ("NR_CPF_DEPE_REND_TRES").set (cpfTerceiroMaiorDepPagador);
    String cpfQuartoMaiorDepPagador = objDecl.recuperarQuartoMaiorDependente ().asString ();
    if (cpfQuartoMaiorDepPagador != null)
      objRegTXT.fieldByName ("NR_CPF_DEPE_REND_QUATRO").set (cpfQuartoMaiorDepPagador);
    if (objDecl.getIdentificadorDeclaracao ().getTipoDeclaracao ().asString ().equals ("0"))
      {
	String niBaseMaiorDespMedica = objDecl.getPagamentos ().recuperarNIMaiorDespMedicas ().asString ();
	if (niBaseMaiorDespMedica != null)
	  objRegTXT.fieldByName ("NR_BASE_BENEF_DESP_MED_MAIOR").set (niBaseMaiorDespMedica);
	String niBaseSegundoMaiorDespMedica = objDecl.getPagamentos ().recuperarNISegundoMaiorDespMedicas ().asString ();
	if (niBaseSegundoMaiorDespMedica != null)
	  objRegTXT.fieldByName ("NR_BASE_BENEF_DESP_MED_DOIS").set (niBaseSegundoMaiorDespMedica);
      }
    objRegTXT.fieldByName ("IN_OBRIGAT_ENTREGA").set (objDecl.verificaObrigatoriedadeEntrega ());
    if (objDecl.getIdentificadorDeclaracao ().getTipoDeclaracao ().asString ().equals ("0"))
      objRegTXT.fieldByName ("VR_IMPDEVIDO").set (objDecl.getModelo ().getImpostoDevidoII ());
    if (objDecl.getIdentificadorDeclaracao ().getTipoDeclaracao ().asString ().equals ("1"))
      objRegTXT.fieldByName ("VR_IMPDEVIDO").set (objDecl.getModelo ().getImpostoDevido ());
    objRegTXT.fieldByName ("FILLER2").set ("");
    if (! objDecl.getModelo ().getSaldoImpostoPagar ().isVazio () && objDecl.getResumo ().getCalculoImposto ().getDebitoAutomatico ().asString ().equals ("autorizado"))
      objRegTXT.fieldByName ("IN_RESULTADO_IMPOSTO").set (3);
    else if (! objDecl.getModelo ().getSaldoImpostoPagar ().isVazio ())
      objRegTXT.fieldByName ("IN_RESULTADO_IMPOSTO").set (1);
    else if (! objDecl.getModelo ().getImpostoRestituir ().isVazio ())
      objRegTXT.fieldByName ("IN_RESULTADO_IMPOSTO").set (2);
    else
      objRegTXT.fieldByName ("IN_RESULTADO_IMPOSTO").set (0);
    objRegTXT.fieldByName ("IN_IMPOSTO_PAGO").set (objDecl.getModelo ().recuperarCodInImpostoPago ());
    objRegTXT.fieldByName ("NR_CPF_INVENTARIANTE").set (objDecl.getEspolio ().getCpfInventariante ().asString ());
    String maiorPensaoAlimenticia = objDecl.getPagamentos ().recuperarCpfMaiorPensaoAlimenticia ();
    objRegTXT.fieldByName ("NR_CPF_DEST_PENSAO_ALIMENT_MAIOR").set (maiorPensaoAlimenticia);
    boolean ehRetificadora = objDecl.getIdentificadorDeclaracao ().isRetificadora ();
    objDecl.getContribuinte ().getNumeroReciboDecAnterior ().validar ();
    if (ehRetificadora)
      objRegTXT.fieldByName ("IN_SEGURANCA").set (0);
    else if (objDecl.getContribuinte ().getNumeroReciboDecAnterior ().isVazio () || ! objDecl.getContribuinte ().getNumeroReciboDecAnterior ().isValido ())
      objRegTXT.fieldByName ("IN_SEGURANCA").set (1);
    else
      objRegTXT.fieldByName ("IN_SEGURANCA").set (2);
    objRegTXT.fieldByName ("IN_PLATAFORMAPGD").set (2);
    linha.add (objRegTXT);
    return linha;
  }
  
  public Vector montarRegistroContribuinte (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    Contribuinte contribuinte = objDecl.getContribuinte ();
    RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "16");
    objRegTXT.fieldByName ("NR_REG").set ("16");
    objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
    objRegTXT.fieldByName ("NM_NOME").setLimitado (objDecl.getIdentificadorDeclaracao ().getNome ().asString ());
    objRegTXT.fieldByName ("NR_TITELEITOR").set (contribuinte.getTituloEleitor ().asString ());
    objRegTXT.fieldByName ("DT_NASCIM").set (contribuinte.getDataNascimento ().asString ());
    objRegTXT.fieldByName ("TIP_LOGRA").set (contribuinte.getTipoLogradouro ().getConteudoAtual (0));
    objRegTXT.fieldByName ("CD_MUNICIP").set (contribuinte.getMunicipio ().getConteudoAtual (0));
    objRegTXT.fieldByName ("NM_COMPLEM").setLimitado (contribuinte.getComplemento ().asString ());
    objRegTXT.fieldByName ("NR_CEP").set (contribuinte.getCep ().asString ());
    if (contribuinte.getExterior ().asString ().equals (Logico.SIM))
      {
	objRegTXT.fieldByName ("SG_UF").set ("EX");
	objRegTXT.fieldByName ("CD_EX").set (contribuinte.getCodigoExterior ().getConteudoAtual (0));
	objRegTXT.fieldByName ("CD_PAIS").set (contribuinte.getPais ().getConteudoAtual (0));
	objRegTXT.fieldByName ("NM_MUNICIP").setLimitado (contribuinte.getCidade ().getConteudoFormatado ());
	objRegTXT.fieldByName ("NM_LOGRA").setLimitado (contribuinte.getLogradouroExt ().asString ());
	objRegTXT.fieldByName ("NR_NUMERO").setLimitado (contribuinte.getNumeroExt ().asString ());
	objRegTXT.fieldByName ("NM_BAIRRO").setLimitado (contribuinte.getBairroExt ().asString ());
	objRegTXT.fieldByName ("NM_COMPLEM").setLimitado (contribuinte.getComplementoExt ().asString ());
      }
    else
      {
	objRegTXT.fieldByName ("NM_MUNICIP").setLimitado (contribuinte.getMunicipio ().getConteudoAtual (1));
	objRegTXT.fieldByName ("SG_UF").set (contribuinte.getUf ().getConteudoAtual (0));
	objRegTXT.fieldByName ("NM_LOGRA").setLimitado (contribuinte.getLogradouro ().asString ());
	objRegTXT.fieldByName ("NR_NUMERO").setLimitado (contribuinte.getNumero ().asString ());
	objRegTXT.fieldByName ("NM_BAIRRO").setLimitado (contribuinte.getBairro ().asString ());
	objRegTXT.fieldByName ("NM_COMPLEM").setLimitado (contribuinte.getComplemento ().asString ());
      }
    String ddd = contribuinte.getDdd ().asString ().trim ();
    objRegTXT.fieldByName ("NR_DDD_TELEFONE").set (ddd);
    objRegTXT.fieldByName ("NR_TELEFONE").set (contribuinte.getTelefone ().asString ());
    objRegTXT.fieldByName ("CD_OCUP").set (contribuinte.getOcupacaoPrincipal ().getConteudoAtual (0));
    objRegTXT.fieldByName ("CD_NATUR").set (contribuinte.getNaturezaOcupacao ().getConteudoAtual (0));
    objRegTXT.fieldByName ("NR_QUOTAS").set (objDecl.getResumo ().getCalculoImposto ().getNumQuotas ().asInteger ());
    objRegTXT.fieldByName ("NR_BANCO").set (objDecl.getResumo ().getCalculoImposto ().getBanco ().getConteudoAtual (0));
    objRegTXT.fieldByName ("NR_AGENCIA").set (objDecl.getResumo ().getCalculoImposto ().getAgencia ().asString ());
    objRegTXT.fieldByName ("NR_DV_AGENCIA").set (objDecl.getResumo ().getCalculoImposto ().getDvAgencia ().asString ());
    objRegTXT.fieldByName ("NR_CONTA").set (objDecl.getResumo ().getCalculoImposto ().getContaCredito ().asString ());
    objRegTXT.fieldByName ("NR_DV_CONTA").set (objDecl.getResumo ().getCalculoImposto ().getDvContaCredito ().asString ());
    String debitoAutom = objDecl.getResumo ().getCalculoImposto ().getDebitoAutomatico ().asString ().equals ("autorizado") ? "S" : "N";
    objRegTXT.fieldByName ("IN_DEBITO_AUTOM").set (debitoAutom);
    objRegTXT.fieldByName ("IN_COMPLETA").set (objDecl.getIdentificadorDeclaracao ().isCompleta () ? "S" : "N");
    objRegTXT.fieldByName ("IN_GERADO").set (objDecl.getIdentificadorDeclaracao ().isDeclaracaoGerada () ? "S" : "N");
    objRegTXT.fieldByName ("IN_RETIFICADORA").set (objDecl.getIdentificadorDeclaracao ().isRetificadora () ? "S" : "N");
    if (objDecl.getIdentificadorDeclaracao ().isRetificadora ())
      objRegTXT.fieldByName ("NR_CONTROLE_ORIGINAL").set (objDecl.getIdentificadorDeclaracao ().getNumReciboDecRetif ().asString ());
    objRegTXT.fieldByName ("IN_ENDERECO").set (objDecl.getContribuinte ().getEnderecoDiferente ().asString ().equals (Logico.SIM) ? "S" : "N");
    objRegTXT.fieldByName ("NR_FONTE_PRINCIPAL").set (objDecl.recuperarPrincipalFontePagadora ().asString ());
    String numeroDeclaracaoAnoAnterior = objDecl.getContribuinte ().getNumeroReciboDecAnterior ().asString ();
    if (numeroDeclaracaoAnoAnterior.length () >= 10)
      objRegTXT.fieldByName ("NR_RECIBO_ULTIMA_DEC_ANO_ANTERIOR").set (numeroDeclaracaoAnoAnterior.substring (0, 10));
    else if (numeroDeclaracaoAnoAnterior.length () > 0)
      objRegTXT.fieldByName ("NR_RECIBO_ULTIMA_DEC_ANO_ANTERIOR").set (numeroDeclaracaoAnoAnterior.substring (0, 9));
    linha.add (objRegTXT);
    return linha;
  }
  
  public Vector montarRegistroDeclaracaoCompleta (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "19");
    objRegTXT.fieldByName ("NR_REG").set ("19");
    objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
    objRegTXT.fieldByName ("NR_FONTE").set (objDecl.getColecaoRendPJTitular ().getNiMaiorFontePagadora ().asString ());
    objRegTXT.fieldByName ("VR_IMPEXT").set (objDecl.getModeloCompleta ().getImpostoPagoExterior ());
    objRegTXT.fieldByName ("VR_IMPCOMP").set (objDecl.getModeloCompleta ().getImpostoComplementar ());
    objRegTXT.fieldByName ("VR_IRFONTELEI11033").set (objDecl.getModeloCompleta ().getImpostoRetidoFonteLei11033 ());
    objRegTXT.fieldByName ("VR_RECEX_TIT").set (objDecl.getRendPFTitular ().getTotalExterior ());
    objRegTXT.fieldByName ("VR_LIVCAIX_TIT").set (objDecl.getRendPFTitular ().getTotalLivroCaixa ());
    objRegTXT.fieldByName ("VR_CARNELEAO_TIT").set (objDecl.getRendPFTitular ().getTotalDarfPago ());
    objRegTXT.fieldByName ("VR_RECEX_DEP").set (objDecl.getRendPFDependente ().getTotalExterior ());
    objRegTXT.fieldByName ("VR_LIVCAIX_DEP").set (objDecl.getRendPFDependente ().getTotalLivroCaixa ());
    objRegTXT.fieldByName ("VR_CARNELEAO_DEP").set (objDecl.getRendPFDependente ().getTotalDarfPago ());
    objRegTXT.fieldByName ("VR_PREVPRIV").set (CalculosPagamentos.totalizarPagamentosGlosado (objDecl.getPagamentos (), new String[] { "13" }, true));
    objRegTXT.fieldByName ("VR_FAPI").set (CalculosPagamentos.totalizarPagamentosGlosado (objDecl.getPagamentos (), new String[] { "14" }, true));
    objRegTXT.fieldByName ("VR_PREVOFTITULAR").set (objDecl.getColecaoRendPJTitular ().getTotaisContribuicaoPrevOficial ());
    objRegTXT.fieldByName ("VR_PREVOFDEPENDENTE").set (objDecl.getColecaoRendPJDependente ().getTotaisContribuicaoPrevOficial ());
    objRegTXT.fieldByName ("VR_TOTAL13SALARIOTITULAR").set (objDecl.getRendTributacaoExclusiva ().getDecimoTerceiro ());
    objRegTXT.fieldByName ("VR_TOTAL13SALARIODEPENDENTE").set (objDecl.getRendTributacaoExclusiva ().getDecimoTerceiroDependentes ());
    objRegTXT.fieldByName ("NR_DEPENDENTE_DESP_INSTRUCAO").set (objDecl.getPagamentos ().obterTotalDependentesEnvolvidos ());
    objRegTXT.fieldByName ("NR_ALIMENTANDO_DESP_INSTRUCAO").set (objDecl.getPagamentos ().obterTotalAlimentandosEnvolvidos ());
    linha.add (objRegTXT);
    return linha;
  }
  
  public Vector montarFichaResumoCompleta (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "20");
    objRegTXT.fieldByName ("NR_REG").set ("20");
    objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
    objRegTXT.fieldByName ("VR_RENDJUR").set (objDecl.getModeloCompleta ().getRendRecebidoPJTitular ());
    objRegTXT.fieldByName ("VR_RENDJURDEPENDENTE").set (objDecl.getModeloCompleta ().getRendRecebidoPJDependentes ());
    objRegTXT.fieldByName ("VR_RENDFISIC_TIT").set (objDecl.getModeloCompleta ().getRendRecebidoPFTitular ());
    objRegTXT.fieldByName ("VR_RENDFISIC_DEP").set (objDecl.getModeloCompleta ().getRendRecebidoPFDependentes ());
    objRegTXT.fieldByName ("VR_RECEX").set (objDecl.getModeloCompleta ().getRendRecebidoExterior ());
    objRegTXT.fieldByName ("VR_RESAR").set (objDecl.getModeloCompleta ().getResultadoTributavelAR ());
    objRegTXT.fieldByName ("VR_TOTTRIB").set (objDecl.getModeloCompleta ().getTotalRendimentos ());
    objRegTXT.fieldByName ("VR_PREVOF").set (objDecl.getModeloCompleta ().getPrevidenciaOficial ());
    objRegTXT.fieldByName ("VR_TOTPRIVADA").set (objDecl.getModeloCompleta ().getPrevidenciaFAPI ());
    objRegTXT.fieldByName ("VR_DEPEN").set (objDecl.getModeloCompleta ().getDeducaoDependentes ());
    objRegTXT.fieldByName ("VR_DESPINST").set (objDecl.getModeloCompleta ().getDespesasInstrucao ());
    objRegTXT.fieldByName ("VR_DESPMEDIC").set (objDecl.getModeloCompleta ().getDespesasMedicas ());
    objRegTXT.fieldByName ("VR_PENSAO").set (objDecl.getModeloCompleta ().getPensaoAlimenticia ());
    objRegTXT.fieldByName ("VR_LIVCAIX").set (objDecl.getModeloCompleta ().getLivroCaixa ());
    objRegTXT.fieldByName ("VR_DEDUC").set (objDecl.getModeloCompleta ().getTotalDeducoes ());
    objRegTXT.fieldByName ("VR_BASECALC").set (objDecl.getModeloCompleta ().getBaseCalculo ());
    objRegTXT.fieldByName ("VR_IMPOSTO").set (objDecl.getModeloCompleta ().getImposto ());
    objRegTXT.fieldByName ("VR_DEDIMPOSTO").set (objDecl.getModeloCompleta ().getDeducaoIncentivo ());
    objRegTXT.fieldByName ("VR_IMPDEV").set (objDecl.getModeloCompleta ().getImpostoDevido ());
    objRegTXT.fieldByName ("VR_CONTPATRONAL").set (objDecl.getResumo ().getCalculoImposto ().getTotalContribEmpregadoDomestico ());
    objRegTXT.fieldByName ("VR_IMPDEV2").set (objDecl.getModeloCompleta ().getImpostoDevidoII ());
    objRegTXT.fieldByName ("VR_IMPFONTE").set (objDecl.getModeloCompleta ().getImpostoRetidoFonteTitular ());
    objRegTXT.fieldByName ("VR_IMPFONTEDEPENDENTE").set (objDecl.getModeloCompleta ().getImpostoRetidoFonteDependentes ());
    objRegTXT.fieldByName ("VR_CARNELEAO").set (objDecl.getModeloCompleta ().getCarneLeao ());
    objRegTXT.fieldByName ("VR_IMPCOMPL").set (objDecl.getModeloCompleta ().getImpostoComplementar ());
    objRegTXT.fieldByName ("VR_IMPEXT").set (objDecl.getModeloCompleta ().getImpostoPagoExterior ());
    objRegTXT.fieldByName ("VR_IRFONTELEI11033").set (objDecl.getModeloCompleta ().getImpostoRetidoFonteLei11033 ());
    objRegTXT.fieldByName ("VR_TOTIMPPAGO").set (objDecl.getModeloCompleta ().getTotalImpostoPago ());
    if (objDecl.getModelo ().getImpostoRestituir ().comparacao (">", "0,00"))
      {
	objRegTXT.fieldByName ("VR_IMPREST").set (objDecl.getModeloCompleta ().getImpostoRestituir ());
	objRegTXT.fieldByName ("VR_IMPPAGAR").set (0);
      }
    else if (objDecl.getModelo ().getSaldoImpostoPagar ().comparacao (">", "0,00"))
      {
	objRegTXT.fieldByName ("VR_IMPPAGAR").set (objDecl.getModeloCompleta ().getSaldoImpostoPagar ());
	objRegTXT.fieldByName ("VR_IMPREST").set (0);
      }
    else
      {
	objRegTXT.fieldByName ("VR_IMPREST").set (0);
	objRegTXT.fieldByName ("VR_IMPPAGAR").set (0);
      }
    objRegTXT.fieldByName ("NR_QUOTAS").set (objDecl.getResumo ().getCalculoImposto ().getNumQuotas ().asInteger ());
    objRegTXT.fieldByName ("VR_QUOTA").set (objDecl.getResumo ().getCalculoImposto ().getValorQuota ());
    objRegTXT.fieldByName ("VR_BENSANT").set (objDecl.getModeloCompleta ().getBensDireitosExercicioAnterior ());
    objRegTXT.fieldByName ("VR_BENSATUAL").set (objDecl.getModeloCompleta ().getBensDireitosExercicioAtual ());
    objRegTXT.fieldByName ("VR_DIVIDAANT").set (objDecl.getModeloCompleta ().getDividasExercicioAnterior ());
    objRegTXT.fieldByName ("VR_DIVIDAATUAL").set (objDecl.getModeloCompleta ().getDividasExercicioAtual ());
    objRegTXT.fieldByName ("VR_CONJUGE").set (objDecl.getModeloCompleta ().getInformacoesConjuge ());
    objRegTXT.fieldByName ("VR_TOTISENTOS").set (objDecl.getModeloCompleta ().getRendIsentosNaoTributaveis ());
    objRegTXT.fieldByName ("VR_TOTEXCLUS").set (objDecl.getModeloCompleta ().getRendSujeitoTribExclusiva ());
    objRegTXT.fieldByName ("VR_IMPGC").set (0);
    objRegTXT.fieldByName ("VR_TOTIRFONTELEI11033").set (objDecl.getModeloCompleta ().getTotalImpostoRetidoNaFonte ());
    objRegTXT.fieldByName ("VR_IMPRV").set (objDecl.getModeloCompleta ().getImpostoPagoSobreRendaVariavel ());
    objRegTXT.fieldByName ("VR_IMPPAGOVCBENS").set (0);
    objRegTXT.fieldByName ("VR_IMPPAGOVCESPECIE").set (0);
    Valor totalDep = objDecl.getRendIsentos ().getRendDependentes ().operacao ('+', objDecl.getRendIsentos ().recuperarTotalLucrosDividendosDep ());
    objRegTXT.fieldByName ("VR_TOTRENDISENTOSTITULAR").set (objDecl.getRendIsentos ().getTotal ().operacao ('-', totalDep));
    objRegTXT.fieldByName ("VR_TOTRENDISENTOSDEPENDENTE").set (totalDep);
    objRegTXT.fieldByName ("VR_TOTRENDEXCLTITULAR").set (objDecl.getRendTributacaoExclusiva ().recuperarExclusivosTitular ());
    objRegTXT.fieldByName ("VR_TOTRENDEXCLDEPENDENTE").set (objDecl.getRendTributacaoExclusiva ().recuperarExclusivosDependentes ());
    objRegTXT.fieldByName ("VR_DOACOESCAMPANHA").set (objDecl.getResumo ().getOutrasInformacoes ().getTotalDoacoesCampanhasEleitorais ());
    linha.add (objRegTXT);
    return linha;
  }
  
  public Vector montarFichaRendPJ (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    ColecaoRendPJTitular colecaoRendimentos = objDecl.getColecaoRendPJTitular ();
    for (int i = 0; i < colecaoRendimentos.recuperarLista ().size (); i++)
      {
	RendPJTitular rendimentoPJ = (RendPJTitular) colecaoRendimentos.recuperarLista ().get (i);
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "21");
	objRegTXT.fieldByName ("NR_REG").set ("21");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("NR_PAGADOR").set (rendimentoPJ.getNIFontePagadora ().asString ());
	objRegTXT.fieldByName ("NM_PAGADOR").setLimitado (rendimentoPJ.getNomeFontePagadora ().asString ());
	objRegTXT.fieldByName ("VR_RENDTO").set (rendimentoPJ.getRendRecebidoPJ ());
	objRegTXT.fieldByName ("VR_IMPOSTO").set (rendimentoPJ.getImpostoRetidoFonte ());
	objRegTXT.fieldByName ("VR_CONTRIB").set (rendimentoPJ.getContribuicaoPrevOficial ());
	objRegTXT.fieldByName ("VR_DECTERC").set (rendimentoPJ.getDecimoTerceiro ());
	linha.add (objRegTXT);
      }
    return linha;
  }
  
  public Vector montarFichaRendPF (DeclaracaoIRPF objDecl, boolean ehDependente) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    RendPF colecaoRendPF;
    if (ehDependente)
      colecaoRendPF = objDecl.getRendPFDependente ();
    else
      colecaoRendPF = objDecl.getRendPFTitular ();
    if (! colecaoRendPF.isVazio ())
      {
	for (int i = 1; i <= 12; i++)
	  {
	    MesRendPF rendimentoMensalPF = colecaoRendPF.getMesRendPFPorIndice (i - 1);
	    RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "22");
	    objRegTXT.fieldByName ("NR_REG").set ("22");
	    objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	    objRegTXT.fieldByName ("E_DEPENDENTE").set (ehDependente);
	    objRegTXT.fieldByName ("NR_MES").set (i);
	    objRegTXT.fieldByName ("VR_RENDTO").set (rendimentoMensalPF.getPessoaFisica ());
	    objRegTXT.fieldByName ("VR_EXTER").set (rendimentoMensalPF.getExterior ());
	    objRegTXT.fieldByName ("VR_LIVCAIX").set (rendimentoMensalPF.getLivroCaixa ());
	    objRegTXT.fieldByName ("VR_ALIMENT").set (rendimentoMensalPF.getPensao ());
	    objRegTXT.fieldByName ("VR_DEDUC").set (rendimentoMensalPF.getDependentes ());
	    objRegTXT.fieldByName ("VR_PREVID").set (rendimentoMensalPF.getPrevidencia ());
	    Valor valBaseCalculo = new Valor ();
	    valBaseCalculo.append ('+', rendimentoMensalPF.getPessoaFisica ());
	    valBaseCalculo.append ('+', rendimentoMensalPF.getExterior ());
	    valBaseCalculo.append ('-', rendimentoMensalPF.getPrevidencia ());
	    valBaseCalculo.append ('-', rendimentoMensalPF.getDependentes ());
	    valBaseCalculo.append ('-', rendimentoMensalPF.getPensao ());
	    valBaseCalculo.append ('-', rendimentoMensalPF.getLivroCaixa ());
	    objRegTXT.fieldByName ("VR_BASECALCULO").set (valBaseCalculo);
	    objRegTXT.fieldByName ("VR_IMPOSTO").set (rendimentoMensalPF.getDarfPago ());
	    linha.add (objRegTXT);
	  }
      }
    return linha;
  }
  
  public Vector montarFichaRendIsentos (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    RendIsentos rendIsentos = objDecl.getRendIsentos ();
    if (rendIsentos.getTotal ().comparacao (">", "0,00"))
      {
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "23");
	objRegTXT.fieldByName ("NR_REG").set ("23");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("VR_BOLSA").set (rendIsentos.getBolsaEstudos ());
	objRegTXT.fieldByName ("VR_FGTS").set (rendIsentos.getIndenizacoes ());
	objRegTXT.fieldByName ("VR_GCISENTO").set (rendIsentos.getLucroAlienacao ());
	objRegTXT.fieldByName ("VR_LUCROS").set (rendIsentos.getLucroRecebido ());
	objRegTXT.fieldByName ("VR_RURAL").set (rendIsentos.getParcIsentaAtivRural ());
	objRegTXT.fieldByName ("VR_65ANOS").set (rendIsentos.getParcIsentaAposentadoria ());
	objRegTXT.fieldByName ("VR_PREVID").set (rendIsentos.getCapitalApolices ());
	objRegTXT.fieldByName ("VR_INVALIDEZ").set (rendIsentos.getPensao ());
	objRegTXT.fieldByName ("VR_POUPANCA").set (rendIsentos.getPoupanca ());
	objRegTXT.fieldByName ("VR_SOCIO").set (rendIsentos.getRendSocio ());
	objRegTXT.fieldByName ("VR_HERANCA").set (rendIsentos.getTransferencias ());
	objRegTXT.fieldByName ("VR_OUTROS").set (rendIsentos.getOutros ());
	objRegTXT.fieldByName ("TX_OUTROS").set (rendIsentos.getDescOutros ().asString ());
	objRegTXT.fieldByName ("VR_ISENTOSDEPENDENTES").set (rendIsentos.getRendDependentes ());
	objRegTXT.fieldByName ("VR_PEQUENO").set (rendIsentos.getBensPequenoValorInformado ());
	objRegTXT.fieldByName ("VR_UNICO").set (rendIsentos.getUnicoImovelInformado ());
	objRegTXT.fieldByName ("VR_REDUCAO").set (rendIsentos.getOutrosBensImoveisInformado ());
	objRegTXT.fieldByName ("VR_GCMOEDAEST").set (rendIsentos.getMoedaEstrangeiraEspecieInformado ());
	objRegTXT.fieldByName ("VR_PEQTRANSP").set (rendIsentos.getBensPequenoValorTransportado ());
	objRegTXT.fieldByName ("VR_UNITRANSP").set (rendIsentos.getUnicoImovelTransportado ());
	objRegTXT.fieldByName ("VR_REDTRANSP").set (rendIsentos.getOutrosBensImoveisTransportado ());
	objRegTXT.fieldByName ("VR_GCMOEDAESTTRANSP").set (rendIsentos.getMoedaEstrangeiraEspecieTransportado ());
	objRegTXT.fieldByName ("VR_GCTOTALINFORMADO").set (rendIsentos.getTotalInformado ());
	objRegTXT.fieldByName ("VR_GCTOTALTRANSPORTADO").set (rendIsentos.getTotalTransportado ());
	linha.add (objRegTXT);
      }
    return linha;
  }
  
  public Vector montarFichaRendTribExcl (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    RendTributacaoExclusiva rendExclusivos = objDecl.getRendTributacaoExclusiva ();
    if (rendExclusivos.getTotal ().comparacao (">", "0,00"))
      {
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "24");
	objRegTXT.fieldByName ("NR_REG").set ("24");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("VR_13SAL").set (rendExclusivos.getDecimoTerceiro ());
	objRegTXT.fieldByName ("VR_RENDAVAR").set (rendExclusivos.getGanhosRendaVariavel ());
	objRegTXT.fieldByName ("VR_FINANCEIRAS").set (rendExclusivos.getRendAplicacoes ());
	objRegTXT.fieldByName ("VR_OUTROS").set (rendExclusivos.getOutros ());
	objRegTXT.fieldByName ("TX_OUTROS").set (rendExclusivos.getDescTotal ().asString ());
	objRegTXT.fieldByName ("VR_13SALDEPENDENTES").set (rendExclusivos.getDecimoTerceiroDependentes ());
	objRegTXT.fieldByName ("VR_EXCLUSIVAEXCETO13SALDEP").set (rendExclusivos.getRendExcetoDecimoTerceiro ());
	linha.add (objRegTXT);
      }
    return linha;
  }
  
  public Vector montarFichaDependentes (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    Dependentes colecaoDependentes = objDecl.getDependentes ();
    for (int i = 0; i < colecaoDependentes.recuperarLista ().size (); i++)
      {
	Dependente dependente = (Dependente) colecaoDependentes.recuperarLista ().get (i);
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "25");
	objRegTXT.fieldByName ("NR_REG").set ("25");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	try
	  {
	    objRegTXT.fieldByName ("CD_DEPEND").set (dependente.getCodigo ().getConteudoAtual (0));
	  }
	catch (Exception exception)
	  {
	    /* empty */
	  }
	objRegTXT.fieldByName ("NR_CHAVE").set (i + 1);
	objRegTXT.fieldByName ("NM_DEPEND").setLimitado (dependente.getNome ().asString ());
	objRegTXT.fieldByName ("DT_NASCIM").set (dependente.getDataNascimento ().asString ());
	objRegTXT.fieldByName ("NI_DEPEND").set (dependente.getCpfDependente ().asString ());
	linha.add (objRegTXT);
      }
    return linha;
  }
  
  public Vector montarFichaPagamentos (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    Pagamentos colecaoPagamentos = objDecl.getPagamentos ();
    for (int i = 0; i < colecaoPagamentos.recuperarLista ().size (); i++)
      {
	Pagamento pagamento = (Pagamento) colecaoPagamentos.recuperarLista ().get (i);
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "26");
	objRegTXT.fieldByName ("NR_REG").set ("26");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("CD_PAGTO").set (pagamento.getCodigo ().asString ());
	objRegTXT.fieldByName ("NR_BENEF").set (pagamento.getNiBeneficiario ().asString ());
	objRegTXT.fieldByName ("NR_CHAVE_DEPEND").set (objDecl.getChaveDependenteOuAlimentando (pagamento));
	objRegTXT.fieldByName ("NM_BENEF").setLimitado (pagamento.getNomeBeneficiario ().asString ());
	objRegTXT.fieldByName ("NR_NIT").setLimitado (pagamento.getNitEmpregadoDomestico ().asString ());
	objRegTXT.fieldByName ("VR_PAGTO").set (pagamento.getValorPago ());
	objRegTXT.fieldByName ("VR_REDUC").set (pagamento.getParcelaNaoDedutivel ());
	linha.add (objRegTXT);
      }
    return linha;
  }
  
  public Vector montarFichaLucrosDividendos (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    ColecaoItemQuadroLucrosDividendos colDividendos = objDecl.getRendIsentos ().getLucroRecebidoQuadroAuxiliar ();
    int count = 1;
    Iterator it = colDividendos.recuperarLista ().iterator ();
    while (it.hasNext ())
      {
	ItemQuadroLucrosDividendos item = (ItemQuadroLucrosDividendos) it.next ();
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "33");
	objRegTXT.fieldByName ("NR_REG").set ("33");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("NR_CHAVE").set (count++);
	objRegTXT.fieldByName ("NR_TIPO").set ("" + item.getTipo ().getConteudoFormatado ().charAt (0));
	objRegTXT.fieldByName ("NR_PAGADORA").set (item.getCnpjEmpresa ().asString ());
	objRegTXT.fieldByName ("NM_PAGADORA").set (item.getNomeFonte ().asString ());
	objRegTXT.fieldByName ("VR_LUCRO").set (item.getValor ());
	linha.add (objRegTXT);
      }
    return linha;
  }
  
  public Vector montarFichaDoacoesCampanha (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    Doacoes colDoacoes = objDecl.getDoacoes ();
    Iterator it = colDoacoes.recuperarLista ().iterator ();
    while (it.hasNext ())
      {
	Doacao item = (Doacao) it.next ();
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "34");
	objRegTXT.fieldByName ("NR_REG").set ("34");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("NR_PARTIDO").set (item.getCNPJ ().asString ());
	objRegTXT.fieldByName ("NM_PARTIDO").set (item.getNome ().asString ());
	objRegTXT.fieldByName ("VR_DOACAO").set (item.getValor ());
	linha.add (objRegTXT);
      }
    return linha;
  }
  
  public Vector montarFichaAlimentandos (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    Alimentandos colecaoAlimentandos = objDecl.getAlimentandos ();
    for (int i = 0; i < colecaoAlimentandos.recuperarLista ().size (); i++)
      {
	Alimentando alimentando = (Alimentando) colecaoAlimentandos.recuperarLista ().get (i);
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "35");
	objRegTXT.fieldByName ("NR_REG").set ("35");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("NM_NOME").setLimitado (alimentando.getNome ().asString ());
	objRegTXT.fieldByName ("NR_CHAVE").set (i + 1);
	linha.add (objRegTXT);
      }
    return linha;
  }
  
  public Vector montarFichaBem (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    objDecl.getBens ().excluirRegistrosEmBranco ();
    Bens colecaoBens = objDecl.getBens ();
    for (int i = 0; i < colecaoBens.recuperarLista ().size (); i++)
      {
	Bem bem = (Bem) colecaoBens.recuperarLista ().get (i);
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "27");
	objRegTXT.fieldByName ("NR_REG").set ("27");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("TX_BEM").setLimitado (bem.getDiscriminacao ().asString ());
	objRegTXT.fieldByName ("CD_BEM").set (bem.getCodigo ().asString ());
	objRegTXT.fieldByName ("VR_ANTER").set (bem.getValorExercicioAnterior ());
	objRegTXT.fieldByName ("VR_ATUAL").set (bem.getValorExercicioAtual ());
	if (bem.getPais ().getConteudoAtual (0).equals ("105"))
	  {
	    objRegTXT.fieldByName ("CD_PAIS").set ("000");
	    objRegTXT.fieldByName ("IN_EXTERIOR").set (0);
	  }
	else
	  {
	    objRegTXT.fieldByName ("CD_PAIS").set (bem.getPais ().getConteudoAtual (0));
	    objRegTXT.fieldByName ("IN_EXTERIOR").set (1);
	  }
	linha.add (objRegTXT);
      }
    return linha;
  }
  
  public Vector montarFichaDividas (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    Dividas colecaoDividas = objDecl.getDividas ();
    for (int i = 0; i < colecaoDividas.recuperarLista ().size (); i++)
      {
	Divida divida = (Divida) colecaoDividas.recuperarLista ().get (i);
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "28");
	objRegTXT.fieldByName ("NR_REG").set ("28");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("TX_DIV").setLimitado (divida.getDiscriminacao ().asString ());
	objRegTXT.fieldByName ("CD_DIV").set (divida.getCodigo ().asString ());
	objRegTXT.fieldByName ("VR_ANTER").set (divida.getValorExercicioAnterior ());
	objRegTXT.fieldByName ("VR_ATUAL").set (divida.getValorExercicioAtual ());
	linha.add (objRegTXT);
      }
    return linha;
  }
  
  public Vector montarFichaConjuge (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "29");
    objRegTXT.fieldByName ("NR_REG").set ("29");
    objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
    Conjuge conjuge = objDecl.getConjuge ();
    objRegTXT.fieldByName ("NR_CONJ").set (conjuge.getCpfConjuge ().asString ());
    objRegTXT.fieldByName ("VR_BASE").set (conjuge.getBaseCalculoImposto ());
    objRegTXT.fieldByName ("IN_TIPO").set (conjuge.getDecSimplificada ().asString ().equals (Logico.SIM) ? "S" : "N");
    if (conjuge.getDecSimplificada ().asString ().equals (Logico.SIM))
      {
	objRegTXT.fieldByName ("VR_RETIDO_S").set (conjuge.getImpRetidoFonte ());
	objRegTXT.fieldByName ("VR_LEAO_S").set (conjuge.getCarneComImpComplementar ());
      }
    else
      objRegTXT.fieldByName ("VR_IMPOSTO_C").set (conjuge.getImpRetidoFonte ());
    objRegTXT.fieldByName ("VR_ISENTO_C").set (conjuge.getRendIsentoNaoTributaveis ());
    objRegTXT.fieldByName ("VR_EXCLUSIVO_C").set (conjuge.getRendSujeitosTribExcl ());
    objRegTXT.fieldByName ("VR_TOTALCONJ").set (conjuge.getResultado ());
    linha.add (objRegTXT);
    return linha;
  }
  
  public Vector montarFichaInventariante (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    Espolio inventariante = objDecl.getEspolio ();
    if (! inventariante.getNomeInventariante ().asString ().equals ("") && ! inventariante.getEndInventariante ().asString ().equals ("") && ! inventariante.getCpfInventariante ().asString ().equals (""))
      {
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "30");
	objRegTXT.fieldByName ("NR_REG").set ("30");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("NR_INVENT").set (inventariante.getCpfInventariante ().asString ());
	objRegTXT.fieldByName ("NM_INVENT").setLimitado (inventariante.getNomeInventariante ().asString ());
	objRegTXT.fieldByName ("NM_END_INVENT").setLimitado (inventariante.getEndInventariante ().asString ());
	linha.add (objRegTXT);
      }
    return linha;
  }
  
  public Vector montarFichaRendPJDependente (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    ColecaoRendPJDependente colecaoRendimentos = objDecl.getColecaoRendPJDependente ();
    for (int i = 0; i < colecaoRendimentos.recuperarLista ().size (); i++)
      {
	RendPJDependente rendimentoPJ = (RendPJDependente) colecaoRendimentos.recuperarLista ().get (i);
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "32");
	objRegTXT.fieldByName ("NR_REG").set ("32");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("CPF_BENEF").set (rendimentoPJ.getCpfDependente ().asString ());
	objRegTXT.fieldByName ("NR_PAGADOR").set (rendimentoPJ.getNIFontePagadora ().asString ());
	objRegTXT.fieldByName ("NM_PAGADOR").setLimitado (rendimentoPJ.getNomeFontePagadora ().asString ());
	objRegTXT.fieldByName ("VR_RENDTO").set (rendimentoPJ.getRendRecebidoPJ ());
	objRegTXT.fieldByName ("VR_CONTRIB").set (rendimentoPJ.getContribuicaoPrevOficial ());
	objRegTXT.fieldByName ("VR_DECTERC").set (rendimentoPJ.getDecimoTerceiro ());
	objRegTXT.fieldByName ("VR_IMPOSTO").set (rendimentoPJ.getImpostoRetidoFonte ());
	linha.add (objRegTXT);
      }
    return linha;
  }
  
  public Vector montarFichaCPFDependentesRendPF (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    ColecaoCPFDependentes colecaoCPF = objDecl.getRendPFDependente ().getColecaoCPFDependentes ();
    colecaoCPF.excluirRegistrosEmBranco ();
    for (int i = 0; i < colecaoCPF.recuperarLista ().size (); i++)
      {
	CPFDependente cpfDependente = (CPFDependente) colecaoCPF.recuperarLista ().get (i);
	System.out.println ("cpf depen: " + cpfDependente.getCpf ());
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "36");
	objRegTXT.fieldByName ("NR_REG").set ("36");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("RENDPF_DEP_CPF").set (cpfDependente.getCpf ().asString ());
	linha.add (objRegTXT);
      }
    return linha;
  }
  
  public Vector montarFichaSimplificada (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "17");
    objRegTXT.fieldByName ("NR_REG").set ("17");
    objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
    RendPF rendimentosPF = objDecl.getRendPFTitular ();
    objRegTXT.fieldByName ("VR_RENDPFEXT").set (objDecl.getModeloSimplificada ().getRendRecebidoPF ().operacao ('+', objDecl.getModeloSimplificada ().getRendRecebidoExterior ()));
    objRegTXT.fieldByName ("VR_LEAO").set (objDecl.getModeloSimplificada ().getCarneLeaoMaisImpostoComplementarTitular ());
    objRegTXT.fieldByName ("VR_LUCROSTIT").set (objDecl.getRendIsentos ().recuperarTotalLucrosDividendosTit ());
    objRegTXT.fieldByName ("VR_ISENTOS").set (objDecl.getRendIsentos ().recuperarTotalTitularExcetoAtividadeRuraleGC ());
    objRegTXT.fieldByName ("VR_EXCLUSIVOS").set (objDecl.getRendTributacaoExclusiva ().recuperarTotalTitularExceto13_RV_e_GC ());
    objRegTXT.fieldByName ("VR_TOTAL13").set (objDecl.getRendTributacaoExclusiva ().getDecimoTerceiro ());
    objRegTXT.fieldByName ("VR_IRFONTELEI11033").set (objDecl.getModeloSimplificada ().getImpostoRetidoFonteLei11033 ());
    objRegTXT.fieldByName ("VR_TOTAL13DEPEND").set (objDecl.getRendTributacaoExclusiva ().getDecimoTerceiroDependentes ());
    objRegTXT.fieldByName ("VR_LUCROSDEPEND").set (objDecl.getRendIsentos ().recuperarTotalLucrosDividendosDep ());
    objRegTXT.fieldByName ("VR_ISENTOSDEPEND").set (objDecl.getRendIsentos ().getRendDependentes ());
    objRegTXT.fieldByName ("VR_EXCLUSIVOSDEPEND").set (objDecl.getRendTributacaoExclusiva ().getRendExcetoDecimoTerceiro ());
    Valor valRendPfExtDep = new Valor ();
    valRendPfExtDep.append ('+', objDecl.getRendPFDependente ().getTotalExterior ());
    valRendPfExtDep.append ('+', objDecl.getRendPFDependente ().getTotalPessoaFisica ());
    objRegTXT.fieldByName ("VR_RENDPFEXT_DEPEND").set (valRendPfExtDep);
    objRegTXT.fieldByName ("VR_LEAO_DEPEND").set (objDecl.getRendPFDependente ().getTotalDarfPago ());
    linha.add (objRegTXT);
    return linha;
  }
  
  public Vector montarFichaResumoSimplificada (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "18");
    objRegTXT.fieldByName ("NR_REG").set ("18");
    objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
    objRegTXT.fieldByName ("VR_RENDTRIB").set (objDecl.getModeloSimplificada ().getTotalResultadosTributaveis ());
    objRegTXT.fieldByName ("VR_DESCSIMP").set (objDecl.getModeloSimplificada ().getDescontoSimplificado ());
    objRegTXT.fieldByName ("VR_BASECALC").set (objDecl.getModeloSimplificada ().getBaseCalculo ());
    objRegTXT.fieldByName ("VR_IMPDEVIDO").set (objDecl.getModeloSimplificada ().getImpostoDevido ());
    Valor totImpostoRetido = new Valor ();
    totImpostoRetido.append ('+', objDecl.getModeloSimplificada ().getImpostoRetidoFonteTitular ());
    totImpostoRetido.append ('+', objDecl.getModeloSimplificada ().getImpostoRetidoFonteDependentes ());
    objRegTXT.fieldByName ("VR_IMPOSTO").set (totImpostoRetido);
    objRegTXT.fieldByName ("VR_LEAO").set (objDecl.getModeloSimplificada ().getCarneLeaoMaisImpostoComplementar ().operacao ('+', objDecl.getImpostoPago ().getImpostoPagoExterior ()));
    objRegTXT.fieldByName ("VR_IRFONTELEI11033").set (objDecl.getModeloSimplificada ().getImpostoRetidoFonteLei11033 ());
    objRegTXT.fieldByName ("VR_RENDTRIBDEPENDENTE").set (objDecl.getColecaoRendPJDependente ().getTotaisRendRecebidoPJ ());
    objRegTXT.fieldByName ("VR_IMPOSTODEPENDENTE").set (objDecl.getModeloSimplificada ().getImpostoRetidoFonteDependentes ());
    if (objDecl.getModeloSimplificada ().getImpostoRestituir ().comparacao (">", "0,00"))
      {
	objRegTXT.fieldByName ("VR_IMPRESTIT").set (objDecl.getModeloSimplificada ().getImpostoRestituir ());
	objRegTXT.fieldByName ("VR_IMPPAGAR").set (ConstantesGlobais.ZERO);
      }
    else if (objDecl.getModeloSimplificada ().getSaldoImpostoPagar ().comparacao (">", "0,00"))
      {
	objRegTXT.fieldByName ("VR_IMPRESTIT").set (ConstantesGlobais.ZERO);
	objRegTXT.fieldByName ("VR_IMPPAGAR").set (objDecl.getModeloSimplificada ().getSaldoImpostoPagar ());
      }
    else
      {
	objRegTXT.fieldByName ("VR_IMPPAGAR").set (ConstantesGlobais.ZERO);
	objRegTXT.fieldByName ("VR_IMPRESTIT").set (ConstantesGlobais.ZERO);
      }
    objRegTXT.fieldByName ("NR_QUOTAS").set (objDecl.getResumo ().getCalculoImposto ().getNumQuotas ().asInteger ());
    objRegTXT.fieldByName ("VR_QUOTA").set (objDecl.getResumo ().getCalculoImposto ().getValorQuota ());
    objRegTXT.fieldByName ("VR_TOTISENTO").set (objDecl.getModeloSimplificada ().getRendIsentosNaoTributaveis ());
    objRegTXT.fieldByName ("VR_TOTEXCLUSIVO").set (objDecl.getModeloSimplificada ().getRendSujeitoTribExclusiva ());
    objRegTXT.fieldByName ("VR_CONJUGE").set (objDecl.getConjuge ().getResultado ());
    objRegTXT.fieldByName ("VR_IMPPAGOESPECIE").set (ConstantesGlobais.ZERO);
    objRegTXT.fieldByName ("VR_TOTRENDTRIBPJTITULAR").set (objDecl.getModeloSimplificada ().getRendRecebidoPJTitular ());
    objRegTXT.fieldByName ("VR_RENDTRIBARURAL").set (objDecl.getModeloSimplificada ().getResultadoTributavelAR ());
    objRegTXT.fieldByName ("VR_TOTFONTETITULAR").set (objDecl.getModeloSimplificada ().getImpostoRetidoFonteTitular ());
    objRegTXT.fieldByName ("VR_TOTBENSANOBASEANTERIOR").set (objDecl.getModeloSimplificada ().getBensDireitosExercicioAnterior ());
    objRegTXT.fieldByName ("VR_TOTBENSANOBASE").set (objDecl.getModeloSimplificada ().getBensDireitosExercicioAtual ());
    objRegTXT.fieldByName ("VR_TOTDIVIDAANOBASEANTERIOR").set (objDecl.getModeloSimplificada ().getDividasExercicioAnterior ());
    objRegTXT.fieldByName ("VR_TOTDIVIDAANOBASE").set (objDecl.getModeloSimplificada ().getDividasExercicioAtual ());
    objRegTXT.fieldByName ("VR_TOTIRFONTELEI11033").set (objDecl.getModeloSimplificada ().calculaImpostoLei11033ComRendaVariavel ());
    objRegTXT.fieldByName ("VR_RENDISENTOTITULAR").set (objDecl.getRendIsentos ().recuperarTotalTitular ());
    objRegTXT.fieldByName ("VR_RENDISENTODEPENDENTES").set (objDecl.getRendIsentos ().getRendDependentes ().operacao ('+', objDecl.getRendIsentos ().recuperarTotalLucrosDividendosDep ()));
    objRegTXT.fieldByName ("VR_TOTRENDEXCLUSTITULAR").set (objDecl.getRendTributacaoExclusiva ().recuperarExclusivosTitular ());
    objRegTXT.fieldByName ("VR_RENDEXCLUSDEPENDENTES").set (objDecl.getRendTributacaoExclusiva ().recuperarExclusivosDependentes ());
    objRegTXT.fieldByName ("VR_RESNAOTRIB_AR").set (objDecl.getRendIsentos ().getParcIsentaAtivRural ());
    objRegTXT.fieldByName ("VR_SUBTOTALISENTOTRANSPORTE").set (objDecl.getRendIsentos ().recuperarSubTotalRendIsentoTransportado ());
    objRegTXT.fieldByName ("VR_SUBTOTALEXCLUSIVOTRANSPORTE").set (objDecl.recuperarSubTotalExclusivoTransporteRendTribExclusiva ());
    objRegTXT.fieldByName ("VR_GANHOLIQUIDORVTRANSPORTE").set (objDecl.recuperarRendaVariavelTribtExclusiva ());
    objRegTXT.fieldByName ("VR_RENDISENTOGCTRANSPORTE").set (objDecl.getRendIsentos ().getLucroAlienacao ());
    Valor totalRend = objDecl.getModeloSimplificada ().getRendRecebidoPF ().operacao ('+', objDecl.getModeloSimplificada ().getRendRecebidoExterior ());
    objRegTXT.fieldByName ("VR_RENDPFEXT").set (totalRend);
    objRegTXT.fieldByName ("VR_DOACOESCAMPANHA").set (objDecl.getResumo ().getOutrasInformacoes ().getTotalDoacoesCampanhasEleitorais ());
    linha.add (objRegTXT);
    return linha;
  }
  
  public Vector montarFichaRendPJSimplificada (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    ColecaoRendPJTitular colecaoRendimentos = objDecl.getColecaoRendPJTitular ();
    for (int i = 0; i < colecaoRendimentos.recuperarLista ().size (); i++)
      {
	RendPJTitular rendimentoPJ = (RendPJTitular) colecaoRendimentos.recuperarLista ().get (i);
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "31");
	objRegTXT.fieldByName ("NR_REG").set ("31");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("NR_PAGADOR").set (rendimentoPJ.getNIFontePagadora ().asString ());
	objRegTXT.fieldByName ("NM_PAGADOR").set (rendimentoPJ.getNomeFontePagadora ().asString ());
	objRegTXT.fieldByName ("VR_RENDTO").set (rendimentoPJ.getRendRecebidoPJ ());
	objRegTXT.fieldByName ("VR_IMPOSTO").set (rendimentoPJ.getImpostoRetidoFonte ());
	linha.add (objRegTXT);
      }
    return linha;
  }
  
  public Vector montarRecibo (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "HR");
    objRegTXT.fieldByName ("NR_REG").set ("HR");
    objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
    linha.add (objRegTXT);
    Contribuinte contribuinte = objDecl.getContribuinte ();
    objRegTXT = new RegistroTxt ("ARQ_IRPF", "DR");
    objRegTXT.fieldByName ("NR_REG").set ("DR");
    objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
    objRegTXT.fieldByName ("IN_COMPLETA").set (objDecl.getIdentificadorDeclaracao ().isCompleta ());
    objRegTXT.fieldByName ("NM_NOME").setLimitado (objDecl.getIdentificadorDeclaracao ().getNome ().asString ());
    if (contribuinte.getExterior ().asString ().equals (Logico.SIM))
      {
	objRegTXT.fieldByName ("NM_LOGRA").setLimitado (contribuinte.getLogradouroExt ().asString ());
	objRegTXT.fieldByName ("NR_NUMERO").setLimitado (contribuinte.getNumeroExt ().asString ());
	objRegTXT.fieldByName ("NM_COMPLEM").setLimitado (contribuinte.getComplementoExt ().asString ());
	objRegTXT.fieldByName ("NM_BAIRRO").setLimitado (contribuinte.getBairroExt ().asString ());
	objRegTXT.fieldByName ("NR_CEP").set (contribuinte.getCepExt ().asString ());
	objRegTXT.fieldByName ("CD_MUNICIP").set ("9701");
	objRegTXT.fieldByName ("NM_MUNICIP").set (contribuinte.getCidade ().getConteudoFormatado ());
	objRegTXT.fieldByName ("SG_UF").set ("EX");
      }
    else
      {
	objRegTXT.fieldByName ("NR_NUMERO").setLimitado (contribuinte.getNumero ().asString ());
	objRegTXT.fieldByName ("NM_COMPLEM").setLimitado (contribuinte.getComplemento ().asString ());
	objRegTXT.fieldByName ("NM_BAIRRO").setLimitado (contribuinte.getBairro ().asString ());
	objRegTXT.fieldByName ("NR_CEP").set (contribuinte.getCep ().asString ());
	objRegTXT.fieldByName ("TIP_LOGRA").setLimitado (contribuinte.getTipoLogradouro ().asString ());
	objRegTXT.fieldByName ("NM_LOGRA").setLimitado (contribuinte.getLogradouro ().asString ());
	objRegTXT.fieldByName ("CD_MUNICIP").set (contribuinte.getMunicipio ().getConteudoAtual (0));
	objRegTXT.fieldByName ("NM_MUNICIP").set (contribuinte.getMunicipio ().getConteudoAtual (1));
	objRegTXT.fieldByName ("SG_UF").set (contribuinte.getUf ().getConteudoAtual (0));
      }
    objRegTXT.fieldByName ("NR_DDD_TELEFONE").set (contribuinte.getDdd ().asString ());
    objRegTXT.fieldByName ("NR_TELEFONE").set (contribuinte.getTelefone ().asString ());
    objRegTXT.fieldByName ("IN_RETIFICADORA").set (objDecl.getIdentificadorDeclaracao ().isRetificadora () ? "S" : "N");
    objRegTXT.fieldByName ("VR_TOTTRIB").set (objDecl.getModelo ().recuperarTotalRendimentosTributaveis ());
    objRegTXT.fieldByName ("VR_IMPDEV").set (objDecl.getModelo ().getImpostoDevido ());
    if (objDecl.getModelo ().getSaldoImpostoPagar ().comparacao (">", "0,00"))
      {
	objRegTXT.fieldByName ("VR_IMPPAGAR").set (objDecl.getModelo ().getSaldoImpostoPagar ());
	objRegTXT.fieldByName ("VR_IMPREST").set (0);
      }
    else if (objDecl.getModelo ().getImpostoRestituir ().comparacao (">", "0,00"))
      {
	objRegTXT.fieldByName ("VR_IMPPAGAR").set (0);
	objRegTXT.fieldByName ("VR_IMPREST").set (objDecl.getModelo ().getImpostoRestituir ());
      }
    objRegTXT.fieldByName ("NR_QUOTAS").set (objDecl.getResumo ().getCalculoImposto ().getNumQuotas ().asInteger ());
    objRegTXT.fieldByName ("VR_QUOTA").set (objDecl.getResumo ().getCalculoImposto ().getValorQuota ());
    objRegTXT.fieldByName ("NR_BANCO").set (objDecl.getResumo ().getCalculoImposto ().getBanco ().getConteudoAtual (0));
    objRegTXT.fieldByName ("NR_AGENCIA").set (objDecl.getResumo ().getCalculoImposto ().getAgencia ().getConteudoFormatado ());
    objRegTXT.fieldByName ("NR_DV_AGENCIA").set (objDecl.getResumo ().getCalculoImposto ().getDvAgencia ().getConteudoFormatado ());
    objRegTXT.fieldByName ("VR_GCIMPOSTOPAGO").set (0);
    objRegTXT.fieldByName ("NR_CONTA").set (objDecl.getResumo ().getCalculoImposto ().getContaCredito ().getConteudoFormatado ());
    objRegTXT.fieldByName ("NR_DV_CONTA").set (objDecl.getResumo ().getCalculoImposto ().getDvContaCredito ().getConteudoFormatado ());
    objRegTXT.fieldByName ("VR_VCMOEDAEST").set (0);
    linha.add (objRegTXT);
    objRegTXT = new RegistroTxt ("ARQ_IRPF", "R9");
    objRegTXT.fieldByName ("NR_REG").set ("R9");
    objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
    linha.add (objRegTXT);
    return linha;
  }
  
  public Vector montarFichaRendaVariavel (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    RendaVariavel rendaVariavel = objDecl.getRendaVariavel ();
    for (int i = 1; i <= 12; i++)
      {
	GanhosLiquidosOuPerdas ganhoLiquidoOuPerda = rendaVariavel.getGanhosPorIndice (i - 1);
	if (! ganhoLiquidoOuPerda.estaVazio ())
	  {
	    RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "40");
	    objRegTXT.fieldByName ("NR_REG").set ("40");
	    objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	    objRegTXT.fieldByName ("NR_MES").set (rendaVariavel.obterMesFormatoNumerico (ganhoLiquidoOuPerda));
	    Operacoes opComuns = ganhoLiquidoOuPerda.getOperacoesComuns ();
	    objRegTXT.fieldByName ("VR_COMUM_MVISTA_ACOES").set (opComuns.getMercadoVistaAcoes ());
	    objRegTXT.fieldByName ("VR_COMUM_MVISTA_OURO").set (opComuns.getMercadoVistaOuro ());
	    objRegTXT.fieldByName ("VR_COMUM_MVISTA_OUROFORA").set (opComuns.getMercadoVistaForaBolsa ());
	    objRegTXT.fieldByName ("VR_COMUM_MOPC_ACOES").set (opComuns.getMercadoOpcoesAcoes ());
	    objRegTXT.fieldByName ("VR_COMUM_MOPC_OURO").set (opComuns.getMercadoOpcoesOuro ());
	    objRegTXT.fieldByName ("VR_COMUM_MOPC_OUROFORA").set (opComuns.getMercadoOpcoesForaDeBolsa ());
	    objRegTXT.fieldByName ("VR_COMUM_MOPC_OUTROS").set (opComuns.getMercadoOpcoesOutros ());
	    objRegTXT.fieldByName ("VR_COMUM_MFUT_DOLAR").set (opComuns.getMercadoFuturoDolar ());
	    objRegTXT.fieldByName ("VR_COMUM_MFUT_INDICES").set (opComuns.getMercadoFuturoIndices ());
	    objRegTXT.fieldByName ("VR_COMUM_MFUT_JUROS").set (opComuns.getMercadoFuturoJuros ());
	    objRegTXT.fieldByName ("VR_COMUM_MFUT_OUTROS").set (opComuns.getMercadoFuturoOutros ());
	    objRegTXT.fieldByName ("VR_COMUM_MTERMO_ACOESOURO").set (opComuns.getMercadoTermoAcoes ());
	    objRegTXT.fieldByName ("VR_COMUM_MTERMO_OUTROS").set (opComuns.getMercadoTermoOutros ());
	    Operacoes opDayTrade = ganhoLiquidoOuPerda.getOperacoesDayTrade ();
	    objRegTXT.fieldByName ("VR_DAYTR_MVISTA_ACOES").set (opDayTrade.getMercadoVistaAcoes ());
	    objRegTXT.fieldByName ("VR_DAYTR_MVISTA_OURO").set (opDayTrade.getMercadoVistaOuro ());
	    objRegTXT.fieldByName ("VR_DAYTR_MVISTA_OUROFORA").set (opDayTrade.getMercadoVistaForaBolsa ());
	    objRegTXT.fieldByName ("VR_DAYTR_MOPC_ACOES").set (opDayTrade.getMercadoOpcoesAcoes ());
	    objRegTXT.fieldByName ("VR_DAYTR_MOPC_OURO").set (opDayTrade.getMercadoOpcoesOuro ());
	    objRegTXT.fieldByName ("VR_DAYTR_MOPC_OUROFORA").set (opDayTrade.getMercadoOpcoesForaDeBolsa ());
	    objRegTXT.fieldByName ("VR_DAYTR_MOPC_OUTROS").set (opDayTrade.getMercadoOpcoesOutros ());
	    objRegTXT.fieldByName ("VR_DAYTR_MFUT_DOLAR").set (opDayTrade.getMercadoFuturoDolar ());
	    objRegTXT.fieldByName ("VR_DAYTR_MFUT_INDICES").set (opDayTrade.getMercadoFuturoIndices ());
	    objRegTXT.fieldByName ("VR_DAYTR_MFUT_JUROS").set (opDayTrade.getMercadoFuturoJuros ());
	    objRegTXT.fieldByName ("VR_DAYTR_MFUT_OUTROS").set (opDayTrade.getMercadoFuturoOutros ());
	    objRegTXT.fieldByName ("VR_DAYTR_MTERMO_ACOESOURO").set (opDayTrade.getMercadoTermoAcoes ());
	    objRegTXT.fieldByName ("VR_DAYTR_MTERMO_OUTROS").set (opDayTrade.getMercadoTermoOutros ());
	    objRegTXT.fieldByName ("VR_FONTE_DAYTRADE").set (ganhoLiquidoOuPerda.getIrFonteDayTradeMesAtual ());
	    objRegTXT.fieldByName ("VR_IMPOSTO_PAGO").set (ganhoLiquidoOuPerda.getImpostoPago ());
	    objRegTXT.fieldByName ("VR_IMPRENDAFONTE").set (ganhoLiquidoOuPerda.getImpostoRetidoFonteLei11033 ());
	    objRegTXT.fieldByName ("VR_RESULTNEG_MESANT_COMUM").set (opComuns.getResultadoNegativoMesAnterior ());
	    objRegTXT.fieldByName ("VR_RESULTNEG_MESANT_DAYTR").set (opDayTrade.getResultadoNegativoMesAnterior ());
	    objRegTXT.fieldByName ("VR_RESULTLIQ_MES_COMUM").set (opComuns.getResultadoLiquidoMes ());
	    objRegTXT.fieldByName ("VR_RESULTLIQ_MES_DAYTR").set (opDayTrade.getResultadoLiquidoMes ());
	    objRegTXT.fieldByName ("VR_BASECALCULO_MES_COMUM").set (opComuns.getBaseCalculoImposto ());
	    objRegTXT.fieldByName ("VR_BASECALCULO_MES_DAYTR").set (opDayTrade.getBaseCalculoImposto ());
	    objRegTXT.fieldByName ("VR_PREJACOMPENS_MES_COMUM").set (opComuns.getPrejuizoCompensar ());
	    objRegTXT.fieldByName ("VR_PREJACOMPENS_MES_DAYTR").set (opDayTrade.getPrejuizoCompensar ());
	    objRegTXT.fieldByName ("VR_ALIQUOTA_IMPOSTO_COMUM").set (new Valor ("15"));
	    objRegTXT.fieldByName ("VR_ALIQUOTA_IMPOSTO_DAYTRADE").set (new Valor ("20"));
	    objRegTXT.fieldByName ("VR_IMPOSTODEVIDO_MES_COMUM").set (opComuns.getImpostoDevido ());
	    objRegTXT.fieldByName ("VR_IMPOSTODEVIDO_MES_DAYTR").set (opDayTrade.getImpostoDevido ());
	    objRegTXT.fieldByName ("VR_TOTAL_IMPOSTODEVIDO").set (ganhoLiquidoOuPerda.getTotalImpostoDevido ());
	    objRegTXT.fieldByName ("VR_IRFONTE_MESESANT_DAYTR").set (ganhoLiquidoOuPerda.getIrFonteDayTradeMesesAnteriores ());
	    objRegTXT.fieldByName ("VR_IRFONTE_ACOMPENS_DAYTR").set (ganhoLiquidoOuPerda.getIrFonteDayTradeAcompensar ());
	    objRegTXT.fieldByName ("VR_TOTAL_IMPOSTOAPAGAR").set (ganhoLiquidoOuPerda.getImpostoApagar ());
	    linha.add (objRegTXT);
	  }
      }
    return linha;
  }
  
  public Vector montarFichaRendaVariavelAnual (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    RendaVariavel colecaoRendaVariavel = objDecl.getRendaVariavel ();
    Hashtable tabelaTotais = colecaoRendaVariavel.obterTotalAnual ();
    RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "41");
    objRegTXT.fieldByName ("NR_REG").set ("41");
    objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
    objRegTXT.fieldByName ("VR_RESULTLIQUIDO").set ((Valor) tabelaTotais.get ("TotalResultadosLiquidos"));
    objRegTXT.fieldByName ("VR_RESULTNEGMESANTERIOR").set ((Valor) tabelaTotais.get ("TotalResultadosNegativos"));
    objRegTXT.fieldByName ("VR_BASECALCULO").set ((Valor) tabelaTotais.get ("BaseCalculoImposto"));
    objRegTXT.fieldByName ("VR_PREJUIZOCOMPENSAR").set ((Valor) tabelaTotais.get ("PrejuizoCompensar"));
    objRegTXT.fieldByName ("VR_IMPOSTODEVIDO").set ((Valor) tabelaTotais.get ("ImpostoDevido"));
    objRegTXT.fieldByName ("VR_CONSOL_IMPOSTOIMPOSTODEVIDO").set ((Valor) tabelaTotais.get ("ImpostoDevidoConsolidacao"));
    objRegTXT.fieldByName ("VR_CONSOL_IRFONTEDAYTRMESANT").set ((Valor) tabelaTotais.get ("IRDayTradeMesesAnteriores"));
    objRegTXT.fieldByName ("VR_CONSOL_IRFONTEDAYTRCOMPENSAR").set ((Valor) tabelaTotais.get ("IRDayTradeCompensar"));
    objRegTXT.fieldByName ("VR_TOTALANUALIRFONTELEI11033").set (objDecl.getRendaVariavel ().getTotalImpostoRetidoFonteLei11033 ());
    objRegTXT.fieldByName ("VR_CONSOL_IMPOSTOAPAGAR").set ((Valor) tabelaTotais.get ("TotalImpostoAPagar"));
    linha.add (objRegTXT);
    return linha;
  }
  
  public Vector montarFichaRendaVariavelFII (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    NumberFormat numFormat = null;
    RendaVariavel rendaVariavel = objDecl.getRendaVariavel ();
    for (int i = 1; i <= 12; i++)
      {
	MesFundosInvestimentos fundoInvest = rendaVariavel.getFundInvest ().getMeses ()[i - 1];
	if (! fundoInvest.isVazio ())
	  {
	    RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "42");
	    objRegTXT.fieldByName ("NR_REG").set ("42");
	    objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	    numFormat = NumberFormat.getInstance ();
	    numFormat.setMaximumFractionDigits (0);
	    numFormat.setMinimumIntegerDigits (2);
	    numFormat.setGroupingUsed (false);
	    objRegTXT.fieldByName ("NR_MES").set (numFormat.format ((long) i));
	    objRegTXT.fieldByName ("VR_RESLIQUIDO_MES").set (fundoInvest.getResultLiquidoMes ());
	    objRegTXT.fieldByName ("VR_RESULT_NEG_MESANT").set (fundoInvest.getResultNegativoAnterior ());
	    objRegTXT.fieldByName ("VR_BASECALCULO_MES").set (fundoInvest.getBaseCalcImposto ());
	    objRegTXT.fieldByName ("VR_PREJACOMPENSAR_MES_OPCOMUNS").set (fundoInvest.getPrejuizoCompensar ());
	    objRegTXT.fieldByName ("VR_ALIQUOTA_IMPOSTO_OPCOMUNS").set (fundoInvest.getAliquotaImposto ());
	    objRegTXT.fieldByName ("VR_IMPOSTODEVIDO_MES_OPCOMUNS").set (fundoInvest.getImpostoDevido ());
	    objRegTXT.fieldByName ("VR_IMPOSTOPAGO").set (fundoInvest.getImpostoPago ());
	    linha.add (objRegTXT);
	  }
      }
    return linha;
  }
  
  public Vector montarFichaRendaVariavelTotaisFII (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    FundosInvestimentos fundoInvest = objDecl.getRendaVariavel ().getFundInvest ();
    if (! fundoInvest.isVazio ())
      {
	java.util.Map map = fundoInvest.obterTotalAnual ();
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "43");
	objRegTXT.fieldByName ("NR_REG").set ("43");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("VR_TOTALANUALRESULTADOLIQUIDOSRENDAVARIAVEL_FII").set ((Valor) map.get ("VR_TOTALANUALRESULTADOLIQUIDOSRENDAVARIAVEL_FII"));
	objRegTXT.fieldByName ("VR_TOTALANUALRESULTADONEGATIVOMESANTERIOR_FII").set ((Valor) map.get ("VR_TOTALANUALRESULTADONEGATIVOMESANTERIOR_FII"));
	objRegTXT.fieldByName ("VR_TOTALANUALBASECALCULOIMPOSTO_FII").set ((Valor) map.get ("VR_TOTALANUALBASECALCULOIMPOSTO_FII"));
	objRegTXT.fieldByName ("VR_TOTALANUALPREJUIZOCOMPENSAR_FII").set ((Valor) map.get ("VR_TOTALANUALPREJUIZOCOMPENSAR_FII"));
	objRegTXT.fieldByName ("VR_TOTALANUALIMPOSTODEVIDO_FII").set ((Valor) map.get ("VR_TOTALANUALIMPOSTODEVIDO_FII"));
	objRegTXT.fieldByName ("VR_TOTALANUALIMPOSTOPAGAR_FII").set ((Valor) map.get ("VR_TOTALANUALIMPOSTOPAGAR_FII"));
	linha.add (objRegTXT);
      }
    return linha;
  }
  
  public Vector montarFichaAtividadeRuralIdentificacaoImovel (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    ARBrasil arBrasil = objDecl.getAtividadeRural ().getBrasil ();
    arBrasil.getIdentificacaoImovel ().excluirRegistrosEmBranco ();
    Iterator itImoveis = arBrasil.getIdentificacaoImovel ().recuperarLista ().iterator ();
    while (itImoveis.hasNext ())
      {
	ImovelARBrasil imovel = (ImovelARBrasil) itImoveis.next ();
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "50");
	objRegTXT.fieldByName ("NR_REG").set ("50");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("IN_EXTERIOR").set (0);
	objRegTXT.fieldByName ("NR_INCRA").set (imovel.getNirf ().asString ());
	objRegTXT.fieldByName ("NM_IMOVEL").set (imovel.getNome ().asString ());
	objRegTXT.fieldByName ("NM_LOCAL").set (imovel.getLocalizacao ().asString ());
	objRegTXT.fieldByName ("QT_AREA").set (imovel.getArea ());
	objRegTXT.fieldByName ("PC_PARTIC").set (imovel.getParticipacao ());
	objRegTXT.fieldByName ("CD_EXPLOR").set (imovel.getCondicaoExploracao ().getConteudoAtual (0));
	objRegTXT.fieldByName ("CD_ATIV").set (imovel.getCodigo ().getConteudoAtual (0));
	linha.add (objRegTXT);
      }
    ARExterior arExterior = objDecl.getAtividadeRural ().getExterior ();
    arExterior.getIdentificacaoImovel ().excluirRegistrosEmBranco ();
    itImoveis = arExterior.getIdentificacaoImovel ().recuperarLista ().iterator ();
    while (itImoveis.hasNext ())
      {
	ImovelAR imovel = (ImovelAR) itImoveis.next ();
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "50");
	objRegTXT.fieldByName ("NR_REG").set ("50");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("IN_EXTERIOR").set (1);
	objRegTXT.fieldByName ("NM_IMOVEL").set (imovel.getNome ().asString ());
	objRegTXT.fieldByName ("NM_LOCAL").set (imovel.getLocalizacao ().asString ());
	objRegTXT.fieldByName ("QT_AREA").set (imovel.getArea ());
	objRegTXT.fieldByName ("PC_PARTIC").set (imovel.getParticipacao ());
	objRegTXT.fieldByName ("CD_EXPLOR").set (imovel.getCondicaoExploracao ().getConteudoAtual (0));
	objRegTXT.fieldByName ("CD_ATIV").set (imovel.getCodigo ().asString ());
	linha.add (objRegTXT);
      }
    return linha;
  }
  
  public Vector montarFichaAtividadeRuralReceitasDespesasBrasil (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    ReceitasDespesas receitasDespesas = objDecl.getAtividadeRural ().getBrasil ().getReceitasDespesas ();
    for (int i = 0; i <= 11; i++)
      {
	MesReceitaDespesa receita = receitasDespesas.getMesReceitaPorIndice (i);
	if (! receita.isVazio ())
	  {
	    RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "51");
	    objRegTXT.fieldByName ("NR_REG").set ("51");
	    objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	    objRegTXT.fieldByName ("IN_EXTERIOR").set (0);
	    objRegTXT.fieldByName ("NR_MES").set (receitasDespesas.obterMesFormatoNumerico (receita));
	    objRegTXT.fieldByName ("VR_DESP").set (receita.getDespesaCusteioInvestimento ());
	    objRegTXT.fieldByName ("VR_REC").set (receita.getReceitaBrutaMensal ());
	    linha.add (objRegTXT);
	  }
      }
    return linha;
  }
  
  public Vector montarFichaAtividadeRuralApuracaoResultado (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    AtividadeRural atividadeRural = objDecl.getAtividadeRural ();
    ApuracaoResultadoBrasil apuracaoBR = atividadeRural.getBrasil ().getApuracaoResultado ();
    if (! apuracaoBR.isVazio ())
      {
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "52");
	objRegTXT.fieldByName ("NR_REG").set ("52");
	objRegTXT.fieldByName ("IN_EXTERIOR").set (0);
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("VR_RECTOTAL").set (apuracaoBR.getReceitaBrutaTotal ());
	objRegTXT.fieldByName ("VR_DESPTOTAL").set (apuracaoBR.getDespesaCusteio ());
	objRegTXT.fieldByName ("VR_RES1REAL").set (apuracaoBR.getResultadoI ());
	objRegTXT.fieldByName ("VR_PREJEXERCANT").set (apuracaoBR.getPrejuizoExercicioAnterior ());
	objRegTXT.fieldByName ("VR_RESAPOS").set (apuracaoBR.getResultadoAposCompensacaoPrejuizo ());
	objRegTXT.fieldByName ("VR_OPCAO").set (apuracaoBR.getOpcaoArbitramento ());
	objRegTXT.fieldByName ("VR_RESTRIB").set (apuracaoBR.getResultadoTributavel ());
	objRegTXT.fieldByName ("VR_PREJUIZO").set (apuracaoBR.getPrejuizoCompensar ());
	objRegTXT.fieldByName ("VR_RECVENDAFUTURA").set (apuracaoBR.getReceitaRecebidaContaVenda ());
	objRegTXT.fieldByName ("VR_ADIANT").set (apuracaoBR.getValorAdiantamento ());
	objRegTXT.fieldByName ("VR_RESNAOTRIBAR").set (apuracaoBR.getResultadoNaoTributavel ());
	objRegTXT.fieldByName ("VR_RES1DOLAR").set (0);
	linha.add (objRegTXT);
      }
    ApuracaoResultadoExterior apuracaoEx = atividadeRural.getExterior ().getApuracaoResultado ();
    if (! apuracaoEx.isVazio ())
      {
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "52");
	objRegTXT.fieldByName ("NR_REG").set ("52");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("IN_EXTERIOR").set (1);
	objRegTXT.fieldByName ("VR_RECTOTAL").set (0);
	objRegTXT.fieldByName ("VR_DESPTOTAL").set (0);
	objRegTXT.fieldByName ("VR_RES1REAL").set (apuracaoEx.getResultadoI_EmReais ());
	objRegTXT.fieldByName ("VR_PREJEXERCANT").set (apuracaoEx.getPrejuizoExercicioAnterior ());
	objRegTXT.fieldByName ("VR_RESAPOS").set (apuracaoEx.getResultadoAposCompensacaoPrejuizo ());
	objRegTXT.fieldByName ("VR_OPCAO").set (apuracaoEx.getOpcaoArbitramento ());
	objRegTXT.fieldByName ("VR_RESTRIB").set (apuracaoEx.getResultadoTributavel ());
	objRegTXT.fieldByName ("VR_PREJUIZO").set (apuracaoEx.getPrejuizoCompensar ());
	objRegTXT.fieldByName ("VR_RECVENDAFUTURA").set (apuracaoEx.getReceitaRecebidaContaVenda ());
	objRegTXT.fieldByName ("VR_ADIANT").set (apuracaoEx.getValorAdiantamento ());
	objRegTXT.fieldByName ("VR_RESNAOTRIBAR").set (apuracaoEx.getResultadoNaoTributavel ());
	objRegTXT.fieldByName ("VR_RES1DOLAR").set (apuracaoEx.getResultadoI_EmDolar ());
	linha.add (objRegTXT);
      }
    return linha;
  }
  
  private void setarValoresTipoDadoMovimentacaoRebanho (ItemMovimentacaoRebanho tipoDado, RegistroTxt pReg) throws GeracaoTxtException
  {
    pReg.fieldByName ("QT_INIC").set (tipoDado.getEstoqueInicial ());
    pReg.fieldByName ("QT_COMPRA").set (tipoDado.getAquisicoesAno ());
    pReg.fieldByName ("QT_NASCIM").set (tipoDado.getNascidosAno ());
    pReg.fieldByName ("QT_PERDA").set (tipoDado.getConsumo ());
    pReg.fieldByName ("QT_VENDA").set (tipoDado.getVendas ());
    pReg.fieldByName ("QT_ESTFINAL").set (tipoDado.getEstoqueFinal ());
  }
  
  private void setarMovimentacaoRebanho (DeclaracaoIRPF objDecl, RegistroTxt objRegTXT, Vector linha, int pExterior) throws GeracaoTxtException
  {
    AtividadeRural atividadeRural = objDecl.getAtividadeRural ();
    MovimentacaoRebanho movBr;
    if (pExterior == 0)
      movBr = atividadeRural.getBrasil ().getMovimentacaoRebanho ();
    else
      movBr = atividadeRural.getExterior ().getMovimentacaoRebanho ();
    if (! movBr.getBovinos ().isVazio ())
      {
	objRegTXT = new RegistroTxt ("ARQ_IRPF", "53");
	objRegTXT.fieldByName ("NR_REG").set ("53");
	objRegTXT.fieldByName ("IN_EXTERIOR").set (pExterior);
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("CD_ESPEC").set (1);
	setarValoresTipoDadoMovimentacaoRebanho (movBr.getBovinos (), objRegTXT);
	linha.add (objRegTXT);
      }
    if (! movBr.getSuinos ().isVazio ())
      {
	objRegTXT = new RegistroTxt ("ARQ_IRPF", "53");
	objRegTXT.fieldByName ("NR_REG").set ("53");
	objRegTXT.fieldByName ("IN_EXTERIOR").set (pExterior);
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("CD_ESPEC").set (2);
	setarValoresTipoDadoMovimentacaoRebanho (movBr.getSuinos (), objRegTXT);
	linha.add (objRegTXT);
      }
    if (! movBr.getCaprinos ().isVazio ())
      {
	objRegTXT = new RegistroTxt ("ARQ_IRPF", "53");
	objRegTXT.fieldByName ("NR_REG").set ("53");
	objRegTXT.fieldByName ("IN_EXTERIOR").set (pExterior);
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("CD_ESPEC").set (3);
	setarValoresTipoDadoMovimentacaoRebanho (movBr.getCaprinos (), objRegTXT);
	linha.add (objRegTXT);
      }
    if (! movBr.getAsininos ().isVazio ())
      {
	objRegTXT = new RegistroTxt ("ARQ_IRPF", "53");
	objRegTXT.fieldByName ("NR_REG").set ("53");
	objRegTXT.fieldByName ("IN_EXTERIOR").set (pExterior);
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("CD_ESPEC").set (4);
	setarValoresTipoDadoMovimentacaoRebanho (movBr.getAsininos (), objRegTXT);
	linha.add (objRegTXT);
      }
    if (! movBr.getOutros ().isVazio ())
      {
	objRegTXT = new RegistroTxt ("ARQ_IRPF", "53");
	objRegTXT.fieldByName ("NR_REG").set ("53");
	objRegTXT.fieldByName ("IN_EXTERIOR").set (pExterior);
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("CD_ESPEC").set (5);
	setarValoresTipoDadoMovimentacaoRebanho (movBr.getOutros (), objRegTXT);
	linha.add (objRegTXT);
      }
  }
  
  public Vector montarFichaAtividadeRuralMovimentacaoRebanho (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    RegistroTxt objRegTXT = null;
    if (! objDecl.getAtividadeRural ().getBrasil ().getMovimentacaoRebanho ().isVazio ())
      setarMovimentacaoRebanho (objDecl, objRegTXT, linha, 0);
    if (! objDecl.getAtividadeRural ().getExterior ().getMovimentacaoRebanho ().isVazio ())
      setarMovimentacaoRebanho (objDecl, objRegTXT, linha, 1);
    return linha;
  }
  
  public Vector montarFichaAtividadeRuralBens (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    setarBensAR (objDecl, 0, linha);
    setarBensAR (objDecl, 1, linha);
    return linha;
  }
  
  public void setarBensAR (DeclaracaoIRPF objDecl, int pExterior, Vector linha) throws GeracaoTxtException
  {
    AtividadeRural atividadeRural = objDecl.getAtividadeRural ();
    Iterator itBens = null;
    if (pExterior == 0)
      {
	atividadeRural.getBrasil ().getBens ().excluirRegistrosEmBranco ();
	itBens = atividadeRural.getBrasil ().getBens ().recuperarLista ().iterator ();
      }
    else
      {
	atividadeRural.getExterior ().getBens ().excluirRegistrosEmBranco ();
	itBens = atividadeRural.getExterior ().getBens ().recuperarLista ().iterator ();
      }
    while (itBens.hasNext ())
      {
	BemAR bem = (BemAR) itBens.next ();
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "54");
	objRegTXT.fieldByName ("NR_REG").set ("54");
	objRegTXT.fieldByName ("IN_EXTERIOR").set (pExterior);
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	if (pExterior == 0)
	  objRegTXT.fieldByName ("CD_PAIS").set ("000");
	else
	  objRegTXT.fieldByName ("CD_PAIS").set (((BemARExterior) bem).getPais ().getConteudoAtual (0));
	objRegTXT.fieldByName ("CD_BEMAR").set (bem.getCodigo ().getConteudoAtual (0));
	objRegTXT.fieldByName ("TX_BEM").set (bem.getDiscriminacao ().getConteudoFormatado ());
	objRegTXT.fieldByName ("VR_BEM").set (bem.getValor ());
	linha.add (objRegTXT);
      }
  }
  
  public Vector montarFichaAtividadeRuralDividas (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    RegistroTxt objRegTXT = null;
    setarDividasAR (objDecl, 0, linha);
    setarDividasAR (objDecl, 1, linha);
    return linha;
  }
  
  public void setarDividasAR (DeclaracaoIRPF objDecl, int pExterior, Vector linha) throws GeracaoTxtException
  {
    AtividadeRural atividadeRural = objDecl.getAtividadeRural ();
    Iterator itDividas = null;
    if (pExterior == 0)
      {
	atividadeRural.getBrasil ().getDividas ().excluirRegistrosEmBranco ();
	itDividas = atividadeRural.getBrasil ().getDividas ().recuperarLista ().iterator ();
      }
    else
      {
	atividadeRural.getExterior ().getDividas ().excluirRegistrosEmBranco ();
	itDividas = atividadeRural.getExterior ().getDividas ().recuperarLista ().iterator ();
      }
    while (itDividas.hasNext ())
      {
	DividaAR divida = (DividaAR) itDividas.next ();
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "55");
	objRegTXT.fieldByName ("NR_REG").set ("55");
	objRegTXT.fieldByName ("IN_EXTERIOR").set (pExterior);
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("TX_DIVIDA").set (divida.getDiscriminacao ().getConteudoFormatado ());
	objRegTXT.fieldByName ("VR_DIVATE").set (divida.getContraidasAteExercicioAnterior ());
	objRegTXT.fieldByName ("VR_DIVATU").set (divida.getContraidasAteExercicioAtual ());
	objRegTXT.fieldByName ("VR_DIVPAG").set (divida.getEfetivamentePagas ());
	linha.add (objRegTXT);
      }
  }
  
  public Vector montarFichaAtividadeRuralReceitasDespesasExterior (DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    Vector linha = new Vector ();
    AtividadeRural atividadeRural = objDecl.getAtividadeRural ();
    atividadeRural.getExterior ().getReceitasDespesas ().excluirRegistrosEmBranco ();
    Iterator itReceitasExterior = atividadeRural.getExterior ().getReceitasDespesas ().recuperarLista ().iterator ();
    while (itReceitasExterior.hasNext ())
      {
	ReceitaDespesa receita = (ReceitaDespesa) itReceitasExterior.next ();
	RegistroTxt objRegTXT = new RegistroTxt ("ARQ_IRPF", "56");
	objRegTXT.fieldByName ("NR_REG").set ("56");
	objRegTXT.fieldByName ("NR_CPF").set (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
	objRegTXT.fieldByName ("CD_PAIS").set (receita.getPais ().getConteudoAtual (0));
	objRegTXT.fieldByName ("DESPCUSTEIO").set (receita.getDespesaCusteio ());
	objRegTXT.fieldByName ("RECBRUTA").set (receita.getReceitaBruta ());
	objRegTXT.fieldByName ("RESDOLAR").set (receita.getResultadoI_EmDolar ());
	objRegTXT.fieldByName ("RESORIGINAL").set (receita.getResultadoIMoedaOriginal ());
	linha.add (objRegTXT);
      }
    return linha;
  }
}
