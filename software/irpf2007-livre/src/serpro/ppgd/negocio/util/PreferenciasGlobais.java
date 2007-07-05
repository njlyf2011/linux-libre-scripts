/* PreferenciasGlobais - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.util;
import java.util.prefs.Preferences;

public class PreferenciasGlobais
{
  private static Preferences prefs;
  
  public static void put (String aChave, String aValor)
  {
    prefs.put (aChave, aValor);
  }
  
  public static String get (String aChave)
  {
    return prefs.get (aChave, null);
  }
  
  public static void putBoolean (String aChave, boolean aBool)
  {
    prefs.putBoolean (aChave, aBool);
  }
  
  public static boolean getBoolean (String aChave, boolean aDefault)
  {
    return prefs.getBoolean (aChave, aDefault);
  }
  
  
  static
  {
    prefs = Preferences.userNodeForPackage (serpro.ppgd.negocio.util.PreferenciasGlobais.class);
  }
}
