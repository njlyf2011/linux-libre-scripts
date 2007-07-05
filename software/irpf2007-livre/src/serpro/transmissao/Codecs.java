/* Codecs - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.transmissao;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

public class Codecs
{
  private static BitSet BoundChar = new BitSet (256);
  private static BitSet EBCDICUnsafeChar;
  private static byte[] Base64EncMap;
  private static byte[] Base64DecMap;
  private static char[] UUEncMap;
  private static byte[] UUDecMap;
  private static final String ContDisp = "\r\nContent-Disposition: form-data; name=\"";
  private static final String FileName = "\"; filename=\"";
  private static final String ContType = "\r\nContent-Type: ";
  private static final String Boundary = "\r\n----------ieoau._._+2_8_GoodLuck8.3-dskdfJwSJKl234324jfLdsjfdAuaoei-----";
  
  private Codecs ()
  {
    /* empty */
  }
  
  public static final String base64Encode (String string)
  {
    if (string == null)
      return null;
    String string_0_;
    try
      {
	string_0_ = new String (base64Encode (string.getBytes ("8859_1")), "8859_1");
      }
    catch (UnsupportedEncodingException unsupportedencodingexception)
      {
	throw new Error (unsupportedencodingexception.toString ());
      }
    return string_0_;
  }
  
  public static final byte[] base64Encode (byte[] is)
  {
    if (is == null)
      return null;
    byte[] is_1_ = new byte[(is.length + 2) / 3 * 4];
    int i = 0;
    int i_2_ = 0;
    for (/**/; i < is.length - 2; i += 3)
      {
	is_1_[i_2_++] = Base64EncMap[is[i] >>> 2 & 0x3f];
	is_1_[i_2_++] = Base64EncMap[is[i + 1] >>> 4 & 0xf | is[i] << 4 & 0x3f];
	is_1_[i_2_++] = Base64EncMap[is[i + 2] >>> 6 & 0x3 | is[i + 1] << 2 & 0x3f];
	is_1_[i_2_++] = Base64EncMap[is[i + 2] & 0x3f];
      }
    if (i < is.length)
      {
	is_1_[i_2_++] = Base64EncMap[is[i] >>> 2 & 0x3f];
	if (i < is.length - 1)
	  {
	    is_1_[i_2_++] = Base64EncMap[is[i + 1] >>> 4 & 0xf | is[i] << 4 & 0x3f];
	    is_1_[i_2_++] = Base64EncMap[is[i + 1] << 2 & 0x3f];
	  }
	else
	  is_1_[i_2_++] = Base64EncMap[is[i] << 4 & 0x3f];
      }
    for (/**/; i_2_ < is_1_.length; i_2_++)
      is_1_[i_2_] = (byte) 61;
    return is_1_;
  }
  
  public static final String base64Decode (String string)
  {
    if (string == null)
      return null;
    String string_3_;
    try
      {
	string_3_ = new String (base64Decode (string.getBytes ("8859_1")), "8859_1");
      }
    catch (UnsupportedEncodingException unsupportedencodingexception)
      {
	throw new Error (unsupportedencodingexception.toString ());
      }
    return string_3_;
  }
  
  public static final byte[] base64Decode (byte[] is)
  {
    if (is == null)
      return null;
    int i;
    for (i = is.length; is[i - 1] == 61; i--)
      {
	/* empty */
      }
    byte[] is_4_ = new byte[i - is.length / 4];
    for (int i_5_ = 0; i_5_ < is.length; i_5_++)
      is[i_5_] = Base64DecMap[is[i_5_]];
    int i_6_ = 0;
    int i_7_;
    for (i_7_ = 0; i_7_ < is_4_.length - 2; i_7_ += 3)
      {
	is_4_[i_7_] = (byte) (is[i_6_] << 2 & 0xff | is[i_6_ + 1] >>> 4 & 0x3);
	is_4_[i_7_ + 1] = (byte) (is[i_6_ + 1] << 4 & 0xff | is[i_6_ + 2] >>> 2 & 0xf);
	is_4_[i_7_ + 2] = (byte) (is[i_6_ + 2] << 6 & 0xff | is[i_6_ + 3] & 0x3f);
	i_6_ += 4;
      }
    if (i_7_ < is_4_.length)
      is_4_[i_7_] = (byte) (is[i_6_] << 2 & 0xff | is[i_6_ + 1] >>> 4 & 0x3);
    if (++i_7_ < is_4_.length)
      is_4_[i_7_] = (byte) (is[i_6_ + 1] << 4 & 0xff | is[i_6_ + 2] >>> 2 & 0xf);
    return is_4_;
  }
  
  static
  {
    for (int i = 48; i <= 57; i++)
      BoundChar.set (i);
    for (int i = 65; i <= 90; i++)
      BoundChar.set (i);
    for (int i = 97; i <= 122; i++)
      BoundChar.set (i);
    BoundChar.set (43);
    BoundChar.set (95);
    BoundChar.set (45);
    BoundChar.set (46);
    EBCDICUnsafeChar = new BitSet (256);
    EBCDICUnsafeChar.set (33);
    EBCDICUnsafeChar.set (34);
    EBCDICUnsafeChar.set (35);
    EBCDICUnsafeChar.set (36);
    EBCDICUnsafeChar.set (64);
    EBCDICUnsafeChar.set (91);
    EBCDICUnsafeChar.set (92);
    EBCDICUnsafeChar.set (93);
    EBCDICUnsafeChar.set (94);
    EBCDICUnsafeChar.set (96);
    EBCDICUnsafeChar.set (123);
    EBCDICUnsafeChar.set (124);
    EBCDICUnsafeChar.set (125);
    EBCDICUnsafeChar.set (126);
    byte[] is = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
    Base64EncMap = is;
    Base64DecMap = new byte[128];
    for (int i = 0; i < Base64EncMap.length; i++)
      Base64DecMap[Base64EncMap[i]] = (byte) i;
    UUEncMap = new char[64];
    for (int i = 0; i < UUEncMap.length; i++)
      UUEncMap[i] = (char) (i + 32);
    UUDecMap = new byte[128];
    for (int i = 0; i < UUEncMap.length; i++)
      UUDecMap[UUEncMap[i]] = (byte) i;
  }
}
