/* ValidadorData - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.validadoresBasicos;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.util.Validador;

public class ValidadorData extends ValidadorDefault
{
  private String msgValidacao;
  private int anoLimite = Integer.parseInt (ConstantesGlobais.EXERCICIO);
  
  public ValidadorData (byte severidade)
  {
    super (severidade);
    msgValidacao = "\"Data inv\u00e1lida";
  }
  
  public ValidadorData (byte severidade, int pAnoLimite)
  {
    super (severidade);
    msgValidacao = "\"Data inv\u00e1lida";
    anoLimite = pAnoLimite;
  }
  
  public RetornoValidacao validarImplementado ()
  {
    return Validador.validarData (getInformacao ().getConteudoFormatado (), anoLimite);
  }
}
