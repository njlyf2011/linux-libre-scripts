/* iCrc32 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.hash;
import serpro.util.PLong;

public interface iCrc32
{
  public long CalcCrc32 (String string, int i, PLong plong);
  
  public String getStrCrc32 ();
}
