/* TableModelQuadroAuxiliarLucrosDividendos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import serpro.ppgd.irpf.ColecaoItemQuadroLucrosDividendos;
import serpro.ppgd.irpf.ItemQuadroLucrosDividendos;
import serpro.ppgd.negocio.Informacao;

public class TableModelQuadroAuxiliarLucrosDividendos extends IRPFTableModel
{
  public TableModelQuadroAuxiliarLucrosDividendos (ColecaoItemQuadroLucrosDividendos pColecaoItemQuadroAuxiliar)
  {
    super ((serpro.ppgd.negocio.ObjetoNegocio) pColecaoItemQuadroAuxiliar);
  }
  
  public int getColumnCount ()
  {
    return 4;
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
	return "Benefici\u00e1rio";
      case 1:
	return "CNPJ";
      case 2:
	return "Fonte pagadora";
      case 3:
	return "Valor";
      default:
	return "";
      }
  }
  
  public boolean isCellEditable (int rowIndex, int columnIndex)
  {
    return false;
  }
  
  public ColecaoItemQuadroLucrosDividendos getColecaoItemQuadroAuxiliar ()
  {
    return (ColecaoItemQuadroLucrosDividendos) getObjetoNegocio ();
  }
  
  public Informacao getInformacaoAt (int row, int col)
  {
    ItemQuadroLucrosDividendos itemQuadroAuxiliar = (ItemQuadroLucrosDividendos) getColecaoItemQuadroAuxiliar ().recuperarLista ().get (row);
    switch (col)
      {
      case 0:
	return itemQuadroAuxiliar.getTipo ();
      case 1:
	return itemQuadroAuxiliar.getCnpjEmpresa ();
      case 2:
	return itemQuadroAuxiliar.getNomeFonte ();
      case 3:
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
