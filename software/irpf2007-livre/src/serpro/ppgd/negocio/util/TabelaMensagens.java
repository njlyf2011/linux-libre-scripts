/* TabelaMensagens - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.util;
import java.util.Properties;

public class TabelaMensagens
{
  private Properties arquivoMensagens = null;
  private static TabelaMensagens tabelaMensagens = null;
  
  public static TabelaMensagens getTabelaMensagens ()
  {
    String arquivo = FabricaUtilitarios.getProperties ().getProperty ("arquivoMsgErro", "MsgErro.txt");
    if (tabelaMensagens == null)
      tabelaMensagens = new TabelaMensagens (arquivo);
    return tabelaMensagens;
  }
  
  private TabelaMensagens ()
  {
    /* empty */
  }
  
  private TabelaMensagens (String pPathRelativoParaArquivoMensagens)
  {
    arquivoMensagens = UtilitariosArquivo.loadProperties ("/" + pPathRelativoParaArquivoMensagens, this.getClass ());
  }
  
  public String msg (String pChave)
  {
    if (! arquivoMensagens.containsKey (pChave))
      {
	LogPPGD.erro ("Mensagem " + pChave + " n\u00e3o est\u00e1 cadastrada no Mensagens.properties!");
	return "C\u00f3digo " + pChave + " de Mensagem n\u00e3o encontrado";
      }
    return arquivoMensagens.getProperty (pChave, "");
  }
  
  public String msg (String pChave, String[] pParams)
  {
    String retorno = null;
    retorno = msg (pChave);
    StringBuffer retornoBuff = new StringBuffer (retorno);
    if (pParams != null)
      {
	for (int i = 0; i < pParams.length; i++)
	  {
	    String chave = "@" + String.valueOf (i + 1);
	    int posicaoParam = retornoBuff.indexOf (chave);
	    if (posicaoParam != -1)
	      retornoBuff.replace (posicaoParam, posicaoParam + 2, pParams[i]);
	  }
      }
    return retornoBuff.toString ();
  }
  
  protected void finalize () throws Throwable
  {
    super.finalize ();
  }
  
  public static void main (String[] args)
  {
    TabelaMensagens t = getTabelaMensagens ();
    System.out.println ("msg1->" + t.msg ("5"));
    System.out.println ("msgParam->" + t.msg ("15", new String[] { "Joselito Messias Lobo", "25" }));
  }
}
