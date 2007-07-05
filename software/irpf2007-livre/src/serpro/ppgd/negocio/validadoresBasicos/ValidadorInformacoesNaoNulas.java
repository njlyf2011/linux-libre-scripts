/* ValidadorInformacoesNaoNulas - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import java.util.List;

import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;

public class ValidadorInformacoesNaoNulas extends ValidadorDefault
{
  private List listaInformacao;
  private Informacao[] arrayInformacao;
  private String mensagem;
  
  public ValidadorInformacoesNaoNulas (byte severidade, Informacao[] arrayInformacao, String mensagem)
  {
    super (severidade);
    this.arrayInformacao = arrayInformacao;
    this.mensagem = mensagem;
  }
  
  public RetornoValidacao validarImplementado ()
  {
    String msg = "<HTML>";
    int i = 0;
    for (i = 0; i <= arrayInformacao.length - 1; i++)
      {
	Object objeto = arrayInformacao[i];
	Informacao informacao;
	if (objeto instanceof Informacao)
	  informacao = (Informacao) objeto;
	else
	  throw new IllegalArgumentException ("Lista tem elemento que n\u00e3o Informacao");
	if (! informacao.isVazio ())
	  return null;
	msg = msg.concat ("\"" + informacao.getNomeCampo () + "\"");
	if (i == 2)
	  msg += "<BR>";
	if (i < arrayInformacao.length - 1)
	  {
	    if (i < arrayInformacao.length - 2)
	      msg += ", ";
	    else
	      msg += " e ";
	  }
      }
    msg = msg.concat (" n\u00e3o preenchidos.");
    msg += "</HTML>";
    RetornoValidacao retorno = null;
    if (i > 4)
      {
	retorno = new RetornoValidacao ("Campos obrigat\u00f3rios n\u00e3o preenchidos.");
	retorno.setMensagemValidacaoExtendida (msg);
      }
    else
      retorno = new RetornoValidacao (msg);
    return retorno;
  }
}
