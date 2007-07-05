/* PainelDividas - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dividas;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
import serpro.ppgd.irpf.dividas.Divida;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelDividas extends JPanel implements PainelIRPFIf
{
  private static final String SUB_TIT = "Dados da d\u00edvida de n\u00ba";
  private static final String TITULO = "D\u00edvidas e \u00d4nus Reais";
  private JEditColecao edtCodigo;
  private JEditMemo jEditMemo1;
  private JEditValor jEditValor1;
  private JEditValor jEditValor2;
  private JEditValor jEditValor3;
  private JEditValor jEditValor4;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JNavegadorColecao jNavegadorColecao1;
  private JNavegadorIndice jNavegadorIndice1;
  private JPanel jPanel1;
  private JTextField jTextField1;
  
  public String getTituloPainel ()
  {
    return "D\u00edvidas e \u00d4nus Reais";
  }
  
  public PainelDividas ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "D\u00edvidas_onus");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (1);
    f = f.deriveFont (f.getSize2D () + 2.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
    reinitEdits ();
    jNavegadorColecao1.primeiro ();
    jEditMemo1.getComponenteFoco ().addKeyListener (new KeyAdapter ()
    {
      public void keyPressed (KeyEvent e)
      {
	PainelDividas.this.jEditMemo1KeyPressed (e);
      }
      
      public void keyReleased (KeyEvent e)
      {
	PainelDividas.this.jEditMemo1KeyReleased (e);
      }
    });
  }
  
  public JNavegadorColecao getNavegador ()
  {
    return jNavegadorColecao1;
  }
  
  private void reinitEdits ()
  {
    edtCodigo.getInformacao ().setHabilitado (false);
    jEditMemo1.getInformacao ().setHabilitado (false);
    jEditValor3.getInformacao ().setHabilitado (false);
    jEditValor4.getInformacao ().setHabilitado (false);
    jLabel2.setText ("Dados da d\u00edvida de n\u00ba");
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jNavegadorColecao1 = new JNavegadorColecao ();
    jLabel2 = new JLabel ();
    jPanel1 = new JPanel ();
    jLabel3 = new JLabel ();
    jLabel5 = new JLabel ();
    jLabel6 = new JLabel ();
    jLabel7 = new JLabel ();
    jEditValor1 = new JEditValor ();
    jEditValor2 = new JEditValor ();
    jEditValor3 = new JEditValor ();
    jEditValor4 = new JEditValor ();
    edtCodigo = new JEditColecao ();
    jLabel4 = new JLabel ();
    jEditMemo1 = new JEditMemo ();
    jTextField1 = new JTextField ();
    jNavegadorIndice1 = new JNavegadorIndice ();
    jLabel8 = new JLabel ();
    jLabel1.setText ("Consulta/Atualiza\u00e7\u00e3o");
    jNavegadorColecao1.setInformacaoAssociada ("dividas");
    jNavegadorColecao1.addNavegadorColecaoListener (new NavegadorColecaoListener ()
    {
      public void exibeColecaoVazia (NavegadorColecaoEvent evt)
      {
	PainelDividas.this.jNavegadorColecao1ExibeColecaoVazia (evt);
      }
      
      public void exibeOutro (NavegadorColecaoEvent evt)
      {
	PainelDividas.this.jNavegadorColecao1ExibeOutro (evt);
      }
    });
    jNavegadorColecao1.addNavegadorRemocaoListener (new NavegadorRemocaoListener ()
    {
      public boolean confirmaExclusao (NavegadorRemocaoEvent evt)
      {
	return PainelDividas.this.jNavegadorColecao1ConfirmaExclusao (evt);
      }
      
      public void objetoExcluido (NavegadorRemocaoEvent evt)
      {
	/* empty */
      }
    });
    jLabel2.setHorizontalAlignment (0);
    jLabel2.setText ("Dados da d\u00edvida de n\u00ba");
    jLabel3.setText ("C\u00f3digo");
    jLabel5.setText ("Totais");
    jLabel6.setText ("Situa\u00e7\u00e3o em 31/12/2005 (R$)");
    jLabel7.setText ("Situa\u00e7\u00e3o em 31/12/2006 (R$)");
    jEditValor1.setAceitaFoco (false);
    jEditValor1.setInformacaoAssociada ("dividas.totalExercicioAnterior");
    jEditValor2.setAceitaFoco (false);
    jEditValor2.setInformacaoAssociada ("dividas.totalExercicioAtual");
    edtCodigo.setCaracteresValidosTxtCodigo ("0123456789 ");
    edtCodigo.setMascaraTxtCodigo ("**'");
    jLabel4.setText ("Discrimina\u00e7\u00e3o");
    jLabel4.setVerticalAlignment (1);
    jEditMemo1.setMaxChars (250);
    jEditMemo1.addKeyListener (new KeyAdapter ()
    {
      public void keyPressed (KeyEvent evt)
      {
	PainelDividas.this.jEditMemo1KeyPressed (evt);
      }
      
      public void keyReleased (KeyEvent evt)
      {
	PainelDividas.this.jEditMemo1KeyReleased (evt);
      }
    });
    jTextField1.setBorder (null);
    jTextField1.addFocusListener (new FocusAdapter ()
    {
      public void focusGained (FocusEvent evt)
      {
	PainelDividas.this.jTextField1FocusGained (evt);
      }
    });
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (2, jPanel1Layout.createSequentialGroup ().add (jLabel5).add (64, 64, 64)).add (2, jPanel1Layout.createSequentialGroup ().add (jPanel1Layout.createParallelGroup (1).add (jLabel6).add (jLabel7)).addPreferredGap (0, 100, 32767).add (jPanel1Layout.createParallelGroup (2, false).add (jEditValor4, -1, -1, 32767).add (jEditValor3, -1, 133, 32767).add (1, jTextField1, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2, false).add (jEditValor2, -1, -1, 32767).add (jEditValor1, -1, 138, 32767)).addContainerGap ()).add (2, jPanel1Layout.createSequentialGroup ().add (jPanel1Layout.createParallelGroup (1, false).add (jLabel4, -1, -1, 32767).add (jLabel3, -1, 73, 32767)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (edtCodigo, -1, 439, 32767).add (jEditMemo1, -1, -1, 32767)).addContainerGap ()))));
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (2).add (jLabel3, -2, 23, -2).add (edtCodigo, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jLabel4, -2, 112, -2).add (jEditMemo1, -2, 112, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jPanel1Layout.createSequentialGroup ().add (jLabel5).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jLabel6).add (jEditValor1, -2, -1, -2))).add (jEditValor3, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jLabel7).add (jEditValor2, -2, -1, -2).add (jEditValor4, -2, -1, -2)).addPreferredGap (0, 56, 32767).add (jTextField1, -2, -1, -2).addContainerGap ()));
    jPanel1Layout.linkSize (new Component[] { jEditMemo1, jLabel4 }, 2);
    jNavegadorIndice1.setNavegadorColecao (jNavegadorColecao1);
    jLabel8.setText ("Pesquisar por n\u00b0 do item:");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (jLabel2, -1, 542, 32767).add (jPanel1, -1, -1, 32767).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel1).addPreferredGap (0, 172, 32767).add (jLabel8).addPreferredGap (0).add (jNavegadorIndice1, -2, -1, -2).add (54, 54, 54)).add (layout.createSequentialGroup ().add (jNavegadorColecao1, -2, 174, -2).addContainerGap (368, 32767)));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (23, 23, 23).add (jNavegadorColecao1, -2, -1, -2)).add (jNavegadorIndice1, -2, -1, -2)).addPreferredGap (0).add (jLabel2).addPreferredGap (0).add (jPanel1, -1, -1, 32767)).add (layout.createSequentialGroup ().add (layout.createParallelGroup (3).add (jLabel1).add (jLabel8)).addContainerGap (374, 32767)))));
  }
  
  private void jEditMemo1KeyReleased (KeyEvent evt)
  {
    /* empty */
  }
  
  private void jEditMemo1KeyPressed (KeyEvent evt)
  {
    /* empty */
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
    if (JOptionPane.showConfirmDialog (getParent (), tab.msg ("divida_exclusao"), "Confirma\u00e7\u00e3o", 0) == 0)
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
    jLabel2.setText ("Dados da d\u00edvida de n\u00ba " + String.valueOf (index));
    Divida divida = (Divida) evt.getObjetoNegocio ();
    edtCodigo.setInformacao (divida.getCodigo ());
    jEditMemo1.setInformacao (divida.getDiscriminacao ());
    jEditValor3.setInformacao (divida.getValorExercicioAnterior ());
    jEditValor4.setInformacao (divida.getValorExercicioAtual ());
    edtCodigo.getComponenteFoco ().grabFocus ();
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
