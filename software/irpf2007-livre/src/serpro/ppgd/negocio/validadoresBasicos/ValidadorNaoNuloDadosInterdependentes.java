/* ValidadorNaoNuloDadosInterdependentes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import java.util.Collection;
import java.util.Iterator;

import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;

public class ValidadorNaoNuloDadosInterdependentes extends ValidadorDefault
{
  private String msg;
  private Collection colecao;
  
  public ValidadorNaoNuloDadosInterdependentes (byte severidade, String pMsg, Collection pColInfo)
  {
    super (severidade);
    msg = pMsg;
    colecao = pColInfo;
    setVerificaVazio (true);
    setSeveridade (severidade);
  }
  
  public void tiraEspacosEmBranco ()
  {
    /* empty */
  }
  
  public RetornoValidacao validarImplementado ()
  {
    tiraEspacosEmBranco ();
    if (! getInformacao ().isVazio ())
      return null;
    Iterator itDados = colecao.iterator ();
    while (itDados.hasNext ())
      {
	Informacao infoAtual = (Informacao) itDados.next ();
	if (! infoAtual.isVazio ())
	  {
	    setMensagemValidacao (msg);
	    return new RetornoValidacao (msg, getSeveridade ());
	  }
      }
    return null;
  }
}
