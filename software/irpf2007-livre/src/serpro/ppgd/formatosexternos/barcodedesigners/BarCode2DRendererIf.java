/* BarCode2DRendererIf - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.barcodedesigners;
import java.awt.Graphics;

public interface BarCode2DRendererIf
{
  public void setQuietZoneWidth (int i);
  
  public int getQuietZoneWidth ();
  
  public void setPattern (String string);
  
  public void render (Graphics graphics, int i, int i_0_, int i_1_);
  
  public void render (String string, int i, int i_2_, int i_3_);
}
