/* JEditCodigo - Decompiled by JODE
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
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import serpro.ppgd.gui.editors.PPGDComboBox;
import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.ElementoTabela;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.negocio.util.UtilitariosString;

public class JEditCodigo extends JEditCampo implements ActionListener
{
  private static Codigo vazio = new Codigo ("C\u00f3digo");
  JComboBox combo;
  Dimension[] tamCols;
  boolean[] colsVisiveisExpandidas;
  boolean[] colsVisiveis;
  private Border bordaColunas = BorderFactory.createMatteBorder (0, 0, 1, 1, Color.GRAY);
  private Border bordaVazia = BorderFactory.createEmptyBorder ();
  
  class KeySelectionCustomizado implements JComboBox.KeySelectionManager
  {
    long ultimaVez = 0L;
    String filtro = "";
    
    public KeySelectionCustomizado ()
    {
      /* empty */
    }
    
    public int selectionForKey (char aKey, ComboBoxModel aModel)
    {
      int itemSelecionado = -1;
      int colunaFiltro = ((Codigo) JEditCodigo.this.getInformacao ()).getColunaFiltro ();
      if (colunaFiltro < 0)
	{
	  LogPPGD.erro ("N\u00e3o foi especificada nenhuma coluna-filtro para o Combo!");
	  LogPPGD.erro ("N\u00e3o ser\u00e1 poss\u00edvel fazer filtragem por teclado");
	  LogPPGD.erro ("");
	}
      else
	{
	  long atual = System.currentTimeMillis ();
	  long diferenca = atual - ultimaVez;
	  if (diferenca < 500L)
	    {
	      KeySelectionCustomizado keyselectioncustomizado = this;
	      String string = keyselectioncustomizado.filtro;
	      StringBuffer stringbuffer = new StringBuffer (string);
	      keyselectioncustomizado.filtro = stringbuffer.append (aKey).toString ();
	      itemSelecionado = tentaSelecionar (aModel);
	    }
	  else
	    {
	      filtro = "";
	      KeySelectionCustomizado keyselectioncustomizado = this;
	      String string = keyselectioncustomizado.filtro;
	      StringBuffer stringbuffer = new StringBuffer (string);
	      keyselectioncustomizado.filtro = stringbuffer.append (aKey).toString ();
	      itemSelecionado = tentaSelecionar (aModel);
	    }
	  ultimaVez = atual;
	  if (filtro.length () > 100)
	    filtro = "";
	}
      return itemSelecionado;
    }
    
    private synchronized int tentaSelecionar (ComboBoxModel aModel)
    {
      int total = aModel.getSize ();
      int colunaFiltro = ((Codigo) JEditCodigo.this.getInformacao ()).getColunaFiltro ();
      int itemSelecionado = -1;
      String upperFiltro = filtro.toUpperCase ();
      for (int i = 0; i < total; i++)
	{
	  ElementoTabela elem = (ElementoTabela) aModel.getElementAt (i);
	  String item = UtilitariosString.removeAcentos (elem.getConteudo (colunaFiltro));
	  if (item.toUpperCase ().startsWith (upperFiltro))
	    {
	      itemSelecionado = i;
	      break;
	    }
	}
      return itemSelecionado;
    }
  }
  
  public JEditCodigo ()
  {
    super (vazio);
    aplicaItens (((Codigo) campo).getColecaoElementoTabela ());
    inicio ();
  }
  
  public JEditCodigo (Informacao aCampo)
  {
    super (aCampo);
    inicio ();
  }
  
  public JEditCodigo (Informacao aCampo, String idAjuda)
  {
    super (aCampo, idAjuda);
    inicio ();
  }
  
  protected boolean continuaValidacaoImpeditiva (Object proxConteudo)
  {
    if (proxConteudo != null && proxConteudo.equals (((Codigo) getInformacao ()).getElementoTabela ()))
      return false;
    return true;
  }
  
  protected void desfazModificacao ()
  {
    int indice = ((Codigo) getInformacao ()).getIndiceElementoTabela ();
    combo.setSelectedIndex (indice);
    SwingUtilities.invokeLater (new Runnable ()
    {
      public void run ()
      {
	combo.grabFocus ();
      }
    });
  }
  
  private void inicio ()
  {
    setInformacao (getInformacao ());
    combo.setKeySelectionManager (new KeySelectionCustomizado ());
    combo.addActionListener (this);
    combo.addFocusListener (new FocusAdapter ()
    {
      public void focusLost (FocusEvent e)
      {
	if (e.getOppositeComponent () != null && JEditCodigo.this.isFocusLostAtivo ())
	  {
	    JEditCodigo.this.setIdentificacaoFoco (false);
	    if (! JEditCodigo.this.getInformacao ().getListaValidadoresImpeditivos ().isEmpty ())
	      {
		ElementoTabela elemento = ((Codigo) JEditCodigo.this.getInformacao ()).getElementoTabela (combo.getSelectedIndex ());
		if (! JEditCodigo.this.verificaValidacoesImpeditivas (elemento))
		  return;
		setaCampo ();
	      }
	    JEditCodigo.this.chamaValidacao ();
	  }
      }
    });
    getInformacao ().getObservadores ().addPropertyChangeListener ("ComboReiniciado", this);
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
  
  private void aplicaItens (List elems)
  {
    if (elems != null)
      {
	calculaTamanhoMaxCols ();
	combo.setModel (new DefaultComboBoxModel (elems.toArray ()));
	combo.setSelectedItem (((Codigo) campo).getElementoTabela ());
      }
  }
  
  protected void buildComponente ()
  {
    removeAll ();
    setLayout (new BorderLayout ());
    if (combo == null)
      {
	combo = new CustomComboBox (this);
	combo.setRenderer (new MyCustomRenderer (this));
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
    aplicaItens (((Codigo) campo).getColecaoElementoTabela ());
    ((PPGDComboBox) combo).setInformacao (campo);
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    if (evt.getPropertyName () != null && evt.getPropertyName ().equals ("ComboReiniciado"))
      setInformacao (getInformacao ());
    if (evt.getPropertyName () != null && evt.getPropertyName ().equals (getInformacao ().getNomeCampo ()))
      combo.setSelectedIndex (((Codigo) getInformacao ()).getIndiceElementoTabela ());
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
    ElementoTabela elem = (ElementoTabela) combo.getSelectedItem ();
    if (elem != null)
      campo.setConteudo (elem.getConteudo (0));
  }
  
  private void calculaTamanhoMaxCols ()
  {
    Iterator it = ((Codigo) campo).getColecaoElementoTabela ().iterator ();
    if (it.hasNext ())
      {
	ElementoTabela elem = (ElementoTabela) it.next ();
	tamCols = new Dimension[elem.size ()];
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
	ElementoTabela elem = (ElementoTabela) it.next ();
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
