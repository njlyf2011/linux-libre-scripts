/* ConstantesGlobais - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import java.util.Locale;

import serpro.ppgd.negocio.util.FabricaUtilitarios;

public class ConstantesGlobais
{
  private static int EXERCICIO_INT;
  private static int _TAMANHO_VALOR = 13;
  private static int _TAMANHO_VALOR_PARTE_DECIMAL = 2;
  public static final int TAMANHO_VALOR;
  public static final int TAMANHO_VALOR_PARTE_DECIMAL;
  public static final String EXERCICIO;
  public static final Locale LOCALIDADE;
  public static final String DECIMAL_SEPARATOR = ".";
  public static final boolean VERSAO_BETA = false;
  public static final String PAIS_BRASIL = "105";
  public static final String CD_MUNICIPIO_BRASILIA = "9701";
  public static final String ENDERECO_CORREIOS = "http://www.correios.com.br/servicos/cep/cep_default.cfm";
  public static final String CODIGO_NATUREZA_OCUPACAO_ESPOLIO = "81";
  public static final Valor ZERO;
  public static final Valor ALIQUOTA_TRIBUTACAO_1;
  public static final Valor ALIQUOTA_TRIBUTACAO_2;
  public static final Valor LIMITE_ISENCAO;
  public static final Valor FAIXA_TRIBUTACAO_1;
  public static final Valor DEDUCAO_FAIXA_1;
  public static final Valor DEDUCAO_FAIXA_2;
  public static final Valor DEDUCAO_FAIXA_1_MENSAL;
  public static final Valor DEDUCAO_FAIXA_2_MENSAL;
  public static final Valor DEDUCAO_DEPENDENTE;
  public static final Valor DEDUCAO_INSTRUCAO;
  public static final Valor DESCONTO_PADRAO;
  public static final Valor DOLAR_31_12_ANO_ANTERIOR;
  public static final String TIPO_IMOVEL_EXPLORA_APICULTURA = "13";
  public static final String LIMITE_RECEITA_BRUTA_AR = "63.480,00";
  public static final String TIPO_IMOVEL_CULTURA_PEQUENOS = "14";
  public static final Valor MULTA_POR_ATRASO_ENTREGA;
  public static final String SEPARADORDEEXTENSAO = ".";
  public static final String TIPO_IMOVEL_OUTRAS = "99";
  public static final String EXTENSAO_ARQ_DECLARACAO = ".DEC";
  public static final String EXTENSAO_COPIA_SEGURA = ".DBK";
  public static final String EXTENSAO_COMPL_RECIBO = ".REC";
  public static final String EXTENSAO_BACKUP_ANO_ANTERIOR = ".F2B";
  public static final String EXTENSAOANOANTERIOR = "F";
  public static final int NR_MAX_QUOTAS = 6;
  public static final Valor VALOR_MINIMO_QUOTA;
  public static final String DATA_PRIMEIRA_QUOTA;
  public static final String DATA_SEGUNDA_QUOTA;
  public static final String DATA_TERCEIRA_QUOTA;
  public static final String DATA_QUARTA_QUOTA;
  public static final String DATA_QUINTA_QUOTA;
  public static final String DATA_SEXTA_QUOTA;
  final String AVISO_PAGAMENTO_QUOTA = "O pagamento da 1a. quota ou quota \u00fanica at\u00e9 a data de vencimento n\u00e3o sofre qualquer acr\u00e9scimo. Nas demais quotas, mesmo se recolhidas no prazo legal, ser\u00e3o acrescidos juros SELIC calculados de 01/05/" + EXERCICIO + " at\u00e9 o " + "m\u00eas anterior ao do pagamento e 1%% no m\u00eas do pagamento.";
  public static final String EXT_BANCO = "001";
  public static final String EXT_AGENCIA = "0686";
  public static final String EXT_DVAGENCIA = "6";
  public static final String NOME_PROGRAMA;
  public static final String NOME_PROGRAMA_EXTENSO;
  public static final String NOME_SRF = "Secretaria da Receita Federal";
  public static final String TIPO_DECLARACAO = "DECLARA\u00c7\u00c3O DE AJUSTE ANUAL";
  public static final String URL_SRF = "http://www.receita.fazenda.gov.br";
  public static final String URL_LEAOZINHO = "http://leaozinho.receita.fazenda.gov.br";
  public static final String XMLNS;
  public static final String VERSAO = "000";
  public static final String EXERCICIO_ANTERIOR;
  public static final String EXERCICIO_POSTERIOR;
  public static final String ANO_BASE;
  public static final String ANO_BASE_ANTERIOR;
  public static final String ANO_BASE_SEGUINTE;
  public static final String ENDERECO_GATEWAY;
  public static final String ENDERECO_GATEWAY_TESTE;
  public static final String PREFIXO_ENDERECO_HELP_ONLINE;
  public static final String PATH_RELATIVO_HELP = "ajusteAnual/index.htm";
  public static final int CODIGO_RECNET = 1500;
  public static final int VERSAORECNET;
  public static final String FILENAME_PROPERTIES_APLICACAO = "/aplicacao.properties";
  public static final String JANEIRO = "Janeiro";
  public static final String FEVEREIRO = "Fevereiro";
  public static final String MARCO = "Mar\u00e7o";
  public static final String ABRIL = "Abril";
  public static final String MAIO = "Maio";
  public static final String JUNHO = "Junho";
  public static final String JULHO = "Julho";
  public static final String AGOSTO = "Agosto";
  public static final String SETEMBRO = "Setembro";
  public static final String OUTUBRO = "Outubro";
  public static final String NOVEMBRO = "Novembro";
  public static final String DEZEMBRO = "Dezembro";
  public static final String[] MESES;
  public static final String BEM_CODIGO_DEPOSITO_CONTA_BRASIL = "61";
  public static final String BEM_CODIGO_DEPOSITO_CONTA_EXTERIOR = "62";
  public static final String TIPO_IMOVEL_AGRICULTURA = "10";
  public static final String TIPO_IMOVEL_PECUARIA = "11";
  public static final String TIPO_IMOVEL_EXTRACAO = "12";
  public static final String CODIGO_EXPLORACAO_PROPRIETARIO = "1";
  public static final String CODIGO_EXPLORACAO_CONDOMINIO = "2";
  public static final String CODIGO_EXPLORACAO_PARCEIRO = "3";
  public static final String TOTAL_RESULTADOS_LIQUIDOS = "TotalResultadosLiquidos";
  public static final String TOTAL_RESULTADOS_NEGATIVOS = "TotalResultadosNegativos";
  public static final String TOTAL_BASE_CALCULO_IMPOSTO = "BaseCalculoImposto";
  public static final String TOTAL_PREJUZO_COMPENSAR = "PrejuizoCompensar";
  public static final String TOTAL_IMPOSTO_DEVIDO = "ImpostoDevido";
  public static final String TOTAL_IMPOSTO_DEVIDO_CONSOLIDACAO = "ImpostoDevidoConsolidacao";
  public static final String TOTAL_IR_DAYTRADE_MESESANTERIORES = "IRDayTradeMesesAnteriores";
  public static final String TOTAL_IR_DAYTRADE_COMPENSAR = "IRDayTradeCompensar";
  public static final String TOTAL_IMPOSTO_APAGAR = "TotalImpostoAPagar";
  
  static
  {
    try
      {
	EXERCICIO_INT = Integer.parseInt (FabricaUtilitarios.getProperties ().getProperty ("exercicio", "0"));
      }
    catch (Exception exception)
      {
	/* empty */
      }
    try
      {
	String tamValor = FabricaUtilitarios.getProperties ().getProperty ("informacao.valor.maxTotalDigitos", "13").trim ();
	String tamValorParteDecimal = FabricaUtilitarios.getProperties ().getProperty ("informacao.valor.casasDecimais", "2").trim ();
	_TAMANHO_VALOR = Integer.parseInt (tamValor);
	_TAMANHO_VALOR_PARTE_DECIMAL = Integer.parseInt (tamValorParteDecimal);
      }
    catch (Exception e)
      {
	_TAMANHO_VALOR = 13;
	_TAMANHO_VALOR_PARTE_DECIMAL = 2;
      }
    TAMANHO_VALOR = _TAMANHO_VALOR;
    TAMANHO_VALOR_PARTE_DECIMAL = _TAMANHO_VALOR_PARTE_DECIMAL;
    EXERCICIO = String.valueOf (EXERCICIO_INT);
    LOCALIDADE = new Locale ("pt", "BR");
    ZERO = new Valor ("0,00");
    ALIQUOTA_TRIBUTACAO_1 = new Valor ("0,15");
    ALIQUOTA_TRIBUTACAO_2 = new Valor ("0,275");
    LIMITE_ISENCAO = new Valor ("12696,00");
    FAIXA_TRIBUTACAO_1 = new Valor ("25380,00");
    DEDUCAO_FAIXA_1 = new Valor ("1904,40");
    DEDUCAO_FAIXA_2 = new Valor ("5076,90");
    DEDUCAO_FAIXA_1_MENSAL = new Valor ("158,70");
    DEDUCAO_FAIXA_2_MENSAL = new Valor ("423,08");
    DEDUCAO_DEPENDENTE = new Valor ("1272,00");
    DEDUCAO_INSTRUCAO = new Valor ("1998,00");
    DESCONTO_PADRAO = new Valor ("9400,00");
    DOLAR_31_12_ANO_ANTERIOR = new Valor ("3,5325");
    MULTA_POR_ATRASO_ENTREGA = new Valor ("165,74");
    VALOR_MINIMO_QUOTA = new Valor ("50,00");
    DATA_PRIMEIRA_QUOTA = "29/04/" + EXERCICIO;
    DATA_SEGUNDA_QUOTA = "31/05/" + EXERCICIO;
    DATA_TERCEIRA_QUOTA = "30/06/" + EXERCICIO;
    DATA_QUARTA_QUOTA = "29/07/" + EXERCICIO;
    DATA_QUINTA_QUOTA = "31/08/" + EXERCICIO;
    DATA_SEXTA_QUOTA = "30/09/" + EXERCICIO;
    NOME_PROGRAMA = FabricaUtilitarios.getProperties ().getProperty ("nomeAplicacao", "CONST_NOME_PROGRAMA");
    NOME_PROGRAMA_EXTENSO = FabricaUtilitarios.getProperties ().getProperty ("nomeAplicacaoExtenso", "CONST_NOME_PROGRAMA_EXTENSO");
    XMLNS = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.namespace", "http://www.receita.fazenda.gov.br/declaracao");
    EXERCICIO_ANTERIOR = String.valueOf (EXERCICIO_INT - 1);
    EXERCICIO_POSTERIOR = String.valueOf (EXERCICIO_INT + 1);
    ANO_BASE = EXERCICIO_ANTERIOR;
    ANO_BASE_ANTERIOR = String.valueOf (EXERCICIO_INT - 2);
    ANO_BASE_SEGUINTE = EXERCICIO;
    ENDERECO_GATEWAY = "HTTPS://pagamento.serpro.gov.br/pir" + ANO_BASE + "/pgdpf" + ANO_BASE + ".asp?parametros=";
    ENDERECO_GATEWAY_TESTE = "http://10.15.11.207/HPir" + ANO_BASE + "/PGDPF" + ANO_BASE + ".asp?parametros=";
    PREFIXO_ENDERECO_HELP_ONLINE = "http://www.receita.fazenda.gov.br/pessoafisica/irpf/" + EXERCICIO + "/";
    VERSAORECNET = Integer.parseInt (FabricaUtilitarios.getProperties ().getProperty ("versaoMinimaRecNet", "200506"));
    MESES = new String[] { "Janeiro", "Fevereiro", "Mar\u00e7o", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" };
  }
}
