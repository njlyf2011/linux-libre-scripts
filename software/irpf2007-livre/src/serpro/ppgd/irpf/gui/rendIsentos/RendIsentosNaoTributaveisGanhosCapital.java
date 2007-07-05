/* RendIsentosNaoTributaveisGanhosCapital - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.rendIsentos;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.ConstantesGlobaisGUI;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;

public class RendIsentosNaoTributaveisGanhosCapital extends JPanel
{
  private JButton btnAjuda;
  private JButton btnOk;
  private JEditValor edtBensPequenoValorInformado;
  private JEditValor edtBensPequenoValorTransportado;
  private JEditValor edtMEEspecieInformado;
  private JEditValor edtMEEspecieTransportado;
  private JEditValor edtOutrosBensImoveisInformado;
  private JEditValor edtOutrosBensImoveisTransportado;
  private JEditValor edtTotalInformado;
  private JEditValor edtTotalTransportado;
  private JEditValor edtUnicoImovelInformado;
  private JEditValor edtUnicoImovelTransportado;
  private JEditValor jEditValor11;
  private JLabel jLabel1;
  private JLabel jLabel10;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JLabel jLabel9;
  private JPanel jPanel1;
  
  public RendIsentosNaoTributaveisGanhosCapital ()
  {
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT;
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f.deriveFont (f.getSize2D () + 1.0F));
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jLabel2 = new JLabel ();
    jLabel3 = new JLabel ();
    jLabel4 = new JLabel ();
    jLabel5 = new JLabel ();
    edtBensPequenoValorInformado = new JEditValor ();
    edtBensPequenoValorTransportado = new JEditValor ();
    jLabel6 = new JLabel ();
    edtUnicoImovelInformado = new JEditValor ();
    edtUnicoImovelTransportado = new JEditValor ();
    jLabel7 = new JLabel ();
    edtOutrosBensImoveisInformado = new JEditValor ();
    edtOutrosBensImoveisTransportado = new JEditValor ();
    jLabel8 = new JLabel ();
    edtMEEspecieInformado = new JEditValor ();
    edtMEEspecieTransportado = new JEditValor ();
    jLabel9 = new JLabel ();
    edtTotalInformado = new JEditValor ();
    edtTotalTransportado = new JEditValor ();
    jLabel10 = new JLabel ();
    jEditValor11 = new JEditValor ();
    jPanel1 = new JPanel ();
    btnOk = new JButton ();
    btnAjuda = new JButton ();
    jLabel1.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel1.setText ("<HTML><B><P>Caso n\u00e3o tenha preenchido o Demonstrativo de Ganhos de Capital, por estar dispensado (veja Ajuda) e tenha rendimentos isentos/n\u00e3o-tribut\u00e1veis na aliena\u00e7\u00e3o de bens e/ou direitos, preencha abaixo a coluna da esquerda.</P><P>Informe tamb\u00e9m os rendimentos isentos e n\u00e3o-tribut\u00e1veis decorrentes de ganhos de capital \u2013moeda estrangeira (veja Ajuda).</P><P>A coluna da direita \u00e9 preenchida pelo pr\u00f3prio programa com dados transportados do Demonstrativo de Ganhos de Capital.</P></B></HTML>");
    jLabel1.setVerticalAlignment (1);
    jLabel2.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel2.setHorizontalAlignment (0);
    jLabel2.setText ("<HTML><B>Ganhos de Capital</B></HTML>");
    jLabel3.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel3.setHorizontalAlignment (0);
    jLabel3.setText ("<HTML><B><CENTER>Informados pelo Contribuinte</CENTER></B></HTML>");
    jLabel3.setVerticalAlignment (1);
    jLabel4.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel4.setText ("<HTML><B><CENTER>Transportados pelo programa</CENTER></B></HTML>");
    jLabel4.setVerticalAlignment (1);
    jLabel5.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel5.setText ("<HTML><B>A) Bens de pequeno valor</B></HTML>");
    edtBensPequenoValorInformado.setInformacaoAssociada ("rendIsentos.bensPequenoValorInformado");
    edtBensPequenoValorTransportado.setInformacaoAssociada ("rendIsentos.bensPequenoValorTransportado");
    jLabel6.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel6.setText ("<HTML><B>B) \u00danico im\u00f3vel</B></HTML>");
    edtUnicoImovelInformado.setInformacaoAssociada ("rendIsentos.unicoImovelInformado");
    edtUnicoImovelTransportado.setInformacaoAssociada ("rendIsentos.unicoImovelTransportado");
    jLabel7.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel7.setText ("<HTML><B>C) Outros bens im\u00f3veis (valor da redu\u00e7\u00e3o)</B></HTML>");
    edtOutrosBensImoveisInformado.setInformacaoAssociada ("rendIsentos.outrosBensImoveisInformado");
    edtOutrosBensImoveisTransportado.setInformacaoAssociada ("rendIsentos.outrosBensImoveisTransportado");
    jLabel8.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel8.setText ("<HTML><B>D) Moeda estrangeira em esp\u00e9cie </B></HTML>");
    edtMEEspecieInformado.setInformacaoAssociada ("rendIsentos.moedaEstrangeiraEspecieInformado");
    edtMEEspecieTransportado.setInformacaoAssociada ("rendIsentos.moedaEstrangeiraEspecieTransportado");
    jLabel9.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel9.setHorizontalAlignment (4);
    jLabel9.setText ("<HTML><B>TOTAIS    </B></HTML>");
    jLabel9.setVerticalAlignment (3);
    edtTotalInformado.setInformacaoAssociada ("rendIsentos.totalInformado");
    edtTotalTransportado.setInformacaoAssociada ("rendIsentos.totalTransportado");
    jLabel10.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    jLabel10.setText ("<HTML><B>Total a ser transportado para a linha 04</B></HTML>");
    jEditValor11.setInformacaoAssociada ("rendIsentos.lucroAlienacao");
    btnOk.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/OK.PNG")));
    btnOk.setMnemonic ('O');
    btnOk.setText ("<HTML><B><U>O</U>K</B></HTML>");
    btnOk.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	RendIsentosNaoTributaveisGanhosCapital.this.btnOkActionPerformed (evt);
      }
    });
    btnAjuda.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/HELP.PNG")));
    btnAjuda.setMnemonic ('A');
    btnAjuda.setText ("<HTML><B><U>A</U>juda</B></HTML>");
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (btnOk).addPreferredGap (0).add (btnAjuda).addContainerGap (-1, 32767)));
    jPanel1Layout.linkSize (new Component[] { btnAjuda, btnOk }, 1);
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (3).add (btnOk).add (btnAjuda)).addContainerGap (-1, 32767)));
    jPanel1Layout.linkSize (new Component[] { btnAjuda, btnOk }, 2);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (10, 10, 10).add (jLabel2, -1, 301, 32767).addContainerGap (307, 32767)).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jLabel1, -1, 598, 32767).addContainerGap ()).add (layout.createSequentialGroup ().add (layout.createParallelGroup (1).add (jLabel5, -1, 301, 32767).add (jLabel6, -1, 301, 32767).add (jLabel7, -1, 301, 32767).add (jLabel8, -1, 301, 32767).add (jLabel9, -1, 301, 32767).add (jLabel10, -1, 301, 32767)).addPreferredGap (0).add (layout.createParallelGroup (1).add (edtTotalInformado, -2, 127, -2).add (edtMEEspecieInformado, -2, 127, -2).add (edtOutrosBensImoveisInformado, -2, 127, -2).add (edtUnicoImovelInformado, -2, 127, -2).add (edtBensPequenoValorInformado, -2, 127, -2).add (jLabel3, -1, 127, 32767)).addPreferredGap (0).add (layout.createParallelGroup (1, false).add (jEditValor11, -2, -1, -2).add (edtTotalTransportado, -2, -1, -2).add (edtMEEspecieTransportado, -2, -1, -2).add (edtOutrosBensImoveisTransportado, -2, -1, -2).add (edtBensPequenoValorTransportado, -2, -1, -2).add (jLabel4, -1, 104, 32767).add (edtUnicoImovelTransportado, -2, -1, -2)).add (41, 41, 41)))).add (layout.createSequentialGroup ().addContainerGap ().add (jPanel1, -2, -1, -2).addContainerGap (408, 32767)));
    layout.linkSize (new Component[] { edtBensPequenoValorInformado, edtBensPequenoValorTransportado, edtMEEspecieInformado, edtMEEspecieTransportado, edtOutrosBensImoveisInformado, edtOutrosBensImoveisTransportado, edtTotalInformado, edtTotalTransportado, edtUnicoImovelInformado, edtUnicoImovelTransportado, jEditValor11, jLabel3, jLabel4 }, 1);
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel1, -2, 127, -2).addPreferredGap (0).add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (layout.createParallelGroup (1).add (jLabel2).add (layout.createParallelGroup (3).add (jLabel3).add (jLabel4, -2, 28, -2))).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel5, -2, 23, -2).add (edtBensPequenoValorInformado, -2, -1, -2))).add (2, edtBensPequenoValorTransportado, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel6).add (edtUnicoImovelInformado, -2, -1, -2).add (edtUnicoImovelTransportado, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel7).add (edtOutrosBensImoveisInformado, -2, -1, -2).add (edtOutrosBensImoveisTransportado, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel8).add (edtMEEspecieInformado, -2, -1, -2).add (edtMEEspecieTransportado, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel9).add (edtTotalInformado, -2, -1, -2).add (edtTotalTransportado, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel10).add (jEditValor11, -2, -1, -2)).addPreferredGap (0).add (jPanel1, -2, -1, -2).addContainerGap (-1, 32767)));
    layout.linkSize (new Component[] { jLabel10, jLabel5, jLabel6, jLabel7, jLabel8, jLabel9 }, 2);
    layout.linkSize (new Component[] { jLabel2, jLabel3, jLabel4 }, 2);
  }
  
  private void btnOkActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
}
