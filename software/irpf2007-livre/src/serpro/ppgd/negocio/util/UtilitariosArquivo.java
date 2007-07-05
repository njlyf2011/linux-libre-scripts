/* UtilitariosArquivo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class UtilitariosArquivo
{
  
  public static Properties loadProperties (String path, Class baseClass)
  {
    InputStream in = baseClass.getResourceAsStream (path);
    Properties p = new Properties ();
    try
      {
	if (in != null)
	  {
	    p.load (in);
	    LogPPGD.debug ("Propriedades carregadas do arquivo: " + path);
	  }
	else
	  LogPPGD.aviso ("Nao foi poss\u00edvel localizar o arquivo: " + path);
      }
    catch (IOException ioe)
      {
	LogPPGD.aviso ("N\u00e3o foi poss\u00edvel carregar arquivo: " + path);
	LogPPGD.erro ("Erro ao carregar arquivo.");
	ioe.printStackTrace ();
      }
    return p;
  }
  
  public static InputStream getResource (String path, Class baseClass)
  {
    InputStream in = baseClass.getResourceAsStream (path);
    if (in == null)
      LogPPGD.aviso ("Retornando um stream NULO para o arquivo: " + path);
    return in;
  }
  
  public static void copiaArquivoDoJar (String fromJarPath, String toPath, Class baseClass)
  {
    try
      {
	InputStream is = new BufferedInputStream (getResource (fromJarPath, baseClass));
	FileOutputStream file = new FileOutputStream (toPath);
	java.io.OutputStream o = new BufferedOutputStream (file);
	for (int b = is.read (); b != -1; b = is.read ())
	  o.write (b);
	o.flush ();
	o.close ();
      }
    catch (FileNotFoundException fnf)
      {
	LogPPGD.erro ("Arquivo de saida n\u00e3o encontrado.");
      }
    catch (IOException ioe)
      {
	LogPPGD.erro ("Erro na escrita do arquivo.");
      }
  }
  
  public static String extraiNomeArquivo (String nomeArquivoComPath)
  {
    return new File (nomeArquivoComPath).getName ();
  }
  
  public static String extraiPath (String nomeArquivo)
  {
    if (nomeArquivo.startsWith ("file:"))
      nomeArquivo = nomeArquivo.substring (5);
    int posjar = nomeArquivo.indexOf ('!');
    if (posjar != -1)
      nomeArquivo = nomeArquivo.substring (0, posjar);
    return nomeArquivo.substring (0, nomeArquivo.lastIndexOf (extraiNomeArquivo (nomeArquivo)));
  }
  
  public static String extraiNomeAquivoSemExtensao (String nomeArquivo)
  {
    return nomeArquivo.substring (0, nomeArquivo.lastIndexOf ("."));
  }
  
  public static String extraiExtensaoAquivo (String nomeArquivo)
  {
    return nomeArquivo.substring (nomeArquivo.lastIndexOf (".", nomeArquivo.length ()));
  }
  
  public static void copiaArquivo (String in, String outPath)
  {
    try
      {
	File source = null;
	int fileLength;
	char[] charBuff;

	source = new File (in);
	fileLength = (int) source.length ();
	charBuff = new char[fileLength];
	File flSaida = new File (outPath);
	if (! flSaida.exists ())
	  flSaida.mkdirs ();
	if (source.getParent ().equals (flSaida.getPath ()))
	  return;

	FileWriter fw = null;
	FileReader fr = null;
	BufferedReader br = null;
	BufferedWriter bw = null;

	try
	  {
	    outPath += "/" + source.getName ();
	    fr = new FileReader (in);
	    fw = new FileWriter (outPath);
	    br = new BufferedReader (fr);
	    bw = new BufferedWriter (fw);
	    while (br.read (charBuff, 0, fileLength) != -1)
	      bw.write (charBuff, 0, fileLength);
	  }
	finally
	  {
	    if (br != null)
	      br.close ();
	    if (bw != null)
	      bw.close ();
	  }
      }
    catch (FileNotFoundException _)
      {
      }
    catch (IOException _)
      {
      }
  }
  
  public static boolean ehDisquete (File pFile)
  {
    return pFile.getPath ().toUpperCase ().indexOf ("A:") != -1;
  }
  
  public static void apagaDiretorio (File pFile) throws IOException
  {
    File[] arquivos = pFile.listFiles ();
    for (int i = 0; i < arquivos.length; i++)
      {
	if (arquivos[i].isDirectory ())
	  apagaDiretorio (arquivos[i]);
	else
	  arquivos[i].delete ();
      }
    pFile.delete ();
  }
  
  public static File getRECCorrespondente (File aDec)
  {
    File retVal = null;
    String arqDEC = aDec.getPath ();
    int indiceUltimoSeparador = arqDEC.lastIndexOf (File.separator);
    String arqREC = arqDEC.substring (0, arqDEC.length () - 4) + ".REC";
    String cpf = arqDEC.substring (0, 11);
    File fileRec = new File (arqREC);
    if (! fileRec.exists ())
      {
	arqREC = arqDEC.substring (0, indiceUltimoSeparador + 1);
	arqREC += cpf.substring (0, 8);
	arqREC += ".REC";
	fileRec = new File (arqREC);
      }
    if (fileRec.exists ())
      retVal = fileRec;
    return retVal;
  }
  
  public static File getDisquete ()
  {
    File[] roots = File.listRoots ();
    for (int i = 0; i < roots.length; i++)
      {
	if (ehDisquete (roots[i]))
	  return roots[i];
      }
    return null;
  }
  
  public static void apagaDisquete ()
  {
    try
      {
	File[] arquivosDisquete = getDisquete ().listFiles ();
	for (int i = 0; i < arquivosDisquete.length; i++)
	  {
	    if (arquivosDisquete[i].isDirectory ())
	      apagaDiretorio (arquivosDisquete[i]);
	    else
	      arquivosDisquete[i].delete ();
	  }
      }
    catch (Exception exception)
      {
	/* empty */
      }
  }
  
  public static URL localizaArquivoEmClasspath (String aRecurso)
  {
    return (serpro.ppgd.negocio.util.UtilitariosArquivo.class).getResource (aRecurso);
  }
  
  public static String getPathAplicacao ()
  {
    URL url = (serpro.ppgd.negocio.util.FabricaUtilitarios.class).getResource ("/aplicacao.properties");
    String path;
    if (url == null)
      path = System.getProperty ("user.dir");
    else
      {
	URI uri = null;
	try
	  {
	    uri = new URI (url.getFile ());
	  }
	catch (URISyntaxException e)
	  {
	    path = System.getProperty ("user.dir");
	  }
	String s = uri.getPath ();
	if (s == null)
	  s = url.getPath ();
	path = extraiPath (s);
      }
    return path;
  }
  
  public static String getPathDados ()
  {
    String dirDados = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.diretorio.dados");
    if (dirDados.charAt (dirDados.length () - 1) != '/')
      dirDados += "/";
    dirDados += "aplicacao/dados";
    return dirDados;
  }
  
  public static String getPathImpressao ()
  {
    return getPathAplicacao () + "impressao";
  }
  
  public static String getPathGravadas ()
  {
    return getPathAplicacao () + "gravadas";
  }
  
  public static String getPathTransmitidas ()
  {
    return getPathAplicacao () + "transmitidas";
  }
  
  public static String getPathLib ()
  {
    return getPathAplicacao () + "lib";
  }
  
  public static void main (String[] args)
  {
    apagaDisquete ();
  }
  
}
