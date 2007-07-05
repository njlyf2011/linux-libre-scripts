/* InfUtil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import java.net.URL;
import java.net.URLClassLoader;

public class InfUtil
{
  public static Class carregaClassesDinamicamente (Class pBaseClass, String pClassPath, String pNomeClasseCompleto)
  {
    Class var_class;
    try
      {
	URL url = pBaseClass.getResource (pClassPath);
	URL[] urls = { url };
	ClassLoader cl = new URLClassLoader (urls);
	Class cls = cl.loadClass (pNomeClasseCompleto);
	var_class = cls;
      }
    catch (ClassNotFoundException e)
      {
	e.printStackTrace ();
	return null;
      }
    return var_class;
  }
}
