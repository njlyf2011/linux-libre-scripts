/* AcaoMenuFerramentas - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.app.acoes;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import serpro.ppgd.infraestrutura.PlataformaPPGD;

public class AcaoMenuFerramentas extends AbstractAction
{
  public void actionPerformed (ActionEvent e)
  {
    String acao = e.getActionCommand ();
    JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getSwingEngine ().getRootComponent (), "A\u00e7\u00e3o: " + acao);
  }
}
