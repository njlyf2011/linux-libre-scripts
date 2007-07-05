/* NavegadorHtml - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.Vector;

import javax.swing.JOptionPane;

import serpro.ppgd.negocio.util.LogPPGD;

public class NavegadorHtml
{
  private static String[] exec = null;
  private static ClassLoader classLoader;
  private static String LABEL_TITULO_NAVEGADOR;
  private static String ERRO_NAVEGADOR;
  
  static
  {
    try
      {
	if (System.getProperty ("os.name").startsWith ("Mac"))
	  classLoader = new URLClassLoader (new URL[] { new File ("/System/Library/Java").toURL () });
      }
    catch (Exception e)
      {
	e.getStackTrace ();
      }
    LABEL_TITULO_NAVEGADOR = "Navegador Ausente";
    ERRO_NAVEGADOR = "Nenhum navegador web foi encontrado nesta m\u00e1quina.\nProceda a instala\u00e7\u00e3o de um navegador de sua prefer\u00eancia\n(p. ex. Internet Explorer, Netscape, Mozilla, Opera etc.)\ne repita a opera\u00e7\u00e3o.";
  }
  
  public static String[] localizarNavegadores ()
  {
    String[] exec = null;
    String osName = System.getProperty ("os.name");
    if (osName.startsWith ("Windows"))
      {
	if (osName.indexOf ("9") != -1 || osName.indexOf ("M") != -1)
	  exec = new String[] { "command.com" };
	else
	  exec = new String[] { "cmd.exe /K start  " };
      }
    else if (System.getProperty ("os.name").startsWith ("Mac"))
      {
	Vector browsers = new Vector ();
	try
	  {
	    Process p = Runtime.getRuntime ().exec ("which open");
	    if (p.waitFor () == 0)
	      {
		browsers.add ("open ");
		browsers.add (" safari ");
	      }
	  }
	catch (IOException e)
	  {
	    e.getStackTrace ();
	  }
	catch (InterruptedException e)
	  {
	    e.getStackTrace ();
	  }
	if (browsers.size () == 0)
	  exec = null;
	else
	  exec = (String[]) browsers.toArray (new String[0]);
      }
    else
      {
	Vector browsers = new Vector ();
	String[] browsersNames = { "firebird", "mozilla", "opera", "galeon", "konqueror", "netscape", "firefox" };
	try
	  {
	    for (int loop = 0; loop < browsersNames.length; loop++)
	      {
		StringBuffer sb = new StringBuffer ("which ");
		sb.append (browsersNames[loop]);
		Process p = Runtime.getRuntime ().exec (sb.toString ());
		if (p.waitFor () == 0)
		  {
		    sb = new StringBuffer (browsersNames[loop]);
		    sb.append (" ");
		    browsers.add (sb.toString ());
		  }
	      }
	  }
	catch (IOException e)
	  {
	    e.getStackTrace ();
	  }
	catch (InterruptedException e)
	  {
	    e.getStackTrace ();
	  }
	try
	  {
	    Process p = Runtime.getRuntime ().exec ("which xterm");
	    if (p.waitFor () == 0)
	      {
		p = Runtime.getRuntime ().exec ("which lynx");
		if (p.waitFor () == 0)
		  browsers.add ("xterm -e lynx ");
	      }
	  }
	catch (IOException e)
	  {
	    e.getStackTrace ();
	  }
	catch (InterruptedException e)
	  {
	    e.getStackTrace ();
	  }
	if (browsers.size () == 0)
	  exec = null;
	else
	  exec = (String[]) browsers.toArray (new String[0]);
      }
    return exec;
  }
  
  public static void executarNavegadorComMsgErro (String url)
  {
    try
      {
	executarNavegador (url);
      }
    catch (IOException e)
      {
	JOptionPane.showMessageDialog (null, ERRO_NAVEGADOR, LABEL_TITULO_NAVEGADOR, 2);
      }
  }
  
  public static void executarNavegador (String url) throws IOException, MalformedURLException
  {
    url = normalizaUrl (url);
    exec = localizarNavegadores ();
    if (exec == null || exec.length == 0 || System.getProperty ("os.name").startsWith ("Mac"))
      {
	if (System.getProperty ("os.name").startsWith ("Mac"))
	  {
	    boolean success = false;
	    try
	      {
		Class nSWorkspace;
		if (new File ("/System/Library/Java/com/apple/cocoa/application/NSWorkspace.class").exists ())
		  nSWorkspace = Class.forName ("com.apple.cocoa.application.NSWorkspace", true, classLoader);
		else
		  nSWorkspace = Class.forName ("com.apple.cocoa.application.NSWorkspace");
		Method sharedWorkspace = nSWorkspace.getMethod ("sharedWorkspace", new Class[0]);
		Object workspace = sharedWorkspace.invoke (null, new Object[0]);
		Method openURL = nSWorkspace.getMethod ("openURL", new Class[] { Class.forName ("java.net.URL") });
		success = ((Boolean) openURL.invoke (workspace, new Object[] { new URL (url) })).booleanValue ();
	      }
	    catch (Exception x)
	      {
		x.getStackTrace ();
	      }
	    if (! success)
	      {
		try
		  {
		    Class mrjFileUtils = Class.forName ("com.apple.mrj.MRJFileUtils");
		    Method openURL = mrjFileUtils.getMethod ("openURL", new Class[] { Class.forName ("java.lang.String") });
		    openURL.invoke (null, new Object[] { url });
		  }
		catch (Exception x)
		  {
		    LogPPGD.erro (x.getMessage ());
		    throw new IOException ("Falha na abertura do navegador");
		  }
	      }
	  }
	else
	  throw new IOException ("Linha de comando inv\u00e1lida");
      }
    else
      {
	String osName = System.getProperty ("os.name");
	if (osName.startsWith ("Windows"))
	  {
	    if ((osName.indexOf ("9") != -1 || osName.indexOf ("M") != -1) && url.startsWith ("file://"))
	      url = url.substring (8, url.length ());
	  }
	else
	  {
	    try
	      {
		new URL (url);
	      }
	    catch (MalformedURLException ex)
	      {
		throw new MalformedURLException ("Url incorreta");
	      }
	  }
	StringBuffer sb = new StringBuffer (url.toString ().length ());
	for (int i = 0; i < url.length (); i++)
	  {
	    char c = url.charAt (i);
	    if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c == '.' || c == ':' || c == '&' || c == '@' || c == '/' || c == '?' || c == '%' || c == '+' || c == '=' || c == '#' || c == '-' || c == '\\')
	      sb.append (c);
	    else
	      {
		c &= '\u00ff';
		if (c < '\020')
		  sb.append ("%0" + Integer.toHexString (c));
		else
		  sb.append ("%" + Integer.toHexString (c));
	      }
	  }
	String[] messageArray = new String[1];
	messageArray[0] = sb.toString ();
	String command = null;
	url = sb.toString ();
	boolean found = false;
	try
	  {
	    for (int i = 0; i < exec.length && ! found; i++)
	      {
		try
		  {
		    command = MessageFormat.format (exec[i], messageArray);
		    command += (String) url;
		    Process p = null;
		    if (osName.startsWith ("Windows"))
		      {
			if (osName.indexOf ("9") != -1 || osName.indexOf ("M") != -1)
			  {
			    url = "\"" + url.replaceAll ("%20", " ") + "\"";
			    LogPPGD.debug ("caminho->" + url);
			    p = Runtime.getRuntime ().exec ("command.com /c start " + url);
			  }
			else
			  p = Runtime.getRuntime ().exec (command);
		      }
		    else
		      p = Runtime.getRuntime ().exec (command);
		    for (int j = 0; j < 2; j++)
		      {
			try
			  {
			    Thread.sleep (1000L);
			  }
			catch (InterruptedException inte)
			  {
			    inte.getStackTrace ();
			  }
		      }
		    if (p.exitValue () == 0)
		      found = true;
		  }
		catch (IOException x)
		  {
		    x.getStackTrace ();
		  }
	      }
	    if (! found)
	      throw new IOException ("Nenhum navegador foi localizado.");
	  }
	catch (IllegalThreadStateException e)
	  {
	    e.getStackTrace ();
	  }
      }
  }
  
  private static String normalizaUrl (String url)
  {
    StringBuffer sb = new StringBuffer (url);
    if (url.startsWith ("file:/"))
      {
	if (url.charAt (6) != '/')
	  {
	    sb = new StringBuffer ("file://");
	    sb.append (url.substring (6));
	  }
	if (url.charAt (7) != '/')
	  {
	    sb = new StringBuffer ("file:///");
	    sb.append (url.substring (7));
	  }
      }
    return url = sb.toString ();
  }
  
  public static void main (String[] args)
  {
    String s = "Arquivos%20de%202005";
    System.out.println ("string->" + s.replaceAll ("%20", " "));
  }
}
