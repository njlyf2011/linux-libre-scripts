/* KeySelectionCustomizadoObjNegocio - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.negocio.util.UtilitariosString;

class KeySelectionCustomizadoObjNegocio implements JComboBox.KeySelectionManager
{
  long ultimaVez = 0L;
  String filtro = "";
  int colunaFiltro;
  
  public KeySelectionCustomizadoObjNegocio (int aColunaFiltro)
  {
    colunaFiltro = aColunaFiltro;
  }
  
  public int selectionForKey (char aKey, ComboBoxModel aModel)
  {
    int itemSelecionado = -1;
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
	    KeySelectionCustomizadoObjNegocio keyselectioncustomizadoobjnegocio = this;
	    String string = keyselectioncustomizadoobjnegocio.filtro;
	    StringBuffer stringbuffer = new StringBuffer (string);
	    keyselectioncustomizadoobjnegocio.filtro = stringbuffer.append (aKey).toString ();
	    itemSelecionado = tentaSelecionar (aModel);
	  }
	else
	  {
	    filtro = "";
	    KeySelectionCustomizadoObjNegocio keyselectioncustomizadoobjnegocio = this;
	    String string = keyselectioncustomizadoobjnegocio.filtro;
	    StringBuffer stringbuffer = new StringBuffer (string);
	    keyselectioncustomizadoobjnegocio.filtro = stringbuffer.append (aKey).toString ();
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
	JEditObjetoNegocioItemIf elem = (JEditObjetoNegocioItemIf) aModel.getElementAt (i);
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
