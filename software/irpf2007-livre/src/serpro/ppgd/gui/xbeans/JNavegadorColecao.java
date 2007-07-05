/* JNavegadorColecao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.event.EventListenerList;

import serpro.ppgd.infraestrutura.util.NavegadorColecaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoIf;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoListener;
import serpro.ppgd.infraestrutura.util.NavegadorRemocaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorRemocaoListener;
import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.PPGDFacade;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.negocio.util.UtilitariosArquivo;

public class JNavegadorColecao extends JEditCampo implements NavegadorColecaoIf
{
  private JButton btnPrimeiro;
  private JButton btnUltimo;
  private JButton btnAnterior;
  private JButton btnProximo;
  private JButton btnRemover;
  private JButton btnAdicionar;
  protected Icon iconePrimeiro = null;
  protected Icon iconeProximo = null;
  protected Icon iconeAnterior = null;
  protected Icon iconeRemover = null;
  protected Icon iconeAdicionar = null;
  protected Icon iconeUltimo = null;
  public static final int MODO_COMPLETO = 0;
  public static final int MODO_SOMENTE_NAVEGACAO = 1;
  private int modoNavegador = 0;
  private JToolBar toolbar;
  private Colecao colecao;
  private int indexAtual = -1;
  private EventListenerList listeners = new EventListenerList ();
  private EventListenerList remocaoListeners = new EventListenerList ();
  private Observador observadorColecao = new Observador ()
  {
    public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
    {
      if (getColecao ().recuperarLista ().size () == 0)
	{
	  NavegadorColecaoEvent evt = new NavegadorColecaoEvent (this);
	  evt.setObjetoNegocio (null);
	  exibe (-1);
	}
    }
  };
  
  protected void buildComponente ()
  {
    removeAll ();
    setLayout (new BorderLayout ());
    if (btnPrimeiro == null)
      {
	btnPrimeiro = new JButton ();
	btnAnterior = new JButton ();
	btnProximo = new JButton ();
	btnUltimo = new JButton ();
	btnRemover = new JButton ();
	btnAdicionar = new JButton ();
	btnPrimeiro.addActionListener (new ActionListener ()
	{
	  public void actionPerformed (ActionEvent e)
	  {
	    if (getColecao () != null)
	      primeiro ();
	  }
	});
	btnUltimo.addActionListener (new ActionListener ()
	{
	  public void actionPerformed (ActionEvent e)
	  {
	    if (getColecao () != null)
	      ultimo ();
	  }
	});
	btnAnterior.addActionListener (new ActionListener ()
	{
	  public void actionPerformed (ActionEvent e)
	  {
	    if (getColecao () != null)
	      anterior ();
	  }
	});
	btnProximo.addActionListener (new ActionListener ()
	{
	  public void actionPerformed (ActionEvent e)
	  {
	    if (getColecao () != null)
	      proximo ();
	  }
	});
	btnRemover.addActionListener (new ActionListener ()
	{
	  public void actionPerformed (ActionEvent e)
	  {
	    if (getColecao () != null)
	      remove ();
	  }
	});
	btnAdicionar.addActionListener (new ActionListener ()
	{
	  public void actionPerformed (ActionEvent e)
	  {
	    if (getColecao () != null)
	      adiciona ();
	  }
	});
      }
    btnPrimeiro.setIcon (getIconePrimeiro ());
    btnAnterior.setIcon (getIconeAnterior ());
    btnProximo.setIcon (getIconeProximo ());
    btnUltimo.setIcon (getIconeUltimo ());
    btnRemover.setIcon (getIconeRemover ());
    btnAdicionar.setIcon (getIconeAdicionar ());
    toolbar = new JToolBar ();
    if (getModoNavegador () == 0)
      {
	toolbar.add (btnPrimeiro);
	toolbar.add (btnAnterior);
	toolbar.add (btnProximo);
	toolbar.add (btnUltimo);
	toolbar.add (btnAdicionar);
	toolbar.add (btnRemover);
      }
    else if (getModoNavegador () == 1)
      {
	toolbar.add (btnPrimeiro);
	toolbar.add (btnAnterior);
	toolbar.add (btnProximo);
	toolbar.add (btnUltimo);
      }
    add (getComponenteEditor (), "Center");
    setPreferredSize (getMinimumSize ());
    validate ();
    repaint ();
  }
  
  protected void informacaoModificada ()
  {
    /* empty */
  }
  
  protected void readOnlyPropertyChange (boolean readOnly)
  {
    /* empty */
  }
  
  protected void habilitadoPropertyChange (boolean habilitado)
  {
    /* empty */
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    /* empty */
  }
  
  public JComponent getComponenteEditor ()
  {
    return toolbar;
  }
  
  public JComponent getComponenteFoco ()
  {
    return toolbar;
  }
  
  protected void associaInformacao (String aInfo)
  {
    try
      {
	String classeFacade = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.classes.facade", "serpro.ppgd.repositorio.FacadeDefault");
	Class classe = Class.forName (classeFacade);
	Method methGetInstancia = classe.getMethod ("getInstancia", null);
	PPGDFacade facade = (PPGDFacade) methGetInstancia.invoke (null, null);
	Object objInfo = facade;
	StringTokenizer tokens = new StringTokenizer (aInfo, ".");
	while (tokens.hasMoreTokens ())
	  {
	    Class clazz = objInfo.getClass ();
	    String nomeMetodo = tokens.nextToken ();
	    nomeMetodo = nomeMetodo.substring (0, 1).toUpperCase () + nomeMetodo.substring (1, nomeMetodo.length ());
	    Method mtd = clazz.getMethod ("get" + nomeMetodo, null);
	    objInfo = mtd.invoke (objInfo, null);
	    if (objInfo == null)
	      break;
	  }
	if (objInfo != null && objInfo instanceof Colecao)
	  setColecao ((Colecao) objInfo);
	else
	  LogPPGD.erro ("N\u00e3o foi poss\u00edvel encontrar '" + aInfo + "'. Este atributo existe mesmo?");
      }
    catch (Exception e)
      {
	LogPPGD.erro ("N\u00e3o foi poss\u00edvel encontrar '" + aInfo + "'. Este atributo existe mesmo?");
	e.printStackTrace ();
      }
  }
  
  public void setColecao (Colecao objInfo)
  {
    colecao = objInfo;
    colecao.addObservador (observadorColecao);
    primeiro ();
  }
  
  public void adiciona ()
  {
    indexAtual = colecao.novoObjeto ();
    exibe (indexAtual);
  }
  
  public void remove ()
  {
    if (indexAtual > -1)
      {
	NavegadorRemocaoEvent evt = new NavegadorRemocaoEvent (this);
	evt.setObjetoNegocio ((ObjetoNegocio) colecao.recuperarLista ().get (indexAtual));
	if (fireConfirmaExclusao (evt))
	  {
	    ObjetoNegocio obj = (ObjetoNegocio) colecao.recuperarLista ().remove (indexAtual);
	    fireObjetoExcluido (evt);
	    obj.clear ();
	    indexAtual--;
	  }
      }
    if (indexAtual == -1 && colecao.recuperarLista ().size () > 0)
      indexAtual = 0;
    exibe (indexAtual);
  }
  
  public void proximo ()
  {
    if (indexAtual + 1 < getColecao ().recuperarLista ().size ())
      indexAtual++;
    exibe (indexAtual);
  }
  
  public void anterior ()
  {
    if (indexAtual > 0)
      indexAtual--;
    exibe (indexAtual);
  }
  
  public void primeiro ()
  {
    indexAtual = 0;
    exibe (indexAtual);
  }
  
  public void ultimo ()
  {
    indexAtual = getColecao ().recuperarLista ().size () - 1;
    exibe (indexAtual);
  }
  
  public void exibe (int indice)
  {
    btnPrimeiro.setEnabled (true);
    btnAnterior.setEnabled (true);
    btnProximo.setEnabled (true);
    btnUltimo.setEnabled (true);
    btnRemover.setEnabled (true);
    int tamColecao = getColecao ().recuperarLista ().size ();
    if (tamColecao > 0 && indice < tamColecao && indice >= 0)
      indexAtual = indice;
    if (tamColecao == 0 || tamColecao == 1)
      {
	btnPrimeiro.setEnabled (false);
	btnAnterior.setEnabled (false);
	btnProximo.setEnabled (false);
	btnUltimo.setEnabled (false);
	btnRemover.setEnabled (tamColecao == 1);
      }
    else if (indexAtual == 0)
      {
	btnPrimeiro.setEnabled (false);
	btnAnterior.setEnabled (false);
	btnProximo.setEnabled (true);
	btnUltimo.setEnabled (true);
      }
    else if (indexAtual == tamColecao - 1)
      {
	btnPrimeiro.setEnabled (true);
	btnAnterior.setEnabled (true);
	btnProximo.setEnabled (false);
	btnUltimo.setEnabled (false);
      }
    else
      {
	btnPrimeiro.setEnabled (true);
	btnAnterior.setEnabled (true);
	btnProximo.setEnabled (true);
	btnUltimo.setEnabled (true);
      }
    NavegadorColecaoEvent evt = new NavegadorColecaoEvent (this);
    evt.setObjetoNegocio (getItem ());
    if (tamColecao == 0)
      fireExibeColecaoVazia (evt);
    else
      fireExibeOutro (evt);
  }
  
  private void fireExibeOutro (NavegadorColecaoEvent evt)
  {
    EventListenerList eventlistenerlist = listeners;
    Class var_class = serpro.ppgd.infraestrutura.util.NavegadorColecaoListener.class;
    NavegadorColecaoListener[] navListeners = (NavegadorColecaoListener[]) eventlistenerlist.getListeners (var_class);
    for (int i = 0; i < navListeners.length; i++)
      navListeners[i].exibeOutro (evt);
  }
  
  private void fireExibeColecaoVazia (NavegadorColecaoEvent evt)
  {
    EventListenerList eventlistenerlist = listeners;
    Class var_class = serpro.ppgd.infraestrutura.util.NavegadorColecaoListener.class;
    NavegadorColecaoListener[] navListeners = (NavegadorColecaoListener[]) eventlistenerlist.getListeners (var_class);
    for (int i = 0; i < navListeners.length; i++)
      navListeners[i].exibeColecaoVazia (evt);
  }
  
  private boolean fireConfirmaExclusao (NavegadorRemocaoEvent evt)
  {
    EventListenerList eventlistenerlist = remocaoListeners;
    Class var_class = serpro.ppgd.infraestrutura.util.NavegadorRemocaoListener.class;
    NavegadorRemocaoListener[] navListeners = (NavegadorRemocaoListener[]) eventlistenerlist.getListeners (var_class);
    for (int i = 0; i < navListeners.length; i++)
      {
	if (! navListeners[i].confirmaExclusao (evt))
	  return false;
      }
    return true;
  }
  
  private void fireObjetoExcluido (NavegadorRemocaoEvent evt)
  {
    EventListenerList eventlistenerlist = remocaoListeners;
    Class var_class = serpro.ppgd.infraestrutura.util.NavegadorRemocaoListener.class;
    NavegadorRemocaoListener[] navListeners = (NavegadorRemocaoListener[]) eventlistenerlist.getListeners (var_class);
    for (int i = 0; i < navListeners.length; i++)
      navListeners[i].objetoExcluido (evt);
  }
  
  public int getIndiceAtual ()
  {
    return indexAtual;
  }
  
  public ObjetoNegocio getItem ()
  {
    if (getColecao () != null && indexAtual > -1 && indexAtual < getColecao ().recuperarLista ().size ())
      return (ObjetoNegocio) getColecao ().recuperarLista ().get (indexAtual);
    return null;
  }
  
  public Colecao getColecao ()
  {
    return colecao;
  }
  
  public void addNavegadorColecaoListener (NavegadorColecaoListener listener)
  {
    EventListenerList eventlistenerlist = listeners;
    Class var_class = serpro.ppgd.infraestrutura.util.NavegadorColecaoListener.class;
    eventlistenerlist.add (var_class, listener);
  }
  
  public void removeNavegadorColecaoListener (NavegadorColecaoListener listener)
  {
    EventListenerList eventlistenerlist = listeners;
    Class var_class = serpro.ppgd.infraestrutura.util.NavegadorColecaoListener.class;
    eventlistenerlist.remove (var_class, listener);
  }
  
  public void addNavegadorRemocaoListener (NavegadorRemocaoListener listener)
  {
    EventListenerList eventlistenerlist = remocaoListeners;
    Class var_class = serpro.ppgd.infraestrutura.util.NavegadorRemocaoListener.class;
    eventlistenerlist.add (var_class, listener);
  }
  
  public void removeNavegadorRemocaoListener (NavegadorRemocaoListener listener)
  {
    EventListenerList eventlistenerlist = remocaoListeners;
    Class var_class = serpro.ppgd.infraestrutura.util.NavegadorRemocaoListener.class;
    eventlistenerlist.remove (var_class, listener);
  }
  
  public void setEstiloFonte (int estilo)
  {
    /* empty */
  }
  
  public void setIncrementoTamanhoFonte (int incremento)
  {
    /* empty */
  }
  
  public int getIncrementoTamanhoFonte ()
  {
    return 0;
  }
  
  public Icon getIconePrimeiro ()
  {
    if (iconePrimeiro == null)
      iconePrimeiro = new ImageIcon (UtilitariosArquivo.localizaArquivoEmClasspath ("/icones/nav-primeiro.png"));
    return iconePrimeiro;
  }
  
  public void setIconePrimeiro (Icon icon)
  {
    iconePrimeiro = icon;
    btnPrimeiro.setIcon (getIconePrimeiro ());
  }
  
  public Icon getIconeAnterior ()
  {
    if (iconeAnterior == null)
      iconeAnterior = new ImageIcon (UtilitariosArquivo.localizaArquivoEmClasspath ("/icones/nav-anterior.png"));
    return iconeAnterior;
  }
  
  public void setIconeAnterior (Icon icon)
  {
    iconeAnterior = icon;
    btnAnterior.setIcon (getIconeAnterior ());
  }
  
  public Icon getIconeProximo ()
  {
    if (iconeProximo == null)
      iconeProximo = new ImageIcon (UtilitariosArquivo.localizaArquivoEmClasspath ("/icones/nav-proximo.png"));
    return iconeProximo;
  }
  
  public void setIconeProximo (Icon icon)
  {
    iconeProximo = icon;
    btnProximo.setIcon (getIconeProximo ());
  }
  
  public Icon getIconeUltimo ()
  {
    if (iconeUltimo == null)
      iconeUltimo = new ImageIcon (UtilitariosArquivo.localizaArquivoEmClasspath ("/icones/nav-ultimo.png"));
    return iconeUltimo;
  }
  
  public void setIconeUltimo (Icon icon)
  {
    iconeUltimo = icon;
    btnUltimo.setIcon (getIconeUltimo ());
  }
  
  public Icon getIconeAdicionar ()
  {
    if (iconeAdicionar == null)
      iconeAdicionar = new ImageIcon (UtilitariosArquivo.localizaArquivoEmClasspath ("/icones/nav-adiciona.png"));
    return iconeAdicionar;
  }
  
  public void setIconeAdicionar (Icon icon)
  {
    iconeAdicionar = icon;
    btnAdicionar.setIcon (getIconeAdicionar ());
  }
  
  public Icon getIconeRemover ()
  {
    if (iconeRemover == null)
      iconeRemover = new ImageIcon (UtilitariosArquivo.localizaArquivoEmClasspath ("/icones/nav-remove.png"));
    return iconeRemover;
  }
  
  public void setIconeRemover (Icon icon)
  {
    iconeRemover = icon;
    btnRemover.setIcon (getIconeRemover ());
  }
  
  public static void main (String[] args)
  {
    new JNavegadorColecao ();
  }
  
  public void setModoNavegador (int modoNavegador)
  {
    this.modoNavegador = modoNavegador;
    buildComponente ();
  }
  
  public int getModoNavegador ()
  {
    return modoNavegador;
  }
}
