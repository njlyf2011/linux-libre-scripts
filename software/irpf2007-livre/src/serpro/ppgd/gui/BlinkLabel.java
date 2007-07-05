/* BlinkLabel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.Color;

import javax.swing.JLabel;

import serpro.ppgd.negocio.util.FabricaUtilitarios;

public class BlinkLabel extends Thread
{
  private JLabel label;
  private long delay;
  private Color cOriginal;
  private Color cBlink;
  private boolean blink;
  private int repeticao;
  private boolean flOk;
  
  public BlinkLabel ()
  {
    /* empty */
  }
  
  public BlinkLabel (JLabel nLabel, long nDelay, Color nBlink, int nRepeticao)
  {
    cOriginal = nLabel.getForeground ();
    cBlink = nBlink;
    label = nLabel;
    delay = nDelay;
    repeticao = nRepeticao;
    flOk = true;
    blink = false;
  }
  
  public BlinkLabel (JLabel nLabel)
  {
    this (nLabel, (long) Integer.parseInt (FabricaUtilitarios.getProperties ().getProperty ("aplicacao.gui.blink.delay", "350")), ConstantesGlobaisGUI.COR_BRANCO, Integer.parseInt (FabricaUtilitarios.getProperties ().getProperty ("aplicacao.gui.blink.repeticao", "10")));
  }
  
  public void run ()
  {
    int count = repeticao;
    boolean opaco = label.isOpaque ();
    while (flOk)
      {
	blink = ! blink;
	if (blink)
	  label.setForeground (cOriginal);
	else
	  label.setForeground (cBlink);
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
    label.setOpaque (opaco);
    label.setForeground (cOriginal);
  }
  
  public void parar ()
  {
    flOk = false;
  }
}
