/* NavegadorColecaoIf - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.ObjetoNegocio;

public interface NavegadorColecaoIf
{
  public void adiciona ();
  
  public void remove ();
  
  public void proximo ();
  
  public void anterior ();
  
  public void primeiro ();
  
  public void ultimo ();
  
  public void exibe (int i);
  
  public int getIndiceAtual ();
  
  public ObjetoNegocio getItem ();
  
  public Colecao getColecao ();
  
  public void addNavegadorColecaoListener (NavegadorColecaoListener navegadorcolecaolistener);
  
  public void removeNavegadorColecaoListener (NavegadorColecaoListener navegadorcolecaolistener);
}
