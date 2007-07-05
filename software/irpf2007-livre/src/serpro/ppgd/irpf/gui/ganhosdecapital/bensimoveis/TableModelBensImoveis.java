/* TableModelBensImoveis - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.ganhosdecapital.bensimoveis;
import serpro.ppgd.irpf.ganhosdecapital.bensimoveis.ColecaoBensImoveis;
import serpro.ppgd.irpf.gui.ganhosdecapital.TableModelGCap;

public class TableModelBensImoveis extends TableModelGCap
{
  public TableModelBensImoveis (ColecaoBensImoveis pObj)
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
	return "Rela\u00e7\u00e3o de bens im\u00f3veis alienados";
      case 2:
	return "CPF";
      default:
	return "";
      }
  }
}
