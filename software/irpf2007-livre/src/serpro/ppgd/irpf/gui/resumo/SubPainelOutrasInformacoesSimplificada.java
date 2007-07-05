/* SubPainelOutrasInformacoesSimplificada - Decompiled by JODE
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

public class SubPainelOutrasInformacoesSimplificada extends JPanel
{
  private JEditValor jEditValor1;
  private JEditValor jEditValor10;
  private JEditValor jEditValor12;
  private JEditValor jEditValor2;
  private JEditValor jEditValor3;
  private JEditValor jEditValor4;
  private JEditValor jEditValor6;
  private JEditValor jEditValor7;
  private JLabel jLabel1;
  private JLabel jLabel12;
  private JLabel jLabel2;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel8;
  private JLabel jLabel9;
  public JLabel lblTit2;
  
  public SubPainelOutrasInformacoesSimplificada ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "resumosimp");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT.deriveFont (1);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f.deriveFont (f.getSize2D () + 2.0F));
  }
  
  private void initComponents ()
  {
    lblTit2 = new JLabel ();
    lblTit2.setFont (lblTit2.getFont ().deriveFont (lblTit2.getFont ().getSize2D () + 3.0F));
    jLabel8 = new JLabel ();
    jEditValor6 = new JEditValor ();
    jEditValor6.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jLabel9 = new JLabel ();
    jEditValor7 = new JEditValor ();
    jEditValor7.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jLabel2 = new JLabel ();
    jEditValor1 = new JEditValor ();
    jEditValor1.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jLabel4 = new JLabel ();
    jEditValor3 = new JEditValor ();
    jEditValor3.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jLabel5 = new JLabel ();
    jEditValor4 = new JEditValor ();
    jEditValor4.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jLabel6 = new JLabel ();
    jEditValor2 = new JEditValor ();
    jEditValor2.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jLabel1 = new JLabel ();
    jEditValor12 = new JEditValor ();
    jEditValor12.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jLabel12 = new JLabel ();
    jEditValor10 = new JEditValor ();
    jEditValor10.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    lblTit2.setHorizontalAlignment (0);
    lblTit2.setText ("OUTRAS INFORMA\u00c7\u00d5ES");
    jLabel8.setText ("Rendimentos Isentos e N\u00e3o-Tribut\u00e1veis");
    jEditValor6.setInformacaoAssociada ("resumo.outrasInformacoes.rendIsentosNaoTributaveis");
    jLabel9.setText ("Rendimentos sujeitos \u00e0 tributa\u00e7\u00e3o exclusiva/definitiva");
    jEditValor7.setInformacaoAssociada ("resumo.outrasInformacoes.rendIsentosTributacaoExclusiva");
    jLabel2.setText ("Bens e Direitos em 31/12/2005");
    jEditValor1.setInformacaoAssociada ("resumo.outrasInformacoes.bensDireitosExercicioAnterior");
    jLabel4.setText ("D\u00edvidas e \u00d4nus Reais  em 31/12/2005");
    jEditValor3.setInformacaoAssociada ("resumo.outrasInformacoes.dividasOnusReaisExercicioAnterior");
    jLabel5.setText ("D\u00edvidas e \u00d4nus Reais  em 31/12/2006");
    jEditValor4.setInformacaoAssociada ("resumo.outrasInformacoes.dividasOnusReaisExercicioAtual");
    jLabel6.setText ("Bens e Direitos em 31/12/2006");
    jEditValor2.setInformacaoAssociada ("resumo.outrasInformacoes.bensDireitosExercicioAtual");
    jLabel1.setText ("Doa\u00e7\u00f5es a Part. Pol\u00edticos, Comit\u00eas Financ. e Candidatos");
    jEditValor12.setInformacaoAssociada ("resumo.outrasInformacoes.totalDoacoesCampanhasEleitorais");
    jLabel12.setText ("<HTML>Total do imposto retido na fonte (Lei n\u00ba 11.033/ 2004), conforme<BR>dados informados pelo contribuinte</HTML>");
    jEditValor10.setInformacaoAssociada ("resumo.outrasInformacoes.totalImpostoRetidoNaFonte");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (lblTit2, -1, 518, 32767).add (2, layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (layout.createParallelGroup (1).add (jLabel9, -1, 329, 32767).add (jLabel2, -1, 329, 32767).add (jLabel6, -1, 329, 32767).add (jLabel4, -1, 329, 32767).add (jLabel5, -1, 329, 32767).add (jLabel8, -1, 329, 32767)).addPreferredGap (0, 38, 32767)).add (jLabel12)).add (layout.createSequentialGroup ().add (jLabel1).addPreferredGap (0))).add (layout.createParallelGroup (2).add (jEditValor3, -1, 131, 32767).add (jEditValor2, -1, 131, 32767).add (jEditValor1, -1, 131, 32767).add (jEditValor7, -1, 131, 32767).add (jEditValor6, -1, 131, 32767).add (1, jEditValor4, -1, 131, 32767).add (jEditValor10, -1, 131, 32767).add (jEditValor12, -1, 131, 32767)).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (lblTit2, -2, 29, -2).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel8).add (jEditValor6, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel9).add (jEditValor7, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel2).add (jEditValor1, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel6).add (jEditValor2, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel4).add (jEditValor3, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel5).add (jEditValor4, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel12).add (jEditValor10, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel1).add (jEditValor12, -2, -1, -2)).addContainerGap (114, 32767)));
  }
}
