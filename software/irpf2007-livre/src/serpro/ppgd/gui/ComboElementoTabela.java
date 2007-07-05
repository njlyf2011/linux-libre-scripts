/* ComboElementoTabela - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import serpro.ppgd.negocio.ElementoTabela;
import serpro.ppgd.negocio.util.UtilitariosString;

public class ComboElementoTabela extends JComboBox
{
  protected Vector vItems;
  protected int widthTable;
  protected Vector maiorLinha = new Vector ();
  protected Vector tamColunas = new Vector ();
  
  private class LocalUI extends BasicComboBoxUI
  {
    private BasicComboPopup customPopup;
    
    protected ComboPopup createPopup ()
    {
      BasicComboPopup popup = new BasicComboPopup (comboBox)
      {
	protected JScrollPane createScroller ()
	{
	  JScrollPane jscrollpane = new JScrollPane (list, 20, 30);
	  return jscrollpane;
	}
	
	public void show ()
	{
	  int selectedIndex = comboBox.getSelectedIndex ();
	  if (selectedIndex == -1)
	    list.clearSelection ();
	  else
	    list.setSelectedIndex (selectedIndex);
	  list.ensureIndexIsVisible (list.getSelectedIndex ());
	  Dimension popupSize = ((ComboElementoTabela) comboBox).getPopupSize ();
	  popupSize.setSize (popupSize.width, getPopupHeightForRowCount (comboBox.getMaximumRowCount ()));
	  Rectangle popupBounds = computePopupBounds (0, comboBox.getBounds ().height, popupSize.width, popupSize.height);
	  scroller.setMaximumSize (popupBounds.getSize ());
	  scroller.setPreferredSize (popupBounds.getSize ());
	  scroller.setMinimumSize (popupBounds.getSize ());
	  list.invalidate ();
	  setLightWeightPopupEnabled (comboBox.isLightWeightPopupEnabled ());
	  this.show (comboBox, popupBounds.x, popupBounds.y);
	}
      };
      popup.getAccessibleContext ().setAccessibleParent (comboBox);
      customPopup = popup;
      return popup;
    }
    
    public BasicComboPopup getCustomPopup ()
    {
      return customPopup;
    }
    
    public void setCustomPopup (BasicComboPopup customPopup)
    {
      this.customPopup = customPopup;
    }
  }
  
  private class LocalRenderer extends JTable implements ListCellRenderer
  {
    private ComboElementoTabela combo;
    private int nCols;
    
    public LocalRenderer (ComboElementoTabela combo, int nCols)
    {
      super (1, nCols);
      this.combo = combo;
      this.nCols = nCols;
    }
    
    public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
      setBackground (isSelected ? list.getSelectionBackground () : list.getBackground ());
      setForeground (isSelected ? list.getSelectionForeground () : list.getForeground ());
      Graphics g = combo.getGraphics ();
      int w = 0;
      int tamTotal = 0;
      int tamCol = 0;
      if (value instanceof ElementoTabela)
	{
	  ElementoTabela elementoTabela = (ElementoTabela) value;
	  for (int i = 0; i < nCols; i++)
	    {
	      getModel ().setValueAt (elementoTabela.getConteudo (i), 0, i);
	      TableColumnModel columnModel = getColumnModel ();
	      TableColumn tableColumn = columnModel.getColumn (i);
	      tamCol = ((Integer) tamColunas.get (i)).intValue ();
	      if (g != null)
		{
		  if (tamCol != 0)
		    {
		      w = g.getFontMetrics (g.getFont ()).charWidth ('0') * tamCol;
		      if (i == 0)
			w += 17;
		      else
			w += 8;
		    }
		  else
		    w = g.getFontMetrics (g.getFont ()).stringWidth ((String) maiorLinha.get (i)) + 8;
		}
	      else
		w = 50;
	      tableColumn.setMaxWidth (w);
	      tableColumn.setMinWidth (w);
	      tamTotal += w;
	    }
	  setRowSelectionAllowed (true);
	  setPopupWidth (tamTotal + 17);
	}
      else if (index == -1)
	getModel ().setValueAt (" ", 0, 0);
      return this;
    }
  }
  
  private class KeySelectionCustomizado implements JComboBox.KeySelectionManager
  {
    long ultimaVez = 0L;
    String filtro = "";
    int colunaFiltro;
    
    public KeySelectionCustomizado (int aColunaFiltro)
    {
      colunaFiltro = aColunaFiltro;
    }
    
    public int selectionForKey (char aKey, ComboBoxModel aModel)
    {
      long atual = System.currentTimeMillis ();
      long diferenca = atual - ultimaVez;
      int itemSelecionado = -1;
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
      return itemSelecionado;
    }
    
    private synchronized int tentaSelecionar (ComboBoxModel aModel)
    {
      int total = aModel.getSize ();
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
  
  public ComboElementoTabela (Vector vItems)
  {
    super (vItems);
    this.vItems = vItems;
    int nCols = init ();
    setBackground (ConstantesGlobaisGUI.COR_BRANCO);
    setForeground (ConstantesGlobaisGUI.COR_PRETO);
    setUI (getCustomLocalUI ());
    setBorder (getCustomBorder ());
    setRenderer (getCustomRenderer (nCols));
  }
  
  public ComboElementoTabela (Vector vItems, int aColunaFiltro)
  {
    this (vItems);
    if (aColunaFiltro != -1)
      setKeySelectionManager (new KeySelectionCustomizado (aColunaFiltro));
  }
  
  public ListCellRenderer getCustomRenderer (int nCols)
  {
    return new LocalRenderer (this, nCols);
  }
  
  public EtchedBorder getCustomBorder ()
  {
    return new EtchedBorder ();
  }
  
  public BasicComboBoxUI getCustomLocalUI ()
  {
    return new LocalUI ();
  }
  
  public void setColecao (Vector vItems)
  {
    setModel (new DefaultComboBoxModel (vItems));
    this.vItems = vItems;
    int nCols = init ();
    setRenderer (getCustomRenderer (nCols));
  }
  
  public Vector getColecao ()
  {
    return vItems;
  }
  
  public void setPopupWidth (int width)
  {
    widthTable = width;
  }
  
  public Dimension getPopupSize ()
  {
    Dimension size = getSize ();
    if (widthTable < 1)
      widthTable = size.width;
    return new Dimension (widthTable, size.height);
  }
  
  public void setSizeCol (int coluna, int valor)
  {
    tamColunas.set (coluna, new Integer (valor));
  }
  
  public int init ()
  {
    if (vItems.size () == 0)
      {
	ElementoTabela elementoTabela = new ElementoTabela ();
	elementoTabela.setConteudo (0, "  ");
	vItems.add (elementoTabela);
      }
    ElementoTabela elementoTabela = (ElementoTabela) vItems.firstElement ();
    int nCols = elementoTabela.size ();
    for (int i = 0; i < nCols; i++)
      {
	maiorLinha.add ("");
	tamColunas.add (new Integer (0));
      }
    for (int i = 0; i < vItems.size (); i++)
      {
	elementoTabela = (ElementoTabela) vItems.get (i);
	for (int j = 0; j < elementoTabela.size (); j++)
	  {
	    if (elementoTabela.getConteudo (j).length () > ((String) maiorLinha.get (j)).length ())
	      maiorLinha.set (j, elementoTabela.getConteudo (j));
	  }
      }
    return nCols;
  }
}
