/* JEditOcupacaoPrincipal - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.text.MaskFormatter;
import javax.swing.tree.DefaultMutableTreeNode;

import com.jgoodies.forms.layout.FormLayout;

import serpro.ppgd.gui.ConstantesGlobaisGUI;
import serpro.ppgd.gui.xbeans.JButtonMensagem;
import serpro.ppgd.gui.xbeans.JEditCampo;
import serpro.ppgd.gui.xbeans.PainelBotao;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.PPGDFormPanel;
import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.ElementoTabela;
import serpro.ppgd.negocio.Informacao;

public class JEditOcupacaoPrincipal extends JEditCampo
{
  private static Codigo vazio = new Codigo (null, "EditOcupacaoPrincipal", new Vector ());
  private static String TITULO_OCUP_PRINCIPAL = "Ocupa\u00e7\u00e3o Principal";
  private JTextField labelCodigo;
  private JLabel label;
  private JButton button;
  private JPanel box;
  private JDialog frame;
  private JTree tree;
  private float tamanhoOriginalLabel = -1.0F;
  
  private class LocalActionListener implements ActionListener
  {
    public void actionPerformed (ActionEvent e)
    {
      if (frame == null)
	{
	  frame = new JDialog ();
	  frame.setDefaultCloseOperation (2);
	  DefaultMutableTreeNode raiz = new DefaultMutableTreeNode (JEditOcupacaoPrincipal.TITULO_OCUP_PRINCIPAL);
	  Vector colecao = (Vector) ((Codigo) JEditOcupacaoPrincipal.this.getInformacao ()).getColecaoElementoTabela ();
	  ElementoTabela elementoTabela = (ElementoTabela) colecao.firstElement ();
	  String codigoNivel1 = elementoTabela.getConteudo (1);
	  String codigoNivel2 = elementoTabela.getConteudo (0);
	  String descricaoNivel1 = elementoTabela.getConteudo (2);
	  String descricaoNivel2 = elementoTabela.getConteudo (3);
	  for (int i = 0; i < colecao.size (); i++)
	    {
	      String _codigoNivel1 = codigoNivel1;
	      DefaultMutableTreeNode elementoRaiz = new DefaultMutableTreeNode (codigoNivel1 + "-" + descricaoNivel1);
	      raiz.add (elementoRaiz);
	      while (_codigoNivel1.equals (elementoTabela.getConteudo (1)))
		{
		  DefaultMutableTreeNode elemento = new DefaultMutableTreeNode (codigoNivel2 + "-" + descricaoNivel2);
		  elementoRaiz.add (elemento);
		  if (++i >= colecao.size ())
		    break;
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
      frame.setSize (new Dimension (700, 400));
      frame.setLocationRelativeTo (null);
      frame.setVisible (true);
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
	      JEditOcupacaoPrincipal.this.setObservadorAtivo (false);
	      JEditOcupacaoPrincipal.this.getInformacao ().setConteudo (codigo);
	      labelCodigo.setText (codigo);
	      JEditOcupacaoPrincipal.this.chamaValidacao ();
	      JEditOcupacaoPrincipal.this.setObservadorAtivo (true);
	      label.setText (descricao);
	    }
	}
      return ret;
    }
  }
  
  public JEditOcupacaoPrincipal ()
  {
    this (vazio);
  }
  
  public JEditOcupacaoPrincipal (Informacao campo, Dimension[] d)
  {
    super (campo);
    if (d.length < 4)
      throw new IllegalStateException ("O array d[] deve ter pelo menos 4 elementos");
  }
  
  public JEditOcupacaoPrincipal (Informacao campo)
  {
    this (campo, new Dimension[] { new Dimension (3, 0), new Dimension (2, 0), new Dimension (0, 0), new Dimension (700, 400) });
  }
  
  public JEditOcupacaoPrincipal (Informacao campo, Dimension d)
  {
    this (campo, new Dimension[] { new Dimension (3, 0), new Dimension (2, 0), new Dimension (0, 0), d });
  }
  
  public JComponent getComponenteEditor ()
  {
    return box;
  }
  
  protected void buildComponente ()
  {
    removeAll ();
    setLayout (new BorderLayout ());
    if (box == null)
      {
	box = new JPanel (new BorderLayout ());
	JPanel boxInter1 = new JPanel (new BorderLayout ());
	MaskFormatter format = null;
	try
	  {
	    format = new MaskFormatter ("###");
	  }
	catch (Exception e)
	  {
	    e.printStackTrace ();
	  }
	labelCodigo = new JFormattedTextField (format);
	((JFormattedTextField) labelCodigo).setFocusLostBehavior (0);
	labelCodigo.setPreferredSize (new Dimension (30, labelCodigo.getPreferredSize ().height));
	labelCodigo.addFocusListener (new FocusAdapter ()
	{
	  public void focusGained (FocusEvent e)
	  {
	    SwingUtilities.invokeLater (new Runnable ()
	    {
	      public void run ()
	      {
		labelCodigo.selectAll ();
	      }
	    });
	  }
	  
	  public void focusLost (FocusEvent e)
	  {
	    JEditOcupacaoPrincipal.this.setIdentificacaoFoco (false);
	    JEditOcupacaoPrincipal.this.setObservadorAtivo (false);
	    JEditOcupacaoPrincipal.this.getInformacao ().setConteudo (labelCodigo.getText ());
	    if (JEditOcupacaoPrincipal.this.chamaValidacao ())
	      label.setText (((Codigo) campo).getConteudoAtual (3));
	    else
	      label.setText ("");
	    JEditOcupacaoPrincipal.this.setObservadorAtivo (true);
	  }
	});
	button = new JButton (new ImageIcon (this.getClass ().getResource ("/icones/ico_ocup.png")));
	button.setPreferredSize (new Dimension (24, button.getPreferredSize ().height));
	if (! PlataformaPPGD.isEmDesign ())
	  button.addActionListener (new LocalActionListener ());
	boxInter1.add (labelCodigo, "Center");
	boxInter1.add (button, "East");
	box.add (boxInter1, "West");
	label = new JLabel ();
	label.setPreferredSize (new Dimension (90, label.getPreferredSize ().height));
	init (label, ConstantesGlobaisGUI.BORDA_EDIT_CAMPO);
	label.setHorizontalAlignment (2);
	label.setFocusable (false);
	box.add (label, "Center");
      }
    PPGDFormPanel pnl = new PPGDFormPanel ();
    pnl.setLayout (new FormLayout ("30dlu:grow", "P"));
    pnl.getBuilder ().append (getComponenteEditor ());
    add (pnl, "Center");
    JButtonMensagem btnMsg = getButtonMensagem ();
    if (btnMsg != null)
      add (new PainelBotao (btnMsg), "East");
  }
  
  private void init (JComponent componente, Border border)
  {
    componente.setAlignmentY (0.0F);
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
	if (getRotulo () != null)
	  getRotulo ().setEnabled (false);
      }
    else
      {
	labelCodigo.setEnabled (true);
	label.setEnabled (true);
	button.setEnabled (true);
	if (getRotulo () != null)
	  getRotulo ().setEnabled (true);
      }
  }
  
  protected void habilitadoPropertyChange (boolean habilitado)
  {
    labelCodigo.setEnabled (habilitado);
    label.setEnabled (habilitado);
    button.setEnabled (habilitado);
    if (getRotulo () != null)
      getRotulo ().setEnabled (habilitado);
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    labelCodigo.setText (getInformacao ().getConteudoFormatado ());
    label.setText (((Codigo) getInformacao ()).getConteudoAtual (3));
  }
  
  public JComponent getComponenteFoco ()
  {
    return labelCodigo;
  }
  
  protected void informacaoModificada ()
  {
    implementacaoPropertyChange (null);
  }
  
  public void setEstiloFonte (int estilo)
  {
    Font f = labelCodigo.getFont ();
    f = f.deriveFont (estilo);
    labelCodigo.setFont (f);
    f = label.getFont ();
    f = f.deriveFont (estilo);
    label.setFont (f);
  }
  
  public void setIncrementoTamanhoFonte (int incremento)
  {
    incrementoTamanhoFonte = incremento;
    Font f = labelCodigo.getFont ();
    if (tamanhoOriginal == -1.0F)
      tamanhoOriginal = f.getSize2D ();
    f = f.deriveFont (tamanhoOriginal + (float) incremento);
    labelCodigo.setFont (f);
    f = label.getFont ();
    if (tamanhoOriginalLabel == -1.0F)
      tamanhoOriginalLabel = f.getSize2D ();
    f = f.deriveFont (tamanhoOriginal + (float) incremento);
    label.setFont (f);
  }
  
  public int getIncrementoTamanhoFonte ()
  {
    return incrementoTamanhoFonte;
  }
}
