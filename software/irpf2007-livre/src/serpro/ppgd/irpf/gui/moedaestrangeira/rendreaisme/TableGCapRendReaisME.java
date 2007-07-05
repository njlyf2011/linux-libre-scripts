/* TableGCapRendReaisME - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.moedaestrangeira.rendreaisme;
import serpro.ppgd.irpf.gui.ganhosdecapital.TableGCap;
import serpro.ppgd.irpf.moedaestrangeira.rendreaisme.ColecaoRendReaisMe;

public class TableGCapRendReaisME extends TableGCap
{
  protected void iniciaModelVazio ()
  {
    setModel (new TableModelGCapMERendReaisME (new ColecaoRendReaisMe ()));
  }
}
