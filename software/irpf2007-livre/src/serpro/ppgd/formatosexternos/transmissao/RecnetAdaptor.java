/* RecnetAdaptor - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.transmissao;
import java.awt.Cursor;
import java.io.File;

import javax.swing.JDialog;

import serpro.ppgd.gui.UtilitariosGUI;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.negocio.util.UtilitariosArquivo;
import serpro.ppgd.negocio.util.UtilitariosString;
import serpro.transmissao.JDeclaracao;
import serpro.transmissao.JParametrosNet;

import srf.irpf.util.Mestre21J;

public class RecnetAdaptor
{
  private static final String LABEL_TITULO = "Transmitir declara\u00e7\u00e3o";
  private static final String MSG_ERRO_DEFAULT = "A vers\u00e3o do Receitanet instalada em seu computador n\u00e3o transmite declara\u00e7\u00f5es do " + ConstantesGlobais.NOME_PROGRAMA_EXTENSO + "." + "\n" + " V\u00e1 ao site da Receita Federal, fa\u00e7a um download  da nova vers\u00e3o do programa Receitanet e instale-o.";
  public static final String MSG_ERRO_VERSAO_RECNETINVALIDA = FabricaUtilitarios.getProperties ().getProperty ("msgErroVersaoRecNet", MSG_ERRO_DEFAULT);
  
  public static int chamarRecNetWeb (JParametrosNet jParam, Class classeBase)
  {
    File f = new File ("imagens");
    if (! f.exists () || f.isFile ())
      f.mkdir ();
    UtilitariosArquivo.copiaArquivoDoJar ("/cacerts", FabricaUtilitarios.getPathCompletoDirAplicacao () + File.separator + "cacerts", classeBase.getClass ());
    UtilitariosArquivo.copiaArquivoDoJar ("/imagens/logotxt_srf.gif", "./imagens/logotxt_srf.gif", classeBase.getClass ());
    UtilitariosArquivo.copiaArquivoDoJar ("/imagens/transmitindo.gif", "./imagens/transmitindo.gif", classeBase.getClass ());
    UtilitariosArquivo.copiaArquivoDoJar ("/imagens/wait.gif", "./imagens/wait.gif", classeBase.getClass ());
    JDeclaracao oDeclaracao = new JDeclaracao ();
    String path = FabricaUtilitarios.getPathCompletoDirAplicacao () + "/cacerts";
    String titulo = FabricaUtilitarios.getProperties ().getProperty ("titulo");
    JDialog dialog = new JDialog (UtilitariosGUI.tentaObterJanelaPrincipal (), titulo, true);
    int iRet = oDeclaracao.EnviarDeclaracao (dialog, jParam, path);
    f = new File (FabricaUtilitarios.getPathCompletoDirAplicacao () + "/cacerts");
    f.delete ();
    return iRet;
  }
  
  public static int chamarRecNetWindows (String nomeJanelaChamadora, JParametrosNet jParam, int usarCertificacao, Class classeBase) throws Exception
  {
    UtilitariosGUI.tentaObterJanelaPrincipal ().setCursor (Cursor.getPredefinedCursor (3));
    Mestre21J mestre = Mestre21J.getInstance ();
    int iVersaoRecnet = mestre.getRecnetVersion ();
    LogPPGD.debug ("Vers\u00e3o do Receitanet: " + iVersaoRecnet);
    if (iVersaoRecnet == 0)
      {
	UtilitariosGUI.tentaObterJanelaPrincipal ().setCursor (Cursor.getDefaultCursor ());
	throw new Exception ("N\u00e3o foi possivel identificar a vers\u00e3o do Receitanet.\n" + UtilitariosString.expandeStringHTML (mestre.getMensagem (), 40));
      }
    if (iVersaoRecnet < ConstantesGlobais.VERSAORECNET)
      {
	UtilitariosGUI.tentaObterJanelaPrincipal ().setCursor (Cursor.getDefaultCursor ());
	throw new Exception (MSG_ERRO_VERSAO_RECNETINVALIDA);
      }
    String dadosAssinatura = "";
    String strArqDeclaracao = jParam.m_strNomeArqDeclaracao;
    String titulo = nomeJanelaChamadora;
    int ret = mestre.EnviarDeclaracaoEx (titulo, strArqDeclaracao, usarCertificacao, dadosAssinatura);
    jParam.m_strMensagem = mestre.getMensagem ();
    return ret;
  }
  
  public static RetornoTransmissao transmitirDeclaracao (String nomeJanelaChamadora, String path, int usarCertificacao, Class classeBase) throws Exception
  {
    RetornoTransmissao retorno = null;
    LogPPGD.debug ("Path de transmiss\u00e3o: " + path);
    String osName = System.getProperty ("os.name").toLowerCase ();
    LogPPGD.debug ("Sistema Operacional: " + osName);
    JParametrosNet jParam = new JParametrosNet (path);
    int ret;
    if (osName.indexOf ("windows") == -1)
      {
	LogPPGD.debug ("chama RecNetWeb");
	ret = chamarRecNetWeb (jParam, classeBase);
      }
    else
      {
	try
	  {
	    LogPPGD.debug ("chama RecNet Windows");
	    ret = chamarRecNetWindows (nomeJanelaChamadora, jParam, usarCertificacao, classeBase);
	  }
	catch (Exception e)
	  {
	    throw new Exception (e.getMessage ());
	  }
      }
    UtilitariosGUI.tentaObterJanelaPrincipal ().setCursor (Cursor.getDefaultCursor ());
    LogPPGD.debug ("Retorno do Receitanet: " + ret);
    LogPPGD.debug ("mensagem do Receitanet: " + jParam.getValueMensagem ());
    retorno = new RetornoTransmissao (ret, jParam.getValueMensagem ());
    return retorno;
  }
}
