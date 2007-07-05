/* BarCode2DIf - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.barcodedesigners;

public interface BarCode2DIf
{
  public void setValue (String string);
  
  public String getValue ();
  
  public void setAutoStartStopMarks (boolean bool);
  
  public boolean getAutoStartStopMarks ();
  
  public void setAutoQuietZones (boolean bool);
  
  public boolean getAutoQuietZones ();
  
  public void setRenderer (BarCode2DRendererIf barcode2drendererif);
  
  public BarCode2DRendererIf getRenderer ();
}
