/* PainelCalculoImposto - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.resumo;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditAlfa;
import serpro.ppgd.gui.xbeans.JEditColecao;
import serpro.ppgd.gui.xbeans.JEditInteiro;
import serpro.ppgd.gui.xbeans.JEditLogico;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.gui.xbeans.JFlipComponentes;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.negocio.Logico;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelCalculoImposto extends JPanel implements PainelIRPFIf
{
  private static final String TITULO = "C\u00e1lculo do imposto";
  private JEditLogico debitoAutomatico;
  private JEditAlfa jEditAlfa1;
  private JEditAlfa jEditAlfa2;
  private JEditAlfa jEditAlfa3;
  private JEditAlfa jEditAlfa5;
  private JEditColecao jEditColecao1;
  private JEditInteiro jEditInteiro2;
  private JEditValor jEditValor13;
  private JEditValor jEditValor14;
  private JEditValor jEditValor15;
  private JFlipComponentes jFlipComponentes1;
  private JLabel jLabel1;
  private JLabel jLabel16;
  private JLabel jLabel17;
  private JLabel jLabel18;
  private JLabel jLabel19;
  private JLabel jLabel2;
  private JLabel jLabel20;
  private JLabel jLabel21;
  private JLabel jLabel22;
  private JLabel jLabel23;
  private JLabel jLabel25;
  private JLabel jLabel3;
  private JPanel jPanel1;
  private JSeparator jSeparator2;
  private SubPainelCompleta subPainelCompleta = new SubPainelCompleta ();
  private SubPainelSimplificada subPainelSimplificada = new SubPainelSimplificada ();
  
  public String getTituloPainel ()
  {
    return "C\u00e1lculo do imposto";
  }
  
  public PainelCalculoImposto ()
  {
    initComponents ();
    try
      {
	IRPFFacade.getInstancia ().getIdDeclaracaoAberto ().getTipoDeclaracao ().removeObservadores (new Class[] { this.getClass () });
      }
    catch (Exception exception)
      {
	/* empty */
      }
    IRPFFacade.getInstancia ().getIdDeclaracaoAberto ().getTipoDeclaracao ().addObservador (new Observador ()
    {
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	PainelCalculoImposto.this.atualizaSubPainel ();
      }
    });
    atualizaSubPainel ();
    jEditInteiro2.getInformacao ().addObservador (new Observador ()
    {
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	PainelCalculoImposto.this.habilitaDesabilitaControle ();
	SwingUtilities.invokeLater (new Runnable ()
	{
	  public void run ()
	  {
	    DeclaracaoIRPF dec = IRPFFacade.getInstancia ().getDeclaracao ();
	    if (dec != null && jEditInteiro2.isShowing () && dec.getResumo ().getCalculoImposto ().getNumQuotas ().getConteudoAntigo ().equals ("1") && dec.getResumo ().getCalculoImposto ().getNumQuotas ().asInteger () > 1)
	      JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), TabelaMensagens.getTabelaMensagens ().msg ("info_quotas"), "Informa\u00e7\u00e3o", 1);
	  }
	});
      }
    });
    debitoAutomatico.getInformacao ().addObservador (new Observador ()
    {
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	DeclaracaoIRPF dec = IRPFFacade.getInstancia ().getDeclaracao ();
	if (debitoAutomatico.isShowing () && dec != null && nomePropriedade.equals ("D\u00e9bito autom\u00e1tico") && dec.getResumo ().getCalculoImposto ().getDebitoAutomatico ().asString ().equals ("autorizado"))
	  JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), TabelaMensagens.getTabelaMensagens ().msg ("info_debito_automatico"), "Informa\u00e7\u00e3o", 1);
      }
    });
    habilitaDesabilitaControle ();
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (f.getSize2D () + 1.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
  }
  
  private void initComponents ()
  {
    jFlipComponentes1 = new JFlipComponentes ();
    jSeparator2 = new JSeparator ();
    jPanel1 = new JPanel ();
    jLabel1 = new JLabel ();
    jLabel16 = new JLabel ();
    jEditColecao1 = new JEditColecao ();
    jLabel17 = new JLabel ();
    jLabel18 = new JLabel ();
    jEditAlfa1 = new JEditAlfa ();
    jEditAlfa2 = new JEditAlfa ();
    jLabel19 = new JLabel ();
    jLabel20 = new JLabel ();
    jEditAlfa3 = new JEditAlfa ();
    jEditAlfa5 = new JEditAlfa ();
    jLabel2 = new JLabel ();
    jLabel23 = new JLabel ();
    jEditInteiro2 = new JEditInteiro ();
    jLabel25 = new JLabel ();
    jEditValor14 = new JEditValor ();
    jLabel21 = new JLabel ();
    jEditValor13 = new JEditValor ();
    jLabel22 = new JLabel ();
    jEditValor15 = new JEditValor ();
    debitoAutomatico = new JEditLogico ();
    jLabel3 = new JLabel ();
    jFlipComponentes1.setBorder (null);
    jFlipComponentes1.setComponenteA (subPainelCompleta);
    jFlipComponentes1.setComponenteB (subPainelSimplificada);
    jSeparator2.setOrientation (1);
    jLabel1.setHorizontalAlignment (0);
    jLabel1.setText ("<HTML><B>INFORMA\u00c7\u00d5ES BANC\u00c1RIAS:</B></HTML>");
    jLabel1.setVerticalAlignment (3);
    jLabel16.setText ("Banco");
    jEditColecao1.setInformacaoAssociada ("resumo.calculoImposto.banco");
    jEditColecao1.setCaracteresValidosTxtCodigo ("1234567890 ");
    jEditColecao1.setMascaraTxtCodigo ("***");
    jLabel17.setText ("Ag\u00eancia");
    jLabel18.setText ("DV");
    jEditAlfa1.setInformacaoAssociada ("resumo.calculoImposto.agencia");
    jEditAlfa1.setMaxChars (5);
    jEditAlfa2.setInformacaoAssociada ("resumo.calculoImposto.dvAgencia");
    jEditAlfa2.setMaxChars (1);
    jLabel19.setText ("Conta para cr\u00e9dito");
    jLabel20.setText ("DV");
    jEditAlfa3.setInformacaoAssociada ("resumo.calculoImposto.contaCredito");
    jEditAlfa3.setMaxChars (13);
    jEditAlfa5.setInformacaoAssociada ("resumo.calculoImposto.dvContaCredito");
    jEditAlfa5.setMaxChars (2);
    jLabel2.setHorizontalAlignment (0);
    jLabel2.setText ("<HTML><B>PARCELAMENTO DO IMPOSTO:</B></HTML>");
    jLabel2.setVerticalAlignment (3);
    jLabel23.setHorizontalAlignment (2);
    jLabel23.setText ("N\u00famero de quotas (at\u00e9 8)");
    jLabel23.setVerticalAlignment (3);
    jEditInteiro2.setInformacaoAssociada ("resumo.calculoImposto.numQuotas");
    jLabel25.setText ("Valor da quota");
    jLabel25.setVerticalAlignment (3);
    jEditValor14.setInformacaoAssociada ("resumo.calculoImposto.valorQuota");
    jLabel21.setText ("<HTML><P>SALDO DO IMPOSTO A PAGAR</P></HTML>");
    jEditValor13.setInformacaoAssociada ("resumo.calculoImposto.saldoImpostoPagar");
    jLabel22.setText ("IMPOSTO A RESTITUIR");
    jEditValor15.setInformacaoAssociada ("resumo.calculoImposto.impostoRestituir");
    debitoAutomatico.setInformacaoAssociada ("resumo.calculoImposto.debitoAutomatico");
    debitoAutomatico.setOrientacaoTexto (0);
    jLabel3.setText ("D\u00e9bito autom\u00e1tico?");
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().add (jPanel1Layout.createParallelGroup (1).add (jLabel21, -2, 154, -2).add (jEditValor13, -2, 161, -2).add (jLabel22, -2, 154, -2).add (jEditValor15, -2, 161, -2)).add (42, 42, 42)).add (2, jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (jLabel2, -1, 240, 32767).add (2, jLabel23, -1, 240, 32767).add (jPanel1Layout.createSequentialGroup ().add (jEditInteiro2, -2, 84, -2).addPreferredGap (0, 156, -2)).add (jLabel25, -1, 240, 32767).add (jPanel1Layout.createSequentialGroup ().add (jEditValor14, -2, 142, -2).addPreferredGap (0, 98, -2))).addContainerGap ()).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jLabel16, -1, -1, 32767).add (223, 223, 223)).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createParallelGroup (2, false).add (1, jEditColecao1, -1, -1, 32767).add (1, jPanel1Layout.createSequentialGroup ().add (jLabel3).addPreferredGap (0).add (debitoAutomatico, -2, 142, -2))).add (2, jLabel1, -1, 240, 32767).add (jPanel1Layout.createSequentialGroup ().add (jPanel1Layout.createParallelGroup (2).add (1, jPanel1Layout.createSequentialGroup ().add (jEditAlfa3, -2, -1, -2).addPreferredGap (0).add (jEditAlfa5, -2, 46, -2)).add (1, jPanel1Layout.createSequentialGroup ().add (jPanel1Layout.createParallelGroup (2, false).add (1, jLabel19, -1, -1, 32767).add (1, jLabel17, -1, -1, 32767).add (1, jEditAlfa1, -1, -1, 32767)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jLabel20).add (jPanel1Layout.createParallelGroup (1).add (jEditAlfa2, -2, 49, -2).add (jLabel18, -1, 71, 32767))))).add (55, 55, 55))).addContainerGap ()));
    jPanel1Layout.linkSize (new Component[] { jEditAlfa2, jEditAlfa5 }, 1);
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().add (jLabel21).addPreferredGap (0).add (jEditValor13, -2, -1, -2).addPreferredGap (0).add (jLabel22).addPreferredGap (0).add (jEditValor15, -2, -1, -2).addPreferredGap (0).add (jLabel2).addPreferredGap (0).add (jLabel23, -2, 24, -2).addPreferredGap (0).add (jEditInteiro2, -2, -1, -2).addPreferredGap (0).add (jLabel25, -2, 23, -2).addPreferredGap (0).add (jEditValor14, -2, -1, -2).addPreferredGap (0).add (jLabel1, -2, 24, -2).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1, false).add (2, debitoAutomatico, -1, -1, 32767).add (2, jLabel3, -1, -1, 32767)).add (15, 15, 15).add (jLabel16).addPreferredGap (0).add (jEditColecao1, -2, -1, -2).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (1).add (jLabel17).add (jLabel18)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jEditAlfa2, -2, -1, -2).add (jEditAlfa1, -2, -1, -2)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jLabel19).add (jLabel20)).addPreferredGap (0).add (jPanel1Layout.createParallelGroup (2).add (jEditAlfa3, -2, -1, -2).add (jEditAlfa5, -2, -1, -2)).addContainerGap (51, 32767)));
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (2, layout.createSequentialGroup ().add (jFlipComponentes1, -1, 215, 32767).addPreferredGap (0).add (jSeparator2, -2, -1, -2).addPreferredGap (0).add (jPanel1, -2, -1, -2).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jSeparator2, -1, 474, 32767).addContainerGap ()).add (layout.createSequentialGroup ().add (jPanel1, -1, -1, 32767).addContainerGap ()).add (jFlipComponentes1, -1, 486, 32767));
  }
  
  private void atualizaSubPainel ()
  {
    if (IRPFFacade.getInstancia ().getIdDeclaracaoAberto () != null && IRPFFacade.getInstancia ().getIdDeclaracaoAberto ().getTipoDeclaracao ().asString ().equals ("1"))
      {
	PlataformaPPGD.getPlataforma ().setHelpID (this, "resumosimp");
	jFlipComponentes1.exibeComponenteB ();
      }
    else
      {
	PlataformaPPGD.getPlataforma ().setHelpID (this, "ResumoC\u00e1lculo");
	jFlipComponentes1.exibeComponenteA ();
      }
  }
  
  private void habilitaDesabilitaControle ()
  {
    if (IRPFFacade.getInstancia ().getDeclaracao () != null)
      {
	int numQuotas = IRPFFacade.getInstancia ().getDeclaracao ().getResumo ().getCalculoImposto ().getNumQuotas ().getConteudoInteiro ();
	boolean ehRetif = IRPFFacade.getInstancia ().getIdDeclaracaoAberto ().getDeclaracaoRetificadora ().asString ().equals (Logico.SIM);
	if (ehRetif)
	  debitoAutomatico.getInformacao ().setHabilitado (false);
	else if (numQuotas >= 2)
	  {
	    debitoAutomatico.getInformacao ().setHabilitado (true);
	    jLabel19.setText ("Conta para d\u00e9bito");
	  }
	else
	  {
	    debitoAutomatico.getInformacao ().setHabilitado (false);
	    debitoAutomatico.getInformacao ().setConteudo ("N");
	    jLabel19.setText ("Conta para cr\u00e9dito");
	  }
      }
  }
  
  public void vaiExibir ()
  {
    habilitaDesabilitaControle ();
  }
  
  public void painelExibido ()
  {
    /* empty */
  }
}
