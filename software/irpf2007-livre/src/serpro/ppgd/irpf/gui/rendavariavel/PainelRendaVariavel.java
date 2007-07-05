/* PainelRendaVariavel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.rendavariavel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.border.SoftBevelBorder;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.UtilitariosGUI;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.rendavariavel.GanhosLiquidosOuPerdas;
import serpro.ppgd.irpf.rendavariavel.Operacoes;

public class PainelRendaVariavel extends JPanel implements PainelIRPFIf
{
  private static final String TITULO = "Renda Vari\u00e1vel - Opera\u00e7\u00f5es Comuns/Day-Trade";
  private ButtonGroup btnGroup;
  private JEditValor jEditValor1;
  private JEditValor jEditValor10;
  private JEditValor jEditValor11;
  private JEditValor jEditValor12;
  private JEditValor jEditValor13;
  private JEditValor jEditValor14;
  private JEditValor jEditValor15;
  private JEditValor jEditValor16;
  private JEditValor jEditValor17;
  private JEditValor jEditValor18;
  private JEditValor jEditValor19;
  private JEditValor jEditValor2;
  private JEditValor jEditValor20;
  private JEditValor jEditValor21;
  private JEditValor jEditValor22;
  private JEditValor jEditValor23;
  private JEditValor jEditValor24;
  private JEditValor jEditValor25;
  private JEditValor jEditValor26;
  private JEditValor jEditValor27;
  private JEditValor jEditValor28;
  private JEditValor jEditValor29;
  private JEditValor jEditValor3;
  private JEditValor jEditValor30;
  private JEditValor jEditValor31;
  private JEditValor jEditValor32;
  private JEditValor jEditValor33;
  private JEditValor jEditValor34;
  private JEditValor jEditValor35;
  private JEditValor jEditValor36;
  private JEditValor jEditValor37;
  private JEditValor jEditValor38;
  private JEditValor jEditValor39;
  private JEditValor jEditValor4;
  private JEditValor jEditValor40;
  private JEditValor jEditValor41;
  private JEditValor jEditValor42;
  private JEditValor jEditValor43;
  private JEditValor jEditValor5;
  private JEditValor jEditValor6;
  private JEditValor jEditValor7;
  private JEditValor jEditValor8;
  private JEditValor jEditValor9;
  private JLabel jLabel1;
  private JLabel jLabel10;
  private JLabel jLabel11;
  private JLabel jLabel12;
  private JLabel jLabel13;
  private JLabel jLabel14;
  private JLabel jLabel15;
  private JLabel jLabel16;
  private JLabel jLabel17;
  private JLabel jLabel18;
  private JLabel jLabel19;
  private JLabel jLabel2;
  private JLabel jLabel20;
  private JLabel jLabel21;
  private JLabel jLabel22;
  private JLabel jLabel23;
  private JLabel jLabel24;
  private JLabel jLabel25;
  private JLabel jLabel26;
  private JLabel jLabel27;
  private JLabel jLabel28;
  private JLabel jLabel29;
  private JLabel jLabel3;
  private JLabel jLabel30;
  private JLabel jLabel31;
  private JLabel jLabel32;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JLabel jLabel9;
  private JPanel jPanel1;
  private JPanel jPanel2;
  private JPanel jPanel3;
  private JPanel jPanel4;
  private JScrollPane jScrollPane1;
  private JTextField jTextField1;
  private JTextField jTextField2;
  private JToggleButton jToggleButton1;
  private JToggleButton jToggleButton10;
  private JToggleButton jToggleButton11;
  private JToggleButton jToggleButton12;
  private JToggleButton jToggleButton2;
  private JToggleButton jToggleButton3;
  private JToggleButton jToggleButton4;
  private JToggleButton jToggleButton5;
  private JToggleButton jToggleButton6;
  private JToggleButton jToggleButton7;
  private JToggleButton jToggleButton8;
  private JToggleButton jToggleButton9;
  private JToolBar jToolBar1;
  public JLabel lblMes;
  
  public String getTituloPainel ()
  {
    return "Renda Vari\u00e1vel - Opera\u00e7\u00f5es Comuns/Day-Trade";
  }
  
  public PainelRendaVariavel ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "RendaVariavel");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (1);
    f = f.deriveFont (f.getSize2D () + 1.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
    jToggleButton1.setSelected (true);
    mudaMes (jToggleButton1);
  }
  
  private int obtemMes (JToggleButton btn)
  {
    if (btn.getText ().toUpperCase ().startsWith ("JAN"))
      {
	lblMes.setText ("Janeiro".toUpperCase ());
	return 0;
      }
    if (btn.getText ().toUpperCase ().startsWith ("FEV"))
      {
	lblMes.setText ("Fevereiro".toUpperCase ());
	return 1;
      }
    if (btn.getText ().toUpperCase ().startsWith ("MAR"))
      {
	lblMes.setText ("Mar\u00e7o".toUpperCase ());
	return 2;
      }
    if (btn.getText ().toUpperCase ().startsWith ("ABR"))
      {
	lblMes.setText ("Abril".toUpperCase ());
	return 3;
      }
    if (btn.getText ().toUpperCase ().startsWith ("MAI"))
      {
	lblMes.setText ("Maio".toUpperCase ());
	return 4;
      }
    if (btn.getText ().toUpperCase ().startsWith ("JUN"))
      {
	lblMes.setText ("Junho".toUpperCase ());
	return 5;
      }
    if (btn.getText ().toUpperCase ().startsWith ("JUL"))
      {
	lblMes.setText ("Julho".toUpperCase ());
	return 6;
      }
    if (btn.getText ().toUpperCase ().startsWith ("AGO"))
      {
	lblMes.setText ("Agosto".toUpperCase ());
	return 7;
      }
    if (btn.getText ().toUpperCase ().startsWith ("SET"))
      {
	lblMes.setText ("Setembro".toUpperCase ());
	return 8;
      }
    if (btn.getText ().toUpperCase ().startsWith ("OUT"))
      {
	lblMes.setText ("Outubro".toUpperCase ());
	return 9;
      }
    if (btn.getText ().toUpperCase ().startsWith ("NOV"))
      {
	lblMes.setText ("Novembro".toUpperCase ());
	return 10;
      }
    if (btn.getText ().toUpperCase ().startsWith ("DEZ"))
      {
	lblMes.setText ("Dezembro".toUpperCase ());
	return 11;
      }
    return 0;
  }
  
  public void mudaMes (JToggleButton btn)
  {
    int mes = obtemMes (btn);
    GanhosLiquidosOuPerdas ganhosLiquidosOuPerdas = IRPFFacade.getInstancia ().getRendaVariavel ().getGanhosPorIndice (mes);
    Operacoes opComuns = ganhosLiquidosOuPerdas.getOperacoesComuns ();
    jEditValor2.setInformacao (opComuns.getMercadoVistaAcoes ());
    jEditValor4.setInformacao (opComuns.getMercadoVistaOuro ());
    jEditValor6.setInformacao (opComuns.getMercadoVistaForaBolsa ());
    jEditValor8.setInformacao (opComuns.getMercadoOpcoesAcoes ());
    jEditValor10.setInformacao (opComuns.getMercadoOpcoesOuro ());
    jEditValor12.setInformacao (opComuns.getMercadoOpcoesForaDeBolsa ());
    jEditValor14.setInformacao (opComuns.getMercadoOpcoesOutros ());
    jEditValor16.setInformacao (opComuns.getMercadoFuturoDolar ());
    jEditValor18.setInformacao (opComuns.getMercadoFuturoIndices ());
    jEditValor20.setInformacao (opComuns.getMercadoFuturoJuros ());
    jEditValor22.setInformacao (opComuns.getMercadoFuturoOutros ());
    jEditValor24.setInformacao (opComuns.getMercadoTermoAcoes ());
    jEditValor26.setInformacao (opComuns.getMercadoTermoOutros ());
    jEditValor28.setInformacao (opComuns.getResultadoLiquidoMes ());
    jEditValor30.setInformacao (opComuns.getResultadoNegativoMesAnterior ());
    jEditValor32.setInformacao (opComuns.getBaseCalculoImposto ());
    jEditValor34.setInformacao (opComuns.getPrejuizoCompensar ());
    jEditValor38.setInformacao (opComuns.getImpostoDevido ());
    Operacoes opDayTrade = ganhosLiquidosOuPerdas.getOperacoesDayTrade ();
    jEditValor1.setInformacao (opDayTrade.getMercadoVistaAcoes ());
    jEditValor3.setInformacao (opDayTrade.getMercadoVistaOuro ());
    jEditValor5.setInformacao (opDayTrade.getMercadoVistaForaBolsa ());
    jEditValor7.setInformacao (opDayTrade.getMercadoOpcoesAcoes ());
    jEditValor9.setInformacao (opDayTrade.getMercadoOpcoesOuro ());
    jEditValor11.setInformacao (opDayTrade.getMercadoOpcoesForaDeBolsa ());
    jEditValor13.setInformacao (opDayTrade.getMercadoOpcoesOutros ());
    jEditValor15.setInformacao (opDayTrade.getMercadoFuturoDolar ());
    jEditValor17.setInformacao (opDayTrade.getMercadoFuturoIndices ());
    jEditValor19.setInformacao (opDayTrade.getMercadoFuturoJuros ());
    jEditValor21.setInformacao (opDayTrade.getMercadoFuturoOutros ());
    jEditValor23.setInformacao (opDayTrade.getMercadoTermoAcoes ());
    jEditValor25.setInformacao (opDayTrade.getMercadoTermoOutros ());
    jEditValor27.setInformacao (opDayTrade.getResultadoLiquidoMes ());
    jEditValor29.setInformacao (opDayTrade.getResultadoNegativoMesAnterior ());
    jEditValor31.setInformacao (opDayTrade.getBaseCalculoImposto ());
    jEditValor33.setInformacao (opDayTrade.getPrejuizoCompensar ());
    jEditValor37.setInformacao (opDayTrade.getImpostoDevido ());
    jEditValor39.setInformacao (ganhosLiquidosOuPerdas.getTotalImpostoDevido ());
    jEditValor40.setInformacao (ganhosLiquidosOuPerdas.getIrFonteDayTradeMesAtual ());
    jEditValor41.setInformacao (ganhosLiquidosOuPerdas.getIrFonteDayTradeMesesAnteriores ());
    jEditValor42.setInformacao (ganhosLiquidosOuPerdas.getIrFonteDayTradeAcompensar ());
    jEditValor43.setInformacao (ganhosLiquidosOuPerdas.getImpostoRetidoFonteLei11033 ());
    jEditValor35.setInformacao (ganhosLiquidosOuPerdas.getImpostoApagar ());
    jEditValor36.setInformacao (ganhosLiquidosOuPerdas.getImpostoPago ());
  }
  
  private void initComponents ()
  {
    btnGroup = new ButtonGroup ();
    jPanel1 = new JPanel ();
    jToolBar1 = new JToolBar ();
    jToggleButton1 = new JToggleButton ();
    UtilitariosGUI.estiloFonte (jToggleButton1, 1);
    jToggleButton2 = new JToggleButton ();
    UtilitariosGUI.estiloFonte (jToggleButton2, 1);
    jToggleButton3 = new JToggleButton ();
    UtilitariosGUI.estiloFonte (jToggleButton3, 1);
    jToggleButton4 = new JToggleButton ();
    UtilitariosGUI.estiloFonte (jToggleButton4, 1);
    jToggleButton5 = new JToggleButton ();
    UtilitariosGUI.estiloFonte (jToggleButton5, 1);
    jToggleButton6 = new JToggleButton ();
    UtilitariosGUI.estiloFonte (jToggleButton6, 1);
    jToggleButton7 = new JToggleButton ();
    UtilitariosGUI.estiloFonte (jToggleButton7, 1);
    jToggleButton8 = new JToggleButton ();
    UtilitariosGUI.estiloFonte (jToggleButton8, 1);
    jToggleButton9 = new JToggleButton ();
    UtilitariosGUI.estiloFonte (jToggleButton9, 1);
    jToggleButton10 = new JToggleButton ();
    UtilitariosGUI.estiloFonte (jToggleButton10, 1);
    jToggleButton11 = new JToggleButton ();
    UtilitariosGUI.estiloFonte (jToggleButton11, 1);
    jToggleButton12 = new JToggleButton ();
    UtilitariosGUI.estiloFonte (jToggleButton12, 1);
    jPanel2 = new JPanel ();
    jLabel1 = new JLabel ();
    lblMes = new JLabel ();
    jLabel2 = new JLabel ();
    jLabel3 = new JLabel ();
    jLabel4 = new JLabel ();
    jLabel5 = new JLabel ();
    jPanel4 = new JPanel ();
    jLabel6 = new JLabel ();
    jScrollPane1 = new JScrollPane ();
    jPanel3 = new JPanel ();
    jLabel7 = new JLabel ();
    jLabel8 = new JLabel ();
    jLabel9 = new JLabel ();
    jLabel10 = new JLabel ();
    jLabel11 = new JLabel ();
    jLabel12 = new JLabel ();
    jLabel13 = new JLabel ();
    jLabel14 = new JLabel ();
    jLabel15 = new JLabel ();
    jLabel16 = new JLabel ();
    jLabel17 = new JLabel ();
    jLabel18 = new JLabel ();
    jLabel19 = new JLabel ();
    jLabel20 = new JLabel ();
    jLabel21 = new JLabel ();
    jLabel22 = new JLabel ();
    jLabel23 = new JLabel ();
    jLabel24 = new JLabel ();
    jLabel25 = new JLabel ();
    jEditValor1 = new JEditValor ();
    jEditValor2 = new JEditValor ();
    jEditValor3 = new JEditValor ();
    jEditValor4 = new JEditValor ();
    jEditValor5 = new JEditValor ();
    jEditValor6 = new JEditValor ();
    jEditValor7 = new JEditValor ();
    jEditValor8 = new JEditValor ();
    jEditValor9 = new JEditValor ();
    jEditValor10 = new JEditValor ();
    jEditValor11 = new JEditValor ();
    jEditValor12 = new JEditValor ();
    jEditValor13 = new JEditValor ();
    jEditValor14 = new JEditValor ();
    jEditValor15 = new JEditValor ();
    jEditValor16 = new JEditValor ();
    jEditValor17 = new JEditValor ();
    jEditValor18 = new JEditValor ();
    jEditValor19 = new JEditValor ();
    jEditValor20 = new JEditValor ();
    jEditValor21 = new JEditValor ();
    jEditValor22 = new JEditValor ();
    jEditValor23 = new JEditValor ();
    jEditValor24 = new JEditValor ();
    jEditValor25 = new JEditValor ();
    jEditValor26 = new JEditValor ();
    jEditValor27 = new JEditValor ();
    jEditValor28 = new JEditValor ();
    jEditValor29 = new JEditValor ();
    jEditValor30 = new JEditValor ();
    jEditValor31 = new JEditValor ();
    jEditValor32 = new JEditValor ();
    jEditValor33 = new JEditValor ();
    jEditValor34 = new JEditValor ();
    jEditValor37 = new JEditValor ();
    jEditValor38 = new JEditValor ();
    jTextField1 = new JTextField ();
    UtilitariosGUI.estiloFonte (jTextField1, 1);
    jTextField2 = new JTextField ();
    UtilitariosGUI.estiloFonte (jTextField2, 1);
    jLabel26 = new JLabel ();
    jLabel27 = new JLabel ();
    jLabel28 = new JLabel ();
    jLabel29 = new JLabel ();
    jLabel30 = new JLabel ();
    jEditValor39 = new JEditValor ();
    jEditValor40 = new JEditValor ();
    jEditValor41 = new JEditValor ();
    jEditValor42 = new JEditValor ();
    jEditValor43 = new JEditValor ();
    jLabel31 = new JLabel ();
    jEditValor35 = new JEditValor ();
    jLabel32 = new JLabel ();
    jEditValor36 = new JEditValor ();
    jPanel1.setLayout (new FlowLayout (1, 0, 1));
    btnGroup.add (jToggleButton1);
    jToggleButton1.setText ("JAN");
    jToggleButton1.setBorder (new SoftBevelBorder (0));
    jToggleButton1.setIconTextGap (1);
    jToggleButton1.setPreferredSize (new Dimension (51, 23));
    jToggleButton1.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent evt)
      {
	PainelRendaVariavel.this.mouseClicked (evt);
      }
    });
    jToolBar1.add (jToggleButton1);
    btnGroup.add (jToggleButton2);
    jToggleButton2.setText ("FEV");
    jToggleButton2.setBorder (new SoftBevelBorder (0));
    jToggleButton2.setIconTextGap (1);
    jToggleButton2.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent evt)
      {
	PainelRendaVariavel.this.mouseClicked (evt);
      }
    });
    jToolBar1.add (jToggleButton2);
    btnGroup.add (jToggleButton3);
    jToggleButton3.setText ("MAR");
    jToggleButton3.setBorder (new SoftBevelBorder (0));
    jToggleButton3.setIconTextGap (1);
    jToggleButton3.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent evt)
      {
	PainelRendaVariavel.this.mouseClicked (evt);
      }
    });
    jToolBar1.add (jToggleButton3);
    btnGroup.add (jToggleButton4);
    jToggleButton4.setText ("ABR");
    jToggleButton4.setBorder (new SoftBevelBorder (0));
    jToggleButton4.setIconTextGap (1);
    jToggleButton4.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent evt)
      {
	PainelRendaVariavel.this.mouseClicked (evt);
      }
    });
    jToolBar1.add (jToggleButton4);
    btnGroup.add (jToggleButton5);
    jToggleButton5.setText ("MAI");
    jToggleButton5.setBorder (new SoftBevelBorder (0));
    jToggleButton5.setIconTextGap (1);
    jToggleButton5.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent evt)
      {
	PainelRendaVariavel.this.mouseClicked (evt);
      }
    });
    jToolBar1.add (jToggleButton5);
    btnGroup.add (jToggleButton6);
    jToggleButton6.setText ("JUN");
    jToggleButton6.setBorder (new SoftBevelBorder (0));
    jToggleButton6.setIconTextGap (1);
    jToggleButton6.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent evt)
      {
	PainelRendaVariavel.this.mouseClicked (evt);
      }
    });
    jToolBar1.add (jToggleButton6);
    btnGroup.add (jToggleButton7);
    jToggleButton7.setText ("JUL");
    jToggleButton7.setBorder (new SoftBevelBorder (0));
    jToggleButton7.setIconTextGap (1);
    jToggleButton7.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent evt)
      {
	PainelRendaVariavel.this.mouseClicked (evt);
      }
    });
    jToolBar1.add (jToggleButton7);
    btnGroup.add (jToggleButton8);
    jToggleButton8.setText ("AGO");
    jToggleButton8.setBorder (new SoftBevelBorder (0));
    jToggleButton8.setIconTextGap (1);
    jToggleButton8.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent evt)
      {
	PainelRendaVariavel.this.mouseClicked (evt);
      }
    });
    jToolBar1.add (jToggleButton8);
    btnGroup.add (jToggleButton9);
    jToggleButton9.setText ("SET");
    jToggleButton9.setBorder (new SoftBevelBorder (0));
    jToggleButton9.setIconTextGap (1);
    jToggleButton9.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent evt)
      {
	PainelRendaVariavel.this.mouseClicked (evt);
      }
    });
    jToolBar1.add (jToggleButton9);
    btnGroup.add (jToggleButton10);
    jToggleButton10.setText ("OUT");
    jToggleButton10.setBorder (new SoftBevelBorder (0));
    jToggleButton10.setIconTextGap (1);
    jToggleButton10.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent evt)
      {
	PainelRendaVariavel.this.mouseClicked (evt);
      }
    });
    jToolBar1.add (jToggleButton10);
    btnGroup.add (jToggleButton11);
    jToggleButton11.setText ("NOV");
    jToggleButton11.setBorder (new SoftBevelBorder (0));
    jToggleButton11.setIconTextGap (1);
    jToggleButton11.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent evt)
      {
	PainelRendaVariavel.this.mouseClicked (evt);
      }
    });
    jToolBar1.add (jToggleButton11);
    btnGroup.add (jToggleButton12);
    jToggleButton12.setText ("DEZ");
    jToggleButton12.setBorder (new SoftBevelBorder (0));
    jToggleButton12.setIconTextGap (1);
    jToggleButton12.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent evt)
      {
	PainelRendaVariavel.this.mouseClicked (evt);
      }
    });
    jToolBar1.add (jToggleButton12);
    jPanel1.add (jToolBar1);
    jLabel1.setText ("GANHOS L\u00cdQUIDOS OU PERDAS -");
    jPanel2.add (jLabel1);
    lblMes.setText ("JANEIRO");
    jPanel2.add (lblMes);
    jLabel2.setText ("Tipo de Mercado/Ativo");
    jLabel3.setText ("Opera\u00e7\u00f5es");
    jLabel4.setText ("Comuns");
    jLabel5.setText ("Day-Trade");
    jLabel6.setText ("CONSOLIDA\u00c7\u00c3O DO M\u00caS");
    jPanel4.add (jLabel6);
    jScrollPane1.setBorder (BorderFactory.createBevelBorder (1));
    jLabel7.setText ("Mercado \u00e0 vista - a\u00e7\u00f5es");
    jLabel8.setText ("Mercado \u00e0 vista - ouro");
    jLabel9.setText ("Mercado \u00e0 vista - ouro at. fin. fora bolsa");
    jLabel10.setText ("Mercado op\u00e7\u00f5es - a\u00e7\u00f5es");
    jLabel11.setText ("Mercado op\u00e7\u00f5es - ouro");
    jLabel12.setText ("Mercado op\u00e7\u00f5es - fora de bolsa");
    jLabel13.setText ("Mercado op\u00e7\u00f5es - outros");
    jLabel14.setText ("Mercado futuro - d\u00f3lar dos EUA");
    jLabel15.setText ("Mercado futuro - \u00edndices");
    jLabel16.setText ("Mercado futuro - juros");
    jLabel17.setText ("Mercado futuro - outros");
    jLabel18.setText ("Mercado a termo - a\u00e7\u00f5es/ouro");
    jLabel19.setText ("Mercado a termo - outros");
    jLabel20.setText ("RESULTADO L\u00cdQUIDO DO M\u00caS");
    jLabel21.setText ("Resultado negativo at\u00e9 o m\u00eas anterior");
    jLabel22.setText ("BASE DE C\u00c1LCULO DO IMPOSTO");
    jLabel23.setText ("Preju\u00edzo a compensar");
    jLabel24.setText ("Al\u00edquota do imposto");
    jLabel25.setText ("IMPOSTO DEVIDO");
    jEditValor1.setAceitaNumerosNegativos (true);
    jEditValor2.setAceitaNumerosNegativos (true);
    jEditValor3.setAceitaNumerosNegativos (true);
    jEditValor4.setAceitaNumerosNegativos (true);
    jEditValor5.setAceitaNumerosNegativos (true);
    jEditValor6.setAceitaNumerosNegativos (true);
    jEditValor7.setAceitaNumerosNegativos (true);
    jEditValor8.setAceitaNumerosNegativos (true);
    jEditValor9.setAceitaNumerosNegativos (true);
    jEditValor10.setAceitaNumerosNegativos (true);
    jEditValor11.setAceitaNumerosNegativos (true);
    jEditValor12.setAceitaNumerosNegativos (true);
    jEditValor13.setAceitaNumerosNegativos (true);
    jEditValor14.setAceitaNumerosNegativos (true);
    jEditValor15.setAceitaNumerosNegativos (true);
    jEditValor16.setAceitaNumerosNegativos (true);
    jEditValor17.setAceitaNumerosNegativos (true);
    jEditValor18.setAceitaNumerosNegativos (true);
    jEditValor19.setAceitaNumerosNegativos (true);
    jEditValor20.setAceitaNumerosNegativos (true);
    jEditValor21.setAceitaNumerosNegativos (true);
    jEditValor22.setAceitaNumerosNegativos (true);
    jEditValor23.setAceitaNumerosNegativos (true);
    jEditValor24.setAceitaNumerosNegativos (true);
    jEditValor25.setAceitaNumerosNegativos (true);
    jEditValor26.setAceitaNumerosNegativos (true);
    jEditValor27.setAceitaFoco (false);
    jEditValor28.setAceitaFoco (false);
    jEditValor31.setAceitaFoco (false);
    jEditValor32.setAceitaFoco (false);
    jEditValor37.setAceitaFoco (false);
    jEditValor38.setAceitaFoco (false);
    jTextField1.setEditable (false);
    jTextField1.setHorizontalAlignment (0);
    jTextField1.setText ("20%");
    jTextField1.setBorder (BorderFactory.createBevelBorder (1));
    jTextField2.setEditable (false);
    jTextField2.setHorizontalAlignment (0);
    jTextField2.setText ("15%");
    jTextField2.setBorder (BorderFactory.createBevelBorder (1));
    GroupLayout jPanel3Layout = new GroupLayout (jPanel3);
    jPanel3.setLayout (jPanel3Layout);
    jPanel3Layout.setHorizontalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().addContainerGap ().add (jPanel3Layout.createParallelGroup (1).add (jLabel7).add (jLabel8).add (jLabel9).add (jLabel10).add (jLabel11).add (jLabel12).add (jLabel13).add (jLabel14).add (jLabel15).add (jLabel16).add (jLabel17).add (jLabel18).add (jLabel19).add (jLabel20).add (jLabel21).add (jLabel22).add (jLabel23).add (jLabel24).add (jLabel25)).addPreferredGap (0, 27, 32767).add (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createParallelGroup (2).add (jEditValor2, -2, 122, -2).add (jEditValor4, -2, 122, -2).add (jEditValor6, -2, 122, -2).add (jEditValor8, -2, 122, -2).add (jEditValor10, -2, 122, -2).add (jEditValor12, -2, 122, -2).add (jEditValor14, -2, 122, -2).add (jEditValor16, -2, 122, -2).add (jEditValor18, -2, 122, -2).add (jEditValor20, -2, 122, -2).add (jEditValor22, -2, 122, -2).add (jEditValor24, -2, 122, -2).add (jEditValor26, -2, 122, -2).add (jEditValor28, -2, 122, -2).add (jEditValor30, -2, 122, -2).add (jEditValor32, -2, 122, -2).add (jEditValor34, -2, 122, -2).add (jEditValor38, -2, 122, -2)).add (jTextField2, -2, 102, -2)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createParallelGroup (2).add (jEditValor1, -2, 122, -2).add (jEditValor3, -2, 122, -2).add (jEditValor5, -2, 122, -2).add (jEditValor7, -2, 122, -2).add (jEditValor9, -2, 122, -2).add (jEditValor11, -2, 122, -2).add (jEditValor13, -2, 122, -2).add (jEditValor15, -2, 122, -2).add (jEditValor17, -2, 122, -2).add (jEditValor19, -2, 122, -2).add (jEditValor21, -2, 122, -2).add (jEditValor23, -2, 122, -2).add (jEditValor25, -2, 122, -2).add (jEditValor27, -2, 122, -2).add (jEditValor29, -2, 122, -2).add (jEditValor31, -2, 122, -2).add (jEditValor33, -2, 122, -2).add (jEditValor37, -2, 122, -2)).add (jTextField1, -2, 102, -2)).addContainerGap ()));
    jPanel3Layout.setVerticalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().addContainerGap ().add (jPanel3Layout.createParallelGroup (1).add (2, jEditValor1, -2, -1, -2).add (2, jEditValor2, -2, -1, -2).add (2, jLabel7)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (2, jEditValor3, -2, -1, -2).add (2, jEditValor4, -2, -1, -2).add (2, jLabel8)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (2, jEditValor5, -2, -1, -2).add (2, jEditValor6, -2, -1, -2).add (2, jLabel9)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (2, jEditValor7, -2, -1, -2).add (2, jEditValor8, -2, -1, -2).add (2, jLabel10)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (2, jEditValor9, -2, -1, -2).add (2, jEditValor10, -2, -1, -2).add (2, jLabel11)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (2, jEditValor11, -2, -1, -2).add (2, jEditValor12, -2, -1, -2).add (2, jLabel12)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (2, jEditValor13, -2, -1, -2).add (2, jEditValor14, -2, -1, -2).add (2, jLabel13)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (2, jEditValor15, -2, -1, -2).add (2, jEditValor16, -2, -1, -2).add (2, jLabel14)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (2, jEditValor17, -2, -1, -2).add (2, jEditValor18, -2, -1, -2).add (2, jLabel15)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (2, jEditValor19, -2, -1, -2).add (2, jEditValor20, -2, -1, -2).add (2, jLabel16)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (2, jEditValor21, -2, -1, -2).add (2, jEditValor22, -2, -1, -2).add (2, jLabel17)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (2, jLabel18).add (2, jEditValor24, -2, -1, -2).add (2, jEditValor23, -2, -1, -2)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (2, jEditValor26, -2, -1, -2).add (2, jEditValor25, -2, -1, -2).add (2, jLabel19)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (2, jLabel20).add (2, jEditValor28, -2, -1, -2).add (2, jEditValor27, -2, -1, -2)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (2, jEditValor30, -2, -1, -2).add (2, jEditValor29, -2, -1, -2).add (2, jLabel21)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (2, jEditValor32, -2, -1, -2).add (2, jEditValor31, -2, -1, -2).add (2, jLabel22)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (2, jEditValor34, -2, -1, -2).add (2, jEditValor33, -2, -1, -2).add (2, jLabel23)).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (2).add (jLabel24).add (jPanel3Layout.createParallelGroup (3).add (jTextField2, -2, -1, -2).add (jTextField1, -2, -1, -2))).addPreferredGap (0).add (jPanel3Layout.createParallelGroup (1).add (2, jEditValor38, -2, -1, -2).add (2, jEditValor37, -2, -1, -2).add (2, jLabel25)).addContainerGap (13, 32767)));
    jScrollPane1.setViewportView (jPanel3);
    jLabel26.setText ("Total do imposto devido");
    jLabel27.setText ("IR fonte de Day-Trade no m\u00eas");
    jLabel28.setText ("IR fonte de Day-Trade nos meses anteriores");
    jLabel29.setText ("IR fonte de Day-Trade a compensar");
    jLabel30.setText ("Imp. renda na fonte (Lei n\u00ba 11.033/2004)");
    jEditValor39.setAceitaFoco (false);
    jEditValor39.setEstiloFonte (0);
    jEditValor40.setEstiloFonte (0);
    jEditValor41.setAceitaFoco (false);
    jEditValor41.setEstiloFonte (0);
    jEditValor42.setAceitaFoco (false);
    jEditValor42.setEstiloFonte (0);
    jEditValor43.setEstiloFonte (0);
    jLabel31.setText ("Imposto a pagar");
    jLabel32.setText ("Imposto pago");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (jPanel1, -1, 529, 32767).add (jPanel2, -1, 529, 32767).add (2, layout.createSequentialGroup ().addContainerGap ().add (jLabel2).addPreferredGap (0, 136, 32767).add (jLabel4).add (91, 91, 91).add (jLabel5).add (95, 95, 95)).add (2, layout.createSequentialGroup ().addContainerGap (321, 32767).add (jLabel3).add (156, 156, 156)).add (layout.createSequentialGroup ().addContainerGap ().add (jScrollPane1, -1, 499, 32767).add (20, 20, 20)).add (layout.createSequentialGroup ().add (layout.createParallelGroup (1).add (jPanel4, -1, 519, 32767).add (2, layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (2).add (1, layout.createSequentialGroup ().add (jLabel29).addPreferredGap (0, 174, 32767).add (jEditValor42, -2, 122, -2)).add (1, layout.createSequentialGroup ().add (jLabel28).addPreferredGap (0, 133, 32767).add (jEditValor41, -2, 122, -2)).add (1, layout.createSequentialGroup ().add (jLabel26).addPreferredGap (0, 233, 32767).add (jEditValor39, -2, 122, -2)).add (1, layout.createSequentialGroup ().add (jLabel27).addPreferredGap (0, 201, 32767).add (jEditValor40, -2, 122, -2)).add (layout.createSequentialGroup ().add (layout.createParallelGroup (1).add (jLabel30).add (jLabel31).add (jLabel32)).addPreferredGap (0, 147, 32767).add (layout.createParallelGroup (1, false).add (jEditValor36, -1, -1, 32767).add (jEditValor35, -1, -1, 32767).add (jEditValor43, -2, 122, -2)))).add (40, 40, 40))).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jPanel1, -2, -1, -2).addPreferredGap (0).add (jPanel2, -2, -1, -2).addPreferredGap (0).add (jLabel3).addPreferredGap (0).add (layout.createParallelGroup (1).add (2, jLabel2).add (2, layout.createParallelGroup (3).add (jLabel4).add (jLabel5))).addPreferredGap (0).add (jScrollPane1, -2, 141, -2).addPreferredGap (0).add (jPanel4, -2, -1, -2).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel26).add (jEditValor39, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel27).add (jEditValor40, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel28).add (jEditValor41, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel29).add (jEditValor42, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel30).add (jEditValor43, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (2, jEditValor35, -2, -1, -2).add (2, jLabel31)).addPreferredGap (0).add (layout.createParallelGroup (1).add (2, jEditValor36, -2, -1, -2).add (2, jLabel32)).addContainerGap (-1, 32767)));
  }
  
  private void mouseClicked (MouseEvent evt)
  {
    mudaMes ((JToggleButton) evt.getSource ());
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
