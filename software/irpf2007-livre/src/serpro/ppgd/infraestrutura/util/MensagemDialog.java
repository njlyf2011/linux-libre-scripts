/* MensagemDialog - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.jgoodies.forms.layout.FormLayout;

import serpro.ppgd.gui.ButtonPPGD;
import serpro.ppgd.gui.ConstantesGlobaisGUI;
import serpro.ppgd.gui.PPGDFormBuilder;
import serpro.ppgd.gui.UtilitariosGUI;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.negocio.util.UtilitariosString;

public class MensagemDialog
{
  private final String LABEL_BOTAO_FECHAR = "Fechar";
  private final ImageIcon ICO_INDICADOR = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("imagens/ico_indicador.gif"));
  private JLabel lbMensagem;
  private JDialog dialog;
  
  public MensagemDialog ()
  {
    this (null);
  }
  
  public MensagemDialog (JFrame owner)
  {
    if (owner != null)
      dialog = new JDialog (owner, "", true);
    else
      dialog = new JDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "", true);
    dialog.setDefaultCloseOperation (2);
    dialog.getContentPane ().setBackground (ConstantesGlobaisGUI.COR_BRANCO);
    dialog.getContentPane ().add (buildPanel ());
  }
  
  private void fechar ()
  {
    dialog.setVisible (false);
  }
  
  public void setMensagem (String mensagem)
  {
    lbMensagem.setText (mensagem);
  }
  
  public static void show (String mensagem)
  {
    show ("", mensagem);
  }
  
  public static void show (String titulo, String mensagem)
  {
    MensagemDialog m = new MensagemDialog ();
    m.showDialog (titulo, UtilitariosString.strToHtml (mensagem));
  }
  
  public void showDialog (String titulo, String mensagem)
  {
    dialog.setTitle (titulo);
    setMensagem (mensagem);
    dialog.setSize (dialog.getPreferredSize ());
    dialog.setResizable (false);
    dialog.pack ();
    dialog.setLocationRelativeTo (null);
    dialog.setVisible (true);
  }
  
  public JComponent buildPanel ()
  {
    FormLayout layout = new FormLayout ("5dlu,f:p:grow,5dlu", "8dlu,p,15dlu,p,3dlu");
    PPGDFormBuilder builder = new PPGDFormBuilder (layout);
    builder.setDefaultDialogBorder ();
    builder.getPanel ().setBackground (ConstantesGlobaisGUI.COR_BRANCO);
    builder.nextLine ();
    lbMensagem = new JLabel ();
    UtilitariosGUI.setParametrosGUI (lbMensagem, ConstantesGlobaisGUI.FONTE_10_NORMAL, ConstantesGlobaisGUI.COR_PRETO, ConstantesGlobaisGUI.COR_BRANCO);
    builder.setColumn (2);
    builder.append (lbMensagem);
    builder.nextLine (2);
    ButtonPPGD btnFechar = new ButtonPPGD ("Fechar", ConstantesGlobaisGUI.ICO_STOP, ConstantesGlobaisGUI.ICO_STOP_SEL, ConstantesGlobaisGUI.ICO_STOP_PRES);
    UtilitariosGUI.setParametrosGUI (btnFechar, ConstantesGlobaisGUI.FONTE_10_NORMAL, ConstantesGlobaisGUI.COR_PRETO, ConstantesGlobaisGUI.COR_BRANCO);
    btnFechar.setaDimensoes ();
    btnFechar.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent e)
      {
	MensagemDialog.this.fechar ();
      }
    });
    builder.appendCenter (btnFechar);
    return builder.getPanel ();
  }
}
