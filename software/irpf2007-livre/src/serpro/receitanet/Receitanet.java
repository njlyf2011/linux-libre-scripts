/* Receitanet - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.receitanet;
import java.awt.Window;
import java.io.File;

public interface Receitanet
{
  public static final int ENVIO_OK = 0;
  public static final int ENVIO_ERRO = 1;
  public static final int ENVIO_OK_COM_MULTA = 2;
  
  public int enviarDeclaracao (Window window, File file, boolean bool, StringBuffer stringbuffer);
}
