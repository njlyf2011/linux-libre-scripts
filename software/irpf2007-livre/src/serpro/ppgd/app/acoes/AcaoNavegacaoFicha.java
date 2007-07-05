/* AcaoNavegacaoFicha - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.app.acoes;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;

public class AcaoNavegacaoFicha extends AbstractAction
{
  public void actionPerformed (ActionEvent e)
  {
    String acao = e.getActionCommand ();
    if (acao.equals ("ficha_anterior"))
      IRPFGuiUtil.nodoAnteriorArvore ();
    else if (acao.equals ("ficha_proxima"))
      IRPFGuiUtil.proximoNodoArvore ();
  }
}
