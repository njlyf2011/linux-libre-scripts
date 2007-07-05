/* PainelImprimirDarfQuota - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import com.jgoodies.forms.layout.FormLayout;

import serpro.ppgd.formatosexternos.barcode.util.DarfAdaptor;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.BotoesDialog;
import serpro.ppgd.infraestrutura.util.PPGDFormPanel;
import serpro.ppgd.irpf.impressao.ImpressaoDarf;

public class PainelImprimirDarfQuota extends PPGDFormPanel
{
  private static final String helpID = "Menu_Declaracao:_Imprimir";
  private JLabel label = new JLabel ("<html><b><font color='blue'>Selecione as quotas a serem impressas:</html>");
  private BotoesDialog botoes;
  private String pathDados;
  private String idITR;
  private String nomeContrib;
  private int qtdQuota;
  private JCheckBox todos = new JCheckBox ("Todas as quotas");
  private JCheckBox primeira = new JCheckBox ("Primeira quota");
  private JCheckBox segunda = new JCheckBox ("Segunda quota");
  private JCheckBox terceira = new JCheckBox ("Terceira quota");
  private JCheckBox quarta = new JCheckBox ("Quarta quota");
  private JCheckBox quinta = new JCheckBox ("Quinta quota");
  private JCheckBox sexta = new JCheckBox ("Sexta quota");
  private JCheckBox setima = new JCheckBox ("S\u00e9tima quota");
  private JCheckBox oitava = new JCheckBox ("Oitava quota");
  private boolean visualizaImpressao = false;
  private boolean debitoAutomatico;
  private DarfAdaptor dados;
  
  public PainelImprimirDarfQuota (String pathDados, String idITR, int QTDQuota, boolean pVisualizaImpressao, String nomeContrib, boolean aDebitoAutomatico, DarfAdaptor dados)
  {
    this.pathDados = pathDados;
    this.idITR = idITR;
    this.nomeContrib = nomeContrib;
    qtdQuota = QTDQuota;
    debitoAutomatico = aDebitoAutomatico;
    this.dados = dados;
    if (dados.getValorPrincipal ().equals ("10,00"))
      todos.setEnabled (false);
    primeira.setEnabled (false);
    segunda.setEnabled (false);
    terceira.setEnabled (false);
    quarta.setEnabled (false);
    quinta.setEnabled (false);
    sexta.setEnabled (false);
    setima.setEnabled (false);
    oitava.setEnabled (false);
    FormLayout layout = new FormLayout ("5dlu,p:grow,5dlu", "5dlu,t:20dlu,p,15dlu,15dlu,15dlu,15dlu,p,15dlu,p,15dlu,p,15dlu,p,15dlu,p,15dlu,p,15dlu");
    setLayout (layout);
    iniciaComponentes ();
    adicionaComponentes ();
    visualizaImpressao = pVisualizaImpressao;
  }
  
  private void iniciaComponentes ()
  {
    todos.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent arg0)
      {
	selecionaTodas ();
      }
    });
    botoes = new BotoesDialog (new String[] { "OK", "Cancelar", "Ajuda" });
    botoes.setMouseEvents (this, new String[] { "imprimirDarf", null, "ajuda" });
    habilitaQuotas ();
    PlataformaPPGD.getPlataforma ().habilitaHelp (builder.getPanel (), "Menu_Declaracao:_Imprimir");
    PlataformaPPGD.getPlataforma ().setHelpID (getBotoes ().getBotao (2), "Menu_Declaracao:_Imprimir");
  }
  
  private void habilitaQuotas ()
  {
    switch (qtdQuota)
      {
      case 8:
	oitava.setEnabled (! debitoAutomatico);
	/* fall through */
      case 7:
	setima.setEnabled (! debitoAutomatico);
	/* fall through */
      case 6:
	sexta.setEnabled (! debitoAutomatico);
	/* fall through */
      case 5:
	quinta.setEnabled (! debitoAutomatico);
	/* fall through */
      case 4:
	quarta.setEnabled (! debitoAutomatico);
	/* fall through */
      case 3:
	terceira.setEnabled (! debitoAutomatico);
	/* fall through */
      case 2:
	segunda.setEnabled (! debitoAutomatico);
	/* fall through */
      case 1:
	primeira.setEnabled (true);
	/* fall through */
      default:
	if (qtdQuota == 1)
	  primeira.setText ("Quota \u00fanica");
      }
  }
  
  private void adicionaComponentes ()
  {
    builder.setRow (2);
    builder.setColumn (2);
    builder.append (label);
    builder.setRow (3);
    builder.setColumn (2);
    builder.append (todos);
    builder.setRow (4);
    builder.setColumn (2);
    builder.append (primeira);
    builder.setRow (5);
    builder.setColumn (2);
    builder.append (segunda);
    builder.setRow (6);
    builder.setColumn (2);
    builder.append (terceira);
    builder.setRow (7);
    builder.setColumn (2);
    builder.append (quarta);
    builder.setRow (8);
    builder.setColumn (2);
    builder.append (quinta);
    builder.setRow (9);
    builder.setColumn (2);
    builder.append (sexta);
    builder.setRow (10);
    builder.setColumn (2);
    builder.append (setima);
    builder.setRow (11);
    builder.setColumn (2);
    builder.append (oitava);
    builder.setRow (18);
    builder.setColumn (2);
    builder.appendCenter (botoes);
  }
  
  public void imprimirDarf ()
  {
    int contSelecionado = 0;
    boolean[] quotas = new boolean[qtdQuota];
    switch (qtdQuota)
      {
      case 8:
	quotas[7] = oitava.isSelected ();
	if (oitava.isSelected ())
	  contSelecionado++;
	/* fall through */
      case 7:
	quotas[6] = setima.isSelected ();
	if (setima.isSelected ())
	  contSelecionado++;
	/* fall through */
      case 6:
	quotas[5] = sexta.isSelected ();
	if (sexta.isSelected ())
	  contSelecionado++;
	/* fall through */
      case 5:
	quotas[4] = quinta.isSelected ();
	if (quinta.isSelected ())
	  contSelecionado++;
	/* fall through */
      case 4:
	quotas[3] = quarta.isSelected ();
	if (quarta.isSelected ())
	  contSelecionado++;
	/* fall through */
      case 3:
	quotas[2] = terceira.isSelected ();
	if (terceira.isSelected ())
	  contSelecionado++;
	/* fall through */
      case 2:
	quotas[1] = segunda.isSelected ();
	if (segunda.isSelected ())
	  contSelecionado++;
	/* fall through */
      case 1:
	quotas[0] = primeira.isSelected ();
	if (primeira.isSelected ())
	  contSelecionado++;
	/* fall through */
      default:
	if (contSelecionado != 0)
	  {
	    botoes.fecharDialogo ();
	    new ImpressaoDarf ("Darf", "relDarf.jasper", pathDados, "/classe", idITR, quotas, visualizaImpressao, nomeContrib, dados);
	  }
      }
  }
  
  public void selecionaTodas ()
  {
    if (todos.isSelected ())
      {
	if (primeira.isEnabled ())
	  primeira.setSelected (true);
	if (segunda.isEnabled ())
	  segunda.setSelected (true);
	if (terceira.isEnabled ())
	  terceira.setSelected (true);
	if (quarta.isEnabled ())
	  quarta.setSelected (true);
	if (quinta.isEnabled ())
	  quinta.setSelected (true);
	if (sexta.isEnabled ())
	  sexta.setSelected (true);
	if (setima.isEnabled ())
	  setima.setSelected (true);
	if (oitava.isEnabled ())
	  oitava.setSelected (true);
      }
    else
      {
	if (primeira.isEnabled ())
	  primeira.setSelected (false);
	if (segunda.isEnabled ())
	  segunda.setSelected (false);
	if (terceira.isEnabled ())
	  terceira.setSelected (false);
	if (quarta.isEnabled ())
	  quarta.setSelected (false);
	if (quinta.isEnabled ())
	  quinta.setSelected (false);
	if (sexta.isEnabled ())
	  sexta.setSelected (false);
	if (setima.isEnabled ())
	  setima.setSelected (false);
	if (oitava.isEnabled ())
	  oitava.setSelected (false);
      }
  }
  
  public void ajuda ()
  {
    /* empty */
  }
  
  public BotoesDialog getBotoes ()
  {
    return botoes;
  }
  
  public void setBotoes (BotoesDialog botoes)
  {
    this.botoes = botoes;
  }
}
