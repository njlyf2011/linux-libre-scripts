/* TableModelQuadroAuxiliarTransporteValores - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import serpro.ppgd.irpf.ColecaoItemQuadroAuxiliar;
import serpro.ppgd.irpf.ItemQuadroAuxiliar;
import serpro.ppgd.negocio.Informacao;

public class TableModelQuadroAuxiliarTransporteValores extends IRPFTableModel
{
  public TableModelQuadroAuxiliarTransporteValores (ColecaoItemQuadroAuxiliar pColecaoItemQuadroAuxiliar)
  {
    super ((serpro.ppgd.negocio.ObjetoNegocio) pColecaoItemQuadroAuxiliar);
  }
  
  public int getColumnCount ()
  {
    return 2;
  }
  
  public int getRowCount ()
  {
    return getColecaoItemQuadroAuxiliar ().recuperarLista ().size ();
  }
  
  public String getColumnName (int column)
  {
    switch (column)
      {
      case 0:
	return "Especifica\u00e7\u00e3o";
      case 1:
	return "Valor";
      default:
	return "";
      }
  }
  
  public boolean isCellEditable (int rowIndex, int columnIndex)
  {
    return false;
  }
  
  public ColecaoItemQuadroAuxiliar getColecaoItemQuadroAuxiliar ()
  {
    return (ColecaoItemQuadroAuxiliar) getObjetoNegocio ();
  }
  
  public Informacao getInformacaoAt (int row, int col)
  {
    ItemQuadroAuxiliar itemQuadroAuxiliar = (ItemQuadroAuxiliar) getColecaoItemQuadroAuxiliar ().recuperarLista ().get (row);
    switch (col)
      {
      case 0:
	return itemQuadroAuxiliar.getEspecificacao ();
      case 1:
	return itemQuadroAuxiliar.getValor ();
      default:
	return null;
      }
  }
  
  public void setValueAt (Object aValue, int rowIndex, int columnIndex)
  {
    /* empty */
  }
  
  public Object getValueAt (int rowIndex, int columnIndex)
  {
    return getInformacaoAt (rowIndex, columnIndex).getConteudoFormatado ();
  }
}
