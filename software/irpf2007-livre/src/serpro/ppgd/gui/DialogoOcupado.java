/* DialogoOcupado - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

public class DialogoOcupado extends JWindow
{
  private boolean duracaoIndeterminada = true;
  private int min = 0;
  private int max = 100;
  private JLabel mensagem;
  private JProgressBar progress;
  
  public DialogoOcupado ()
  {
    buildComponente ("");
  }
  
  public DialogoOcupado (String msg)
  {
    buildComponente (msg);
  }
  
  public DialogoOcupado (int aMin, int aMax, String msg)
  {
    duracaoIndeterminada = false;
    min = aMin;
    max = aMax;
    buildComponente (msg);
  }
  
  private void buildComponente (String msg)
  {
    JPanel content = new JPanel ();
    progress = new JProgressBar (min, max);
    progress.setIndeterminate (duracaoIndeterminada);
    mensagem = new JLabel (msg);
    content.setLayout (new BorderLayout ());
    content.add (progress, "Center");
    content.add (mensagem, "South");
    content.setBorder (BorderFactory.createCompoundBorder (BorderFactory.createLineBorder (Color.black), BorderFactory.createEmptyBorder (6, 6, 6, 6)));
    setContentPane (content);
  }
  
  public void atualiza (final String novaMsg)
  {
    try
      {
	SwingUtilities.invokeLater (new Runnable ()
	{
	  public void run ()
	  {
	    mensagem.setText (novaMsg);
	    if (! duracaoIndeterminada)
	      progress.setValue (progress.getValue () + 1);
	  }
	});
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public void atualiza ()
  {
    try
      {
	SwingUtilities.invokeLater (new Runnable ()
	{
	  public void run ()
	  {
	    if (! duracaoIndeterminada)
	      progress.setValue (progress.getValue () + 1);
	  }
	});
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public void atualiza (final int valor, final String novaMsg)
  {
    try
      {
	SwingUtilities.invokeLater (new Runnable ()
	{
	  public void run ()
	  {
	    mensagem.setText (novaMsg);
	    progress.setValue (valor);
	  }
	});
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public void atualiza (final int valor)
  {
    try
      {
	SwingUtilities.invokeLater (new Runnable ()
	{
	  public void run ()
	  {
	    progress.setValue (valor);
	  }
	});
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public void finaliza ()
  {
    try
      {
	SwingUtilities.invokeLater (new Runnable ()
	{
	  public void run ()
	  {
	    DialogoOcupado.this.setVisible (false);
	    DialogoOcupado.this.dispose ();
	  }
	});
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public static DialogoOcupado exibeDialogo ()
  {
    DialogoOcupado dialogo = new DialogoOcupado ();
    dialogo.pack ();
    dialogo.setSize (250, 30);
    dialogo.setLocationRelativeTo (null);
    dialogo.setVisible (true);
    return dialogo;
  }
  
  public static DialogoOcupado exibeDialogo (String msg)
  {
    DialogoOcupado dialogo = new DialogoOcupado (msg);
    dialogo.pack ();
    dialogo.setSize (250, 40);
    dialogo.setLocationRelativeTo (null);
    dialogo.setVisible (true);
    return dialogo;
  }
  
  public static DialogoOcupado exibeDialogo (int min, int max, String msg)
  {
    DialogoOcupado dialogo = new DialogoOcupado (min, max, msg);
    dialogo.pack ();
    dialogo.setSize (250, 40);
    dialogo.setLocationRelativeTo (null);
    dialogo.setVisible (true);
    return dialogo;
  }
  
  public boolean isDuracaoIndeterminada ()
  {
    return duracaoIndeterminada;
  }
  
  public String getMensagem ()
  {
    return mensagem.getText ();
  }
  
  public static void main (String[] args)
  {
    System.out.println ("1");
    DialogoOcupado dialogo = exibeDialogo ("Aguarde...");
    System.out.println ("2");
  }
}
