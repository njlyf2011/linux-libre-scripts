/* PainelImpostoPago - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.impostopago;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelImpostoPago extends JPanel implements PainelIRPFIf
{
  private static final String TITULO = "Imposto Pago";
  private boolean exibiuAlerta = false;
  private JEditValor jEditValor1;
  private JEditValor jEditValor2;
  private JEditValor jEditValor3;
  private JEditValor jEditValor4;
  private JEditValor jEditValor5;
  private JEditValor jEditValor6;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JLabel jLabel9;
  
  public String getTituloPainel ()
  {
    return "Imposto Pago";
  }
  
  public PainelImpostoPago ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "ImpostoPago");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT.deriveFont (1);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f.deriveFont (f.getSize2D () + 2.0F));
    jEditValor3.getComponenteEditor ().addFocusListener (new FocusAdapter ()
    {
      public void focusGained (FocusEvent e)
      {
	if (! exibiuAlerta && IRPFUtil.getEstadoSistema () != 1)
	  {
	    JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), TabelaMensagens.getTabelaMensagens ().msg ("alerta_imposto_fonte"), "IRPF" + ConstantesGlobais.EXERCICIO, 1);
	    exibiuAlerta = true;
	  }
      }
    });
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jLabel2 = new JLabel ();
    jLabel3 = new JLabel ();
    jLabel4 = new JLabel ();
    jLabel5 = new JLabel ();
    jLabel6 = new JLabel ();
    jEditValor1 = new JEditValor ();
    jEditValor2 = new JEditValor ();
    jEditValor3 = new JEditValor ();
    jLabel7 = new JLabel ();
    jLabel8 = new JLabel ();
    jEditValor4 = new JEditValor ();
    jEditValor5 = new JEditValor ();
    jLabel9 = new JLabel ();
    jEditValor6 = new JEditValor ();
    jLabel1.setText ("01. Imposto Complementar");
    jLabel2.setText ("<HTML>Informe a soma do campo 7 dos Darf correspondentes ao Imposto Complementar pago de 01/01/2006 a 31/12/2006 (c\u00f3digo 0246)</HTML>");
    jLabel2.setVerticalAlignment (1);
    jLabel3.setText ("02. Imposto pago no exterior");
    jLabel4.setText ("<HTML><p>Informe o total de imposto pago no exterior relativo aos rendimentos informados na ficha Rendimentos Tribut\u00e1veis Recebidos de Pessoas F\u00edsicas e do Exterior, desde que a compensa\u00e7\u00e3o desse imposto seja legalmente permitida. Veja Ajuda.</p></HTML>");
    jLabel4.setVerticalAlignment (1);
    jLabel5.setText ("03. Imposto de renda na fonte (Lei n\u00ba 11.033/2004) ");
    jLabel5.setVerticalAlignment (3);
    jLabel6.setText ("<HTML> Informe o valor do imposto de renda retido na fonte de que tratam os \u00a7 \u00a7 1\u00ba e 2\u00ba, do art. 2\u00ba da Lei N\u00ba 11.033, de 2004, desde que a compensa\u00e7\u00e3o deste imposto j\u00e1 n\u00e3o tenha sido efetuada. Veja Ajuda.</HTML>");
    jLabel6.setVerticalAlignment (1);
    jEditValor1.setInformacaoAssociada ("impostoPago.impostoComplementar");
    jEditValor2.setInformacaoAssociada ("impostoPago.impostoPagoExterior");
    jEditValor3.setInformacaoAssociada ("impostoPago.impostoRetidoFonte");
    jLabel7.setHorizontalAlignment (2);
    jLabel7.setText ("Imposto devido com os rendimentos no exterior");
    jLabel8.setHorizontalAlignment (2);
    jLabel8.setText ("Imposto devido sem os rendimentos no exterior");
    jEditValor4.setInformacaoAssociada ("impostoPago.impostoDevidoComRendExterior");
    jEditValor5.setInformacaoAssociada ("impostoPago.impostoDevidoSemRendExterior");
    jLabel9.setHorizontalAlignment (2);
    jLabel9.setText ("Diferen\u00e7a a ser considerada para c\u00e1lculo do imposto(limite legal) ");
    jEditValor6.setInformacaoAssociada ("impostoPago.limiteLegalImpPagoExterior");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (layout.createParallelGroup (1).add (jLabel2, -1, 386, 32767).add (jLabel1, -1, 386, 32767).add (2, jLabel6, -1, 386, 32767).add (jLabel7, -1, 386, 32767).add (jLabel8, -1, 386, 32767).add (jLabel9, -1, 386, 32767).add (jLabel4, -1, 386, 32767)).add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addPreferredGap (0).add (layout.createParallelGroup (2).add (jEditValor6, -2, 140, -2).add (1, layout.createParallelGroup (2).add (jEditValor5, -2, 140, -2).add (1, layout.createParallelGroup (2).add (jEditValor4, -2, 140, -2).add (1, layout.createParallelGroup (2).add (jEditValor1, -2, 140, -2).add (1, jEditValor2, -2, 140, -2)))))).add (2, layout.createSequentialGroup ().addPreferredGap (0).add (jEditValor3, -2, 140, -2))).addContainerGap ()).add (layout.createSequentialGroup ().add (jLabel5, -1, 365, 32767).add (175, 175, 175)).add (2, layout.createSequentialGroup ().add (jLabel3, -1, 365, 32767).add (175, 175, 175)))));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel1).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel2, -2, 44, -2).add (jEditValor1, -2, -1, -2)).addPreferredGap (0).add (jLabel3).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel4, -2, 72, -2).add (jEditValor2, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel7).add (jEditValor4, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel8).add (jEditValor5, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jLabel9).add (12, 12, 12).add (jLabel5, -2, 33, -2)).add (jEditValor6, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel6, -2, 67, -2).add (jEditValor3, -2, -1, -2)).addContainerGap (-1, 32767)));
  }
  
  public void vaiExibir ()
  {
    exibiuAlerta = false;
  }
  
  public void painelExibido ()
  {
    /* empty */
  }
}
