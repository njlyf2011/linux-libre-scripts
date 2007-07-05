/* PainelEnderecoBrasil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.contribuinte;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.NavegadorHtml;
import serpro.ppgd.gui.xbeans.JEditAlfa;
import serpro.ppgd.gui.xbeans.JEditCEP;
import serpro.ppgd.gui.xbeans.JEditCodigo;
import serpro.ppgd.gui.xbeans.JEditMascara;
import serpro.ppgd.gui.xbeans.JEditTelefone;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelEnderecoBrasil extends JPanel
{
  private static final TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
  private JButton jButton1;
  private JEditAlfa jEditAlfa2;
  private JEditAlfa jEditAlfa3;
  private JEditAlfa jEditAlfa4;
  private JEditAlfa jEditAlfa5;
  public JEditCEP jEditCEP1;
  private JEditCodigo jEditCodigo1;
  private JEditCodigo jEditCodigo2;
  private JEditCodigo jEditCodigo3;
  private JEditMascara jEditMascara2;
  private JEditTelefone jEditTelefone1;
  private JLabel jLabel10;
  private JLabel jLabel11;
  private JLabel jLabel12;
  private JLabel jLabel13;
  private JLabel jLabel14;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JLabel jLabel9;
  private JPanel jPanel1;
  
  public PainelEnderecoBrasil ()
  {
    initComponents ();
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, PgdIRPF.FONTE_DEFAULT.deriveFont (1));
  }
  
  private void initComponents ()
  {
    jPanel1 = new JPanel ();
    jLabel5 = new JLabel ();
    jEditCodigo1 = new JEditCodigo ();
    jLabel6 = new JLabel ();
    jEditAlfa2 = new JEditAlfa ();
    jLabel7 = new JLabel ();
    jEditAlfa3 = new JEditAlfa ();
    jLabel8 = new JLabel ();
    jEditAlfa4 = new JEditAlfa ();
    jLabel9 = new JLabel ();
    jEditAlfa5 = new JEditAlfa ();
    jLabel10 = new JLabel ();
    jEditCodigo2 = new JEditCodigo ();
    jLabel11 = new JLabel ();
    jLabel12 = new JLabel ();
    jEditCEP1 = new JEditCEP ();
    jEditCodigo3 = new JEditCodigo ();
    jButton1 = new JButton ();
    jLabel13 = new JLabel ();
    jEditMascara2 = new JEditMascara ();
    jLabel14 = new JLabel ();
    jEditTelefone1 = new JEditTelefone ();
    jLabel5.setText ("Tipo");
    jEditCodigo1.setInformacaoAssociada ("contribuinte.tipoLogradouro");
    jEditCodigo1.getComponenteFoco ().setToolTipText (tab.msg ("hint_tipo_logradouro"));
    jLabel6.setText ("Logradouro");
    jEditAlfa2.setInformacaoAssociada ("contribuinte.logradouro");
    jEditAlfa2.getComponenteFoco ().setToolTipText (tab.msg ("hint_logradouro"));
    jEditAlfa2.setMaxChars (40);
    jLabel7.setText ("N\u00famero");
    jEditAlfa3.setInformacaoAssociada ("contribuinte.numero");
    jEditAlfa3.getComponenteFoco ().setToolTipText (tab.msg ("hint_numero"));
    jEditAlfa3.setMaxChars (6);
    jLabel8.setText ("Complemento");
    jEditAlfa4.setInformacaoAssociada ("contribuinte.complemento");
    jEditAlfa4.getComponenteFoco ().setToolTipText (tab.msg ("hint_complemento"));
    jEditAlfa4.setMaxChars (21);
    jLabel9.setText ("Bairro/Distrito");
    jEditAlfa5.setInformacaoAssociada ("contribuinte.bairro");
    jEditAlfa5.getComponenteFoco ().setToolTipText (tab.msg ("hint_bairro"));
    jEditAlfa5.setMaxChars (19);
    jLabel10.setText ("UF");
    jEditCodigo2.setInformacaoAssociada ("contribuinte.uf");
    jEditCodigo2.getComponenteFoco ().setToolTipText (tab.msg ("hint_uf"));
    jLabel11.setText ("Munic\u00edpio");
    jLabel12.setText ("CEP");
    jEditCEP1.setCaracteresValidos ("0123456789 ");
    jEditCEP1.setInformacaoAssociada ("contribuinte.cep");
    jEditCEP1.getComponenteFoco ().setToolTipText (tab.msg ("hint_cep"));
    jEditCEP1.setMascara ("*****-***'");
    jEditCodigo3.setInformacaoAssociada ("contribuinte.municipio");
    jEditCodigo3.getComponenteFoco ().setToolTipText (tab.msg ("hint_municipio"));
    jButton1.setText ("Consulta CEP");
    jButton1.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent evt)
      {
	PainelEnderecoBrasil.this.jButton1botaoCepMouseClicked (evt);
      }
    });
    jLabel13.setText ("DDD 0xx");
    jEditMascara2.setCaracteresValidos ("0123456789 ");
    jEditMascara2.setInformacaoAssociada ("contribuinte.ddd");
    jEditMascara2.setMascara ("**'");
    jLabel14.setText ("Telefone");
    jEditTelefone1.setInformacaoAssociada ("contribuinte.telefone");
    jEditTelefone1.getComponenteFoco ().setToolTipText (tab.msg ("hint_telefone"));
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (2, jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (jLabel7).add (jLabel5).add (jLabel10).add (jLabel12)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jEditCodigo1, -2, 111, -2).add (jEditAlfa3, -2, -1, -2).add (jEditCEP1, -2, 111, -2).add (jEditCodigo2, -2, 111, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().add (jPanel1Layout.createParallelGroup (1).add (jLabel8).add (jLabel6).add (jLabel11)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jEditCodigo3, -1, 310, 32767).add (jPanel1Layout.createSequentialGroup ().add (jEditAlfa4, -2, -1, -2).addPreferredGap (0).add (jLabel9).addPreferredGap (0).add (jEditAlfa5, -1, 125, 32767)).add (2, jEditAlfa2, -1, 310, 32767))).add (jPanel1Layout.createSequentialGroup ().add (jButton1).add (18, 18, 18).add (jLabel13).addPreferredGap (0).add (jEditMascara2, -2, 55, -2).addPreferredGap (0).add (jLabel14).addPreferredGap (0).add (jEditTelefone1, -2, 113, -2))).addContainerGap ()));
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (2).add (jLabel5).add (jLabel6).add (jEditAlfa2, -2, -1, -2).add (jEditCodigo1, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (2, jLabel7).add (2, jEditAlfa3, -2, -1, -2).add (2, jLabel8).add (2, jEditAlfa4, -2, -1, -2).add (2, jLabel9).add (2, jEditAlfa5, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jLabel10).add (jEditCodigo3, -2, -1, -2).add (jLabel11).add (jEditCodigo2, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jEditCEP1, -2, -1, -2).add (jLabel12).add (jButton1).add (jLabel13).add (jEditTelefone1, -2, -1, -2).add (jLabel14).add (jEditMascara2, -2, -1, -2)).addContainerGap (-1, 32767)));
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (2, jPanel1, -1, -1, 32767));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jPanel1, -2, -1, -2).addContainerGap (-1, 32767)));
  }
  
  private void jButton1botaoCepMouseClicked (MouseEvent evt)
  {
    NavegadorHtml.executarNavegadorComMsgErro ("http://www.correios.com.br/servicos/cep/cep_default.cfm");
  }
}
