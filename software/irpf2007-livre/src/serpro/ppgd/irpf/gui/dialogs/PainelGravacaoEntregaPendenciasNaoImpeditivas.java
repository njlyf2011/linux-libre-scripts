/* PainelGravacaoEntregaPendenciasNaoImpeditivas - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.gui.ConstantesGlobaisGUI;

public class PainelGravacaoEntregaPendenciasNaoImpeditivas extends JPanel
{
  public static int OPT_CANCELAR = 0;
  public static int OPT_GRAVAR = 1;
  public static int OPT_CORRIGIR = 2;
  public static int OPCAO_SELECIONADA = OPT_CANCELAR;
  private JButton btnCancelar;
  private JButton btnCorrigir;
  private JButton btnGravar;
  private JLabel jLabel7;
  private JPanel jPanel1;
  private JPanel jPanel2;
  private JPanel jPanel3;
  
  public PainelGravacaoEntregaPendenciasNaoImpeditivas ()
  {
    initComponents ();
  }
  
  private void initComponents ()
  {
    jPanel1 = new JPanel ();
    jLabel7 = new JLabel ();
    jPanel3 = new JPanel ();
    jPanel2 = new JPanel ();
    btnCorrigir = new JButton ();
    btnCancelar = new JButton ();
    btnGravar = new JButton ();
    jPanel1.setBorder (BorderFactory.createEtchedBorder ());
    jLabel7.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel7.setHorizontalAlignment (0);
    jLabel7.setText ("<html><B>A declara\u00e7\u00e3o possui pend\u00eancias que n\u00e3o impedem a grava\u00e7\u00e3o.<br>Voc\u00ea pode abrir a lista de pend\u00eancias para verifica\u00e7\u00e3o, continuar a<br>grava\u00e7\u00e3o ou cancelar a opera\u00e7\u00e3o.<br>O que deseja fazer?</B></html>");
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().add (jLabel7, -2, 472, -2).addContainerGap (32, 32767)));
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().add (jLabel7, -1, 69, 32767).addContainerGap ()));
    btnCorrigir.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/OK.PNG")));
    btnCorrigir.setMnemonic ('C');
    btnCorrigir.setText ("<HTML><B><U>C</U>orrigir</B></HTML>");
    btnCorrigir.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelGravacaoEntregaPendenciasNaoImpeditivas.this.btnCorrigirActionPerformed (evt);
      }
    });
    btnCancelar.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/CANCELAR.PNG")));
    btnCancelar.setMnemonic ('C');
    btnCancelar.setText ("<HTML><B><U>C</U>ancelar</B></HTML>");
    btnCancelar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelGravacaoEntregaPendenciasNaoImpeditivas.this.btnCancelarActionPerformed (evt);
      }
    });
    btnGravar.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/btnGravar.png")));
    btnGravar.setMnemonic ('G');
    btnGravar.setText ("<HTML><B><U>G</U>ravar</B></HTML>");
    btnGravar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelGravacaoEntregaPendenciasNaoImpeditivas.this.btnGravarActionPerformed (evt);
      }
    });
    GroupLayout jPanel2Layout = new GroupLayout (jPanel2);
    jPanel2.setLayout (jPanel2Layout);
    jPanel2Layout.setHorizontalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().add (btnCorrigir).addPreferredGap (0).add (btnGravar).addPreferredGap (0).add (btnCancelar).addContainerGap (-1, 32767)));
    jPanel2Layout.linkSize (new Component[] { btnCancelar, btnCorrigir, btnGravar }, 1);
    jPanel2Layout.setVerticalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createParallelGroup (3).add (btnCorrigir).add (btnGravar).add (btnCancelar)));
    jPanel2Layout.linkSize (new Component[] { btnCancelar, btnCorrigir, btnGravar }, 2);
    jPanel3.add (jPanel2);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jPanel1, -1, -1, 32767).add (2, jPanel3, -1, 508, 32767)).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jPanel1, -2, -1, -2).addPreferredGap (0).add (jPanel3, -2, -1, -2).addContainerGap (-1, 32767)));
  }
  
  private void btnCancelarActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    OPCAO_SELECIONADA = OPT_CANCELAR;
  }
  
  private void btnGravarActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    OPCAO_SELECIONADA = OPT_GRAVAR;
  }
  
  private void btnCorrigirActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    OPCAO_SELECIONADA = OPT_CORRIGIR;
  }
}
