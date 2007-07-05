/* EditTableIdDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.util.List;

import serpro.ppgd.gui.table.TabelaComponentes;

public class EditTableIdDeclaracao extends TabelaComponentes
{
  protected TableModelIdDeclaracao tableModel;
  
  public EditTableIdDeclaracao (TableModelIdDeclaracao pTableModel)
  {
    setBorder (ConstantesGlobaisGUI.BORDA_VAZIA);
    tableModel = pTableModel;
    setModel (tableModel);
    configuraLayout ();
    for (int i = 0; i < getColumnCount (); i++)
      getColumnModel ().getColumn (i).setPreferredWidth (tableModel.getTamanhoColunas ()[i]);
  }
  
  public void setLstIdDeclaracoes (List lstIdDeclaracoes)
  {
    tableModel.setLstIdDeclaracao (lstIdDeclaracoes);
  }
  
  protected void configuraLayout ()
  {
    /* empty */
  }
}
