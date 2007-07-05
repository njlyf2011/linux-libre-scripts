/* SubPainelOutrasInformacoesCompleta - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.resumo;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;

public class SubPainelOutrasInformacoesCompleta extends JPanel
{
  private JEditValor jEditValor1;
  private JEditValor jEditValor10;
  private JEditValor jEditValor11;
  private JEditValor jEditValor12;
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
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel8;
  private JLabel jLabel9;
  private JPanel jPanel1;
  private JPanel jPanel2;
  public JLabel lblTit1;
  public JLabel lblTit2;
  
  public SubPainelOutrasInformacoesCompleta ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "OutrasInforma\u00e7\u00f5es");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT.deriveFont (1);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f.deriveFont (f.getSize2D () + 2.0F));
  }
  
  private void initComponents ()
  {
    jPanel1 = new JPanel ();
    lblTit1 = new JLabel ();
    lblTit1.setFont (lblTit1.getFont ().deriveFont (lblTit1.getFont ().getSize2D () + 3.0F));
    jLabel2 = new JLabel ();
    jLabel3 = new JLabel ();
    jLabel4 = new JLabel ();
    jLabel5 = new JLabel ();
    jLabel6 = new JLabel ();
    jPanel2 = new JPanel ();
    lblTit2 = new JLabel ();
    lblTit2.setFont (lblTit2.getFont ().deriveFont (lblTit2.getFont ().getSize2D () + 3.0F));
    jEditValor1 = new JEditValor ();
    jEditValor1.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jEditValor2 = new JEditValor ();
    jEditValor2.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jEditValor3 = new JEditValor ();
    jEditValor3.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jEditValor4 = new JEditValor ();
    jEditValor4.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jEditValor5 = new JEditValor ();
    jEditValor5.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jLabel8 = new JLabel ();
    jLabel9 = new JLabel ();
    jLabel10 = new JLabel ();
    jLabel11 = new JLabel ();
    jLabel12 = new JLabel ();
    jLabel13 = new JLabel ();
    jEditValor6 = new JEditValor ();
    jEditValor6.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jEditValor7 = new JEditValor ();
    jEditValor7.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jEditValor8 = new JEditValor ();
    jEditValor8.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jEditValor9 = new JEditValor ();
    jEditValor9.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jEditValor10 = new JEditValor ();
    jEditValor10.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jEditValor11 = new JEditValor ();
    jEditValor11.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jLabel1 = new JLabel ();
    jEditValor12 = new JEditValor ();
    jEditValor12.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    lblTit1.setText ("EVOLU\u00c7\u00c3O PATRIMONIAL");
    jPanel1.add (lblTit1);
    jLabel2.setText ("Bens e Direitos em 31/12/2005");
    jLabel3.setText ("Bens e Direitos em 31/12/2006");
    jLabel4.setText ("D\u00edvidas e \u00d4nus Reais  em 31/12/2005");
    jLabel5.setText ("D\u00edvidas e \u00d4nus Reais  em 31/12/2006");
    jLabel6.setText ("Informa\u00e7\u00f5es do c\u00f4njuge");
    lblTit2.setText ("OUTRAS INFORMA\u00c7\u00d5ES");
    jPanel2.add (lblTit2);
    jEditValor1.setInformacaoAssociada ("resumo.outrasInformacoes.bensDireitosExercicioAnterior");
    jEditValor2.setInformacaoAssociada ("resumo.outrasInformacoes.bensDireitosExercicioAtual");
    jEditValor3.setInformacaoAssociada ("resumo.outrasInformacoes.dividasOnusReaisExercicioAnterior");
    jEditValor4.setInformacaoAssociada ("resumo.outrasInformacoes.dividasOnusReaisExercicioAtual");
    jEditValor5.setInformacaoAssociada ("resumo.outrasInformacoes.informacoesConjuge");
    jLabel8.setText ("Rendimentos Isentos e N\u00e3o-Tribut\u00e1veis");
    jLabel9.setText ("Rendimentos sujeitos \u00e0 tributa\u00e7\u00e3o exclusiva/definitiva");
    jLabel10.setText ("Imposto Pago sobre Ganhos de Capital");
    jLabel11.setText ("Imposto Pago Moeda Estrangeira - bens, dir. e aplic. fin.");
    jLabel12.setText ("<HTML>Total do imposto retido na fonte (Lei n\u00ba 11.033/ 2004), conforme<BR>dados informados pelo contribuinte</HTML>");
    jLabel13.setText ("Imposto Pago sobre Renda Vari\u00e1vel");
    jEditValor6.setInformacaoAssociada ("resumo.outrasInformacoes.rendIsentosNaoTributaveis");
    jEditValor7.setInformacaoAssociada ("resumo.outrasInformacoes.rendIsentosTributacaoExclusiva");
    jEditValor8.setInformacaoAssociada ("resumo.outrasInformacoes.impostoPagoGCAP");
    jEditValor9.setInformacaoAssociada ("resumo.outrasInformacoes.impostoPagoME");
    jEditValor10.setInformacaoAssociada ("resumo.outrasInformacoes.totalImpostoRetidoNaFonte");
    jEditValor11.setInformacaoAssociada ("resumo.outrasInformacoes.impostoPagoSobreRendaVariavel");
    jLabel1.setText ("Doa\u00e7\u00f5es a Part. Pol\u00edticos, Comit\u00eas Financ. e Candidatos");
    jEditValor12.setInformacaoAssociada ("resumo.outrasInformacoes.totalDoacoesCampanhasEleitorais");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (layout.createParallelGroup (1).add (jLabel8).add (jLabel9).add (jLabel10).add (jLabel11).add (jLabel12).add (jLabel13).add (jLabel1)).addPreferredGap (0, 50, 32767).add (layout.createParallelGroup (2, false).add (1, jEditValor11, -1, -1, 32767).add (1, jEditValor10, -1, -1, 32767).add (1, jEditValor9, -1, -1, 32767).add (1, jEditValor8, -1, -1, 32767).add (1, jEditValor7, -1, -1, 32767).add (1, jEditValor12, -1, -1, 32767).add (jEditValor6, -2, 125, -2))).add (layout.createSequentialGroup ().add (layout.createParallelGroup (1).add (jLabel2).add (jLabel3).add (jLabel4).add (jLabel5).add (jLabel6)).addPreferredGap (0, 182, 32767).add (layout.createParallelGroup (2, false).add (jEditValor5, -1, -1, 32767).add (jEditValor4, -1, -1, 32767).add (jEditValor3, -1, -1, 32767).add (jEditValor2, -1, -1, 32767).add (jEditValor1, -2, 126, -2)))).addContainerGap ()).add (jPanel1, -1, 507, 32767).add (jPanel2, -1, 507, 32767));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jPanel1, -2, -1, -2).add (12, 12, 12).add (layout.createParallelGroup (2).add (jLabel2).add (jEditValor1, -2, -1, -2)).add (6, 6, 6).add (layout.createParallelGroup (1).add (2, jEditValor2, -2, -1, -2).add (2, jLabel3)).add (6, 6, 6).add (layout.createParallelGroup (1).add (2, jEditValor3, -2, -1, -2).add (2, jLabel4)).add (6, 6, 6).add (layout.createParallelGroup (1).add (2, jEditValor4, -2, -1, -2).add (2, jLabel5)).add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (12, 12, 12).add (jLabel6)).add (layout.createSequentialGroup ().add (6, 6, 6).add (jEditValor5, -2, -1, -2))).addPreferredGap (0).add (jPanel2, -2, -1, -2).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel8).add (jEditValor6, -2, -1, -2)).add (6, 6, 6).add (layout.createParallelGroup (1).add (2, jLabel9).add (2, jEditValor7, -2, -1, -2)).add (6, 6, 6).add (layout.createParallelGroup (1).add (2, jEditValor8, -2, -1, -2).add (2, jLabel10)).add (6, 6, 6).add (layout.createParallelGroup (1).add (2, jEditValor9, -2, -1, -2).add (2, jLabel11)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jEditValor10, -2, -1, -2).add (jLabel12)).add (6, 6, 6).add (layout.createParallelGroup (1).add (2, jEditValor11, -2, -1, -2).add (2, jLabel13)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jEditValor12, -2, -1, -2).add (jLabel1)).addContainerGap (51, 32767)));
  }
}
