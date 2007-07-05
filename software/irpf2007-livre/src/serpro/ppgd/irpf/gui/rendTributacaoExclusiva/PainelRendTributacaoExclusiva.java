/* PainelRendTributacaoExclusiva - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.rendTributacaoExclusiva;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.QuadroAuxiliarTransporteValor;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Valor;

public class PainelRendTributacaoExclusiva extends JPanel implements PainelIRPFIf
{
  private static final String TITULO = "Rendimentos Sujeitos \u00e0 Tributa\u00e7\u00e3o Exclusiva/Definitiva";
  private JButton btnOutros;
  private JButton btnRendAplicacoesFin;
  private JButton btnRendExcetoDecimoTerc;
  private JEditAlfa jEditAlfa1;
  private JEditValor jEditValor1;
  private JEditValor jEditValor10;
  private JEditValor jEditValor5;
  private JEditValor jEditValor6;
  private JEditValor jEditValor7;
  private JEditValor jEditValor8;
  private JEditValor jEditValor9;
  private JLabel jLabel1;
  private JLabel jLabel10;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JLabel jLabel9;
  
  public String getTituloPainel ()
  {
    return "Rendimentos Sujeitos \u00e0 Tributa\u00e7\u00e3o Exclusiva/Definitiva";
  }
  
  public PainelRendTributacaoExclusiva ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "RendimentosExclusiva");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT.deriveFont (1);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f.deriveFont (f.getSize2D () + 2.0F));
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jLabel5 = new JLabel ();
    jLabel6 = new JLabel ();
    jLabel7 = new JLabel ();
    jLabel8 = new JLabel ();
    jLabel9 = new JLabel ();
    jLabel10 = new JLabel ();
    jEditValor1 = new JEditValor ();
    jEditValor5 = new JEditValor ();
    jEditValor6 = new JEditValor ();
    jEditValor7 = new JEditValor ();
    jEditValor8 = new JEditValor ();
    jEditValor9 = new JEditValor ();
    jEditValor10 = new JEditValor ();
    btnRendAplicacoesFin = new JButton ();
    btnOutros = new JButton ();
    btnRendExcetoDecimoTerc = new JButton ();
    jEditAlfa1 = new JEditAlfa ();
    jLabel1.setText ("<html>01. 13\u00ba sal\u00e1rio</html>");
    jLabel5.setText ("<html>02. Ganhos l\u00edquidos em renda vari\u00e1vel  (bolsas de valores, de mercadorias, de futuros e assemelhadas)</html>");
    jLabel6.setText ("<html>03. Rendimentos de aplica\u00e7\u00f5es financeiras</html>");
    jLabel7.setText ("<html>04. Outros (especifique)</html>");
    jLabel8.setText ("<html>05. 13\u00ba sal\u00e1rio recebido pelos dependentes</html>");
    jLabel9.setText ("<html>06. Rendimentos sujeitos \u00e0 tributa\u00e7\u00e3o exclusiva/definitiva dos dependentes, exceto 13\u00ba sal\u00e1rio</html>");
    jLabel10.setHorizontalAlignment (4);
    jLabel10.setText ("TOTAL");
    jEditValor1.setInformacaoAssociada ("rendTributacaoExclusiva.decimoTerceiro");
    jEditValor1.setMinimumSize (new Dimension (50, 20));
    jEditValor5.setInformacaoAssociada ("rendTributacaoExclusiva.ganhosRendaVariavel");
    jEditValor6.setInformacaoAssociada ("rendTributacaoExclusiva.rendAplicacoes");
    jEditValor7.setInformacaoAssociada ("rendTributacaoExclusiva.outros");
    jEditValor8.setInformacaoAssociada ("rendTributacaoExclusiva.decimoTerceiroDependentes");
    jEditValor9.setInformacaoAssociada ("rendTributacaoExclusiva.rendExcetoDecimoTerceiro");
    jEditValor10.setAceitaFoco (false);
    jEditValor10.setInformacaoAssociada ("rendTributacaoExclusiva.total");
    btnRendAplicacoesFin.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/detalhe.png")));
    btnRendAplicacoesFin.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelRendTributacaoExclusiva.this.btnRendAplicacoesFinActionPerformed (evt);
      }
    });
    btnOutros.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/detalhe.png")));
    btnOutros.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelRendTributacaoExclusiva.this.btnOutrosActionPerformed (evt);
      }
    });
    btnRendExcetoDecimoTerc.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/detalhe.png")));
    btnRendExcetoDecimoTerc.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelRendTributacaoExclusiva.this.btnRendExcetoDecimoTercActionPerformed (evt);
      }
    });
    jEditAlfa1.setInformacaoAssociada ("rendTributacaoExclusiva.descTotal");
    jEditAlfa1.setMaxChars (60);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (2, layout.createSequentialGroup ().add (jLabel8, -1, 423, 32767).addPreferredGap (0).add (jEditValor8, -2, 127, -2).addPreferredGap (0)).add (2, layout.createSequentialGroup ().add (jLabel9, -1, 377, 32767).addPreferredGap (0).add (btnRendExcetoDecimoTerc, -2, 40, -2).addPreferredGap (0).add (jEditValor9, -2, 127, -2)).add (2, layout.createSequentialGroup ().add (jLabel10, -1, 423, 32767).addPreferredGap (0).add (jEditValor10, -2, 127, -2).addPreferredGap (0)).add (2, layout.createSequentialGroup ().add (layout.createParallelGroup (2).add (layout.createSequentialGroup ().add (jLabel1, -1, 238, 32767).add (137, 137, 137)).add (1, jLabel5, -1, 375, 32767).add (layout.createSequentialGroup ().add (jLabel7, -1, 163, 32767).addPreferredGap (0).add (jEditAlfa1, -2, 208, -2)).add (1, jLabel6, -1, 375, 32767)).addPreferredGap (0).add (layout.createParallelGroup (1).add (2, layout.createSequentialGroup ().add (btnOutros, -2, 40, -2).addPreferredGap (0).add (jEditValor7, -2, 127, -2).addPreferredGap (0)).add (2, layout.createSequentialGroup ().add (btnRendAplicacoesFin, -2, 40, -2).addPreferredGap (0).add (layout.createParallelGroup (2).add (jEditValor6, -2, 127, -2).add (jEditValor5, -2, 127, -2).add (jEditValor1, -2, 127, -2)))))).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jEditValor1, -2, -1, -2).add (jLabel1)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel5).add (jEditValor5, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel6, -2, 20, -2).add (jEditValor6, -2, -1, -2).add (btnRendAplicacoesFin, -2, 20, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel7, -2, 20, -2).add (jEditAlfa1, -2, 20, -2).add (btnOutros, -2, 20, -2).add (jEditValor7, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1, false).add (jLabel8, -1, -1, 32767).add (jEditValor8, -1, -1, 32767)).addPreferredGap (0).add (layout.createParallelGroup (1).add (btnRendExcetoDecimoTerc, -2, 20, -2).add (jLabel9).add (jEditValor9, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1, false).add (jLabel10, -1, -1, 32767).add (jEditValor10, -1, -1, 32767)).add (95, 95, 95)));
  }
  
  private void btnOutrosActionPerformed (ActionEvent evt)
  {
    Valor recep = IRPFFacade.getInstancia ().getDeclaracao ().getRendTributacaoExclusiva ().getOutros ();
    Alfa descOutros = IRPFFacade.getInstancia ().getDeclaracao ().getRendTributacaoExclusiva ().getDescTotal ();
    ColecaoItemQuadroAuxiliar col = IRPFFacade.getInstancia ().getDeclaracao ().getRendTributacaoExclusiva ().getOutrosQuadroAuxiliar ();
    IRPFGuiUtil.exibeDialog (new QuadroAuxiliarTransporteValor (recep, col, descOutros), true, "Quadro auxiliar para transporte de valor", false);
  }
  
  private void btnRendAplicacoesFinActionPerformed (ActionEvent evt)
  {
    Valor recep = IRPFFacade.getInstancia ().getDeclaracao ().getRendTributacaoExclusiva ().getRendAplicacoes ();
    ColecaoItemQuadroAuxiliar col = IRPFFacade.getInstancia ().getDeclaracao ().getRendTributacaoExclusiva ().getRendAplicacoesQuadroAuxiliar ();
    IRPFGuiUtil.exibeDialog (new QuadroAuxiliarTransporteValor (recep, col), true, "Quadro auxiliar para transporte de valor", false);
  }
  
  private void btnRendExcetoDecimoTercActionPerformed (ActionEvent evt)
  {
    Valor recep = IRPFFacade.getInstancia ().getDeclaracao ().getRendTributacaoExclusiva ().getRendExcetoDecimoTerceiro ();
    ColecaoItemQuadroAuxiliar col = IRPFFacade.getInstancia ().getDeclaracao ().getRendTributacaoExclusiva ().getRendExcetoDecimoTerceiroQuadroAuxiliar ();
    IRPFGuiUtil.exibeDialog (new QuadroAuxiliarTransporteValor (recep, col), true, "Quadro auxiliar para transporte de valor", false);
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
