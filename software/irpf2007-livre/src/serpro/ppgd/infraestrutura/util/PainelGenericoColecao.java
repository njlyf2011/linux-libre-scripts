/* PainelGenericoColecao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import java.awt.FlowLayout;
import java.awt.KeyboardFocusManager;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.FormLayout;

import serpro.ppgd.gui.ConfirmacaoDialog;
import serpro.ppgd.gui.PPGDFormBuilder;
import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.util.FabricaUtilitarios;

public abstract class PainelGenericoColecao extends PainelGenerico implements FichaColecaoIf
{
  private final String LABEL_TITULO_EXCLUIR = "Confirma\u00e7\u00e3o de exclus\u00e3o";
  private final String LABEL_TEXTO_EXCLUIR = "<html>Voc\u00ea tem certeza de que deseja excluir o<br>registro atual?</html>";
  private Colecao colecao;
  private ObjetoNegocio objetoNegocio;
  protected BarraNavegacaoPPGD barraNavegacaoPPGD;
  private boolean isContentPanelScrollable = false;
  private FormLayout layoutPrincipal;
  
  public PainelGenericoColecao ()
  {
    layoutPrincipal = new FormLayout ("2dlu,F:P:grow,2dlu", "F:P,F:10dlu:grow");
    setLayout (layoutPrincipal);
    colecao = getColecao ();
    configuraPanel ();
  }
  
  public PainelGenericoColecao (boolean configuraLayoutPadrao, boolean configuraPainelAutomatico)
  {
    layoutPrincipal = new FormLayout ("2dlu,F:P:grow,2dlu", "F:P,F:10dlu:grow");
    colecao = getColecao ();
    if (configuraLayoutPadrao)
      setLayout (layoutPrincipal);
    if (configuraPainelAutomatico)
      configuraPanel ();
  }
  
  public PainelGenericoColecao (BarraNavegacaoPPGD barraCustomizada, boolean configuraLayoutPadrao, boolean configuraPainelAutomatico)
  {
    layoutPrincipal = new FormLayout ("2dlu,F:P:grow,2dlu", "F:P,F:10dlu:grow");
    colecao = getColecao ();
    barraNavegacaoPPGD = barraCustomizada;
    if (configuraLayoutPadrao)
      setLayout (layoutPrincipal);
    if (configuraPainelAutomatico)
      configuraPanel ();
  }
  
  public PainelGenericoColecao (boolean isContentPanelScrollable, boolean configuraLayoutPadrao, boolean configuraPainelAutomatico)
  {
    layoutPrincipal = new FormLayout ("2dlu,F:P:grow,2dlu", "F:P,F:10dlu:grow");
    this.isContentPanelScrollable = isContentPanelScrollable;
    colecao = getColecao ();
    if (configuraLayoutPadrao)
      setLayout (layoutPrincipal);
    if (configuraPainelAutomatico)
      configuraPanel ();
  }
  
  public PainelGenericoColecao (boolean isContentPanelScrollable, BarraNavegacaoPPGD barraCustomizada, boolean configuraLayoutPadrao, boolean configuraPainelAutomatico)
  {
    layoutPrincipal = new FormLayout ("2dlu,F:P:grow,2dlu", "F:P,F:10dlu:grow");
    this.isContentPanelScrollable = isContentPanelScrollable;
    barraNavegacaoPPGD = barraCustomizada;
    colecao = getColecao ();
    if (configuraLayoutPadrao)
      setLayout (layoutPrincipal);
    if (configuraPainelAutomatico)
      configuraPanel ();
  }
  
  public boolean isContentPanelScrollable ()
  {
    return isContentPanelScrollable;
  }
  
  public void setContentPanelScrollable (boolean isContentPanelScrollable)
  {
    this.isContentPanelScrollable = isContentPanelScrollable;
  }
  
  public BarraNavegacaoPPGD getBarraNavegacaoPPGD ()
  {
    return barraNavegacaoPPGD;
  }
  
  public void setBarraNavegacaoPPGD (BarraNavegacaoPPGD barraNavegacaoPPGD)
  {
    this.barraNavegacaoPPGD = barraNavegacaoPPGD;
  }
  
  protected boolean permiteCriacao ()
  {
    return true;
  }
  
  protected void mostraPainel ()
  {
    if (colecao == null || colecao.recuperarLista ().size () == 0)
      {
	objetoNegocio = criaObjetoNegocio ();
	colecao.recuperarLista ().add (objetoNegocio);
      }
    objetoNegocio = (ObjetoNegocio) colecao.recuperarLista ().get (0);
    if (barraNavegacaoPPGD != null)
      barraNavegacaoPPGD.executaPrimeiro ();
  }
  
  protected void configuraPanel ()
  {
    if (barraNavegacaoPPGD == null)
      barraNavegacaoPPGD = new BarraNavegacaoPPGD (colecao, this)
      {
	protected boolean permiteCriacao ()
	{
	  return PainelGenericoColecao.this.permiteCriacao ();
	}
      };
    JPanel panelBN = new JPanel (new FlowLayout (1));
    panelBN.setBorder (Borders.DIALOG_BORDER);
    panelBN.add (barraNavegacaoPPGD);
    builder.setRow (1);
    builder.setColumn (2);
    builder.append (panelBN);
    builder.setRow (2);
    builder.setColumn (2);
    if (isContentPanelScrollable)
      builder.append (new JScrollPane (buildPanel ()));
    else
      builder.append (buildPanel ());
    mostraPainel ();
  }
  
  protected void escondePainel ()
  {
    KeyboardFocusManager.getCurrentKeyboardFocusManager ().clearGlobalFocusOwner ();
    try
      {
	SwingUtilities.invokeLater (new Runnable ()
	{
	  public void run ()
	  {
	    colecao.excluirRegistrosEmBranco ();
	  }
	});
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public ObjetoNegocio getObjetoNegocio ()
  {
    if (objetoNegocio == null)
      {
	colecao = getColecao ();
	if (colecao.recuperarLista ().isEmpty ())
	  {
	    objetoNegocio = criaObjetoNegocio ();
	    colecao.recuperarLista ().add (objetoNegocio);
	  }
	else
	  objetoNegocio = (ObjetoNegocio) colecao.recuperarLista ().get (0);
      }
    return objetoNegocio;
  }
  
  public PPGDFormBuilder getBuilder ()
  {
    return null;
  }
  
  public final void adicionaComponentes ()
  {
    /* empty */
  }
  
  protected JPanel buildPanel ()
  {
    PPGDFormPanel painelConteudo = new PPGDFormPanel ();
    adicionaComponentes (painelConteudo);
    return painelConteudo;
  }
  
  public boolean obtemConfirmacaoExclusao (ObjetoNegocio obj)
  {
    int confirmacao = ConfirmacaoDialog.show ("Confirma\u00e7\u00e3o de exclus\u00e3o", "<html>Voc\u00ea tem certeza de que deseja excluir o<br>registro atual?</html>");
    if (confirmacao != 1)
      return false;
    return true;
  }
  
  protected abstract void adicionaComponentes (PPGDFormPanel ppgdformpanel);
  
  public ObjetoNegocio criaObjetoNegocio ()
  {
    Colecao colecao = getColecao ();
    return FabricaUtilitarios.instanciaObjetoNegocio (colecao.getTipoItens (), colecao.getIdDeclaracao ());
  }
  
  public abstract void mostraOutroObjetoNegocio (ObjetoNegocio objetonegocio);
  
  protected abstract Colecao getColecao ();
}
