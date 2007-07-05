/* EditLogico - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
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
import javax.swing.SwingUtilities;

import serpro.ppgd.gui.editors.PPGDRadioButton;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.Logico;
import serpro.ppgd.negocio.OpcaoLogico;
import serpro.ppgd.negocio.ValidadorImpeditivoDefault;

public class EditLogico extends EditCampo
{
  private static Logico vazio = new Logico ();
  private Box box;
  private ButtonGroup group;
  private int orientacaoTexto = 0;
  private static final Color DEFAULT_FOREGROUND = new JRadioButton ().getForeground ();
  private static final Color DISABLED_FOREGROUND = ConstantesGlobaisGUI.COR_CINZA_CLARO;
  private boolean bloqueiaItemListener = false;
  private Map radios;
  private LinkedList listaOrdenada;
  private BlinkBorder borda;
  
  public EditLogico ()
  {
    super (vazio);
    listaOrdenada = new LinkedList ();
  }
  
  public EditLogico (Informacao campo, int tamanho)
  {
    super (campo, tamanho);
    listaOrdenada = new LinkedList ();
  }
  
  public EditLogico (Informacao campo, int tamanho, int orientacao)
  {
    super (campo, tamanho);
    listaOrdenada = new LinkedList ();
    setOrientacaoTexto (orientacao);
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
  
  public void addValidador (ValidadorImpeditivoDefault validador, String valOpcao)
  {
    /* empty */
  }
  
  public int getOrientacaoTexto ()
  {
    return orientacaoTexto;
  }
  
  public void setOrientacaoTexto (int orientacaoTexto)
  {
    this.orientacaoTexto = orientacaoTexto;
    box = new Box (orientacaoTexto);
    setInformacao (getInformacao ());
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
  
  public void setInformacao (Informacao campo)
  {
    rebuildComponente (campo);
    setObservadorAtivo (false);
    PropertyChangeEvent evt = new PropertyChangeEvent (this, null, null, getInformacao ().asString ());
    implementacaoPropertyChange (evt);
    setObservadorAtivo (true);
  }
  
  private void rebuildComponente (Informacao campo)
  {
    radios = new Hashtable ();
    listaOrdenada = new LinkedList ();
    if (box == null)
      box = new Box (getOrientacaoTexto ());
    else
      box.removeAll ();
    Logico logico = (Logico) campo;
    group = new ButtonGroup ();
    Iterator itOpcoes = logico.getListaOrdenada ().iterator ();
    while (itOpcoes.hasNext ())
      {
	OpcaoLogico opt = (OpcaoLogico) itOpcoes.next ();
	JRadioButton rb = new PPGDRadioButton (opt.labelOpcao, opt.selecionado);
	((PPGDRadioButton) rb).setInformacao (campo);
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
		setIdentificacaoFoco (false);
		EditLogico.this.chamaValidacao ();
	      }
	  }
	});
	rb.addItemListener (new ItemListener ()
	{
	  public void itemStateChanged (ItemEvent e)
	  {
	    if (e.getStateChange () == 1 && ! isBloqueiaItemListener ())
	      {
		JRadioButton r = (JRadioButton) e.getSource ();
		String labelOpcao = r.getText ().trim ();
		String valorOpcao = ((Logico) EditLogico.this.getInformacao ()).getValorOpcao (labelOpcao);
		((Logico) EditLogico.this.getInformacao ()).atualizaListaValidadoresImpeditivos (valorOpcao);
		if (EditLogico.this.getInformacao ().getListaValidadoresImpeditivos ().isEmpty () || EditLogico.this.verificaValidacoesImpeditivas (valorOpcao))
		  {
		    ((Logico) EditLogico.this.getInformacao ()).setConteudo (valorOpcao);
		    EditLogico.this.chamaValidacao ();
		  }
	      }
	  }
	});
	if (getOrientacaoTexto () == 0)
	  box.add (new JLabel (" "));
	box.add (rb);
	if (! ((Logico) campo).isSelecaoMultipla ())
	  group.add (rb);
      }
    box.validate ();
  }
  
  public JComponent getComponenteEditor ()
  {
    return box;
  }
  
  public void setIdentificacaoFoco (boolean status)
  {
    if (getComponenteEditor ().getBorder () != null)
      {
	if (status)
	  {
	    borda = new BlinkBorder (getComponenteEditor ());
	    borda.start ();
	  }
	else if (borda != null)
	  {
	    borda.parar ();
	    borda = null;
	  }
      }
    else
      super.setIdentificacaoFoco (status);
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    setObservadorAtivo (false);
    if (evt == null)
      System.out.println ("Evento nulo no metodo implementacaoPropertyChange da classe " + this.getClass ());
    if ((evt.getPropertyName () == null || ! evt.getPropertyName ().equals ("ReadOnly")) && evt.getNewValue () instanceof String)
      {
	String novoValor = (String) evt.getNewValue ();
	if (novoValor == null)
	  novoValor = "";
	if (novoValor.equals (""))
	  rebuildComponente (getInformacao ());
	else
	  {
	    String labelNovoValor = ((Logico) getInformacao ()).getLabelOpcao (novoValor);
	    Iterator it = radios.values ().iterator ();
	    while (it.hasNext ())
	      {
		final JRadioButton rb = (JRadioButton) it.next ();
		javax.swing.ButtonModel model = rb.getModel ();
		if (rb.getText ().equals (labelNovoValor))
		  {
		    setBloqueiaItemListener (true);
		    rb.setSelected (true);
		    SwingUtilities.invokeLater (new Runnable ()
		    {
		      public void run ()
		      {
			rb.requestFocusInWindow ();
		      }
		    });
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
	rb.setForeground (ConstantesGlobaisGUI.COR_CINZA_CLARO);
      }
    labelCampo.setEnabled (habilitado);
  }
  
  public JComponent getComponenteFoco ()
  {
    int qtdRadios = radios.values ().size ();
    int posicaoPrimeiroAtivo = 0;
    if (qtdRadios > 0)
      {
	for (int i = 0; i < qtdRadios; i++)
	  {
	    if (((JRadioButton) radios.values ().toArray ()[i]).isEnabled ())
	      {
		posicaoPrimeiroAtivo = i;
		break;
	      }
	  }
	return (JRadioButton) radios.values ().toArray ()[posicaoPrimeiroAtivo];
      }
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
  
  public void setPerdeFocoComEnter (boolean isPerdeFocoComEnter)
  {
    /* empty */
  }
}
