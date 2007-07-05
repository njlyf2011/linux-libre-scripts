/* PainelRendimentosTributaveisDeducoes - Decompiled by JODE
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
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;

public class PainelRendimentosTributaveisDeducoes extends JPanel implements PainelIRPFIf
{
  private static final String TITULO = "Rendimentos Tribut\u00e1veis e Dedu\u00e7\u00f5es";
  public JEditValor jEditValor1;
  private JEditValor jEditValor10;
  private JEditValor jEditValor11;
  private JEditValor jEditValor12;
  private JEditValor jEditValor13;
  private JEditValor jEditValor14;
  private JEditValor jEditValor15;
  public JEditValor jEditValor2;
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
  private JLabel jLabel14;
  private JLabel jLabel15;
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
  private JPanel jPanel1;
  private JPanel jPanel2;
  
  public String getTituloPainel ()
  {
    return "Rendimentos Tribut\u00e1veis e Dedu\u00e7\u00f5es";
  }
  
  public PainelRendimentosTributaveisDeducoes ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "ResumoRendimentos");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT.deriveFont (1);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f.deriveFont (f.getSize2D () + 2.0F));
  }
  
  private void initComponents ()
  {
    jPanel1 = new JPanel ();
    jLabel1 = new JLabel ();
    jPanel2 = new JPanel ();
    jLabel2 = new JLabel ();
    jLabel3 = new JLabel ();
    jEditValor1 = new JEditValor ();
    jEditValor1.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jLabel4 = new JLabel ();
    jEditValor2 = new JEditValor ();
    jEditValor2.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jLabel5 = new JLabel ();
    jLabel6 = new JLabel ();
    jEditValor3 = new JEditValor ();
    jEditValor3.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jEditValor4 = new JEditValor ();
    jEditValor4.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jLabel7 = new JLabel ();
    jEditValor5 = new JEditValor ();
    jEditValor5.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jLabel8 = new JLabel ();
    jEditValor6 = new JEditValor ();
    jEditValor6.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jLabel9 = new JLabel ();
    jEditValor7 = new JEditValor ();
    jEditValor7.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jLabel10 = new JLabel ();
    jEditValor8 = new JEditValor ();
    jEditValor8.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jLabel11 = new JLabel ();
    jLabel12 = new JLabel ();
    jLabel13 = new JLabel ();
    jLabel14 = new JLabel ();
    jLabel15 = new JLabel ();
    jLabel16 = new JLabel ();
    jLabel17 = new JLabel ();
    jEditValor10 = new JEditValor ();
    jEditValor10.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jEditValor11 = new JEditValor ();
    jEditValor11.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jEditValor9 = new JEditValor ();
    jEditValor9.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jEditValor12 = new JEditValor ();
    jEditValor12.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jEditValor13 = new JEditValor ();
    jEditValor13.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jEditValor14 = new JEditValor ();
    jEditValor14.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jEditValor15 = new JEditValor ();
    jEditValor15.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
    jLabel1.setText ("RENDIMENTOS TRIBUT\u00c1VEIS");
    jPanel1.add (jLabel1);
    jLabel2.setText ("DEDU\u00c7\u00d5ES");
    jPanel2.add (jLabel2);
    jLabel3.setText ("Recebidos de Pessoa Jur\u00eddica pelo titular");
    jEditValor1.setInformacaoAssociada ("resumo.rendimentosTributaveisDeducoes.rendRecebidoPJTitular");
    jLabel4.setText ("Recebidos de Pessoa Jur\u00eddica pelos dependentes");
    jEditValor2.setInformacaoAssociada ("resumo.rendimentosTributaveisDeducoes.rendRecebidoPJDependentes");
    jLabel5.setText ("Recebidos de Pessoa F\u00edsica pelo titular");
    jLabel6.setText ("Recebidos de Pessoa F\u00edsica pelos dependentes");
    jEditValor3.setInformacaoAssociada ("resumo.rendimentosTributaveisDeducoes.rendRecebidoPFTitular");
    jEditValor4.setInformacaoAssociada ("resumo.rendimentosTributaveisDeducoes.rendRecebidoPFDependentes");
    jLabel7.setText ("Recebidos do exterior");
    jEditValor5.setInformacaoAssociada ("resumo.rendimentosTributaveisDeducoes.rendRecebidoExterior");
    jLabel8.setText ("Resultado tribut\u00e1vel da atividade rural");
    jEditValor6.setInformacaoAssociada ("resumo.rendimentosTributaveisDeducoes.rendTributavelAR");
    jLabel9.setText ("TOTAL");
    jEditValor7.setInformacaoAssociada ("resumo.rendimentosTributaveisDeducoes.totalRendimentos");
    jLabel10.setText ("Contribui\u00e7\u00e3o \u00e0 previd\u00eancia oficial");
    jEditValor8.setInformacaoAssociada ("resumo.rendimentosTributaveisDeducoes.previdenciaOficial");
    jLabel11.setText ("Contribui\u00e7\u00e3o \u00e0 previd\u00eancia privada e FAPI");
    jLabel12.setText ("Dependentes");
    jLabel13.setText ("Despesas com instru\u00e7\u00e3o");
    jLabel14.setText ("Despesas m\u00e9dicas");
    jLabel15.setText ("Pens\u00e3o aliment\u00edcia judicial");
    jLabel16.setText ("Livro caixa");
    jLabel17.setText ("TOTAL");
    jEditValor10.setInformacaoAssociada ("resumo.rendimentosTributaveisDeducoes.previdenciaFAPI");
    jEditValor11.setInformacaoAssociada ("resumo.rendimentosTributaveisDeducoes.dependentes");
    jEditValor9.setInformacaoAssociada ("resumo.rendimentosTributaveisDeducoes.despesasInstrucao");
    jEditValor12.setInformacaoAssociada ("resumo.rendimentosTributaveisDeducoes.despesasMedicas");
    jEditValor13.setInformacaoAssociada ("resumo.rendimentosTributaveisDeducoes.pensaoAlimenticia");
    jEditValor14.setInformacaoAssociada ("resumo.rendimentosTributaveisDeducoes.livroCaixa");
    jEditValor15.setInformacaoAssociada ("resumo.rendimentosTributaveisDeducoes.totalDeducoes");
    jEditValor15.setaFoco (true);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (jPanel1, -1, 528, 32767).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel3).addPreferredGap (0, 185, 32767).add (jEditValor1, -2, 129, -2).addContainerGap ()).add (2, layout.createSequentialGroup ().addContainerGap ().add (jPanel2, -1, 508, 32767).addContainerGap ()).add (2, layout.createSequentialGroup ().add (layout.createParallelGroup (2).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jLabel4).add (jLabel5).add (jLabel6).add (jLabel7).add (jLabel8)).addPreferredGap (0, 145, 32767)).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel9).addPreferredGap (0))).add (layout.createParallelGroup (1, false).add (jEditValor7, -1, -1, 32767).add (jEditValor6, -1, -1, 32767).add (jEditValor5, -1, -1, 32767).add (jEditValor4, -1, -1, 32767).add (jEditValor3, -1, -1, 32767).add (jEditValor2, -1, 129, 32767)).addContainerGap ()).add (2, layout.createSequentialGroup ().add (layout.createParallelGroup (2).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jLabel14).add (jLabel13).add (jLabel12).add (jLabel11).add (jLabel10).add (jLabel15).add (jLabel16)).addPreferredGap (0, 171, 32767)).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel17).addPreferredGap (0))).addPreferredGap (0, 8, 32767).add (layout.createParallelGroup (2, false).add (jEditValor12, -1, -1, 32767).add (jEditValor8, -1, -1, 32767).add (jEditValor15, -1, -1, 32767).add (jEditValor14, -1, -1, 32767).add (jEditValor13, -1, -1, 32767).add (jEditValor9, -1, -1, 32767).add (jEditValor10, -1, 128, 32767).add (jEditValor11, -1, -1, 32767)).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jPanel1, -2, -1, -2).addPreferredGap (0).add (layout.createParallelGroup (2).add (layout.createSequentialGroup ().add (layout.createParallelGroup (2).add (jLabel3).add (jEditValor1, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (2, jEditValor2, -2, -1, -2).add (2, jLabel4)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel5).add (jEditValor3, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel6).add (jEditValor4, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel7).add (jEditValor5, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel8).add (jEditValor6, -2, -1, -2)).addPreferredGap (0).add (jEditValor7, -2, -1, -2)).add (jLabel9)).add (16, 16, 16).add (jPanel2, -2, -1, -2).addPreferredGap (0).add (layout.createParallelGroup (2).add (layout.createSequentialGroup ().add (layout.createParallelGroup (1).add (jLabel10).add (jEditValor8, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel11).add (jEditValor10, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel12).add (jEditValor11, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel13).add (jEditValor9, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel14).add (jEditValor12, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel15).add (jEditValor13, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel16).add (jEditValor14, -2, -1, -2)).addPreferredGap (0).add (jEditValor15, -2, -1, -2)).add (jLabel17)).addContainerGap ()));
  }
  
  public void vaiExibir ()
  {
    IRPFFacade.getInstancia ().getDeclaracao ().getModeloCompleta ().resumoRendimentosTributaveis ();
    IRPFFacade.getInstancia ().getDeclaracao ().getModeloCompleta ().aplicaValoresNaDeclaracao ();
  }
  
  public void painelExibido ()
  {
    /* empty */
  }
}
