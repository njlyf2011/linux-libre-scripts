/* TableModelGCapMERendReaisME - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.moedaestrangeira.rendreaisme;
import serpro.ppgd.irpf.gui.ganhosdecapital.TableModelGCap;
import serpro.ppgd.irpf.moedaestrangeira.rendreaisme.ColecaoRendReaisMe;

public class TableModelGCapMERendReaisME extends TableModelGCap
{
  public TableModelGCapMERendReaisME (ColecaoRendReaisMe pObj)
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
	return "Rela\u00e7\u00e3o de bens, direitos ou aplica\u00e7\u00f5es financeiras";
      case 2:
	return "CPF";
      default:
	return "";
      }
  }
}
