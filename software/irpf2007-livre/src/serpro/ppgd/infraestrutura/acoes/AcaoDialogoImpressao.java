/* AcaoDialogoImpressao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.acoes;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import serpro.ppgd.infraestrutura.util.DialogImprimirRelatorio;

public class AcaoDialogoImpressao extends AbstractAction
{
  public void actionPerformed (ActionEvent e)
  {
    new DialogImprimirRelatorio ();
  }
}
