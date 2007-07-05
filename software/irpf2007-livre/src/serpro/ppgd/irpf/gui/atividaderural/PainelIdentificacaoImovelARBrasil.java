/* PainelIdentificacaoImovelARBrasil - Decompiled by JODE
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
import serpro.ppgd.gui.xbeans.JEditNirf;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.gui.xbeans.JNavegadorColecao;
import serpro.ppgd.gui.xbeans.JNavegadorIndice;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoListener;
import serpro.ppgd.infraestrutura.util.NavegadorRemocaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorRemocaoListener;
import serpro.ppgd.irpf.atividaderural.brasil.ImovelARBrasil;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelIdentificacaoImovelARBrasil extends JPanel implements PainelIRPFIf
{
  private static final String SUB_TIT = "Dados do Im\u00f3vel de n\u00ba";
  private static final String TITULO = "Dados e Identifica\u00e7\u00e3o do Im\u00f3vel Explorado - Brasil";
  private JEditValor edtArea;
  private JEditColecao edtCodigo;
  private JEditColecao edtCondicao;
  private JEditAlfa edtLocalizacao;
  private JEditNirf edtNirf;
  private JEditAlfa edtNome;
  private JEditValor edtParticipacao;
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
  private JNavegadorIndice jNavegadorIndice1;
  private JPanel jPanel1;
  private JTextField jTextField1;
  
  public String getTituloPainel ()
  {
    return "Dados e Identifica\u00e7\u00e3o do Im\u00f3vel Explorado - Brasil";
  }
  
  public PainelIdentificacaoImovelARBrasil ()
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
    edtNirf.getInformacao ().setHabilitado (false);
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
    jLabel6 = new JLabel ();
    jLabel5 = new JLabel ();
    jLabel4 = new JLabel ();
    jLabel3 = new JLabel ();
    edtCodigo = new JEditColecao ();
    edtNome = new JEditAlfa ();
    edtLocalizacao = new JEditAlfa ();
    edtNirf = new JEditNirf ();
    jLabel8 = new JLabel ();
    edtArea = new JEditValor ();
    jLabel9 = new JLabel ();
    edtParticipacao = new JEditValor ();
    jLabel7 = new JLabel ();
    edtCondicao = new JEditColecao ();
    jNavegadorIndice1 = new JNavegadorIndice ();
    jLabel10 = new JLabel ();
    jTextField1 = new JTextField ();
    jLabel1.setText ("Consulta/Atualiza\u00e7\u00e3o");
    jNavegadorColecao1.setInformacaoAssociada ("atividadeRural.brasil.identificacaoImovel");
    jNavegadorColecao1.addNavegadorColecaoListener (new NavegadorColecaoListener ()
    {
      public void exibeColecaoVazia (NavegadorColecaoEvent evt)
      {
	PainelIdentificacaoImovelARBrasil.this.jNavegadorColecao1ExibeColecaoVazia (evt);
      }
      
      public void exibeOutro (NavegadorColecaoEvent evt)
      {
	PainelIdentificacaoImovelARBrasil.this.jNavegadorColecao1ExibeOutro (evt);
      }
    });
    jNavegadorColecao1.addNavegadorRemocaoListener (new NavegadorRemocaoListener ()
    {
      public boolean confirmaExclusao (NavegadorRemocaoEvent evt)
      {
	return PainelIdentificacaoImovelARBrasil.this.jNavegadorColecao1ConfirmaExclusao (evt);
      }
      
      public void objetoExcluido (NavegadorRemocaoEvent evt)
      {
	/* empty */
      }
    });
    jLabel2.setHorizontalAlignment (0);
    jLabel2.setText ("Dados do Im\u00f3vel de N\u00ba ");
    jLabel6.setText ("<HTML>N\u00ba do Im\u00f3vel na SRF</HTML>");
    jLabel5.setText ("<HTML>Localiza\u00e7\u00e3o do Im\u00f3vel</HTML>");
    jLabel4.setText ("<HTML>Nome do Im\u00f3vel</HTML>");
    jLabel3.setText ("C\u00f3digo");
    edtCodigo.setCaracteresValidosTxtCodigo ("0123456789 ");
    edtCodigo.setMascaraTxtCodigo ("**'");
    edtCodigo.setPesoTxtCodigo (0.05);
    jLabel8.setText ("\u00c1rea (ha)");
    jLabel9.setText ("Participa\u00e7\u00e3o(%)");
    jLabel7.setText ("<HTML>Condi\u00e7\u00e3o explora\u00e7\u00e3o</HTML>");
    edtCondicao.setCaracteresValidosTxtCodigo ("0123456789 ");
    edtCondicao.setMascaraTxtCodigo ("*'");
    edtCondicao.setPesoTxtCodigo (0.05);
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1, false).add (jLabel7, -1, -1, 32767).add (jLabel6, -1, -1, 32767).add (jLabel3, -1, -1, 32767).add (jLabel4, -1, -1, 32767).add (jLabel5, -1, 134, 32767)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().add (edtNirf, -2, 96, -2).addPreferredGap (0).add (jLabel8).addPreferredGap (0).add (edtArea, -2, 82, -2).addPreferredGap (0).add (jLabel9).addPreferredGap (0).add (edtParticipacao, -1, 36, 32767)).add (edtNome, -1, 361, 32767).add (edtLocalizacao, -1, 361, 32767).add (edtCondicao, -1, 361, 32767).add (edtCodigo, -1, 361, 32767)).addContainerGap ()));
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (jLabel3).add (edtCodigo, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jLabel4).add (edtNome, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jLabel5).add (edtLocalizacao, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jLabel6).add (edtNirf, -2, -1, -2).add (jPanel1Layout.createParallelGroup (1, false).add (jLabel8, -1, -1, 32767).add (edtArea, -1, -1, 32767).add (jLabel9, -2, 20, -2)).add (edtParticipacao, -2, -1, -2)).add (21, 21, 21).add (jPanel1Layout.createParallelGroup (1).add (jLabel7).add (edtCondicao, -2, -1, -2)).addContainerGap (-1, 32767)));
    jNavegadorIndice1.setNavegadorColecao (jNavegadorColecao1);
    jLabel10.setText ("Pesquisar por c\u00f3digo do im\u00f3vel:");
    jTextField1.setBorder (null);
    jTextField1.addFocusListener (new FocusAdapter ()
    {
      public void focusGained (FocusEvent evt)
      {
	PainelIdentificacaoImovelARBrasil.this.jTextField1FocusGained (evt);
      }
    });
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jLabel1).addPreferredGap (0, 89, 32767).add (jLabel10).addPreferredGap (0).add (jNavegadorIndice1, -2, -1, -2).add (102, 102, 102)).add (layout.createSequentialGroup ().add (jNavegadorColecao1, -2, -1, -2).addContainerGap (353, 32767)))).add (jLabel2, -1, 537, 32767).add (layout.createSequentialGroup ().add (jPanel1, -1, -1, 32767).addContainerGap ()).add (2, layout.createSequentialGroup ().addContainerGap (525, 32767).add (jTextField1, -2, -1, -2).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (layout.createParallelGroup (3).add (jLabel1).add (jLabel10, -2, 20, -2)).addPreferredGap (0).add (jNavegadorColecao1, -2, -1, -2)).add (jNavegadorIndice1, -2, -1, -2)).addPreferredGap (0).add (jLabel2).addPreferredGap (0).add (jPanel1, -2, -1, -2).addPreferredGap (0, 29, 32767).add (jTextField1, -2, -1, -2).addContainerGap ()));
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
    ImovelARBrasil imovel = (ImovelARBrasil) evt.getObjetoNegocio ();
    edtCodigo.setInformacao (imovel.getCodigo ());
    edtArea.setInformacao (imovel.getArea ());
    edtCondicao.setInformacao (imovel.getCondicaoExploracao ());
    edtLocalizacao.setInformacao (imovel.getLocalizacao ());
    edtNirf.setInformacao (imovel.getNirf ());
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
