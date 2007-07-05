/* BarcodeEncoder - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.barcode;
import serpro.ppgd.formatosexternos.barcode.util.CalculosGenericos;
import serpro.ppgd.formatosexternos.barcode.util.DarfAdaptor;

public class BarcodeEncoder
{
  private String[] grupos = new String[4];
  private String numero = "";
  
  public static BarcodeEncoder gerarCodigo (DarfAdaptor dados)
  {
    BarcodeEncoder barcodeEncoder = new BarcodeEncoder ();
    StringBuffer sbBarra = new StringBuffer ();
    String valor = dados.getValorPrincipal ();
    valor = CalculosGenericos.limpaFormatoValor (dados.getValorPrincipal ());
    valor = CalculosGenericos.preencheZeros (valor, 11);
    sbBarra.append (dados.getNumeroReferencia ().substring (0, 3));
    sbBarra.append (' ');
    sbBarra.append (valor.trim ());
    sbBarra.append (dados.getCodigoEmpresa ().substring (0, 4));
    sbBarra.append (dados.getDataVencimentoJuliana ());
    sbBarra.append (dados.getTipoDocumento ());
    sbBarra.append (dados.getCpfCnpjCalculado ().substring (0, 12));
    sbBarra.append (dados.getCodigoReceita ().substring (0, 4));
    sbBarra.append (dados.getPeriodoApuracaoJuliana ());
    StringBuffer sbTempCalcDv = new StringBuffer ();
    sbTempCalcDv.append (sbBarra.substring (0, 3));
    sbTempCalcDv.append (sbBarra.substring (4, 44));
    int dv = CalculosGenericos.calcularDVMod10 (sbTempCalcDv.substring (0, 43));
    sbBarra.replace (3, 4, String.valueOf (dv));
    barcodeEncoder.numero = sbBarra.toString ();
    int idxStart = 0;
    int idxEnd = 11;
    for (int i = 0; i < barcodeEncoder.grupos.length; i++)
      {
	sbTempCalcDv = new StringBuffer (sbBarra.substring (idxStart, idxEnd));
	dv = CalculosGenericos.calcularDVMod10 (sbTempCalcDv.toString ());
	barcodeEncoder.grupos[i] = new String (sbTempCalcDv.append ('-').append (dv));
	idxStart = idxEnd;
	idxEnd += 11;
      }
    return barcodeEncoder;
  }
  
  public String getNumero ()
  {
    return numero;
  }
  
  public String[] getGrupos ()
  {
    return grupos;
  }
}
