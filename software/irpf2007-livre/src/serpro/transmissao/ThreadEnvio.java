/* ThreadEnvio - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.transmissao;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ThreadEnvio extends Thread
{
  static int READ_BUFFER_SIZE = 8192;
  String m_strNomeArquivoDecl;
  String m_strRecibo;
  String m_strErro;
  int m_iRetCode;
  ControleProgressoEnvio m_controleProgresso;
  
  public ThreadEnvio (ControleProgressoEnvio controleprogressoenvio, String string)
  {
    m_controleProgresso = controleprogressoenvio;
    m_strNomeArquivoDecl = string;
  }
  
  public void run ()
  {
    if (Declaracao2SRF () == true)
      m_iRetCode = 1;
    else
      m_iRetCode = 0;
    m_controleProgresso.fimTransmissao ();
  }
  
  public boolean Declaracao2SRF ()
  {
    String string = new String ();
    String string_0_ = "---------------------------12945144193168";
    Exception exception;
  while_178_:
    do
      {
	URLConnection urlconnection;
	DataOutputStream dataoutputstream;
      while_177_:
	do
	  {
	    boolean bool;
	    do
	      {
		try
		  {
		    m_strRecibo = "";
		    m_controleProgresso.conectando ();
		    try
		      {
			URL url = new URL (JUtil.HTTPS_SERVER);
			urlconnection = url.openConnection ();
			urlconnection.setRequestProperty ("Content-Type", "multipart/form-data; boundary=" + string_0_);
			urlconnection.setDoOutput (true);
			dataoutputstream = new DataOutputStream (urlconnection.getOutputStream ());
		      }
		    catch (IOException ioexception)
		      {
			m_strErro = "Ocorreu um erro durante a tentativa de conex\u00e3o com o servidor :\n" + ioexception.getMessage ();
			bool = false;
			break;
		      }
		    break while_177_;
		  }
		catch (Exception exception_1_)
		  {
		    exception = exception_1_;
		    break while_178_;
		  }
	      }
	    while (false);
	    return bool;
	  }
	while (false);
	do
	  {
	    boolean bool;
	    try
	      {
		if (! m_controleProgresso.cancelado ())
		  break;
		bool = false;
	      }
	    catch (Exception exception_2_)
	      {
		exception = exception_2_;
		break while_178_;
	      }
	    return bool;
	  }
	while (false);
	do
	  {
	    boolean bool;
	    try
	      {
		File file = new File (m_strNomeArquivoDecl);
		Long var_long = new Long (file.length () / 1024L);
		m_controleProgresso.iniciandoEnvio (var_long.intValue ());
		int i = 0;
		string += "--" + (String) string_0_ + "\r\n";
		string += "Content-Disposition: form-data; name=\"Filedata\"; filename=\"";
		try
		  {
		    Class.forName ("receitanet.ReceitanetApp");
		    string += "RECJBB";
		  }
		catch (Exception exception_3_)
		  {
		    string += "RNJAVA";
		  }
		string += file.getName ();
		string += "\"\r\n";
		string += "Content-Type: text/html\r\n\r\n";
		FileInputStream fileinputstream = new FileInputStream (file);
		InputStreamReader inputstreamreader = new InputStreamReader (fileinputstream, "iso-8859-1");
		BufferedReader bufferedreader = new BufferedReader (inputstreamreader);
		String string_4_ = "";
		dataoutputstream.writeBytes (string);
		char[] cs = new char[READ_BUFFER_SIZE];
		while (bufferedreader.read (cs, 0, READ_BUFFER_SIZE) != -1 && ! m_controleProgresso.cancelado ())
		  {
		    String string_5_ = new String (cs);
		    i += string_5_.length ();
		    dataoutputstream.writeBytes (string_5_);
		    m_controleProgresso.atualizaProgresso (i / 1024);
		  }
		bufferedreader.close ();
		if (! m_controleProgresso.cancelado ())
		  break;
		dataoutputstream.close ();
		bool = false;
	      }
	    catch (Exception exception_6_)
	      {
		exception = exception_6_;
		break while_178_;
	      }
	    return bool;
	  }
	while (false);
	boolean bool;
	try
	  {
	    string = "\r\n";
	    string += "--" + (String) string_0_ + "--\r\n\r\n";
	    dataoutputstream.writeBytes (string);
	    dataoutputstream.close ();
	    InputStream inputstream = urlconnection.getInputStream ();
	    StringBuffer stringbuffer = new StringBuffer ();
	    byte[] is = new byte[4096];
	    int i;
	    while ((i = inputstream.read (is)) != -1)
	      stringbuffer.append (new String (is, 0, i));
	    inputstream.close ();
	    bool = VerificaRetornoHTML (stringbuffer.toString ());
	  }
	catch (Exception exception_7_)
	  {
	    exception = exception_7_;
	    break;
	  }
	return bool;
      }
    while (false);
    Exception exception_8_ = exception;
    m_strErro = exception_8_.getMessage ();
    return false;
  }
  
  public boolean VerificaRetornoHTML (String string)
  {
    m_iRetCode = 0;
    m_strRecibo = "";
    m_strErro = "";
    String string_9_ = new String ("<!--MSGJAPP=[");
    int i = string.indexOf (string_9_);
    if (i != -1)
      {
	int i_10_ = string.indexOf ("]-->", i);
	if (i_10_ != -1)
	  {
	    i += string_9_.length ();
	    m_strErro = string.substring (i, i_10_);
	  }
	else
	  m_strErro = "Ocorreu um erro durante a transmiss\u00e3o da declara\u00e7\u00e3o.\nPor favor, tente novamente.";
      }
    else
      {
	int i_11_ = string.indexOf ("HC");
	if (i_11_ != -1)
	  {
	    m_strRecibo = string;
	    m_iRetCode = 1;
	  }
	else
	  m_strErro = "Ocorreu um erro durante a transmiss\u00e3o da declara\u00e7\u00e3o.\nPor favor, tente novamente.";
      }
    if (m_iRetCode == 1)
      return true;
    return false;
  }
  
  public String getLastError ()
  {
    return m_strErro;
  }
  
  public void setRetCode (int i)
  {
    m_iRetCode = i;
  }
  
  public int getRetCode ()
  {
    return m_iRetCode;
  }
  
  public void setError (String string)
  {
    m_strErro = string;
  }
  
  public String getRecibo ()
  {
    return m_strRecibo;
  }
}
