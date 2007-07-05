/* DocumentoAjusteTXT - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.txt.gravacaorestauracao;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import serpro.hash.Crc32;
import serpro.ppgd.formatosexternos.txt.DocumentoTXT;
import serpro.ppgd.formatosexternos.txt.RegistroTxt;
import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.negocio.util.UtilitariosArquivo;
import serpro.ppgd.negocio.util.UtilitariosString;
import serpro.util.PLong;

public class DocumentoAjusteTXT extends DocumentoTXT
{
  private static final Logger logger = Logger.getLogger ("srf.irpf.importacaoGravacao.ImportaGravaAjusteAnua.DocumentoTXT");
  private RegistroTxt regTotal;
  private ConversorRegistros2ObjetosIRPF conversor2ObjetosIRPF = new ConversorRegistros2ObjetosIRPF ();
  
  public DocumentoAjusteTXT (String tipoArquivo, String path) throws GeracaoTxtException
  {
    super (tipoArquivo, path);
    if (tipoArquivo.equals ("ARQ_IRPF"))
      {
	regTotal = new RegistroTxt (tipoArquivo, "T9");
	regTotal.fieldByName ("NR_REG").set ("T9");
	regTotal.fieldByName ("QT_TOTAL").set (1);
	Iterator iter = ConstantesRepositorio.recuperarRegistrosDeclaracao ().iterator ();
	while (iter.hasNext ())
	  {
	    String elemento = (String) iter.next ();
	    regTotal.fieldByName (elemento).set (0);
	  }
      }
  }
  
  private String calculaCRCDeclaracao (String strLinha, long lHashCode)
  {
    PLong pLong = new PLong ();
    Crc32 crc32 = new Crc32 ();
    pLong.setValue (lHashCode);
    long lret = crc32.CalcCrc32 (strLinha, strLinha.length (), pLong);
    return crc32.getStrCrc32 ();
  }
  
  private void totalizarLinhas (String tipo, int qt) throws GeracaoTxtException
  {
    String campo = "QT_R";
    if (! tipo.equals ("IR"))
      {
	regTotal.fieldByName (campo + tipo).set (regTotal.fieldByName (campo + tipo).asInteger () + qt);
	regTotal.fieldByName ("QT_TOTAL").set (regTotal.fieldByName ("QT_TOTAL").asInteger () + qt);
      }
  }
  
  protected Vector transformarRegistroTXTEmObjDaDeclaracao (Vector linhas, String tipoArquivo, String tipoReg) throws GeracaoTxtException
  {
    Vector ficha = new Vector ();
    for (int i = 0; i < linhas.size (); i++)
      {
	RegistroTxt objRegGeracao = new RegistroTxt (tipoArquivo, tipoReg);
	objRegGeracao.setLinha ((String) linhas.elementAt (i));
	if (objRegGeracao.getTipo () != "IR" && objRegGeracao.getTipo () != "IR" && ! tipoArquivo.equals ("ARQ_IRPFANOANTERIOR") && ! tipoArquivo.equals ("ARQ_COMPLRECIBO"))
	  objRegGeracao.validar ();
	ficha.addElement (objRegGeracao);
      }
    return ficha;
  }
  
  protected Vector transformarObjDaDeclaracaoEmRegistroTXT (Vector ficha) throws GeracaoTxtException
  {
    Vector linhas = new Vector ();
    for (int i = 0; i < ficha.size (); i++)
      {
	RegistroTxt objRegGeracao = (RegistroTxt) ficha.elementAt (i);
	if (objRegGeracao.getTipo () != "IR")
	  objRegGeracao.calculaCRCRegistro ();
	if (objRegGeracao.opcional ())
	  {
	    if (objRegGeracao.estaPreenchido ())
	      linhas.add (objRegGeracao.getLinha ());
	  }
	else
	  linhas.add (objRegGeracao.getLinha ());
      }
    return linhas;
  }
  
  public void setFicha (Vector ficha) throws GeracaoTxtException
  {
    String ultRegistro = "";
    int numRegistros = 0;
    Vector linhas = transformarObjDaDeclaracaoEmRegistroTXT (ficha);
    if (linhas.size () > 0)
      {
	incluirLinhas (linhas);
	for (int i = 0; i < linhas.size (); i++)
	  {
	    ultRegistro = ((String) linhas.elementAt (i)).substring (0, 2);
	    numRegistros++;
	    if (i >= linhas.size () | ultRegistro != ((String) linhas.elementAt (i)).substring (0, 2))
	      {
		totalizarLinhas (ultRegistro, numRegistros);
		numRegistros = 0;
	      }
	  }
      }
  }
  
  public void atualizarNrReciboTransmitida (String nrRecibo) throws GeracaoTxtException, IOException
  {
    RegistroTxt regHeader = (RegistroTxt) getRegistrosTxt ("IR").get (0);
    regHeader.fieldByName ("NR_RECIBO_DECLARACAO_TRANSMITIDA").set (nrRecibo);
    String filename = preparaNomeArquivoParaCrc ();
    regHeader.fieldByName ("NR_CONTROLE").set (regHeader.calculaCRCHeader (filename));
    arquivo ().set (0, UtilitariosString.retiraCaracteresEspeciais (regHeader.getLinha ()));
    salvar ();
  }
  
  public void atualizaHeader (Vector header, String hash) throws GeracaoTxtException
  {
    RegistroTxt objReg = (RegistroTxt) getRegistrosTxt ("IR").get (0);
    objReg.fieldByName ("NR_HASH").set (hash);
    String filename = preparaNomeArquivoParaCrc ();
    objReg.fieldByName ("NR_CONTROLE").set (objReg.calculaCRCHeader (filename));
    arquivo ().set (0, UtilitariosString.retiraCaracteresEspeciais (objReg.getLinha ()));
  }
  
  public void validarHeader (IdentificadorDeclaracao objId) throws GeracaoTxtException
  {
    RegistroTxt objRegTXT = new RegistroTxt (getTipoArquivo (), "IR");
    objRegTXT.setLinha ((String) arquivo ().get (0));
    if (! objId.getCpf ().asString ().equals (objRegTXT.fieldByName ("NR_CPF").asString ()))
      throw new GeracaoTxtException ("Declara\u00e7\u00e3o n\u00e3o pertence ao CPF " + objId.getCpf () + ". Opera\u00e7\u00e3o cancelada.");
    String hashLido = objRegTXT.fieldByName ("NR_CONTROLE").asString ();
    String filename = preparaNomeArquivoParaCrc ();
    String hashCalc = objRegTXT.calculaCRCHeader (filename);
    if (! hashCalc.equals (hashLido))
      throw new GeracaoTxtException ("N\u00famero de controle do Header inv\u00e1lido.");
    if (! "IRPF".equals (objRegTXT.fieldByName ("SISTEMA").asString ().trim ()))
      throw new GeracaoTxtException ("Arquivo n\u00e3o \u00e9 declara\u00e7\u00e3o IRPF. Opera\u00e7\u00e3o cancelada.");
  }
  
  public void validarHeaderAnoAnt (IdentificadorDeclaracao objId, boolean validaHash) throws GeracaoTxtException
  {
    RegistroTxt objRegTXT = new RegistroTxt (getTipoArquivo (), "IR");
    objRegTXT.setLinha ((String) arquivo ().get (0));
    if (! objId.getCpf ().asString ().equals (objRegTXT.fieldByName ("NR_CPF").asString ()))
      throw new GeracaoTxtException ("Declara\u00e7\u00e3o n\u00e3o pertence ao CPF " + objId.getCpf () + ". Opera\u00e7\u00e3o cancelada.");
    if (validaHash)
      {
	String hashLido = objRegTXT.fieldByName ("NR_CONTROLE").asString ();
	String filename = preparaNomeArquivoParaCrc ();
	String hashCalc = objRegTXT.calculaCRCHeader (filename);
	if (! hashCalc.equals (hashLido))
	  throw new GeracaoTxtException ("As informa\u00e7\u00f5es do Header da declara\u00e7\u00e3o foram corrompidas ap\u00f3s sua grava\u00e7\u00e3o.");
      }
    if (! "IRPF".equals (objRegTXT.fieldByName ("SISTEMA").asString ()))
      throw new GeracaoTxtException ("Arquivo n\u00e3o \u00e9 declara\u00e7\u00e3o IRPF.");
  }
  
  public void validarComplRecibo (IdentificadorDeclaracao objId) throws GeracaoTxtException
  {
    int posVetor = 0;
    RegistroTxt objRegTXT = new RegistroTxt (getTipoArquivo (), "HC");
    objRegTXT.setLinha ((String) arquivo ().elementAt (posVetor));
    if (! objId.getCpf ().asString ().equals (objRegTXT.fieldByName ("NR_CPF").asString ()))
      throw new GeracaoTxtException ("Recibo n\u00e3o pertence ao CPF " + objId.getCpf () + ".");
    posVetor++;
    objRegTXT = new RegistroTxt (getTipoArquivo (), "RC");
    objRegTXT.setLinha ((String) arquivo ().elementAt (posVetor));
    if (! objId.getCpf ().asString ().equals (objRegTXT.fieldByName ("NR_CPF").asString ()))
      throw new GeracaoTxtException ("Recibo n\u00e3o pertence ao CPF " + objId.getCpf () + ".");
    posVetor++;
    if (arquivo ().size () > 3)
      {
	objRegTXT = new RegistroTxt (getTipoArquivo (), "NC");
	objRegTXT.setLinha ((String) arquivo ().elementAt (posVetor));
	posVetor++;
      }
    objRegTXT = new RegistroTxt (getTipoArquivo (), "TC");
    objRegTXT.setLinha ((String) arquivo ().elementAt (posVetor));
    if (! objId.getCpf ().asString ().equals (objRegTXT.fieldByName ("NR_CPF").asString ()))
      throw new GeracaoTxtException ("Recibo n\u00e3o pertence ao CPF " + objId.getCpf () + ".");
  }
  
  public void validarCRC () throws GeracaoTxtException
  {
    PLong pLong = new PLong ();
    Crc32 crc32 = new Crc32 ();
    for (int i = 1; i < arquivo ().size () - 1; i++)
      {
	String linha = (String) arquivo ().elementAt (i);
	String hashLido = linha.substring (linha.length () - 10);
	StringBuffer sbLinha = new StringBuffer ("");
	sbLinha.append (UtilitariosString.retiraCaracteresEspeciais (linha.substring (0, linha.length () - 10)));
	long hash = crc32.CalcCrc32 (sbLinha.toString (), sbLinha.toString ().length (), pLong);
	String hashCalculado = crc32.getStrCrc32 ();
	if (! hashLido.equals (hashCalculado))
	  throw new GeracaoTxtException ("As informa\u00e7\u00f5es da declara\u00e7\u00e3o foram corrompidas ap\u00f3s sua grava\u00e7\u00e3o, no registro " + sbLinha.toString ().substring (0, 2) + ".");
      }
  }
  
  public void validarCRCAcumulado () throws GeracaoTxtException
  {
    PLong pLong = new PLong ();
    Crc32 crc32 = new Crc32 ();
    String hashCalculado = null;
    long hashCalculadoLinhaAnterior = 0L;
    for (int i = 0; i <= arquivo ().size () - 1; i++)
      {
	String linha = (String) arquivo ().elementAt (i);
	String hashLido = linha.substring (linha.length () - 10);
	StringBuffer sbLinha = new StringBuffer ("");
	sbLinha.append (linha.substring (0, linha.length () - 10));
	if (hashCalculadoLinhaAnterior != 0L)
	  pLong.setValue (hashCalculadoLinhaAnterior);
	long hash = crc32.CalcCrc32 (sbLinha.toString (), sbLinha.toString ().length (), pLong);
	hashCalculadoLinhaAnterior = hash;
	hashCalculado = crc32.getStrCrc32 ();
	if (! hashLido.trim ().equals (hashCalculado.trim ()))
	  throw new GeracaoTxtException ("As informa\u00e7\u00f5es do recibo foram corrompidas ap\u00f3s sua grava\u00e7\u00e3o, no registro " + sbLinha.toString ().substring (0, 2) + ".");
	if (i == arquivo ().size () - 1 && ! linha.startsWith ("TC"))
	  throw new GeracaoTxtException ("O \u00faltimo registro do complemento de recibo, n\u00e3o \u00e9 o registro Trailler.");
      }
  }
  
  public void incluirTrailler (String cpf) throws GeracaoTxtException
  {
    regTotal.fieldByName ("NR_CPF").set (cpf);
    regTotal.calculaCRCRegistro ();
    arquivo ().add (regTotal.getLinha ());
  }
  
  public void incluirRecibo (Vector ficha, String hash) throws GeracaoTxtException
  {
    long lCRCDeclaracao;
    try
      {
	lCRCDeclaracao = Long.parseLong (hash);
      }
    catch (NumberFormatException e)
      {
	lCRCDeclaracao = 0L;
      }
    RegistroTxt objRegGeracao = (RegistroTxt) ficha.get (0);
    objRegGeracao.setLinha (UtilitariosString.retiraCaracteresEspeciais (objRegGeracao.getLinha ()));
    objRegGeracao.calculaCRCRegistro ();
    hash = calculaCRCDeclaracao (objRegGeracao.getLinha (), lCRCDeclaracao);
    arquivo ().add (objRegGeracao.getLinha ());
    try
      {
	lCRCDeclaracao = Long.parseLong (hash);
      }
    catch (NumberFormatException e)
      {
	lCRCDeclaracao = 0L;
      }
    objRegGeracao = (RegistroTxt) ficha.get (1);
    objRegGeracao.setLinha (UtilitariosString.retiraCaracteresEspeciais (objRegGeracao.getLinha ()));
    objRegGeracao.calculaCRCRegistro ();
    hash = calculaCRCDeclaracao (objRegGeracao.getLinha (), lCRCDeclaracao);
    arquivo ().add (objRegGeracao.getLinha ());
    objRegGeracao = (RegistroTxt) ficha.get (2);
    objRegGeracao.setLinha (UtilitariosString.retiraCaracteresEspeciais (objRegGeracao.getLinha ()));
    objRegGeracao.fieldByName ("NR_HASH").set (hash);
    objRegGeracao.calculaCRCRegistro ();
    arquivo ().add (objRegGeracao.getLinha ());
  }
  
  public String calcularHash ()
  {
    String hashAcumulado = "";
    long lCRCDeclaracao = 0L;
    for (int i = 1; i < arquivo ().size (); i++)
      {
	String strLinha = (String) arquivo ().get (i);
	try
	  {
	    lCRCDeclaracao = Long.parseLong (hashAcumulado);
	  }
	catch (NumberFormatException e)
	  {
	    lCRCDeclaracao = 0L;
	  }
	hashAcumulado = calculaCRCDeclaracao (strLinha, lCRCDeclaracao);
      }
    return hashAcumulado;
  }
  
  private String preparaNomeArquivoParaCrc ()
  {
    String filenameTemp = UtilitariosArquivo.extraiNomeArquivo (getPath ()).toUpperCase ();
    String filename = UtilitariosArquivo.extraiNomeAquivoSemExtensao (filenameTemp);
    if (filename.length () > 8)
      filename = filename.substring (0, 8);
    filename = filename.concat (UtilitariosArquivo.extraiExtensaoAquivo (filenameTemp));
    return filename;
  }
  
  public static void main (String[] args)
  {
    try
      {
	File file = new File ("C:/Documents and Settings/messiasl/Desktop/Decs/00000481513-IRPF-2005-2004-ORIGI.REC");
	DocumentoAjusteTXT doc = new DocumentoAjusteTXT ("ARQ_COMPLRECIBO", file.getPath ());
	doc.ler ();
	doc.validarCRCAcumulado ();
	System.out.println ("OK");
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
}
