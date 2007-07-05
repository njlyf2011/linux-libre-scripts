/* AbstractBarCode2DRenderer - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.barcodedesigners;
import java.awt.Graphics;
import java.awt.Point;

public abstract class AbstractBarCode2DRenderer implements BarCode2DRendererIf
{
  protected String pattern;
  protected int quietZoneWidth;
  public static final int DEFAULT_BARCODE_HEIGHT = 25;
  public static final int DEFAULT_QUIETZONE_WIDTH = 22;
  public static final int WIDE_ELEMENT_WIDTH = 4;
  public static final int NARROW_ELEMENT_WIDTH = 2;
  
  public AbstractBarCode2DRenderer ()
  {
    pattern = "";
    quietZoneWidth = 22;
  }
  
  public AbstractBarCode2DRenderer (String t)
  {
    if (! invalidPattern (t))
      pattern = t;
    else
      pattern = "";
    quietZoneWidth = 22;
  }
  
  public AbstractBarCode2DRenderer (String t, int qzw)
  {
    if (! invalidPattern (t))
      pattern = t;
    else
      pattern = "";
    quietZoneWidth = qzw;
  }
  
  public abstract void render (Graphics graphics, int i, int i_0_, int i_1_);
  
  public void render (Graphics g, Point p, int height)
  {
    render (g, p.x, p.y, height);
  }
  
  public void render (Graphics g, int x, int y)
  {
    render (g, x, y, 25);
  }
  
  public void render (Graphics g, Point p)
  {
    render (g, p.x, p.y, 25);
  }
  
  public void setQuietZoneWidth (int qzw)
  {
    quietZoneWidth = qzw;
  }
  
  public int getQuietZoneWidth ()
  {
    return quietZoneWidth;
  }
  
  public void setPattern (String t)
  {
    if (! invalidPattern (t))
      pattern = t;
    else
      pattern = "";
  }
  
  private boolean invalidPattern (String t)
  {
    if (t == null)
      return true;
    if (t.length () == 0)
      return true;
    for (int i = 0; i < t.length (); i++)
      {
	switch (t.charAt (i))
	  {
	  case '!':
	  case 'N':
	  case 'W':
	    break;
	  default:
	    return true;
	  }
      }
    return false;
  }
}
