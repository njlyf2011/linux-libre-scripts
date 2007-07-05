/* TableModelBensMoveis - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.ganhosdecapital.bensmoveis;
import serpro.ppgd.irpf.ganhosdecapital.bensmoveis.ColecaoBensMoveis;
import serpro.ppgd.irpf.gui.ganhosdecapital.TableModelGCap;

public class TableModelBensMoveis extends TableModelGCap
{
  public TableModelBensMoveis (ColecaoBensMoveis pObj)
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
	return "Rela\u00e7\u00e3o de bens m\u00f3veis alienados";
      case 2:
	return "CPF";
      default:
	return "";
      }
  }
}
