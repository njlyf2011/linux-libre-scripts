/* IRPFTxtUtil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.txt.util;
import java.io.File;

import serpro.ppgd.formatosexternos.txt.DocumentoTXTDefault;
import serpro.ppgd.formatosexternos.txt.RegistroTxt;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.txt.gravacaorestauracao.ConstantesRepositorio;
import serpro.ppgd.irpf.txt.gravacaorestauracao.GravadorTXT;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.UtilitariosString;
import serpro.ppgd.negocio.util.Validador;

public class IRPFTxtUtil
{
  static String diretorioCorrente = FabricaUtilitarios.getPathCompletoDirGravadas ();
  
  public static String declaracaoFoiTransmitida (IdentificadorDeclaracao idDecITR)
  {
    File decGeradaEntrega = montaPathTXTDeclaracao (diretorioCorrente, idDecITR, true);
    File decGeradaTransmitida8Digitos = new File (diretorioCorrente + File.separator + idDecITR.getCpf ().asString () + ".REC");
    File decGeradaTransmitida = new File (decGeradaEntrega.toString ().substring (0, decGeradaEntrega.toString ().length () - 4) + ".REC");
    boolean foiTrans = decGeradaEntrega.exists () && (decGeradaTransmitida.exists () || decGeradaTransmitida8Digitos.exists ());
    if (foiTrans)
      {
	try
	  {
	    DocumentoTXTDefault doc = new DocumentoTXTDefault ("ARQ_IRPF", decGeradaEntrega.getPath ());
	    doc.ler ();
	    String numRecibo = ((RegistroTxt) doc.getRegistrosTxt ("IR").elementAt (0)).fieldByName ("NR_HASH").asString ();
	    return (numRecibo + obtemDVNumRecibo (numRecibo)).trim ();
	  }
	catch (Exception e)
	  {
	    e.printStackTrace ();
	    return null;
	  }
      }
    return null;
  }
  
  public static File montaPathTXTDeclaracao (String dirDados, IdentificadorDeclaracao idDec, boolean isParaEntrega)
  {
    File f = new File (dirDados.toString ());
    f.mkdirs ();
    StringBuffer strPath = new StringBuffer (f.getPath ());
    strPath.append (System.getProperty ("file.separator"));
    strPath.append (GravadorTXT.montaNomeArquivoTXT (ConstantesRepositorio.GRAV_GERACAO, idDec));
    f = new File (strPath.toString ());
    return f;
  }
  
  public static String formataNumeroRecibo (String hashCode)
  {
    String hashCodeFormatado = UtilitariosString.formataComPontos (hashCode, 2);
    return hashCodeFormatado;
  }
  
  public static String obtemDVNumRecibo (String numRecibo)
  {
    String DV = "";
    int dv1 = Validador.calcularModulo11 (numRecibo, null, 2);
    DV += dv1;
    int dv2 = Validador.calcularModulo11 (numRecibo + dv1, null, 2);
    DV += dv2;
    return DV;
  }
}
