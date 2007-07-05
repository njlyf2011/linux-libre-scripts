/* PainelBensImoveisIdentificacao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.ganhosdecapital.bensimoveis;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.gui.xbeans.JEditAlfa;
import serpro.ppgd.gui.xbeans.JEditCEP;
import serpro.ppgd.gui.xbeans.JEditData;
import serpro.ppgd.gui.xbeans.JEditLogico;
import serpro.ppgd.gui.xbeans.JEditNirf;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.irpf.gui.PainelIRPFIf;

public class PainelBensImoveisIdentificacao extends JPanel implements PainelIRPFIf
{
  private JButton jButton1;
  private JEditAlfa jEditAlfa1;
  private JEditAlfa jEditAlfa2;
  private JEditAlfa jEditAlfa3;
  private JEditAlfa jEditAlfa4;
  private JEditAlfa jEditAlfa5;
  private JEditAlfa jEditAlfa6;
  private JEditAlfa jEditAlfa7;
  private JEditAlfa jEditAlfa8;
  private JEditAlfa jEditAlfa9;
  private JEditCEP jEditCEP1;
  private JEditData jEditData1;
  private JEditData jEditData2;
  private JEditLogico jEditLogico1;
  private JEditLogico jEditLogico2;
  private JEditNirf jEditNirf1;
  private JEditValor jEditValor1;
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
  private JPanel jPanel3;
  
  public PainelBensImoveisIdentificacao ()
  {
    initComponents ();
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jPanel1 = new JPanel ();
    jLabel2 = new JLabel ();
    jEditAlfa1 = new JEditAlfa ();
    jEditNirf1 = new JEditNirf ();
    jLabel3 = new JLabel ();
    jPanel2 = new JPanel ();
    jLabel4 = new JLabel ();
    jEditAlfa2 = new JEditAlfa ();
    jLabel5 = new JLabel ();
    jEditAlfa3 = new JEditAlfa ();
    jEditAlfa4 = new JEditAlfa ();
    jEditAlfa5 = new JEditAlfa ();
    jLabel6 = new JLabel ();
    jLabel7 = new JLabel ();
    jLabel8 = new JLabel ();
    jEditAlfa6 = new JEditAlfa ();
    jEditCEP1 = new JEditCEP ();
    jEditAlfa7 = new JEditAlfa ();
    jEditAlfa8 = new JEditAlfa ();
    jLabel9 = new JLabel ();
    jLabel10 = new JLabel ();
    jLabel11 = new JLabel ();
    jPanel3 = new JPanel ();
    jLabel12 = new JLabel ();
    jEditAlfa9 = new JEditAlfa ();
    jEditData1 = new JEditData ();
    jEditData2 = new JEditData ();
    jLabel13 = new JLabel ();
    jLabel14 = new JLabel ();
    jLabel15 = new JLabel ();
    jEditValor1 = new JEditValor ();
    jLabel16 = new JLabel ();
    jEditLogico1 = new JEditLogico ();
    jLabel17 = new JLabel ();
    jEditLogico2 = new JEditLogico ();
    jButton1 = new JButton ();
    jLabel1.setText ("01 -");
    jPanel1.setBorder (BorderFactory.createTitledBorder ("Adquirente"));
    jLabel2.setText ("Nome");
    jLabel3.setText ("CPF/CNPJ");
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (2, jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().add (jEditAlfa1, -1, 401, 32767).addPreferredGap (0)).add (jPanel1Layout.createSequentialGroup ().add (jLabel2, -1, 178, 32767).add (229, 229, 229))).add (jPanel1Layout.createParallelGroup (1).add (jLabel3).add (jEditNirf1, -2, 125, -2)).addContainerGap ()));
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().add (jPanel1Layout.createParallelGroup (1).add (jLabel2).add (jLabel3)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jEditAlfa1, -2, -1, -2).add (jEditNirf1, -2, -1, -2)).addContainerGap (-1, 32767)));
    jPanel2.setBorder (BorderFactory.createTitledBorder ("Dados do Im\u00f3vel"));
    jLabel4.setText ("Especifica\u00e7\u00e3o");
    jLabel5.setText ("Rua, Avenida, Pra\u00e7a, etc.");
    jLabel6.setText ("N\u00famero");
    jLabel7.setText ("Complemento");
    jLabel8.setText ("Bairro");
    jLabel9.setText ("CEP");
    jLabel10.setText ("Munic\u00edpio");
    jLabel11.setText ("UF");
    GroupLayout jPanel2Layout = new GroupLayout (jPanel2);
    jPanel2.setLayout (jPanel2Layout);
    jPanel2Layout.setHorizontalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().addContainerGap ().add (jPanel2Layout.createParallelGroup (1).add (jEditAlfa2, -1, 532, 32767).add (jLabel4).add (jPanel2Layout.createSequentialGroup ().add (jPanel2Layout.createParallelGroup (1).add (jEditAlfa3, -1, 254, 32767).add (jLabel5)).addPreferredGap (0).add (jPanel2Layout.createParallelGroup (1).add (jEditAlfa4, -2, 68, -2).add (jLabel6)).addPreferredGap (0).add (jPanel2Layout.createParallelGroup (1).add (jLabel7).add (jEditAlfa5, -1, 198, 32767))).add (jPanel2Layout.createSequentialGroup ().add (jPanel2Layout.createParallelGroup (1).add (jEditAlfa6, -1, 182, 32767).add (jLabel8)).addPreferredGap (0).add (jPanel2Layout.createParallelGroup (1).add (jEditCEP1, -2, 99, -2).add (jLabel9)).addPreferredGap (0).add (jPanel2Layout.createParallelGroup (1).add (jEditAlfa7, -1, 179, 32767).add (jLabel10)).addPreferredGap (0).add (jPanel2Layout.createParallelGroup (1, false).add (jLabel11).add (jEditAlfa8, -2, 54, -2)))).addContainerGap ()));
    jPanel2Layout.setVerticalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().add (jLabel4).addPreferredGap (0).add (jEditAlfa2, -2, -1, -2).addPreferredGap (0).add (jPanel2Layout.createParallelGroup (2).add (jLabel5).add (jLabel6).add (jLabel7)).addPreferredGap (0).add (jPanel2Layout.createParallelGroup (1).add (jEditAlfa3, -2, -1, -2).add (jEditAlfa4, -2, -1, -2).add (jEditAlfa5, -2, -1, -2)).addPreferredGap (0).add (jPanel2Layout.createParallelGroup (1).add (jLabel8).add (jLabel9).add (jLabel10).add (jLabel11)).addPreferredGap (0).add (jPanel2Layout.createParallelGroup (1).add (jEditAlfa6, -2, -1, -2).add (jEditCEP1, -2, -1, -2).add (jEditAlfa7, -2, -1, -2).add (jEditAlfa8, -2, -1, -2)).addContainerGap (-1, 32767)));
    jPanel3.setBorder (BorderFactory.createTitledBorder ("Dados da Opera\u00e7\u00e3o"));
    jLabel12.setText ("Descri\u00e7\u00e3o");
    jLabel13.setText ("Data da aquisi\u00e7\u00e3o");
    jLabel14.setText ("Data de aliena\u00e7\u00e3o");
    jLabel15.setText ("Valor da aliena\u00e7\u00e3o (R$)");
    jLabel16.setText ("<html><p>A aliena\u00e7\u00e3o foi a prazo?</p></html>");
    jLabel17.setText ("<html><p>Houve edifica\u00e7\u00e3o, reforma ou amplia\u00e7\u00e3o no im\u00f3vel a partir de 01/06/1996?</p></html>");
    GroupLayout jPanel3Layout = new GroupLayout (jPanel3);
    jPanel3.setLayout (jPanel3Layout);
    jPanel3Layout.setHorizontalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().addContainerGap ().add (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().add (jEditAlfa9, -1, 299, 32767).addPreferredGap (0)).add (jPanel3Layout.createSequentialGroup ().add (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().add (jPanel3Layout.createParallelGroup (1).add (jEditValor1, -1, 122, 32767).add (2, jLabel15, -1, -1, 32767)).addPreferredGap (0).add (jLabel16, -2, 72, -2).addPreferredGap (0).add (jEditLogico1, -2, -1, -2).addPreferredGap (0)).add (jLabel12)).add (14, 14, 14))).add (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().add (jLabel17, -2, 124, -2).addPreferredGap (0).add (jEditLogico2, -2, -1, -2)).add (jPanel3Layout.createSequentialGroup ().add (jPanel3Layout.createParallelGroup (1).add (jEditData1, -2, 107, -2).add (jLabel13)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (jEditData2, -2, 114, -2).add (jLabel14)))).addContainerGap ()));
    jPanel3Layout.setVerticalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().add (jPanel3Layout.createParallelGroup (1).add (jLabel12).add (jLabel13).add (jLabel14)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (jEditData1, -2, -1, -2).add (jEditAlfa9, -2, -1, -2).add (jEditData2, -2, -1, -2)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().add (jLabel15).addPreferredGap (0).add (jEditValor1, -2, -1, -2).add (34, 34, 34)).add (jLabel16).add (jEditLogico1, -2, -1, -2).add (jPanel3Layout.createParallelGroup (2, false).add (1, jLabel17, 0, 0, 32767).add (1, jEditLogico2, -1, -1, 32767))).addContainerGap ()));
    jButton1.setText ("Sair");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jPanel2, -1, -1, 32767).add (jPanel1, -1, -1, 32767).add (layout.createSequentialGroup ().add (jLabel1).addPreferredGap (0, 491, 32767).add (jButton1)).add (jPanel3, -1, -1, 32767)).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (2).add (jLabel1).add (jButton1)).addPreferredGap (0).add (jPanel1, -2, -1, -2).addPreferredGap (0).add (jPanel2, -2, -1, -2).addPreferredGap (0).add (jPanel3, -2, -1, -2).addContainerGap (-1, 32767)));
  }
  
  public String getTituloPainel ()
  {
    return "Ganhos de Capitais - Bens Im\u00f3veis";
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
