/* Barcode - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.barcode;
import serpro.ppgd.formatosexternos.barcodedesigners.Interleaved2of5;

public class Barcode
{
  public static void gerarBarcode (String codigo, String arquivo)
  {
    Interleaved2of5 barCode = new Interleaved2of5 ();
    barCode.setAutoStartStopMarks (true);
    barCode.setValue (codigo);
    serpro.ppgd.formatosexternos.barcodedesigners.BarCode2DIf bar = barCode;
    bar.getRenderer ().render (arquivo, 0, 0, 30);
  }
}
