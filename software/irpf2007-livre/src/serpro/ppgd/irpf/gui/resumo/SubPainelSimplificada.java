/* SubPainelSimplificada - Decompiled by JODE
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

public class SubPainelSimplificada extends JPanel
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
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JLabel jLabel9;
  
  public SubPainelSimplificada ()
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
    jEditValor2 = new JEditValor ();
    jLabel4 = new JLabel ();
    jEditValor3 = new JEditValor ();
    jLabel5 = new JLabel ();
    jEditValor4 = new JEditValor ();
    jLabel6 = new JLabel ();
    jEditValor5 = new JEditValor ();
    jLabel7 = new JLabel ();
    jEditValor6 = new JEditValor ();
    jLabel8 = new JLabel ();
    jEditValor7 = new JEditValor ();
    jLabel9 = new JLabel ();
    jEditValor8 = new JEditValor ();
    jLabel10 = new JLabel ();
    jEditValor9 = new JEditValor ();
    jLabel11 = new JLabel ();
    jEditValor10 = new JEditValor ();
    jLabel12 = new JLabel ();
    jEditValor11 = new JEditValor ();
    jLabel13 = new JLabel ();
    jEditValor12 = new JEditValor ();
    jLabel16 = new JLabel ();
    jEditValor13 = new JEditValor ();
    jLabel1.setHorizontalAlignment (0);
    jLabel1.setText ("<HTML><B>RENDIMENTOS TRIBUT\u00c1VEIS E DESCONTO SIMPLIFICADO</B></HTML>");
    jLabel2.setText ("<HTML><P>Rendimentos Recebidos de PJ pelo Titular</P></HTML>");
    jEditValor1.setInformacaoAssociada ("resumo.calculoImposto.rendPJRecebidoTitular");
    jLabel3.setText ("<HTML><P>Rendimentos Recebidos de PJ pelos Dependentes</P></HTML>");
    jEditValor2.setInformacaoAssociada ("resumo.calculoImposto.rendPJRecebidoDependentes");
    jLabel4.setText ("<HTML><P>Rendimentos Recebidos de Pessoas F\u00edsicas</P></HTML>");
    jEditValor3.setInformacaoAssociada ("resumo.calculoImposto.rendRecebidoPF");
    jLabel5.setText ("<HTML><P>Rendimentos Recebidos do Exterior</P></HTML>");
    jEditValor4.setInformacaoAssociada ("resumo.calculoImposto.rendRecebidoExterior");
    jLabel6.setText ("<HTML><P>Resultado Tribut\u00e1vel da Atividade Rural</P></HTML>");
    jEditValor5.setInformacaoAssociada ("resumo.calculoImposto.resultadoTributavelAR");
    jLabel7.setText ("Total dos Rendimentos Tribut\u00e1veis");
    jEditValor6.setInformacaoAssociada ("resumo.calculoImposto.totalResultadosTributaveis");
    jLabel8.setText ("Desconto Simplificado");
    jEditValor7.setInformacaoAssociada ("resumo.calculoImposto.descontoSimplificado");
    jLabel9.setText ("Base de C\u00e1lculo do Imposto");
    jEditValor8.setInformacaoAssociada ("resumo.calculoImposto.baseCalculo");
    jLabel10.setText ("Imposto Devido");
    jEditValor9.setInformacaoAssociada ("resumo.calculoImposto.impostoDevido");
    jLabel11.setText ("<HTML><P>Imposto Retido na Fonte do Titular</P></HTML>");
    jEditValor10.setInformacaoAssociada ("resumo.calculoImposto.impostoRetidoFonteTitular");
    jLabel12.setText ("<HTML><P>Imposto Retido na Fonte dos Dependentes</P></HTML>");
    jEditValor11.setInformacaoAssociada ("resumo.calculoImposto.impostoRetidoFonteDependentes");
    jLabel13.setText ("<HTML><P>Carn\u00ea-Le\u00e3o e Imposto Complementar</P></HTML>");
    jEditValor12.setInformacaoAssociada ("resumo.calculoImposto.carneLeaoMaisImpostoComplementar");
    jLabel16.setText ("<html><p>IR fonte (Lei n\u00b0 11.033/2004)</p></html>");
    jEditValor13.setInformacaoAssociada ("resumo.calculoImposto.impostoRetidoFonteLei11033");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jLabel13, -1, 189, 32767).add (jLabel6, -1, 189, 32767).add (jLabel12, -1, 189, 32767).add (jLabel11, -1, 189, 32767).add (jLabel10, -1, 189, 32767).add (jLabel9, -1, 189, 32767).add (jLabel8, -1, 189, 32767).add (jLabel7, -1, 189, 32767).add (jLabel5, -1, 189, 32767).add (jLabel4, -1, 189, 32767).add (jLabel3, -1, 189, 32767).add (jLabel2, -2, 188, -2).add (2, jLabel16, -1, 189, 32767)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jEditValor13, -1, 140, 32767).add (jEditValor12, -1, 140, 32767).add (jEditValor11, -1, 140, 32767).add (jEditValor10, -1, 140, 32767).add (jEditValor9, -1, 140, 32767).add (jEditValor8, -1, 140, 32767).add (jEditValor7, -1, 140, 32767).add (jEditValor6, -1, 140, 32767).add (jEditValor5, -1, 140, 32767).add (jEditValor4, -1, 140, 32767).add (jEditValor3, -1, 140, 32767).add (jEditValor2, -1, 140, 32767).add (jEditValor1, -1, 140, 32767)).addContainerGap ()).add (jLabel1, -1, 353, 32767));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jLabel1, -2, 24, -2).add (12, 12, 12).add (layout.createParallelGroup (1).add (jEditValor1, -2, -1, -2).add (jLabel2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jEditValor2, -2, -1, -2).add (jLabel3, -2, 31, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jEditValor3, -2, -1, -2).add (jLabel4)).addPreferredGap (0).add (layout.createParallelGroup (2, false).add (jLabel5).add (jEditValor4, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jEditValor5, -2, -1, -2).add (jLabel6)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel7, -2, 20, -2).add (jEditValor6, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel8, -2, 20, -2).add (1, jEditValor7, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel9, -2, 20, -2).add (1, jEditValor8, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel10, -2, 20, -2).add (1, jEditValor9, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel11, -2, 20, -2).add (1, jEditValor10, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jEditValor11, -2, -1, -2).add (jLabel12)).add (8, 8, 8).add (layout.createParallelGroup (1).add (jEditValor12, -2, -1, -2).add (jLabel13)).addPreferredGap (0).add (layout.createParallelGroup (1).add (2, jEditValor13, -2, -1, -2).add (2, jLabel16, -2, 20, -2)).addContainerGap (-1, 32767)));
  }
}
