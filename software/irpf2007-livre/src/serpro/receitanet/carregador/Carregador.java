/* Carregador - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.receitanet.carregador;
import serpro.receitanet.Receitanet;

public class Carregador
{
  private static final String NOME_DA_CLASSE = "ControleReceitanet";
  
  public static Receitanet carregar ()
  {
    Configuracao c = new Configuracao ();
    JARClassLoader j = JARClassLoader.criar (c.obterCaminhoDoPrograma ());
    Class classe = j.obterClasse (j.completarNomeDaClasse ("ControleReceitanet"));
    Receitanet receitanet;
    try
      {
	receitanet = (Receitanet) classe.newInstance ();
      }
    catch (InstantiationException e)
      {
	throw new ExcecaoErroDeLeitura ("Classe carregada do JAR \u00e9 inv\u00e1lida.", e);
      }
    catch (IllegalAccessException e)
      {
	throw new ExcecaoErroDeLeitura ("Classe carregada do JAR \u00e9 inv\u00e1lida.", e);
      }
    return receitanet;
  }
}
