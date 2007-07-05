/* VerificarPendencias - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.pendencia;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;

import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.util.FabricaUtilitarios;

public abstract class VerificarPendencias extends AbstractAction
{
  protected ObjetoNegocio objetoNegocio = null;
  
  public VerificarPendencias (ObjetoNegocio pObj)
  {
    objetoNegocio = pObj;
  }
  
  protected abstract void preparaVerificacaoPendencias (List list);
  
  public void actionPerformed (ActionEvent e)
  {
    preparaVerificacaoPendencias (FabricaUtilitarios.verificarPendencias (objetoNegocio));
    finalizaVerificacaoPendencias ();
  }
  
  protected abstract void finalizaVerificacaoPendencias ();
}
