/* FabricaTratamentoErro - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.util;
import javax.swing.JFrame;

public abstract class FabricaTratamentoErro
{
  private static TrataErroSistemicoIf trataErroSistemico;
  
  public static void configuraTrataErroSistemico (JFrame framePrincipal)
  {
    trataErroSistemico = new TrataErroSistemicoStandalone (framePrincipal);
  }
  
  public static TrataErroSistemicoIf getTrataErroSistemico ()
  {
    return trataErroSistemico;
  }
}
