/* TablePSocietarias - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.ganhosdecapital.psocietarias;
import serpro.ppgd.irpf.ganhosdecapital.psocietarias.ColecaoPSocietarias;
import serpro.ppgd.irpf.gui.ganhosdecapital.TableGCap;

public class TablePSocietarias extends TableGCap
{
  protected void iniciaModelVazio ()
  {
    setModel (new TableModelPSocietarias (new ColecaoPSocietarias ()));
  }
}
