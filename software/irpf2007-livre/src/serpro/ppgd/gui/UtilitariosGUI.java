/* UtilitariosGUI - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.LayoutManager;
import java.io.File;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.border.Border;

import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.negocio.util.PreferenciasGlobais;

public class UtilitariosGUI
{
  protected static File ultimoCaminhoSelecionado = null;
  protected static JFileChooser fc;
  
  static class CaixaSelecao extends JFileChooser
  {
    public CaixaSelecao ()
    {
      /* empty */
    }
    
    public File getSelectedFile ()
    {
      File retorno = super.getSelectedFile ();
      if (retorno != null && retorno.getParentFile () != null && retorno.getParentFile ().getPath () != null)
	UtilitariosGUI.ultimoCaminhoSelecionado = retorno;
      return retorno;
    }
  }
  
  public static void setParametrosGUI (JComponent c, LayoutManager l)
  {
    setParametrosGUI (c, l, null);
  }
  
  public static void setParametrosGUI (JComponent c, LayoutManager l, Border b)
  {
    if (l != null)
      c.setLayout (l);
    if (b != null)
      c.setBorder (b);
  }
  
  public static void setParametrosGUI (JComponent c, Color bk)
  {
    setParametrosGUI (c, null, null, bk);
  }
  
  public static void setParametrosGUI (JComponent c, Font f, Color fr, Color bk)
  {
    c.setOpaque (true);
    if (f != null)
      c.setFont (f);
    if (bk != null)
      c.setBackground (bk);
    if (fr != null)
      c.setForeground (fr);
  }
  
  public static void setParametrosGUI (JComponent c, int tam, int alt)
  {
    c.setPreferredSize (new Dimension (tam, alt));
    c.setMinimumSize (c.getPreferredSize ());
    c.setMaximumSize (c.getPreferredSize ());
  }
  
  public static void setParametrosGUIPrefSize (JComponent c, int tam, int alt)
  {
    c.setPreferredSize (new Dimension (tam, alt));
  }
  
  public static void addButtonPanel (JButton btn, JComponent area, Dimension GapHorizontal, Dimension GapVertical)
  {
    area.add (Box.createRigidArea (GapVertical));
    Box boxX = new Box (0);
    boxX.add (Box.createRigidArea (GapHorizontal));
    boxX.setAlignmentX (0.0F);
    boxX.add (btn);
    area.add (boxX);
    area.add (Box.createRigidArea (GapVertical));
  }
  
  public static void addLabelPanel (String strLabel, JComponent area, Dimension GapHorizontal, Dimension GapVertical)
  {
    JLabel label = new JLabel (strLabel);
    area.add (Box.createRigidArea (GapVertical));
    Box boxX = new Box (0);
    boxX.add (Box.createRigidArea (GapHorizontal));
    boxX.setAlignmentX (0.0F);
    boxX.add (label);
    area.add (boxX);
    area.add (Box.createRigidArea (GapVertical));
  }
  
  public static void addLink (IdLink link, Vector vetorLink, Vector vetorBtn)
  {
    ButtonPPGD btn = null;
    int i = vetorLink.indexOf (link);
    if (i == -1)
      {
	btn = new ButtonPPGD (link.getNome (), link.getIcone (), link.getIconeSelecionado (), link.getIconePressionado (), link.getIconeDesabilitado (), link.getIconeToggled ());
	vetorBtn.add (btn);
	vetorLink.add (link);
      }
    else
      btn = (ButtonPPGD) vetorBtn.get (i);
    btn.addActionListener (link.getAction ());
  }
  
  public static JFileChooser setFileChooserProperties (String titulo, String lookInLabelText, String approveButtonText, String approveButtonToolTipText)
  {
    UIManager.put ("FileChooser.fileNameLabelText", "Nome do Arquivo:");
    UIManager.put ("FileChooser.filesOfTypeLabelText", "Tipo do Arquivo:");
    UIManager.put ("FileChooser.detailsViewButtonToolTipText", "Detalhes");
    UIManager.put ("FileChooser.listViewButtonToolTipText", "Listar");
    UIManager.put ("FileChooser.upFolderToolTipText", "Um n\u00edvel acima");
    UIManager.put ("FileChooser.newFolderToolTipText", "Criar nova pasta");
    UIManager.put ("FileChooser.homeFolderToolTipText", "\u00c1rea de Trabalho");
    UIManager.put ("FileChooser.fileNameHeaderText", "Nome");
    UIManager.put ("FileChooser.fileSizeHeaderText", "Tamanho");
    UIManager.put ("FileChooser.fileTypeHeaderText", "Tipo");
    UIManager.put ("FileChooser.fileDateHeaderText", "Modificado");
    UIManager.put ("FileChooser.fileAttrHeaderText", "Atributos");
    UIManager.put ("FileChooser.lookInLabelText", lookInLabelText);
    UIManager.put ("FileChooser.directoryOpenButtonText", "Abrir");
    UIManager.put ("FileChooser.directoryOpenButtonToolTipText", "Abrir pasta");
    UIManager.put ("FileChooser.cancelButtonText", "Cancelar");
    UIManager.put ("FileChooser.cancelButtonToolTipText", "Cancela");
    fc = new CaixaSelecao ();
    fc.setApproveButtonText (approveButtonText);
    fc.setApproveButtonToolTipText (approveButtonToolTipText);
    fc.setDialogTitle (titulo);
    if (ultimoCaminhoSelecionado != null)
      fc.setSelectedFile (ultimoCaminhoSelecionado);
    return fc;
  }
  
  public static JFrame tentaObterJanelaPrincipal ()
  {
    Frame[] frames = Frame.getFrames ();
    JFrame principal = null;
    for (int i = 0; i < frames.length; i++)
      {
	if (frames[i] instanceof JFrame && frames[i].getName ().equals ("PPGD_JANELA_PRINCIPAL"))
	  principal = (JFrame) frames[i];
      }
    if (principal == null)
      {
	LogPPGD.erro ("N\u00e3o foi poss\u00edvel encontrar a janela principal!");
	LogPPGD.erro ("A janela principal precisa ter sua propriedade 'name' setada com o valor da constante ConstantesGlobaisGUI.NOME_JANELA_PRINCIPAL");
      }
    return principal;
  }
  
  public static void estiloFonte (JComponent comp, int estilo)
  {
    Font f = comp.getFont ();
    f = f.deriveFont (estilo);
    comp.setFont (f);
  }
  
  public static void aumentaFonte (JComponent comp, int acrescimo)
  {
    Font f = comp.getFont ();
    f = f.deriveFont (f.getSize2D () + (float) acrescimo);
    comp.setFont (f);
  }
  
  public static void diminuiFonte (JComponent comp, int decrescimo)
  {
    Font f = comp.getFont ();
    f = f.deriveFont (f.getSize2D () - (float) decrescimo);
    comp.setFont (f);
  }
  
  public static void preparaFontes ()
  {
    Font fontDefault = null;
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment ();
    if (System.getProperty ("os.name").startsWith ("Linux"))
      {
	String fDefault = PreferenciasGlobais.get ("fonte-default-linux");
	if (fDefault == null)
	  {
	    String[] fontnames = ge.getAvailableFontFamilyNames ();
	    for (int i = 0; i < fontnames.length; i++)
	      {
		if (fontnames[i].equals ("Arial") || fontnames[i].equals ("SansSerif") || fontnames[i].equals ("Dialog"))
		  {
		    fDefault = fontnames[i];
		    break;
		  }
	      }
	    PreferenciasGlobais.put ("fonte-default-linux", fDefault);
	  }
	fontDefault = new Font (fDefault, 0, 10);
      }
    else
      fontDefault = new Font ("Arial", 0, 11);
    UIManager.put ("Button.font", fontDefault);
    UIManager.put ("RadioButton.font", fontDefault);
    UIManager.put ("CheckBox.font", fontDefault);
    UIManager.put ("FileChooser.font", fontDefault);
    UIManager.put ("Label.font", fontDefault);
    UIManager.put ("List.font", fontDefault);
    UIManager.put ("RadioButtonMenuItem.font", fontDefault);
    UIManager.put ("CheckBoxMenuItem.font", fontDefault);
    UIManager.put ("PopupMenu.font", fontDefault);
    UIManager.put ("Frame.font", fontDefault);
    UIManager.put ("Panel.font", fontDefault);
    UIManager.put ("ScrollPane.font", fontDefault);
    UIManager.put ("Viewport.font", fontDefault);
    UIManager.put ("TabbedPane.font", fontDefault);
    UIManager.put ("Table.font", fontDefault);
    UIManager.put ("TextField.font", fontDefault);
    UIManager.put ("FormattedTextField.font", fontDefault);
    UIManager.put ("TextArea.font", fontDefault);
    UIManager.put ("TextPane.font", fontDefault);
    UIManager.put ("EditorPane.font", fontDefault);
    UIManager.put ("TitledBorder.font", fontDefault);
    UIManager.put ("ToolBar.font", fontDefault);
    UIManager.put ("ToolTip.font", fontDefault.deriveFont (1));
    UIManager.put ("Tree.font", fontDefault);
  }
}
