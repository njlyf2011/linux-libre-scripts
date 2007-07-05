/* AbstractBarCode2D - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.barcodedesigners;

public abstract class AbstractBarCode2D implements BarCode2DIf
{
  protected String value = "";
  protected String pattern = "";
  protected boolean autoQuietZones = true;
  protected boolean autoStartStopMarks = true;
  
  public abstract void setValue (String string);
  
  public abstract BarCode2DRendererIf getRenderer ();
  
  public abstract void setRenderer (BarCode2DRendererIf barcode2drendererif);
  
  public String getValue ()
  {
    return value;
  }
  
  public void setAutoStartStopMarks (boolean b)
  {
    autoStartStopMarks = b;
  }
  
  public boolean getAutoStartStopMarks ()
  {
    return autoStartStopMarks;
  }
  
  public void setAutoQuietZones (boolean b)
  {
    autoQuietZones = b;
  }
  
  public boolean getAutoQuietZones ()
  {
    return autoQuietZones;
  }
}
