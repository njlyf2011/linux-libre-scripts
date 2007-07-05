/* TableModelPSocietarias - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.ganhosdecapital.psocietarias;
import serpro.ppgd.irpf.ganhosdecapital.psocietarias.ColecaoPSocietarias;
import serpro.ppgd.irpf.gui.ganhosdecapital.TableModelGCap;

public class TableModelPSocietarias extends TableModelGCap
{
  public TableModelPSocietarias (ColecaoPSocietarias pObj)
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
	return "Rela\u00e7\u00e3o de participa\u00e7\u00f5es societ\u00e1rias alienadas";
      case 2:
	return "CPF";
      default:
	return "";
      }
  }
}
