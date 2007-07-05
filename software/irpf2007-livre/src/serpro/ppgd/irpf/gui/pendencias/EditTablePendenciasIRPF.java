/* EditTablePendenciasIRPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.pendencias;
import java.awt.Color;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import serpro.ppgd.gui.ConstantesGlobaisGUI;
import serpro.ppgd.gui.pendencia.EditTablePendencia;
import serpro.ppgd.gui.pendencia.LinhaPendencia;
import serpro.ppgd.gui.pendencia.MapeamentoInformacaoEditCampo;
import serpro.ppgd.gui.pendencia.TableModelPendencia;
import serpro.ppgd.gui.xbeans.JEditCampo;
import serpro.ppgd.infraestrutura.util.PainelCacher;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.atividaderural.PainelBensARBrasil;
import serpro.ppgd.irpf.gui.atividaderural.PainelBensARExterior;
import serpro.ppgd.irpf.gui.atividaderural.PainelDividasARBrasil;
import serpro.ppgd.irpf.gui.atividaderural.PainelDividasARExterior;
import serpro.ppgd.irpf.gui.atividaderural.PainelIdentificacaoImovelARBrasil;
import serpro.ppgd.irpf.gui.atividaderural.PainelIdentificacaoImovelARExterior;
import serpro.ppgd.irpf.gui.atividaderural.PainelReceitasDespesasExterior;
import serpro.ppgd.irpf.gui.bens.PainelBens;
import serpro.ppgd.irpf.gui.dependentes.PainelDependentes;
import serpro.ppgd.irpf.gui.dividas.PainelDividas;
import serpro.ppgd.irpf.gui.pagamentos.PainelPagamentos;
import serpro.ppgd.irpf.gui.rendpj.PainelRendPJDependentes;
import serpro.ppgd.irpf.gui.rendpj.PainelRendPJTitular;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.Pendencia;
import serpro.ppgd.negocio.util.UtilitariosArquivo;

public class EditTablePendenciasIRPF extends EditTablePendencia
{
  private static final ImageIcon ICO_ERRO_EXTENDIDO = new ImageIcon (UtilitariosArquivo.localizaArquivoEmClasspath ("/icones/ico_errop.gif"));
  private static final ImageIcon ICO_AVISO_EXTENDIDO = new ImageIcon (UtilitariosArquivo.localizaArquivoEmClasspath ("/icones/ico_avisop.gif"));
  
  private EditTablePendenciasIRPF (TableModelPendencia pTableModel)
  {
    super (pTableModel);
  }
  
  public EditTablePendenciasIRPF ()
  {
    super (new TableModelPendencia (new String[] { "Tipo", "Campo" }, 10, new int[] { 15, 430 }, new Vector ()));
    tableModel.setPosicaoQuebraDeLinhaMensagem (90);
    tableModel.setNumMinLinhas (12);
  }
  
  public Object getValueAt (int row, int column)
  {
    if (column == 0)
      {
	LinhaPendencia linhaPendencia = ((TableModelPendencia) getModel ()).getLinhaPendenciaAt (row);
	if (linhaPendencia != null)
	  {
	    if (linhaPendencia.getSeveridade () == 3)
	      return ICO_ERRO_EXTENDIDO;
	    if (linhaPendencia.getSeveridade () == 2)
	      return ICO_AVISO_EXTENDIDO;
	  }
      }
    return super.getValueAt (row, column);
  }
  
  protected void configuraLayout ()
  {
    setAlignmentX (0.0F);
    setTableHeader (null);
    setBackground (ConstantesGlobaisGUI.COR_BRANCO);
    setShowHorizontalLines (false);
    setShowVerticalLines (false);
    setShowGrid (false);
    setRowHeight (26);
    setSelectionMode (0);
    setSelectionBackground (getBackground ());
    setSelectionForeground (getForeground ());
  }
  
  protected void formataTextoDaPendenciaOnMouseOver (JLabel pLblTexto)
  {
    pLblTexto.setText ("<HTML><U><FONT COLOR=BLUE> " + pLblTexto.getText () + "</FONT></U></HTML>");
  }
  
  protected void formataTextoDaPendencia (JLabel pLblTexto)
  {
    setForeground (Color.BLACK);
  }
  
  public boolean getScrollableTracksViewportHeight ()
  {
    return true;
  }
  
  public boolean getScrollableTracksViewportWidth ()
  {
    return true;
  }
  
  public void selecionaPendencia (Pendencia pPendencia)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    IRPFUtil.setEstadoSistema (1);
    String ficha = pPendencia.getCampoInformacao ().getFicha ();
    DeclaracaoIRPF dec = IRPFFacade.getInstancia ().getDeclaracao ();
    JPanel painel = null;
    System.out.println ("ficha: " + ficha);
    if (ficha.equals (dec.getContribuinte ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.contribuinte.PainelContribuinte.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
      }
    else if (ficha.equals (dec.getColecaoRendPJTitular ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.rendpj.PainelRendPJTitular.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
	((PainelRendPJTitular) painel).getNavegador ().exibe (pPendencia.getNumItem () - 1);
      }
    else if (ficha.equals (dec.getColecaoRendPJDependente ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.rendpj.PainelRendPJDependentes.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
	((PainelRendPJDependentes) painel).getNavegador ().exibe (pPendencia.getNumItem () - 1);
      }
    else if (ficha.equals (dec.getRendPFTitular ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.rendpf.PainelRendPFTitular.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
      }
    else if (ficha.equals (dec.getRendPFDependente ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.rendpf.PainelRendPFDependentes.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
      }
    else if (ficha.equals (dec.getRendIsentos ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.rendIsentos.PainelRendIsentos.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
      }
    else if (ficha.equals (dec.getDependentes ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.dependentes.PainelDependentes.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
	((PainelDependentes) painel).getNavegador ().exibe (pPendencia.getNumItem () - 1);
      }
    else if (ficha.equals (dec.getPagamentos ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.pagamentos.PainelPagamentos.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
	((PainelPagamentos) painel).getNavegador ().exibe (pPendencia.getNumItem () - 1);
      }
    else if (ficha.equals (dec.getBens ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.bens.PainelBens.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
	((PainelBens) painel).getNavegador ().exibe (pPendencia.getNumItem () - 1);
      }
    else if (ficha.equals (dec.getDividas ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.dividas.PainelDividas.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
	((PainelDividas) painel).getNavegador ().exibe (pPendencia.getNumItem () - 1);
      }
    else if (ficha.equals (dec.getConjuge ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.conjuge.PainelConjuge.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
      }
    else if (ficha.equals (dec.getEspolio ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.espolio.PainelEspolio.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
      }
    else if (ficha.equals (dec.getDoacoes ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.eleicoes.PainelDoacoesCampanhas.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
      }
    else if (ficha.equals (dec.getImpostoPago ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.impostopago.PainelImpostoPago.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
      }
    else if (ficha.equals (dec.getAtividadeRural ().getBrasil ().getIdentificacaoImovel ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.atividaderural.PainelIdentificacaoImovelARBrasil.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
	((PainelIdentificacaoImovelARBrasil) painel).getNavegador ().exibe (pPendencia.getNumItem () - 1);
      }
    else if (ficha.equals (dec.getAtividadeRural ().getExterior ().getIdentificacaoImovel ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.atividaderural.PainelIdentificacaoImovelARExterior.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
	((PainelIdentificacaoImovelARExterior) painel).getNavegador ().exibe (pPendencia.getNumItem () - 1);
      }
    else if (ficha.equals (dec.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.atividaderural.PainelReceitasDespesasBrasil.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
      }
    else if (ficha.equals (dec.getAtividadeRural ().getExterior ().getReceitasDespesas ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.atividaderural.PainelReceitasDespesasExterior.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
	((PainelReceitasDespesasExterior) painel).getNavegador ().exibe (pPendencia.getNumItem () - 1);
      }
    else if (ficha.equals (dec.getAtividadeRural ().getBrasil ().getApuracaoResultado ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.atividaderural.PainelApuracaoResultadoBrasil.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
      }
    else if (ficha.equals (dec.getAtividadeRural ().getExterior ().getApuracaoResultado ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.atividaderural.PainelApuracaoResultadoExterior.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
      }
    else if (ficha.equals (dec.getAtividadeRural ().getBrasil ().getMovimentacaoRebanho ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.atividaderural.PainelMovimentacaoRebanhoBrasil.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
      }
    else if (ficha.equals (dec.getAtividadeRural ().getExterior ().getMovimentacaoRebanho ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.atividaderural.PainelMovimentacaoRebanhoExterior.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
      }
    else if (ficha.equals (dec.getAtividadeRural ().getBrasil ().getBens ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.atividaderural.PainelBensARBrasil.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
	((PainelBensARBrasil) painel).getNavegador ().exibe (pPendencia.getNumItem () - 1);
      }
    else if (ficha.equals (dec.getAtividadeRural ().getExterior ().getBens ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.atividaderural.PainelBensARExterior.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
	((PainelBensARExterior) painel).getNavegador ().exibe (pPendencia.getNumItem () - 1);
      }
    else if (ficha.equals (dec.getAtividadeRural ().getBrasil ().getDividas ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.atividaderural.PainelDividasARBrasil.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
	((PainelDividasARBrasil) painel).getNavegador ().exibe (pPendencia.getNumItem () - 1);
      }
    else if (ficha.equals (dec.getAtividadeRural ().getExterior ().getDividas ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.atividaderural.PainelDividasARExterior.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
	((PainelDividasARExterior) painel).getNavegador ().exibe (pPendencia.getNumItem () - 1);
      }
    else if (ficha.equals (dec.getResumo ().getCalculoImposto ().getFicha ()))
      {
	PainelCacher painelcacher = PainelCacher.getInstancia ();
	Class var_class = serpro.ppgd.irpf.gui.resumo.PainelCalculoImposto.class;
	painel = painelcacher.obtemUrgentemente (var_class.getName ());
      }
    IRPFGuiUtil.selecionaNodoArvore (painel);
    JEditCampo edit = (JEditCampo) MapeamentoInformacaoEditCampo.getEditAssociado (pPendencia.getCampoInformacao ());
    if (edit != null)
      edit.setaFoco (true);
    IRPFUtil.setEstadoSistema (0);
  }
}
