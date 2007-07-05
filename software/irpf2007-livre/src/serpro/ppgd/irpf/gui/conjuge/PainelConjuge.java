/* PainelConjuge - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.conjuge;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JButtonGroupPanel;
import serpro.ppgd.gui.xbeans.JButtonMensagem;
import serpro.ppgd.gui.xbeans.JEditCPF;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.gui.xbeans.PPGDRadioItem;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.negocio.Logico;
import serpro.ppgd.negocio.Observador;

public class PainelConjuge extends JPanel implements PainelIRPFIf
{
  public static final String TEXTO_BASE_CALCULO_SIM = "<html><p>Base de c\u00e1lculo do imposto devido</p></html>";
  public static final String TEXTO_BASE_CALCULO_NAO = "<html><p>Base de c\u00e1lculo</p></html>";
  public static final String TEXTO_IR_RETIDO_SIM = "<html><p>Imposto retido na fonte</p></html>";
  public static final String TEXTO_IR_RETIDO_NAO = "<html><p>Total do imposto pago</p></html>";
  public static final String TEXTO_REND_TRIB_EXCL_SIM = "<html><p>Rendimentos sujeitos \u00e0 tributa\u00e7\u00e3o exclusiva (inclusive 13\u00b0 sal\u00e1rio)</p></html>";
  public static final String TEXTO_REND_TRIB_EXCL_NAO = "<html><p>Rendimentos sujeitos \u00e0 tributa\u00e7\u00e3o exclusiva</p></html>";
  private JButtonGroupPanel jButtonGroupPanel1;
  private JButtonMensagem jButtonMensagem1;
  private JEditCPF jEditCPF1;
  private JEditValor jEditValor1;
  private JEditValor jEditValor2;
  private JEditValor jEditValor3;
  private JEditValor jEditValor4;
  private JEditValor jEditValor5;
  private JEditValor jEditValor6;
  private JLabel jLabel1;
  private JLabel jLabel11;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel8;
  private JLabel jLabel9;
  private JPanel jPanel1;
  private JSeparator jSeparator1;
  private JLabel lblCalculoImposto;
  private JLabel lblIRRetidoFonte;
  private JLabel lblRendTribExclusiva;
  private PPGDRadioItem pPGDRadioItem1;
  private PPGDRadioItem pPGDRadioItem2;
  
  public PainelConjuge ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "conjuge");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT.deriveFont (1);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f.deriveFont (f.getSize2D () + 2.0F));
    IRPFFacade.getInstancia ().getConjuge ().getDecSimplificada ().addObservador (new Observador ()
    {
      public void notifica (Object object, String string, Object object0, Object object1)
      {
	if (IRPFFacade.getInstancia ().getConjuge ().getDecSimplificada ().asString ().equals (Logico.SIM))
	  {
	    lblCalculoImposto.setText ("<html><p>Base de c\u00e1lculo do imposto devido</p></html>");
	    lblIRRetidoFonte.setText ("<html><p>Imposto retido na fonte</p></html>");
	    lblRendTribExclusiva.setText ("<html><p>Rendimentos sujeitos \u00e0 tributa\u00e7\u00e3o exclusiva (inclusive 13\u00b0 sal\u00e1rio)</p></html>");
	  }
	else
	  {
	    lblCalculoImposto.setText ("<html><p>Base de c\u00e1lculo</p></html>");
	    lblIRRetidoFonte.setText ("<html><p>Total do imposto pago</p></html>");
	    lblRendTribExclusiva.setText ("<html><p>Rendimentos sujeitos \u00e0 tributa\u00e7\u00e3o exclusiva</p></html>");
	  }
      }
    });
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jLabel2 = new JLabel ();
    jLabel3 = new JLabel ();
    jSeparator1 = new JSeparator ();
    lblCalculoImposto = new JLabel ();
    lblIRRetidoFonte = new JLabel ();
    jLabel8 = new JLabel ();
    jLabel9 = new JLabel ();
    lblRendTribExclusiva = new JLabel ();
    jLabel11 = new JLabel ();
    jEditValor1 = new JEditValor ();
    jEditValor2 = new JEditValor ();
    jEditValor3 = new JEditValor ();
    jEditValor4 = new JEditValor ();
    jEditValor5 = new JEditValor ();
    jEditValor6 = new JEditValor ();
    jPanel1 = new JPanel ();
    jLabel4 = new JLabel ();
    jEditCPF1 = new JEditCPF ();
    jLabel5 = new JLabel ();
    jButtonGroupPanel1 = new JButtonGroupPanel ();
    pPGDRadioItem1 = new PPGDRadioItem ();
    pPGDRadioItem2 = new PPGDRadioItem ();
    jButtonMensagem1 = new JButtonMensagem ();
    jLabel1.setText ("<html><p>Informe o CPF do c\u00f4njuge. Somente informe os demais dados desta ficha se:</p></html>");
    jLabel2.setText ("<html><p>a) a declara\u00e7\u00e3o n\u00e3o for em conjunto com o c\u00f4njuge; E");
    jLabel3.setText ("<html><p>b) os bens comuns do casal estiverem informados nesta declara\u00e7\u00e3o.</p></html>");
    jSeparator1.setOrientation (1);
    lblCalculoImposto.setText ("<html><p>Base de c\u00e1lculo do imposto devido</p></html>");
    lblIRRetidoFonte.setText ("<html><p>Imposto retido na <br>fonte</p></html>");
    jLabel8.setText ("<html><p>Carn\u00ea-le\u00e3o e imposto complementar</p></html>");
    jLabel9.setText ("<html><p>Rendimentos isentos e n\u00e3o tribut\u00e1veis</p></html>");
    lblRendTribExclusiva.setText ("<html><p>Rendimentos sujeitos \u00e0 tributa\u00e7\u00e3o exclusiva (inclusive 13\u00b0 sal\u00e1rio)</p></html>");
    jLabel11.setText ("RESULTADO");
    jEditValor1.setInformacaoAssociada ("conjuge.baseCalculoImposto");
    jEditValor2.setInformacaoAssociada ("conjuge.impRetidoFonte");
    jEditValor3.setInformacaoAssociada ("conjuge.carneComImpComplementar");
    jEditValor4.setInformacaoAssociada ("conjuge.rendIsentoNaoTributaveis");
    jEditValor5.setInformacaoAssociada ("conjuge.rendSujeitosTribExcl");
    jEditValor6.setInformacaoAssociada ("conjuge.resultado");
    jLabel4.setText ("N\u00famero do CPF do c\u00f4njuge");
    jEditCPF1.setInformacaoAssociada ("conjuge.cpfConjuge");
    jLabel5.setText ("<html><p>O c\u00f4njuge apresentou declara\u00e7\u00e3o de ajuste anual simplificada?<br> Se dispensado de apresenta\u00e7\u00e3o, pressione F1.<br></p></html>");
    jButtonGroupPanel1.setEstiloFonte (1);
    jButtonGroupPanel1.setInformacaoAssociada ("conjuge.decSimplificada");
    pPGDRadioItem1.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    pPGDRadioItem1.setText ("Sim");
    pPGDRadioItem1.setMargin (new Insets (0, 0, 0, 0));
    pPGDRadioItem2.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    pPGDRadioItem2.setText ("N\u00e3o");
    pPGDRadioItem2.setMargin (new Insets (0, 0, 0, 0));
    pPGDRadioItem2.setValorSelecionadoTrue ("0");
    jButtonMensagem1.setText ("jButtonMensagem1");
    GroupLayout jButtonGroupPanel1Layout = new GroupLayout (jButtonGroupPanel1);
    jButtonGroupPanel1.setLayout (jButtonGroupPanel1Layout);
    jButtonGroupPanel1Layout.setHorizontalGroup (jButtonGroupPanel1Layout.createParallelGroup (1).add (jButtonGroupPanel1Layout.createSequentialGroup ().addContainerGap ().add (pPGDRadioItem1, -2, -1, -2).addPreferredGap (0).add (pPGDRadioItem2, -2, -1, -2).addPreferredGap (0, -1, 32767).add (jButtonMensagem1, -2, -1, -2)));
    jButtonGroupPanel1Layout.setVerticalGroup (jButtonGroupPanel1Layout.createParallelGroup (1).add (jButtonGroupPanel1Layout.createSequentialGroup ().add (jButtonGroupPanel1Layout.createParallelGroup (3).add (pPGDRadioItem2, -2, -1, -2).add (pPGDRadioItem1, -2, -1, -2).add (jButtonMensagem1, -2, -1, -2)).addContainerGap (-1, 32767)));
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (jLabel4).add (jEditCPF1, -2, 169, -2).add (jLabel5, -2, 211, -2).add (jButtonGroupPanel1, -2, -1, -2)).addContainerGap (32, 32767)));
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().add (jLabel4).addPreferredGap (0).add (jEditCPF1, -2, -1, -2).addPreferredGap (0).add (jLabel5).addPreferredGap (0).add (jButtonGroupPanel1, -2, -1, -2).addContainerGap (66, 32767)));
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (2, jLabel1, -1, 587, 32767).add (jLabel2).add (jLabel3).add (layout.createSequentialGroup ().add (jPanel1, -2, -1, -2).addPreferredGap (0).add (jSeparator1, -2, 2, -2).addPreferredGap (0).add (layout.createParallelGroup (1, false).add (lblRendTribExclusiva, 0, 0, 32767).add (lblIRRetidoFonte, -1, -1, 32767).add (jLabel8, 0, 0, 32767).add (2, jLabel9, -1, 162, 32767).add (jLabel11).add (lblCalculoImposto, 0, 0, 32767)).addPreferredGap (0).add (layout.createParallelGroup (2).add (1, jEditValor2, -1, 150, 32767).add (jEditValor1, -1, 150, 32767).add (1, jEditValor3, -1, 150, 32767).add (jEditValor4, -1, 150, 32767).add (jEditValor5, -1, 150, 32767).add (jEditValor6, -1, 150, 32767)))).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel1).addPreferredGap (0).add (jLabel2).addPreferredGap (0).add (jLabel3).add (layout.createParallelGroup (1).add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (30, 30, 30).add (layout.createParallelGroup (1).add (lblCalculoImposto).add (jEditValor1, -2, -1, -2)).add (11, 11, 11).add (layout.createParallelGroup (1).add (lblIRRetidoFonte).add (jEditValor2, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel8).add (jEditValor3, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel9).add (jEditValor4, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1, false).add (2, layout.createSequentialGroup ().add (lblRendTribExclusiva).addPreferredGap (0).add (jLabel11)).add (layout.createSequentialGroup ().add (jEditValor5, -2, -1, -2).addPreferredGap (0, 26, 32767).add (jEditValor6, -2, -1, -2)))).add (layout.createSequentialGroup ().addPreferredGap (0).add (jSeparator1, -1, 326, 32767).addContainerGap ())).add (layout.createSequentialGroup ().add (31, 31, 31).add (jPanel1, -2, -1, -2).addContainerGap ()))));
  }
  
  public String getTituloPainel ()
  {
    return "Informa\u00e7\u00f5es do c\u00f4njuge";
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
