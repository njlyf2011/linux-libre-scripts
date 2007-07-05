/* BlinkBorder - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.TitledBorder;

import serpro.ppgd.negocio.util.FabricaUtilitarios;

public class BlinkBorder extends Thread
{
  int repeticao;
  boolean continuaExecucao;
  private long delay;
  private boolean blink;
  private JComponent componente;
  
  public BlinkBorder ()
  {
    /* empty */
  }
  
  public BlinkBorder (JComponent componente)
  {
    this.componente = componente;
    repeticao = Integer.parseInt (FabricaUtilitarios.getProperties ().getProperty ("aplicacao.gui.blink.repeticao", "10"));
    delay = (long) Integer.parseInt (FabricaUtilitarios.getProperties ().getProperty ("aplicacao.gui.blink.delay", "350"));
  }
  
  public BlinkBorder (JComponent componente, long delay, int repeticao)
  {
    this.componente = componente;
    this.delay = delay;
    this.repeticao = repeticao;
  }
  
  public void run ()
  {
    int count = repeticao;
    continuaExecucao = true;
    TitledBorder bordaOriginal = (TitledBorder) componente.getBorder ();
    TitledBorder bordaBlink = BorderFactory.createTitledBorder (bordaOriginal.getTitle ());
    bordaBlink.setTitleColor (ConstantesGlobaisGUI.COR_BRANCO);
    bordaBlink.setTitleFont (bordaOriginal.getTitleFont ().deriveFont (0));
    while (continuaExecucao)
      {
	blink = ! blink;
	if (blink)
	  componente.setBorder (bordaOriginal);
	else
	  componente.setBorder (bordaBlink);
	if (repeticao > 0 && --count <= 0)
	  parar ();
	try
	  {
	    Thread.sleep (delay);
	  }
	catch (Exception err)
	  {
	    err.printStackTrace ();
	  }
      }
    componente.setBorder (bordaOriginal);
  }
  
  public void parar ()
  {
    continuaExecucao = false;
  }
}
