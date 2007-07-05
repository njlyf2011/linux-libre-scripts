/* CalculosGenericos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.barcode.util;

public class CalculosGenericos
{
  public static String limpaFormatoValor (String campo)
  {
    StringBuffer sb = new StringBuffer ();
    for (int i = 0; i < campo.length (); i++)
      {
	if (campo.charAt (i) != '.' && campo.charAt (i) != ',')
	  sb.append (campo.charAt (i));
      }
    return sb.toString ();
  }
  
  public static String EditaValor (String campo)
  {
    String inteiro = "";
    String decimal = "";
    String vlAux = "";
    inteiro = campo.substring (0, campo.indexOf ("."));
    decimal = campo.substring (campo.indexOf (".") + 1);
    if (decimal.length () > 2)
      {
	if (Integer.parseInt (decimal.substring (2, 3)) > 5)
	  {
	    int dec = Integer.parseInt (decimal.substring (0, 2));
	    decimal = String.valueOf (++dec);
	  }
	else
	  decimal = decimal.substring (0, 2);
      }
    else if (decimal.length () == 1)
      decimal += "0";
    if (inteiro.length () <= 3)
      vlAux = inteiro + "," + decimal;
    else if (inteiro.length () == 4)
      vlAux = inteiro.substring (0, 1) + "." + inteiro.substring (1, 4) + "," + decimal;
    else if (inteiro.length () == 5)
      vlAux = inteiro.substring (0, 2) + "." + inteiro.substring (2, 5) + "," + decimal;
    else if (inteiro.length () == 6)
      vlAux = inteiro.substring (0, 3) + "." + inteiro.substring (3, 6) + "," + decimal;
    else if (inteiro.length () == 7)
      vlAux = inteiro.substring (0, 1) + "." + inteiro.substring (1, 4) + "." + inteiro.substring (4, 7) + "," + decimal;
    else if (inteiro.length () == 8)
      vlAux = inteiro.substring (0, 2) + "." + inteiro.substring (2, 5) + "." + inteiro.substring (5, 8) + "," + decimal;
    else if (inteiro.length () == 9)
      vlAux = inteiro.substring (0, 3) + "." + inteiro.substring (3, 6) + "." + inteiro.substring (6, 9) + "," + decimal;
    else if (inteiro.length () == 10)
      vlAux = inteiro.substring (0, 1) + "." + inteiro.substring (1, 4) + "." + inteiro.substring (4, 7) + inteiro.substring (7, 10) + "," + decimal;
    else if (inteiro.length () == 11)
      vlAux = inteiro.substring (0, 2) + "." + inteiro.substring (2, 5) + "." + inteiro.substring (5, 8) + inteiro.substring (8, 11) + "," + decimal;
    else if (inteiro.length () == 12)
      vlAux = inteiro.substring (0, 3) + "." + inteiro.substring (3, 6) + "." + inteiro.substring (6, 9) + inteiro.substring (9, 12) + "," + decimal;
    else if (inteiro.length () == 13)
      vlAux = inteiro.substring (0, 1) + "." + inteiro.substring (1, 4) + "." + inteiro.substring (4, 7) + inteiro.substring (7, 10) + inteiro.substring (10, 13) + "," + decimal;
    return vlAux;
  }
  
  public static String preencheBrancosDireita (String texto, int tamanho)
  {
    StringBuffer x = new StringBuffer ();
    texto = texto != null ? texto : "";
    texto = texto.trim ();
    x.append (texto);
    int diferenca = tamanho - texto.length ();
    for (int i = 0; i < diferenca; i++)
      x.append (' ');
    return x.toString ();
  }
  
  public static String PreencheBrancosDireita (String texto, int tamanho)
  {
    StringBuffer x = new StringBuffer ();
    texto = texto.trim ();
    x.append (texto);
    int diferenca = tamanho - texto.length ();
    for (int i = 0; i < diferenca; i++)
      x.append (' ');
    return x.toString ();
  }
  
  public static String preencheBrancosEsquerda (String texto, int tamanho)
  {
    StringBuffer x = new StringBuffer ();
    texto = texto.trim ();
    x.append (texto);
    int diferenca = tamanho - texto.length ();
    for (int i = 0; i < diferenca; i++)
      x.insert (0, ' ');
    return x.toString ();
  }
  
  public static String preencheZeros (String numero, int tamanho)
  {
    StringBuffer x = new StringBuffer ();
    numero = numero != null ? numero : "";
    numero = numero.trim ();
    x.append (numero);
    int diferenca = tamanho - numero.length ();
    for (int i = 0; i < diferenca; i++)
      x.insert (0, '0');
    return x.toString ();
  }
  
  public static String preencheZerosDireita (String numero, int tamanho)
  {
    StringBuffer x = new StringBuffer ();
    numero = numero.trim ();
    x.append (numero);
    int diferenca = tamanho - numero.length ();
    for (int i = 0; i < diferenca; i++)
      x.append ('0');
    return x.toString ();
  }
  
  public static String RemoveZerosAEsquerda (String numero)
  {
    StringBuffer x = new StringBuffer ();
    String y = "";
    x.append (numero);
    for (int i = 0; i < numero.length () && x.charAt (i) == '0'; i++)
      x.setCharAt (i, ' ');
    y = x.toString ().trim ();
    if (y.equals (""))
      return "0";
    if (y.substring (0, 1).equals (","))
      return "0" + y;
    return y;
  }
  
  public static String tiraPonto (String campo, int numDecimais)
  {
    String inteiro = "";
    String decimal = "";
    if (campo.indexOf (".") != -1)
      {
	inteiro = campo.substring (0, campo.indexOf ("."));
	decimal = campo.substring (campo.indexOf (".") + 1);
	if (decimal.length () < numDecimais)
	  {
	    int fim = numDecimais - decimal.length ();
	    for (int i = 0; i < fim; i++)
	      decimal += "0";
	  }
      }
    else
      {
	inteiro = campo;
	for (int i = 0; i < numDecimais; i++)
	  decimal += "0";
      }
    return inteiro + decimal;
  }
  
  public static String tiraVirgula (String campo, int numDecimais)
  {
    String inteiro = "";
    String decimal = "";
    if (campo.indexOf (",") != -1)
      {
	inteiro = campo.substring (0, campo.indexOf (","));
	decimal = campo.substring (campo.indexOf (",") + 1);
	if (decimal.length () < numDecimais)
	  {
	    int fim = numDecimais - decimal.length ();
	    for (int i = 0; i < fim; i++)
	      decimal += "0";
	  }
      }
    else
      {
	inteiro = campo;
	for (int i = 0; i < numDecimais; i++)
	  decimal += "0";
      }
    return inteiro + decimal;
  }
  
  public static int calcularDVMod10 (String strNumero)
  {
    int soma = 0;
    int dvmod10 = 0;
    int produto = 0;
    byte[] b = strNumero.getBytes ();
    int i = 0;
    int peso = 0;
    while (i < b.length)
      {
	produto = (b[i] - 48) * (2 - peso % 2);
	soma += produto / 10 + produto % 10;
	i++;
	peso++;
      }
    dvmod10 = 10 - soma % 10;
    if (dvmod10 == 10)
      dvmod10 = 0;
    return dvmod10;
  }
  
  public static int testaCampo (String campo)
  {
    char[] c = campo.toCharArray ();
    int i;
    for (i = 0; i < c.length; i++)
      {
	if (! Character.isDigit (c[i]) && c[i] != '%' && c[i] != '/')
	  return 0;
      }
    return i;
  }
}
