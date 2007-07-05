/* SubPainelCompleta - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.resumo;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;

public class SubPainelCompleta extends JPanel
{
  private JEditValor jEditValor1;
  private JEditValor jEditValor10;
  private JEditValor jEditValor11;
  private JEditValor jEditValor12;
  private JEditValor jEditValor13;
  private JEditValor jEditValor2;
  private JEditValor jEditValor3;
  private JEditValor jEditValor4;
  private JEditValor jEditValor5;
  private JEditValor jEditValor6;
  private JEditValor jEditValor7;
  private JEditValor jEditValor8;
  private JEditValor jEditValor9;
  private JLabel jLabel1;
  private JLabel jLabel10;
  private JLabel jLabel11;
  private JLabel jLabel12;
  private JLabel jLabel13;
  private JLabel jLabel16;
  private JLabel jLabel17;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JLabel jLabel9;
  
  public SubPainelCompleta ()
  {
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (f.getSize2D () + 1.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jLabel2 = new JLabel ();
    jEditValor1 = new JEditValor ();
    jLabel3 = new JLabel ();
    jEditValor3 = new JEditValor ();
    jLabel4 = new JLabel ();
    jEditValor4 = new JEditValor ();
    jLabel6 = new JLabel ();
    jEditValor5 = new JEditValor ();
    jLabel7 = new JLabel ();
    jLabel8 = new JLabel ();
    jEditValor6 = new JEditValor ();
    jLabel10 = new JLabel ();
    jEditValor7 = new JEditValor ();
    jLabel11 = new JLabel ();
    jEditValor8 = new JEditValor ();
    jLabel12 = new JLabel ();
    jEditValor9 = new JEditValor ();
    jLabel13 = new JLabel ();
    jEditValor10 = new JEditValor ();
    jLabel16 = new JLabel ();
    jEditValor11 = new JEditValor ();
    jLabel17 = new JLabel ();
    jEditValor12 = new JEditValor ();
    jLabel5 = new JLabel ();
    jEditValor2 = new JEditValor ();
    jLabel9 = new JLabel ();
    jEditValor13 = new JEditValor ();
    jLabel1.setHorizontalAlignment (0);
    jLabel1.setText ("<HTML><B>IMPOSTO DEVIDO</B></HTML>");
    jLabel2.setText ("Base de c\u00e1lculo");
    jEditValor1.setInformacaoAssociada ("resumo.calculoImposto.baseCalculo");
    jLabel3.setText ("Imposto");
    jEditValor3.setInformacaoAssociada ("resumo.calculoImposto.imposto");
    jLabel4.setText ("Dedu\u00e7\u00e3o de incentivo");
    jEditValor4.setInformacaoAssociada ("resumo.calculoImposto.deducaoIncentivo");
    jLabel6.setText ("Imposto devido I");
    jEditValor5.setInformacaoAssociada ("resumo.calculoImposto.impostoDevido");
    jLabel7.setHorizontalAlignment (0);
    jLabel7.setText ("<HTML><B>IMPOSTO PAGO</B></HTML>");
    jLabel7.setVerticalAlignment (3);
    jLabel8.setText ("<html><p>Imposto retido na fonte do titular</p></html>");
    jEditValor6.setInformacaoAssociada ("resumo.calculoImposto.impostoRetidoFonteTitular");
    jLabel10.setText ("<html><p>Imposto retido na fonte dos dependentes</p></html>");
    jEditValor7.setInformacaoAssociada ("resumo.calculoImposto.impostoRetidoFonteDependentes");
    jLabel11.setText ("Carn\u00ea-le\u00e3o");
    jEditValor8.setInformacaoAssociada ("resumo.calculoImposto.carneLeao");
    jLabel12.setText ("Imposto complementar");
    jEditValor9.setInformacaoAssociada ("resumo.calculoImposto.impostoComplementar");
    jLabel13.setText ("Imposto pago no exterior");
    jEditValor10.setInformacaoAssociada ("resumo.calculoImposto.impostoPagoExterior");
    jLabel16.setText ("<html><p>IR fonte (Lei n\u00b0 11.033/2004)</p></html>");
    jEditValor11.setInformacaoAssociada ("resumo.calculoImposto.impostoRetidoFonteLei11033");
    jLabel17.setHorizontalAlignment (0);
    jLabel17.setText ("TOTAL");
    jEditValor12.setInformacaoAssociada ("resumo.calculoImposto.totalImpostoPago");
    jLabel5.setText ("<html>Contribui\u00e7\u00e3o Prev. emp. Dom\u00e9stico</html>");
    jEditValor2.setInformacaoAssociada ("resumo.calculoImposto.totalContribEmpregadoDomestico");
    jLabel9.setText ("Imposto devido II");
    jEditValor13.setInformacaoAssociada ("resumo.calculoImposto.impostoDevidoII");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jLabel2, -1, 198, 32767).add (jLabel3, -1, 198, 32767).add (jLabel4, -1, 198, 32767).add (jLabel6, -1, 198, 32767).add (jLabel5, -1, 198, 32767).add (jLabel9, -2, 198, -2)).addPreferredGap (0).add (layout.createParallelGroup (1, false).add (2, jEditValor13, -2, 123, -2).add (2, jEditValor2, -2, 123, -2).add (2, jEditValor5, -2, 123, -2).add (2, jEditValor4, -2, 123, -2).add (2, jEditValor3, -2, 123, -2).add (2, jEditValor1, -2, 123, -2)).addContainerGap ()).add (2, layout.createSequentialGroup ().addContainerGap ().add (jLabel7, -1, 325, 32767).addContainerGap ()).add (jLabel1, -1, 345, 32767).add (2, layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (2, layout.createSequentialGroup ().add (layout.createParallelGroup (2).add (jLabel8, -1, 142, 32767).add (jLabel10, -1, 142, 32767).add (jLabel11, -1, 142, 32767).add (jLabel16, -1, 142, 32767)).add (46, 46, 46)).add (2, layout.createSequentialGroup ().add (layout.createParallelGroup (2).add (jLabel13, -1, 188, 32767).add (jLabel12, -1, 188, 32767)).addPreferredGap (0)).add (layout.createSequentialGroup ().add (jLabel17, -1, 188, 32767).addPreferredGap (0))).add (14, 14, 14).add (layout.createParallelGroup (2, false).add (jEditValor10, -1, -1, 32767).add (jEditValor12, -1, -1, 32767).add (jEditValor9, -1, -1, 32767).add (jEditValor11, -1, -1, 32767).add (jEditValor8, -1, -1, 32767).add (jEditValor7, -1, -1, 32767).add (jEditValor6, -1, 123, 32767)).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jLabel1).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel2, -2, 20, -2).add (jEditValor1, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel3, -2, 20, -2).add (jEditValor3, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel4, -2, 20, -2).add (jEditValor4, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel6, -2, 20, -2).add (jEditValor5, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel5, -2, 25, -2).add (jEditValor2, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel9, -2, 20, -2).add (jEditValor13, -2, -1, -2)).addPreferredGap (0).add (jLabel7, -2, 24, -2).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel8).add (jEditValor6, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel10, -2, 26, -2).add (jEditValor7, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel11, -2, 20, -2).add (jEditValor8, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel16).add (jEditValor11, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel12, -2, 20, -2).add (jEditValor9, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel13).add (jEditValor10, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel17).add (jEditValor12, -2, -1, -2)).addContainerGap (-1, 32767)));
  }
}
