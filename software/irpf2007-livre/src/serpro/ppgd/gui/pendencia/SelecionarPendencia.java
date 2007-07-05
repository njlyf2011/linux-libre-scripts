/* SelecionarPendencia - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.pendencia;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import serpro.ppgd.negocio.Pendencia;

public abstract class SelecionarPendencia extends MouseAdapter
{
  protected EditTablePendencia tablePendencia;
  
  public SelecionarPendencia (EditTablePendencia tablePendencia)
  {
    this.tablePendencia = tablePendencia;
  }
  
  public void mouseClicked (MouseEvent e)
  {
    if (e.getClickCount () == 1)
      {
	int i = tablePendencia.getSelectedRow ();
	if (i != -1)
	  {
	    Pendencia pendencia = ((TableModelPendencia) tablePendencia.getModel ()).getPendenciaAt (i);
	    if (pendencia != null)
	      selecaoCampoPendente (pendencia);
	  }
      }
  }
  
  protected abstract void selecaoCampoPendente (Pendencia pendencia);
}
