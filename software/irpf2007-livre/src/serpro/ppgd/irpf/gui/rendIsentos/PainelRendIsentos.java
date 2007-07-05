/* PainelRendIsentos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.rendIsentos;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditAlfa;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.ColecaoItemQuadroAuxiliar;
import serpro.ppgd.irpf.ColecaoItemQuadroLucrosDividendos;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.QuadroAuxiliarLucrosDividendos;
import serpro.ppgd.irpf.gui.QuadroAuxiliarTransporteValor;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Valor;

public class PainelRendIsentos extends JPanel implements PainelIRPFIf
{
  private static final String TITULO = "Rendimentos Isentos e N\u00e3o-Tribut\u00e1veis";
  private JButton btnLucrosDividendos;
  private JButton btnOutros;
  private JButton btnRendIsentos;
  private JButton btnRendPoupanca;
  private JEditAlfa jEditAlfa1;
  private JEditValor jEditValor1;
  private JEditValor jEditValor10;
  private JEditValor jEditValor11;
  private JEditValor jEditValor12;
  private JEditValor jEditValor13;
  private JEditValor jEditValor14;
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
  private JLabel jLabel14;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JLabel jLabel9;
  
  public PainelRendIsentos ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "RendimentosIsentos");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT.deriveFont (1);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f.deriveFont (f.getSize2D () + 2.0F));
    jEditValor4.getComponenteFoco ().setBorder (BorderFactory.createLoweredBevelBorder ());
  }
  
  public String getTituloPainel ()
  {
    return "Rendimentos Isentos e N\u00e3o-Tribut\u00e1veis";
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jEditValor1 = new JEditValor ();
    jEditValor2 = new JEditValor ();
    jLabel2 = new JLabel ();
    jEditValor3 = new JEditValor ();
    jLabel3 = new JLabel ();
    jLabel4 = new JLabel ();
    jEditValor4 = new JEditValor ();
    jEditValor5 = new JEditValor ();
    jEditValor6 = new JEditValor ();
    jLabel5 = new JLabel ();
    jLabel6 = new JLabel ();
    jLabel8 = new JLabel ();
    jLabel9 = new JLabel ();
    jLabel10 = new JLabel ();
    jLabel11 = new JLabel ();
    jLabel12 = new JLabel ();
    jEditValor7 = new JEditValor ();
    jEditValor8 = new JEditValor ();
    jEditValor9 = new JEditValor ();
    jEditValor10 = new JEditValor ();
    jEditValor11 = new JEditValor ();
    jEditAlfa1 = new JEditAlfa ();
    jEditValor12 = new JEditValor ();
    btnRendPoupanca = new JButton ();
    btnOutros = new JButton ();
    btnLucrosDividendos = new JButton ();
    jLabel13 = new JLabel ();
    jEditValor13 = new JEditValor ();
    btnRendIsentos = new JButton ();
    jLabel14 = new JLabel ();
    jEditValor14 = new JEditValor ();
    jLabel7 = new JLabel ();
    jLabel1.setLabelFor (jEditValor1);
    jLabel1.setText ("<html>01. Bolsa de estudos e pesquisa, desde que n\u00e3o represente<BR>vantagem ao doador e n\u00e3o caracterize contrapresta\u00e7\u00e3o de servi\u00e7o</html>");
    jLabel1.setHorizontalTextPosition (10);
    jLabel1.getAccessibleContext ().setAccessibleName ("");
    jLabel1.getAccessibleContext ().setAccessibleDescription ("");
    jEditValor1.setInformacaoAssociada ("rendIsentos.bolsaEstudos");
    jEditValor2.setInformacaoAssociada ("rendIsentos.capitalApolices");
    jLabel2.setText ("<html>02. Capital das ap\u00f3lices de seguro ou pec\u00falio pago por morte do<BR>segurado, pr\u00eamio de seguro restitu\u00eddo em qualquer caso e<BR>pec\u00falio recebido de entidades de previd\u00eancia privada em<BR>decorr\u00eancia de morte ou invalidez permanente</html>");
    jEditValor3.setInformacaoAssociada ("rendIsentos.indenizacoes");
    jLabel3.setText ("<html>03. Indeniza\u00e7\u00f5es por rescis\u00e3o de contrato de trabalho, inclusive a<BR>t\u00edtulo de PDV, e por acidente de trabalho; e FGTS</html>");
    jLabel4.setText ("<html>04. Lucro na aliena\u00e7\u00e3o de bens e/ou direitos de pequeno valor<br>ou do \u00fanico im\u00f3vel e redu\u00e7\u00e3o do ganho de capital.</html>");
    jEditValor4.setInformacaoAssociada ("rendIsentos.lucroAlienacao");
    jEditValor5.setAceitaFoco (false);
    jEditValor5.setInformacaoAssociada ("rendIsentos.lucroRecebido");
    jEditValor6.setInformacaoAssociada ("rendIsentos.parcIsentaAtivRural");
    jLabel5.setText ("<html>05. Lucros e dividendos recebidos pelo titular e pelos dependentes</html>");
    jLabel6.setText ("<html>11. Parcela isenta correspondente \u00e0 atividade rural</html>");
    jLabel8.setText ("<html>07. Pens\u00e3o, proventos de aposentadoria ou reforma por mol\u00e9stia<BR>grave ou aposentadoria ou reforma por acidente em servi\u00e7o</html>");
    jLabel9.setText ("<html>08. Rendimentos de cadernetas de poupan\u00e7as e letras<BR>hipotec\u00e1rias</html>");
    jLabel10.setText ("<html>09. Rendimento de s\u00f3cio ou titular de microempresa ou empresa de<BR>pequeno porte optante pelo Simples, exceto pro labore, alugu\u00e9is<BR>e servi\u00e7os prestados</html>");
    jLabel11.setText ("<html>10. Transfer\u00eancias patrimoniais - doa\u00e7\u00f5es, heran\u00e7as, mea\u00e7\u00f5es e<BR>dissolu\u00e7\u00e3o da sociedade conjugal ou da unidade familiar</html>");
    jLabel12.setText ("<html>12. Outros (especifique)</html>");
    jEditValor7.setInformacaoAssociada ("rendIsentos.parcIsentaAposentadoria");
    jEditValor8.setInformacaoAssociada ("rendIsentos.pensao");
    jEditValor9.setInformacaoAssociada ("rendIsentos.poupanca");
    jEditValor10.setInformacaoAssociada ("rendIsentos.rendSocio");
    jEditValor11.setInformacaoAssociada ("rendIsentos.transferencias");
    jEditAlfa1.setInformacaoAssociada ("rendIsentos.descOutros");
    jEditAlfa1.setMaxChars (60);
    jEditValor12.setInformacaoAssociada ("rendIsentos.outros");
    btnRendPoupanca.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/detalhe.png")));
    btnRendPoupanca.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelRendIsentos.this.btnRendPoupancaActionPerformed (evt);
      }
    });
    btnOutros.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/detalhe.png")));
    btnOutros.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelRendIsentos.this.btnOutrosActionPerformed (evt);
      }
    });
    btnLucrosDividendos.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/detalhe.png")));
    btnLucrosDividendos.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelRendIsentos.this.btnLucrosDividendosActionPerformed (evt);
      }
    });
    jLabel13.setText ("<html><p>13. Demais rendimentos isentos e n\u00e3o tribut\u00e1veis dos dependentes</p></html>");
    jEditValor13.setInformacaoAssociada ("rendIsentos.rendDependentes");
    btnRendIsentos.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/detalhe.png")));
    btnRendIsentos.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelRendIsentos.this.btnRendIsentosActionPerformed (evt);
      }
    });
    jLabel14.setHorizontalAlignment (4);
    jLabel14.setText ("TOTAL");
    jEditValor14.setAceitaFoco (false);
    jEditValor14.setInformacaoAssociada ("rendIsentos.total");
    jLabel7.setText ("<html>06. Parcela isenta de proventos de aposentadoria, reserva remunerada, <br>reforma e pens\u00e3o de declarante com 65 anos ou mais</html>");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (layout.createParallelGroup (2).add (layout.createSequentialGroup ().add (jLabel5, -2, 270, -2).addPreferredGap (0, 42, 32767).add (btnLucrosDividendos, -2, 40, -2)).add (1, jLabel1, -1, 352, 32767).add (1, jLabel2, -1, 352, 32767).add (1, jLabel3, -1, 352, 32767).add (1, jLabel4, -1, 352, 32767)).add (5, 5, 5).add (layout.createParallelGroup (1).add (jEditValor1, -2, 128, -2).add (jEditValor2, -2, 128, -2).add (jEditValor5, -2, 128, -2).add (jEditValor4, -2, 128, -2).add (jEditValor3, -2, 128, -2))).add (2, layout.createSequentialGroup ().add (jLabel7).addPreferredGap (0).add (jEditValor7, -2, 128, -2)).add (layout.createSequentialGroup ().add (jLabel8).addPreferredGap (0, 43, 32767).add (jEditValor8, -2, 128, -2)).add (layout.createSequentialGroup ().add (jLabel9, -1, 308, 32767).addPreferredGap (0).add (btnRendPoupanca, -2, 40, -2).add (5, 5, 5).add (jEditValor9, -2, 128, -2)).add (layout.createSequentialGroup ().add (jLabel10).addPreferredGap (0, 35, 32767).add (jEditValor10, -2, 128, -2)).add (layout.createSequentialGroup ().add (jLabel11).addPreferredGap (0, 48, 32767).add (jEditValor11, -2, 128, -2)).add (layout.createSequentialGroup ().add (jLabel6).addPreferredGap (0, 111, 32767).add (jEditValor6, -2, 128, -2)).add (layout.createSequentialGroup ().add (jLabel12).add (59, 59, 59).add (jEditAlfa1, -1, 130, 32767).addPreferredGap (0).add (btnOutros, -2, 40, -2).add (5, 5, 5).add (jEditValor12, -2, 128, -2)).add (2, layout.createSequentialGroup ().add (jLabel13, -1, 308, 32767).addPreferredGap (0).add (btnRendIsentos, -2, 40, -2).add (5, 5, 5).add (jEditValor13, -2, 128, -2)).add (2, layout.createSequentialGroup ().add (jLabel14, -1, 343, 32767).add (14, 14, 14).add (jEditValor14, -2, 128, -2))).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jEditValor1, -2, -1, -2).add (jLabel1)).add (10, 10, 10).add (layout.createParallelGroup (1).add (jEditValor2, -2, -1, -2).add (jLabel2)).add (10, 10, 10).add (layout.createParallelGroup (1).add (jEditValor3, -2, -1, -2).add (jLabel3)).add (10, 10, 10).add (layout.createParallelGroup (1).add (jEditValor4, -2, -1, -2).add (jLabel4)).add (10, 10, 10).add (layout.createParallelGroup (1).add (btnLucrosDividendos, -2, 20, -2).add (jEditValor5, -2, -1, -2).add (jLabel5, -2, 33, -2)).add (12, 12, 12).add (layout.createParallelGroup (1).add (jEditValor7, -2, -1, -2).add (jLabel7)).add (15, 15, 15).add (layout.createParallelGroup (1).add (jLabel8).add (jEditValor8, -2, -1, -2)).add (14, 14, 14).add (layout.createParallelGroup (1).add (btnRendPoupanca, -2, 20, -2).add (jEditValor9, -2, -1, -2).add (jLabel9)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel10).add (jEditValor10, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel11).add (jEditValor11, -2, -1, -2)).add (14, 14, 14).add (layout.createParallelGroup (1).add (jEditValor6, -2, -1, -2).add (jLabel6)).add (14, 14, 14).add (layout.createParallelGroup (1).add (jEditValor12, -2, -1, -2).add (layout.createParallelGroup (2, false).add (1, jEditAlfa1, -1, -1, 32767).add (1, btnOutros, -2, 20, -2)).add (jLabel12)).addPreferredGap (0).add (layout.createParallelGroup (1).add (layout.createParallelGroup (1, false).add (btnRendIsentos, -2, 20, 32767).add (jEditValor13, -1, -1, 32767)).add (jLabel13)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel14).add (jEditValor14, -2, -1, -2)).addContainerGap (25, 32767)));
  }
  
  private void btnRendIsentosActionPerformed (ActionEvent evt)
  {
    Valor recep = IRPFFacade.getInstancia ().getDeclaracao ().getRendIsentos ().getRendDependentes ();
    ColecaoItemQuadroAuxiliar col = IRPFFacade.getInstancia ().getDeclaracao ().getRendIsentos ().getRendDependentesQuadroAuxiliar ();
    IRPFGuiUtil.exibeDialog (new QuadroAuxiliarTransporteValor (recep, col), true, "Quadro auxiliar para transporte de valor", false);
  }
  
  private void btnOutrosActionPerformed (ActionEvent evt)
  {
    Valor recep = IRPFFacade.getInstancia ().getDeclaracao ().getRendIsentos ().getOutros ();
    Alfa descOutros = IRPFFacade.getInstancia ().getDeclaracao ().getRendIsentos ().getDescOutros ();
    ColecaoItemQuadroAuxiliar col = IRPFFacade.getInstancia ().getDeclaracao ().getRendIsentos ().getOutrosQuadroAuxiliar ();
    IRPFGuiUtil.exibeDialog (new QuadroAuxiliarTransporteValor (recep, col, descOutros), true, "Quadro auxiliar para transporte de valor", false);
  }
  
  private void btnRendPoupancaActionPerformed (ActionEvent evt)
  {
    Valor recep = IRPFFacade.getInstancia ().getDeclaracao ().getRendIsentos ().getPoupanca ();
    ColecaoItemQuadroAuxiliar col = IRPFFacade.getInstancia ().getDeclaracao ().getRendIsentos ().getPoupancaQuadroAuxiliar ();
    IRPFGuiUtil.exibeDialog (new QuadroAuxiliarTransporteValor (recep, col), true, "Quadro auxiliar para transporte de valor", false);
  }
  
  private void btnLucrosDividendosActionPerformed (ActionEvent evt)
  {
    Valor recep = IRPFFacade.getInstancia ().getDeclaracao ().getRendIsentos ().getLucroRecebido ();
    ColecaoItemQuadroLucrosDividendos col = IRPFFacade.getInstancia ().getDeclaracao ().getRendIsentos ().getLucroRecebidoQuadroAuxiliar ();
    IRPFGuiUtil.exibeDialog (new QuadroAuxiliarLucrosDividendos (recep, col), true, "Quadro auxiliar para transporte de valor", false);
  }
  
  public void vaiExibir ()
  {
    /* empty */
  }
  
  public void painelExibido ()
  {
    /* empty */
  }
}
