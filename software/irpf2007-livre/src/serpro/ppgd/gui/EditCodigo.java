/* EditCodigo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.Color;
import java.awt.Dimension;
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
import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.ElementoTabela;
import serpro.ppgd.negocio.Informacao;

public class EditCodigo extends EditCampo implements ActionListener
{
  private static Codigo vazio = new Codigo ("C\u00f3digo");
  JComboBox combo;
  Dimension[] tamCols;
  boolean[] colsVisiveisExpandidas;
  boolean[] colsVisiveis;
  private Border bordaColunas = BorderFactory.createMatteBorder (0, 0, 1, 1, Color.GRAY);
  private Border bordaVazia = BorderFactory.createEmptyBorder ();
  private KeySelectionCustomizado keySelectionCustomizado;
  
  public EditCodigo ()
  {
    super (vazio);
    aplicaItens (((Codigo) campo).getColecaoElementoTabela ());
    inicio ();
  }
  
  public EditCodigo (Informacao aCampo)
  {
    super (aCampo);
    inicio ();
  }
  
  public EditCodigo (Informacao aCampo, Dimension[] d, String idAjuda)
  {
    super (aCampo, d, idAjuda);
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
    combo.setKeySelectionManager (keySelectionCustomizado);
    combo.addActionListener (this);
    combo.addFocusListener (new FocusAdapter ()
    {
      public void focusLost (FocusEvent e)
      {
	if (e.getOppositeComponent () != null && EditCodigo.this.isFocusLostAtivo ())
	  {
	    EditCodigo.this.setIdentificacaoFoco (false);
	    if (! EditCodigo.this.getInformacao ().getListaValidadoresImpeditivos ().isEmpty ())
	      {
		ElementoTabela elemento = ((Codigo) EditCodigo.this.getInformacao ()).getElementoTabela (combo.getSelectedIndex ());
		if (! EditCodigo.this.verificaValidacoesImpeditivas (elemento))
		  return;
		setaCampo ();
	      }
	    EditCodigo.this.chamaValidacao ();
	  }
      }
    });
    getInformacao ().getObservadores ().addPropertyChangeListener ("ComboReiniciado", this);
  }
  
  private void initColsVisiveis ()
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
    if (combo == null)
      combo = new CustomComboBox (this);
    if (colsVisiveis == null || colsVisiveisExpandidas == null)
      initColsVisiveis ();
    combo.setRenderer (new MyCustomRenderer (this));
    if (elems != null)
      {
	calculaTamanhoMaxCols ();
	combo.setModel (new DefaultComboBoxModel (elems.toArray ()));
	combo.setSelectedItem (((Codigo) campo).getElementoTabela ());
      }
  }
  
  public void setInformacao (Informacao campo)
  {
    if (campo != null)
      {
	this.campo = campo;
	aplicaItens (((Codigo) campo).getColecaoElementoTabela ());
	((PPGDComboBox) combo).setInformacao (campo);
	if (keySelectionCustomizado == null)
	  keySelectionCustomizado = new KeySelectionCustomizado (((Codigo) campo).getColunaFiltro ());
	else
	  keySelectionCustomizado.setColunaFiltro (((Codigo) campo).getColunaFiltro ());
      }
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
    labelCampo.setEnabled (habilitado);
  }
  
  public void setMaximoCaracteresNaColuna (int col, int qtd)
  {
    String s = "";
    for (int i = 0; i < qtd; i++)
      s += " ";
    tamCols[col] = new JLabel (s).getPreferredSize ();
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
  
  public void setPerdeFocoComEnter (boolean aPerdeFocoComEnter)
  {
    isPerdeFocoComEnter = aPerdeFocoComEnter;
    if (isPerdeFocoComEnter ())
      aplicaTransfereFocoEnter ();
    else
      removeTransfereFocoEnter ();
  }
}
