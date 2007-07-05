/* DialogGenerico - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.FormLayout;

public class DialogGenerico extends JDialog
{
  private static final String LABEL_BOTAO_FECHAR = "Fechar";
  private static ButtonPPGD btnFechar = new ButtonPPGD ("Fechar", ConstantesGlobaisGUI.ICO_STOP, ConstantesGlobaisGUI.ICO_STOP_SEL, ConstantesGlobaisGUI.ICO_STOP_PRES);
  private static int value;
  private JPanel painel;
  private JPanel painelComBotoes;
  
  public DialogGenerico (JPanel painel, String pTitulo)
  {
    this (null, painel, pTitulo, btnFechar);
  }
  
  public DialogGenerico (JFrame owner, JPanel painel, String title)
  {
    this (owner, painel, title, btnFechar);
  }
  
  public DialogGenerico (JFrame owner, JPanel painel, String title, JComponent botoes)
  {
    this (owner, painel, title, botoes, true);
  }
  
  public DialogGenerico (JFrame owner, JPanel painel, String title, JComponent botoes, boolean resetValue)
  {
    super (owner, title, true);
    this.painel = painel;
    if (resetValue)
      value = 0;
    setDefaultCloseOperation (2);
    getContentPane ().add (buildPanel (painel, botoes));
    setSize (getPreferredSize ());
    showDialog ();
  }
  
  public DialogGenerico (JFrame owner)
  {
    super ((java.awt.Frame) owner);
  }
  
  private void opcaoFechar ()
  {
    setVisible (false);
  }
  
  public void showDialog ()
  {
    setResizable (true);
    pack ();
    setLocationRelativeTo (null);
    setVisible (true);
  }
  
  private JPanel buildPanel (JPanel painel, JComponent botoes)
  {
    FormLayout layout = new FormLayout ("c:max(210dlu;p):grow", "t:p, 5dlu, t:p");
    PPGDFormBuilder builder = new PPGDFormBuilder (new JPanel (), layout);
    builder.setDefaultDialogBorder ();
    builder.append (painel);
    builder.nextLine ();
    builder.nextLine ();
    UtilitariosGUI.setParametrosGUI (btnFechar, ConstantesGlobaisGUI.FONTE_10_NORMAL, ConstantesGlobaisGUI.COR_PRETO, null);
    btnFechar.setaDimensoes ();
    btnFechar.setAlignmentX (0.5F);
    btnFechar.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent e)
      {
	DialogGenerico.this.opcaoFechar ();
      }
    });
    if (botoes != null)
      builder.appendCenter (botoes);
    painelComBotoes = builder.getPanel ();
    return painelComBotoes;
  }
  
  public JPanel getPainel ()
  {
    return painel;
  }
  
  public void atualizaConteudo (JPanel pPainel, JComponent botoes, String titulo, boolean modal, boolean resetValue, boolean exibeImediatamente)
  {
    getContentPane ().removeAll ();
    painel = pPainel;
    getContentPane ().add (buildPanel (painel, botoes));
    setTitle (titulo);
    setModal (modal);
    if (resetValue)
      value = 0;
    setSize (getPreferredSize ());
    if (exibeImediatamente)
      showDialog ();
  }
  
  public static int getValue ()
  {
    return value;
  }
  
  public static void setValue (int val)
  {
    value = val;
  }
}
