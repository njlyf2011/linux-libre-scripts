/* TableBensImoveis - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.ganhosdecapital.bensimoveis;
import serpro.ppgd.irpf.ganhosdecapital.bensimoveis.ColecaoBensImoveis;
import serpro.ppgd.irpf.gui.ganhosdecapital.TableGCap;

public class TableBensImoveis extends TableGCap
{
  protected void iniciaModelVazio ()
  {
    setModel (new TableModelBensImoveis (new ColecaoBensImoveis ()));
  }
}
