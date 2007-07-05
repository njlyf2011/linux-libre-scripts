/* JButtonGroupPanel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.EventListenerList;

import serpro.ppgd.negocio.Alfa;

public class JButtonGroupPanel extends JEditCampo implements ActionListener, FocusListener
{
  private ButtonGroup buttonGroup;
  private Map buttons;
  private boolean selecaoMultipla;
  private String delimitadorOpcoes;
  private EventListenerList listeners;
  
  public JButtonGroupPanel ()
  {
    super ((serpro.ppgd.negocio.Informacao) new Alfa (null, "", 20));
    buttonGroup = new ButtonGroup ();
    buttons = new Hashtable ();
    selecaoMultipla = false;
    delimitadorOpcoes = "#";
    listeners = new EventListenerList ();
    setBorder (BorderFactory.createEtchedBorder ());
  }
  
  private void preparaFocusListener (AbstractButton btn)
  {
    btn.addFocusListener (this);
  }
  
  public JButtonGroupPanel (Alfa campo)
  {
    super ((serpro.ppgd.negocio.Informacao) campo);
    buttonGroup = new ButtonGroup ();
    buttons = new Hashtable ();
    selecaoMultipla = false;
    delimitadorOpcoes = "#";
    listeners = new EventListenerList ();
  }
  
  public JButtonGroupPanel (Alfa campo, String idAjuda)
  {
    super ((serpro.ppgd.negocio.Informacao) campo, idAjuda);
    buttonGroup = new ButtonGroup ();
    buttons = new Hashtable ();
    selecaoMultipla = false;
    delimitadorOpcoes = "#";
    listeners = new EventListenerList ();
  }
  
  protected void instanciaComponentes ()
  {
    buttonGroup = new ButtonGroup ();
    buttons = new Hashtable ();
    selecaoMultipla = false;
  }
  
  public void actionPerformed (ActionEvent e)
  {
    if (e.getSource () != null && e.getSource () instanceof BotaoItemIf)
      {
	String strVal = montaConteudo ();
	setIdentificacaoFoco (false);
	if (getInformacao ().getListaValidadoresImpeditivos ().isEmpty () || verificaValidacoesImpeditivas (strVal))
	  {
	    setarCampo (e, strVal);
	    chamaValidacao ();
	  }
      }
  }
  
  private String montaConteudo ()
  {
    Iterator iter = buttons.values ().iterator ();
    StringBuffer strVal = new StringBuffer ();
    while (iter.hasNext ())
      {
	AbstractButton radio = (AbstractButton) iter.next ();
	if (radio.isSelected ())
	  {
	    strVal.append (((BotaoItemIf) radio).getValorSelecionadoTrue ());
	    strVal.append (delimitadorOpcoes);
	  }
      }
    if (strVal.toString ().endsWith (delimitadorOpcoes))
      strVal.deleteCharAt (strVal.length () - 1);
    return strVal.toString ();
  }
  
  public void setarCampo (ActionEvent e, String strVal)
  {
    getInformacao ().setConteudo (strVal);
    GroupPanelEvent evt = new GroupPanelEvent (e.getSource ());
    evt.setInformacao (getInformacao ());
    fireAtualizaPanel (evt);
  }
  
  public void focusGained (FocusEvent e)
  {
    /* empty */
  }
  
  public void focusLost (FocusEvent e)
  {
    /* empty */
  }
  
  private void fireAtualizaPanel (GroupPanelEvent evt)
  {
    EventListenerList eventlistenerlist = listeners;
    Class var_class = serpro.ppgd.gui.xbeans.GroupPanelListener.class;
    GroupPanelListener[] navListeners = (GroupPanelListener[]) eventlistenerlist.getListeners (var_class);
    for (int i = 0; i < navListeners.length; i++)
      navListeners[i].atualizaPainel (evt);
  }
  
  public void addGroupPanelListener (GroupPanelListener listener)
  {
    EventListenerList eventlistenerlist = listeners;
    Class var_class = serpro.ppgd.gui.xbeans.GroupPanelListener.class;
    eventlistenerlist.add (var_class, listener);
  }
  
  public void removeGroupPanelListener (GroupPanelListener listener)
  {
    EventListenerList eventlistenerlist = listeners;
    Class var_class = serpro.ppgd.gui.xbeans.GroupPanelListener.class;
    eventlistenerlist.remove (var_class, listener);
  }
  
  public Component add (Component comp, int index)
  {
    adicionaOpcao (comp);
    return super.add (comp, index);
  }
  
  public void adicionaOpcao (Component comp)
  {
    comp.setEnabled (isEnabled ());
    if (comp instanceof BotaoItemIf)
      {
	((AbstractButton) comp).addActionListener (this);
	buttons.put (comp, comp);
	if (! isSelecaoMultipla ())
	  buttonGroup.add ((AbstractButton) comp);
	((BotaoItemIf) comp).setEstiloFonte (getEstiloFonte ());
	((BotaoItemIf) comp).setIncrementoTamanhoFonte (getIncrementoTamanhoFonte ());
	implementacaoPropertyChange (null);
      }
    else if (comp instanceof JButtonMensagem)
      setButtonMensagem ((JButtonMensagem) comp);
  }
  
  public void add (Component comp, Object constraints, int index)
  {
    adicionaOpcao (comp);
    super.add (comp, constraints, index);
  }
  
  public void add (Component comp, Object constraints)
  {
    adicionaOpcao (comp);
    super.add (comp, constraints);
  }
  
  public Component add (Component comp)
  {
    adicionaOpcao (comp);
    return super.add (comp);
  }
  
  public Component add (String name, Component comp)
  {
    adicionaOpcao (comp);
    return super.add (name, comp);
  }
  
  public void remove (Component comp)
  {
    if (buttons != null && comp instanceof BotaoItemIf)
      {
	buttons.remove (comp);
	buttonGroup.remove ((AbstractButton) comp);
      }
    super.remove (comp);
  }
  
  public void removeAll ()
  {
    if (buttons != null)
      {
	buttons.clear ();
	removeDoButtonGroup (true);
      }
    super.removeAll ();
  }
  
  private void removeDoButtonGroup (boolean removeListener)
  {
    buttonGroup = new ButtonGroup ();
    Iterator itOpcoes = buttons.values ().iterator ();
    while (itOpcoes.hasNext ())
      {
	AbstractButton radio = (AbstractButton) itOpcoes.next ();
	radio.getModel ().setGroup (null);
	if (removeListener)
	  radio.removeActionListener (this);
      }
  }
  
  public boolean isSelecaoMultipla ()
  {
    return selecaoMultipla;
  }
  
  public void setSelecaoMultipla (boolean selecaoMul)
  {
    selecaoMultipla = selecaoMul;
    removeDoButtonGroup (false);
    if (! isSelecaoMultipla ())
      readicionaAoButtonGroup ();
  }
  
  private void readicionaAoButtonGroup ()
  {
    Iterator it = buttons.values ().iterator ();
    while (it.hasNext ())
      buttonGroup.add ((AbstractButton) it.next ());
  }
  
  protected void buildComponente ()
  {
    /* empty */
  }
  
  protected void informacaoModificada ()
  {
    implementacaoPropertyChange (null);
  }
  
  protected void readOnlyPropertyChange (boolean readOnly)
  {
    setEnabled (! readOnly);
    Iterator itOpcoes = buttons.values ().iterator ();
    while (itOpcoes.hasNext ())
      {
	AbstractButton radio = (AbstractButton) itOpcoes.next ();
	radio.setEnabled (! readOnly);
      }
    if (getButtonMensagem () != null)
      getButtonMensagem ().setEnabled (! readOnly);
  }
  
  protected void habilitadoPropertyChange (boolean habilitado)
  {
    setEnabled (habilitado);
    Iterator itOpcoes = buttons.values ().iterator ();
    while (itOpcoes.hasNext ())
      {
	AbstractButton radio = (AbstractButton) itOpcoes.next ();
	radio.setEnabled (habilitado);
      }
    if (getButtonMensagem () != null)
      getButtonMensagem ().setEnabled (habilitado);
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    if (getInformacao ().isVazio ())
      {
	removeDoButtonGroup (false);
	Iterator itOpcoes = buttons.values ().iterator ();
	while (itOpcoes.hasNext ())
	  {
	    AbstractButton radio = (AbstractButton) itOpcoes.next ();
	    radio.setSelected (false);
	  }
      }
    else
      {
	readicionaAoButtonGroup ();
	Map listaTemp = new Hashtable ();
	Iterator itOpcoes = buttons.values ().iterator ();
	while (itOpcoes.hasNext ())
	  {
	    AbstractButton radio = (AbstractButton) itOpcoes.next ();
	    listaTemp.put (((BotaoItemIf) radio).getValorSelecionadoTrue (), radio);
	  }
	StringTokenizer strTokens = new StringTokenizer (getInformacao ().asString (), delimitadorOpcoes);
	while (strTokens.hasMoreTokens ())
	  {
	    String opt = strTokens.nextToken ();
	    AbstractButton radio = (AbstractButton) listaTemp.get (opt);
	    if (radio != null)
	      radio.setSelected (true);
	  }
      }
  }
  
  public JComponent getComponenteEditor ()
  {
    if (! buttons.isEmpty ())
      return (AbstractButton) buttons.values ().toArray ()[0];
    return this;
  }
  
  public JComponent getComponenteFoco ()
  {
    if (! buttons.isEmpty ())
      return (AbstractButton) buttons.values ().toArray ()[0];
    return this;
  }
  
  public void setEstiloFonte (int estilo)
  {
    Font f = getFont ();
    f = f == null ? new JLabel ().getFont () : f;
    f = f.deriveFont (estilo);
    setFont (f);
    Iterator itOpcoes = buttons.values ().iterator ();
    while (itOpcoes.hasNext ())
      {
	BotaoItemIf radio = (BotaoItemIf) itOpcoes.next ();
	radio.setEstiloFonte (estilo);
      }
  }
  
  public int getEstiloFonte ()
  {
    Font f = getFont ();
    if (f == null)
      return 0;
    return f.getStyle ();
  }
  
  public void setIncrementoTamanhoFonte (int incremento)
  {
    incrementoTamanhoFonte = incremento;
    Font f = new JLabel ().getFont ();
    if (tamanhoOriginal == -1.0F)
      tamanhoOriginal = f.getSize2D ();
    f = f.deriveFont (tamanhoOriginal + (float) incremento);
    setFont (f);
    Iterator itOpcoes = buttons.values ().iterator ();
    while (itOpcoes.hasNext ())
      {
	BotaoItemIf radio = (BotaoItemIf) itOpcoes.next ();
	radio.setIncrementoTamanhoFonte (incremento);
      }
  }
  
  public int getIncrementoTamanhoFonte ()
  {
    return incrementoTamanhoFonte;
  }
}
