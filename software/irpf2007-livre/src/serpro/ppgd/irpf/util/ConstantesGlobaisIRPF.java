/* ConstantesGlobaisIRPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.util;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.Valor;

public class ConstantesGlobaisIRPF
{
  public static final String IRPF_PATH_APP = "IRPF2006_PATH_APP";
  public static final String IRPF_PATH_TRANSMITIDAS = "IRPF2006_PATH_TRANSMITIDAS";
  public static final String IRPF_PATH_GRAVADAS = "IRPF2006_PATH_GRAVADAS";
  public static final String IRPF_PATH_DADOS = "IRPF2006_PATH_DADOS";
  public static final String NOME_PROGRAMA = "IRPF";
  public static final String PADRAO_NOME_ARQ_DECLARACAO_SEM_EXTENSAO = "\\d{11}-IRPF-" + ConstantesGlobais.EXERCICIO + "-" + ConstantesGlobais.ANO_BASE + "-" + "(ORIGI|RETIF|origi|retif)";
  public static final String PADRAO_NOME_ARQ_DECLARACAO_ANO_ANTERIOR_SEM_EXTENSAO = "\\d{11}-IRPF-" + ConstantesGlobais.EXERCICIO_ANTERIOR + "-" + ConstantesGlobais.ANO_BASE_ANTERIOR + "-" + "(ORIGI|RETIF|origi|retif)";
  public static final String PADRAO_NOME_ARQ_DECLARACAO = PADRAO_NOME_ARQ_DECLARACAO_SEM_EXTENSAO + "." + "(DEC|dec)";
  public static final String PADRAO_NOME_ARQ_DECLARACAO_ANO_ANTERIOR = PADRAO_NOME_ARQ_DECLARACAO_ANO_ANTERIOR_SEM_EXTENSAO + "." + "(DEC|dec)";
  public static final String PADRAO_NOME_ARQ_RECIBO = PADRAO_NOME_ARQ_DECLARACAO_SEM_EXTENSAO + "." + "(REC|rec)";
  public static final String PADRAO_EXT_CARNE_LEAO = ".(LE" + ConstantesGlobais.EXERCICIO_ANTERIOR.charAt (3) + "|le" + ConstantesGlobais.EXERCICIO_ANTERIOR.charAt (3) + ")";
  public static final String PADRAO_EXT_ATIV_RURAL = ".(AE" + ConstantesGlobais.EXERCICIO_ANTERIOR.charAt (3) + "|ae" + ConstantesGlobais.EXERCICIO_ANTERIOR.charAt (3) + ")";
  public static final String PADRAO_NOME_ARQ_RECIBO_LIMITADO_A_8_CARACTERES = "\\d{8}.(REC|rec)";
  public static final String PADRAO_NOME_ARQ_COPIA_SEG = PADRAO_NOME_ARQ_DECLARACAO_SEM_EXTENSAO + "." + "(DEC|DBK|dec|dbk)";
  public static final int CODIGO_RECNET = 1700;
  public static final String SISTEMAANOANTERIOR = "IRPF";
  public static final boolean VERSAO_BETA = false;
  public static final boolean VERSAO_TESTES = false;
  public static final Valor LIMITE_REND_ISEN_TRIB_EXCL = new Valor ("40000,00");
  public static final Valor LIMITE_BENS_DIREITOS = new Valor ("80000,00");
  public static final String LIMITE_ISENCAO_2007 = "14.992,32";
  public static final String LIMITE_RECEITA_BRUTA_AR_2007 = "74.961,60";
}
