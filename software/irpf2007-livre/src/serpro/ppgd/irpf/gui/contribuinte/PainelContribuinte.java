/* PainelContribuinte - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.contribuinte;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.UtilitariosGUI;
import serpro.ppgd.gui.xbeans.GroupPanelEvent;
import serpro.ppgd.gui.xbeans.GroupPanelListener;
import serpro.ppgd.gui.xbeans.JButtonGroupPanel;
import serpro.ppgd.gui.xbeans.JButtonMensagem;
import serpro.ppgd.gui.xbeans.JEditAlfa;
import serpro.ppgd.gui.xbeans.JEditColecao;
import serpro.ppgd.gui.xbeans.JEditData;
import serpro.ppgd.gui.xbeans.JEditMascara;
import serpro.ppgd.gui.xbeans.JFlipComponentes;
import serpro.ppgd.gui.xbeans.PPGDRadioItem;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.JEditOcupacaoPrincipal;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.negocio.Logico;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelContribuinte extends JPanel implements PainelIRPFIf
{
  private static final String TITULO = "Identifica\u00e7\u00e3o do Contribuinte";
  private static final TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
  private JButtonMensagem btnMsgEnderecoDiferente;
  private JButtonMensagem btnMsgExterior;
  private JButtonMensagem btnMsgRetif;
  private JButtonGroupPanel edtEnderecoDiferente;
  private JButtonGroupPanel edtExterior;
  private JEditColecao edtNatureza;
  private JButtonGroupPanel edtRetif;
  private JEditAlfa jEditAlfa1;
  private JEditData jEditData1;
  private JEditMascara jEditMascara1;
  private JEditOcupacaoPrincipal jEditOcupacaoPrincipal1;
  private JFlipComponentes jFlipComponentes1;
  private JFlipComponentes jFlipComponentes2;
  private JLabel jLabel1;
  private JLabel jLabel15;
  private JLabel jLabel16;
  private JLabel jLabel18;
  private JLabel jLabel19;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JPanel jPanel2;
  private JPanel jPanel3;
  private PPGDRadioItem pPGDRadioItem3;
  private PPGDRadioItem pPGDRadioItem4;
  private PPGDRadioItem pPGDRadioItem7;
  private PPGDRadioItem pPGDRadioItem8;
  private PPGDRadioItem radioBrasil;
  private PPGDRadioItem radioExterior;
  public PainelEnderecoBrasil painelEnderecoBrasil = new PainelEnderecoBrasil ();
  public PainelEnderecoExterior painelEnderecoExterior = new PainelEnderecoExterior ();
  
  public PainelContribuinte ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "Identificacao");
    initComponents ();
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, PgdIRPF.FONTE_DEFAULT.deriveFont (1));
    atualizaSubPainelEndereco ();
    atualizaVisivelNumRecibo ();
    UtilitariosGUI.aumentaFonte (jLabel15, 3);
    Font f = ((TitledBorder) jPanel3.getBorder ()).getTitleFont ();
    ((TitledBorder) jPanel3.getBorder ()).setTitleFont (f.deriveFont (f.getSize2D () + 3.0F));
  }
  
  private void atualizaSubPainelEndereco ()
  {
    serpro.ppgd.negocio.Informacao cep = IRPFFacade.getInstancia ().getContribuinte ().getCep ();
    if (IRPFFacade.getInstancia ().getContribuinte ().getExterior ().asString ().equals (Logico.NAO))
      jFlipComponentes1.exibeComponenteA ();
    else
      jFlipComponentes1.exibeComponenteB ();
  }
  
  private void atualizaVisivelNumRecibo ()
  {
    boolean isRetif = edtRetif.getInformacao ().asString ().equals (Logico.SIM);
    if (isRetif)
      jFlipComponentes2.exibeComponenteA ();
    else
      jFlipComponentes2.exibeComponenteB ();
  }
  
  public String getTituloPainel ()
  {
    return "Identifica\u00e7\u00e3o do Contribuinte";
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jLabel18 = new JLabel ();
    jLabel19 = new JLabel ();
    jEditAlfa1 = new JEditAlfa ();
    jLabel2 = new JLabel ();
    jEditData1 = new JEditData ();
    jLabel3 = new JLabel ();
    jEditMascara1 = new JEditMascara ();
    jPanel3 = new JPanel ();
    jPanel3.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEmptyBorder (), "Ocupa\u00e7\u00e3o Principal", 2, 1, new JLabel ().getFont (), new Color (0, 0, 0)));
    jLabel16 = new JLabel ();
    jLabel4 = new JLabel ();
    jEditOcupacaoPrincipal1 = new JEditOcupacaoPrincipal ();
    edtNatureza = new JEditColecao ();
    jPanel2 = new JPanel ();
    edtExterior = new JButtonGroupPanel ();
    radioBrasil = new PPGDRadioItem ();
    radioExterior = new PPGDRadioItem ();
    btnMsgExterior = new JButtonMensagem ();
    jFlipComponentes1 = new JFlipComponentes ();
    jLabel15 = new JLabel ();
    edtEnderecoDiferente = new JButtonGroupPanel ();
    pPGDRadioItem3 = new PPGDRadioItem ();
    pPGDRadioItem4 = new PPGDRadioItem ();
    edtRetif = new JButtonGroupPanel ();
    pPGDRadioItem7 = new PPGDRadioItem ();
    pPGDRadioItem8 = new PPGDRadioItem ();
    btnMsgEnderecoDiferente = new JButtonMensagem ();
    btnMsgRetif = new JButtonMensagem ();
    jFlipComponentes2 = new JFlipComponentes ();
    jLabel1.setText ("Nome");
    jLabel18.setText ("O endere\u00e7o atual \u00e9 diferente do constante na \u00faltima declara\u00e7\u00e3o?");
    jLabel19.setText ("Esta declara\u00e7\u00e3o \u00e9 retificadora?");
    jEditAlfa1.setInformacaoAssociada ("idDeclaracaoAberto.nome");
    jLabel2.setText ("Data de Nascimento");
    jEditData1.setInformacaoAssociada ("contribuinte.dataNascimento");
    jLabel3.setText ("T\u00edtulo Eleitoral");
    jEditMascara1.setInformacaoAssociada ("contribuinte.tituloEleitor");
    jEditMascara1.setMascara ("*************");
    jLabel16.setText ("Natureza da ocupa\u00e7\u00e3o");
    jLabel4.setText ("Ocupa\u00e7\u00e3o Principal");
    jEditOcupacaoPrincipal1.setInformacaoAssociada ("contribuinte.ocupacaoPrincipal");
    edtNatureza.setInformacaoAssociada ("contribuinte.naturezaOcupacao");
    edtNatureza.setCaracteresValidosTxtCodigo ("0123456789 ");
    edtNatureza.setMascaraTxtCodigo ("**'");
    edtNatureza.setPesoLabel (0.97);
    edtNatureza.setPodeDigitarValoresForaDoDominio (true);
    edtNatureza.setLarguraColunaTabela (0, 10);
    GroupLayout jPanel3Layout = new GroupLayout (jPanel3);
    jPanel3.setLayout (jPanel3Layout);
    jPanel3Layout.setHorizontalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().add (jPanel3Layout.createParallelGroup (1).add (jLabel16).add (jLabel4)).add (14, 14, 14).add (jPanel3Layout.createParallelGroup (1).add (jEditOcupacaoPrincipal1, -1, 335, 32767).add (edtNatureza, -1, 335, 32767)).addContainerGap ()));
    jPanel3Layout.linkSize (new Component[] { jLabel16, jLabel4 }, 1);
    jPanel3Layout.setVerticalGroup (jPanel3Layout.createParallelGroup (1).add (2, jPanel3Layout.createSequentialGroup ().addContainerGap ().add (jPanel3Layout.createParallelGroup (2).add (edtNatureza, -2, -1, -2).add (jLabel16)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (2).add (jEditOcupacaoPrincipal1, -2, -1, -2).add (jLabel4)).addContainerGap ()));
    jPanel3Layout.linkSize (new Component[] { jEditOcupacaoPrincipal1, jLabel4 }, 2);
    jPanel3Layout.linkSize (new Component[] { edtNatureza, jLabel16 }, 2);
    edtExterior.setButtonMensagem (btnMsgExterior);
    edtExterior.setEstiloFonte (1);
    edtExterior.setInformacaoAssociada ("contribuinte.exterior");
    edtExterior.setBorder (null);
    edtExterior.addGroupPanelListener (new GroupPanelListener ()
    {
      public void atualizaPainel (GroupPanelEvent evt)
      {
	PainelContribuinte.this.edtExteriorAtualizaPainel (evt);
      }
    });
    radioBrasil.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    radioBrasil.setText ("No Brasil");
    radioBrasil.setMargin (new Insets (0, 0, 0, 0));
    radioBrasil.setValorSelecionadoTrue (Logico.NAO);
    radioExterior.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    radioExterior.setText ("No Exterior");
    radioExterior.setMargin (new Insets (0, 0, 0, 0));
    radioExterior.setValorSelecionadoTrue (Logico.SIM);
    GroupLayout edtExteriorLayout = new GroupLayout (edtExterior);
    edtExterior.setLayout (edtExteriorLayout);
    edtExteriorLayout.setHorizontalGroup (edtExteriorLayout.createParallelGroup (1).add (edtExteriorLayout.createSequentialGroup ().add (radioBrasil, -2, 76, -2).addPreferredGap (0).add (radioExterior, -1, 81, 32767)));
    edtExteriorLayout.setVerticalGroup (edtExteriorLayout.createParallelGroup (1).add (edtExteriorLayout.createSequentialGroup ().add (edtExteriorLayout.createParallelGroup (3).add (radioBrasil, -2, -1, -2).add (radioExterior, -2, -1, -2)).addContainerGap (-1, 32767)));
    jPanel2.add (edtExterior);
    btnMsgExterior.setText ("jButtonMensagem1");
    jPanel2.add (btnMsgExterior);
    jFlipComponentes1.setComponenteA (painelEnderecoBrasil);
    jFlipComponentes1.setComponenteB (painelEnderecoExterior);
    jLabel15.setHorizontalAlignment (0);
    jLabel15.setText ("<HTML><B>Endere\u00e7o</B></HTML>");
    jLabel15.setVerticalAlignment (3);
    edtEnderecoDiferente.setButtonMensagem (btnMsgEnderecoDiferente);
    edtEnderecoDiferente.setEstiloFonte (1);
    edtEnderecoDiferente.setInformacaoAssociada ("contribuinte.enderecoDiferente");
    edtEnderecoDiferente.setBorder (null);
    pPGDRadioItem3.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    pPGDRadioItem3.setText ("Sim");
    pPGDRadioItem3.setMargin (new Insets (0, 0, 0, 0));
    pPGDRadioItem3.setValorSelecionadoTrue (Logico.SIM);
    pPGDRadioItem4.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    pPGDRadioItem4.setText ("N\u00e3o");
    pPGDRadioItem4.setMargin (new Insets (0, 0, 0, 0));
    pPGDRadioItem4.setValorSelecionadoTrue (Logico.NAO);
    GroupLayout edtEnderecoDiferenteLayout = new GroupLayout (edtEnderecoDiferente);
    edtEnderecoDiferente.setLayout (edtEnderecoDiferenteLayout);
    edtEnderecoDiferenteLayout.setHorizontalGroup (edtEnderecoDiferenteLayout.createParallelGroup (1).add (edtEnderecoDiferenteLayout.createSequentialGroup ().addContainerGap (-1, 32767).add (pPGDRadioItem3, -2, 44, -2).addPreferredGap (0).add (pPGDRadioItem4, -2, 46, -2)));
    edtEnderecoDiferenteLayout.setVerticalGroup (edtEnderecoDiferenteLayout.createParallelGroup (1).add (edtEnderecoDiferenteLayout.createSequentialGroup ().add (edtEnderecoDiferenteLayout.createParallelGroup (1).add (pPGDRadioItem3, -2, -1, -2).add (pPGDRadioItem4, -2, -1, -2)).addContainerGap (6, 32767)));
    edtRetif.setButtonMensagem (btnMsgRetif);
    edtRetif.setEstiloFonte (1);
    edtRetif.setInformacaoAssociada ("contribuinte.declaracaoRetificadora");
    edtRetif.setBorder (null);
    edtRetif.addGroupPanelListener (new GroupPanelListener ()
    {
      public void atualizaPainel (GroupPanelEvent evt)
      {
	PainelContribuinte.this.edtRetifAtualizaPainel (evt);
      }
    });
    pPGDRadioItem7.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    pPGDRadioItem7.setText ("Sim");
    pPGDRadioItem7.setMargin (new Insets (0, 0, 0, 0));
    pPGDRadioItem7.setValorSelecionadoTrue (Logico.SIM);
    pPGDRadioItem8.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    pPGDRadioItem8.setText ("N\u00e3o");
    pPGDRadioItem8.setMargin (new Insets (0, 0, 0, 0));
    pPGDRadioItem8.setValorSelecionadoTrue (Logico.NAO);
    GroupLayout edtRetifLayout = new GroupLayout (edtRetif);
    edtRetif.setLayout (edtRetifLayout);
    edtRetifLayout.setHorizontalGroup (edtRetifLayout.createParallelGroup (1).add (edtRetifLayout.createSequentialGroup ().addContainerGap (-1, 32767).add (pPGDRadioItem7, -2, 45, -2).addPreferredGap (0).add (pPGDRadioItem8, -2, 48, -2)));
    edtRetifLayout.setVerticalGroup (edtRetifLayout.createParallelGroup (1).add (edtRetifLayout.createSequentialGroup ().add (edtRetifLayout.createParallelGroup (1).add (pPGDRadioItem7, -2, -1, -2).add (pPGDRadioItem8, -2, -1, -2)).addContainerGap (6, 32767)));
    btnMsgEnderecoDiferente.setText ("jButtonMensagem1");
    btnMsgRetif.setText ("jButtonMensagem1");
    jFlipComponentes2.setComponenteA (new PainelReciboRetif ());
    jFlipComponentes2.setComponenteB (new PainelReciboAnterior ());
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jLabel1).add (layout.createSequentialGroup ().add (jLabel2).add (19, 19, 19).add (jEditData1, -2, 114, -2).add (14, 14, 14).add (jLabel3).add (15, 15, 15).add (jEditMascara1, -2, 125, -2))).addContainerGap (60, 32767)).add (jLabel15, -1, 522, 32767).add (layout.createSequentialGroup ().add (jPanel2, -1, 510, 32767).addContainerGap ()).add (layout.createSequentialGroup ().addContainerGap ().add (jFlipComponentes2, -1, 498, 32767).addContainerGap ()).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jFlipComponentes1, -1, 498, 32767).addContainerGap ()).add (layout.createSequentialGroup ().add (layout.createParallelGroup (2).add (1, layout.createSequentialGroup ().add (jLabel18).addPreferredGap (0).add (edtEnderecoDiferente, -2, -1, -2).addPreferredGap (0).add (btnMsgEnderecoDiferente, -2, -1, -2)).add (1, layout.createSequentialGroup ().add (jLabel19).addPreferredGap (0).add (edtRetif, -2, -1, -2).addPreferredGap (0).add (btnMsgRetif, -2, -1, -2)).add (1, jPanel3, -1, -1, 32767)).addContainerGap (41, 32767)))).add (layout.createSequentialGroup ().add (53, 53, 53).add (jEditAlfa1, -1, 457, 32767).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (2).add (jLabel1).add (jEditAlfa1, -2, -1, -2)).add (7, 7, 7).add (layout.createParallelGroup (2).add (jLabel2).add (jLabel3).add (jEditData1, -2, -1, -2).add (jEditMascara1, -2, -1, -2)).addPreferredGap (0).add (jLabel15, -2, 27, -2).addPreferredGap (0).add (jPanel2, -2, 37, -2).addPreferredGap (0).add (jFlipComponentes1, -2, 119, -2).addPreferredGap (0).add (jPanel3, -2, -1, -2).addPreferredGap (0).add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jLabel18).add (12, 12, 12).add (layout.createParallelGroup (1).add (2, layout.createSequentialGroup ().add (jLabel19).add (12, 12, 12)).add (layout.createParallelGroup (2).add (btnMsgRetif, -2, -1, -2).add (edtRetif, -2, 20, -2)))).add (layout.createParallelGroup (2, false).add (1, edtEnderecoDiferente, 0, 20, 32767).add (1, btnMsgEnderecoDiferente, -1, -1, 32767))).addPreferredGap (0).add (jFlipComponentes2, -2, 41, -2).addContainerGap (-1, 32767)));
    layout.linkSize (new Component[] { jEditAlfa1, jEditData1, jEditMascara1 }, 2);
  }
  
  private void edtRetifAtualizaPainel (GroupPanelEvent evt)
  {
    atualizaVisivelNumRecibo ();
  }
  
  private void edtExteriorAtualizaPainel (GroupPanelEvent evt)
  {
    atualizaSubPainelEndereco ();
  }
  
  public void vaiExibir ()
  {
    /* empty */
  }
  
  public void painelExibido ()
  {
    /* empty */
  }
}
