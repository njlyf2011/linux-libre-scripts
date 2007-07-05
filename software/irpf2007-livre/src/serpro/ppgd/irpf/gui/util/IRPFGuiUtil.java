/* IRPFGuiUtil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.util;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import serpro.ppgd.gui.xbeans.JDropDownButton;
import serpro.ppgd.gui.xbeans.JEditLogico;
import serpro.ppgd.gui.xbeans.JEditOpcao;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.treeview.ArvoreGenerica;
import serpro.ppgd.infraestrutura.treeview.NoGenerico;
import serpro.ppgd.infraestrutura.util.ProcessoSwing;
import serpro.ppgd.infraestrutura.util.Tarefa;

public class IRPFGuiUtil
{
  public static JPanel painelAtualmenteExibido = null;
  private static final String errMsg = "Ocorreu um erro ao tentar abrir o navegador internet.";
  
  public static void selecionaNodoArvore (JPanel painel)
  {
    ArvoreGenerica a = (ArvoreGenerica) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("ppgdarvore");
    Iterator itPainel = a.getVctNos ().values ().iterator ();
    while (itPainel.hasNext ())
      {
	NoGenerico no = (NoGenerico) itPainel.next ();
	if (no.getActionCommand ().equals (painel.getClass ().getName ()))
	  a.selecionarNodo (no.getNome ());
      }
  }
  
  public static void setaFonteTodosLabelContainer (Container c, Font pFonte)
  {
    Component[] components = c.getComponents ();
    for (int i = 0; i < components.length; i++)
      {
	Font fonte = pFonte;
	if (components[i] instanceof JComponent && ((JComponent) components[i]).getBorder () instanceof TitledBorder)
	  {
	    TitledBorder t = (TitledBorder) ((JComponent) components[i]).getBorder ();
	    t.setTitleFont (fonte);
	  }
	if (components[i] instanceof JEditLogico || components[i] instanceof JLabel || components[i] instanceof JEditOpcao)
	  components[i].setFont (fonte);
	else if (components[i] instanceof Container)
	  setaFonteTodosLabelContainer ((Container) components[i], fonte);
      }
  }
  
  public static void exibeDialog (JComponent c, boolean modal, String tit, boolean resizable)
  {
    exibeDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), c, modal, tit, resizable);
  }
  
  public static void exibeDialog (Object parent, JComponent c, boolean modal, String tit, boolean resizable)
  {
    JDialog dlg = null;
    if (parent == null)
      dlg = new JDialog ((Frame) null, tit, modal);
    else if (parent instanceof Dialog)
      dlg = new JDialog ((Dialog) parent, tit, modal);
    else if (parent instanceof Frame)
      dlg = new JDialog ((Frame) parent, tit, modal);
    else
      dlg = new JDialog ((Frame) null, tit, modal);
    dlg.getContentPane ().add (c);
    dlg.pack ();
    dlg.setLocationRelativeTo (null);
    dlg.setResizable (resizable);
    dlg.setVisible (true);
  }
  
  public static void proximoNodoArvore ()
  {
    ArvoreGenerica a = (ArvoreGenerica) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("ppgdarvore");
    NoGenerico noAtual = (NoGenerico) a.getSelectionPath ().getLastPathComponent ();
    NoGenerico proxNo = (NoGenerico) noAtual.getNextLeaf ();
    a.selecionarNodo (proxNo.getNome ());
    atualizaBotoes (proxNo);
  }
  
  public static void atualizaMenuFichas ()
  {
    ArvoreGenerica a = (ArvoreGenerica) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("ppgdarvore");
    final JMenu mnuFichas = (JMenu) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("mnuFichas");
    mnuFichas.removeAll ();
    NoGenerico no = (NoGenerico) a.getSelectionPath ().getLastPathComponent ();
    no = (NoGenerico) no.getParent ();
    Enumeration filhos = no.children ();
    while (filhos.hasMoreElements ())
      {
	NoGenerico noFilho = (NoGenerico) filhos.nextElement ();
	final NoGenerico novoNo = noFilho;
	if (novoNo != null)
	  {
	    final JMenuItem item = new JMenuItem (new AbstractAction ()
	    {
	      public void actionPerformed (ActionEvent e)
	      {
		mnuFichas.getPopupMenu ().setVisible (false);
		ProcessoSwing.executa (new Tarefa ()
		{
		  public Object run ()
		  {
		    NoGenerico noAcionado = novoNo;
		    if (! novoNo.isLeaf ())
		      noAcionado = (NoGenerico) novoNo.getFirstLeaf ();
		    ArvoreGenerica a_2_ = (ArvoreGenerica) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("ppgdarvore");
		    a_2_.selecionarNodo (noAcionado.getNome ());
		    return null;
		  }
		});
	      }
	    });
	    item.setText (novoNo.getNome ());
	    item.addMouseListener (new MouseAdapter ()
	    {
	      public void mouseEntered (MouseEvent e)
	      {
		item.setForeground (Color.WHITE);
	      }
	      
	      public void mouseExited (MouseEvent e)
	      {
		item.setForeground (Color.BLACK);
	      }
	    });
	    mnuFichas.add (item);
	  }
      }
  }
  
  public static void nodoAnteriorArvore ()
  {
    ArvoreGenerica a = (ArvoreGenerica) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("ppgdarvore");
    NoGenerico noAtual = (NoGenerico) a.getSelectionPath ().getLastPathComponent ();
    NoGenerico proxNo = (NoGenerico) noAtual.getPreviousLeaf ();
    a.selecionarNodo (proxNo.getNome ());
    atualizaBotoes (proxNo);
  }
  
  public static void atualizaBotoes (NoGenerico proxNo)
  {
    JDropDownButton btnProx = (JDropDownButton) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("btnFichaProxima");
    JDropDownButton btnAnterior = (JDropDownButton) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("btnFichaAnterior");
    btnProx.setEnabled (true);
    btnAnterior.setEnabled (true);
    if (proxNo.getPreviousLeaf () == null)
      {
	btnProx.setEnabled (true);
	btnAnterior.setEnabled (false);
      }
    else if (proxNo.getNextLeaf () == null)
      {
	btnProx.setEnabled (false);
	btnAnterior.setEnabled (true);
      }
  }
  
  public static void atualizaDropDownListProximo ()
  {
    ArvoreGenerica a = (ArvoreGenerica) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("ppgdarvore");
    JDropDownButton btnProx = (JDropDownButton) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("btnFichaProxima");
    btnProx.getPopupMenu ().removeAll ();
    NoGenerico no = (NoGenerico) a.getSelectionPath ().getLastPathComponent ();
    adicionaItensProximo (no, (NoGenerico) no.getParent (), btnProx);
  }
  
  private static void adicionaItensProximo (NoGenerico noChamador, NoGenerico no, final JDropDownButton btn)
  {
    NoGenerico prox = noChamador;
    while (prox != null)
      {
	prox = (NoGenerico) prox.getNextSibling ();
	final NoGenerico novoNo = prox;
	if (novoNo != null)
	  {
	    final JMenuItem item = new JMenuItem (new AbstractAction ()
	    {
	      public void actionPerformed (ActionEvent e)
	      {
		btn.getPopupMenu ().setVisible (false);
		ProcessoSwing.executa (new Tarefa ()
		{
		  public Object run ()
		  {
		    NoGenerico noAcionado = novoNo;
		    if (! novoNo.isLeaf ())
		      noAcionado = (NoGenerico) novoNo.getFirstLeaf ();
		    ArvoreGenerica a = (ArvoreGenerica) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("ppgdarvore");
		    a.selecionarNodo (noAcionado.getNome ());
		    return null;
		  }
		});
	      }
	    });
	    item.setText (novoNo.getNome ());
	    item.addMouseListener (new MouseAdapter ()
	    {
	      public void mouseEntered (MouseEvent e)
	      {
		item.setForeground (Color.WHITE);
	      }
	      
	      public void mouseExited (MouseEvent e)
	      {
		item.setForeground (Color.BLACK);
	      }
	    });
	    btn.addPopupItem (item);
	  }
      }
    if (no.getPreviousNode () != null)
      adicionaItensProximo (no, (NoGenerico) no.getParent (), btn);
  }
  
  public static void atualizaDropDownListAnterior ()
  {
    ArvoreGenerica a = (ArvoreGenerica) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("ppgdarvore");
    JDropDownButton btnAnterior = (JDropDownButton) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("btnFichaAnterior");
    btnAnterior.getPopupMenu ().removeAll ();
    NoGenerico no = (NoGenerico) a.getSelectionPath ().getLastPathComponent ();
    adicionaItensAnterior (no, (NoGenerico) no.getParent (), btnAnterior);
  }
  
  private static void adicionaItensAnterior (NoGenerico noChamador, NoGenerico no, final JDropDownButton btn)
  {
    NoGenerico prox = noChamador;
    while (prox != null)
      {
	prox = (NoGenerico) prox.getPreviousSibling ();
	final NoGenerico novoNo = prox;
	if (novoNo != null)
	  {
	    final JMenuItem item = new JMenuItem (new AbstractAction ()
	    {
	      public void actionPerformed (ActionEvent e)
	      {
		btn.getPopupMenu ().setVisible (false);
		ProcessoSwing.executa (new Tarefa ()
		{
		  public Object run ()
		  {
		    NoGenerico noAcionado = novoNo;
		    if (! novoNo.isLeaf ())
		      noAcionado = (NoGenerico) novoNo.getFirstLeaf ();
		    ArvoreGenerica a = (ArvoreGenerica) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("ppgdarvore");
		    a.selecionarNodo (noAcionado.getNome ());
		    return null;
		  }
		});
	      }
	    });
	    item.setText (novoNo.getNome ());
	    btn.addPopupItem (item);
	    item.addMouseListener (new MouseAdapter ()
	    {
	      public void mouseEntered (MouseEvent e)
	      {
		item.setForeground (Color.WHITE);
	      }
	      
	      public void mouseExited (MouseEvent e)
	      {
		item.setForeground (Color.BLACK);
	      }
	    });
	  }
      }
    if (no.getPreviousNode () != null)
      adicionaItensAnterior (no, (NoGenerico) no.getParent (), btn);
  }
  
  public static void abreURL (String url)
  {
    String os = System.getProperty ("os.name").toLowerCase ();
    try
      {
	if (os.startsWith ("mac os"))
	  {
	    Class fileMgr = Class.forName ("com.apple.eio.FileManager");
	    Class var_class = fileMgr;
	    String string = "openURL";
	    Class[] var_classes = new Class[1];
	    int i = 0;
	    Class var_class_7_ = java.lang.String.class;
	    var_classes[i] = var_class_7_;
	    Method openURL = var_class.getDeclaredMethod (string, var_classes);
	    openURL.invoke (null, new Object[] { url });
	  }
	else if (os.startsWith ("windows"))
	  Runtime.getRuntime ().exec ("rundll32 url.dll,FileProtocolHandler " + url);
	else
	  {
	    String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
	    String browser = null;
	    for (int count = 0; count < browsers.length && browser == null; count++)
	      {
		if (Runtime.getRuntime ().exec (new String[] { "which", browsers[count] }).waitFor () == 0)
		  browser = browsers[count];
	      }
	    if (browser == null)
	      throw new Exception ("Navegador internet n\u00ef\u00bf\u00bdo encontrado.");
	    Runtime.getRuntime ().exec (new String[] { browser, url });
	  }
      }
    catch (Exception e)
      {
	JOptionPane.showMessageDialog (null, "Ocorreu um erro ao tentar abrir o navegador internet.:\n" + e.getLocalizedMessage ());
      }
  }
}
