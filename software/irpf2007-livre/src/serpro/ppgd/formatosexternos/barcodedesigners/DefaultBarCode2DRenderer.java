/* DefaultBarCode2DRenderer - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.barcodedesigners;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import serpro.ppgd.negocio.util.LogPPGD;

public class DefaultBarCode2DRenderer extends AbstractBarCode2DRenderer
{
  public DefaultBarCode2DRenderer ()
  {
    /* empty */
  }
  
  public DefaultBarCode2DRenderer (String t)
  {
    super (t);
  }
  
  public DefaultBarCode2DRenderer (String t, int qzw)
  {
    super (t, qzw);
  }
  
  public void render (Graphics g1, int x, int y, int height)
  {
    BufferedImage Bi = new BufferedImage (700, 50, 1);
    Graphics g = Bi.getGraphics ();
    boolean bBar = false;
    int iX = x;
    int iW = 0;
    g.setColor (Color.white);
    g.fillRect (0, 0, 700, 50);
    g.setColor (Color.black);
    for (int i = 0; i < pattern.length (); i++)
      {
	switch (pattern.charAt (i))
	  {
	  case '!':
	    iW = quietZoneWidth;
	    bBar = false;
	    break;
	  case 'N':
	    iW = 2;
	    break;
	  case 'W':
	    iW = 4;
	    break;
	  default:
	    return;
	  }
	if (bBar)
	  g.fillRect (iX, y, iW, y + height);
	iX += iW;
	bBar = ! bBar;
      }
    g1 = g;
    try
      {
	JPEGImageEncoder ImgEnc = JPEGCodec.createJPEGEncoder (new FileOutputStream ("e:\\testeElizier.jpg"));
	ImgEnc.encode (Bi);
	ImgEnc.getOutputStream ().close ();
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public void render (String arquivo, int x, int y, int height)
  {
    BufferedImage Bi = new BufferedImage (700, 30, 1);
    Graphics g = Bi.getGraphics ();
    boolean bBar = false;
    int iX = x;
    int iW = 0;
    g.setColor (Color.white);
    g.fillRect (0, 0, 700, 30);
    g.setColor (Color.black);
    for (int i = 0; i < pattern.length (); i++)
      {
	switch (pattern.charAt (i))
	  {
	  case '!':
	    iW = quietZoneWidth;
	    bBar = false;
	    break;
	  case 'N':
	    iW = 2;
	    break;
	  case 'W':
	    iW = 4;
	    break;
	  default:
	    return;
	  }
	if (bBar)
	  g.fillRect (iX, y, iW, y + height);
	iX += iW;
	bBar = ! bBar;
      }
    try
      {
	JPEGImageEncoder ImgEnc = JPEGCodec.createJPEGEncoder (new FileOutputStream (arquivo));
	ImgEnc.encode (Bi);
	ImgEnc.getOutputStream ().close ();
      }
    catch (Exception e)
      {
	LogPPGD.erro ("Exce\u00e7\u00e3o render");
	e.printStackTrace ();
      }
  }
}
