/* PainelExcluirDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.TableSelecionaDeclaracao;
import serpro.ppgd.irpf.util.IRPFUtil;

public class PainelExcluirDeclaracao extends JPanel
{
  private JButton btnAjuda;
  private JButton btnCancelar;
  private JButton btnOk;
  private TableSelecionaDeclaracao edtTableDecs;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JPanel jPanel2;
  private JPanel jPanel3;
  private JScrollPane jScrollPane1;
  
  public PainelExcluirDeclaracao ()
  {
    initComponents ();
    PlataformaPPGD.getPlataforma ().setHelpID (this, "CEXCLUIR");
    PlataformaPPGD.getPlataforma ().setHelpID (btnAjuda, "CEXCLUIR");
    jScrollPane1.getViewport ().setBackground (Color.WHITE);
    edtTableDecs.addDeclaracaoSelectionListener (new ListSelectionListener ()
    {
      public void valueChanged (ListSelectionEvent e)
      {
	if (edtTableDecs.getSelectedRowCount () > 0)
	  btnOk.setEnabled (true);
	else
	  btnOk.setEnabled (false);
      }
    });
    edtTableDecs.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent e)
      {
	if (e.getClickCount () == 2 && edtTableDecs.getSelectedRowCount () > 0)
	  PainelExcluirDeclaracao.this.btnOkActionPerformed (null);
      }
    });
    edtTableDecs.setSelectionMode (2);
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jPanel2 = new JPanel ();
    btnOk = new JButton ();
    btnCancelar = new JButton ();
    btnAjuda = new JButton ();
    jPanel3 = new JPanel ();
    jLabel2 = new JLabel ();
    jLabel3 = new JLabel ();
    jLabel4 = new JLabel ();
    jLabel5 = new JLabel ();
    jScrollPane1 = new JScrollPane ();
    edtTableDecs = new TableSelecionaDeclaracao ();
    jLabel1.setForeground (new Color (0, 0, 128));
    jLabel1.setText ("<HTML><B>Selecione os contribuintes cujos dados deseja excluir:</B></HTML>");
    btnOk.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/OK.PNG")));
    btnOk.setText ("<HTML><B><U>E</U>xcluir</B></HTML>");
    btnOk.setEnabled (false);
    btnOk.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelExcluirDeclaracao.this.btnOkActionPerformed (evt);
      }
    });
    btnCancelar.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/CANCELAR.PNG")));
    btnCancelar.setText ("<HTML><B><U>F</U>echar</B></HTML>");
    btnCancelar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelExcluirDeclaracao.this.btnCancelarActionPerformed (evt);
      }
    });
    btnAjuda.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/HELP.PNG")));
    btnAjuda.setText ("<HTML><B><U>A</U>juda</B></HTML>");
    GroupLayout jPanel2Layout = new GroupLayout (jPanel2);
    jPanel2.setLayout (jPanel2Layout);
    jPanel2Layout.setHorizontalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().addContainerGap ().add (jPanel2Layout.createParallelGroup (1).add (btnOk).add (btnCancelar).add (btnAjuda)).addContainerGap (-1, 32767)));
    jPanel2Layout.linkSize (new Component[] { btnAjuda, btnCancelar, btnOk }, 1);
    jPanel2Layout.setVerticalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().add (btnOk).addPreferredGap (0).add (btnCancelar).addPreferredGap (0).add (btnAjuda).addContainerGap (209, 32767)));
    jPanel2Layout.linkSize (new Component[] { btnAjuda, btnCancelar, btnOk }, 2);
    jLabel2.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/irpr_gravada.png")));
    jLabel2.setText ("<HTML><B>Gravada</B.</HTML>");
    jLabel3.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/irpr_transmitida.png")));
    jLabel3.setText ("<HTML><B>Transmitida</B.</HTML>");
    jLabel4.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/irpr_completa.png")));
    jLabel4.setText ("<HTML><B>Completa</B></HTML>");
    jLabel5.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/irpr_simplificada.png")));
    jLabel5.setText ("<HTML><B>Simplificada</B></HTML>");
    GroupLayout jPanel3Layout = new GroupLayout (jPanel3);
    jPanel3.setLayout (jPanel3Layout);
    jPanel3Layout.setHorizontalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().addContainerGap ().add (jLabel2, -2, 89, -2).addPreferredGap (0).add (jLabel3, -2, 105, -2).addPreferredGap (0).add (jLabel4, -2, 91, -2).addPreferredGap (0).add (jLabel5).addContainerGap (-1, 32767)));
    jPanel3Layout.setVerticalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().addContainerGap ().add (jPanel3Layout.createParallelGroup (3).add (jLabel2).add (jLabel3).add (jLabel5).add (jLabel4)).addContainerGap (-1, 32767)));
    edtTableDecs.setAutoResizeMode (0);
    edtTableDecs.getColumnModel ().getColumn (0).setPreferredWidth (100);
    edtTableDecs.getColumnModel ().getColumn (1).setPreferredWidth (80);
    jScrollPane1.setViewportView (edtTableDecs);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jPanel3, -2, -1, -2).add (jLabel1).add (2, layout.createSequentialGroup ().add (jScrollPane1, -1, 552, 32767).addPreferredGap (0).add (jPanel2, -2, -1, -2).add (10, 10, 10))).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (2, layout.createSequentialGroup ().addContainerGap ().add (jLabel1).addPreferredGap (0).add (layout.createParallelGroup (2).add (1, jPanel2, -1, -1, 32767).add (1, jScrollPane1, -1, 299, 32767)).addPreferredGap (0).add (jPanel3, -2, -1, -2).addContainerGap ()));
  }
  
  private void btnCancelarActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
  
  private void btnOkActionPerformed (ActionEvent evt)
  {
    if (JOptionPane.showConfirmDialog (getParent (), "Tem certeza que deseja excluir esta declara\u00e7\u00e3o?", "Confirma\u00e7\u00e3o", 0) == 0)
      {
	((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
	List ids = edtTableDecs.getIdentificadorDeclaracao (edtTableDecs.getSelectedRows ());
	IRPFFacade.excluirDeclaracao (ids);
	IRPFUtil.habilitaComponentesTemDeclaracao ();
      }
  }
}
