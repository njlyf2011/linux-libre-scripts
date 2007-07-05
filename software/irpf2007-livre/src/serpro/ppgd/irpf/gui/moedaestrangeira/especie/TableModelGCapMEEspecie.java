/* TableModelGCapMEEspecie - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.moedaestrangeira.especie;
import serpro.ppgd.irpf.gui.ganhosdecapital.TableModelGCap;
import serpro.ppgd.irpf.moedaestrangeira.especie.ColecaoEspecie;

public class TableModelGCapMEEspecie extends TableModelGCap
{
  public TableModelGCapMEEspecie (ColecaoEspecie pObj)
  {
    super ((serpro.ppgd.negocio.ObjetoNegocio) pObj);
  }
  
  public String getColumnName (int column)
  {
    switch (column)
      {
      case 0:
	return "Item";
      case 1:
	return "Rela\u00e7\u00e3o de moedas alienadas";
      case 2:
	return "CPF";
      default:
	return "";
      }
  }
}
