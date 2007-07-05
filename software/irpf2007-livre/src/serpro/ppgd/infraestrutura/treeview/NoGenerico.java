/* NoGenerico - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.treeview;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import serpro.ppgd.infraestrutura.acoes.AcaoMudaPainel;

public class NoGenerico extends DefaultMutableTreeNode
{
  private String nome = "";
  private String comando = "";
  private Icon icone;
  private Icon iconeSelecionado;
  private Icon iconePressionado;
  private Icon iconeDesabilitado;
  private Icon iconeToggled;
  private Action action;
  private String nomeAbreviado = null;
  private String tooltip = null;
  private TreeNode oldParent = null;
  private Icon iconeExpandido;
  private Icon iconeContraido;
  private boolean actionHabilitado = true;
  private boolean selecionado = false;
  
  public NoGenerico ()
  {
    setUserObject (this);
  }
  
  public NoGenerico (String nome, Action action)
  {
    setLabel (nome);
    setAction (action);
    setUserObject (this);
  }
  
  public String getNome ()
  {
    return nome;
  }
  
  public Icon getIcone ()
  {
    return icone;
  }
  
  public Icon getIconeSelecionado ()
  {
    return iconeSelecionado;
  }
  
  public Icon getIconePressionado ()
  {
    return iconePressionado;
  }
  
  public Icon getIconeDesabilitado ()
  {
    return iconeDesabilitado;
  }
  
  public Icon getIconeToggled ()
  {
    return iconeToggled;
  }
  
  public Action getAction ()
  {
    return action;
  }
  
  public void setAction (Action action)
  {
    this.action = action;
  }
  
  public void setIcone (Icon icone)
  {
    this.icone = icone;
  }
  
  public void setIconeSelecionado (Icon icone)
  {
    iconeSelecionado = icone;
  }
  
  public void setIconePressionado (Icon icone)
  {
    iconePressionado = icone;
  }
  
  public void setIconeDesabilitado (Icon icone)
  {
    iconeDesabilitado = icone;
  }
  
  public void setIconeToggled (Icon icone)
  {
    iconeToggled = icone;
  }
  
  public void setIcone (String icone)
  {
    try
      {
	this.icone = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("/" + icone));
      }
    catch (Exception e)
      {
	this.icone = null;
      }
  }
  
  public void setIconeSelecionado (String icone)
  {
    try
      {
	iconeSelecionado = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("/" + icone));
      }
    catch (Exception e)
      {
	iconeSelecionado = null;
      }
  }
  
  public void setIconePressionado (String icone)
  {
    try
      {
	iconePressionado = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("/" + icone));
      }
    catch (Exception e)
      {
	iconePressionado = null;
      }
  }
  
  public void setIconeDesabilitado (String icone)
  {
    try
      {
	iconeDesabilitado = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("/" + icone));
      }
    catch (Exception e)
      {
	iconeDesabilitado = null;
      }
  }
  
  public void setIconeToggled (String icone)
  {
    try
      {
	iconeToggled = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("/" + icone));
      }
    catch (Exception e)
      {
	iconeToggled = null;
      }
  }
  
  public void setLabel (String nome)
  {
    if (nome.length () == 0)
      this.nome = "Node";
    else
      this.nome = nome;
  }
  
  public String toString ()
  {
    return getNome ();
  }
  
  public String getNomeAbreviado ()
  {
    return nomeAbreviado;
  }
  
  public void setNomeAbreviado (String nomeAbreviado)
  {
    this.nomeAbreviado = nomeAbreviado;
  }
  
  public Icon getIconeContraido ()
  {
    return iconeContraido;
  }
  
  public void setIconeContraido (Icon iconeContraido)
  {
    this.iconeContraido = iconeContraido;
  }
  
  public Icon getIconeExpandido ()
  {
    return iconeExpandido;
  }
  
  public void setIconeExpandido (Icon iconeExpandido)
  {
    this.iconeExpandido = iconeExpandido;
  }
  
  public void setIconeExpandido (String iconeExpandido)
  {
    try
      {
	this.iconeExpandido = new ImageIcon (this.getClass ().getResource ("/" + iconeExpandido));
      }
    catch (Exception e)
      {
	this.iconeExpandido = null;
      }
  }
  
  public void setIconeContraido (String iconeContraido)
  {
    try
      {
	this.iconeContraido = new ImageIcon (this.getClass ().getResource ("/" + iconeContraido));
      }
    catch (Exception e)
      {
	this.iconeContraido = null;
      }
  }
  
  public String getTooltip ()
  {
    return tooltip;
  }
  
  public void setTooltip (String tooltip)
  {
    this.tooltip = tooltip;
  }
  
  public void setActionCommand (String comando)
  {
    checaNecessidadeCache (comando);
    this.comando = comando;
  }
  
  public void add (MutableTreeNode newChild)
  {
    super.add (newChild);
  }
  
  private void checaNecessidadeCache (String comando)
  {
    if (action != null && action instanceof AcaoMudaPainel)
      {
	AcaoMudaPainel acaoMudaPainel = (AcaoMudaPainel) action;
	acaoMudaPainel.setPainelStr (comando);
      }
  }
  
  public String getActionCommand ()
  {
    return comando;
  }
  
  public boolean isActionHabilitado ()
  {
    return actionHabilitado;
  }
  
  public void setActionHabilitado (boolean actionHabilitado)
  {
    this.actionHabilitado = actionHabilitado;
  }
  
  public void setParent (MutableTreeNode newParent)
  {
    if (newParent == null)
      setOldParent (getParent ());
    super.setParent (newParent);
  }
  
  public void setOldParent (TreeNode oldParent)
  {
    this.oldParent = oldParent;
  }
  
  public TreeNode getOldParent ()
  {
    return oldParent;
  }
}
