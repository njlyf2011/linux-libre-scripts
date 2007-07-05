/* BarraNavegacaoPPGD - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import serpro.ppgd.gui.ButtonPPGD;
import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.ObjetoNegocio;

public class BarraNavegacaoPPGD extends PPGDFormPanel
{
  private final Icon ICON_ADICIONAR = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("imagens/nav_adicionar.gif"));
  private final Icon ICON_ADICIONAR_SEL = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("imagens/nav_adicionarselecionado.gif"));
  private final Icon ICON_EXCLUIR = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("imagens/nav_excluir.gif"));
  private final Icon ICON_EXCLUIR_SEL = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("imagens/nav_excluirselecionado.gif"));
  private final Icon ICON_PRIMEIRO = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("imagens/nav_primeiro.gif"));
  private final Icon ICON_PRIMEIRO_SEL = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("imagens/nav_primeiroselecionado.gif"));
  private final Icon ICON_ANTERIOR = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("imagens/nav_anterior.gif"));
  private final Icon ICON_ANTERIOR_SEL = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("imagens/nav_anteriorselecionado.gif"));
  private final Icon ICON_PROXIMO = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("imagens/nav_proximo.gif"));
  private final Icon ICON_PROXIMO_SEL = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("imagens/nav_proximoselecionado.gif"));
  private final Icon ICON_ULTIMO = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("imagens/nav_ultimo.gif"));
  private final Icon ICON_ULTIMO_SEL = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("imagens/nav_ultimoselecionado.gif"));
  private final String LABEL_ADICIONAR = "adicionar";
  private final String LABEL_EXCLUIR = "excluir";
  private final String LABEL_PRIMEIRO = "primeiro";
  private final String LABEL_ANTERIOR = "anterior";
  private final String LABEL_PROXIMO = "pr\u00f3ximo";
  private final String LABEL_ULTIMO = "\u00faltimo";
  private ButtonPPGD adicionar;
  private ButtonPPGD excluir;
  private ButtonPPGD primeiro;
  private ButtonPPGD anterior;
  private ButtonPPGD proximo;
  private ButtonPPGD ultimo;
  private List lista;
  private ObjetoNegocio obj;
  private FichaColecaoIf ficha;
  private int posAtual = 1;
  
  public BarraNavegacaoPPGD (Colecao colecao, FichaColecaoIf ficha)
  {
    lista = colecao.recuperarLista ();
    obj = null;
    if (! lista.isEmpty ())
      obj = (ObjetoNegocio) lista.get (0);
    this.ficha = ficha;
    adicionar = new ButtonPPGD ("adicionar", ICON_ADICIONAR, ICON_ADICIONAR, ICON_ADICIONAR);
    adicionar.setaDimensoes ();
    excluir = new ButtonPPGD ("excluir", ICON_EXCLUIR, ICON_EXCLUIR, ICON_EXCLUIR);
    excluir.setaDimensoes ();
    primeiro = new ButtonPPGD ("primeiro", ICON_PRIMEIRO, ICON_PRIMEIRO, ICON_PRIMEIRO);
    primeiro.setaDimensoes ();
    anterior = new ButtonPPGD ("anterior", ICON_ANTERIOR, ICON_ANTERIOR, ICON_ANTERIOR);
    anterior.setaDimensoes ();
    proximo = new ButtonPPGD ("pr\u00f3ximo", ICON_PROXIMO, ICON_PROXIMO, ICON_PROXIMO);
    proximo.setaDimensoes ();
    ultimo = new ButtonPPGD ("\u00faltimo", ICON_ULTIMO, ICON_ULTIMO, ICON_ULTIMO);
    ultimo.setaDimensoes ();
    adicionar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	executaAdicionar ();
      }
    });
    excluir.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	executaExcluir ();
      }
    });
    primeiro.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	executaPrimeiro ();
      }
    });
    anterior.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	executaAnterior ();
      }
    });
    proximo.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	executaProximo ();
      }
    });
    ultimo.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	executaUltimo ();
      }
    });
    FormLayout layout = new FormLayout ("p:grow,p,5dlu,p,5dlu,p,5dlu,p,5dlu,p,5dlu,p,p:grow", "c:30dlu");
    layout.setColumnGroups (new int[][] { { 2, 4, 6, 8, 10, 12 } });
    setLayout (layout);
    configuraLayout ();
    CellConstraints cc = new CellConstraints ();
    add (adicionar, cc.xy (2, 1));
    add (excluir, cc.xy (4, 1));
    add (primeiro, cc.xy (6, 1));
    add (anterior, cc.xy (8, 1));
    add (proximo, cc.xy (10, 1));
    add (ultimo, cc.xy (12, 1));
  }
  
  protected void configuraLayout ()
  {
    /* empty */
  }
  
  protected boolean permiteCriacao ()
  {
    return true;
  }
  
  public void executaAdicionar ()
  {
    if (permiteCriacao ())
      {
	obj = ficha.criaObjetoNegocio ();
	lista.add (obj);
	posAtual++;
	ficha.mostraOutroObjetoNegocio (obj);
	excluir.setEnabled (true);
      }
  }
  
  public void executaExcluir ()
  {
    boolean confirmacao = ficha.obtemConfirmacaoExclusao (obj);
    if (confirmacao)
      {
	int size = lista.size ();
	int pos = lista.indexOf (obj);
	switch (size)
	  {
	  case 0:
	    excluir.setEnabled (false);
	    obj = null;
	    break;
	  case 1:
	    lista.remove (obj);
	    posAtual--;
	    executaAdicionar ();
	    excluir.setEnabled (false);
	    break;
	  default:
	    if (pos == size - 1)
	      {
		obj = (ObjetoNegocio) lista.get (size - 2);
		lista.remove (size - 1);
		posAtual--;
	      }
	    else
	      {
		lista.remove (pos);
		obj = (ObjetoNegocio) lista.get (pos);
	      }
	    ficha.mostraOutroObjetoNegocio (obj);
	  }
      }
  }
  
  public void executaPrimeiro ()
  {
    obj = (ObjetoNegocio) lista.get (0);
    posAtual = 1;
    ficha.mostraOutroObjetoNegocio (obj);
  }
  
  public void executaAnterior ()
  {
    if (lista.indexOf (obj) == 0)
      executaPrimeiro ();
    else
      {
	obj = (ObjetoNegocio) lista.get (lista.indexOf (obj) - 1);
	posAtual--;
	ficha.mostraOutroObjetoNegocio (obj);
      }
  }
  
  public void executaProximo ()
  {
    if (lista.indexOf (obj) == lista.size () - 1)
      executaUltimo ();
    else
      {
	obj = (ObjetoNegocio) lista.get (lista.indexOf (obj) + 1);
	posAtual++;
	ficha.mostraOutroObjetoNegocio (obj);
      }
  }
  
  public void executaUltimo ()
  {
    if (lista.size () > 0)
      {
	obj = (ObjetoNegocio) lista.get (lista.size () - 1);
	posAtual = lista.size ();
	ficha.mostraOutroObjetoNegocio (obj);
      }
  }
  
  public void setColecao (Colecao Colecao)
  {
    lista = Colecao.recuperarLista ();
    obj = null;
    if (! lista.isEmpty ())
      obj = (ObjetoNegocio) lista.get (0);
  }
  
  public int getPosicaoAtual ()
  {
    return posAtual;
  }
}
