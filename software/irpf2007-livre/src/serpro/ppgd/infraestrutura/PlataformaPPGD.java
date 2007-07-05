/* PlataformaPPGD - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.Silver;

import org.swixml.ConverterLibrary;
import org.swixml.SwingEngine;
import /*org.swixml...*/serpro.ppgd.infraestrutura/*...*/.converters.CellConstraintsConverter;

import serpro.ppgd.formatosexternos.ListaRelatoriosIf;
import serpro.ppgd.gui.EditCampo;
import serpro.ppgd.gui.OpcaoDialog;
import serpro.ppgd.infraestrutura.converters.ActionConverter;
import serpro.ppgd.infraestrutura.converters.ConstructorConverter;
import serpro.ppgd.infraestrutura.converters.DimensaoArrConverter;
import serpro.ppgd.infraestrutura.converters.FormLayoutConverter;
import serpro.ppgd.infraestrutura.converters.InformacaoConverter;
import serpro.ppgd.infraestrutura.treeview.ArvoreGenerica;
import serpro.ppgd.infraestrutura.util.HelpUtil;
import serpro.ppgd.negocio.PPGDFacade;
import serpro.ppgd.negocio.util.FabricaTratamentoErro;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.UtilitariosArquivo;

public class PlataformaPPGD
{
  protected static PlataformaPPGD plataformaDefault = null;
  private SwingEngine swingEngine = null;
  protected Map listaPaineisInstanciados = new HashMap ();
  private Collection colListaRelatorios = new ArrayList ();
  private HelpUtil help;
  private static boolean emDesign = true;
  private boolean comportamentoPadraoSair = true;
  
  public PlataformaPPGD ()
  {
    init ();
  }
  
  public void init ()
  {
    SwingEngine engine = new SwingEngine (this);
    setSwingEngine (engine);
    registraConverters ();
    registraTags ();
  }
  
  public void cadastraPainel (String chave, JPanel pPainel)
  {
    listaPaineisInstanciados.put (chave, pPainel);
  }
  
  public boolean existePainelCadastrado (String chave)
  {
    return listaPaineisInstanciados.containsKey (chave);
  }
  
  public JPanel getPainel (String chave)
  {
    return (JPanel) listaPaineisInstanciados.get (chave);
  }
  
  public HelpUtil getHelp ()
  {
    return help;
  }
  
  public void setHelpID (Component componente, String id)
  {
    if (help != null)
      help.setHelpID (componente, id);
  }
  
  public void habilitaHelp (JRootPane root, String id)
  {
    if (help != null)
      help.enableHelpKey (root, id);
  }
  
  public void habilitaHelp (JPanel p, String id)
  {
    if (help != null)
      help.enableHelpKey (p, id);
  }
  
  public void setHelpID (String nomeComponente, String id)
  {
    if (help != null)
      {
	JComponent c = (JComponent) getComponent (nomeComponente);
	help.setHelpID (c, id);
      }
  }
  
  public void setHelpID (EditCampo edit, String id)
  {
    if (help != null)
      help.setHelpID (edit.getComponenteFoco (), id);
  }
  
  public void setHelpID (String nomeComponente)
  {
    if (help != null)
      {
	JComponent c = (JComponent) getComponent (nomeComponente);
	String helpID = c.getClientProperty ("helpID").toString ();
	help.setHelpID (c, helpID);
      }
  }
  
  protected void registraTags ()
  {
    getSwingEngine ().getTaglib ().registerTag ("arvore", serpro.ppgd.infraestrutura.treeview.ArvoreGenerica.class);
    getSwingEngine ().getTaglib ().registerTag ("nodo", serpro.ppgd.infraestrutura.treeview.NoGenerico.class);
    getSwingEngine ().getTaglib ().registerTag ("debugpanel", com.jgoodies.forms.debug.FormDebugPanel.class);
    getSwingEngine ().getTaglib ().registerTag ("formpanel", serpro.ppgd.infraestrutura.util.PPGDFormPanel.class);
    getSwingEngine ().getTaglib ().registerTag ("editCodigo", serpro.ppgd.gui.EditCodigo.class);
    getSwingEngine ().getTaglib ().registerTag ("editMemo", serpro.ppgd.gui.EditMemo.class);
    getSwingEngine ().getTaglib ().registerTag ("editValor", serpro.ppgd.gui.EditValor.class);
    getSwingEngine ().getTaglib ().registerTag ("editAlfa", serpro.ppgd.gui.EditAlfa.class);
    getSwingEngine ().getTaglib ().registerTag ("editCpf", serpro.ppgd.gui.EditCPF.class);
    getSwingEngine ().getTaglib ().registerTag ("editCnpj", serpro.ppgd.gui.EditCNPJ.class);
    getSwingEngine ().getTaglib ().registerTag ("editCEP", serpro.ppgd.gui.EditCEP.class);
    getSwingEngine ().getTaglib ().registerTag ("editData", serpro.ppgd.gui.EditData.class);
    getSwingEngine ().getTaglib ().registerTag ("editInteiro", serpro.ppgd.gui.EditInteiro.class);
    getSwingEngine ().getTaglib ().registerTag ("editNI", serpro.ppgd.gui.EditNI.class);
    getSwingEngine ().getTaglib ().registerTag ("editNIRF", serpro.ppgd.gui.EditNIRF.class);
    getSwingEngine ().getTaglib ().registerTag ("editNumeroRecibo", serpro.ppgd.gui.EditNumeroRecibo.class);
    getSwingEngine ().getTaglib ().registerTag ("editTelefone", serpro.ppgd.gui.EditTelefone.class);
    getSwingEngine ().getTaglib ().registerTag ("editTituloEleitor", serpro.ppgd.gui.EditTituloEleitor.class);
    getSwingEngine ().getTaglib ().registerTag ("comando", java.lang.Object.class);
  }
  
  protected void registraConverters ()
  {
    ConverterLibrary.getInstance ().register (com.jgoodies.forms.layout.CellConstraints.class, new CellConstraintsConverter ());
    ConverterLibrary.getInstance ().register (java.awt.LayoutManager.class, new FormLayoutConverter ());
    ConverterLibrary.getInstance ().register (java.lang.reflect.Constructor.class, new ConstructorConverter ());
    ConverterLibrary.getInstance ().register (javax.swing.Action.class, new ActionConverter ());
    ConverterLibrary.getInstance ().register (java.awt.Dimension[].class, new DimensaoArrConverter ());
    ConverterLibrary.getInstance ().register (serpro.ppgd.negocio.Informacao.class, new InformacaoConverter ());
  }
  
  public static void setPlataforma (PlataformaPPGD aPlataforma)
  {
    plataformaDefault = aPlataforma;
  }
  
  public static PlataformaPPGD getPlataforma ()
  {
    if (plataformaDefault == null)
      plataformaDefault = new PlataformaPPGD ();
    return plataformaDefault;
  }
  
  public void carrega ()
  {
    try
      {
	setEmDesign (false);
	if (System.getProperty ("os.name").toUpperCase ().startsWith ("LINUX"))
	  {
	    // PlasticLookAndFeel.setMyCurrentTheme (new Silver ());
	    UIManager.setLookAndFeel (new PlasticXPLookAndFeel ());
	  }
	else
	  UIManager.setLookAndFeel (Options.getSystemLookAndFeelClassName ());
	UIManager.put ("OptionPane.yesButtonText", "Sim");
	UIManager.put ("OptionPane.yesButtonMnemonic", "83");
	UIManager.put ("OptionPane.noButtonText", "N\u00e3o");
	UIManager.put ("OptionPane.noButtonMnemonic", "78");
	UIManager.put ("OptionPane.okButtonText", "Ok");
	UIManager.put ("OptionPane.okButtonMnemonic", "79");
	UIManager.put ("OptionPane.cancelButtonText", "Cancelar");
	UIManager.put ("OptionPane.cancelButtonMnemonic", "83");
	URL ppgdDefault = UtilitariosArquivo.localizaArquivoEmClasspath ("/ppgd-default.xml");
	getSwingEngine ().render (ppgdDefault);
	carregaHelp ();
	getJanelaPrincipal ().addWindowListener (new WindowAdapter ()
	{
	  public void windowClosing (WindowEvent e)
	  {
	    if (isComportamentoPadraoSair ())
	      exitPgd ();
	  }
	});
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public void exitPgd ()
  {
    try
      {
	String classeFacade = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.classes.facade", "serpro.ppgd.repositorio.FacadeDefault");
	Class classe = Class.forName (classeFacade);
	Method methGetInstancia = classe.getMethod ("getInstancia", null);
	PPGDFacade facade = (PPGDFacade) methGetInstancia.invoke (null, null);
	Method methGetDeclaracao = classe.getMethod ("salvaUltimaDeclaracaoAberta", null);
	methGetDeclaracao.invoke (facade, null);
      }
    catch (Exception exception)
      {
	/* empty */
      }
    System.exit (0);
  }
  
  public void exibe ()
  {
    Dimension dim = obtemResolucao ();
    if (dim != null)
      getJanelaPrincipal ().setSize (dim);
    getJanelaPrincipal ().setLocationRelativeTo (null);
    getJanelaPrincipal ().setVisible (true);
  }
  
  private Dimension obtemResolucao ()
  {
    String resolucao = FabricaUtilitarios.getProperties ().getProperty ("resolucao", "");
    if (resolucao.trim ().length () == 0)
      return null;
    StringTokenizer st = new StringTokenizer (resolucao, ",");
    int width = 0;
    int height = 0;
    if (st.hasMoreTokens ())
      width = Integer.parseInt (st.nextToken ().trim ());
    if (st.hasMoreTokens ())
      height = Integer.parseInt (st.nextToken ().trim ());
    return new Dimension (width, height);
  }
  
  public void refreshJanelaPrincipal ()
  {
    Dimension d = getJanelaPrincipal ().getSize ();
    d.width++;
    d.height++;
    getJanelaPrincipal ().setSize (d);
    d.width--;
    d.height--;
    getJanelaPrincipal ().setSize (d);
  }
  
  public JFrame getJanelaPrincipal ()
  {
    return (JFrame) getSwingEngine ().find ("mainframe");
  }
  
  public JPanel getPainelPrincipal ()
  {
    return (JPanel) getSwingEngine ().find ("pnlPrincipal");
  }
  
  public Component getAreaPaineis ()
  {
    return getSwingEngine ().find ("pnlPPGD");
  }
  
  public Component getScrollAreaPaineis ()
  {
    return getSwingEngine ().find ("ppgdScrollPnlPPGD");
  }
  
  public Component getAreaArvore ()
  {
    return getSwingEngine ().find ("ppgdAreaArvore");
  }
  
  public Component getArvore ()
  {
    return getSwingEngine ().find ("ppgdarvore");
  }
  
  public Component getToolbar ()
  {
    return getSwingEngine ().find ("tbPPGD");
  }
  
  public Component getPainelSplit ()
  {
    return getSwingEngine ().find ("splitPPGD");
  }
  
  public void aplicaMenuBar (String aXml)
  {
    try
      {
	SwingEngine menuEngine = new SwingEngine ();
	JMenuBar menuBar = (JMenuBar) menuEngine.render (aXml);
	getJanelaPrincipal ().setJMenuBar (menuBar);
	getSwingEngine ().getIdMap ().putAll (menuEngine.getIdMap ());
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public void aplicaArvore (String aFonteArvore)
  {
    JScrollPane areaArvore = (JScrollPane) getAreaArvore ();
    if (aFonteArvore.endsWith (".xml"))
      {
	ArvoreGenerica arvore = new ArvoreGenerica ();
	areaArvore.setViewportView (arvore);
      }
    else
      {
	try
	  {
	    Class c = Class.forName (aFonteArvore);
	    areaArvore.setViewportView ((Component) c.newInstance ());
	  }
	catch (Exception e)
	  {
	    e.printStackTrace ();
	  }
      }
  }
  
  public Component getComponent (String aNome)
  {
    return getSwingEngine ().find (aNome);
  }
  
  public void setSwingEngine (SwingEngine swingEngine)
  {
    this.swingEngine = swingEngine;
  }
  
  public SwingEngine getSwingEngine ()
  {
    return swingEngine;
  }
  
  public void mudaPainelExibido (JPanel painelAcionado)
  {
    Container areaPaineis = (Container) getAreaPaineis ();
    areaPaineis.removeAll ();
    areaPaineis.add (painelAcionado);
    areaPaineis.validate ();
    areaPaineis.repaint ();
    getScrollAreaPaineis ().repaint ();
    ((JScrollPane) getScrollAreaPaineis ()).revalidate ();
  }
  
  public void componentEnabled (String id, boolean enabled)
  {
    getSwingEngine ().find (id).setEnabled (enabled);
  }
  
  public void componentVisible (String id, boolean visible)
  {
    getSwingEngine ().find (id).setVisible (visible);
  }
  
  public void exibeDialog (JPanel p, String titulo, boolean modal)
  {
    JDialog dialogo = new JDialog (getJanelaPrincipal (), titulo, modal);
    dialogo.getContentPane ().add (p);
    dialogo.setSize (dialogo.getPreferredSize ());
    dialogo.setResizable (false);
    dialogo.setLocationRelativeTo (null);
    dialogo.pack ();
    dialogo.setVisible (true);
  }
  
  public void exibeDialog (String xml, String titulo, boolean modal)
  {
    try
      {
	JPanel p = (JPanel) swingEngine.render (this.getClass ().getResource (xml));
	exibeDialog (p, titulo, modal);
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	FabricaTratamentoErro.getTrataErroSistemico ().trataErroSistemico (e);
      }
  }
  
  public int exibeDialogOpcoes (String titulo, String texto, String[] opcoes, boolean modal)
  {
    return new OpcaoDialog (getJanelaPrincipal (), titulo, texto, opcoes, modal).showDialog (titulo);
  }
  
  public Collection getListaRelatorios ()
  {
    return colListaRelatorios;
  }
  
  public void registraListaRelatorio (ListaRelatoriosIf aListaRelatorio)
  {
    colListaRelatorios.add (aListaRelatorio);
  }
  
  public JPanel instanciaPainel (String painelStr) throws Exception, ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    JPanel painelInstanciado = null;
    if (painelStr.endsWith (".xml"))
      {
	SwingEngine engine = new SwingEngine ();
	painelInstanciado = (JPanel) engine.render (painelStr);
      }
    else if (painelStr.trim ().length () > 0)
      {
	Class c = Class.forName (painelStr);
	painelInstanciado = (JPanel) c.newInstance ();
      }
    return painelInstanciado;
  }
  
  public void limpaPainelPrincipal ()
  {
    componentVisible ("splitPPGD", false);
  }
  
  public void mostraPainelPrincipal ()
  {
    componentVisible ("splitPPGD", true);
  }
  
  public void desabilitaComponentes (String[] componentes)
  {
    for (int i = 0; i < componentes.length; i++)
      componentEnabled (componentes[i], false);
  }
  
  public void habilitaComponentes (String[] componentes)
  {
    for (int i = 0; i < componentes.length; i++)
      componentEnabled (componentes[i], true);
  }
  
  public boolean temDeclaracoes ()
  {
    boolean bool;
    try
      {
	String classeFacade = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.classes.facade", "serpro.ppgd.repositorio.FacadeDefault");
	Class classe = Class.forName (classeFacade);
	Method methGetInstancia = classe.getMethod ("getInstancia", null);
	PPGDFacade facade = (PPGDFacade) methGetInstancia.invoke (null, null);
	Method methExisteDeclaracoes = classe.getMethod ("existeDeclaracoes", null);
	bool = ((Boolean) methExisteDeclaracoes.invoke (facade, null)).booleanValue ();
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	return false;
      }
    return bool;
  }
  
  protected void carregaHelp ()
  {
    String helpSetName = FabricaUtilitarios.getProperties ().getProperty ("help", "");
    String defaultHelpID = FabricaUtilitarios.getProperties ().getProperty ("help.default.id", "");
    if (! helpSetName.equals (""))
      {
	help = new HelpUtil (getJanelaPrincipal ().getRootPane (), defaultHelpID);
	String appPath = FabricaUtilitarios.getPathCompletoDirAplicacao ();
	appPath = null;
	help.setHelpSet (helpSetName, appPath);
	String iconPath = appPath + FabricaUtilitarios.getProperties ().getProperty ("help.icon", "");
	ImageIcon icon = new ImageIcon (iconPath);
	help.setIcon (icon);
      }
  }
  
  public static void setEmDesign (boolean emDesign)
  {
    PlataformaPPGD.emDesign = emDesign;
  }
  
  public static boolean isEmDesign ()
  {
    return emDesign;
  }
  
  public void setComportamentoPadraoSair (boolean comportamentoPadraoSair)
  {
    this.comportamentoPadraoSair = comportamentoPadraoSair;
  }
  
  public boolean isComportamentoPadraoSair ()
  {
    return comportamentoPadraoSair;
  }
  
}
