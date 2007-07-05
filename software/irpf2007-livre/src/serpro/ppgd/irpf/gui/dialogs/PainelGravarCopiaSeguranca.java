/* PainelGravarCopiaSeguranca - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.ConstantesGlobaisGUI;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;

public class PainelGravarCopiaSeguranca extends JPanel
{
  private JButton btnCancelar;
  private JButton btnOk;
  private ButtonGroup buttonGroup1;
  private JLabel jLabel1;
  private JPanel jPanel1;
  private JPanel pnlOpt;
  private JRadioButton rdCopiaDadosAtuais;
  private JRadioButton rdCopiaTransmitida;
  
  public PainelGravarCopiaSeguranca ()
  {
    initComponents ();
    TitledBorder borda = BorderFactory.createTitledBorder ("Selecione a op\u00e7\u00e3o:");
    Font f = PgdIRPF.FONTE_DEFAULT.deriveFont (PgdIRPF.FONTE_DEFAULT.getSize2D () + 2.0F);
    f = f.deriveFont (1);
    borda.setTitleFont (f);
    pnlOpt.setBorder (borda);
  }
  
  private void initComponents ()
  {
    buttonGroup1 = new ButtonGroup ();
    jLabel1 = new JLabel ();
    pnlOpt = new JPanel ();
    rdCopiaDadosAtuais = new JRadioButton ();
    rdCopiaTransmitida = new JRadioButton ();
    jPanel1 = new JPanel ();
    btnOk = new JButton ();
    btnCancelar = new JButton ();
    jLabel1.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel1.setText ("<html> A fun\u00e7\u00e3o <b>\"C\u00f3pia da declara\u00e7\u00e3o com os dados atuais\"</b> gera um arquivo, com os campos preenchidos at\u00e9 o momento. Mesmo que a declara\u00e7\u00e3o tenha sido entregue, essa op\u00e7\u00e3o n\u00e3o grava os dados do recibo.<br>A fun\u00e7\u00e3o <b>\"C\u00f3pia da \u00faltima declara\u00e7\u00e3o\"</b> transmitida e do recibo de entrega gera um arquivo com os dados da declara\u00e7\u00e3o entregue e o respectivo recibo, ainda que a declara\u00e7\u00e3o tenha sido posteriormente modificada.<br>O arquivo gravado pela op\u00e7\u00e3o <b>\"C\u00f3pia da declara\u00e7\u00e3o com os dados atuais\"</b> n\u00e3o pode ser utilizado para entrega \u00e0 Receita Federal.</html>");
    jLabel1.setVerticalAlignment (1);
    buttonGroup1.add (rdCopiaDadosAtuais);
    rdCopiaDadosAtuais.setSelected (true);
    rdCopiaDadosAtuais.setText ("<HTML><B>C\u00f3pia da declara\u00e7\u00e3o com os dados atuais</B></HTML>");
    rdCopiaDadosAtuais.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    rdCopiaDadosAtuais.setMargin (new Insets (0, 0, 0, 0));
    buttonGroup1.add (rdCopiaTransmitida);
    rdCopiaTransmitida.setText ("<HTML><B>C\u00f3pia da \u00faltima declara\u00e7\u00e3o transmitida e do recibo de entrega</B></HTML>");
    rdCopiaTransmitida.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    rdCopiaTransmitida.setMargin (new Insets (0, 0, 0, 0));
    GroupLayout pnlOptLayout = new GroupLayout (pnlOpt);
    pnlOpt.setLayout (pnlOptLayout);
    pnlOptLayout.setHorizontalGroup (pnlOptLayout.createParallelGroup (1).add (pnlOptLayout.createSequentialGroup ().addContainerGap ().add (pnlOptLayout.createParallelGroup (1).add (rdCopiaDadosAtuais, -1, 466, 32767).add (rdCopiaTransmitida)).addContainerGap ()));
    pnlOptLayout.setVerticalGroup (pnlOptLayout.createParallelGroup (1).add (pnlOptLayout.createSequentialGroup ().addContainerGap ().add (rdCopiaDadosAtuais).addPreferredGap (0).add (rdCopiaTransmitida).addContainerGap (-1, 32767)));
    btnOk.setText ("OK");
    btnOk.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelGravarCopiaSeguranca.this.btnOkActionPerformed (evt);
      }
    });
    btnCancelar.setText ("Cancelar");
    btnCancelar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelGravarCopiaSeguranca.this.btnCancelarActionPerformed (evt);
      }
    });
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (btnOk).addPreferredGap (0).add (btnCancelar).addContainerGap (320, 32767)));
    jPanel1Layout.linkSize (new Component[] { btnCancelar, btnOk }, 1);
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (3).add (btnOk).add (btnCancelar)).addContainerGap (-1, 32767)));
    jPanel1Layout.linkSize (new Component[] { btnCancelar, btnOk }, 2);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (2, layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (2).add (1, jPanel1, -1, -1, 32767).add (1, pnlOpt, -1, -1, 32767).add (1, jLabel1, -1, 486, 32767)).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel1, -2, 150, -2).addPreferredGap (0).add (pnlOpt, -2, -1, -2).addPreferredGap (0).add (jPanel1, -2, -1, -2).addContainerGap (-1, 32767)));
  }
  
  private void btnCancelarActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
  
  private void btnOkActionPerformed (ActionEvent evt)
  {
    JPanel pnlCopiaSelecionada;
    if (rdCopiaDadosAtuais.isSelected ())
      pnlCopiaSelecionada = new PainelGravarCopiaSegurancaPasso2 ();
    else
      pnlCopiaSelecionada = new PainelGravarCopiaDeclaracaoTransmitida ();
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    IRPFGuiUtil.exibeDialog (pnlCopiaSelecionada, true, "Grava\u00e7\u00e3o de C\u00f3pia de Seguran\u00e7a", false);
  }
}
