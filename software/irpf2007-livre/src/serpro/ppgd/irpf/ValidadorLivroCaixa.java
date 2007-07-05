/* ValidadorLivroCaixa - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;

public class ValidadorLivroCaixa extends ValidadorDefault
{
  DeclaracaoIRPF declaracaoIRPF = null;
  
  public ValidadorLivroCaixa (DeclaracaoIRPF dec)
  {
    super ((byte) 3);
    declaracaoIRPF = dec;
  }
  
  public RetornoValidacao validarImplementado ()
  {
    IdentificadorDeclaracao idDeclaracao = declaracaoIRPF.getIdentificadorDeclaracao ();
    if (idDeclaracao.getTipoDeclaracao ().asString ().equals ("1"))
      return null;
    if (declaracaoIRPF.getModelo ().getTotalRendRecebidosMaisExterior ().comparacao ("<", declaracaoIRPF.getModelo ().getTotalLivroCaixa ()))
      return new RetornoValidacao (tab.msg ("rend_maior_livro_caixa"), getSeveridade ());
    return null;
  }
}
