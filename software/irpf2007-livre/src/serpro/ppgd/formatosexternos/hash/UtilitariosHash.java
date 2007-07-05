/* UtilitariosHash - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.hash;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import serpro.hash.Crc32;
import serpro.ppgd.formatosexternos.hash.excecao.HashIncorretoException;
import serpro.util.PLong;

public class UtilitariosHash
{
  public static void geraCRCArquivo (String pArquivo, String pArquiHash, String codificacao) throws HashIncorretoException
  {
    PLong pLong = new PLong ();
    Crc32 crc32 = new Crc32 ();
    String hashCalculado = null;
    long hashCalculadoLinhaAnterior = 0L;
    try
      {
	BufferedReader in = null;
	if (codificacao != null)
	  in = new BufferedReader (new InputStreamReader (Thread.currentThread ().getContextClassLoader ().getResourceAsStream (pArquivo), codificacao));
	else
	  in = new BufferedReader (new InputStreamReader (Thread.currentThread ().getContextClassLoader ().getResourceAsStream (pArquivo)));
	String linha;
	while ((linha = in.readLine ()) != null)
	  {
	    if (hashCalculadoLinhaAnterior != 0L)
	      pLong.setValue (hashCalculadoLinhaAnterior);
	    long hash = crc32.CalcCrc32 (linha, linha.length (), pLong);
	    hashCalculadoLinhaAnterior = hash;
	    hashCalculado = crc32.getStrCrc32 ();
	  }
	in.close ();
	File arqHash = new File (pArquiHash);
	if (arqHash.exists ())
	  arqHash.delete ();
	arqHash.createNewFile ();
	BufferedWriter outHsh = null;
	if (codificacao != null)
	  outHsh = new BufferedWriter (new OutputStreamWriter (new FileOutputStream (arqHash), codificacao));
	else
	  outHsh = new BufferedWriter (new OutputStreamWriter (new FileOutputStream (arqHash)));
	outHsh.write (hashCalculado);
	outHsh.close ();
      }
    catch (IOException e)
      {
	e.printStackTrace ();
	throw new HashIncorretoException (e);
      }
  }
  
  public static void validarCRCArquivo (String pArquivo, String pArquiHash, String codificacao) throws HashIncorretoException
  {
    PLong pLong = new PLong ();
    Crc32 crc32 = new Crc32 ();
    String hashCalculado = null;
    long hashCalculadoLinhaAnterior = 0L;
    String hashLido = "";
    try
      {
	BufferedReader inHsh = null;
	if (codificacao != null)
	  inHsh = new BufferedReader (new InputStreamReader (Thread.currentThread ().getContextClassLoader ().getResourceAsStream (pArquiHash), codificacao));
	else
	  inHsh = new BufferedReader (new InputStreamReader (Thread.currentThread ().getContextClassLoader ().getResourceAsStream (pArquiHash)));
	hashLido = inHsh.readLine ();
	BufferedReader in = null;
	if (codificacao != null)
	  in = new BufferedReader (new InputStreamReader (Thread.currentThread ().getContextClassLoader ().getResourceAsStream (pArquivo), codificacao));
	else
	  in = new BufferedReader (new InputStreamReader (Thread.currentThread ().getContextClassLoader ().getResourceAsStream (pArquivo)));
	String linha;
	while ((linha = in.readLine ()) != null)
	  {
	    if (hashCalculadoLinhaAnterior != 0L)
	      pLong.setValue (hashCalculadoLinhaAnterior);
	    long hash = crc32.CalcCrc32 (linha, linha.length (), pLong);
	    hashCalculadoLinhaAnterior = hash;
	    hashCalculado = crc32.getStrCrc32 ();
	  }
	in.close ();
	inHsh.close ();
	if (! hashLido.equals (hashCalculado))
	  throw new HashIncorretoException ("As informa\u00e7\u00f5es da declara\u00e7\u00e3o foram corrompidas ap\u00f3s sua grava\u00e7\u00e3o no registro.");
      }
    catch (IOException e)
      {
	e.printStackTrace ();
	throw new HashIncorretoException (e);
      }
  }
}
