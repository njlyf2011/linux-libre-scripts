/* ConfirmacaoDialog - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.FormLayout;

import serpro.ppgd.negocio.util.UtilitariosString;

public class ConfirmacaoDialog
{
  public static final int OPCAO_FECHAR = 0;
  public static final int OPCAO_SIM = 1;
  public static final int OPCAO_NAO = 2;
  private final String LABEL_BOTAO_SIM = "Sim";
  private final String LABEL_BOTAO_NAO = "N\u00e3o";
  private JLabel lbTexto;
  private int value;
  private JDialog dialog;
  
  public ConfirmacaoDialog ()
  {
    this (null, "", "", true);
  }
  
  public ConfirmacaoDialog (JFrame owner)
  {
    this (owner, "", "", true);
  }
  
  public ConfirmacaoDialog (JFrame owner, String dialogTitle)
  {
    this (owner, dialogTitle, true);
  }
  
  public ConfirmacaoDialog (JFrame owner, String dialogTitle, boolean modal)
  {
    this (owner, dialogTitle, "", modal);
  }
  
  public ConfirmacaoDialog (JFrame owner, String dialogTitle, String textoConfirmacao, boolean modal)
  {
    /* empty */
  }
  
  private void opcaoSim ()
  {
    value = 1;
    dialog.setVisible (false);
  }
  
  private void opcaoNao ()
  {
    value = 2;
    dialog.setVisible (false);
  }
  
  public void setTitulo (String titulo)
  {
    dialog.setTitle (titulo);
  }
  
  public void setTexto (String texto)
  {
    lbTexto.setText (texto);
  }
  
  public static int show (String textoConfirmacao)
  {
    return show ("", textoConfirmacao);
  }
  
  public static int show (String titulo, String textoConfirmacao)
  {
    ConfirmacaoDialog d = new ConfirmacaoDialog ();
    return d.showDialog (titulo, UtilitariosString.strToHtml (textoConfirmacao));
  }
  
  public int showDialog (String titulo, String textoConfirmacao)
  {
    int escolha = JOptionPane.showConfirmDialog (null, textoConfirmacao, titulo, 0);
    if (escolha == 0)
      value = 1;
    else if (escolha == 1)
      value = 2;
    else
      value = 0;
    return value;
  }
  
  public JComponent buildPanel (String textoConfirmacao)
  {
    FormLayout layout = new FormLayout ("5dlu, f:p:grow", "8dlu, p, 15dlu, p, 2dlu");
    PPGDFormBuilder builder = new PPGDFormBuilder (layout);
    builder.setDefaultDialogBorder ();
    builder.nextLine ();
    lbTexto = new JLabel ();
    builder.setColumn (2);
    builder.append (lbTexto);
    builder.nextLine (2);
    ButtonPPGD btnSim = new ButtonPPGD ("Sim", ConstantesGlobaisGUI.ICO_PLAY, ConstantesGlobaisGUI.ICO_PLAY_SEL, ConstantesGlobaisGUI.ICO_PLAY_PRES);
    btnSim.setEstiloLink (true);
    btnSim.setaDimensoes ();
    btnSim.setAlignmentX (0.5F);
    btnSim.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent e)
      {
	ConfirmacaoDialog.this.opcaoSim ();
      }
    });
    ButtonPPGD btnNao = new ButtonPPGD ("N\u00e3o", ConstantesGlobaisGUI.ICO_STOP, ConstantesGlobaisGUI.ICO_STOP_SEL, ConstantesGlobaisGUI.ICO_STOP_PRES);
    btnNao.setEstiloLink (true);
    btnNao.setaDimensoes ();
    btnNao.setAlignmentX (0.5F);
    btnNao.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent e)
      {
	ConfirmacaoDialog.this.opcaoNao ();
      }
    });
    JPanel pBotoes = new JPanel ();
    pBotoes.add (btnSim);
    pBotoes.add (new JLabel ("    "));
    pBotoes.add (btnNao);
    builder.setColumn (2);
    builder.appendCenter (pBotoes);
    return builder.getPanel ();
  }
}
