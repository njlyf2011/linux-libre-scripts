/* TableGCapMERendReais - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.moedaestrangeira.rendreais;
import serpro.ppgd.irpf.gui.ganhosdecapital.TableGCap;
import serpro.ppgd.irpf.moedaestrangeira.rendreais.ColecaoRendReais;

public class TableGCapMERendReais extends TableGCap
{
  protected void iniciaModelVazio ()
  {
    setModel (new TableModelGCapMERendReais (new ColecaoRendReais ()));
  }
}
