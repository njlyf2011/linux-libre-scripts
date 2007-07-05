/* OpcaoDialog - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.jgoodies.forms.layout.FormLayout;

import serpro.ppgd.negocio.util.UtilitariosString;

public class OpcaoDialog
{
  public static final int OPCAO_FECHAR = 0;
  private static final String LABEL_TEXTO_DEFAULT = "Clique na op\u00e7\u00e3o desejada:";
  private static final String LABEL_BOTAO_FECHAR = "Fechar";
  protected static final ImageIcon ICO_INDICADOR = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("imagens/ico_indicador.gif"));
  protected int value;
  protected JDialog dialog;
  
  protected class ExecutarSelecionarOpcao extends MouseAdapter
  {
    private int numOpcao;
    
    public ExecutarSelecionarOpcao (int numOpcao)
    {
      this.numOpcao = numOpcao;
    }
    
    public void mouseClicked (MouseEvent e)
    {
      value = numOpcao;
      dialog.setVisible (false);
    }
  }
  
  public OpcaoDialog (String[] opcao)
  {
    this (null, "", "Clique na op\u00e7\u00e3o desejada:", opcao, true);
  }
  
  public OpcaoDialog (String texto, String[] opcao)
  {
    this (null, "", texto, opcao, true);
  }
  
  public OpcaoDialog (JFrame owner, String titulo, String texto, String[] opcao, boolean modal)
  {
    if (owner != null)
      dialog = new JDialog (owner, titulo, modal);
    else
      dialog = new JDialog ((java.awt.Dialog) null, titulo, modal);
    dialog.setDefaultCloseOperation (2);
    dialog.getContentPane ().setBackground (ConstantesGlobaisGUI.COR_BRANCO);
    dialog.getContentPane ().add (buildPanel (titulo, texto, opcao));
    dialog.setSize (dialog.getPreferredSize ());
    dialog.setResizable (false);
    dialog.setLocationRelativeTo (null);
    dialog.pack ();
  }
  
  private void fechar ()
  {
    value = 0;
    dialog.setVisible (false);
  }
  
  public int showDialog (String titulo)
  {
    dialog.setTitle (titulo);
    dialog.show ();
    return value;
  }
  
  public JComponent buildPanel (String titulo, String texto, String[] opcao)
  {
    String rowsLayout = "8dlu, p, 10dlu";
    for (int i = 0; i < opcao.length; i++)
      rowsLayout += ", p, 5dlu";
    rowsLayout += ", p, 15dlu, p, 3dlu";
    FormLayout layout = new FormLayout ("5dlu, l:p:grow, 5dlu", rowsLayout);
    PPGDFormBuilder builder = new PPGDFormBuilder (layout);
    builder.setDefaultDialogBorder ();
    builder.getPanel ().setBackground (ConstantesGlobaisGUI.COR_BRANCO);
    builder.nextLine ();
    JLabel lbTexto = new JLabel (UtilitariosString.strToHtml (texto));
    UtilitariosGUI.setParametrosGUI (lbTexto, ConstantesGlobaisGUI.FONTE_10_NORMAL, ConstantesGlobaisGUI.COR_PRETO, ConstantesGlobaisGUI.COR_BRANCO);
    builder.setColumn (2);
    builder.append (lbTexto);
    builder.nextLine (2);
    if (opcao != null)
      {
	ButtonPPGD[] btnOpcao = new ButtonPPGD[opcao.length];
	for (int i = 0; i < opcao.length; i++)
	  {
	    btnOpcao[i] = new ButtonPPGD (opcao[i], ICO_INDICADOR);
	    UtilitariosGUI.setParametrosGUI (btnOpcao[i], ConstantesGlobaisGUI.FONTE_10_NORMAL, ConstantesGlobaisGUI.COR_PRETO, ConstantesGlobaisGUI.COR_BRANCO);
	    btnOpcao[i].addMouseListener (new ExecutarSelecionarOpcao (i + 1));
	    builder.setColumn (2);
	    builder.append (btnOpcao[i]);
	    builder.nextLine (2);
	  }
      }
    builder.nextLine (2);
    ButtonPPGD btnFechar = new ButtonPPGD ("Fechar", ConstantesGlobaisGUI.ICO_STOP, ConstantesGlobaisGUI.ICO_STOP_SEL, ConstantesGlobaisGUI.ICO_STOP_PRES);
    UtilitariosGUI.setParametrosGUI (btnFechar, ConstantesGlobaisGUI.FONTE_10_NORMAL, ConstantesGlobaisGUI.COR_PRETO, ConstantesGlobaisGUI.COR_BRANCO);
    btnFechar.setaDimensoes ();
    btnFechar.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent e)
      {
	OpcaoDialog.this.fechar ();
      }
    });
    builder.appendCenter (btnFechar);
    return builder.getPanel ();
  }
}
