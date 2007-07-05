/* PainelDicasModal - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class PainelDicasModal extends JDialog
{
  public static final int OK = 1;
  public static final int CANCELAR = 2;
  private static int opcaoSelecionada = 2;
  private ConteudoPainelDicas painelConteudo;
  
  public static int getOpcaoSelecionada ()
  {
    return opcaoSelecionada;
  }
  
  public PainelDicasModal (int tipo, String titulo, String texto, int x, int y, int tamMin)
  {
    super (UtilitariosGUI.tentaObterJanelaPrincipal ());
    setPainelConteudo (new ConteudoPainelDicas (tipo, titulo, texto, x, y, tamMin));
    adicionaListeners ();
    getContentPane ().add (getPainelConteudo ());
    posicionaDicas (getPainelConteudo ().getAltura (), x, y, tamMin);
  }
  
  public PainelDicasModal (Dialog parent, int tipo, String titulo, String texto, int x, int y, int tamMin)
  {
    super (parent);
    setPainelConteudo (new ConteudoPainelDicas (tipo, titulo, texto, x, y, tamMin));
    adicionaListeners ();
    getContentPane ().add (getPainelConteudo ());
    posicionaDicas (getPainelConteudo ().getAltura (), x, y, tamMin);
  }
  
  public PainelDicasModal (Frame parent, int tipo, String titulo, String texto, int x, int y, int tamMin)
  {
    super (parent);
    setPainelConteudo (new ConteudoPainelDicas (tipo, titulo, texto, x, y, tamMin));
    adicionaListeners ();
    getContentPane ().add (getPainelConteudo ());
    posicionaDicas (getPainelConteudo ().getAltura (), x, y, tamMin);
  }
  
  private void adicionaListeners ()
  {
    getPainelConteudo ().getBtnCancel ().addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	PainelDicasModal.opcaoSelecionada = 2;
	esconderPainelDicas ();
      }
    });
    getPainelConteudo ().getBtnFechar ().addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	PainelDicasModal.opcaoSelecionada = 2;
	esconderPainelDicas ();
      }
    });
    getPainelConteudo ().getBtnOk ().addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	PainelDicasModal.opcaoSelecionada = 1;
	esconderPainelDicas ();
      }
    });
  }
  
  public void exibe ()
  {
    opcaoSelecionada = 2;
    setModal (true);
    setResizable (false);
    setUndecorated (true);
    pack ();
    getPainelConteudo ().getBtnOk ().requestFocusInWindow ();
    setVisible (true);
  }
  
  private void posicionaDicas (int alt, int x, int y, int tamMin)
  {
    JFrame janelaPrincipal = UtilitariosGUI.tentaObterJanelaPrincipal ();
    Rectangle r = janelaPrincipal.getRootPane ().getBounds ();
    Insets i = janelaPrincipal.getRootPane ().getInsets ();
    if ((double) (x + tamMin) > r.getWidth ())
      x = (int) r.getWidth () - tamMin - i.right - i.left - 50;
    if ((double) (y + alt) > r.getHeight () - 60.0)
      y = (int) r.getHeight () - alt - i.top - i.bottom - 75;
    setLocation (x, y);
  }
  
  public void esconderPainelDicas ()
  {
    setVisible (false);
  }
  
  public void setPainelConteudo (ConteudoPainelDicas painelConteudo)
  {
    this.painelConteudo = painelConteudo;
  }
  
  public ConteudoPainelDicas getPainelConteudo ()
  {
    return painelConteudo;
  }
}
