/* SplashScreen - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import serpro.ppgd.negocio.util.UtilitariosArquivo;

public class SplashScreen extends JWindow
{
  private static final String SPLASH_DEFAULT = "/icones/splash_ppgd.png";
  private boolean rodando = true;
  
  public SplashScreen (Frame parent, int segundos)
  {
    super (parent);
    URL urlSplash = UtilitariosArquivo.localizaArquivoEmClasspath ("/icones/splash_ppgd.png");
    int width = new ImageIcon (urlSplash).getIconWidth ();
    int height = new ImageIcon (urlSplash).getIconHeight ();
    init (segundos, new JLabel ("<HTML><body bgcolor=white><IMG width='" + width + "' height='" + height + "'  SRC='" + urlSplash + "' ><br><p align=right><br><a href=#>Clique na imagem para fechar anima&ccedil;&atilde;o</a> <br></body></HTML>"));
  }
  
  public SplashScreen (Frame parent, int segundos, JLabel lblIcon)
  {
    init (segundos, lblIcon);
  }
  
  public SplashScreen (Frame parent, int segundos, String filename)
  {
    super (parent);
    URL urlSplash = UtilitariosArquivo.localizaArquivoEmClasspath (filename);
    int width = new ImageIcon (urlSplash).getIconWidth ();
    int height = new ImageIcon (urlSplash).getIconHeight ();
    init (segundos, new JLabel ("<HTML><body bgcolor=white><IMG width='" + width + "' height='" + height + "'  SRC='" + urlSplash + "' ><br><p align=right><br><a href=#>Clique na imagem para fechar anima&ccedil;&atilde;o</a> <br></body></HTML>"));
  }
  
  /**
   * @deprecated
   */
  public SplashScreen (String filename, Frame parent, int segundos)
  {
    super (parent);
    URL urlSplash = UtilitariosArquivo.localizaArquivoEmClasspath (filename);
    int width = new ImageIcon (urlSplash).getIconWidth ();
    int height = new ImageIcon (urlSplash).getIconHeight ();
    init (segundos, new JLabel ("<HTML><body bgcolor=white><IMG width='" + width + "' height='" + height + "'  SRC='" + urlSplash + "' ><br><p align=right><br><a href=#>Clique na imagem para fechar anima&ccedil;&atilde;o</a> <br></body></HTML>"));
  }
  
  private void init (int segundos, JLabel l)
  {
    l.setCursor (Cursor.getPredefinedCursor (12));
    getContentPane ().add (l, "Center");
    pack ();
    Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
    Dimension labelSize = l.getPreferredSize ();
    setLocation (screenSize.width / 2 - labelSize.width / 2, screenSize.height / 2 - labelSize.height / 2);
    addMouseListener (new MouseAdapter ()
    {
      public void mousePressed (MouseEvent e)
      {
	fecha ();
      }
    });
    final int pause = segundos * 1000;
    final Runnable closerRunner = new Runnable ()
    {
      public void run ()
      {
	fecha ();
      }
    };
    Runnable waitRunner = new Runnable ()
    {
      public void run ()
      {
	try
	  {
	    Thread.sleep ((long) pause);
	    SwingUtilities.invokeAndWait (closerRunner);
	  }
	catch (Exception e)
	  {
	    e.printStackTrace ();
	  }
      }
    };
    setVisible (true);
    if (segundos > -1)
      {
	Thread splashThread = new Thread (waitRunner, "SplashThread");
	splashThread.start ();
      }
  }
  
  public void fecha ()
  {
    setVisible (false);
    dispose ();
    setRodando (false);
  }
  
  public void aguardaTerminar ()
  {
    while (isRodando ())
      {
	try
	  {
	    Thread.sleep (500L);
	  }
	catch (InterruptedException e)
	  {
	    e.printStackTrace ();
	  }
      }
  }
  
  private synchronized void setRodando (boolean rodando)
  {
    this.rodando = rodando;
  }
  
  private synchronized boolean isRodando ()
  {
    return rodando;
  }
}
