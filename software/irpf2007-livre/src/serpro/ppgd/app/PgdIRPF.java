/* PgdIRPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.app;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import org.swixml.SwingEngine;

import serpro.ppgd.gui.SplashScreen;
import serpro.ppgd.gui.xbeans.JDropDownButton;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.treeview.ArvoreGenerica;
import serpro.ppgd.infraestrutura.util.VisualizadorHelp;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.util.FabricaTratamentoErro;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.PreferenciasGlobais;

public class PgdIRPF
{
  private PlataformaPPGD plataforma = PlataformaPPGD.getPlataforma ();
  public static Font FONTE_DEFAULT = null;
  public static int SPLIT_DIVIDER_LOCATION = 160;
  public static final String VERSAO_BETA_DESCRICAO = "<html><b>Vers\u00e3o teste do programa IRPF2007 Java</b> <br><br>A Receita Federal disponibiliza a vers\u00e3o teste do programa IRPF2007 Java, <br> para que o contribuinte possa conhecer o aplicativo com anteced\u00eancia, <br>detectar eventuais inconsist\u00eancias e fazer propostas para que o programa<br> atenda cada vez mais aos usu\u00e1rios. <br><br>As cr\u00edticas e sugest\u00f5es devem ser encaminhadas para o endere\u00e7o <br><b>irpf.beta@receita.fazenda.gov.br</b> at\u00e9 o dia 18/12/2006.</html>";
  private static boolean permiteMaisDeUmaInstanciaAplicacao = false;
  private JLabel lblSplash = new JLabel ("<HTML><body bgcolor=white><IMG SRC='" + Thread.currentThread ().getContextClassLoader ().getResource ("icones/splashirpf.gif") + "' ><br><p align=right></body></HTML>");
  
  public PgdIRPF ()
  {
    SplashScreen splash = new SplashScreen ("/icones/splashirpf.gif", null, 20);
    plataforma.setSwingEngine (new SwingEngine (this));
    org.swixml.TagLibrary taglibrary = plataforma.getSwingEngine ().getTaglib ();
    String string = "dropdownbutton";
    Class var_class = serpro.ppgd.gui.xbeans.JDropDownButton.class;
    taglibrary.registerTag (string, var_class);
    plataforma.carrega ();
    final JDropDownButton btnProx = (JDropDownButton) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("btnFichaProxima");
    final JDropDownButton btnAnterior = (JDropDownButton) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("btnFichaAnterior");
    PlataformaPPGD.getPlataforma ().getJanelaPrincipal ().addWindowFocusListener (new WindowFocusListener ()
    {
      public void windowGainedFocus (WindowEvent e)
      {
	/* empty */
      }
      
      public void windowLostFocus (WindowEvent e)
      {
	if (e.getOppositeWindow () == null || ! e.getOppositeWindow ().equals (PlataformaPPGD.getPlataforma ().getJanelaPrincipal ()))
	  {
	    btnProx.getPopupMenu ().setVisible (false);
	    btnAnterior.getPopupMenu ().setVisible (false);
	  }
      }
    });
    plataforma.getJanelaPrincipal ().setDefaultCloseOperation (2);
    plataforma.setComportamentoPadraoSair (false);
    plataforma.getJanelaPrincipal ().addWindowListener (new WindowAdapter ()
    {
      public void windowClosed (WindowEvent e)
      {
	if (IRPFUtil.temDeclaracaoAberta)
	  IRPFFacade.salvaDeclaracaoAberta ();
	System.out.println ("Aplica\u00e7\u00e3o Encerrada");
	plataforma.getJanelaPrincipal ().setVisible (false);
	System.exit (0);
      }
    });
    FabricaTratamentoErro.configuraTrataErroSistemico (plataforma.getJanelaPrincipal ());
    plataforma.getPainelPrincipal ().setBackground (new Color (255, 255, 179));
    plataforma.getPainelPrincipal ().setBorder (BorderFactory.createLoweredBevelBorder ());
    JSplitPane split = (JSplitPane) PlataformaPPGD.getPlataforma ().getComponent ("splitPPGD");
    split.setDividerLocation (SPLIT_DIVIDER_LOCATION);
    split.setVisible (false);
    split.setOpaque (false);
    JScrollPane scrollAreaArvore = (JScrollPane) PlataformaPPGD.getPlataforma ().getComponent ("ppgdAreaArvore");
    scrollAreaArvore.setHorizontalScrollBarPolicy (32);
    scrollAreaArvore.setVerticalScrollBarPolicy (20);
    JScrollPane scrollAreaPrincipal = (JScrollPane) PlataformaPPGD.getPlataforma ().getComponent ("ppgdScrollPnlPPGD");
    scrollAreaPrincipal.setHorizontalScrollBarPolicy (30);
    scrollAreaPrincipal.setVerticalScrollBarPolicy (20);
    ArvoreGenerica a = (ArvoreGenerica) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("ppgdarvore");
    a.setSize (a.getPreferredSize ());
    configuraHelp ();
    javax.swing.UIDefaults uiDefaults = UIManager.getDefaults ();
    splash.aguardaTerminar ();
    IRPFUtil.fecharDeclaracao ();
    JToolBar tool = (JToolBar) PlataformaPPGD.getPlataforma ().getComponent ("tbPPGD");
    ajustaBotoesToolbar (tool);
    tool = (JToolBar) PlataformaPPGD.getPlataforma ().getComponent ("tbPPGD2");
    ajustaBotoesToolbar (tool);
    tool = (JToolBar) PlataformaPPGD.getPlataforma ().getComponent ("tbPPGD3");
    ajustaBotoesToolbar (tool);
    tool = (JToolBar) PlataformaPPGD.getPlataforma ().getComponent ("tbPPGD5");
    ajustaBotoesToolbar (tool);
    tool = (JToolBar) PlataformaPPGD.getPlataforma ().getComponent ("tbPPGD6");
    ajustaBotoesToolbar (tool);
    tool = (JToolBar) PlataformaPPGD.getPlataforma ().getComponent ("tbPPGD8");
    ajustaBotoesToolbar (tool);
    IRPFUtil.habilitaComponentesTemDeclaracao ();
    plataforma.exibe ();
  }
  
  private void aplicaPreferenciasIniciais ()
  {
    PreferenciasGlobais.put ("IRPF2006_PATH_APP", FabricaUtilitarios.getPathCompletoDirAplicacao ());
    PreferenciasGlobais.put ("IRPF2006_PATH_TRANSMITIDAS", FabricaUtilitarios.getPathCompletoDirAplicacao () + "transmitidas");
    PreferenciasGlobais.put ("IRPF2006_PATH_GRAVADAS", FabricaUtilitarios.getPathCompletoDirAplicacao () + "gravadas");
    PreferenciasGlobais.put ("IRPF2006_PATH_DADOS", FabricaUtilitarios.getPathCompletoDirDadosAplicacao ());
  }
  
  private void ajustaBotoesToolbar (JToolBar tool)
  {
    JButton btnMaiorBotao = null;
    for (int i = tool.getComponentCount () - 1; i >= 0; i--)
      {
	java.awt.Component c = tool.getComponent (i);
	if (c instanceof JButton)
	  {
	    int tam = 36;
	    ((JButton) c).setPreferredSize (new Dimension (tam, tam));
	    ((JButton) c).setSize (new Dimension (tam, tam));
	    ((JButton) c).setMinimumSize (new Dimension (tam, tam));
	    ((JButton) c).setMaximumSize (new Dimension (tam, tam));
	  }
      }
  }
  
  public static void preparaFontes ()
  {
    boolean temArial = false;
    boolean temSansSerif = false;
    boolean temDialog = false;
    Font fontDefault = null;
    Toolkit tk = Toolkit.getDefaultToolkit ();
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment ();
    String[] fontnames = ge.getAvailableFontFamilyNames ();
    for (int i = 0; i < fontnames.length; i++)
      {
	if (fontnames[i].equals ("SansSerif"))
	  temSansSerif = true;
	else if (fontnames[i].equals ("Arial"))
	  temArial = true;
	else if (fontnames[i].equals ("Dialog"))
	  temDialog = true;
      }
    if (System.getProperty ("os.name").startsWith ("Linux"))
      {
	if (temSansSerif)
	  fontDefault = new Font ("SansSerif", 0, 10);
	else if (temArial)
	  fontDefault = new Font ("Arial", 0, 10);
	else if (temDialog)
	  fontDefault = new Font ("Dialog", 0, 10);
	else
	  fontDefault = new Font ("Default", 0, 10);
      }
    else if (temSansSerif)
      fontDefault = new Font ("SansSerif", 0, 11);
    else if (temArial)
      fontDefault = new Font ("Arial", 0, 10);
    else if (temDialog)
      fontDefault = new Font ("Dialog", 0, 10);
    else
      fontDefault = new Font ("Default", 0, 10);
    System.out.println ("fontdefault: " + fontDefault);
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
    FONTE_DEFAULT = fontDefault;
  }
  
  private void configuraHelp ()
  {
    plataforma.getHelp ().setNavigatorVisible (false);
    plataforma.setHelpID ("menuAjudaConteudo", "conteudo");
    plataforma.setHelpID ("menuAjudaInstrucoes", "instrucoes");
    plataforma.setHelpID ("menuAjudaComo", "ASSISTENTE");
    plataforma.setHelpID ("menuAjudaErros", "erros");
    plataforma.setHelpID ("menuAjudaPerguntas", "perguntas");
    plataforma.setHelpID ("btnAjuda", "conteudo");
  }
  
  private static void executaITR () throws IOException, FileNotFoundException
  {
    if (! permiteMaisDeUmaInstanciaAplicacao)
      {
	RandomAccessFile raf = new RandomAccessFile ("TryLock.txt", "rw");
	java.nio.channels.FileLock lock = raf.getChannel ().tryLock ();
	if (lock == null)
	  {
	    System.out.println ("Uma inst\u00e2ncia anterior est\u00e1 em execu\u00e7\u00e3o.");
	    JOptionPane.showMessageDialog (null, "Uma inst\u00e2ncia anterior da aplica\u00e7\u00e3o est\u00e1 em execu\u00e7\u00e3o", "PPGD", 2);
	    System.exit (1);
	  }
      }
    new PgdIRPF ();
  }
  
  private static void chamaHelp ()
  {
    VisualizadorHelp visualizador = new VisualizadorHelp ();
    visualizador.exibe ();
    Frame.getFrames ()[0].addWindowListener (new WindowAdapter ()
    {
      public void windowClosing (WindowEvent e)
      {
	System.exit (0);
      }
    });
  }
  
  public static void main (String[] args) throws Exception
  {
    PrintStream p = new PrintStream (new FileOutputStream ("Log.txt"));
    preparaFontes ();
    if (args.length > 0)
      {
	if (args[0].equals ("-h") || args[0].equals ("--help"))
	  chamaHelp ();
	else
	  executaITR ();
      }
    else
      executaITR ();
  }
}
