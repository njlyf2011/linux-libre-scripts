/* TableGCapMERendME - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.moedaestrangeira.rendme;
import serpro.ppgd.irpf.gui.ganhosdecapital.TableGCap;
import serpro.ppgd.irpf.moedaestrangeira.rendme.ColecaoRendME;

public class TableGCapMERendME extends TableGCap
{
  protected void iniciaModelVazio ()
  {
    setModel (new TableModelGCapMERendME (new ColecaoRendME ()));
  }
}
