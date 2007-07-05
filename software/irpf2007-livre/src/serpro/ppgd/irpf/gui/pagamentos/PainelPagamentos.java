/* PainelPagamentos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.pagamentos;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditAlfa;
import serpro.ppgd.gui.xbeans.JEditColecao;
import serpro.ppgd.gui.xbeans.JEditMascara;
import serpro.ppgd.gui.xbeans.JEditNI;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.gui.xbeans.JNavegadorColecao;
import serpro.ppgd.gui.xbeans.JNavegadorIndice;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoListener;
import serpro.ppgd.infraestrutura.util.NavegadorRemocaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorRemocaoListener;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.pagamentos.Pagamento;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelPagamentos extends JPanel implements PainelIRPFIf
{
  private static final String SUB_TIT = "Dados do pagamento de n\u00ba";
  private static final String TITULO = "Pagamentos e Doa\u00e7\u00f5es Efetuados";
  private JEditColecao edtCodigo;
  private EditDependenteAlimentando edtDependenteAlimentando;
  private JEditNI edtNi;
  private JEditMascara edtNit;
  private JEditAlfa jEditAlfa1;
  private JEditValor jEditValor1;
  private JEditValor jEditValor2;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JLabel jLabel9;
  private JNavegadorColecao jNavegadorColecao1;
  private JNavegadorIndice jNavegadorIndice1;
  private JPanel jPanel1;
  private JTextField jTextField1;
  private JLabel lblNi;
  private JLabel lblNit;
  
  public String getTituloPainel ()
  {
    return "Pagamentos e Doa\u00e7\u00f5es Efetuados";
  }
  
  public PainelPagamentos ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "Doacoes");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (1);
    f = f.deriveFont (f.getSize2D () + 2.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
    reinitEdits ();
    jNavegadorColecao1.primeiro ();
    lblNi.setEnabled (edtNi.getInformacao ().isHabilitado ());
    lblNit.setEnabled (edtNit.getInformacao ().isHabilitado ());
    edtNi.getComponenteEditor ().addPropertyChangeListener (new PropertyChangeListener ()
    {
      public void propertyChange (PropertyChangeEvent evt)
      {
	lblNi.setEnabled (edtNi.getInformacao ().isHabilitado ());
      }
    });
    edtNit.getComponenteEditor ().addPropertyChangeListener (new PropertyChangeListener ()
    {
      public void propertyChange (PropertyChangeEvent evt)
      {
	lblNit.setEnabled (edtNit.getInformacao ().isHabilitado ());
      }
    });
  }
  
  public JNavegadorColecao getNavegador ()
  {
    return jNavegadorColecao1;
  }
  
  private void reinitEdits ()
  {
    edtNi.getInformacao ().setHabilitado (false);
    jEditAlfa1.getInformacao ().setHabilitado (false);
    edtDependenteAlimentando.getInformacao ().setHabilitado (false);
    edtCodigo.getInformacao ().setHabilitado (false);
    jEditValor1.getInformacao ().setHabilitado (false);
    jEditValor2.getInformacao ().setHabilitado (false);
    edtNit.getInformacao ().setHabilitado (false);
    jLabel2.setText ("Dados do pagamento de n\u00ba");
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jNavegadorColecao1 = new JNavegadorColecao ();
    jLabel2 = new JLabel ();
    jPanel1 = new JPanel ();
    jLabel3 = new JLabel ();
    jLabel4 = new JLabel ();
    jEditAlfa1 = new JEditAlfa ();
    lblNi = new JLabel ();
    edtNi = new JEditNI ();
    edtCodigo = new JEditColecao ();
    jLabel8 = new JLabel ();
    edtDependenteAlimentando = new EditDependenteAlimentando ();
    jLabel6 = new JLabel ();
    jEditValor1 = new JEditValor ();
    jLabel7 = new JLabel ();
    jEditValor2 = new JEditValor ();
    lblNit = new JLabel ();
    edtNit = new JEditMascara ();
    jTextField1 = new JTextField ();
    jNavegadorIndice1 = new JNavegadorIndice ();
    jLabel9 = new JLabel ();
    jLabel1.setText ("Consulta/Atualiza\u00e7\u00e3o");
    jNavegadorColecao1.setInformacaoAssociada ("pagamentos");
    jNavegadorColecao1.addNavegadorColecaoListener (new NavegadorColecaoListener ()
    {
      public void exibeColecaoVazia (NavegadorColecaoEvent evt)
      {
	PainelPagamentos.this.jNavegadorColecao1ExibeColecaoVazia (evt);
      }
      
      public void exibeOutro (NavegadorColecaoEvent evt)
      {
	PainelPagamentos.this.jNavegadorColecao1ExibeOutro (evt);
      }
    });
    jNavegadorColecao1.addNavegadorRemocaoListener (new NavegadorRemocaoListener ()
    {
      public boolean confirmaExclusao (NavegadorRemocaoEvent evt)
      {
	return PainelPagamentos.this.jNavegadorColecao1ConfirmaExclusao (evt);
      }
      
      public void objetoExcluido (NavegadorRemocaoEvent evt)
      {
	/* empty */
      }
    });
    jLabel2.setHorizontalAlignment (0);
    jLabel2.setText ("Dados do pagamento de n\u00ba");
    jLabel3.setText ("C\u00f3digo");
    jLabel4.setText ("<HTML>Nome do<BR>benefici\u00e1rio</HTML>");
    lblNi.setText ("CPF/CNPJ do benefici\u00e1rio");
    edtCodigo.setTituloDialogo ("Tipo de Pagamento");
    edtCodigo.setCaracteresValidosTxtCodigo ("0123456789 ");
    edtCodigo.setMascaraTxtCodigo ("**'");
    jLabel8.setText ("Nome do dependente/alimentando");
    jLabel6.setText ("Valor pago");
    jLabel7.setText ("Parcela n\u00e3o-dedut\u00edvel/valor reembolsado");
    lblNit.setText ("NIT do empregado dom\u00e9stico");
    edtNit.setCaracteresInvalidos ("");
    edtNit.setCaracteresValidos ("0123456789");
    edtNit.setMascara ("***.*****.**-*");
    jTextField1.setBorder (null);
    jTextField1.addFocusListener (new FocusAdapter ()
    {
      public void focusGained (FocusEvent evt)
      {
	PainelPagamentos.this.jTextField1FocusGained (evt);
      }
    });
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().add (jLabel3).add (17, 17, 17).add (edtCodigo, -1, 455, 32767)).add (jPanel1Layout.createSequentialGroup ().add (jLabel8, -1, 194, 32767).add (42, 42, 42).add (edtDependenteAlimentando, -1, 269, 32767)).add (jPanel1Layout.createSequentialGroup ().add (jLabel4).addPreferredGap (0).add (jEditAlfa1, -1, 444, 32767)).add (jPanel1Layout.createSequentialGroup ().add (jPanel1Layout.createParallelGroup (1).add (jLabel7).add (jLabel6).add (lblNi).add (lblNit)).add (192, 192, 192).add (jPanel1Layout.createParallelGroup (1).add (2, edtNit, -1, 117, 32767).add (jEditValor1, -1, 117, 32767).add (jEditValor2, -1, 117, 32767).add (2, edtNi, -1, 117, 32767).add (jTextField1, -2, -1, -2)))).addContainerGap ()));
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (jLabel3).add (edtCodigo, -2, -1, -2)).add (24, 24, 24).add (jPanel1Layout.createParallelGroup (2).add (jLabel8, -2, 22, -2).add (edtDependenteAlimentando, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jLabel4).add (jEditAlfa1, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (edtNi, -2, -1, -2).add (lblNi)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (edtNit, -2, -1, -2).add (lblNit)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jLabel6, -2, 20, -2).add (jEditValor1, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1, false).add (jLabel7, -1, -1, 32767).add (jEditValor2, -1, -1, 32767)).add (40, 40, 40).add (jTextField1, -2, -1, -2).addContainerGap ()));
    jNavegadorIndice1.setNavegadorColecao (jNavegadorColecao1);
    jLabel9.setText ("Pesquisar por n\u00b0 do item:");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (2, jLabel2, -1, 529, 32767).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jLabel1).addPreferredGap (0, 143, 32767).add (jLabel9).addPreferredGap (0).add (jNavegadorIndice1, -2, -1, -2).add (70, 70, 70)).add (layout.createSequentialGroup ().add (jNavegadorColecao1, -2, -1, -2).addContainerGap (345, 32767)))).add (jPanel1, -1, -1, 32767));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (layout.createParallelGroup (3).add (jLabel1).add (jLabel9, -2, 20, -2)).addPreferredGap (0).add (jNavegadorColecao1, -2, -1, -2)).add (jNavegadorIndice1, -2, -1, -2)).addPreferredGap (0).add (jLabel2).addPreferredGap (0).add (jPanel1, -1, 291, 32767).addContainerGap ()));
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
    if (JOptionPane.showConfirmDialog (getParent (), tab.msg ("pagamento_exclusao"), "Confirma\u00e7\u00e3o", 0) == 0)
      return true;
    return false;
  }
  
  private void jNavegadorColecao1ExibeOutro (NavegadorColecaoEvent evt)
  {
    Pagamento pagamento = (Pagamento) evt.getObjetoNegocio ();
    int index = jNavegadorColecao1.getIndiceAtual () + 1;
    jLabel2.setText ("Dados do pagamento de n\u00ba " + String.valueOf (index));
    edtNi.setInformacao (pagamento.getNiBeneficiario ());
    jEditAlfa1.setInformacao (pagamento.getNomeBeneficiario ());
    edtCodigo.setInformacao (pagamento.getCodigo ());
    edtDependenteAlimentando.setInformacao (pagamento.getDependenteOuAlimentando ());
    jEditValor1.setInformacao (pagamento.getValorPago ());
    jEditValor2.setInformacao (pagamento.getParcelaNaoDedutivel ());
    edtNit.setInformacao (pagamento.getNitEmpregadoDomestico ());
    lblNi.setEnabled (edtNi.getInformacao ().isHabilitado ());
    edtCodigo.getComponenteFoco ().grabFocus ();
  }
  
  private void jNavegadorColecao1ExibeColecaoVazia (NavegadorColecaoEvent evt)
  {
    reinitEdits ();
  }
  
  public void vaiExibir ()
  {
    edtDependenteAlimentando.atualizaModel ();
    jNavegadorColecao1.exibe (jNavegadorColecao1.getIndiceAtual ());
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
