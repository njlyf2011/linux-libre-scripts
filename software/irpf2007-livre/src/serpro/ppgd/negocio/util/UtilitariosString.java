/* UtilitariosString - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.util;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;
import java.util.zip.CRC32;

import serpro.ppgd.negocio.ConstantesGlobais;

public class UtilitariosString
{
  public static interface VerificadorRemocaoAcentos
  {
    public boolean podeRemoverAcentoDe (char c);
  }
  
  /**
   * @deprecated
   */
  public static int ContaPalavras (String Frase)
  {
    int retorno = 0;
    if (Frase != null)
      {
	StringTokenizer testeFrase = new StringTokenizer (Frase);
	retorno = testeFrase.countTokens ();
      }
    return retorno;
  }
  
  public static int contaPalavras (String Frase)
  {
    int retorno = 0;
    if (Frase != null)
      {
	StringTokenizer testeFrase = new StringTokenizer (Frase);
	retorno = testeFrase.countTokens ();
      }
    return retorno;
  }
  
  /**
   * @deprecated
   */
  public static int ContaPalavras (String Frase, String separadorDePalavras)
  {
    int pos = 0;
    int count = 0;
    if (Frase != null && separadorDePalavras != null)
      {
	pos = Frase.indexOf (separadorDePalavras, pos);
	if (pos == 0)
	  count = 0;
	else
	  count = 1;
	while (pos != -1)
	  {
	    pos = Frase.indexOf (separadorDePalavras, pos);
	    if (pos != -1 && pos != Frase.length () - 1)
	      count++;
	    if (pos != -1)
	      pos += separadorDePalavras.length ();
	  }
      }
    return count;
  }
  
  public static int contaPalavras (String Frase, String separadorDePalavras)
  {
    int pos = 0;
    int count = 0;
    if (Frase != null && separadorDePalavras != null)
      {
	pos = Frase.indexOf (separadorDePalavras, pos);
	if (pos == 0)
	  count = 0;
	else
	  count = 1;
	while (pos != -1)
	  {
	    pos = Frase.indexOf (separadorDePalavras, pos);
	    if (pos != -1 && pos != Frase.length () - 1)
	      count++;
	    if (pos != -1)
	      pos += separadorDePalavras.length ();
	  }
      }
    return count;
  }
  
  public static String insereQuebraDeLinha (String pTexto, int qtdCaracteres, String pQuebraDeLinha)
  {
    StringBuffer retorno = new StringBuffer ();
    StringTokenizer strTokens = new StringTokenizer (pTexto, " ");
    int pontoDeQuebra = qtdCaracteres - 1;
    while (strTokens.hasMoreTokens ())
      {
	String proximoToken = strTokens.nextToken ();
	if (retorno.length () + proximoToken.length () >= pontoDeQuebra)
	  {
	    retorno.append (pQuebraDeLinha);
	    pontoDeQuebra = retorno.length () + qtdCaracteres;
	  }
	retorno.append (" ");
	retorno.append (proximoToken);
      }
    return retorno.toString ().trim ();
  }
  
  public static String corrigeDataFormatada (String data)
  {
    StringBuffer retorno = new StringBuffer ();
    String dia = "  ";
    String mes = "  ";
    String ano = "    ";
    int pos = 1;
    StringTokenizer tokens = new StringTokenizer (data, "/");
    while (tokens.hasMoreTokens ())
      {
	String tokenAtual = tokens.nextToken ();
	switch (pos)
	  {
	  case 1:
	    dia = tokenAtual;
	    break;
	  case 2:
	    mes = tokenAtual;
	    break;
	  case 3:
	    ano = tokenAtual;
	    break;
	  }
	pos++;
      }
    try
      {
	int diaInt = Integer.parseInt (dia.trim ());
	if (diaInt > 0 && diaInt < 32)
	  {
	    dia = String.valueOf (diaInt);
	    dia = dia.length () == 1 ? "0" + dia : dia;
	  }
      }
    catch (Exception exception)
      {
	/* empty */
      }
    try
      {
	int mesInt = Integer.parseInt (mes.trim ());
	if (mesInt > 0 && mesInt < 13)
	  {
	    mes = String.valueOf (mesInt);
	    mes = mes.length () == 1 ? "0" + mes : mes;
	  }
      }
    catch (Exception exception)
      {
	/* empty */
      }
    try
      {
	int anoInt = Integer.parseInt (ano.trim ());
	if (anoInt >= 1000)
	  ano = String.valueOf (anoInt);
	else if (anoInt >= 0 && anoInt < 10)
	  ano = "200" + String.valueOf (anoInt);
	else if (anoInt >= 10 && anoInt <= 99)
	  ano = "19" + String.valueOf (anoInt);
	else
	  ano = "    ";
      }
    catch (Exception exception)
      {
	/* empty */
      }
    retorno.append (dia);
    retorno.append ("/");
    retorno.append (mes);
    retorno.append ("/");
    retorno.append (ano);
    return retorno.toString ();
  }
  
  public static String corrigeData (String data)
  {
    String retorno = "";
    int y = 0;
    int m = 0;
    int d = 0;
    int segundos = 0;
    retorno = data;
    data = formataData (data);
    if (! data.equals ("  /  /    "))
      {
	String ano = data.substring (6, 10).trim ();
	y = StringToInt (ano);
	m = StringToInt (data.substring (3, 5).trim ());
	d = StringToInt (data.substring (0, 2).trim ());
	if (! ano.equals ("") && ! ano.equals ("0"))
	  {
	    if (y <= Integer.parseInt (ConstantesGlobais.ANO_BASE) % 10)
	      y = 2000 + y;
	    if (y > Integer.parseInt (ConstantesGlobais.ANO_BASE) % 10 && y <= 99)
	      y = 1900 + y;
	    ano = Integer.toString (y);
	  }
	if (y >= 100 && y <= 999)
	  ano = "0" + ano;
	String mes;
	if (m >= 1 && m <= 9)
	  mes = "0" + Integer.toString (m);
	else
	  mes = Integer.toString (m);
	String dia;
	if (d >= 1 && d <= 9)
	  dia = "0" + Integer.toString (d);
	else
	  dia = Integer.toString (d);
	retorno = dia + "/" + mes + "/" + ano;
      }
    return retorno;
  }
  
  public static String retiraMascara (String texto)
  {
    return texto.replaceAll ("[\\p{Punct}\\p{Space}]", "");
  }
  
  public static String retiraMascara (String ent, String mask)
  {
    if (ent != null || mask != null)
      {
	String saida = "";
	mask = expandeMascara (mask, '*');
	for (int i = 0; i < ent.length (); i++)
	  {
	    if (mask.charAt (i) == '*')
	      saida += ent.charAt (i);
	  }
	return saida;
      }
    return ent;
  }
  
  public static String expandeMascara (String mask, char ch)
  {
    String s = "";
    for (int i = 0; i < mask.length (); i++)
      {
	char c = mask.charAt (i);
	if (c == '#' || c == '0' || c == '*' || c == 'A' || c == 'U' || c == 'L' || c == '?' || c == 'H')
	  s += ch;
	else
	  s += c;
      }
    return s;
  }
  
  public static String expandeString (int n, String ch)
  {
    String s = "";
    for (int i = 0; i < n; i++)
      s = s.concat (ch);
    return s;
  }
  
  public static String retornaComMascara (String ent, String mask)
  {
    if (ent != null || mask != null)
      {
	String saida = "";
	mask = expandeMascara (mask, '*');
	int j = 0;
	for (int i = 0; i < mask.length (); i++)
	  {
	    if (mask.charAt (i) == '*')
	      {
		if (j < ent.length ())
		  saida += ent.charAt (j);
		else
		  saida += " ";
		j++;
	      }
	    else
	      saida += mask.charAt (i);
	  }
	return saida;
      }
    return ent;
  }
  
  public static String expandeStringHTML (String texto)
  {
    return expandeStringHTML (texto, "", 0);
  }
  
  public static String expandeStringHTML (String texto, int nrMaxColuna)
  {
    return expandeStringHTML (texto, "", nrMaxColuna);
  }
  
  public static String expandeStringHTML (String texto, String tag, int nrMaxColuna)
  {
    String saida = "";
    if (tag == null)
      tag = "";
    if (texto != null && texto.length () > 0)
      {
	switch (texto.charAt (texto.length () - 1))
	  {
	  case '!':
	  case '.':
	  case '?':
	    break;
	  case '^':
	    texto = texto.substring (0, texto.length () - 1);
	    break;
	  default:
	    texto += '.';
	  }
	if (nrMaxColuna > 0)
	  {
	    StringTokenizer sToken = new StringTokenizer (texto);
	    String lin = "";
	    while (sToken.hasMoreTokens ())
	      {
		String token = sToken.nextToken ();
		if (lin.length () + token.length () >= nrMaxColuna)
		  {
		    saida += (String) lin + "<br>";
		    lin = "";
		  }
		lin += (String) token + " ";
	      }
	    saida += (String) lin;
	  }
	else
	  saida = texto;
      }
    else
      saida = "";
    int pos = saida.indexOf ("<html><body>");
    if (pos == -1)
      {
	saida = "<html><body>" + tag + saida;
	saida += " </body></html>";
      }
    else
      {
	pos += "<html><body>".length ();
	saida = "<html><body>" + tag + saida.substring (pos);
      }
    return saida;
  }
  
  public static String formataCEP (String cep)
  {
    if (cep.length () != 8)
      return cep;
    return cep = cep.substring (0, 5) + "-" + cep.substring (5, 8);
  }
  
  public static String formataComPontos (String pEnt, int intervalo)
  {
    StringBuffer retorno = new StringBuffer ();
    for (int i = 0; i < pEnt.length (); i++)
      {
	retorno.append (pEnt.charAt (i));
	if (i + 1 >= intervalo && (i + 1) % intervalo == 0 && i < pEnt.length () - 1)
	  retorno.append ('.');
      }
    return retorno.toString ();
  }
  
  public static String formataCNPJ (String valor)
  {
    String retorno = retiraMascara (valor);
    if (valor.length () < 14)
      return valor;
    retorno = retorno.substring (0, 2) + "." + retorno.substring (2, 5) + "." + retorno.substring (5, 8) + "/" + retorno.substring (8, 12) + "-" + retorno.substring (12, 14);
    return retorno;
  }
  
  public static String formataCPF (String valor)
  {
    String retorno = retiraMascara (valor);
    for (int i = 11 - retorno.length (); i > 0; i--)
      retorno = retorno.concat (" ");
    retorno = retorno.substring (0, 3) + "." + retorno.substring (3, 6) + "." + retorno.substring (6, 9) + "-" + retorno.substring (9, 11);
    return retorno;
  }
  
  public static String formataNIRF (String pVal)
  {
    String retorno = retiraMascara (pVal);
    for (int i = 8 - retorno.length (); i > 0; i--)
      retorno = retorno.concat (" ");
    retorno = retorno.substring (0, 1) + "." + retorno.substring (1, 4) + "." + retorno.substring (4, 7) + "-" + retorno.substring (7, 8);
    return retorno;
  }
  
  public static String formataData (String valor)
  {
    String retorno = retiraMascara (valor);
    for (int i = 8 - retorno.length (); i > 0; i--)
      retorno = retorno.concat (" ");
    retorno = retorno.substring (0, 2) + "/" + retorno.substring (2, 4) + "/" + retorno.substring (4, 8);
    return retorno;
  }
  
  public static String formataHash (long nHash)
  {
    StringBuffer strTemp = new StringBuffer ();
    strTemp.append (String.valueOf (nHash));
    while (strTemp.length () < 10)
      strTemp.insert (0, "0");
    return strTemp.toString ();
  }
  
  public static String formataNI (String valor)
  {
    String retorno = retiraMascara (valor);
    if (valor.length () == 11)
      retorno = formataCPF (retorno);
    else if (valor.length () == 14)
      retorno = formataCNPJ (retorno);
    else
      retorno = retorno.trim ();
    return retorno;
  }
  
  public static String obtemBaseNI (String valor)
  {
    String retorno = retiraMascara (valor);
    if (retorno.length () == 11)
      return retorno.substring (0, 9);
    if (retorno.length () == 14)
      return retorno.substring (0, 8);
    return null;
  }
  
  public static String formatarMsgErro (String classe, String campo, String mensagemValidacao)
  {
    return "[ " + classe + " ] " + campo + ": " + mensagemValidacao;
  }
  
  public static String GerarCRC (String CRC, String linha)
  {
    Integer crc = new Integer (0);
    try
      {
	crc = Integer.valueOf (CRC);
      }
    catch (Exception e)
      {
	CRC = "0";
      }
    return String.valueOf (CRC.hashCode () ^ linha.hashCode () ^ crc.hashCode ());
  }
  
  public static long gerarCRCArquivo (String pArquivo)
  {
    long l;
    try
      {
	FileReader fr = new FileReader (FabricaUtilitarios.getPathCompletoDirAplicacao () + pArquivo);
	StringBuffer sb = new StringBuffer ();
	char[] carac = new char[1];
	int retorno = -1;
	do
	  {
	    retorno = fr.read (carac);
	    sb.append (carac);
	  }
	while (retorno != -1);
	fr.close ();
	CRC32 c = new CRC32 ();
	c.update (sb.toString ().getBytes ());
	long hash = c.getValue ();
	LogPPGD.debug ("hash->" + hash);
	c = new CRC32 ();
	c.update (String.valueOf (hash).getBytes ());
	long hashDoHash = c.getValue ();
	LogPPGD.debug ("hashDoHash->" + hashDoHash);
	l = hashDoHash;
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	return 0L;
      }
    return l;
  }
  
  public static String geraMD5Sum (String conteudo, String chave)
  {
    String string;
    try
      {
	MessageDigest md5 = MessageDigest.getInstance ("MD5");
	md5.update (conteudo.getBytes ());
	string = new String (md5.digest (chave.getBytes ()));
      }
    catch (NoSuchAlgorithmException e)
      {
	e.printStackTrace ();
	return "0";
      }
    return string;
  }
  
  public static boolean isNumeric (String stEnt)
  {
    for (int i = 0; i < stEnt.length (); i++)
      {
	if (! Character.isDigit (stEnt.charAt (i)))
	  return false;
      }
    return true;
  }
  
  public static String mesPorExtenso (int mes)
  {
    String retorno = "";
    switch (mes)
      {
      case 1:
	retorno = "JANEIRO";
	break;
      case 2:
	retorno = "FEVEREIRO";
	break;
      case 3:
	retorno = "MAR\u00c7O";
	break;
      case 4:
	retorno = "ABRIL";
	break;
      case 5:
	retorno = "MAIO";
	break;
      case 6:
	retorno = "JUNHO";
	break;
      case 7:
	retorno = "JULHO";
	break;
      case 8:
	retorno = "AGOSTO";
	break;
      case 9:
	retorno = "SETEMBRO";
	break;
      case 10:
	retorno = "OUTUBRO";
	break;
      case 11:
	retorno = "NOVEMBRO";
	break;
      case 12:
	retorno = "DEZEMBRO";
	break;
      }
    return retorno;
  }
  
  public static String retiraCaracteresEspeciais (String stEntrada)
  {
    StringBuffer strTemp = new StringBuffer ();
    stEntrada = stEntrada.toUpperCase ();
    stEntrada = stEntrada.replaceAll ("[\u00c7\u00e7\u00a2]", "C");
    stEntrada = stEntrada.replaceAll ("[\u00c9\u00e9\u00c8\u00e8\u00ca\u00ea\u00cb\u00eb]", "E");
    stEntrada = stEntrada.replaceAll ("[\u00c1\u00e1\u00c0\u00e0\u00c2\u00e2\u00c3\u00e3\u00c4\u00e4\u00c5\u00e5\u00c6\u00e6\u00aa]", "A");
    stEntrada = stEntrada.replaceAll ("[\u00d3\u00f3\u00d2\u00f2\u00d4\u00f4\u00d5\u00f5\u00d6\u00f6\u00ba]", "O");
    stEntrada = stEntrada.replaceAll ("[\u00c1\u00e1\u00c0\u00e0\u00c2\u00e2\u00c3\u00e3\u00c4\u00e4\u00c5\u00e5\u00c6\u00e6\u00aa]", "A");
    stEntrada = stEntrada.replaceAll ("[\u00cd\u00ed\u00cc\u00ec\u00ce\u00ee\u00cf\u00ef]", "I");
    stEntrada = stEntrada.replaceAll ("[\u00da\u00fa\u00d9\u00f9\u00db\u00fb\u00dc\u00fc]", "U");
    stEntrada = stEntrada.replaceAll ("[\u00ff\u00fd\u00dd\u009f]", "Y");
    stEntrada = stEntrada.replaceAll ("[\u00f1\u00d1]", "N");
    for (int i = 0; i < stEntrada.length (); i++)
      {
	if ("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789$%& -,./!#*):;(+<>?@_".indexOf (stEntrada.substring (i, i + 1)) < 0)
	  strTemp.append (" ");
	else
	  strTemp.append (stEntrada.substring (i, i + 1));
      }
    return strTemp.toString ();
  }
  
  public static int StringToInt (String valor)
  {
    if (valor.equals (""))
      return 0;
    return Integer.parseInt (valor);
  }
  
  public static String strToHtml (String str)
  {
    if (! str.toLowerCase ().startsWith ("<html>"))
      str = "<html>" + str;
    if (! str.toLowerCase ().endsWith ("</html>"))
      str += "</html>";
    str = str.replaceAll ("\\n", "<br>");
    return str;
  }
  
  public static void main (String[] args)
  {
    /* empty */
  }
  
  public static String removeAcentos (String str)
  {
    char[] buffer = str.toCharArray ();
    for (int i = 0; i < buffer.length; i++)
      {
	switch (buffer[i])
	  {
	  case '\u00c0':
	  case '\u00c1':
	  case '\u00c2':
	  case '\u00c3':
	  case '\u00c4':
	  case '\u00c5':
	    buffer[i] = 'A';
	    break;
	  case '\u00e0':
	  case '\u00e1':
	  case '\u00e2':
	  case '\u00e3':
	  case '\u00e4':
	  case '\u00e5':
	    buffer[i] = 'a';
	    break;
	  case '\u00c7':
	    buffer[i] = 'C';
	    break;
	  case '\u00e7':
	    buffer[i] = 'c';
	    break;
	  case '\u00f0':
	    buffer[i] = 'O';
	    break;
	  case '\u00c8':
	  case '\u00c9':
	  case '\u00ca':
	  case '\u00cb':
	    buffer[i] = 'E';
	    break;
	  case '\u00e8':
	  case '\u00e9':
	  case '\u00ea':
	  case '\u00eb':
	    buffer[i] = 'e';
	    break;
	  case '\u00cc':
	  case '\u00cd':
	  case '\u00ce':
	  case '\u00cf':
	    buffer[i] = 'I';
	    break;
	  case '\u00ec':
	  case '\u00ed':
	  case '\u00ee':
	  case '\u00ef':
	    buffer[i] = 'i';
	    break;
	  case '\u00d1':
	    buffer[i] = 'N';
	    break;
	  case '\u00f1':
	    buffer[i] = 'n';
	    break;
	  case '\u00d2':
	  case '\u00d3':
	  case '\u00d4':
	  case '\u00d5':
	  case '\u00d6':
	    buffer[i] = 'O';
	    break;
	  case '\u00f2':
	  case '\u00f3':
	  case '\u00f4':
	  case '\u00f5':
	  case '\u00f6':
	    buffer[i] = 'o';
	    break;
	  case '\u00d9':
	  case '\u00da':
	  case '\u00db':
	  case '\u00dc':
	    buffer[i] = 'U';
	    break;
	  case '\u00f9':
	  case '\u00fa':
	  case '\u00fb':
	  case '\u00fc':
	    buffer[i] = 'u';
	    break;
	  }
      }
    return new String (buffer);
  }
  
  public static String removeAcentos (String str, VerificadorRemocaoAcentos aVerificador)
  {
    char[] buffer = str.toCharArray ();
    for (int i = 0; i < buffer.length; i++)
      {
	switch (buffer[i])
	  {
	  case '\u00c0':
	  case '\u00c1':
	  case '\u00c2':
	  case '\u00c3':
	  case '\u00c4':
	  case '\u00c5':
	    if (aVerificador.podeRemoverAcentoDe (buffer[i]))
	      buffer[i] = 'A';
	    break;
	  case '\u00e0':
	  case '\u00e1':
	  case '\u00e2':
	  case '\u00e3':
	  case '\u00e4':
	  case '\u00e5':
	    if (aVerificador.podeRemoverAcentoDe (buffer[i]))
	      buffer[i] = 'a';
	    break;
	  case '\u00c7':
	    if (aVerificador.podeRemoverAcentoDe (buffer[i]))
	      buffer[i] = 'C';
	    break;
	  case '\u00e7':
	    if (aVerificador.podeRemoverAcentoDe (buffer[i]))
	      buffer[i] = 'c';
	    break;
	  case '\u00f0':
	    if (aVerificador.podeRemoverAcentoDe (buffer[i]))
	      buffer[i] = 'O';
	    break;
	  case '\u00c8':
	  case '\u00c9':
	  case '\u00ca':
	  case '\u00cb':
	    if (aVerificador.podeRemoverAcentoDe (buffer[i]))
	      buffer[i] = 'E';
	    break;
	  case '\u00e8':
	  case '\u00e9':
	  case '\u00ea':
	  case '\u00eb':
	    if (aVerificador.podeRemoverAcentoDe (buffer[i]))
	      buffer[i] = 'e';
	    break;
	  case '\u00cc':
	  case '\u00cd':
	  case '\u00ce':
	  case '\u00cf':
	    if (aVerificador.podeRemoverAcentoDe (buffer[i]))
	      buffer[i] = 'I';
	    break;
	  case '\u00ec':
	  case '\u00ed':
	  case '\u00ee':
	  case '\u00ef':
	    if (aVerificador.podeRemoverAcentoDe (buffer[i]))
	      buffer[i] = 'i';
	    break;
	  case '\u00d1':
	    if (aVerificador.podeRemoverAcentoDe (buffer[i]))
	      buffer[i] = 'N';
	    break;
	  case '\u00f1':
	    if (aVerificador.podeRemoverAcentoDe (buffer[i]))
	      buffer[i] = 'n';
	    break;
	  case '\u00d2':
	  case '\u00d3':
	  case '\u00d4':
	  case '\u00d5':
	  case '\u00d6':
	    if (aVerificador.podeRemoverAcentoDe (buffer[i]))
	      buffer[i] = 'O';
	    break;
	  case '\u00f2':
	  case '\u00f3':
	  case '\u00f4':
	  case '\u00f5':
	  case '\u00f6':
	    if (aVerificador.podeRemoverAcentoDe (buffer[i]))
	      buffer[i] = 'o';
	    break;
	  case '\u00d9':
	  case '\u00da':
	  case '\u00db':
	  case '\u00dc':
	    if (aVerificador.podeRemoverAcentoDe (buffer[i]))
	      buffer[i] = 'U';
	    break;
	  case '\u00f9':
	  case '\u00fa':
	  case '\u00fb':
	  case '\u00fc':
	    if (aVerificador.podeRemoverAcentoDe (buffer[i]))
	      buffer[i] = 'u';
	    break;
	  }
      }
    return new String (buffer);
  }
  
  public static void trataArquivoHTMLComCaracteresEspeciais (String pathParaoArquivo)
  {
    try
      {
	FileReader fr = new FileReader (pathParaoArquivo);
	StringBuffer sb = new StringBuffer ();
	char[] carac = new char[1];
	int retorno = -1;
	do
	  {
	    retorno = fr.read (carac);
	    sb.append (carac);
	  }
	while (retorno != -1);
	fr.close ();
	FileWriter fw = new FileWriter (pathParaoArquivo);
	String conteudoArquivoHtml = substituiCaracteresEspeciaisTextoHTML (sb.toString ());
	fw.write (conteudoArquivoHtml);
	fw.close ();
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public static String substituiCaracteresEspeciaisTextoHTML (String pStringInicial)
  {
    String retorno = pStringInicial.replaceAll ("[\u00e7]", "&ccedil;");
    retorno = retorno.replaceAll ("[\u00c7]", "&Ccedil;");
    retorno = retorno.replaceAll ("[\u00e1]", "&aacute;");
    retorno = retorno.replaceAll ("[\u00c1]", "&Aacute;");
    retorno = retorno.replaceAll ("[\u00e0]", "&agrave;");
    retorno = retorno.replaceAll ("[\u00c0]", "&Agrave;");
    retorno = retorno.replaceAll ("[\u00f3]", "&oacute;");
    retorno = retorno.replaceAll ("[\u00d3]", "&Oacute;");
    retorno = retorno.replaceAll ("[\u00e9]", "&eacute;");
    retorno = retorno.replaceAll ("[\u00c9]", "&Eacute;");
    retorno = retorno.replaceAll ("[\u00ed]", "&iacute;");
    retorno = retorno.replaceAll ("[\u00cd]", "&Iacute;");
    retorno = retorno.replaceAll ("[\u00fa]", "&uacute;");
    retorno = retorno.replaceAll ("[\u00da]", "&Uacute;");
    retorno = retorno.replaceAll ("[\u00e3]", "&atilde;");
    retorno = retorno.replaceAll ("[\u00c3]", "&Atilde;");
    retorno = retorno.replaceAll ("[\u00f5]", "&otilde;");
    retorno = retorno.replaceAll ("[\u00d5]", "&Otilde;");
    return retorno;
  }
}
