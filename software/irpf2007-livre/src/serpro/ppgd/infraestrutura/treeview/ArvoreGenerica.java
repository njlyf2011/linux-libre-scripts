/* ArvoreGenerica - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.treeview;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import serpro.ppgd.gui.UtilitariosGUI;
import serpro.ppgd.infraestrutura.acoes.AcaoMudaPainel;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;

public class ArvoreGenerica extends JTree
{
  protected DefaultMutableTreeNode raizTree;
  protected Map vctNos;
  protected Map vctNosEscondidos;
  protected Map vctListaOrdenadaDeRotulos = new Hashtable ();
  protected int TAM_MIN = 25;
  protected String nodoInicial = "";
  private boolean minimizado = false;
  private String labelRaiz;
  private int tamanhoMaximo = 0;
  private int tamanhoMinimo = 0;
  
  public ArvoreGenerica ()
  {
    super (new NoGenerico ("", (Action) null));
    vctNos = new Hashtable ();
    vctNosEscondidos = new Hashtable ();
    raizTree = (DefaultMutableTreeNode) getModel ().getRoot ();
    setToggleClickCount (1);
    getSelectionModel ().setSelectionMode (1);
    setFocusable (false);
    ToolTipManager.sharedInstance ().registerComponent (this);
  }
  
  public void setFonteDados (String pXml)
  {
    Document definicoes = FabricaUtilitarios.carregarDOM (pXml);
    Element raiz = definicoes.getDocumentElement ();
    if (raiz.getNodeName ().trim ().equalsIgnoreCase ("arvore"))
      {
	try
	  {
	    String labelRaiz = raiz.getAttribute ("labelRaiz");
	    String tamanhoMaximo = raiz.getAttribute ("tamanhoMaximo");
	    String tamanhoMinimo = raiz.getAttribute ("tamanhoMinimo");
	    String classeCellRenderer = raiz.getAttribute ("cellRenderer");
	    String classeTreeSelectionListener = raiz.getAttribute ("treeSelectionListener");
	    String iconeExpandido = raiz.getAttribute ("iconeExpandido");
	    String iconeContraido = raiz.getAttribute ("iconeContraido");
	    String cellEditor = raiz.getAttribute ("cellEditor");
	    String editable = raiz.getAttribute ("editable");
	    if (labelRaiz != null && labelRaiz.trim ().length () > 0)
	      {
		((NoGenerico) raizTree).setActionHabilitado (false);
		((NoGenerico) raizTree).setLabel (labelRaiz);
		if (iconeExpandido.length () != 0)
		  ((NoGenerico) raizTree).setIconeExpandido (iconeExpandido);
		if (iconeContraido.length () != 0)
		  ((NoGenerico) raizTree).setIconeContraido (iconeContraido);
		raizTree.setUserObject (labelRaiz);
	      }
	    else
	      {
		super.setRootVisible (false);
		putClientProperty ("JTree.lineStyle", "None");
		((NoGenerico) raizTree).setActionHabilitado (false);
		String iconeVazio = "imagens/ico_vazio.gif";
		((NoGenerico) raizTree).setIconeExpandido (iconeVazio);
		((NoGenerico) raizTree).setIconeContraido (iconeVazio);
		raizTree.setUserObject (labelRaiz);
	      }
	    if (tamanhoMaximo != null && tamanhoMaximo.trim ().length () > 0)
	      setTamanhoMaximo (Integer.parseInt (tamanhoMaximo));
	    if (tamanhoMinimo != null && tamanhoMaximo.trim ().length () > 0)
	      setTamanhoMinimo (Integer.parseInt (tamanhoMinimo));
	    if (cellEditor != null && cellEditor.trim ().length () > 0)
	      {
		try
		  {
		    Class classeCellEditor = Class.forName (cellEditor);
		    TreeCellEditor treeCellEditor = (TreeCellEditor) classeCellEditor.newInstance ();
		    setCellEditor (treeCellEditor);
		  }
		catch (Exception e)
		  {
		    e.printStackTrace ();
		  }
	      }
	    if (editable != null && editable.trim ().length () > 0)
	      {
		if (editable.toLowerCase ().equals ("true"))
		  setEditable (true);
		else
		  setEditable (false);
	      }
	    try
	      {
		Class[] argumentosFormais = { javax.swing.JTree.class };
		Object[] argumentosReais = { this };
		if (classeCellRenderer.trim ().length () != 0)
		  {
		    Class classe = Class.forName (classeCellRenderer);
		    Constructor construtor = classe.getConstructor (argumentosFormais);
		    Object cellRenderer = construtor.newInstance (argumentosReais);
		    this.setCellRenderer ((TreeCellRenderer) cellRenderer);
		  }
		if (classeTreeSelectionListener.trim ().length () != 0)
		  {
		    Class classe = Class.forName (classeTreeSelectionListener);
		    Constructor construtor = classe.getConstructor (argumentosFormais);
		    Object treeSelectionListener = construtor.newInstance (argumentosReais);
		    addTreeSelectionListener ((TreeSelectionListener) treeSelectionListener);
		  }
	      }
	    catch (Exception e)
	      {
		e.printStackTrace ();
		throw new IllegalArgumentException ("Erro na instancia\u00e7\u00e3o do CellRenderer ou do TreeSelectionListener " + e.getMessage ());
	      }
	    obtemNodos (raiz, null);
	  }
	catch (Exception e)
	  {
	    LogPPGD.erro ("Erro no carregamento da \u00e1rvore a partir do XML:" + e.getMessage ());
	    e.printStackTrace ();
	    throw new IllegalArgumentException (e.getMessage ());
	  }
      }
    else
      throw new IllegalArgumentException ("Tag <arvore> n\u00e3o encontrada");
    ((DefaultTreeModel) getModel ()).nodeStructureChanged ((DefaultMutableTreeNode) getModel ().getRoot ());
  }
  
  private void obtemNodos (Element element, NoGenerico idLinkPai) throws Exception
  {
    if (element.hasChildNodes ())
      {
	NodeList filhos = element.getChildNodes ();
	for (int i = 0; i < filhos.getLength (); i++)
	  {
	    if (filhos.item (i).getNodeType () == 1)
	      {
		Element elementFilho = (Element) filhos.item (i);
		if (elementFilho.getNodeName ().trim ().equalsIgnoreCase ("nodo"))
		  {
		    String labelNodo = elementFilho.getAttribute ("label");
		    String classAction = elementFilho.getAttribute ("action");
		    String actionCmd = elementFilho.getAttribute ("actionCommand");
		    String painelStr = elementFilho.getAttribute ("painel");
		    String iconeExpandido = elementFilho.getAttribute ("iconeExpandido");
		    String iconeContraido = elementFilho.getAttribute ("iconeContraido");
		    String selecionado = elementFilho.getAttribute ("selecionado");
		    String tooltip = elementFilho.getAttribute ("tooltip");
		    String habilitado = elementFilho.getAttribute ("enable");
		    String valNodoInicial = elementFilho.getAttribute ("nodoInicial");
		    if (labelNodo.length () == 0)
		      labelNodo = "Node";
		    Action action = null;
		    NoGenerico idLinkAtual;
		    if (classAction.length () == 0)
		      {
			idLinkAtual = new NoGenerico ();
			idLinkAtual.setLabel (labelNodo);
		      }
		    else
		      {
			if (classAction.equals ("AcaoMudaPainel"))
			  action = new AcaoMudaPainel (painelStr);
			else
			  action = (Action) Class.forName (classAction).newInstance ();
			idLinkAtual = new NoGenerico (labelNodo, action);
		      }
		    if (actionCmd.length () > 0)
		      idLinkAtual.setActionCommand (actionCmd);
		    if (iconeExpandido.length () != 0)
		      idLinkAtual.setIconeExpandido (iconeExpandido);
		    if (iconeContraido.length () != 0)
		      idLinkAtual.setIconeContraido (iconeContraido);
		    if (valNodoInicial.length () != 0 || nodoInicial.trim ().length () == 0)
		      nodoInicial = labelNodo;
		    if (tooltip.trim ().length () != 0)
		      idLinkAtual.setTooltip (tooltip);
		    if (habilitado.trim ().length () != 0)
		      idLinkAtual.setActionHabilitado (habilitado.trim ().equalsIgnoreCase ("true"));
		    add (idLinkPai, idLinkAtual);
		    if (selecionado.trim ().equalsIgnoreCase ("sim"))
		      selecionarNodo (idLinkAtual.toString ());
		    obtemNodos (elementFilho, idLinkAtual);
		  }
	      }
	  }
      }
  }
  
  public String getRotuloNodo (String pActionCommand)
  {
    if (vctListaOrdenadaDeRotulos.containsKey (pActionCommand))
      return (String) vctListaOrdenadaDeRotulos.get (pActionCommand);
    return "";
  }
  
  public void add (NoGenerico topicoPai, NoGenerico topico)
  {
    vctListaOrdenadaDeRotulos.put (topico.getActionCommand (), topico.getNome ());
    if (topicoPai == null)
      raizTree.add (topico);
    else
      {
	DefaultMutableTreeNode nodoPai = (DefaultMutableTreeNode) vctNos.get (topicoPai.toString ());
	if (nodoPai == null)
	  nodoPai = (DefaultMutableTreeNode) vctNosEscondidos.get (topicoPai.toString ());
	if (nodoPai == null)
	  nodoPai = raizTree;
	nodoPai.add (topico);
      }
    vctNos.put (topico.toString (), topico);
    ((DefaultTreeModel) getModel ()).nodeStructureChanged (raizTree);
  }
  
  public void add (NoGenerico link)
  {
    raizTree.add (link);
    vctNos.put (link.toString (), link);
  }
  
  public void selecionaNodoInicial ()
  {
    selecionarNodo (nodoInicial);
  }
  
  public void setSelectionPath (TreePath pTree)
  {
    DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) pTree.getLastPathComponent ();
    if (nodo.getLevel () == 1)
      ((DefaultTreeModel) getModel ()).nodeStructureChanged (raizTree);
    else if (nodo.getLevel () == 2)
      {
	((DefaultTreeModel) getModel ()).nodeStructureChanged (nodo.getParent ());
	((DefaultTreeModel) getModel ()).nodeStructureChanged (raizTree);
      }
    else if (nodo.getLevel () == 3)
      {
	((DefaultTreeModel) getModel ()).nodeStructureChanged (nodo.getParent ());
	((DefaultTreeModel) getModel ()).nodeStructureChanged (nodo.getParent ().getParent ());
	((DefaultTreeModel) getModel ()).nodeStructureChanged (raizTree);
      }
    else if (nodo.getLevel () == 4)
      {
	((DefaultTreeModel) getModel ()).nodeStructureChanged (nodo.getParent ());
	((DefaultTreeModel) getModel ()).nodeStructureChanged (nodo.getParent ().getParent ());
	((DefaultTreeModel) getModel ()).nodeStructureChanged (nodo.getParent ().getParent ().getParent ());
	((DefaultTreeModel) getModel ()).nodeStructureChanged (raizTree);
      }
    super.setSelectionPath (pTree);
  }
  
  public void preparaNavegador (JTree pTree, DefaultMutableTreeNode pUltimoNodo)
  {
    /* empty */
  }
  
  public DefaultMutableTreeNode getRaiz ()
  {
    return raizTree;
  }
  
  public Map getVctNos ()
  {
    return vctNos;
  }
  
  public void setRaiz (DefaultMutableTreeNode node)
  {
    raizTree = node;
  }
  
  public void minimiza ()
  {
    minimizado = true;
    setExpandsSelectedPaths (false);
    UtilitariosGUI.setParametrosGUI (this, getSize ().width, getTamanhoMinimo ());
    collapseRow (0);
  }
  
  public void maximiza ()
  {
    minimizado = false;
    setExpandsSelectedPaths (true);
    UtilitariosGUI.setParametrosGUI (this, getSize ().width, getTamanhoMaximo ());
    expandRow (0);
  }
  
  public void esconde ()
  {
    UtilitariosGUI.setParametrosGUI (this, 0, 0);
  }
  
  public boolean isMinimizado ()
  {
    return minimizado;
  }
  
  public void setMinimizado (boolean minimizado)
  {
    this.minimizado = minimizado;
  }
  
  public void selecionarNodo (String pLabelNodo)
  {
    if (vctNos.containsKey (pLabelNodo))
      {
	NoGenerico nodo = (NoGenerico) vctNos.get (pLabelNodo);
	setSelectionPath (encontrarObjetoNaTree (nodo));
      }
  }
  
  public void removerNodo (String pLabelNodo)
  {
    if (vctNos.containsKey (pLabelNodo))
      {
	NoGenerico nodo = (NoGenerico) vctNos.get (pLabelNodo);
	vctNos.remove (nodo.toString ());
	((DefaultTreeModel) getModel ()).removeNodeFromParent (nodo);
	((DefaultTreeModel) getModel ()).nodeStructureChanged (raizTree);
      }
  }
  
  public void setNodoEnabled (String pLabelNodo, boolean pOpt)
  {
    if (vctNos.containsKey (pLabelNodo))
      {
	NoGenerico nodo = (NoGenerico) vctNos.get (pLabelNodo);
	nodo.setActionHabilitado (pOpt);
	repaint ();
      }
  }
  
  private void escondeNodo (String pLabelNodo)
  {
    if (vctNos.containsKey (pLabelNodo))
      {
	NoGenerico nodo = (NoGenerico) vctNos.remove (pLabelNodo);
	((DefaultTreeModel) getModel ()).removeNodeFromParent (nodo);
	((DefaultTreeModel) getModel ()).nodeStructureChanged (raizTree);
	vctNosEscondidos.put (nodo.toString (), nodo);
      }
  }
  
  private void mostraNodo (String pLabelNodo)
  {
    if (vctNosEscondidos.containsKey (pLabelNodo))
      {
	NoGenerico nodo = (NoGenerico) vctNosEscondidos.remove (pLabelNodo);
	NoGenerico nodoPai = (NoGenerico) nodo.getParent ();
	if (nodoPai == null)
	  nodoPai = (NoGenerico) nodo.getOldParent ();
	add (nodoPai, nodo);
      }
  }
  
  public void setNodoVisivel (String pLabelNodo, boolean pVisivel)
  {
    if (pVisivel)
      mostraNodo (pLabelNodo);
    else
      escondeNodo (pLabelNodo);
  }
  
  public void escondeTodosNodos ()
  {
    Iterator it = new ArrayList (vctNos.values ()).iterator ();
    while (it.hasNext ())
      {
	NoGenerico no = (NoGenerico) it.next ();
	escondeNodo (no.toString ());
      }
  }
  
  public TreePath encontrarObjetoNaTree (Object object)
  {
    Enumeration nodes = ((DefaultMutableTreeNode) getModel ().getRoot ()).preorderEnumeration ();
    while (nodes.hasMoreElements ())
      {
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes.nextElement ();
	if (node.getUserObject () == object)
	  return new TreePath (node.getPath ());
      }
    return null;
  }
  
  public int getTamanhoMaximo ()
  {
    return tamanhoMaximo;
  }
  
  public void setTamanhoMaximo (int tamanhoMaximo)
  {
    this.tamanhoMaximo = tamanhoMaximo;
  }
  
  public int getTamanhoMinimo ()
  {
    return tamanhoMinimo;
  }
  
  public void setTamanhoMinimo (int tamanhoMinimo)
  {
    this.tamanhoMinimo = tamanhoMinimo;
  }
  
  public String getLabelRaiz ()
  {
    return labelRaiz;
  }
  
  public void setLabelRaiz (String labelRaiz)
  {
    this.labelRaiz = labelRaiz;
    raizTree.setUserObject (labelRaiz);
  }
  
  public DefaultMutableTreeNode getRaizTree ()
  {
    return raizTree;
  }
  
  public void setRaizTree (DefaultMutableTreeNode raizTree)
  {
    this.raizTree = raizTree;
  }
  
  public void setCellRenderer (Constructor c)
  {
    Object[] arg = { this };
    try
      {
	this.setCellRenderer ((TreeCellRenderer) c.newInstance (arg));
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public void setTreeSelectionListener (Constructor c)
  {
    Object[] arg = { this };
    try
      {
	addTreeSelectionListener ((TreeSelectionListener) c.newInstance (arg));
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public String getNodoInicial ()
  {
    return nodoInicial;
  }
  
  public void setNodoInicial (String nodoInicial)
  {
    this.nodoInicial = nodoInicial;
  }
  
}
