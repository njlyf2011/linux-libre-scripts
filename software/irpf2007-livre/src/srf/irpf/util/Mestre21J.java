/* Mestre21J - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package srf.irpf.util;
import java.io.File;

import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.negocio.util.UtilitariosArquivo;

public class Mestre21J
{
  private String mensagemRetorno = new String ("");
  private static Mestre21J instance;
  /*synthetic*/ static Class class$srf$irpf$util$Mestre21J;
  
  public native int EnviarDeclaracaoEx (String string, String string_0_, int i, String string_1_);
  
  public native int GetRecnetVersion ();
  
  private Mestre21J ()
  {
    /* empty */
  }
  
  public static Mestre21J getInstance () throws Exception
  {
    Mestre21J mestre21j;
    try
      {
	if (instance == null)
	  {
	    String nomeDLL = "Mestre21J.dll";
	    String dirLib = FabricaUtilitarios.getPathCompletoDirLib ();
	    String pathLib = dirLib + "/" + nomeDLL;
	    File flDirLib = new File (dirLib);
	    if (! flDirLib.exists ())
	      flDirLib.mkdirs ();
	    File flMestre21DLL = new File (pathLib);
	    if (! flMestre21DLL.exists ())
	      UtilitariosArquivo.copiaArquivoDoJar ("/lib/" + nomeDLL, pathLib, class$srf$irpf$util$Mestre21J == null ? class$srf$irpf$util$Mestre21J = class$ ("srf.irpf.util.Mestre21J") : class$srf$irpf$util$Mestre21J);
	    System.load (pathLib);
	    instance = new Mestre21J ();
	  }
	mestre21j = instance;
      }
    catch (UnsatisfiedLinkError errLink)
      {
	throw new Exception (errLink.getMessage ());
      }
    catch (SecurityException errSec)
      {
	throw new Exception (errSec.getMessage ());
      }
    return mestre21j;
  }
  
  public int enviarDeclaracaoEx (String tituloJanelaChamadora, String arquivoDeclaracao, int usarCertificacao, String dadosAssinatura)
  {
    return EnviarDeclaracaoEx (tituloJanelaChamadora, arquivoDeclaracao, usarCertificacao, dadosAssinatura);
  }
  
  public int getRecnetVersion ()
  {
    return GetRecnetVersion ();
  }
  
  public String getMensagem ()
  {
    return mensagemRetorno;
  }
  
  public static void main (String[] args) throws Exception
  {
    Mestre21J mestre = getInstance ();
    int iVersaoRecnet = mestre.getRecnetVersion ();
    LogPPGD.debug ("Vers\u00e3o do Receitanet :" + iVersaoRecnet);
    if (iVersaoRecnet == 0)
      LogPPGD.debug ("N\u00e3o foi possivel identificar a vers\u00e3o do Receitanet.\nErro=" + mestre.getMensagem ());
    else
      {
	String dadosAssinatura = "";
	String strArqDeclaracao = "C:\\Declaracoes\\IRPF2004\\v141\\00003255034-IRPF-2004-2003-ORIGI.DEC";
	int ret = mestre.EnviarDeclaracaoEx ("TEste", strArqDeclaracao, 0, dadosAssinatura);
	LogPPGD.debug ("\nretorno :" + ret);
	LogPPGD.debug ("mensagem:" + mestre.getMensagem ());
      }
  }
  
  /*synthetic*/ static Class class$ (String x0)
  {
    Class var_class;
    try
      {
	var_class = Class.forName (x0);
      }
    catch (ClassNotFoundException x1)
      {
	throw new NoClassDefFoundError (x1.getMessage ());
      }
    return var_class;
  }
}
