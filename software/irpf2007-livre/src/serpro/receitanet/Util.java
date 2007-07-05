/* Util - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.receitanet;
import java.math.BigInteger;

public final class Util
{
  private Util ()
  {
    /* empty */
  }
  
  public static String decodificaControleSRF (byte[] bits)
  {
    if (bits == null)
      throw new NullPointerException ("Campo nulo.");
    if (bits.length != 10)
      throw new IllegalArgumentException ("Campo inv\u00e1lido.");
    BigInteger crc32 = BigInteger.ZERO;
    for (int i = 0; i < 32; i++)
      {
	if (((bits[i % 10] & 1 << i / 10) != 0) == true)
	  crc32 = crc32.setBit (i);
      }
    String stCrc;
    for (stCrc = crc32.toString (); stCrc.length () < 10; stCrc = "0" + stCrc)
      {
	/* empty */
      }
    return stCrc;
  }
}
