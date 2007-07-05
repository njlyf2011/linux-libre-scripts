/* PainelIdentificacaoImovelARExterior - Decompiled by JODE
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
import serpro.ppgd.gui.xbeans.JEditAlfa;
import serpro.ppgd.gui.xbeans.JEditColecao;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.gui.xbeans.JNavegadorColecao;
import serpro.ppgd.gui.xbeans.JNavegadorIndice;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoListener;
import serpro.ppgd.infraestrutura.util.NavegadorRemocaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorRemocaoListener;
import serpro.ppgd.irpf.atividaderural.ImovelAR;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelIdentificacaoImovelARExterior extends JPanel implements PainelIRPFIf
{
  private static final String SUB_TIT = "Dados do Im\u00f3vel de n\u00ba";
  private static final String TITULO = "Dados e Identifica\u00e7\u00e3o do Im\u00f3vel Explorado - Exterior";
  private JEditValor edtArea;
  private JEditColecao edtCodigo;
  private JEditColecao edtCondicao;
  private JEditAlfa edtLocalizacao;
  private JEditAlfa edtNome;
  private JEditValor edtParticipacao;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JLabel jLabel9;
  private JNavegadorColecao jNavegadorColecao1;
  private JNavegadorIndice jNavegadorIndice1;
  private JPanel jPanel1;
  private JTextField jTextField1;
  
  public String getTituloPainel ()
  {
    return "Dados e Identifica\u00e7\u00e3o do Im\u00f3vel Explorado - Exterior";
  }
  
  public PainelIdentificacaoImovelARExterior ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "ARIm\u00f3vel");
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
    edtCodigo.getInformacao ().setHabilitado (false);
    edtArea.getInformacao ().setHabilitado (false);
    edtCondicao.getInformacao ().setHabilitado (false);
    edtLocalizacao.getInformacao ().setHabilitado (false);
    edtNome.getInformacao ().setHabilitado (false);
    edtParticipacao.getInformacao ().setHabilitado (false);
    jLabel2.setText ("Dados do Im\u00f3vel de n\u00ba");
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jNavegadorColecao1 = new JNavegadorColecao ();
    jLabel2 = new JLabel ();
    jPanel1 = new JPanel ();
    jLabel5 = new JLabel ();
    jLabel4 = new JLabel ();
    jLabel3 = new JLabel ();
    edtCodigo = new JEditColecao ();
    edtNome = new JEditAlfa ();
    edtLocalizacao = new JEditAlfa ();
    jLabel8 = new JLabel ();
    edtArea = new JEditValor ();
    jLabel9 = new JLabel ();
    edtParticipacao = new JEditValor ();
    jLabel7 = new JLabel ();
    edtCondicao = new JEditColecao ();
    jNavegadorIndice1 = new JNavegadorIndice ();
    jLabel6 = new JLabel ();
    jTextField1 = new JTextField ();
    jLabel1.setText ("Consulta/Atualiza\u00e7\u00e3o");
    jNavegadorColecao1.setInformacaoAssociada ("atividadeRural.exterior.identificacaoImovel");
    jNavegadorColecao1.addNavegadorColecaoListener (new NavegadorColecaoListener ()
    {
      public void exibeColecaoVazia (NavegadorColecaoEvent evt)
      {
	PainelIdentificacaoImovelARExterior.this.jNavegadorColecao1ExibeColecaoVazia (evt);
      }
      
      public void exibeOutro (NavegadorColecaoEvent evt)
      {
	PainelIdentificacaoImovelARExterior.this.jNavegadorColecao1ExibeOutro (evt);
      }
    });
    jNavegadorColecao1.addNavegadorRemocaoListener (new NavegadorRemocaoListener ()
    {
      public boolean confirmaExclusao (NavegadorRemocaoEvent evt)
      {
	return PainelIdentificacaoImovelARExterior.this.jNavegadorColecao1ConfirmaExclusao (evt);
      }
      
      public void objetoExcluido (NavegadorRemocaoEvent evt)
      {
	/* empty */
      }
    });
    jLabel2.setHorizontalAlignment (0);
    jLabel2.setText ("Dados do Im\u00f3vel de N\u00ba ");
    jLabel5.setText ("<HTML>Localiza\u00e7\u00e3o do Im\u00f3vel</HTML>");
    jLabel4.setText ("<HTML>Nome do Im\u00f3vel</HTML>");
    jLabel3.setText ("C\u00f3digo");
    edtCodigo.setCaracteresValidosTxtCodigo ("0123456789 ");
    edtCodigo.setMascaraTxtCodigo ("**'");
    jLabel8.setText ("\u00c1rea (ha)");
    jLabel9.setText ("Participa\u00e7\u00e3o(%)");
    jLabel7.setText ("<HTML>Condi\u00e7\u00e3o explora\u00e7\u00e3o</HTML>");
    edtCondicao.setCaracteresValidosTxtCodigo ("0123456789 ");
    edtCondicao.setMascaraTxtCodigo ("*'");
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1, false).add (jLabel8, -1, -1, 32767).add (jLabel7, -1, -1, 32767).add (jLabel3, -1, -1, 32767).add (jLabel4, -1, -1, 32767).add (jLabel5, -1, 134, 32767)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (1, edtCodigo, -1, 336, 32767).add (1, edtNome, -1, 336, 32767).add (1, edtLocalizacao, -1, 336, 32767).add (1, jPanel1Layout.createSequentialGroup ().add (edtArea, -2, 108, -2).addPreferredGap (0).add (jLabel9).addPreferredGap (0).add (edtParticipacao, -2, 80, -2)).add (edtCondicao, -1, 336, 32767)).addContainerGap ()));
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (jLabel3).add (edtCodigo, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jLabel4).add (edtNome, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jLabel5).add (edtLocalizacao, -2, -1, -2)).add (7, 7, 7).add (jPanel1Layout.createParallelGroup (2).add (jLabel8).add (edtArea, -2, -1, -2).add (jLabel9).add (edtParticipacao, -2, -1, -2)).add (21, 21, 21).add (jPanel1Layout.createParallelGroup (1).add (jLabel7).add (edtCondicao, -2, -1, -2)).addContainerGap (-1, 32767)));
    jNavegadorIndice1.setNavegadorColecao (jNavegadorColecao1);
    jLabel6.setText ("Pesquisar por c\u00f3digo do im\u00f3vel:");
    jTextField1.setBorder (null);
    jTextField1.addFocusListener (new FocusAdapter ()
    {
      public void focusGained (FocusEvent evt)
      {
	PainelIdentificacaoImovelARExterior.this.jTextField1FocusGained (evt);
      }
    });
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jLabel1).addPreferredGap (0, 89, 32767).add (jLabel6).addPreferredGap (0).add (jNavegadorIndice1, -2, -1, -2).add (65, 65, 65)).add (layout.createSequentialGroup ().add (jNavegadorColecao1, -2, -1, -2).addContainerGap (316, 32767)))).add (jLabel2, -1, 500, 32767).add (jPanel1, -1, -1, 32767).add (layout.createSequentialGroup ().addContainerGap ().add (jTextField1, -2, -1, -2).addContainerGap (488, 32767)));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (layout.createParallelGroup (3).add (jLabel1).add (jLabel6, -2, 20, -2)).addPreferredGap (0).add (jNavegadorColecao1, -2, -1, -2)).add (jNavegadorIndice1, -2, -1, -2)).addPreferredGap (0).add (jLabel2).addPreferredGap (0).add (jPanel1, -2, -1, -2).addPreferredGap (0, 55, 32767).add (jTextField1, -2, -1, -2).addContainerGap ()));
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
    if (JOptionPane.showConfirmDialog (getParent (), tab.msg ("ar_imovel_exclusao"), "Confirma\u00e7\u00e3o", 0) == 0)
      return true;
    return false;
  }
  
  private void jNavegadorColecao1ExibeOutro (NavegadorColecaoEvent evt)
  {
    ImovelAR imovel = (ImovelAR) evt.getObjetoNegocio ();
    edtCodigo.setInformacao (imovel.getCodigo ());
    edtArea.setInformacao (imovel.getArea ());
    edtCondicao.setInformacao (imovel.getCondicaoExploracao ());
    edtLocalizacao.setInformacao (imovel.getLocalizacao ());
    edtNome.setInformacao (imovel.getNome ());
    edtParticipacao.setInformacao (imovel.getParticipacao ());
    int index = jNavegadorColecao1.getIndiceAtual () + 1;
    jLabel2.setText ("Dados do Im\u00f3vel de n\u00ba " + String.valueOf (index));
    edtCodigo.getComponenteFoco ().grabFocus ();
  }
  
  private void jNavegadorColecao1ExibeColecaoVazia (NavegadorColecaoEvent evt)
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
