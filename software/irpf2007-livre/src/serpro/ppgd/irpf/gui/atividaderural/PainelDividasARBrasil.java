/* PainelDividasARBrasil - Decompiled by JODE
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
import serpro.ppgd.gui.xbeans.JEditMemo;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.gui.xbeans.JNavegadorColecao;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoListener;
import serpro.ppgd.infraestrutura.util.NavegadorRemocaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorRemocaoListener;
import serpro.ppgd.irpf.atividaderural.DividaAR;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelDividasARBrasil extends JPanel implements PainelIRPFIf
{
  private final String SUB_TIT = "Dados da d\u00edvida de n\u00ba ";
  private JEditValor edtContraidasPassado;
  private JEditValor edtContraidasRetrasado;
  private JEditMemo edtDiscriminacao;
  private JEditValor edtPagas;
  private JLabel jLabel1;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JPanel jPanel1;
  private JPanel jPanel2;
  private JTextField jTextField1;
  private JLabel labelContador;
  private JNavegadorColecao navegadorColecao;
  
  public PainelDividasARBrasil ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "ARdividas");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (1);
    f = f.deriveFont (f.getSize2D () + 2.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
    reinitEdits ();
    navegadorColecao.primeiro ();
  }
  
  public JNavegadorColecao getNavegador ()
  {
    return navegadorColecao;
  }
  
  private void reinitEdits ()
  {
    edtDiscriminacao.getInformacao ().setHabilitado (false);
    edtContraidasPassado.getInformacao ().setHabilitado (false);
    edtContraidasRetrasado.getInformacao ().setHabilitado (false);
    edtPagas.getInformacao ().setHabilitado (false);
    labelContador.setText ("Dados da d\u00edvida de n\u00ba ");
  }
  
  private void initComponents ()
  {
    navegadorColecao = new JNavegadorColecao ();
    jLabel1 = new JLabel ();
    jPanel1 = new JPanel ();
    labelContador = new JLabel ();
    jPanel2 = new JPanel ();
    jLabel3 = new JLabel ();
    jLabel4 = new JLabel ();
    jLabel5 = new JLabel ();
    jLabel6 = new JLabel ();
    edtContraidasPassado = new JEditValor ();
    edtPagas = new JEditValor ();
    edtContraidasRetrasado = new JEditValor ();
    edtDiscriminacao = new JEditMemo ();
    jTextField1 = new JTextField ();
    navegadorColecao.setInformacaoAssociada ("atividadeRural.brasil.dividas");
    navegadorColecao.addNavegadorColecaoListener (new NavegadorColecaoListener ()
    {
      public void exibeColecaoVazia (NavegadorColecaoEvent evt)
      {
	PainelDividasARBrasil.this.navegadorColecaoExibeColecaoVazia (evt);
      }
      
      public void exibeOutro (NavegadorColecaoEvent evt)
      {
	PainelDividasARBrasil.this.navegadorColecaoExibeOutro (evt);
      }
    });
    navegadorColecao.addNavegadorRemocaoListener (new NavegadorRemocaoListener ()
    {
      public boolean confirmaExclusao (NavegadorRemocaoEvent evt)
      {
	return PainelDividasARBrasil.this.navegadorColecaoConfirmaExclusao (evt);
      }
      
      public void objetoExcluido (NavegadorRemocaoEvent evt)
      {
	/* empty */
      }
    });
    jLabel1.setText ("Consulta/Atualiza\u00e7\u00e3o");
    labelContador.setText ("Dados da d\u00edvida de n\u00ba ");
    jPanel1.add (labelContador);
    jLabel3.setText ("Discrimina\u00e7\u00e3o");
    jLabel4.setText ("Contra\u00eddas at\u00e9 2005 R$:");
    jLabel5.setText ("Contra\u00eddas em 2006 R$:");
    jLabel6.setText ("Efetivamente pagas em 2006 R$:");
    edtDiscriminacao.setMaxChars (250);
    jTextField1.setBorder (null);
    jTextField1.addFocusListener (new FocusAdapter ()
    {
      public void focusGained (FocusEvent evt)
      {
	PainelDividasARBrasil.this.jTextField1FocusGained (evt);
      }
    });
    GroupLayout jPanel2Layout = new GroupLayout (jPanel2);
    jPanel2.setLayout (jPanel2Layout);
    jPanel2Layout.setHorizontalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().addContainerGap ().add (jPanel2Layout.createParallelGroup (1).add (edtDiscriminacao, -1, -1, 32767).add (jPanel2Layout.createSequentialGroup ().add (jPanel2Layout.createParallelGroup (1).add (jLabel3).add (jPanel2Layout.createSequentialGroup ().add (jPanel2Layout.createParallelGroup (1).add (jLabel4).add (jLabel5).add (jLabel6)).addPreferredGap (0).add (jPanel2Layout.createParallelGroup (1, false).add (edtContraidasPassado, -1, 144, 32767).add (edtPagas, -1, -1, 32767).add (edtContraidasRetrasado, -1, -1, 32767).add (jTextField1, -2, -1, -2)))).addContainerGap ()))));
    jPanel2Layout.setVerticalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().addContainerGap ().add (jLabel3).addPreferredGap (0).add (edtDiscriminacao, -2, 79, -2).addPreferredGap (0).add (jPanel2Layout.createParallelGroup (2).add (jLabel4).add (edtContraidasRetrasado, -2, -1, -2)).addPreferredGap (0).add (jPanel2Layout.createParallelGroup (2).add (jLabel5).add (edtContraidasPassado, -2, -1, -2)).addPreferredGap (0).add (jPanel2Layout.createParallelGroup (2).add (jLabel6).add (edtPagas, -2, -1, -2)).addPreferredGap (0, 30, 32767).add (jTextField1, -2, -1, -2).addContainerGap ()));
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (2, jPanel2, -1, -1, 32767).add (2, jPanel1, -1, 471, 32767).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jLabel1).add (navegadorColecao, -2, -1, -2)).addContainerGap (287, 32767)));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel1).addPreferredGap (0).add (navegadorColecao, -2, -1, -2).addPreferredGap (0).add (jPanel1, -2, -1, -2).addPreferredGap (0).add (jPanel2, -1, -1, 32767)));
  }
  
  private void jTextField1FocusGained (FocusEvent evt)
  {
    if (navegadorColecao.getIndiceAtual () + 1 >= navegadorColecao.getColecao ().recuperarLista ().size ())
      {
	if (navegadorColecao.getIndiceAtual () != -1)
	  navegadorColecao.adiciona ();
      }
    else
      navegadorColecao.proximo ();
  }
  
  private boolean navegadorColecaoConfirmaExclusao (NavegadorRemocaoEvent evt)
  {
    TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
    if (JOptionPane.showConfirmDialog (getParent (), tab.msg ("divida_exclusao"), "Confirma\u00e7\u00e3o", 0) == 0)
      return true;
    return false;
  }
  
  private void navegadorColecaoExibeOutro (NavegadorColecaoEvent evt)
  {
    DividaAR divida = (DividaAR) evt.getObjetoNegocio ();
    edtContraidasPassado.setInformacao (divida.getContraidasAteExercicioAtual ());
    edtContraidasRetrasado.setInformacao (divida.getContraidasAteExercicioAnterior ());
    edtDiscriminacao.setInformacao (divida.getDiscriminacao ());
    edtPagas.setInformacao (divida.getEfetivamentePagas ());
    labelContador.setText ("Dados da d\u00edvida de n\u00ba  " + (navegadorColecao.getIndiceAtual () + 1));
  }
  
  private void navegadorColecaoExibeColecaoVazia (NavegadorColecaoEvent evt)
  {
    reinitEdits ();
  }
  
  public String getTituloPainel ()
  {
    return "D\u00edvidas Vinculadas \u00e0 Atividade Rural - Brasil";
  }
  
  public void vaiExibir ()
  {
    if (IRPFUtil.getEstadoSistema () != 1)
      {
	if (navegadorColecao.getColecao ().recuperarLista ().size () == 0)
	  navegadorColecao.adiciona ();
	if (navegadorColecao.getColecao ().recuperarLista ().size () > 1)
	  navegadorColecao.getColecao ().excluirRegistrosEmBranco ();
	navegadorColecao.primeiro ();
      }
  }
  
  public void painelExibido ()
  {
    /* empty */
  }
}
