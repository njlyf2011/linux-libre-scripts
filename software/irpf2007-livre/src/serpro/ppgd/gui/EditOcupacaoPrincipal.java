/* EditOcupacaoPrincipal - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.beans.PropertyChangeEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.ElementoTabela;
import serpro.ppgd.negocio.Informacao;

public class EditOcupacaoPrincipal extends EditCampo
{
  private static Codigo vazio = new Codigo ("EditOcupacaoPrincipal");
  private static String TITULO_OCUP_PRINCIPAL = "Ocupa\u00e7\u00e3o Principal";
  private JTextField labelCodigo;
  private JLabel label;
  private JButton button;
  private JPanel box;
  private JDialog frame;
  private JTree tree;
  
  private class LocalActionListener implements ActionListener
  {
    public void actionPerformed (ActionEvent e)
    {
      if (frame == null)
	{
	  frame = new JDialog ();
	  frame.setDefaultCloseOperation (2);
	  DefaultMutableTreeNode raiz = new DefaultMutableTreeNode (EditOcupacaoPrincipal.TITULO_OCUP_PRINCIPAL);
	  Vector colecao = (Vector) ((Codigo) EditOcupacaoPrincipal.this.getInformacao ()).getColecaoElementoTabela ();
	  ElementoTabela elementoTabela = (ElementoTabela) colecao.firstElement ();
	  String codigoNivel1 = elementoTabela.getConteudo (1);
	  String codigoNivel2 = elementoTabela.getConteudo (0);
	  String descricaoNivel1 = elementoTabela.getConteudo (2);
	  String descricaoNivel2 = elementoTabela.getConteudo (3);
	  for (int i = 1; i < colecao.size (); i++)
	    {
	      String _codigoNivel1 = codigoNivel1;
	      DefaultMutableTreeNode elementoRaiz = new DefaultMutableTreeNode (codigoNivel1 + "-" + descricaoNivel1);
	      raiz.add (elementoRaiz);
	      for (/**/; _codigoNivel1.equals (elementoTabela.getConteudo (1)) && i < colecao.size (); i++)
		{
		  DefaultMutableTreeNode elemento = new DefaultMutableTreeNode (codigoNivel2 + "-" + descricaoNivel2);
		  elementoRaiz.add (elemento);
		  elementoTabela = (ElementoTabela) colecao.get (i);
		  codigoNivel1 = elementoTabela.getConteudo (1);
		  codigoNivel2 = elementoTabela.getConteudo (0);
		  descricaoNivel1 = elementoTabela.getConteudo (2);
		  descricaoNivel2 = elementoTabela.getConteudo (3);
		}
	      i--;
	    }
	  tree = new JTree (raiz);
	  tree.getSelectionModel ().setSelectionMode (1);
	  tree.addKeyListener (new KeyAdapter ()
	  {
	    public void keyReleased (KeyEvent e_2_)
	    {
	      super.keyReleased (e_2_);
	      switch (e_2_.getKeyCode ())
		{
		case 27:
		  frame.dispose ();
		  break;
		case 10:
		  if (LocalActionListener.this.setLabelCodigo (tree))
		    frame.dispose ();
		  break;
		}
	    }
	  });
	  tree.addMouseListener (new MouseAdapter ()
	  {
	    public void mousePressed (MouseEvent e_5_)
	    {
	      if (e_5_.getClickCount () == 2 && LocalActionListener.this.setLabelCodigo (tree))
		frame.dispose ();
	    }
	  });
	  JScrollPane scrollPane = new JScrollPane (tree);
	  frame.getContentPane ().add (scrollPane);
	  frame.addWindowFocusListener (new WindowFocusListener ()
	  {
	    public void windowGainedFocus (WindowEvent e_8_)
	    {
	      /* empty */
	    }
	    
	    public void windowLostFocus (WindowEvent e_10_)
	    {
	      frame.dispose ();
	    }
	  });
	}
      frame.setSize (new Dimension (d[3].width, d[3].height));
      frame.setLocationRelativeTo (null);
      frame.show ();
    }
    
    private boolean setLabelCodigo (JTree tree)
    {
      boolean ret = false;
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent ();
      if (node != null)
	{
	  String s = (String) node.getUserObject ();
	  int i = s.indexOf ('-');
	  if (i == 3)
	    {
	      ret = true;
	      String codigo = s.substring (0, i);
	      String descricao = s.substring (i + 1, s.length ());
	      EditOcupacaoPrincipal.this.setObservadorAtivo (false);
	      EditOcupacaoPrincipal.this.getInformacao ().setConteudo (codigo);
	      labelCodigo.setText (codigo);
	      EditOcupacaoPrincipal.this.chamaValidacao ();
	      EditOcupacaoPrincipal.this.setObservadorAtivo (true);
	      label.setText (descricao);
	    }
	}
      return ret;
    }
  }
  
  public EditOcupacaoPrincipal ()
  {
    this (vazio);
  }
  
  public EditOcupacaoPrincipal (Informacao campo, Dimension[] d)
  {
    super (campo, d);
    if (d.length < 4)
      throw new IllegalStateException ("O array d[] deve ter pelo menos 4 elementos");
  }
  
  public EditOcupacaoPrincipal (Informacao campo)
  {
    this (campo, new Dimension[] { new Dimension (3, 0), new Dimension (2, 0), new Dimension (0, 0), new Dimension (700, 400) });
  }
  
  public EditOcupacaoPrincipal (Informacao campo, Dimension d)
  {
    this (campo, new Dimension[] { new Dimension (3, 0), new Dimension (2, 0), new Dimension (0, 0), d });
  }
  
  public JComponent getComponenteEditor ()
  {
    return box;
  }
  
  public void setInformacao (final Informacao campo)
  {
    FormLayout layout = new FormLayout ("pref, 1dlu, 0:grow", "fill:pref:grow");
    box = new JPanel (layout);
    CellConstraints cc = new CellConstraints ();
    JPanel boxInter1 = new JPanel (new BorderLayout ());
    if (labelCodigo == null)
      {
	labelCodigo = new JTextField ();
	init (labelCodigo, d[0].width * 10 - 2, d[0].height, ConstantesGlobaisGUI.BORDA_VAZIA);
	if (campo.getConteudoFormatado ().length () != 0)
	  labelCodigo.setText (campo.getConteudoFormatado ());
	labelCodigo.addFocusListener (new FocusAdapter ()
	{
	  public void focusGained (FocusEvent e)
	  {
	    /* empty */
	  }
	  
	  public void focusLost (FocusEvent e)
	  {
	    EditOcupacaoPrincipal.this.setIdentificacaoFoco (false);
	    EditOcupacaoPrincipal.this.setObservadorAtivo (false);
	    EditOcupacaoPrincipal.this.getInformacao ().setConteudo (labelCodigo.getText ());
	    if (EditOcupacaoPrincipal.this.chamaValidacao ())
	      label.setText (((Codigo) campo).getConteudoAtual (3));
	    else
	      label.setText ("");
	    EditOcupacaoPrincipal.this.setObservadorAtivo (true);
	  }
	});
	button = new JButton ();
	init (button, d[1].width * 10 - 3, d[1].height, ConstantesGlobaisGUI.BORDA_BOTAO);
	javax.swing.Icon icon = new ImageIcon (this.getClass ().getResource ("/imagens/ico_setaparabaixo.gif"));
	button.setIcon (icon);
	button.setFocusable (false);
	button.addActionListener (new LocalActionListener ());
	boxInter1.add (labelCodigo, "Center");
	boxInter1.add (button, "East");
	box.add (boxInter1, cc.xy (1, 1));
	label = new JLabel ();
	init (label, d[2].width * 10, d[2].height, ConstantesGlobaisGUI.BORDA_EDIT_CAMPO);
	label.setHorizontalAlignment (2);
	label.setFocusable (false);
	box.add (label, cc.xy (3, 1));
	if (campo.getConteudoFormatado ().length () != 0)
	  label.setText (((Codigo) campo).getConteudoAtual (3));
	d[0].width = d[0].width + d[1].width + d[2].width;
      }
  }
  
  private void init (JComponent componente, int tam, int alt, Border border)
  {
    componente.setAlignmentY (0.0F);
    componente.setPreferredSize (new Dimension (tam, alt));
    if (border != null)
      componente.setBorder (border);
  }
  
  private void initNaoFixo (JComponent componente, int tam, int alt, Border border)
  {
    componente.setAlignmentY (0.0F);
    componente.setPreferredSize (new Dimension (tam, alt * 20));
    if (border != null)
      componente.setBorder (border);
  }
  
  protected void readOnlyPropertyChange (boolean readOnly)
  {
    if (readOnly)
      {
	labelCodigo.setEnabled (false);
	label.setEnabled (false);
	button.setEnabled (false);
	labelCampo.setEnabled (false);
      }
    else
      {
	labelCodigo.setEnabled (true);
	label.setEnabled (true);
	button.setEnabled (true);
	labelCampo.setEnabled (true);
      }
  }
  
  protected void habilitadoPropertyChange (boolean habilitado)
  {
    labelCodigo.setEnabled (habilitado);
    label.setEnabled (habilitado);
    button.setEnabled (habilitado);
    labelCampo.setEnabled (habilitado);
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    labelCodigo.setText (campo.getConteudoFormatado ());
    label.setText (((Codigo) campo).getConteudoAtual (3));
  }
  
  public JComponent getComponenteFoco ()
  {
    return labelCodigo;
  }
  
  public void setPerdeFocoComEnter (boolean isPerdeFocoComEnter)
  {
    /* empty */
  }
}
