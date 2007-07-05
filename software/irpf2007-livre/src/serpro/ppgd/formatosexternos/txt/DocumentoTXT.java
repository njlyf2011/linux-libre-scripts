/* DocumentoTXT - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.txt;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.negocio.util.UtilitariosString;

public abstract class DocumentoTXT
{
  private Vector vectorDeclaracao = new Vector ();
  private String fTipoArquivo;
  private boolean fAlterado = true;
  private boolean fBKP = true;
  private String fPath;
  
  public DocumentoTXT (String tipoArquivo, String path)
  {
    fPath = path;
    fTipoArquivo = tipoArquivo;
  }
  
  protected Vector lerLinhas (String tipoReg)
  {
    Vector vetorLinhas = new Vector ();
    for (int i = 0; i < vectorDeclaracao.size (); i++)
      {
	String linha = (String) arquivo ().elementAt (i);
	if (tipoReg.equals ("Default") || tipoReg.trim ().length () == 0 || tipoReg.equals (linha.substring (0, tipoReg.length ())))
	  vetorLinhas.add (linha);
      }
    return vetorLinhas;
  }
  
  protected void incluirLinhas (Vector linhas)
  {
    for (int i = 0; i < linhas.size (); i++)
      arquivo ().add (UtilitariosString.retiraCaracteresEspeciais ((String) linhas.elementAt (i)));
    fAlterado = true;
  }
  
  protected abstract Vector transformarObjDaDeclaracaoEmRegistroTXT (Vector vector) throws GeracaoTxtException;
  
  protected abstract Vector transformarRegistroTXTEmObjDaDeclaracao (Vector vector, String string, String string_0_) throws GeracaoTxtException;
  
  public Vector arquivo ()
  {
    return vectorDeclaracao;
  }
  
  public void setTipoArquivo (String tipoArquivo)
  {
    fTipoArquivo = tipoArquivo;
  }
  
  public String getTipoArquivo ()
  {
    return fTipoArquivo;
  }
  
  public Vector getRegistrosTxt (String tipoReg) throws GeracaoTxtException
  {
    Vector linhas = lerLinhas (tipoReg);
    Vector ficha = transformarRegistroTXTEmObjDaDeclaracao (linhas, getTipoArquivo (), tipoReg);
    return ficha;
  }
  
  public void atualizarRegistros (int pos, RegistroTxt pReg) throws GeracaoTxtException
  {
    arquivo ().set (pos, pReg.getLinha ());
  }
  
  protected void antesDeAtualizarRegistros (String pTipoReg, RegistroTxt pReg)
  {
    /* empty */
  }
  
  public void setObjetosDaDeclaracao (Vector objetos) throws GeracaoTxtException
  {
    Vector linhas = transformarObjDaDeclaracaoEmRegistroTXT (objetos);
  }
  
  public String getPath ()
  {
    return fPath;
  }
  
  public void setAlterado ()
  {
    fAlterado = true;
  }
  
  public void setUpper ()
  {
    setAlterado ();
    for (int i = 0; i < vectorDeclaracao.size (); i++)
      vectorDeclaracao.set (i, UtilitariosString.retiraCaracteresEspeciais ((String) vectorDeclaracao.elementAt (i)));
  }
  
  public void setBKPno ()
  {
    fBKP = false;
  }
  
  public void salvar () throws IOException
  {
    char[] CRLF = { '\r', '\n' };
    String strCRLF = new String (CRLF);
    if (fAlterado)
      {
	try
	  {
	    File arquivo = new File (fPath);
	    if (arquivo.exists ())
	      {
		File arquivoBkp = new File (fPath.substring (0, fPath.lastIndexOf (".")) + ".BAK");
		if (arquivoBkp.exists ())
		  arquivoBkp.delete ();
		if (! fBKP)
		  arquivo.delete ();
		else if (! arquivo.renameTo (arquivoBkp))
		  throw new IOException ("Erro na c\u00f3pia do arquivo de gravacao.");
	      }
	    BufferedWriter arqDeclaracao = new BufferedWriter (new FileWriter (arquivo));
	    for (int i = 0; i < vectorDeclaracao.size (); i++)
	      arqDeclaracao.write ((String) vectorDeclaracao.elementAt (i) + strCRLF);
	    try
	      {
		arqDeclaracao.close ();
	      }
	    catch (IOException e)
	      {
		e.printStackTrace ();
		throw new IOException ("Erro ao gravar arquivo - " + fPath);
	      }
	  }
	catch (IOException e)
	  {
	    e.printStackTrace ();
	    throw new IOException ("Erro ao gravar arquivo - " + fPath);
	  }
      }
  }
  
  public void ler () throws IOException
  {
    try
      {
	BufferedReader arqDeclaracao = new BufferedReader (new FileReader (fPath));
	boolean eof = false;
	while (! eof)
	  {
	    String linha = arqDeclaracao.readLine ();
	    if (linha != null)
	      vectorDeclaracao.add (linha);
	    else
	      eof = true;
	  }
	arqDeclaracao.close ();
      }
    catch (IOException e)
      {
	throw new IOException ("Erro ao ler arquivo - " + fPath);
      }
  }
  
  public void clear ()
  {
    vectorDeclaracao.clear ();
  }
  
  public void setFicha (Vector ficha) throws GeracaoTxtException
  {
    String ultRegistro = "";
    int numRegistros = 0;
    Vector linhas = transformarObjDaDeclaracaoEmRegistroTXT (ficha);
    if (linhas.size () > 0)
      incluirLinhas (linhas);
  }
}
