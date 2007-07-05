/* DocumentoTXTDefault - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.txt;
import java.util.Vector;

import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;

public class DocumentoTXTDefault extends DocumentoTXT
{
  public DocumentoTXTDefault (String tipoArquivo, String path)
  {
    super (tipoArquivo, path);
  }
  
  protected void antesDeTransformarObjDaDeclaracaoEmRegistrosTXT (RegistroTxt pRegAtual) throws GeracaoTxtException
  {
    /* empty */
  }
  
  protected Vector transformarObjDaDeclaracaoEmRegistroTXT (Vector ficha) throws GeracaoTxtException
  {
    Vector linhas = new Vector ();
    for (int i = 0; i < ficha.size (); i++)
      {
	RegistroTxt objRegGeracao = (RegistroTxt) ficha.elementAt (i);
	antesDeTransformarObjDaDeclaracaoEmRegistrosTXT (objRegGeracao);
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
  
  protected void antesDeTransformarTXTEmObjDaDeclaracao (String pTipoArq, String pTipoReg, RegistroTxt pRegAtual) throws GeracaoTxtException
  {
    /* empty */
  }
  
  protected Vector transformarRegistroTXTEmObjDaDeclaracao (Vector linhas, String tipoArquivo, String tipoReg) throws GeracaoTxtException
  {
    Vector ficha = new Vector ();
    for (int i = 0; i < linhas.size (); i++)
      {
	RegistroTxt objRegGeracao = new RegistroTxt (tipoArquivo, tipoReg);
	objRegGeracao.setLinha ((String) linhas.elementAt (i));
	antesDeTransformarTXTEmObjDaDeclaracao (tipoArquivo, tipoReg, objRegGeracao);
	ficha.addElement (objRegGeracao);
      }
    return ficha;
  }
}
