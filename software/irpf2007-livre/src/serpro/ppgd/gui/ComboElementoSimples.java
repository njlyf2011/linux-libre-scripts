/* ComboElementoSimples - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import serpro.ppgd.negocio.ElementoTabela;

public class ComboElementoSimples extends ComboElementoTabela
{
  private class LocalUI extends BasicComboBoxUI
  {
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
	    {
	      comboBox.setSelectedIndex (0);
	      list.setSelectedIndex (0);
	    }
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
      return popup;
    }
  }
  
  private class LocalRenderer extends JLabel implements ListCellRenderer
  {
    private ComboElementoSimples combo;
    
    public LocalRenderer (ComboElementoSimples combo)
    {
      setOpaque (true);
      this.combo = combo;
    }
    
    public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
      Graphics g = combo.getGraphics ();
      int width = 0;
      int tamTotal = 0;
      int tamCol = 0;
      int height = 10;
      setBackground (isSelected ? list.getSelectionBackground () : list.getBackground ());
      setForeground (isSelected ? list.getSelectionForeground () : list.getForeground ());
      if (value instanceof ElementoTabela)
	{
	  ElementoTabela elementoTabela = (ElementoTabela) value;
	  setText (elementoTabela.getConteudo (1));
	  tamCol = ((Integer) tamColunas.get (1)).intValue ();
	  if (g != null)
	    {
	      if (tamCol != 0)
		{
		  width = g.getFontMetrics (g.getFont ()).charWidth ('0') * tamCol;
		  width += 17;
		}
	      else
		width = g.getFontMetrics (g.getFont ()).stringWidth ((String) maiorLinha.get (1)) + 8;
	      height = g.getFontMetrics (g.getFont ()).getHeight ();
	    }
	  else
	    width = 50;
	  tamTotal += width;
	  int popupWidth = tamTotal + 17;
	  int comboWidth = ComboElementoSimples.this.getWidth ();
	  int comboMenosBarraWidth = comboWidth - 17;
	  if (popupWidth < comboMenosBarraWidth)
	    popupWidth = comboMenosBarraWidth;
	  ComboElementoSimples.this.setPopupWidth (popupWidth);
	  setPreferredSize (new Dimension (popupWidth - 17, height + 7));
	}
      else if (index == -1)
	setText (" ");
      return this;
    }
  }
  
  public ComboElementoSimples (Vector vItems)
  {
    super (vItems);
  }
  
  public ComboElementoSimples (Vector vItems, int colunaFiltro)
  {
    super (vItems, colunaFiltro);
  }
  
  public ListCellRenderer getCustomRenderer (int nCols)
  {
    return new LocalRenderer (this);
  }
  
  public EtchedBorder getCustomBorder ()
  {
    return new EtchedBorder ();
  }
  
  public BasicComboBoxUI getCustomLocalUI ()
  {
    return new LocalUI ();
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
    int nCols = 1;
    int colDesc = 1;
    maiorLinha.add ("");
    maiorLinha.add ("");
    tamColunas.add (new Integer (0));
    tamColunas.add (new Integer (0));
    for (int i = 0; i < vItems.size (); i++)
      {
	elementoTabela = (ElementoTabela) vItems.get (i);
	if (elementoTabela.size () > colDesc && elementoTabela.getConteudo (colDesc).length () > ((String) maiorLinha.get (colDesc)).length ())
	  maiorLinha.set (colDesc, elementoTabela.getConteudo (colDesc));
      }
    return nCols;
  }
}
