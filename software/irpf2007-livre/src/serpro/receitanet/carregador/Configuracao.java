/* Configuracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.receitanet.carregador;
import java.io.File;
import java.util.prefs.Preferences;

public class Configuracao
{
  protected static final String CHAVE_CAMINHO = "Path";
  protected static final String CHAVE_VERSAO = "Versao";
  private static final String NO_RECEITANET = "br/gov/serpro/receitanet";
  private static Preferences preferencias_sistema = Preferences.systemRoot ().node ("br/gov/serpro/receitanet");
  private static Preferences preferencias_usuario = Preferences.userRoot ().node ("br/gov/serpro/receitanet");
  private File _caminho;
  private int _versao;
  
  public Configuracao ()
  {
    try
      {
	obterConfiguracoes (preferencias_sistema);
      }
    catch (ExcecaoInformacaoIncorreta e)
      {
	obterConfiguracoes (preferencias_usuario);
      }
  }
  
  private void obterConfiguracoes (Preferences preferencias)
  {
    int INVALIDA = -1;
    String caminho = preferencias.get ("Path", null);
    if (caminho == null)
      throw new ExcecaoInformacaoIncorreta ("Localiza\u00e7\u00e3o do Receitanet n\u00e3o foi encontrada nas configura\u00e7\u00f5es.");
    _caminho = new File (caminho, "receitanet.jar");
    _versao = preferencias.getInt ("Versao", -1);
    if (_versao == -1)
      throw new ExcecaoInformacaoIncorreta ("N\u00e3o foi poss\u00edvel determinar a vers\u00e3o do Receitanet.");
  }
  
  public File obterCaminhoDoPrograma ()
  {
    return _caminho;
  }
  
  public int obterVersao ()
  {
    return _versao;
  }
}
