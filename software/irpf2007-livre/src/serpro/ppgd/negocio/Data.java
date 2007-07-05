/* Data - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.negocio.util.UtilitariosString;

public class Data extends Informacao
{
  public Data ()
  {
    this (null, "");
  }
  
  public Data (ObjetoNegocio owner, String nomeCampo)
  {
    super (owner, nomeCampo);
  }
  
  public String getConteudoFormatado ()
  {
    return UtilitariosString.formataData (asString ());
  }
  
  public void setConteudo (String conteudo)
  {
    String dataCorrigida = null;
    if (conteudo.indexOf ("/") != -1)
      super.setConteudo (UtilitariosString.corrigeDataFormatada (conteudo).replaceAll ("[/]", ""));
    else
      super.setConteudo (conteudo.replaceAll ("[/]", ""));
  }
  
  public void setConteudo (Date conteudo)
  {
    Calendar calendario = Calendar.getInstance (ConstantesGlobais.LOCALIDADE);
    calendario.setTime (conteudo);
    setConteudo (calendario);
  }
  
  public Date asDate ()
  {
    Calendar c = Calendar.getInstance ();
    String data = getConteudoFormatado ();
    StringTokenizer strTokens = new StringTokenizer (data, "/");
    int i = 0;
    int dia = 0;
    int mes = 1;
    int ano = 2;
    while (strTokens.hasMoreTokens ())
      {
	String tokenAtual = strTokens.nextToken ().trim ();
	switch (i)
	  {
	  case 0:
	    try
	      {
		dia = Integer.parseInt (tokenAtual);
	      }
	    catch (Exception e)
	      {
		dia = 0;
	      }
	    /* fall through */
	  case 1:
	    try
	      {
		mes = Integer.parseInt (tokenAtual);
	      }
	    catch (Exception e)
	      {
		mes = 0;
	      }
	    /* fall through */
	  case 2:
	    try
	      {
		ano = Integer.parseInt (tokenAtual);
	      }
	    catch (Exception e)
	      {
		ano = 0;
	      }
	    /* fall through */
	  default:
	    i++;
	  }
      }
    if (dia == 0 || ano == 0 || mes == 0)
      return null;
    c.set (ano, mes - 1, dia);
    return c.getTime ();
  }
  
  public boolean maisAntiga (Data pData)
  {
    Date pDataAsDate = pData.asDate ();
    if (pDataAsDate == null)
      return true;
    if (asDate () == null)
      return false;
    return asDate ().before (pDataAsDate);
  }
  
  public boolean maisNova (Data pData)
  {
    Date pDataAsDate = pData.asDate ();
    if (pDataAsDate == null)
      return true;
    if (asDate () == null)
      return false;
    return asDate ().after (pDataAsDate);
  }
  
  public boolean igual (Data pData)
  {
    Date pDataAsDate = pData.asDate ();
    if (pDataAsDate == null)
      return false;
    if (asDate () == null)
      return false;
    return asDate ().equals (pDataAsDate);
  }
  
  public void setConteudo (Calendar conteudo)
  {
    String dia = String.valueOf (conteudo.get (5));
    String mes = String.valueOf (conteudo.get (2) + 1);
    String ano = String.valueOf (conteudo.get (1));
    if (dia.length () < 2)
      dia = "0" + dia;
    if (mes.length () < 2)
      mes = "0" + mes;
    String stringConteudo = dia + mes + ano;
    setConteudo (stringConteudo);
  }
  
  public int compareTo (Object o)
  {
    return -1;
  }
  
  public static void main (String[] args)
  {
    Calendar calendar = Calendar.getInstance (ConstantesGlobais.LOCALIDADE);
    LogPPGD.debug (String.valueOf (calendar.get (2)));
  }
}
