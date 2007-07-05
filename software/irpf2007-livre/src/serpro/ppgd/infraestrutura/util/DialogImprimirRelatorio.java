/* DialogImprimirRelatorio - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.forms.layout.FormLayout;

import serpro.ppgd.formatosexternos.ListaRelatoriosIf;
import serpro.ppgd.formatosexternos.RelatorioIf;
import serpro.ppgd.gui.PPGDFormBuilder;
import serpro.ppgd.infraestrutura.PlataformaPPGD;

public class DialogImprimirRelatorio extends JPanel
{
  private static final PlataformaPPGD plataforma = PlataformaPPGD.getPlataforma ();
  private JDialog dialog = new JDialog (plataforma.getJanelaPrincipal (), "Imprimir", true);
  private String msg = "Selecione o que deseja imprimir:";
  private JList lista = new JList ();
  private JScrollPane scrollLista;
  private JPanel barraBotoes;
  private JButton btnVisualizar;
  private JButton btnOk;
  
  public DialogImprimirRelatorio ()
  {
    super (new BorderLayout ());
    initComponentes ();
    super.add (buildPanel (), "Center");
    super.setBorder (BorderFactory.createEmptyBorder (6, 6, 3, 6));
    dialog.setContentPane (this);
    dialog.pack ();
    dialog.setLocationRelativeTo (plataforma.getJanelaPrincipal ());
    dialog.setVisible (true);
  }
  
  private void initComponentes ()
  {
    scrollLista = getListaRelatorios ();
    barraBotoes = getBarraBotoes ();
    btnVisualizar = new JButton ("Visualizar...");
    btnVisualizar.setEnabled (false);
    btnOk.setEnabled (false);
    btnVisualizar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	clickedVisualizar ();
      }
    });
    lista.addListSelectionListener (new ListSelectionListener ()
    {
      public void valueChanged (ListSelectionEvent e)
      {
	selectionChanged (e);
      }
    });
  }
  
  protected void selectionChanged (ListSelectionEvent e)
  {
    ItemRelatorio item = (ItemRelatorio) lista.getSelectedValue ();
    if (item != null)
      {
	boolean habilitado = item.getRelatorio ().isHabilitado ();
	btnVisualizar.setEnabled (habilitado);
	btnOk.setEnabled (habilitado);
      }
  }
  
  private JPanel buildPanel ()
  {
    FormLayout layout = new FormLayout ("fill:p:grow, fill:5dlu:grow, fill:100dlu:grow", "20dlu, 65dlu, 20dlu");
    PPGDFormBuilder builder = new PPGDFormBuilder (layout);
    JPanel panel = new JPanel (new FlowLayout (0));
    panel.add (btnVisualizar);
    builder.append (new JLabel (msg), 3);
    builder.append (scrollLista, 3);
    builder.append (panel);
    builder.append (barraBotoes);
    return builder.getPanel ();
  }
  
  private void clickedCancel ()
  {
    dialog.setVisible (false);
  }
  
  private void clickedOk ()
  {
    dialog.setVisible (false);
    ItemRelatorio item = (ItemRelatorio) lista.getSelectedValue ();
    if (item != null)
      item.getRelatorio ().imprimir ();
  }
  
  protected void clickedVisualizar ()
  {
    ItemRelatorio item = (ItemRelatorio) lista.getSelectedValue ();
    if (item != null)
      item.getRelatorio ().visualizar ();
  }
  
  private JPanel getBarraBotoes ()
  {
    JPanel painel = new JPanel (new FlowLayout (2));
    JPanel gridPainel = new JPanel (new GridLayout (1, 4, 6, 2));
    btnOk = new JButton ("Ok");
    JButton cancel = new JButton ("Cancelar");
    btnOk.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	DialogImprimirRelatorio.this.clickedOk ();
      }
    });
    cancel.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	DialogImprimirRelatorio.this.clickedCancel ();
      }
    });
    gridPainel.add (btnOk);
    gridPainel.add (cancel);
    painel.add (gridPainel);
    return painel;
  }
  
  private JScrollPane getListaRelatorios ()
  {
    Vector dados = new Vector ();
    Iterator it = plataforma.getListaRelatorios ().iterator ();
    while (it.hasNext ())
      {
	ListaRelatoriosIf listaRelatorio = (ListaRelatoriosIf) it.next ();
	adicionaItensRelatorio (dados, listaRelatorio);
      }
    lista.setListData (dados);
    JScrollPane scroll = new JScrollPane (lista);
    lista.setSelectionMode (0);
    return scroll;
  }
  
  private void adicionaItensRelatorio (Vector dados, ListaRelatoriosIf listaRelatorio)
  {
    Iterator it = listaRelatorio.getRelatorios ().iterator ();
    while (it.hasNext ())
      {
	RelatorioIf rel = (RelatorioIf) it.next ();
	dados.add (new ItemRelatorio (rel));
      }
  }
  
  public static void main (String[] args)
  {
    new DialogImprimirRelatorio ();
  }
}
