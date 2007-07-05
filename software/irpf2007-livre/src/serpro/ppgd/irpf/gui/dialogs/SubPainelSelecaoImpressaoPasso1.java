/* SubPainelSelecaoImpressaoPasso1 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.Color;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.gui.ConstantesGlobaisGUI;

public class SubPainelSelecaoImpressaoPasso1 extends JPanel
{
  private JLabel jLabel1;
  public JRadioButton rdPartes;
  public JRadioButton rdTodaDeclaracao;
  
  public SubPainelSelecaoImpressaoPasso1 ()
  {
    TitledBorder borda = BorderFactory.createTitledBorder ("SELE\u00c7\u00c3O DE IMPRESS\u00c3O");
    borda.setTitleColor (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    borda.setTitleFont (borda.getTitleFont ().deriveFont (1));
    setBorder (borda);
    initComponents ();
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    rdTodaDeclaracao = new JRadioButton ();
    rdPartes = new JRadioButton ();
    jLabel1.setForeground (new Color (0, 0, 128));
    jLabel1.setText ("<HTML><B>Selecione o que voc\u00ea deseja imprimir:</B></HTML>");
    rdTodaDeclaracao.setText ("<HTML><B>Toda a declara\u00e7\u00e3o</B></HTML>");
    rdTodaDeclaracao.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    rdTodaDeclaracao.setMargin (new Insets (0, 0, 0, 0));
    rdPartes.setText ("<HTML><B>Partes da declara\u00e7\u00e3o</B></HTML>");
    rdPartes.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    rdPartes.setMargin (new Insets (0, 0, 0, 0));
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (rdTodaDeclaracao).add (rdPartes).add (jLabel1, -2, 238, -2)).addContainerGap (21, 32767)));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel1).addPreferredGap (0).add (rdTodaDeclaracao).addPreferredGap (0).add (rdPartes).addContainerGap (98, 32767)));
  }
}
