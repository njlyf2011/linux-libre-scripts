/* Interleaved2of5 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.barcodedesigners;

public class Interleaved2of5 extends AbstractBarCode2D
{
  private static final String startPattern = "NNNN";
  private static final String stopPattern = "WNN";
  private static final String[] I25Pattern = { "NNWWN", "WNNNW", "NWNNW", "WWNNN", "NNWNW", "WNWNN", "NWWNN", "NNNWW", "WNNWN", "NWNWN" };
  private BarCode2DRendererIf internalRenderer;
  
  public Interleaved2of5 ()
  {
    internalRenderer = new DefaultBarCode2DRenderer ();
  }
  
  public Interleaved2of5 (String s)
  {
    value = s;
    pattern = encodeValue (s);
    internalRenderer = new DefaultBarCode2DRenderer (pattern);
  }
  
  public void setValue (String s)
  {
    value = s;
    pattern = encodeValue (s);
    if (internalRenderer != null)
      internalRenderer.setPattern (pattern);
  }
  
  public BarCode2DRendererIf getRenderer ()
  {
    return internalRenderer;
  }
  
  public void setRenderer (BarCode2DRendererIf r)
  {
    internalRenderer = r;
  }
  
  private String encodeValue (String s)
  {
    if (s.length () == 0 || s.length () % 2 != 0)
      return "";
    char[] c = s.toCharArray ();
    StringBuffer encoded = new StringBuffer ();
    if (autoQuietZones)
      encoded.append ("!");
    if (autoStartStopMarks)
      encoded.append ("NNNN");
    for (int i = 0; i < c.length; i += 2)
      {
	if (! Character.isDigit (c[i]) || ! Character.isDigit (c[i + 1]))
	  return "";
	String d1 = I25Pattern[c[i] - '0'];
	String d2 = I25Pattern[c[i + 1] - '0'];
	for (int n = 0; n <= 4; n++)
	  {
	    encoded.append (d1.charAt (n));
	    encoded.append (d2.charAt (n));
	  }
      }
    if (autoStartStopMarks)
      encoded.append ("WNN");
    if (autoQuietZones)
      encoded.append ("!");
    return encoded.toString ();
  }
}
