/* ObservadorHabDesabIrRetidoFonteLei11033 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendavariavel;
import serpro.ppgd.negocio.Observador;

public class ObservadorHabDesabIrRetidoFonteLei11033 extends Observador
{
  private GanhosLiquidosOuPerdas ganhos = null;
  
  public ObservadorHabDesabIrRetidoFonteLei11033 (GanhosLiquidosOuPerdas _ganho)
  {
    ganhos = _ganho;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    /* empty */
  }
}
