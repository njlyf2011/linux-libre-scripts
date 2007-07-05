/* JEditObjetoNegocio - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import serpro.ppgd.gui.editors.PPGDComboBox;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.Informacao;

public class JEditObjetoNegocio extends JEditCampo implements ActionListener
{
  private static Alfa vazio = new Alfa ("C\u00f3digo");
  JComboBox combo;
  protected Colecao colecao;
  Dimension[] tamCols;
  boolean[] colsVisiveisExpandidas;
  boolean[] colsVisiveis;
  private Border bordaColunas = BorderFactory.createMatteBorder (0, 0, 1, 1, Color.GRAY);
  private Border bordaVazia = BorderFactory.createEmptyBorder ();
  
  public JEditObjetoNegocio ()
  {
    super (vazio);
    aplicaItens (colecao.recuperarLista ());
    inicio ();
  }
  
  public JEditObjetoNegocio (Informacao aCampo)
  {
    super (aCampo);
    inicio ();
  }
  
  public JEditObjetoNegocio (Informacao aCampo, String idAjuda)
  {
    super (aCampo, idAjuda);
    inicio ();
  }
  
  protected boolean continuaValidacaoImpeditiva (Object proxConteudo)
  {
    if (proxConteudo != null && proxConteudo.equals (getItemSelecionado ()))
      return false;
    return true;
  }
  
  protected int getIndiceSelecionado ()
  {
    Iterator it = colecao.recuperarLista ().iterator ();
    int indice = 0;
    while (it.hasNext ())
      {
	JEditObjetoNegocioItemIf item = (JEditObjetoNegocioItemIf) it.next ();
	if (item.getConteudo (0).equals (getInformacao ().asString ()))
	  return indice;
	indice++;
      }
    return -1;
  }
  
  protected JEditObjetoNegocioItemIf getItemSelecionado ()
  {
    int indice = getIndiceSelecionado ();
    if (indice != -1)
      return (JEditObjetoNegocioItemIf) colecao.recuperarLista ().get (indice);
    return null;
  }
  
  protected void desfazModificacao ()
  {
    int indice = getIndiceSelecionado ();
    combo.setSelectedIndex (indice);
    SwingUtilities.invokeLater (new Runnable ()
    {
      public void run ()
      {
	combo.grabFocus ();
      }
    });
  }
  
  protected int getTotalColunas ()
  {
    if (colecao.getTipoItens () == null)
      return 0;
    if (colecao.recuperarLista ().isEmpty ())
      return ((JEditObjetoNegocioItemIf) colecao.instanciaNovoObjeto ()).getTotalAtributos ();
    return ((JEditObjetoNegocioItemIf) colecao.recuperarLista ().get (0)).getTotalAtributos ();
  }
  
  protected int getColunaFiltro ()
  {
    if (colecao.getTipoItens () == null)
      return 0;
    if (colecao.recuperarLista ().isEmpty ())
      return ((JEditObjetoNegocioItemIf) colecao.instanciaNovoObjeto ()).getColunaFiltro ();
    return ((JEditObjetoNegocioItemIf) colecao.recuperarLista ().get (0)).getColunaFiltro ();
  }
  
  private void inicio ()
  {
    setInformacao (getInformacao ());
    combo.setKeySelectionManager (new KeySelectionCustomizadoObjNegocio (getColunaFiltro ()));
    combo.addActionListener (this);
    combo.addFocusListener (new FocusAdapter ()
    {
      public void focusLost (FocusEvent e)
      {
	if (e.getOppositeComponent () != null && JEditObjetoNegocio.this.isFocusLostAtivo ())
	  {
	    JEditObjetoNegocio.this.setIdentificacaoFoco (false);
	    if (! JEditObjetoNegocio.this.getInformacao ().getListaValidadoresImpeditivos ().isEmpty ())
	      {
		JEditObjetoNegocioItemIf elemento = getItemSelecionado ();
		if (! JEditObjetoNegocio.this.verificaValidacoesImpeditivas (elemento))
		  return;
		setaCampo ();
	      }
	    JEditObjetoNegocio.this.chamaValidacao ();
	  }
      }
    });
  }
  
  protected void initColsVisiveis ()
  {
    colsVisiveis = new boolean[30];
    colsVisiveisExpandidas = new boolean[30];
    for (int i = 0; i < colsVisiveis.length; i++)
      colsVisiveis[i] = colsVisiveisExpandidas[i] = false;
    exibeColunaNaoExpandida (1);
    exibeColunaExpandida (1);
  }
  
  public void exibeColunaNaoExpandida (int aCol)
  {
    colsVisiveis[aCol] = true;
  }
  
  public void escondeColunaNaoExpandida (int aCol)
  {
    colsVisiveis[aCol] = false;
  }
  
  public void exibeColunaExpandida (int aCol)
  {
    colsVisiveisExpandidas[aCol] = true;
  }
  
  public void escondeColunaExpandida (int aCol)
  {
    colsVisiveisExpandidas[aCol] = false;
  }
  
  public Colecao getColecao ()
  {
    return colecao;
  }
  
  public void setColecao (Colecao colecao)
  {
    this.colecao = colecao;
    aplicaItens (colecao.recuperarLista ());
  }
  
  private void aplicaItens (List elems)
  {
    if (elems != null)
      {
	calculaTamanhoMaxCols ();
	combo.setModel (new DefaultComboBoxModel (elems.toArray ()));
	combo.setSelectedItem (getItemSelecionado ());
      }
  }
  
  protected void buildComponente ()
  {
    removeAll ();
    setLayout (new BorderLayout ());
    if (combo == null)
      {
	colecao = new Colecao ()
	{
	};
	combo = new CustomComboBoxObjNegocio (this);
	combo.setRenderer (new MyCustomRendererObjNegocio (this));
      }
    if (colsVisiveis == null || colsVisiveisExpandidas == null)
      initColsVisiveis ();
    add (combo, "Center");
    JButtonMensagem btnMsg = getButtonMensagem ();
    if (btnMsg != null)
      add (new PainelBotao (btnMsg), "East");
  }
  
  protected void informacaoModificada ()
  {
    aplicaItens (colecao.recuperarLista ());
    ((PPGDComboBox) combo).setInformacao (campo);
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    if (evt.getPropertyName () != null && evt.getPropertyName ().equals (getInformacao ().getNomeCampo ()))
      combo.setSelectedIndex (getIndiceSelecionado ());
  }
  
  public JComponent getComponenteEditor ()
  {
    return combo;
  }
  
  public JComponent getComponenteFoco ()
  {
    return combo;
  }
  
  public void actionPerformed (ActionEvent e)
  {
    if (getInformacao ().getListaValidadoresImpeditivos ().isEmpty ())
      setaCampo ();
  }
  
  public void setaCampo ()
  {
    JEditObjetoNegocioItemIf elem = (JEditObjetoNegocioItemIf) combo.getSelectedItem ();
    if (elem != null)
      campo.setConteudo (elem.getConteudo (0));
  }
  
  private void calculaTamanhoMaxCols ()
  {
    Iterator it = colecao.recuperarLista ().iterator ();
    if (it.hasNext ())
      {
	JEditObjetoNegocioItemIf elem = (JEditObjetoNegocioItemIf) it.next ();
	tamCols = new Dimension[elem.getTotalAtributos ()];
	for (int i = 0; i < tamCols.length; i++)
	  {
	    Dimension tam = new JLabel (elem.getConteudo (i)).getPreferredSize ();
	    if (tamCols[i] == null || tamCols[i].width < tam.width)
	      {
		tamCols[i] = tam;
		tamCols[i].width += 5;
	      }
	  }
      }
    while (it.hasNext ())
      {
	JEditObjetoNegocioItemIf elem = (JEditObjetoNegocioItemIf) it.next ();
	for (int i = 0; i < tamCols.length; i++)
	  {
	    Dimension tam = new JLabel (elem.getConteudo (i)).getPreferredSize ();
	    if (tamCols[i] == null || tamCols[i].width < tam.width)
	      {
		tamCols[i] = tam;
		tamCols[i].width += 5;
	      }
	  }
      }
  }
  
  protected void readOnlyPropertyChange (boolean readOnly)
  {
    combo.setEnabled (! readOnly);
  }
  
  protected void habilitadoPropertyChange (boolean habilitado)
  {
    combo.setEnabled (habilitado);
  }
  
  public Border getBordaColunas ()
  {
    Border borda = null;
    int qtdColsVisiveis = 0;
    for (int i = 0; i < colsVisiveis.length; i++)
      {
	if (colsVisiveis[i])
	  qtdColsVisiveis++;
      }
    if (qtdColsVisiveis > 1)
      borda = bordaColunas;
    else
      borda = bordaVazia;
    return borda;
  }
  
  public void setBordaColunas (Border bordaColunas)
  {
    this.bordaColunas = bordaColunas;
  }
  
  public void setEstiloFonte (int estilo)
  {
    Font f = combo.getFont ();
    f = f.deriveFont (estilo);
    combo.setFont (f);
  }
  
  public void setIncrementoTamanhoFonte (int incremento)
  {
    incrementoTamanhoFonte = incremento;
    Font f = combo.getFont ();
    if (tamanhoOriginal == -1.0F)
      tamanhoOriginal = f.getSize2D ();
    f = f.deriveFont (tamanhoOriginal + (float) incremento);
    combo.setFont (f);
  }
  
  public int getIncrementoTamanhoFonte ()
  {
    return incrementoTamanhoFonte;
  }
}
