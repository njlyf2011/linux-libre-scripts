/* PropertyEditorInfoAssociada - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans.customizers;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.Customizer;
import java.beans.PropertyEditor;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.gui.xbeans.JEditCampo;
import serpro.ppgd.negocio.util.FabricaUtilitarios;

public class PropertyEditorInfoAssociada extends JPanel implements Customizer, PropertyEditor
{
  private JLabel jLabel1;
  private JPanel pnlInfoAssociada;
  
  private class NavegadorAtributos extends JPanel implements ListSelectionListener
  {
    private JScrollListAtributos jlistRaiz;
    private String selecao;
    private Class classeFacade;
    private JEditCampo bean;
    private PropertyEditorInfoAssociada customizer;
    
    public NavegadorAtributos (JEditCampo aBean, PropertyEditorInfoAssociada aCustomizer)
    {
      bean = aBean;
      customizer = aCustomizer;
      BoxLayout layout = new BoxLayout (this, 0);
      setLayout (layout);
      init ();
    }
    
    private void mostraInformacaoAssociada (String informacaoAssociada)
    {
      StringTokenizer tokens = new StringTokenizer (informacaoAssociada, ".");
      JScrollListAtributos jlist = jlistRaiz;
      while (tokens.hasMoreTokens ())
	{
	  String tk = tokens.nextToken ();
	  Field field = FabricaUtilitarios.getFieldPorNome (classeFacade, tk);
	  avancaPara (jlist, field.getType ());
	  jlist = jlist.getProximo ();
	}
    }
    
    private void init ()
    {
      try
	{
	  classeFacade = Class.forName (FabricaUtilitarios.getProperties ().getProperty ("aplicacao.classes.facade", "serpro.ppgd.repositorio.FacadeDefault"));
	  Vector col = new Vector ();
	  col.addAll (constroiModel (FabricaUtilitarios.getFieldsCamposObjetoNegocio (classeFacade)));
	  col.addAll (constroiModel (FabricaUtilitarios.getFieldsCamposInformacao (classeFacade)));
	  JScrollListAtributos jlistNovo = new JScrollListAtributos (new JListAtributos ());
	  jlistNovo.getList ().setListData (col);
	  jlistNovo.getList ().addListSelectionListener (this);
	  jlistNovo.getList ().setScrollDono (jlistNovo);
	  jlistRaiz = jlistNovo;
	  add (jlistRaiz);
	}
      catch (ClassNotFoundException e)
	{
	  e.printStackTrace ();
	}
    }
    
    private Vector constroiModel (List listFields)
    {
      Vector model = new Vector ();
      Iterator it = listFields.iterator ();
      while (it.hasNext ())
	model.add (new ItemAtributo ((Field) it.next ()));
      return model;
    }
    
    public void valueChanged (ListSelectionEvent e)
    {
      JListAtributos jlist = (JListAtributos) e.getSource ();
      ItemAtributo item = (ItemAtributo) jlist.getSelectedValue ();
      if (jlist.getScrollDono ().getProximo () != null)
	{
	  jlist.getScrollDono ().setProximo (null);
	  removeAll ();
	  adicionaRecursivamente (jlistRaiz);
	  validate ();
	  repaint ();
	}
      Class var_class = serpro.ppgd.negocio.Informacao.class;
      if (var_class.isAssignableFrom (item.getField ().getType ()))
	armazenaSelecao ();
      else
	{
	  Class var_class_1_ = serpro.ppgd.negocio.ObjetoNegocio.class;
	  if (var_class_1_.isAssignableFrom (item.getField ().getType ()))
	    avancaPara (jlist.getScrollDono (), item.getField ().getType ());
	}
    }
    
    private void adicionaRecursivamente (JScrollListAtributos aJlist)
    {
      add (aJlist);
      add (Box.createRigidArea (new Dimension (10, 1)));
      if (aJlist.getProximo () != null)
	adicionaRecursivamente (aJlist.getProximo ());
    }
    
    private void avancaPara (JScrollListAtributos jlistAtual, Class proximaFonte)
    {
      Vector col = new Vector ();
      col.addAll (constroiModel (FabricaUtilitarios.getFieldsCamposObjetoNegocio (proximaFonte)));
      col.addAll (constroiModel (FabricaUtilitarios.getFieldsCamposInformacao (proximaFonte)));
      JScrollListAtributos jlistNovo = new JScrollListAtributos (new JListAtributos ());
      jlistNovo.getList ().setListData (col);
      jlistNovo.getList ().addListSelectionListener (this);
      jlistNovo.getList ().setScrollDono (jlistNovo);
      jlistAtual.setProximo (jlistNovo);
      add (jlistNovo);
      getParent ().validate ();
      getParent ().repaint ();
      scrollRectToVisible (new Rectangle (jlistNovo.getLocationOnScreen (), jlistNovo.getSize ()));
      validate ();
      repaint ();
    }
    
    private void armazenaSelecao ()
    {
      JScrollListAtributos jlist = jlistRaiz;
      selecao = ((ItemAtributo) jlist.getList ().getSelectedValue ()).getField ().getName ();
      while (jlist.getProximo () != null)
	{
	  jlist = jlist.getProximo ();
	  NavegadorAtributos navegadoratributos = this;
	  String string = navegadoratributos.selecao;
	  StringBuffer stringbuffer = new StringBuffer (string);
	  navegadoratributos.selecao = stringbuffer.append (".").append (((ItemAtributo) jlist.getList ().getSelectedValue ()).getField ().getName ()).toString ();
	}
      String old = bean.getInformacaoAssociada ();
      bean.setInformacaoAssociada (selecao);
      customizer.disparaMudancaEm ("informacaoAssociada", old, selecao);
    }
  }
  
  private class ItemAtributo
  {
    private Field field;
    
    public ItemAtributo (Field f)
    {
      setField (f);
    }
    
    public String toString ()
    {
      String descricao = null;
      Class var_class = serpro.ppgd.negocio.Informacao.class;
      if (var_class.isAssignableFrom (getField ().getType ()))
	descricao = "<html><b>" + getField ().getName () + "</b></html>";
      else
	descricao = getField ().getName ();
      return descricao;
    }
    
    public void setField (Field field)
    {
      this.field = field;
    }
    
    public Field getField ()
    {
      return field;
    }
  }
  
  private class JScrollListAtributos extends JScrollPane
  {
    private JScrollListAtributos proximo = null;
    private JListAtributos list;
    
    public JScrollListAtributos (JListAtributos aList)
    {
      super (aList);
      setVerticalScrollBarPolicy (22);
      setAlignmentY (0.0F);
      setMaximumSize (new Dimension (150, 160));
      setPreferredSize (new Dimension (150, 160));
      setMinimumSize (new Dimension (150, 160));
      list = aList;
    }
    
    public void setProximo (JScrollListAtributos proximo)
    {
      this.proximo = proximo;
    }
    
    public JScrollListAtributos getProximo ()
    {
      return proximo;
    }
    
    public void setList (JListAtributos list)
    {
      this.list = list;
    }
    
    public JListAtributos getList ()
    {
      return list;
    }
  }
  
  private class JListAtributos extends JList
  {
    private JScrollListAtributos scrollDono;
    
    public JListAtributos ()
    {
      /* empty */
    }
    
    public void setScrollDono (JScrollListAtributos scrollDono)
    {
      this.scrollDono = scrollDono;
    }
    
    public JScrollListAtributos getScrollDono ()
    {
      return scrollDono;
    }
  }
  
  public PropertyEditorInfoAssociada ()
  {
    initComponents ();
  }
  
  private void initComponents ()
  {
    pnlInfoAssociada = new JPanel ();
    jLabel1 = new JLabel ();
    pnlInfoAssociada.setLayout (new BorderLayout ());
    jLabel1.setText ("Informa\u00e7\u00e3o associada:");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (pnlInfoAssociada, -1, 581, 32767).add (jLabel1)).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel1).addPreferredGap (0).add (pnlInfoAssociada, -1, 199, 32767).addContainerGap ()));
  }
  
  public void setObject (Object bean)
  {
    JScrollPane scroll = new JScrollPane (new NavegadorAtributos ((JEditCampo) bean, this));
    scroll.setViewportBorder (BorderFactory.createEmptyBorder (4, 4, 4, 4));
    scroll.setBorder (BorderFactory.createEmptyBorder ());
    scroll.setHorizontalScrollBarPolicy (32);
    pnlInfoAssociada.add (scroll, "Center");
  }
  
  public void disparaMudancaEm (String propriedade, Object antigo, Object novo)
  {
    firePropertyChange (propriedade, antigo, novo);
  }
  
  public boolean isPaintable ()
  {
    return false;
  }
  
  public boolean supportsCustomEditor ()
  {
    return true;
  }
  
  public Component getCustomEditor ()
  {
    return this;
  }
  
  public Object getValue ()
  {
    return null;
  }
  
  public void setValue (Object value)
  {
    /* empty */
  }
  
  public String getAsText ()
  {
    return null;
  }
  
  public String getJavaInitializationString ()
  {
    return null;
  }
  
  public String[] getTags ()
  {
    return null;
  }
  
  public void setAsText (String text) throws IllegalArgumentException
  {
    /* empty */
  }
  
  public void paintValue (Graphics gfx, Rectangle box)
  {
    /* empty */
  }
}
