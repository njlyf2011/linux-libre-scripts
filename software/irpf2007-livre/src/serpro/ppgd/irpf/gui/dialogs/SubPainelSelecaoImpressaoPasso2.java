/* SubPainelSelecaoImpressaoPasso2 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.gui.ConstantesGlobaisGUI;

public class SubPainelSelecaoImpressaoPasso2 extends JPanel
{
  public JCheckBox chkARBrasil;
  public JCheckBox chkARExterior;
  public JCheckBox chkBens;
  public JCheckBox chkConjuge;
  public JCheckBox chkContribuinte;
  public JCheckBox chkDependentes;
  public JCheckBox chkDividas;
  public JCheckBox chkDoacoesCampanha;
  public JCheckBox chkEspolio;
  public JCheckBox chkFundoInvest;
  public JCheckBox chkImpostoPago;
  public JCheckBox chkPagamentos;
  public JCheckBox chkRendIsentos;
  public JCheckBox chkRendPFDep;
  public JCheckBox chkRendPFTitular;
  public JCheckBox chkRendPJDep;
  public JCheckBox chkRendPJTitular;
  public JCheckBox chkRendSujTributExcl;
  public JCheckBox chkResumoDeclaracao;
  public JCheckBox chkResumoRendaVariavel;
  public JCheckBox chkTodos;
  private JPanel pnlCheckBoxes;
  
  public SubPainelSelecaoImpressaoPasso2 ()
  {
    initComponents ();
    chkTodos.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	boolean isSelected = chkTodos.isSelected ();
	int totalComp = pnlCheckBoxes.getComponentCount ();
	for (int i = 0; i < totalComp; i++)
	  {
	    java.awt.Component comp = pnlCheckBoxes.getComponent (i);
	    if (comp instanceof JCheckBox && ! comp.equals (chkTodos))
	      ((JCheckBox) comp).setSelected (isSelected);
	  }
      }
    });
  }
  
  private void initComponents ()
  {
    pnlCheckBoxes = new JPanel ();
    chkTodos = new JCheckBox ();
    chkContribuinte = new JCheckBox ();
    chkConjuge = new JCheckBox ();
    chkRendPJTitular = new JCheckBox ();
    chkRendPJDep = new JCheckBox ();
    chkRendPFTitular = new JCheckBox ();
    chkRendPFDep = new JCheckBox ();
    chkRendIsentos = new JCheckBox ();
    chkRendSujTributExcl = new JCheckBox ();
    chkImpostoPago = new JCheckBox ();
    chkDependentes = new JCheckBox ();
    chkPagamentos = new JCheckBox ();
    chkBens = new JCheckBox ();
    chkDividas = new JCheckBox ();
    chkEspolio = new JCheckBox ();
    chkResumoRendaVariavel = new JCheckBox ();
    chkARBrasil = new JCheckBox ();
    chkARExterior = new JCheckBox ();
    chkResumoDeclaracao = new JCheckBox ();
    chkDoacoesCampanha = new JCheckBox ();
    chkFundoInvest = new JCheckBox ();
    chkTodos.setForeground (ConstantesGlobaisGUI.COR_AZUL_ESCURO);
    chkTodos.setText ("<HTML><B>Marcar/desmarcar todos os quadros</B></HTML>");
    chkTodos.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkTodos.setMargin (new Insets (0, 0, 0, 0));
    chkContribuinte.setText ("<HTML><B>Identifica\u00e7\u00e3o do Contribuinte</B></HTML>");
    chkContribuinte.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkContribuinte.setMargin (new Insets (0, 0, 0, 0));
    chkConjuge.setText ("<HTML><B>Informa\u00e7\u00f5es do c\u00f4njuge</B></HTML>");
    chkConjuge.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkConjuge.setMargin (new Insets (0, 0, 0, 0));
    chkRendPJTitular.setText ("<HTML><B>Rendimentos tribut\u00e1veis recebidos de PJ pelo titular</B></HTML>");
    chkRendPJTitular.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkRendPJTitular.setMargin (new Insets (0, 0, 0, 0));
    chkRendPJDep.setText ("<HTML><B>Rendimentos tribut\u00e1veis recebidos de PJ pelos dependentes</B></HTML>");
    chkRendPJDep.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkRendPJDep.setMargin (new Insets (0, 0, 0, 0));
    chkRendPFTitular.setText ("<HTML><B>Rendimentos tribut\u00e1veis recebidos de PF e do exterior pelo titular</B></HTML>");
    chkRendPFTitular.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkRendPFTitular.setMargin (new Insets (0, 0, 0, 0));
    chkRendPFDep.setText ("<HTML><B>Rendimentos tribut\u00e1veis recebidos de PF e do exterior pelos dependentes</B></HTML>");
    chkRendPFDep.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkRendPFDep.setMargin (new Insets (0, 0, 0, 0));
    chkRendIsentos.setText ("<HTML><B>Rendimentos isentos e n\u00e3o-tribut\u00e1veis</B></HTML>");
    chkRendIsentos.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkRendIsentos.setMargin (new Insets (0, 0, 0, 0));
    chkRendSujTributExcl.setText ("<HTML><B>Rendimentos sujeitos a tributa\u00e7\u00e3o exclusiva</B></HTML>");
    chkRendSujTributExcl.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkRendSujTributExcl.setMargin (new Insets (0, 0, 0, 0));
    chkImpostoPago.setText ("<HTML><B>Imposto pago</B></HTML>");
    chkImpostoPago.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkImpostoPago.setMargin (new Insets (0, 0, 0, 0));
    chkDependentes.setText ("<HTML><B>Dependentes</B></HTML>");
    chkDependentes.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkDependentes.setMargin (new Insets (0, 0, 0, 0));
    chkPagamentos.setText ("<HTML><B>Pagamentos e doa\u00e7\u00f5es efetuados</B></HTML>");
    chkPagamentos.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkPagamentos.setMargin (new Insets (0, 0, 0, 0));
    chkBens.setText ("<HTML><B>Bens e direitos</B></HTML>");
    chkBens.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkBens.setMargin (new Insets (0, 0, 0, 0));
    chkDividas.setText ("<HTML><B>D\u00edvidas e \u00f4nus reais</B></HTML>");
    chkDividas.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkDividas.setMargin (new Insets (0, 0, 0, 0));
    chkEspolio.setText ("<HTML><B>Esp\u00f3lio</B></HTML>");
    chkEspolio.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkEspolio.setMargin (new Insets (0, 0, 0, 0));
    chkResumoRendaVariavel.setText ("<HTML><B>Resumo de apura\u00e7\u00e3o de ganhos - Renda vari\u00e1vel</B></HTML>");
    chkResumoRendaVariavel.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkResumoRendaVariavel.setMargin (new Insets (0, 0, 0, 0));
    chkARBrasil.setText ("<HTML><B>Atividade Rural (Brasil)</B></HTML>");
    chkARBrasil.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkARBrasil.setMargin (new Insets (0, 0, 0, 0));
    chkARExterior.setText ("<HTML><B>Atividade Rural (Exterior)</B></HTML>");
    chkARExterior.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkARExterior.setMargin (new Insets (0, 0, 0, 0));
    chkResumoDeclaracao.setText ("<HTML><B>Resumo da declara\u00e7\u00e3o</B></HTML>");
    chkResumoDeclaracao.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkResumoDeclaracao.setMargin (new Insets (0, 0, 0, 0));
    chkDoacoesCampanha.setText ("<html><b>Doa\u00e7\u00f5es a Part. Pol\u00edticos, Comit\u00eas Financ. e Candidatos<b></html>");
    chkDoacoesCampanha.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkDoacoesCampanha.setMargin (new Insets (0, 0, 0, 0));
    chkFundoInvest.setFont (new Font ("Tahoma", 1, 11));
    chkFundoInvest.setText ("Fundos Investimento Imobili\u00e1rio - Renda Vari\u00e1vel");
    chkFundoInvest.setBorder (BorderFactory.createEmptyBorder (0, 0, 0, 0));
    chkFundoInvest.setMargin (new Insets (0, 0, 0, 0));
    GroupLayout pnlCheckBoxesLayout = new GroupLayout (pnlCheckBoxes);
    pnlCheckBoxes.setLayout (pnlCheckBoxesLayout);
    pnlCheckBoxesLayout.setHorizontalGroup (pnlCheckBoxesLayout.createParallelGroup (1).add (pnlCheckBoxesLayout.createSequentialGroup ().addContainerGap ().add (pnlCheckBoxesLayout.createParallelGroup (1).add (pnlCheckBoxesLayout.createSequentialGroup ().add (chkTodos, -1, 225, 32767).add (210, 210, 210)).add (pnlCheckBoxesLayout.createSequentialGroup ().add (chkContribuinte, -1, -1, 32767).add (254, 254, 254)).add (pnlCheckBoxesLayout.createSequentialGroup ().add (chkConjuge, -1, 155, 32767).add (280, 280, 280)).add (pnlCheckBoxesLayout.createSequentialGroup ().add (chkRendPJTitular, -1, -1, 32767).add (122, 122, 122)).add (pnlCheckBoxesLayout.createSequentialGroup ().add (chkRendPJDep, -1, 359, 32767).add (76, 76, 76)).add (pnlCheckBoxesLayout.createSequentialGroup ().add (chkRendPFTitular, -1, -1, 32767).add (46, 46, 46)).add (chkRendPFDep, -1, 435, 32767).add (pnlCheckBoxesLayout.createSequentialGroup ().add (chkRendIsentos, -1, -1, 32767).add (198, 198, 198)).add (pnlCheckBoxesLayout.createSequentialGroup ().add (chkRendSujTributExcl, -1, 269, 32767).add (166, 166, 166)).add (pnlCheckBoxesLayout.createSequentialGroup ().add (chkImpostoPago, -1, 97, 32767).add (338, 338, 338)).add (pnlCheckBoxesLayout.createSequentialGroup ().add (chkDependentes, -1, 93, 32767).add (342, 342, 342)).add (pnlCheckBoxesLayout.createSequentialGroup ().add (chkPagamentos, -1, 209, 32767).add (226, 226, 226)).add (pnlCheckBoxesLayout.createSequentialGroup ().add (chkBens, -1, 101, 32767).add (334, 334, 334)).add (pnlCheckBoxesLayout.createSequentialGroup ().add (chkDividas, -1, -1, 32767).add (306, 306, 306)).add (pnlCheckBoxesLayout.createSequentialGroup ().add (chkEspolio, -1, 57, 32767).add (378, 378, 378)).add (pnlCheckBoxesLayout.createSequentialGroup ().add (chkResumoRendaVariavel, -1, -1, 32767).add (142, 142, 142)).add (chkARBrasil).add (chkARExterior).add (chkResumoDeclaracao).add (chkDoacoesCampanha, -2, 341, -2).add (chkFundoInvest)).addContainerGap ()));
    pnlCheckBoxesLayout.setVerticalGroup (pnlCheckBoxesLayout.createParallelGroup (1).add (pnlCheckBoxesLayout.createSequentialGroup ().addContainerGap ().add (chkTodos).addPreferredGap (0).add (chkContribuinte).addPreferredGap (0).add (chkConjuge).addPreferredGap (0).add (chkRendPJTitular).addPreferredGap (0).add (chkRendPJDep).addPreferredGap (0).add (chkRendPFTitular).addPreferredGap (0).add (chkRendPFDep).addPreferredGap (0).add (chkRendIsentos).addPreferredGap (0).add (chkRendSujTributExcl).addPreferredGap (0).add (chkImpostoPago).addPreferredGap (0).add (chkDependentes).addPreferredGap (0).add (chkPagamentos).addPreferredGap (0).add (chkBens).addPreferredGap (0).add (chkDividas).addPreferredGap (0).add (chkEspolio).addPreferredGap (0).add (chkDoacoesCampanha).addPreferredGap (0).add (chkResumoRendaVariavel).addPreferredGap (0).add (chkFundoInvest).addPreferredGap (0).add (chkARBrasil).addPreferredGap (0).add (chkARExterior).addPreferredGap (0).add (chkResumoDeclaracao).addContainerGap (32, 32767)));
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (pnlCheckBoxes, -2, -1, -2).addContainerGap (-1, 32767)));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (pnlCheckBoxes, -2, -1, -2).addContainerGap (-1, 32767)));
  }
}
