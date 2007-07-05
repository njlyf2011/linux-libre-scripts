/* TrataErroSistemicoStandalone - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.util;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TrataErroSistemicoStandalone implements TrataErroSistemicoIf
{
  JFrame framePrincipal = null;
  private static final String MSG_ERRO = "Um componente cr\u00edtico n\u00e3o foi localizado. A aplica\u00e7\u00e3o ser\u00e1 finalizada.\n\nMaiores informa\u00e7\u00f5es:";
  
  public TrataErroSistemicoStandalone (JFrame parent)
  {
    framePrincipal = parent;
  }
  
  public void trataErroSistemico (Throwable e)
  {
    StackTraceElement[] stack = e.getStackTrace ();
    String msgStack = "";
    for (int i = 0; i < stack.length; i++)
      {
	msgStack += "\nClasse:" + stack[i].getClassName () + ", M\u00e9todo:" + stack[i].getMethodName () + ", Linha:" + stack[i].getLineNumber ();
	if (i > 5)
	  break;
      }
    msgStack += ".";
    JOptionPane.showMessageDialog (framePrincipal, "Um componente cr\u00edtico n\u00e3o foi localizado. A aplica\u00e7\u00e3o ser\u00e1 finalizada.\n\nMaiores informa\u00e7\u00f5es:" + e.getClass () + msgStack, "Erro Fatal", 0);
    System.exit (1);
  }
  
  public void trataErroSistemico (String msg)
  {
    JOptionPane.showMessageDialog (framePrincipal, "Um componente cr\u00edtico n\u00e3o foi localizado. A aplica\u00e7\u00e3o ser\u00e1 finalizada.\n\nMaiores informa\u00e7\u00f5es:" + msg, "Erro Fatal", 0);
    System.exit (1);
  }
}
