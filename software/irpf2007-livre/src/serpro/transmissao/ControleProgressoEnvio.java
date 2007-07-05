/* ControleProgressoEnvio - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.transmissao;

public interface ControleProgressoEnvio
{
  public void conectando ();
  
  public void iniciandoEnvio (int i);
  
  public void atualizaProgresso (int i);
  
  public void fimTransmissao ();
  
  public boolean cancelado ();
}
