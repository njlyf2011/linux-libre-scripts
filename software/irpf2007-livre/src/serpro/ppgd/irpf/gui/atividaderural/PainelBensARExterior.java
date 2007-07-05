/* PainelBensARExterior - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.atividaderural;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditColecao;
import serpro.ppgd.gui.xbeans.JEditMemo;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.gui.xbeans.JNavegadorColecao;
import serpro.ppgd.gui.xbeans.JNavegadorIndice;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoListener;
import serpro.ppgd.infraestrutura.util.NavegadorRemocaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorRemocaoListener;
import serpro.ppgd.irpf.atividaderural.exterior.BemARExterior;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelBensARExterior extends JPanel implements PainelIRPFIf
{
  private static final String TITULO = "Bens da Atividade Rural - Exterior";
  private static final String SUB_TIT = "Dados do bem de n\u00ba";
  private JEditColecao edtCodigo;
  private JEditMemo edtDiscriminacao;
  private JEditColecao edtPais;
  private JEditValor edtValor;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JNavegadorColecao jNavegadorColecao1;
  private JNavegadorIndice jNavegadorIndice1;
  private JPanel jPanel1;
  private JTextField jTextField1;
  
  public String getTituloPainel ()
  {
    return "Bens da Atividade Rural - Exterior";
  }
  
  public PainelBensARExterior ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "ARBens");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (1);
    f = f.deriveFont (f.getSize2D () + 2.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
    reinitEdits ();
    jNavegadorColecao1.primeiro ();
  }
  
  public JNavegadorColecao getNavegador ()
  {
    return jNavegadorColecao1;
  }
  
  private void reinitEdits ()
  {
    edtPais.getInformacao ().setHabilitado (false);
    edtCodigo.getInformacao ().setHabilitado (false);
    edtDiscriminacao.getInformacao ().setHabilitado (false);
    edtValor.getInformacao ().setHabilitado (false);
    edtValor.getComponenteEditor ().setFocusable (true);
    jLabel2.setText ("Dados do bem de n\u00ba");
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jNavegadorColecao1 = new JNavegadorColecao ();
    jLabel2 = new JLabel ();
    jPanel1 = new JPanel ();
    jLabel3 = new JLabel ();
    edtPais = new JEditColecao ();
    jLabel4 = new JLabel ();
    edtCodigo = new JEditColecao ();
    jLabel5 = new JLabel ();
    edtDiscriminacao = new JEditMemo ();
    jLabel6 = new JLabel ();
    edtValor = new JEditValor ();
    jTextField1 = new JTextField ();
    jNavegadorIndice1 = new JNavegadorIndice ();
    jLabel7 = new JLabel ();
    jLabel1.setText ("Consulta/Atualiza\u00e7\u00e3o");
    jNavegadorColecao1.setInformacaoAssociada ("atividadeRural.exterior.bens");
    jNavegadorColecao1.addNavegadorColecaoListener (new NavegadorColecaoListener ()
    {
      public void exibeColecaoVazia (NavegadorColecaoEvent evt)
      {
	PainelBensARExterior.this.jNavegadorColecao1ExibeColecaoVazia (evt);
      }
      
      public void exibeOutro (NavegadorColecaoEvent evt)
      {
	PainelBensARExterior.this.jNavegadorColecao1ExibeOutro (evt);
      }
    });
    jNavegadorColecao1.addNavegadorRemocaoListener (new NavegadorRemocaoListener ()
    {
      public boolean confirmaExclusao (NavegadorRemocaoEvent evt)
      {
	return PainelBensARExterior.this.jNavegadorColecao1ConfirmaExclusao (evt);
      }
      
      public void objetoExcluido (NavegadorRemocaoEvent evt)
      {
	/* empty */
      }
    });
    jLabel2.setHorizontalAlignment (0);
    jLabel2.setText ("Dados do bem de n\u00ba");
    jLabel3.setText ("Pa\u00eds");
    edtPais.setCaracteresValidosTxtCodigo ("0123456789 ");
    edtPais.setMascaraTxtCodigo ("***'");
    edtPais.setPesoTxtCodigo (0.07);
    jLabel4.setText ("C\u00f3digo");
    edtCodigo.setCaracteresValidosTxtCodigo ("0123456789 ");
    edtCodigo.setMascaraTxtCodigo ("**'");
    edtCodigo.setPesoTxtCodigo (0.05);
    jLabel5.setText ("Discrimina\u00e7\u00e3o");
    jLabel5.setVerticalAlignment (3);
    edtDiscriminacao.setMaxChars (512);
    jLabel6.setText ("Valor");
    jLabel6.setVerticalAlignment (3);
    edtValor.setAceitaFoco (false);
    jTextField1.setBorder (null);
    jTextField1.addFocusListener (new FocusAdapter ()
    {
      public void focusGained (FocusEvent evt)
      {
	PainelBensARExterior.this.jTextField1FocusGained (evt);
      }
    });
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (edtDiscriminacao, -1, -1, 32767).add (jLabel5).add (jPanel1Layout.createSequentialGroup ().add (jPanel1Layout.createParallelGroup (2, false).add (1, jLabel4, -1, -1, 32767).add (1, jLabel3, -1, 54, 32767)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (edtCodigo, -1, 451, 32767).add (edtPais, -1, 451, 32767))).add (jPanel1Layout.createSequentialGroup ().add (jLabel6, -2, 45, -2).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jTextField1, -2, -1, -2).add (edtValor, -2, 134, -2)))).addContainerGap ()));
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (2).add (edtPais, -2, -1, -2).add (jLabel3, -2, 24, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jLabel4, -2, 31, -2).add (edtCodigo, -2, -1, -2)).addPreferredGap (0).add (jLabel5, -2, 28, -2).addPreferredGap (0).add (edtDiscriminacao, -2, 79, -2).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jLabel6, -2, 23, -2).add (edtValor, -2, -1, -2)).addPreferredGap (0, 30, 32767).add (jTextField1, -2, -1, -2).addContainerGap ()));
    jNavegadorIndice1.setNavegadorColecao (jNavegadorColecao1);
    jLabel7.setText ("Pesquisar por c\u00f3digo de bem:");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jLabel1).addPreferredGap (0, 139, 32767).add (jLabel7).addPreferredGap (0).add (jNavegadorIndice1, -2, -1, -2).add (60, 60, 60)).add (layout.createSequentialGroup ().add (jNavegadorColecao1, -2, -1, -2).addContainerGap (351, 32767)))).add (jLabel2, -1, 535, 32767).add (jPanel1, -1, -1, 32767));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (layout.createParallelGroup (3).add (jLabel1).add (jLabel7, -2, 20, -2)).addPreferredGap (0).add (jNavegadorColecao1, -2, -1, -2)).add (jNavegadorIndice1, -2, -1, -2)).addPreferredGap (0).add (jLabel2).addPreferredGap (0).add (jPanel1, -1, -1, 32767)));
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
    if (JOptionPane.showConfirmDialog (getParent (), tab.msg ("bem_exclusao"), "Confirma\u00e7\u00e3o", 0) == 0)
      return true;
    return false;
  }
  
  private void jNavegadorColecao1ExibeColecaoVazia (NavegadorColecaoEvent evt)
  {
    reinitEdits ();
  }
  
  private void jNavegadorColecao1ExibeOutro (NavegadorColecaoEvent evt)
  {
    int index = jNavegadorColecao1.getIndiceAtual () + 1;
    jLabel2.setText ("Dados do bem de n\u00ba " + String.valueOf (index));
    BemARExterior bem = (BemARExterior) evt.getObjetoNegocio ();
    edtPais.setInformacao (bem.getPais ());
    edtCodigo.setInformacao (bem.getCodigo ());
    edtDiscriminacao.setInformacao (bem.getDiscriminacao ());
    edtValor.setInformacao (bem.getValor ());
    edtPais.setaFoco (false);
    edtCodigo.setLarguraColunaTabela (0, 15);
    edtPais.setLarguraColunaTabela (0, 15);
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
