/* PaginadorTabela - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import serpro.ppgd.negocio.util.UtilitariosArquivo;

public abstract class PaginadorTabela extends JPanel implements ActionListener
{
  protected JButton btnPrimeiro = new JButton (new ImageIcon (UtilitariosArquivo.localizaArquivoEmClasspath ("/imagens/pag-primeiro.png")));
  protected JButton btnAnterior = new JButton (new ImageIcon (UtilitariosArquivo.localizaArquivoEmClasspath ("/imagens/pag-anterior.png")));
  protected JButton btnProximo = new JButton (new ImageIcon (UtilitariosArquivo.localizaArquivoEmClasspath ("/imagens/pag-proximo.png")));
  protected JButton btnUltimo = new JButton (new ImageIcon (UtilitariosArquivo.localizaArquivoEmClasspath ("/imagens/pag-ultimo.png")));
  protected JToolBar toolbar = new JToolBar ();
  protected JFormattedTextField txtPagina = new JFormattedTextField ();
  protected int indice = 1;
  
  public PaginadorTabela ()
  {
    setLayout (new FormLayout ("2dlu,P,2dlu,MIN(P;25dlu),2dlu", "P"));
    btnPrimeiro.setContentAreaFilled (false);
    btnAnterior.setContentAreaFilled (false);
    btnProximo.setContentAreaFilled (false);
    btnUltimo.setContentAreaFilled (false);
    btnPrimeiro.setBorderPainted (false);
    btnAnterior.setBorderPainted (false);
    btnProximo.setBorderPainted (false);
    btnUltimo.setBorderPainted (false);
    btnPrimeiro.setOpaque (false);
    btnAnterior.setOpaque (false);
    btnProximo.setOpaque (false);
    btnUltimo.setOpaque (false);
    toolbar.setOpaque (false);
    toolbar.add (btnPrimeiro);
    toolbar.add (btnAnterior);
    toolbar.add (btnProximo);
    toolbar.add (btnUltimo);
    toolbar.setFloatable (false);
    try
      {
	txtPagina.setColumns (4);
	MaskFormatter formatador = new MaskFormatter ("****");
	formatador.setValidCharacters ("0123456789 ");
	txtPagina.setFormatterFactory (new DefaultFormatterFactory (formatador));
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
    txtPagina.addActionListener (this);
    btnPrimeiro.addActionListener (this);
    btnAnterior.addActionListener (this);
    btnProximo.addActionListener (this);
    btnUltimo.addActionListener (this);
    CellConstraints cc = new CellConstraints ();
    add (toolbar, cc.xy (2, 1));
    add (txtPagina, cc.xy (4, 1));
    setOpaque (false);
    btnPrimeiro.doClick ();
  }
  
  public JButton getBtnAnterior ()
  {
    return btnAnterior;
  }
  
  public JButton getBtnPrimeiro ()
  {
    return btnPrimeiro;
  }
  
  public JButton getBtnProximo ()
  {
    return btnProximo;
  }
  
  public JButton getBtnUltimo ()
  {
    return btnUltimo;
  }
  
  public JToolBar getToolbar ()
  {
    return toolbar;
  }
  
  public static void main (String[] args)
  {
    new DialogGenerico (null, new PaginadorTabela ()
    {
      public int getTotalPaginas ()
      {
	return 6;
      }
      
      public void exibe (int pagina)
      {
	/* empty */
      }
    }, "Teste");
  }
  
  public void actionPerformed (ActionEvent e)
  {
    int totalPaginas = getTotalPaginas ();
    if (e.getSource ().equals (btnPrimeiro))
      indice = 1;
    else if (e.getSource ().equals (btnAnterior))
      indice--;
    else if (e.getSource ().equals (btnProximo))
      indice++;
    else if (e.getSource ().equals (btnUltimo))
      indice = totalPaginas;
    else if (e.getSource ().equals (txtPagina) && ! txtPagina.getText ().trim ().equals (""))
      {
	int paginaDigitada = Integer.parseInt (txtPagina.getText ().trim ());
	if (paginaDigitada >= 1 && paginaDigitada <= totalPaginas)
	  indice = paginaDigitada;
      }
    txtPagina.setText (String.valueOf (indice));
    atualizaBotoes (totalPaginas);
    exibe (indice);
  }
  
  protected void atualizaBotoes (int totalPaginas)
  {
    btnPrimeiro.setEnabled (true);
    btnAnterior.setEnabled (true);
    btnProximo.setEnabled (true);
    btnUltimo.setEnabled (true);
    if (totalPaginas == 0 || totalPaginas == 1)
      {
	btnPrimeiro.setEnabled (false);
	btnAnterior.setEnabled (false);
	btnProximo.setEnabled (false);
	btnUltimo.setEnabled (false);
      }
    else if (indice == 1)
      {
	btnPrimeiro.setEnabled (false);
	btnAnterior.setEnabled (false);
	btnProximo.setEnabled (true);
	btnUltimo.setEnabled (true);
      }
    else if (indice == totalPaginas)
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
  }
  
  public void setIndice (int pIndice)
  {
    int totalPaginas = getTotalPaginas ();
    if (pIndice >= 1 && pIndice <= totalPaginas)
      {
	indice = pIndice;
	txtPagina.setText (String.valueOf (indice));
	atualizaBotoes (totalPaginas);
	exibe (indice);
      }
  }
  
  public abstract void exibe (int i);
  
  public abstract int getTotalPaginas ();
}
