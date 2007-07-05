/* TableModelGCap - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.ganhosdecapital;
import serpro.ppgd.irpf.ganhosdecapital.ItemDadosGCap;
import serpro.ppgd.irpf.gui.IRPFTableModel;
import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;

public abstract class TableModelGCap extends IRPFTableModel
{
  public TableModelGCap (ObjetoNegocio pObj)
  {
    super (pObj);
  }
  
  public boolean isCellEditable (int rowIndex, int columnIndex)
  {
    return false;
  }
  
  public void setValueAt (Object aValue, int rowIndex, int columnIndex)
  {
    /* empty */
  }
  
  public int getRowCount ()
  {
    return ((Colecao) getObjetoNegocio ()).recuperarLista ().size ();
  }
  
  public int getColumnCount ()
  {
    return 3;
  }
  
  public Informacao getInformacaoAt (int row, int col)
  {
    Colecao colecao = (Colecao) getObjetoNegocio ();
    ItemDadosGCap item = (ItemDadosGCap) colecao.recuperarLista ().get (row);
    switch (col)
      {
      case 0:
	return item.getCodItem ();
      case 1:
	return item.getDescricao ();
      case 2:
	return item.getCpf ();
      default:
	return null;
      }
  }
  
  public Object getValueAt (int rowIndex, int columnIndex)
  {
    return getInformacaoAt (rowIndex, columnIndex).getConteudoFormatado ();
  }
}
