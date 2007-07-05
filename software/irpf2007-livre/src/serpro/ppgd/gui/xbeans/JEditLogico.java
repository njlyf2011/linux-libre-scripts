/* JEditLogico - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.util.EventListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import serpro.ppgd.gui.ConstantesGlobaisGUI;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.Logico;
import serpro.ppgd.negocio.OpcaoLogico;

public class JEditLogico extends JEditCampo
{
  private Box box;
  private ButtonGroup group;
  private int orientacaoTexto;
  private static final Color DEFAULT_FOREGROUND = new JRadioButton ().getForeground ();
  private static final Color DISABLED_FOREGROUND = ConstantesGlobaisGUI.COR_CINZA_CLARO;
  private boolean bloqueiaItemListener = false;
  private Map radios;
  private LinkedList listaOrdenada = new LinkedList ();
  
  public JEditLogico ()
  {
    this (new LogicoDefault ());
  }
  
  public JEditLogico (Informacao campo)
  {
    super (campo);
    setOrientacaoTexto (1);
  }
  
  public void setFont (Font pFont)
  {
    Iterator itOpcoes = radios.values ().iterator ();
    while (itOpcoes.hasNext ())
      {
	JRadioButton radio = (JRadioButton) itOpcoes.next ();
	radio.setFont (pFont);
      }
  }
  
  public void setForeground (Color pCor)
  {
    Iterator itOpcoes = radios.values ().iterator ();
    while (itOpcoes.hasNext ())
      {
	JRadioButton radio = (JRadioButton) itOpcoes.next ();
	radio.setForeground (pCor);
      }
  }
  
  public void setFont (String pValOpcao, Color pCor)
  {
    String lblOpcao = ((Logico) getInformacao ()).getLabelOpcao (pValOpcao);
    if (radios.containsKey (lblOpcao))
      {
	JRadioButton radio = (JRadioButton) radios.get (lblOpcao);
	radio.setForeground (pCor);
      }
  }
  
  public void setFont (String pValOpcao, Font pFont)
  {
    String lblOpcao = ((Logico) getInformacao ()).getLabelOpcao (pValOpcao);
    if (radios.containsKey (lblOpcao))
      {
	JRadioButton radio = (JRadioButton) radios.get (lblOpcao);
	radio.setFont (pFont);
      }
  }
  
  public void addListener (String pValOpcao, EventListener listener)
  {
    String lblOpcao = ((Logico) getInformacao ()).getLabelOpcao (pValOpcao);
    if (radios.containsKey (lblOpcao))
      {
	JRadioButton radio = (JRadioButton) radios.get (lblOpcao);
	adicionaListener (radio, listener);
      }
  }
  
  private void adicionaListener (JRadioButton radio, EventListener listener)
  {
    if (listener instanceof ActionListener)
      radio.addActionListener ((ActionListener) listener);
    else if (listener instanceof ItemListener)
      radio.addItemListener ((ItemListener) listener);
    else if (listener instanceof KeyListener)
      radio.addKeyListener ((KeyListener) listener);
    else if (listener instanceof FocusListener)
      radio.addFocusListener ((FocusListener) listener);
    else if (listener instanceof MouseListener)
      radio.addMouseListener ((MouseListener) listener);
    else if (listener instanceof MouseMotionListener)
      radio.addMouseMotionListener ((MouseMotionListener) listener);
  }
  
  public void addListener (EventListener listener)
  {
    Iterator itOpcoes = radios.values ().iterator ();
    while (itOpcoes.hasNext ())
      {
	JRadioButton radio = (JRadioButton) itOpcoes.next ();
	adicionaListener (radio, listener);
      }
  }
  
  public int getOrientacaoTexto ()
  {
    return orientacaoTexto;
  }
  
  public void setOrientacaoTexto (int orientacaoTexto)
  {
    this.orientacaoTexto = orientacaoTexto;
    buildComponente ();
  }
  
  public void setOpcaoHabilitada (String pValOpcao, boolean pOpt)
  {
    String lblOpcao = ((Logico) getInformacao ()).getLabelOpcao (pValOpcao);
    if (radios.containsKey (lblOpcao))
      {
	JRadioButton radio = (JRadioButton) radios.get (lblOpcao);
	radio.setEnabled (pOpt);
	if (! pOpt)
	  radio.setForeground (DISABLED_FOREGROUND);
	else
	  radio.setForeground (DEFAULT_FOREGROUND);
      }
  }
  
  public JComponent getComponenteEditor ()
  {
    return box;
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    setObservadorAtivo (false);
    if ((evt.getPropertyName () == null || ! evt.getPropertyName ().equals ("ReadOnly")) && evt.getNewValue () instanceof String)
      {
	String novoValor = (String) evt.getNewValue ();
	if (novoValor == null)
	  novoValor = "";
	if (novoValor.equals (""))
	  group.setSelected (new JRadioButton ().getModel (), true);
	else
	  {
	    String labelNovoValor = ((Logico) getInformacao ()).getLabelOpcao (novoValor);
	    Iterator it = radios.values ().iterator ();
	    while (it.hasNext ())
	      {
		JRadioButton rb = (JRadioButton) it.next ();
		if (rb.getText ().equals (labelNovoValor))
		  {
		    setBloqueiaItemListener (true);
		    rb.setSelected (true);
		    setBloqueiaItemListener (false);
		  }
		else
		  {
		    setBloqueiaItemListener (true);
		    rb.setSelected (false);
		    setBloqueiaItemListener (false);
		  }
	      }
	  }
      }
    setObservadorAtivo (true);
  }
  
  protected void readOnlyPropertyChange (boolean readOnly)
  {
    Iterator it = radios.values ().iterator ();
    while (it.hasNext ())
      {
	JRadioButton rb = (JRadioButton) it.next ();
	rb.setEnabled (! readOnly);
      }
  }
  
  protected void habilitadoPropertyChange (boolean habilitado)
  {
    Iterator it = radios.values ().iterator ();
    while (it.hasNext ())
      {
	JRadioButton rb = (JRadioButton) it.next ();
	rb.setEnabled (habilitado);
      }
  }
  
  public JComponent getComponenteFoco ()
  {
    if (radios.values ().size () > 0)
      return (JRadioButton) radios.values ().toArray ()[0];
    return null;
  }
  
  public Map getRadios ()
  {
    return radios;
  }
  
  public void setRadios (Map radios)
  {
    this.radios = radios;
  }
  
  public LinkedList getListaRadiosOrdenada ()
  {
    return listaOrdenada;
  }
  
  public boolean isBloqueiaItemListener ()
  {
    return bloqueiaItemListener;
  }
  
  public void setBloqueiaItemListener (boolean bloqueiaItemListener)
  {
    this.bloqueiaItemListener = bloqueiaItemListener;
  }
  
  protected void buildComponente ()
  {
    removeAll ();
    setLayout (new BorderLayout ());
    radios = new Hashtable ();
    listaOrdenada = new LinkedList ();
    box = new Box (getOrientacaoTexto ());
    Logico logico = (Logico) getInformacao ();
    group = new ButtonGroup ();
    if (logico != null)
      {
	Iterator itOpcoes = logico.getListaOrdenada ().iterator ();
	while (itOpcoes.hasNext ())
	  {
	    OpcaoLogico opt = (OpcaoLogico) itOpcoes.next ();
	    JRadioButton rb = new serpro.ppgd.gui.editors.PPGDRadioButton (opt.labelOpcao, opt.selecionado);
	    ((serpro.ppgd.gui.editors.PPGDRadioButton) rb).setInformacao (campo);
	    rb.setVisible (true);
	    radios.put (rb.getText (), rb);
	    listaOrdenada.addLast (rb);
	    rb.addFocusListener (new FocusAdapter ()
	    {
	      public void focusGained (FocusEvent e)
	      {
		/* empty */
	      }
	      
	      public void focusLost (FocusEvent e)
	      {
		if (e.getOppositeComponent () != null)
		  {
		    JEditLogico.this.setIdentificacaoFoco (false);
		    JEditLogico.this.chamaValidacao ();
		  }
	      }
	    });
	    rb.addItemListener (new ItemListener ()
	    {
	      public void itemStateChanged (ItemEvent e)
	      {
		if (e.getStateChange () == 1 && ! isBloqueiaItemListener ())
		  {
		    System.out.println ("id evento->" + e.getID ());
		    JRadioButton r = (JRadioButton) e.getSource ();
		    String labelOpcao = r.getText ().trim ();
		    String valorOpcao = ((Logico) JEditLogico.this.getInformacao ()).getValorOpcao (labelOpcao);
		    ((Logico) JEditLogico.this.getInformacao ()).atualizaListaValidadoresImpeditivos (valorOpcao);
		    if (JEditLogico.this.getInformacao ().getListaValidadoresImpeditivos ().isEmpty () || JEditLogico.this.verificaValidacoesImpeditivas (valorOpcao))
		      ((Logico) JEditLogico.this.getInformacao ()).setConteudo (valorOpcao);
		  }
	      }
	    });
	    if (getOrientacaoTexto () == 0)
	      box.add (new JLabel (" "));
	    box.add (rb);
	    if (! ((Logico) campo).isSelecaoMultipla ())
	      group.add (rb);
	    if (! itOpcoes.hasNext () && getOrientacaoTexto () == 0)
	      box.add (new JLabel (" "));
	  }
	setObservadorAtivo (false);
	PropertyChangeEvent evt = new PropertyChangeEvent (this, null, null, getInformacao ().asString ());
	implementacaoPropertyChange (evt);
	setObservadorAtivo (true);
      }
    add (box, "Center");
    JButtonMensagem btnMsg = getButtonMensagem ();
    if (btnMsg != null)
      add (new PainelBotao (btnMsg), "East");
  }
  
  protected void informacaoModificada ()
  {
    buildComponente ();
  }
  
  public void setEstiloFonte (int estilo)
  {
    JRadioButton radio = null;
    if (! radios.isEmpty ())
      radio = (JRadioButton) radios.values ().toArray ()[0];
    else
      radio = new JRadioButton ();
    Font f = radio.getFont ();
    f = f.deriveFont (estilo);
    setFont (f);
  }
  
  public void setIncrementoTamanhoFonte (int incremento)
  {
    incrementoTamanhoFonte = incremento;
    JRadioButton radio = null;
    if (! radios.isEmpty ())
      radio = (JRadioButton) radios.values ().toArray ()[0];
    else
      radio = new JRadioButton ();
    Font f = radio.getFont ();
    if (tamanhoOriginal == -1.0F)
      tamanhoOriginal = f.getSize2D ();
    f = f.deriveFont (tamanhoOriginal + (float) incremento);
    setFont (f);
  }
  
  public int getIncrementoTamanhoFonte ()
  {
    return incrementoTamanhoFonte;
  }
}
