/* PainelVerificarPendencias_1 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.gui.pendencia.TableModelPendencia;
import serpro.ppgd.infraestrutura.util.PainelCacher;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.pendencias.EditTablePendenciasIRPF;
import serpro.ppgd.negocio.util.FabricaUtilitarios;

public class PainelVerificarPendencias_1 extends JPanel
{
  private JButton btnAjuda;
  private JButton btnImprirmir;
  private JButton btnOk;
  private EditTablePendenciasIRPF editTablePendenciasIRPF1;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel5;
  private JPanel jPanel1;
  private JPanel jPanel2;
  private JPanel jPanel3;
  private JPanel jPanel4;
  private JScrollPane jScrollPane1;
  private JLabel lblAvisos;
  private JLabel lblErros;
  
  public PainelVerificarPendencias_1 ()
  {
    initComponents ();
    PainelCacher.getInstance ().instanciaTodosUrgenteMente ();
    ((TableModelPendencia) editTablePendenciasIRPF1.getModel ()).atualizaPendencias (FabricaUtilitarios.verificarPendencias (IRPFFacade.getInstancia ().getDeclaracao ()));
  }
  
  private void initComponents ()
  {
    jScrollPane1 = new JScrollPane ();
    editTablePendenciasIRPF1 = new EditTablePendenciasIRPF ();
    jPanel4 = new JPanel ();
    jPanel1 = new JPanel ();
    jLabel1 = new JLabel ();
    jPanel2 = new JPanel ();
    jLabel2 = new JLabel ();
    jLabel3 = new JLabel ();
    lblErros = new JLabel ();
    jLabel5 = new JLabel ();
    lblAvisos = new JLabel ();
    jPanel3 = new JPanel ();
    btnOk = new JButton ();
    btnAjuda = new JButton ();
    btnImprirmir = new JButton ();
    jScrollPane1.setBorder (BorderFactory.createBevelBorder (1));
    jScrollPane1.setViewportView (editTablePendenciasIRPF1);
    jPanel1.setBorder (BorderFactory.createEtchedBorder ());
    jLabel1.setForeground (new Color (0, 0, 128));
    jLabel1.setText ("<HTML><B>Para corrigir erro ou aviso, clique na respectiva descri\u00e7\u00e3o, que o programa mostrar\u00e1 o campo a ser corrigido.</B></HTML>");
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jLabel1, -1, 526, 32767).addContainerGap ()));
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().add (jLabel1).addContainerGap (-1, 32767)));
    jPanel2.setBorder (BorderFactory.createEtchedBorder ());
    jLabel2.setForeground (new Color (0, 0, 128));
    jLabel2.setText ("<HTML><B>Os ERROS impedem a grava\u00e7\u00e3o da declara\u00e7\u00e3o para a entrega \u00e0 SRF, enquanto os AVISOS s\u00e3o apenas alertas de pend\u00eancias que n\u00e3o impedem a grava\u00e7\u00e3o da mesma</B></HTML>");
    jLabel3.setForeground (new Color (0, 0, 128));
    jLabel3.setText ("<HTML><B>Erros:</B></HTML>");
    lblErros.setBorder (BorderFactory.createBevelBorder (1));
    jLabel5.setForeground (new Color (0, 0, 128));
    jLabel5.setText ("<HTML><B>Avisos:</B></HTML>");
    lblAvisos.setBorder (BorderFactory.createBevelBorder (1));
    GroupLayout jPanel2Layout = new GroupLayout (jPanel2);
    jPanel2.setLayout (jPanel2Layout);
    jPanel2Layout.setHorizontalGroup (jPanel2Layout.createParallelGroup (1).add (2, jPanel2Layout.createSequentialGroup ().addContainerGap ().add (jPanel2Layout.createParallelGroup (2).add (1, jLabel2, -1, 526, 32767).add (1, jPanel2Layout.createSequentialGroup ().add (jLabel3).addPreferredGap (0).add (lblErros, -2, 58, -2).addPreferredGap (0, 325, 32767).add (jLabel5).addPreferredGap (0).add (lblAvisos, -2, 59, -2))).addContainerGap ()));
    jPanel2Layout.setVerticalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().addContainerGap ().add (jLabel2).addPreferredGap (0).add (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createParallelGroup (1).add (2, lblErros, -2, 19, -2).add (2, jLabel3, -2, 24, -2)).add (jPanel2Layout.createParallelGroup (3).add (lblAvisos, -2, 24, -2).add (jLabel5))).addContainerGap ()));
    jPanel2Layout.linkSize (new Component[] { jLabel3, lblErros }, 2);
    jPanel3.setBorder (BorderFactory.createEtchedBorder ());
    btnOk.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/OK.PNG")));
    btnOk.setText ("<HTML><B><U>O</U>k</B></HTML>");
    btnOk.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelVerificarPendencias_1.this.btnOkActionPerformed (evt);
      }
    });
    btnAjuda.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/HELP.PNG")));
    btnAjuda.setText ("<HTML><B><U>A</U>juda</B></HTML>");
    btnImprirmir.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/HELP.PNG")));
    btnImprirmir.setMnemonic ('I');
    btnImprirmir.setText ("<HTML><B><U>I</U>mprimir</B></HTML>");
    GroupLayout jPanel3Layout = new GroupLayout (jPanel3);
    jPanel3.setLayout (jPanel3Layout);
    jPanel3Layout.setHorizontalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().add (btnOk).addPreferredGap (0).add (btnImprirmir).addPreferredGap (0).add (btnAjuda).addContainerGap (219, 32767)));
    jPanel3Layout.linkSize (new Component[] { btnAjuda, btnImprirmir, btnOk }, 1);
    jPanel3Layout.setVerticalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().add (jPanel3Layout.createParallelGroup (3).add (btnOk).add (btnImprirmir).add (btnAjuda)).addContainerGap (-1, 32767)));
    GroupLayout jPanel4Layout = new GroupLayout (jPanel4);
    jPanel4.setLayout (jPanel4Layout);
    jPanel4Layout.setHorizontalGroup (jPanel4Layout.createParallelGroup (1).add (2, jPanel1, -1, -1, 32767).add (jPanel2, -1, -1, 32767).add (jPanel3, -1, -1, 32767));
    jPanel4Layout.setVerticalGroup (jPanel4Layout.createParallelGroup (1).add (jPanel4Layout.createSequentialGroup ().add (jPanel1, -2, 42, -2).addPreferredGap (0).add (jPanel2, -2, 81, -2).addPreferredGap (0).add (jPanel3, -2, -1, -2).addContainerGap (-1, 32767)));
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (jScrollPane1, -1, 550, 32767).add (2, jPanel4, -1, -1, 32767));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jScrollPane1, -2, 208, -2).addPreferredGap (0).add (jPanel4, -2, -1, -2).addContainerGap (-1, 32767)));
  }
  
  private void btnOkActionPerformed (ActionEvent evt)
  {
    /* empty */
  }
}
