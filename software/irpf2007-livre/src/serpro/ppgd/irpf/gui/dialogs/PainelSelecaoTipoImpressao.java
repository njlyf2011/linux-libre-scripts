/* PainelSelecaoTipoImpressao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.gui.xbeans.JEditCPF;
import serpro.ppgd.gui.xbeans.JFlipComponentes;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.PreenchedorFichasImpressao;
import serpro.ppgd.irpf.impressao.ImpressaoDeclaracao;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelSelecaoTipoImpressao extends JPanel
{
  private SubPainelSelecaoImpressaoPasso1 subPainelSelecaoImpressaoPasso1 = new SubPainelSelecaoImpressaoPasso1 ();
  private SubPainelSelecaoImpressaoPasso2 subPainelSelecaoImpressaoPasso2 = new SubPainelSelecaoImpressaoPasso2 ();
  private JScrollPane scrollSubPainelImpPasso2 = new JScrollPane (subPainelSelecaoImpressaoPasso2);
  private int passoAtual = 0;
  private boolean primeiroRelatorio = true;
  private IdentificadorDeclaracao identificadorDec = null;
  private JButton btnAjuda;
  private JButton btnCancelar;
  private JButton btnImprimir;
  private JEditCPF edtCPF;
  private JFlipComponentes flipPanel;
  private JLabel jLabel1;
  private JPanel jPanel2;
  
  public PainelSelecaoTipoImpressao (IdentificadorDeclaracao id)
  {
    identificadorDec = id;
    initComponents ();
    PlataformaPPGD.getPlataforma ().setHelpID (this, "CIMPRIMIR");
    PlataformaPPGD.getPlataforma ().setHelpID (btnAjuda, "CIMPRIMIR");
    edtCPF.setInformacao (identificadorDec.getCpf ());
    edtCPF.getComponenteFoco ().setEnabled (false);
    flipPanel.setComponenteA (subPainelSelecaoImpressaoPasso1);
    flipPanel.setComponenteB (scrollSubPainelImpPasso2);
    flipPanel.exibeComponenteA ();
    subPainelSelecaoImpressaoPasso1.rdPartes.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	if (subPainelSelecaoImpressaoPasso1.rdPartes.isSelected ())
	  SwingUtilities.invokeLater (new Runnable ()
	  {
	    public void run ()
	    {
	      passoAtual = 1;
	      btnImprimir.setEnabled (true);
	      flipPanel.exibeComponenteB ();
	    }
	  });
      }
    });
    subPainelSelecaoImpressaoPasso1.rdTodaDeclaracao.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	if (subPainelSelecaoImpressaoPasso1.rdTodaDeclaracao.isSelected ())
	  SwingUtilities.invokeLater (new Runnable ()
	  {
	    public void run ()
	    {
	      passoAtual = 0;
	      btnImprimir.setEnabled (true);
	    }
	  });
      }
    });
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    edtCPF = new JEditCPF ();
    jPanel2 = new JPanel ();
    btnImprimir = new JButton ();
    btnCancelar = new JButton ();
    btnAjuda = new JButton ();
    flipPanel = new JFlipComponentes ();
    jLabel1.setForeground (new Color (0, 0, 128));
    jLabel1.setText ("<HTML><B>CPF selecionado:</B></HTML>");
    btnImprimir.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/impressora.png")));
    btnImprimir.setMnemonic ('I');
    btnImprimir.setText ("<HTML><B><U>I</U>mprimir</B></HTML>");
    btnImprimir.setEnabled (false);
    btnImprimir.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelSelecaoTipoImpressao.this.btnImprimirActionPerformed (evt);
      }
    });
    btnCancelar.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/CANCELAR.PNG")));
    btnCancelar.setMnemonic ('C');
    btnCancelar.setText ("<HTML><B><U>C</U>ancelar</B></HTML>");
    btnCancelar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelSelecaoTipoImpressao.this.btnCancelarActionPerformed (evt);
      }
    });
    btnAjuda.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/HELP.PNG")));
    btnAjuda.setMnemonic ('A');
    btnAjuda.setText ("<HTML><B><U>A</U>juda</B></HTML>");
    GroupLayout jPanel2Layout = new GroupLayout (jPanel2);
    jPanel2.setLayout (jPanel2Layout);
    jPanel2Layout.setHorizontalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().addContainerGap ().add (jPanel2Layout.createParallelGroup (1).add (btnImprimir).add (btnCancelar).add (btnAjuda)).addContainerGap (34, 32767)));
    jPanel2Layout.linkSize (new Component[] { btnAjuda, btnCancelar, btnImprimir }, 1);
    jPanel2Layout.setVerticalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().add (btnImprimir).addPreferredGap (0).add (btnCancelar).addPreferredGap (0).add (btnAjuda).addContainerGap (278, 32767)));
    flipPanel.setBorder (BorderFactory.createBevelBorder (0));
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (flipPanel, -1, 394, 32767).addPreferredGap (0).add (jPanel2, -2, -1, -2)).add (layout.createSequentialGroup ().add (jLabel1, -2, 109, -2).addPreferredGap (0).add (edtCPF, -2, 134, -2))).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (2, layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (2).add (edtCPF, -2, -1, -2).add (jLabel1)).addPreferredGap (0).add (layout.createParallelGroup (1).add (flipPanel, -1, 377, 32767).add (jPanel2, -1, -1, 32767)).addContainerGap ()));
    layout.linkSize (new Component[] { edtCPF, jLabel1 }, 2);
  }
  
  private void btnCancelarActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
  
  private void btnImprimirActionPerformed (ActionEvent evt)
  {
    String pathDados = identificadorDec.getPathArquivo ();
    IdentificadorDeclaracao idDec = identificadorDec;
    IRPFFacade.abreDeclaracao (idDec);
    DeclaracaoIRPF dec = IRPFFacade.getInstancia ().getDeclaracao ();
    if (passoAtual == 0)
      {
	ImpressaoDeclaracao novaImpressao = new ImpressaoDeclaracao ();
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoContribuinte.jasper", pathDados, "/classe");
	PreenchedorFichasImpressao.preencheContribuinte (novaImpressao, dec, true);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoConjuge.jasper", pathDados, "/classe");
	PreenchedorFichasImpressao.preencheConjuge (novaImpressao, dec, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosTributaveisPJTitular.jasper", pathDados, "/classe/rendPJ/colecaoRendPJTitular/item");
	PreenchedorFichasImpressao.preencheRendPJTitular (novaImpressao, dec, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosTributaveisPJDependente.jasper", pathDados, "/classe/rendPJ/colecaoRendPJDependente/item");
	PreenchedorFichasImpressao.preencheRendPJDependente (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosTributaveisPFTitular.jasper", pathDados, "/classe");
	PreenchedorFichasImpressao.preencheRendPFTitular (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosTributaveisPFDependente.jasper", pathDados, "/classe/rendPFDependente/colecaoCPFDependentes/item");
	PreenchedorFichasImpressao.preencheRendPFDependente (dec, novaImpressao, false);
	if (dec.getRendIsentos ().getLucroRecebidoQuadroAuxiliar ().isVazio ())
	  novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosIsentosNaoTributaveis.jasper", pathDados, "/classe");
	else
	  novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosIsentosNaoTributaveisLucros.jasper", pathDados, "/classe/rendIsentos/lucroRecebidoQuadroAuxiliar/item");
	PreenchedorFichasImpressao.preencheRendIsentos (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosSujTribExclusiva.jasper", pathDados, "/classe");
	PreenchedorFichasImpressao.preencheRendTribExcl (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoImpostoPago.jasper", pathDados, "/classe");
	PreenchedorFichasImpressao.preencheImpostoPago (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoDependentes.jasper", pathDados, "/classe/dependentes/item");
	PreenchedorFichasImpressao.preencheDependentes (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoPagamentosDoacoes.jasper", pathDados, "/classe/pagamentos/item");
	PreenchedorFichasImpressao.preenchePgtosDoacoes (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoBensDireitos.jasper", pathDados, "/classe/bens/item");
	PreenchedorFichasImpressao.preencheBensDireitos (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoDividasOnusReais.jasper", pathDados, "/classe/dividas/item");
	PreenchedorFichasImpressao.preencheDividasOnus (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoInventariante.jasper", pathDados, "/classe");
	PreenchedorFichasImpressao.preencheInventariante (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoDoacoesEleitorais.jasper", pathDados, "/classe/doacoes/item");
	PreenchedorFichasImpressao.preencheDoacoesCampanha (dec, novaImpressao, false);
	if (idDec.getTipoDeclaracao ().asString ().equals ("0"))
	  novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoResumo.jasper", pathDados, "/classe");
	else
	  novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoResumoSimplificada.jasper", pathDados, "/classe");
	PreenchedorFichasImpressao.preencheResumo (dec, novaImpressao, false);
	if (! dec.getAtividadeRural ().getBrasil ().isVazio ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARIdentificacao.jasper", pathDados, "/classe/atividadeRural/brasil/identificacaoImovel/item");
	    novaImpressao.addParametroUltimo ("ARLocal", "BRASIL");
	    PreenchedorFichasImpressao.preencheARBrasil (dec, novaImpressao, false);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARReceitasDespesasBrasil.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheARBrasil (dec, novaImpressao, false);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARApuracaoBrasil.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheARApuracaoBrasil (dec, novaImpressao, false);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARRebanhoBrasil.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheARBrasil (dec, novaImpressao, false);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARBensDaAtividadeRural.jasper", pathDados, "/classe/atividadeRural/brasil/bens/item");
	    PreenchedorFichasImpressao.preencheARBrasil (dec, novaImpressao, false);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARDividas.jasper", pathDados, "/classe/atividadeRural/brasil/dividas/item");
	    PreenchedorFichasImpressao.preencheARBrasil (dec, novaImpressao, false);
	  }
	if (! dec.getAtividadeRural ().getExterior ().isVazio ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARIdentificacao.jasper", pathDados, "/classe/atividadeRural/exterior/identificacaoImovel/item");
	    PreenchedorFichasImpressao.preencheARExterior (dec, novaImpressao, false);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARReceitasDespesasExterior.jasper", pathDados, "/classe/atividadeRural/exterior/receitasDespesas/item");
	    PreenchedorFichasImpressao.preencheARExterior (dec, novaImpressao, false);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARApuracaoExterior.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheARApuracaoExterior (dec, novaImpressao, false);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARRebanhoExterior.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheARExterior (dec, novaImpressao, false);
	    if (dec.getAtividadeRural ().getExterior ().getBens ().isVazio ())
	      {
		novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARBensDaAtividadeRuralExterior-SemInfo.jasper", pathDados, "/classe");
		PreenchedorFichasImpressao.preencheARExterior (dec, novaImpressao, false);
	      }
	    else
	      {
		novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARBensDaAtividadeRuralExterior.jasper", pathDados, "/classe/atividadeRural/exterior/bens/item");
		PreenchedorFichasImpressao.preencheARExterior (dec, novaImpressao, false);
	      }
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARDividas.jasper", pathDados, "/classe/atividadeRural/exterior/dividas/item");
	    PreenchedorFichasImpressao.preencheARExterior (dec, novaImpressao, false);
	  }
	if (! dec.getRendaVariavel ().isVazio ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/janeiro");
	    novaImpressao.addParametroUltimo ("MES", "JANEIRO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/fevereiro");
	    novaImpressao.addParametroUltimo ("MES", "FEVEREIRO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/marco");
	    novaImpressao.addParametroUltimo ("MES", "MARCO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/abril");
	    novaImpressao.addParametroUltimo ("MES", "ABRIL");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/maio");
	    novaImpressao.addParametroUltimo ("MES", "MAIO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/junho");
	    novaImpressao.addParametroUltimo ("MES", "JUNHO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/julho");
	    novaImpressao.addParametroUltimo ("MES", "JULHO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/agosto");
	    novaImpressao.addParametroUltimo ("MES", "AGOSTO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/setembro");
	    novaImpressao.addParametroUltimo ("MES", "SETEMBRO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/outubro");
	    novaImpressao.addParametroUltimo ("MES", "OUTUBRO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/novembro");
	    novaImpressao.addParametroUltimo ("MES", "NOVEMBRO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/dezembro");
	    novaImpressao.addParametroUltimo ("MES", "DEZEMBRO");
	  }
	if (! dec.getRendaVariavel ().getFundInvest ().isVazio ())
	  {
	    ImpressaoDeclaracao novaImpressaoFundoInvest = new ImpressaoDeclaracao ();
	    novaImpressaoFundoInvest.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavelFundoInvestimento.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheRelatorioGeral (novaImpressaoFundoInvest, dec);
	    novaImpressaoFundoInvest.visualizar ();
	  }
	novaImpressao.visualizar ();
      }
    else if (passoAtual == 1)
      {
	ImpressaoDeclaracao novaImpressao = new ImpressaoDeclaracao ();
	if (subPainelSelecaoImpressaoPasso2.chkContribuinte.isSelected ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoContribuinte.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheContribuinte (novaImpressao, dec, primeiroRelatorio);
	    primeiroRelatorio = false;
	  }
	if (subPainelSelecaoImpressaoPasso2.chkConjuge.isSelected ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoConjuge.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheConjuge (novaImpressao, dec, primeiroRelatorio);
	    primeiroRelatorio = false;
	  }
	if (subPainelSelecaoImpressaoPasso2.chkRendPJTitular.isSelected ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosTributaveisPJTitular.jasper", pathDados, "/classe/rendPJ/colecaoRendPJTitular/item");
	    PreenchedorFichasImpressao.preencheRendPJTitular (novaImpressao, dec, primeiroRelatorio);
	    primeiroRelatorio = false;
	  }
	if (subPainelSelecaoImpressaoPasso2.chkRendPJDep.isSelected ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosTributaveisPJDependente.jasper", pathDados, "/classe/rendPJ/colecaoRendPJDependente/item");
	    PreenchedorFichasImpressao.preencheRendPJDependente (dec, novaImpressao, primeiroRelatorio);
	    primeiroRelatorio = false;
	  }
	if (subPainelSelecaoImpressaoPasso2.chkRendPFTitular.isSelected ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosTributaveisPFTitular.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheRendPFTitular (dec, novaImpressao, primeiroRelatorio);
	    primeiroRelatorio = false;
	  }
	if (subPainelSelecaoImpressaoPasso2.chkRendPFDep.isSelected ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosTributaveisPFDependente.jasper", pathDados, "/classe/rendPFDependente/colecaoCPFDependentes/item");
	    PreenchedorFichasImpressao.preencheRendPFDependente (dec, novaImpressao, primeiroRelatorio);
	    primeiroRelatorio = false;
	  }
	if (subPainelSelecaoImpressaoPasso2.chkRendIsentos.isSelected ())
	  {
	    if (dec.getRendIsentos ().getLucroRecebidoQuadroAuxiliar ().isVazio ())
	      novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosIsentosNaoTributaveis.jasper", pathDados, "/classe");
	    else
	      novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosIsentosNaoTributaveisLucros.jasper", pathDados, "/classe/rendIsentos/lucroRecebidoQuadroAuxiliar/item");
	    PreenchedorFichasImpressao.preencheRendIsentos (dec, novaImpressao, primeiroRelatorio);
	    primeiroRelatorio = false;
	  }
	if (subPainelSelecaoImpressaoPasso2.chkRendSujTributExcl.isSelected ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosSujTribExclusiva.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheRendTribExcl (dec, novaImpressao, primeiroRelatorio);
	    primeiroRelatorio = false;
	  }
	if (subPainelSelecaoImpressaoPasso2.chkImpostoPago.isSelected ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoImpostoPago.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheImpostoPago (dec, novaImpressao, primeiroRelatorio);
	    primeiroRelatorio = false;
	  }
	if (subPainelSelecaoImpressaoPasso2.chkDependentes.isSelected ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoDependentes.jasper", pathDados, "/classe/dependentes/item");
	    PreenchedorFichasImpressao.preencheDependentes (dec, novaImpressao, primeiroRelatorio);
	    primeiroRelatorio = false;
	  }
	if (subPainelSelecaoImpressaoPasso2.chkPagamentos.isSelected ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoPagamentosDoacoes.jasper", pathDados, "/classe/pagamentos/item");
	    PreenchedorFichasImpressao.preenchePgtosDoacoes (dec, novaImpressao, primeiroRelatorio);
	    primeiroRelatorio = false;
	  }
	if (subPainelSelecaoImpressaoPasso2.chkBens.isSelected ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoBensDireitos.jasper", pathDados, "/classe/bens/item");
	    PreenchedorFichasImpressao.preencheBensDireitos (dec, novaImpressao, primeiroRelatorio);
	    primeiroRelatorio = false;
	  }
	if (subPainelSelecaoImpressaoPasso2.chkDividas.isSelected ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoDividasOnusReais.jasper", pathDados, "/classe/dividas/item");
	    PreenchedorFichasImpressao.preencheDividasOnus (dec, novaImpressao, primeiroRelatorio);
	    primeiroRelatorio = false;
	  }
	if (subPainelSelecaoImpressaoPasso2.chkDoacoesCampanha.isSelected ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoDoacoesEleitorais.jasper", pathDados, "/classe/doacoes/item");
	    PreenchedorFichasImpressao.preencheDoacoesCampanha (dec, novaImpressao, primeiroRelatorio);
	    primeiroRelatorio = false;
	  }
	if (subPainelSelecaoImpressaoPasso2.chkEspolio.isSelected ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoInventariante.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheInventariante (dec, novaImpressao, primeiroRelatorio);
	    primeiroRelatorio = false;
	  }
	if (subPainelSelecaoImpressaoPasso2.chkResumoDeclaracao.isSelected ())
	  {
	    if (idDec.getTipoDeclaracao ().asString ().equals ("0"))
	      novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoResumo.jasper", pathDados, "/classe");
	    else
	      novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoResumoSimplificada.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheResumo (dec, novaImpressao, primeiroRelatorio);
	    primeiroRelatorio = false;
	  }
	if (subPainelSelecaoImpressaoPasso2.chkARBrasil.isSelected ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARIdentificacao.jasper", pathDados, "/classe/atividadeRural/brasil/identificacaoImovel/item");
	    PreenchedorFichasImpressao.preencheARBrasil (dec, novaImpressao, primeiroRelatorio);
	    primeiroRelatorio = false;
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARReceitasDespesasBrasil.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheARBrasil (dec, novaImpressao, primeiroRelatorio);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARApuracaoBrasil.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheARBrasil (dec, novaImpressao, primeiroRelatorio);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARRebanhoBrasil.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheARBrasil (dec, novaImpressao, primeiroRelatorio);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARBensDaAtividadeRural.jasper", pathDados, "/classe/atividadeRural/brasil/bens/item");
	    PreenchedorFichasImpressao.preencheARBrasil (dec, novaImpressao, primeiroRelatorio);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARDividas.jasper", pathDados, "/classe/atividadeRural/brasil/dividas/item");
	    PreenchedorFichasImpressao.preencheARBrasil (dec, novaImpressao, primeiroRelatorio);
	  }
	if (subPainelSelecaoImpressaoPasso2.chkARExterior.isSelected ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARIdentificacao.jasper", pathDados, "/classe/atividadeRural/exterior/identificacaoImovel/item");
	    PreenchedorFichasImpressao.preencheARExterior (dec, novaImpressao, primeiroRelatorio);
	    primeiroRelatorio = false;
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARReceitasDespesasExterior.jasper", pathDados, "/classe/atividadeRural/exterior/receitasDespesas/item");
	    PreenchedorFichasImpressao.preencheARExterior (dec, novaImpressao, primeiroRelatorio);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARApuracaoExterior.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheARExterior (dec, novaImpressao, primeiroRelatorio);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARRebanhoExterior.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheARExterior (dec, novaImpressao, primeiroRelatorio);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARBensDaAtividadeRuralExterior.jasper", pathDados, "/classe/atividadeRural/exterior/bens/item");
	    PreenchedorFichasImpressao.preencheARExterior (dec, novaImpressao, primeiroRelatorio);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARDividas.jasper", pathDados, "/classe/atividadeRural/exterior/dividas/item");
	    PreenchedorFichasImpressao.preencheARExterior (dec, novaImpressao, primeiroRelatorio);
	  }
	if (subPainelSelecaoImpressaoPasso2.chkResumoRendaVariavel.isSelected ())
	  {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/janeiro");
	    novaImpressao.addParametroUltimo ("MES", "JANEIRO");
	    PreenchedorFichasImpressao.preencheRendaVariavel (dec, novaImpressao, primeiroRelatorio);
	    primeiroRelatorio = false;
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/fevereiro");
	    novaImpressao.addParametroUltimo ("MES", "FEVEREIRO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/marco");
	    novaImpressao.addParametroUltimo ("MES", "MARCO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/abril");
	    novaImpressao.addParametroUltimo ("MES", "ABRIL");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/maio");
	    novaImpressao.addParametroUltimo ("MES", "MAIO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/junho");
	    novaImpressao.addParametroUltimo ("MES", "JUNHO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/julho");
	    novaImpressao.addParametroUltimo ("MES", "JULHO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/agosto");
	    novaImpressao.addParametroUltimo ("MES", "AGOSTO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/setembro");
	    novaImpressao.addParametroUltimo ("MES", "SETEMBRO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/outubro");
	    novaImpressao.addParametroUltimo ("MES", "OUTUBRO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/novembro");
	    novaImpressao.addParametroUltimo ("MES", "NOVEMBRO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/dezembro");
	    novaImpressao.addParametroUltimo ("MES", "DEZEMBRO");
	  }
	if (subPainelSelecaoImpressaoPasso2.chkFundoInvest.isSelected ())
	  {
	    ImpressaoDeclaracao novaImpressaoFundoInvest = new ImpressaoDeclaracao ();
	    novaImpressaoFundoInvest.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavelFundoInvestimento.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheRelatorioGeral (novaImpressaoFundoInvest, dec);
	    novaImpressaoFundoInvest.visualizar ();
	  }
	if (novaImpressao.relatorio != null)
	  {
	    novaImpressao.relatorio.prepara ();
	    if (novaImpressao.relatorio.getQtdPaginas () > 0)
	      novaImpressao.visualizar ();
	    else
	      JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), TabelaMensagens.getTabelaMensagens ().msg ("print_sem_informacao"), "IRPF" + ConstantesGlobais.EXERCICIO, 1);
	  }
      }
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
}
