/* PainelEnderecoExterior - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.contribuinte;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditAlfa;
import serpro.ppgd.gui.xbeans.JEditCEP;
import serpro.ppgd.gui.xbeans.JEditColecao;
import serpro.ppgd.gui.xbeans.JEditMascara;
import serpro.ppgd.gui.xbeans.JEditTelefone;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelEnderecoExterior extends JPanel
{
  private static final TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
  private JEditColecao edtCodExterior;
  private JEditCEP edtZip;
  private JEditAlfa jEditAlfa1;
  private JEditAlfa jEditAlfa2;
  private JEditAlfa jEditAlfa3;
  private JEditAlfa jEditAlfa4;
  private JEditAlfa jEditAlfa5;
  private JEditColecao jEditColecao1;
  private JEditMascara jEditMascara2;
  private JEditTelefone jEditTelefone1;
  private JLabel jLabel1;
  private JLabel jLabel13;
  private JLabel jLabel14;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JLabel jLabel9;
  
  public PainelEnderecoExterior ()
  {
    initComponents ();
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, PgdIRPF.FONTE_DEFAULT.deriveFont (1));
    edtCodExterior.getBtnPopup ().setPreferredSize (new Dimension (30, edtCodExterior.getBtnPopup ().getPreferredSize ().height + 3));
  }
  
  private void initComponents ()
  {
    jEditAlfa2 = new JEditAlfa ();
    jLabel6 = new JLabel ();
    jLabel7 = new JLabel ();
    jEditAlfa3 = new JEditAlfa ();
    jLabel8 = new JLabel ();
    jEditAlfa4 = new JEditAlfa ();
    jLabel9 = new JLabel ();
    jEditAlfa5 = new JEditAlfa ();
    jLabel1 = new JLabel ();
    jEditColecao1 = new JEditColecao ();
    jLabel2 = new JLabel ();
    edtCodExterior = new JEditColecao ();
    jLabel3 = new JLabel ();
    jEditAlfa1 = new JEditAlfa ();
    jLabel4 = new JLabel ();
    edtZip = new JEditCEP ();
    jLabel13 = new JLabel ();
    jEditMascara2 = new JEditMascara ();
    jLabel14 = new JLabel ();
    jEditTelefone1 = new JEditTelefone ();
    jEditAlfa2.setInformacaoAssociada ("contribuinte.logradouroExt");
    jLabel6.setText ("Logradouro");
    jLabel7.setText ("N\u00famero");
    jEditAlfa3.setInformacaoAssociada ("contribuinte.numeroExt");
    jLabel8.setText ("Complemento");
    jEditAlfa4.setInformacaoAssociada ("contribuinte.complementoExt");
    jLabel9.setText ("Bairro/Distrito");
    jEditAlfa5.setInformacaoAssociada ("contribuinte.bairroExt");
    jLabel1.setText ("Pa\u00eds");
    jEditColecao1.setInformacaoAssociada ("contribuinte.pais");
    jEditColecao1.setCaracteresValidosTxtCodigo ("0123456789 ");
    jEditColecao1.setLblTextoVisivel (false);
    jEditColecao1.setMascaraTxtCodigo ("***'");
    jEditColecao1.setPesoLabel (0.1);
    jLabel2.setText ("C\u00f3d. Ext.");
    edtCodExterior.setInformacaoAssociada ("contribuinte.codigoExterior");
    edtCodExterior.setIconBtnPopup (new ImageIcon (this.getClass ().getResource ("/icones/ico_ocup.png")));
    edtCodExterior.setLblTextoVisivel (false);
    edtCodExterior.setMascaraTxtCodigo ("***");
    edtCodExterior.setPesoLabel (0.1);
    edtCodExterior.setTxtCodigoEditavel (false);
    edtCodExterior.setTxtCodigoHabilitado (false);
    jLabel3.setText ("Cidade");
    jEditAlfa1.setInformacaoAssociada ("contribuinte.cidade");
    jLabel4.setText ("ZIP");
    edtZip.setBrasileiro (false);
    edtZip.setInformacaoAssociada ("contribuinte.cepExt");
    jLabel13.setText ("DDI");
    jEditMascara2.setInformacaoAssociada ("contribuinte.ddd");
    jEditMascara2.setCaracteresValidos ("0123456789 ");
    jEditMascara2.setMascara ("****'");
    jLabel14.setText ("Telefone");
    jEditTelefone1.setInformacaoAssociada ("contribuinte.telefone");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jLabel6).addPreferredGap (0).add (jEditAlfa2, -1, 506, 32767)).add (layout.createSequentialGroup ().add (jLabel7).addPreferredGap (0).add (jEditAlfa3, -2, 97, -2).addPreferredGap (0).add (jLabel8).addPreferredGap (0).add (jEditAlfa4, -2, -1, -2).addPreferredGap (0).add (jLabel9, -2, 76, -2).addPreferredGap (0).add (jEditAlfa5, -1, 136, 32767)).add (layout.createSequentialGroup ().add (layout.createParallelGroup (2, false).add (1, layout.createSequentialGroup ().add (jLabel4).addPreferredGap (0).add (edtZip, -2, 111, -2).addPreferredGap (0, -1, 32767).add (jLabel13)).add (1, layout.createSequentialGroup ().add (jLabel1).addPreferredGap (0).add (jEditColecao1, -2, 97, -2).addPreferredGap (0).add (jLabel2, -2, 58, -2))).addPreferredGap (0).add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (edtCodExterior, -2, 95, -2).addPreferredGap (0).add (jLabel3, -2, 42, -2).addPreferredGap (0).add (jEditAlfa1, -1, 190, 32767)).add (layout.createSequentialGroup ().add (jEditMascara2, -2, 64, -2).addPreferredGap (0).add (jLabel14, -2, 53, -2).addPreferredGap (0).add (jEditTelefone1, -2, 113, -2).addContainerGap ()))))));
    layout.linkSize (new Component[] { jLabel1, jLabel4, jLabel6, jLabel7 }, 1);
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (2).add (jEditAlfa2, -2, -1, -2).add (jLabel6, -2, 20, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel7, -2, 20, -2).add (jEditAlfa3, -2, -1, -2).add (jLabel8, -2, 20, -2).add (jEditAlfa4, -2, -1, -2).add (jLabel9, -2, 20, -2).add (jEditAlfa5, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel1, -2, 23, -2).add (jEditColecao1, -2, -1, -2).add (jLabel2, -2, 23, -2).add (edtCodExterior, -2, -1, -2).add (jLabel3, -2, 23, -2).add (jEditAlfa1, -2, 23, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel4).add (edtZip, -2, -1, -2).add (jLabel14, -2, 23, -2).add (jEditTelefone1, -2, -1, -2).add (layout.createParallelGroup (1, false).add (2, jLabel13, -1, -1, 32767).add (2, jEditMascara2, -1, -1, 32767))).addContainerGap ()));
    layout.linkSize (new Component[] { jLabel1, jLabel2, jLabel3, jLabel4 }, 2);
  }
}
