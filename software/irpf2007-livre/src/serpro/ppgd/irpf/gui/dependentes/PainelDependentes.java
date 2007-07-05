/* PainelDependentes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dependentes;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditAlfa;
import serpro.ppgd.gui.xbeans.JEditCPF;
import serpro.ppgd.gui.xbeans.JEditColecao;
import serpro.ppgd.gui.xbeans.JEditData;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.gui.xbeans.JNavegadorColecao;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoListener;
import serpro.ppgd.irpf.dependentes.Dependente;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.util.IRPFUtil;

public class PainelDependentes extends JPanel implements PainelIRPFIf
{
  private static final String SUB_TIT = "Dados do dependente n\u00ba";
  private static final String TITULO = "Dependentes";
  private JEditColecao edtCodigo;
  private JEditCPF edtCpf;
  private JEditData edtData;
  private JEditAlfa edtNome;
  private JEditValor jEditValor1;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JNavegadorColecao jNavegadorColecao1;
  private JSeparator jSeparator1;
  private JTextField jTextField1;
  
  public PainelDependentes ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "Dependentes");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (1);
    f = f.deriveFont (f.getSize2D () + 2.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
    reinitEdits ();
    jNavegadorColecao1.primeiro ();
    ((JFormattedTextField) edtCodigo.getComponenteEditor ()).addPropertyChangeListener (new PropertyChangeListener ()
    {
      public void propertyChange (PropertyChangeEvent evt)
      {
	if (edtCodigo.getInformacao ().getConteudoFormatado ().equals ("11") || edtCodigo.getInformacao ().getConteudoFormatado ().equals ("22") || edtCodigo.getInformacao ().getConteudoFormatado ().equals ("25") || edtCodigo.getInformacao ().getConteudoFormatado ().equals ("31"))
	  jLabel5.setText ("CPF");
	else
	  jLabel5.setText ("CPF (se houver)");
      }
    });
  }
  
  public JNavegadorColecao getNavegador ()
  {
    return jNavegadorColecao1;
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jLabel2 = new JLabel ();
    jLabel3 = new JLabel ();
    jLabel4 = new JLabel ();
    edtNome = new JEditAlfa ();
    jLabel5 = new JLabel ();
    edtCpf = new JEditCPF ();
    jLabel6 = new JLabel ();
    edtData = new JEditData ();
    jNavegadorColecao1 = new JNavegadorColecao ();
    jSeparator1 = new JSeparator ();
    jLabel7 = new JLabel ();
    jEditValor1 = new JEditValor ();
    edtCodigo = new JEditColecao ();
    jTextField1 = new JTextField ();
    jLabel1.setText ("Consulta/Atualiza\u00e7\u00e3o");
    jLabel2.setHorizontalAlignment (0);
    jLabel2.setText ("Dados do dependente de n\u00ba");
    jLabel3.setText ("C\u00f3digo");
    jLabel4.setText ("Nome");
    jLabel5.setText ("CPF (se houver)");
    jLabel6.setText ("Data de nascimento");
    jNavegadorColecao1.setInformacaoAssociada ("dependentes");
    jNavegadorColecao1.addNavegadorColecaoListener (new NavegadorColecaoListener ()
    {
      public void exibeColecaoVazia (NavegadorColecaoEvent evt)
      {
	PainelDependentes.this.jNavegadorColecao1ExibeColecaoVazia (evt);
      }
      
      public void exibeOutro (NavegadorColecaoEvent evt)
      {
	PainelDependentes.this.jNavegadorColecao1ExibeOutro (evt);
      }
    });
    jLabel7.setText ("Total de dedu\u00e7\u00e3o com dependentes:");
    jEditValor1.setAceitaFoco (false);
    jEditValor1.setInformacaoAssociada ("dependentes.totalDeducaoDependentes");
    edtCodigo.setCaracteresValidosTxtCodigo ("0123456789 ");
    edtCodigo.setMascaraTxtCodigo ("**'");
    edtCodigo.setPesoTxtCodigo (0.03);
    jTextField1.setBorder (null);
    jTextField1.addFocusListener (new FocusAdapter ()
    {
      public void focusGained (FocusEvent evt)
      {
	PainelDependentes.this.jTextField1FocusGained (evt);
      }
    });
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel1).addContainerGap (293, 32767)).add (2, jLabel2, -1, 406, 32767).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jLabel3).add (jLabel4)).addPreferredGap (0).add (layout.createParallelGroup (1).add (edtCodigo, -1, 343, 32767).add (edtNome, -1, 343, 32767)).addContainerGap ()).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jLabel6).add (jLabel5)).addPreferredGap (0).add (layout.createParallelGroup (1, false).add (edtData, -1, -1, 32767).add (edtCpf, -1, 107, 32767)).addPreferredGap (0, 174, 32767).add (jTextField1, -2, -1, -2).addContainerGap ()).add (layout.createSequentialGroup ().addContainerGap ().add (jSeparator1, -1, 382, 32767).addContainerGap ()).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel7).addPreferredGap (0, 57, 32767).add (jEditValor1, -2, 150, -2).addContainerGap ()).add (layout.createSequentialGroup ().addContainerGap ().add (jNavegadorColecao1, -2, -1, -2).addContainerGap (222, 32767)));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel1).addPreferredGap (0).add (jNavegadorColecao1, -2, -1, -2).addPreferredGap (0).add (jLabel2).addPreferredGap (0).add (layout.createParallelGroup (1).add (edtCodigo, -2, -1, -2).add (jLabel3)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel4).add (edtNome, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (layout.createSequentialGroup ().add (layout.createParallelGroup (2).add (jLabel5).add (edtCpf, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel6).add (edtData, -2, -1, -2))).add (jTextField1, -2, -1, -2)).addPreferredGap (0).add (jSeparator1, -2, -1, -2).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel7).add (jEditValor1, -2, -1, -2)).addContainerGap (73, 32767)));
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
  
  private void jNavegadorColecao1ExibeColecaoVazia (NavegadorColecaoEvent evt)
  {
    reinitEdits ();
  }
  
  private void jNavegadorColecao1ExibeOutro (NavegadorColecaoEvent evt)
  {
    Dependente obj = (Dependente) evt.getObjetoNegocio ();
    int index = jNavegadorColecao1.getIndiceAtual () + 1;
    jLabel2.setText ("Dados do dependente n\u00ba " + String.valueOf (index));
    edtCodigo.setInformacao (obj.getCodigo ());
    edtNome.setInformacao (obj.getNome ());
    edtData.setInformacao (obj.getDataNascimento ());
    edtCpf.setInformacao (obj.getCpfDependente ());
    edtCodigo.setaFoco (false);
    edtCodigo.setLarguraColunaTabela (0, 10);
  }
  
  private void reinitEdits ()
  {
    edtCodigo.getInformacao ().setHabilitado (false);
    edtNome.getInformacao ().setHabilitado (false);
    edtData.getInformacao ().setHabilitado (false);
    edtCpf.getInformacao ().setHabilitado (false);
    jLabel2.setText ("Dados do dependente n\u00ba");
  }
  
  public String getTituloPainel ()
  {
    return "Dependentes";
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
