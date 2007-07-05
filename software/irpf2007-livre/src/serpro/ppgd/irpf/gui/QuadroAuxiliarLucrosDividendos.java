/* QuadroAuxiliarLucrosDividendos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import java.awt.Component;
import java.awt.Dimension;
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
import serpro.ppgd.irpf.ColecaoItemQuadroLucrosDividendos;
import serpro.ppgd.irpf.ItemQuadroLucrosDividendos;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.negocio.Valor;

public class QuadroAuxiliarLucrosDividendos extends JPanel
{
  private Valor receptor = null;
  private ColecaoItemQuadroLucrosDividendos col = null;
  private String valAntigo = null;
  private JButton btnAdicionar;
  private JButton btnEditar;
  private JButton btnExcluir;
  private JButton btnTransportar;
  private JEditValor jEditValor1;
  private JLabel jLabel2;
  private JPanel jPanel1;
  private JPanel jPanel2;
  private JScrollPane jScrollPane1;
  private JLabel lbl1;
  private JLabel lbl2;
  private TableQuadroAuxiliarLucrosDividendos tableQuadroAuxiliarTransporteValores1;
  
  public QuadroAuxiliarLucrosDividendos (Valor _recep, ColecaoItemQuadroLucrosDividendos _col)
  {
    receptor = _recep;
    col = _col;
    valAntigo = receptor.getConteudoFormatado ();
    initComponents ();
    UtilitariosGUI.aumentaFonte (lbl1, 2);
    UtilitariosGUI.aumentaFonte (lbl2, 2);
    ((TableModelQuadroAuxiliarLucrosDividendos) tableQuadroAuxiliarTransporteValores1.getModel ()).setObjetoNegocio (col);
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
  
  private void initComponents ()
  {
    lbl1 = new JLabel ();
    lbl2 = new JLabel ();
    jPanel1 = new JPanel ();
    btnAdicionar = new JButton ();
    btnExcluir = new JButton ();
    btnEditar = new JButton ();
    jLabel2 = new JLabel ();
    jEditValor1 = new JEditValor ();
    jPanel2 = new JPanel ();
    btnTransportar = new JButton ();
    jScrollPane1 = new JScrollPane ();
    tableQuadroAuxiliarTransporteValores1 = new TableQuadroAuxiliarLucrosDividendos ();
    lbl1.setText ("<HTML><B>Informe neste quadro os CNPJ, fontes pagadoras e os respectivos valores recebidos a t\u00edtulo de lucros e dividendos.</B></HTML>");
    lbl1.setVerticalAlignment (1);
    lbl1.setPreferredSize (new Dimension (705, 34));
    lbl2.setText ("<HTML>Ao clicar no bot\u00e3o \"Transportar\" o programa levar\u00e1 o valor indicado na linha \"Totais\", substituindo eventual valor j\u00e1 informado na ficha.</HTML>");
    lbl2.setVerticalAlignment (1);
    lbl2.setPreferredSize (new Dimension (648, 34));
    btnAdicionar.setMnemonic ('d');
    btnAdicionar.setText ("<HTML><B>A<U>d</U>icionar</B></HTML>");
    btnAdicionar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	QuadroAuxiliarLucrosDividendos.this.btnAdicionarActionPerformed (evt);
      }
    });
    btnExcluir.setMnemonic ('x');
    btnExcluir.setText ("<HTML><B>E<U>x</U>cluir</B></HTML>");
    btnExcluir.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	QuadroAuxiliarLucrosDividendos.this.btnExcluirActionPerformed (evt);
      }
    });
    btnEditar.setMnemonic ('E');
    btnEditar.setText ("<HTML><B><U>E</U>ditar</B></HTML>");
    btnEditar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	QuadroAuxiliarLucrosDividendos.this.btnEditarActionPerformed (evt);
      }
    });
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (btnAdicionar).add (btnExcluir).add (btnEditar)).addContainerGap (-1, 32767)));
    jPanel1Layout.linkSize (new Component[] { btnAdicionar, btnEditar, btnExcluir }, 1);
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (btnAdicionar).addPreferredGap (0).add (btnExcluir).addPreferredGap (0).add (btnEditar).addContainerGap (198, 32767)));
    jLabel2.setHorizontalAlignment (4);
    jLabel2.setText ("<HTML><B>Totais: </B></HTML>");
    jLabel2.setBorder (new SoftBevelBorder (0));
    btnTransportar.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/OK.PNG")));
    btnTransportar.setMnemonic ('O');
    btnTransportar.setText ("<HTML><B><U>T</U>ransportar</B></HTML>");
    btnTransportar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	QuadroAuxiliarLucrosDividendos.this.btnTransportarActionPerformed (evt);
      }
    });
    GroupLayout jPanel2Layout = new GroupLayout (jPanel2);
    jPanel2.setLayout (jPanel2Layout);
    jPanel2Layout.setHorizontalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().add (btnTransportar).addContainerGap (310, 32767)));
    jPanel2Layout.setVerticalGroup (jPanel2Layout.createParallelGroup (1).add (btnTransportar));
    jScrollPane1.setViewportView (tableQuadroAuxiliarTransporteValores1);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (2, layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (2).add (1, lbl2, -1, 551, 32767).add (1, lbl1, -1, 551, 32767).add (1, layout.createSequentialGroup ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jLabel2, -2, 285, -2).addPreferredGap (0).add (jEditValor1, -2, 141, -2)).add (jPanel2, -2, -1, -2).add (jScrollPane1, -2, 430, -2)).addPreferredGap (0).add (jPanel1, -2, -1, -2))).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (lbl1, -2, -1, -2).addPreferredGap (0).add (lbl2, -2, -1, -2).addPreferredGap (0).add (layout.createParallelGroup (1).add (jPanel1, -1, -1, 32767).add (layout.createSequentialGroup ().addPreferredGap (0).add (jScrollPane1, -2, 230, -2).addPreferredGap (0).add (layout.createParallelGroup (1).add (jEditValor1, -2, -1, -2).add (jLabel2)).addPreferredGap (0).add (jPanel2, -2, -1, -2))).addContainerGap ()));
  }
  
  private void btnEditarActionPerformed (ActionEvent evt)
  {
    if (tableQuadroAuxiliarTransporteValores1.getSelectedRows ().length > 0)
      {
	ItemQuadroLucrosDividendos itemEditado = (ItemQuadroLucrosDividendos) col.recuperarLista ().get (tableQuadroAuxiliarTransporteValores1.getSelectedRow ());
	JDialog dlgParent = (JDialog) SwingUtilities.getRoot (this);
	IRPFGuiUtil.exibeDialog (dlgParent, new PainelEdicaoItemLucrosDividendos (col, itemEditado, true), true, "Rendimentos", false);
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
    ItemQuadroLucrosDividendos novoItem = new ItemQuadroLucrosDividendos ();
    JDialog dlgParent = (JDialog) SwingUtilities.getRoot (this);
    IRPFGuiUtil.exibeDialog (dlgParent, new PainelEdicaoItemLucrosDividendos (col, novoItem, false), true, "Rendimentos", false);
  }
  
  private void btnTransportarActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    receptor.setConteudo (col.getTotais ());
  }
}
