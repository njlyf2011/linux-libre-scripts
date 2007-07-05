/* Validador - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.util;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.ElementoTabela;
import serpro.ppgd.negocio.RetornoValidacao;

public class Validador
{
  private static final String NOME_BRANCO = "Nome em branco";
  private static final String NOME_MAIS_60 = "Nome tem mais que 60 caracteres";
  private static final String NOME_MENOS_2 = "Nome tem menos de 2 partes";
  private static final String NOME_MAIS_15 = "Nome tem mais de 15 partes";
  private static final String NOME_1_CARACTER = "Primeira parte do nome tem somente um caracter";
  private static final String NOME_MAIS_20 = "Parte do nome tem mais de 20 caracteres";
  private static final String NOME_3_CONSECUTIVOS = "Nome com 3 caracteres iguais consecutivos";
  private static final String NOME_NUMERICO = "Nome n\u00e3o pode ter n\u00fameros";
  private static final String NOME_INVALIDO = "Nome tem caracteres inv\u00e1lidos";
  private static final String NOME_ESPOLIO = "Na declara\u00e7\u00e3o de esp\u00f3lio,deve ser informado o nome do contribuinte,sem a palavra ESP\u00d3LIO";
  public static final String DATA_INVALIDO = "Data inv\u00e1lida ou em branco";
  public static final String DATA_DIA_INVALIDO = "Data com dia inv\u00e1lido";
  public static final String DATA_MES_INVALIDO = "Data com m\u00eas inv\u00e1lido";
  public static final String DATA_ANO_INVALIDO = "Data com ano inv\u00e1lido";
  private static final String VALOR_GRANDE = "Valor informado excede o tamanho do campo";
  private static final String VALOR_NEGATIVO = "Valor n\u00e3o pode ser negativo";
  private static final String EMAIL_INVALIDO = "Correio eletr\u00f4nico inv\u00e1lido ou em branco";
  private static final String NI_INVALIDO = "CNPJ/CPF inv\u00e1lido ou em branco";
  private static final String TITULO_INVALIDO = "N\u00famero do t\u00edtulo eleitoral inv\u00e1lido";
  private static final String CNPJ_INVALIDO = "CNPJ inv\u00e1lido ou em branco";
  private static final String CPF_INVALIDO = "CPF inv\u00e1lido ou em branco";
  private static final String DDD_DDI_INVALIDO = "DDD/DDI inv\u00e1lido ou em branco";
  private static final String DDD_DDI_2_DIGITOS = "O campo DDD/DDI deve ter 2 d\u00edgitos";
  private static final String CEP_INVALIDO = "CEP inv\u00e1lido.Informe-o com oito d\u00edgitos num\u00e9ricos";
  private static final String NIRF_INVALIDO = "N\u00famero do Im\u00f3vel na Receita inv\u00e1lido.";
  private static final String NIT_INVALIDO = "NIT inv\u00e1lido ou em branco";
  public static final int DATA_OK = 0;
  public static final int DATA_INVALIDA_FORMATO_INVALIDO = 1;
  public static final int DATA_INVALIDA_DIA_INVALIDO = 2;
  public static final int DATA_INVALIDA_MES_INVALIDO = 3;
  public static final int DATA_INVALIDA_ANO_INVALIDO = 4;
  public static final int MOD11_CASO_0 = 0;
  public static final int MOD11_CASO_1 = 1;
  public static final int MOD11_CASO_2 = 2;
  
  public static int calcularModulo11 (String stEnt, String stMult, int inStatus)
  {
    int inSoma = 0;
    if (stMult == null)
      {
	for (int i = 0; i < stEnt.length (); i++)
	  inSoma += (stEnt.length () + 1 - i) * Character.getNumericValue (stEnt.charAt (i));
      }
    else
      {
	for (int i = 0; i < stEnt.length (); i++)
	  inSoma += Character.getNumericValue (stMult.charAt (i)) * Character.getNumericValue (stEnt.charAt (i));
      }
    int inResto = inSoma - inSoma / 11 * 11;
    int inMod11 = 11 - inResto;
    if (inStatus == 2)
      {
	if (inMod11 > 9)
	  inMod11 = 0;
      }
    else if (inStatus == 1)
      {
	if (inMod11 == 10)
	  inMod11 = 0;
	if (inMod11 == 11)
	  inMod11 = 1;
      }
    return inMod11;
  }
  
  public static boolean validarModulo11 (String stEnt, String stMult, int inStatus)
  {
    String stNumero = stEnt.substring (0, stEnt.length () - 1);
    int inDigVerifCalc = calcularModulo11 (stNumero, stMult, inStatus);
    String stDigVerif = stEnt.substring (stEnt.length () - 1, stEnt.length ());
    int inDigVerif = Integer.valueOf (stDigVerif).intValue ();
    return inDigVerifCalc == inDigVerif;
  }
  
  public static RetornoValidacao validarCPF (String stCPF)
  {
    if (stCPF == null || stCPF.equals (""))
      return new RetornoValidacao ("CPF inv\u00e1lido ou em branco");
    String CPFInvalidos = "00000000000 00000000191 99999999999 11111111111 22222222222 33333333333 44444444444 55555555555 66666666666 77777777777 88888888888 99999999999";
    if (CPFInvalidos.indexOf (stCPF) != -1)
      return new RetornoValidacao ("CPF inv\u00e1lido ou em branco");
    if (! UtilitariosString.isNumeric (stCPF))
      return new RetornoValidacao ("CPF inv\u00e1lido ou em branco");
    if (stCPF.length () != 11)
      return new RetornoValidacao ("CPF inv\u00e1lido ou em branco");
    if (! validarModulo11 (stCPF.substring (0, 10), null, 2) || ! validarModulo11 (stCPF, null, 2))
      return new RetornoValidacao ("CPF inv\u00e1lido ou em branco");
    return null;
  }
  
  public static RetornoValidacao validarNIRF (String pNIRF)
  {
    if (pNIRF.trim ().length () < 8)
      return new RetornoValidacao ("N\u00famero do Im\u00f3vel na Receita inv\u00e1lido.", (byte) 2);
    if (pNIRF.trim ().equals ("00000000"))
      return new RetornoValidacao ("N\u00famero do Im\u00f3vel na Receita inv\u00e1lido.", (byte) 2);
    if (! validarModulo11 (pNIRF, null, 2))
      return new RetornoValidacao ("N\u00famero do Im\u00f3vel na Receita inv\u00e1lido.", (byte) 2);
    return null;
  }
  
  public static RetornoValidacao validarCNPJ (String stCNPJ)
  {
    if (stCNPJ == null || stCNPJ.equals (""))
      return new RetornoValidacao ("CNPJ inv\u00e1lido ou em branco");
    if (! UtilitariosString.isNumeric (stCNPJ))
      return new RetornoValidacao ("CNPJ inv\u00e1lido ou em branco");
    if (stCNPJ.length () != 14)
      return new RetornoValidacao ("CNPJ inv\u00e1lido ou em branco");
    if (stCNPJ.equals ("11111111000191") || stCNPJ.equals ("00000000000000") || stCNPJ.equals ("22222222000191") || stCNPJ.equals ("33333333000191") || stCNPJ.equals ("44444444000191") || stCNPJ.equals ("55555555000191") || stCNPJ.equals ("66666666000191") || stCNPJ.equals ("77777777000191") || stCNPJ.equals ("88888888000191") || stCNPJ.equals ("99999999000191"))
      return new RetornoValidacao ("CNPJ inv\u00e1lido ou em branco");
    if (! validarModulo11 (stCNPJ.substring (0, 13), "543298765432", 2) || ! validarModulo11 (stCNPJ, "6543298765432", 2))
      return new RetornoValidacao ("CNPJ inv\u00e1lido ou em branco");
    return null;
  }
  
  public static RetornoValidacao validarTituloEleitor (String tituloEleitor)
  {
    if (tituloEleitor == null || tituloEleitor.equals (""))
      return new RetornoValidacao ("N\u00famero do t\u00edtulo eleitoral inv\u00e1lido");
    tituloEleitor = tituloEleitor.trim ();
    int tamanho = tituloEleitor.length ();
    if (tamanho > 13 || tamanho < 3)
      return new RetornoValidacao ("N\u00famero do t\u00edtulo eleitoral inv\u00e1lido");
    tituloEleitor = "0000000000000".substring (0, 13 - tamanho) + tituloEleitor;
    if (! UtilitariosString.isNumeric (tituloEleitor))
      return new RetornoValidacao ("N\u00famero do t\u00edtulo eleitoral inv\u00e1lido");
    int uf = Integer.valueOf (tituloEleitor.substring (9, 11)).intValue ();
    if (uf < 1 || uf > 28)
      return new RetornoValidacao ("N\u00famero do t\u00edtulo eleitoral inv\u00e1lido");
    int mod11;
    if (uf == 1 || uf == 2)
      mod11 = 1;
    else
      mod11 = 2;
    if (! validarModulo11 (tituloEleitor.substring (0, 9) + tituloEleitor.substring (11, 12), null, mod11) || ! validarModulo11 (tituloEleitor.substring (9, 12) + tituloEleitor.substring (12, 13), null, mod11))
      return new RetornoValidacao ("N\u00famero do t\u00edtulo eleitoral inv\u00e1lido");
    return null;
  }
  
  public static RetornoValidacao validarNI (String ni)
  {
    if (ni == null || ni.equals (""))
      return new RetornoValidacao ("CNPJ/CPF inv\u00e1lido ou em branco");
    if (ni.length () == 11)
      return validarCPF (ni);
    if (ni.length () == 14)
      return validarCNPJ (ni);
    return new RetornoValidacao ("CNPJ/CPF inv\u00e1lido ou em branco");
  }
  
  public static int verificaData (String data)
  {
    if (data == null || data.equals ("  /  /    "))
      return 1;
    int maxDia = 31;
    StringTokenizer tokenizer = new StringTokenizer (data, "/");
    if (tokenizer.countTokens () != 3)
      return 1;
    String token = tokenizer.nextToken ();
    int inDia;
    try
      {
	inDia = Integer.parseInt (token);
      }
    catch (Exception e)
      {
	return 2;
      }
    token = tokenizer.nextToken ();
    int inMes;
    try
      {
	inMes = Integer.parseInt (token);
      }
    catch (Exception e)
      {
	return 3;
      }
    token = tokenizer.nextToken ();
    int inAno;
    try
      {
	inAno = Integer.parseInt (token);
      }
    catch (Exception e)
      {
	return 4;
      }
    if (inMes <= 0 || inMes >= 13)
      return 3;
    maxDia = calculaDiasMes (inMes, inAno);
    if (inDia <= 0 || inDia > maxDia)
      return 2;
    return 0;
  }
  
  public static RetornoValidacao validarDataComCriterio (String data, String criterio)
  {
    if (data == null || data.equals ("  /  /    "))
      return new RetornoValidacao ("Data inv\u00e1lida ou em branco");
    int maxDia = 31;
    StringTokenizer tokenizer = new StringTokenizer (data, "/");
    if (tokenizer.countTokens () != 3)
      return new RetornoValidacao ("Data inv\u00e1lida ou em branco");
    String token = tokenizer.nextToken ();
    try
      {
	int inDia = Integer.parseInt (token);
      }
    catch (Exception e)
      {
	return new RetornoValidacao ("Data com dia inv\u00e1lido");
      }
    token = tokenizer.nextToken ();
    int inMes;
    try
      {
	inMes = Integer.parseInt (token);
      }
    catch (Exception e)
      {
	return new RetornoValidacao ("Data com m\u00eas inv\u00e1lido");
      }
    token = tokenizer.nextToken ();
    int inAno;
    try
      {
	inAno = Integer.parseInt (token);
      }
    catch (Exception e)
      {
	return new RetornoValidacao ("Data com ano inv\u00e1lido");
      }
    if (inMes <= 0 || inMes >= 13)
      return new RetornoValidacao ("Data com m\u00eas inv\u00e1lido");
    int exercicioWord = Integer.parseInt (ConstantesGlobais.EXERCICIO);
    if (criterio.equals ("<"))
      {
	if (inAno <= 0 || inAno >= exercicioWord || inAno < exercicioWord - 133 && inAno != 0)
	  return new RetornoValidacao ("Data com ano inv\u00e1lido");
      }
    else if (criterio.equals ("="))
      {
	if (inAno <= 0 || inAno != exercicioWord || inAno < exercicioWord - 133 && inAno != 0)
	  return new RetornoValidacao ("Data com ano inv\u00e1lido");
      }
    else if (criterio.equals (">") && (inAno <= 0 || inAno <= exercicioWord || inAno < exercicioWord - 133 && inAno != 0))
      return new RetornoValidacao ("Data com ano inv\u00e1lido");
    return null;
  }
  
  public static RetornoValidacao validarData (String data, int anoLimite)
  {
    if (data == null || data.equals ("  /  /    "))
      return new RetornoValidacao ("Data inv\u00e1lida ou em branco");
    int maxDia = 31;
    StringTokenizer tokenizer = new StringTokenizer (data, "/");
    if (tokenizer.countTokens () != 3)
      return new RetornoValidacao ("Data inv\u00e1lida ou em branco");
    String token = tokenizer.nextToken ();
    int inDia;
    try
      {
	inDia = Integer.parseInt (token);
      }
    catch (Exception e)
      {
	return new RetornoValidacao ("Data com dia inv\u00e1lido");
      }
    token = tokenizer.nextToken ();
    int inMes;
    try
      {
	inMes = Integer.parseInt (token);
      }
    catch (Exception e)
      {
	return new RetornoValidacao ("Data com m\u00eas inv\u00e1lido");
      }
    token = tokenizer.nextToken ();
    int inAno;
    try
      {
	inAno = Integer.parseInt (token);
      }
    catch (Exception e)
      {
	return new RetornoValidacao ("Data com ano inv\u00e1lido");
      }
    if (inMes <= 0 || inMes >= 13)
      return new RetornoValidacao ("Data com m\u00eas inv\u00e1lido");
    int exercicioWord = Integer.parseInt (ConstantesGlobais.EXERCICIO);
    if (inAno <= 0 || inAno >= anoLimite || inAno < exercicioWord - 131 && inAno != 0)
      return new RetornoValidacao ("Data com ano inv\u00e1lido");
    maxDia = calculaDiasMes (inMes, inAno);
    if (inDia <= 0 || inDia > maxDia)
      return new RetornoValidacao ("Data com dia inv\u00e1lido");
    return null;
  }
  
  public static RetornoValidacao validarData (String data)
  {
    if (data == null || data.equals ("  /  /    "))
      return new RetornoValidacao ("Data inv\u00e1lida ou em branco");
    int maxDia = 31;
    StringTokenizer tokenizer = new StringTokenizer (data, "/");
    if (tokenizer.countTokens () != 3)
      return new RetornoValidacao ("Data inv\u00e1lida ou em branco");
    String token = tokenizer.nextToken ();
    int inDia;
    try
      {
	inDia = Integer.parseInt (token);
      }
    catch (Exception e)
      {
	return new RetornoValidacao ("Data com dia inv\u00e1lido");
      }
    token = tokenizer.nextToken ();
    int inMes;
    try
      {
	inMes = Integer.parseInt (token);
      }
    catch (Exception e)
      {
	return new RetornoValidacao ("Data com m\u00eas inv\u00e1lido");
      }
    token = tokenizer.nextToken ();
    int inAno;
    try
      {
	inAno = Integer.parseInt (token);
      }
    catch (Exception e)
      {
	return new RetornoValidacao ("Data com ano inv\u00e1lido");
      }
    if (inMes <= 0 || inMes >= 13)
      return new RetornoValidacao ("Data com m\u00eas inv\u00e1lido");
    int exercicioWord = Integer.parseInt (ConstantesGlobais.EXERCICIO);
    if (inAno <= 0 || inAno >= exercicioWord || inAno < exercicioWord - 133 && inAno != 0)
      return new RetornoValidacao ("Data com ano inv\u00e1lido");
    maxDia = calculaDiasMes (inMes, inAno);
    if (inDia <= 0 || inDia > maxDia)
      return new RetornoValidacao ("Data com dia inv\u00e1lido");
    if (inAno > Integer.parseInt (ConstantesGlobais.ANO_BASE))
      return new RetornoValidacao ("Data com ano inv\u00e1lido");
    return null;
  }
  
  public static RetornoValidacao validarEmail (String email)
  {
    if (email == null || email.equals (""))
      return new RetornoValidacao ("Correio eletr\u00f4nico inv\u00e1lido ou em branco");
    byte TAMANHO_CAMPO_EMAIL = 50;
    int objNumeroPalavras = 0;
    int numeroArrobas = 0;
    int j = 0;
    objNumeroPalavras = UtilitariosString.ContaPalavras (email);
    if (objNumeroPalavras > 1)
      return new RetornoValidacao ("Correio eletr\u00f4nico inv\u00e1lido ou em branco");
    objNumeroPalavras = UtilitariosString.ContaPalavras (email, "@");
    if (objNumeroPalavras != 2)
      return new RetornoValidacao ("Correio eletr\u00f4nico inv\u00e1lido ou em branco");
    if (email.length () > 50)
      return new RetornoValidacao ("Correio eletr\u00f4nico inv\u00e1lido ou em branco");
    if (email.charAt (0) == '@')
      return new RetornoValidacao ("Correio eletr\u00f4nico inv\u00e1lido ou em branco");
    for (int i = 1; i <= email.length () - 1; i++)
      {
	if (email.charAt (i) == '@')
	  numeroArrobas++;
	if (email.charAt (i) == '.' && email.charAt (i - 1) == '.')
	  return new RetornoValidacao ("Correio eletr\u00f4nico inv\u00e1lido ou em branco");
	if (email.charAt (i) == ' ')
	  return new RetornoValidacao ("Correio eletr\u00f4nico inv\u00e1lido ou em branco");
      }
    if (numeroArrobas != 1)
      return new RetornoValidacao ("Correio eletr\u00f4nico inv\u00e1lido ou em branco");
    return null;
  }
  
  public static RetornoValidacao validarNomeCompleto (String nomeCompleto)
  {
    if (nomeCompleto == null || nomeCompleto.equals (""))
      return new RetornoValidacao ("Nome em branco");
    if (nomeCompleto.length () > 60)
      return new RetornoValidacao ("Nome tem mais que 60 caracteres");
    StringTokenizer testeNomeCompleto = new StringTokenizer (nomeCompleto);
    if (testeNomeCompleto.countTokens () < 2)
      return new RetornoValidacao ("Nome tem menos de 2 partes");
    if (testeNomeCompleto.countTokens () > 15)
      return new RetornoValidacao ("Nome tem mais de 15 partes");
    RetornoValidacao rValidacao = null;
    boolean primeiraVez = true;
    while (testeNomeCompleto.hasMoreTokens ())
      {
	String testeNome = testeNomeCompleto.nextToken ();
	if (primeiraVez)
	  {
	    if (testeNome.length () < 2)
	      return new RetornoValidacao ("Primeira parte do nome tem somente um caracter");
	    primeiraVez = false;
	  }
	rValidacao = validarNome (testeNome);
	if (rValidacao != null)
	  break;
      }
    return rValidacao;
  }
  
  private static RetornoValidacao validarNome (String parteNomeCompleto)
  {
    if (parteNomeCompleto == null || parteNomeCompleto.equals (""))
      return new RetornoValidacao ("Nome em branco");
    if (parteNomeCompleto.length () > 20)
      return new RetornoValidacao ("Parte do nome tem mais de 20 caracteres");
    int letrasRepetidas = 1;
    for (int j = 1; j < parteNomeCompleto.length (); j++)
      {
	if (parteNomeCompleto.charAt (j) == parteNomeCompleto.charAt (j - 1))
	  letrasRepetidas++;
	else
	  letrasRepetidas = 1;
	if (letrasRepetidas == 3 && ! parteNomeCompleto.equals ("III"))
	  return new RetornoValidacao ("Nome com 3 caracteres iguais consecutivos");
	if (Character.isDigit (parteNomeCompleto.charAt (j - 1)))
	  return new RetornoValidacao ("Nome n\u00e3o pode ter n\u00fameros");
	if (! Character.isLetter (parteNomeCompleto.charAt (j - 1)))
	  return new RetornoValidacao ("Nome tem caracteres inv\u00e1lidos");
      }
    if (Character.isDigit (parteNomeCompleto.charAt (parteNomeCompleto.length () - 1)))
      return new RetornoValidacao ("Nome n\u00e3o pode ter n\u00fameros");
    if (! Character.isLetter (parteNomeCompleto.charAt (parteNomeCompleto.length () - 1)))
      return new RetornoValidacao ("Nome tem caracteres inv\u00e1lidos");
    if ("ESP\u00d3LIO".equals (parteNomeCompleto.toUpperCase ()))
      return new RetornoValidacao ("Na declara\u00e7\u00e3o de esp\u00f3lio,deve ser informado o nome do contribuinte,sem a palavra ESP\u00d3LIO");
    return null;
  }
  
  public static RetornoValidacao validarDataDecisaoJudicialPartilha (Date DataDecisaoJudicialPartilha)
  {
    if (DataDecisaoJudicialPartilha == null || DataDecisaoJudicialPartilha.equals (""))
      return new RetornoValidacao ("Data inv\u00e1lida ou em branco");
    return null;
  }
  
  public static RetornoValidacao validarDataNaoResidente (Date dataNaoResidente)
  {
    if (dataNaoResidente == null || dataNaoResidente.equals (""))
      return new RetornoValidacao ("Data inv\u00e1lida ou em branco");
    return null;
  }
  
  public static RetornoValidacao validarDataResidente (Date dataResidente)
  {
    if (dataResidente == null || dataResidente.equals (""))
      return new RetornoValidacao ("Data inv\u00e1lida ou em branco");
    return null;
  }
  
  public static RetornoValidacao validarDataTransitoJulgado (Date informarDataTransitoJulgado)
  {
    if (informarDataTransitoJulgado == null || informarDataTransitoJulgado.equals (""))
      return new RetornoValidacao ("Data inv\u00e1lida ou em branco");
    return null;
  }
  
  public static RetornoValidacao validarDDD_DDI (String ddd)
  {
    if (ddd == null || ddd.equals (""))
      return new RetornoValidacao ("DDD/DDI inv\u00e1lido ou em branco");
    try
      {
	int d = Integer.parseInt (ddd);
      }
    catch (NumberFormatException excecaoConversao)
      {
	return new RetornoValidacao ("DDD/DDI inv\u00e1lido ou em branco");
      }
    if (ddd.length () != 2)
      return new RetornoValidacao ("O campo DDD/DDI deve ter 2 d\u00edgitos");
    return null;
  }
  
  public static RetornoValidacao validarCEP (String cep)
  {
    if (cep == null || cep.trim ().equals ("-"))
      return new RetornoValidacao ("CEP inv\u00e1lido.Informe-o com oito d\u00edgitos num\u00e9ricos");
    if (cep.trim ().length () != 8)
      return new RetornoValidacao ("CEP inv\u00e1lido.Informe-o com oito d\u00edgitos num\u00e9ricos");
    return null;
  }
  
  private static int calculaDiasMes (int mes, int ano)
  {
    int retorno = 31;
    switch (mes)
      {
      case 1:
	retorno = 31;
	break;
      case 2:
	{
	  GregorianCalendar calendar = new GregorianCalendar ();
	  if (calendar.isLeapYear (ano))
	    retorno = 29;
	  else
	    retorno = 28;
	  break;
	}
      case 3:
	retorno = 31;
	break;
      case 4:
	retorno = 30;
	break;
      case 5:
	retorno = 31;
	break;
      case 6:
	retorno = 30;
	break;
      case 7:
	retorno = 31;
	break;
      case 8:
	retorno = 31;
	break;
      case 9:
	retorno = 30;
	break;
      case 10:
	retorno = 31;
	break;
      case 11:
	retorno = 30;
	break;
      case 12:
	retorno = 31;
	break;
      }
    return retorno;
  }
  
  public static RetornoValidacao validarElementoTabela (String conteudo, List colecao, String msgValidacao)
  {
    if (colecao != null)
      {
	for (int i = 0; i < colecao.size (); i++)
	  {
	    ElementoTabela localElementoTabela = (ElementoTabela) colecao.get (i);
	    if (localElementoTabela.getConteudo (0).compareTo (conteudo) == 0)
	      return new RetornoValidacao ((byte) 0);
	  }
      }
    return new RetornoValidacao (msgValidacao);
  }
  
  public static boolean validarNrRecibo (String value)
  {
    value = value.trim ();
    if (value.length () == 0 || value.length () != 12 && value.length () != 9 || ! UtilitariosString.isNumeric (value))
      return false;
    if (value.length () == 9)
      {
	int dvCalculado = calculaDVRecibo9Digitos (value);
	int dvPassado = Integer.parseInt (value.substring (value.length () - 1));
	if (dvCalculado != dvPassado)
	  return false;
      }
    else if (! validarModulo11 (value.substring (0, 11), null, 2) || ! validarModulo11 (value, null, 2))
      return false;
    return true;
  }
  
  private static int calculaDVRecibo9Digitos (String numRecibo)
  {
    int retorno = 0;
    int[] fatores = { 8, 6, 4, 2, 3, 5, 9, 7 };
    int totalProduto = 0;
    for (int i = 0; i < fatores.length; i++)
      {
	int digitoAtual = Integer.parseInt (numRecibo.substring (i, i + 1));
	totalProduto += digitoAtual * fatores[i];
      }
    totalProduto %= 11;
    if (totalProduto == 0)
      retorno = 5;
    else if (totalProduto == 1)
      retorno = 0;
    else
      retorno = 11 - totalProduto;
    return retorno;
  }
  
  public static boolean validarString (String s, String regExp)
  {
    Pattern p = Pattern.compile (regExp);
    Matcher m = p.matcher (s);
    return m.find ();
  }
  
  public static boolean validarCodImovelIncra (String pCod)
  {
    if (! validarModulo11 (pCod, "765432765432", 2))
      return false;
    if (! validarModulo11 (pCod.substring (0, 6), "65432", 2))
      return false;
    if (! validarModulo11 (pCod.substring (6, 12), "65432", 2))
      return false;
    return true;
  }
  
  public static RetornoValidacao validarNIT (String pNIT)
  {
    pNIT = pNIT.trim ();
    if (pNIT.length () == 0 || pNIT.length () != 11 || ! UtilitariosString.isNumeric (pNIT))
      return new RetornoValidacao ("NIT inv\u00e1lido ou em branco");
    if (pNIT.length () == 11)
      {
	int dvCalculado = calculaDVNIT (pNIT);
	int dvPassado = Integer.parseInt (pNIT.substring (pNIT.length () - 1));
	if (dvCalculado != dvPassado)
	  return new RetornoValidacao ("NIT inv\u00e1lido ou em branco");
      }
    return null;
  }
  
  private static int calculaDVNIT (String numNIT)
  {
    int retorno = 0;
    int totalProduto = 0;
    int resto = 0;
    int[] pesos = { 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
    for (int i = 0; i < pesos.length; i++)
      {
	int digitoAtual = Integer.parseInt (numNIT.substring (i, i + 1));
	totalProduto += digitoAtual * pesos[i];
      }
    resto = totalProduto % 11;
    retorno = 11 - resto;
    return retorno;
  }
  
  public static void main (String[] Args)
  {
    /* empty */
  }
}
