/* EditOpcao - Decompiled by JODE
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
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

import serpro.ppgd.gui.editors.PPGDCheckBox;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ItemOpcao;
import serpro.ppgd.negocio.Opcao;
import serpro.ppgd.negocio.ValidadorImpeditivoDefault;
import serpro.ppgd.negocio.util.LogPPGD;

public class EditOpcao extends EditCampo
{
  private static Opcao vazio = new Opcao ();
  private Box box;
  private int orientacaoTexto = 0;
  private static final Color DEFAULT_FOREGROUND = new JCheckBox ().getForeground ();
  private static final Color DISABLED_FOREGROUND = ConstantesGlobaisGUI.COR_CINZA_CLARO;
  private boolean bloqueiaItemListener = false;
  private Map opcoes;
  private LinkedList listaOrdenada;
  private BlinkBorder borda;
  
  public EditOpcao ()
  {
    super (vazio);
    listaOrdenada = new LinkedList ();
  }
  
  public EditOpcao (Informacao campo, int tamanho)
  {
    super (campo, tamanho);
    listaOrdenada = new LinkedList ();
  }
  
  public EditOpcao (Informacao campo)
  {
    super (campo);
    listaOrdenada = new LinkedList ();
  }
  
  public void setFont (Font pFont)
  {
    Iterator itOpcoes = opcoes.values ().iterator ();
    while (itOpcoes.hasNext ())
      {
	JCheckBox opcao = (JCheckBox) itOpcoes.next ();
	opcao.setFont (pFont);
      }
  }
  
  public void setForeground (Color pCor)
  {
    Iterator itOpcoes = opcoes.values ().iterator ();
    while (itOpcoes.hasNext ())
      {
	JCheckBox opcao = (JCheckBox) itOpcoes.next ();
	opcao.setForeground (pCor);
      }
  }
  
  public void setFont (String pCodigoOpcao, Color pCor)
  {
    if (opcoes.containsKey (pCodigoOpcao))
      {
	JCheckBox opcao = (JCheckBox) opcoes.get (pCodigoOpcao);
	opcao.setForeground (pCor);
      }
  }
  
  public void setFont (String pCodigoOpcao, Font pFont)
  {
    if (opcoes.containsKey (pCodigoOpcao))
      {
	JCheckBox opcao = (JCheckBox) opcoes.get (pCodigoOpcao);
	opcao.setFont (pFont);
      }
  }
  
  public void addListener (String pCodigoOpcao, EventListener listener)
  {
    if (opcoes.containsKey (pCodigoOpcao))
      {
	JCheckBox opcao = (JCheckBox) opcoes.get (pCodigoOpcao);
	adicionaListener (opcao, listener);
      }
  }
  
  private void adicionaListener (JCheckBox opcao, EventListener listener)
  {
    if (listener instanceof ActionListener)
      opcao.addActionListener ((ActionListener) listener);
    else if (listener instanceof ItemListener)
      opcao.addItemListener ((ItemListener) listener);
    else if (listener instanceof KeyListener)
      opcao.addKeyListener ((KeyListener) listener);
    else if (listener instanceof FocusListener)
      opcao.addFocusListener ((FocusListener) listener);
    else if (listener instanceof MouseListener)
      opcao.addMouseListener ((MouseListener) listener);
    else if (listener instanceof MouseMotionListener)
      opcao.addMouseMotionListener ((MouseMotionListener) listener);
  }
  
  public void addListener (EventListener listener)
  {
    Iterator itOpcoes = opcoes.values ().iterator ();
    while (itOpcoes.hasNext ())
      {
	JCheckBox opcao = (JCheckBox) itOpcoes.next ();
	adicionaListener (opcao, listener);
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
  
  public void setOpcaoHabilitada (String pCodigoOpcao, boolean pOpt)
  {
    if (opcoes.containsKey (pCodigoOpcao))
      {
	JCheckBox opcao = (JCheckBox) opcoes.get (pCodigoOpcao);
	opcao.setEnabled (pOpt);
	if (! pOpt)
	  opcao.setForeground (DISABLED_FOREGROUND);
	else
	  opcao.setForeground (DEFAULT_FOREGROUND);
      }
  }
  
  public void setInformacao (Informacao campo)
  {
    opcoes = new Hashtable ();
    listaOrdenada = new LinkedList ();
    if (box == null)
      box = new Box (getOrientacaoTexto ());
    else
      box.removeAll ();
    Opcao opcao = (Opcao) campo;
    Iterator itOpcoes = opcao.getListaOrdenada ().iterator ();
    while (itOpcoes.hasNext ())
      {
	ItemOpcao opt = (ItemOpcao) itOpcoes.next ();
	JCheckBox cb = new PPGDCheckBox (opt.getCodigo (), opt.getDescricao (), opt.isSelecionado ());
	((PPGDCheckBox) cb).setInformacao (campo);
	cb.setVisible (true);
	cb.setSelected (opt.isSelecionado ());
	opcoes.put (cb.getName (), cb);
	listaOrdenada.addLast (cb);
	cb.addFocusListener (new FocusAdapter ()
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
		EditOpcao.this.chamaValidacao ();
	      }
	  }
	});
	cb.addItemListener (new ItemListener ()
	{
	  public void itemStateChanged (ItemEvent e)
	  {
	    JCheckBox opcao_5_ = (JCheckBox) e.getSource ();
	    String codigoOpcao = opcao_5_.getName ().trim ();
	    ((Opcao) EditOpcao.this.getInformacao ()).atualizaListaValidadoresImpeditivos (codigoOpcao);
	    if (EditOpcao.this.getInformacao ().getListaValidadoresImpeditivos ().isEmpty () || EditOpcao.this.verificaValidacoesImpeditivas (codigoOpcao))
	      {
		if (e.getStateChange () == 1 && ! isBloqueiaItemListener ())
		  ((Opcao) EditOpcao.this.getInformacao ()).addSelectedItem (codigoOpcao);
		if (e.getStateChange () == 2 && ! isBloqueiaItemListener ())
		  ((Opcao) EditOpcao.this.getInformacao ()).delSelectedItem (codigoOpcao);
		EditOpcao.this.chamaValidacao ();
	      }
	  }
	});
	if (getOrientacaoTexto () == 0)
	  box.add (new JLabel (" "));
	box.add (cb);
      }
    setObservadorAtivo (false);
    PropertyChangeEvent evt = new PropertyChangeEvent (this, null, null, getInformacao ().asString ());
    implementacaoPropertyChange (evt);
    setObservadorAtivo (true);
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
      LogPPGD.debug ("Evento nulo no metodo implementacaoPropertyChange da classe " + this.getClass ());
    if ((evt.getPropertyName () == null || ! evt.getPropertyName ().equals ("ReadOnly")) && evt.getNewValue () instanceof String)
      {
	String novoValor = (String) evt.getNewValue ();
	if (novoValor == null)
	  novoValor = "";
	String descNovoValor = ((Opcao) getInformacao ()).getDescricaoOpcoes ();
	Iterator it = opcoes.values ().iterator ();
	while (it.hasNext ())
	  {
	    JCheckBox cb = (JCheckBox) it.next ();
	    if (descNovoValor.indexOf (cb.getText ().trim ()) > -1)
	      {
		setBloqueiaItemListener (true);
		cb.setSelected (true);
		setBloqueiaItemListener (false);
	      }
	    else
	      {
		setBloqueiaItemListener (true);
		cb.setSelected (false);
		setBloqueiaItemListener (false);
	      }
	  }
      }
    setObservadorAtivo (true);
  }
  
  protected void readOnlyPropertyChange (boolean readOnly)
  {
    Iterator it = opcoes.values ().iterator ();
    while (it.hasNext ())
      {
	JCheckBox cb = (JCheckBox) it.next ();
	cb.setEnabled (! readOnly);
      }
  }
  
  protected void habilitadoPropertyChange (boolean habilitado)
  {
    Iterator it = opcoes.values ().iterator ();
    while (it.hasNext ())
      {
	JCheckBox cb = (JCheckBox) it.next ();
	cb.setEnabled (habilitado);
	cb.setForeground (ConstantesGlobaisGUI.COR_CINZA_CLARO);
      }
    labelCampo.setEnabled (habilitado);
  }
  
  public JComponent getComponenteFoco ()
  {
    int qtdOpcoes = opcoes.values ().size ();
    int posicaoPrimeiroAtivo = 0;
    if (qtdOpcoes > 0)
      {
	for (int i = 0; i < qtdOpcoes; i++)
	  {
	    if (((JCheckBox) opcoes.values ().toArray ()[i]).isEnabled ())
	      {
		posicaoPrimeiroAtivo = i;
		break;
	      }
	  }
	return (JCheckBox) opcoes.values ().toArray ()[posicaoPrimeiroAtivo];
      }
    return null;
  }
  
  public Map getOpcoes ()
  {
    return opcoes;
  }
  
  public void setOpcoes (Map opcoes)
  {
    this.opcoes = opcoes;
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
