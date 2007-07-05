/* BarCodeCanvas2 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.barcodedesigners;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class BarCodeCanvas2 extends JPanel
{
  private BarCode2DIf barCode;
  
  public BarCodeCanvas2 (BarCode2DIf bc)
  {
    setBackground (Color.white);
    barCode = bc;
  }
  
  public void setBarCode2D (BarCode2DIf bc)
  {
    barCode = bc;
    repaint ();
  }
  
  public void paintComponent (Graphics g)
  {
    super.paintComponent (g);
    barCode.getRenderer ().render (g, 10, 10, 25);
  }
}
