/* TableModelGCapMERendME - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.moedaestrangeira.rendme;
import serpro.ppgd.irpf.gui.ganhosdecapital.TableModelGCap;
import serpro.ppgd.irpf.moedaestrangeira.rendme.ColecaoRendME;

public class TableModelGCapMERendME extends TableModelGCap
{
  public TableModelGCapMERendME (ColecaoRendME pObj)
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
