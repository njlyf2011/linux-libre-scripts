/* RepositorioTxtDadosCarneLeao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.txt.importacao.carneleao;
import java.io.IOException;
import java.util.Iterator;

import serpro.hash.Crc32;
import serpro.ppgd.formatosexternos.txt.DocumentoTXT;
import serpro.ppgd.formatosexternos.txt.DocumentoTXTDefault;
import serpro.ppgd.formatosexternos.txt.RegistroTxt;
import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.rendpf.MesRendPF;
import serpro.ppgd.irpf.rendpf.RendPF;
import serpro.ppgd.negocio.Valor;
import serpro.util.PLong;

public class RepositorioTxtDadosCarneLeao
{
  private String pathArquivo = null;
  private DocumentoTXT documentoTXT = null;
  
  public RepositorioTxtDadosCarneLeao (String _pathArquivo)
  {
    pathArquivo = _pathArquivo;
    documentoTXT = new DocumentoTXTDefault ("ARQ_IMPORTACAO_CARNELEAO", pathArquivo);
  }
  
  public void importaDados () throws GeracaoTxtException, IOException
  {
    documentoTXT.ler ();
    verificaCRC ();
    Iterator it = null;
    it = documentoTXT.getRegistrosTxt ("02").iterator ();
    while (it.hasNext ())
      {
	RegistroTxt reg = (RegistroTxt) it.next ();
	String strMes = reg.fieldByName ("Mes").asString ().trim ();
	MesRendPF mes = getMesRendPF (strMes);
	Valor totalPF = new Valor ();
	totalPF.append ('+', reg.fieldByName ("Trabalho Nao Assalariado").asValor ());
	totalPF.append ('+', reg.fieldByName ("Alugueis").asValor ());
	totalPF.append ('+', reg.fieldByName ("Outros").asValor ());
	mes.getPessoaFisica ().setConteudo (totalPF);
	mes.getExterior ().setConteudo (reg.fieldByName ("Exterior").asValor ());
	mes.getPrevidencia ().setConteudo (reg.fieldByName ("previdenciaOficial").asValor ());
	mes.getDependentes ().setConteudo (reg.fieldByName ("dependentes").asValor ());
	mes.getPensao ().setConteudo (reg.fieldByName ("pensaoAlimenticia").asValor ());
	mes.getLivroCaixa ().setConteudo (reg.fieldByName ("livroCaixa").asValor ());
	mes.getCarneLeao ().setConteudo (reg.fieldByName ("impostoAPagar").asValor ());
	mes.getDarfPago ().setConteudo (reg.fieldByName ("impostoPago").asValor ());
      }
  }
  
  private MesRendPF getMesRendPF (String mes)
  {
    mes = mes.toUpperCase ();
    RendPF rendPF = IRPFFacade.getInstancia ().getRendPFTitular ();
    if (mes.equals ("JANEIRO"))
      return rendPF.getJaneiro ();
    if (mes.equals ("FEVEREIRO"))
      return rendPF.getFevereiro ();
    if (mes.equals ("MARCO"))
      return rendPF.getMarco ();
    if (mes.equals ("ABRIL"))
      return rendPF.getAbril ();
    if (mes.equals ("MAIO"))
      return rendPF.getMaio ();
    if (mes.equals ("JUNHO"))
      return rendPF.getJunho ();
    if (mes.equals ("JULHO"))
      return rendPF.getJulho ();
    if (mes.equals ("AGOSTO"))
      return rendPF.getAgosto ();
    if (mes.equals ("SETEMBRO"))
      return rendPF.getSetembro ();
    if (mes.equals ("OUTUBRO"))
      return rendPF.getOutubro ();
    if (mes.equals ("NOVEMBRO"))
      return rendPF.getNovembro ();
    if (mes.equals ("DEZEMBRO"))
      return rendPF.getDezembro ();
    return null;
  }
  
  public void verificaCRC () throws GeracaoTxtException
  {
    PLong pLongAcumulado = new PLong ();
    Crc32 crc32Acumulado = new Crc32 ();
    long hashCalculadoLinhaAnterior = 0L;
    for (int i = 0; i < documentoTXT.arquivo ().size () - 2; i++)
      {
	String linha = (String) documentoTXT.arquivo ().elementAt (i);
	if (hashCalculadoLinhaAnterior != 0L)
	  pLongAcumulado.setValue (hashCalculadoLinhaAnterior);
	hashCalculadoLinhaAnterior = crc32Acumulado.CalcCrc32 (linha, linha.length (), pLongAcumulado);
      }
    String crcAcumuladoFinal = String.valueOf (hashCalculadoLinhaAnterior);
    RegistroTxt reg = (RegistroTxt) documentoTXT.getRegistrosTxt ("99").get (0);
    String crcLido = reg.fieldByName ("HASHCODE").asString ();
    if (! crcLido.equals (crcAcumuladoFinal))
      throw new GeracaoTxtException ("Arquivo corrompido");
  }
}
