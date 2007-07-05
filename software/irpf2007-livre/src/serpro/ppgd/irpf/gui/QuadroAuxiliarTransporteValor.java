/* QuadroAuxiliarTransporteValor - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.SoftBevelBorder;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.gui.UtilitariosGUI;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.irpf.ColecaoItemQuadroAuxiliar;
import serpro.ppgd.irpf.ItemQuadroAuxiliar;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Valor;

public class QuadroAuxiliarTransporteValor extends JPanel
{
  private Valor receptor = null;
  private ColecaoItemQuadroAuxiliar col = null;
  private String valAntigo = null;
  private Alfa descOutros;
  private JButton btnAdicionar;
  private JButton btnEditar;
  private JButton btnExcluir;
  private JButton btnTransportar;
  private JButton jButton2;
  private JEditValor jEditValor1;
  private JLabel jLabel2;
  private JPanel jPanel1;
  private JPanel jPanel2;
  private JScrollPane jScrollPane1;
  private JLabel lbl1;
  private JLabel lbl2;
  private TableQuadroAuxiliarTransporteValores tableQuadroAuxiliarTransporteValores1;
  
  public QuadroAuxiliarTransporteValor (Valor _recep, ColecaoItemQuadroAuxiliar _col)
  {
    receptor = _recep;
    col = _col;
    valAntigo = receptor.getConteudoFormatado ();
    initComponents ();
    UtilitariosGUI.aumentaFonte (lbl1, 2);
    UtilitariosGUI.aumentaFonte (lbl2, 2);
    ((TableModelQuadroAuxiliarTransporteValores) tableQuadroAuxiliarTransporteValores1.getModel ()).setObjetoNegocio (col);
    jEditValor1.setInformacao (col.getTotais ());
    tableQuadroAuxiliarTransporteValores1.addKeyListener (new KeyListener ()
    {
      public void keyPressed (KeyEvent e)
      {
	if (e.getKeyCode () == 127)
	  btnExcluir.doClick ();
	else if (e.getKeyCode () == 10)
	  btnEditar.doClick ();
      }
      
      public void keyReleased (KeyEvent e)
      {
	/* empty */
      }
      
      public void keyTyped (KeyEvent e)
      {
	/* empty */
      }
    });
  }
  
  public QuadroAuxiliarTransporteValor (Valor _recep, ColecaoItemQuadroAuxiliar _col, Alfa aDescOutros)
  {
    this (_recep, _col);
    descOutros = aDescOutros;
  }
  
  private void initComponents ()
  {
    lbl1 = new JLabel ();
    lbl2 = new JLabel ();
    jScrollPane1 = new JScrollPane ();
    tableQuadroAuxiliarTransporteValores1 = new TableQuadroAuxiliarTransporteValores ();
    jPanel1 = new JPanel ();
    btnAdicionar = new JButton ();
    btnExcluir = new JButton ();
    btnEditar = new JButton ();
    jLabel2 = new JLabel ();
    jEditValor1 = new JEditValor ();
    jPanel2 = new JPanel ();
    btnTransportar = new JButton ();
    jButton2 = new JButton ();
    lbl1.setText ("<HTML><B>O uso deste quadro \u00e9 opcional. Objetiva facilitar o preenchimento para quem tem mais de um dado para a linha da ficha. Ao gravar a c\u00f3pia de seguran\u00e7a, os dados nesse quadro n\u00e3o ser\u00e3o aproveitados, mas o total permanece na respectiva linha.</B></HTML>");
    lbl1.setVerticalAlignment (1);
    lbl2.setText ("<HTML>Ao clicar no bot\u00e3o \"Transportar\" o programa levar\u00e1 o valor indicado na linha \"Totais\", substituindo eventual valor j\u00e1 informado na ficha.</HTML>");
    lbl2.setVerticalAlignment (1);
    jScrollPane1.setViewportView (tableQuadroAuxiliarTransporteValores1);
    btnAdicionar.setMnemonic ('d');
    btnAdicionar.setText ("<HTML><B>A<U>d</U>icionar</B></HTML>");
    btnAdicionar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	QuadroAuxiliarTransporteValor.this.btnAdicionarActionPerformed (evt);
      }
    });
    btnExcluir.setMnemonic ('x');
    btnExcluir.setText ("<HTML><B>E<U>x</U>cluir</B></HTML>");
    btnExcluir.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	QuadroAuxiliarTransporteValor.this.btnExcluirActionPerformed (evt);
      }
    });
    btnEditar.setMnemonic ('E');
    btnEditar.setText ("<HTML><B><U>E</U>ditar</B></HTML>");
    btnEditar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	QuadroAuxiliarTransporteValor.this.btnEditarActionPerformed (evt);
      }
    });
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (btnAdicionar).add (btnExcluir).add (btnEditar)).addContainerGap (-1, 32767)));
    jPanel1Layout.linkSize (new Component[] { btnAdicionar, btnEditar, btnExcluir }, 1);
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (btnAdicionar).addPreferredGap (0).add (btnExcluir).addPreferredGap (0).add (btnEditar).addContainerGap (119, 32767)));
    jLabel2.setHorizontalAlignment (4);
    jLabel2.setText ("<HTML><B>Totais: </B></HTML>");
    jLabel2.setBorder (new SoftBevelBorder (0));
    btnTransportar.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/OK.PNG")));
    btnTransportar.setMnemonic ('T');
    btnTransportar.setText ("<HTML><B><U>T</U>ransportar</B></HTML>");
    btnTransportar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	QuadroAuxiliarTransporteValor.this.btnTransportarActionPerformed (evt);
      }
    });
    jButton2.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/CANCELAR.PNG")));
    jButton2.setMnemonic ('C');
    jButton2.setText ("<HTML><B><U>C</U>ancelar</B></HTML>");
    jButton2.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	QuadroAuxiliarTransporteValor.this.jButton2ActionPerformed (evt);
      }
    });
    GroupLayout jPanel2Layout = new GroupLayout (jPanel2);
    jPanel2.setLayout (jPanel2Layout);
    jPanel2Layout.setHorizontalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().add (btnTransportar, -2, 123, -2).addPreferredGap (0).add (jButton2).addContainerGap (200, 32767)));
    jPanel2Layout.linkSize (new Component[] { btnTransportar, jButton2 }, 1);
    jPanel2Layout.setVerticalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().add (jPanel2Layout.createParallelGroup (3).add (btnTransportar).add (jButton2)).addContainerGap (-1, 32767)));
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (lbl2, -1, 567, 32767).add (lbl1, -1, 567, 32767).add (jLabel2, -2, 285, -2).add (layout.createSequentialGroup ().add (layout.createParallelGroup (2, false).add (jEditValor1, -2, 141, -2).add (jScrollPane1, -1, 432, 32767).add (jPanel2, -2, -1, -2)).addPreferredGap (0).add (jPanel1, -2, -1, -2))).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (lbl1, -2, 73, -2).addPreferredGap (0).add (lbl2, -2, 51, -2).addPreferredGap (0).add (layout.createParallelGroup (1).add (jScrollPane1, -2, 211, -2).add (jPanel1, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (2, jLabel2).add (2, jEditValor1, -2, -1, -2)).addPreferredGap (0).add (jPanel2, -2, -1, -2).addContainerGap (-1, 32767)));
  }
  
  private void btnEditarActionPerformed (ActionEvent evt)
  {
    if (tableQuadroAuxiliarTransporteValores1.getSelectedRows ().length > 0)
      {
	ItemQuadroAuxiliar itemEditado = (ItemQuadroAuxiliar) col.recuperarLista ().get (tableQuadroAuxiliarTransporteValores1.getSelectedRow ());
	JDialog dlgParent = (JDialog) SwingUtilities.getRoot (this);
	IRPFGuiUtil.exibeDialog (dlgParent, new PainelEdicaoItemQuadroAuxiliar (col, itemEditado, true), true, "Rendimentos", false);
      }
  }
  
  private void btnExcluirActionPerformed (ActionEvent evt)
  {
    if (tableQuadroAuxiliarTransporteValores1.getSelectedRows ().length > 0)
      {
	java.util.List listaARemover = new ArrayList ();
	for (int i = tableQuadroAuxiliarTransporteValores1.getSelectedRows ().length - 1; i >= 0; i--)
	  {
	    int indiceARemover = tableQuadroAuxiliarTransporteValores1.getSelectedRows ()[i];
	    listaARemover.add (col.recuperarLista ().get (indiceARemover));
	  }
	Iterator it = listaARemover.iterator ();
	while (it.hasNext ())
	  {
	    Object elem = it.next ();
	    col.recuperarLista ().remove (elem);
	  }
	((IRPFTableModel) tableQuadroAuxiliarTransporteValores1.getModel ()).fireTableDataChanged ();
	tableQuadroAuxiliarTransporteValores1.getSelectionModel ().clearSelection ();
      }
  }
  
  private void btnAdicionarActionPerformed (ActionEvent evt)
  {
    ItemQuadroAuxiliar novoItem = new ItemQuadroAuxiliar ();
    JDialog dlgParent = (JDialog) SwingUtilities.getRoot (this);
    IRPFGuiUtil.exibeDialog (dlgParent, new PainelEdicaoItemQuadroAuxiliar (col, novoItem, false), true, "Rendimentos", false);
  }
  
  private void jButton2ActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    receptor.setConteudo (valAntigo);
  }
  
  private void btnTransportarActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    receptor.setConteudo (col.getTotais ());
    if (descOutros != null)
      descOutros.setConteudo (col.getDescricoes ());
  }
}
