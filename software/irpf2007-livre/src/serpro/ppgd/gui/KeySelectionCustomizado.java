/* KeySelectionCustomizado - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import serpro.ppgd.negocio.ElementoTabela;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.UtilitariosString;

class KeySelectionCustomizado implements JComboBox.KeySelectionManager
{
  long ultimaVez = 0L;
  String filtro = "";
  int colunaFiltro;
  int maxDelayFiltroTecla = 500;
  
  public KeySelectionCustomizado (int aColunaFiltro)
  {
    colunaFiltro = aColunaFiltro;
    maxDelayFiltroTecla = Integer.parseInt (FabricaUtilitarios.getProperties ().getProperty ("maxDelayFiltroEditCodigo", "500"));
  }
  
  public void setColunaFiltro (int aColunaFiltro)
  {
    colunaFiltro = aColunaFiltro;
  }
  
  public int selectionForKey (char aKey, ComboBoxModel aModel)
  {
    int itemSelecionado = -1;
    if (colunaFiltro >= 0)
      {
	long atual = System.currentTimeMillis ();
	long diferenca = atual - ultimaVez;
	if (diferenca < (long) maxDelayFiltroTecla)
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
