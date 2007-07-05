/* PainelDoacoesCampanhas - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.eleicoes;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditAlfa;
import serpro.ppgd.gui.xbeans.JEditCNPJ;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.gui.xbeans.JNavegadorColecao;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoListener;
import serpro.ppgd.irpf.eleicoes.Doacao;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.util.IRPFUtil;

public class PainelDoacoesCampanhas extends JPanel implements PainelIRPFIf
{
  private static final String SUB_TIT = "Dados da doa\u00e7\u00e3o n\u00ba";
  private static final String TITULO = "Doa\u00e7\u00f5es a Partidos Pol\u00edticos, Comit\u00eas Financeiros e Candidatos a Cargos Eletivos";
  private JEditCNPJ edtCNPJ;
  private JEditAlfa edtNome;
  private JEditValor edtValor;
  private JNavegadorColecao jNavegadorColecao1;
  private JTextField jTextField1;
  private JLabel lblCNPJ;
  private JLabel lblConsulta;
  private JLabel lblNome;
  private JLabel lblSubtitulo;
  private JLabel lblValor;
  
  public PainelDoacoesCampanhas ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "doacoes_campanhas");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (1);
    f = f.deriveFont (f.getSize2D () + 2.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
    reinitEdits ();
    jNavegadorColecao1.primeiro ();
  }
  
  private void initComponents ()
  {
    jNavegadorColecao1 = new JNavegadorColecao ();
    lblConsulta = new JLabel ();
    lblNome = new JLabel ();
    lblCNPJ = new JLabel ();
    edtCNPJ = new JEditCNPJ ();
    edtNome = new JEditAlfa ();
    lblValor = new JLabel ();
    edtValor = new JEditValor ();
    lblSubtitulo = new JLabel ();
    jTextField1 = new JTextField ();
    jNavegadorColecao1.setInformacaoAssociada ("doacoes");
    jNavegadorColecao1.addNavegadorColecaoListener (new NavegadorColecaoListener ()
    {
      public void exibeColecaoVazia (NavegadorColecaoEvent evt)
      {
	PainelDoacoesCampanhas.this.jNavegadorColecao1ExibeColecaoVazia (evt);
      }
      
      public void exibeOutro (NavegadorColecaoEvent evt)
      {
	PainelDoacoesCampanhas.this.jNavegadorColecao1ExibeOutro (evt);
      }
    });
    lblConsulta.setText ("Consulta/Atualiza\u00e7\u00e3o");
    lblNome.setText ("<html>Nome  do candidato, partido pol\u00edtico ou comit\u00ea financeiro:</html>");
    lblCNPJ.setText ("CNPJ:");
    lblValor.setText ("Valor:");
    lblSubtitulo.setHorizontalAlignment (0);
    lblSubtitulo.setText ("Dados da doa\u00e7\u00e3o n\u00ba");
    jTextField1.setBorder (null);
    jTextField1.addFocusListener (new FocusAdapter ()
    {
      public void focusGained (FocusEvent evt)
      {
	PainelDoacoesCampanhas.this.jTextField1FocusGained (evt);
      }
    });
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (lblConsulta).add (jNavegadorColecao1, -2, -1, -2))).add (lblSubtitulo, -1, 495, 32767).add (layout.createSequentialGroup ().add (91, 91, 91).add (jTextField1, -2, -1, -2).addContainerGap (404, 32767)).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1, false).add (lblValor, -1, -1, 32767).add (lblCNPJ, -1, -1, 32767).add (lblNome, -1, 218, 32767)).addPreferredGap (0).add (layout.createParallelGroup (1).add (edtCNPJ, -2, 147, -2).add (edtValor, -2, 144, -2).add (layout.createSequentialGroup ().add (edtNome, -1, 263, 32767).addPreferredGap (0))).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (lblConsulta).add (11, 11, 11).add (jNavegadorColecao1, -2, -1, -2).addPreferredGap (0).add (lblSubtitulo).add (20, 20, 20).add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (lblCNPJ).add (7, 7, 7).add (lblNome)).add (layout.createSequentialGroup ().add (edtCNPJ, -2, -1, -2).add (7, 7, 7).add (edtNome, -2, -1, -2))).addPreferredGap (0).add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (lblValor).addPreferredGap (0).add (jTextField1, -2, -1, -2)).add (edtValor, -2, -1, -2)).addContainerGap ()));
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
  
  private void jNavegadorColecao1ExibeOutro (NavegadorColecaoEvent evt)
  {
    int index = jNavegadorColecao1.getIndiceAtual () + 1;
    lblSubtitulo.setText ("Dados da doa\u00e7\u00e3o n\u00ba " + String.valueOf (index));
    Doacao d = (Doacao) evt.getObjetoNegocio ();
    edtCNPJ.setInformacao (d.getCNPJ ());
    edtNome.setInformacao (d.getNome ());
    edtValor.setInformacao (d.getValor ());
    edtCNPJ.setaFoco (false);
  }
  
  private void jNavegadorColecao1ExibeColecaoVazia (NavegadorColecaoEvent evt)
  {
    reinitEdits ();
  }
  
  public String getTituloPainel ()
  {
    return "Doa\u00e7\u00f5es a Partidos Pol\u00edticos, Comit\u00eas Financeiros e Candidatos a Cargos Eletivos";
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
  
  private void reinitEdits ()
  {
    edtCNPJ.getInformacao ().setHabilitado (false);
    edtNome.getInformacao ().setHabilitado (false);
    edtValor.getInformacao ().setHabilitado (false);
    lblSubtitulo.setText ("Dados da doa\u00e7\u00e3o n\u00ba");
  }
}
