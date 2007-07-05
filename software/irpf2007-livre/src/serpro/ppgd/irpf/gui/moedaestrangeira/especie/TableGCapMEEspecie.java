/* TableGCapMEEspecie - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.moedaestrangeira.especie;
import serpro.ppgd.irpf.gui.ganhosdecapital.TableGCap;
import serpro.ppgd.irpf.moedaestrangeira.especie.ColecaoEspecie;

public class TableGCapMEEspecie extends TableGCap
{
  protected void iniciaModelVazio ()
  {
    setModel (new TableModelGCapMEEspecie (new ColecaoEspecie ()));
  }
}
