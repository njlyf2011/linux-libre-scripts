/* PainelBens - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.bens;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
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
import serpro.ppgd.irpf.bens.Bem;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelBens extends JPanel implements PainelIRPFIf
{
  private static final String SUB_TIT = "Dados do bem de n\u00ba";
  private static final String TITULO = "Bens e direitos";
  private JButton btnRepetir;
  private JEditColecao edtCodigo;
  private JEditColecao edtPais;
  private JEditMemo jEditMemo1;
  private JEditValor jEditValor1;
  private JEditValor jEditValor2;
  private JEditValor jEditValor3;
  private JEditValor jEditValor4;
  private JLabel jLabel1;
  private JLabel jLabel13;
  private JLabel jLabel14;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JNavegadorColecao jNavegadorColecao2;
  private JNavegadorIndice jNavegadorIndice1;
  private JPanel jPanel1;
  private JTextField jTextField1;
  
  public String getTituloPainel ()
  {
    return "Bens e direitos";
  }
  
  public PainelBens ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "Bens");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (1);
    f = f.deriveFont (f.getSize2D () + 2.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
    reinitEdits ();
    jNavegadorColecao2.primeiro ();
    jEditMemo1.getComponenteFoco ().addKeyListener (new KeyAdapter ()
    {
      public void keyPressed (KeyEvent e)
      {
	PainelBens.this.jEditMemo1KeyPressed (e);
      }
      
      public void keyReleased (KeyEvent e)
      {
	PainelBens.this.jEditMemo1KeyReleased (e);
      }
    });
  }
  
  public JNavegadorColecao getNavegador ()
  {
    return jNavegadorColecao2;
  }
  
  private void reinitEdits ()
  {
    edtCodigo.getInformacao ().setHabilitado (false);
    edtPais.getInformacao ().setHabilitado (false);
    jEditMemo1.getInformacao ().setHabilitado (false);
    jEditValor2.getInformacao ().setHabilitado (false);
    jEditValor4.getInformacao ().setHabilitado (false);
    btnRepetir.setEnabled (false);
    jLabel13.setText ("Dados do bem de n\u00ba");
  }
  
  private void initComponents ()
  {
    jLabel13 = new JLabel ();
    jNavegadorColecao2 = new JNavegadorColecao ();
    jLabel14 = new JLabel ();
    jPanel1 = new JPanel ();
    jLabel1 = new JLabel ();
    jLabel2 = new JLabel ();
    jLabel5 = new JLabel ();
    jLabel6 = new JLabel ();
    jEditValor1 = new JEditValor ();
    jEditValor2 = new JEditValor ();
    jLabel7 = new JLabel ();
    jEditValor3 = new JEditValor ();
    jEditValor4 = new JEditValor ();
    jLabel8 = new JLabel ();
    btnRepetir = new JButton ();
    edtCodigo = new JEditColecao ();
    edtPais = new JEditColecao ();
    jLabel3 = new JLabel ();
    jEditMemo1 = new JEditMemo ();
    jTextField1 = new JTextField ();
    jNavegadorIndice1 = new JNavegadorIndice ();
    jLabel4 = new JLabel ();
    jLabel13.setHorizontalAlignment (0);
    jLabel13.setText ("Dados do bem de n\u00ba");
    jNavegadorColecao2.setInformacaoAssociada ("bens");
    jNavegadorColecao2.addNavegadorColecaoListener (new NavegadorColecaoListener ()
    {
      public void exibeColecaoVazia (NavegadorColecaoEvent evt)
      {
	PainelBens.this.jNavegadorColecao2ExibeColecaoVazia (evt);
      }
      
      public void exibeOutro (NavegadorColecaoEvent evt)
      {
	PainelBens.this.jNavegadorColecao2ExibeOutro (evt);
      }
    });
    jNavegadorColecao2.addNavegadorRemocaoListener (new NavegadorRemocaoListener ()
    {
      public boolean confirmaExclusao (NavegadorRemocaoEvent evt)
      {
	return PainelBens.this.jNavegadorColecao2ConfirmaExclusao (evt);
      }
      
      public void objetoExcluido (NavegadorRemocaoEvent evt)
      {
	/* empty */
      }
    });
    jLabel14.setText ("Consulta/Atualiza\u00e7\u00e3o");
    jLabel1.setText ("C\u00f3digo");
    jLabel2.setText ("Localiza\u00e7\u00e3o(Pa\u00eds)");
    jLabel5.setText ("Totais");
    jLabel6.setText ("Situa\u00e7\u00e3o em 31/12/2005 (R$)");
    jEditValor1.setAceitaFoco (false);
    jEditValor1.setInformacaoAssociada ("bens.totalExercicioAnterior");
    jLabel7.setText ("Situa\u00e7\u00e3o em 31/12/2006 (R$)");
    jEditValor3.setAceitaFoco (false);
    jEditValor3.setInformacaoAssociada ("bens.totalExercicioAtual");
    jLabel8.setText ("<HTML>Repete em 31/12/2006 o valor<BR>em reais de 31/12/2005</HTML>");
    btnRepetir.setMnemonic ('R');
    btnRepetir.setText ("<HTML><B>Repetir</B></HTML>");
    btnRepetir.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelBens.this.btnRepetirActionPerformed (evt);
      }
    });
    edtCodigo.setCaracteresValidosTxtCodigo ("0123456789 ");
    edtCodigo.setMascaraTxtCodigo ("**'");
    edtPais.setCaracteresValidosTxtCodigo ("1234567890 ");
    edtPais.setMascaraTxtCodigo ("***");
    jLabel3.setText ("Discrimina\u00e7\u00e3o");
    jLabel3.setVerticalAlignment (1);
    jEditMemo1.setMaxChars (512);
    jEditMemo1.addKeyListener (new KeyAdapter ()
    {
      public void keyPressed (KeyEvent evt)
      {
	PainelBens.this.jEditMemo1KeyPressed (evt);
      }
      
      public void keyReleased (KeyEvent evt)
      {
	PainelBens.this.jEditMemo1KeyReleased (evt);
      }
      
      public void keyTyped (KeyEvent evt)
      {
	PainelBens.this.jEditMemo1KeyTyped (evt);
      }
    });
    jTextField1.setBorder (null);
    jTextField1.addFocusListener (new FocusAdapter ()
    {
      public void focusGained (FocusEvent evt)
      {
	PainelBens.this.jTextField1FocusGained (evt);
      }
    });
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (2, jPanel1Layout.createSequentialGroup ().add (jLabel5).add (71, 71, 71)).add (2, jPanel1Layout.createSequentialGroup ().add (jPanel1Layout.createParallelGroup (1).add (jLabel6).add (jLabel7)).addPreferredGap (0, 101, 32767).add (jPanel1Layout.createParallelGroup (1, false).add (2, jPanel1Layout.createSequentialGroup ().add (jPanel1Layout.createParallelGroup (2).add (jEditValor2, -2, 140, -2).add (jEditValor4, -2, 142, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2, false).add (jEditValor3, -1, -1, 32767).add (jEditValor1, -2, 142, -2))).add (2, jPanel1Layout.createSequentialGroup ().add (jPanel1Layout.createParallelGroup (1).add (jTextField1, -2, -1, -2).add (jPanel1Layout.createSequentialGroup ().add (btnRepetir).addPreferredGap (0).add (jLabel8))).add (20, 20, 20)))).add (jPanel1Layout.createSequentialGroup ().add (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createParallelGroup (1, false).add (2, jLabel1, -1, -1, 32767).add (2, jLabel2, -1, 94, 32767)).add (jLabel3)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jEditMemo1, -1, -1, 32767).add (2, edtPais, -1, 432, 32767).add (2, edtCodigo, -1, 432, 32767)))).addContainerGap ()));
    jPanel1Layout.linkSize (new Component[] { jEditValor1, jEditValor2, jEditValor3, jEditValor4 }, 1);
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (2).add (jLabel1, -2, 23, -2).add (edtCodigo, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jLabel2).add (edtPais, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jLabel3).add (jEditMemo1, -2, 139, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jPanel1Layout.createSequentialGroup ().add (jLabel5).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jLabel6).add (jEditValor1, -2, -1, -2))).add (jEditValor2, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jLabel7).add (jEditValor3, -2, -1, -2).add (jEditValor4, -2, -1, -2)).add (9, 9, 9).add (jPanel1Layout.createParallelGroup (2, false).add (btnRepetir, -1, -1, 32767).add (jLabel8, -1, -1, 32767)).addPreferredGap (0).add (jTextField1, -2, -1, -2).addContainerGap (27, 32767)));
    jPanel1Layout.linkSize (new Component[] { jEditMemo1, jLabel3 }, 2);
    jNavegadorIndice1.setNavegadorColecao (jNavegadorColecao2);
    jLabel4.setText ("Pesquisar por n\u00b0 do item");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jLabel13, -1, 544, 32767).add (layout.createSequentialGroup ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jLabel14).add (173, 173, 173).add (jLabel4).addPreferredGap (0).add (jNavegadorIndice1, -2, -1, -2)).add (jNavegadorColecao2, -2, 180, -2)).addContainerGap (71, 32767)))).add (jPanel1, -1, -1, 32767));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (layout.createParallelGroup (3).add (jLabel14).add (jLabel4, -2, 20, -2)).addPreferredGap (0).add (jNavegadorColecao2, -2, -1, -2)).add (jNavegadorIndice1, -2, -1, -2)).addPreferredGap (0).add (jLabel13).addPreferredGap (0).add (jPanel1, -1, 365, 32767).addContainerGap ()));
  }
  
  private void jEditMemo1KeyPressed (KeyEvent evt)
  {
    /* empty */
  }
  
  private void jEditMemo1KeyReleased (KeyEvent evt)
  {
    /* empty */
  }
  
  private void jEditMemo1KeyTyped (KeyEvent evt)
  {
    /* empty */
  }
  
  private void jTextField1FocusGained (FocusEvent evt)
  {
    if (jNavegadorColecao2.getIndiceAtual () + 1 >= jNavegadorColecao2.getColecao ().recuperarLista ().size ())
      {
	jNavegadorColecao2.adiciona ();
	if (jNavegadorColecao2.getIndiceAtual () != -1)
	  jNavegadorColecao2.adiciona ();
      }
    else
      jNavegadorColecao2.proximo ();
  }
  
  private boolean jNavegadorColecao2ConfirmaExclusao (NavegadorRemocaoEvent evt)
  {
    TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
    if (JOptionPane.showConfirmDialog (getParent (), tab.msg ("bem_exclusao"), "Confirma\u00e7\u00e3o", 0) == 0)
      return true;
    return false;
  }
  
  private void btnRepetirActionPerformed (ActionEvent evt)
  {
    jEditValor4.getInformacao ().setConteudo (jEditValor2.getInformacao ().getConteudoFormatado ());
  }
  
  private void jNavegadorColecao2ExibeColecaoVazia (NavegadorColecaoEvent evt)
  {
    reinitEdits ();
  }
  
  private void jNavegadorColecao2ExibeOutro (NavegadorColecaoEvent evt)
  {
    Bem bem = (Bem) evt.getObjetoNegocio ();
    int index = jNavegadorColecao2.getIndiceAtual () + 1;
    jLabel13.setText ("Dados do bem de n\u00ba " + String.valueOf (index));
    edtCodigo.setInformacao (bem.getCodigo ());
    edtPais.setInformacao (bem.getPais ());
    jEditMemo1.setInformacao (bem.getDiscriminacao ());
    jEditValor2.setInformacao (bem.getValorExercicioAnterior ());
    jEditValor4.setInformacao (bem.getValorExercicioAtual ());
    if (edtPais.getInformacao ().isVazio ())
      edtPais.getInformacao ().setConteudo ("105");
    btnRepetir.setEnabled (true);
    edtCodigo.getComponenteFoco ().grabFocus ();
  }
  
  public void vaiExibir ()
  {
    if (IRPFUtil.getEstadoSistema () != 1)
      {
	if (jNavegadorColecao2.getColecao ().recuperarLista ().size () == 0)
	  jNavegadorColecao2.adiciona ();
	if (jNavegadorColecao2.getColecao ().recuperarLista ().size () > 1)
	  jNavegadorColecao2.getColecao ().excluirRegistrosEmBranco ();
	jNavegadorColecao2.primeiro ();
      }
  }
  
  public void painelExibido ()
  {
    /* empty */
  }
}
