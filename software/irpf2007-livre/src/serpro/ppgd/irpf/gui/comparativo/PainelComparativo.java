/* PainelComparativo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.comparativo;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.ConverterDeclaracao;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;

public class PainelComparativo extends JPanel implements PainelIRPFIf
{
  private JButton jButton1;
  private JEditValor jEditValor1;
  private JEditValor jEditValor10;
  private JEditValor jEditValor11;
  private JEditValor jEditValor12;
  private JEditValor jEditValor13;
  private JEditValor jEditValor2;
  private JEditValor jEditValor3;
  private JEditValor jEditValor4;
  private JLabel jLabel11;
  private JLabel jLabel12;
  private JLabel jLabel13;
  private JLabel jLabel14;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JPanel jPanel1;
  private JPanel jPanel2;
  private JPanel jPanel3;
  
  public PainelComparativo ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "Comparativo");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (1);
    f = f.deriveFont (f.getSize2D () + 2.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
    jButton1.setFont (f);
    jButton1.setForeground (new Color (0, 0, 128));
    atualizaBotaoOpcao ();
    jButton1.addActionListener (new ConverterDeclaracao (IRPFFacade.getInstancia ().getDeclaracao ()));
  }
  
  public void atualizaBotaoOpcao ()
  {
    if (IRPFFacade.getInstancia ().getIdDeclaracaoAberto ().getTipoDeclaracao ().asString ().equals ("1"))
      jButton1.setText ("Cancelar op\u00e7\u00e3o pela declara\u00e7\u00e3o simplificada");
    else
      jButton1.setText ("Op\u00e7\u00e3o pela declara\u00e7\u00e3o simplificada");
  }
  
  private void initComponents ()
  {
    jPanel1 = new JPanel ();
    jLabel5 = new JLabel ();
    jEditValor1 = new JEditValor ();
    jLabel4 = new JLabel ();
    jEditValor4 = new JEditValor ();
    jEditValor3 = new JEditValor ();
    jLabel3 = new JLabel ();
    jEditValor2 = new JEditValor ();
    jLabel2 = new JLabel ();
    jPanel2 = new JPanel ();
    jLabel11 = new JLabel ();
    jEditValor10 = new JEditValor ();
    jLabel12 = new JLabel ();
    jEditValor11 = new JEditValor ();
    jLabel13 = new JLabel ();
    jEditValor12 = new JEditValor ();
    jLabel14 = new JLabel ();
    jEditValor13 = new JEditValor ();
    jPanel3 = new JPanel ();
    jButton1 = new JButton ();
    jPanel1.setBorder (BorderFactory.createTitledBorder ("C\u00e1lculo do imposto (DECLARA\u00c7\u00c3O COMPLETA)"));
    jLabel5.setText ("IMPOSTO A RESTITUIR");
    jEditValor1.setInformacaoAssociada ("comparativo.totalRendTribCompleta");
    jLabel4.setText ("SALDO DO IMPOSTO A PAGAR");
    jEditValor4.setInformacaoAssociada ("comparativo.impRestituirCompleta");
    jEditValor3.setInformacaoAssociada ("comparativo.saldoPagarCompleta");
    jLabel3.setText ("Base de c\u00e1lculo");
    jEditValor2.setInformacaoAssociada ("comparativo.baseCalcCompleta");
    jLabel2.setText ("Total dos rendimentos tribut\u00e1veis");
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (jLabel2).add (jLabel3).add (jLabel4).add (jLabel5)).addPreferredGap (0, 137, 32767).add (jPanel1Layout.createParallelGroup (1, false).add (jEditValor4, -1, -1, 32767).add (jEditValor2, -1, -1, 32767).add (jEditValor1, -1, 123, 32767).add (jEditValor3, -1, -1, 32767)).addContainerGap ()));
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().add (jPanel1Layout.createParallelGroup (2).add (jEditValor1, -2, -1, -2).add (jLabel2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jEditValor2, -2, -1, -2).add (jLabel3)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jLabel4).add (jEditValor3, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jLabel5).add (jEditValor4, -2, -1, -2)).addContainerGap (-1, 32767)));
    jPanel2.setBorder (BorderFactory.createTitledBorder ("C\u00e1lculo do imposto (DECLARA\u00c7\u00c3O SIMPLIFICADA)"));
    jLabel11.setText ("Total dos rendimentos tribut\u00e1veis");
    jEditValor10.setInformacaoAssociada ("comparativo.totalRendTribSimplificada");
    jLabel12.setText ("Base de c\u00e1lculo");
    jEditValor11.setInformacaoAssociada ("comparativo.baseCalcSimplificada");
    jLabel13.setText ("SALDO DO IMPOSTO A PAGAR");
    jEditValor12.setInformacaoAssociada ("comparativo.saldoPagarSimplificada");
    jLabel14.setText ("IMPOSTO A RESTITUIR");
    jEditValor13.setInformacaoAssociada ("comparativo.impRestituirSimplificada");
    GroupLayout jPanel2Layout = new GroupLayout (jPanel2);
    jPanel2.setLayout (jPanel2Layout);
    jPanel2Layout.setHorizontalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().addContainerGap ().add (jPanel2Layout.createParallelGroup (1).add (jLabel11).add (jLabel12).add (jLabel13).add (jLabel14)).addPreferredGap (0, 137, 32767).add (jPanel2Layout.createParallelGroup (1, false).add (jEditValor13, -1, -1, 32767).add (jEditValor12, -1, -1, 32767).add (jEditValor11, -1, -1, 32767).add (jEditValor10, -1, 123, 32767)).addContainerGap ()));
    jPanel2Layout.setVerticalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().addContainerGap ().add (jPanel2Layout.createParallelGroup (2).add (jEditValor10, -2, -1, -2).add (jLabel11)).addPreferredGap (0).add (jPanel2Layout.createParallelGroup (2).add (jLabel12).add (jEditValor11, -2, -1, -2)).addPreferredGap (0).add (jPanel2Layout.createParallelGroup (2).add (jLabel13).add (jEditValor12, -2, -1, -2)).addPreferredGap (0).add (jPanel2Layout.createParallelGroup (2).add (jLabel14).add (jEditValor13, -2, -1, -2)).addContainerGap (-1, 32767)));
    jButton1.setText ("Op\u00e7\u00e3o pela declara\u00e7\u00e3o simplificada");
    jPanel3.add (jButton1);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (2, jPanel3, -1, 499, 32767).add (2, layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (2).add (1, jPanel1, -1, -1, 32767).add (1, jPanel2, -1, -1, 32767)).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jPanel1, -2, -1, -2).add (18, 18, 18).add (jPanel2, -2, -1, -2).addPreferredGap (0).add (jPanel3, -2, -1, -2).addContainerGap (-1, 32767)));
  }
  
  public String getTituloPainel ()
  {
    return "Comparativo";
  }
  
  public void vaiExibir ()
  {
    atualizaBotaoOpcao ();
  }
  
  public void painelExibido ()
  {
    /* empty */
  }
}
