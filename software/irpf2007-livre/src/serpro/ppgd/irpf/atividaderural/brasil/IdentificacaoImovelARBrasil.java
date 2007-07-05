/* IdentificacaoImovelARBrasil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural.brasil;
import serpro.ppgd.negocio.Colecao;

public class IdentificacaoImovelARBrasil extends Colecao
{
  
  public IdentificacaoImovelARBrasil ()
  {
    super (serpro.ppgd.irpf.atividaderural.brasil.ImovelARBrasil.class.getName ());
    setFicha ("Dados e Identifica\u00e7\u00e3o do Im\u00f3vel Rural - BRASIL");
  }
  
  public void objetoInserido (Object o)
  {
    setFicha (getFicha ());
  }
}
