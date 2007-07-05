/* ProcessoSwing - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import java.awt.Cursor;

import foxtrot.Worker;

public class ProcessoSwing
{
  private static final Cursor DEFAULT_CURSOR = Cursor.getDefaultCursor ();
  private static final Cursor WAIT_CURSOR = Cursor.getPredefinedCursor (3);
  
  public static Object executa (Tarefa tarefa)
  {
    tarefa.getParent ().setCursor (WAIT_CURSOR);
    Object ret = Worker.post (tarefa);
    tarefa.getParent ().setCursor (DEFAULT_CURSOR);
    return ret;
  }
  
  public static Object executa (TarefaComExcecao tarefa) throws Exception
  {
    tarefa.getParent ().setCursor (WAIT_CURSOR);
    Object ret = Worker.post (tarefa);
    tarefa.getParent ().setCursor (DEFAULT_CURSOR);
    return ret;
  }
}
