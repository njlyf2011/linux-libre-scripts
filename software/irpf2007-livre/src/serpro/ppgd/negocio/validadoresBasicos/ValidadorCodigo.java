/* ValidadorCodigo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import java.util.List;

import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.util.Validador;

public class ValidadorCodigo extends ValidadorDefault
{
  private String msgValidacao = "\"C\u00f3digo\" inv\u00e1lido";
  
  public ValidadorCodigo (byte severidade)
  {
    super (severidade);
  }
  
  public ValidadorCodigo (byte severidade, String msgValidacao)
  {
    this (severidade);
    this.msgValidacao = msgValidacao;
  }
  
  public RetornoValidacao validarImplementado ()
  {
    List colecao = ((Codigo) getInformacao ()).getColecaoElementoTabela ();
    return Validador.validarElementoTabela (getInformacao ().getConteudoFormatado (), colecao, msgValidacao);
  }
}
