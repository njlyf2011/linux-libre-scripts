/* PainelRendPJTitular - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.rendpj;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditAlfa;
import serpro.ppgd.gui.xbeans.JEditNI;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.gui.xbeans.JNavegadorColecao;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoListener;
import serpro.ppgd.infraestrutura.util.NavegadorRemocaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorRemocaoListener;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.rendpj.RendPJTitular;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelRendPJTitular extends JPanel implements PainelIRPFIf
{
  private static final String TITULO = "Rendimentos Tribut\u00e1veis Recebidos de PJ pelo Titular";
  private static final String SUB_TIT = "Dados da fonte pagadora de n\u00ba ";
  private JEditAlfa jEditAlfa1;
  private JEditAlfa jEditCPF1;
  private JEditNI jEditNI1;
  private JEditValor jEditValor1;
  private JEditValor jEditValor2;
  private JEditValor jEditValor3;
  private JEditValor jEditValor4;
  private JEditValor jEditValor5;
  private JEditValor jEditValor6;
  private JEditValor jEditValor7;
  private JEditValor jEditValor8;
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
  private JNavegadorColecao jNavegadorColecao1;
  private JPanel jPanel1;
  private JTextField jTextField1;
  
  public String getTituloPainel ()
  {
    return "Rendimentos Tribut\u00e1veis Recebidos de PJ pelo Titular";
  }
  
  public PainelRendPJTitular ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "RendimentosPJTC");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (1);
    f = f.deriveFont (f.getSize2D () + 2.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
    reinitEdits ();
    jNavegadorColecao1.primeiro ();
    jEditCPF1.getInformacao ().setReadOnly (true);
  }
  
  public JNavegadorColecao getNavegador ()
  {
    return jNavegadorColecao1;
  }
  
  private void reinitEdits ()
  {
    jEditAlfa1.getInformacao ().setHabilitado (false);
    jEditNI1.getInformacao ().setHabilitado (false);
    jEditValor1.getInformacao ().setHabilitado (false);
    jEditValor2.getInformacao ().setHabilitado (false);
    jEditValor3.getInformacao ().setHabilitado (false);
    jEditValor4.getInformacao ().setHabilitado (false);
    jLabel3.setText ("Dados da fonte pagadora de n\u00ba ");
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jNavegadorColecao1 = new JNavegadorColecao ();
    jLabel2 = new JLabel ();
    jEditCPF1 = new JEditAlfa ();
    jPanel1 = new JPanel ();
    jLabel3 = new JLabel ();
    jLabel5 = new JLabel ();
    jEditAlfa1 = new JEditAlfa ();
    jLabel6 = new JLabel ();
    jEditNI1 = new JEditNI ();
    jLabel7 = new JLabel ();
    jEditValor1 = new JEditValor ();
    jLabel8 = new JLabel ();
    jEditValor2 = new JEditValor ();
    jLabel9 = new JLabel ();
    jEditValor3 = new JEditValor ();
    jLabel10 = new JLabel ();
    jEditValor4 = new JEditValor ();
    jLabel4 = new JLabel ();
    jEditValor5 = new JEditValor ();
    jEditValor6 = new JEditValor ();
    jEditValor7 = new JEditValor ();
    jEditValor8 = new JEditValor ();
    jTextField1 = new JTextField ();
    jLabel1.setText ("Consulta/Atualiza\u00e7\u00e3o");
    jLabel1.setHorizontalTextPosition (0);
    jNavegadorColecao1.setInformacaoAssociada ("colecaoRendPJTitular");
    jNavegadorColecao1.addNavegadorColecaoListener (new NavegadorColecaoListener ()
    {
      public void exibeColecaoVazia (NavegadorColecaoEvent evt)
      {
	PainelRendPJTitular.this.exibeColecaoVazia (evt);
      }
      
      public void exibeOutro (NavegadorColecaoEvent evt)
      {
	PainelRendPJTitular.this.jNavegadorColecao1ExibeOutro (evt);
      }
    });
    jNavegadorColecao1.addNavegadorRemocaoListener (new NavegadorRemocaoListener ()
    {
      public boolean confirmaExclusao (NavegadorRemocaoEvent evt)
      {
	return PainelRendPJTitular.this.jNavegadorColecao1ConfirmaExclusao (evt);
      }
      
      public void objetoExcluido (NavegadorRemocaoEvent evt)
      {
	/* empty */
      }
    });
    jLabel2.setText ("CNPJ/CPF da principal fonte pagadora");
    jEditCPF1.setInformacaoAssociada ("colecaoRendPJTitular.niMaiorFontePagadora");
    jLabel3.setHorizontalAlignment (0);
    jLabel3.setText ("Dados da fonte pagadora de n\u00ba ");
    jLabel5.setText ("Nome da fonte pagadora");
    jEditAlfa1.setMaxChars (60);
    jLabel6.setText ("CPF/CNPJ da fonte pagadora");
    jLabel7.setText ("<HTML>Rendimentos recebidos de pessoa jur\u00eddica</HTML>");
    jLabel8.setText ("Contribui\u00e7\u00e3o previdenci\u00e1ria oficial");
    jLabel9.setText ("Imposto retido na fonte");
    jLabel10.setText ("13\u00ba sal\u00e1rio");
    jLabel4.setHorizontalAlignment (0);
    jLabel4.setText ("Totais");
    jEditValor5.setAceitaFoco (false);
    jEditValor5.setInformacaoAssociada ("colecaoRendPJTitular.totaisRendRecebidoPJ");
    jEditValor6.setAceitaFoco (false);
    jEditValor6.setInformacaoAssociada ("colecaoRendPJTitular.totaisContribuicaoPrevOficial");
    jEditValor7.setAceitaFoco (false);
    jEditValor7.setInformacaoAssociada ("colecaoRendPJTitular.totaisImpostoRetidoFonte");
    jEditValor8.setAceitaFoco (false);
    jEditValor8.setInformacaoAssociada ("colecaoRendPJTitular.totaisDecimoTerceiro");
    jTextField1.setBorder (null);
    jTextField1.addFocusListener (new FocusAdapter ()
    {
      public void focusGained (FocusEvent evt)
      {
	PainelRendPJTitular.this.jTextField1FocusGained (evt);
      }
    });
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jLabel3, -1, 546, 32767).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (jLabel5).add (jLabel6)).add (26, 26, 26).add (jPanel1Layout.createParallelGroup (1).add (jEditAlfa1, -1, 359, 32767).add (jEditNI1, -2, 169, -2)).addContainerGap ()).add (2, jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (jLabel9, -1, 248, 32767).add (jLabel10, -1, 248, 32767).add (jLabel8, -1, 248, 32767).add (jLabel7, -1, 248, 32767)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jTextField1, -2, -1, -2).add (jPanel1Layout.createSequentialGroup ().add (jPanel1Layout.createParallelGroup (2, false).add (jEditValor1, -2, 134, -2).add (jEditValor3, -2, -1, -2).add (jEditValor4, -2, -1, -2).add (jEditValor2, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1, false).add (jLabel4, -1, -1, 32767).add (2, jEditValor5, -2, 134, -2).add (2, jEditValor6, -2, 134, -2).add (2, jEditValor8, -2, -1, -2).add (2, jEditValor7, -2, -1, -2)))).addContainerGap ()));
    jPanel1Layout.linkSize (new Component[] { jEditValor1, jEditValor2, jEditValor3, jEditValor4, jEditValor5, jEditValor6, jEditValor7, jEditValor8 }, 1);
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().add (jLabel3).add (33, 33, 33).add (jPanel1Layout.createParallelGroup (2).add (jLabel5).add (jEditAlfa1, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jLabel6).add (jEditNI1, -2, -1, -2)).add (23, 23, 23).add (jLabel4).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jEditValor5, -2, -1, -2).add (jEditValor1, -2, -1, -2).add (jLabel7)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jLabel8, -2, 20, -2).add (jEditValor6, -2, -1, -2).add (jEditValor2, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jLabel9, -2, 20, -2).add (jEditValor3, -2, -1, -2).add (jEditValor7, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jLabel10, -2, 20, -2).add (jEditValor4, -2, -1, -2).add (jEditValor8, -2, -1, -2)).addPreferredGap (0).add (jTextField1, -2, -1, -2).addContainerGap (40, 32767)));
    jPanel1Layout.linkSize (new Component[] { jEditValor1, jEditValor2, jEditValor3, jEditValor4, jEditValor5, jEditValor6, jEditValor7, jEditValor8 }, 2);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jNavegadorColecao1, -2, 208, -2).add (jLabel1)).add (12, 12, 12).add (layout.createParallelGroup (1).add (jLabel2).add (jEditCPF1, -2, 162, -2)).addContainerGap (133, 32767)).add (jPanel1, -1, -1, 32767));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (3).add (jLabel2).add (jLabel1, -2, 14, -2)).addPreferredGap (0).add (layout.createParallelGroup (2, false).add (jNavegadorColecao1, -2, 21, -2).add (jEditCPF1, -2, -1, -2)).add (17, 17, 17).add (jPanel1, -1, -1, 32767).addContainerGap (-1, 32767)));
  }
  
  private void jTextField1FocusGained (FocusEvent evt)
  {
    if (jNavegadorColecao1.getIndiceAtual () + 1 >= jNavegadorColecao1.getColecao ().recuperarLista ().size ())
      {
	if (jNavegadorColecao1.getIndiceAtual () != -1)
	  jNavegadorColecao1.adiciona ();
      }
    else
      jNavegadorColecao1.proximo ();
  }
  
  private boolean jNavegadorColecao1ConfirmaExclusao (NavegadorRemocaoEvent evt)
  {
    TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
    if (JOptionPane.showConfirmDialog (getParent (), tab.msg ("rendpj_exclusao"), "Confirma\u00e7\u00e3o", 0) == 0)
      return true;
    return false;
  }
  
  private void jNavegadorColecao1ExibeOutro (NavegadorColecaoEvent evt)
  {
    RendPJTitular rendPJ = (RendPJTitular) evt.getObjetoNegocio ();
    int index = jNavegadorColecao1.getIndiceAtual () + 1;
    jLabel3.setText ("Dados da fonte pagadora de n\u00ba  " + String.valueOf (index));
    jEditAlfa1.setInformacao (rendPJ.getNomeFontePagadora ());
    jEditNI1.setInformacao (rendPJ.getNIFontePagadora ());
    jEditValor1.setInformacao (rendPJ.getRendRecebidoPJ ());
    jEditValor2.setInformacao (rendPJ.getContribuicaoPrevOficial ());
    jEditValor3.setInformacao (rendPJ.getImpostoRetidoFonte ());
    jEditValor4.setInformacao (rendPJ.getDecimoTerceiro ());
    jEditAlfa1.setaFoco (false);
  }
  
  private void exibeColecaoVazia (NavegadorColecaoEvent evt)
  {
    reinitEdits ();
  }
  
  public void vaiExibir ()
  {
    if (IRPFUtil.getEstadoSistema () != 1)
      {
	if (jNavegadorColecao1.getColecao ().recuperarLista ().size () == 0)
	  jNavegadorColecao1.adiciona ();
	if (jNavegadorColecao1.getColecao ().recuperarLista ().size () > 1)
	  jNavegadorColecao1.getColecao ().excluirRegistrosEmBranco ();
	jNavegadorColecao1.primeiro ();
      }
  }
  
  public void painelExibido ()
  {
    /* empty */
  }
}
