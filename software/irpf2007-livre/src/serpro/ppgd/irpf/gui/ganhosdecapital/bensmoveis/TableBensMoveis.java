/* TableBensMoveis - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.ganhosdecapital.bensmoveis;
import serpro.ppgd.irpf.ganhosdecapital.bensmoveis.ColecaoBensMoveis;
import serpro.ppgd.irpf.gui.ganhosdecapital.TableGCap;

public class TableBensMoveis extends TableGCap
{
  protected void iniciaModelVazio ()
  {
    setModel (new TableModelBensMoveis (new ColecaoBensMoveis ()));
  }
}
