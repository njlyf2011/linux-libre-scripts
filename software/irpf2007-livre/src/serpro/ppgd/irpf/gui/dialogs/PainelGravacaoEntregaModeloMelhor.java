/* PainelGravacaoEntregaModeloMelhor - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.ConstantesGlobaisGUI;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.ConverterDeclaracao;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;

public class PainelGravacaoEntregaModeloMelhor extends JPanel
{
  private DeclaracaoIRPF declaracaoIRPF = null;
  private JButton btnNao;
  private JButton btnSim;
  private JEditValor edtIAPCompl;
  private JEditValor edtIAPSimpl;
  private JEditValor edtIARCompl;
  private JEditValor edtIARSimpl;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JLabel jLabel9;
  private JPanel jPanel1;
  private JPanel jPanel2;
  
  public PainelGravacaoEntregaModeloMelhor (DeclaracaoIRPF dec, boolean SimplesEhMelhor)
  {
    declaracaoIRPF = dec;
    initComponents ();
    edtIARSimpl.setInformacao (dec.getComparativo ().getImpRestituirSimplificada ());
    edtIARCompl.setInformacao (dec.getComparativo ().getImpRestituirCompleta ());
    edtIAPSimpl.setInformacao (dec.getComparativo ().getSaldoPagarSimplificada ());
    edtIAPCompl.setInformacao (dec.getComparativo ().getSaldoPagarCompleta ());
    if (! SimplesEhMelhor)
      {
	jLabel1.setText ("<HTML><B>De acordo com os dados digitados, a tributa\u00e7\u00e3o completa lhe \u00e9 mais favor\u00e1vel.</B></HTML>");
	jLabel9.setText ("<HTML><B>Deseja que o programa transfira os dados da declara\u00e7\u00e3o simplificada para a declara\u00e7\u00e3o completa?</B></HTML>");
      }
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, PgdIRPF.FONTE_DEFAULT.deriveFont (PgdIRPF.FONTE_DEFAULT.getSize2D () + 2.0F));
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jLabel2 = new JLabel ();
    jLabel5 = new JLabel ();
    jLabel6 = new JLabel ();
    jLabel4 = new JLabel ();
    jLabel3 = new JLabel ();
    jLabel7 = new JLabel ();
    edtIARCompl = new JEditValor ();
    edtIARSimpl = new JEditValor ();
    jLabel8 = new JLabel ();
    edtIAPCompl = new JEditValor ();
    edtIAPSimpl = new JEditValor ();
    jLabel9 = new JLabel ();
    jPanel2 = new JPanel ();
    jPanel1 = new JPanel ();
    btnNao = new JButton ();
    btnSim = new JButton ();
    jLabel1.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel1.setText ("<HTML><B>De acordo com os dados digitados, a tributa\u00e7\u00e3o simplificada lhe \u00e9  mais favor\u00e1vel.</B></HTML>");
    jLabel1.setVerticalAlignment (3);
    jLabel5.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel5.setHorizontalAlignment (0);
    jLabel5.setText ("<HTML><B>Resultado da Declara\u00e7\u00e3o</B></HTML>");
    jLabel4.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel4.setHorizontalAlignment (0);
    jLabel4.setText ("<HTML><B>Simplificada</B></HTML>");
    jLabel3.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel3.setHorizontalAlignment (0);
    jLabel3.setText ("<HTML><B>Completa</B></HTML>");
    jLabel7.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel7.setHorizontalAlignment (2);
    jLabel7.setText ("<HTML><B>Imposto a Restituir</B></HTML>");
    jLabel8.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel8.setHorizontalAlignment (2);
    jLabel8.setText ("<HTML><B>Imposto a Pagar</B></HTML>");
    jLabel9.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel9.setHorizontalAlignment (0);
    jLabel9.setText ("<HTML><B>Deseja que o programa transfira os dados da declara\u00e7\u00e3o completa para a declara\u00e7\u00e3o simplificada?</B></HTML>");
    jLabel9.setVerticalAlignment (3);
    btnNao.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/CANCELAR.PNG")));
    btnNao.setMnemonic ('N');
    btnNao.setText ("<HTML><B><U>N</U>\u00e3o</B></HTML>");
    btnNao.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelGravacaoEntregaModeloMelhor.this.btnNaoActionPerformed (evt);
      }
    });
    btnSim.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/OK.PNG")));
    btnSim.setMnemonic ('S');
    btnSim.setText ("<HTML><B><U>S</U>im</B></HTML>");
    btnSim.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelGravacaoEntregaModeloMelhor.this.btnSimActionPerformed (evt);
      }
    });
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().add (btnSim).addPreferredGap (0).add (btnNao).addContainerGap (-1, 32767)));
    jPanel1Layout.linkSize (new Component[] { btnNao, btnSim }, 1);
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().add (jPanel1Layout.createParallelGroup (3).add (btnSim).add (btnNao)).addContainerGap (-1, 32767)));
    jPanel1Layout.linkSize (new Component[] { btnNao, btnSim }, 2);
    jPanel2.add (jPanel1);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (2, layout.createSequentialGroup ().add (jLabel2, -1, 291, 32767).addPreferredGap (0).add (jLabel5, -2, 220, -2).addContainerGap ()).add (2, layout.createSequentialGroup ().add (layout.createParallelGroup (1).add (2, layout.createSequentialGroup ().add (jLabel6, -1, 291, 32767).addPreferredGap (0)).add (2, layout.createSequentialGroup ().add (jLabel7, -2, 291, -2).add (6, 6, 6)).add (2, layout.createSequentialGroup ().add (jLabel8, -2, 291, -2).add (6, 6, 6))).add (layout.createParallelGroup (1, false).add (edtIAPSimpl, -1, -1, 32767).add (edtIARSimpl, -1, -1, 32767).add (jLabel4, -1, 107, 32767)).addPreferredGap (0).add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (edtIAPCompl, -2, 107, -2).addContainerGap ()).add (2, layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (edtIARCompl, -2, 107, -2).addContainerGap ()).add (2, layout.createSequentialGroup ().add (jLabel3, -2, 107, -2).addContainerGap ())))).add (2, layout.createSequentialGroup ().add (jPanel2, -1, 517, 32767).addContainerGap ()).add (layout.createSequentialGroup ().add (jLabel1, -2, 517, -2).addContainerGap (-1, 32767)).add (2, layout.createSequentialGroup ().add (jLabel9, -1, 517, 32767).addContainerGap ()))));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jLabel1).addPreferredGap (0).add (layout.createParallelGroup (3).add (jLabel5, -2, 24, -2).add (jLabel2, -2, 19, -2)).addPreferredGap (0).add (layout.createParallelGroup (3).add (jLabel4, -2, 24, -2).add (jLabel3, -2, 24, -2).add (jLabel6, -2, 19, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (edtIARCompl, -2, -1, -2).add (edtIARSimpl, -2, -1, -2).add (jLabel7)).addPreferredGap (0).add (layout.createParallelGroup (2).add (edtIAPCompl, -2, -1, -2).add (edtIAPSimpl, -2, -1, -2).add (jLabel8)).addPreferredGap (0).add (jLabel9, -2, 42, -2).addPreferredGap (0).add (jPanel2, -2, -1, -2).addContainerGap (37, 32767)));
  }
  
  private void btnNaoActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
  
  private void btnSimActionPerformed (ActionEvent evt)
  {
    new ConverterDeclaracao (declaracaoIRPF).actionPerformed (evt);
    IRPFFacade.getInstancia ().salvarDeclaracao (declaracaoIRPF.getIdentificadorDeclaracao ().getCpf ().asString ());
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
}
