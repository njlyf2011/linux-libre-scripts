/* PainelReceitasDespesasExterior - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.atividaderural;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditColecao;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.gui.xbeans.JNavegadorColecao;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoListener;
import serpro.ppgd.infraestrutura.util.NavegadorRemocaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorRemocaoListener;
import serpro.ppgd.irpf.atividaderural.exterior.ReceitaDespesa;
import serpro.ppgd.irpf.gui.ImportarAtividadeRural;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PainelReceitasDespesasExterior extends JPanel implements PainelIRPFIf
{
  private final String SUB_TIT = "Dados de receita e despesa no pa\u00eds n\u00ba ";
  private JEditValor edtDespesasInvestimento;
  private JEditValor edtMoedaOriginal;
  private JEditColecao edtPais;
  private JEditValor edtReceitaAnual;
  private JEditValor edtTotal;
  private JEditValor edtUSS;
  private JButton jButton1;
  private JLabel jLabel1;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JPanel jPanel1;
  private JPanel jPanel2;
  private JPanel jPanel3;
  private JPanel jPanel4;
  private JTextField jTextField1;
  private JLabel labelContador;
  private JNavegadorColecao navegadorColecao;
  
  public PainelReceitasDespesasExterior ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "ARReceitas");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (1);
    f = f.deriveFont (f.getSize2D () + 2.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
    reinitEdits ();
    navegadorColecao.primeiro ();
    edtMoedaOriginal.setAceitaNumerosNegativos (true);
    edtUSS.setAceitaNumerosNegativos (true);
    jButton1.addActionListener (new ImportarAtividadeRural ()
    {
      public boolean importaBrasil ()
      {
	return false;
      }
    });
  }
  
  public JNavegadorColecao getNavegador ()
  {
    return navegadorColecao;
  }
  
  private void reinitEdits ()
  {
    edtDespesasInvestimento.getInformacao ().setHabilitado (false);
    edtMoedaOriginal.getInformacao ().setHabilitado (false);
    edtPais.getInformacao ().setHabilitado (false);
    edtReceitaAnual.getInformacao ().setHabilitado (false);
    edtUSS.getInformacao ().setHabilitado (false);
    labelContador.setText ("Dados de receita e despesa no pa\u00eds n\u00ba ");
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    navegadorColecao = new JNavegadorColecao ();
    jPanel1 = new JPanel ();
    labelContador = new JLabel ();
    jLabel3 = new JLabel ();
    edtPais = new JEditColecao ();
    jPanel2 = new JPanel ();
    jLabel8 = new JLabel ();
    jPanel3 = new JPanel ();
    jButton1 = new JButton ();
    jPanel4 = new JPanel ();
    jLabel4 = new JLabel ();
    edtReceitaAnual = new JEditValor ();
    jLabel5 = new JLabel ();
    edtDespesasInvestimento = new JEditValor ();
    jLabel6 = new JLabel ();
    edtMoedaOriginal = new JEditValor ();
    jLabel7 = new JLabel ();
    edtUSS = new JEditValor ();
    edtTotal = new JEditValor ();
    jTextField1 = new JTextField ();
    jLabel1.setText ("Consulta/Atualiza\u00e7\u00e3o");
    navegadorColecao.setInformacaoAssociada ("atividadeRural.exterior.receitasDespesas");
    navegadorColecao.addNavegadorColecaoListener (new NavegadorColecaoListener ()
    {
      public void exibeColecaoVazia (NavegadorColecaoEvent evt)
      {
	PainelReceitasDespesasExterior.this.navegadorColecaoExibeColecaoVazia (evt);
      }
      
      public void exibeOutro (NavegadorColecaoEvent evt)
      {
	PainelReceitasDespesasExterior.this.navegadorColecaoExibeOutro (evt);
      }
    });
    navegadorColecao.addNavegadorRemocaoListener (new NavegadorRemocaoListener ()
    {
      public boolean confirmaExclusao (NavegadorRemocaoEvent evt)
      {
	return PainelReceitasDespesasExterior.this.navegadorColecaoConfirmaExclusao (evt);
      }
      
      public void objetoExcluido (NavegadorRemocaoEvent evt)
      {
	/* empty */
      }
    });
    labelContador.setText ("Dados de receita e despesa no pa\u00eds n\u00ba ");
    jPanel1.add (labelContador);
    jLabel3.setText ("Pa\u00eds");
    edtPais.setCaracteresValidosTxtCodigo ("0123456789 ");
    edtPais.setMascaraTxtCodigo ("***'");
    jLabel8.setText ("Total");
    jPanel2.add (jLabel8);
    jButton1.setText ("<HTML><B>Importar</B.</HTML>");
    jPanel3.add (jButton1);
    jLabel4.setText ("Receita bruta anual - moeda original");
    jLabel5.setText ("<html><p>Despesas de custeio/investimento - moeda original</p></html>");
    jLabel6.setText ("Resultado I - moeda original");
    jLabel7.setText ("Resultado I - US$");
    GroupLayout jPanel4Layout = new GroupLayout (jPanel4);
    jPanel4.setLayout (jPanel4Layout);
    jPanel4Layout.setHorizontalGroup (jPanel4Layout.createParallelGroup (1).add (jPanel4Layout.createSequentialGroup ().addContainerGap ().add (jPanel4Layout.createParallelGroup (1).add (jPanel4Layout.createSequentialGroup ().add (jLabel7, -1, 299, 32767).add (38, 38, 38).add (edtUSS, -2, 118, -2)).add (2, jPanel4Layout.createSequentialGroup ().add (jPanel4Layout.createParallelGroup (1).add (jPanel4Layout.createParallelGroup (1).add (jLabel6, -1, 299, 32767).add (jLabel5, -2, 280, -2)).add (jLabel4, -2, 280, -2)).add (38, 38, 38).add (jPanel4Layout.createParallelGroup (1, false).add (edtReceitaAnual, -1, -1, 32767).add (edtDespesasInvestimento, -1, -1, 32767).add (edtMoedaOriginal, -1, 118, 32767)))).addContainerGap ()));
    jPanel4Layout.setVerticalGroup (jPanel4Layout.createParallelGroup (1).add (jPanel4Layout.createSequentialGroup ().addContainerGap ().add (jPanel4Layout.createParallelGroup (2).add (jLabel4).add (edtReceitaAnual, -2, -1, -2)).addPreferredGap (0).add (jPanel4Layout.createParallelGroup (1, false).add (jPanel4Layout.createSequentialGroup ().add (jLabel5).addPreferredGap (0).add (jLabel6)).add (2, jPanel4Layout.createSequentialGroup ().add (edtDespesasInvestimento, -2, -1, -2).addPreferredGap (0, -1, 32767).add (edtMoedaOriginal, -2, -1, -2))).addPreferredGap (0).add (jPanel4Layout.createParallelGroup (2).add (jLabel7).add (edtUSS, -2, -1, -2)).add (17, 17, 17)));
    edtTotal.setAceitaFoco (false);
    edtTotal.setInformacaoAssociada ("atividadeRural.exterior.receitasDespesas.totais");
    jTextField1.setBorder (null);
    jTextField1.addFocusListener (new FocusAdapter ()
    {
      public void focusGained (FocusEvent evt)
      {
	PainelReceitasDespesasExterior.this.jTextField1FocusGained (evt);
      }
    });
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (navegadorColecao, -2, -1, -2).addPreferredGap (0).add (jPanel3, -1, 423, 32767)).add (jLabel1).add (layout.createSequentialGroup ().add (jLabel3).addPreferredGap (0).add (edtPais, -1, 576, 32767)).add (2, jPanel1, -1, 601, 32767))).add (2, layout.createSequentialGroup ().add (jPanel4, -1, -1, 32767).addPreferredGap (0).add (layout.createParallelGroup (2).add (jPanel2, -2, 128, -2).add (edtTotal, -2, 128, -2))).add (layout.createSequentialGroup ().addContainerGap ().add (jTextField1, -2, -1, -2))).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel1).addPreferredGap (0).add (layout.createParallelGroup (2, false).add (jPanel3, -1, -1, 32767).add (navegadorColecao, -1, -1, 32767)).addPreferredGap (0).add (jPanel1, -2, -1, -2).addPreferredGap (0).add (layout.createParallelGroup (1).add (edtPais, -2, -1, -2).add (jLabel3)).addPreferredGap (0).add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jPanel2, -2, -1, -2).add (66, 66, 66).add (edtTotal, -2, -1, -2)).add (jPanel4, -2, 128, -2)).addPreferredGap (0, 117, 32767).add (jTextField1, -2, -1, -2).addContainerGap ()));
  }
  
  private void jTextField1FocusGained (FocusEvent evt)
  {
    if (navegadorColecao.getIndiceAtual () + 1 >= navegadorColecao.getColecao ().recuperarLista ().size ())
      {
	if (navegadorColecao.getIndiceAtual () != -1)
	  navegadorColecao.adiciona ();
      }
    else
      navegadorColecao.proximo ();
  }
  
  private boolean navegadorColecaoConfirmaExclusao (NavegadorRemocaoEvent evt)
  {
    TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
    if (JOptionPane.showConfirmDialog (getParent (), tab.msg ("receita_despesa_ar_exclusao"), "Confirma\u00e7\u00e3o", 0) == 0)
      return true;
    return false;
  }
  
  private void navegadorColecaoExibeOutro (NavegadorColecaoEvent evt)
  {
    ReceitaDespesa receita = (ReceitaDespesa) evt.getObjetoNegocio ();
    edtDespesasInvestimento.setInformacao (receita.getDespesaCusteio ());
    edtMoedaOriginal.setInformacao (receita.getResultadoIMoedaOriginal ());
    edtPais.setInformacao (receita.getPais ());
    edtReceitaAnual.setInformacao (receita.getReceitaBruta ());
    edtUSS.setInformacao (receita.getResultadoI_EmDolar ());
    labelContador.setText ("Dados de receita e despesa no pa\u00eds n\u00ba  " + (navegadorColecao.getIndiceAtual () + 1));
  }
  
  private void navegadorColecaoExibeColecaoVazia (NavegadorColecaoEvent evt)
  {
    reinitEdits ();
  }
  
  public String getTituloPainel ()
  {
    return "Receitas e Despesas - Exterior";
  }
  
  public void vaiExibir ()
  {
    if (IRPFUtil.getEstadoSistema () != 1)
      {
	if (navegadorColecao.getColecao ().recuperarLista ().size () == 0)
	  navegadorColecao.adiciona ();
	if (navegadorColecao.getColecao ().recuperarLista ().size () > 1)
	  navegadorColecao.getColecao ().excluirRegistrosEmBranco ();
	navegadorColecao.primeiro ();
      }
  }
  
  public void painelExibido ()
  {
    /* empty */
  }
}
